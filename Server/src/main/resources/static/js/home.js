// home.js

document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('token');
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('activeMenu');
    window.location.replace("login");
});

document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Mời bạn đăng nhập lại!");
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('activeMenu');
        window.location.replace("login");
        return;
    }
    try {
        const response = await fetch('/api/validate', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(`HTTP error! status: ${response.status} - ${errorMessage}`);
        }

        const result = await response.json();
        if (result.status === 1) {
            document.getElementById('accountName').innerText = `Xin chào, ${result.fullName}`;

            // Kiểm tra sessionStorage để xem menu nào đã được chọn trước đó
            const activeMenu = sessionStorage.getItem('activeMenu');
            if (activeMenu) {
                const menuItem = document.querySelector(`.menu-item[href="${activeMenu}"]`);
                if (menuItem) {
                    await activateMenuItem(menuItem); // Kích hoạt menu đã lưu
                }
            }

            // Khôi phục trạng thái mở/đóng của các submenu
            const submenus = document.querySelectorAll('.submenu');
            submenus.forEach(submenu => {
                const state = sessionStorage.getItem(submenu.id);
                submenu.style.display = (state === 'open') ? 'block' : 'none'; // Mở hoặc đóng submenu
            });
        } else {
            alert(result.message);
            localStorage.removeItem('token');
            sessionStorage.removeItem('token');
            sessionStorage.removeItem('activeMenu');
            window.location.replace("login");
        }
    } catch (error) {
        console.error('Lỗi kết nối:', error);
        alert('Đã xảy ra lỗi, vui lòng thử lại sau.');
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('activeMenu');
        window.location.replace("login");
    }
});

function toggleSubmenu(menuItem) {
    const submenu = menuItem.nextElementSibling;
    const allSubmenus = document.querySelectorAll('.submenu');

    // Đóng tất cả các submenu khác
    allSubmenus.forEach(sub => {
        if (sub !== submenu) {
            sub.style.display = 'none';
            sessionStorage.setItem(sub.id, 'closed'); // Lưu trạng thái đóng
        }
    });

    // Chuyển đổi hiển thị submenu hiện tại
    if (submenu.style.display === 'block') {
        submenu.style.display = 'none';
        sessionStorage.setItem(submenu.id, 'closed'); // Lưu trạng thái đóng
    } else {
        submenu.style.display = 'block';
        sessionStorage.setItem(submenu.id, 'open'); // Lưu trạng thái mở
    }
}

let lastActiveMenuItem = null;

async function activateMenuItem(menuItem) {
    const selectedContent = document.querySelector(menuItem.getAttribute('href'));

    // Kiểm tra xem mục đã được chọn trước đó chưa
    if (lastActiveMenuItem === menuItem) {
        return; // Không làm gì nếu mục đã được chọn
    }

    // Ẩn tất cả các phần nội dung
    const contentSections = document.querySelectorAll('.content > div');
    contentSections.forEach(section => {
        section.style.display = 'none';
    });

    // Hiện phần nội dung tương ứng với menu được chọn
    if (selectedContent) {
        selectedContent.style.display = 'block';
    }

    // Đánh dấu item menu đang hoạt động
    const menuItems = document.querySelectorAll('.menu-item');
    menuItems.forEach(item => {
        item.classList.remove('active');
    });
    menuItem.classList.add('active');

    // Lưu trạng thái vào lịch sử trình duyệt và sessionStorage
    const menuHref = menuItem.getAttribute('href');
    window.history.pushState({id: menuHref}, '', menuHref);
    sessionStorage.setItem('activeMenu', menuHref); // Lưu trạng thái vào sessionStorage
    lastActiveMenuItem = menuItem; // Cập nhật menu đang hoạt động

    if (menuHref === '#quanlytaixe') {
        await fetchDriverList(); // Gọi hàm để lấy danh sách tài xế
    }
    else if(menuHref === '#quanlyxe'){
        await fetchVehicleList();
    }
}

// Sự kiện để xử lý trở về khi người dùng sử dụng nút quay lại của trình duyệt
window.onpopstate = function(event) {
    if (event.state) {
        const sectionId = event.state.id;
        const contentSections = document.querySelectorAll('.content > div');
        contentSections.forEach(section => {
            section.style.display = section.id === sectionId.substring(1) ? 'block' : 'none';
        });

        // Đánh dấu item menu tương ứng là đang hoạt động
        const menuItems = document.querySelectorAll('.menu-item');
        menuItems.forEach(item => {
            item.classList.remove('active');
            if (item.getAttribute('href') === sectionId) {
                item.classList.add('active');
            }
        });
    }
};

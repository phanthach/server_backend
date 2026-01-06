
// Mở Modal
document.getElementById('addVehicleButton').addEventListener('click', function () {
    fetchLayoutListt();
    var myModal = new bootstrap.Modal(document.getElementById('addVehicleModal'));
    myModal.show(); // Mở modal
    var process = document.getElementById('progressBarVehicle');
    var save = document.getElementById('saveVehicleBtn');
    process.style.display = 'none';
    save.disabled = false;
});

// Đóng Modal khi nhấn nút đóng
document.getElementById('.btn-close').addEventListener('click', function () {
    var myModal = new bootstrap.Modal(document.getElementById('addVehicleModal'));
    myModal.hide(); // Đóng modal
});
// Lấy nút Đóng
document.getElementById('btnClose').addEventListener('click', function () {
    // Tìm modal
    var myModal = new bootstrap.Modal(document.getElementById('addVehicleModal'));
    myModal.hide(); // Đóng modal
});

async function fetchLayoutListt(page = 0, size = 5) {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Phiên đăng nhập hết hạn Vehicle");
        window.location.replace("login");
        return;
    }
    try {
        const response = await fetch(`/api/alllayouts?page=${page}&size=${size}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        if (!response.ok) {
            const errorResponse = await response.json();
            alert(`Lỗi: ${errorResponse.message}`);
            return;
        }
        const data = await response.json();
        displayLayoutss(data.content); // Hiển thị danh sách sơ đồ ghế
        setupPaginationn(data.totalPages, page); // Thiết lập phân trang
    } catch (error) {
        console.error('Lỗi kết nối:', error);
        alert('Không thể tải danh sách sơ đồ ghế: ' + error);
    }
}

function setupPaginationn(totalPages, currentPage) {
    const paginationContainer = document.getElementById('paginationLayoutVehicle');
    paginationContainer.innerHTML = ''; // Xóa nội dung phân trang hiện tại

    const addPageButton = (page) => {
        const button = document.createElement('button');
        button.innerText = page + 1; // Thay đổi số trang
        button.className = 'btn btn-light me-1';
        button.disabled = (page === currentPage); // Vô hiệu hóa nút trang hiện tại
        button.onclick = () => fetchLayoutList(page); // Gọi lại hàm fetchLayoutList với trang đã chọn
        paginationContainer.appendChild(button);
    };

    if (totalPages <= 5) {
        for (let i = 0; i < totalPages; i++) {
            addPageButton(i);
        }
    } else {
        if (currentPage < 3) {
            for (let i = 0; i < 5; i++) {
                addPageButton(i);
            }
            paginationContainer.appendChild(document.createTextNode('...'));
            addPageButton(totalPages - 1); // Hiển thị trang cuối
        } else if (currentPage > totalPages - 5) {
            addPageButton(0); // Hiển thị trang đầu
            paginationContainer.appendChild(document.createTextNode('...'));
            for (let i = totalPages - 5; i < totalPages; i++) {
                addPageButton(i);
            }
        } else {
            addPageButton(0); // Hiển thị trang đầu
            paginationContainer.appendChild(document.createTextNode('...'));

            for (let i = currentPage - 1; i <= currentPage + 3; i++) {
                addPageButton(i);
            }

            paginationContainer.appendChild(document.createTextNode('...'));
            addPageButton(totalPages - 1); // Hiển thị trang cuối
        }
    }
}

function displayLayoutss(layouts) {
    const layoutTable = document.getElementById('layoutTableVehicle');
    layoutTable.innerHTML = ''; // Xóa nội dung hiện tại trong bảng
    layouts.forEach(layout => {
        const row = document.createElement('tr');
        row.dataset.layoutId = layout.layoutId;
        row.innerHTML = `
            <td><input type="radio" name="selectedLayout" value="${layout.layoutId}"></td>
            <td>${layout.nameLayout}</td>
            <td>${layout.seatCapacity}</td>
            <td>${layout.x}</td>
            <td>${layout.y}</td>
        `;
        layoutTable.appendChild(row); // Thêm hàng vào bảng
    });
}

document.getElementById("layoutTableVehicleContainer").addEventListener("change", function(event) {
    if (event.target.name === "selectedLayout") {
        const layoutId = event.target.value; // Lấy layoutId từ radio đã chọn
        sessionStorage.setItem('selectedLayout', layoutId); // Lưu layoutId nếu cần
    }
});

// Thêm sự kiện cho nút "Lưu Xe"
async function addVehicle(event) {
    event.preventDefault(); // Ngăn chặn hành động gửi form mặc định
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Phiên đăng nhập hết hạn");
        window.location.replace("login");
        return;
    }
    const plateNumber = document.getElementById('plateNumber').value;
    const vehicleType = document.getElementById('vehicleType').value;
    const imgFile = document.getElementById('img').files[0];
    const selectedLayout = document.querySelector('input[name="selectedLayout"]:checked');
    let layoutId = null;
    var process = document.getElementById('progressBarVehicle');
    var save = document.getElementById('saveVehicleBtn');
    if (selectedLayout) {
        layoutId = selectedLayout.value;
    }

    if (imgFile && plateNumber && vehicleType && layoutId) {
        const imgData = new FormData();
        imgData.append('file', imgFile);
        save.disabled = true;
        process.style.display = 'block';
        try {

            const response = await fetch(`/api/cloudinary/upload`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                body: imgData
            });
            if (!response.ok) {
                throw new Error('Đã có lỗi xảy ra, vui lòng đăng nhập lại');
            }
            const data = await response.json();
            const imgUrl = data.url;

            const vehicle = {
                plateNumber: plateNumber,
                vehicleType: vehicleType,
                layoutId: layoutId,
                img: imgUrl
            };

            const vehicleResponse = await fetch('/api/addvehicles', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(vehicle)
            });
            if (!vehicleResponse.ok) {
                alert("Đã có lỗi xảy ra, vui lòng thử lại sau");
                window.location.replace('home');
            }
            else{
                alert('Thêm xe thành công');
                window.location.replace('home');
            }
        } catch (error) {
            console.error('Lỗi:', error);
            alert('Đã xảy ra lỗi, vui lòng thử lại sau');
            window.location.replace('home');
        }
    } else {
        alert('Vui lòng điền đủ thông tin');
    }
}

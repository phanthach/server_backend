async function fetchDriverList(page = 0, size = 10) {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Mời bạn đăng nhập lại!");
        window.location.replace("login");
        return;
    }
    try {
        const response = await fetch(`/api/drivers?page=${page}&size=${size}`, {
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
        displayDrivers(data.content); // Hiển thị danh sách tài xế
        setupPagination1(data.totalPages, page); // Thiết lập phân trang
    } catch (error) {
        console.error('Lỗi kết nối:', error);
        alert('Không thể tải danh sách tài xế.'+ error);
    }
}
function setupPagination1(totalPages, currentPage) {
    const paginationContainer = document.getElementById('paginationDriver');
    paginationContainer.innerHTML = ''; // Xóa nội dung phân trang hiện tại

    // Hàm để thêm nút trang vào container
    const addPageButton = (page) => {
        const button = document.createElement('button');
        button.innerText = page + 1; // Thay đổi số trang
        button.className = 'btn btn-light me-1';
        button.disabled = (page === currentPage); // Vô hiệu hóa nút trang hiện tại
        button.onclick = () => fetchDriverList(page); // Gọi lại hàm fetchLayoutList với trang đã chọn
        paginationContainer.appendChild(button);
    };

    // Hiển thị các nút trang
    if (totalPages <= 5) {
        // Nếu tổng số trang ít hơn hoặc bằng 5, hiển thị tất cả
        for (let i = 0; i < totalPages; i++) {
            addPageButton(i);
        }
    } else {
        // Nếu tổng số trang lớn hơn 5
        if (currentPage < 3) {
            // Nếu trang hiện tại là trang đầu tiên
            for (let i = 0; i < 5; i++) {
                addPageButton(i);
            }
            paginationContainer.appendChild(document.createTextNode('...'));
            addPageButton(totalPages - 1); // Hiển thị trang cuối
        } else if (currentPage > totalPages - 5) {
            // Nếu trang hiện tại là trang cuối
            addPageButton(0); // Hiển thị trang đầu
            paginationContainer.appendChild(document.createTextNode('...'));
            for (let i = totalPages - 5; i < totalPages; i++) {
                addPageButton(i);
            }
        } else {
            // Nếu trang hiện tại ở giữa
            addPageButton(0); // Hiển thị trang đầu
            paginationContainer.appendChild(document.createTextNode('...'));

            // Hiển thị các trang gần với trang hiện tại
            for (let i = currentPage - 1; i <= currentPage + 3; i++) {
                addPageButton(i);
            }

            paginationContainer.appendChild(document.createTextNode('...'));
            addPageButton(totalPages - 1); // Hiển thị trang cuối
        }
    }
}
function displayDrivers(drivers) {
    const driverTable = document.getElementById('driverTable');
    driverTable.innerHTML = ''; // Xóa nội dung hiện tại trong bảng

    // Duyệt qua danh sách tài xế và thêm vào bảng
    drivers.forEach(driver => {
        const row = document.createElement('tr');
        const actionButton = driver.isBlocked === 1
            ? `<button class="btn btn-success btn-sm" onclick="approveDriver(this)">Duyệt</button>
                <button class="btn btn-warning btn-sm" disabled="BlockDriver(this)">Tạm khóa</button> `
            : `<button class="btn btn-secondary btn-sm" disabled>Đang hoạt động</button>
                <button class="btn btn-warning btn-sm" onclick="BlockDriver(this)">Khóa</button>`;

        // Thêm nút "Xóa" cho tất cả tài xế
        const deleteButton = `<button class="btn btn-danger btn-sm" onclick="deleteDriver(this)">Xóa</button>`;
        row.innerHTML = `
            <td>${driver.fullname}</td>
            <td>${driver.birthDay}</td>
            <td>${driver.address}</td>
            <td>${driver.phoneNumber}</td>
            <td>${driver.createdAt}</td>
            <td>${driver.licenseNumber}</td>
            <td>
                ${actionButton}
                ${deleteButton}
               
            </td>
        `;
        driverTable.appendChild(row); // Thêm hàng vào bảng
    });
}

// Hàm tìm kiếm tài xế theo tên
document.getElementById('searchDriver').addEventListener('input', function() {
    const searchText = this.value.toLowerCase();
    const rows = document.querySelectorAll('#driverTable tr');

    rows.forEach(row => {
        const name = row.cells[0].textContent.toLowerCase();
        if (name.includes(searchText)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
});

// Hàm duyệt tài xế
function approveDriver(button) {
    if(!confirm('Bạn có chắc chắn muốn duyệt tài xế này?')) {
        return;
    }
    const row = button.closest('tr');
    const phoneNumber = row.querySelector('td:nth-child(4)').textContent;
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Mời bạn đăng nhập lại!");
        window.location.replace("login");
        return;
    }
    fetch(`api/drivers/${phoneNumber}/block?isBlocked=0`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error =>{
                    throw new Error(error.message || 'Có lỗi xảy ra');
                });
            }
            else{
                alert('Đã duyệt tài xế');
                button.innerText = 'Đang hoạt động';
                button.disabled = true;
                button.classList.remove('btn-success');
                button.classList.add('btn-secondary');
                const approveButton = row.querySelector('.btn-warning'); // Tìm nút "Duyệt"
                if (approveButton) {
                    approveButton.disabled = false;
                    approveButton.innerText = 'Khóa';
                    approveButton.onclick = function() { BlockDriver(this); };
                }
            }
        }).catch(error => {
        console.error('Lỗi: ', error);
        alert(`Đã xảy ra lỗi vui lòng thử lại sau`)
    })
}

// Hàm xóa tài xế
function deleteDriver(button) {
    if (!confirm('Bạn có chắc chắn muốn xóa tài xế này?')) {
        return;
    }
    const row = button.closest('tr');
    const phoneNumber = row.querySelector('td:nth-child(4)').textContent;
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Mời bạn đăng nhập lại!");
        window.location.replace("login");
        return;
    }
    fetch(`api/drivers/${phoneNumber}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            return response.json().then(error =>{
                throw new Error(error.message || 'Có lỗi xảy ra');
            });
        }
        else{
            row.remove();
            alert('Xóa tài xế thành công');
        }
    }).catch(error => {
        console.error('Lỗi: ', error);
        alert(`Đã xảy ra lỗi vui lòng thử lại sau`)
    })
}
// Hàm khóa tài x tạm thời.
function BlockDriver(button) {
    if (!confirm('Bạn có chắc chắn muốn khóa tài xế này?')) {
        return;
    }
    const row = button.closest('tr');
    const phoneNumber = row.querySelector('td:nth-child(4)').textContent;
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Mời bạn đăng nhập lại!");
        window.location.replace("login");
        return;
    }
    fetch(`api/drivers/${phoneNumber}/block?isBlocked=1`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            return response.json().then(error => {
                throw new Error(error.message || 'Có lỗi xảy ra');
            });
        } else {
            alert('Đã khóa tài xế');
            button.innerText = 'Tạm khóa';
            button.disabled = true;
            const approveButtona = row.querySelector('.btn-secondary'); // Tìm nút "Duyệt"
            if (approveButtona) {
                approveButtona.disabled = false; // Bật lại nút "Duyệt"
                approveButtona.innerText = 'Duyệt';
                approveButtona.classList.remove('btn-secondary'); // Nếu bạn có lớp secondary trên nút Duyệt
                approveButtona.classList.add('btn-success'); // Đặt lại lớp
                approveButtona.onclick = function() { approveDriver(this); };
            }
        }
    }).catch(error => {
        console.error('Lỗi: ', error);
        alert(`Đã xảy ra lỗi vui lòng thử lại sau`)
    })
}

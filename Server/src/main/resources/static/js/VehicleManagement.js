async function fetchVehicleList(page = 0, size = 5) {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Mời bạn đăng nhập lại!");
        window.location.replace("login");
        return
    }
    try{
        const response = await fetch(`/api/listVehicle?page=${page}&size=${size}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
    });
    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Lỗi: ${response.status} - ${errorMessage}`);
    }
    const data = await response.json();
            displayVehicles(data.content); // Hiển thị danh sách tài xế
            setupPaginationVehicle(data.totalPages, page); // Thiết lập phân trang

    }catch (error) {
        console.error('Lỗi kết nối:', error);
        alert('Không thể tải danh sách xe.'+ error);
    }
}
function setupPaginationVehicle(totalPages, currentPage) {
    const paginationContainer = document.getElementById('paginationVehicle');
    paginationContainer.innerHTML = ''; // Xóa nội dung phân trang hiện tại

    for (let i = 0; i < totalPages; i++) {
        const button = document.createElement('button');
        button.innerText = i + 1;
        button.className = 'btn btn-light me-1';
        button.disabled = (i === currentPage); // Vô hiệu hóa nút trang hiện tại

        button.onclick = () => fetchVehicleList(i); // Gọi lại hàm fetchDriverList với trang đã chọn

        paginationContainer.appendChild(button);
    }
}
function displayVehicles(vehecles) {
    const vehicleTable = document.getElementById('vehicleTable');
    vehicleTable.innerHTML = ''; // Xóa nội dung hiện tại trong bảng

    // Duyệt qua danh sách tài xế và thêm vào bảng
    vehecles.forEach(vehicle => {
        const row = document.createElement('tr');
        row.dataset.vehicleId = vehicle.vehicleId;
        const actionButton = vehicle.status === 1
            ? `<button class="btn btn-success btn-sm" onclick="approveVehicle(this)">Duyệt</button>
                <button class="btn btn-warning btn-sm" disabled>Tạm khóa</button> `
            : `<button class="btn btn-secondary btn-sm" disabled>Đang hoạt động</button>
                <button class="btn btn-warning btn-sm" onclick="BlockVehicle(this)">Khóa</button>`;

        // Thêm nút "Xóa" cho tất cả tài xế
        const deleteButton = `<button class="btn btn-danger btn-sm" onclick="deleteVehicle(this)">Xóa</button>`;
        row.innerHTML = `
            <td>${vehicle.plateNumber}</td>
            <td>${vehicle.vehicleType}</td>
            <td>${vehicle.seatCapacity}</td>
            <td>${vehicle.nameLayout}</td>
            <td><img src=${vehicle.img} alt="Vehicle Image" style="width: 100px; height: auto;"></td>
            <td>
                ${actionButton}
                ${deleteButton}
            </td>
        `;
        vehicleTable.appendChild(row); // Thêm hàng vào bảng
    });
}
function approveVehicle(button) {
    if(!confirm('Bạn có chắc chắn muốn tiếp tục sử dụng xe?')) {
        return;
    }
    const row = button.closest('tr');
    const vehicleId = row.dataset.vehicleId ;
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Mời bạn đăng nhập lại!");
        window.location.replace("login");
        return;
    }
    fetch(`api/vehicles/${vehicleId}/block?status=0`, {
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
                alert('Đã duyệt xe');
                button.innerText = 'Đang hoạt động';
                button.disabled = true;
                button.classList.remove('btn-success');
                button.classList.add('btn-secondary');
                const approveButton = row.querySelector('.btn-warning'); // Tìm nút "Duyệt"
                if (approveButton) {
                    approveButton.disabled = false;
                    approveButton.innerText = 'Khóa';
                    approveButton.onclick = function() { BlockVehicle(this); };
                }
            }
        }).catch(error => {
        console.error('Lỗi: ', error);
        alert(`Đã xảy ra lỗi vui lòng thử lại sau`)
    })
}

// Hàm xóa tài xế
// function deleteDriver(button) {
//     if (!confirm('Bạn có chắc chắn muốn xóa tài xế này?')) {
//         return;
//     }
//     const row = button.closest('tr');
//     const phoneNumber = row.querySelector('td:nth-child(4)').textContent;
//     const token = localStorage.getItem('token') || sessionStorage.getItem('token');
//     if (!token) {
//         alert("Mời bạn đăng nhập lại!");
//         window.location.replace("login");
//         return;
//     }
//     fetch(`api/drivers/${phoneNumber}`, {
//         method: 'DELETE',
//         headers: {
//             'Authorization': `Bearer ${token}`,
//             'Content-Type': 'application/json'
//         }
//     }).then(response => {
//         if (!response.ok) {
//             return response.json().then(error =>{
//                 throw new Error(error.message || 'Có lỗi xảy ra');
//             });
//         }
//         else{
//             row.remove();
//             alert('Xóa tài xế thành công');
//         }
//     }).catch(error => {
//         console.error('Lỗi: ', error);
//         alert(`Đã xảy ra lỗi vui lòng thử lại sau`)
//     })
// }
// // Hàm khóa tài x tạm thời.
function BlockVehicle(button) {
    if(!confirm('Bạn có chắc chắn muốn khóa xe này?')) {
        return;
    }
    const row = button.closest('tr');
    const vehicleId = row.dataset.vehicleId ;
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Mời bạn đăng nhập lại!");
        window.location.replace("login");
        return;
    }
    fetch(`api/vehicles/${vehicleId}/block?status=1`, {
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
                alert('Đã khóa xe');
                button.innerText = 'Tạm khóa';
                button.disabled = true;
                button.classList.remove('btn-warning');
                button.classList.add('btn-warning');
                const approveButtonn = row.querySelector('.btn-secondary'); // Tìm nút "Duyệt"
                if (approveButtonn) {
                    approveButtonn.disabled = false; // Bật lại nút "Duyệt"
                    approveButtonn.innerText = 'Duyệt';
                    approveButtonn.classList.remove('btn-secondary'); // Nếu bạn có lớp secondary trên nút Duyệt
                    approveButtonn.classList.add('btn-success'); // Đặt lại lớp
                    approveButtonn.onclick = function() { approveVehicle(this); };
                }
            }
        }).catch(error => {
        console.error('Lỗi: ', error);
        alert(`Đã xảy ra lỗi vui lòng thử lại sau`)
    })
}
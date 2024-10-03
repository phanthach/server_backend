async function fetchVehicleList(page = 0, size = 5) {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Mời bạn đăng nhập lại!");
        window.location.replace("login");
        return
    }
    try{
        const response = await fetch(`/api/vehicles?page=${page}&size=${size}`, {
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
        if (data && data._embedded && Array.isArray(data._embedded.vehicles)) {
            displayVehicles(data._embedded.vehicles); // Hiển thị danh sách tài xế
            setupPagination(data.page.totalPages, page); // Thiết lập phân trang
        } else {
            alert('No vehicles found.');
        }
    }catch (error) {
        console.error('Lỗi kết nối:', error);
        alert('Không thể tải danh sách xe.'+ error);
    }
}
function setupPagination(totalPages, currentPage) {
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
        const actionButton = vehicle.status === 1
            ? `<button class="btn btn-success btn-sm" onclick="approveVehicle(this)">Duyệt</button>
                <button class="btn btn-warning btn-sm" disabled>Tạm khóa</button> `
            : `<button class="btn btn-secondary btn-sm" disabled>Đang hoạt động</button>
                <button class="btn btn-warning btn-sm" onclick="BlockVehicle(this)">Khóa</button>`;

        // Thêm nút "Xóa" cho tất cả tài xế
        const deleteButton = `<button class="btn btn-danger btn-sm" onclick="deleteVehicle(this)">Xóa</button>`;
        row.innerHTML = `
            <td>${vehicle.vehicleId}</td>
            <td>${vehicle.vehicleType}</td>
            <td>${vehicle.seatCapacity}</td>
            <td><img src="/img/img_tho_lam.jpg" alt="Vehicle Image" style="width: 100px; height: auto;"></td>
            <td>
                ${actionButton}
                ${deleteButton}
            </td>
        `;
        vehicleTable.appendChild(row); // Thêm hàng vào bảng
    });
}
function approveVehicle(button) {
    if(!confirm('Bạn có chắc chắn muốn tiếp tc sử dụng xe?')) {
        return;
    }
    const row = button.closest('tr');
    const vehicleId = row.dataset.vehicleId;
    alert(vehicleId);
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
                    approveButton.onclick = function() { BlockDriver(this); };
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
// function BlockDriver(button) {
//     if (!confirm('Bạn có chắc chắn muốn khóa tài xế này?')) {
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
//     fetch(`api/drivers/${phoneNumber}/block?isBlocked=1`, {
//         method: 'PUT',
//         headers: {
//             'Authorization': `Bearer ${token}`,
//             'Content-Type': 'application/json'
//         }
//     }).then(response => {
//         if (!response.ok) {
//             return response.json().then(error => {
//                 throw new Error(error.message || 'Có lỗi xảy ra');
//             });
//         } else {
//             alert('Đã khóa tài xế');
//             button.innerText = 'Tạm khóa';
//             button.disabled = true;
//             const approveButtona = row.querySelector('.btn-secondary'); // Tìm nút "Duyệt"
//             if (approveButtona) {
//                 approveButtona.disabled = false; // Bật lại nút "Duyệt"
//                 approveButtona.innerText = 'Duyệt';
//                 approveButtona.classList.remove('btn-secondary'); // Nếu bạn có lớp secondary trên nút Duyệt
//                 approveButtona.classList.add('btn-success'); // Đặt lại lớp
//                 approveButtona.onclick = function() { approveDriver(this); };
//             }
//         }
//     }).catch(error => {
//         console.error('Lỗi: ', error);
//         alert(`Đã xảy ra lỗi vui lòng thử lại sau`)
//     })
// }

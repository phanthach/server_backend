async function fetchLayoutList(page = 0, size = 10) {
    sessionStorage.removeItem('layoutId')
    sessionStorage.removeItem('seatColumns')
    sessionStorage.removeItem('seatRows')
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Phiên đăng nhập hết hạn layout");
        window.location.replace("login");
        return;
    }
    try {
        const response = await fetch(`/api/layouts?page=${page}&size=${size}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            const errorResponse = await response.json();
            alert(`Lỗi: ${errorResponse.message}`);
            return;
        }
        const data = await response.json();
        displayLayouts(data.content); // Hiển thị danh sách sơ đồ ghế
        setupPagination(data.totalPages, page); // Thiết lập phân trang
    }catch (error) {
        console.error('Lỗi kết nối:', error);
        alert('Không thể tải danh sách sơ đồ ghế.'+ error);
    }
}
// setup phan trang
function setupPagination(totalPages, currentPage) {
    const paginationContainer = document.getElementById('paginationLayout');
    paginationContainer.innerHTML = ''; // Xóa nội dung phân trang hiện tại

    // Hàm để thêm nút trang vào container
    const addPageButton = (page) => {
        const button = document.createElement('button');
        button.innerText = page + 1; // Thay đổi số trang
        button.className = 'btn btn-light me-1';
        button.disabled = (page === currentPage); // Vô hiệu hóa nút trang hiện tại
        button.onclick = () => fetchLayoutList(page); // Gọi lại hàm fetchLayoutList với trang đã chọn
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

function displayLayouts(layouts) {
    const layoutTable = document.getElementById('layoutTable');
    layoutTable.innerHTML = ''; // Xóa nội dung hiện tại trong bảng

    // Duyệt qua danh sách tài xế và thêm vào bảng
    layouts.forEach(layout => {
        const row = document.createElement('tr');
        row.dataset.layoutId = layout.layoutId;
        const actionButton = layout.status === 0
            ? `<button class="btn btn-success btn-sm" onclick>Hoạt động</button>`
            : `<button class="btn btn-warning btn-sm" disabled>Tạm khóa</button>`;

        const deleteButton = `<button class="btn btn-danger btn-sm" onclick="deleteLayout(this)">Xóa</button>`;
        const editSeat = `<button class="btn btn-primary btn-sm" onclick="editSeat(this)">Sửa</button>`;
        const reviewLayout = `<button class="btn btn-secondary btn-sm" onclick="reviewLayout(this)">Chi tiết</button>`;
        row.innerHTML = `
            <td>${layout.nameLayout}</td>
            <td>${layout.seatCapacity}</td>
            <td>${layout.x}</td>
            <td>${layout.y}</td>
            <td>${layout.floor}</td>
            <td>
                ${editSeat}
                ${reviewLayout}
            </td>
            <td>
                ${actionButton}
                ${deleteButton}
            </td>
        `;
        layoutTable.appendChild(row); // Thêm hàng vào bảng
    });
}
async function addLayout(){
    const token = localStorage.getItem('token')||sessionStorage.getItem('token')
    if(!token){
        alert("Phiên đăng nhập hết hạn ls3")
        window.location.replace('login')
    }
    const layoutName = document.getElementById('nameLayout').value
    const seatCapacity = document.getElementById('seatCapacity').value
    const xCoordinate = document.getElementById('xCoordinate').value
    const yCoordinate = document.getElementById('yCoordinate').value
    const floor = document.getElementById('floor').value
    const layout = {
        nameLayout: layoutName,
        seatCapacity: seatCapacity,
        x: xCoordinate,
        y: yCoordinate,
        status:1,
        floor: floor
    }
    sessionStorage.setItem('layout', JSON.stringify(layout))
    try {
        const response = await fetch('/api/addlayout', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(layout),
        });
        if (!response.ok) {
            const errorMessage = await response.text()
            throw new Error(`Lỗi: ${errorMessage}`)
        }
        const result = await response.json()
        sessionStorage.setItem('layoutIdAdd', result.layoutId)
        sessionStorage.setItem('seatColumnsAdd', result.x)
        sessionStorage.setItem('seatRowsAdd', result.y)
        sessionStorage.setItem('floorAdd', result.floor)
        window.location.replace('addseat')
    }catch (error) {
        console.error('Lỗi:', error)
        alert('Có lỗi xảy ra khi thêm sơ đồ ghế'+ error)
    }
}
function deleteLayout(button){
    const token = localStorage.getItem('token')||sessionStorage.getItem('token')
    if(!token){
        alert("Phiên đăng nhập hết hạn")
        window.location.replace('login')
    }
    if(!confirm('Bạn có chắc chắn muốn xóa sơ đồ ghế này?')){
        return
    }
    const row = button.closest('tr')
    const layoutId = row.dataset.layoutId
    fetch(`/api/deletelayout/${layoutId}`, {
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
            alert('Xóa sơ đồ ghế thành công');
        }
    }).catch(error => {
        console.error('Lỗi: ', error);
        alert(`Đã xảy ra lỗi vui lòng thử lại sau`)
    })
}
function reviewLayout(button){
    const row = button.closest('tr')
    const layoutId = row.dataset.layoutId
    sessionStorage.setItem('layoutId', layoutId)
    window.location.replace('layoutdetail')
}
function editSeat(button){
    const row = button.closest('tr')
    const layoutId = row.dataset.layoutId
    sessionStorage.setItem('layoutIdEdit', layoutId)
    sessionStorage.setItem("seatRowsEdit", row.cells[3].innerText)
    sessionStorage.setItem("seatColumnsEdit", row.cells[2].innerText)
    sessionStorage.setItem("floorEdit", row.cells[4].innerText)
    window.location.replace('editseat')
}
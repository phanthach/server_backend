async function fetchRouteList(page = 0, size = 10) {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Phiên đăng nhập hết hạn Route");
        window.location.replace("login");
        return;
    }
    try {
        const response = await fetch(`/api/routes?page=${page}&size=${size}`, {
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
        displayRoutes(data.content); // Hiển thị danh sách sơ đồ ghế
        setupPagination(data.totalPages, page); // Thiết lập phân trang
    }catch (error) {
        console.error('Lỗi kết nối:', error);
        alert('Không thể tải danh sách sơ đồ ghế.'+ error);
    }
}
// setup phan trang
function setupPagination(totalPages, currentPage) {
    const paginationContainer = document.getElementById('paginationRoute');
    paginationContainer.innerHTML = ''; // Xóa nội dung phân trang hiện tại

    // Hàm để thêm nút trang vào container
    const addPageButton = (page) => {
        const button = document.createElement('button');
        button.innerText = page + 1; // Thay đổi số trang
        button.className = 'btn btn-light me-1';
        button.disabled = (page === currentPage); // Vô hiệu hóa nút trang hiện tại
        button.onclick = () => fetchRouteList(page); // Gọi lại hàm fetchLayoutList với trang đã chọn
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

function displayRoutes(routes) {
    const routeTable = document.getElementById('routeTable');
    routeTable.innerHTML = ''; // Xóa nội dung hiện tại trong bảng

    // Duyệt qua danh sách tài xế và thêm vào bảng
    routes.forEach(route => {
        const row = document.createElement('tr');
        row.dataset.routeId = route.routeId;
        const deleteButton = `<button class="btn btn-danger btn-sm" onclick="deleteRoute(this)">Xóa</button>`;
        const editRoute = `<button class="btn btn-primary btn-sm" onclick="editRoute(this)">Sửa</button>`;
        const reviewRoute = `<button class="btn btn-secondary btn-sm" onclick="reviewRoute(this)">Chi tiết</button>`;
        row.innerHTML = `
            <td>${route.startLocation}</td>
            <td>${route.endLocation}</td>
            <td>
                ${editRoute}
                ${reviewRoute}
            </td>
            <td>
                ${deleteButton}
            </td>
        `;
        routeTable.appendChild(row); // Thêm hàng vào bảng
    });
}
async function addRoute(){
    const token = localStorage.getItem('token')||sessionStorage.getItem('token')
    if(!token){
        alert("Phiên đăng nhập hết hạn route")
        window.location.replace('login')
    }
    const startLocation = document.getElementById('nameStartLocation').value
    const endLocation = document.getElementById('nameEndLocation').value
    const route = {
        startLocation: startLocation,
        endLocation: endLocation
    }
    sessionStorage.setItem('route', JSON.stringify(route))
    try {
        const response = await fetch('/api/addroute', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(route),
        });
        if (!response.ok) {
            const errorMessage = await response.text()
            throw new Error(`Lỗi: ${errorMessage}`)
        }
        const result = await response.json()
        sessionStorage.setItem('routeIdAdd', result.routeId)
        alert('Thêm tuyến đường thành công')
        window.location.replace('addlocation')
    }catch (error) {
        console.error('Lỗi:', error)
        alert('Có lỗi xảy ra khi thêm sơ đồ ghế'+ error)
    }
}
async function deleteRoute(button){
    const token = localStorage.getItem('token')||sessionStorage.getItem('token')
    if(!token){
        alert("Phiên đăng nhập hết hạn r3")
        window.location.replace('login')
    }
    if(!confirm('Bạn có chắc chắn muốn xóa vị trí này?')){
        return
    }
    const row = button.closest('tr')
    const routeId = row.dataset.routeId
    try{
        const respon = await fetch(`/api/deleteroute/${routeId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        if(!respon.ok){
            const errorMessage = await respon.text()
            throw new Error(`Lỗi: ${errorMessage}`)
        }
        else{
            row.remove();
            alert('Xóa sơ đồ ghế thành công');
        }
    }catch(error){
        console.error('Lỗi:', error)
        alert('Có lỗi xảy ra khi xóa sơ đồ ghế')
    }
}
function reviewRoute(button){
    const row = button.closest('tr')
    const routeId = row.dataset.routeId
    sessionStorage.setItem('routeId', routeId)
    window.location.replace('routedetail')
}
function editRoute(button){
    const row = button.closest('tr')
    const routeIdEdit = row.dataset.routeId
    sessionStorage.setItem('routeIdAdd', routeIdEdit)
    window.location.replace('editroute')
}
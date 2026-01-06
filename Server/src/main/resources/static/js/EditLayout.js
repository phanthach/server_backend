document.addEventListener('DOMContentLoaded', async function() {
    await loadLayout();
});
function cancelEdit() {
    alert("Hủy chỉnh sửa sơ đồ ghế thành công");
    window.location.replace("home"); // Quay lại trang trước đó
}
async function loadLayout(){
    const token = localStorage.getItem('token')||sessionStorage.getItem('token')
    const layoutId = sessionStorage.getItem('layoutIdEdit')
    if(!token){
        alert("Phiên đăng nhập hết hạn EL")
        window.location.replace('login')
    }
    try {
        const response = await fetch(`/api/getLayout/${layoutId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });
        if (!response.ok) {
            const errorMessage = await response.text()
            throw new Error(`Lỗi: ${errorMessage}`)
        }
        const layout = await response.json()
        document.getElementById('nameLayout').value = layout.nameLayout;
        document.getElementById('seatCapacity').value = layout.seatCapacity;
        document.getElementById('xCoordinate').value = layout.x;
        document.getElementById('yCoordinate').value = layout.y;
    }catch (error) {
        console.error('Lỗi:', error)
        alert('Có lỗi xảy ra khi thêm sơ đồ ghế'+ error)
    }
}
async function editLayout(){
    const token = localStorage.getItem('token')||sessionStorage.getItem('token')
    if(!token){
        alert("Phiên đăng nhập hết hạn EL 2")
        window.location.replace('login')
    }
    const layoutId = sessionStorage.getItem('layoutIdEdit')
    const layoutName = document.getElementById('nameLayout').value
    const seatCapacity = document.getElementById('seatCapacity').value
    const xCoordinate = document.getElementById('xCoordinate').value
    const yCoordinate = document.getElementById('yCoordinate').value
    const layout = {
        nameLayout: layoutName,
        seatCapacity: seatCapacity,
        x: xCoordinate,
        y: yCoordinate,
        status:1
    }
    try {
        const response = await fetch(`/api/editlayout/${layoutId}`, {
            method: 'PUT',
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
        alert('Chỉnh sửa sơ đồ ghế thành công')
        window.location.replace('home')
    }catch (error) {
        console.error('Lỗi:', error)
        alert('Có lỗi xảy ra khi chỉnh sửa sơ đồ ghế'+ error)
    }
}
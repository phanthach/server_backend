document.addEventListener('DOMContentLoaded', function () {
    // Khởi tạo flatpickr với định dạng ngày dd/mm/yyyy
    flatpickr("#arrival_date", {
        dateFormat: "d/m/Y"
    });
    flatpickr("#arrival_time", {
        enableTime: true,
        noCalendar: true,
        dateFormat: "H:i", // Định dạng 24h (Giờ:Phút)
        time_24hr: true // Bật chế độ 24h
    });
});
document.getElementById("addLocationBtn").addEventListener("click", addLocation);
async function addLocation() {
    event.preventDefault();
    const token = localStorage.getItem("token")||sessionStorage.getItem("token");
    if(!token){
        alert("Phiên đăng nhập hết hạn vui lòng đăng nhập lại");
        window.location.replace("login");
        return;
    }
    // Lấy dữ liệu từ form
    const routeId = sessionStorage.getItem("routeIdAdd");
    const nameLocation = document.getElementById("nameLocation").value;
    const longitude = document.getElementById("longitude").value
    const latitude = document.getElementById("latitude").value
    const locationType = document.getElementById("locationType").value;
    const arrivalDate = document.getElementById("arrival_date").value;
    const arrivalTime = document.getElementById("arrival_time").value;
    const Location = {
        routeId,
        nameLocation,
        longitude,
        latitude,
        locationType,
        arrivalDate,
        arrivalTime
    }
    try {
        const response = await fetch("/api/addlocation", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                'Authorization': `Bearer ${token}`
            }, body: JSON.stringify(Location)
        });
            if(response.ok){
                alert("Thêm địa điểm thành công");
            }
            else {
                alert("Lỗi" + response.status + " " + response.statusText);
            }
    }catch (error) {
        console.error("Error:", error);
        alert("Có lỗi xảy ra khi thêm địa điểm. Vui lòng thử lại");
    }
}
// Giả sử bạn có routeId và bạn cần tải dữ liệu đón và trả khách
const routeId = sessionStorage.getItem("routeIdAdd"); // Thay đổi theo giá trị thực tế
// Hàm gọi API để lấy danh sách đón khách và trả khách
async function loadLocations() {
    try {
        const token = localStorage.getItem("token") || sessionStorage.getItem("token");
        if (!token) {
            alert("Phiên đăng nhập hết hạn vui lòng đăng nhập lại");
            window.location.replace("login");
            return;
        }

        // Gọi API để lấy danh sách đón khách
        const pickUpResponse = await fetch(`/api/locations?routeId=${routeId}&locationType=1`, {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        // Gọi API để lấy danh sách trả khách
        const dropOffResponse = await fetch(`/api/locations?routeId=${routeId}&locationType=2`, {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!pickUpResponse.ok || !dropOffResponse.ok) {
            throw new Error("Có lỗi xảy ra khi lấy dữ liệu từ API");
        }
        const pickUpLocations = await pickUpResponse.json();
        const dropOffLocations = await dropOffResponse.json();

        // Hiển thị danh sách đón khách
        const pickUpTable = document.getElementById("pickUpTable");
        pickUpTable.innerHTML = ''; // Clear old data
        pickUpLocations.forEach(location => {
            const row = document.createElement("tr");
            row.dataset.locationId = location.locationId;
            const deleteButton = `<button class="btn btn-danger btn-sm" onclick="deleteLocation(this)">Xóa</button>`;
            // Tạo các ô dữ liệu cho bảng
            row.innerHTML = `
                <td>${location.nameLocation}</td>
                <td>${location.longitude}</td>
                <td>${location.latitude}</td>
                <td>${location.arrivalTime}</td>
                <td>${location.arrivalDate}</td>
                <td>${deleteButton}</td>
            `;
            // Gắn sự kiện click vào toàn bộ hàng để chọn hàng khi click
            row.onclick = function () {
                fillFormWithLocationData(location.locationId, location.nameLocation, location.longitude, location.latitude, location.arrivalTime, location.arrivalDate, 2, row);
            };
            // Thêm row vào bảng
            pickUpTable.appendChild(row);
        });

        // Hiển thị danh sách trả khách
        const dropOffTable = document.getElementById("dropOffTable");
        dropOffTable.innerHTML = ''; // Clear old data
        dropOffLocations.forEach(location => {
            const row = document.createElement("tr");
            row.dataset.locationId = location.locationId;
            const deleteButton = `<button class="btn btn-danger btn-sm" onclick="deleteLocation(this)">Xóa</button>`;
            // Tạo các ô dữ liệu cho bảng
            row.innerHTML = `
                <td>${location.nameLocation}</td>
                <td>${location.longitude}</td>
                <td>${location.latitude}</td>
                <td>${location.arrivalTime}</td>
                <td>${location.arrivalDate}</td>
                <td>${deleteButton}</td>
            `;
            // Gắn sự kiện click vào toàn bộ hàng để chọn hàng khi click
            row.onclick = function () {
                fillFormWithLocationData(location.locationId, location.nameLocation, location.longitude, location.latitude, location.arrivalTime, location.arrivalDate, 2, row);
            };
            // Thêm row vào bảng
            dropOffTable.appendChild(row);
        });
    } catch (error) {
        console.error("Lỗi khi tải dữ liệu:", error);
    }
}

let selectedRow = null;

function fillFormWithLocationData(locationId, nameLocation, longitude, latitude, arrivalTime, arrivalDate, locationType, rowElement) {
    // Đặt giá trị lên form
    document.getElementById("nameLocation").value = nameLocation;
    document.getElementById("longitude").value = longitude;
    document.getElementById("latitude").value = latitude;
    document.getElementById("arrival_time").value = arrivalTime;
    document.getElementById("arrival_date").value = arrivalDate;
    document.getElementById("locationType").value = locationType;

    // Bạn có thể lưu locationId để sử dụng sau này khi cập nhật dữ liệu
    sessionStorage.setItem("selectedLocationId", locationId);
    // Kiểm tra và bỏ chọn màu xanh từ hàng đã được chọn trước đó
    if (selectedRow) {
        selectedRow.style.backgroundColor = ""; // Quay về màu mặc định
    }
    updateMap(latitude, longitude);
    // Tô màu xanh cho hàng đang được chọn
    rowElement.style.backgroundColor = "lightgreen";

    // Cập nhật hàng đã chọn
    selectedRow = rowElement;
}

async function deleteLocation(button) {
    const token = localStorage.getItem("token") || sessionStorage.getItem("token");
    if (!token) {
        alert("Phiên đăng nhập hết hạn vui lòng đăng nhập lại");
        window.location.replace("login");
    }
    const row = button.closest('tr')
    const locationId = row.dataset.locationId;
    try {
        const response = await fetch(`/api/deletelocation?locationId=${locationId}`, {
            method: 'DELETE',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });
        if (!response.ok) {
            throw new Error("Có lỗi xảy ra khi xóa dữ liệu");
        } else {
            alert("Xóa dữ liệu thành công");
            loadLocations();
        }
    } catch (error) {
        console.error("Có lỗi xảy ra khi xóa dữ liệu:", error);
        alert("Có lỗi xảy ra khi xóa dữ liệu");
    }

}

// Gọi hàm loadLocations khi trang được tải
window.onload = loadLocations;
document.getElementById("backBtn").addEventListener("click", function () {
    window.location.replace("home");
});

// Giả sử bạn có routeId và bạn cần tải dữ liệu đón và trả khách
const routeId = sessionStorage.getItem("routeId"); // Thay đổi theo giá trị thực tế
// Hàm gọi API để lấy danh sách đón khách và trả khách
async function loadLocations() {
    try {
        const token = localStorage.getItem("token") || sessionStorage.getItem("token");
        if (!token) {
            alert("Phiên đăng nhập hết hạn vui lòng đăng nhập lại");
            window.location.replace("login");
            return;
        }
        const routeResponse = await fetch(`/api/getroute?routeId=${routeId}`, {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });
        if(!routeResponse.ok){
            throw new Error("Có lỗi xảy ra khi lấy dữ liệu ");
        }
        const route = await routeResponse.json();
        document.getElementById("startLocation").innerText = route.startLocation;
        document.getElementById("endLocation").innerText = route.endLocation;


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
            const row = `
                        <tr>
                            <td>${location.nameLocation}</td>
                            <td>${location.longitude}</td>
                            <td>${location.latitude}</td>
                            <td>${location.arrivalTime}</td>
                            <td>${location.arrivalDate}</td>
                        </tr>
                    `;
            pickUpTable.innerHTML += row;
        });

        // Hiển thị danh sách trả khách
        const dropOffTable = document.getElementById("dropOffTable");
        dropOffTable.innerHTML = ''; // Clear old data
        dropOffLocations.forEach(location => {
            const row = `
                        <tr>
                            <td>${location.nameLocation}</td>
                            <td>${location.longitude}</td>
                            <td>${location.latitude}</td>
                            <td>${location.arrivalTime}</td>
                            <td>${location.arrivalDate}</td>
                        </tr>
                    `;
            dropOffTable.innerHTML += row;
        });
    } catch (error) {
        console.error("Lỗi khi tải dữ liệu:", error);
    }
}

// Gọi hàm loadLocations khi trang được tải
window.onload = loadLocations;
document.getElementById("backBtn").addEventListener("click", function() {
    window.location.replace("home");
});

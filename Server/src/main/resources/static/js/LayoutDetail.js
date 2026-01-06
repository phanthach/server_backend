document.addEventListener("DOMContentLoaded", function () {
    getLayout();
});

async function getLayout() {
    const layoutId = sessionStorage.getItem('layoutId');
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Phiên đăng nhập hết hạn LD");
        window.location.replace('login');
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
            const errorMessage = await response.text();
            throw new Error(`Lỗi: ${errorMessage}`);
        }
        const layout = await response.json();
        setUpLayout(layout);
        getSeats(layout.layoutId);
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Không tồn tại ghế trong sơ đồ ghế');
    }
}

function setUpLayout(layout) {
    document.getElementById('layoutName').textContent = layout.nameLayout;
    document.getElementById('seatCapacity').textContent = layout.seatCapacity;
    document.getElementById('seatRows').textContent = layout.x;
    sessionStorage.setItem('seatRows', layout.x);
    sessionStorage.setItem('seatColumns', layout.y);
    document.getElementById('seatColumns').textContent = layout.y;
    if (layout.status == 1) {
        document.getElementById('layoutStatus').textContent = "Tạm khóa";
    } else {
        document.getElementById('layoutStatus').textContent = "Hoạt động";
    }

    // Move the retrieval of rows and cols here
    const rows = sessionStorage.getItem('seatRows'); // Số hàng
    const cols = sessionStorage.getItem('seatColumns'); // Số cột

    // Ensure rows and cols are available for createSeatChart
    createSeatChart(rows, cols, 1); // Assuming floor 1 for initial setup
}

async function getSeats(layoutId) {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Phiên đăng nhập hết hạn LD2");
        window.location.replace('login');
    }
    try {
        const response = await fetch(`/api/listseat/${layoutId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            alert("Không có ghế");
            return;
        }
        const seats = await response.json();
        loadSeatsFromSample(seats);
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Không lấy được thông tin sơ đồ ghế');
    }
}

// Hàm tạo sơ đồ ghế cho tầng
function createSeatChart(rows, cols, floor) {
    const floorDiv = document.createElement('div');
    floorDiv.classList.add('floor-section');
    floorDiv.id = `floor-${floor}`;
    floorDiv.innerHTML = `
                <h3>Sơ Đồ Ghế Tầng ${floor}</h3>
                <div class="seat-chart" style="--columns: ${cols};">
                    <!-- Sơ đồ ghế sẽ được thêm vào đây -->
                </div>
            `;

    document.getElementById("floorsContainer").appendChild(floorDiv);

    // Tạo các ghế trống ban đầu
    const seatChart = floorDiv.querySelector('.seat-chart');
    for (let y = 1; y <= rows; y++) {
        for (let x = 1; x <= cols; x++) {
            const seatDiv = document.createElement('div');
            seatDiv.classList.add('seat');
            seatDiv.dataset.x = x;
            seatDiv.dataset.y = y;
            seatDiv.dataset.floor = floor;
            seatChart.appendChild(seatDiv);
        }
    }
}

// Hàm load ghế từ danh sách mẫu
function loadSeatsFromSample(seats) {
    seats.forEach(seat => {
        const {nameSeat, position_x, position_y, floor} = seat;

        // Nếu sơ đồ cho tầng chưa tồn tại, tạo mới
        if (!document.getElementById(`floor-${floor}`)) {
            createSeatChart(sessionStorage.getItem('seatRows'), sessionStorage.getItem('seatColumns'), floor);
        }

        // Tìm ghế theo vị trí và đánh dấu ghế đã chiếm
        const seatDiv = document.querySelector(`.seat[data-x="${position_x}"][data-y="${position_y}"][data-floor="${floor}"]`);
        seatDiv.classList.add('occupied');
        seatDiv.textContent = nameSeat;
    });
}

// Thêm trình xử lý sự kiện cho nút
document.getElementById('backBtn').addEventListener('click', function () {
    window.history.back(); // Quay lại trang trước đó
});
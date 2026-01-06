document.addEventListener("DOMContentLoaded", function () {
    const layoutId = sessionStorage.getItem('layoutIdAdd');
    const rows = sessionStorage.getItem('seatRowsAdd'); // Số hàng
    const cols = sessionStorage.getItem('seatColumnsAdd'); // Số cột
    const floor = sessionStorage.getItem('floorAdd');
    // Ensure rows and cols are available for createSeatChart
    createSeatChart(rows, cols, floor);
    getSeats(layoutId);
});
document.getElementById('addSeatBtn').addEventListener('click', addSeat);
document.getElementById('saveLayoutBtn').addEventListener('click',function (){
    const layoutId = sessionStorage.getItem('layoutIdAdd')
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    fetch(`/api/countSeats/${layoutId}`, {
        method: 'GET',
        headers:{
            'Authorization': `Bearer ${token}`
        }
    })
    window.location.replace('home');
    sessionStorage.removeItem('layoutIdAdd');
    sessionStorage.removeItem('seatColumnsAdd');
    sessionStorage.removeItem('seatRowsAdd');

});
async function addSeat(){
    const layoutId = sessionStorage.getItem('layoutIdAdd');
    const nameSeat = document.getElementById('nameSeat').value;
    const position_x = document.getElementById('position_x').value;
    const position_y = document.getElementById('position_y').value;
    const floor = document.getElementById('floor').value;
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Phiên đăng nhập hết hạn Seat");
        window.location.replace('login');
    }
    try {
        const response = await fetch(`/api/addseat`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({layoutId: layoutId, nameSeat, position_x, position_y, floor}),
        });
        if (response.ok) {
            alert('Thêm ghế thành công');
            window.location.reload();
        } else {
            alert('Thêm ghế thất bại');
        }
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Có lỗi xảy ra khi thêm ghế' + error);
    }
}
async function getSeats(layoutId) {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Phiên đăng nhập hết hạn Seat 2");
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
        alert('Có lỗi xảy ra khi lấy thông tin sơ đồ ghế' + error);
    }
}

// Hàm tạo sơ đồ ghế cho tầng
function createSeatChart(rows, cols, floor) {
    for (let i = 1; i <= floor; i++) {
        const floorDiv = document.createElement('div');
        floorDiv.classList.add('floor-section');
        floorDiv.id = `floor-${i}`;
        floorDiv.innerHTML = `
            <h3>Sơ Đồ Ghế Tầng ${i}</h3>
            <div class="seat-chart" style="--columns: ${cols};">
                <!-- Sơ đồ ghế sẽ được thêm vào đây -->
            </div>
        `;
        document.getElementById("floorsContainer").appendChild(floorDiv);

        // Tạo các ghế trống ban đầu cho từng tầng
        const seatChart = floorDiv.querySelector('.seat-chart');
        for (let y = 1; y <= rows; y++) {
            for (let x = 1; x <= cols; x++) {
                const seatDiv = document.createElement('div');
                seatDiv.classList.add('seat');
                seatDiv.dataset.x = x;
                seatDiv.dataset.y = y;
                seatDiv.dataset.floor = i;
                // Thêm hình ảnh ghế vào ghế
                const seatImage = document.createElement('img');
                seatImage.src = "/img/armchair.png";
                seatImage.alt = `Ghế ${x},${y} tầng ${i}`;
                seatImage.classList.add('seat-image');

                seatDiv.appendChild(seatImage);
                // Thêm sự kiện click để chọn xóa ghế
                seatDiv.addEventListener('click', handleSeatClick);
                seatChart.appendChild(seatDiv);
            }
        }
    }
}


// Hàm load ghế từ danh sách mẫu
function loadSeatsFromSample(seats) {
    seats.forEach(seat => {
        const {seatId,nameSeat, position_x, position_y, floor} = seat;

        // Tìm ghế theo vị trí và đánh dấu ghế đã chiếm
        const seatDiv = document.querySelector(`.seat[data-x="${position_x}"][data-y="${position_y}"][data-floor="${floor}"]`);
        seatDiv.classList.add('occupied');
        seatDiv.textContent = nameSeat;
        seatDiv.dataset.seatId = seatId;
    });
}
// Xử lý sự kiện khi click vào ghế
function handleSeatClick(event) {
    const seatDiv = event.target;
    const seatName = seatDiv.textContent;
    const seatId =seatDiv.dataset.seatId;

    if (seatDiv.classList.contains('occupied')) {
        const confirmDelete = confirm(`Bạn có chắc chắn muốn xóa ghế ${seatName} không?`);
        if (confirmDelete) {
            deleteSeat(seatId);
        }
    } else {
        alert("Ghế này chưa được thêm.");
    }
}

// Hàm xóa ghế
async function deleteSeat(seatId) {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Phiên đăng nhập hết hạn s3");
        window.location.replace('login');
    }

    try {
        const response = await fetch(`/api/deleteseat/${seatId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            alert('Xóa ghế thành công');
            window.location.reload(); // Tải lại trang để cập nhật sơ đồ ghế
        } else {
            alert('Xóa ghế thất bại');
        }
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Có lỗi xảy ra khi xóa ghế: ' + error);
    }
}
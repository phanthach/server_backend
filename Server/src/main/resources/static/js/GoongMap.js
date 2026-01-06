// API Key của bạn
goongjs.accessToken = 'eFpkDf32mSR7Ik1kK7tZt3TpQMY5IepOaAETGuT1';
var currentMarker = null;
// Tạo bản đồ Goong
var map = new goongjs.Map({
    container: 'map',
    style: 'https://tiles.goong.io/assets/goong_map_web.json', // stylesheet location
    center: [105.83991, 21.02800], // vị trí bắt đầu [lng, lat]
    zoom: 13 // mức zoom bắt đầu
});

// API Key cho Autocomplete
const apiKey = 'vNlQwVhET6mwbHbteAXAUUIyWyl6IFAI3BraxuYj';

// Lấy input và phần tử gợi ý
const input = document.getElementById('autocomplete');
const suggestions = document.getElementById('suggestions');

// Hàm gọi API Goong Autocomplete
input.addEventListener('input', function () {
    const query = input.value;
    if (query.length > 2) { // Chỉ bắt đầu tìm kiếm khi nhập nhiều hơn 2 ký tự
        fetch(`https://rsapi.goong.io/Place/AutoComplete?api_key=${apiKey}&input=${query}`)
            .then(response => response.json())
            .then(data => {
                displaySuggestions(data.predictions);
            })
            .catch(error => console.error('Error:', error));
    } else {
        suggestions.innerHTML = ''; // Xóa gợi ý khi không có input
    }
});
map.on('click', function (e) {
    // Lấy tọa độ nơi người dùng click
    const lngLat = e.lngLat;
    if (currentMarker) {
        currentMarker.remove(); // Xóa marker cũ
    }
    // Thêm một marker tại vị trí click
    currentMarker = new goongjs.Marker()
        .setLngLat(lngLat)
        .addTo(map);
    // Cập nhật tọa độ vào các ô input
    document.getElementById('longitude').value = lngLat.lng;
    document.getElementById('latitude').value = lngLat.lat;
    // Hiển thị tọa độ trong alert
    alert(`Bạn đã chọn vị trí với tọa độ: ${lngLat.lat}, ${lngLat.lng}`);
});

// Hàm hiển thị gợi ý
function displaySuggestions(predictions) {
    suggestions.innerHTML = ''; // Xóa các gợi ý trước đó
    predictions.forEach(prediction => {
        const div = document.createElement('div');
        div.textContent = prediction.description;
        div.addEventListener('click', () => {
            selectSuggestion(prediction);
        });
        suggestions.appendChild(div);
    });
}
function updateMap(lat, lng){
    map.flyTo({
        center: [lng, lat],
        zoom: 14
    });
    if (currentMarker) {
        currentMarker.remove(); // Xóa marker cũ
    }
    // Thêm marker cho vị trí đã chọn
    currentMarker = new goongjs.Marker()
        .setLngLat([lng, lat])
        .addTo(map);
}
// Hàm xử lý khi chọn một gợi ý
function selectSuggestion(prediction) {
    input.value = prediction.description;
    suggestions.innerHTML = ''; // Ẩn các gợi ý

    // Gọi API để lấy thông tin chi tiết từ địa chỉ đã chọn
    fetch(`https://rsapi.goong.io/Place/Detail?place_id=${prediction.place_id}&api_key=${apiKey}`)
        .then(response => response.json())
        .then(data => {
            if (data.status === "OK" && data.result) {
                const location = data.result.geometry.location;
                const formattedAddress = data.result.formatted_address;
                // Cập nhật tọa độ vào các ô input
                document.getElementById('longitude').value = location.lng;
                document.getElementById('latitude').value = location.lat;
                // Hiển thị tọa độ trong alert
                alert(`Địa chỉ: ${formattedAddress}\nTọa độ: ${location.lat}, ${location.lng}`);

                // Di chuyển bản đồ đến vị trí mới
                map.flyTo({
                    center: [location.lng, location.lat],
                    zoom: 14
                });
                if (currentMarker) {
                    currentMarker.remove(); // Xóa marker cũ
                }

                // Thêm marker cho vị trí đã chọn
                currentMarker = new goongjs.Marker()
                    .setLngLat([location.lng, location.lat])
                    .addTo(map);
            } else {
                alert("Không tìm thấy địa chỉ.");
            }
        })
        .catch(error => console.error('Error:', error));
}
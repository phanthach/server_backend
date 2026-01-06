async function loginMod() {
    const phoneNumber = document.getElementById('phoneNumber').value;
    const password = document.getElementById('password').value;
    const remember = document.getElementById('remember').checked;

    if (!phoneNumber || !password) {
        alert('Vui lòng nhập đầy đủ số điện thoại và mật khẩu.');
        return;
    }

    const loginData = {
        phoneNumber: phoneNumber,
        password: password
    };

    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        const result = await response.json();

        if (result.status === 1) {
            alert("Đăng nhập thành công!");
            if (remember) {
                localStorage.setItem('token', result.token);
            } else {
                sessionStorage.setItem('token', result.token);
            }
            // Chuyển hướng đến trang chủ
            window.location.replace("home");
        } else {
            alert(result.message);
        }
    } catch (error) {
        console.error('Lỗi kết nối:', error);
        alert('Đăng nhập không thành công');
    }
}

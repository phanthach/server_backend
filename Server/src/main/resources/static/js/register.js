async function registerUser() {
    const fullname = document.getElementById('fullname').value;
    const birthday = document.getElementById('birthday').value;
    const address = document.getElementById('address').value;
    const phoneNumber = document.getElementById('phonenumber').value;
    const email = document.getElementById('email').value;
    const companyName = document.getElementById('companyName').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const busCode = generateBusCode()
    const roleId = 2

    if (password !== confirmPassword) {
        alert("Mật khẩu và xác nhận mật khẩu không khớp!");
        return;
    }

    const user = {
        fullname: fullname,
        birthDay: birthday,
        address: address,
        phoneNumber: phoneNumber,
        email: email,
        companyName: companyName,
        password: password,
        roleId: roleId
    };

    try {
        const response = await fetch('/api/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user),
        });

        const result = await response.json();
        if (result.status === 1) {
            alert("Đăng ký thành công!");
            window.location.href = "login"; // Chuyển hướng người dùng tới trang đăng nhập
        } else {
            alert(result.message);
        }
    } catch (error) {
        console.error('Error:', error);
        alert("Có lỗi xảy ra khi đăng ký.");
    }
}
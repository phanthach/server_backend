document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('token');
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('activeMenu');
    window.location.replace("login");
});

document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
        alert("Mời bạn đăng nhập lại!");
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('activeMenu');
        window.location.replace("login");
        return;
    }
    try {
        const response = await fetch('/api/validate', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            const errorMessage = await response.text();
        }

        const result = await response.json();
        if (result.status === 1) {
            const accountNameElement = document.getElementById('accountName');
            if (accountNameElement) {
                accountNameElement.innerText = `Xin chào, ${result.fullName}`;
            }

            const activeMenu = sessionStorage.getItem('activeMenu');
            const sectionId = sessionStorage.getItem('sectionId');
            if (activeMenu && sectionId) {
                const menuItem = document.querySelector(`.menu-item[href="${activeMenu}"]`);
                if (menuItem) {
                    await activateMenuItem(menuItem, sectionId);
                }
            }

            const submenus = document.querySelectorAll('.submenu');
            submenus.forEach(submenu => {
                const state = sessionStorage.getItem(submenu.id);
                submenu.style.display = (state === 'open') ? 'block' : 'none';
            });
        } else {
            alert('Mời bạn đăng nhập lại!');
            localStorage.removeItem('token');
            sessionStorage.removeItem('token');
            sessionStorage.removeItem('activeMenu');
            window.location.replace("login");
        }
    } catch (error) {
        alert('Mời bạn đăng nhập lạii!');
        localStorage.removeItem('token');
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('activeMenu');
        window.location.replace("login");
    }
});

function toggleSubmenu(id) {
    var submenu = document.getElementById(id);
    if (submenu) {
        submenu.style.display = (submenu.style.display === "none" || submenu.style.display === "") ? "block" : "none";
    }
}
let lastActiveMenuItem = null;
async function activateMenuItem(element, sectionId) {
    var items = document.querySelectorAll('.menu-item');
    items.forEach(function (item) {
        item.classList.remove('active');
        var submenu = item.nextElementSibling;
        if (submenu && submenu.classList.contains('submenu')) {
            submenu.style.display = 'none';
        }
    });
    element.classList.add('active');
    var submenu = element.nextElementSibling;
    if (submenu && submenu.classList.contains('submenu')) {
        submenu.style.display = 'block';
    }

    var sections = document.querySelectorAll('.content-section');
    sections.forEach(function (section) {
        section.classList.remove('active');
    });
    const sectionElement = document.getElementById(sectionId);
    if (sectionElement) {
        sectionElement.classList.add('active');
    }

    const menuHref = element.getAttribute('href');
    window.history.pushState({id: menuHref}, '', menuHref);
    sessionStorage.setItem('activeMenu', menuHref);
    sessionStorage.setItem('sectionId', sectionId);
    lastActiveMenuItem = element;

    if (menuHref === '#quanlytaixe') {
        await fetchDriverList();
    } else if (menuHref === '#quanlyxe') {
        await fetchVehicleList();
    } else if (menuHref === '#quanlysodoghe') {
        await fetchLayoutList();
    } else if (menuHref === '#quanlytuyenduong') {
        await fetchRouteList();
    }
}

function activateSubmenuItem(element, sectionId) {
    var items = document.querySelectorAll('.submenu-item');
    items.forEach(function (item) {
        item.classList.remove('active');
    });
    element.classList.add('active');

    var sections = document.querySelectorAll('.content-section');
    sections.forEach(function (section) {
        section.classList.remove('active');
    });
    const sectionElement = document.getElementById(sectionId);
    if (sectionElement) {
        sectionElement.classList.add('active');
    }
}
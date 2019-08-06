function login() {
    let url = 'http://localhost:8080/api/users/authenticate';
    let username = $('#username').val();
    let password = $('#password').val();
    let rememberMe = $('#remember_me').prop('checked');

    let data = {
        username,
        rememberMe,
        password
    };

    $.ajax({
        type: "POST",
        url: url,
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(data),
        success: function (data) {
            setJwtToken(data);
            alert("Success");
            createAuthorizationTokenHeader();
            const authorizedButtons = document.querySelectorAll('.authorized-button');
            const unauthorizedButtons = document.querySelectorAll('.unauthorized-button');
            unauthorizedButtons[0].style.display = 'none';
            unauthorizedButtons[1].style.display = 'none';
            authorizedButtons[0].style.display = 'initial';
            authorizedButtons[1].style.display = 'initial';
            $('#content').load('../main.html');
            loadTopTenDrivers();
            loadTrips();
            getJwtToken();
        },
        error: function () {
            alert('Wrong username/password')
        }
    });
}

function register() {
    let username = $('#username').val();
    let first_name = $('#first_name').val();
    let last_name = $('#last_name').val();
    let phone = $('#phone').val();
    let email = $('#email').val();
    let password = $('#password').val();

    let url = 'http://localhost:8080/api/users/register';

    let user = {
        username,
        first_name,
        last_name,
        phone,
        email,
        password
    };

    $.ajax({
        type: "POST",
        url: url,
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(user),
        success: function () {
            alert("Success");
            $('#content').load('../main.html');
            loadTopTenDrivers();
            loadTrips();
        }
    });
}

function logout() {
    removeJwtToken();
    const authorizedButtons = document.querySelectorAll('.authorized-button');
    const unauthorizedButtons = document.querySelectorAll('.unauthorized-button');
    unauthorizedButtons[0].style.display = 'initial';
    unauthorizedButtons[1].style.display = 'initial';
    authorizedButtons[0].style.display = 'none';
    authorizedButtons[1].style.display = 'none';
    $('#content').load('../main.html');
}
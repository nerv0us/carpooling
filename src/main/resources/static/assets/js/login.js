function login() {
    const url = 'http://localhost:8080/api/users/authenticate';

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
            updateNavigationButtons('none', 'initial');
            window.location.href = '/';
            setTimeout(50);
            loadTopTenDrivers();
            loadTrips();
            location.reload();
        },
        error: function () {
            alert('Wrong username/password')
        }
    });
}

function register() {
    const url = 'http://localhost:8080/api/users/register';

    let username = $('#username').val();
    let firstName = $('#first_name').val();
    let lastName = $('#last_name').val();
    let phone = $('#phone').val();
    let email = $('#email').val();
    let password = $('#password').val();
    let data = {

        username,
        firstName,
        lastName,
        email,
        password,
        phone

    };

    $.ajax({
        url: url,
        type: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(data),

        success: function () {
            alert("Successfully registered!");
            $('#page-header').hide();
            $('#content').load('../login.html');
        },
        error: function () {
            alert("Failed");
        }
    });
}

function logout() {
    removeJwtToken();
    window.location.href = '/';
    updateNavigationButtons('initial', 'none');
    location.reload();
}
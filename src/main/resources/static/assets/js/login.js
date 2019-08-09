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
            createAuthorizationTokenHeader();
            updateNavigationButtons('none', 'initial');
            $('#content').load('../main.html');
            loadTopTenDrivers();
            loadTrips();
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

    debugger;

    $.ajax({
        url: url,
        type: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(data),

        success: function () {
            $('#content').load('../login.html');

        },
        error: function () {
            console.log("failed");
            alert("Failed");
        }
    });
}

function logout() {
    removeJwtToken();
    updateNavigationButtons('initial', 'none');
    $('#content').load('../main.html');
    location.reload();
}
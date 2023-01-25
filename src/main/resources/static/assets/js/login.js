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
        success: function (response) {
            Swal.fire({
                position: 'top',
                type: 'success',
                title: 'You have successfully login!',
                showConfirmButton: false,
                timer: 2000
            });
            updateNavigationButtons('none', 'initial');
            window.location.href = '/';
            setJwtToken(response);
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
            Swal.fire({
                position: 'top',
                type: 'success',
                title: 'You have successfully registered!',
                showConfirmButton: false,
                timer: 2000
            });
            $('#content').load('../login.html');
            $('#page-header').hide();
        },
        error: function () {
            $('#content').append()
        }
    });
}

function logout() {
    removeJwtToken();
    window.location.href = '/';
    updateNavigationButtons('initial', 'none');
    location.reload();
}

function successRegisterMessage() {
    $("#message").append("Successfully registered")
}




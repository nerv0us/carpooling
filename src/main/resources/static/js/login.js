function login() {
    let url = 'http://localhost:8080/api/users/authenticate';
    let username = $('#username').val();
    let password = $('#password').val();

    let data = {};
    data.username = username;
    data.password = password;

    $.ajax({
        type: "POST",
        url: url,
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(data),
        success: function (data) {
            console.log(data.token);
            setJwtToken(data.token);
            alert("Success");
            refresh()
        },
        error: function () {
            console.log("Failed to log");
            $('#wrong-pass');
        }
    });
}
let id;
let username;
let reader;
const token = getJwtToken();

$(document).ready(function () {
    const url = 'http://localhost:8080/api/users/my-profile';
    $.ajax({
        async: true,
        crossDomain: true,
        url: url,
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`
        },
        success: function (response) {
            const fullName = `${response.firstName} ${response.lastName}`;
            id = `${response.id}`;
            username = `${response.username}`;
            sessionStorage.setItem('password', response.password);
            $('#userFullName').attr("value", fullName);
            $('#updateUsername').attr("value", username);
            $('#updateEmail').attr("value", response.email);
            $('#updateFirstName').attr("value", response.firstName);
            $('#updateLastName').attr("value", response.lastName);
            $('#updatePhone').attr("value", response.phone);
            $('#avatarImage').attr("src", response.avatarUri);

            $('#ratingAsDriver').append(response.ratingAsDriver);
            $('#ratingAsPassenger').append(response.ratingAsPassenger);
            // document.getElementById("ratingAsDriver").value = response.ratingAsDriver;

            // $('#ratingAsDriver').attr("value", response.ratingAsDriver);
            // $('#ratingAsPassenger').attr("width", response.ratingAsPassenger);
        },
        error: function () {
            console.log("failed");
        }
    });

    let readURL = function (input) {
        if (input.files && input.files[0]) {
            reader = new FileReader();

            reader.onload = function (e) {
                $('.avatar').attr('src', e.target.result);
            };

            reader.readAsDataURL(input.files[0]);
        }
    };

    $(".file-upload").on('change', function () {
        readURL(this);
    });

});

function updateProfileAvatar() {
    let url = `http://localhost:8080/api/users/${id}/avatar`;

    let image = $('#file')[0].files[0];
    let formData = new FormData();
    formData.append("file", image);

    $.ajax({
        url: url,
        method: 'POST',
        processData: false,
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`
        },
        data: formData,

        success: function () {
            alert("Successfully updated user");
            location.reload();

        },
        error: function () {
            console.log(user);
            alert("Failed to update user");
        }
    });
}

function updateProfileInfo() {
    const url = 'http://localhost:8080/api/users/update';

    let firstName = $('#updateFirstName').val();
    let lastName = $('#updateLastName').val();
    let phone = $('#updatePhone').val();
    let email = $('#updateEmail').val();
    let ratingAsDriver = null;
    let ratingAsPassenger = null;
    let avatarUri = null;

    let user = {
        id,
        username,
        firstName,
        lastName,
        email,
        phone,
        ratingAsDriver,
        ratingAsPassenger,
        avatarUri
    };

    $.ajax({
        url: url,
        type: "PUT",
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`
        },
        data: JSON.stringify(user),

        success: function () {
            alert("Successfully updated user");
            location.reload();

        },
        error: function () {
            console.log(user);
            alert("Failed to update user");
        }
    });
}
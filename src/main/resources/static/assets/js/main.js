$('#content').load('../main.html');

$(document).ready(function () {
    loadTopTenDrivers();
    loadTrips();

    if (getJwtToken() !== null) {
        updateNavigationButtons('none', 'initial')
    } else {
        updateNavigationButtons('initial', 'none')
    }
});

function updateNavigationButtons(first, second) {
    let authorizedButtons = document.querySelectorAll('.authorized-button');
    let unauthorizedButtons = document.querySelectorAll('.unauthorized-button');
    unauthorizedButtons[0].style.display = first;
    unauthorizedButtons[1].style.display = first;
    authorizedButtons[0].style.display = second;
    authorizedButtons[1].style.display = second;
    authorizedButtons[2].style.display = second;
}

function createTrip() {
    const url = 'http://localhost:8080/api/trips';
    const token = getJwtToken();

    let origin = $('#origin').val();
    let destination = $('#destination').val();
    let carModel = $('#car_model').val();
    let departureTime = $('#date_time').val();
    let message = $('#comment').val();
    let availablePlaces = parseInt($('#inputState').val());
    let pets = $('#allow_pets').prop('checked');
    let luggage = $('#allow_luggage').prop('checked');
    let smoking = $('#allow_smoking').prop('checked');

    let trip = {
        origin,
        destination,
        carModel,
        departureTime,
        message,
        availablePlaces,
        pets,
        luggage,
        smoking
    };

    console.log(trip);

    $.ajax({
        url: url,
        type: "POST",
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`

        },
        data: JSON.stringify(trip),

        success: function () {
            alert("Success")


        },
        error: function () {
            console.log("failed");
            alert("Failed")
        }
    });
}

function navigateToLogin() {
    $('#content').load('../login.html');
}

function navigateToRegister() {
    $('#content').load('../register.html');
}

function navigateToProfile() {
    window.location.href = "http://localhost:8080/profile.html";
}


$('.datetimepicker').datetimepicker({
    icons: {
        time: "fa fa-clock-o",
        date: "fa fa-calendar",
        up: "fa fa-chevron-up",
        down: "fa fa-chevron-down",
        previous: 'fa fa-chevron-left',
        next: 'fa fa-chevron-right',
        today: 'fa fa-screenshot',
        clear: 'fa fa-trash',
        close: 'fa fa-remove'
    }
});



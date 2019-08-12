// $('#content').load('../main.html');

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
    authorizedButtons[3].style.display = second;
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
        type: 'POST',
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

function shortSearchTrips() {

    const origin = $('#easyOriginSearch').val();
    const destination = $('#easyDestinationSearch').val();
    const easyDateTimeEarliest = $('#easyDateTimeEarliest').val();

    let filter = '';

    if (origin && destination) {
        filter = `?origin=${origin}&destination=${destination}`
    } else if (origin && !destination) {
        filter = `?origin=${origin}`
    } else if (!origin && destination) {
        filter = `?destination=${destination}`
    }

    console.log(filter);

    let url = `http://localhost:8080/api/trips${filter}&earliestDepartureTime=${easyDateTimeEarliest}`;

    $.ajax({
        url: url,
        type: 'GET',

        success: function (response) {
            $('#upperPart').hide();
            $('#top-drivers').hide();


            $('#trip-body').html('');
            $.each(response, function (i) {
                $("#trip-body").append(`
                 <div class="card mb-3" style="max-width: 540px;">
                    <div class="row no-gutters">                 
                        <div class="col-md-8">
                        <a href="#" class="trip">
                            <div class="card-body">
                                <h6 class="card-title">
                                    <p  class="row">
                                        <span>Origin: ${response[i].origin} &nbsp &nbsp Destination: ${response[i].destination}</span>
                                    </p>
                                    <br>
                                    <p  class="row">
                                        <span>Departure time: ${response[i].departureTime}</span>
                                    </p>
                                    <input type="hidden" class="tripId" value=${response[i].id}>                                                            
                                </h6>
                            </div>
                        </a>
                        </div>
                    </div>
                </div>
                 `);
            })

        },
        error: function (response) {
            console.log(response);
            alert("Failed");
            filter = '';
        }
    });
}

function searchTrips() {
    const searchOrigin = $('#searchOrigin').val();
    const searchDestination = $('#searchDestination').val();
    const searchByDateAndTimeEarliest = $('#searchDateTimeEarliest').val();
    const searchByDateAndTimeLatest = $('#searchDateTimeLatest').val();
    const searchAvailablePlaces = $('#searchAvailablePlaces').val();
    const searchIsAllowedSmoking = $('#searchIsAllowedSmoking').prop('checked').toString();
    const searchIsAllowedPets = $('#searchIsAllowedPets').prop('checked').toString();
    const searchIsAllowedLuggage = $('#searchIsAllowedLuggage').prop('checked').toString();
    const searchSortByParameter = $('#searchSortByParameter').val();

    let filterTrips = '';

    if (searchOrigin && searchDestination) {
        filterTrips = `?origin=${searchOrigin}&destination=${searchDestination}&`
    } else if (searchOrigin && !searchDestination) {
        filterTrips = `?origin=${searchOrigin}&`
    } else if (!searchOrigin && searchDestination) {
        filterTrips = `?destination=${searchDestination}&`
    } else {
        filterTrips = '?'
    }

    let sortParameter = '';

    if (searchSortByParameter === "Departure time") {
        sortParameter = "departureTime";
    } else if (searchSortByParameter === "Available places") {
        sortParameter = "availablePlaces"
    } else if (searchSortByParameter === "Driver rating") {
        sortParameter = "driverRating"
    }


    let url = `http://localhost:8080/api/trips${filterTrips}earliestDepartureTime=${searchByDateAndTimeEarliest}
    &latestDepartureTime=${searchByDateAndTimeLatest}&availablePlaces=${searchAvailablePlaces}
    &smoking=${searchIsAllowedSmoking}&pets=${searchIsAllowedPets}&luggage=${searchIsAllowedLuggage}
    &sortParameter=${sortParameter}`;

    console.log(url);

    $.ajax({
        url: url,
        type: 'GET',

        success: function (response) {
            console.log(response);

            $('#upperPart').hide();
            $('#top-drivers').hide();

            $('#trip-body').html('');
            $.each(response, function (i) {
                $("#trip-body").append(`
                 <div class="card mb-3" style="max-width: 540px;">
                    <div class="row no-gutters">                 
                        <div class="col-md-8">
                        <a href="#" class="trip">
                            <div class="card-body">
                                <h6 class="card-title">
                                    <p  class="row">
                                        <span>Origin: ${response[i].origin} &nbsp &nbsp Destination: ${response[i].destination}</span>
                                    </p>
                                    <br>
                                    <p  class="row">
                                        <span>Departure time: ${response[i].departureTime}</span>
                                    </p>
                                    <input type="hidden" class="tripId" value=${response[i].id}>                                                            
                                </h6>
                            </div>
                        </a>
                        </div>
                    </div>
                </div>
                 `);
            })

        },
        error: function (response) {
            console.log(response);
            filterTrips = '';
            alert("Failed")
        }
    });
}

function navigateToLogin() {
    $('#upperPart').hide();
    $('#easySearch').hide();
    $('#top-drivers').hide();
    $('#content').load('../login.html');
}

function navigateToRegister() {
    $('#upperPart').hide();
    $('#easySearch').hide();
    $('#top-drivers').hide();
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

let page;
$(document).on('click', '#page1', function f() {
    page = 1;
    loadTrips();
});
$(document).on('click', '#page2', function f() {
    page = 2;
    loadTrips();
});
$(document).on('click', '#page3', function f() {
    page = 3;
    loadTrips();
});
$(document).on('click', '#page4', function f() {
    page = 4;
    loadTrips();
});
$(document).on('click', '#page5', function f() {
    page = 5;
    loadTrips();
});
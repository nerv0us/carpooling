// $('header').load('../header.html');
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

function openCreateTripModal() {
    //  $('#create-trip-modal').load('../create-trip.html');
    document.querySelector('#create-trip-modal');

//     setTimeout(_ => {
//         $('#datetimepicker').jqxDateTimeInput({
//             width: '300px',
//             height: '25px',
//             formatString: 'F',
//             theme: 'material-purple'
//         });
//     },1000)
//
//
//
// }
}

function closeCreateTripModal() {
    // $('#create-trip-modal').html('');
    document.querySelector('#create-trip-modal').style.display = 'none';
}

function updateNavigationButtons(first, second) {
    const authorizedButtons = document.querySelectorAll('.authorized-button');
    const unauthorizedButtons = document.querySelectorAll('.unauthorized-button');
    unauthorizedButtons[0].style.display = first;
    unauthorizedButtons[1].style.display = first;
    authorizedButtons[0].style.display = second;
    authorizedButtons[1].style.display = second;
    authorizedButtons[2].style.display = second;
}

function navigateToLogin() {
    $('#content').load('../login.html');
}

function navigateToRegister() {
    $('#content').load('../register.html');
}

function navigateToProfile() {
    $('#content').load('../profile.html');
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

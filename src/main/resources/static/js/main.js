$(function () {
    $('header').load('../header.html');
    $('#content').load('../main.html');
    loadTopTenDrivers();
    loadTrips();});

function navigateToLogin() {
    $('#content').load('../login.html');
}

function navigateToRegister() {
    $('#content').load('../register.html');
}
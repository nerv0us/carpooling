loadTopTenDrivers();

loadTrips();

function loadTopTenDrivers() {
    $.ajax({
        url: `http://localhost:8080/api/users/top-ten-drivers`,
        method: 'GET',
        success: function (response) {
            $('#top-ten-drivers').html('');
            $.each(response, function (i) {

                $("#top-ten-drivers").append(`
                <tr>
                    <th scope="row">${i + 1}</th>
                    <td>${response[i].ratingAsDriver}</td>
                    <td>${response[i].username}</td>
                    <td>${response[i].firstName}</td>
                    <td>${response[i].lastName}</td>
                </tr>
                 `);
            })
        }
    })
}

//Add filtering and sorting function!
function loadTrips() {
    $.ajax({
        url: `http://localhost:8080/api/trips`,
        method: 'GET',
        success: function (response) {
            $('#trip-body').html('');
            $.each(response, function (i) {

                $("#trip-body").append(`
                 <div class="card mb-3" style="max-width: 540px;">
                    <div class="row no-gutters">                 
                        <div class="col-md-8">
                            <div class="card-body">
                                <h6 class="card-title">                              
                                    <a  class="row">
                                        <span>
                                        <img src='${response[i].driver.avatarUri}' style="max-width:50px;width:100%" alt="...">
                                        Driver: ${response[i].driver.username} &nbsp &nbsp Rating: ${response[i].driver.ratingAsDriver}</span>
                                    </a>
                                    <br>
                                    <a  class="row">
                                        <span>Origin: ${response[i].origin} &nbsp &nbsp Destination: ${response[i].destination}</span>
                                    </a>
                                    <br>
                                    <a  class="row">
                                        <span>Departure time: ${response[i].departureTime}</span>
                                    </a>
                                </h6>
                            </div>
                        </div>
                    </div>
                </div>
                 `);
            })
        }
    })
}

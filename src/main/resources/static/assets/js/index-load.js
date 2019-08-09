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
                        <a href="#" class="singleTrip">
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
        }
    })
}

$(document).on("click", ".singleTrip", function () {
    let tripId = $(this).parent().find('.tripId').val();
    console.log(tripId);
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId,
        method: 'GET',
        success: function (response) {
            let passengers = response.passengers;
            $('#top-drivers').html('');
            $("#upperPart").children().attr("disabled","disabled");
            $('#trip-body').html('');
            $("#trip-body").append(`
                 <div class="card mb-3" style="max-width: 2040px;">
                    <div class="row no-gutters">                 
                        <div class="col-md-8">
                            <div class="card-body">
                                <h6 class="card-title">                              
                                    <a  class="row">
                                        <span>
                                        <img src='${response.driver.avatarUri}' style="max-width:50px;width:100%" alt="...">
                                        Driver: ${response.driver.username} &nbsp &nbsp Rating: ${response.driver.ratingAsDriver}</span>
                                    </a>
                                    <br>
                                    <a  class="row">
                                    <span>Phone: ${response.driver.phone}</span>
                                    </a>
                                    <br>
                                    <a  class="row">
                                        <span>Origin: ${response.origin} &nbsp &nbsp Destination: ${response.destination}</span>
                                    </a>
                                    <br>
                                    <a  class="row">
                                        <span>Departure time: ${response.departureTime}</span>
                                    </a>
                                    <br>
                                    <a>Passengers:</a>
                                    <a  class="row"  id="tripBody">
                                    </a>
                                    <input type="hidden" class="tripId" value=${response.id}>                                  
                                </h6>
                            </div>
                        </div>
                    </div>
                </div>
                 `);
            $.each(passengers, function (i) {
                $("#tripBody").append(`               
                    <span>Name: ${passengers[i].username}</span>&nbsp&nbsp&nbsp
                    <span>Rating: ${passengers[i].ratingAsPassenger}</span>&nbsp&nbsp&nbsp
                    <span>Status: ${passengers[i].passengerStatusEnum}</span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                `)
            })
        }
    })
});
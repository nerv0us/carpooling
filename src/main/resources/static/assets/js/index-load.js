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
        }
    })
}
let tripId;
$(document).on("click", ".trip", function () {
    tripId = $(this).parent().find('.tripId').val();
    console.log(tripId);
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId,
        method: 'GET',
        success: function (response) {
            let passengers = response.passengers;
            let comments = response.comments;
            /*$('#top-drivers').html('');
            $("#upperPart").html('');*/
            $('#content').html('');
            $('#content').append(`
            <div class="main main-raised" style="margin: 150px;">
                <div class="container">
                    <div class="section text-center">
                        <h2 class="title">Trip</h2>
                        <div class="row">
                            <div class="card mb-3" style="max-width: 2040px;">
                                <div class="row no-gutters">
                                    <div class="col-md-8">
                                        <div class="card-body">
                                            <h6 class="card-title">                                              
                                                <a class="row">
                                                <img src='${response.driver.avatarUri}' style="max-width:100px;width:100%" alt="...">
                                                </a>   
                                                <a  class="row">
                                                    Driver: ${response.driver.firstName}&nbsp ${response.driver.lastName}
                                                </a>                                
                                                <a class="row">
                                                </a>
                                                <a class="row">
                                                    Rating: ${response.driver.ratingAsDriver}
                                                </a>
                                                <a  class="row">
                                                    Phone: ${response.driver.phone}
                                                </a>
                                                <br>
                                                <a class="row">Car model: ${response.carModel}</a>
                                                <br>
                                                <a  class="row">
                                                    Origin: ${response.origin}
                                                </a>
                                                <br>
                                                <a  class="row">
                                                    Destination: ${response.destination}
                                                </a>  
                                                <br>
                                                <a  class="row">
                                                    Departure time: ${response.departureTime}
                                                </a>
                                                <br>
                                                <a  class="row">
                                                    Available places: ${response.availablePlaces}
                                                </a>
                                                <br>
                                                <a  class="row">
                                                    Smoking: ${response.smoking}
                                                </a>
                                                <br>
                                                <a  class="row">
                                                    Pets: ${response.pets}
                                                </a>
                                                <br>
                                                <a  class="row">
                                                    Luggage: ${response.luggage}
                                                </a>
                                                <br>
                                                <a class="row">
                                                    Message: ${response.message}
                                                </a>
                                                <br>
                                                <a class="comments">Comments: </a>
                                                <br>
                                                <a class="passengers">Passengers: </a>
                                                <br>
                                                <br>
                                                <br>
                                                <br>
                                                <span class="apply"></span>
                                            </h6>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>                             
            `);
            $.each(comments, function (i) {
                $(".comments").append(`
                    <a  class="row">                
                        "${comments[i].message}"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp                          
                    </a>                 
                `)
            });
            $.each(passengers, function (i) {
                $(".passengers").append(`
                    <a class="row">               
                    Name: ${passengers[i].firstName}&nbsp ${passengers[i].lastName}&nbsp&nbsp&nbsp&nbsp 
                    Rating: ${passengers[i].ratingAsPassenger}&nbsp&nbsp&nbsp&nbsp 
                    Status: ${passengers[i].passengerStatusEnum}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp  
                    </a> 
                `)
            });
            $('.apply').append(`
            <button type="button" class="btn btn-lg btn-primary" id="applyButton">Apply for this trip</button>
              `)
        }
    })
});

$(document).on("click", "#applyButton", function () {
    const token = getJwtToken();

    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/passengers`,
        headers: {
            'Content-Type': 'application/json',
            Authorization:` Bearer ${token}`
        },
        method: "POST",
        success: function () {
            alert("success")

        },
        error: function(xhr) {
            alert(xhr.responseText);
        }
    })
});


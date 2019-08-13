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

let counter;
let tripId;
$(document).on("click", ".trip", function () {
    $('#upperPart').hide();
    $('#easySearch').hide();
    $('#top-drivers').hide();
    counter = 0;
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
                                                                                                                                            
                                               
                                                <a class="row">
                                                Driver: ${response.driver.firstName}&nbsp ${response.driver.lastName}
                                                <a/>
                                                <br>
                                                <a class="row">
                                                Rating: ${response.driver.ratingAsDriver}
                                                </a>
                                                <br>
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
                                                    Trip status: ${response.tripStatus}
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
                                                <span class="apply"></span>&nbsp&nbsp
                                                <span class="rateDriver"></span>    
                                                <span class="error"></span>                                           
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
                    <a href="#" class="row" id="singlePassenger">
                    <i class="material-icons">face</i>&nbsp       
                    Name: ${passengers[i].firstName}&nbsp ${passengers[i].lastName}&nbsp&nbsp&nbsp&nbsp 
                    Rating: ${passengers[i].ratingAsPassenger}&nbsp&nbsp&nbsp&nbsp 
                    Status: ${passengers[i].passengerStatusEnum}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp  
                    </a>
                    <span class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Rate passenger                       
                        </button>
                        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                          <a class="passenger-rate-1" href="#">1</a>                        
                          <a class="passenger-rate-2" href="#">2</a>
                          <a class="passenger-rate-3" href="#">3</a>
                          <a class="passenger-rate-4" href="#">4</a>
                          <a class="passenger-rate-5" href="#">5</a>  
                          <input type="hidden" class="passenger-id" value=${passengers[i].userId}>
                          <input type="hidden" class="passenger-name" value=${passengers[i].firstName}></a>              
                        </div>
                    </span>
                `)
            });
            $('.apply').append(`
            <button type="button" class="btn btn-lg btn-primary" id="applyButton">Apply for this trip</button>
              `);
            $('.rateDriver').append(`
            <span class="dropdown">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              Rate driver
            </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                <a class="dropdown-item" href="#" id="driver-rate-1">1</a>
                <a class="dropdown-item" href="#" id="driver-rate-2">2</a>
                <a class="dropdown-item" href="#" id="driver-rate-3">3</a>
                <a class="dropdown-item" href="#" id="driver-rate-4">4</a>
                <a class="dropdown-item" href="#" id="driver-rate-5">5</a>
            </div>
            </span>
              `)
        }
    })
});

let passengerId;
let passengerName;
$(document).on("click", ".passenger-rate-1", function () {
    const token = getJwtToken();
    passengerId = $(this).parent().find('.passenger-id').val();
    passengerName = $(this).parent().find('.passenger-name').val();
    console.log(passengerId);
    let rating = 1;
    let data = {
        rating
    };
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/passengers/` + passengerId + `/rate`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        data: JSON.stringify(data),
        success: function () {
            alert("You have successfully rated " + passengerName)
        },
        error: function (xhr) {
            alert(xhr.responseText)
        }
    });
});
$(document).on("click", ".passenger-rate-2", function () {
    const token = getJwtToken();
    passengerId = $(this).parent().find('.passenger-id').val();
    passengerName = $(this).parent().find('.passenger-name').val();
    console.log(passengerId);
    let rating = 2;
    let data = {
        rating
    };
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/passengers/` + passengerId + `/rate`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        data: JSON.stringify(data),
        success: function () {
            alert("You have successfully rated " + passengerName)
        },
        error: function (xhr) {
            alert(xhr.responseText)
        }
    });
});
$(document).on("click", ".passenger-rate-3", function () {
    const token = getJwtToken();
    passengerId = $(this).parent().find('.passenger-id').val();
    passengerName = $(this).parent().find('.passenger-name').val();
    console.log(passengerId);
    let rating = 3;
    let data = {
        rating
    };
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/passengers/` + passengerId + `/rate`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        data: JSON.stringify(data),
        success: function () {
            alert("You have successfully rated " + passengerName)
        },
        error: function (xhr) {
            alert(xhr.responseText)
        }
    });
});
$(document).on("click", ".passenger-rate-4", function () {
    const token = getJwtToken();
    passengerId = $(this).parent().find('.passenger-id').val();
    passengerName = $(this).parent().find('.passenger-name').val();
    console.log(passengerId);
    let rating = 4;
    let data = {
        rating
    };
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/passengers/` + passengerId + `/rate`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        data: JSON.stringify(data),
        success: function () {
            alert("You have successfully rated " + passengerName)
        },
        error: function (xhr) {
            alert(xhr.responseText)
        }
    });
});
$(document).on("click", ".passenger-rate-5", function () {
    const token = getJwtToken();
    passengerId = $(this).parent().find('.passenger-id').val();
    passengerName = $(this).parent().find('.passenger-name').val();
    console.log(passengerId);
    let rating = 5;
    let data = {
        rating
    };
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/passengers/` + passengerId + `/rate`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        data: JSON.stringify(data),
        success: function () {
            alert("You have successfully rated " + passengerName)
        },
        error: function (xhr) {
            alert(xhr.responseText)
        }
    });
});
$(document).on("click", "#driver-rate-1", function () {
    const token = getJwtToken();
    let rating = 1;
    let data = {
        rating
    };
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/driver/rate`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        data: JSON.stringify(data),
        success: function () {
            alert("You have successfully rated driver")
        },
        error: function (xhr) {
            alert(xhr.responseText)
        }
    });
});
$(document).on("click", "#driver-rate-2", function () {
    const token = getJwtToken();
    let rating = 2;
    let data = {
        rating
    };
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/driver/rate`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        data: JSON.stringify(data),
        success: function () {
            alert("You have successfully rated driver")
        },
        error: function (xhr) {
            alert(xhr.text)
        }
    });
});
$(document).on("click", "#driver-rate-3", function () {
    const token = getJwtToken();
    let rating = 3;
    let data = {
        rating
    };
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/driver/rate`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        data: JSON.stringify(data),
        success: function () {
            alert("You have successfully rated driver")
        },
        error: function (xhr) {
            alert(xhr.responseText)
        }
    });
});
$(document).on("click", "#driver-rate-4", function () {
    const token = getJwtToken();
    let rating = 4;
    let data = {
        rating
    };
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/driver/rate`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        data: JSON.stringify(data),
        success: function () {
            alert("You have successfully rated driver")
        },
        error: function (xhr) {
            alert(xhr.responseText)
        }
    });
});
$(document).on("click", "#driver-rate-5", function () {
    const token = getJwtToken();
    let rating = 5;
    let data = {
        rating
    };
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/driver/rate`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        data: JSON.stringify(data),
        success: function () {
            alert("You have successfully rated driver")
        },
        error: function (xhr) {
            alert(xhr.responseText)
        }
    });
});
$(document).on("click", "#applyButton", function () {
    const token = getJwtToken();
    $.ajax({
        url: `http://localhost:8080/api/trips/` + tripId + `/passengers`,
        headers: {
            'Content-Type': 'application/json',
            Authorization: ` Bearer ${token}`
        },
        method: "POST",
        success: function () {
            $('.error').append(`
                        <p style="color: red">
                        Success
                        </p>
                `);
            counter++;
        },
        error: function (xhr) {
            if (counter === 0) {
                if (token === null) {
                    $('.error').append(`
                        <p style="color: red">
                        You are not logged!
                        </p>
                `);
                } else {
                    $('.error').append(`
                        <p style="color: red">
                        ${xhr.responseText}
                        </p>
                 `);
                }
            }
            counter = 1;
        }
    });
});




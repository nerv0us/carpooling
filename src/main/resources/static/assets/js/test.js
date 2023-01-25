'use strict';

// $(document).ready(function () {
function getData() {
    /*prompt("In here");*/

    $("#btnClick").click(function () {
        alert("Handler for .click() called.");
    });

    $("#txtSample").html("Nedko");
    $("#txtSample").css("background-color", "red");
    $("#html").append("<h1>html</h1>");
    $("#html").append("<h2>html</h2>");
    $("#html").remove();

    $("#btnHide").click(function () {
        let val = $("#btnHide").val();
        if (val === 'Hide') {
            $(".image").css("display", "none");
            $("#btnHide").val('Show');
        } else {
            $(".image").css("display", "block");
            $("#btnHide").val('Hide');
        }
    });

    $(".image").on("click", function () {
        $(".image").css("border", "3px solid black")
    });

    $("#btnSubmit").on("click", () => alert(`Hello ${$("#txtName").val()}`));
    $("#btnData").on("click", () =>
        /*$.ajax({
            url: "http://localhost:8080/api/trips/77",
            method: "GET",
            success: function (data) {
                $("#txtSample").html(JSON.stringify(data));

            },
            error: function (err) {
                $(".error").html(JSON.stringify(err));
            }
        })*/
        $.get("http://localhost:8080/api/trips/1", function (data) {
            $("#txtSample").html(`<h1> Driver:${data.driver.username}</h1><h2>${data.carModel}</h2><h2>${data.tripStatus}</h2>`);
        }).fail(function (err) {
            $(".error").html(JSON.stringify(err));
        })
    );

    $("#btnWeb").on("click", () => {
        $.get(
            `http://api.openweathermap.org/data/2.5/weather?q=${$('#txtName').val()}&units=metric&appid=864c48480dc7849091a6d38241a36f2c`,
            function (data) {
                const info = data.weather[0];
                //alert(info.description);
                $("#txtSample").html(`<h1>${info.main}</h1><h2>${info.description}</h2><h2>${data.main.temp}</h2>`);
            }
        ).fail(function (err) {
            $(".error").html(JSON.stringify(err));
        });
    });
}
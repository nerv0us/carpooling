let TOKEN_KEY = 'jwtToken';

let getJwtToken = function () {
    let token = localStorage.key(0);
    if (token < Date.now() / 1000) {
        removeJwtToken();
    }
    console.log(token);
    return token;
};

let setJwtToken = function (token) {

    localStorage.setItem(TOKEN_KEY, token.id_token);
};

let createAuthorizationTokenHeader = function () {
    let token = getJwtToken();
    console.log("createAuthorizationTokenHeader");
    if (token) {
        return {
            "Authorization": "Bearer " + token
        };
    } else {
        return {
            'Content-Type': 'application/json'
        };
    }
};

function getExpiryTimeFromToken (){
    let claims = parseJwt();
    return claims.exp;
};

let justGetJwtToken = function(){
    console.log('justGetJwtToken');
    console.log(TOKEN_KEY);
    return localStorage.getItem(TOKEN_KEY);
};

function parseJwt () {
    let token = justGetJwtToken();
    console.log('parseJwt');
    let base64Url = token.split('.')[1];
    let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(window.atob(base64));
};

let removeJwtToken = function () {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.clear();
};
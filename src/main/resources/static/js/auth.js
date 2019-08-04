let TOKEN_KEY = 'jwtToken';

let getJwtToken = function () {
    let token = localStorage.getItem(TOKEN_KEY);
    if (token && getExpiryTimeFromToken() < Date.now() / 1000) {
        removeJwtToken();
        refresh();
    }
    return token;
};

let setJwtToken = function (token) {
    localStorage.setItem(TOKEN_KEY, token);
};

let createAuthorizationTokenHeader = function () {
    let token = getJwtToken();
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
}

let removeJwtToken = function () {
    localStorage.removeItem(TOKEN_KEY);
};
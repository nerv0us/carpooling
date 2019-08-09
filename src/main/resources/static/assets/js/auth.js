let TOKEN_KEY = 'jwtToken';

let getJwtToken = function () {
    return localStorage.getItem(TOKEN_KEY);
};

let setJwtToken = function (token) {

    localStorage.setItem(TOKEN_KEY, token.id_token);
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

let removeJwtToken = function () {
    localStorage.clear();
};
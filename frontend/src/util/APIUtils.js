import { API_BASE_URL, SEMINAR_LIST_SIZE, CONTRACTOR_LIST_SIZE, ACCESS_TOKEN } from '../constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });
    
    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response => {
            if(!response.ok) {
                return Promise.reject(response.json());
            }
            if (response.status === 204) {
                return Promise.resolve(response);
            }
            return response.json();
        }
    );
};

export function deleteItem(item) {
    return request({
        url: item._links.self.href,
        method: 'DELETE'
    })
}

export function getAllSeminars(page, size, sorter) {
    page = page || 0;
    size = size || SEMINAR_LIST_SIZE;
    sorter = sorter || {field:"key",order:"asc"};
    if (!sorter.field) {
        return;
    }
    sorter.order === "ascend" ? sorter.order = "asc" : null;
    sorter.order === "descend" ? sorter.order = "desc" : null;

    return request({
        url: API_BASE_URL + "/seminars?page=" + page + "&size=" + size + "&sort=" +sorter.field + "," + sorter.order,
        method: 'GET'
    });
}

export function createSeminar(seminarData) {
    return request({
        url: API_BASE_URL + "/seminars",
        method: 'POST',
        body: JSON.stringify(seminarData)
    });
}

export function getAllContractors(page, size) {
    page = page || 0;
    size = size || CONTRACTOR_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/contractors?page=" + page + "&size=" + size + "&sort=key,asc",
        method: 'GET'
    });
}

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/signin",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function checkUsernameAvailability(username) {
    return request({
        url: API_BASE_URL + "/user/checkUsernameAvailability?username=" + username,
        method: 'GET'
    });
}

export function checkEmailAvailability(email) {
    return request({
        url: API_BASE_URL + "/user/checkEmailAvailability?email=" + email,
        method: 'GET'
    });
}


export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/me",
        method: 'GET'
    });
}

export function getUserProfile(username) {
    return request({
        url: API_BASE_URL + "/user/" + username,
        method: 'GET'
    });
}
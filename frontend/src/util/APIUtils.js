import { API_BASE_URL, LIST_SIZE, ACCESS_TOKEN } from '../constants';

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
    size = size || LIST_SIZE;
    if (!sorter || !sorter.field) {
        sorter = {field:"key",order:"asc"};
    }
    sorter.order === "ascend" ? sorter.order = "asc" : null;
    sorter.order === "descend" ? sorter.order = "desc" : null;

    return request({
        url: API_BASE_URL + "/seminars?page=" + page + "&size=" + size + "&sort=" +sorter.field + "," + sorter.order,
        method: 'GET'
    });
}

export function getSeminarById(id) {
    return request({
        url: API_BASE_URL + "/seminars/" + id,
        method: 'GET'
    });
}

export function getContractorById(id) {
    return request({
        url: API_BASE_URL + "/contractors/" + id,
        method: 'GET'
    });
}

export function getTraineeById(id) {
    return request({
        url: API_BASE_URL + "/trainees/" + id,
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

export function getAllContractors(page, size, sorter) {
    page = page || 0;
    size = size || LIST_SIZE;
    if (!sorter || !sorter.field) {
        sorter = {field:"key",order:"asc"};
    }
    sorter.order === "ascend" ? sorter.order = "asc" : null;
    sorter.order === "descend" ? sorter.order = "desc" : null;

    return request({
        url: API_BASE_URL + "/contractors?page=" + page + "&size=" + size + "&sort=" +sorter.field + "," + sorter.order,
        method: 'GET'
    });
}

export function getAllTrainees(page, size, sorter) {
    page = page || 0;
    size = size || LIST_SIZE;
    if (!sorter || !sorter.field) {
        sorter = {field:"key",order:"asc"};
    }
    sorter.order === "ascend" ? sorter.order = "asc" : null;
    sorter.order === "descend" ? sorter.order = "desc" : null;

    return request({
        url: API_BASE_URL + "/trainees?page=" + page + "&size=" + size + "&sort=" +sorter.field + "," + sorter.order,
        method: 'GET'
    });
}

export function getAllSpecialties(page, size, sorter, filter) {
    page = page || 0;
    size = size || LIST_SIZE;
    if (!sorter || !sorter.field) {
        sorter = {field:"key",order:"asc"};
    }
    sorter.order === "ascend" ? sorter.order = "asc" : null;
    sorter.order === "descend" ? sorter.order = "desc" : null;

    return request({
        url: API_BASE_URL + "/specialties?page=" + page + "&size=" + size + "&sort=" +sorter.field + "," + sorter.order,
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
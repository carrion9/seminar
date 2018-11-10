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
                return Promise.reject(response);
            }
            if (response.status === 204) {
                return Promise.resolve(response);
            }
            return response.json();
        }
    );
};

const uploadFile = (options) => {
    const headers = new Headers();

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response => {
                if(!response.ok) {
                    return Promise.reject(response);
                }
                if (response.status === 204) {
                    return Promise.resolve(response);
                }
                return response;
            }
        );
};

const downloadFile = (options) => {
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
            const filename =  options.name;
            response.blob().then(blob => {
                let url = window.URL.createObjectURL(blob);
                let a = document.createElement('a');
                a.href = url;
                a.download = filename;
                a.click();
            });
            }
        );
};

export function deleteItem(item) {
    return request({
        url: item._links.self.href,
        method: 'DELETE'
    });
}

export function updateItem(item) {
    return request({
        url: item._links.self.href,
        method: 'PUT',
        body: JSON.stringify(item)
    });
}

export function updateCost(key, cost) {
    return request({
        url: API_BASE_URL + "/seminarTrainees/" + key,
        method: 'PATCH',
        body: `{
            "cost": ${cost}
        }`
    });
}

export function updateGrade(key, grade) {
    return request({
        url: API_BASE_URL + "/seminarTrainees/" + key,
        method: 'PATCH',
        body: `{
            "grade": ${grade}
        }`
    });
}

export function updatePassed(key, passed) {
    return request({
        url: API_BASE_URL + "/seminarTrainees/" + key,
        method: 'PATCH',
        body: `{
            "passed": ${passed}
        }`
    });
}

export function insertItem(item, type) {
    return request({
        url: API_BASE_URL + '/' + type,
        method: 'POST',
        body: JSON.stringify(item)
    });
}

export function insertSeminarTraineeContractorSpecialty(seminarId, traineeId, contractorid, specialtyId) {
    return request({
        url: API_BASE_URL + "/seminarTrainees",
        method: 'POST',
        body: `{
            "cost": 60,
            "actualCost": 60,
            "passed": false,
            "grade": 0.0,
            "seminar":"${API_BASE_URL}/seminars/${seminarId}",
            "trainee":"${API_BASE_URL}/trainees/${traineeId}",
            "contractor":"${API_BASE_URL}/contractors/${contractorid}",
            "specialty":"${API_BASE_URL}/specialties/${specialtyId}"
        }`
    });
}

export function insertSeminarSpecialty(seminarId, specialtyId) {
    return request({
        url: API_BASE_URL + '/seminarSpecialties',
        method: 'POST',
        body: `{
            "seminar":"${API_BASE_URL}/seminars/${seminarId}",
            "specialty":"${API_BASE_URL}/specialties/${specialtyId}"
        }`
    });
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

export function getAttendance(seminarId, specialtyId, name) {
    return downloadFile({
        url: API_BASE_URL + "/seminars/" + seminarId + "/attendance-document/" + specialtyId,
        method: 'GET',
        name: name
    });
}

export function upload(uploadRequest) {
    return uploadFile({
        url: API_BASE_URL + "/upload",
        method: 'POST',
        body: uploadRequest
    });
}

export function uploadImage(uploadRequest) {
    return uploadFile({
        url: API_BASE_URL + "/traineeImageUpload",
        method: 'POST',
        body: uploadRequest
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


export function getContractorByAFM(afm) {
    return request({
        url: API_BASE_URL + "/contractors/search/findByAfm?afm=" + afm,
        method: 'GET',
    });
}

export function getTraineeByAMA(ama) {
    return request({
        url: API_BASE_URL + "/trainees/search/findByAma?ama=" + ama,
        method: 'GET',
    });
}

export function getSpecialtyByName(name){
    return request({
        url: API_BASE_URL + '/specialties/search/findByName?name=' + name,
        method: 'GET',
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
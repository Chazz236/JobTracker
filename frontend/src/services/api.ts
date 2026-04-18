import axios from "axios";

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

api.interceptors.response.use(function onFulfilled(response) {
    return response;
}, function onRejected(error) {
    return Promise.reject(error)
});

export default api;
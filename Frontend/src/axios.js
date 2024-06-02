import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:8090';
// Add a request interceptor
axios.interceptors.request.use(function (config) {
    // Check if the URL does not include '/auth/'
    if (!config.url.includes('/auth/')) {
        // Get the token from local storage
        const token = localStorage.getItem('token');
        if (token) {
            // Add the token to the headers
            config.headers['Authorization'] = 'Bearer ' + token;
        }
    }
    return config;
}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});
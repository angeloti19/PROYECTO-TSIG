import axios from 'axios'
import { store } from '@/store'

axios.defaults.baseURL = 'http://localhost:8090';

// Add a request interceptor
axios.interceptors.request.use(
    function (config) {
        // Check if the user type is 'admin'
        if (store.tipoUsuario === 'admin') {
            // Get the token from local storage
            const token = localStorage.getItem('token');
            if (token) {
                // Add the token to the headers
                config.headers['Authorization'] = 'Bearer ' + token;
            }
            console.log("Request de admin -> ", store.tipoUsuario);
        }
        else
        {
            console.log("Request de usuario -> ", store.tipoUsuario)
        }
        
        return config;
    }, 
    function (error) {
        // Do something with request error
        return Promise.reject(error);
    }
);
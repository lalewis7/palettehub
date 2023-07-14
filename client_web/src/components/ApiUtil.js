// host url
const API_HOST = process.env.REACT_APP_API_HOST;

export default {
    auth: function authFn(data){
        return fetch(API_HOST+"/auth", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
    },
    selfProfile: function selfProfileFn(token){
        return fetch(API_HOST+"/users/self", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            }
        })
    }
}
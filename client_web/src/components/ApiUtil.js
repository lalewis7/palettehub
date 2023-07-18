// host url
const API_HOST = process.env.REACT_APP_API_HOST;

function getAuthHeaders(token, otherHeaders = {}){
    return token ? {
        headers: {
            ...otherHeaders,
            "Authorization": "Bearer " + token
        }
    } : ""
}

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
            ...getAuthHeaders(token)
        })
    },
    newPalettes: function newPalettesFn(token, page){
        return fetch(API_HOST+"/palettes?" + new URLSearchParams({sort: "new", page: page}), {
            method: "GET",
            ...getAuthHeaders(token)
        })
    },
    popularPalettes: function popularPalettesFn(token, page){
        return fetch(API_HOST+"/palettes?" + new URLSearchParams({sort: "popular", page: page}), {
            method: "GET",
            ...getAuthHeaders(token)
        })
    },
    likePalette: function likePaletteFn(token, id){
        return fetch(API_HOST+"/palettes/"+id+"/like", {
            method: "POST",
            ...getAuthHeaders(token)
        })
    },
    unlikePalette: function unlikePaletteFn(token, id){
        return fetch(API_HOST+"/palettes/"+id+"/like", {
            method: "DELETE",
            ...getAuthHeaders(token)
        })
    },
    postPalette: function postPaletteFn(token, data){
        return fetch(API_HOST+"/palettes", {
            method: "POST",
            ...getAuthHeaders(token, {'Content-Type': 'application/json'}),
            body: JSON.stringify(data)
        })
    }
}
// host url
export const API_HOST = process.env.REACT_APP_API_HOST;

function getAuthHeaders(token, otherHeaders = {}){
    return token ? {
        headers: {
            ...otherHeaders,
            "Authorization": "Bearer " + token
        }
    } : ""
}

export function checkResponse(resp){
    if (resp.ok)
        return resp
    else
        return Promise.reject({message: "Failed to fetch", code: resp.status})
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
    },
    getPalette: function getPaletteFn(token, id){
        return fetch(API_HOST+"/palettes/"+id, {
            method: "GET",
            ...getAuthHeaders(token)
        })
    },
    selfLikedPalettes: function selfLikedPalettesFn(token, page){
        return fetch(API_HOST+"/users/self/likes?" + new URLSearchParams({page: page}), {
            method: "GET",
            ...getAuthHeaders(token)
        })
    },
    getProfile: function getProfileFn(token, id){
        return fetch(API_HOST+"/users/"+id, {
            method: "GET",
            ...getAuthHeaders(token)
        })
    },
    getUserLikedPalettes: function getUserLikedPalettesFn(token, id, page){
        return fetch(API_HOST+"/users/"+id+"/likes?" + new URLSearchParams({page: page}), {
            method: "GET",
            ...getAuthHeaders(token)
        })
    },
    getUserPalettes: function getUserPalettesFn(token, id, page){
        return fetch(API_HOST+"/users/"+id+"/palettes?" + new URLSearchParams({page: page}), {
            method: "GET",
            ...getAuthHeaders(token)
        })
    },
    editUserProfile: function editUserProfileFn(token, id, data){
        return fetch(API_HOST+"/users/"+id, {
            method: "PUT",
            ...getAuthHeaders(token, {'Content-Type': 'application/json'}),
            body: JSON.stringify(data)
        })
    },
    deletePalette: function deletePaletteFn(token, id){
        return fetch(API_HOST+"/palettes/"+id, {
            method: "DELETE",
            ...getAuthHeaders(token)
        })
    },
    getUserCollections: function getUserCollectionsFn(token, id, page){
        return fetch(API_HOST+"/users/"+id+"/collections?" + new URLSearchParams({page: page}), {
            method: "GET",
            ...getAuthHeaders(token)
        })
    },
    getCollection: function getCollectionFn(token, id, page){
        return fetch(API_HOST+"/collections/"+id+"?" + new URLSearchParams({page: page}), {
            method: "GET",
            ...getAuthHeaders(token)
        })
    },
    editCollection: function editCollectionFn(token, id, data){
        return fetch(API_HOST+"/collections/"+id, {
            method: "PUT",
            ...getAuthHeaders(token, {'Content-Type': 'application/json'}),
            body: JSON.stringify(data)
        })
    },
    postCollection: function postCollectionFn(token, data){
        return fetch(API_HOST+"/collections", {
            method: "POST",
            ...getAuthHeaders(token, {'Content-Type': 'application/json'}),
            body: JSON.stringify(data)
        })
    },
    addToCollection: function addToCollectionFn(token, collection_id, palette_id){
        return fetch(API_HOST+"/collections/"+collection_id+"/palettes/"+palette_id, {
            method: "POST",
            ...getAuthHeaders(token)
        })
    },
    removeFromCollection: function removeFromCollectionFn(token, collection_id, palette_id){
        return fetch(API_HOST+"/collections/"+collection_id+"/palettes/"+palette_id, {
            method: "DELETE",
            ...getAuthHeaders(token)
        })
    },
    deleteCollection: function deleteCollectionFn(token, id){
        return fetch(API_HOST+"/collections/"+id, {
            method: "DELETE",
            ...getAuthHeaders(token)
        })
    }
}
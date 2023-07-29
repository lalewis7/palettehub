import { useGoogleLogin } from "@react-oauth/google";
import API from "../utils/API";

export default function LoginPrompt(updateToken, successCB = () => {}, failureCB = () => {}){
    return useGoogleLogin({
        onSuccess: response => API.auth(response)
            .then(apiResponse => apiResponse.text())
            .then(newToken => updateToken(newToken))
            .then(successCB),
        onError: failureCB,
        flow: 'auth-code'
    })
}
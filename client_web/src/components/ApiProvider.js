import React, { useContext } from 'react'
import { useToken } from "./TokenProvider";

const API_HOST = process.env.REACT_APP_API_HOST;

// @ts-ignore
const ApiContext = React.createContext()

export function useAPI(){
    return useContext(ApiContext);
}

export function ApiProvider({ children }){
    const token = useToken()
    const fns = {
        auth: async function(data){
            return await fetch(API_HOST+"/auth", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
        }
    }
    return <ApiContext.Provider value={fns}>
        {children}
    </ApiContext.Provider>
}
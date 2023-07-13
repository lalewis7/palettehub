import React, { useContext, useEffect, useState } from 'react'

const KEY = "token";

// @ts-ignore
const TokenContext = React.createContext()
// @ts-ignore
const TokenUpdateContext = React.createContext()

export function useToken(){
    return useContext(TokenContext)
}

export function useTokenUpdate(){
    return useContext(TokenUpdateContext)
}

function getSavedToken(){
    const savedToken = sessionStorage.getItem(KEY);
    if (savedToken) return savedToken;
}

export function TokenProvider({ children }){
    const [token, setToken] = useState(() => {
        return getSavedToken();
    });

    useEffect(() => {
        sessionStorage.setItem(KEY, token);
    }, [token])

    function updateToken(new_token){
        setToken(new_token)
    }

    return <TokenContext.Provider value={token}>
            <TokenUpdateContext.Provider value={updateToken}>
                {children}
            </TokenUpdateContext.Provider>
    </TokenContext.Provider>
}
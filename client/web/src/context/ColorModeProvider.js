import React, { useContext, useEffect, useState } from 'react'
import { Helmet } from 'react-helmet'

const KEY = "color_mode"

// @ts-ignore
const ColorModeContext = React.createContext()

// @ts-ignore
const ColorModeUpdateContext = React.createContext()

export function useColorMode(){
    return useContext(ColorModeContext)
}

export function useColorModeUpdate(){
    return useContext(ColorModeUpdateContext)
}

function getSavedColorMode(){
    const savedColorMode = localStorage.getItem(KEY)
    if (savedColorMode) return savedColorMode
    return window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches ? "dark" : "light"
}

function deleteSavedToken(){
    localStorage.removeItem(KEY);
}

export function ColorModeProvider({ children }){
    const [colorMode, setColorMode] = useState(() => {
        return getSavedColorMode()
    });

    useEffect(() => {
        if (colorMode) localStorage.setItem(KEY, colorMode)
    }, [colorMode])

    function updateColorMode(newColorMode){
        if (newColorMode)
            setColorMode(newColorMode)
        else{
            deleteSavedToken()
            setColorMode(null)
        }
    }

    return <ColorModeContext.Provider value={colorMode}>
            <ColorModeUpdateContext.Provider value={updateColorMode}>
                <Helmet htmlAttributes={{ "data-bs-theme": colorMode }} />
                {children}
            </ColorModeUpdateContext.Provider>
    </ColorModeContext.Provider>
}
import { useEffect, useReducer } from "react";
import { FeedPalette } from "./FeedPalette";
import { useToken } from "./TokenProvider";
import { Col, Container, Row } from "react-bootstrap";
import API from "./ApiUtil";
import { useLocation } from "react-router-dom";

export const ACTIONS = {
    SET_PALETTES: 'set-palettes',
    LIKE: 'like-palette',
    UNLIKE: 'unlike-palette'
}

function reducer(palettes, action){
    switch (action.type){
        case ACTIONS.SET_PALETTES:
            return action.palettes.map(pal => convertColorsToArray(pal))
        case ACTIONS.LIKE:
            let likePals = [...palettes]
            likePals.forEach(pal => {
                if (pal.palette_id === action.id && !pal.liked) {
                    pal.liked = true
                    pal.likes++
                    API.likePalette(action.token, action.id)
                        .catch(err => {
                            // TODO: REMOVE LIKE FROM UI AND SHOW ERROR MSG
                            console.log(err)
                        })
                }
            })
            return likePals
        case ACTIONS.UNLIKE:
            let unlikePals = [...palettes]
            unlikePals.forEach(pal => {
                if (pal.palette_id === action.id && pal.liked) {
                    pal.liked = false
                    pal.likes--
                    API.unlikePalette(action.token, action.id)
                        .catch(err => {
                            // TODO: REMOVE LIKE FROM UI AND SHOW ERROR MSG
                            console.log(err)
                        })
                }
            })
            return unlikePals
        default:
            return palettes
    }
}

function convertColorsToArray(palette){
    let newPalette = {...palette}
    delete newPalette.color_1
    delete newPalette.color_2
    delete newPalette.color_3
    delete newPalette.color_4
    delete newPalette.color_5
    newPalette.colors = [palette.color_1, palette.color_2, palette.color_3, palette.color_4, palette.color_5]
    return newPalette
}

export function Feed(){
    const token = useToken()
    const location = useLocation()

    const [palettes, dispatch] = useReducer(reducer, [])

    useEffect(() => {
        if (location.pathname === "/feed/new")
            API.newPalettes(token, 1)
                .then(palettes => palettes.json())
                // @ts-ignore
                .then(res => dispatch({type: ACTIONS.SET_PALETTES, palettes: res.palettes}))
        else if (location.pathname === "/feed/popular")
            API.popularPalettes(token, 1)
                .then(palettes => palettes.json())
                // @ts-ignore
                .then(res => dispatch({type: ACTIONS.SET_PALETTES, palettes: res.palettes}))
    }, [token, location])

    return <>
        <Container className="pt-3">
            <div id="feed-content">
                {palettes.map(palette => <FeedPalette key={palette.palette_id} id={palette.palette_id} colors={palette.colors} 
                    likes={palette.likes} liked={palette.liked} timestamp={palette.posted} dispatch={dispatch} />)}
            </div>
        </Container>
    </>
}
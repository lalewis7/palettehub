import { useReducer } from "react";
import { FeedPalette } from "./FeedPalette";
import { useToken } from "./TokenProvider";
import { Container } from "react-bootstrap";

const ACTIONS = {
    SET_PALETTES: 'set-palettes'
}

function reducer(palettes, action){
    switch (action.type){
        case ACTIONS.SET_PALETTES:
            return {...palettes}
        default:
            return palettes
    }
}

export function Feed(){
    const token = useToken()
    const [palettes, dispatch] = useReducer(reducer, [])


    return <>
        <Container className="pt-3">
            <FeedPalette colors={["B670CC", "54fe1b", "1febd5", "383fe5", "29ab6a"]} likes={23} timestamp={Date.now() - (1000*60*10)}/>
        </Container>
    </>
}
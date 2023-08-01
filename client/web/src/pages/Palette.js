import { useParams } from "react-router-dom"
import { useToken } from "../context/TokenProvider"
import { Button, Card, Col, Container, Overlay, OverlayTrigger, Ratio, Row, Tooltip } from "react-bootstrap"
import { useEffect, useReducer, useRef, useState } from "react"
import API from "../utils/API";
import { convertColorsToArray, getTimeElapsed } from "../utils/PaletteUtil";
import { pickTextColor } from "../utils/TextColorUtil";
import { Heart, HeartFill, Link45deg } from "react-bootstrap-icons";
import LikePopover from "../components/LikePopover";
import ErrorPage from "../components/ErrorPage";
import PalettePlaceholder from "../components/PalettePlaceholder";
import React from "react";
import { ColorCodeCopy } from "../components/ColorCodeCopy";
import { useColorMode } from "context/ColorModeProvider";

const ACTIONS = {
    SET_PALETTE: 'set-palette',
    LIKE: 'like-palette',
    UNLIKE: 'unlike-palette'
}

function reducer(pal, action){
    switch (action.type){
        case ACTIONS.SET_PALETTE:
            return convertColorsToArray(action.palette)
        case ACTIONS.LIKE:
            if (!pal.liked) {
                pal.liked = true
                pal.likes++
                API.likePalette(action.token, pal.palette_id)
                    .catch(err => {
                        // TODO: REMOVE LIKE FROM UI AND SHOW ERROR MSG
                        console.log(err)
                    })
                return {...pal, liked: true}
            }
            return pal
        case ACTIONS.UNLIKE:
            if (pal.liked) {
                pal.liked = false
                pal.likes--
                API.unlikePalette(action.token, pal.palette_id)
                    .catch(err => {
                        // TODO: REMOVE LIKE FROM UI AND SHOW ERROR MSG
                        console.log(err)
                    })
                return {...pal, liked: false}
            }
            return pal
        default:
            return pal
    }
}

export function Palette(){

    const copyLinkBtnContent = <><Link45deg size={24} />{' '}<span>Link</span></>

    const copyLinkBtnContentCopied = <span>Copied!</span>

    const token = useToken()
    const colorMode = useColorMode()
    const [loaded, setLoaded] = useState(false)
    const [error, setError] = useState(null)
    const [palette, dispatch] = useReducer(reducer, {})
    const [copyLink, setCopyLink] = useState(copyLinkBtnContent)
    const colorCodes = useRef([])
    let { id } = useParams()

    const getData = () => API.getPalette(token, id)
            .then(res => res.ok ? res : Promise.reject(res))
            .then(res => res.json())
            // @ts-ignore
            .then(resp => dispatch({type: ACTIONS.SET_PALETTE, palette: resp}))
            .then(() => setLoaded(true))
            .catch(err => {
                setError(err)
            })

    useEffect(() => {
        getData()
    }, [id, token])

    if (error)
        return <ErrorPage code={error.status} msg={error.statusText} retry={() => {
            setError(null)
            getData()
        }}/>

    if (!loaded)
        return <Container className="pt-3">
            <PalettePlaceholder />
        </Container>

    const onLikeBtn = () => {
        if (token){
            if (palette.liked)
                // @ts-ignore
                dispatch({type: ACTIONS.UNLIKE, token: token})
            else
                // @ts-ignore
                dispatch({type: ACTIONS.LIKE, token: token})
        }
    }

    const likeBtn = 
        <Button variant={colorMode} className="palette-like-btn" onClick={onLikeBtn}>
            {palette.liked ? <HeartFill color={"red"} size={24} /> : <Heart size={24} />}
            <span className="palette-like-cnt">{palette.likes}</span>
        </Button>

    const onCopyLink = () =>{
        navigator.clipboard.writeText(window.location.href)
        setCopyLink(copyLinkBtnContentCopied)
        setTimeout(() => {
            setCopyLink(copyLinkBtnContent)
        }, 750)
    }

    return <Container className="pt-3">
            <Card id="palette-card">
                <Ratio aspectRatio="1x1">
                    <div id="palette-colors">
                        {loaded ? palette.colors.map((color, i) => 
                            <div key={i} className="palette-card-color" style={{backgroundColor: "#"+color}}>
                                <ColorCodeCopy color={color} />
                            </div>
                        ) : ''}
                    </div>
                </Ratio>
            </Card>
            <Row id="palette-bar-1">
                <Col xs sm="auto">
                    {token ? likeBtn : 
                        <LikePopover placement="top-start">
                            {likeBtn}
                        </LikePopover>
                    }
                </Col>
                <Col xs sm="auto">
                    <Button variant={colorMode} onClick={onCopyLink}>{copyLink}</Button>
                </Col>
                <Col></Col>
                <Col xs sm="auto" className="palette-elapsed-time">
                    <span className="text-secondary">{getTimeElapsed(palette.posted)} ago</span>
                </Col>
            </Row>
    </Container>
}
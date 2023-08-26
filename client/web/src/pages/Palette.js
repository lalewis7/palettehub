import { Link, useParams } from "react-router-dom"
import { useToken } from "../context/TokenProvider"
import { Button, Card, Col, Container, Dropdown, Overlay, OverlayTrigger, Ratio, Row, Tooltip } from "react-bootstrap"
import { useEffect, useReducer, useRef, useState } from "react"
import API from "../utils/API";
import { convertColorsToArray, getTimeElapsed } from "../utils/PaletteUtil";
import { pickTextColor } from "../utils/TextColorUtil";
import { Folder2Open, Heart, HeartFill, Link45deg, ThreeDotsVertical, Trash } from "react-bootstrap-icons";
import LikePopover from "../components/LikePopover";
import ErrorPage from "../components/ErrorPage";
import PalettePlaceholder from "../components/PalettePlaceholder";
import React from "react";
import { ColorCodeCopy } from "../components/ColorCodeCopy";
import { useColorMode } from "context/ColorModeProvider";
import profile_img from '../assets/user-avatar.png';
import DeletePalette from "components/DeletePalette";
import AddToCollection from "components/AddToCollection";
import { useSelector } from "react-redux";

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

    const copyLinkBtnContent = <><Link45deg size={24}/>{' '}<span  className="ms-1">Link</span></>

    const copyLinkBtnContentCopied = <span>Copied!</span>

    const token = useToken()
    const colorMode = useColorMode()
    const [loaded, setLoaded] = useState(false)
    const [error, setError] = useState(null)
    const [palette, dispatch] = useReducer(reducer, {})
    const [copyLink, setCopyLink] = useState(copyLinkBtnContent)
    let { id } = useParams()
    // @ts-ignore
    const self = useSelector(state => state.user.value)

    // delete confirmation modal
    const [showDeleteDialog, setShowDeleteDialog] = useState(false)
    const handleCloseDeleteDialog = () => setShowDeleteDialog(false)
    const handleShowDeleteDialog = () => setShowDeleteDialog(true)

    // add to collection modal
    const [showAddToCollection, setShowAddToCollection] = useState(false)
    const handleCloseCollectionDialog = () => setShowAddToCollection(false)
    const handleShowCollectionDialog = () => setShowAddToCollection(true)

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

    const onDelete = () => {
        return API.deletePalette(token, id)
            .then(res => {
                if (res.ok){
                    handleCloseDeleteDialog()
                }
            })
            .catch((e) => console.log(e))
    }

    const onCopyLink = () =>{
        navigator.clipboard.writeText(window.location.href)
        setCopyLink(copyLinkBtnContentCopied)
        setTimeout(() => {
            setCopyLink(copyLinkBtnContent)
        }, 750)
    }

    return <>
        <Container className="pt-3">
            <Row id="palette-bar-top">
                <Col bsPrefix="col col" xs="auto" className="d-flex justify-content-between w-100">
                    <Link to={"/profile/"+palette.user_id} className="palette-user">
                        <img src={palette.user_img && palette.user_img !== "" ? palette.user_img : profile_img} referrerPolicy="no-referrer" className="palette-avatar" />
                        <span className="palette-user-name">{palette.user_name}</span>
                    </Link>
                    <Dropdown align="end" className="h-100">
                        <Dropdown.Toggle variant={colorMode} className="h-100 lh-1 palette-kebab-btn">
                            <ThreeDotsVertical size={18} />
                        </Dropdown.Toggle>
                        <Dropdown.Menu>
                        <Dropdown.Item onClick={() => navigator.clipboard.writeText(window.location.hostname+"/palettes/"+id)} className="d-flex align-items-center">
                                    <Link45deg className="me-2"/>Copy link
                                </Dropdown.Item>
                                {self ? 
                                <Dropdown.Item onClick={handleShowCollectionDialog} className="d-flex align-items-center">
                                    <Folder2Open className="me-2"/>Add to collection
                                </Dropdown.Item>
                                : ''}
                                {self && (self.user_id === palette.user_id || self.role === "admin") ? 
                                <Dropdown.Item onClick={handleShowDeleteDialog} className="d-flex align-items-center">
                                    <Trash className="me-2"/>Delete
                                </Dropdown.Item>
                                : ''}
                        </Dropdown.Menu>
                    </Dropdown>
                </Col>
            </Row>
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
                <Col bsPrefix="col col" xs="auto" className="me-2">
                    {token ? likeBtn : 
                        <LikePopover placement="top-start">
                            {likeBtn}
                        </LikePopover>
                    }
                </Col>
                <Col bsPrefix="col col" xs="auto">
                    <Button variant={colorMode} onClick={onCopyLink} className="d-flex">{copyLink}</Button>
                </Col>
                <Col></Col>
                <Col bsPrefix="col col" xs="auto" className="palette-elapsed-time">
                    <span className="text-secondary">{getTimeElapsed(palette.posted)}</span>
                </Col>
            </Row>
        </Container>
        <DeletePalette show={showDeleteDialog} handleClose={handleCloseDeleteDialog} handleOpen={handleShowDeleteDialog} onDelete={onDelete} />
        <AddToCollection show={showAddToCollection} handleClose={handleCloseCollectionDialog} handleOpen={handleShowCollectionDialog} id={id} />
    </>
}
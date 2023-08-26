import { Button, Card, Dropdown, Modal, Ratio, Spinner } from "react-bootstrap"
import { Folder2Open, FolderMinus, Heart, HeartFill, Link45deg, ThreeDotsVertical, Trash } from 'react-bootstrap-icons'
import { ACTIONS } from "./PaletteList"
import { useToken } from "../context/TokenProvider"
import LikePopover from "./LikePopover"
import { pickTextColorWhiteBlack, pickTextColorWhiteBlackAlpha } from "../utils/TextColorUtil"
import { Link } from "react-router-dom"
import { getTimeElapsed } from "../utils/PaletteUtil"
import { useColorMode } from "context/ColorModeProvider"
import profile_img from '../assets/user-avatar.png'
import { useSelector } from 'react-redux'
import API from '../utils/API'
import { useState } from "react"
import DeletePalette from "./DeletePalette"
import AddToCollection from "./AddToCollection"
import RemoveFromCollection from "./RemoveFromCollection"

export function FeedPalette(props){
    const token = useToken()
    const colorMode = useColorMode()

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

    // remove from collection modal
    const [showRemoveFromCollection, setRemoveFromCollection] = useState(false)
    const handleCloseRemoveCollectionDialog = () => setRemoveFromCollection(false)
    const handleShowRemoveCollectionDialog = () => setRemoveFromCollection(true)

    const onLikeBtn = () => {
        if (token){
            if (props.liked)
                props.dispatch({type: ACTIONS.UNLIKE, id: props.id, token: token})
            else
                props.dispatch({type: ACTIONS.LIKE, id: props.id, token: token})
        }
    }

    const onDelete = () => {
        return API.deletePalette(token, props.id)
            .then(res => {
                if (res.ok){
                    handleCloseDeleteDialog()
                    props.dispatch({type: ACTIONS.REMOVE, id: props.id})
                }
            })
            .catch((e) => console.log(e))
    }

    const onRemoveFromCollection = () => {
        return API.removeFromCollection(token, props.collection_id, props.id)
            .then(res => {
                if (res.ok){
                    props.dispatch({type: ACTIONS.REMOVE, id: props.id})
                }
            })
            .catch((e) => console.log(e))
    }

    const likeBtn = 
        <Button variant={colorMode} className="feed-palette-like" onClick={onLikeBtn} >
            {props.liked ? <HeartFill color={"red"} size={22} /> : <Heart size={20} />}
        </Button>

    return <>
        <div className="feed-palette-wrapper">
            <Card className="feed-palette">
                <Card.Header className="feed-palette-row feed-palette-top">
                    <Link to={"/profile/"+props.user_id} className="feed-palette-user">
                        <img src={props.user_img && props.user_img !== "" ? props.user_img : profile_img} referrerPolicy="no-referrer" className="feed-palette-avatar" />
                        <span className="feed-palette-user-name">{props.user_name}</span>
                    </Link>
                    <div className="feed-palette-kebab">
                        <Dropdown className="h-100" align="end">
                            <Dropdown.Toggle variant={colorMode} className="h-100 lh-1 feed-palette-kebab-btn">
                                <ThreeDotsVertical size={18} />
                            </Dropdown.Toggle>
                            <Dropdown.Menu className="feed-palette-kebab-menu">
                                <Dropdown.Item onClick={() => navigator.clipboard.writeText(window.location.hostname+"/palettes/"+props.id)} className="d-flex align-items-center">
                                    <Link45deg className="me-2"/>Copy link
                                </Dropdown.Item>
                                {self ? 
                                <Dropdown.Item onClick={handleShowCollectionDialog} className="d-flex align-items-center">
                                    <Folder2Open className="me-2"/>Add to collection
                                </Dropdown.Item>
                                : ''}
                                {self && props.collection_id ? 
                                <Dropdown.Item onClick={handleShowRemoveCollectionDialog} className="d-flex align-items-center">
                                    <FolderMinus className="me-2"/>Remove
                                </Dropdown.Item>
                                : ''}
                                {self && (self.user_id === props.user_id || self.role === "admin") ? 
                                <Dropdown.Item onClick={handleShowDeleteDialog} className="d-flex align-items-center">
                                    <Trash className="me-2"/>Delete
                                </Dropdown.Item>
                                : ''}
                            </Dropdown.Menu>
                        </Dropdown>
                    </div>
                </Card.Header>
                <Ratio aspectRatio={80}>
                    <div className="feed-palette-colors-grid">
                        {props.colors.map((color, i) => <Link key={i} className="feed-palette-color" 
                                style={{backgroundColor: "#"+color}} to={"/palettes/"+props.id}>
                            <span style={{color: pickTextColorWhiteBlack(color),  backgroundColor: pickTextColorWhiteBlackAlpha(color, 0.05)}} 
                                className="feed-palette-code">{"#"+color}</span>
                        </Link>)}
                    </div>
                </Ratio>
                <Card.Body className="feed-palette-row feed-palette-bottom">
                    <div className="feed-palette-likes">
                        {token ? likeBtn : 
                            <LikePopover>
                                {likeBtn}
                            </LikePopover>
                        }
                        <span className="ms-1">{props.likes}</span>
                    </div>
                    <small>{getTimeElapsed(props.timestamp)}</small>
                </Card.Body>
            </Card>
        </div>
        <DeletePalette show={showDeleteDialog} handleClose={handleCloseDeleteDialog} handleOpen={handleShowDeleteDialog} onDelete={onDelete} />
        <AddToCollection show={showAddToCollection} handleClose={handleCloseCollectionDialog} handleOpen={handleShowCollectionDialog} id={props.id} />
        <RemoveFromCollection show={showRemoveFromCollection} handleClose={handleCloseRemoveCollectionDialog} handleOpen={handleShowRemoveCollectionDialog}
            onRemove={onRemoveFromCollection} />
    </>
}
import { Card, Dropdown, Ratio } from "react-bootstrap";
import { Link } from "react-router-dom";
import profile_img from '../assets/user-avatar.png'
import { useColorMode } from "context/ColorModeProvider"
import { Folder, Link45deg, PaintBucket, ThreeDotsVertical, Trash } from "react-bootstrap-icons"
import { useSelector } from "react-redux"
import { ACTIONS } from './CollectionList'
import API from '../utils/API'
import { useToken } from "context/TokenProvider";
import { useState } from "react";
import DeleteCollection from "./DeleteCollection";

export default function FeedCollection(props){
    const token = useToken()
    // @ts-ignore
    const self = useSelector(state => state.user.value)
    const colorMode = useColorMode()

    // delete confirmation modal
    const [showDeleteDialog, setShowDeleteDialog] = useState(false)
    const handleCloseDeleteDialog = () => setShowDeleteDialog(false)
    const handleShowDeleteDialog = () => setShowDeleteDialog(true)

    const onDelete = () => {
        return API.deleteCollection(token, props.id)
            .then(res => {
                if (res.ok){
                    handleCloseDeleteDialog()
                    props.dispatch({type: ACTIONS.REMOVE_COLLECTION, id: props.id})
                }
            })
            .catch((e) => console.log(e))
    }

    return <>
        <Card>
            <Card.Header className="feed-collection-header">
                <Link to={"/profile/"+props.user_id} className="feed-collection-user">
                    <img src={props.user_img && props.user_img !== "" ? props.user_img : profile_img} referrerPolicy="no-referrer" className="feed-collection-avatar" />
                    <span className="feed-collection-user-name">{props.user_name}</span>
                </Link>
                <div className="feed-collection-kebab">
                    <Dropdown className="h-100" align="end">
                        <Dropdown.Toggle variant={colorMode} className="h-100 lh-1 feed-collection-kebab-btn">
                            <ThreeDotsVertical size={18} />
                        </Dropdown.Toggle>
                        <Dropdown.Menu className="feed-collection-kebab-menu">
                            <Dropdown.Item onClick={() => navigator.clipboard.writeText(window.location.hostname+"/collections/"+props.id)} className="d-flex align-items-center">
                                <Link45deg className="me-2"/> Copy link
                            </Dropdown.Item>
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
                <Link to={"/collections/"+props.id} className="text-reset text-decoration-none">
                    {props.palettes.length > 0 ? 
                    <div className="feed-collection-content">
                        {props.palettes.map((pal, i) => <div key={i} className="feed-collection-palette">
                            {pal.colors.map((color, j) => <div key={j} className="feed-collection-palette-color" style={{backgroundColor: "#"+color}}>

                            </div>)}
                        </div>)}
                    </div>
                    : 
                    <div className="d-flex justify-content-center align-items-center flex-column w-100 h-100 px-3">
                        <PaintBucket size={32} />
                        <h6 className="text-center">Looks like there are no palettes</h6>    
                    </div>}
                </Link>
            </Ratio>
            <Card.Footer className="feed-collection-footer">
                <div className="feed-collection-folder-wrapper me-2">
                    <Folder size={18} />
                </div>
                <span className="feed-collection-footer-name">{props.name}</span>
            </Card.Footer>
        </Card>
        <DeleteCollection show={showDeleteDialog} handleClose={handleCloseDeleteDialog} handleOpen={handleShowDeleteDialog} onDelete={onDelete} />
    </>
}
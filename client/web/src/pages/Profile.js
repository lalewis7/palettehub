import { Button, Card, Col, Container, Nav, Row, Tab, Tabs } from "react-bootstrap";
import { useToken } from "../context/TokenProvider";
import { Link, Navigate, Route, Routes, useLocation, useParams, useSearchParams } from "react-router-dom";
import { useEffect, useReducer, useRef, useState } from "react";
import API from "../utils/API";
import UserLikedPalettes from "components/UserLikedPalettes";
import UserPalettes from "components/UserPalettes";
import { Brush, Folder, FolderPlus, Heart, Palette, Pencil, Plus, PlusSquare } from "react-bootstrap-icons";
import { useColorMode } from "context/ColorModeProvider";
import { useSelector } from 'react-redux'
import EditProfile from "components/EditProfile";
import profile_img from '../assets/user-avatar.png';
import ErrorPage from "components/ErrorPage";
import UserCollections from "components/UserCollections";
import ProfilePlaceholder from "components/ProfilePlaceholder";

export const ACTIONS = {
    SET_USER: 'set-user'
}

export function reducer(user, action){
    switch(action.type){
        case ACTIONS.SET_USER:
            return action.user;
        default:
            return user;
    }
}

export default function Profile(){
    const token = useToken()
    const colorMode = useColorMode()
    const location = useLocation()

    // user stuff
    const [user, dispatch] = useReducer(reducer, {})
    const [loaded, setLoaded] = useState(false)
    const [error, setError] = useState(null)
    const [canEdit, setCanEdit] = useState(false)
    let { id } = useParams()

    // @ts-ignore
    const self = useSelector(state => state.user.value)

    // edit profile
    const [show, setShow] = useState(false)
    const handleClose = () => setShow(false)
    const handleShow = () => setShow(true)

    const editProfile = async (data) => {
        return API.editUserProfile(token, id, data)
            .then(() => loadProfile())
    }

    const loadProfile = () => {
        setError(null)
        setLoaded(false)
        API.getProfile(token, id)
            .then(user => user.json())
            .then(res => {
                // @ts-ignore
                dispatch({type: ACTIONS.SET_USER, user: res})
            })
            .then(() => setLoaded(true))
            .catch((err) => setError({code: "", msg:err.message, retry: loadProfile}))
    }

    useEffect(() => {
        loadProfile()
    }, [token, id])

    useEffect(() => {
        setCanEdit(self && token && (self.user_id === id || self.role === "admin"))
    }, [token, self, id])

    if (error)
        return <ErrorPage code={error.code} msg={error.msg} retry={error.retry} />

    return <>
        <div className="d-flex flex-column w-100">
            <Container className="mt-3">
                <Card className="profile-header bg-body-tertiary">
                    {loaded ? 
                    <>
                        <div className="profile-banner" style={{backgroundImage: "linear-gradient(45deg, #" + user.banner_color_left + ", #" + user.banner_color_right + ")"}}>
                            <div id="profile-avatar-bg" className="bg-body-tertiary">
                                <img className="profile-avatar-img" width="32" src={user.picture_url && user.picture_url !== "" ? user.picture_url : profile_img} referrerPolicy="no-referrer" />
                            </div>
                        </div>
                        <div className="px-4 pb-3 d-flex justify-content-between">
                            <div>
                                <h2 className="profile-name me-5 flex-grow-1">{user.name}</h2>
                                <span className="profile-stats-item-font">{user.palettes} Palettes &#8226; {user.likes} Likes &#8226; {user.liked} Liked</span>
                            </div>
                            <div className="mt-3">
                                {canEdit ? 
                                    <Button id="edit-profile-btn" variant={colorMode} onClick={handleShow}><Pencil size={22}/></Button>
                                : ''}
                            </div>
                        </div>
                        <Nav variant="pills" className="px-4 pb-3">
                            <Nav.Item>
                                <Nav.Link bsPrefix="d-flex gap-2 align-items-center nav-link" as={Link} to={"/profile/"+id+"/palettes"} 
                                        className={["palettes", ""].includes(location.pathname.split("/").slice(-1)[0]) || location.pathname.split("/").length === 3 ? "active" : ""}>
                                    <Brush className="d-none d-sm-block" size={18} />Palettes
                                </Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Nav.Link bsPrefix="d-flex gap-2 align-items-center nav-link" as={Link} to={"/profile/"+id+"/likes"} 
                                        className={location.pathname.split("/").slice(-1)[0] === "likes" ? "active" : ""}>
                                    <Heart className="d-none d-sm-block" size={18} />Liked
                                </Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Nav.Link bsPrefix="d-flex gap-2 align-items-center nav-link" as={Link} to={"/profile/"+id+"/collections"} 
                                        className={location.pathname.split("/").slice(-1)[0] === "collections" ? "active" : ""}>
                                    <Folder className="d-none d-sm-block" size={18} />Collections
                                </Nav.Link>
                            </Nav.Item>
                            {self && self.user_id === id ? <>
                                <Nav.Item className="d-none d-lg-block">
                                    <Nav.Link bsPrefix="d-flex gap-2 align-items-center nav-link" as={Link} to={"/palettes/new"}>
                                        <PlusSquare size={18} /> Create Palette
                                    </Nav.Link>
                                </Nav.Item>
                                <Nav.Item className="d-none d-lg-block">
                                    <Nav.Link bsPrefix="d-flex gap-2 align-items-center nav-link" as={Link} to={"/collections/new"}>
                                        <FolderPlus size={18} /> Create Collection
                                    </Nav.Link>
                                </Nav.Item>
                            </>
                            : ''}
                        </Nav>
                    </>
                    : <ProfilePlaceholder />}
                </Card>
            </Container>
            <Routes>
                <Route path="" element={<UserPalettes id={id} />} />
                <Route path="palettes" element={<UserPalettes id={id} />} />
                <Route path="likes" element={<UserLikedPalettes id={id} />} />
                <Route path="collections" element={<UserCollections id={id} />} />
            </Routes>
        </div>
        <EditProfile show={show} handleClose={handleClose} user={user} submit={editProfile} />
    </>
}
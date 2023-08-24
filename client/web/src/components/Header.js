import React, { useCallback, useEffect, useRef, useState } from "react";

import { Navbar, Container, Nav, Button, OverlayTrigger, Tooltip, Offcanvas, Placeholder } from "react-bootstrap";
import { Link, useLocation } from "react-router-dom";

// docs: https://github.com/MomenSherif/react-oauth
import { GoogleLogin } from '@react-oauth/google';
import API from "../utils/API";
import { useToken, useTokenUpdate } from "../context/TokenProvider";
import { HeaderUserDropdown } from "./HeaderUserDropdown";
import { EnvelopeAt, Fire, InfoCircle, Palette, Plus, Stars } from "react-bootstrap-icons";
import ResizeObserver from 'rc-resize-observer';

// import {ReactComponent as Logo} from '../assets/palette-8-svgrepo-com.svg';
import Logo from "./Logo";
import MobileHeaderOffcanvas from "./MobileHeaderOffcanvas";
import ColorModeButton from "./ColorModeButton";
import { useColorMode } from "context/ColorModeProvider";
import NewUserHelperOverlay from "./NewUserHelperOverlay";
import { useSelector, useDispatch } from 'react-redux'
import { setUser, removeUser } from '../redux/userSlice'

export function Header() {
    const updateToken = useTokenUpdate()
    const token = useToken()
    const location = useLocation()
    const colorMode = useColorMode()
    // @ts-ignore
    const self = useSelector(state => state.user.value)
    const dispatch = useDispatch()

    const [googleLoginWrapperRef, setGoogleLoginWrapperRef] = useState(null)
    const [googleLoginVisible, setGoogleLoginVisible] = useState(false)

    const [mobileGoogleLoginWrapperRef, setMobileGoogleLoginWrapperRef] = useState(null)

    const updateProfile = () => {
        API.selfProfile(token)
            .then(resp => resp.ok ? resp : Promise.reject(resp))
            .then(apiResponse => apiResponse.json())
            .then(selfData => dispatch(setUser(selfData)))
            .catch(err => {
                updateToken(null)
                dispatch(removeUser())
            })
    }

    const onLogin = (response) => {
        API.auth(response)
            .then(apiResponse => apiResponse.text())
            .then(newToken => updateToken(newToken))
    }

    const onLoginError = (error) => {
        console.log(error);
    }

    useEffect(() => {
        if (token) updateProfile()
    }, [token])

    const expandBreakpoint = "md";

    return (<>
      <Navbar id="header-navbar" expand={expandBreakpoint} collapseOnSelect className="bg-body-tertiary" sticky="top" >
        <Container>
            <Navbar.Brand as={Link} to="/" id="header-logo-brand">
                <Logo size={28} height={28} color="#249cf3" className="me-2" />
                <span className="lh-1">Palette Hub</span>
            </Navbar.Brand>
            <Navbar.Toggle aria-controls="headerOffcanvasContent" data-bs-target="#headerOffcanvasContent" 
                data-bs-toggle="offcanvas" ref={c => c && !mobileGoogleLoginWrapperRef && setMobileGoogleLoginWrapperRef(c)}/>
            {location.pathname.startsWith("/feed") || location.pathname.startsWith("/palettes") ? 
            <NewUserHelperOverlay show={token === null} target={mobileGoogleLoginWrapperRef} /> : ''}
            <Navbar.Collapse className={"d-none d-"+expandBreakpoint+"-block"}>
                <Nav className="me-auto">
                    <Nav.Link as={Link} to="/feed/popular" {...(location.pathname === "/feed/popular" ? {active: true} : {})}><Fire />{' '}Popular</Nav.Link>
                    <Nav.Link as={Link} to="/feed/new" {...(location.pathname === "/feed/new" ? {active: true} : {})}><Stars />{' '}New</Nav.Link>
                </Nav>
                <OverlayTrigger placement="bottom" overlay={
                    <Tooltip style={{position: 'fixed'}}>
                        About Page
                    </Tooltip>
                }>
                    <div className="header-color-mode-btn-wrapper">
                        <Link to="/about">
                            <Button variant={colorMode} className="header-circle-btn">
                                <InfoCircle size={20} />
                            </Button>
                        </Link>
                    </div>
                </OverlayTrigger>
                <OverlayTrigger placement="bottom" overlay={
                    <Tooltip style={{position: 'fixed'}}>
                        Contact Page
                    </Tooltip>
                }>
                    <div className="header-color-mode-btn-wrapper">
                        <Link to="/contact">
                            <Button variant={colorMode} className="header-circle-btn">
                                <EnvelopeAt size={20} />
                            </Button>
                        </Link>
                    </div>
                </OverlayTrigger>
                <OverlayTrigger placement="bottom" overlay={
                        <Tooltip style={{position: 'fixed'}}>
                            Switch to {colorMode === "light" ? "Dark" : "Light"} Mode
                        </Tooltip>
                    }>
                    <div className="header-color-mode-btn-wrapper">
                        <ColorModeButton btn_size="sm" size={20} />
                    </div>
                </OverlayTrigger>
                {token ? 
                    <>{self ? 
                        <>
                            <OverlayTrigger placement="bottom" overlay={<Tooltip style={{position: 'fixed'}}>Create Palette</Tooltip>}>
                                <Link to="/palettes/new" id="create-palette-btn-link">
                                    <Button id="create-palette-btn" variant={colorMode}>
                                        <Plus size={32} />
                                    </Button>
                                </Link>
                            </OverlayTrigger>
                            <HeaderUserDropdown pictureUrl={self.picture_url} name={self.name} email={self.email}/> 
                        </>
                        : <Placeholder className="user-avatar-placeholder"/>}</>
                    : 
                    <div id="header-anon-right">
                        <ResizeObserver onResize={(dim) => {
                                if (dim.width > 0 && !googleLoginVisible) setGoogleLoginVisible(true)
                            }}>
                            <div style={{colorScheme: 'auto'}} ref={c => c && !googleLoginWrapperRef && setGoogleLoginWrapperRef(c)}>
                                <GoogleLogin 
                                    onSuccess={onLogin} 
                                    onError={onLoginError} 
                                    type="standard"
                                    shape="rectangular"
                                    theme="outline"
                                    text="signin_with"
                                    size="large"
                                    logo_alignment="left"/>
                            </div>
                        </ResizeObserver>
                        {location.pathname.startsWith("/feed") || location.pathname.startsWith("/palettes") ? 
                        <NewUserHelperOverlay show={googleLoginVisible} target={googleLoginWrapperRef} /> : ''}
                    </div>
                }
            </Navbar.Collapse>
            <Navbar.Offcanvas id="headerOffcanvasContent" className={"d-block d-"+expandBreakpoint+"-none"} responsive={expandBreakpoint} placement="end">
                <MobileHeaderOffcanvas onLogin={onLogin} onLoginError={onLoginError} self={self} />
            </Navbar.Offcanvas>
        </Container>
      </Navbar>
    </>);
  }
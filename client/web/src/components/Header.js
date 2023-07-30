import React, { useEffect, useState } from "react";

import { Navbar, Container, Nav, Button, OverlayTrigger, Tooltip, Offcanvas } from "react-bootstrap";
import { Link, useLocation } from "react-router-dom";

// docs: https://github.com/MomenSherif/react-oauth
import { GoogleLogin } from '@react-oauth/google';
import API from "../utils/API";
import { useToken, useTokenUpdate } from "../context/TokenProvider";
import { HeaderUserDropdown } from "./HeaderUserDropdown";
import { Fire, Plus, Stars } from "react-bootstrap-icons";

import logo from '../assets/logo.svg';
import MobileHeaderOffcanvas from "./MobileHeaderOffcanvas";

export function Header() {
    const updateToken = useTokenUpdate()
    const token = useToken()
    const location = useLocation()
    const [self, setSelf] = useState(null)
    const [expanded, setExpanded] = useState(false)

    const updateProfile = () => {
        API.selfProfile(token)
            .then(resp => resp.ok ? resp : Promise.reject(resp))
            .then(apiResponse => apiResponse.json())
            .then(selfData => setSelf(selfData))
            .catch(err => {
                updateToken(null)
                setSelf(null)
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

    const expandBreakpoint = "sm";

    return (<>
      <Navbar expand={expandBreakpoint} collapseOnSelect className="bg-body-tertiary">
        <Container>
            <Navbar.Brand as={Link} to="/">
                {/* <img alt="" src={logo} width="40" height="40" /> */}
                PaletteHub
            </Navbar.Brand>
            {/* <button className="navbar-toggler" type="button" data-bs-toggle="offcanvas" onClick={() => handleShow()} >
                <span className="navbar-toggler-icon"></span>
            </button> */}
            <Navbar.Toggle aria-controls="headerOffcanvasContent" data-bs-target="#headerOffcanvasContent" 
                data-bs-toggle="offcanvas" />
            {/* <Navbar.Offcanvas placement="end">
                <Offcanvas.Header closeButton>
                    <Offcanvas.Title>
                        Palette Hub
                    </Offcanvas.Title>
                </Offcanvas.Header>
            </Navbar.Offcanvas> */}
            <Navbar.Collapse className={"d-none d-"+expandBreakpoint+"-block"}>
                <Nav className="me-auto">
                    <Nav.Link as={Link} to="/feed/new" {...(location.pathname === "/feed/new" ? {active: true} : {})}><Stars />{' '}New</Nav.Link>
                    <Nav.Link as={Link} to="/feed/popular" {...(location.pathname === "/feed/popular" ? {active: true} : {})}><Fire />{' '}Popular</Nav.Link>
                </Nav>
                {token ? 
                    <>{self ? 
                        <>
                            <OverlayTrigger placement="bottom" overlay={<Tooltip style={{position: 'fixed'}}>Create</Tooltip>}>
                                <Link to="/palettes/new" id="create-palette-btn-link">
                                    <Button id="create-palette-btn" variant="primary">
                                        <Plus size={32} />
                                    </Button>
                                </Link>
                            </OverlayTrigger>
                            <HeaderUserDropdown pictureUrl={self.picture_url} name={self.name} email={self.email}/> 
                        </>
                        : ""/* TODO: loading screen here */}</>
                    : 
                    <GoogleLogin 
                        onSuccess={onLogin} 
                        onError={onLoginError} 
                        type="standard"
                        shape="pill"
                        theme="outline"
                        text="signin_with"
                        size="large"
                        logo_alignment="left"/>
                }
            </Navbar.Collapse>
            <Navbar.Offcanvas id="headerOffcanvasContent" className={"d-block d-"+expandBreakpoint+"-none"} placement="end">
                <MobileHeaderOffcanvas onLogin={onLogin} onLoginError={onLoginError} setExpanded={setExpanded} self={self} />
            </Navbar.Offcanvas>
        </Container>
      </Navbar>
    </>);
  }
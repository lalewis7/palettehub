import React, { useEffect, useState } from "react";

import { Navbar, Container, Nav, Button, OverlayTrigger, Tooltip, Offcanvas } from "react-bootstrap";
import { Link, useLocation } from "react-router-dom";

// docs: https://github.com/MomenSherif/react-oauth
import { GoogleLogin } from '@react-oauth/google';
import API from "../utils/API";
import { useToken, useTokenUpdate } from "../context/TokenProvider";
import { HeaderUserDropdown } from "./HeaderUserDropdown";
import { Plus } from "react-bootstrap-icons";

import logo from '../assets/logo.svg';

export function Header() {
    const updateToken = useTokenUpdate()
    const token = useToken()
    const location = useLocation()
    const [self, setSelf] = useState(null)

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

    return (
      <Navbar expand="sm" className="bg-body-tertiary">
        <Container>
            <Navbar.Brand as={Link} to="/">
                {/* <img alt="" src={logo} width="40" height="40" /> */}
                PaletteHub
            </Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Offcanvas id="basic-navbar-nav" placement="end">
                <Offcanvas.Header closeButton>
                    <Offcanvas.Title>
                        Palette Hub
                    </Offcanvas.Title>
                </Offcanvas.Header>
                <Offcanvas.Body>
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/feed/new" {...(location.pathname === "/feed/new" ? {active: true} : {})}>New</Nav.Link>
                        <Nav.Link as={Link} to="/feed/popular" {...(location.pathname === "/feed/popular" ? {active: true} : {})}>Popular</Nav.Link>
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
                            : ""}</>
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
                </Offcanvas.Body>
            </Navbar.Offcanvas>
        </Container>
      </Navbar>
    );
  }
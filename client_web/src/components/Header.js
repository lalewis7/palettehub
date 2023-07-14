import React, { useEffect, useState } from "react";

import { Navbar, Container, Nav } from "react-bootstrap";
import { Link, useLocation } from "react-router-dom";

// docs: https://github.com/MomenSherif/react-oauth
import { GoogleLogin } from '@react-oauth/google';
import API from "./ApiUtil";
import { useToken, useTokenUpdate } from "./TokenProvider";
import { HeaderUserDropdown } from "./HeaderUserDropdown";

export function Header() {
    const updateToken = useTokenUpdate()
    const token = useToken()
    const location = useLocation()
    const [self, setSelf] = useState(null)

    const updateProfile = () => {
        API.selfProfile(token)
            .then(apiResponse => apiResponse.json())
            .then(selfData => setSelf(selfData))
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
      <Navbar expand="lg" className="bg-body-tertiary">
        <Container>
            <Navbar.Brand as={Link} to="/">PaletteHub</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                    <Nav.Link as={Link} to="/palettes/new" {...(location.pathname === "/palettes/new" ? {active: true} : {})}>New</Nav.Link>
                    <Nav.Link as={Link} to="/palettes/popular" {...(location.pathname === "/palettes/popular" ? {active: true} : {})}>Popular</Nav.Link>
                </Nav>
                {token ? 
                    <>{self ? <HeaderUserDropdown pictureUrl={self.picture_url} name={self.name} email={self.email}/> 
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
            </Navbar.Collapse>
        </Container>
      </Navbar>
    );
  }
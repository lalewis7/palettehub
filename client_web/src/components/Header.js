import { Navbar, Container, Nav } from "react-bootstrap";
import { Link } from "react-router-dom";

// docs: https://github.com/MomenSherif/react-oauth
import { GoogleLogin } from '@react-oauth/google';
import { useAPI } from "./ApiProvider";
import { useTokenUpdate } from "./TokenProvider";

export function Header() {
    const api = useAPI()
    const updateToken = useTokenUpdate()
    const onLogin = async (response) => {
        const apiResponse = await api.auth(response)
        updateToken(await apiResponse.text())
    };
    const onLoginError = (error) => {
        console.log(error);
    };
    return (
      <Navbar expand="lg" className="bg-body-tertiary">
        <Container>
            <Navbar.Brand as={Link} to="/">PaletteHub</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                    <Nav.Link as={Link} to="/palettes/new">New</Nav.Link>
                    <Nav.Link as={Link} to="/palettes/popular">Popular</Nav.Link>
                </Nav>
                <GoogleLogin 
                    onSuccess={onLogin} 
                    onError={onLoginError} 
                    type="standard"
                    shape="pill"
                    theme="outline"
                    text="signin_with"
                    size="large"
                    logo_alignment="left"/>
            </Navbar.Collapse>
        </Container>
      </Navbar>
    );
  }
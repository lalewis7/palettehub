import { GoogleLogin } from "@react-oauth/google";
import { useToken, useTokenUpdate } from "context/TokenProvider";
import { Nav, Offcanvas, Image, Row, Col } from "react-bootstrap";
import { Box2Heart, BoxArrowDownRight, BoxArrowRight, Fire, PlusSquare, Stars } from "react-bootstrap-icons";
import { Link, useLocation } from "react-router-dom";
import ColorModeButton from "./ColorModeButton";

export default function MobileHeaderOffcanvas(props){

    const location = useLocation()
    const token = useToken()
    const updateToken = useTokenUpdate()

    const logout = () => {
        updateToken(null)
    }

    return <>
        <Offcanvas.Header className="mobile-header bg-body-secondary" closeButton>
            {token ? <>
                {props.self ? <>
                    <div>
                        <Image className="mobile-header-img" src={props.self.picture_url} roundedCircle/>
                        <h3 className="mobile-header-name">{props.self.name}</h3>
                    </div>
                </> : ''}
            </> : 
            <div style={{colorScheme: 'auto'}}><GoogleLogin 
                onSuccess={props.onLogin} 
                onError={props.onLoginError} 
                type="standard"
                shape="rectangular"
                theme="outline"
                text="signin_with"
                size="large"
                logo_alignment="left"/></div>}
        </Offcanvas.Header>
        <Offcanvas.Body>
            <Nav as="nav" className="me-auto">
                <Nav.Link eventKey={1} className="mobile-header-navlink" as={Link} to="/feed/new" {...(location.pathname === "/feed/new" ? {active: true} : {})}>
                    <Row>
                        <Col className="mobile-header-navlink-icon" xs={2}>
                            <Stars size={28} />
                        </Col>
                        <Col className="mobile-header-navlink-text">
                            <h3 className="m-0">New</h3>
                        </Col>
                    </Row>
                </Nav.Link>
                <Nav.Link eventKey={2} className="mobile-header-navlink" as={Link} to="/feed/popular" {...(location.pathname === "/feed/popular" ? {active: true} : {})}>
                    <Row>
                        <Col className="mobile-header-navlink-icon" xs={2}>
                            <Fire size={28} />
                        </Col>
                        <Col className="mobile-header-navlink-text">
                            <h3 className="m-0">Popular</h3>
                        </Col>
                    </Row>
                </Nav.Link>
                {token ? <>
                <Nav.Link eventKey={3} className="mobile-header-navlink" as={Link} to="/palettes/new" {...(location.pathname === "/profile/likes" ? {active: true} : {})}>
                    <Row>
                        <Col className="mobile-header-navlink-icon" xs={2}>
                            <PlusSquare size={28} />
                        </Col>
                        <Col className="mobile-header-navlink-text">
                            <h3 className="m-0">Create Palette</h3>
                        </Col>
                    </Row>
                </Nav.Link>
                <Nav.Link eventKey={4} className="mobile-header-navlink" as={Link} to="/profile/likes" {...(location.pathname === "/profile/likes" ? {active: true} : {})}>
                    <Row>
                        <Col className="mobile-header-navlink-icon" xs={2}>
                            <Box2Heart size={28} />
                        </Col>
                        <Col className="mobile-header-navlink-text">
                            <h3 className="m-0">Your Likes</h3>
                        </Col>
                    </Row>
                </Nav.Link>
                <Nav.Link eventKey={5} className="mobile-header-navlink" onClick={logout}>
                    <Row>
                        <Col className="mobile-header-navlink-icon" xs={2}>
                            <BoxArrowRight size={28} />
                        </Col>
                        <Col className="mobile-header-navlink-text">
                            <h3 className="m-0">Logout</h3>
                        </Col>
                    </Row>
                </Nav.Link>
                </> : ''}
            </Nav>
            <div className="mobile-header-color-mode-wrapper">
                <ColorModeButton btn_size="lg" size={28} />
            </div>
        </Offcanvas.Body>
    </>
}
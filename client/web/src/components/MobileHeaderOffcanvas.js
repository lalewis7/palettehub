import { GoogleLogin } from "@react-oauth/google";
import { useToken } from "context/TokenProvider";
import { Nav, Offcanvas, Image } from "react-bootstrap";
import { Fire, Stars } from "react-bootstrap-icons";
import { Link, useLocation } from "react-router-dom";

export default function MobileHeaderOffcanvas(props){

    const location = useLocation()
    const token = useToken()

    return <>
        <Offcanvas.Header className="mobile-header" closeButton>
            {token ? <>
                {props.self ? <>
                    <div>
                        <Image className="mobile-header-img" src={props.self.picture_url} roundedCircle/>
                        <h3 className="mobile-header-name">{props.self.name}</h3>
                    </div>
                </> : ''}
            </> : 
            <GoogleLogin 
                onSuccess={props.onLogin} 
                onError={props.onLoginError} 
                type="standard"
                shape="pill"
                theme="outline"
                text="signin_with"
                size="large"
                logo_alignment="left"/>}
        </Offcanvas.Header>
        <Offcanvas.Body>
            <Nav className="me-auto">
                <Nav.Link eventKey={1} as={Link} to="/feed/new" {...(location.pathname === "/feed/new" ? {active: true} : {})}
                    onClick={() => props.setExpanded(false)}>
                    <h4><Stars />{' '}New</h4>
                </Nav.Link>
                <Nav.Link eventKey={2} as={Link} to="/feed/popular" {...(location.pathname === "/feed/popular" ? {active: true} : {})}>
                    <h4><Fire />{' '}Popular</h4>
                </Nav.Link>
            </Nav>
        </Offcanvas.Body>
    </>
}
import { useColorMode } from "context/ColorModeProvider";
import { Col, Container, Nav, Row } from "react-bootstrap";
import { Github, Linkedin } from "react-bootstrap-icons";
import { Link } from "react-router-dom";

export function Footer(){

    const colorMode = useColorMode()
    
    return <>
        <Container as="footer" className="mt-3">
            <Row>
                <Nav as="ul" className="justify-content-center">
                    <Nav.Item as="li">
                        <Nav.Link href={process.env.REACT_APP_GITHUB_LINK}><Github color={colorMode === "light" ? "black" : "white"} size={24}/></Nav.Link>
                    </Nav.Item>
                    <Nav.Item as="li">
                        <Nav.Link href={process.env.REACT_APP_LINKEDIN_LINK}><Linkedin color="#0077B5" size={24}/></Nav.Link>
                    </Nav.Item>
                </Nav>
            </Row>
            <Row>
                <Nav as="ul" className="justify-content-center mb-2">
                    <Nav.Item as="li">
                        <Nav.Link as={Link} to="/feed/new">New</Nav.Link>
                    </Nav.Item>
                    <Nav.Item as="li">
                        <Nav.Link as={Link} to="/feed/popular">Popular</Nav.Link>
                    </Nav.Item>
                    <Nav.Item as="li">
                        <Nav.Link as={Link} to="/about">About</Nav.Link>
                    </Nav.Item>
                    <Nav.Item as="li">
                        <Nav.Link as={Link} to="/contact">Contact</Nav.Link>
                    </Nav.Item>
                </Nav>
            </Row>
            
        </Container>
    </>
}
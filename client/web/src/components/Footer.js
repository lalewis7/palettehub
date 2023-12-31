import { useColorMode } from "context/ColorModeProvider";
import { Col, Container, Nav, Row } from "react-bootstrap";
import { Github, Linkedin } from "react-bootstrap-icons";
import { Link } from "react-router-dom";

export function Footer(){

    const colorMode = useColorMode()
    
    return <>
        <Container id="ph-footer" as="footer" className="py-3 bg-body-tertiary" fluid={true}>
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
                <Nav as="ul" className="justify-content-center mb-1">
                    <Nav.Item as="li">
                        <Nav.Link as={Link} to="/feed/new" className="text-reset">New</Nav.Link>
                    </Nav.Item>
                    <Nav.Item as="li">
                        <Nav.Link as={Link} to="/feed/popular" className="text-reset">Popular</Nav.Link>
                    </Nav.Item>
                    <Nav.Item as="li">
                        <Nav.Link as={Link} to="/about" className="text-reset">About</Nav.Link>
                    </Nav.Item>
                    <Nav.Item as="li">
                        <Nav.Link as={Link} to="/contact" className="text-reset">Contact</Nav.Link>
                    </Nav.Item>
                </Nav>
            </Row>
            <Row>
                <Col className="d-flex justify-content-center align-items-center gap-1">
                    &copy; <span>{new Date().getFullYear()} <a href="https://arthurlewis.net">Arthur Lewis</a></span>
                </Col>
            </Row>
            
        </Container>
    </>
}
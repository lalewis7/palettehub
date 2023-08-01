import { Button, Col, Container, Row } from "react-bootstrap";
import { ArrowRight, ArrowRightShort } from "react-bootstrap-icons";
import { Link } from "react-router-dom";

export default function LandingPage(){
    return <Container className="mt-3">
        <Row>
            <Col className="text-center">
            <h1 id="landing-page-header-1" className="display-3">Share and browse dozens of aesthetic color palettes</h1>
        <h5 className="text-secondary mt-3">Contribute to the growing community of artists and web developers 
            sharing handpicked color palettes.</h5>
        <Link to="/feed/popular"><Button className="mt-3" size="lg">Browse Now{' '}<ArrowRightShort size={26} /></Button></Link>
            </Col>
        </Row>
    </Container>
}
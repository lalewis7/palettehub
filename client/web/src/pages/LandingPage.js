import { Button, Col, Container, Row } from "react-bootstrap";
import { ArrowRight, ArrowRightShort } from "react-bootstrap-icons";
import { Link } from "react-router-dom";
import google_logo from '../assets/google-logo.png';

export default function LandingPage(){
    return <div className="d-flex flex-column w-100">
        <div id="landingpage-cont-1">
            <Container className="py-5 landingpage-mw">
                <Row>
                    <Col className="text-center">
                        <h1 className="display-1 text-white landing-page-header-1 mb-5">Join the color community</h1>
                        <h5 className="text-white landingpage-h landingpage-h1-text mb-4">Are you a web developer styling a website? An artist 
                        in need of inspiration for your next project? Or just a color enthusiast looking for fun? </h5>
                        <h5 className="text-white landingpage-h landingpage-h1-text mb-4">Palette Hub allows you to inspire your creativty 
                        and put it to use by browsing, curating, and sharing color palettes of any and all aesthetics.</h5>
                        {/* <h5 className="text-white landingpage-h">Have an idea or need inspiration for a color palette?
                        Browse dozens of our aesthetic color palettes or make your own and share them with your friends.</h5>
                        <h2 className="display-4 text-white landing-page-header-1 mt-5 mb-4">Join the color community</h2>
                        <h5 className="text-white landingpage-h mb-4">Contribute to the growing group of artists and web developers 
                        sharing handpicked color palettes.</h5> */}
                    <Link to="/feed/popular"><Button variant="outline-light" className="my-3" size="lg">Browse Now{' '}<ArrowRightShort size={26} /></Button></Link>
                    </Col>
                </Row>
            </Container>
        </div>
        <div>
            <Container className="py-5 landingpage-mw">
                <Row>
                    <Col xs={12} md={3} className="d-flex justify-content-center align-items-center mb-5 mb-md-0">
                        <img src={google_logo} width={100}/>
                    </Col>
                    <Col xs={12} md={9} className="d-flex flex-column justify-content-center text-center text-md-start">
                        <h3 className="landingpage-h">Becoming a user is simple and quick</h3>
                        <h5 className="text-secondary">Sign up with a Google account to post and like palettes.</h5>
                    </Col>
                </Row>
            </Container>
        </div>
    </div>
}
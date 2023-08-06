import { Button, Col, Container, Row } from "react-bootstrap";
import { BoxArrowUpRight } from "react-bootstrap-icons";

export function Contact(){
    return <Container className="mt-3">
        <Row>
            <Col>
                <h1 className="display-5 mb-3">Contact Me</h1>
                <p>Hi! My name is Arthur Lewis, and I am a recent graduate of Lehigh University looking for an entry 
                    level software engineer role. I’m interested in exploring how full stack websites work from the 
                    frontend user interface and experience to the backend business logic, database design, and 
                    infrastructure. I built this website to showcase my skills in those respects while learning new 
                    technologies. If you’re interested in chatting about Palette Hub or software engineering, connect 
                    with me on LinkedIn.</p>
                <Button as="a" href={process.env.REACT_APP_LINKEDIN_LINK} target="_blank" rel="noopener noreferrer" 
                    className="d-flex align-items-center contact-fit-content mb-3">
                        View LinkedIn<BoxArrowUpRight className="ms-2"/>
                </Button>
            </Col>
        </Row>
    </Container>
}
import HelmetTags from "components/HelmetTags";
import { Button, Col, Container, Row } from "react-bootstrap";
import { BoxArrowUpRight } from "react-bootstrap-icons";

export function Contact(){
    return <>
        <HelmetTags title="Contact - Palette Hub" />
        <Container className="mt-3">
            <Row>
                <Col>
                    <h1 className="display-5 mb-3">Contact Me</h1>
                    <p>Hi there! My name is <a href="https://arthurlewis.net">Arthur Lewis</a>, and I am a creative, dynamic, and meticulous full-stack 
                        software engineer with a passion for facilitating positive change through my work. I hold 
                        an integrated degree in computer science and business with a minor in data science from 
                        Lehigh University — however, my expertise and enthusiasm for technology extends far past 
                        my undergraduate education. I’ve been creating programs that entertain, educate, and, maybe, 
                        make life a little easier since I was 14 years old. My interest and expertise is in building 
                        full stack websites, from the frontend user interface and experience to the backend business logic, database design, and infrastructure.
                        I built this website to showcase my skills in those respects while learning new 
                        technologies. If you’re interested in chatting about Palette Hub or software engineering, connect 
                        with me on LinkedIn.</p>
                    <div className="d-flex gap-3">
                        <Button as="a" href={process.env.REACT_APP_LINKEDIN_LINK} target="_blank" rel="noopener noreferrer" 
                            className="d-flex align-items-center contact-fit-content mb-3">
                                View LinkedIn<BoxArrowUpRight className="ms-2"/>
                        </Button>
                        <Button as="a" href={"https://arthurlewis.net"} target="_blank" rel="noopener noreferrer" 
                            className="d-flex align-items-center contact-fit-content mb-3">
                                Portfolio Website<BoxArrowUpRight className="ms-2"/>
                        </Button>
                    </div>
                </Col>
            </Row>
        </Container>
    </>
}
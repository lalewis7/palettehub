import { Button, Col, Container, Row } from "react-bootstrap";
import { BoxArrowUpRight } from "react-bootstrap-icons";

export function About(){
    return <Container className="mt-3">
        <Row>
            <Col>
                <h1 className="display-5 mb-3">About Palette Hub</h1>
                <p>Palette Hub is a social media site for sharing palettes of hex code colors. Users can create, browse, 
                    and share palettes, as well as engage with them through “likes”. Palette Hub does not store user 
                    credentials on the site but instead uses Google’s third-party authentication service “Sign In 
                    with Google for Web.” Becoming a user — which unlocks all of the site’s available features — is 
                    simple and only requires a Google account to sign up.</p>
                <p>The purpose of Palette Hub is to showcase a full stack website including Database Management, 
                    Backend REST API, Frontend Website, and Container Orchestration. I diversified the stack to 
                    exhibit a wide range of skills. The stack includes a MySQL Database, Spring Boot REST API, 
                    Create-react-app website, Nginx web server, and Docker Compose container orchestration. Visit 
                    the repository to read more about the technical side and review the code.</p>
                <Button as="a" href={process.env.REACT_APP_GITHUB_LINK} target="_blank" rel="noopener noreferrer" 
                    className="d-flex align-items-center about-fit-content mb-3">
                        View Github<BoxArrowUpRight className="ms-2"/>
                </Button>
            </Col>
        </Row>
    </Container>
}
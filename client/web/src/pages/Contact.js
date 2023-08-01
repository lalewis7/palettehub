import { Button, Col, Container, Row } from "react-bootstrap";
import { BoxArrowUpRight } from "react-bootstrap-icons";

export function Contact(){
    return <Container className="mt-3">
        <Row>
            <Col>
                <h1 className="display-5 mb-3">Contact Me</h1>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus non lacus faucibus justo cursus placerat nec in mi. Sed vulputate risus ac gravida convallis. Nulla ac leo iaculis, vehicula lorem vel, suscipit justo. Vestibulum at tincidunt nunc, non tempus orci. Suspendisse sollicitudin, erat non tempor fringilla, leo dolor tincidunt sem, ut maximus massa massa nec sem. Donec euismod lectus ac turpis commodo, sit amet cursus orci convallis. Nullam at mattis nunc.</p>
                <p>Aliquam vel justo ac lectus varius dignissim. Integer venenatis aliquam lacinia. Sed fermentum enim a molestie fermentum. Proin eget nisl in odio luctus eleifend et at leo. Nam maximus erat et sem auctor pulvinar. Nam iaculis purus ante, ac egestas lacus feugiat eget. Morbi ut lacus sapien. Donec luctus urna justo, in fringilla augue accumsan vel.</p>
                <a href={process.env.REACT_APP_LINKEDIN_LINK} className="text-decoration-none" target="_blank" rel="noopener noreferrer">
                    <Button className="d-flex align-items-center">View LinkedIn<BoxArrowUpRight className="ms-2"/></Button>
                </a>
            </Col>
        </Row>
    </Container>
}
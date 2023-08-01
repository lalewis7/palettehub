import { Col, Container, Row } from "react-bootstrap";
import { PaintBucket } from "react-bootstrap-icons";

export default function EmptyPage(props){
    return <Container className="mt-3">
        <Row>
            <Col className="text-center">
                <PaintBucket size={42} />
                <h2 className="display-6">Nothing here yet?</h2>
                <p>{props.msg}</p>
            </Col>
        </Row>
    </Container>
}
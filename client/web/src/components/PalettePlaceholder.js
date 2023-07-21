import { Col, Placeholder } from "react-bootstrap"

export default function PalettePlaceholder(){
    return <div className="palette-placeholder">
        <Placeholder animation="glow" className="d-flex flex-column">
            <Placeholder xs={12} style={{height: '3.5rem', marginBottom: '0.25rem'}} />
            <Placeholder xs={12} style={{height: '3.5rem', marginBottom: '0.25rem'}} />
            <Placeholder xs={12} style={{height: '3.5rem', marginBottom: '0.25rem'}} />
            <Placeholder xs={12} style={{height: '3.5rem', marginBottom: '0.25rem'}} />
            <Placeholder xs={12} style={{height: '3.5rem', marginBottom: '0.25rem'}} />
            <div className="d-flex flex-row justify-content-between">
                <Col xs={4} className="d-flex flex-row">
                    <Placeholder xs={5} className="me-1" style={{height: '2rem', marginBottom: '0.25rem'}} />
                    <Placeholder xs={7} className="mx-1" style={{height: '2rem', marginBottom: '0.25rem'}} />
                </Col>
                <Placeholder xs={4} className="ms-1 justify-self-end" style={{height: '2rem', marginBottom: '0.25rem'}} />
            </div>
        </Placeholder>
    </div>
}
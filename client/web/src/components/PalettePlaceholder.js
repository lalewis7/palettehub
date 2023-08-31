import { Col, Placeholder } from "react-bootstrap"

export default function PalettePlaceholder(){
    return <div className="palette-placeholder">
        <Placeholder animation="glow" className="d-flex flex-column">
            <div className="d-flex flex-row justify-content-between">
                <div className="d-flex flex-grow-1">
                    <Placeholder style={{height: '30px', width: '30px', marginBottom: '0.5rem'}} className="rounded-circle" />
                    <Placeholder xs={6} style={{height: '30px', marginBottom: '0.5rem', marginLeft: '0.25rem'}} className="rounded" />
                </div>
                <Placeholder xs={2} style={{height: '30px', marginBottom: '0.5rem'}} className="rounded" />
            </div>
            <Placeholder xs={12} style={{height: '4.25rem', marginBottom: '0.25rem'}} className="rounded" />
            <Placeholder xs={12} style={{height: '4.25rem', marginBottom: '0.25rem'}} className="rounded" />
            <Placeholder xs={12} style={{height: '4.25rem', marginBottom: '0.25rem'}} className="rounded" />
            <Placeholder xs={12} style={{height: '4.25rem', marginBottom: '0.25rem'}} className="rounded" />
            <Placeholder xs={12} style={{height: '4.25rem', marginBottom: '0.5rem'}} className="rounded" />
            <div className="d-flex flex-row justify-content-between">
                <Col xs={4} className="d-flex flex-row">
                    <Placeholder xs={5} className="me-1 rounded" style={{height: '2rem', marginBottom: '0.25rem'}} />
                    <Placeholder xs={7} className="mx-1 rounded" style={{height: '2rem', marginBottom: '0.25rem'}} />
                </Col>
                <Placeholder xs={4} className="ms-1 justify-self-end rounded" style={{height: '2rem', marginBottom: '0.25rem'}} />
            </div>
        </Placeholder>
    </div>
}
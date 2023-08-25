import { Col, Placeholder } from "react-bootstrap"

export default function PalettePlaceholder(){
    return <>
        <Placeholder animation="glow" className="d-flex flex-column">
            <Placeholder xs={10} style={{height: '2.5rem', marginBottom: '0.5rem'}} />
            <Placeholder xs={3} style={{height: '1.5rem', marginBottom: '0.25rem'}} />
        </Placeholder>
    </>
}
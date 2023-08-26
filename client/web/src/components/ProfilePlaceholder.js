import { Col, Placeholder } from "react-bootstrap"

export default function PalettePlaceholder(){
    return <>
        <Placeholder animation="glow" className="d-flex flex-column p-4">
            <Placeholder style={{height:'80px', width: '80px', borderRadius: '100%', marginBottom: '1rem', marginTop: '1.75rem', marginLeft: '0.5rem'}} />
            <Placeholder xs={6} style={{height: '2rem', marginBottom: '0.75rem'}} />
            <Placeholder xs={3} style={{height: '1rem', marginBottom: '1rem'}} />
            <div className="d-flex">
                <Placeholder.Button variant="primary" style={{width: "125px", marginRight: '0.5rem'}} />
                <Placeholder.Button variant="primary" style={{width: "125px", marginRight: '0.5rem'}} />
                <Placeholder.Button variant="primary" style={{width: "125px", marginRight: '0.5rem'}} />
            </div>
        </Placeholder>
    </>
}
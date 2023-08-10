import { Card, Placeholder } from "react-bootstrap";

export default function FeedPalettePlaceholder(){
    const phStyle = {
        height: '2.25rem',
        marginBottom: '0.25rem'
    }
    const phBottomStyle = {
        height: '1.5rem', 
        marginBottom: '0.25rem'
    }
    return <div className="feed-palette-placeholder-wrapper">
        <div className="feed-palette-placeholder">
            <Placeholder animation="glow" className="d-flex flex-column">
                <Placeholder xs={12} style={phStyle} />
                <Placeholder xs={12} style={phStyle} />
                <Placeholder xs={12} style={phStyle} />
                <Placeholder xs={12} style={phStyle} />
                <Placeholder xs={12} style={phStyle} />
                <div className="d-flex flex-row justify-content-between">
                    <Placeholder xs={2} style={phBottomStyle} />
                    <Placeholder xs={4} style={phBottomStyle} />
                </div>
            </Placeholder>
        </div>
    </div>
}
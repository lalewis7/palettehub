import { Card, Placeholder } from "react-bootstrap";

export default function FeedPalettePlaceholder(){
    const phStyle = {
        height: '2.75rem',
        marginBottom: '0.25rem'
    }
    const phBottomStyle = {
        height: '30px', 
        marginBottom: '0.25rem'
    }
    return <div className="feed-palette-placeholder-wrapper">
        <div className="feed-palette-placeholder">
            <Placeholder animation="glow" className="d-flex flex-column">
                <div className="d-flex flex-row justify-content-between">
                    <div className="d-flex flex-grow-1">
                        <Placeholder style={{height: '30px', width: '30px', marginBottom: '0.25rem'}} className="rounded-circle" />
                        <Placeholder xs={6} style={{height: '30px', marginBottom: '0.25rem', marginLeft: '0.25rem'}} className="rounded" />
                    </div>
                    <Placeholder xs={2} style={{height: '30px', marginBottom: '0.25rem'}} className="rounded" />
                </div>
                <Placeholder xs={12} style={phStyle} className="rounded" />
                <Placeholder xs={12} style={phStyle} className="rounded" />
                <Placeholder xs={12} style={phStyle} className="rounded" />
                <Placeholder xs={12} style={phStyle} className="rounded" />
                <Placeholder xs={12} style={phStyle} className="rounded" />
                <div className="d-flex flex-row justify-content-between">
                    <Placeholder xs={3} style={phBottomStyle} className="rounded" />
                    <Placeholder xs={3} style={phBottomStyle} className="rounded" />
                </div>
            </Placeholder>
        </div>
    </div>
}
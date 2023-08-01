import { Card, Placeholder } from "react-bootstrap";

export default function FeedPalettePlaceholder(){
    return <div className="feed-palette-placeholder-wrapper">
        <div className="feed-palette-placeholder">
            <Placeholder animation="glow" className="d-flex flex-column">
                <Placeholder xs={12} style={{height: '1.75rem', marginBottom: '0.25rem'}} />
                <Placeholder xs={12} style={{height: '1.75rem', marginBottom: '0.25rem'}} />
                <Placeholder xs={12} style={{height: '1.75rem', marginBottom: '0.25rem'}} />
                <Placeholder xs={12} style={{height: '1.75rem', marginBottom: '0.25rem'}} />
                <Placeholder xs={12} style={{height: '1.75rem', marginBottom: '0.25rem'}} />
                <div className="d-flex flex-row justify-content-between">
                    <Placeholder xs={2} style={{height: '1.25rem', marginBottom: '0.25rem'}} />
                    <Placeholder xs={4} style={{height: '1.25rem', marginBottom: '0.25rem'}} />
                </div>
            </Placeholder>
        </div>
    </div>
}
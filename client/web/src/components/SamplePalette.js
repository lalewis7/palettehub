import { Card } from 'react-bootstrap';
import { Ratio } from 'react-bootstrap';
import { pickTextColorWhiteBlack, pickTextColorWhiteBlackAlpha } from '../utils/TextColorUtil';

export default function SamplePalette(props){
    const samplePaletteStyle = {
        width: props.width || "15rem",
        overflow: "hidden",
        marginLeft: '0.5rem',
        marginRight: '0.5rem',
        border: 'none'
    }
    const gridStyle = {
        display: 'grid',
        gridTemplateRows: 'repeat(5, 1fr)',
        gap: 0
    }
    return <>
        <Card style={samplePaletteStyle}>
            <Ratio aspectRatio={80}>
                <div style={gridStyle}>
                    {props.colors.map((color, i) => <div key={i} className="d-flex align-items-end justify-content-end" 
                            style={{backgroundColor: "#"+color, transition: "all 2s ease-out"}}>
                        <span style={{color: pickTextColorWhiteBlackAlpha(color, 0.75), backgroundColor: pickTextColorWhiteBlackAlpha(color, 0.05)}} 
                            className="feed-palette-code">{"#"+color}</span>
                    </div>)}
                </div>
            </Ratio>
        </Card>
    </>
}
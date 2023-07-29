import { Overlay, Tooltip } from "react-bootstrap";
import { pickTextColor } from "../utils/TextColorUtil";
import { useRef, useState } from "react";

export function ColorCodeCopy(props){
    const [show, setShow] = useState(false);
    const target = useRef(null);
    return <>
        <span ref={target} style={{color: pickTextColor(props.color, "#FFFFFF", "#000000")}} 
                className="palette-code" onClick={(e) => {
                    setShow(true)
                    navigator.clipboard.writeText("#"+props.color)
                    setTimeout(() => {
                        setShow(false)
                    }, 1000)
                }}>{"#"+props.color}</span>
        <Overlay target={target.current} show={show}>
            <Tooltip style={{position: 'fixed'}} >Copied!</Tooltip>
        </Overlay>
    </>
}
import { useEffect, useRef, useState } from "react";
import { Button, OverlayTrigger, Popover } from "react-bootstrap";
import { Pencil, PencilSquare } from "react-bootstrap-icons";
import { ChromePicker } from "react-color";
import { hexToRGB, pickTextColor } from "../utils/TextColorUtil";
import { useColorMode } from "context/ColorModeProvider";

export default function NewPaletteColor(props){
    const [color, setColor] = useState("#FFFFFF")
    const colorSet = useRef(false)
    const colorMode = useColorMode()
    
    const onColorChange = (color) => {
        setColor(color.hex)
        colorSet.current = true
        props.onColorChange(color.hex)
    }
    
    const rowStyle = {
        backgroundColor: colorSet.current ? color : hexToRGB(color, "0")
    }

    useEffect(() => {
        if (!colorSet.current && props.color) setColor(props.color)
    }, [props.color])
    
    return <OverlayTrigger
            trigger="click"
            placement="auto-end"
            rootClose={true}
            overlay={
                <Popover>
                    <Popover.Body>
                        <div style={{colorScheme: 'auto', marginLeft: '0.5rem'}}>
                            <ChromePicker color={color} onChange={onColorChange} disableAlpha={true} styles={{}}/>
                        </div>
                    </Popover.Body>
                </Popover>
            }
        >
            <div className="new-palette-color-row" style={rowStyle}>
                <div className="new-palette-color-code" >
                    <h6 style={{margin: 0, color: pickTextColor(colorSet.current ? color : colorMode === "light" ? "#FFFFFF" : "#000000", "rgba(255,255,255,0.5)", "rgba(0,0,0,0.5)")}}>
                        {colorSet.current ? color : "Click here to select a color"}
                    </h6>
                </div>
                    <Pencil className="new-palette-color-edit" color={pickTextColor(colorSet.current ? color : colorMode === "light" ? "#FFFFFF" : "#000000", "rgba(255,255,255,0.5)", "rgba(0,0,0,0.5)")} size={24} />
            </div>
        </OverlayTrigger>
}
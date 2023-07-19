import { useEffect, useState } from "react";
import { Button, OverlayTrigger, Popover } from "react-bootstrap";
import { PencilSquare } from "react-bootstrap-icons";
import { ChromePicker } from "react-color";
import { pickTextColor } from "./TextColorUtil";

export default function NewPaletteColor(props){
    const [color, setColor] = useState("#FFFFFF")
    useEffect(() => {
        props.onColorChange(color)
    }, [])
    const onColorChange = (color) => {
        setColor(color.hex)
        props.onColorChange(color.hex)
    }
    return <div className="new-palette-color-row" style={{backgroundColor:color}}>
        <div className="new-palette-color-code">
            <h5 style={{margin: 0, color: pickTextColor(color, "#FFFFFF", "#000000")}}>{color}</h5>
        </div>
        <OverlayTrigger
            trigger="click"
            placement="auto-end"
            rootClose={true}
            overlay={
                <Popover>
                    <Popover.Body>
                        <ChromePicker color={color} onChange={onColorChange} />
                    </Popover.Body>
                </Popover>
            }
        >
            <Button className="new-palette-color-edit" variant={"outline-"+pickTextColor(color, "light", "dark")}>
                <PencilSquare size={24} />
            </Button>
        </OverlayTrigger>
    </div>
}
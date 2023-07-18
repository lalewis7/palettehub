import { useEffect, useState } from "react";
import { Button, OverlayTrigger, Popover } from "react-bootstrap";
import { PencilSquare } from "react-bootstrap-icons";
import { ChromePicker } from "react-color";

// https://stackoverflow.com/questions/3942878/how-to-decide-font-color-in-white-or-black-depending-on-background-color
function pickTextColor(bgColor, lightColor, darkColor) {
    var color = (bgColor.charAt(0) === '#') ? bgColor.substring(1, 7) : bgColor;
    var r = parseInt(color.substring(0, 2), 16); // hexToR
    var g = parseInt(color.substring(2, 4), 16); // hexToG
    var b = parseInt(color.substring(4, 6), 16); // hexToB
    var uicolors = [r / 255, g / 255, b / 255];
    var c = uicolors.map((col) => {
      if (col <= 0.03928) {
        return col / 12.92;
      }
      return Math.pow((col + 0.055) / 1.055, 2.4);
    });
    var L = (0.2126 * c[0]) + (0.7152 * c[1]) + (0.0722 * c[2]);
    return (L > 0.179) ? darkColor : lightColor;
  }

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
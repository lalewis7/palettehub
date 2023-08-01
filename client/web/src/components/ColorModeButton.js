import { useColorMode, useColorModeUpdate } from "context/ColorModeProvider";
import { Button } from "react-bootstrap";
import { Moon, Sun } from "react-bootstrap-icons";

export default function ColorModeButton(props){
    const colorMode = useColorMode()
    const updateColorMode = useColorModeUpdate()

    return <Button className={"color-mode-btn color-mode-btn-" + (props.btn_size ? props.btn_size : "lg")} size={props.size ? props.size : ''} 
                variant={colorMode} onClick={() => {updateColorMode(colorMode === "light" ? "dark" : "light")}}>
        {colorMode === "light" ? <Moon size={props.size ? props.size : 28} /> : <Sun size={props.size ? props.size : 28} />}
    </Button>
}
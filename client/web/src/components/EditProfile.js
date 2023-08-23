import { useEffect, useState } from "react"
import { Button, Col, Form, Modal, OverlayTrigger, Popover, Row } from "react-bootstrap"
import NewPaletteColor from "./NewPaletteColor"
import { ChromePicker } from "react-color"
import { pickTextColor } from "utils/TextColorUtil"

export default function EditProfile(props){
    const [validated, setValidated] = useState(false)
    const [colorLeft, setColorLeft] = useState("#"+props.user.banner_color_left)
    const [colorRight, setColorRight] = useState("#"+props.user.banner_color_right)
    const [name, setName] = useState(props.user.name)
    const [nameInvalid, setNameInvalid] = useState(false)
    const [show, setShow] = useState(props.user.picture_visible)
    const [edited, setEdited] = useState(false)
    const [loading, setLoading] = useState(false)
    const [color, setColor] = useState("left")

    const resetValues = () => {
        setColorLeft("#"+props.user.banner_color_left)
        setColorRight("#"+props.user.banner_color_right)
        setName(props.user.name)
        setShow(props.user.picture_visible)
    }

    useEffect(() => {
        resetValues()
    }, [props.user])

    useEffect(() => {
        setEdited(colorLeft !== "#"+props.user.banner_color_left || colorRight !== "#"+props.user.banner_color_right || name !== props.user.name || show !== props.user.picture_visible)
    }, [colorLeft, colorRight, name, show])

    const handleClose = () => {
        setNameInvalid(false)
        setValidated(false)
        resetValues()
        props.handleClose()
    }

    const handleSubmit = async event => {
        // prevent default behavior
        event.preventDefault()
        event.stopPropagation()
        setLoading(true)

        let newNameInvalid = nameInvalid

        // reset
        setNameInvalid(false)

        // check
        if (name.length === 0 || name.length > 64)
            newNameInvalid = true

        const form = event.currentTarget
        if (form.checkValidity() && !newNameInvalid){
            await props.submit({name: name, banner_color_left: colorLeft.substring(1), banner_color_right: colorRight.substring(1), picture_visible: show})
            handleClose()
        } else {
            //setValidated(true)
        }
        setLoading(false)
        setNameInvalid(newNameInvalid)
    }

    return <Modal show={props.show} onHide={handleClose} fullscreen="sm-down" scrollable={true} animation={false}>
        <Modal.Header closeButton>
            <Modal.Title>Edit Profile</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Form noValidate validated={validated} onSubmit={handleSubmit}>
                <Form.Group>
                    <Form.Label>Banner</Form.Label>
                    <div id="edit-profile-banner-demo" style={{backgroundImage: "linear-gradient(45deg, "+colorLeft+", "+colorRight+")"}}>
                        <OverlayTrigger
                            trigger="click"
                            placement="auto-end"
                            rootClose={true}
                            overlay={
                                <Popover>
                                    <Popover.Body>
                                        <div style={{colorScheme: 'auto'}}>
                                            <ChromePicker color={colorLeft} onChangeComplete={color => setColorLeft(color.hex)} disableAlpha={true} styles={{}}/>
                                        </div>
                                    </Popover.Body>
                                </Popover>
                            }
                        ><span style={{margin: 0, color: pickTextColor(colorLeft, "rgba(255,255,255,0.75)", "rgba(0,0,0,0.75)")}}>{colorLeft}</span>
                        </OverlayTrigger>
                        <OverlayTrigger
                            trigger="click"
                            placement="auto-end"
                            rootClose={true}
                            overlay={
                                <Popover>
                                    <Popover.Body>
                                        <div style={{colorScheme: 'auto'}}>
                                        <ChromePicker color={colorRight} onChange={color => setColorRight(color.hex)} disableAlpha={true} styles={{}}/>
                                        </div>
                                    </Popover.Body>
                                </Popover>
                            }
                        ><span id="edit-profile-color-right" style={{margin: 0, color: pickTextColor(colorRight, "rgba(255,255,255,0.75)", "rgba(0,0,0,0.75)")}}>{colorRight}</span>
                        </OverlayTrigger>
                    </div>
                </Form.Group>
                <Form.Group className="mt-3">
                    <Form.Select value={color} onChange={e => setColor(e.target.value)}>
                        <option value="left">Left Color</option>
                        <option value="right">Right Color</option>
                    </Form.Select>
                    <div style={{colorScheme: 'auto'}} className="mt-3">
                        <ChromePicker color={color === "left" ? colorLeft : colorRight} onChangeComplete={newColor => color === "left" ? setColorLeft(newColor.hex) : setColorRight(newColor.hex)} disableAlpha={true} styles={{}}/>
                    </div>
                </Form.Group>
                <Form.Group className="mt-3">
                    <Form.Label>Name</Form.Label>
                    <Form.Control type="text" name="uname" value={name} onChange={evt => {
                        setNameInvalid(false)
                        setName(evt.target.value)
                    }} 
                        placeholder="Enter your name here..." required isInvalid={nameInvalid}></Form.Control>
                    <Form.Control.Feedback type="invalid">Name must be between 1-64 characters.</Form.Control.Feedback>
                </Form.Group>
                <Form.Group className="mt-3">
                    <Form.Check type="checkbox" name="show_avatar" defaultChecked={show} onChange={e => setShow(e.target.checked)} 
                        label="Show profile picture"></Form.Check>
                </Form.Group>
            </Form>
        </Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
                Close
            </Button>
            <Button variant="primary" onClick={handleSubmit} disabled={!edited}>
                {loading ? "Loading..." : "Save Changes"}
            </Button>
        </Modal.Footer>
    </Modal>
}
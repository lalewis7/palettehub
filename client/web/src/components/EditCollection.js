import { useEffect, useState } from "react"
import { Button, Form, Modal } from "react-bootstrap"
import { Trash } from "react-bootstrap-icons"

export default function EditCollection(props){
    // values
    const [name, setName] = useState(props.collection.name)

    // validation
    const [nameInvalid, setNameInvalid] = useState(false)

    // changes
    const [edited, setEdited] = useState(false)

    // loading api
    const [loading, setLoading] = useState(false)

    const resetValues = () => {
        setName(props.collection.name)
    }

    useEffect(() => {
        resetValues()
    }, [props.collection])

    useEffect(() => {
        setEdited(name !== props.collection.name)
    }, [name])

    const handleClose = () => {
        setNameInvalid(false)
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
            await props.submit({name: name})
            handleClose()
        } 
        setLoading(false)
        setNameInvalid(newNameInvalid)
    }

    return <Modal show={props.show} onHide={handleClose} fullscreen="sm-down" scrollable={true} animation={false}>
        <Modal.Header closeButton>
            <Modal.Title>Edit Collection</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Form noValidate onSubmit={handleSubmit}>
                <Form.Group>
                    <Form.Label>Name</Form.Label>
                    <Form.Control type="text" name="collection-name" value={name} onChange={evt => {
                        setNameInvalid(false)
                        setName(evt.target.value)
                    }} 
                        placeholder="Enter name here..." required isInvalid={nameInvalid}></Form.Control>
                    <Form.Control.Feedback type="invalid">Name must be between 1-64 characters.</Form.Control.Feedback>    
                </Form.Group>
            </Form>
        </Modal.Body>
        <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
                Close
            </Button>
            <Button variant="primary" onClick={handleSubmit} disabled={!edited} className="ms-2">
                {loading ? "Loading..." : "Save Changes"}
            </Button>
        </Modal.Footer>
    </Modal>
}
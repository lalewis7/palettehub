import { useState } from "react";
import { Button, Modal, Spinner } from "react-bootstrap";
import { Trash } from "react-bootstrap-icons";

export default function RemoveFromCollection(props){
    // delete confirmation modal
    const [removing, setRemoving] = useState(false)

    const onDelete = async () => {
        setRemoving(true)
        await props.onRemove()
        setRemoving(false)
        props.handleClose()
    }

    return <>
        <Modal show={props.show} onHide={props.handleClose} animation={false}>
            <Modal.Header closeButton>
                <Modal.Title>Remove this palette?</Modal.Title>
            </Modal.Header>
            <Modal.Body>Are you sure you want to remove this palette? You can add it back to this collection later.</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={props.handleClose}>
                    Close
                </Button>
                <Button variant="danger" onClick={onDelete} disabled={removing}>
                    {removing ? <>
                        <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> Loading...
                    </> : <span className="d-flex align-items-center"><Trash className="me-1" />Remove</span>}
                </Button>
            </Modal.Footer>
        </Modal>
    </>
}
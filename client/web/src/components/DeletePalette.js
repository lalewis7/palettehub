import { useState } from "react";
import { Button, Modal, Spinner } from "react-bootstrap";
import { Trash } from "react-bootstrap-icons";

export default function DeletePalette(props){
    // delete confirmation modal
    const [deleting, setDeleting] = useState(false)

    const onDelete = async () => {
        setDeleting(true)
        await props.onDelete()
        setDeleting(false)
    }

    return <>
        <Modal show={props.show} onHide={props.handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Delete this palette?</Modal.Title>
            </Modal.Header>
            <Modal.Body>Are you sure you want to permanetly delete this palette? This action cannot be reversed.</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={props.handleClose}>
                    Close
                </Button>
                <Button variant="danger" onClick={onDelete} disabled={deleting}>
                    {deleting ? <>
                        <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> Loading...
                    </> : <span className="d-flex align-items-center"><Trash className="me-1" />Delete</span>}
                </Button>
            </Modal.Footer>
        </Modal>
    </>
}
import { useToken } from "context/TokenProvider"
import { useState } from "react"
import { Navigate, useNavigate } from "react-router-dom"
import API from '../utils/API'
import { Button, Col, Container, Form, Row, Spinner } from "react-bootstrap"

export default function NewCollection(){
    const token = useToken()
    const navigateTo = useNavigate()
    // values
    const [name, setName] = useState("")

    // validation
    const [nameInvalid, setNameInvalid] = useState(false)

    // loading api
    const [loading, setLoading] = useState(false)

    // not logged in
    if (!token) return <Navigate to="/" />

    const post = () => {
        setLoading(true)
        setNameInvalid(false)
        // data validation
        if (name.length === 0 || name.length > 64){
            setNameInvalid(true)
            setLoading(false)
            return
        }
        // api call
        API.postCollection(token, {
            name: name
        }).then((res) => res.text())
        .then(id => {navigateTo("/collections/"+id)})
    }

    return <Container className="mt-3"> 
        <Row className="mb-2">
            <Col>
                <h4 className="text-center">New Collection</h4>
            </Col>
        </Row>
        <Row className="new-collection-row m-auto">
            <Col className="p-0">
                <Form noValidate onSubmit={post}>
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
            </Col>
        </Row>
        <Row className="new-collection-row justify-content-end m-auto mt-3">
            <Col className="p-0">
                <Button style={{float: 'right'}} onClick={post} disabled={loading}>
                    {loading ? <><Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> Loading...</> : "Create Collection"}
                </Button>
            </Col>
        </Row>
    </Container>
}
import { Button, Col, Container, Row, Spinner } from "react-bootstrap";
import NewPaletteColor from "./NewPaletteColor";
import { useToken } from "./TokenProvider";
import { Navigate, useNavigate } from "react-router-dom";
import API from './API';
import { useRef, useState } from "react";

export function NewPalette(){
    const token = useToken()
    const navigateTo = useNavigate()
    const color_1 = useRef("")
    const color_2 = useRef("")
    const color_3 = useRef("")
    const color_4 = useRef("")
    const color_5 = useRef("")

    const [loading, setLoading] = useState(false)

    const getColorRef = i => {
        switch (i){
            case 0:
                return color_1
            case 1:
                return color_2
            case 2:
                return color_3
            case 3:
                return color_4
            case 4:
                return color_5
            default:
                return null
        }
    }

    // not logged in
    if (!token) return <Navigate to="/" />

    const post = () => {
        setLoading(true)
        API.postPalette(token, {
            color_1: color_1.current.substring(1),
            color_2: color_2.current.substring(1),
            color_3: color_3.current.substring(1),
            color_4: color_4.current.substring(1),
            color_5: color_5.current.substring(1)
        }).then(() => {navigateTo("/feed/new")})
    }

    return <Container className="mt-3">
        <Row className="mb-3">
            <Col>
                <h4>Create New Palette</h4>
            </Col>
        </Row>
        {[...Array(5)].map((_, i) => <Row className="mb-3">
            <Col>
                <NewPaletteColor onColorChange={color => getColorRef(i).current = color}/>
            </Col>
        </Row>)}
        <Row className="justify-content-end">
            <Col>
                <Button style={{float: 'right'}} onClick={post} disabled={loading}>
                    {loading ? <><Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> Loading...</> : "Post Palette"}
                </Button>
            </Col>
        </Row>
    </Container>
}
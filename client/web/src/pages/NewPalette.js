import { Button, Card, Col, Container, Row, Spinner } from "react-bootstrap";
import NewPaletteColor from "../components/NewPaletteColor";
import { useToken } from "../context/TokenProvider";
import { Navigate, useNavigate } from "react-router-dom";
import API from '../utils/API';
import { useRef, useState } from "react";
import { ExclamationCircle } from "react-bootstrap-icons";

export function NewPalette(){
    const [errMsg, setErrMsg] = useState(null)
    const token = useToken()
    const navigateTo = useNavigate()
    const color_1 = useRef("")
    const color_2 = useRef("")
    const color_3 = useRef("")
    const color_4 = useRef("")
    const color_5 = useRef("")
    const [lastColor, setLastColor] = useState()

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
        setErrMsg(null)
        // data validation
        if (color_1.current === "" || color_2.current === "" || color_3.current === "" || color_4.current === "" 
                || color_5.current === ""){
            setErrMsg("Error: Please select 5 colors.")
            setLoading(false)
            return
        }
        // api call
        API.postPalette(token, {
            color_1: color_1.current.substring(1),
            color_2: color_2.current.substring(1),
            color_3: color_3.current.substring(1),
            color_4: color_4.current.substring(1),
            color_5: color_5.current.substring(1)
        }).then(() => {navigateTo("/feed/new")})
    }

    return <Container className="mt-3"> 
        <Row className="mb-2">
            <Col>
                <h4 className="text-center">New Post</h4>
            </Col>
        </Row>
        <Card id="new-palette-card">
            {[...Array(5)].map((_, i) => <Row>
                <Col>
                    <NewPaletteColor color={lastColor} onColorChange={color => {
                        setErrMsg(null)
                        getColorRef(i).current = color
                        setLastColor(color)
                    }}/>
                </Col>
            </Row>)}
        </Card>
        {errMsg ? 
        <Row className="new-palette-row mt-2">
            <Col>
                <span className="new-palette-error-text d-flex align-items-center"><ExclamationCircle className="me-2"/>{errMsg}</span>
            </Col>
        </Row> : ''}
        <Row id="new-palette-post-row" className="justify-content-end">
            <Col className="p-0">
                <Button style={{float: 'right'}} onClick={post} disabled={loading}>
                    {loading ? <><Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> Loading...</> : "Post Palette"}
                </Button>
            </Col>
        </Row>
    </Container>
}
import { Card, Col, Container, Row, Tab, Tabs } from "react-bootstrap";
import PaletteList, { ACTIONS, reducer } from "../components/PaletteList";
import { useToken } from "../context/TokenProvider";
import { Navigate, useSearchParams } from "react-router-dom";
import { useEffect, useReducer, useRef, useState } from "react";
import API from "../utils/API";

const PAGE_LENGTH = 50

export default function Profile(){
    const token = useToken()
    let [searchParams, setSearchParams] = useSearchParams()

    const [loaded, setLoaded] = useState(false)
    const [error, setError] = useState(null)
    const [palettes, dispatch] = useReducer(reducer, [])
    const [page, setPage] = useState(1)
    const count = useRef(0)

    useEffect(() => {
        if (searchParams.has("page") && Number(searchParams.get("page")) !== page)
            setPage(Number(searchParams.get("page")))
    }, [])

    useEffect(() => {
        if (!token)
            return
        let apiPage = page
        let timeout = setTimeout(() => {
            setLoaded(false)
        }, 100)
        if (!searchParams.has("page")){
            apiPage = 1
            setPage(1)
        }
        API.selfLikedPalettes(token, apiPage)
            .then(palettes => palettes.json())
            .then(res => {
                count.current = res.count
                // @ts-ignore
                dispatch({type: ACTIONS.SET_PALETTES, palettes: res.palettes})
            })
            .then(() => clearTimeout(timeout))
            .then(() => setLoaded(true))
    }, [token, page])

    if (!token)
        return <Navigate to="/" />

    const gotoPage = (page) => {
        setPage(page)
        if (page !== 1)
            setSearchParams(params => {
                params.set('page', page)
                return params
            })
        else
            setSearchParams(params => {
                params.delete('page')
                return params
            })
    }

    return <>
        <div className="d-flex flex-column w-100">
            {/* <Container className="mt-3">
                <Card className="profile-header bg-body-tertiary">
                    <div className="profile-banner">
                    <img className="profile-avatar-img" width="32" src="https://lh3.googleusercontent.com/a/AAcHTtcNGVsdckVs5BZssPcdI78Wz3KDDHpVtoSu8-VrZ3ZLNA=s96-c" referrerPolicy="no-referrer" />
                    <ul className="profile-stats-list">
                        <li className="profile-stats-item"><h6>25<br/>Palettes</h6></li>
                        <li className="profile-stats-item"><h6>18<br/>Likes</h6></li>
                        <li className="profile-stats-item"><h6>35<br/>Liked</h6></li>
                    </ul>
                    </div>
                    <div className="px-4 pb-2">
                        <h2 className="mt-5 me-5 flex-grow-1">Arthur</h2>
                    </div>
                    <Tabs defaultActiveKey="liked" className="mt-3">
                        <Tab eventKey="palettes" title="Palettes" disabled></Tab>
                        <Tab eventKey="liked" title="Likes"></Tab>
                        <Tab eventKey="collections" title="Collections" disabled></Tab>
                    </Tabs>
                </Card>
            </Container> */}
            <Container id="feed-container" className="pt-3">
                <PaletteList palettes={palettes} dispatch_palettes={dispatch} loaded={loaded} error={error} 
                    page={page} page_len={PAGE_LENGTH} count={count.current} gotoPage={gotoPage} 
                    empty_msg="Browse the &ldquo;New&rdquo; and &ldquo;Popular&rdquo; pages for palettes to add to your liked collection."/>
            </Container>
        </div>
    </>
}
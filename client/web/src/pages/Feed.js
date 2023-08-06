import { useEffect, useReducer, useRef, useState } from "react";
import { useToken } from "../context/TokenProvider";
import { Container } from "react-bootstrap";
import API, { checkResponse } from "../utils/API";
import { useLocation, useSearchParams } from "react-router-dom";
import PaletteList, { ACTIONS, reducer } from "../components/PaletteList";

const PAGE_LENGTH = 50

export function Feed(){
    const token = useToken()
    const location = useLocation()
    let [searchParams, setSearchParams] = useSearchParams()

    const [loaded, setLoaded] = useState(false)
    const [error, setError] = useState(null)
    const [palettes, dispatch] = useReducer(reducer, [])
    const [page, setPage] = useState(1)
    const count = useRef(0)

    const loadPage = () => {
        setError(null)
        window.scrollTo(0, 0)
        let apiPage = page
        let timeout = setTimeout(() => {
            setLoaded(false)
        }, 100)
        if (!searchParams.has("page")){
            apiPage = 1
            setPage(1)
        }
        if (location.pathname === "/feed/new")
            API.newPalettes(token, apiPage)
                .then(res => checkResponse(res))
                .then(palettes => palettes.json())
                .then(res => {
                    count.current = res.count
                    // @ts-ignore
                    dispatch({type: ACTIONS.SET_PALETTES, palettes: res.palettes})
                })
                .then(() => clearTimeout(timeout))
                .then(() => setLoaded(true))
                .catch((err) => setError({code: "", msg:err.message, retry: loadPage}))
        else if (location.pathname === "/feed/popular")
            API.popularPalettes(token, apiPage)
                .then(res => checkResponse(res))
                .then(palettes => palettes.json())
                .then(res => {
                    count.current = res.count
                    // @ts-ignore
                    dispatch({type: ACTIONS.SET_PALETTES, palettes: res.palettes})
                })
                .then(() => clearTimeout(timeout))
                .then(() => setLoaded(true))
                .catch((err) => setError({code: "", msg:err.message, retry: loadPage}))
    }

    useEffect(() => {
        loadPage()
    }, [token, location, page])

    useEffect(() => {
        if (searchParams.has("page") && Number(searchParams.get("page")) !== page)
            setPage(Number(searchParams.get("page")))
    }, [])

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
        <Container id="feed-container" className="pt-3">
            <PaletteList palettes={palettes} dispatch_palettes={dispatch} loaded={loaded} error={error} 
                page={page} page_len={PAGE_LENGTH} count={count.current} placeholder_count={6} gotoPage={gotoPage} 
                empty_msg="Sign in with your google account in the top right to create a palette." />
        </Container>
    </>
}
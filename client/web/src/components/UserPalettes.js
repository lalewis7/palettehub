import { Container } from "react-bootstrap";
import PaletteList, { ACTIONS, reducer } from "./PaletteList";
import { useEffect, useReducer, useRef, useState } from "react";
import { useToken } from "context/TokenProvider";
import { useParams, useSearchParams } from "react-router-dom";
import API from "utils/API";

const PAGE_LENGTH = 50

export default function UserPalettes(props){
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
        loadPage()
    }, [token, props.id, page])

    const loadPage = () => {
        setError(null)
        let apiPage = page
        let timeout = setTimeout(() => {
            setLoaded(false)
        }, 100)
        if (!searchParams.has("page")){
            apiPage = 1
            setPage(1)
        }
        API.getUserPalettes(token, props.id, apiPage)
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
        <Container id="feed-container" className="py-3">
            <PaletteList palettes={palettes} dispatch_palettes={dispatch} loaded={loaded} error={error} 
                page={page} page_len={PAGE_LENGTH} count={count.current} gotoPage={gotoPage} 
                empty_msg="Check back to see this users first palette."/>
        </Container>
    </>
}
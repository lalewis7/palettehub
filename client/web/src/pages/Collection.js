import { useToken } from "context/TokenProvider"
import PaletteList, {reducer as paletteReducer, ACTIONS as PALETTE_ACTIONS} from "../components/PaletteList"
import { useEffect, useReducer, useRef, useState } from "react"
import { Link, useParams, useSearchParams } from "react-router-dom"
import API from '../utils/API'
import { convertColorsToArray } from "utils/PaletteUtil"
import { Button, Card, Container } from "react-bootstrap"
import ErrorPage from "components/ErrorPage"
import { Pencil } from "react-bootstrap-icons"
import { useColorMode } from "context/ColorModeProvider"
import { useSelector } from "react-redux"
import EditCollection from "components/EditCollection"
import CollectionPlaceholder from "components/CollectionPlaceholder"

const PAGE_LENGTH = 50

const ACTIONS = {
    SET_COLLECTION: 'set-collection'
}

function reducer(collection, action){
    switch (action.type){
        case ACTIONS.SET_COLLECTION:
            let newCollection = {...action.collection}
            newCollection.palettes = newCollection.palettes.map(pal => {
                if (pal.colors)
                    return pal
                return convertColorsToArray(pal)
            })
            console.log(newCollection)
            return newCollection
        default:
            return collection
    }
}

export default function Collection(){
    const token = useToken()
    const colorMode = useColorMode()
    let [searchParams, setSearchParams] = useSearchParams()

    // @ts-ignore
    const self = useSelector(state => state.user.value)

    const [loaded, setLoaded] = useState(false)
    const [error, setError] = useState(null)
    const [canEdit, setCanEdit] = useState(false)
    const [collection, dispatch] = useReducer(reducer, [])
    const [palettes, dispatchPalettes] = useReducer(paletteReducer, [])
    const [page, setPage] = useState(1)
    const count = useRef(0)
    let { id } = useParams()

    // edit profile
    const [show, setShow] = useState(false)
    const handleClose = () => setShow(false)
    const handleShow = () => setShow(true)

    useEffect(() => {
        if (searchParams.has("page") && Number(searchParams.get("page")) !== page)
            setPage(Number(searchParams.get("page")))
    }, [])

    useEffect(() => {
        loadPage()
    }, [token, id, page])
    
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
        API.getCollection(token, id, apiPage)
            .then(collection => collection.json())
            .then(res => {
                count.current = res.count
                // @ts-ignore
                dispatch({type: ACTIONS.SET_COLLECTION, collection: res})
                // @ts-ignore
                dispatchPalettes({type: PALETTE_ACTIONS.SET_PALETTES, palettes: res.palettes})
            })
            .then(() => clearTimeout(timeout))
            .then(() => setLoaded(true))
            .catch((err) => setError({code: "", msg:err.message, retry: loadPage}))
    }

    const gotoPage = (page) => {
        window.scrollTo({top: 0, left: 0, behavior: 'smooth'})
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

    useEffect(() => {
        setCanEdit(self && token && (self.user_id === collection.user_id || self.role === "admin"))
    }, [token, self, collection, id])

    const editCollection = async (data) => {
        return API.editCollection(token, id, data)
            .then(() => loadPage())
    }

    if (error)
        return <ErrorPage code={error.code} msg={error.msg} retry={error.retry} />

    return <>
        <div className="d-flex flex-column w-100">
            <Container className="d-flex justify-content-between flex-column pt-3">
                <Card id="collection-card" className="bg-body-tertiary p-3 mb-3">
                    {loaded ? <>
                        <div className="d-flex flex-row justify-content-between">
                            <h1 className="h2 d-flex align-items-center">{collection.name}</h1>
                            {canEdit ? 
                            <Button id="edit-collection-btn" variant={colorMode} onClick={handleShow}><Pencil size={22}/></Button>
                            : ''}
                        </div>
                        <div>
                            <span className="collection-user-name">
                                By&nbsp;
                                <Link to={"/profile/"+collection.user_id} className="collection-user">
                                    {collection.user_name}
                                </Link>
                            </span>
                        </div>
                    </>
                    : <CollectionPlaceholder />}
                </Card>
                <PaletteList palettes={palettes} dispatch_palettes={dispatchPalettes} loaded={loaded} error={error} 
                    page={page} page_len={PAGE_LENGTH} count={count.current} gotoPage={gotoPage} 
                    collection_id={self && collection && self.user_id === collection.user_id ? id : null}
                    empty_msg="Looks like this collection doesn't have any palettes yet." />
            </Container>
        </div>
        <EditCollection show={show} handleClose={handleClose} collection={collection} submit={editCollection} />
    </>
}
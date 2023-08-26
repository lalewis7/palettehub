import { useEffect, useReducer, useRef, useState } from "react";
import { Button, Form, Modal, Pagination, Placeholder, Spinner } from "react-bootstrap";
import { useSelector } from 'react-redux'
import { convertColorsToArray } from "utils/PaletteUtil";
import API from "../utils/API"
import { useToken } from "context/TokenProvider";
import EmptyPage from "./EmptyPage";
import ErrorPage from "./ErrorPage";

const PAGE_LENGTH = 50

const ACTIONS = {
    SET_COLLECTIONS: "set-collections"
}

export function reducer(collections, action){
    switch (action.type){
        case ACTIONS.SET_COLLECTIONS:
            let newCollections = [...action.collections]
            newCollections.map(collection => {
                collection.palettes = collection.palettes.map(pal => {
                    if (pal.colors)
                        return pal
                    return convertColorsToArray(pal)
                })
            })
            return newCollections
        default:
            return collections
    }
}

export default function AddToCollection(props){
    const token = useToken()
    // @ts-ignore
    const self = useSelector(state => state.user.value)

    const [loaded, setLoaded] = useState(false)
    const [error, setError] = useState(null)
    const [collections, dispatch] = useReducer(reducer, [])
    const [page, setPage] = useState(1)
    const count = useRef(0)

    const [checked, setChecked] = useState(null)
    const [loading, setLoading] = useState(false)

    const loadCollections = () => {
        setError(null)
        let timeout = setTimeout(() => {
            setLoaded(false)
        }, 100)
        API.getUserCollections(token, self.user_id, page)
            .then(collections => collections.json())
            .then(res => {
                count.current = res.count
                // @ts-ignore
                dispatch({type: ACTIONS.SET_COLLECTIONS, collections: res.collections})
            })
            .then(() => clearTimeout(timeout))
            .then(() => setLoaded(true))
            .catch((err) => setError({code: "", msg:err.message, retry: loadCollections}))
    }

    useEffect(() => {
        if (props.show)
            loadCollections()
    }, [props.show, page])

    const handleSubmit = async (event) => {
        // prevent default behavior
        event.preventDefault()
        event.stopPropagation()
        setLoading(true)

        await API.addToCollection(token, checked, props.id)
            .then(res => {
                handleClose()
            })
        
        setLoading(false)
    }

    const handleClose = () => {
        setChecked(null)
        props.handleClose()
    }

    const pages = () => {
        if (!loaded)
            return <></>
        let total_pages = Math.ceil(count.current / PAGE_LENGTH)
        return <>
            <Pagination.Prev disabled={page === 1} onClick={() => setPage(page => page-1)}/>
            {pageItem(1)}
            {page > 4 ? <Pagination.Ellipsis disabled/> : ''}
            {page - 2 > 1 ? pageItem(page-2) : ''}
            {page - 1 > 1 ? pageItem(page-1) : ''}
            {page !== 1 ? pageItem(page) : ''}
            {page + 1 < total_pages ? pageItem(page+1) : ''}
            {page + 2 < total_pages ? pageItem(page+2) : ''}
            {page < total_pages - 3 ? <Pagination.Ellipsis disabled /> : ''}
            {page !== total_pages ? pageItem(total_pages) : ''}
            <Pagination.Next disabled={page === total_pages} onClick={() => setPage(page => page+1)}/>
        </>
    }

    const pageItem = (num) => <Pagination.Item key={num} active={page === num} onClick={() => setPage(num)} >{num}</Pagination.Item>

    if (!self)
        return

    if (error)
        return <ErrorPage code={error.code} msg={error.msg} retry={error.retry} />

    return <>
        <Modal show={props.show} onHide={handleClose} fullscreen="sm-down" scrollable={true} animation={false}>
            <Modal.Header closeButton>
                <Modal.Title>Add to Collection</Modal.Title>
            </Modal.Header>
            <Modal.Body className="d-flex flex-column justify-content-between">
                {loaded ? 
                    count.current > 0 ? <>
                        <Form noValidate onSubmit={handleSubmit}>
                            <Form.Group>
                                {collections.map(collection => <>
                                    <Form.Check key={collection.collection_id} id={"collection-"+collection.collection_id}className="w-100 m-0 d-flex align-items-center" style={{height: "2.5rem"}} >
                                        <Form.Check.Input type="checkbox" checked={checked===collection.collection_id} onChange={e => setChecked(collection.collection_id)}  style={{width: "1.5rem", height: "1.5rem"}} />
                                        <Form.Check.Label className="ms-3">{collection.name}</Form.Check.Label>
                                    </Form.Check>
                                </>)}
                            </Form.Group>
                        </Form>
                    </>
                    : <EmptyPage msg={"You don't have any collections yet."} />
                    : <Placeholder animation="glow" className="d-flex flex-column">
                        {[...Array(5)].map(() => <>
                            <div className="d-flex w-100">
                                <Placeholder style={{width: '1.25rem', height: '1.25rem', borderRadius: '100%', marginRight: '0.5rem'}} />
                                <Placeholder xs={10} style={{height: '1.25rem', marginBottom: '0.5rem'}} />
                            </div>
                        </>)}
                    </Placeholder>}
                    {loaded && count.current > PAGE_LENGTH ? 
                    <Pagination className="my-3 d-flex justify-content-center">
                        {pages()}
                    </Pagination>
                    : ''}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleSubmit} disabled={!checked || loading}>
                    {loading ? <>
                        <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> Loading...
                    </> : <span className="d-flex align-items-center">Add Palette</span>}
                </Button>
            </Modal.Footer>
        </Modal>
    </>
}
import { Pagination } from "react-bootstrap"
import ErrorPage from "./ErrorPage"
import EmptyPage from "./EmptyPage"
import FeedPalettePlaceholder from "./FeedPalettePlaceholder"
import FeedCollection from "./FeedCollection"
import { convertColorsToArray } from "utils/PaletteUtil"

export const ACTIONS = {
    SET_COLLECTIONS: 'collections',
    REMOVE_COLLECTION: 'remove-collection'
}

export function reducer(collections, action){
    switch (action.type){
        case ACTIONS.SET_COLLECTIONS:
            {
                let newCollections = [...action.collections]
                newCollections.map(collection => {
                    collection.palettes = collection.palettes.map(pal => {
                        if (pal.colors)
                            return pal
                        return convertColorsToArray(pal)
                    })
                })
                return newCollections
            }
        case ACTIONS.REMOVE_COLLECTION:
            {
                console.log(action)
                let newCollections = [...collections]
                for (let i = 0; i < newCollections.length; i++){
                    let collection = newCollections[i]
                    if (collection.collection_id === action.id){
                        newCollections.splice(i, 1)
                        return newCollections
                    }
                }
                return newCollections
            }
        default:
            return collections
    }
}

export default function CollectionList(props){
    const pages = () => {
        if (!props.loaded)
            return <></>
        let total_pages = Math.ceil(props.count / props.page_len)
        return <>
            <Pagination.Prev disabled={props.page === 1} onClick={() => props.gotoPage(props.page-1)}/>
            {pageItem(1)}
            {props.page > 4 ? <Pagination.Ellipsis disabled/> : ''}
            {props.page - 2 > 1 ? pageItem(props.page-2) : ''}
            {props.page - 1 > 1 ? pageItem(props.page-1) : ''}
            {props.page !== 1 ? pageItem(props.page) : ''}
            {props.page + 1 < total_pages ? pageItem(props.page+1) : ''}
            {props.page + 2 < total_pages ? pageItem(props.page+2) : ''}
            {props.page < total_pages - 3 ? <Pagination.Ellipsis disabled /> : ''}
            {props.page !== total_pages ? pageItem(total_pages) : ''}
            <Pagination.Next disabled={props.page === total_pages} onClick={() => props.gotoPage(props.page+1)}/>
        </>
    }

    const pageItem = (num) => <Pagination.Item key={num} active={props.page === num} onClick={() => props.gotoPage(num)} >{num}</Pagination.Item>

    if (props.error)
        return <ErrorPage code={props.error.code} msg={props.error.msg} retry={props.error.retry} />
    
    return <>
        {props.loaded ? 
        props.count > 0 ? <>
            <div id="feed-content">
                {props.collections.map(collection => <FeedCollection key={collection.collection_id} id={collection.collection_id} name={collection.name} 
                user_id={collection.user_id} user_name={collection.user_name} user_img={collection.user_img} palettes={collection.palettes} 
                palette_count={collection.palettes} dispatch={props.dispatch_collections} />)}
            </div>
        </>
        : <EmptyPage msg={props.empty_msg} />
        : <div id="feed-content-placeholder">{[...Array(15)].map(() => <FeedPalettePlaceholder />)}</div>}
        {props.loaded && props.count > props.page_len ? 
        <Pagination id="feed-pagination">
            {pages()}
        </Pagination>
        : ''}
    </>
}
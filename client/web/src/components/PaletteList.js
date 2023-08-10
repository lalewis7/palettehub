import { Pagination } from "react-bootstrap";
import { FeedPalette } from "./FeedPalette";
import FeedPalettePlaceholder from "./FeedPalettePlaceholder";
import { convertColorsToArray } from "../utils/PaletteUtil";
import API from "../utils/API";
import EmptyPage from "./EmptyPage";
import ErrorPage from "./ErrorPage";

export const ACTIONS = {
    SET_PALETTES: 'set-palettes',
    LIKE: 'like-palette',
    UNLIKE: 'unlike-palette'
}

export function reducer(palettes, action){
    switch (action.type){
        case ACTIONS.SET_PALETTES:
            return action.palettes.map(pal => convertColorsToArray(pal))
        case ACTIONS.LIKE:
            let likePals = [...palettes]
            likePals.forEach(pal => {
                if (pal.palette_id === action.id && !pal.liked) {
                    pal.liked = true
                    pal.likes++
                    API.likePalette(action.token, action.id)
                        .catch(err => {
                            // TODO: REMOVE LIKE FROM UI AND SHOW ERROR MSG
                            console.log(err)
                        })
                }
            })
            return likePals
        case ACTIONS.UNLIKE:
            let unlikePals = [...palettes]
            unlikePals.forEach(pal => {
                if (pal.palette_id === action.id && pal.liked) {
                    pal.liked = false
                    pal.likes--
                    API.unlikePalette(action.token, action.id)
                        .catch(err => {
                            // TODO: REMOVE LIKE FROM UI AND SHOW ERROR MSG
                            console.log(err)
                        })
                }
            })
            return unlikePals
        default:
            return palettes
    }
}

/**
 * React Component
 * Props: 
 * - palettes: array
 * - dispatch_palette: func
 * - loaded: bool
 * - error: bool
 * - page: num
 * - page_len: num
 * - count: num
 * - gotoPage: func
 * @param {*} props 
 * @returns 
 */
export default function PaletteList(props){
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
            {props.palettes.map(palette => <FeedPalette key={palette.palette_id} id={palette.palette_id} colors={palette.colors} 
                likes={palette.likes} liked={palette.liked} timestamp={palette.posted} dispatch={props.dispatch_palettes} />)}
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
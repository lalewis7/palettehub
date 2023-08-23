import { Button, Card, Dropdown, Ratio } from "react-bootstrap"
import { Heart, HeartFill, ThreeDotsVertical } from 'react-bootstrap-icons'
import { ACTIONS } from "./PaletteList"
import { useToken } from "../context/TokenProvider"
import LikePopover from "./LikePopover"
import { pickTextColorWhiteBlack } from "../utils/TextColorUtil"
import { Link } from "react-router-dom"
import { getTimeElapsed } from "../utils/PaletteUtil"
import { useColorMode } from "context/ColorModeProvider"
import profile_img from '../assets/user-avatar.png'
import { useSelector } from 'react-redux'
import API from '../utils/API'

export function FeedPalette(props){
    const token = useToken()
    const colorMode = useColorMode()

    // @ts-ignore
    const self = useSelector(state => state.user.value)

    const onLikeBtn = () => {
        if (token){
            if (props.liked)
                props.dispatch({type: ACTIONS.UNLIKE, id: props.id, token: token})
            else
                props.dispatch({type: ACTIONS.LIKE, id: props.id, token: token})
        }
    }

    const onDelete = () => {
        API.deletePalette(token, props.palette_id)
            .then(() => {
                // delete from palettelist
            })
            .catch((e) => console.log(e))
    }

    const likeBtn = 
        <Button variant={colorMode} className="feed-palette-like" onClick={onLikeBtn} >
            {props.liked ? <HeartFill color={"red"} size={22} /> : <Heart size={20} />}
        </Button>

    return <>
        <div className="feed-palette-wrapper">
            <Card className="feed-palette">
                <Card.Header className="feed-palette-row feed-palette-top">
                    <Link to={"/profile/"+props.user_id} className="feed-palette-user">
                        <img src={props.user_img && props.user_img !== "" ? props.user_img : profile_img} referrerPolicy="no-referrer" className="feed-palette-avatar" />
                        <span className="feed-palette-user-name">{props.user_name}</span>
                    </Link>
                    <div className="feed-palette-kebab">
                        <Dropdown  className="h-100">
                            <Dropdown.Toggle variant={colorMode} className="h-100 lh-1 feed-palette-kebab-btn">
                                <ThreeDotsVertical size={18} />
                            </Dropdown.Toggle>
                            <Dropdown.Menu className="feed-palette-kebab-menu">
                                <Dropdown.Item>
                                    Copy link
                                </Dropdown.Item>
                                {self ? 
                                <Dropdown.Item href="#/action-1">Add to collection</Dropdown.Item>
                                : ''}
                                {self && (self.user_id === props.user_id || self.role === "admin") ? 
                                <Dropdown.Item onClick={onDelete}>Delete</Dropdown.Item>
                                : ''}
                            </Dropdown.Menu>
                        </Dropdown>
                    </div>
                </Card.Header>
                <Ratio aspectRatio={80}>
                    <div className="feed-palette-colors-grid">
                        {props.colors.map((color, i) => <Link key={i} className="feed-palette-color" 
                                style={{backgroundColor: "#"+color}} to={"/palettes/"+props.id}>
                            <span style={{color: pickTextColorWhiteBlack(color)}} className="feed-palette-code">{"#"+color}</span>
                        </Link>)}
                    </div>
                </Ratio>
                <Card.Body className="feed-palette-row feed-palette-bottom">
                    <div className="feed-palette-likes">
                        {token ? likeBtn : 
                            <LikePopover>
                                {likeBtn}
                            </LikePopover>
                        }
                        <span className="ms-1">{props.likes}</span>
                    </div>
                    <small>{getTimeElapsed(props.timestamp)}</small>
                </Card.Body>
            </Card>
        </div>
    </>
}
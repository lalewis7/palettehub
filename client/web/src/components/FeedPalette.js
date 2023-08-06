import { Button, Card } from "react-bootstrap"
import { Heart, HeartFill } from 'react-bootstrap-icons'
import { ACTIONS } from "./PaletteList"
import { useToken } from "../context/TokenProvider"
import LikePopover from "./LikePopover"
import { pickTextColorWhiteBlack } from "../utils/TextColorUtil"
import { Link } from "react-router-dom"
import { getTimeElapsed } from "../utils/PaletteUtil"
import { useColorMode } from "context/ColorModeProvider"

export function FeedPalette(props){
    const token = useToken()
    const colorMode = useColorMode()

    const onLikeBtn = () => {
        if (token){
            if (props.liked)
                props.dispatch({type: ACTIONS.UNLIKE, id: props.id, token: token})
            else
                props.dispatch({type: ACTIONS.LIKE, id: props.id, token: token})
        }
    }

    const likeBtn = 
        <Button variant={colorMode} size="sm" onClick={onLikeBtn} className="m-1">
            {props.liked ? <HeartFill color={"red"} /> : <Heart />}
        </Button>

    return <>
        <div className="feed-palette-wrapper">
            <Card className="feed-palette">
                {props.colors.map((color, i) => <Link key={i} className="feed-palette-row feed-palette-color" 
                        style={{backgroundColor: "#"+color}} to={"/palettes/"+props.id}>
                    <span style={{color: pickTextColorWhiteBlack(color)}} className="feed-palette-code">{"#"+color}</span>
                </Link>)}
                <div className="feed-palette-row feed-palette-bottom">
                    <div className="feed-palette-likes">
                        {token ? likeBtn : 
                            <LikePopover>
                                {likeBtn}
                            </LikePopover>
                        }
                        <span>{props.likes}</span>
                    </div>
                    <small>{getTimeElapsed(props.timestamp)}</small>
                </div>
            </Card>
        </div>
    </>
}
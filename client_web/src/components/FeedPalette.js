import { Button, Card } from "react-bootstrap"
import { Heart, HeartFill } from 'react-bootstrap-icons'
import { ACTIONS } from "./PaletteList"
import { useToken } from "./TokenProvider"
import LikePopover from "./LikePopover"
import { pickTextColor } from "./TextColorUtil"
import { Link } from "react-router-dom"
import { getTimeElapsed } from "./PaletteUtil"

export function FeedPalette(props){
    const token = useToken()

    const onLikeBtn = () => {
        if (token){
            if (props.liked)
                props.dispatch({type: ACTIONS.UNLIKE, id: props.id, token: token})
            else
                props.dispatch({type: ACTIONS.LIKE, id: props.id, token: token})
        }
    }

    const likeBtn = 
        <Button variant="light" size="sm" onClick={onLikeBtn}>
            {props.liked ? <HeartFill color={"red"} /> : <Heart />}
        </Button>

    return <Card className="feed-palette">
        {props.colors.map((color, i) => <Link key={i} className="feed-palette-row feed-palette-color" 
                style={{backgroundColor: "#"+color}} to={"/palettes/"+props.id}>
            <span style={{color: pickTextColor(color, "#FFFFFF", "#000000")}} className="feed-palette-code">{"#"+color}</span>
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
            <small>{getTimeElapsed(props.timestamp)} ago</small>
        </div>
    </Card>
}
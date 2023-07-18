import { Button, Card } from "react-bootstrap"
import { Heart, HeartFill } from 'react-bootstrap-icons'
import { ACTIONS } from "./Feed"
import { useToken } from "./TokenProvider"
import LikePopover from "./LikePopover"

function getTimeElapsed(timestamp){
    let elapsedSeconds = (Date.now() / 1000 - timestamp)
    if (elapsedSeconds < 60)
        return Math.floor(elapsedSeconds) + "s"
    else if (elapsedSeconds < 60 * 60)
        return Math.floor(elapsedSeconds / 60) + "m"
    else if (elapsedSeconds < 60 * 60 * 24)
        return Math.floor(elapsedSeconds / (60 * 60)) + "h"
    else if (elapsedSeconds < 60 * 60 * 24 * 30)
        return Math.floor(elapsedSeconds / (60 * 60 * 24)) + "d"
    else if (elapsedSeconds < 60 * 60 * 24 * 365)
        return Math.floor(elapsedSeconds / (60 * 60 * 24 * 30)) + "mo"
    else
        return Math.floor(elapsedSeconds / (60 * 60 * 24 * 365)) + "yr"
}

export function FeedPalette(props){
    const token = useToken()

    const cardStyle = {
        width: "14rem",
        overflow: "hidden"
    }

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

    return <Card style={cardStyle}>
        {props.colors.map((color, i) => <div key={i} className="feed-palette-row feed-palette-color" style={{backgroundColor: "#"+color}}></div>)}
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
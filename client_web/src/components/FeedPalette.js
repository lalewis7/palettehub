import { Button, Card } from "react-bootstrap"
import { Heart, HeartFill } from 'react-bootstrap-icons'

function getTimeElapsed(timestamp){
    let elapsedSeconds = (Date.now() - timestamp) / 1000
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
    const cardStyle = {
        width: "14rem",
        overflow: "hidden"
    }
    return <Card style={cardStyle}>
        {props.colors.map(color => <div className="feed-palette-row" style={{backgroundColor: "#"+color}}></div>)}
        <div className="feed-palette-row feed-palette-bottom">
            <div className="feed-palette-likes">
                <Button variant="light" size="sm"><HeartFill color={"red"} /></Button>
                <span>{props.likes}</span>
            </div>
            <small>{getTimeElapsed(props.timestamp)} ago</small>
        </div>
    </Card>
}
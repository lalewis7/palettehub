import { Button, Card, Ratio } from "react-bootstrap"
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
        <Button variant={colorMode} size="sm" onClick={onLikeBtn} className="h-100">
            {props.liked ? <HeartFill color={"red"} size={18} /> : <Heart size={16} />}
        </Button>

    return <>
        <div className="feed-palette-wrapper">
            <Card className="feed-palette">
                <Ratio aspectRatio={90}>
                    <div className="feed-palette-colors-grid">
                        {props.colors.map((color, i) => <Link key={i} className="feed-palette-color" 
                                style={{backgroundColor: "#"+color}} to={"/palettes/"+props.id}>
                            <span style={{color: pickTextColorWhiteBlack(color)}} className="feed-palette-code">{"#"+color}</span>
                        </Link>)}
                    </div>
                </Ratio>
                <div className="feed-palette-row feed-palette-bottom">
                    <div className="feed-palette-likes">
                        {token ? likeBtn : 
                            <LikePopover>
                                {likeBtn}
                            </LikePopover>
                        }
                        <span className="ms-1">{props.likes}</span>
                    </div>
                    <small>{getTimeElapsed(props.timestamp)}</small>
                </div>
            </Card>
        </div>
    </>
}
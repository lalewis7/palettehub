import { Card, Dropdown, Ratio } from "react-bootstrap";
import { Link } from "react-router-dom";
import profile_img from '../assets/user-avatar.png'
import { useColorMode } from "context/ColorModeProvider";
import { Folder, ThreeDotsVertical } from "react-bootstrap-icons";
import { useSelector } from "react-redux";

export default function FeedCollection(props){
    // @ts-ignore
    const self = useSelector(state => state.user.value)
    const colorMode = useColorMode()

    const onDelete = () => {
        console.log("delete")
    }

    return <>
        <Card>
            <Card.Header className="feed-collection-header">
                <Link to={"/profile/"+props.user_id} className="feed-collection-user">
                    <img src={props.user_img && props.user_img !== "" ? props.user_img : profile_img} referrerPolicy="no-referrer" className="feed-collection-avatar" />
                    <span className="feed-collection-user-name">{props.user_name}</span>
                </Link>
                <div className="feed-collection-kebab">
                    <Dropdown className="h-100" align="end">
                        <Dropdown.Toggle variant={colorMode} className="h-100 lh-1 feed-collection-kebab-btn">
                            <ThreeDotsVertical size={18} />
                        </Dropdown.Toggle>
                        <Dropdown.Menu className="feed-collection-kebab-menu">
                            <Dropdown.Item>
                                Copy link
                            </Dropdown.Item>
                            {self && (self.user_id === props.user_id || self.role === "admin") ? 
                            <Dropdown.Item onClick={onDelete}>Delete</Dropdown.Item>
                            : ''}
                        </Dropdown.Menu>
                    </Dropdown>
                </div>
            </Card.Header>
            <Ratio aspectRatio={80}>
                <Link to={"/collections/"+props.id} className="feed-collection-content">
                    {props.palettes.map((pal, i) => <div key={i} className="feed-collection-palette">
                        {pal.colors.map((color, j) => <div key={j} className="feed-collection-palette-color" style={{backgroundColor: "#"+color}}>

                        </div>)}
                    </div>)}
                </Link>
            </Ratio>
            <Card.Footer className="feed-collection-footer">
                <div className="feed-collection-folder-wrapper me-2">
                    <Folder size={18} />
                </div>
                <span className="feed-collection-footer-name">{props.name}</span>
            </Card.Footer>
        </Card>
    </>
}
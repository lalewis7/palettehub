import { OverlayTrigger, Popover } from "react-bootstrap";

export default function LikePopover(props){
    return <>
        <OverlayTrigger 
            trigger="click"
            placement={props.placement || "auto-start"}
            delay={50}
            rootClose={true}
            overlay={
                <Popover>
                    <Popover.Header>Like this palette?</Popover.Header>
                    <Popover.Body>
                        Sign in with google to like this post.
                    </Popover.Body>
                </Popover>
            }
        >
            {props.children}
        </OverlayTrigger>
    </>
}
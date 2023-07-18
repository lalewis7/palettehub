import { OverlayTrigger, Popover } from "react-bootstrap";

export default function LikePopover({children}){
    return <>
        <OverlayTrigger 
            trigger="click"
            placement="bottom-start"
            delay={50}
            rootClose={true}
            overlay={
                <Popover>
                    <Popover.Header>Like this palette?</Popover.Header>
                    <Popover.Body>
                        Sign in with google to show your enjoyment.
                    </Popover.Body>
                </Popover>
            }
        >
            {children}
        </OverlayTrigger>
    </>
}
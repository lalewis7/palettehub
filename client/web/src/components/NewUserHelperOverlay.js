import { useState } from "react";
import { CloseButton, Overlay, Popover } from "react-bootstrap";

const KEY = 'new_user_sign_up_msg_dismissed'

export default function NewUserHelperOverlay(props){
    const [show, setShow] = useState(sessionStorage.getItem(KEY) ? false : true);

    const handleClick = (event) => {
        setShow(false);
        sessionStorage.setItem(KEY, "true")
    };

    return <>
        <Overlay
            key={props.test}
            show={show && props.show}
            target={props.target}
            container={props.target}
            placement="bottom"
        >
            <Popover>
                <Popover.Header as="h3" className="d-flex justify-content-between align-items-center"><span>Join the community!</span><CloseButton onClick={handleClick} /></Popover.Header>
                <Popover.Body>
                    <p className="mb-2">Sign in with your Google account to access these features:</p>
                    <ul className="m-0">
                        <li>Create and like palettes</li>
                        <li>Organize your palettes with collections</li>
                        <li>Customize your profile</li>
                    </ul>
                </Popover.Body>
            </Popover>
        </Overlay>
    </>
}
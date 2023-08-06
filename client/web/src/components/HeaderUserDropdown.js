import { Button, Col, Dropdown, NavDropdown, Row } from 'react-bootstrap'
import { useTokenUpdate } from '../context/TokenProvider'
import { Link } from 'react-router-dom'
import { Box2Heart, BoxArrowRight } from 'react-bootstrap-icons'

export function HeaderUserDropdown(props){

    const updateToken = useTokenUpdate()

    const btnStyles = {
        backgroundSize: "contain",
        borderRadius: "100%",
        border: "none",
        padding: "0.25rem"
    }

    const logout = () => {
        updateToken(null)
    }

    return <>
        <NavDropdown id="profile-dropdown" align="end" title={
            <Button type="button" variant="" style={btnStyles} >
                <img src={props.pictureUrl} referrerPolicy="no-referrer" className="user-avatar-placeholder" />
            </Button>
        }>
            <div style={{width: '220px'}} />
            <NavDropdown.ItemText><b>{props.name}</b></NavDropdown.ItemText>
            <Dropdown.Divider />
            <NavDropdown.Item as={Link} to="/profile/likes">
                <Row className="py-1">
                    <Col xs={2} className="text-center">
                        <Box2Heart size={22} />
                    </Col>
                    <Col>
                        Your Likes
                    </Col>
                </Row>
            </NavDropdown.Item>
            <NavDropdown.Item as="button" onClick={logout}>
                <Row className="py-1">
                    <Col xs={2} className="text-center">
                        <BoxArrowRight size={22} />
                    </Col>
                    <Col>
                        Logout
                    </Col>
                </Row>
            </NavDropdown.Item>
        </NavDropdown>
    </>
}
import { Button, Col, Dropdown, NavDropdown, Row } from 'react-bootstrap'
import { useTokenUpdate } from '../context/TokenProvider'
import { Link } from 'react-router-dom'
import { Box2Heart, BoxArrowRight, Collection, Folder, PersonCircle } from 'react-bootstrap-icons'
import { useSelector } from 'react-redux'

export function HeaderUserDropdown(props){

    const updateToken = useTokenUpdate()
    // @ts-ignore
    const self = useSelector(state => state.user.value)

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
            <NavDropdown.Item as={Link} to={"/profile/"+self.user_id}>
                <Row className="py-1">
                    <Col xs={2} className="text-center">
                        <PersonCircle size={22} />
                    </Col>
                    <Col>
                        Your Profile
                    </Col>
                </Row>
            </NavDropdown.Item>
            <NavDropdown.Item as={Link} to={"/profile/"+self.user_id+"/likes"}>
                <Row className="py-1">
                    <Col xs={2} className="text-center">
                        <Box2Heart size={22} />
                    </Col>
                    <Col>
                        Your Likes
                    </Col>
                </Row>
            </NavDropdown.Item>
            <NavDropdown.Item as={Link} to={"/profile/"+self.user_id+"/collections"}>
                <Row className="py-1">
                    <Col xs={2} className="text-center">
                        <Folder size={22} />
                    </Col>
                    <Col>
                        Your Collections
                    </Col>
                </Row>
            </NavDropdown.Item>
            <Dropdown.Divider />
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
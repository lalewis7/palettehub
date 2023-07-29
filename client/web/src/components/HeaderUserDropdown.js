import { Button, Dropdown, NavDropdown } from 'react-bootstrap'
import { useTokenUpdate } from '../context/TokenProvider'
import { Link } from 'react-router-dom'

export function HeaderUserDropdown(props){

    const updateToken = useTokenUpdate()

    const btnStyles = {
        backgroundImage: "url("+props.pictureUrl+")",
        width: "32px",
        height: "32px",
        backgroundSize: "contain",
        borderRadius: "100%",
        border: "none"
    }

    const logout = () => {
        updateToken(null)
    }

    return <>
        <NavDropdown id="profile-dropdown" align="end" title={<Button type="button" variant="secondary" style={btnStyles} />}>
            <NavDropdown.ItemText><b>{props.name}</b></NavDropdown.ItemText>
            <NavDropdown.ItemText><i>{props.email}</i></NavDropdown.ItemText>
            <Dropdown.Divider />
            <NavDropdown.Item as={Link} to="/profile/likes">Your Likes</NavDropdown.Item>
            <NavDropdown.Item as="button" onClick={logout}>Logout</NavDropdown.Item>
        </NavDropdown>
    </>
}
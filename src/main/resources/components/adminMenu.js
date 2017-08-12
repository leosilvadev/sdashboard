import React from 'react';
import adminStore from '../flux/stores/admin';
import { logout } from '../flux/actions/admin';
import ComponentRegistration from './componentRegistration';
import { Nav, Navbar, NavItem } from 'react-bootstrap';

class AdminMenu extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            showModal: false
        };
        this.render = this.render.bind(this);
    }

    openRegistrationModal() {
        this.setState({ showModal: true });
    }

    closeRegistrationModal() {
        this.setState({ showModal: false });
    }

    render() {
        return  <Navbar>
                    <Navbar.Header>
                        <Navbar.Brand>
                            <a href="#">SDashboard</a>
                        </Navbar.Brand>
                    </Navbar.Header>
                    <Nav>
                        <NavItem eventKey={1} href="#">Dashboard</NavItem>
                        <NavItem eventKey={2} className="component_registration">
                            <ComponentRegistration></ComponentRegistration>
                        </NavItem>
                        <NavItem eventKey={3} href="#" onClick={logout}>Logout</NavItem>
                    </Nav>
                </Navbar>;
    }
}

export default AdminMenu;
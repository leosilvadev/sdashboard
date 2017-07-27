import React from 'react';
import Component from './component';
import componentsStore from '../flux/stores/components';
import ComponentRegistration from './componentRegistration';
import { Button, Nav, Navbar, NavItem, NavDropdown, MenuItem } from 'react-bootstrap';

import './components.scss';

export default class Components extends React.Component {

    constructor(props) {
        super(props);

        this.component = {};
        this.state = {
            components: componentsStore.getComponents()
        }
    }

    componentDidMount() {
        componentsStore.on('componentsUpdated', () => {
            this.setState(componentsStore.getComponents())
        });
    }

    load(components) {
        this.setState({components});
    }

    openRegistrationModal() {
        this.setState({ showModal: true });
    }

    closeRegistrationModal() {
        this.setState({ showModal: false });
    }

    render() {
        const {components} = this.state;
        const data = components.map(component =>
            <Component key={component._id} component={component}>
            </Component>
        );
        return  <div className="components_wrapper container">
                <Navbar>
                    <Navbar.Header>
                        <Navbar.Brand>
                            <a href="#">React-Bootstrap</a>
                        </Navbar.Brand>
                    </Navbar.Header>
                    <Nav>
                        <NavItem eventKey={1} href="#">Link</NavItem>
                        <NavItem eventKey={2} className="component_registration">
                            <ComponentRegistration></ComponentRegistration>
                        </NavItem>
                    </Nav>
                </Navbar>
                    <div className="components_body">
                        {data}
                    </div>
                </div>;
    }

}
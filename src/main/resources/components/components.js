import React from 'react';
import AdminMenu from './adminMenu';
import Component from './component';
import componentsStore from '../flux/stores/components';
import adminStore from '../flux/stores/admin';
import { withRouter } from 'react-router-dom';

import Socket from '../ws/socket';

import './components.scss';

class Components extends React.Component {

    constructor(props) {
        super(props);

        this.socket = new Socket();
        this.state = {
            components: componentsStore.getComponents()
        }
        this.render = this.render.bind(this);
        this.updateComponents = this.updateComponents.bind(this);
        this.goToIndex = this.goToIndex.bind(this);
    }

    componentWillUnmount() {
        this.socket.stop();
        componentsStore.removeListener('componentsUpdated', this.updateComponents);
        adminStore.removeListener('adminLoggedOut', this.goToIndex);
    }

    componentDidMount() {
        const token = adminStore.getAdmin().token;
        if (!token) {
            this.props.history.push('/');
            return;
        }

        this.socket.start(token);
        componentsStore.on('componentsUpdated', this.updateComponents);
        adminStore.on('adminLoggedOut', this.goToIndex);
    }

    updateComponents() {
        return this.setState({components: componentsStore.getComponents()});
    }

    goToIndex() {
        return this.props.history.push('/');
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
        return <div className="components_wrapper container">
                    <AdminMenu></AdminMenu>
                    <div className="components_body">
                        {data}
                    </div>
                </div>;
    }

}

export default withRouter(Components);
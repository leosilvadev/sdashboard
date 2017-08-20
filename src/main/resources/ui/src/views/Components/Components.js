import React from 'react';
import Component from './Component';
import componentsStore from '../../flux/stores/components';
import adminStore from '../../flux/stores/admin';
import { withRouter } from 'react-router-dom';

import Socket from '../../ws/socket';

class Components extends React.Component {

    constructor(props) {
        super(props);

        this.socket = new Socket();
        this.state = {
            components: componentsStore.getComponents()
        }
        this.render = this.render.bind(this);
        this.goToIndex = this.goToIndex.bind(this);
        this.updateComponents = this.updateComponents.bind(this);
    }

    componentWillUnmount() {
        this.socket.stop();
        componentsStore.removeListener('componentsUpdated', this.updateComponents);
    }

    componentDidMount() {
        const token = adminStore.getAdmin().token;
        if (!token) {
            this.goToIndex();
            return;
        }
        this.socket.start(token);
        componentsStore.on('componentsUpdated', this.updateComponents);
    }

    goToIndex() {
        return this.props.history.push('/login');
    }

    updateComponents() {
        return this.setState({components: componentsStore.getComponents()});
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
        return <div className="components_body">
                {data}
               </div>;
    }

}

export default withRouter(Components);
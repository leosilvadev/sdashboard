import React from 'react';
import Component from './component';
import ComponentsMenu from './componentsMenu';
import componentsStore from '../flux/stores/components';

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

    render() {
        const {components} = this.state;
        const data = components.map(component =>
            <Component key={component._id} component={component}>
            </Component>
        );
        return  <div className="components_wrapper container">
                    <ComponentsMenu />
                    <div className="components_body">
                        {data}
                    </div>
                </div>;
    }

}
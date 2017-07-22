import React from 'react';
import Component from './component';
import {registerComponent} from '../flux/actions/components';
import componentsStore from '../flux/stores/components';

export default class Components extends React.Component {

    constructor(props) {
        super(props);

        this.component = {};
        this.register = this.register.bind(this);
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

    register() {
        const component = {
            name: this.component.name.value,
            tasks: [{
                url: this.component.taskUrl.value,
                frequency: parseInt(this.component.frequency.value)
            }]
        }
        registerComponent(component);
    }

    render() {
        const {components} = this.state;
        const data = components.map(component =>
            <Component key={component._id} id={component._id} status={component.status}>
                {component.name} - {component.datetime}
            </Component>
        );
        return  <div className="components_wrapper">
                    <div className="components_header">
                        <input id="componentName" ref={(name) => this.component.name = name} />
                        <input id="componentTaskUrl" ref={(url) => this.component.taskUrl = url} />
                        <input id="componentTaskFrequency" ref={(frequency) => this.component.frequency = frequency} type="number" />
                        <button onClick={this.register}>Register</button>
                    </div>
                    <div className="components_body">
                        {data}
                    </div>
                </div>;
    }

}
import React from 'react';
import { Form } from 'formsy-react';
import Input from './input';
import {registerComponent} from '../flux/actions/components';

import './componentsMenu.scss';

const ComponentsMenu = React.createClass({

    getInitialState() {
        return { canSubmit: false };
    },

    enableButton() {
        this.setState({ canSubmit: true });
    },

    disableButton() {
        this.setState({ canSubmit: false });
    },

    register(data) {
        const component = {
            name: data.name,
            tasks: [{
                url: data.url,
                frequency: parseInt(data.frequency)
            }]
        }
        registerComponent(component);
    },

    render() {
        return (
            <div className="components_header">
                <Form onSubmit={this.register} onValid={this.enableButton} onInvalid={this.disableButton} className="login">
                    <div className="row">
                        <div className="col-sm-8">
                            <Input value="" name="name" title="Name" validationError="This is not a valid name" required />
                        </div>
                        <div className="col-sm-4">
                            <Input value="" name="frequency" title="Frequency" type="number" validationError="This is not a valid frequency" required />
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-sm-8">
                            <Input value="" name="url" title="Status Url" validationError="This is not a valid url" required />
                        </div>
                        <div className="components_commands col-sm-4">
                            <button type="submit" disabled={!this.state.canSubmit} className="btn btn-primary">Submit</button>
                        </div>
                    </div>
                  </Form>
                </div>
            );
    }
});

export default ComponentsMenu;
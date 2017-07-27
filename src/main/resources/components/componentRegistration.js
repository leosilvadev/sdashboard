import React from 'react';
import { Button, Modal } from 'react-bootstrap';
import { Form } from 'formsy-react';
import Input from './input';

import {registerComponent} from '../flux/actions/components';

export default class ComponentRegistration extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            show: false,
            canSubmit: false
        };
        this.open = this.open.bind(this);
        this.close = this.close.bind(this);
        this.register = this.register.bind(this);
        this.enableButton = this.enableButton.bind(this);
        this.disableButton = this.disableButton.bind(this);
    }

    open() {
        this.setState({show: true});
    }

    close() {
        this.setState({show: false});
    }

    enableButton() {
        this.setState({ canSubmit: true });
    }

    disableButton() {
        this.setState({ canSubmit: false });
    }

    register(data) {
        const component = {
            name: data.name,
            tasks: [{
                url: data.url,
                frequency: parseInt(data.frequency)
            }]
        }
        registerComponent(component);
        this.close();
    }

    render() {
        return <span>
        <Button bsStyle="primary" onClick={this.open}>
            Register new
        </Button>

        <Modal show={this.state.show} onHide={this.close}>
            <Form onSubmit={this.register} onValid={this.enableButton} onInvalid={this.disableButton} className="login">
                        <Modal.Header closeButton>
                    <Modal.Title>New component</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="row">
                        <div className="col-sm-8">
                            <Input value="" name="name" title="Name" validationError="This is not a valid name" required />
                        </div>
                        <div className="col-sm-4">
                            <Input value="" name="frequency" title="Frequency" type="number" validationError="This is not a valid frequency" required />
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-sm-12">
                            <Input value="" name="url" title="Status Url" validationError="This is not a valid url" required />
                        </div>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button type="submit" disabled={!this.state.canSubmit} bsStyle="primary">Register</Button>
                </Modal.Footer>
            </Form>
        </Modal>
        </span>
    }

}
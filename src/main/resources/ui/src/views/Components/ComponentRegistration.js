import React, {Component} from 'react';
import { Form } from 'formsy-react';

import {
  Button,
  Card,
  CardBlock,
  CardFooter,
  CardTitle,
  CardHeader
} from "reactstrap";

import Input from '../../components/Input/Input';
import { withRouter } from 'react-router-dom';

import {registerComponent} from '../../flux/actions/components';

class ComponentRegistration extends Component {

    constructor(props) {
        super(props);

        this.state = {
            show: false,
            canSubmit: false
        };
        this.register = this.register.bind(this);
        this.enableButton = this.enableButton.bind(this);
        this.disableButton = this.disableButton.bind(this);
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
        this.props.history.push('/components/all');
    }

    render() {
        return  <Form onSubmit={this.register} onValid={this.enableButton} onInvalid={this.disableButton} className="login">
                    <Card>
                        <CardHeader>New component</CardHeader>
                        <CardBlock>
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
                        </CardBlock>
                        <CardFooter>
                            <Button type="submit" disabled={!this.state.canSubmit} bsStyle="primary">Register</Button>
                        </CardFooter>
                    </Card>
                </Form>;
    }
}

export default withRouter(ComponentRegistration);
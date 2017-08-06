import React from 'react';
import adminStore from '../flux/stores/admin';
import { login } from '../flux/actions/admin';
import { Modal, Button } from 'react-bootstrap';
import { Form } from 'formsy-react';
import Input from './input';
import { withRouter } from 'react-router-dom';

class Login extends React.Component {

    constructor(props) {
        super(props);

        this.state = {}
        this.componentDidMount = this.componentDidMount.bind(this);
        this.doLogin = this.doLogin.bind(this);
    }

    componentDidMount() {
        adminStore.on('adminAuthenticated', () => {
            this.props.history.push('/components');
        });
    }

    doLogin(user) {
        login(user);
    }

    render() {
        return (
            <div className="static-modal">
                <Form onSubmit={this.doLogin} onValid={this.enableButton} onInvalid={this.disableButton} className="login">
                    <Modal.Dialog>
                        <Modal.Header>
                            <Modal.Title>SDashboard - Login</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <div className="row">
                                <div className="col-sm-8">
                                    <Input value="" name="username" title="Username" validationError="This is not a valid username" required />
                                </div>
                                <div className="col-sm-4">
                                    <Input value="" name="password" title="Password" type="password" validationError="This is not a valid password" required />
                                </div>
                            </div>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button>Close</Button>
                            <Button type="submit" bsStyle="primary">Login</Button>
                        </Modal.Footer>
                    </Modal.Dialog>
                </Form>
            </div>
        );
    }
}

export default withRouter(Login);
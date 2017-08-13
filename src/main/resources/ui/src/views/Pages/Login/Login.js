import React, {Component} from "react";
import {Container, Row, Col, CardGroup, Card, CardBlock, Button, Input, InputGroup, InputGroupAddon} from "reactstrap";
import adminStore from '../../../flux/stores/admin';
import { login } from '../../../flux/actions/admin';

class Login extends Component {

    constructor(props) {
        super(props);

        this.state = {}
        this.user = {};
        this.componentDidMount = this.componentDidMount.bind(this);
        this.doLogin = this.doLogin.bind(this);
    }

    componentDidMount() {
        adminStore.on('adminAuthenticated', () => {
            this.props.history.push('/dashboard');
        });
    }

    doLogin() {
        const user = {
            username: this.username.value,
            password: this.password.value
        };
        login(user);
    }

  render() {
    return (
      <div className="app flex-row align-items-center">
        <Container>
          <Row className="justify-content-center">
            <Col md="8">
              <CardGroup className="mb-0">
                <Card className="p-4">
                  <CardBlock className="card-body">
                    <h1>Login</h1>
                    <p className="text-muted">Sign In to your account</p>
                    <InputGroup className="mb-3">
                      <InputGroupAddon><i className="icon-user"></i></InputGroupAddon>
                      <Input type="text" placeholder="Username" getRef={(input) => { this.username = input; }}/>
                    </InputGroup>
                    <InputGroup className="mb-4">
                      <InputGroupAddon><i className="icon-lock"></i></InputGroupAddon>
                      <Input type="password" placeholder="Password" getRef={(input) => { this.password = input; }}/>
                    </InputGroup>
                    <Row>
                      <Col xs="6">
                        <Button color="primary" className="px-4" onClick={this.doLogin}>Login</Button>
                      </Col>
                      <Col xs="6" className="text-right">
                        <Button color="link" className="px-0">Forgot password?</Button>
                      </Col>
                    </Row>
                  </CardBlock>
                </Card>
              </CardGroup>
            </Col>
          </Row>
        </Container>
      </div>
    );
  }
}

export default Login;

import React, {Component} from 'react';
import {Switch, Route, Redirect} from 'react-router-dom';
import {Container} from 'reactstrap';
import Header from '../../components/Header/';
import Sidebar from '../../components/Sidebar/';
import Breadcrumb from '../../components/Breadcrumb/';
import Aside from '../../components/Aside/';
import Footer from '../../components/Footer/';
import Dashboard from '../../views/Dashboard/';
import Components from '../../views/Components/Components';
import ComponentRegistration from '../../views/Components/ComponentRegistration';

import adminStore from '../../flux/stores/admin';

class Full extends Component {

    constructor(props) {
        super(props);
        this.render = this.render.bind(this);
        this.goToIndex = this.goToIndex.bind(this);
    }

    componentWillUnmount() {
        adminStore.removeListener('adminLoggedOut', this.goToIndex);
    }

    componentDidMount() {
        const token = adminStore.getAdmin().token;
        if (!token) {
            this.props.history.push('/login');
            return;
        }

        adminStore.on('adminLoggedOut', this.goToIndex);
    }

    goToIndex() {
        return this.props.history.push('/');
    }

  render() {
    return (
      <div className="app">
        <Header />
        <div className="app-body">
          <Sidebar {...this.props}/>
          <main className="main">
            <Breadcrumb />
            <Container fluid>
              <Switch>
                <Route path="/dashboard" name="Dashboard" component={Dashboard}/>
                <Route exact path="/components" name="Components" component={Components}/>
                <Route exact path="/components/all" name="Components" component={Components}/>
                <Route exact path="/components/new" name="Register Component" component={ComponentRegistration}/>
                <Redirect from="/" to="/dashboard"/>
              </Switch>
            </Container>
          </main>
        </div>
        <Footer />
      </div>
    );
  }
}

export default Full;

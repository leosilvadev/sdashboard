import React from 'react';
import ReactDOM from 'react-dom';
import Login from './components/login';
import Components from './components/components';
import { HashRouter, Route } from 'react-router-dom'

import "bootstrap-sass/assets/stylesheets/_bootstrap.scss";

const app = <HashRouter>
                <div>
                    <Route exact path="/" component={Login}/>
                    <Route exact path="/components" component={Components}/>
                </div>
            </HashRouter>;

ReactDOM.render(app, document.getElementById('content'))
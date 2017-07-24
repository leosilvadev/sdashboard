import React from 'react';
import ReactDOM from 'react-dom';
import Components from './components/components';
import socket from './ws/socket';

import "bootstrap-sass/assets/stylesheets/_bootstrap.scss";

ReactDOM.render(<Components />, document.getElementById('content'))
import React from 'react';
import ReactDOM from 'react-dom';
import Components from './components/components';
import socket from './ws/socket';

ReactDOM.render(<Components />, document.getElementById('content'))
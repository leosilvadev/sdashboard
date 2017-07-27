import SockJS from 'sockjs-client';
import * as evt from '../flux/events';
import {addComponent} from '../flux/actions/components';

const sock = new SockJS('http://localhost:8080/ws/dashboard');

sock.onopen = function() {
  console.log('open');
};

sock.onmessage = function(e) {
    const {component, status, datetime, data, error} = JSON.parse(e.data);
    const comp = Object.assign({}, component, {status, datetime, data, error});
    addComponent(comp);
};

sock.onclose = function() {
  console.log('close');
};
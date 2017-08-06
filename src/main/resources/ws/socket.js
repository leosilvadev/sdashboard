import SockJS from 'sockjs-client';
import * as evt from '../flux/events';
import {addComponent} from '../flux/actions/components';

export default class Socket {

    start(token) {
        const sock = new SockJS(`http://localhost:8080/ws/v1/dashboard?token=${token}`);

        sock.onopen = function() {
          console.log('open');
        };

        sock.onmessage = function(e) {
            const {component, status, datetime, data, error, socket_error} = JSON.parse(e.data);
            if (socket_error) {
                console.error(socket_error);
                return;
            }
            const comp = Object.assign({}, component, {status, datetime, data, error});
            addComponent(comp);
        };

        sock.onclose = function() {
          console.log('close');
        };
    }

}
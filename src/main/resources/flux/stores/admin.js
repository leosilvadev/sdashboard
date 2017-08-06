import axios from 'axios';
import EventEmitter from 'events';
import dispatcher from '../dispatcher';
import * as evt from '../events';

class AdminStore extends EventEmitter {

    constructor() {
        super();

        this.admin = {};
        this.login = this.login.bind(this);
        this.action = this.action.bind(this);
        this.getAdmin = this.getAdmin.bind(this);
    }

    login(user) {
        axios.post('/api/v1/auth', user)
          .then(response => {
            console.log(`Admin ${user.username} authenticated!`);
            this.admin = Object.assign({}, user, {token: response.data.token});
            this.emit('adminAuthenticated');
          })
          .catch(error => {
            console.error(error);
          });
    }

    getAdmin() {
        return this.admin;
    }

    action({type, payload}) {
        switch(type) {
            case evt.LOGIN:
                this.login(payload);
                break;
        }
    }

}

const store = new AdminStore();

dispatcher.register(store.action);

export default store;
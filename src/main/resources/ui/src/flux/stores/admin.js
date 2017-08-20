import axios from 'axios';
import EventEmitter from 'events';
import dispatcher from '../dispatcher';
import * as evt from '../events';

const persistUser = user => {
    localStorage.setItem('sdashboardUser', user);
}

const restoreUser = () => {
    try {
        const admin = JSON.parse(localStorage.getItem('sdashboardUser'));
        if (admin.token) {
            return admin;
        }
        persistUser(null);
        return {};
    } catch(ex) {
        return {};
    }
}

class AdminStore extends EventEmitter {

    constructor() {
        super();

        this.admin = restoreUser();
        this.login = this.login.bind(this);
        this.action = this.action.bind(this);
        this.getAdmin = this.getAdmin.bind(this);
    }

    login(user) {
        axios.post('/api/v1/auth', user)
          .then(response => {
            console.log(`Admin ${user.username} authenticated!`);
            const {username} = user;
            const {token} = response.data;
            this.admin = Object.assign({}, {username}, {token});
            persistUser(JSON.stringify(this.admin));
            this.emit('adminAuthenticated');
          })
          .catch(error => {
            console.error(error);
          });
    }

    logout() {
        this.admin = {};
        persistUser(null);
        this.emit('adminLoggedOut');
    }

    getAdmin() {
        return this.admin;
    }

    action({type, payload}) {
        switch(type) {
            case evt.LOGIN:
                this.login(payload);
                break;
            case evt.LOGOUT:
                this.logout();
                break;
        }
    }

}

const store = new AdminStore();

dispatcher.register(store.action);

export default store;
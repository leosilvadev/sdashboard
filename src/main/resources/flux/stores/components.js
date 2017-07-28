import axios from 'axios';
import EventEmitter from 'events';
import dispatcher from '../dispatcher';
import * as evt from '../events';

class ComponentsStore extends EventEmitter {

    constructor() {
        super();

        this.components = [];
        this.action = this.action.bind(this);
    }

    addComponent(component) {
        const index = this.components.findIndex(c => {
            return component._id === c._id;
        });
        if (index >= 0) {
            this.components[index] = component;
        } else {
            console.log('Pushing new component... '+ component.name);
            this.components.push(component);
        }
        this.emit('componentsUpdated');
    }

    registerComponent(component) {
        axios.post('/api/v1/components', component)
          .then(response => {
            console.log(`Component ${component.name} registered successfully!`);
          })
          .catch(error => {
            console.log(error);
          });
    }

    getComponents() {
        return this.components.sort((c1, c2) => c1.name > c2.name);
    }

    action({type, payload}) {
        switch(type) {
            case evt.ADD_COMPONENT:
                this.addComponent(payload);
                break;

            case evt.REGISTER_COMPONENT:
                this.registerComponent(payload);
                break;
        }
    }

}

const store = new ComponentsStore();

dispatcher.register(store.action);

export default store;
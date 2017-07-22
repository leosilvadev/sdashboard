import dispatcher from '../dispatcher';
import * as evt from '../events';

export function addComponent(component) {
    dispatcher.dispatch({type: evt.ADD_COMPONENT, payload: component});
}

export function registerComponent(component) {
    dispatcher.dispatch({type: evt.REGISTER_COMPONENT, payload: component});
}
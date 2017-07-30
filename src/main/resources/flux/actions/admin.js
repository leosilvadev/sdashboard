import dispatcher from '../dispatcher';
import * as evt from '../events';

export function login(user) {
    dispatcher.dispatch({type: evt.LOGIN, payload: user});
}

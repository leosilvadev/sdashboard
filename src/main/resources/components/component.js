import React from 'react';
require('./component.scss');

export default class Component extends React.Component {

    render() {
        return <div id={this.props.id} className={'component ' + this.props.status}>{this.props.children}</div>
    }

}
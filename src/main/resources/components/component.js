import React from 'react';
import ComponentData from './componentData';

import './component.scss';

export default class Component extends React.Component {

    render() {
        const component = this.props.component;
        return  <div id={component.id} className={`panel component ${component.status}`}>
                    <div className='panel-heading'>{component.name} - {component.status.toUpperCase()} - {component.datetime}</div>
                    <div className="panel-body">
                        <ComponentData data={component.data} error={component.error}/>
                    </div>
                </div>
    }

}
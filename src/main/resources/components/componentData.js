import React from 'react';
import Highlight from 'react-highlight';
import { Table } from 'react-bootstrap';

import './componentData.scss';

export default class ComponentData extends React.Component {

    constructor(props) {
        super(props);

        this.objectToComponents = this.objectToComponents.bind(this);
        this.state = {
            error: this.props.error,
            data: this.props.data
        };
    }

    objectToComponents(data) {
        const keys = Object.keys(data);
        return keys.map(key => {
            const value = data[key];
            return {key, value};
        }).map(({key, value}) => {
            if (typeof value === 'object') {
                value = <Highlight>{JSON.stringify(value)}</Highlight>;
            }

            return  <tr key={key} className="component">
                        <td className="component_label">{key}</td>
                        <td className="component_value">{value}</td>
                    </tr>
        });
    }

    render() {
        let data = this.state.error;

        if (data) {
            try {
                data = JSON.parse(data);

            } catch(ex) {
                return <Highlight>{data}</Highlight>;
            }

        } else {
            data = this.state.data;

        }

        const components = this.objectToComponents(data);
        return <div>
                <Table striped bordered condensed hover>
                    <thead>
                        <tr>
                            <th className="component_label">Property</th>
                            <th className="component_value">Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        {components}
                    </tbody>
                </Table>
               </div>;
    }

}
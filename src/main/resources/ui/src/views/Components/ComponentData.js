import React, {Component} from 'react';
import Highlight from 'react-highlight';
import { Table, Jumbotron } from 'reactstrap';

import './ComponentData.scss';

export default class ComponentData extends Component {
b
    constructor(props) {
        super(props);

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
            let id = value;
            if (typeof value === 'object') {
                const json = JSON.stringify(value);
                id = json;
                value = <span>{json}</span>;
            }

            return  <tr key={`component_${key}_${id}`} className="component">
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
                return <span key={data}>{data}</span>;
            }

        } else {
            data = this.state.data;

        }

        const components = this.objectToComponents(data);
        return <div>
                <Table size="sm" responsive bordered striped>
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
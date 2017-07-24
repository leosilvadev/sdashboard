import React from 'react';

export default class ComponentData extends React.Component {

    render() {
        const data = this.props.data;
        if (!data) {
            return <div></div>;
        }

        const keys = Object.keys(data);
        const components = keys.map(key => {
            const value = data[key];
            return {key, value};

        }).map(({key, value}) => {
            return  <div key={key} className="row">
                        <div className="col-sm-4">{key}</div>
                        <div className="col-sm-8">{value}</div>
                    </div>

        });
        return  <div>
                    {components}
                </div>;
    }

}
import React from 'react';
import ComponentData from './ComponentData';

import {
  Card,
  CardBlock,
  CardFooter,
  CardTitle,
  CardHeader
} from "reactstrap";

import './Component.scss';

export default class Component extends React.Component {

    render() {
        const component = this.props.component;
        return  <Card className={`component ${component.status}`}>
                    <CardHeader>Last update: {component.datetime}</CardHeader>
                    <CardBlock>
                        <CardTitle>{component.name} - {component.status.toUpperCase()}</CardTitle>
                        <ComponentData data={component.data} error={component.error}/>
                    </CardBlock>
                    <CardFooter>Footer</CardFooter>
                </Card>;
    }

}
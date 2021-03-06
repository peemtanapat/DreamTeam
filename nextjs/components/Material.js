import React from 'react'
import 'isomorphic-fetch'
import axios from 'axios';

export default class Material extends React.Component {

    constructor() {
        super()
        this.state = {
            material: []
        }
    }

    async componentDidMount() {
        let {data} = await axios.get('http://dreamteam-gateway.mybluemix.net/material-service/material/1')
        this.setState({ material: {data}.data })
    }

    render() {
        return (
            <div>
                <h1>Material</h1>
                <table className="table table-hover">
                    <thead className="thead-dark">
                        <tr>
                            <th scope="col">Id</th>
                            <th scope="col">Name</th>
                            <th scope="col">Path</th>
                            <th scope="col">SubjectId</th>
                            <th scope="col">Uploader</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th scope="row"> {this.state.material.map(materials => <h4>{materials.materialId}</h4>)} </th>
                            <td> {this.state.material.map(materials => <h4>{materials.materialName}</h4>)} </td>
                            <td> {this.state.material.map(materials => <h4>{materials.materialPath}</h4>)} </td>
                            <td> {this.state.material.map(materials => <h4>{materials.subjectId}</h4>)} </td>
                            <td> {this.state.material.map(materials => <h4>{materials.uploader}</h4>)} </td>
                        </tr>
                    </tbody>    
                </table>

            </div>
        )
    }
}
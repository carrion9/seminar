import React, { Component } from 'react';
import './Seminar.css';
import { Radio, Form, Input, Button, Icon, Select, Col, Table, notification, Row, DatePicker, Avatar } from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { getSeminarById, deleteItem } from '../util/APIUtils';
import { formatDate, formatDateTime } from '../util/Helpers';
import { withRouter } from 'react-router-dom';

const Option = Select.Option;
const RadioGroup = Radio.Group;

class SeminarCreator extends Component{
    
    render(){
        return(
            <Link className="creator-link" to={`/user/${this.state.seminar.createdBy.username}`}>
                <Avatar className="seminar-creator-avatar"
                        style={{ backgroundColor: getAvatarColor(this.state.seminar.createdBy.name)}} >
                    {this.state.seminar.createdBy.name[0].toUpperCase()}
                </Avatar>
                <span className="seminar-creator-name">
                    {this.state.seminar.createdBy.name}
                </span>
                <span className="seminar-creator-username">
                    @{this.state.seminar.createdBy.username}
                </span>
                <span className="seminar-creation-date">
                    {formatDateTime(this.state.seminar.date)}
                </span>
            </Link>
            );
    }
}
/*****************************************************
//TODO fetch actuall data. Setted to some default dums
*****************************************************/
class Seminar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            columnsS : [
            {
                title: "Specialties",
                dataIndex: "key",
                sorter: true,
                key: "key",
                render: (key, spec) => (
                      <span>{spec.name}</span>
                  )
            }],
            columnsT: [
            {
                title: "Trainees",
                dataIndex: "key",
                sorter: true,
                key: "key",
                render: (key, trainee) => (
                      <span>{trainee.surname} {trainee.name}</span>
                  )
            }],
            isLoading: false,
            seminar: {},
            specialties: [],
            trainees: []
        };
        this.getSeminar = this.getSeminar.bind(this);
        this.setColumns = this.setColumns.bind(this);
    }

    getSeminar(){
        let promise;

        promise = getSeminarById("1");

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });
        promise
            .then(response => {
                this.setState({
                    seminar: response,
                    specialties: response._embedded.seminarSpecialties.map( x => x.specialty),
                    trainees: response._embedded.seminarTrainees.map( x => x.trainee),
                    isLoading: false
                })
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }


    setColumns(){
        const columns = [{
            title: "Name/Type",
            dataIndex: "key",
            sorter: true,
            key: "key",
            render: (trainee) => (
                  <span>{trainee.surname} {trainee.name}</span>
              )
        }]
        return columns.push.apply(columns, this.state.specialties.map(spec => 
                    ({
                      title: spec.name,
                      dataIndex: spec.name,
                      sorter: true,
                      key: spec.name
                    })
                ));
    }

    componentWillMount() {
        this.getSeminar();
        // this.getSpecialities();
        // this.getTrainees();
        // this.setColumns();
    }


    render() {
       
        return (
            <div className="seminar-container">
                <h1 className="page-title">Seminar {this.state.seminar.name}</h1>
                <div className="seminar-content">
                    <div className="seminar-info">
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="nameTitle" class="seminar-tag">
                                    Seminar's Name: 
                                </span>
                            </Col>
                            <Col span={12}>
                                <span label="name">
                                    {this.state.seminar.name}
                                </span>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="dateTitle" class="seminar-tag">
                                    Taking place at:
                                </span>
                            </Col>
                            <Col span={12}>
                                <span label="date">
                                    {formatDate(this.state.seminar.date)}
                                </span>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="seminarTypeTitle" class="seminar-tag">
                                    Seminar's Type:
                                </span>
                            </Col>
                            <Col span={12}>
                                <span label="seminarType">
                                    {this.state.seminar.seminarType}
                                </span>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="createdTitle" class="seminar-tag">
                                    Created:
                                </span>
                            </Col>
                            <Col span={12}>
                                <span label="created" >
                                    {this.state.seminar.createdBy} at {formatDate(this.state.seminar.createdAt)}
                                </span>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="updatedTitle" class="seminar-tag">
                                    Last edit:
                                </span>
                            </Col>
                            <Col span={12}>
                                <span label="updated" >
                                    {this.state.seminar.updatedBy} at {formatDate(this.state.seminar.updatedAt)}
                                </span>
                            </Col>
                        </Row>
                    </div>
                    <div className="specialties-list">
                        <Table
                            columns={this.state.columnsS} 
                            dataSource={this.state.specialties}
                        />
                    </div>
                    <div className="trainees-list">
                        <Table 
                            columns={this.state.columnsT} 
                            dataSource={this.state.trainees}
                        />
                    </div>
                </div>
            </div>
        );
    }
}


export default withRouter(Seminar);
import React, { Component } from 'react';
import './Seminar.css';
import { Radio, Form, Input, Button, Icon, Select, Col, Table, Popconfirm, message, notification, Row, DatePicker, Avatar } from 'antd';
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
            id: this.props.match.params.id,
            pagination: false,
            columnsS : [
            {
                title: "Label",
                dataIndex: "key",
                sorter: true,
                key: "key",
                render: (key, spec) => (
                      <span>{spec.name}</span>
                  )
            }],
            columnsT: [{
              title: 'AMA',
              dataIndex: 'ama',
              sorter: true,
              key: 'ama',
              render: (name, trainee ) => (
                  <Link to={"/trainees/" + trainee.key}>{trainee.ama}</Link>
              )
            }, {
              title: 'Full Name',
              dataIndex: 'name',
              sorter: true,
              key: 'name',
              render: (name, trainee ) => (
                  <Link to={"/trainees/" + trainee.key}>{trainee.surname} {trainee.name}</Link>
              )
            }, {
              title: 'Fathers Name',
              dataIndex: 'fathersName',
              sorter: true,
              key: 'fathersName',
            }, {
              title: 'Nationality',
              dataIndex: 'nationality',
              sorter: true,
              key: 'nationality',
            }, {
              title: 'Cart Type',
              dataIndex: 'cardType',
              sorter: true,
              key: 'cardType',
            }, {
              title: 'Cart Status',
              dataIndex: 'cardStatus',
              sorter: true,
              key: 'cardStatus',
            }, {
              title: 'Document Code',
              dataIndex: 'documentCode',
              sorter: true,
              key: 'documentCode',
            }, {
              key: 'remove',
              render: (trainee) => {
                  return (
                      <Popconfirm title="Remove from seminar?" onConfirm={this.confirm.bind(this, trainee)} onCancel={this.cancel.bind(this)} okText="Yes" cancelText="No">
                        <Button type="danger" >Remove</Button>
                      </Popconfirm>
                  )
              }
            }],
            isLoading: false,
            seminar: {},
            specialties: [],
            trainees: []
        };
        this.getSeminar = this.getSeminar.bind(this);
        this.setColumns = this.setColumns.bind(this);
    }


    confirm(trainee) {
        this.remove.bind(this, trainee);
        this.remove(trainee);
        message.success('Removed');
    }

    cancel(e) {
        message.error('Canceled remove');
    }

    remove(trainee){
        let promise;

        //promise = deleteItem(trainee);

        const trainees = this.state.trainees.filter(i => i.key !== trainee.key)
        this.setState({trainees})
    }

    getSeminar(){
        let promise;

        promise = getSeminarById(this.state.id);

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
                    specialties: response._embedded ? response._embedded.seminarSpecialties.map( x => x.specialty) : [],
                    trainees: response._embedded ? response._embedded.seminarTrainees.map( x => x.trainee) : [],
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
                            {...this.state}
                            title={() => {return "Specialties";} }
                            columns={this.state.columnsS} 
                            dataSource={this.state.specialties}
                            footer={() => {return (
                                <div className="table-footer">
                                    <Button className="add-button" type="Submit" >Add Specialty</Button>
                                </div>
                            )}}
                        />
                    </div>
                    <div className="trainees-list">
                        <Table 
                            {...this.state}
                            title={() => {return "Trainees";} }
                            columns={this.state.columnsT} 
                            dataSource={this.state.trainees}
                            footer={() => {return (
                                <div className="table-footer">
                                    <Button className="add-button" type="Submit" >Add Trainee</Button>
                                </div>
                            )}}
                        />
                    </div>
                </div>
            </div>
        );
    }
}


export default withRouter(Seminar);
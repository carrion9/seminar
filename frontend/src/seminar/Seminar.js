import React, { Component } from 'react';
import './Seminar.css';
import {
    Radio,
    Form,
    Input,
    Button,
    Icon,
    Select,
    Col,
    Table,
    Popconfirm,
    message,
    notification,
    Row,
    DatePicker,
    Avatar,
    Upload
} from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { getSeminarById, deleteItem, updateItem, getAttendance } from '../util/APIUtils';
import { formatDate, formatDateTime } from '../util/Helpers';
import { withRouter } from 'react-router-dom';

const FormItem = Form.Item;

class Seminar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            pagination: false,
            columnsS : [
            {
                title: 'Label',
                dataIndex: 'key',
                sorter: true,
                key: 'key',
                render: (key, spec) => (
                      <span>{spec.specialty.name}</span>
                  )
            }, {
              key: 'attendances',
              render: (seminar) => {
                  return (
                        <Button onClick={this.handleAttendance.bind(this)}>
                            Attendances
                        </Button>
                  )
              }
            }],
            columnsT: [{
              title: 'AMA',
              dataIndex: 'trainee.ama',
              sorter: true,
              key: 'ama',
              render: (ama, record ) => (
                  <Link to={"/trainee/" + record.key}>{ama}</Link>
              )
            }, {
              title: 'Full Name',
              dataIndex: 'trainee',
              sorter: true,
              key: 'name',
              render: (trainee) => (
                  <Link to={"/trainee/" + trainee.key}>{trainee.surname} {trainee.name}</Link>
              )
            },{
              title: 'Contractor',
              dataIndex: 'contractor',
              sorter: true,
              key: 'contractor',
              render: (contractor) => (
                    <Link to={"/contractor/" + contractor.key}>{contractor.name}</Link>
                )
            },{
              title: 'Specialty',
              dataIndex: 'specialty.name',
              sorter: true,
              key: 'specialty',
            },{
              title: 'Grade',
              dataIndex: 'grade',
              key: 'grade',
            },{
              title: 'Passed',
              dataIndex: 'passed',
              key: 'passed',
            }], 
            isLoading: false,
            seminar: {},
            specialties: [],
            trainSpec: [],
            isEdit: false
        };
        this.getSeminar = this.getSeminar.bind(this);
        this.handleEdit = this.handleEdit.bind(this);
        this.handleUpload = this.handleUpload.bind(this);
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

        promise = deleteItem(trainee);

        const trainees = this.state.trainees.filter(i => i.key !== trainee.key)
        this.setState({trainees})
    }

    update(){
        let promise;

        promise = updateItem(this.state.seminar);
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
                    specialties: response._embedded ? response._embedded.seminarSpecialties: [],
                    trainSpec: response._embedded ? response._embedded.seminarTrainees: [],
                    isLoading: false
                })
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

    handleEdit(){
        this.setState({
            isEdit: !this.state.isEdit
        })
    }

    handleInputChange(event, validationFun) {
        const target = event.target;
        const inputName = target.name;        
        const inputValue = target.value;
        const seminarEdit = this.state.seminar;
        seminarEdit[inputName] = inputValue;

        this.setState({
            seminar: seminarEdit
        });
    }

    handleAttendance(seminar) {
        getAttendance(this.state.seminar.key, 1);
    }

    handleUpload(seminar) {

    }

    componentWillMount() {
        this.getSeminar();
    }


    render() {
        let content;
        if (this.state.isEdit){
            content =(
                    <Form layout="inline" className="seminar-info"  onSubmit={this.update.bind(this)}>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="nameTitle" className="seminar-tag">
                                    Seminar's Name: 
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input 
                                        defaultValue={this.state.seminar.name}
                                        name="name"
                                        onChange={(event) => this.handleInputChange(event)}
                                        />
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="dateTitle" className="seminar-tag">
                                    Taking place at:
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input defaultValue={this.state.seminar.date}/>
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="seminarTypeTitle" className="seminar-tag">
                                    Seminar's Type:
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input defaultValue={this.state.seminar.seminarType}/>
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="createdTitle" className="seminar-tag">
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
                                <span label="updatedTitle" className="seminar-tag">
                                    Last edit:
                                </span>
                            </Col>
                            <Col span={12}>
                                <span label="updated" >
                                    {this.state.seminar.updatedBy} at {formatDate(this.state.seminar.updatedAt)}
                                </span>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}/>
                            <Col span={12}>
                                <FormItem>
                                     <Button htmlType="submit">
                                        Save
                                    </Button>
                                </FormItem>
                                <FormItem>
                                     <Button type="Submit" onClick={this.handleEdit}>
                                        Cancel
                                    </Button>
                                </FormItem>
                            </Col>
                        </Row>
                    </Form>
                )
        }else{
            content=(
                <div className="seminar-info">
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="nameTitle" className="seminar-tag">
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
                            <span label="dateTitle" className="seminar-tag">
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
                            <span label="seminarTypeTitle" className="seminar-tag">
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
                            <span label="createdTitle" className="seminar-tag">
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
                            <span label="updatedTitle" className="seminar-tag">
                                Last edit:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="updated" >
                                {this.state.seminar.updatedBy} at {formatDate(this.state.seminar.updatedAt)}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}/>
                        <Col span={6}>

                            <Upload
                                className="add-button"
                                name="seminar"
                                action={this.handleUpload}
                                showUploadList={false}>
                                <Button>
                                    <Icon type="upload" /> Upload Seminar
                                </Button>
                            </Upload>
                        </Col>
                        <Col span={6}>
                            <Button className="edit-seminar-button" type="Submit" onClick={this.handleEdit}>Edit</Button>
                        </Col>
                    </Row>
                    </div>
                )
        }
        return (
            <div className="seminar-container">
                <h1 className="page-title">Seminar {this.state.seminar.name}</h1>
                <div className="seminar-content">
                        {content}
                    
                    <div className="specialties-list">
                        <Table 
                            {...this.state}
                            title={() => {return ( 
                                <div className="table-header">
                                    <span className="table-title"> Specialities </span>
                                    <Button className="add-to-seminar-button" type="Submit" >Add Specialty</Button>
                                </div> 
                                )}}
                            columns={this.state.columnsS} 
                            dataSource={this.state.specialties}
                        />
                    </div> 
                    <br/>
                    <br/>
                    <div className="trainees-list">
                        <Table 
                            {...this.state}
                            title={() => {return ( 
                                <div className="table-header">
                                    <span className="table-title"> Trainees </span>
                                    <Button className="add-to-seminar-button" type="Submit" >Add Trainee</Button>
                                </div> 
                                )}}
                            columns={this.state.columnsT} 
                            dataSource={this.state.trainSpec}
                        />
                    </div>
                </div>
            </div>
        );
    }
}
export default withRouter(Seminar);


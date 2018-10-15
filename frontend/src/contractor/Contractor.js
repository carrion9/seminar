import React, { Component } from 'react';
import './Contractor.css';
import { Radio, Form, Input, Button, Icon, Select, Col, Table, Popconfirm, message, notification, Row, DatePicker, Avatar } from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { getContractorById, deleteItem } from '../util/APIUtils';
import { formatDate, formatDateTime } from '../util/Helpers';
import { withRouter } from 'react-router-dom';

const FormItem = Form.Item;

class Contractor extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            pagination: false,
            columnsT: [{
              title: 'AMA',
              dataIndex: 'trainee.ama',
              sorter: true,
              key: 'ama',
              render: (ama, record ) => (
                  <Link to={"/trainees/" + record.key}>{ama}</Link>
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
            contractor: {},
            specialties: [],
            trainSpec: [],
            isEdit: false
        };
        this.getContractor = this.getContractor.bind(this);
        this.handleEdit = this.handleEdit.bind(this);
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

    getContractor(){
        let promise;

        promise = getContractorById(this.state.id);

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });
        promise
            .then(response => {

                this.setState({
                    contractor: response,
                    specialties: response._embedded ? response._embedded.contractorSpecialties: [],
                    trainSpec: response._embedded ? response._embedded.contractorTrainees: [],
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

    componentWillMount() {
        this.getContractor();
    }


    render() {
        let content;
        if (this.state.isEdit){
            content =(
                    <Form layout="inline" className="contractor-info">
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="afmTitle" className="contractor-tag">
                                    AFM: 
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input defaultValue={this.state.contractor.afm} editable/>
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="nameTitle" className="contractor-tag">
                                    Name: 
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input defaultValue={this.state.contractor.name} editable/>
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="representativeNameTitle" className="contractor-tag">
                                    Representive Name:
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input defaultValue={this.state.contractor.representativeName}/>
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="phoneNumberTitle" className="contractor-tag">
                                    Phone Number:
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input defaultValue={this.state.contractor.phoneNumber}/>
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="emailTitle" className="contractor-tag">
                                    Email:
                                </span>
                            </Col>
                            <Col span={12}>
                                 <FormItem>
                                    <Input defaultValue={this.state.contractor.email}/>
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}/>
                            <Col span={12}>
                                <FormItem>
                                     <Button type="Submit">
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
                <div className="contractor-info">
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="afmTitle" className="contractor-tag">
                                AFM: 
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="afm">
                                {this.state.contractor.afm}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="nameTitle" className="contractor-tag">
                                Name: 
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="name">
                                {this.state.contractor.name}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="representativeNameTitle" className="contractor-tag">
                                Representive Name:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="representativeName">
                                {this.state.contractor.representativeName}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="phoneNumberTitle" className="contractor-tag">
                                Phone Number:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="phoneNumber">
                                {this.state.contractor.phoneNumber}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="emailTitle" className="contractor-tag">
                                Email:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="email" >
                                {this.state.contractor.email}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}/>
                        <Col span={12}>
                            <Button className="edit-contractor-button" type="Submit" onClick={this.handleEdit}>Edit</Button>
                        </Col>
                    </Row>
                    </div>
                )
        }
        return (
            <div className="contractor-container">
                <h1 className="page-title">Contractor {this.state.contractor.name}</h1>
                <div className="contractor-content">
                        {content} 
                    <br/>
                    <br/>
                    <div className="trainees-list">
                        <Table 
                            {...this.state}
                            title={() => {return ( 
                                <div className="table-header">
                                    <span className="table-title"> Trainees (?)</span>
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
export default withRouter(Contractor);


import React, { Component } from 'react';
import './Contractor.css';
import { Radio, Form, Input, Button, Icon, Select, Col, Table, Popconfirm, message, notification, Row, DatePicker, Avatar } from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { getContractorById, deleteItem, updateItem } from '../util/APIUtils';
import { formatDate, formatDateTime } from '../util/Helpers';
import { withRouter } from 'react-router-dom';
import LoadingIndicator from '../common/LoadingIndicator';

const FormItem = Form.Item;

class Contractor extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            pagination: false,
            isLoading: false,
            contractor: {},
            isEdit: false,
            columnsT: [{
              title: 'AMA',
              dataIndex: 'trainee',
              sorter: true,
              key: 'ama',
              render: (trainee ) => (
                  <Link to={"/trainee/" + trainee.key}>{trainee.ama}</Link>
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
              render: (passed) => {
                    if (passed)
                        return (<div style={{ color: '#008000' }}>Pass</div>)
                    else
                        return (<div style={{ color: '#FF0000' }}>Fail</div>)
                    
              }
            }]
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

    update(){
        this.setState({
            isLoading: true,
            isEdit: false,
        });
        let promise;

        promise = updateItem(this.state.contractor);
        promise
        .then(response => {
            notification.success({
                message: 'Seminar App',
                description: "Sucessfully saved cahnges!",
            }); 
            this.setState({
                isLoading: false,
                isEdit: false
            });
        })
        .catch(error => {
            notification.error({
                message: 'Seminar App',
                description: error.message || 'Sorry! Something went wrong. Please try again!'
            });
            this.setState({
                isLoading: false
            });
        });
    }

    getContractor(){
        this.setState({
            isLoading: true
        });
        let promise;

        promise = getContractorById(this.state.id);

        if(!promise) {
            return;
        }

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

    handleInputChange(event, validationFun) {
        const target = event.target;
        const inputName = target.name;        
        const inputValue = target.value;
        const contractorEdit = this.state.contractor;
        contractorEdit[inputName] = inputValue;

        this.setState({
            contractor: contractorEdit
        });
    }

    componentWillMount() {
        this.getContractor();
    }


    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />
        }
        let content;
        if (this.state.isEdit){
            content =(
                    <Form layout="inline" className="contractor-info" onSubmit={this.update.bind(this)}>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="nameTitle" className="contractor-tag">
                                    Name: 
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input 
                                        defaultValue={this.state.contractor.name} 
                                        name="name"
                                        onChange={(event) => this.handleInputChange(event)}
                                    />
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="afmTitle" className="contractor-tag">
                                    AFM: 
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input 
                                        defaultValue={this.state.contractor.afm} 
                                        name="afm"
                                        onChange={(event) => this.handleInputChange(event)}
                                        />
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="doyTitle" className="contractor-tag">
                                    DOY: 
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input 
                                        defaultValue={this.state.contractor.doy} 
                                        name="doy"
                                        onChange={(event) => this.handleInputChange(event)}
                                        />
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="activityTitle" className="contractor-tag">
                                    activity: 
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input 
                                        defaultValue={this.state.contractor.activity} 
                                        name="activity"
                                        onChange={(event) => this.handleInputChange(event)}
                                        />
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <span label="addressTitle" className="contractor-tag">
                                    Address: 
                                </span>
                            </Col>
                            <Col span={12}>
                                <FormItem>
                                    <Input 
                                        defaultValue={this.state.contractor.address} 
                                        name="address"
                                        onChange={(event) => this.handleInputChange(event)}
                                        />
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
                                    <Input 
                                        defaultValue={this.state.contractor.representativeName}
                                        name="representativeName"
                                        onChange={(event) => this.handleInputChange(event)}
                                        />
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
                                    <Input 
                                        defaultValue={this.state.contractor.phoneNumber}
                                        name="phoneNumber"
                                        onChange={(event) => this.handleInputChange(event)}
                                        />
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
                                    <Input 
                                        defaultValue={this.state.contractor.email}
                                        name="email"
                                        onChange={(event) => this.handleInputChange(event)}
                                        />
                                </FormItem>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}/>
                            <Col span={12}>
                                <FormItem>
                                     <Button htmlType="submit" type="primary">
                                        Save
                                    </Button>
                                </FormItem>
                                <FormItem>
                                     <Button type="danger" onClick={this.handleEdit}>
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
                            <span label="doyTitle" className="contractor-tag">
                                DOY: 
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="doy">
                                {this.state.contractor.doy}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="activityTitle" className="contractor-tag">
                                Activity:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="activity">
                                {this.state.contractor.activity}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="addressTitle" className="contractor-tag">
                                Address:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="address">
                                {this.state.contractor.address}
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
                            <Button className="edit-contractor-button" type="primary" onClick={this.handleEdit}>Edit</Button>
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

                    <div className="trainees-list">
                        <Table 
                            {...this.state}
                            title={() => {return ( 
                                <div className="table-header">
                                    <span className="table-title"> Trainees </span>
                                </div> 
                                )}}
                            columns={this.state.columnsT} 
                            dataSource={this.state.contractor._embedded.seminarTrainees}
                        />
                    </div>
                </div>
            </div>
        );
    }
}
export default withRouter(Contractor);


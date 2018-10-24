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
    Popover,
    Avatar,
    Upload
} from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { getSeminarById, deleteItem, updateItem, getAttendance, upload } from '../util/APIUtils';
import { formatDate, formatDateTime } from '../util/Helpers';
import { withRouter } from 'react-router-dom';
import moment from 'moment';

const FormItem = Form.Item;
const Option = Select.Option;

class Seminar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: this.props.match.params.id,
            pagination: false,
            columnsS : [
            {
                title: 'Label',
                dataIndex: 'specialty.name',
                sorter: true,
                key: 'name'
            }, {
                dataIndex: 'key',
                key: 'key',
                render: (key) => {
                  return (
                        <Button onClick={this.handleAttendance.bind(this, key)} >
                            Attendances
                        </Button>
                  )
              }
            }],
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
            }], 
            isLoading: false,
            seminar: {},
            specialties: [],
            trainSpec: [],
            isEdit: false,
            file: '',
            error: '',
            msg: ''
        };
        this.getSeminar = this.getSeminar.bind(this);
        this.handleEdit = this.handleEdit.bind(this);
        this.handleAddSpecialty = this.handleAddSpecialty.bind(this);
        this.handleAddTrainee = this.handleAddTrainee.bind(this);

        // this.uploadFile = this.uploadFile().bind(this);
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
        if (this.state.isEdit){
            this.getSeminar();
        }
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

    handleDateChange(event, validationFun) {
        if (event == null){
            return
        }
        const seminarEdit = this.state.seminar;
        seminarEdit.date = event._i;

        this.setState({
            seminar: seminarEdit
        });
    }

    handleAddSpecialty(){

    }

    handleAddTrainee(){

    }

    handleAttendance = (specialtyKey) => {
        let me;
        me = getAttendance(this.state.seminar.key, specialtyKey, "attendance-document-" + this.state.seminar.name.replace(/\s+/g, '-').toLowerCase() + ".docx");
        if(!me) {
            return;
        }
    };

    uploadFile = (file) => {
        this.setState({error: '', msg: ''});
        if(!file) {
            this.setState({error: 'Please upload a file.'})
            return;
        }

        let data = new FormData();
        data.append('file', file);
        data.append('seminarId', this.state.seminar.key);
        let me = upload(data);
        if(!me) {
            return;
        }
        me.then(response => {
            this.setState({error: '', msg: 'Sucessfully uploaded file'});
        })
    };

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
                                    <DatePicker 
                                        defaultValue={moment(formatDate(this.state.seminar.date), 'DD/MM/YYYY')}
                                        format='DD/MM/YYYY'
                                        name="date"
                                        onChange={(event) => this.handleDateChange(event)}
                                    />
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
                                    <Input 
                                        defaultValue={this.state.seminar.seminarType}
                                        name="seminarType"
                                        onChange={(event) => this.handleInputChange(event)}
                                    />
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
                        <Col span={12}>
                            <Upload
                                className="add-button"
                                name="seminar"
                                action={this.uploadFile}
                                showUploadList={false}>
                                <Button>
                                    <Icon type="upload" /> Upload Contractor
                                </Button>
                            </Upload>
                        </Col>
                        <Col span={12}>
                            <Button className="edit-seminar-button" type="Submit" onClick={this.handleEdit}>Edit</Button>
                        </Col>
                    </Row>
                    </div>
                )
        }

        let addSpecContent=(
            <Form layout="inline" onSubmit={this.handleAddSpecialty}>
                <FormItem>
                    <Input 
                        placeholder="Title" 
                        style={{ width: 600 }}
                    />
                </FormItem>
                <FormItem>
                  <Button>
                    Add
                  </Button>
                </FormItem>
            </Form>
        )

        let addTraineeContent=(
            <Form layout="inline" onSubmit={this.handleAddTrainee}>
                <FormItem>
                    <Input 
                        placeholder="Trainee's AMA" 
                        style={{ width: 400 }}
                    />
                </FormItem>
                <FormItem>
                    <Input 
                        placeholder="Contractor's AFM" 
                        style={{ width: 400 }}
                    />
                </FormItem>
                <FormItem>
                    <Select
                      defaultValue="Specialties"
                      style={{ width: 500 }}
                    >
                      {this.state.specialties.map(spec => <Option key={spec.key}>{spec.specialty.name}</Option>)}
                    </Select>
                </FormItem>
                <FormItem>
                  <Button>
                    Add
                  </Button>
                </FormItem>
            </Form>
        )

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
                                    <Popover content={addSpecContent} title="Add Specialty" trigger="click">
                                        <Button className="add-to-seminar-button" type="Submit" >Add Specialty</Button>
                                    </Popover>
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
                                    <Popover content={addTraineeContent} title="Add Trainee" trigger="click">
                                        <Button className="add-to-seminar-button" type="Submit" >Add Trainee</Button>
                                    </Popover>
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


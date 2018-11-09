import React, { Component } from 'react';
import './Seminar.css';
import {
    Form,
    Input,
    Button,
    Icon,
    Select,
    Col,
    Table,
    message,
    notification,
    Row,
    DatePicker,
    Popover,
    Upload
} from 'antd';
import { Link } from 'react-router-dom';
import { getSeminarById, deleteItem, updateItem, updateCost, insertItem, getAttendance, upload, getTraineeByAMA, getContractorByAFM, insertSeminarTraineeContractorSpecialty, getSpecialtyByName, insertSeminarSpecialty } from '../util/APIUtils';
import { formatDate } from '../util/Helpers';
import { withRouter } from 'react-router-dom';
import LoadingIndicator from '../common/LoadingIndicator';
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
                        <Button type='primary' onClick={this.handleAttendance.bind(this, key)} >
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
              render: (passed) => {
                    if (passed)
                        return (<div style={{ color: '#008000' }}>Passed</div>)
                    else
                        return (<div style={{ color: '#FF0000' }}>Failed</div>)
                    
              }
            },{
              title: 'Total Cost',
              key: 'cost',
              render: (record) => (
                    <Input name={record.key} defaultValue={record.cost} onPressEnter={this.updateCost.bind(this)} onChange={(event) => this.handleCostChange(event)}/>
                )
            }], 
            isLoading: false,
            seminar: {},
            specialties: [],
            trainSpec: [],
            isEdit: false,
            file: '',
            ama: '',
            afm: '',
            spec: '',
            costEdit: ''
        };
        this.getSeminar = this.getSeminar.bind(this);
        this.handleEdit = this.handleEdit.bind(this);
        this.handleCostChange = this.handleCostChange.bind(this);
        this.handleAddSpecialty = this.handleAddSpecialty.bind(this);
        this.handleAddTrainee = this.handleAddTrainee.bind(this);
        this.updateCost = this.updateCost.bind(this);
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
        this.setState({
            isLoading: true,
            isEdit: false,
        });
        let promise;

        promise = updateItem(this.state.seminar);
        promise
        .then(response => {
            notification.success({
                message: 'Seminar App',
                description: "Sucessfully saved cahnges!",
            }); 
            this.setState({
                isLoading: false
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

    updateCost(){
        if (!this.state.costEdit)
            return
        this.setState({
            isLoading: true,
        });
        let promise;

        promise = updateCost(this.state.costEdit.key, this.state.costEdit.cost);
        promise
        .then(response => {
            notification.success({
                message: 'Seminar App',
                description: "Sucessfully changed cost!",
            }); 
            this.setState({
                isLoading: false
            });
            this.getSeminar();
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

    getSeminar(){
        this.setState({
            isLoading: true
        });
        let promise;

        promise = getSeminarById(this.state.id);

        if(!promise) {
            return;
        }

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

    handleInputChangeGeneric(event, validationFun) {
        const target = event.target;
        const inputName = target.name;        
        const inputValue = target.value;

        this.setState({
            [inputName]: inputValue
        });
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

    handleCostChange(event) {
        if (!event)
            return
        const target = event.target;
        const inputKey = target.name;        
        const inputValue = target.value;

        this.setState({
            costEdit: {
                cost: inputValue,
                key: inputKey
            }
        });
    }

    handleSelectChange(inputValue) {
        
        this.setState({
                spec: inputValue
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
        if (!this.state.spec)
            return
        this.setState({
            isLoading: true
        });

        let searchPromise;
        searchPromise = getSpecialtyByName(this.state.spec);

        searchPromise
            .then(response => {

                let addPromise;
                addPromise = insertSeminarSpecialty(this.state.seminar.key, response.key)
                addPromise
                    .then(response => {

                        notification.success({
                                message: 'Seminar App',
                                description: "Sucessfully added",
                            }); 

                        this.setState({
                            isLoading: false
                        });
                        this.getSeminar();

                    }).catch(error => {
                        notification.error({
                            message: 'Seminar App',
                            description: error.message || 'Sorry! Something went wrong. Please try again!'
                        });
                        this.setState({
                            isLoading: false
                        });
                    });

            }).catch(error => {
                let addSpec = insertItem({name:this.state.spec}, 'specialties');
                addSpec 
                    .then(response => {
                        let addPromise;
                        addPromise = insertSeminarSpecialty(this.state.seminar.key, response.key)
                        addPromise
                            .then(response => {

                                notification.success({
                                        message: 'Seminar App',
                                        description: "Sucessfully added",
                                    }); 

                                this.setState({
                                    isLoading: false
                                });
                                this.getSeminar();

                            }).catch(error => {
                                notification.error({
                                    message: 'Seminar App',
                                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                                });
                                this.setState({
                                    isLoading: false
                                });
                            });

                    }).catch(error => {
                        notification.error({
                            message: 'Seminar App',
                            description: error.message || 'Sorry! Something went wrong. Please try again!'
                        });
                        this.setState({
                            isLoading: false
                        });
                    });
            });
    }

    handleAddTrainee(){
        this.setState({
            isLoading: true
        });

        let traineePromise;
        traineePromise = getTraineeByAMA(this.state.ama);

        traineePromise
            .then(response => {

                let contractorPromise;
                const traineeKey = response.key;
                contractorPromise = getContractorByAFM(this.state.afm);
                contractorPromise
                    .then(response => {

                            let addAllPromise;
                            addAllPromise = insertSeminarTraineeContractorSpecialty(this.state.seminar.key, traineeKey, response.key, this.state.spec)
                            addAllPromise
                                .then(response => {

                                    notification.success({
                                            message: 'Seminar App',
                                            description: "Sucessfully added",
                                        }); 

                                    this.setState({
                                        isLoading: false
                                    });
                                    this.getSeminar();

                                }).catch(error => {
                                    notification.error({
                                        message: 'Seminar App',
                                        description: error.message || 'Sorry! Something went wrong. Please try again!'
                                    });
                                    this.setState({
                                        isLoading: false
                                    });
                                });

                    }).catch(error => {

                        notification.error({
                            message: 'Seminar App',
                            description: error.message || 'Sorry! Contractor not found...'
                        });
                        this.setState({
                            isLoading: false
                        });
                    });
            }).catch(error => {
                notification.error({
                    message: 'Seminar App',
                    description: error.message || 'Sorry! Trainee not found...'
                });
                this.setState({
                    isLoading: false
                });
            });
    }

    handleAttendance = (specialtyKey) => {
        let me;
        me = getAttendance(this.state.seminar.key, specialtyKey, "attendance-document-" + this.state.seminar.name.replace(/\s+/g, '-').toLowerCase() + ".docx");
        if(!me) {
            return;
        }
    };

    uploadFile = (file) => {
        this.setState({
            isLoading: true,
        });
        if(!file) {
            notification.error({
                message: 'Seminar App',
                description: 'Please upload a file.'
            });
            return;
        }

        let data = new FormData();
        data.append('file', file);
        data.append('seminarId', this.state.seminar.key);
        let me = upload(data);
        if(!me) {
            return;
        }
        me
        .then(response => {
            notification.success({
                message: 'Seminar App',
                description: "Sucessfully uploaded file!",
            }); 
            this.getSeminar();
        })
        .catch(error => {
            notification.error({
                message: 'Seminar App',
                description: error.message || 'Sorry! Something went wrong. Please try again!'
            });
        });
    };

    componentWillMount() {
        this.getSeminar();
    }


    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />
        }
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
                                     <Button htmlType="submit" type='primary'>
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
                                <Button type='primary'>
                                    <Icon type="upload" /> Upload Contractor
                                </Button>
                            </Upload>
                        </Col>
                        <Col span={12}>
                            <Button className="edit-seminar-button" type='primary' onClick={this.handleEdit}>Edit</Button>
                        </Col>
                    </Row>
                    </div>
                )
        }

        let addSpecContent=(
            <Form layout="inline" onSubmit={this.handleAddSpecialty}>
                <FormItem>
                    <Input 
                        name="spec"
                        placeholder="Title" 
                        style={{ width: 600 }}
                        onChange={(event) => this.handleInputChangeGeneric(event)}
                    />
                </FormItem>
                <FormItem>
                  <Button htmlType="submit" type='primary'>
                    Add
                  </Button>
                </FormItem>
            </Form>
        )

        let addTraineeContent=(
            <Form layout="inline" onSubmit={this.handleAddTrainee}>
                <FormItem>
                    <Input 
                        name="ama"
                        placeholder="Trainee's AMA" 
                        style={{ width: 400 }}
                        onChange={(event) => this.handleInputChangeGeneric(event)} 
                    />
                </FormItem>
                <FormItem>
                    <Input 
                        name="afm"
                        placeholder="Contractor's AFM" 
                        style={{ width: 400 }}
                        onChange={(event) => this.handleInputChangeGeneric(event)}
                    />
                </FormItem>
                <FormItem>
                    <Select
                        name="spec"
                        defaultValue="Specialties"
                        style={{ width: 500 }}
                        onChange={(event) => this.handleSelectChange(event)}                      
                    >
                      {this.state.specialties.map(spec => <Option key={spec.specialty.key}>{spec.specialty.name}</Option>)}
                    </Select>
                </FormItem>
                <FormItem>
                  <Button htmlType="submit" type='primary'>
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
                                        <Button className="add-to-seminar-button" type="primary" >Add Specialty</Button>
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
                                        <Button className="add-to-seminar-button" type="primary">Add Trainee</Button>
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


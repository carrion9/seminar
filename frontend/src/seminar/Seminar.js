import React, {Component} from 'react';
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
    Upload,
    Popconfirm, Tag
} from 'antd';
import {Link} from 'react-router-dom';
import {
    getSeminarById,
    deleteItem,
    updateItem,
    updateCost,
    updateGrade,
    updatePassed,
    insertItem,
    getAttendance,
    upload,
    getTraineeByAMA,
    getContractorByAFM,
    insertSeminarTraineeContractorSpecialty,
    getSpecialtyByName,
    insertSeminarSpecialty, 
    insertSeminarContractor,
    getWelcomeDoc
} from '../util/APIUtils';
import {formatDate, humanize, reverseDate} from '../util/Helpers';
import {withRouter} from 'react-router-dom';
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
            specialtiesColumns: [
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
                            <Button type='primary' onClick={this.handleAttendance.bind(this, key)}>
                                Attendances
                            </Button>
                        )
                    }
                }, {
                    key: 'delete',
                    render: (trainee) => {
                        return (
                            <Popconfirm title="Are you sure delete this specialty?"
                                        onConfirm={this.confirm.bind(this, trainee, "specialties")}
                                        onCancel={this.cancel.bind(this)} okText="Yes" cancelText="No">
                                <Button type="danger">Delete</Button>
                            </Popconfirm>
                        )
                    }
                }],
            traineesColumns: [
                {
                    title: 'AMA',
                    dataIndex: 'trainee',
                    sorter: true,
                    key: 'ama',
                    render: (trainee) => (
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
                }, {
                    title: 'Contractor',
                    dataIndex: 'contractor',
                    sorter: true,
                    key: 'contractor',
                    render: (contractor) => (
                        <Link to={"/contractor/" + contractor.key}>{contractor.name}</Link>
                    )
                }, {
                    title: 'Specialty',
                    dataIndex: 'specialty.name',
                    sorter: true,
                    key: 'specialty',
                }, {
                    title: 'Grade',
                    key: 'grade',
                    render: (record) => (
                        <Input
                            style={{width: 75}}
                            name={record.key}
                            defaultValue={record.grade}
                            onPressEnter={(event) => this.updateGrade(event)}
                            onBlur={(event) => this.updateGrade(event)}/>
                    )
                }, {
                    title: 'Passed',
                    key: 'passed',
                    render: (record) => (
                        <Select
                            size="large"
                            name={record.key}
                            style={{width: 150}}
                            defaultValue={record.passed.toString()}
                            onSelect={(value) => this.updatePassed(value, record.key)}>
                            <Option key="true">
                                <div style={{color: '#008000'}}>Pass</div>
                            </Option>
                            <Option key="false">
                                <div style={{color: '#FF0000'}}>Fail</div>
                            </Option>
                        </Select>

                    )
                }, {
                    key: 'delete',
                    render: (trainee) => {
                        return (
                            <Popconfirm title="Are you sure delete this record?"
                                        onConfirm={this.confirm.bind(this, trainee, "trainSpec")}
                                        onCancel={this.cancel.bind(this)} okText="Yes" cancelText="No">
                                <Button type="danger">Delete</Button>
                            </Popconfirm>
                        )
                    }
                }],
            contractorsColumns: [
                {
                    title: 'AFM',
                    dataIndex: 'contractor',
                    sorter: true,
                    key: 'afm',
                    render: (contractor) => (
                        <Link to={"/contractor/" + contractor.key}>{contractor.afm}</Link>
                    )
                }, {
                    title: 'Contractor',
                    dataIndex: 'contractor',
                    sorter: true,
                    key: 'contractor',
                    render: (contractor) => (
                        <Link to={"/contractor/" + contractor.key}>{contractor.name}</Link>
                    )
                }, {
                    title: 'Sub Refineries',
                    dataIndex: 'seminarContractorSubRefineries',
                    key: 'seminarContractorSubRefineries',
                    render: seminarContractorSubRefineries => (
                        <span>
                            {seminarContractorSubRefineries.map(subRefinery => <Tag color="blue" key={subRefinery.key}>{subRefinery.subRefinery.name}</Tag>)}
                        </span>
                    ),
                }, {
                    title: 'No of Trainees',
                    dataIndex: 'numOfTrainees',
                    sorter: true,
                    key: 'numOfTrainees',
                }, {
                    title: 'Suggested Cost',
                    dataIndex: 'suggestedCost',
                    sorter: true,
                    key: 'suggestedCost',
                }, {
                    title: 'Total Cost',
                    key: 'cost',
                    render: (contractor) => (
                        <Input
                            style={{width: 75}}
                            name={contractor.key}
                            defaultValue={contractor.cost}
                            onPressEnter={this.updateCost.bind(this)}
                            onBlur={this.updateCost.bind(this)}/>
                    )
                }, {
                    key: 'delete',
                    render: (contractor) => {
                        return (
                            <Popconfirm title="Are you sure delete this record?"
                                        onConfirm={this.confirm.bind(this, contractor, "contractorSpec")}
                                        onCancel={this.cancel.bind(this)} okText="Yes" cancelText="No">
                                <Button type="danger">Delete</Button>
                            </Popconfirm>
                        )
                    }
                }
            ],
            isLoading: false,
            seminar: {},
            specialties: [],
            trainSpec: [],
            contractorSpec: [],
            isEdit: false,
            file: '',
            ama: '',
            afm: '',
            spec: '',
            costEdit: '',
            gradeEdit: '',
            passedEdit: ''
        };
        this.getSeminar = this.getSeminar.bind(this);
        this.handleEdit = this.handleEdit.bind(this);
        this.handleCostChange = this.handleCostChange.bind(this);
        this.handleAddSpecialty = this.handleAddSpecialty.bind(this);
        this.handleAddTrainee = this.handleAddTrainee.bind(this);
        this.handleAddContractor = this.handleAddContractor.bind(this);
        this.updateCost = this.updateCost.bind(this);
        this.updateGrade = this.updateGrade.bind(this);
        this.updatePassed = this.updatePassed.bind(this);
    }

    confirm(record, type) {
        this.remove.bind(this, record, type);
        this.remove(record, type);
    }

    cancel(e) {
        message.error('Canceled remove');
    }

    remove(record, type) {
        let promise;

        promise = deleteItem(record);
        promise
            .then(response => {
                notification.success({
                    message: 'Seminar App',
                    description: "Removed!",
                });
                const records = this.state[type].filter(i => i.key !== record.key)
                this.setState({
                    [type]: records
                })
            })
            .catch(error => {
                notification.error({
                    message: 'Seminar App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            });
    }

    update() {
        this.setState({
            isLoading: true,
            isEdit: false,
        });

        const editRequest = {
            name: this.state.seminar.name,
            date: this.state.seminar.date,
            refinery: this.state.seminar.refinery,
            _links: this.state.seminar._links
        };
        let promise;

        promise = updateItem(editRequest);
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

    updateCost(event) {
        if (!event)
            return
        const target = event.target;
        const inputKey = target.name;
        const inputValue = target.value;
        let promise;

        promise = updateCost(inputKey, inputValue);
        promise
            .then(response => {
                notification.success({
                    message: 'Seminar App',
                    description: "Sucessfully changed cost!",
                });
            })
            .catch(error => {
                notification.error({
                    message: 'Seminar App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            });
    }

    updateGrade(event) {
        if (!event)
            return
        const target = event.target;
        const inputKey = target.name;
        let inputValue = target.value;
        while (inputValue.startsWith("0") && inputValue.length > 1)
            inputValue = inputValue.substring(1);

        let promise;

        promise = updateGrade(inputKey, inputValue);
        promise
            .then(response => {
                notification.success({
                    message: 'Seminar App',
                    description: "Sucessfully changed grade!",
                });
            })
            .catch(error => {
                notification.error({
                    message: 'Seminar App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            });
    }

    updatePassed(value, key) {
        let promise;

        promise = updatePassed(key, value);
        promise
            .then(response => {
                notification.success({
                    message: 'Seminar App',
                    description: "Sucessfully changed!",
                });
            })
            .catch(error => {
                notification.error({
                    message: 'Seminar App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            });
    }

    getSeminar() {
        this.setState({
            isLoading: true
        });
        let promise;

        promise = getSeminarById(this.state.id);

        if (!promise) {
            return;
        }

        promise
            .then(response => {

                this.setState({
                    seminar: response,
                    specialties: response ? response.seminarSpecialties : [],
                    trainSpec: response ? response.seminarTrainees : [],
                    contractorSpec: response ? response.seminarContractors : [],
                    isLoading: false
                })
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

    handleEdit() {
        if (this.state.isEdit) {
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

    handleTypeChange(inputValue) {

        const seminarEdit = this.state.seminar;
        seminarEdit.refinery = inputValue
        this.setState({
            seminar: seminarEdit
        });
    }

    handleDateChange(date) {
        if (!date) {
            return
        }
        const seminarEdit = this.state.seminar;
        seminarEdit.date = date.format('YYYY-MM-DD');

        this.setState({
            seminar: seminarEdit
        });
    }

    handleAddSpecialty() {
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
            let addSpec = insertItem({name: this.state.spec}, 'specialties');
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

    handleAddContractor() {
        this.setState({
            isLoading: true
        });

        let searchPromise;
        searchPromise = getContractorByAFM(this.state.afm);

        searchPromise
            .then(response => {

                let addPromise;
                addPromise = insertSeminarContractor(this.state.seminar.key, response.key)
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
    }

    handleAddTrainee() {
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
        if (!me) {
            return;
        }
    };

    handleWelcomeDoc = () => {
        let me;
        me = getWelcomeDoc(this.state.seminar.key, "welcome-document-" + this.state.seminar.name.replace(/\s+/g, '-').toLowerCase() + ".docx");
        if (!me) {
            return;
        }
    };

    uploadFile = (file) => {
        this.setState({
            isLoading: true,
        });
        if (!file) {
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
        if (!me) {
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
        if (this.state.isLoading) {
            return <LoadingIndicator/>
        }
        let content;
        if (this.state.isEdit) {
            content = (
                <Form layout="inline" className="seminar-info" onSubmit={this.update.bind(this)}>
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
                                    onChange={(date) => this.handleDateChange(date)}
                                />
                            </FormItem>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                                <span label="refineryTitle" className="seminar-tag">
                                    Seminar's Refinery:
                                </span>
                        </Col>
                        <Col span={12}>
                            <FormItem>
                                <Select
                                    size="large"
                                    name="seminarRefinery"
                                    autoComplete="off"
                                    defaultValue={this.state.seminar.refinery}
                                    onChange={(value) => this.handleTypeChange(value)}>
                                    <Option key="MOTOROIL">Motoroil</Option>
                                    <Option key="ELPE">ELPE</Option>
                                </Select>
                            </FormItem>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                                <span label="seminarCostTitle" className="seminar-tag">
                                    Seminar's Total Cost:
                                </span>
                        </Col>
                        <Col span={12}>
                                <span label="refinery">
                                    {this.state.seminar.cost}
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
                                <span label="created">
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
                                <span label="updated">
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
        } else {
            content = (
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
                            <span label="refineryTitle" className="seminar-tag">
                                Seminar's Refinery:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="refinery">
                                {humanize(this.state.seminar.refinery)}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="seminarCostTitle" className="seminar-tag">
                                Seminar's Total Cost:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="refinery">
                                {this.state.seminar.cost}
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
                            <span label="created">
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
                            <span label="updated">
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
                                    <Icon type="upload"/> Upload Contractor
                                </Button>
                            </Upload>
                        </Col>
                        <Col span={6} >
                            <Button className="welcome-button" type='primary' onClick={this.handleWelcomeDoc.bind(this)}>
                                Welcome Document
                            </Button>
                        </Col>
                        <Col span={6}>
                            <Button className="edit-seminar-button" type='primary'
                                    onClick={this.handleEdit}>Edit</Button>
                        </Col>
                    </Row>
                </div>
            )
        }

        let addSpecContent = (
            <Form layout="inline" onSubmit={this.handleAddSpecialty}>
                <FormItem>
                    <Input
                        name="spec"
                        placeholder="Title"
                        style={{width: 600}}
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

        let addTraineeContent = (
            <Form layout="inline" onSubmit={this.handleAddTrainee}>
                <FormItem>
                    <Input
                        name="ama"
                        placeholder="Trainee's AMA"
                        style={{width: 400}}
                        onChange={(event) => this.handleInputChangeGeneric(event)}
                    />
                </FormItem>
                <FormItem>
                    <Input
                        name="afm"
                        placeholder="Contractor's AFM"
                        style={{width: 400}}
                        onChange={(event) => this.handleInputChangeGeneric(event)}
                    />
                </FormItem>
                <FormItem>
                    <Select
                        name="spec"
                        defaultValue="Specialties"
                        style={{width: 500}}
                        onChange={(event) => this.handleSelectChange(event)}
                    >
                        {this.state.specialties.map(spec => <Option
                            key={spec.specialty.key}>{spec.specialty.name}</Option>)}
                    </Select>
                </FormItem>
                <FormItem>
                    <Button htmlType="submit" type='primary'>
                        Add
                    </Button>
                </FormItem>
            </Form>
        )

        let addContractorContent = (
            <Form layout="inline" onSubmit={this.handleAddContractor}>
                <FormItem>
                    <Input
                        name="afm"
                        placeholder="Contractor's AFM"
                        style={{width: 400}}
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

        return (
            <div className="seminar-container">
                <h1 className="page-title">Seminar {this.state.seminar.name}</h1>
                <div className="seminar-content">
                    {content}

                    <div className="specialties-list">
                        <Table
                            {...this.state}
                            title={() => {
                                return (
                                    <div className="table-header">
                                        <span className="table-title"> Specialities </span>
                                        <Popover content={addSpecContent} title="Add Specialty" trigger="click">
                                            <Button className="add-to-seminar-button" type="primary">Add
                                                Specialty</Button>
                                        </Popover>
                                    </div>
                                )
                            }}
                            columns={this.state.specialtiesColumns}
                            dataSource={this.state.specialties}
                        />
                    </div>
                    <br/>
                    <br/>
                    <div className="contractors-list">
                        <Table
                            {...this.state}
                            title={() => {
                                return (
                                    <div className="table-header">
                                        <span className="table-title"> Contractors </span>
                                        <Popover content={addContractorContent} title="Add Contractor" trigger="click">
                                            <Button className="add-to-seminar-button" type="primary">Add
                                                Contractor</Button>
                                        </Popover>
                                    </div>
                                )
                            }}
                            columns={this.state.contractorsColumns}
                            dataSource={this.state.contractorSpec}
                        />
                    </div>
                    <br/>
                    <br/>
                    <div className="trainees-list">
                        <Table
                            {...this.state}
                            title={() => {
                                return (
                                    <div className="table-header">
                                        <span className="table-title"> Trainees </span>
                                        <Popover content={addTraineeContent} title="Add Trainee" trigger="click">
                                            <Button className="add-to-seminar-button" type="primary">Add
                                                Trainee</Button>
                                        </Popover>
                                    </div>
                                )
                            }}
                            columns={this.state.traineesColumns}
                            dataSource={this.state.trainSpec}
                        />
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(Seminar);


import React, { Component } from 'react';
import { createSeminar } from '../util/APIUtils';
import { MAX_CHOICES } from '../constants';
import './NewSeminar.css';
import { Form, Input, Button, Icon, Select, Col, notification, Row, DatePicker } from 'antd';
import { Link } from 'react-router-dom';
const Option = Select.Option;
const FormItem = Form.Item;
const { TextArea } = Input

class NewSeminar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            question: {
                text: ''
            },
            choices: [{
                text: ''
            }, {
                text: ''
            }],
            seminarLength: {
                days: 1,
                hours: 0
            }
        };
        this.addChoice = this.addChoice.bind(this);
        this.removeChoice = this.removeChoice.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleQuestionChange = this.handleQuestionChange.bind(this);
        this.handleChoiceChange = this.handleChoiceChange.bind(this);
        this.handleSeminarDaysChange = this.handleSeminarDaysChange.bind(this);
        this.handleSeminarHoursChange = this.handleSeminarHoursChange.bind(this);
        this.isFormInvalid = this.isFormInvalid.bind(this);
    }

    addChoice(event) {
        const choices = this.state.choices.slice();
        this.setState({
            choices: choices.concat([{
                text: ''
            }])
        });
    }

    removeChoice(choiceNumber) {
        const choices = this.state.choices.slice();
        this.setState({
            choices: [...choices.slice(0, choiceNumber), ...choices.slice(choiceNumber+1)]
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        const seminarData = {
            question: this.state.question.text,
            choices: this.state.choices.map(choice => {
                return {text: choice.text}
            }),
            seminarLength: this.state.seminarLength
        };

        createSeminar(seminarData)
            .then(response => {
                this.props.history.push("/");
            }).catch(error => {
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login create seminar.');
            } else {
                notification.error({
                    message: 'Seminaring App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    }

    validateQuestion = (questionText) => {
        if(questionText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter your question!'
            }
        } else if (questionText.length > 20) {
            return {
                validateStatus: 'error',
                errorMsg: `Question is too long (Maximum ${40} characters allowed)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    handleQuestionChange(event) {
        const value = event.target.value;
        this.setState({
            question: {
                text: value,
                ...this.validateQuestion(value)
            }
        });
    }

    validateChoice = (choiceText) => {
        if(choiceText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter a choice!'
            }
        } else if (choiceText.length > 40) {
            return {
                validateStatus: 'error',
                errorMsg: `Choice is too long (Maximum ${40} characters allowed)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    handleChoiceChange(event, index) {
        const choices = this.state.choices.slice();
        const value = event.target.value;

        choices[index] = {
            text: value,
            ...this.validateChoice(value)
        }

        this.setState({
            choices: choices
        });
    }


    handleSeminarDaysChange(value) {
        const seminarLength = Object.assign(this.state.seminarLength, {days: value});
        this.setState({
            seminarLength: seminarLength
        });
    }

    handleSeminarHoursChange(value) {
        const seminarLength = Object.assign(this.state.seminarLength, {hours: value});
        this.setState({
            seminarLength: seminarLength
        });
    }

    isFormInvalid() {
        if(this.state.question.validateStatus !== 'success') {
            return true;
        }

        for(let i = 0; i < this.state.choices.length; i++) {
            const choice = this.state.choices[i];
            if(choice.validateStatus !== 'success') {
                return true;
            }
        }
    }

    render() {
        const choiceViews = [];
        this.state.choices.forEach((choice, index) => {
            choiceViews.push(<SeminarChoice key={index} choice={choice} choiceNumber={index} removeChoice={this.removeChoice} handleChoiceChange={this.handleChoiceChange}/>);
        });
        const { getFieldDecorator } = this.props.form;
        return (
            <div className="new-seminar-container">
                <h1 className="page-title">Create Seminar</h1>
                <div className="new-seminar-content">
                    <Form layout="vertical" hideRequiredMark>
                        <Row gutter={16}>
                            <Col span={12}>
                                <Form.Item label="Name">
                                    {getFieldDecorator('name', {
                                        rules: [{ required: true, message: 'please enter user name' }],
                                    })(<Input placeholder="please enter user name" />)}
                                </Form.Item>
                            </Col>
                            <Col span={12}>
                                <Form.Item label="Url">
                                    {getFieldDecorator('url', {
                                            rules: [{ required: true, message: 'please enter url' }],
                                        })(
                                        <Input
                                          style={{ width: '100%' }}
                                          addonBefore="http://"
                                          addonAfter=".com"
                                          placeholder="please enter url"
                                        />
                                    )}
                                </Form.Item>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <Form.Item label="Owner">
                                    {getFieldDecorator('owner', {
                                            rules: [{ required: true, message: 'Please select an owner' }],
                                        })(
                                        <Select placeholder="Please select an owner">
                                            <Option value="xiao">Xiaoxiao Fu</Option>
                                            <Option value="mao">Maomao Zhou</Option>
                                        </Select>
                                    )}
                                </Form.Item>
                            </Col>
                            <Col span={12}>
                                <Form.Item label="Type">
                                    {getFieldDecorator('type', {
                                            rules: [{ required: true, message: 'Please choose the type' }],
                                        })(
                                        <Select placeholder="Please choose the type">
                                            <Option value="private">Private</Option>
                                            <Option value="public">Public</Option>
                                        </Select>
                                    )}
                                </Form.Item>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={12}>
                                <Form.Item label="Approver">
                                    {getFieldDecorator('approver', {
                                            rules: [{ required: true, message: 'Please choose the approver' }],
                                        })(
                                        <Select placeholder="Please choose the approver">
                                            <Option value="jack">Jack Ma</Option>
                                            <Option value="tom">Tom Liu</Option>
                                        </Select>
                                    )}
                                </Form.Item>
                            </Col>
                            <Col span={12}>
                                <Form.Item label="DateTime">
                                    {getFieldDecorator('dateTime', {
                                            rules: [{ required: true, message: 'Please choose the dateTime' }],
                                        })(
                                        <DatePicker.RangePicker
                                            style={{ width: '100%' }}
                                            getPopupContainer={trigger => trigger.parentNode}
                                        />
                                    )}
                                </Form.Item>
                            </Col>
                        </Row>
                        <Row gutter={16}>
                            <Col span={24}>
                                <Form.Item label="Description">
                                    {getFieldDecorator('description', {
                                        rules: [
                                          {
                                            required: true,
                                            message: 'please enter url description',
                                          },
                                        ],
                                        })(<Input.TextArea rows={4} placeholder="please enter url description" />)}
                                </Form.Item>
                            </Col>
                        </Row>
                    </Form>
                    <div
                        // style={{
                        //     position: 'absolute',
                        //     bottom: 0,
                        //     width: '100%',
                        //     borderTop: '1px solid #e8e8e8',
                        //     padding: '10px 16px',
                        //     textAlign: 'right',
                        //     left: 0,
                        //     background: '#fff',
                        //     borderRadius: '0 0 4px 4px',
                        // }}
                    >
                    <Button
                        style={{
                        marginRight: 8,
                        }}
                        onClick={this.onClose}
                    >
                      Cancel
                    </Button>
                    <Button onClick={this.onClose} type="primary">Submit</Button>
                </div>
                </div>
            </div>
        );
    }
}

function SeminarChoice(props) {
    return (
        <FormItem validateStatus={props.choice.validateStatus}
                  help={props.choice.errorMsg} className="seminar-form-row">
            <Input
                placeholder = {'Choice ' + (props.choiceNumber + 1)}
                size="large"
                value={props.choice.text}
                className={ props.choiceNumber > 1 ? "optional-choice": null}
                onChange={(event) => props.handleChoiceChange(event, props.choiceNumber)} />

            {
                props.choiceNumber > 1 ? (
                    <Icon
                        className="dynamic-delete-button"
                        type="close"
                        disabled={props.choiceNumber <= 1}
                        onClick={() => props.removeChoice(props.choiceNumber)}
                    /> ): null
            }
        </FormItem>
    );
}


export default Form.create()(NewSeminar);
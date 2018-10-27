import React, { Component } from 'react';
import './NewSeminar.css';
import { Link } from 'react-router-dom';
import { Form, Input, Button, DatePicker, Select, notification } from 'antd';
import moment from 'moment';
import { formatDate, formatDateTime } from '../util/Helpers';

const FormItem = Form.Item;
const Option = Select.Option;

class NewSeminar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: {
                value: ''
            },
            date: {
                value: ''
            },
            type: {
                value: ''
            },
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.validateDate = this.validateDate.bind(this);
        this.isFormInvalid = this.isFormInvalid.bind(this);
    }

    handleInputChange(event, validationFun) {
        const target = event.target;
        const inputName = target.name;        
        const inputValue = target.value;

        this.setState({
            [inputName] : {
                value: inputValue,
                ...validationFun(inputValue)
            }
        });
    }

    handleSubmit(event) {
        event.preventDefault();
    
        const signupRequest = {
            name: this.state.name.value,
            date: this.state.date.value,
            type: this.state.type.value
        };
        // signup(signupRequest)
        // .then(response => {
        //     notification.success({
        //         message: 'Seminar App',
        //         description: "Thank you! You're successfully registered. Please Login to continue!",
        //     });          
        //     this.props.history.push("/login");
        // }).catch(error => {
        //     notification.error({
        //         message: 'Seminar App',
        //         description: error.message || 'Sorry! Something went wrong. Please try again!'
        //     });
        // });
    }

    isFormInvalid() {
        return false;
    }

    validateDate = (username) => {
        
    }

    render() {
        return (
            <div className="signup-container">
                <h1 className="page-title">New Seminar</h1>
                <div className="signup-content">
                    <Form onSubmit={this.handleSubmit} className="signup-form">
                        <FormItem label="Name" required={true}>
                            <Input 
                                size="large"
                                name="name"
                                autoComplete="off"
                                placeholder="Seminar's full name"
                                value={this.state.name.value} 
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>

                        <FormItem 
                            label="Taking place at:" 
                            hasFeedback 
                            validateStatus={this.state.date.validateDate}
                            help={this.state.date.errorMsg}>
                                <DatePicker 
                                    size="large"
                                    name="date" 
                                    autoComplete="off"
                                    placeholder="Seminar's date"
                                    value={this.state.date.value} 
                                    onBlur={this.validateDate}
                                    onChange={(event) => this.handleDateChange(event, this.validateDate)}
                                    defaultValue={moment()}
                                    format='DD/MM/YYYY'/>
                        </FormItem>
                        <FormItem 
                            label="Type"
                            hasFeedback
                            validateStatus={this.state.type.validateStatus}
                            help={this.state.type.errorMsg}> 
                            <Select 
                                size="large">
                                    <Option key="1">Basic</Option>
                                    <Option key="2">Advanced</Option>
                            </Select>  
                        </FormItem>   
                        <FormItem>
                            <Button 
                                type="primary" 
                                htmlType="submit" 
                                size="large" 
                                className="signup-form-button"
                                disabled={this.isFormInvalid()}>Create</Button>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }

}

export default NewSeminar;
                


import React, { Component } from 'react';
import './NewContractor.css';
import { Link } from 'react-router-dom';
import { Form, Input, Button, DatePicker, Select, notification } from 'antd';
import moment from 'moment';
import { formatDate, formatDateTime } from '../util/Helpers';

const FormItem = Form.Item;
const Option = Select.Option;

class NewContractor extends Component {
    constructor(props) {
        super(props);
        this.state = {
            afm: {
                value: ''
            },
            name: {
                value: ''
            },
            representativeName: {
                value: ''
            },
            phoneNumber: {
                value: ''
            },
            email: {
                value: ''
            },
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
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
            afm: this.state.afm.value,
            name: this.state.name.value,
            representativeName: this.state.representativeName.value,
            phoneNumber: this.state.phoneNumber.value,
            email: this.state.email.value
        };
        // signup(signupRequest)
        // .then(response => {
        //     notification.success({
        //         message: 'Contractor App',
        //         description: "Thank you! You're successfully registered. Please Login to continue!",
        //     });          
        //     this.props.history.push("/login");
        // }).catch(error => {
        //     notification.error({
        //         message: 'Contractor App',
        //         description: error.message || 'Sorry! Something went wrong. Please try again!'
        //     });
        // });
    }

    isFormInvalid() {
        return false;
    }

    render() {
        return (
            <div className="signup-container">
                <h1 className="page-title">New Contractor</h1>
                <div className="signup-content">
                    <Form onSubmit={this.handleSubmit} className="signup-form">
                        <FormItem label="AFM" required={true}>
                            <Input 
                                size="large"
                                name="afm"
                                autoComplete="off"
                                placeholder="Contractor's afm"
                                value={this.state.afm.value} 
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Name" required={true}>
                            <Input 
                                size="large"
                                name="name"
                                autoComplete="off"
                                placeholder="Contractor's full name"
                                value={this.state.name.value} 
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Representative's name" required={true}>
                            <Input 
                                size="large"
                                name="representativeName"
                                autoComplete="off"
                                placeholder="Contractor's representative's name"
                                value={this.state.representativeName.value} 
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Phone Number" required={true}>
                            <Input 
                                size="large"
                                name="phoneNumber"
                                autoComplete="off"
                                placeholder="Contractor's phone number"
                                value={this.state.phoneNumber.value} 
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Email" required={true}>
                            <Input 
                                size="large"
                                name="email"
                                autoComplete="off"
                                placeholder="Contractor's email"
                                value={this.state.email.value} 
                                onChange={(event) => this.handleInputChange(event)} />    
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

export default NewContractor;
                


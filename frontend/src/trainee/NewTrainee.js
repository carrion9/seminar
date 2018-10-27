import React, { Component } from 'react';
import './NewTrainee.css';
import { Link } from 'react-router-dom';
import { Form, Input, Button, DatePicker, Select, notification } from 'antd';
import moment from 'moment';
import { formatDate, formatDateTime } from '../util/Helpers';

const FormItem = Form.Item;
const Option = Select.Option;

class NewTrainee extends Component {
    constructor(props) {
        super(props);
        this.state = {
            ama: {
                value: ''
            },
            surname: {
                value: ''
            },
            name: {
                value: ''
            },
            fathersName: {
                value: ''
            },
            nationality: {
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
        //         message: 'Trainee App',
        //         description: "Thank you! You're successfully registered. Please Login to continue!",
        //     });          
        //     this.props.history.push("/login");
        // }).catch(error => {
        //     notification.error({
        //         message: 'Trainee App',
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
                <h1 className="page-title">New Trainee</h1>
                <div className="signup-content">
                    <Form onSubmit={this.handleSubmit} className="signup-form">
                        <FormItem label="AMA" required={true}>
                            <Input 
                                size="large"
                                name="ama"
                                autoComplete="off"
                                placeholder="Trainee's AMA"
                                value={this.state.ama.value} 
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Surame" required={true}>
                            <Input 
                                size="large"
                                name="surname"
                                autoComplete="off"
                                placeholder="Trainee's surname"
                                value={this.state.surname.value} 
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Name" required={true}>
                            <Input 
                                size="large"
                                name="name"
                                autoComplete="off"
                                placeholder="Trainee's full name"
                                value={this.state.name.value} 
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Father's ame" required={true}>
                            <Input 
                                size="large"
                                name="fathersName"
                                autoComplete="off"
                                placeholder="Trainee's father's name"
                                value={this.state.fathersName.value} 
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Nationality" required={true}>
                            <Input 
                                size="large"
                                name="nationality"
                                autoComplete="off"
                                placeholder="Trainee's nationality"
                                value={this.state.nationality.value} 
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

export default NewTrainee;
                


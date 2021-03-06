import React, { Component } from 'react';
import './NewSeminar.css';
import { Link, Redirect, withRouter } from 'react-router-dom';
import { Form, Input, Button, DatePicker, Select, notification } from 'antd';
import moment from 'moment';
import { formatDate, formatDateTime } from '../util/Helpers';
import { insertItem } from '../util/APIUtils';
import LoadingIndicator from '../common/LoadingIndicator';

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
                value: moment()
            },
            refinery: {
                value: ''
            },
            isLoading: false,
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleDateChange = this.handleDateChange.bind(this);
        this.handleSelectChange = this.handleSelectChange.bind(this);
        this.validateDate = this.validateDate.bind(this);
        this.isFormInvalid = this.isFormInvalid.bind(this);
    }

    handleInputChange(event) {
        const target = event.target;
        const inputName = target.name;        
        const inputValue = target.value;
        this.setState({
            [inputName] : {
                value: inputValue
            }
        });

    }

    handleSelectChange(value) {
        this.setState({
            refinery : {
                value: value
            }
        });

    }

    handleDateChange(date) {
        this.setState({
            date: {
                value: date,
                ...this.validateDate(date)
            }
        });
    }

    handleSubmit(event) {
        const newRequest = {
            name: this.state.name.value,
            date: this.state.date.value.format('YYYY-MM-DD'),
            refinery: this.state.refinery.value
        };
        this.setState({
            isLoading: true
        });
        insertItem(newRequest, 'seminars')
        .then(response => {
            notification.success({
                message: 'Seminar App',
                description: "Seminar created!",
            });
            this.setState({
                isLoading: false
            });
            this.props.history.push("/seminar/"+response.key);
        }).catch(error => {
            notification.error({
                message: 'Seminar App',
                description: error.message || 'Sorry! Something went wrong. Please try again!'
            });
        });
    }

    isFormInvalid() {
        return !(this.state.date.validateStatus === 'success' &&
                 this.state.name.value !== '' &&
                 this.state.refinery.value !== '');
    }

    validateDate(date){
        if(moment().isAfter(date)){
            return {
                validationStatus: 'error',
                errorMsg: `Invalid date!`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
              };            
        }
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />
        }
        return (

            <div className="signup-container">
                <h1 className="page-title">New Seminar</h1>
                <div className="signup-content">
                    <Form onSubmit={this.handleSubmit} className="signup-form">
                        <FormItem 
                            label="Name" 
                            required={true}>
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
                                    onChange={this.handleDateChange}
                                    defaultValue={moment()}
                                    format='DD/MM/YYYY'/>
                        </FormItem>
                        <FormItem 
                            label="Refinery"
                            required={true}>
                                <Select 
                                    size="large"
                                    name="refinery"
                                    autoComplete="off"
                                    placeholder="Seminar's Refinery"
                                    value={this.state.refinery.value}
                                    onChange={(value) => this.handleSelectChange(value)} >  
                                        <Option key="MOTOROIL">Motoroil</Option>
                                        <Option key="ELPE">ELPE</Option>
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


export default withRouter(NewSeminar);
                


import React, { Component } from 'react';
import './NewTrainee.css';
import { Link } from 'react-router-dom';
import { Form, Input, Button, DatePicker, Select, notification } from 'antd';
import moment from 'moment';
import { formatDate, formatDateTime } from '../util/Helpers';
import LoadingIndicator from '../common/LoadingIndicator';
import { insertItem } from '../util/APIUtils';

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
            cardType: {
                value: ''
            },
            cardStatus: {
                value: ''
            },
            documentCode: {
                value: ''
            },
            docType: {
                value: ''
            },
            imageLocation: {
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
              //  ...validationFun(inputValue)
            }
        });
    }

    handleSubmit(event) {
        event.preventDefault();
    
        const newRequest = {
            ama: this.state.ama.value,
            name: this.state.name.value,
            surname: this.state.surname.value,
            fathersName: this.state.fathersName.value,
            nationality: this.state.nationality.value,
            cardType: this.state.cardType.value,
            cardStatus: this.state.cardStatus.value,
            documentCode: this.state.documentCode.value,
            docType: this.state.docType.value,
        };
        insertItem(newRequest, 'trainees')
        .then(response => {
            notification.success({
                message: 'Seminar App',
                description: "Trainee created!",
            });
            this.setState({
                isLoading: false
            });
            this.props.history.push("/trainee/"+response.key);
        }).catch(error => {
            notification.error({
                message: 'Seminar App',
                description: error.message || 'Sorry! Something went wrong. Please try again!'
            });
        });
    }

    handleSelectChange(value, field) {
        this.setState({
            [field]:{
                value: value
            }
        });

    }

    isFormInvalid() {
        return false;
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />
        }
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
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Surame" required={true}>
                            <Input 
                                size="large"
                                name="surname"
                                autoComplete="off"
                                placeholder="Trainee's surname"
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Name" required={true}>
                            <Input 
                                size="large"
                                name="name"
                                autoComplete="off"
                                placeholder="Trainee's full name"
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Father's ame" required={true}>
                            <Input 
                                size="large"
                                name="fathersName"
                                autoComplete="off"
                                placeholder="Trainee's father's name"
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Nationality" required={true}>
                            <Input 
                                size="large"
                                name="nationality"
                                autoComplete="off"
                                placeholder="Trainee's nationality"
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Card Type" required={true}>
                            <Select 
                                size="large"
                                name="cardType"
                                autoComplete="off"
                                onChange={(value) => this.handleSelectChange(value, 'cardType')} >  
                                    <Option key="RED">RED</Option>
                                    <Option key="GREEN">GREEN</Option>
                                    <Option key="YELLOW">YELLOW</Option>
                            </Select>     
                        </FormItem>

                        <FormItem label="Card Status" required={true}>
                            <Select 
                                size="large"
                                name="cardStatus"
                                autoComplete="off"
                                onChange={(value) => this.handleSelectChange(value, 'cardStatus')} >  
                                    <Option key="DELIVERED">DELIVERED</Option>
                                    <Option key="PENDING">PENDING</Option>
                                    <Option key="PRINTED">PRINTED</Option>
                            </Select>    
                        </FormItem>
                        <FormItem label="Document Code" required={true}>
                            <Input 
                                size="large"
                                name="documentCode"
                                autoComplete="off"
                                placeholder="Trainee's Document Code"
                                onChange={(event) => this.handleInputChange(event)} />    
                        </FormItem>
                        <FormItem label="Document Type" required={true}>
                            <Select 
                                size="large"
                                name="docType"
                                autoComplete="off"
                                onChange={(value) => this.handleSelectChange(value, 'docType')} >  
                                    <Option key="NONE">NONE</Option>
                                    <Option key="DRIVING_LICENSE">DRIVING LICENSE</Option>
                                    <Option key="PASSPORT">PASSPORT</Option>
                                    <Option key="IDENTITY">IDENTITY</Option>
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

export default NewTrainee;
                


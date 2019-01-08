import React, { Component } from 'react';
import { getUserProfile, updateItem } from '../../util/APIUtils';
import { Avatar, Tabs, Input, Form, notification, Button } from 'antd';
import { getAvatarColor } from '../../util/Colors';
import { formatDate } from '../../util/Helpers';
import LoadingIndicator  from '../../common/LoadingIndicator';
import './Profile.css';
import NotFound from '../../common/NotFound';
import ServerError from '../../common/ServerError';
import { 
    NAME_MIN_LENGTH, NAME_MAX_LENGTH, 
    USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH,
    EMAIL_MAX_LENGTH,
    PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH
} from '../../constants';

const TabPane = Tabs.TabPane;
const FormItem = Form.Item;

class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            isLoading: true,
            password: {
                value: ''
            }
        }
        this.loadUserProfile = this.loadUserProfile.bind(this);
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
        
        const uRequest = {
            name: this.state.user.value,
            email: this.state.user.value,
            username: this.state.user.value,
            _links: {
                self: {
                    href: this.state.user._links.self.href
                }
            },
            password: this.state.password.value
        };
        updateItem(uRequest)
        .then(response => {
            notification.success({
                message: 'Seminar App',
                description: "Thank you! You're successfully registered. Please Login to continue!",
            });          
            this.props.history.push("/login");
        }).catch(error => {
            notification.error({
                message: 'Seminar App',
                description: error.message || 'Sorry! Something went wrong. Please try again!'
            });
        });
    }

    loadUserProfile(username) {
        this.setState({
            isLoading: true
        });

        getUserProfile(username)
        .then(response => {
            this.setState({
                user: response,
                isLoading: false
            });
        }).catch(error => {
            if(error.status === 404) {
                this.setState({
                    notFound: true,
                    isLoading: false
                });
            } else {
                this.setState({
                    serverError: true,
                    isLoading: false
                });        
            }
        });        
    }
      
    componentDidMount() {
        const username = this.props.match.params.username;
        this.loadUserProfile(username);
    }

    componentWillReceiveProps(nextProps) {
        if(this.props.match.params.username !== nextProps.match.params.username) {
            this.loadUserProfile(nextProps.match.params.username);
        }        
    }

    isFormInvalid() {
        return !(this.state.password.validateStatus === 'success');
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />;
        }

        if(this.state.notFound) {
            return <NotFound />;
        }

        if(this.state.serverError) {
            return <ServerError />;
        }

        const tabBarStyle = {
            textAlign: 'center'
        };

        return (
            <div className="profile">
                <div className="user-profile">
                    <div className="user-details">
                        <div className="user-avatar">
                            <Avatar className="user-avatar-circle" style={{ backgroundColor: getAvatarColor(this.state.user.name)}}>
                                {this.state.user.name.toUpperCase()}
                            </Avatar>
                        </div>
                        <div className="user-summary">
                            <div className="full-name">{this.state.user.name}</div>
                            <div className="username">@{this.state.user.username}</div>
                            <div className="user-joined">
                                Joined {formatDate(this.state.user.joinedAt)}
                            </div>
                        </div>
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <Form onSubmit={this.handleSubmit} className="signup-form">
                            <FormItem 
                                    label="Change Password"
                                    validateStatus={this.state.password.validateStatus}
                                    help={this.state.password.errorMsg}>
                                    <Input 
                                        size="large"
                                        name="password" 
                                        type="password"
                                        autoComplete="off"
                                        placeholder="A password between 6 to 20 characters" 
                                        value={this.state.password.value} 
                                        onChange={(event) => this.handleInputChange(event, this.validatePassword)} />    
                            </FormItem>
                             <FormItem>
                                <Button type="primary" 
                                    htmlType="submit" 
                                    size="large" 
                                    className="signup-form-button"
                                    disabled={this.isFormInvalid()}>Apply</Button>
                                </FormItem>
                        </Form>
                    </div>
                </div>   
            </div>
        );
    }


    validatePassword = (password) => {
        if(password.length < PASSWORD_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Password is too short (Minimum ${PASSWORD_MIN_LENGTH} characters needed.)`
            }
        } else if (password.length > PASSWORD_MAX_LENGTH) {
            return {
                validationStatus: 'error',
                errorMsg: `Password is too long (Maximum ${PASSWORD_MAX_LENGTH} characters allowed.)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };            
        }
    }
}

export default Profile;
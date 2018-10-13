import React, { Component } from 'react';
import './Seminar.css';
import { Radio, Form, Input, Button, Icon, Select, Col, Table, Popconfirm, message, notification, Row, DatePicker, Avatar } from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { getSeminarById, deleteItem } from '../util/APIUtils';
import { formatDate, formatDateTime } from '../util/Helpers';
import { withRouter } from 'react-router-dom';

const Option = Select.Option;
const RadioGroup = Radio.Group;

class SeminarCreator extends Component{
    
    render(){
        return(
            <Link className="creator-link" to={`/user/${this.state.seminar.createdBy.username}`}>
                <Avatar className="seminar-creator-avatar"
                        style={{ backgroundColor: getAvatarColor(this.state.seminar.createdBy.name)}} >
                    {this.state.seminar.createdBy.name[0].toUpperCase()}
                </Avatar>
                <span className="seminar-creator-name">
                    {this.state.seminar.createdBy.name}
                </span>
                <span className="seminar-creator-username">
                    @{this.state.seminar.createdBy.username}
                </span>
                <span className="seminar-creation-date">
                    {formatDateTime(this.state.seminar.date)}
                </span>
            </Link>
            );
    }
}
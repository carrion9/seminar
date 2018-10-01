import React, { Component } from 'react';
import './Seminar.css';
import { Avatar, Icon } from 'antd';
import { Link } from 'react-router-dom';
import { getAvatarColor } from '../util/Colors';
import { formatDateTime } from '../util/Helpers';

import { Radio, Button, Col, Row } from 'antd';
const RadioGroup = Radio.Group;

class SeminarCreator extends Component{

    render(){
        return(
            <Link className="creator-link" to={`/user/${this.props.seminar.createdBy.username}`}>
                <Avatar className="seminar-creator-avatar"
                        style={{ backgroundColor: getAvatarColor(this.props.seminar.createdBy.name)}} >
                    {this.props.seminar.createdBy.name[0].toUpperCase()}
                </Avatar>
                <span className="seminar-creator-name">
                    {this.props.seminar.createdBy.name}
                </span>
                <span className="seminar-creator-username">
                    @{this.props.seminar.createdBy.username}
                </span>
                <span className="seminar-creation-date">
                    {formatDateTime(this.props.seminar.date)}
                </span>
            </Link>
            );
    }
}

class Seminar extends Component {

    render() {
        return (
            <div className="seminar-content">
                <div className="seminar-header">
                {/*<SeminarCreator className="seminar-creator-info" seminar={this.props.seminar}/>*/}
                    <div className="seminar-question">
                        {this.props.seminar.name}
                    </div>
                    <div className="seminar-creation-date">
                        {formatDateTime(this.props.seminar.date)}
                    </div>
                    <div className="seminar-creator-name">
                        {this.props.seminar.seminarType}
                    </div>
                    <div className="seminar-footer">
                        Created by: {this.props.seminar.createdBy.name}({this.props.seminar.createdBy.username}) 
                        <br/>
                        Last edit: {/*this.props.seminar.editBy.name}({this.props.seminar.editBy.username*/}) 
                    </div>
                </div>
                {/*<div className="seminar-footer">*/}
                    {/*{*/}
                        {/*!(this.props.seminar.selectedChoice || this.props.seminar.expired) ?*/}
                            {/*(<Button className="vote-button" disabled={!this.props.currentVote} onClick={this.props.handleVoteSubmit}>Vote</Button>) : null*/}
                    {/*}*/}
                    {/*<span className="total-votes">{this.props.seminar.totalVotes} votes</span>*/}
                    {/*<span className="separator">β€Ά</span>*/}
                    {/*<span className="time-left">*/}
                        {/*{*/}
                            {/*this.props.seminar.expired ? "Final results" :*/}
                                {/*this.getTimeRemaining(this.props.seminar)*/}
                        {/*}*/}
                    {/*</span>*/}
                {/*</div>*/}
            </div>
        );
    }
}


export default Seminar;
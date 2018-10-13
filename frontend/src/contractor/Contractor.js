// import React, { Component } from 'react';
// import './Seminar.css';
// import { Radio, Form, Input, Button, Icon, Select, Col, Table, Popconfirm, message, notification, Row, DatePicker, Avatar } from 'antd';
// import { Link } from 'react-router-dom';
// import { getAvatarColor } from '../util/Colors';
// import { getSeminarById, deleteItem } from '../util/APIUtils';
// import { formatDate, formatDateTime } from '../util/Helpers';
// import { withRouter } from 'react-router-dom';


// render() {
       
//         return (
//             <div className="seminar-container">
//                 <h1 className="page-title">Seminar {this.state.seminar.name}</h1>
//                 <div className="seminar-content">
//                     <div className="seminar-info">
//                         <Row gutter={16}>
//                             <Col span={12}>
//                                 <span label="nameTitle" class="seminar-tag">
//                                     Seminar's Name: 
//                                 </span>
//                             </Col>
//                             <Col span={12}>
//                                 <span label="name">
//                                     {this.state.seminar.name}
//                                 </span>
//                             </Col>
//                         </Row>
//                         <Row gutter={16}>
//                             <Col span={12}>
//                                 <span label="dateTitle" class="seminar-tag">
//                                     Taking place at:
//                                 </span>
//                             </Col>
//                             <Col span={12}>
//                                 <span label="date">
//                                     {formatDate(this.state.seminar.date)}
//                                 </span>
//                             </Col>
//                         </Row>
//                         <Row gutter={16}>
//                             <Col span={12}>
//                                 <span label="seminarTypeTitle" class="seminar-tag">
//                                     Seminar's Type:
//                                 </span>
//                             </Col>
//                             <Col span={12}>
//                                 <span label="seminarType">
//                                     {this.state.seminar.seminarType}
//                                 </span>
//                             </Col>
//                         </Row>
//                         <Row gutter={16}>
//                             <Col span={12}>
//                                 <span label="createdTitle" class="seminar-tag">
//                                     Created:
//                                 </span>
//                             </Col>
//                             <Col span={12}>
//                                 <span label="created" >
//                                     {this.state.seminar.createdBy} at {formatDate(this.state.seminar.createdAt)}
//                                 </span>
//                             </Col>
//                         </Row>
//                         <Row gutter={16}>
//                             <Col span={12}>
//                                 <span label="updatedTitle" class="seminar-tag">
//                                     Last edit:
//                                 </span>
//                             </Col>
//                             <Col span={12}>
//                                 <span label="updated" >
//                                     {this.state.seminar.updatedBy} at {formatDate(this.state.seminar.updatedAt)}
//                                 </span>
//                             </Col>
//                         </Row>
//                     </div>
//             </div>
//         );
//     }
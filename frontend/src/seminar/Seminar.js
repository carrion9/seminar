import React, { Component } from 'react';
import './Seminar.css';
import { Radio, Form, Input, Button, Icon, Select, Col, notification, Row, DatePicker, Avatar } from 'antd';
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
/*****************************************************
//TODO fetch actuall data. Setted to some default dums
*****************************************************/
class Seminar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            columns : [],
            isLoading: false,
            seminar: {},
            specialities: [],
            trainees: []
        };
        this.getSeminar = this.getSeminar.bind(this);
        this.getSpecialities = this.getSpecialities.bind(this);
        this.getTrainees = this.getTrainees.bind(this);
        this.setColumns = this.setColumns.bind(this);
    }

    getSeminar(){
        let promise;

        promise = getSeminarById("1");

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });
        promise
            .then(response => {
                this.setState({
                    seminar: response,
                    isLoading: false
                })

        const x = response;
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

/*****************************************************
//TODO fetch actuall data. Setted to some default dums
*****************************************************/
    getSpecialities(){
        const specialities = ["special1","sp2","speciality long 3","speciality 4"];
        this.setState({
            specialities: specialities
        });
    }

/*****************************************************
//TODO fetch actuall data. Setted to some default dums
*****************************************************/
    getTrainees(){
        const trainees = [{
        "createdAt": "2018-09-28T09:42:31.461Z",
        "updatedAt": "2018-09-28T09:42:31.461Z",
        "createdBy": "John Tsompos",
        "updatedBy": "John Tsompos",
        "key": 1,
        "cost": 60,
        "actualCost": 60,
        "_links": {
          "self": {
            "href": "http://localhost:5000/api/seminarTrainees/1"
          },
          "seminarTrainee": {
            "href": "http://localhost:5000/api/seminarTrainees/1"
          },
          "seminar": {
            "href": "http://localhost:5000/api/seminarTrainees/1/seminar"
          },
          "seminarTraineeSpecialitySet": {
            "href": "http://localhost:5000/api/seminarTrainees/1/seminarTraineeSpecialitySet"
          },
          "contractor": {
            "href": "http://localhost:5000/api/seminarTrainees/1/contractor"
          },
          "trainee": {
            "href": "http://localhost:5000/api/seminarTrainees/1/trainee"
          }
        }
      },
      {
        "createdAt": "2018-09-28T09:42:31.461Z",
        "updatedAt": "2018-09-28T09:42:31.461Z",
        "createdBy": "John Tsompos",
        "updatedBy": "John Tsompos",
        "key": 2,
        "cost": 60,
        "actualCost": 60,
        "_links": {
          "self": {
            "href": "http://localhost:5000/api/seminarTrainees/2"
          },
          "seminarTrainee": {
            "href": "http://localhost:5000/api/seminarTrainees/2"
          },
          "seminar": {
            "href": "http://localhost:5000/api/seminarTrainees/2/seminar"
          },
          "seminarTraineeSpecialitySet": {
            "href": "http://localhost:5000/api/seminarTrainees/2/seminarTraineeSpecialitySet"
          },
          "contractor": {
            "href": "http://localhost:5000/api/seminarTrainees/2/contractor"
          },
          "trainee": {
            "href": "http://localhost:5000/api/seminarTrainees/2/trainee"
          }
        }
      },
      {
        "createdAt": "2018-09-28T09:42:31.461Z",
        "updatedAt": "2018-09-28T09:42:31.461Z",
        "createdBy": "John Tsompos",
        "updatedBy": "John Tsompos",
        "key": 3,
        "cost": 60,
        "actualCost": 60,
        "_links": {
          "self": {
            "href": "http://localhost:5000/api/seminarTrainees/3"
          },
          "seminarTrainee": {
            "href": "http://localhost:5000/api/seminarTrainees/3"
          },
          "seminar": {
            "href": "http://localhost:5000/api/seminarTrainees/3/seminar"
          },
          "seminarTraineeSpecialitySet": {
            "href": "http://localhost:5000/api/seminarTrainees/3/seminarTraineeSpecialitySet"
          },
          "contractor": {
            "href": "http://localhost:5000/api/seminarTrainees/3/contractor"
          },
          "trainee": {
            "href": "http://localhost:5000/api/seminarTrainees/3/trainee"
          }
        }
      }];
        this.setState({
            trainees: trainees
        });
    }

    setColumns(){
        const columns = this.state.specialities.map(x => x
            /*{
              title: x,
              dataIndex: x,
              sorter: true,
              key: x
            }*/);
        this.setState({
            columns: columns
        });
    }

    componentWillMount() {
        this.getSeminar();
        this.getSpecialities();
        this.getTrainees();
        this.setColumns();
    }


    render() {
       
        return (
            <div className="new-seminar-container">
                <h1 className="page-title">Seminar {this.state.seminar.name}</h1>
                <div className="seminar-content">
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="nameTitle" class="seminar-tag">
                                Seminar's Name: 
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="name">
                                {this.state.seminar.name}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="dateTitle" class="seminar-tag">
                                Taking place at:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="date">
                                {formatDate(this.state.seminar.date)}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="seminarTypeTitle" class="seminar-tag">
                                Seminar's Type:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="seminarType">
                                {this.state.seminar.seminarType}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="createdTitle" class="seminar-tag">
                                Created:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="created" >
                                {this.state.seminar.createdBy} at {formatDate(this.state.seminar.createdAt)}
                            </span>
                        </Col>
                    </Row>
                    <Row gutter={16}>
                        <Col span={12}>
                            <span label="updatedTitle" class="seminar-tag">
                                Last edit:
                            </span>
                        </Col>
                        <Col span={12}>
                            <span label="updated" >
                                {this.state.seminar.updatedBy} at {formatDate(this.state.seminar.updatedAt)}
                            </span>
                        </Col>
                    </Row>
                </div>
            </div>
        );
    }
}


export default withRouter(Seminar);
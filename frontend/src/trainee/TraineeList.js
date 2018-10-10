import React, { Component } from 'react';
import { getAllTrainees, deleteItem } from '../util/APIUtils';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Table, notification, Popconfirm, message } from 'antd';
import { LIST_SIZE } from '../constants';
import { withRouter } from 'react-router-dom';
import { Link } from 'react-router-dom';
import {formatHumanDate, humanize, formatDate} from '../util/Helpers';

class TraineeList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            columns : [{
              title: 'AMA',
              dataIndex: 'ama',
              sorter: true,
              key: 'ama',
              render: (name, trainee ) => (
                  <Link to={"trainee/" + trainee.key}>{trainee.ama}</Link>
              )
            }, {
              title: 'Full Name',
              dataIndex: 'name',
              sorter: true,
              key: 'name',
              render: (name, trainee ) => (
                  <Link to={"trainee/" + trainee.key}>{trainee.surname} {trainee.name}</Link>
              )
            }, {
              title: 'Fathers Name',
              dataIndex: 'fathersName',
              sorter: true,
              key: 'fathersName',
            }, {
              title: 'Nationality',
              dataIndex: 'nationality',
              sorter: true,
              key: 'nationality',
            }, {
              title: 'Cart Type',
              dataIndex: 'cardType',
              sorter: true,
              key: 'cardType',
            }, {
              title: 'Cart Status',
              dataIndex: 'cardStatus',
              sorter: true,
              key: 'cardStatus',
            }, {
              title: 'Document Code',
              dataIndex: 'documentCode',
              sorter: true,
              key: 'documentCode',
            }, {
              title: 'Created',
              dataIndex: 'createdBy',
              sorter: true,
              key: 'created',
              render: (created, trainee) => {
                return(
                    <span>{trainee.createdBy} at {formatDate(trainee.createdAt)}</span>
                 )
              }
            }, {
              title: 'Updated',
              dataIndex: 'updatedBy',
              sorter: true,
              key: 'updatedBy',
              render: (updated, trainee) => {
                return(
                    <span>{trainee.updatedBy} at {formatDate(trainee.updatedAt)}</span>
                  )
              }
            }, {
              key: 'edit',
              render: (trainee) => {
                return (
                    <Button>Edit</Button>
                      )
              }
            }, {
              key: 'delete',
              render: (trainee) => {
                  return (
                      <Popconfirm title="Are you sure delete this task?" onConfirm={this.confirm.bind(this, trainee)} onCancel={this.cancel.bind(this)} okText="Yes" cancelText="No">
                        <Button type="danger" >Delete</Button>
                      </Popconfirm>
                  )
              }
            }],
            trainees: [],
            isLoading: false,
            pagination: {},
        };
        this.loadTraineeList = this.loadTraineeList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    confirm(trainee) {
        this.delete.bind(this, trainee);
        this.delete(trainee);
        message.success('Deleted');
    }

    cancel(e) {
        message.error('Canceled delete');
    }

    delete(trainee){
        let promise;

        promise = deleteItem(trainee);

        const trainees = this.state.trainees.filter(i => i.key !== trainee.key)
        this.setState({trainees})
    }

    loadTraineeList(page = 1, size = LIST_SIZE, sorter) {
        let promise;

        promise = getAllTrainees(page -1 , size, sorter);

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                const trainees = this.state.trainees.slice();
                const pagination = this.state.pagination;
                pagination.pageSize = response.page.size;
                pagination.total = response.page.totalElements;
                this.setState({
                    trainees: response._embedded.trainees,
                    isLoading: false,
                    pagination: pagination
                })
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });

    }

    componentWillMount() {
        this.loadTraineeList();
    }

    componentWillReceiveProps(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                trainees: [],
                isLoading: false
            });
            this.loadTraineeList();
        }
    }

    handleLoadMore(pagination, filter, sorter) {
        const pager = this.state.pagination;
        pager.current = pagination.current;
        this.setState({
            pagination: pager,
        });     
        this.loadTraineeList(this.state.pagination.current, LIST_SIZE, sorter);
    }

    render() {
        return (
            <div className="list-container">
                <h1 className="page-title">Trainees<Button className="add-button" type="Submit" >Add Trainee</Button></h1>
                <div className="list-content">
                    <Table 
                        columns={this.state.columns} 
                        dataSource={this.state.trainees} 
                        loading={this.state.isLoading}
                        pagination={this.state.pagination}
                        onChange={this.handleLoadMore}
                    />
                </div>
            </div>
        );    

    }
}

export default withRouter(TraineeList);
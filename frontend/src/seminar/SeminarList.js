import React, { Component } from 'react';
import { getAllSeminars, deleteItem } from '../util/APIUtils';
import Seminar from './Seminar';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Upload, Icon, Table, Pagination, Popconfirm, message } from 'antd';
import { LIST_SIZE } from '../constants';
import { withRouter } from 'react-router-dom';
import { Link } from 'react-router-dom';
import {formatHumanDate, humanize, formatDate} from '../util/Helpers';

class SeminarList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            columns : [{
              title: 'Name',
              dataIndex: 'name',
              sorter: true,
              key: 'name',
              render: (name, seminar ) => (
                  <Link to={{ pathname: "/seminar/" + seminar.key, state:{seminar: this.seminar} }}>{seminar.name}</Link>
              )
            }, {
              title: 'Date',
              dataIndex: 'date',
              sorter: true,
              key: 'date',
              render: (date) => (
                formatHumanDate(date)
              )
            }, {
              title: 'Type',
              dataIndex: 'seminarType',
              sorter: true,
              key: 'seminarType',
              render: (seminarType) => (
                  humanize(seminarType)
              )
            }, {
              title: 'Created',
              dataIndex: 'createdBy',
              sorter: true,
              key: 'created',
              render: (created, seminar) => {
                return(
                    <span>{seminar.createdBy} at {formatDate(seminar.createdAt)}</span>
                 )
              }
            }, {
              title: 'Updated',
              dataIndex: 'updatedBy',
              sorter: true,
              key: 'updatedBy',
              render: (updated, seminar) => {
                return(
                    <span>{seminar.updatedBy} at {formatDate(seminar.updatedAt)}</span>
                  )
              }
            }, {
              key: 'delete',
              render: (seminar) => {
                  return (
                      <Popconfirm title="Are you sure delete this task?" onConfirm={this.confirm.bind(this, seminar)} onCancel={this.cancel.bind(this)} okText="Yes" cancelText="No">
                        <Button type="danger" >Delete</Button>
                      </Popconfirm>
                  )
              }
            }],
            seminars: [],
            isLoading: false,
            pagination: {
                pageSize: LIST_SIZE,
                showSizeChanger: true,
                pageSizeOptions: ['5','10','20','30','40']
            },
        };
        this.loadSeminarList = this.loadSeminarList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    confirm(seminar) {
        this.delete.bind(this, seminar);
        this.delete(seminar);
        message.success('Deleted');
    }

    cancel(e) {
        message.error('Canceled delete');
    }

    delete(seminar){
        let promise;

        promise = deleteItem(seminar);

        const seminars = this.state.seminars.filter(i => i.key !== seminar.key)
        this.setState({seminars})
    }

    loadSeminarList(page = 1, size = LIST_SIZE, sorter) {
        let promise;

        promise = getAllSeminars(page -1 , size, sorter);

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                const seminars = this.state.seminars.slice();
                const pagination = this.state.pagination;
                pagination.total = response.page.totalElements;
                this.setState({
                    seminars: response._embedded.seminars,
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
        this.loadSeminarList();
    }

    componentWillReceiveProps(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                seminars: [],
                isLoading: false
            });
            this.loadSeminarList();
        }
    }

    handleLoadMore(pagination, filter, sorter) {
        this.setState({
            pagination: pagination,
        });     
        this.loadSeminarList(pagination.current, pagination.pageSize, sorter);
    }

    render() {
        return (
            <div className="list-container">
                <h1 className="page-title">
                    Seminars
                </h1>
                <div className="list-content">
                    <Table 
                        columns={this.state.columns} 
                        dataSource={this.state.seminars} 
                        loading={this.state.isLoading}
                        pagination={this.state.pagination}
                        onChange={this.handleLoadMore}
                        onShowSizeChange={this.loadSeminarList}
                    />
                </div>
            </div>
        );    

    }
}

export default withRouter(SeminarList);
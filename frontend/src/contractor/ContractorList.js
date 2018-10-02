import React, { Component } from 'react';
import { getAllContractors, getUserCreatedContractors, getUserVotedContractors } from '../util/APIUtils';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Icon, notification } from 'antd';
import { CONTRACTOR_LIST_SIZE } from '../constants';
import { withRouter } from 'react-router-dom';
import './ContractorList.css';
import { Table } from 'antd';
import { formatDateTime } from '../util/Helpers';

class ContractorList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            columns : [{
              title: 'Name',
              dataIndex: 'name',
              sorter: true,
              key: 'name',
            }, {
              title: 'Date',
              dataIndex: 'date',
              sorter: true,
              key: 'date',
              render: (date) => (
                formatDateTime(date)
              )
            }, {
              title: 'Type',
              dataIndex: 'contractorType',
              sorter: true,
              key: 'contractorType',
            }, {
              title: 'Created by',
              dataIndex: 'createdBy',
              sorter: true,
              key: 'createdBy',
            }, {
              title: 'Created at',
              dataIndex: 'createdAt',
              sorter: true,
              key: 'createdAt',
              render: (createdAt) => (
                 formatDateTime(createdAt)
              )
            }, {
              title: 'Updated by',
              dataIndex: 'updatedBy',
              sorter: true,
              key: 'updatedBy',
            }, {
              title: 'Updated at',
              dataIndex: 'updatedAt',
              sorter: true,
              key: 'updatedAt',
              render: (updatedAt) => (
                 formatDateTime(updatedAt)
              )
            }],
            contractors: [],
            isLoading: false,
            pagination: {},
        };
        this.loadContractorList = this.loadContractorList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    loadContractorList(page = 1, size = CONTRACTOR_LIST_SIZE, sorter) {
        let promise;

        promise = getAllContractors(page -1 , size, sorter);

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                const contractors = this.state.contractors.slice();
                const pagination = this.state.pagination;
                pagination.pageSize = response.page.size;
                pagination.total = response.page.totalElements;
                this.setState({
                    contractors: response._embedded.contractors,
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
        this.loadContractorList();
    }

    componentWillReceiveProps(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                contractors: [],
                isLoading: false
            });
            this.loadContractorList();
        }
    }

    handleLoadMore(pagination, filter, sorter) {
        const pager = this.state.pagination;
        pager.current = pagination.current;
        this.setState({
            pagination: pager,
        });     
        this.loadContractorList(this.state.pagination.current, CONTRACTOR_LIST_SIZE, sorter);
    }

    render() {
        return (
            <div className="contractorList-container">
                <h1 className="page-title">Contractors</h1>
                <div className="contractorList-content">
                    <Table 
                        columns={this.state.columns} 
                        dataSource={this.state.contractors} 
                        loading={this.state.isLoading}
                        pagination={this.state.pagination}
                        onChange={this.handleLoadMore}
                    />
                </div>
            </div>
        );    

    }
}

export default withRouter(ContractorList);
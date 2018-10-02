import React, { Component } from 'react';
import { getAllContractors, getUserCreatedcontractors, getUserVotedcontractors } from '../util/APIUtils';
import { CONTRACTOR_LIST_SIZE } from '../constants';
import LoadingIndicator  from '../common/LoadingIndicator';
import { withRouter } from 'react-router-dom';
import { Button, Icon, notification } from 'antd';
import './ContractorList.css';
import { Table } from 'antd';

class ContractorList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            columns: [{
              title: 'Name',
              dataIndex: 'name',
              key: 'name',
            }, {
              title: 'Date',
              dataIndex: 'date',
              key: 'date',
            }, {
              title: 'Type',
              dataIndex: 'contractorType',
              key: 'contractorType',
            }, {
              title: 'Created by',
              dataIndex: 'createdBy',
              key: 'createdBy',
              render: (creator) => {
                return creator.name
              }
            }],
            contractors: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            currentVotes: [],
            isLoading: false,
            searchText: '',
            pagination: {},
        };
        this.loadcontractorList = this.loadcontractorList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    loadcontractorList(page = 1, size = CONTRACTOR_LIST_SIZE) {
        let promise;

        promise = getAllContractors(page -1 , size);

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
                    page: pagination.current,
                    size: response.page.size,
                    totalElements: response.page.totalElements,
                    totalPages: response.page.totalPages,
                    last: response.page.last +1,
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
        this.loadcontractorList();
    }

    componentWillReceiveProps(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                contractors: [],
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                currentVotes: [],
                isLoading: false
            });
            this.loadcontractorList();
        }
    }

    handleLoadMore(pagination) {
        const pager = this.state.pagination;
        pager.current = pagination.current;
        this.setState({
            pagination: pager,
        });     
        this.loadcontractorList(this.state.pagination.current);
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
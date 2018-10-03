import React, { Component } from 'react';
import { getAllContractors, deleteItem } from '../util/APIUtils';
import Contractor from './Contractor';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Table, notification, Popconfirm, message } from 'antd';
import { LIST_SIZE } from '../constants';
import { withRouter } from 'react-router-dom';
import './ContractorList.css';
import { Link } from 'react-router-dom';
import {formatHumanDate, humanize, formatDate} from '../util/Helpers';

class ContractorList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            columns : [{
              title: 'AFM',
              dataIndex: 'afm',
              sorter: true,
              key: 'afm',
              render: (name, contractor ) => (
                  <Link to={"contractors/" + contractor.key}>{contractor.afm}</Link>
              )
            }, {
              title: 'Name',
              dataIndex: 'name',
              sorter: true,
              key: 'name',
              render: (name, contractor ) => (
                  <Link to={"contractors/" + contractor.key}>{contractor.name}</Link>
              )
            }, {
              title: 'Representative Name',
              dataIndex: 'representativeName',
              sorter: true,
              key: 'representativeName',
            }, {
              title: 'Phone Number',
              dataIndex: 'phoneNumber',
              sorter: true,
              key: 'phoneNumber',
            }, {
              title: 'Email',
              dataIndex: 'email',
              sorter: true,
              key: 'email',
            }, {
              key: 'edit',
              render: (contractor) => {
                return (
                    <Button>Edit</Button>
                      )
              }
            }, {
              key: 'delete',
              render: (contractor) => {
                  return (
                      <Popconfirm title="Are you sure delete this task?" onConfirm={this.confirm.bind(this, contractor)} onCancel={this.cancel.bind(this)} okText="Yes" cancelText="No">
                        <Button type="danger" >Delete</Button>
                      </Popconfirm>
                  )
              }
            }],
            contractors: [],
            isLoading: false,
            pagination: {},
        };
        this.loadContractorList = this.loadContractorList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    confirm(contractor) {
        this.delete.bind(this, contractor);
        this.delete(contractor);
        message.success('Deleted');
    }

    cancel(e) {
        message.error('Canceled delete');
    }

    delete(contractor){
        let promise;

        promise = deleteItem(contractor);

        const contractors = this.state.contractors.filter(i => i.key !== contractor.key)
        this.setState({contractors})
    }

    loadContractorList(page = 1, size = LIST_SIZE, sorter) {
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
        this.loadContractorList(this.state.pagination.current, LIST_SIZE, sorter);
    }

    render() {
        return (
            <div className="contractorList-container">
                <h1 className="page-title">Contractors</h1>
                <div className="contractorList-content">
                    <Table 
                        columns={this.state.columns} 
                        // onRow={(contractor) => {
                        //         return {
                        //           onClick: () => {window.location=contractor._links.self.href}
                        //         };
                        //       }}  
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
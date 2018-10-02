import React, { Component } from 'react';
import { getAllSeminars, getUserCreatedSeminars, getUserVotedSeminars } from '../util/APIUtils';
import Seminar from './Seminar';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Icon, notification } from 'antd';
import { SEMINAR_LIST_SIZE } from '../constants';
import { withRouter } from 'react-router-dom';
import './SeminarList.css';
import { Table } from 'antd';
import { formatDateTime } from '../util/Helpers';

class SeminarList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            seminars: [],
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
        this.loadSeminarList = this.loadSeminarList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    loadSeminarList(page = 0, size = SEMINAR_LIST_SIZE) {
        let promise;

        promise = getAllSeminars(page, size);

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                const seminars = this.state.seminars.slice();
                const currentVotes = this.state.currentVotes.slice();
                const pagination = this.state.pagination;
                pagination.pageSize = response.size;
                pagination.total = response.totalElements;
                pagination.onChange = this.handleLoadMore
                this.setState({
                    seminars: seminars.concat(response.content),
                    page: pagination.current,
                    size: response.size,
                    totalElements: response.totalElements,
                    totalPages: response.totalPages,
                    last: response.last,
                    currentVotes: currentVotes.concat(Array(response.content.length).fill(null)),
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
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                currentVotes: [],
                isLoading: false
            });
            this.loadSeminarList();
        }
    }

    handleLoadMore(pagination) {
        const pager = this.state.pagination;
        pager.current = pagination.current;
        this.setState({
            pagination: pager,
        });     
        this.loadSeminarList(this.state.pagination.current);
    }

    handleVoteChange(event, seminarIndex) {
        const currentVotes = this.state.currentVotes.slice();
        currentVotes[seminarIndex] = event.target.value;

        this.setState({
            currentVotes: currentVotes
        });
    }


    handleVoteSubmit(event, seminarIndex) {
        event.preventDefault();
        if(!this.props.isAuthenticated) {
            this.props.history.push("/login");
            notification.info({
                message: 'Seminaring App',
                description: "Please login to vote.",
            });
            return;
        }

        const seminar = this.state.seminars[seminarIndex];
        const selectedChoice = this.state.currentVotes[seminarIndex];

    }

    handleSearch = (selectedKeys, confirm) => () => {
        confirm();
        this.setState({ searchText: selectedKeys[0] });
    }

    handleReset = clearFilters => () => {
        clearFilters();
        this.setState({ searchText: '' });
    }

    render() {
        const columns = [{
          title: 'Name',
          dataIndex: 'name',
          key: 'name',
        }, {
          title: 'Date',
          dataIndex: 'date',
          key: 'date',
          render: (date) => {
            return formatDateTime(date);
          },
        }, {
          title: 'Type',
          dataIndex: 'seminarType',
          key: 'seminarType',
        }, {
          title: 'Created by',
          dataIndex: 'createdBy',
          key: 'createdBy',
          render: (creator) => {
            return creator.name
          }
        }];
        return (
            <div className="seminarList-container">
                <h1 className="page-title">Seminars</h1>
                <div className="seminarList-content">
                    <Table 
                        columns={columns} 
                        dataSource={this.state.seminars} 
                        loading={this.state.isLoading}
                        pagination={this.state.pagination}
                        onChange={this.handleLoadMore}
                    />
                </div>
            </div>
        );    

    }
}

export default withRouter(SeminarList);
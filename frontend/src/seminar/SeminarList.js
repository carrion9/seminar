import React, { Component } from 'react';
import { getAllSeminars, getUserCreatedSeminars, getUserVotedSeminars } from '../util/APIUtils';
import Seminar from './Seminar';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Icon, notification } from 'antd';
import { SEMINAR_LIST_SIZE } from '../constants';
import { withRouter } from 'react-router-dom';
import './SeminarList.css';

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
            isLoading: false
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

                this.setState({
                    seminars: seminars.concat(response.content),
                    page: response.page,
                    size: response.size,
                    totalElements: response.totalElements,
                    totalPages: response.totalPages,
                    last: response.last,
                    currentVotes: currentVotes.concat(Array(response.content.length).fill(null)),
                    isLoading: false
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

    handleLoadMore() {
        this.loadSeminarList(this.state.page + 1);
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

    render() {
        const seminarViews = [];
        this.state.seminars.forEach((seminar, seminarIndex) => {
            seminarViews.push(<Seminar
                key={seminar.id}
                seminar={seminar}
                currentVote={this.state.currentVotes[seminarIndex]}
                handleVoteChange={(event) => this.handleVoteChange(event, seminarIndex)}
                handleVoteSubmit={(event) => this.handleVoteSubmit(event, seminarIndex)} />)
        });

        return (
            <div className="seminars-container">
                {seminarViews}
                {
                    !this.state.isLoading && this.state.seminars.length === 0 ? (
                        <div className="no-seminars-found">
                            <span>No Seminars Found.</span>
                        </div>
                    ): null
                }
                {
                    !this.state.isLoading && !this.state.last ? (
                        <div className="load-more-seminars">
                            <Button type="dashed" onClick={this.handleLoadMore} disabled={this.state.isLoading}>
                                <Icon type="plus" /> Load more
                            </Button>
                        </div>): null
                }
                {
                    this.state.isLoading ?
                        <LoadingIndicator />: null
                }
            </div>
        );
    }
}

export default withRouter(SeminarList);
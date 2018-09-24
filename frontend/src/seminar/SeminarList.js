import React, { Component } from 'react';
import { getAllSeminars, getUserCreatedSeminars, getUserVotedSeminars } from '../util/APIUtils';
import Seminar from './Seminar';
import { castVote } from '../util/APIUtils';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Icon, notification } from 'antd';
import { POLL_LIST_SIZE } from '../constants';
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

    loadSeminarList(page = 0, size = POLL_LIST_SIZE) {
        let promise;
        if(this.props.username) {
            if(this.props.type === 'USER_CREATED_POLLS') {
                promise = getUserCreatedSeminars(this.props.username, page, size);
            } else if (this.props.type === 'USER_VOTED_POLLS') {
                promise = getUserVotedSeminars(this.props.username, page, size);
            }
        } else {
            promise = getAllSeminars(page, size);
        }

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

        const voteData = {
            seminarId: seminar.id,
            choiceId: selectedChoice
        };

        castVote(voteData)
            .then(response => {
                const seminars = this.state.seminars.slice();
                seminars[seminarIndex] = response;
                this.setState({
                    seminars: seminars
                });
            }).catch(error => {
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login to vote');
            } else {
                notification.error({
                    message: 'Seminaring App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
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
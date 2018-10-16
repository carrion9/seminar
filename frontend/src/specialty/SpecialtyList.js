import React, {Component} from 'react';
import {getAllSpecialties, deleteItem} from '../util/APIUtils';
import {Button, Table, Input, Icon, Popconfirm, message, Pagination} from 'antd';
import {LIST_SIZE} from '../constants';
import {withRouter} from 'react-router-dom';
import {Link} from 'react-router-dom';
import {formatDate} from '../util/Helpers';

class SpecialtyList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            columns: [{
                title: 'Name',
                dataIndex: 'name',
                sorter: true,
                key: 'name',
                render: (name, specialty) => (
                    <Link to={"specialties/" + specialty.key}>{specialty.name}</Link>
                ),
                filterDropdown: ({setSelectedKeys, selectedKeys, confirm, clearFilters}) => (
                    <div className="custom-filter-dropdown">
                        <Input
                            ref={ele => this.searchInput = ele}
                            placeholder="Search name"
                            value={selectedKeys[0]}
                            onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                            onPressEnter={this.handleSearch(selectedKeys, confirm)}
                        />
                        <Button type="primary" onClick={this.handleSearch(selectedKeys, confirm)}>Search</Button>
                        <Button onClick={this.handleReset(clearFilters)}>Reset</Button>
                    </div>
                ),
                filterIcon: filtered => <Icon type="smile-o" style={{color: filtered ? '#108ee9' : '#aaa'}}/>,
                onFilter: (value, record) => record.name.toLowerCase().includes(value.toLowerCase()),
                onFilterDropdownVisibleChange: (visible) => {
                    if (visible) {
                        setTimeout(() => {
                            this.searchInput.focus();
                        });
                    }
                },
                render: (text) => {
                    const {searchText} = this.state;
                    return searchText ? (
                        <span>
                            {text.split(new RegExp(`(?<=${searchText})|(?=${searchText})`, 'i')).map((fragment, i) => (
                            fragment.toLowerCase() === searchText.toLowerCase()
                            ? <span key={i} className="highlight">{fragment}</span> : fragment // eslint-disable-line
                            ))}
                        </span>
                    ) : text;
                },
            }, {
                title: 'Created',
                dataIndex: 'createdBy',
                sorter: true,
                key: 'created',
                render: (created, specialty) => {
                    return (
                        <span>{specialty.createdBy} at {formatDate(specialty.createdAt)}</span>
                    )
                }
            }, {
                title: 'Updated',
                dataIndex: 'updatedBy',
                sorter: true,
                key: 'updatedBy',
                render: (updated, specialty) => {
                    return (
                        <span>{specialty.updatedBy} at {formatDate(specialty.updatedAt)}</span>
                    )
                }
            }, {
                key: 'edit',
                render: (specialty) => {
                    return (
                        <Button>Edit</Button>
                    )
                }
            }, {
                key: 'delete',
                render: (specialty) => {
                    return (
                        <Popconfirm title="Are you sure delete this specialty?"
                                    onConfirm={this.confirm.bind(this, specialty)} onCancel={this.cancel.bind(this)}
                                    okText="Yes" cancelText="No">
                            <Button type="danger">Delete</Button>
                        </Popconfirm>
                    )
                }
            }],
            specialties: [],
            isLoading: false,
            pagination: {
                pageSize: LIST_SIZE,
                showSizeChanger: true,
                pageSizeOptions: ['5','10','20','30','40']
            },
        };
        this.loadSpecialtyList = this.loadSpecialtyList.bind(this);
        this.handleLoadMore = this.handleLoadMore.bind(this);
    }

    confirm(specialty) {
        this.delete.bind(this, specialty);
        this.delete(specialty);
        message.success('Deleted');
    }

    cancel(e) {
        message.error('Canceled delete');
    }

    delete(specialty) {
        let promise;

        promise = deleteItem(specialty);

        const specialties = this.state.specialties.filter(i => i.key !== specialty.key)
        this.setState({specialties})
    }

    loadSpecialtyList(page = 1, size = LIST_SIZE, sorter, filter) {
        let promise;

        promise = getAllSpecialties(page - 1, size, sorter, filter);

        if (!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

        promise
            .then(response => {
                const specialties = this.state.specialties.slice();
                const pagination = this.state.pagination;
                pagination.pageSize = response.page.size;
                pagination.total = response.page.totalElements;
                this.setState({
                    specialties: response._embedded.specialties,
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
        this.loadSpecialtyList();
    }

    componentWillReceiveProps(nextProps) {
        if (this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                specialties: [],
                isLoading: false
            });
            this.loadSpecialtyList();
        }
    }

    handleLoadMore(pagination, filter, sorter) {
        this.setState({
            pagination: pagination,
        });
        this.loadSpecialtyList(pagination.current, pagination.pageSize, sorter);
    }

    handleSearch = (selectedKeys, confirm) => () => {
        confirm();
        this.setState({ searchText: selectedKeys[0] });
    };

    handleReset = clearFilters => () => {
        clearFilters();
        this.setState({ searchText: '' });
    };

    render() {
        return (
            <div className="list-container">
                <h1 className="page-title">Specialties<Button className="add-button" type="Submit" >Add Specialty</Button></h1>
                <div className="list-content">
                    <Table
                        columns={this.state.columns}
                        dataSource={this.state.specialties}
                        loading={this.state.isLoading}
                        pagination={this.state.pagination}
                        onChange={this.handleLoadMore}
                        onShowSizeChange={this.loadSpecialtyList}
                    />
                </div>
            </div>
        );

    }
}

export default withRouter(SpecialtyList);
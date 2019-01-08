import React, { Component } from 'react';
import { withRouter, Link } from 'react-router-dom';
import LoadingIndicator from '../common/LoadingIndicator';
import { 
      Table,
      Popconfirm,
      Button,
      notification,
      message
} from 'antd';
import { LIST_SIZE } from '../constants';
import { formatHumanDate } from '../util/Helpers';
import { deleteItem, getAllUsers } from '../util/APIUtils';

class UserList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            columns: [{
                  title: 'Name',
                  dataIndex: 'name',
                  key: 'name',
                  render: (name, record) =>{
                        return (
                              <Link to={{ pathname: "/user/" + record.username, state:{user: this.record} }}>{name}</Link>
                              );
                  }
            },{
                title: 'Username',
                dataIndex: 'username',
                key: 'username',
            },{
                title: 'Email',
                dataIndex: 'email',
                key: 'email',
            },{
                title: 'Role',
                dataIndex: 'roles[0].name',
                key: 'role',
            },{
                  key: 'delete',
                  render: (record) => {
                        return (
                              <Popconfirm title="Are you sure delete this user?"
                                    onConfirm={this.deleteRecord.bind(this, record)}>
                                          <Button type="danger">Delete</Button>
                              </Popconfirm>
                  )}
            }],
            dataSource:[]
        };
    }

    componentWillMount(){
        this.loadUsersList();
    }

    loadUsersList(page = 1, size = LIST_SIZE, sorter){
        this.setState({
            isLoading: true
        });
        let promise;

        promise = getAllUsers(page -1 , size, sorter);

        if (!promise) {
            return;
        }

        promise
            .then(response => {
                this.setState({
                    dataSource: response.content ? response.content : [],
                    isLoading: false
                });
            }).catch(error => {
            this.setState({
                isLoading: false
            })
        });
    }

    deleteRecord(item){

          let promise;

          promise = deleteItem(item);

          const dataSource = this.state.dataSource.filter(i => i.id !== item.id)
          this.setState({dataSource})

          message.success('Deleted');

      }

    componentWillReceiveProps(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                dataSource: [],
                isLoading: false
            });
            this.loadTraineeList();
        }
    }

    handleLoadMore(pagination, filter, sorter) {
        this.setState({
            pagination: pagination,
        });     
    }

    render(){
          if(this.state.isLoading) {
                return <LoadingIndicator />
          }

          return (
              <div className="list-container">
                  <div className="list-content">
                      <Table  
                          title ={() =>{ return (
                              <div className="table-header">
                                <span className="table-title">Users</span>
                                <Button className="add-main-button" type="primary" href="/user/new">Add User</Button>
                              </div>
                            )}}
                          columns={this.state.columns} 
                          dataSource={this.state.dataSource} 
                          loading={this.state.isLoading}
                          pagination={this.state.pagination}
                          onShowSizeChange={this.loadUsersList}
                          onChange={this.handleLoadMore}
                      />
                  </div>
              </div>
          );   
    }
}
export default withRouter(UserList);




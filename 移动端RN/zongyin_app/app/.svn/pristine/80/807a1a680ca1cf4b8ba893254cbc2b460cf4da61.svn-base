
/**
 * Created by 卓原 on 2017/10/31.
 * zhuoyuan93@gmail.com
 */
import React from 'react';
import {
    View,
    Text,
    StyleSheet,
    Image,
    TextInput,
    FlatList,
    TouchableOpacity,
    AsyncStorage,
    Alert,
    Button
} from 'react-native';

import {sys,NoDataView} from "../common/Data"
const host = sys.host;

import HttpUtils from "../common/HttpUtil"
import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'


export default class RechargeRecord extends React.Component {






    constructor(props) {
        super(props)

        this.state = {
            dataList: [],
            refreshState: RefreshState.HeaderRefreshing,
            currentPage:1,
            pageSize:20,
            showLoading:false
        }
    }

    componentDidMount(){
        this.onHeaderRefresh()
    }

    onHeaderRefresh = () => {
        this.state.currentPage = 1;
        this.setState(
            {
                refreshState: RefreshState.HeaderRefreshing,
            })
        //获取测试数据
        if(this.state.refreshState !=RefreshState.FooterRefreshing) this.getDataList(true)

    }

    onFooterRefresh = () => {
        this.state.currentPage = this.state.currentPage+1;
        this.setState({
            refreshState: RefreshState.FooterRefreshing
        })

        if(this.state.refreshState != RefreshState.HeaderRefreshing)
            this.getDataList(false)
    }


    getDataList(isReload){

        let codeurl = host + '/fubi/purchase/fubiHIstoryList';
        let formData = new FormData();

        // 请求参数 ('key',value)
        formData.append('page',this.state.currentPage);
        formData.append('page_size',this.state.pageSize);

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                
                this.setState({
                    refreshState: RefreshState.Idle,
                })

                let testData = result['data']['rows'];
                let newList = testData.map((data) => {
                    return {
                        cdate:data["cdate"],
                        explain:data["explain"],
                    }
                });

                let dataList =  isReload ? newList : [...this.state.dataList, ...newList]
                let state = dataList.length > result['data']['total'] ? RefreshState.NoMoreData : RefreshState.Idle;
               
                this.setState({
                    dataList: dataList,
                    refreshState: state,
                })

            },(error)=>{
                //这里处理reject回调方法
                // Alert.alert(error)
                this.setState({
                    refreshState: RefreshState.Idle,
                })
            })
            .catch(error=>{

                this.setState({
                    dataList: dataList,
                    refreshState: RefreshState.Idle,
                })
            })
    }

    render() {
        return(
            <View style={styles.container}>

                {this.state.dataList.length==0?<NoDataView text={"暂无购币记录"}/>:
                <RefreshListView

                    renderItem={this._renderCell}
                    refreshState={this.state.refreshState}
                    onHeaderRefresh={this.onHeaderRefresh}
                    onFooterRefresh={this.onFooterRefresh}
                    //onRefresh={this.refreshing}
                    refreshing={false}
                    onEndReachedThreshold={0}
                    keyExtractor={this.keyExtractor}
                    data={this.state.dataList}>
                </RefreshListView>}
            </View>
        )
    }

    keyExtractor = (item, index) => {
        return index
    }


    _renderCell = (item) => {
        let record =item.item
        var content = "0";

        return <TouchableOpacity style={styles.content}>
          <Text style={styles.leftText}>{record.cdate}</Text>
          <Text style={styles.rightText}>{record.explain}</Text>
        </TouchableOpacity>
    }
    _separator = () => {
        return <View style={{height:1,backgroundColor:'#f2f2f2'}}/>;
    }

}

const styles = StyleSheet.create({

    container: {
        flex:1,
        backgroundColor:sys.grayColor
    },

    content:{
        backgroundColor:'white',
        flexDirection:'row',
        height:50,
        width:sys.dwidth,
        alignItems:'center',
    },

    leftText:{
        marginLeft:15,
        fontSize:14,
        width:145
    },

    rightText:{
        marginLeft:sys.dwidth - 290,
        fontSize:14,
        width:120,
        textAlign:'right'
    }
})



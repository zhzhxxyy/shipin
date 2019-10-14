
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
    Switch,
    Button
} from 'react-native';
import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'
import {sys,NoDataView} from "../common/Data"
const host = sys.host;

import HttpUtils from "../common/HttpUtil"


export default class MyFollow extends React.Component {



    static  navigationOptions = ({navigation}) => ({
        headerTitle:"邀请记录",
        headerTitleStyle:{
            alignSelf:'center',
            flex: 1,
            textAlign: 'center',
            
        },
        headerRight: <View />
    });

    constructor(props) {
        super(props)

        this.state = {
            dataList: [],
            refreshState: RefreshState.HeaderRefreshing,
            currentPage:1,
            pageSize:20
        }
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



    componentDidMount(){

        this.getDataList();
    }


    getDataList(isReload){

        let codeurl = host + '/member/index/getInviteMemberList';
        let formData = new FormData();

        // 请求参数 ('key',value)
        formData.append('page',this.state.currentPage);
        formData.append('page_size',this.state.pageSize);

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                let testData = result['data']['rows'];
                let newList = testData.map((data) => {
                    return {
                        head_pic:data["head_pic"],
                        name:data["name"],
                        identifier:data["identifier"]
                    }
                });

                let dataList =  isReload ? newList : [...this.state.dataList, ...newList]
                let state = dataList.length > result['data']['total'] ? RefreshState.NoMoreData : RefreshState.Idle;
                if(isReload){
                    state = RefreshState.Idle;
                }
                this.setState({
                    dataList: dataList,
                    refreshState: dataList.length >= result['data']['total'] ? RefreshState.NoMoreData : RefreshState.Idle,
                })

            })
            .catch(error=>{
              
            })

    }

    render() {



        let showView = this.state.dataList.length?
            <RefreshListView
                data={this.state.dataList}
                keyExtractor={this.keyExtractor}
                renderItem={this.renderCell}
                refreshState={this.state.refreshState}
                onHeaderRefresh={this.onHeaderRefresh}
                onFooterRefresh={this.onFooterRefresh}
                numColumns ={1}
                ListHeaderComponent={this.renderHeadView}
                // horizontal={false}
                //ItemSeparatorComponent={this._separator}
                // 可选
                footerRefreshingText= '玩命加载中 >.<'
                footerFailureText = '我擦嘞，居然失败了 =.=!'
                footerNoMoreDataText= '-我是有底线的-'
            />
        :<NoDataView

            text = {"暂无邀请记录"}
            />
        return(

            <View style={styles.container}>

                {showView}

            </View>



        )

    }


    itemView(index){


        var follow = this.state.dataList[index];


        return <View style={styles.content}>

            <Image style={styles.contentIconeImage} source={{uri:follow.head_pic}}></Image>
            <View style={styles.contentInfoStyles}>
                <Text style={styles.nametext}>{follow.name}</Text>
                <Text style={styles.numtext}>{follow.identifier}</Text>
            </View>

        </View>

    }

    keyExtractor = (item, index) => {
        return index
    }



    changeGz(gzob,flistD) {

        if (gzob.isgz == 0) {
            gzob.isgz =1;
        } else {
            gzob.isgz =0;
        }


        this.setState({

            flistData:flistD
        })

    }

    renderCell = (item) => {

        return this.itemView(item.index)
    }

    // _header = () => {
    //
    // }

    // _footer = () => {
    //     return <Text style={[styles.txt,{backgroundColor:'#333333'},{fontSize:14}]}>购币记录></Text>;
    // }

    _separator = () => {
        return <View style={{height:1,backgroundColor:'#f2f2f2'}}/>;
    }

}

const styles = StyleSheet.create({

    container: {
        flex:1,
        backgroundColor:sys.grayColor
    },

    nametext:{
        // marginTop:11,
        // width:100,
        fontSize:14,
        color:'white',
        position: 'absolute',
        // bottom: 20,
        // left: 0,
        top: 5,
        color:'#333333'

    },
    numtext:{
        // marginLeft:10,
        // marginTop:11,
        // width:100,
        fontSize:11,
        color:'#333333',
        position: 'absolute',
        bottom: 5,
        // left: 0,
        //  right: 10,
    },

    content:{
        backgroundColor:'white',
        flexDirection:'row',
        height:50,
        width:sys.dwidth
    },


    contentIconeImage:{
        marginTop:5,
        //alignSelf:'center',
        //alignItems:'center',
        marginLeft:10,
        width:40,
        height:40
    },
    contentInfoStyles:{
        // flexDirection:'row',
        //   backgroundColor:'white',
        marginTop:5,
        marginLeft:10,
        //width:150,
        height:40
    },

    switchStyte:{
        width:80,
        height:30,
        position: 'absolute',
        bottom: 10,
        // left: 0,
        right: 15,
        resizeMode:'center'

        //  backgroundColor:'red'
    }


})

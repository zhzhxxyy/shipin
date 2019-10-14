
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



export default class ConsumeDetail extends React.Component {



    static  navigationOptions = ({navigation}) => ({
        headerTitle:"善金明细",
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


        let codeurl = host +'/fubi/purchase/getUsersConsumeList'
        // 请求参数 ('key',value)
        let formData = new FormData();
        formData.append('page',this.state.currentPage);
        formData.append('page_size',this.state.pageSize);

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                let testData = result['data']['rows'];
                let newList = testData.map((data) => {
                    return {
                        fubi:data['fubi'],
                        extra:data["extra"],
                        create_time:data["create_time"]
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
             
                this.setState({
                    refreshState: RefreshState.Idle,
                })
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

                text = {"暂无记录"}
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

            {/*<Image style={styles.contentIconeImage} source={{uri:follow.head_pic}}></Image>*/}
            <View style={styles.contentInfoStyles}>
                <Text
                    numberOfLines={1}
                    style={styles.nametext}>{follow.extra}</Text>
                <Text style={styles.numtext}>{follow.create_time}</Text>
            </View>
            <Text style={styles.fubi}>{follow.fubi}</Text>
        </View>

    }

    keyExtractor = (item, index) => {
        return index
    }

    renderCell = (item) => {

        return this.itemView(item.index)
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

    nametext:{

        fontSize:14,
        marginTop: 10,
        color:'#333333'

    },
    numtext:{

        marginTop:10,
        fontSize:11,
        color:'#999999',
        bottom: 5,

    },

    content:{
        backgroundColor:'white',
        flexDirection:'row',
        height:50,
        width:sys.dwidth
    },

    contentInfoStyles:{
        width:250,
        left:10
    },
    fubi:{
        marginTop:10,
        marginLeft:sys.dwidth-340,
        height:30,
        width:80,
        color:sys.mainColor,
        fontSize:12,
        textAlign:'right'
    }

})

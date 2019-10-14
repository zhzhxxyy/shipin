
import React, {Component} from 'react'
import {
    View, StyleSheet, Text, Platform, Alert, TouchableOpacity, Image, PixelRatio, ImageBackground,
    DeviceEventEmitter
} from 'react-native'
import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'


import {Button, sys} from "../common/Data"
const host = sys.host;
import HttpUtils from "../common/HttpUtil"
import DetailCell from  './CourseCell'
import RNLogin from "../common/RNLoginModule"
import DialogSelected from '../common/AlertSelect';

export default class CourseDetail extends React.Component {

    static  navigationOptions = ({navigation}) => ({
        headerTitle:"上师详情",
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
            pageSize:10,
            headInfo:{},
            isFollow:true
        }
    }


    componentDidMount() {

        this.onHeaderRefresh()
    }

    onHeaderRefresh = () => {
        this.state.currentPage = 1;
        this.setState(
            {
                currentPage:1
            })
        //获取测试数据
        this.getDataList(true)

    }

    onFooterRefresh = () => {
        this.state.currentPage = this.state.currentPage+1;
        this.setState({
            refreshState: RefreshState.FooterRefreshing,
        })


        this.getDataList(false)
    }

    // 获取测试数据
    getDataList(isReload) {

        const {params} = this.props.navigation.state;
        let codeurl = host + '/live/index/getHostDetail';
        let formData = new FormData();
        let default_pic =  "http://p2bm1h3pe.bkt.clouddn.com/1517361721_php0UDGba.png";
        // 请求参数 ('key',value)
        formData.append('id', params.item.id);
        formData.append('page', this.state.currentPage);
        formData.append('page_size',this.state.pageSize);

        HttpUtils.post(codeurl,formData)
            .then(result=>{

                if(result['respCode']==1){

                    this.state.headInfo = result['data']['info'];
                    this.state.isFollow = this.state.headInfo.is_attention;
                    let testData = result['data']['rows'];
                    let newList = testData.map((data) => {
                        return {
                            id:data.id?data.id:0,
                            userid:data.userid?data.userid:0,
                            front_cover: data.front_cover,
                            describe: data.describe?data.describe:'暂无签名',
                            visit_num: data.visit_num?data.visit_num:0,
                            follow_num: data.follow_num?data.follow_num:0,
                            head_pic:data.head_pic?data.head_pic:default_pic,
                            name:data.name?data.name:'',
                            identifier:data.identifier?data.identifier:'',
                            stream_id:data.stream_id,
                            nickname:data.nickname,

                            location:data.location,
                            push_url:data.push_url,
                            status:data.status,
                            like_count:data.like_count,
                            play_url:data.play_url,
                            hls_play_url:data.hls_play_url,
                            fubi:data.fubi,
                            viewer_count:data.viewer_count,
                            title:data.title?data.title:'',
                            type:data.type?data.type:'',
                            timestamp:data.timestamp?data.timestamp:'',
                            groupid:data.groupid,
                            fileid:data.fileid,
                            isattention:this.state.headInfo.is_attention?1:0,

                            is_buy:data.is_buy

                        }
                    })
                    let dataList =  isReload ? newList : [...this.state.dataList, ...newList]
                    let state = dataList.length >= result['data']['total'] ? RefreshState.NoMoreData : RefreshState.Idle;
                    if(isReload){
                        state = RefreshState.Idle;
                    }
                    this.setState({
                        dataList: dataList,
                        refreshState: state,
                    })
                }else{
                    Alert.alert(result['respMsg'])
                    this.setState({
                        refreshState: state,
                    })
                }

            })
            .catch(error=>{
                this.setState({
                    result:JSON.stringify(error)
                })
            })



    }

    keyExtractor = (item, index) => {
        return index
    }

    render() {

        return (
            <View style={styles.container}>

                <RefreshListView
                    data={this.state.dataList}
                    keyExtractor={this.keyExtractor}
                    renderItem={this.renderCell}
                    refreshState={this.state.refreshState}
                    onHeaderRefresh={this.onHeaderRefresh}
                    //onFooterRefresh={this.onFooterRefresh}
                    numColumns ={2}
                    ListHeaderComponent={this.rendHeader}
                    ListFooterComponent={this.rendFooter}
                    horizontal={false}
                    ItemSeparatorComponent={this._separator}

                    // 可选
                    footerRefreshingText= '玩命加载中 >.<'
                    footerFailureText = '我擦嘞，居然失败了 =.=!'
                    footerNoMoreDataText= '-我是有底线的-'
                />
                <DialogSelected ref={(dialog)=>{
                    this.dialog = dialog;
                }} />
            </View>
        )
    }



    renderCell = (info) => {
        return <DetailCell info={info.item} onPress={this.itemClick.bind(this, info)}/>
    }


    itemClick(e){

        let info = e.item
        // if(parseInt(info.fubi)>0 && !info.is_buy){
        if(parseInt(info.fubi)>0 && !info.is_buy){
            this.dialog.show("该视频需要支付"+info.fubi+"福币", ['确定支付'], '#333333', this.callbackSelected.bind(this,info));

        }else{
            RNLogin.startLivePlay(e.item)
        }



    }

    // 回调
    callbackSelected(info,i){
        switch (i){
            case 0:
                this.payCourse(info)
                break;

        }
    }

    payCourse(info){


        let codeurl = host + '/fubi/purchase/course';
        let formData = new FormData();
        // 请求参数 ('key',value)
        formData.append("file_id",info.fileid);
        formData.append("userid",info.userid);

        HttpUtils.post(codeurl, formData)
            .then(result => {
                if (result['respCode'] == 1) {
                    // this.state.headInfo.is_attention =  !this.state.headInfo.is_attention;
                    // let follow_num = -1;
                    // if(this.state.headInfo.is_attention){
                    //     follow_num = 1
                    // }
                    // //this.state.hadInfo.follow_num = this.state.hadInfo.follow_num + follow_num
                    // this.setState({
                    //     isFollow:!this.state.isFollow
                    // })
                    Alert.alert("支付成功")
                    info.is_buy = true
                    RNLogin.startLivePlay(info)
                } else if (result['respCode'] == 3) {
                    Alert.alert('福币不足请先充值','',
                        [
                            {text:"取消"},
                            {text:"充值", onPress: ()=>{
                                    this.props.navigation.navigate("BuycoinsCent",{});

                                }}
                        ]
                    );
                }else{
                    Alert.alert(result['respMsg']);
                }

            })
            .catch(error => {
                Alert.alert("支付失败" + error);
            })


    }



    rendFooter = (e) =>{

        return this.state.dataList.length==0?<View>
            <Image style={styles.nodataImg} source={require('../res/images/image_nodata.png')}>

            </Image>
            <Text style={styles.nodataText}>主播还没有开课</Text>

        </View>:null;

    }


    rendHeader = (e) => {

        let info = this.state.headInfo;
        let text = this.state.isFollow?'已关注':'关注';
        let default_pic  = "http://p2bm1h3pe.bkt.clouddn.com/1517361721_php0UDGba.png";
        return <View  style={styles.hedView}>
            <ImageBackground style={styles.hedBgImg} source={require('../res/images/image_background.png')}>
                <ImageBackground
                    style={styles.iconeImg}
                source={{
                uri: info.head_pic?info.head_pic:default_pic}}/>
                <Text style={styles.nametext}>{info.name}</Text>
                <Text style={styles.numtext}>宗教号:{info.identifier}</Text>
                <Text style={styles.shuostext}>{info.describe?info.describe:'暂无签名'}</Text>
               <TouchableOpacity style={styles.courseBtnView}  onPress={()=>this.followOrNot()}>
                    <Text style={styles.courseBtnText}>{text}</Text>
                </TouchableOpacity>
            </ImageBackground>
            <View style={styles.lineView}></View>
            <View style={styles.boldlineView}></View>
        </View>
    }



    _separator = () => {
        return <View style={{height:5,backgroundColor:'#f2f2f2'}}/>;
    }

    followOrNot(){


        let codeurl = host + '/member/index/attention';
        let formData = new FormData();
        // 请求参数 ('key',value)
        const {params} = this.props.navigation.state;
        let info = params.item;
        formData.append('state', Number(!this.state.isFollow));
        formData.append('id',info.id)

        HttpUtils.post(codeurl, formData)
            .then(result => {
                if (result['respCode'] == 1) {
                    // this.state.headInfo.is_attention =  !this.state.headInfo.is_attention;
                    // let follow_num = -1;
                    // if(this.state.headInfo.is_attention){
                    //     follow_num = 1
                    // }
                    // //this.state.hadInfo.follow_num = this.state.hadInfo.follow_num + follow_num
                    // this.setState({
                    //     isFollow:!this.state.isFollow
                    // })
                    this.onHeaderRefresh()
                } else {
                    Alert.alert("操作失败", result['respMsg'])
                }

            })
            .catch(error => {
                Alert.alert("操作失败" + error);
            })


    }

}

const styles = StyleSheet.create({

    container: {
        flex:1,
        backgroundColor:sys.grayColor
    },

    hedView:{
        width:sys.dwidth,
        //height:sys.dwidth/3*2 + 195,
        height:sys.dwidth/3*2,
    },

    hedBgImg:{

        alignItems:'center',
       // justifyContent:'center',
        width:sys.dwidth,
        height:sys.dwidth/3*2,

    },
    nodataImg:{
        marginTop:10,
        width:sys.dwidth/2,
        height:sys.dwidth/2,
        marginLeft:sys.dwidth/4,
        resizeMode:'center'
    },
    nodataText:{
        textAlign:'center',
        color:'#c3c3c3',
        marginBottom:10
    },

    iconeImg:{
        marginTop:15,
        width:50,
        height:50,
        borderRadius:25,
        borderWidth: (Platform.OS==='ios' ? 1.0 : 1.5) / PixelRatio.get(),
        overflow:'hidden'
    },

    nametext:{
        marginTop:10,
        fontSize:15,
        color:'white',
        backgroundColor: 'rgba(52, 52, 52, 0.1)',
    },
    numtext:{
        marginTop:5,
        fontSize:10,
        color:'white',
        backgroundColor: 'rgba(52, 52, 52, 0.1)',
    },

    shuostext:{
        marginTop:20,
        fontSize:11,
        color:'white',
        backgroundColor: 'rgba(52, 52, 52, 0.1)',
    },

    courseBtnView:{
        marginTop:20,
        width:70,
        height:25,
        borderRadius:5,
        backgroundColor:sys.mainColor,
        alignItems:'center',
         justifyContent:'center',
    },

    courseBtnText:{
       // marginTop:9,
        color:'#ffffff',

        fontSize:13
    },

    bomhedView:{
        flexDirection:'row',
        marginTop:0,
        width:sys.dwidth,
        height:50,
        backgroundColor:sys.mainColor
    },

    bomchildView:{
        marginTop:0,
        marginLeft:0,
        width:sys.dwidth/2,
        height:50,
        alignItems:'center',
        justifyContent:'center',

    },

    bomchildText:{
        color:'#ffffff',

        fontSize:15
    },

    contentTitView:{
        //alignItems:'center',
        justifyContent:'center',
        marginTop:0,
        marginLeft:0,
        width:sys.dwidth,
        height:45
    },
    contentTitText:{

        marginLeft:10,

        color:'#7d2d17',

        fontSize:16
    },
    contentTitContent:{
        marginLeft:10,
        marginTop:0,
        marginBottom:10,
        color:'#333333'
    },

    content:{
        backgroundColor:'white',
        width:sys.dwidth
    },



    bthirdViewOne:{
        flexDirection:'row',
        backgroundColor:'white',
        height:44,
    },
    bthirdViewtwo:{
        flexDirection:'row',
        backgroundColor:'white',
        height:44
    },
    thirdView: {
        backgroundColor: '#f2f2f2',
        width: sys.dwidth,
        height:Platform.OS=='ios'?sys.dheight - 114:sys.dheight-130,
    },

    tbuttontext:{
        marginLeft:10,
        marginTop:11,
        marginRight:10,
        width:100,
        fontSize:14,
        color:"#000000"
    },
    tbuttonnum:{
        marginTop:11,
        fontSize:14,
        color:"#000000",
        width:100,
        textAlign:'right',
        marginLeft:sys.dwidth-230,
    },
    btimage:{
        marginTop:12.5,
        marginLeft:sys.dwidth-100-24-10,
        width:12,
        height:19
    },
    lineView:{
        backgroundColor:"#cccccc",
        height:1
    },
    boldlineView:{
        backgroundColor:"#f2f2f2",
        height:10
    }

})

//
//  Created by Liu Jinyong on 17/4/5.
//  Copyright © 2016年 Liu Jinyong. All rights reserved.
//
//  @flow
//  Github:
//  https://github.com/huanxsd/react-native-refresh-list-view

import React, {Component} from 'react'
import {View, StyleSheet, Text, Platform,Alert,TouchableOpacity,Image,PixelRatio,DeviceEventEmitter} from 'react-native'
import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'
import CourseCell from './CourseCell'
import {sys,NoDataView} from "../common/Data"
const host = sys.host;
import HttpUtils from "../common/HttpUtil"
// import FcusImage from './HeadView2';
import RNLogin from "../common/RNLoginModule"

import TradingRecordAll from './TradingRecordAll'
import TradingRecordCz from './TradingRecordCz'
import TradingRecordTx from './TradingRecordTx'
import TradingRecordXM from './TradingRecordXM'

import ScrollableTabView, {DefaultTabBar, } from 'react-native-scrollable-tab-view';


export default class TradingRecord extends Component {

    
    static  navigationOptions = ({navigation}) => {
       

        const { params } = navigation.state;
      
        if (Platform.OS=='ios') {
            var leftView = <TouchableOpacity
            onPress={() => {
                 navigation.goBack()
            }}

            style={{width:50}}

            >

           <Image
            source={require('../res/images/iosfanhui.png')}
            style={{marginLeft:10,marginTop:0}}
            />

            </TouchableOpacity>

            return {
                title:params.isyongjin?'我的佣金':'交易记录',
                headerTitleStyle:{
                    alignSelf:'center',
                    textAlign: 'center',
                    flex:1
                },
                headerRight:React.createElement(View, null, null),
                headerLeft:leftView
            }

        }  

        return {
        title:params.isyongjin?'我的佣金':'交易记录',
        headerTitleStyle:{
            alignSelf:'center',
            textAlign: 'center',
            flex:1
        },
        headerRight:React.createElement(View, null, null),
    }
    };


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


    componentDidMount(){


        // this.onHeaderRefresh()
        // //注册通知
        // DeviceEventEmitter.addListener('pressImage',(dic)=>{
        //     //接收到详情页发送的通知，刷新首页的数据，改变按钮颜色和文字，刷新UI
        //     const {navigate} = this.props.navigation;
        //     navigate('WebViewScene', {uri: dic.link});
        // });

        // //监听事件名为EventName的事件
        // DeviceEventEmitter.addListener('eventName',(dic)=>{

        //     const {navigate} = this.props.navigation;
        //     navigate('CourseDetail',{item:dic})

        // });

        // //监听事件名为EventName的事件
        // DeviceEventEmitter.addListener('followChange',(dic)=>{

        //     this.onHeaderRefresh()

        // });



    }


    render() {

        // return <View></View>

        let contentView = null;

        if (this.props.navigation.state.params.isyongjin) {
            contentView = <ScrollableTabView
            style={{marginTop: 0, }}
            initialPage={0}
            renderTabBar={() => <DefaultTabBar />}
            tabBarUnderlineStyle={{backgroundColor:sys.mainColor,height:4,width:(sys.dwidth - 90)/3,marginLeft:15}}
            tabBarBackgroundColor='#FFFFFF'
            tabBarActiveTextColor={sys.mainColor}
            tabBarInactiveTextColor='#333333'
            tabBarTextStyle={{fontSize: 15}}

            >
            <TradingRecordXM tabLabel='全部' style={{backgroundColor:sys.mainColor}}
            isyongjin={true} typeStr='dailiyongjinall            '
            ></TradingRecordXM>
            <TradingRecordXM tabLabel='我的返点' style={{backgroundColor:sys.mainColor}}
            isyongjin={true} typeStr='yongjinshenhe'
            ></TradingRecordXM>
            <TradingRecordXM tabLabel='我的返水' style={{backgroundColor:sys.mainColor}}
            isyongjin={true} typeStr='dailifanshui'
            ></TradingRecordXM>
            
            {/* //<View tabLabel='全部3' style={{backgroundColor:sys.silveryColor}}></View> */}
            

            {/* import TradingRecordAll from './TradingRecordAll'
import TradingRecordCz from './TradingRecordCz'
import TradingRecordTx from './TradingRecordTx' */}

        </ScrollableTabView>;
        } else {

            contentView = <ScrollableTabView
            style={{marginTop: 0, }}
            initialPage={0}
            renderTabBar={() => <DefaultTabBar />}
            tabBarUnderlineStyle={{backgroundColor:sys.mainColor,height:4,width:(sys.dwidth - 120)/4,marginLeft:15}}
            tabBarBackgroundColor='#FFFFFF'
            tabBarActiveTextColor={sys.mainColor}
            tabBarInactiveTextColor='#333333'
            tabBarTextStyle={{fontSize: 15}}

            >
            <TradingRecordAll tabLabel='所有类型' style={{backgroundColor:'red'}}></TradingRecordAll>
            <TradingRecordCz tabLabel='充值记录' style={{backgroundColor:sys.backgroundColor}}></TradingRecordCz>
            <TradingRecordTx tabLabel='提现记录' style={{backgroundColor:sys.mainColor}}></TradingRecordTx>
            <TradingRecordXM tabLabel='洗码记录' style={{backgroundColor:sys.mainColor}}></TradingRecordXM>
            
            {/* //<View tabLabel='全部3' style={{backgroundColor:sys.silveryColor}}></View> */}
            

            {/* import TradingRecordAll from './TradingRecordAll'
import TradingRecordCz from './TradingRecordCz'
import TradingRecordTx from './TradingRecordTx' */}

        </ScrollableTabView>;
        }


        return contentView

    }



}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        marginTop: Platform.OS == 'ios' ? 0 : 0,
        backgroundColor:sys.grayColor,

    },
    headView:{
        width:sys.dwidth,
        height:sys.dwidth/2
    }
})


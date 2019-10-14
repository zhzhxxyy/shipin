import React, {Component} from 'react'
import {
    Text,
    View,
    Alert
} from 'react-native';

import ScrollableTabView, {DefaultTabBar,ScrollableTabBar } from 'react-native-scrollable-tab-view';
import MyTaBFollowHost from './MyTaBFollowHost'

import {sys} from "../common/Data"


const host = sys.host;

var mythis =null;

export default class MyTaBFollow extends Component{

    static  navigationOptions = ({navigation}) => ({
        headerTitle:"投注记录",

        headerTitleStyle:{
            alignSelf:'center',
            flex: 1,
            textAlign: 'center',
            fontSize:18
        },

        // async tabBarOnPress({ navigation, defaultHandler }) {
         
        //     if (mythis!=null) {


        //         if (mythis.refs['quanbu'].state) {
        //             mythis.refs['quanbu'].state.currentPage=1,
        //             mythis.refs['quanbu'].getDataList(true)
        //         }

                
        //     }


        //     defaultHandler();
        //   },


        headerRight: <View />
    });

    render(){


        mythis = this;

        return <ScrollableTabView
            style={{marginTop: 0, }}
            initialPage={0}
            renderTabBar={() => <DefaultTabBar />}
            tabBarUnderlineStyle={{backgroundColor:sys.mainColor,height:4,width:(sys.dwidth)/4,marginLeft:0}}
            tabBarBackgroundColor={sys.whitecolor}
            tabBarActiveTextColor={sys.mainColor}
            tabBarInactiveTextColor={sys.titleColor}
            tabBarTextStyle={{fontSize: 15}}

            onChangeTab={obj => {
                //业务逻辑 obj.i 标识第几个tab，从0开始
                if (obj.i == 0) {
                    mythis.refs['quanbu'].state.currentPage=1,
                    mythis.refs['quanbu'].getDataList(true)
                } else if (obj.i == 1) {
                    mythis.refs['yizhongjian'].state.currentPage=1,
                    mythis.refs['yizhongjian'].getDataList(true)
                } else if (obj.i == 2) {
                    mythis.refs['weizhongjian'].state.currentPage=1,
                    mythis.refs['weizhongjian'].getDataList(true)
                } else if (obj.i == 3) {
                    mythis.refs['weikaijian'].state.currentPage=1,
                    mythis.refs['weikaijian'].getDataList(true)
                } else if (obj.i == 4) {
                    // mythis.refs['weikaijian'].state.currentPage=1,
                    // mythis.refs['weikaijian'].getDataList(true)
                } 

              }}

            >
            <MyTaBFollowHost ref='quanbu' params={1} navigation={this.props.navigation} tabLabel='全部'>My</MyTaBFollowHost>
            <MyTaBFollowHost ref='yizhongjian' params={2} navigation={this.props.navigation} tabLabel='已中奖'>favorite</MyTaBFollowHost>
            <MyTaBFollowHost ref='weizhongjian' params={3} navigation={this.props.navigation} tabLabel='未中奖'>favorite</MyTaBFollowHost>
            <MyTaBFollowHost ref='weikaijian' params={4} navigation={this.props.navigation} tabLabel='未开奖'>My</MyTaBFollowHost>
            {/* <MyTaBFollowHost ref='qipai' params={5} navigation={this.props.navigation} tabLabel='棋牌'>My</MyTaBFollowHost> */}

        </ScrollableTabView>;
    }

}
import React, {Component} from 'react'
import {
    Text,
    View,
    Image,
    TouchableOpacity,
    Platform
} from 'react-native';

import ScrollableTabView, {DefaultTabBar,ScrollableTabBar } from 'react-native-scrollable-tab-view';
import MyFollowHost from './MyFollowHost'

import {sys} from "../common/Data"


const host = sys.host;

var mythis =null;

export default class MyFollow extends Component{



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
                headerTitle:"投注记录",
                headerTitleStyle:{
                    alignSelf:'center',
                    flex: 1,
                    textAlign: 'center',
                    fontSize:18
                },

                headerRight: <View />,
                headerLeft:leftView
            }

        }

        return {
            headerTitle:"投注记录",

            headerTitleStyle:{
                alignSelf:'center',
                flex: 1,
                textAlign: 'center',
                fontSize:18
            },

            headerRight: <View />

        }
    };


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

            >
            <MyFollowHost  params={1} navigation={this.props.navigation} tabLabel='全部'>My</MyFollowHost>
            <MyFollowHost  params={2} navigation={this.props.navigation} tabLabel='已中奖'>favorite</MyFollowHost>
            <MyFollowHost  params={3} navigation={this.props.navigation} tabLabel='未中奖'>favorite</MyFollowHost>
            <MyFollowHost  params={4} navigation={this.props.navigation} tabLabel='未开奖'>My</MyFollowHost>

        </ScrollableTabView>;
    }

}
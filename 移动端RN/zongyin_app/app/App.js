/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */
import React,{Component} from 'react';
import {
    Image, View,
    StyleSheet,
    Text, 
    DeviceEventEmitter,
    Platform,
    Alert,
    TouchableOpacity
} from 'react-native';
import storage from './common/Storage'
import {} from './utils/ScreenUtils'
import {createBottomTabNavigator, createStackNavigator,createAppContainer} from 'react-navigation';

import HttpUtils from "./common/HttpUtil"


// import Chat from './hot/ChatView'

// import ChatMore from './hot/ChatMore'


import WelcomePage from "./WelcomePage";


import WebViewScene from './hot/WebView'


import StackViewStyleInterpolator from "react-navigation-stack/src/views/StackView/StackViewStyleInterpolator"

import Recharge from './hot/RechargeMoney'
import Search from './hot/Search'
import SearchInfo from './hot/SearchInfo'

import {sys,isIphoneX} from './common/Data'

import { Header } from 'react-navigation';

// import MoviePage from './pages/MoviePage'


import Home from './pages/Home';
import MovieDetail from './pages/MovieDetail'
import My from './pages/Mine'
import Spread from './pages/Spread'
import SmallVideo from './pages/Live'
import HelpPage from './pages/HelpPage'


var unRead=false
const styles = StyleSheet.create({
    rightView: {
        marginHorizontal: 20
    },
    headerView: {
        backgroundColor: sys.mainColor
    },
    headerTitleStyle: {
        color: '#ffffff',
        fontSize: 18,
        // alignSelf: 'center'
    },
    leftText: {
        color: 'green'
    }
});

class TabBarBradgeItem extends React.Component {


    // mixins: [TimerMixin]

    constructor(props) {
        super(props);
        this.state = {
            unRead:false
        };
    }

    componentDidMount() {
         this.getChatUnReadData() 
       
          //监听状态改变事件
        // AppState.addEventListener('change', this.handleAppStateChange);

        DeviceEventEmitter.addListener('Read',(dic)=>{
                this.setState({
                    unRead:false
                });
        });

       
    }


    componentWillUnmount() {
        // 如果存在this.timer，则使用clearTimeout清空。
        // 如果你使用多个timer，那么用多个变量，或者用个数组来保存引用，然后逐个clear
        this.timer && clearTimeout(this.timer);
        // AppState.removeEventListener('change', this.handleAppStateChange);
        
      }

    getChatUnReadData(){


        this.state.unRead = unRead
        // if(this.state.unRead ){
        //     return;
        // }

        let codeurl = sys.host + "/AppChat.unReadRoommessage.do."
        let formData = new FormData();

        HttpUtils.post(codeurl,formData)
            .then(result=>{

                console.log('unread----')
                console.log(result)

                if(result['respCode']==1){

                    unRead = result['data']['kefu_unread']

                    if(unRead){
                        this.setState({
                            unRead:result['data']['kefu_unread']
                        },()=>{
                            DeviceEventEmitter.emit('unRead', {});//发给我的
                        })     
                        // Alert.alert("有新的客服消息");
                    }

                }else{

                }
        })
        .catch(error=>{
        
               
        })        
    }


    render() {

        // var count = this.props.count

        myTabBar = this;

        var bradgeView = null;

        if(this.state.unRead ){
            bradgeView = (
                <View style={{backgroundColor: 'red', position: 'absolute', right: 2, top: 3, height: 8, width: 8, borderRadius: 4, overflow: 'hidden'}}>
                    {/* <Text style={{fontSize: 12, textAlign: 'center',color:'white'}}>{count}</Text> */}
                </View>
            )
        }

        return(

            <View style={{position: 'absolute', top: 3}}>
                <Image source={ this.props.focused ? this.props.selectedImage : this.props.normalImage }
                   style={ { tintColor:this.props.tintColor,width:26,height:26, resizeMode: 'contain' } }
                />
                {
                    bradgeView
                }
           
          </View>
           
        )
    }

}

class TabBarItem extends React.Component {

    render() {   
        return(         
                <Image source={ this.props.focused ? this.props.selectedImage : this.props.normalImage }
                   style={ { tintColor:this.props.tintColor,width:26,height:26, resizeMode: 'contain' } }
                />                 
        )
    }

}

const Tab = createBottomTabNavigator({
    首页: {
        screen: Home,
        navigationOptions: {
            
            tabBarLabel: '首页',    //若不设置,则以key为标题
            tabBarVisible: true,  //是否隐藏标签栏。默认不隐藏(true),该选项卡激活时生效
            tabBarIcon: ({focused,tintColor}) => (
                <TabBarItem
                    // tintColor={tintColor}
                    focused={focused}
                    selectedImage={require('../source/image/main_choice_click.png')}
                    normalImage={require('../source/image/main_choice.png')}
                />
            ),
            // title:"充值",
            headerTitle : "主页",
            headerTitleStyle:{
                alignSelf:'center',
                color:sys.titleColor
            },

        },

    },
 
     注单: {
        screen: SmallVideo,
        navigationOptions: {
            tabBarLabel: '小视频',
            tabBarVisible: true,  //是否隐藏标签栏。默认不隐藏(true),该选项卡激活时生效
            tabBarIcon: ({focused,tintColor}) => (<TabBarItem
                // tintColor={tintColor}
                focused={focused}
                selectedImage={require('../source/image/main_movie_click.png')}
                normalImage={require('../source/image/main_movie.png')}
            />)
        },
    },
    // 聊天室: {
    //     screen: Hot1,
    //     navigationOptions: {
    //         tabBarLabel: '聊天',
    //         tabBarVisible: true,  //是否隐藏标签栏。默认不隐藏(true),该选项卡激活时生效
    //         tabBarIcon: ({focused,tintColor}) => (<TabBarItem
    //             // tintColor={tintColor}
    //             focused={focused}
    //             normalImage={require('./res/images/unselected_remen.png')}
    //             selectedImage={require('./res/images/selected_remen.png')}
    //         />)
    //     },
    //
    // },
    Spread: {
        screen: Spread,
        navigationOptions: {
            tabBarLabel: '宣传',
            tabBarVisible: true,  //是否隐藏标签栏。默认不隐藏(true),该选项卡激活时生效
            tabBarIcon: ({focused,tintColor}) => (<TabBarItem
                // tintColor={tintColor}
                focused={focused}
                selectedImage={require('../source/image/main_tv_click.png')}
                normalImage={require('../source/image/main_tv.png')}
            />)
        },
    },
    我的: {
        screen: My,
        navigationOptions: {
            tabBarLabel: '我的',
            tabBarVisible: true,  //是否隐藏标签栏。默认不隐藏(true),该选项卡激活时生效
            tabBarIcon: ({focused,tintColor}) => (<TabBarItem
                // tintColor={tintColor}
                count = {2}
                focused={focused}
                selectedImage={require('../source/image/icon_cartoon_nor_click.png')}
                normalImage={require('../source/image/icon_cartoon_nor.png')}
            />)
        },
        tabBarOnPress:(obj)=>{
            //  obj.jumpToIndex(obj.scene.index)
             Alert.alert("xiao huo zi");
            if(obj.scene.index == 3){
                        Alert.alert("come on");
                        DeviceEventEmitter.emit('ChangeUI', {});
                    
            }
        }
    },



}, {

    tabBarPosition: 'bottom',    //设置tabbar的位置，iOS默认在底部，安卓默认在顶部。（属性值：'top'，'bottom'）
    swipeEnabled: true,          //是否允许在标签之间滑动
    animationEnabled: false,     //是否在更改标签时显示动画。
    lazy: true,                  //是否根据需要懒惰呈现标签，而不是提前制作，意思是在app打开的时候将底部标签栏全部加载，默认false,推荐改成true哦
    initialRouteName: '首页',    //设置默认的页面组件
    backBehavior: 'none',        //按 back 键是否跳转到第一个Tab(首页)， none 为不跳转
    // tabBarComponent: TabBarBottom, //设置安卓和iOS一样的样式
   


    tabBarOptions: {
        activeTintColor: sys.mainColor,//label和icon的前景色 活跃状态下（选中）。
        activeBackgroundColor: 'white', //label和icon的背景色 活跃状态下（选中） 。
        //inactiveTintColor:'black',
        inactiveBackgroundColor: 'white', //label和icon的背景色 活跃状态下（选中） 。
        showLabel: true,         //是否显示label，默认开启
        labelStyle: {backgroundColor:'white',fontSize: 12}, //label的样式
        style: {height: 49},  //tabbar的样式
        iconStyle: {height: 30,width:30},   //安卓,
        indicatorStyle:{height:0}

    }

});

Tab.navigationOptions = ({navigation}) => {
    let {routeName} = navigation.state.routes[navigation.state.index];

   
    if(Platform.OS == 'ios'){
        return {         
            headerTitle:routeName,
            // backgroundColor:sys.mainColor
            headerStyle:{
                // alignSelf:'center',
                // flex: 1,
                // textAlign: 'center',
                backgroundColor:'red',
                // color:'red'
            },
            headerTintColor:'red',
        };
       
    }else{

        let isMine = (routeName == '我的')


        return {
            header: <View style={{backgroundColor:sys.whiteColor,width:sys.dwidth,height:
                Header.HEIGHT,borderColor:sys.grayColor,borderBottomWidth:1}}>
                <Text style={{marginTop:(Header.HEIGHT-25)/2,textAlign:'center',
                fontWeight:'bold',
                color:sys.titleColor,
                textAlignVertical:'center',
                fontSize:20,

                width:sys.dwidth,height:25}}>
    
                    {routeName}</Text></View>,
            headerTitle:routeName,
            headerTitleStyle:{
                alignSelf:'center',
                flex: 1,
                width:sys.dwidth,
                textAlign: 'center',              
            },
            headerRight: <View/>
        };
    }
};

const StacksOverTab = createStackNavigator({
        WelcomePage: {
            screen: WelcomePage,
            navigationOptions: {
                header: null
            }
        },



        Tab: {
            screen: Tab,
            navigationOptions: {
                 header: null
                // gesturesEnabled: true,
                // headerTitle: '设置',  //设置导航栏标题，推荐用这个方法。
                // headerRight: <View><Text></Text></View>,
                // headerStyle: {},
                // headerTitleStyle: {
                //     color: '#000000',
                //     fontSize: 18,
                //     alignSelf: 'center'
                // },
                // headerBackTitle: null,
                // headerBackTitleStyle: styles.leftText,
                // headerLeft: <View style={styles.rightView}></View>,
                // headerTintColor : '#ffffff', //返回按钮的颜色
            }
        },

      
        MovieDetail: {
            screen: MovieDetail,
            navigationOptions: {
                header: null
            }
        },
       




        WebViewScene:{
            screen: WebViewScene,
            navigationOptions :(props) => {
     
            }
        },

        HelpPage:{
            screen:HelpPage
        },

        
        Recharge: {
            screen: Recharge,
            navigationOptions :(props) => {
                // header: null
                // const {params} = props.navigation.state;
                // return{
                // title:params.title?params.title:'标题'
                // }
            }
        },
        
        Search: {
            screen: Search,
            navigationOptions: {
                header: null
            }
        },
        SearchInfo: {
            screen: SearchInfo,
            navigationOptions :(props) => {
                // header: null
                // const {params} = props.navigation.state;
                // return{
                // title:params.title?params.title:'标题'
                // }
            }
        }
      
    },
    {
        mode: 'card',
        headerMode: 'screen',
        navigationOptions: {
            gesturesEnabled: true,
            // headerTitle: '设置',  //设置导航栏标题，推荐用这个方法。
            headerRight: <View style={styles.rightView}><Text></Text></View>,
            headerStyle: styles.headerView,
            headerTitleStyle: styles.headerTitleStyle,
            headerBackTitle: null,
            headerBackTitleStyle: styles.leftText,
            //headerLeft: <View style={styles.rightView}></View>,
            headerTintColor : '#ffffff', //返回按钮的颜色
        },

        transitionConfig:()=>({

            screenInterpolator: StackViewStyleInterpolator.forHorizontal,
            
        }),

    }
);

const App = createAppContainer(StacksOverTab);
export default App;
// export default class App extends Component {

//     render() {
//        return(
//           <View style={{flex: 1}}>
//                {/*...你的代码*/}

//                {/*toast*/}
//                <ToastComponent/>
//           </View>);
//      }
// }


// export default class App extends React.Component {
//     render(){
//         return <StacksOverTab />
//     }
// };

//
//  Created by Liu Jinyong on 17/4/5.
//  Copyright © 2016年 Liu Jinyong. All rights reserved.
//
//  @flow
//  Github:
//  https://github.com/huanxsd/react-native-refresh-list-view

import React, {Component} from 'react'
import {View, StyleSheet,Text, DeviceEventEmitter,Platform,Alert,
    TouchableOpacity,Image,PixelRatio,AppState
    ,Linking,NativeModules} from 'react-native'
import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'
// import Cell from './Cell'

import HomeCell from './HomeCell'

import {NoDataView, sys,TitleHeadView, isIphoneX} from "../common/Data"
const host = sys.host;
import HttpUtils from "../common/HttpUtil"
import codePush from "react-native-code-push";

import HotUpdate, { ImmediateCheckCodePush } from 'react-native-code-push-dialog';

const { StatusBarManager } = NativeModules;

// import FcusImage from "../hot/HeadView"

import LunBoImage from "./LunBoImage"
// import DeviceInfo from 'react-native-device-info';
import AlertTool from '../common/Alert'

import Toast,{DURATION} from 'react-native-easy-toast';//导入弹出框组件

import Pop from 'rn-global-modal'

import JPushModule from 'jpush-react-native';

// let CryptoJS = require('crypto-js');r

import { MarqueeHorizontal,MarqueeVertical } from 'react-native-marquee-ab';

var gonggaoIndext=0;

var myThis;

var clickLastTime=0;

var gonggaoTime=0;

class Home extends Component {

    
    static  navigationOptions = ({navigation}) => {
        const { params } = navigation.state;

        return {
            headerTitle: '购彩',
            headerTitleStyle:{
                alignSelf:'center',
                flex: 1,
                textAlign: 'center',   
            },
            
            headerRight: <View/>
        }

    };
 


    constructor(props) {
        super(props)

        this.state = {
            islogin:0,
            dataList: [],
            refreshState: false,
            currentPage:1,
            pageSize:10,
            noNetwork:false,
            noData:false,

          //  headViewIndext:0,

            imageDataSource: [],
            imageDataList:[],

            gonggaoDataSource:[],

            caipiaoTypeList:[],

            isCode:true,

            isloading:false,

            zhongjiangInfo:[],

            priceInfo:[],

            typeInfo:[],

            gameUrl:'',

            isZhongjiang:false


        }
    }


    timingTaskStart(){//定时任务
       
        //倒计时
        this.countDownTask=setInterval(
            () => {


                if (gonggaoTime<100) {
                    gonggaoTime++

                    if (gonggaoTime%2) {                                                
                        if (gonggaoIndext<this.state.gonggaoDataSource.length-1) {
                            gonggaoIndext++;
                        } else {
                            gonggaoIndext=0;
                        }
                    }

                } else {
                    gonggaoTime=1;
                }
                     
            },
            1000
        );
    }

    timingTaskEnd(){
        this.countDownTask && clearTimeout(this.countDownTask);
    }

    componentDidMount() {
    
        // Alert.alert('有新版本请前往下载','',
        //         [
        //             {text:"取消", onPress: ()=>{

        //                 }},
        //             {text:"前往", onPress: ()=>{
                        
        //                 Linking.openURL('https://app.xieyutech.com/A/IKH6QBQT').catch(err => console.error('An error occurred', err))
 

        //             }}
        //         ]
        //     );


         //注册通知
         DeviceEventEmitter.addListener('Login',(dic)=>{
            //接收到详情页发送的通知，刷新首页的数据，改变按钮颜色和文字，刷新UI
            Pop.hide()
            //const {navigate} = this.props.navigation;
            this.props.navigation.navigate("Login",dic);

            // this.getGameUrl()
        });


        DeviceEventEmitter.addListener('LoginQuxiao',(dic)=>{

            Pop.hide()
            
        });

        this.onHeaderRefresh()

        // this.appUpdate()
        ImmediateCheckCodePush();

        this.timingTaskStart();

        this.getImageDataList(false,0)

        this.ggShowDo(false,0)

      //  this.getGameUrl()
        this.getBingoData()

        //登录成功通知
        // DeviceEventEmitter.addListener('ChangeUI',(dic)=>{

        //     //接收到详情页发送的通知，刷新首页的数据，改变按钮颜色和文字，刷新UI
        //     if(dic['islogin'] != 0){
        //         // this.getPersonInfo()
        //         this.setState({
        //             islogin:1
        //         });
        //         //存储数据
        //     }else{

        //         this.setState({
        //             islogin:0
        //         });
        //         //存储数据
        //         storage.save('islogin', "")
        //     }

        // });


        // //注册通知
        // DeviceEventEmitter.addListener('Login',(dic)=>{
        //     //接收到详情页发送的通知，刷新首页的数据，改变按钮颜色和文字，刷新UI
        //     const {navigate} = this.props.navigation;
        //     this.props.navigation.navigate("Login",dic);
        // });
        storage.load(host + '/AppPublic.lotteryHallNew.do', (dic) => {
            if (dic != "" && dic != null) {
                let dataList =  dic
                this.state.currentPage = 2;
                let state = RefreshState.Idle;
                let nodata = false;
                this.setState({
                    dataList: dataList,
                    refreshState: state,
                    noData:nodata,
                    noNetwork:false,
                })
            }
         })

         storage.load(host + '/AppChat.getChatAd.do', (dic) => {
            if (dic != "" && dic != null) {
                
                this.lunBoImage.setState({
                    images:dic
                })
            }
         })

         storage.load(host + '/AppMember.ggshow.do', (dic) => {
            if (dic != "" && dic != null) {
                this.setState({
                        gonggaoDataSource:dic
                    })
            }
         })
      
         
         JPushModule.setAlias(sys.host, () => {
            console.log("Set alias succeed");
        }, () => {
            console.log("Set alias failed ！！");
        });

        if (global.user.userData != '' && global.user.userData != null) {
            JPushModule.setTags([global.user.userData['id'],"proxy"+global.user.userData['proxy'],'login'], () => {
                console.log("Set tag succeed");
            }, () => {
                console.log("Set tag failed");
            });
        } else {

            JPushModule.setTags(['proxy0','nologin'], () => {
                console.log("Set tag succeed");
            }, () => {
                console.log("Set tag failed");
            });
        }

    // 新版本必需写回调函数
    // JPushModule.notifyJSDidLoad();
    if(Platform.OS === 'android'){ 
        JPushModule.notifyJSDidLoad((resultCode) => {
            if (resultCode === 0) {}
      });
    }
    

  // 接收自定义消息
  JPushModule.addReceiveCustomMsgListener((message) => {
    // this.setState({pushMsg: message});
  });
//   // 接收推送通知
  JPushModule.addReceiveNotificationListener((message) => {
    // console.log("receive notification: " + message);


    // console.log(message)

    var tsTit =''

    var typeStr = ''

    var personalStr = ''

    let extras = ''

    try {
        extras = message['extras'];
    } catch (error) {


        // console.log('error')

        // console.log(error)

         //Alert.alert('错误');

        return;
        
    }

    // this.refs.alert.alertWithOptions({
    //     options:['好的'],
    //     title:'公告',
    //     detailText:tsTit
    // },
    //      // index是当前点击选项的索引
    //     (index)=>{
           
    //       if(index == 0) {
               
    //       }
    //     }
    // );


    var JSMess = {}
    if (Platform.OS === 'ios') {
        JSMess = extras
    } else {
        try {
            JSMess = JSON.parse(extras)
        } catch (error) {
            return;            
        }
    }



    if (!JSMess.hasOwnProperty('data')) {

        if (message.hasOwnProperty('alertContent')) {
            tsTit = message['alertContent']
        } else {
            return;
        }

    } else {
        // console.log('JSMessextras')

        // console.log(JSMess)
    
    
        tsTit = JSMess['data']['message']
    
    
        typeStr = JSMess['type']
    
        personalStr = JSMess['personal']
    }

        
     

    if (personalStr != 'all') {
        if (global.user.userData != '' && global.user.userData != null) {

            if (personalStr != global.user.userData['id']) {
                return;
            }


        } else {
            return;
        }
    } 




    // Pop.show(
    //     <View style={{width:sys.dwidth - 40,backgroundColor:sys.whiteColor,borderRadius:5}}>
        
    //     <Text style={{color:sys.titleColor,width:sys.dwidth - 60,marginLeft:10,marginTop:20}}
    //     numberOfLines={3}
    //     >{tsTit}</Text>
        
    //     <View  style={{width:sys.dwidth - 40, height:40,flexDirection:'row',marginTop:10}}>
    //             <TouchableOpacity  style={{width:(sys.dwidth - 40)/2-20, height:40,backgroundColor:sys.whiteColor,marginLeft:10}}
    //                                onPress={() => {
    //                                 Pop.hide()
                                       
    //                                }}>
    //                 <Text  style={{width:(sys.dwidth - 40)/2-20,height:40,textAlign:'center',color:sys.titleColor,fontSize:16,textAlignVertical:'center',
    //                 marginLeft:10,...Platform.select({
    //                     ios:{
    //                         lineHeight:40,
    //                     },
    //                     android:{
    //                     }
    //                 }),}}>取消</Text>
    //             </TouchableOpacity>
    //             <TouchableOpacity  style={{width:(sys.dwidth - 40)/2-20, height:40,backgroundColor:sys.whiteColor,marginLeft:10}}
    //                                onPress={() => {
                                    
    //                                 Pop.hide()
    //                                  myThis.tianZhuanTuisong(typeStr,JSMess)

    //                                }}>
    //                 <Text  style={{width:(sys.dwidth - 40)/2-20,height:40,textAlign:'center',color:sys.titleColor,fontSize:16,textAlignVertical:'center',
    //                 marginLeft:10,...Platform.select({
    //                     ios:{
    //                         lineHeight:40,
    //                     },
    //                     android:{
    //                     }
    //                 }),}}>确定</Text>
    //             </TouchableOpacity>
    //         </View>
        
    //     </View>
    // )



    Pop.show(
        <View style={{width:sys.dwidth - 50,backgroundColor:sys.whiteColor,borderRadius:5}}>
        
        <Text style={{color:sys.titleColor,width:sys.dwidth - 100,marginLeft:25,marginTop:20,fontSize:16}}
        numberOfLines={12}
        >{tsTit}</Text>
        
        <View  style={{width:(sys.dwidth - 50)/2, height:40,flexDirection:'row',marginTop:10}}>
                <TouchableOpacity  style={{width:(sys.dwidth - 50)/4-20, height:40,backgroundColor:sys.whiteColor,marginLeft:(sys.dwidth - 50)/2}}
                                   onPress={() => {
                                    Pop.hide()
                                       
                                   }}>
                    <Text  style={{width:(sys.dwidth - 50)/4-20,height:40,textAlign:'center',color:sys.titleColor,fontSize:16,textAlignVertical:'center',
                    marginLeft:10,color:'#067a6a',...Platform.select({
                        ios:{
                            lineHeight:40,
                        },
                        android:{
                        }
                    }),}}>取消</Text>
                </TouchableOpacity>
                <TouchableOpacity  style={{width:(sys.dwidth - 50)/4-20, height:40,backgroundColor:sys.whiteColor,marginLeft:10}}
                                   onPress={() => {
                                    
                                    Pop.hide()
                                     myThis.tianZhuanTuisong(typeStr,JSMess)

                                   }}>
                    <Text  style={{width:(sys.dwidth - 50)/4-20,height:40,textAlign:'center',color:sys.titleColor,fontSize:16,textAlignVertical:'center',
                    marginLeft:10,color:'#067a6a',...Platform.select({
                        ios:{
                            lineHeight:40,
                        },
                        android:{
                        }
                    }),}}>确定</Text>
                </TouchableOpacity>
            </View>
        
        </View>
    )


    // Alert.alert(tsTit,'',
    //     [
    //         {text:"取消", onPress: ()=>{
    //             Pop.hide()
                   
    //             }},
    //         {text:"确定", onPress: ()=>{

    //             Pop.hide()

    //             myThis.tianZhuanTuisong(typeStr,JSMess)
                
    //         }}
    //     ]
    // );
    console.log(message)

    // Alert.alert("推送来了");
  });
          // 打开通知
        JPushModule.addReceiveOpenNotificationListener((map) => {
                console.log("Opening notification!");
                console.log("map.extra: " + map.extras);
            // 可执行跳转操作，也可跳转原生页面
            // this.props.navigation.navigate("SecondActivity");

            var typeStr = ''

            var personalStr = ''

            var JSMess = {}
            if (Platform.OS === 'ios') {
                    JSMess = map.extras
                } else {
                    try {
                        JSMess = JSON.parse(map.extras)
                    } catch (error) {
                        return;            
                    }
            }


            if (!JSMess.hasOwnProperty('data')) {
                    return;
                
            } else {
            
                typeStr = JSMess['type']
            
                personalStr = JSMess['personal']
            }        
            if (personalStr != 'all') {
                if (global.user.userData != '' && global.user.userData != null) {     
                    if (personalStr != global.user.userData['id']) {
                        return;
                    }   
        
                } else {
                    return;
                }
            } 

            myThis.tianZhuanTuisong(typeStr,JSMess)
         
        });
    }


    tianZhuanTuisong(typeStr,JSMess)
    {


        // console.log('typeStr,JSMess')

        // console.log(typeStr,JSMess)

        Pop.hide()

        if (typeStr == 'web') { //打开网页

            const {navigate} = myThis.props.navigation;
            navigate('WebViewScene', {uri: {content:JSMess['data']['url']}});

        } else if (typeStr == 'kefu') { //客服消息

            var item={
                id: 1,
                typeid: null,
                title: "客服",
                ftitle: "客服",
                pic:'',
                name:'客服'
            };
            item['userInfo']=null;
            item['token']="";
            this.props.navigation.navigate("Chat",{item:item});


        } else if (typeStr == 'reward') { //通知中奖

        } else if (typeStr == 'at') { //聊天@ 推送
            var item={
                id: 1,
                typeid: null,
                title: "聊天室",
                ftitle: "聊天室",
                pic:'',
                name:'聊天室'
            };
            item['userInfo']=null;
            item['token']="";
            this.props.navigation.navigate("Chat",{item:item});
        } 
                
    }

    componentWillUnmount() {
        //删除状态改变事件监听
        DeviceEventEmitter.removeListener('ChangeUI');
        DeviceEventEmitter.removeListener('Login');
       
        this.timingTaskEnd();

        // JPushModule.clearAllNotifications();
        // AppState.removeEventListener('change', this.handleAppStateChange);

     }



     getGameUrl(){

       
        // Alert.alert('------')
        // storage.load('islogin',(userInfo)=>{
            let codeurl = host + '/AppKy.kylogin.do';
            let formData = new FormData();
            // formData.append('username',userInfo.username)
            // 请求参数 ('key',value)
           
            HttpUtils.post(codeurl,formData)
                .then(result=>{

                    // console.log('~~~~~lalalalaal~~')
                    // console.log(result)


                    if(result['respCode']==1){
                
        
                        this.setState({
                            gameUrl: result['data']['url']
                        })

                    }else{
    
                    }
                }).catch(error=>{
                   if(num<=0){
                       this.getImageDataList(isReload,num+1);
                   }
                })    

        // })    
     
     }



     getImageDataList(isReload,num) {
        let codeurl = host + '/AppChat.getChatAd.do';
        let formData = new FormData();
        // 请求参数 ('key',value)
        HttpUtils.post(codeurl,formData)
            .then(result=>{
                if(result['respCode']==1){
            
                    let imageData = result['data']['adList'];
                    // let newList = imageData.map((data) => {
                    //     return data.pic
                    // })
                    // this.setState({
                    //     imageDataSource:newList,
                    //     imageDataList:imageData
                    // })

                    this.lunBoImage.setState({
                        images:imageData
                    })

                    storage.save(codeurl, imageData)

                }else{

                }
            }).catch(error=>{
               if(num<=0){
                   this.getImageDataList(isReload,num+1);
               }
            })
    }




    zhongjiangSetContent(contentData) {
        let textArr = []
        let priceArr = []
        let typeArr = []
        for(i in contentData){
            let data = contentData[i];
            textArr.push(data.username)
            priceArr.push("喜中"+data.okamount+'元')
            typeArr.push(data.message)
        }

        let text = '';
        let valueArr = [];
        for(i in textArr){
            if(i%5 == 0  && i != 0   ){

                // if(i== textArr.length - 1){
                //     text = text + "\n" + textArr[i]  + "\n"   
                // }

                if(i != 0){
                
                    let textDict = {value:text}
                    valueArr.push(textDict)
                }
                
                
                text = textArr[i]
            }else  if(i%5 == 4){
                text = text + "\n" + textArr[i]  + "\n"
            }else  if(i == 0){
                text =   textArr[i] 
            }else{
                text = text + "\n" + textArr[i]   
            }
            
            
        }

        
        

        let price = '';
        let pricevalueArr = [];
        for(i in priceArr){
            if(i%5 == 0  && i != 0   ){
                // if(i== priceArr.length - 1){
                //     price = price + "\n" + priceArr[i]  + "\n"  
                // }

                if(i != 0){
                    price = price + "\n"
                    let textDict = {value:price}
                    pricevalueArr.push(textDict)
                }
                
                
                price = priceArr[i]
            }else  if(i%5 == 4){
                price = price + "\n" + priceArr[i]   + "\n"
            }else  if(i == 0){
                price =   priceArr[i] 
            }else{
                price = price + "\n" + priceArr[i]   
            }                                               
        }

        let type = '';
        let typevalueArr = [];
        for(i in typeArr){
            if(i%5 == 0 && i != 0 ){
                // if(i== typeArr.length - 1){
                //     type = type + "\n" + typeArr[i] + "\n"  
                // }

                if(i != 0){
                    type = type + "\n"
                    let textDict = {value:type}
                    typevalueArr.push(textDict)
                }
                
                
                type = typeArr[i]
            }else  if(i%5 == 4){
                type = type + "\n" + typeArr[i]  + "\n"
            }else  if(i == 0){
                type = typeArr[i] 
            }else{
                type = type + "\n" + typeArr[i]   
            }                                               
        }


        this.setState({
            zhongjiangInfo:valueArr,
            priceInfo:pricevalueArr,
            typeInfo:typevalueArr
        })
    }


    ///中奖号码
    getBingoData(){
        let codeurl = host + '/AppPublic.zhongjianList.do';
        let formData = new FormData();

        this.setState({
            isZhongjiang:true
        })


        // 请求参数 ('key',value)
        HttpUtils.post(codeurl,formData)
            .then(result=>{
                if(result['respCode']==1){

                   //  console.log('zhongjiang---')
                    // console.log(result)

                    this.setState({
                        isZhongjiang:false
                    })

                    this.zhongjiangSetContent(result['data'])


                            // if(i== textArr.length - 1){
                            //     text = text + "\n" + textArr[i]  + "\n"   
                            // }

                    //         if(i != 0){
                            
                    //             let textDict = {value:text}
                    //             valueArr.push(textDict)
                    //         }
                           
                            
                    //         text = textArr[i]
                        
                    //     }else  if(i == 0){
                    //         text =   textArr[i] 
                    //     }else{
                    //         text = text + "\n" + textArr[i] 
                    //         if(i == textArr.length - 1){
                    //             let textDict = {value:text}
                    //             valueArr.push(textDict)
                    //         }  
                    //     }
                        
                        
                    // }


                    storage.save(codeurl, result['data'])

                    

                    // let price = '';
                    // let pricevalueArr = [];
                    // for(i in priceArr){
                    //     if(i%5 == 0  && i != 0   ){
                    //         // if(i== priceArr.length - 1){
                    //         //     price = price + "\n" + priceArr[i]  + "\n"  
                    //         // }

                    //         if(i != 0){
                              
                    //             let textDict = {value:price}
                    //             pricevalueArr.push(textDict)
                    //         }
                           
                            
                    //         price = priceArr[i]
                    //     // }else  if(i%5 == 4){
                    //     //     price = price + "\n" + priceArr[i]   + "\n"
                    //     }else  if(i == 0){
                    //         price =   priceArr[i] 
                    //     }else{
                    //         price = price + "\n" + priceArr[i]   
                    //         if(i == priceArr.length - 1){
                    //             let textDict = {value:price}
                    //             pricevalueArr.push(textDict)
                    //         }  
                    //     }                                               
                    // }

                    // let type = '';
                    // let typevalueArr = [];
                    // for(i in typeArr){
                    //     if(i%5 == 0 && i != 0 ){
                    //         // if(i== typeArr.length - 1){
                    //         //     type = type + "\n" + typeArr[i] + "\n"  
                    //         // }

                    //         if(i != 0){
                                
                    //             let textDict = {value:type}
                    //             typevalueArr.push(textDict)
                    //         }
                           
                            
                    //         type = typeArr[i]
                    //     // }else  if(i%5 == 4){
                    //     //     type = type + "\n" + typeArr[i]  + "\n"
                    //     }else  if(i == 0){
                    //         type = typeArr[i] 
                    //     }else{
                    //         type = type + "\n" + typeArr[i]   
                    //         if(i == typeArr.length - 1){
   
                                
                    //             let textDict = {value:type}
                    //             typevalueArr.push(textDict)
                    //         }  
                    //     }                                               
                    // }



                }else{

                    this.setState({
                        isZhongjiang:false
                    })
                }
            }).catch(error=>{
                this.setState({
                    isZhongjiang:false
                })
            })
    }


    ggShowDo(isReload,num){
        let codeurl = host + '/AppMember.ggshow.do';
        let formData = new FormData();
        // 请求参数 ('key',value)
        HttpUtils.post(codeurl,formData)
            .then(result=>{

                if(result['respCode']==1){
        
                    // console.log('~~~~~~~~result')

                    // console.log(result)
                    this.setState({
                        gonggaoDataSource:result['data']
                    })
                    

                    storage.save(codeurl, result['data'])

                    // let imageData = result['data']['adList'];
                    // let newList = imageData.map((data) => {
                    //     return data.pic
                    // })
                    // this.setState({
                    //     imageDataSource:newList,
                    //     imageDataList:imageData
                    // })


                }else{

                }
            }).catch(error=>{
               if(num<=0){
                //    this.getImageDataList(isReload,num+1);
               }
            })
    }


   

    appUpdate(){
        // ImmediateCheckCodePush();
        codePush.sync({
            updateDialog: {
                appendReleaseDescription: true,
                descriptionPrefix:'\n\n有新版本：\n',
                title:'更新',
                mandatoryUpdateMessage : "必须更新后才能使用" ,
                mandatoryContinueButtonLabel : "立即更新" ,
            },
            mandatoryInstallMode:codePush.InstallMode.IMMEDIATE,
        });
    }

    onHeaderRefresh = () => {
        this.getBingoData()
        this.state.currentPage = 1;
        // this.setState(
        //     {
        //         refreshState: RefreshState.HeaderRefreshing,
        //     })
            //获取测试数据
        // if(this.state.refreshState == RefreshState.Idle){
            this.getDataList(true)
        // }

    }

    onFooterRefresh = () => {

        this.setState({
            refreshState: RefreshState.FooterRefreshing,
        })
        //
        // if(this.state.refreshState == RefreshState.Idle){
            this.getDataList(false)
        // }

    }


    getDataList(isReload) {

        this.setState({
            noNetwork:false,
            isloading:true
        })
        let codeurl = host + '/AppPublic.lotteryHallNew.do';
        let formData = new FormData();
        HttpUtils.post(codeurl,formData)
            .then(result=>{



                // console.log('00000')
                // console.log(result)

                if(result['respCode']==1){


                    var titArr = [{titel:'全部',typeid:''}]
                    let categoryArr = result['data']['category'];

                    let newCategoryArr = [...titArr,...categoryArr]

                  //  console.log(newCategoryArr)

                    let testData = result['data']['list'];
                    let newList = testData.map((data) => {
                        return {
                            id: data.id?data.id:1,
                            typeid: data.typeid,
                            title: data.title,
                            ftitle: data.ftitle,
                            pic:data.pic,
                            name:data.name
                        }
                    });
                    storage.save(codeurl, newList)

                    let dataList =  isReload ? newList : [...this.state.dataList, ...newList]
                    // this.state.currentPage = parseInt(dataList.length/this.state.pageSize)+2;

                    if (newList.length) {
                        this.state.currentPage++
                    }

                    let state = dataList.length >= result['data']['total'] ? RefreshState.NoMoreData : RefreshState.Idle;
                    let nodata = false;
                    if(isReload){
                        state = RefreshState.Idle;

                        if(testData.length == 0){
                            nodata=true;
                        }
                    }

                    this.setState({
                        caipiaoTypeList:newCategoryArr,
                        dataList: dataList,
                        refreshState: state,
                        noData:nodata,
                        noNetwork:false,
                        isloading:false
                    })

                    // this.getRemainigTime()
                }else{
                     this.setState({
                        refreshState: RefreshState.Idle,
                         noNetwork:false,
                         isloading:false
                    })
                }

            })

            .catch(error=>{
    
                storage.load(codeurl, (dic) => {
         
                    if (dic != "" && dic != null) {
                        let dataList =  dic
                        this.state.currentPage = 2;
                        let state = RefreshState.Idle;
                        let nodata = false;
                        if(isReload){
                            state = RefreshState.Idle;
                        }
                        this.setState({
                            dataList: dataList,
                            refreshState: state,
                            noData:nodata,
                            noNetwork:false,
                            isloading:false
                        })
                    }else{
                        this.setState({
                            noNetwork:true,
                            isloading:false
                        })
                    }
                 })

            })
    }



    keyExtractor = (item, index) => {
        return index.toString()
    }

    renderCell = (info) => {

        
        if (myThis.state.dataList.length) {
            return <HomeCell info={info.item}  onPress={this.itemClick.bind(this, info)}/>
        } else {
            return <NoDataView height={sys.dheight/4}
            click={()=>this.chongXingRoad()}
                                isloading={this.state.isloading}
                                text = {this.state.isloading?"加载中":"暂无内容,点击重新加载"}
                            />;
        }

         
    }

    // renderAllCell = (info) => {
    //     return <Cell type='all' index={info.index} info={info.item} onPress={this.itemClick.bind(this, info)}/>
    // }


    itemClick(e){

        var nowTime= (new Date()).getTime();
        if(Math.abs(nowTime-clickLastTime)<sys.clickIntervalTime){
            //小于0.5秒
            return false;
        }
        clickLastTime=nowTime;

        // if(this.state.islogin != 1){
        //     DeviceEventEmitter.emit('Login', {}); 
        // }else{
            const {navigate} = this.props.navigation;


            // console.log('eitem')
            // console.log(e.item)

            // console.log('更多.typeid')

            switch(e.item.typeid){
                
                case 'ssc':
                    navigate('Cqssc',{item:e.item})
                    break;
                case 'k3':
                    navigate('KuaiThreeAction',{item:e.item})
                    break;

                case 'pk10':

                    navigate('Pkten',{item:e.item});
                    break;

                case 'lhc':
                    navigate('Sixhc',{item:e.item});
                    break;

                case 'keno':
                    navigate('Keno',{item:e.item});
                    break;
                case 'dpc':
                    navigate('Dpc',{item:e.item});
                    break;

                case 'x5':
                    navigate('ElevenSelectFive',{item:e.item});
                    break;

                case 'pcdd':                  
                    navigate('PcDanDan',{item:e.item});

                default: //更多
                    
                    //  console.log('更多')

                    navigate('CaipiaoMore',{caipiaoTypeList:myThis.state.caipiaoTypeList,
                        dataList:myThis.state.dataList})
                    break;

            }
        // }



    }

    xuanzheClick(indext) {
        //console.log(indext);

        if (indext==0) { //棋牌
            // this.refs.toast.show('暂未开放 敬请期待', DURATION.LENGTH_LONG);
            if(global.user.loginState != 1) {
                DeviceEventEmitter.emit('Login', {});
                return false;
            }

            // const {navigate} = this.props.navigation;
            this.props.navigation.navigate("Game",{gameUrl:this.state.gameUrl});

        } 

        if (indext==1) { //充值


            if(global.user.loginState != 1) {
                DeviceEventEmitter.emit('Login', {});
                return false;
            }

            const {navigate} = this.props.navigation;
            this.props.navigation.navigate("BuycoinsCent",{fubi:0});
        } 

        if (indext==2) { //聊天室
            var item={
                id: 1,
                typeid: null,
                title: "聊天室",
                ftitle: "聊天室",
                pic:'',
                name:'聊天室'
            };
            item['userInfo']=null;
            item['token']="";
            this.props.navigation.navigate("Chat",{item:item});
        }

        if (indext==3) { //在线客服
            DeviceEventEmitter.emit('Read', {});
                var item={
                    id: 1,
                    typeid: null,
                    title: "客服",
                    ftitle: "客服",
                    pic:'',
                    name:'客服'
                };
                item['userInfo']=null;
                item['token']="";
                this.props.navigation.navigate("Chat",{item:item});
        }
 
    }



    delHtmlTag(str)
    {
    return str.replace(/<[^>]+>/g,"");  //正则去掉所有的html标记
    }


    renderHeadView = (info) => {
        let hedImageView = <View style={styles.headView}>

        {/* <FcusImage/> */}
                <LunBoImage isNeedRun={false} 
                onPress={this.clickImgView.bind()}
                 ref={(LunBoImage) => {this.lunBoImage = LunBoImage;}}
                />
            </View>

        var ggStr = '暂无公告'   
                

        var valueArr = []
        for(i in this.state.gonggaoDataSource){

            let reskuStr = this.delHtmlTag(this.state.gonggaoDataSource[i]['content']).replace(/^\s+|\s+$/g,"");
            valueArr.push({value:reskuStr})

            // valueArr.push({value:this.delHtmlTag(this.state.gonggaoDataSource[i]['content'])})
        }

        let gonggaoView = <View style={{height:30,width:sys.dwidth,backgroundColor:sys.whiteColor,marginTop:0,flexDirection:'row'}}>
        <Text style={{color:sys.mainColor,textAlign:'left',
        textAlignVertical:'center',marginLeft:10,paddingRight:3,
        height:30,...Platform.select({
            ios: { lineHeight: 30},
                android: {}
        })}}>公告:</Text>
        {/* <Text style={{color:sys.titleColor}}>{ggStr}</Text> */}
        <MarqueeHorizontal
                textList = {valueArr }
                speed = {60}
                width = {sys.dwidth-55}
                height = {30}
                direction = {'left'}
                reverse = {false}
                bgContainerStyle = {{backgroundColor : '#white'}}
                textStyle = {{fontSize : 14,color : sys.titleColor}}
                onTextClick = {(item) => {


                    // if(Platform.OS == 'ios'){
                    //     Alert.alert(JSON.stringify(item.value));
                    // }else{
                        
                        this.refs.alert.alertWithOptions({
                            options:['好的'],
                            title:'公告',
                            detailText:item.value
                        },
                             // index是当前点击选项的索引
                            (index)=>{
                               
                              if(index == 0) {
                                   
                              }
                            }
                        );
                
                    // }

  
                }}
            />
        </View>

        let titArr = ['棋牌游戏','充值','聊天室','在线客服']
        let xuanzheView = <View style={{flexDirection:'row',marginTop:0,marginLeft:0,
        width:sys.dwidth}}>

            {titArr.map((info, index) => {

                let requireStr = require('./images/qpgame.png')
                if (index==1) {
                    requireStr = require('./images/chongzhi.png')
                } else if (index == 2) {
                    requireStr = require('./images/liao.png')
                }else if (index == 3) {
                    requireStr = require('./images/kefu.png')
                }
                 
                 return <TouchableOpacity key={index} onPress={this.xuanzheClick.bind(this, index)}>
          
                 <View style={[{marginLeft:0,height:75+25,width:sys.dwidth/4}]}>
                     <Image
                     source={requireStr}
                     style={[{marginLeft:(sys.dwidth/4 - 55)/2,marginTop:10,width:55,height:55}]}
                     />
    
                     {/* <View style={[styles.title,{marginLeft:0,width:sys.dwidth/3,height:50}]}> */}
    
                    <Text style={{marginTop:5,fontSize:13,
                    color:sys.heiColor,textAlign:'center'}} numberOfLines={1}>{info}</Text>
                         {/* <Text style={{marginTop:3,fontSize: 13,color: '#999999',textAlign:'center'}} 
                         numberOfLines={1}>{info.ftitle}</Text> */}
                     {/* </View> */}
                 </View>
             </TouchableOpacity>
            })
            }


        </View>

 
        let titView = <View style={{height:30,width:sys.dwidth,backgroundColor:sys.whiteColor,marginTop:0}}>
        <Text style={{color:sys.mainColor,textAlign:'left',
        textAlignVertical:'center',marginLeft:10,
        height:30,...Platform.select({
            ios: { lineHeight: 30},
                android: {}
        })}}>热门彩种</Text>
        </View>
       
         return <View>

             {hedImageView}
             {gonggaoView}
             <View style={{height:1,backgroundColor:sys.backgroundColor}}></View>
             {xuanzheView}
             <View style={{height:10,backgroundColor:sys.backgroundColor}}></View>
             {titView}
             <View style={{height:1,backgroundColor:sys.backgroundColor}}></View>

         </View>

      
    }

    renderFooterView(){


        let contentView=null

        if (myThis.state.zhongjiangInfo.length) {
            contentView = <MarqueeVertical
            textList = {myThis.state.zhongjiangInfo.length > 0 ? myThis.state.zhongjiangInfo : [ {value : '用户****'}] }
            priceList = {myThis.state.priceInfo.length > 0 ? myThis.state.priceInfo : [ {value : '恭喜中奖***元'}] }
            typeList = {myThis.state.typeInfo.length > 0 ? myThis.state.typeInfo : [ {value : '购买***彩票'}] }
            
            width = {sys.dwidth}
            height = {121}
            direction = {'up'}
            delay = {0}
            numberOfLines = {5}
            duration = {6800}
            bgContainerStyle = {{backgroundColor : 'white'}}
            textStyle = {{fontSize : 14,color : sys.titleColor,lineHeight:24,width:90}}
            priceStyle = {{fontSize : 14,color : sys.mainColor,lineHeight:24,width:(sys.dwidth-110)/2}}
            typeStyle = {{fontSize : 14,color : sys.titleColor,lineHeight:24,width:(sys.dwidth-110)/2}}
            onTextClick = {(item) => {
            // alert(''+JSON.stringify(item));
            }}
        />
        } else {
            contentView= <NoDataView height={120} click={()=>myThis.getBingoData()}
             isloading = {myThis.state.isZhongjiang}
             text = {"加载失败，点击重新加载"}></NoDataView>
        }


        return <View style={{left:0,top:0,backgroundColor:'white'}}>
            <View style={{left:0,top:0,height:10,backgroundColor:sys.backgroundColor,width:sys.dwidth}}></View>
            <View style={{left:10,top:0}}>
            <Text style={{fontSize:16,color:sys.titleColor,height:40,lineHeight:40}}>中奖信息</Text>


            {contentView}

        </View>
        <View style={{left:0,top:0,height:10,backgroundColor:sys.whiteColor,width:sys.dwidth}}></View>
            
       </View>
       




    }


    clickImgView(url) {     
        const {navigate} = myThis.props.navigation;
        navigate('WebViewScene', {uri: {content:url}});
    }


    render() {


        myThis = this;
      
         let gendoudic = {curExpect:'',ftitle:"更多好玩游戏",id:"",
            lastFullExpect:"",name:"",pic:"",
            remainTime:632,title:"更多彩种",typeid:""}

            var newList = [];  
            
            if (this.state.dataList.length) {
                if (this.state.dataList.length<7) {

                    var size = this.state.dataList.length;
                    if (size%2 ==0) {
                        size = size-1;
                    }

                    for(var i=0;i<size;i++) {
                        var caipiao = this.state.dataList[i]
                        newList.push(caipiao)
                    }

                    newList.push(gendoudic)    
                } else {
                    for(var i=0;i<7;i++) {
                        var caipiao = this.state.dataList[i]
                        newList.push(caipiao)
                    }
    
                    newList.push(gendoudic)
                }
            } else {
                newList.push(gendoudic)
             }


            let contentView =<RefreshListView
                    style={{width:sys.dwidth}}
                    data={newList}
                    keyExtractor={this.keyExtractor}
                    renderItem={this.renderCell}
                    refreshState={this.state.refreshState}
                    onHeaderRefresh={this.onHeaderRefresh}
                    //onFooterRefresh={this.onFooterRefresh}
                    numColumns ={2}
                     ListHeaderComponent={this.renderHeadView}
                    ListFooterComponent={this.renderFooterView}
                    // horizontal={false}
                    ItemSeparatorComponent={this._separator}
                    // 可选
                    footerRefreshingText= '玩命加载中 >.<'
                    footerFailureText = '我擦嘞，居然失败了 =.=!'
                    footerNoMoreDataText= '-我是有底线的-'
                />

        return(  <View  style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
        <HotUpdate />
            {contentView  }

            <Toast  //提示
                    ref="toast"
                    style={{backgroundColor:'gray'}}
                    position='center'
                    positionValue={200}
                    opacity={0.7}
                    textStyle={{color:'white'}}
                />  
                <AlertTool ref="alert" />
        </View>    
                     
        )
    }

  

    chongXingRoad() {


        if (!this.lunBoImage.state.images.length) {
            this.getImageDataList(false,0)
        }

        if (!this.state.gonggaoDataSource.length) {
            this.ggShowDo(false,0)
        }

        this.getDataList(true)
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
         marginTop: Platform.OS == 'ios' ? 0 : 0,
        backgroundColor:sys.whiteColor
        
    },
    headView:{
        width:sys.dwidth,
        height:sys.dwidth/3
     },
     
     front_cover: {

        marginLeft:10,
        marginTop:10,
        width: sys.dwidth/4 - 20,
        height: sys.dwidth/4 - 20,
        borderRadius:0,
        // borderWidth:2,
        // borderColor:'white',
        alignItems:'center',

        // backgroundColor:sys.yellowColor

    },

    
})

export default Home







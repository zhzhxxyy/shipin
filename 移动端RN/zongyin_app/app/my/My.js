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
    TouchableOpacity,
    AsyncStorage,
    Alert,
    Button,
    DeviceEventEmitter,
    ImageBackground,
    Platform,
    ScrollView,
    TouchableHighlight
} from 'react-native';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import {sys} from "../common/Data"
const host = sys.host;
import codePush from "react-native-code-push";
var ImagePicker = require('react-native-image-picker');

import RNLogin from "../common/RNLoginModule"
import HttpUtils from "../common/HttpUtil";

import Pop from 'rn-global-modal'

import DialogSelected from '../common/AlertSelect';
import {NavigationActions} from 'react-navigation'
var clickLastTime=0;
var mythis =null;

import JPushModule from 'jpush-react-native';

export default class More extends React.Component {

    static  navigationOptions = ({navigation,screenProps}) => ({

   
        headerTitle:"我的",
        headerTitleStyle:{
            alignSelf:'center',
            flex: 1,
            textAlign: 'center',
            
        },
        onTabPress:(()=>{
            alert('Home');
        }),
        async tabBarOnPress({ navigation, defaultHandler }) {
         
            if (mythis!=null) {
                mythis.getPersonInfo()

                if (!mythis.state.dailiqq.length) {
                    mythis.getDailiData()
                }
                
            }

            defaultHandler();
          },

        headerRight: <TouchableOpacity onPress={()=>self.rightViewClick()}>
            <Text style={{color:sys.whiteColor,marginRight:8}}>提现记录</Text>
        </TouchableOpacity>,

    });
    

    constructor(props) {
        super(props);
     
        this.state = {
            islogin:0,
            unRead:false,
            head_pic:sys.host+'/resources/images/face/1.jpg',
            nick_name:'',
            balance:'',
            identifier:'',
            id:0,
            describe:'暂无签名',
            fubi:'0.00',
            token:'',
            host_state:0,
            isshowEye:true,
            userInfo:{},
            proxy : 0,
            dailiqq:''
        }
    }

    componentDidMount(){
        this.initView(null);

        mythis.getDailiData()
        storage.load('islogin', (dic) => {
            if (dic != "" && dic != null) {
                this.initView(dic);
            }
        })
        //登录成功通知
        DeviceEventEmitter.addListener('ChangeUI',(dic)=>{
            //接收到详情页发送的通知，刷新首页的数据，改变按钮颜色和文字，刷新UI
            if(dic['islogin'] != 0){
                this.initView(dic);
                this.getPersonInfo(0)
            }else{
                storage.save('islogin', "")
                global.user.loginState=0;
                global.user.userData=null;
                global.user.token="";
                this.initView(null);
            }
        });

         //消息未读
         DeviceEventEmitter.addListener('unRead',(dic)=>{
                this.setState({
                    unRead:true
                });
        });

         //消息未读
         DeviceEventEmitter.addListener('Read',(dic)=>{
            this.setState({
                unRead:false
            });
    });

        //注册通知
        DeviceEventEmitter.addListener('Login',(dic)=>{
            //接收到详情页发送的通知，刷新首页的数据，改变按钮颜色和文字，刷新UI
            const {navigate} = this.props.navigation;
            this.props.navigation.navigate("Login",dic);
        });
    }


    componentWillUnmount() {
        //删除状态改变事件监听
        DeviceEventEmitter.removeListener('ChangeUI');
        DeviceEventEmitter.removeListener('UnRead');
        DeviceEventEmitter.removeListener('Login');
     }


    getPersonInfo(num){
        let codeurl = host + '/AppMember.index.do';
        let formData = new FormData();
        HttpUtils.post(codeurl,formData)
            .then(result=>{
                if(result['respCode']==1){  
                    let dic = result['data'];
                    global.user.loginState=1;
                    global.user.userData=dic;
                    global.user.token=dic.token;
                    storage.save('islogin',dic)
                    this.initView(dic);  
                }else{
                    storage.save('islogin', "")
                    global.user.loginState=0;
                    global.user.userData=null;
                    global.user.token="";
                    this.initView(null);  
                }
            })
            .catch(error=>{
              
                if(num<=0){
                    this.getPersonInfo(++num);
                }
            })
    }


    getDailiData(){

        let codeurl = host + '/AppChat.more.do';
            let formData = new FormData();
            // formData.append('username',userInfo.username)
            // 请求参数 ('key',value)

            HttpUtils.post(codeurl,formData)
                .then(result=>{

                    if(result['respCode']==1){
                
        
                        mythis.setState({
                            dailiqq:result['data']['dailiqq'],
                        })

                    }else{
    
                    }
                }).catch(error=>{
                    

                })    
    }


    //初始化界面
    initView(userInfo){
        if(userInfo!=null&&userInfo!=""){
            this.state.userInfo = userInfo;
            this.setState({
                islogin:1,
                id:userInfo['id'],
                head_pic:userInfo['face'],
                nick_name:userInfo['username'],
                balance:userInfo['balance'],
                identifier:userInfo['identifier'],
                describe:userInfo['describe'],
                fubi:userInfo['fubi']?userInfo['fubi']:'0.00',
                token:userInfo['token'],
                host_state:userInfo['host_state'],
                proxy:userInfo['proxy']
            });
        }else{
            this.setState({
                islogin:0
            });
        }
    }


    login(){
      const {navigate} = this.props.navigation;
      this.props.navigation.navigate("Login",{name:"zhengsan"});
    }

    clickThirdViewButton(locationNum){

        var nowTime= (new Date()).getTime();
        if(Math.abs(nowTime-clickLastTime)<sys.clickIntervalTime){
            //小于0.5秒
            return false;
        }
        clickLastTime=nowTime;

        switch (locationNum){

            case 0:{
                
                this.props.navigation.navigate("TodayLoss");
                break;
            }
            case 1:{
                this.props.navigation.navigate("MyIncome");
                break;
            }
            case 2:{
                this.props.navigation.navigate("AFAnchors");
                break;
            }
            case 3:{

                if(Platform.OS == 'ios'){
                    // this.props.navigation.navigate("PushSetting");
                }else{
                    RNLogin.startActivityFromJS("com.tujing.zongyin.push.TCPublishSettingActivity", this.state.fubi);
                }

                break;
            }
            case 4:{
                this.props.navigation.navigate("MyFuns",{userInfo:this.state.userInfo});
                break;
            }
            case 5:{
                this.props.navigation.navigate("WithdrawCash",{userInfo:this.state.userInfo});

                break;
            }

        }
    }



    clickMimaCenTer(){

        this.props.navigation.navigate("SecurityCenter",{security:'security'});
        

    }


    clickfourBtn(locationNum) {


        var nowTime= (new Date()).getTime();
        if(Math.abs(nowTime-clickLastTime)<sys.clickIntervalTime){
            //小于0.5秒 点击速度太快导致重复下单 
            return false;
        }
        clickLastTime=nowTime;
        
        if (locationNum == 0) {

            const {navigate} = this.props.navigation;
            this.props.navigation.navigate("BuycoinsCent",{fubi:this.state.fubi});
            //this.props.navigation.navigate("BuycoinsCent");

        }
        if (locationNum == 1) {

            const {navigate} = this.props.navigation;
            // /this.props.navigation.navigate("BuddhaFriend",{fubi:this.state.fubi,identifier:this.state.identifier});

        }
        if (locationNum == 2) {

            const {navigate} = this.props.navigation;
            // this.props.navigation.navigate("MyFollow",{userInfo:this.state.userInfo});

            this.props.navigation.navigate("Hot",{userInfo:this.state.userInfo});
        }
        if (locationNum == 3) {


            const {navigate} = this.props.navigation;
            //改成交易记录
            //this.props.navigation.navigate("MyBuyCourse",{fubi:this.state.fubi});

            this.props.navigation.navigate("TradingRecord",{fubi:this.state.fubi});

            
        }

        if (locationNum == 4) {
            this.props.navigation.navigate("WithdrawCash",{userInfo:this.state.userInfo});
        }

        if (locationNum == 5) {
              //客服系统 先取游客的账号 再取登陆用户的账号信息
                this.setState({
                    unRead:false
                })
            
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

        if (locationNum == 6) {
            this.tuiChu();
        }

        if (locationNum == 7) {

            if(this.state.proxy == 1 || this.state.proxy == '1' || this.state.proxy == true ){
                this.props.navigation.navigate("DelegateCenter",{userInfo:this.state.userInfo});
            }else{


                var qqView = <View style={{height:20}}></View>;

                if (mythis.state.dailiqq.length) {
                    qqView = <Text style={{color:sys.titleColor,width:sys.dwidth - 100,marginLeft:25,marginTop:10,fontSize:18,fontWeight: 'bold'}}
                    numberOfLines={3}
                    >{'QQ:'+mythis.state.dailiqq}</Text>
                }

                Pop.show(
                    <View style={{width:sys.dwidth - 50,backgroundColor:sys.whiteColor,borderRadius:5}}>
                    
                    <Text style={{color:sys.titleColor,width:sys.dwidth - 100,marginLeft:25,marginTop:20,fontSize:18,fontWeight: 'bold'}}
                    numberOfLines={3}
                    >成为代理请咨询代理中心客服</Text>

                    {qqView}
                    
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
            
                                               }}>
                                <Text  style={{width:(sys.dwidth - 50)/4-20,height:40,textAlign:'center',color:sys.titleColor,fontSize:16,textAlignVertical:'center',
                                marginLeft:10,color:'#067a6a',...Platform.select({
                                    ios:{
                                        lineHeight:40,
                                    },
                                    android:{
                                    }
                                }),}}>客服</Text>
                            </TouchableOpacity>
                        </View>
                    
                    </View>
                )


                // Alert.alert('成为代理请咨询代理中心客服 QQ：66131321','',
                // [
                //     {text:"取消", onPress: ()=>{
                //             DeviceEventEmitter.emit('LoginQuxiao', {});
                //         }},
                //     {text:"咨询", onPress: ()=>{
                //         DeviceEventEmitter.emit('Read', {});
                //         var item={
                //             id: 1,
                //             typeid: null,
                //             title: "客服",
                //             ftitle: "客服",
                //             pic:'',
                //             name:'客服'
                //         };
                //         item['userInfo']=null;
                //         item['token']="";
                //         this.props.navigation.navigate("Chat",{item:item});
                //     }}
                // ])
            }

           
        }

        if (locationNum == 8) {
        codePush.sync({
            updateDialog: {
                appendReleaseDescription: true,
                descriptionPrefix:'\n\n更新内容：\n',
                title:'更新',
                mandatoryUpdateMessage:'',
                mandatoryContinueButtonLabel:'更新',
            },
            mandatoryInstallMode:codePush.InstallMode.IMMEDIATE,
        });

        }

    }


    tuiChu(){
        Alert.alert('确认退出登录？','',
            [
               
                {text:"退出", onPress: ()=>{
                    let codeurl = host + '/index/index/logout';
                    let formData = new FormData();
                    // 请求参数 ('key',value)
                    HttpUtils.post(codeurl, formData)
                        .then(result => {
                        })
                        .catch(error => {
                            //Alert.alert("登陆失败" + JSON.stringify(error));
                        })
                    //DeviceEventEmitter.emit('ChangeUI', {islogin:0});
                    WebSocket.bind()
                
                    storage.remove('token')
                    storage.remove('islogin')

                    JPushModule.setTags(['proxy0','nologin'], () => {
                        console.log("Set tag succeed");
                    }, () => {
                        console.log("Set tag failed");
                    });

                    global.user = { 
                        loginState:-1,//登录状态 -1 还没初始化（需要获取数据初始化） 0 表示没登录 1 表示登录
                        userData:null,//用户数据 
                        token:"",
                      }; 

                    this.setState({
                        islogin:0
                    })


                     this.getYouke()

                     
                }},
                {text:"继续玩彩", onPress: ()=>{
                    this.props.navigation.reset([NavigationActions.navigate({ routeName: 'Tab' })])                    
                }}
            ]
           );
    }


    getYouke()
    {


        storage.load('youkeChat',(youkeChat)=>{

            console.log('youkeChat')
            console.log(youkeChat)

           // if(youkeChat==null || youkeChat=='')
          
        })


        var codeurl = sys.host + '/AppChat.keFuChat.do';
        let formData = new FormData();

        HttpUtils.post(codeurl, formData)
            .then(result => {
             
                console.log('result')

                console.log(result)
            
            }).catch(error => {
                
            })
        
    }

    //设置
    setup(){
        const {navigate} = this.props.navigation;
        navigate('WebViewScene', {uri: {content:'https://tb.53kf.com/code/client/10165470/1'}});
        // const {navigate} = this.props.navigation;
        // this.props.navigation.navigate("Setup",{name:"zhengsan"});

    }
    //点击眼睛
    clickEye(){
        this.setState({
            isshowEye:!this.state.isshowEye
        })
    }



    renderunLogindView(){

        var bradgeView = null;

        if(this.state.unRead ){
            bradgeView = (
                <View style={{backgroundColor: 'red', position: 'absolute', right: 2, top: 3, height: 10, width: 25, borderRadius: 5, overflow: 'hidden'}}>
                    <Text style={{fontSize: 8, textAlign: 'center',color:'white'}}>新消息</Text>
                </View>
            )
        }

        
        return <KeyboardAwareScrollView

          style={styles.container}>

            <ImageBackground  style={{height:140,width:sys.dwidth,backgroundColor:'white'}}>
            <TouchableOpacity onPress={()=>this.login()}>
                <Image style={[styles.image]} source={require('../res/images/placeholderHeader.png')}></Image>
                <Text style={styles.text}>未登录</Text>
            </TouchableOpacity>
            </ImageBackground>
            <View style={styles.secondView}>
                <View style={styles.secondView}>
                    {/* <TouchableOpacity style={styles.button} onPress={()=>this.clickfourBtn(0)}> */}
                    <TouchableOpacity style={styles.button} onPress={()=>this.login()}>
                        <Image style={styles.bimage} source={require('./images/icon_goubizhongxin.png')}></Image>
                        <Text style={styles.buttontext}>充值</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.button} onPress={()=>this.login()}>
                        <Image style={styles.bimage} source={require('./images/icon_yaoqingfoyou.png')}></Image>
                        <Text style={styles.buttontext}>提现</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.button} onPress={()=>this.clickfourBtn(5)}>
                    
                        <Image style={styles.bimage} source={require('./images/icon_wodeguanzhu.png')}></Image>
                        {
                            bradgeView
                        }
                        <Text style={styles.buttontext}>客服</Text>
                    </TouchableOpacity>
                    {/*<TouchableOpacity style={styles.button} onPress={()=>this.login()}>*/}
                        {/*<Image style={styles.bimage} source={require('./images/icon_kecheng.png')}></Image>*/}
                        {/*<Text style={styles.buttontext}>交易记录</Text>*/}
                    {/*</TouchableOpacity>*/}
                </View>
            </View>
            <View style={[styles.thirdView]}>
                {/* <TouchableOpacity style={styles.bthirdView} onPress={()=>this.login()}> */}

                <TouchableOpacity style={styles.bthirdView} onPress={()=>this.login()}>
                    <Image style={styles.leftimage} source={require('./images/mine_icon1.png')}></Image>
                    <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>投注记录</Text>
                        <Text style={styles.subText}>查看所参与的游戏记录</Text>
                    </View>                   
                    <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity>
                <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.login()}>
                    <Image style={styles.leftimage} source={require('./images/mine_icon2.png')}></Image>
                    <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>交易记录</Text>
                        <Text style={styles.subText}>查看充值提现记录</Text>
                    </View>                   
            
                    <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity>
                <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.login()}>
                

                <Image style={styles.leftimage} source={require('./images/mine_icon3.png')}></Image>
                    <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>今日盈亏</Text>
                        <Text style={styles.subText}>查看盈亏情况</Text>
                    </View>  
                <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
            </TouchableOpacity>

            <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.erweima()}>
                

                <Image style={styles.leftimage} source={require('./images/erweima.png')}></Image>
                    <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>我的二维码</Text>
                        <Text style={styles.subText}>截图分享</Text>
                    </View>  
                <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
            </TouchableOpacity>

        
            <View style={{marginTop:1,flexDirection:'row',backgroundColor:'white',height:64}}>
                
                <Image style={styles.leftimage} source={require('./images/banbenNum.png')}></Image>
                 
                   <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>1.0.8</Text>
                        <Text style={styles.subText}>2019.07.05</Text>
                    </View>
                
            </View>

            </View>
        </KeyboardAwareScrollView>
    }


    renderLoginedView(){
        // default_red_bg

        // var delegateView = null;
        // if(this.state.proxy == 1 || this.state.proxy == '1' || this.state.proxy == true ){

            var  delegateView = (
                <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.clickfourBtn(7)}>
                    {/* <Image style={styles.bimage} source={require('./images/icon_kecheng.png')}></Image> */}


                    <Image style={styles.leftimage} source={require('./images/mine_icon4.png')}></Image>
                    <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>代理中心</Text>
                        <Text style={styles.subText}>查看下级及团队情况</Text>
                    </View>  
             
                    <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity>
                )

        // }

    //     let ishost = this.state.host_state==2?<View><TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.clickThirdViewButton(3)}>
    //         <Text style={styles.tbuttontext}>发布直播</Text>
    //         <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
    //     </TouchableOpacity>
    //         <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.clickThirdViewButton(4)}>
    // <Text style={styles.tbuttontext}>我的粉丝</Text>
    //     <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
    //     </TouchableOpacity></View>:null;
        
        let isShowEyeImage = this.state.isshowEye?require('./images/icon_show.png'):require('./images/icon_hide.png');
        let isShowEyeMoney = this.state.isshowEye?this.state.fubi:"******"





        var bradgeView = null;

        if(this.state.unRead ){
            bradgeView = (
                <View style={{backgroundColor: 'red', position: 'absolute', right: sys.dwidth/6 - 40, top: 3, height: 12, width: 36, borderRadius: 4, overflow: 'hidden'}}>
                    <Text style={{fontSize: 10, textAlign: 'center',color:'white',lineHeight:12}}>新消息</Text>
                </View>
            )
        }




        if(this.state.islogin==1){
            var tuichuButton=( <TouchableOpacity style={styles.tuichuView} onPress={()=>this.clickfourBtn(6)}>

                {/* <Image style={{marginLeft:sys.dwidth/2 - 10 - 12 - 30,marginTop:10,width:24,height:24,}} source={require('./images/mine_icon5.png')}></Image> */}
                    
                {/* <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image> */}
            <Text style={{marginLeft:sys.dwidth/2 - 33,width:66,lineHeight:50,fontSize:16,color:sys.mainColor}}>退出登录</Text>
            {/* <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image> */}
        </TouchableOpacity>);
        }else{
            var tuichuButton=null;
        }

        return <KeyboardAwareScrollView

            style={styles.container}>
            <ImageBackground  style={styles.hedViewStyles}>

                {/* <Image style={styles.imageLogin} source={require('../res/images/logo.png')}></Image> */}
         
                <TouchableOpacity onPress={()=>this.clickBianjiBtn()}>
                <Image style={styles.imageLogin} source={{uri:this.state.head_pic}}></Image>
                </TouchableOpacity>
                <View style={styles.infoStyles}>
                    <Text style={styles.nametext}>{'欢迎您，'+this.state.nick_name}</Text>
                    <Text style={styles.numtext}>余额:{this.state.balance}</Text>
                </View>

                <TouchableOpacity onPress={()=>this.getPersonInfo()}>
                <Image style={styles.shuaxinImage} source={require('./images/myShuaXin.png')}></Image>
                </TouchableOpacity>


            </ImageBackground>

            <View style={styles.secondView}>

                    <TouchableOpacity style={styles.button} onPress={()=>this.clickfourBtn(0)}>
                        <Image style={styles.bimage} source={require('./images/icon_goubizhongxin.png')}></Image>
                        <Text style={styles.buttontext}>充值</Text>
                    </TouchableOpacity>

                    <TouchableOpacity style={styles.button} onPress={()=>this.clickThirdViewButton(5)}>
                        <Image style={styles.bimage} source={require('./images/icon_yaoqingfoyou.png')}></Image>
                        <Text style={styles.buttontext}>提现</Text>
                    </TouchableOpacity>

                    <TouchableOpacity style={styles.button} onPress={()=>this.clickfourBtn(5)}>
                        <Image style={styles.bimage} source={require('./images/icon_wodeguanzhu.png')}></Image>
                        <Text style={styles.buttontext}>客服</Text>
                        {
                    bradgeView
                }
                    </TouchableOpacity>
                    {/* <TouchableOpacity style={styles.button} onPress={()=>this.clickfourBtn(3)}>
                        <Image style={styles.bimage} source={require('./images/icon_kecheng.png')}></Image>
                        <Text style={styles.buttontext}>交易记录</Text>
                    </TouchableOpacity> */}

            </View>
            <View style={styles.thirdView}>
                 <TouchableOpacity style={styles.bthirdView} onPress={()=>this.clickfourBtn(2)}>
                    <Image style={styles.leftimage} source={require('./images/mine_icon1.png')}></Image>
                    <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>投注记录</Text>
                        <Text style={styles.subText}>查看所参与的游戏记录</Text>
                    </View>                   
                    <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity>

                <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.clickfourBtn(3)}>
                <Image style={styles.leftimage} source={require('./images/mine_icon2.png')}></Image>
                    <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>交易记录</Text>
                        <Text style={styles.subText}>查看充值提现记录</Text>
                    </View>                   
            
                    <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity>

                <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.clickThirdViewButton(0)}>
                <Image style={styles.leftimage} source={require('./images/mine_icon3.png')}></Image>
                    <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>今日盈亏</Text>
                        <Text style={styles.subText}>查看盈亏情况</Text>
                    </View>  
                <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity>

                {delegateView}


                <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.clickMimaCenTer()}>
                <Image style={styles.leftimage} source={require('./images/mine_anquan.png')}></Image>
                    <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>安全中心</Text>
                        <Text style={styles.subText}>密码管理</Text>
                    </View>  
                <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
            </TouchableOpacity>

            <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.erweima()}>
                

                <Image style={styles.leftimage} source={require('./images/erweima.png')}></Image>
                    <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>我的二维码</Text>
                        <Text style={styles.subText}>截图分享</Text>
                    </View>  
                <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
            </TouchableOpacity>

            <View style={{marginTop:1,flexDirection:'row',backgroundColor:'white',height:64}}>
                
                <Image style={styles.leftimage} source={require('./images/banbenNum.png')}></Image>
                 
                   <View style={{top:10}}>
                        <Text style={styles.tbuttontext}>1.0.8</Text>
                        <Text style={styles.subText}>2019.07.17</Text>
                    </View>
                
            </View>

                {/* <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.clickfourBtn(8)}>
                    <Text style={styles.tbuttontext}>版本升级</Text>
                    <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity> */}

                {/* {ishost} */}
                {tuichuButton}  
                <View style = {{height:20,width:sys.dwidth}}></View>
            </View>


            <DialogSelected ref={(dialog)=>{
                this.dialog = dialog;
            }} />
        </KeyboardAwareScrollView>
    }
    render() {
        mythis = this;
        return(this.state.islogin?this.renderLoginedView():this.renderunLogindView())
    }



    erweima() {


        this.props.navigation.navigate("Erweima",{Erweima:'Erweima'});

        
    }


    clickBianjiBtn(){
    
        this.props.navigation.navigate("UserImgList",{changeUseImg:(imgUrl)=>{

            // this.setState({
            //     head_pic:imgUrl
            // })

            this.getPersonInfo()



        }});

      //  this.state.isCover = isCover
        // this.dialog.show("请选择照片", ['拍照','相册'], '#333333', this.callbackSelected);
    }


    // 回调
    callbackSelected(i){


        switch (i){
            case 0: // 拍照
                this.choose(1)
                break;
            case 1: // 图库
                this.choose(0)
                break;
        }
    }

    choose(isCamera){


        let options = {
            title: 'Select Avatar',
            customButtons: [
                {name: 'fb', title: 'Choose Photo from Facebook'},
            ],
            storageOptions: {
                skipBackup: true,
                path: 'images'
            }
        };

        if(isCamera){
            // Open Image Library:
            ImagePicker.launchCamera(options, (response)  => {
                // Same code as in above section!
                if (response.didCancel) {
                    console.log('User cancelled image picker');
                }
                else if (response.error) {
                    console.log('ImagePicker Error: ', response.error);
                }
                else if (response.customButton) {
                    console.log('User tapped custom button: ', response.customButton);
                }
                else {
                    // let source = { uri: response.uri };
                    // You can also display the image using data:
                    // let source = { uri: 'data:image/jpeg;base64,' + response.data };
                    this.uploadImage(response.uri)
                }
            });
            return;
        }

        ImagePicker.launchImageLibrary(options, (response) => {


            if (response.didCancel) {
                console.log('User cancelled image picker');
            }
            else if (response.error) {
                console.log('ImagePicker Error: ', response.error);
            }
            else if (response.customButton) {
                console.log('User tapped custom button: ', response.customButton);
            }
            else {
                // let source = { uri: response.uri };
                // You can also display the image using data:
                // let source = { uri: 'data:image/jpeg;base64,' + response.data };
                this.uploadImage(response.uri)

            }
        });

    }


}

const styles = StyleSheet.create({

    container: {
        flex:1,
        backgroundColor:sys.backgroundColor,
        height:sys.dheight
    },

    text:{
        fontSize:18,
        color:sys.subTitleColor,
        textAlign: 'center',
        marginTop:10,
        width:150,
        marginLeft:sys.dwidth/2-75,
        marginBottom:10
    },
    image:{
        marginTop:20,
        alignSelf:'center',
        width:80,
        height:80,
        borderRadius:40,
        borderWidth:1,
        borderColor:sys.grayColor,
        overflow:'hidden'
    },
    imageLogin:{
        marginTop:20,
        marginLeft:20,
        width:100,
        height:100,
        backgroundColor:sys.grayColor,
        borderRadius:50,
        
    },
    shuaxinImage:{
        marginTop:140 - 30 - 25,
        marginLeft:sys.dwidth/2-125 - 30,
        width:20,
        height:20,
        // backgroundColor:sys.mainColor,
        borderRadius:0,
        
    },

    secondView:{
        flexDirection:'row',
        height:92,
        marginTop:10,
       backgroundColor:sys.whiteColor,
    },

    buttontext:{
        marginTop:11,
        textAlign:'center',
        fontSize:14,
        alignSelf:'center',
        width:sys.dwidth/4
    },

    button:{
        width:sys.dwidth/3,
        marginTop:10,
        backgroundColor:'white'
    },


    leftimage:{
        marginLeft:10,
        marginTop:12,
        width:40,
        height:40,
    },

    bimage:{
        marginLeft:sys.dwidth/6-14.5,
        marginTop:7.5,
        width:29,
        height:29,
    },

    thirdView: {
        backgroundColor:sys.backgroundColor,// '#f2f2f2',
        // height:45 *10,    //sys.dheight,
        width: sys.dwidth,
    },


    tuichuView:{
        marginTop:10,
        flexDirection:'row',
        backgroundColor:'white',
        height:50
    },

    bthirdView:{
        marginTop:10,
        flexDirection:'row',
        backgroundColor:'white',
        height:60
    },

    bthirdViewtwo:{
        marginTop:1,
        flexDirection:'row',
        backgroundColor:'white',
        height:64
    },

    subText:{
        marginLeft:10,
        //marginTop:11,
        width:150,
        fontSize:12,
        lineHeight:20,
        color:sys.subTitleColor
    },

    tbuttontext:{
        marginLeft:10,
        //marginTop:11,
        width:150,
        fontSize:16,
        lineHeight:20,
        color:sys.titleColor,
    },

    btimage:{
        marginTop:22.5,
        marginLeft:sys.dwidth-150-64-20,
        width:9,
        height:14.25,
    },

    head_pic:{
        alignSelf:'center',
        marginLeft:14,
        width:50,
        height:50,
        borderRadius:25,
        overflow:'hidden'
    },

    hedViewStyles:{
        flexDirection:'row',
        height:140,
        backgroundColor:'white',       
    },

    firstView:{
        marginTop:105,
        marginLeft:0,
        backgroundColor:'#f2f2f2',
        width:sys.dwidth,
        height:90
    },

    firstViewOne:{
        marginTop:-195,
        backgroundColor:'white',
        alignSelf:'center',
        width:sys.dwidth - 28,
        height:140
    },

    infoStyles:{
        marginTop:20,
        marginLeft:5,
        width:sys.dwidth/2,//150,
        height:100,
      //  backgroundColor:'red'
    },

    nametext:{
        marginLeft:10,
        marginTop:15,
        width:sys.dwidth/2-15,
        fontSize:15,
        color:sys.titleColor
    },

    numtext:{
        marginLeft:10,
        width:sys.dwidth/2-15,// sys.dwidth/2,
        fontSize:15,
       // marginBottom:10,
        marginTop:35,
        color:sys.titleColor
    },

    settext:{
        marginTop:20,
        fontSize:18,
        //textAlign:'left',

        color:'red',
        marginLeft:10
    },


    moneyStyte:{
        marginTop:25,
        flexDirection:'row',
        height:25,
        alignSelf:'center',
    },

    moneyNumStyte:{

        marginLeft:0,
        marginTop:(25-14)/2,
       // fontSize:14,
        fontSize:18,
        color:'#7d2d17'
    },

    moneyImgStyte:{

        marginTop:5,
        marginLeft:10,
        height:20,
        resizeMode:'center',
        width:20
    },
    intViewStyle:{
        flexDirection:'row',
        position: 'absolute',
        bottom:0,
        left: 0,
        right: 0,
        backgroundColor:'white',
        height:40,
        // width:sys.dwidth - 28

    },
    contView:{
        marginLeft:0,
        width:(sys.dwidth - 28)/3,
        height:40,
        textAlign:'center',
        color:'#7d2d17',
        fontSize:15
    }

})

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
    Alert,
    DeviceEventEmitter,
    ActivityIndicator,
    Platform, NativeModules,
    Keyboard
} from 'react-native';

import {sys,isIphoneX} from "../common/Data"
const host = sys.host;
import RNLogin from "../common/RNLoginModule"
import HttpUtils from "../common/HttpUtil"
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'

import JPushModule from 'jpush-react-native';

import Toast,{DURATION} from 'react-native-easy-toast';

var clickLastTime=0;

export default class Login extends React.Component {

    constructor(props) {
        super(props);
        const {params} = this.props.navigation.state;

        this.state = {
            phone:params?params.username:'',
            passwd:params?params.password:'',
            iscommit:false
        }
    }

    static navigationOptions = ({navigation}) => ({
        header:null
    });



    login() {

        Keyboard.dismiss()

        if(!(typeof(this.state.phone) != "undefined" && this.state.phone.length<=20 && this.state.phone.length>=6)){
     
            this.refs.toast.show("请输入正确账号", DURATION.LENGTH_LONG);
            return;
        }

        if (typeof(this.state.passwd)=="undefined" || this.state.passwd.length < 6) {
            this.refs.toast.show("密码长度不能小于6位", DURATION.LENGTH_LONG);
        
            return;
        }
        var nowTime= (new Date()).getTime();
        if(Math.abs(nowTime-clickLastTime)<sys.clickIntervalTime){
            //小于0.5秒
            return false;
        }
        clickLastTime=nowTime;

        this.setState({
            iscommit:true
        })
        let codeurl = host + '/AppPublic.LoginDo.do';
        let formData = new FormData();
        // 请求参数 ('key',value)
        formData.append('name', this.state.phone);
        formData.append('pass', this.state.passwd)


        HttpUtils.post(codeurl, formData)
            .then(result => {
                this.setState({
                    iscommit:false
                })
                if (result['respCode'] == 1) {
                    let resultdata = result['data'];
                    global.user.loginState=1;
                    global.user.userData=resultdata;
                    global.user.token=resultdata.token;
                    
                    resultdata['islogin']=1
                    storage.save('token',resultdata['token'])
                    storage.save('islogin',resultdata)
                    //登陆成功时候发送通知
                    
                    DeviceEventEmitter.emit('ChangeUI', resultdata);
                    this.bindUser();
                    this.popBack();


                    JPushModule.setTags([global.user.userData['id'],"proxy"+global.user.userData['proxy'],'login'], () => {
                        console.log("Set tag succeed");
                    }, () => {
                        console.log("Set tag failed");
                    });


                } else {
                   
                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                }
            })
            .catch(error => {
                this.setState({
                    iscommit:false
                })
       
                this.refs.toast.show(error, DURATION.LENGTH_LONG);
            })

    }

    returnZh(zhanghao){
        this.setState({phone:zhanghao});
    }

    register(){

        // this.setState({
        //     passwd:''
        // })

        this.props.navigation.navigate('Register',{name:"zhengsan",returnZh:(zhanghao)=>{
            this.setState({phone:zhanghao})
        }});
    }


    popBack(){

        
        // let {params} = this.props.navigation.state;
        // if(Platform.OS=='android' && params.name == 'login'){
        //     RNLogin.goBack((name) => {
        //     }, (err) => {
    
        //     });
        // }else{
            this.props.navigation.goBack()
        // }

    }

    //綁定用戶
    bindUser(){
        if(global.lianjiestate==2&&global.user!=null){
            var arr = {}  //等同于 arr=new Array();
            arr['token'] =global.user.token,
            arr['userInfo']= global.user.userData;
            let dic = {
                data:{'logined':JSON.stringify(arr)},
                url:'User.bind'
            }
            global.ws.send(JSON.stringify(dic));
        }
    }
   

    render() {
        return (
            <KeyboardAwareScrollView
                style={styles.container}
                keyboardShouldPersistTaps = {"always"}

                >
                <View style={styles.row}>
                    <TouchableOpacity onPress={()=>{
                        this.popBack();
                    }}>
                    <Image style={styles.back} source={require('../res/images/icon_back.png')}></Image>
                    </TouchableOpacity>
                    <Text style={styles.text}>登录</Text>
                </View>
                {/* <Image style={styles.image} source={require('../..//res/images/logo.png')}></Image> */}
                <TextInput
                    underlineColorAndroid='transparent'
                    style={styles.textIp}
                    placeholder="用户名"
                    value={this.state.phone}
                    onChangeText={(text) => this.setState({phone:text})}
                />
                <View style={styles.view}></View>

                <TextInput
                    underlineColorAndroid='transparent'
                    secureTextEntry={true}//显示输入的为密码
                    style={styles.textIp}
                    // value={this.state.passwd}
                    placeholder="密码"
                    onChangeText={(text) => this.setState({passwd:text})}
                />
                <View style={styles.view}></View>

                <TouchableOpacity disabled={this.state.iscommit} style={styles.button} onPress={()=>this.login()}>
                    {this.state.iscommit?<ActivityIndicator style={{marginLeft:(sys.dwidth-40-100)/2 - 10 - 20,width:20
                        ,height:20,marginTop:10}} />:null}
                    <Text style={[styles.buttontext,{width:100,marginLeft:this.state.iscommit?10:(sys.dwidth-40-100)/2}]}>{this.state.iscommit?'登录中..':'登录'}</Text>
                </TouchableOpacity>


                <TouchableOpacity disabled={this.state.iscommit} style={styles.zcButton} onPress={()=>this.register()}>
                    <Text style={styles.buttontext}>免费注册</Text>
                </TouchableOpacity>




                {/* <View style={styles.forgetregister}>

                    <TouchableOpacity tyle={styles.forgetPw} onPress={()=>this.forgetCode()}>
                        <Text style={styles.forgetPw}>忘记密码</Text>
                    </TouchableOpacity>
                <TouchableOpacity style={styles.register} onPress={()=>this.register()}>
                    <Text style={styles.registerTX}>立即注册</Text>
                </TouchableOpacity>
                </View> */}

                {/* <View style={{flexDirection:'row',marginTop:40}}>
                    <View style={
                        {
                            width:sys.dwidth/5,
                            height:1,
                            backgroundColor:sys.subTitleColor,
                            marginLeft:sys.dwidth/5-10,

                        }
                    }></View>
                    <Text style={
                        {
                            marginLeft:5,
                            color:sys.titleColor,
                            marginTop:-10

                        }
                    }>其他方式登录</Text>
                    <View style={{marginLeft:5,width:sys.dwidth/5,height:1,backgroundColor:sys.subTitleColor}}></View>

                </View> */}

                {/* <View style={{flexDirection:'row',marginTop:30}}>
                    <TouchableOpacity style={{marginLeft:(sys.dwidth-180)/2}} onPress={()=>this.thirdLogin("qq")}>
                    <Image style={
                        {

                            width:50,
                            height:50

                        }
                    } source={require('./images/icon_log_QQ_normal.png')}/>
                    </TouchableOpacity>
                    <TouchableOpacity style={{marginLeft:5}} onPress={()=>this.thirdLogin("weibo")}>
                    <Image style={
                        {
                            marginLeft:15,
                            width:50,
                            height:50

                        }
                    } source={require('./images/icon_log_weibo_normal.png')}/>
                    </TouchableOpacity>
                    <TouchableOpacity style={{marginLeft:5}} onPress={()=>this.thirdLogin("weixin")}>
                    <Image style={
                        {
                            marginLeft:15,
                            width:50,
                            height:50

                        }
                    } source={require('./images/icon_log_weixin_normal.png')}/>
                    </TouchableOpacity>
                </View> */}

                <Toast  //提示
                    ref="toast"
                    style={{backgroundColor:'gray'}}
                    position='top'
                    positionValue={200}
                    opacity={0.6}
                    textStyle={{color:'white'}}
                /> 
            </KeyboardAwareScrollView>
        )
    }

    thirdLogin(plat){



        NativeModules.RNShareModule.thirdLogin(plat,(resp)=>{

            let third_type = 1
            if(plat == "qq"){
                third_type = 2
            }else if(plat == "weibo"){
                third_type = 3
            }


            let codeurl = host + '/index/index/loginThird';
            let formData = new FormData();
            // 请求参数 ('key',value)
            formData.append('third_type', third_type);
            formData.append('open_id', resp.open_id);
            formData.append("name",resp.name);
            formData.append("image_url",resp.image_url);
            formData.append("access_token",resp.access_token);
            HttpUtils.post(codeurl, formData)
                .then(result => {
                    if (result['respCode'] == 1) {

                            
                            let resultdata =   result['data'];
                            resultdata['islogin']=1
                            //登陆成功时候发送通知
                         
                            this.refs.toast.show("登录成功", DURATION.LENGTH_LONG);
                            global.user.loginState = true;//设置登录状态 
                            global.user.userData = resultdata;//保存用户数据 
                          
                            DeviceEventEmitter.emit('ChangeUI', resultdata);
                            this.popBack();
                      
                    } else {                    
                        this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                    }

                })
                .catch(error => {
             
                    this.refs.toast.show(error, DURATION.LENGTH_LONG);
                })


        },(error)=>{

            this.refs.toast.show("登录失败，请检查网络", DURATION.LENGTH_LONG);

        })
    }

    // componentWillMount(){
    //
    //     BackHandler.addEventListener('hardwareBackPress', this.onBackAndroid);
    //
    // }
    //
    // componentWillUnmount() {
    //
    //     BackHandler.removeEventListener('hardwareBackPress', this.onBackAndroid);
    // }
    //
    // onBackAndroid = () => {
    //
    //     let {params} = this.props.navigation.state;
    //     if(Platform.OS=='android' && params.name == 'login'){
    //         RNLogin.goBack((name) => {
    //         }, (err) => {
    //             Alert.alert(err)
    //         });
    //     }else{
    //         this.props.navigation.goBack()
    //     }
    // };
}

const styles = StyleSheet.create({
    row:{
        //改变主轴的方向
      flexDirection:'row',
        height:60,
        marginTop:10

    },
    back:{
        marginLeft:14,
        marginTop: Platform.OS == 'ios' ? 21 : 5,
    },
    container: {
        flex:1,
        marginTop: 0,
        paddingTop:isIphoneX()?20:0,
        backgroundColor:'#ffffff',
        height:sys.dheight
    },
    text:{
        fontSize:18,
        color:"#333333",
        textAlign: 'center',
        marginTop: Platform.OS == 'ios' ? 21 : 5,
        width:150,
        marginLeft:sys.dwidth/2-75-21
    },
    image:{
        marginTop:20,
        alignSelf:'center',
        width:100,
        height:100

    },
    textIp:{
        marginTop:22.5,
        marginLeft:12,
        width:sys.dwidth-40,
        height:40,
        // backgroundColor:'yellow',
    },
    view:{
        height:0.8,//TODO这里小于0.8没显示出来
        backgroundColor:'#cccccc',
        marginTop:0,
        marginLeft:12,
        width:sys.dwidth-24,
    },
    button:{
        width:sys.dwidth-40,
        marginLeft:20,
        marginTop:40,
        height:40,
        backgroundColor:sys.mainColor,
        flexDirection:'row',
        borderRadius:5,
        
        // textAlignVertical:'center',
        // ...Platform.select({
        //     ios: { lineHeight: 40},
        //      android: {}
        // })

    },
    zcButton:{
        width:sys.dwidth-40,
        marginLeft:20,
        marginTop:20,
        height:40,
        backgroundColor:sys.mainColor,
        borderRadius:5,

    },
    buttontext:{
        //marginTop:11,
        color:'#ffffff',
        textAlign:'center',
        fontSize:18,
        height:40,
        textAlignVertical:'center',
        ...Platform.select({
            ios: { lineHeight: 40},
             android: {}
        })
        //backgroundColor:'blue'
    },
    forgetregister:{
      flexDirection:'row'
    },
    forgetPw:{
        marginLeft:20,
        marginTop:20,
        width:100,
        color:sys.mainColor,

    },
    register:{
        marginLeft:sys.dwidth-120-100-20,
        flexDirection:'row',
        width:100,
        justifyContent:'flex-end',
        marginTop:20
    },
    registerTX:{
        fontSize:14,
        color:sys.mainColor
    }
})
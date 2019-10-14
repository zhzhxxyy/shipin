/**
 * Created by 卓原 on 2017/10/31.
 * zhuoyuan93@gmail.com
 */
import React from 'react';
import {
    View,
    Text,
    StyleSheet,
    Keyboard,
    Image,
    TextInput,
    TouchableOpacity,
    Alert,
    DeviceEventEmitter,
    ActivityIndicator,
    Platform, NativeModules,
} from 'react-native';

import {sys,isIphoneX} from "../common/Data"
const host = sys.host;
import HttpUtils from "../common/HttpUtil"
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'

import Toast,{DURATION} from 'react-native-easy-toast';

var clickLastTime=0;

export default class PassWordChange extends React.Component {


  
    constructor(props) {
        super(props);
        // const {params} = this.props.navigation.state;

        this.state = {
            oldPasswd:'',
            passwd:'',
            twoPasswd:'',
            iscommit:false,
            isZhanHuPws:this.props.navigation.state.params.isZhanHuPws,
        }
    }

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
                title: params.isZhanHuPws?'修改登录密码':'修改提现密码',
                headerTitleStyle:{
                    alignSelf:'center',
                    flex: 1,
                    textAlign: 'center',
                    
                },
                
                
                // headerRight:React.createElement(View, null, null),
                headerRight: <View />,
                headerLeft:leftView
            }

        }

        return {
        title: params.isZhanHuPws?'修改登录密码':'修改提现密码',
        headerTitleStyle:{
            alignSelf:'center',
            flex: 1,
            textAlign: 'center',
            
        },
        
        
        // headerRight:React.createElement(View, null, null),
        headerRight: <View />
       }
    };

 

    changePws() {

        Keyboard.dismiss();

        if(!(typeof(this.state.oldPasswd) != "undefined" && this.state.oldPasswd.length<=20 && this.state.oldPasswd.length>=6)){
     
            this.refs.toast.show("请输入正确旧密码", DURATION.LENGTH_LONG);
            return;
        }

        if (typeof(this.state.passwd)=="undefined" || this.state.passwd.length < 6) {
            this.refs.toast.show("密码长度不能小于6位", DURATION.LENGTH_LONG);
        
            return;
        }


        if (typeof(this.state.twoPasswd)=="undefined" || this.state.twoPasswd.length < 6) {
            this.refs.toast.show("密码长度不能小于6位", DURATION.LENGTH_LONG);
        
            return;
        }


        if (this.state.passwd != this.state.twoPasswd) {
            this.refs.toast.show("两个密码不一致", DURATION.LENGTH_LONG);
       
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


        let codeurl = host + '/AppMember.updateSafePass.do';

        if (this.state.isZhanHuPws) {

            console.log('0000000')

            codeurl = host + '/AppMember.updatePass.do';
        }
        let formData = new FormData();
        // 请求参数 ('key',value)
        formData.append('oldpassword', this.state.oldPasswd);
        formData.append('pa1', this.state.passwd)
        formData.append('password', this.state.twoPasswd);
        HttpUtils.post(codeurl, formData)
            .then(result => {
                this.setState({
                    iscommit:false
                })

                // console.log(result)

                if (result['respCode'] == 1) {
                 
                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                    this.popBack();
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


    popBack(){

        
        this.props.navigation.goBack()


    }


    render() {

        // const { params } = this.navigation.state;

        return (
            // <KeyboardAwareScrollView
            //     style={styles.container}
            //     keyboardShouldPersistTaps = {"always"}

            //     >
                <View>
                <TextInput
                    underlineColorAndroid='transparent'
                    style={styles.textIp}
                    placeholder="旧密码"
                    value={this.state.oldPasswd}
                    onChangeText={(text) => this.setState({oldPasswd:text})}
                />
                <View style={styles.view}></View>

                <TextInput
                    underlineColorAndroid='transparent'
                    secureTextEntry={true}//显示输入的为密码
                    style={styles.textIp}
                    placeholder="新密码"
                    onChangeText={(text) => this.setState({passwd:text})}
                />
                <View style={styles.view}></View>

                <TextInput
                    underlineColorAndroid='transparent'
                    secureTextEntry={true}//显示输入的为密码
                    style={styles.textIp}
                    placeholder="再次输入新密码"
                    onChangeText={(text) => this.setState({twoPasswd:text})}
                />
                <View style={styles.view}></View>

                <TouchableOpacity disabled={this.state.iscommit} style={styles.button} onPress={()=>this.changePws()}>
                    {this.state.iscommit?<ActivityIndicator style={{marginLeft:(sys.dwidth-40-100)/2 - 10 - 20,width:20
                        ,height:20,marginTop:10}} />:null}
                    <Text style={[styles.buttontext,{width:100,marginLeft:this.state.iscommit?10:(sys.dwidth-40-100)/2}]}>{this.state.iscommit?'修改中..':'确定'}</Text>
                </TouchableOpacity>
                <Toast  //提示
                    ref="toast"
                    style={{backgroundColor:'gray'}}
                    position='center'
                    positionValue={200}
                    opacity={0.6}
                    textStyle={{color:'white'}}
                />
                </View>
                 
            // </KeyboardAwareScrollView>
        )
    }

 
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
        marginTop:10,
        marginLeft:12,
        width:sys.dwidth-40,
        height:40,
        //  backgroundColor:'yellow',
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
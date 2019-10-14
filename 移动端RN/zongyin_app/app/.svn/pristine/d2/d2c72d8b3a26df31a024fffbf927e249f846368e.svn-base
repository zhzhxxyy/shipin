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

import DialogSelected from '../common/AlertSelect';
import {NavigationActions} from 'react-navigation'
var clickLastTime=0;
var mythis;
export default class SecurityCenter extends React.Component {

    // static  navigationOptions = ({navigation}) => ({
    //     headerTitle:"安全中心",
    //     headerTitleStyle:{
    //         alignSelf:'center',
    //         flex: 1,
    //         textAlign: 'center',
            
    //     },
            
    //     headerRight: <View />

    // });
    

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
                headerTitle:"安全中心",
                headerTitleStyle:{
                    alignSelf:'center',
                    flex: 1,
                    textAlign: 'center',
                    
                },
                    
                headerRight: <View />,
                headerLeft:leftView
            }

        }

        return {
            headerTitle:"安全中心",
            headerTitleStyle:{
                alignSelf:'center',
                flex: 1,
                textAlign: 'center',
                
            },
                
            headerRight: <View />
       }
    };


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
            proxy : 0
        }
    }

    componentDidMount(){

    }


    xiugaiPws(num) {
      
        if (num ==0) {

            this.props.navigation.navigate("PassWordChange",{isZhanHuPws:true});

        } else {
            this.props.navigation.navigate("PassWordChange",{isZhanHuPws:false});
        }


    }



    render() {

        mythis = this;


        // this.props.navigation.state.params.item

        return(<View style={styles.thirdView}>
          
        <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.xiugaiPws(0)}>
            <Image style={styles.leftimage} source={require('./images/xiugai_zhanhuPws.png')}></Image>
            <View style={styles.tbuttontext}>
                <Text style={[styles.tbuttontext,,{marginLeft:0,marginTop:0}]}>修改登录密码</Text>      
            </View>                   
            <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
        </TouchableOpacity>

        <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.xiugaiPws(1)}>
            <Image style={styles.leftimage} source={require('./images/xiugai_yinhangPws.png')}></Image>
            <View style={styles.tbuttontext}>
                <Text style={[styles.tbuttontext,{marginLeft:0,marginTop:0}]}>修改提现密码</Text> 
            </View>  
            <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
        </TouchableOpacity>

        </View>)
    }


}

const styles = StyleSheet.create({

    container: {
        flex:1,
        backgroundColor:sys.backgroundColor,
        height:sys.dheight
    },


    thirdView: {
        backgroundColor:sys.backgroundColor,// '#f2f2f2',
         height:sys.dheight,    //sys.dheight,
        width: sys.dwidth,
    },


    bthirdViewtwo:{
        marginTop:1,
        flexDirection:'row',
        backgroundColor:'white',
        height:60
    },

    leftimage:{
        marginLeft:10,
        marginTop:10,
        width:40,
        height:40,
    },

    tbuttontext:{
        marginLeft:10,
        marginTop:10,
        width:150,
        height:40,
        fontSize:16,
        color:sys.titleColor,
        textAlign:'left',
        textAlignVertical:'center',
        ...Platform.select({
            ios: { lineHeight:40},
            android: {}
        })
    },
 

    btimage:{
        marginTop:(60-15)/2,
        marginLeft:sys.dwidth-150-60-20,
        width:9,
        height:15,
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


    

    bimage:{
        marginLeft:sys.dwidth/6-14.5,
        marginTop:7.5,
        width:29,
        height:29,
    },

})

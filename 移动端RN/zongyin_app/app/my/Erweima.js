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

import {sys,isIphoneX,NoDataView} from "../common/Data"
const host = sys.host;
import RNLogin from "../common/RNLoginModule"
import HttpUtils from "../common/HttpUtil"
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'

import Toast,{DURATION} from 'react-native-easy-toast';


var clickLastTime=0;

export default class Erweima extends React.Component {

   

    // static  navigationOptions = ({navigation}) => ({
        
    //     headerTitle:"我的二维码",
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
                headerTitle:"我的二维码",
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
            headerTitle:"我的二维码",
            headerTitleStyle:{
                alignSelf:'center',
                flex: 1,
                textAlign: 'center',
                
            },
            headerRight: <View />
        }
    };

    constructor(props) {
        super(props)

        this.state = {
            tuijianMaStr:'',
           
        }
    }


    componentDidMount(){

    
        storage.load('islogin', (dic) => {
            if (dic != "" && dic != null) {
          

                this.setState({
                    tuijianMaStr:dic['username'],
                })

            }
        })




        // this.payMoney()


    }


  
    // payMoney(){
    //     let codeurl = sys.host + '/Pay.appPay.do';


    //     let formData = new FormData();

    //     formData.append('money',100);


    //     this.setState({
    //         isloading:true
    //     })

    //     HttpUtils.post(codeurl,formData)
    //         .then(result=>{


    //             console.log('result')
    //             console.log(result)

                

    //         })
    //         .catch(error=>{
    //             //Alert.alert(error)
                

    //         })
    // }


    render() {




        let codeurl =  sys.host + '/AppPublic.shareAddress.do'

        let imgView=<Image
                    source={{
                    uri:codeurl
                    }}
                    style={{height:sys.dwidth/2,width:sys.dwidth/2,marginTop:30,marginLeft:sys.dwidth/4,backgroundColor:sys.silveryColor}}
                    />

        


        
        let yaoqinStr = ''

        if (this.state.tuijianMaStr.length) {
            yaoqinStr = '邀请码：'+ this.state.tuijianMaStr
        }

        return (
           
             <View style={{width:sys.dwidth,height:sys.dheight-200}}>


             {/* <Image
            source={require('./images/erweima.png')}
            style={{height:sys.dwidth/2,width:sys.dwidth/2,marginTop:30,marginLeft:sys.dwidth/4}}
            /> */}

            {imgView}

            <Text style={{marginTop:10,marginLeft:10,width:sys.dwidth-20,
                textAlign:'center',fontSize:20,color:sys.titleColor}}>{yaoqinStr}</Text>

             </View>
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
import React from 'react';
import {
    TouchableOpacity,StyleSheet,Text,View,Image,ActivityIndicator,Platform,NativeModules
} from 'react-native';

// import { Header } from 'react-navigation';

const { StatusBarManager } = NativeModules;

var Dimensions = require('Dimensions');
// iPhoneX
const X_WIDTH = 375;
const X_HEIGHT = 812;

const XR_WIDTH = 414;
const XR_HEIGHT = 896;

// screen
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT = Dimensions.get('window').height;
// export function isIphoneX() {
//     return (
//         Platform.OS === 'ios' &&
//         ((SCREEN_HEIGHT === X_HEIGHT && SCREEN_WIDTH === X_WIDTH) ||
//             (SCREEN_HEIGHT === X_WIDTH && SCREEN_WIDTH === X_HEIGHT))
//     )
// }


export function isIphoneX() {


    if (Platform.OS === 'ios' && (SCREEN_HEIGHT==X_HEIGHT && SCREEN_WIDTH === X_WIDTH)) {
        return true;
    }

    if (Platform.OS === 'ios' && (SCREEN_WIDTH==X_HEIGHT && SCREEN_HEIGHT === X_WIDTH)) {
        return true;
    }

    if (Platform.OS === 'ios' && (SCREEN_HEIGHT==XR_HEIGHT && SCREEN_WIDTH === XR_WIDTH)) {
        return true;
    }

    if (Platform.OS === 'ios' && (SCREEN_WIDTH==XR_HEIGHT && SCREEN_HEIGHT === XR_WIDTH)) {
        return true;
    }
   
    return false;
}




export function sysScrollH(height) {

    return Platform.OS=='ios'?(isIphoneX()?320-height:290+height):310-height
}


export class TitleHeadView extends React.Component{

    render(){
        let {text} = this.props;
    
        return (
            <View style={{  flex: 1,
                justifyContent: 'center',
                alignItems: 'center'}}> 
                {/* <Text style={{ flex: 1, textAlign: 'center',fontSize:18 }}>{text}</Text> */}
                <Text style={{
        ...Platform.select({
            ios:{
                lineHeight:36,
            }
        }), textAlign: 'center',fontSize:18 ,fontWeight:'bold',color:sys.titleColor}}>{text}</Text>
         
            </View>

            
        );

    }
}



export const sys = {
    dwidth: Dimensions.get('window').width,
    dheight: Dimensions.get('window').height,
    bottomPourHeight:105,
    headKaijiHeight:70,
    toubuChangeHeight:45,
    navBarHeight:Platform.OS=='ios'?(isIphoneX()?(88+24):64):88,
    mainColor:'#573D1B',
    backgroundColor: 'rgba(248,248,248,1)',
    grayColor: 'rgba(240, 240, 240, 1)',
    deepGrayColor: 'rgba(180, 180, 180, 1)',
    titleColor:"#333333",
    subTitleColor:'gray',
    whiteColor:"#ffffff",
    silveryColor:'#e4e3eb',
    redColor:'rgba(252, 66, 0, 1)',
    yellowColor:"#fa7e00",
    blueColor:"#218ddd",
    greenColor:'rgba(113, 154, 112, 1)',
    subRedColor:"#e91e63",
    heiColor: 'rgba(18, 18, 18, 1)',
    headerColor: 'rgba(74, 74, 74, 1)',
    appName:'视频RN',
    priceNum:1,
    versionCode:"1.0.0",


    host:"http://spf.zhzhxxyy.com/",//todo 修改 正式
    webSocketUrl:"ws://119.27.164.96:8845",//todo  修改 正式

    pl3Color:'#18CDCA',

    purpleColor:'rgba(98,93,144,1)',


    k3RusultDict:{'1':'src_page_resouce_addon_newlottery_k3_dice1.png','2':'src_page_resouce_addon_newlottery_k3_dice2.png','3':'src_page_resouce_addon_newlottery_k3_dice3.png',
            '4':'src_page_resouce_addon_newlottery_k3_dice4.png','5':'src_page_resouce_addon_newlottery_k3_dice5.png','6':'src_page_resouce_addon_newlottery_k3_dice6.png'},


    clickIntervalTime:500,//每次点击的间隔事件0.5秒
    line: {
        height: 4,
        opacity:0.5,
        backgroundColor: 'darkgray',
        width:Dimensions.get('window').width,
        marginLeft: 10
    },
    root_container:{
        flex: 1,
        backgroundColor: '#f3f3f4',
    },


}

export class DateTool extends React.Component{


    static parserDateString(dateString){
        // if(dateString){
            // var date = '2015-03-05 17:59:00.0';
            dateString = dateString.substring(0,19);    
            dateString = dateString.replace(/-/g,'/'); 
            var timestamp = new Date(dateString).getTime();
            // document.write(timestamp);
            return timestamp;

        // }
    }

// timestamp时间戳  formater时间格式

 static formatDate(timestamp, formater) {
    let date = new Date();
    //date.setTime(timestamp);//setTime(parseInt(timestamp));
    formater = (formater != null)? formater : 'yyyy-MM-dd hh:mm';
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };

        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ?
                (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
        return fmt;
    }
    return date.Format(formater);
 }


}



// export class Date extends React.Component{



export class NoDataView extends React.Component{

    constructor(props) {
        super(props);
    }

    render(){
        let {text,click,noNet,isloading=true,height,imagestr} = this.props;
      
        var loadView =  null;


        if(isloading){
            // loadView = <View><Text>{text}</Text><ActivityIndicator /></View>;
            loadView = <View style={styles.center}>
            <ActivityIndicator size="large" color={sys.mainColor} />
            <Text style={{ marginTop: 10, color: sys.mainColor }}>{text?text:"正在加载中..."}</Text>
        </View>;
        }else{
            loadView = <TouchableOpacity onPress={click} style={{flex:1,
                backgroundColor:"#ffffff",
                justifyContent:'center',
                height:height}}>

            <Image style={styles.image} source={imagestr}></Image>
                {/* <View style={styles.center}>
                    <ActivityIndicator size="large" color={sys.mainColor} />
                    <Text style={{ marginTop: 10, color: sys.mainColor }}>{text}</Text>
                </View> */}
            <Text style={styles.text}>{text?text:"正在加载中..."}</Text>
        </TouchableOpacity>;
        }
        return (
            <View style={{  flex: 1,
                justifyContent: 'center',
                alignItems: 'center',backgroundColor:sys.whiteColor}}>{loadView}</View>
        );

    }
}

const styles = StyleSheet.create({

  
    image:{
        marginTop:-64,
        alignSelf:'center',
        width:100,
        height:100,
        resizeMode:'center'
    },
    text:{
        width:sys.dwidth,
        height:30,
        marginTop:10,
        textAlign:'center',
        justifyContent: 'center',
        textAlignVertical:'center',
        ...Platform.select({
            ios: { lineHeight: 30},
            android: {}
        })
    }
})

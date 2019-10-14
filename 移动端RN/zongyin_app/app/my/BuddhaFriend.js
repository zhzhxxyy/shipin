
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
    FlatList,
    TouchableOpacity,
    AsyncStorage,
    Alert,
    Button
} from 'react-native';

import {sys} from "../common/Data"
import QRCode from 'react-native-qrcode'
const host = sys.host;
import HttpUtils from "../common/HttpUtil"
import { NativeModules } from 'react-native';

export default class BuddhaFriend extends React.Component {



    static  navigationOptions = ({navigation}) => ({
        headerTitle:"我的佛友",
        headerTitleStyle:{
            alignSelf:'center',
            flex: 1,
            textAlign: 'center',
            
        },
        headerRight: <View />
    });

    constructor(props) {
        super(props);
        this.state = {
            data: [],
            text: "http://ttt.ttt.cn/index/index/invite?id=7",
            fubi:''
        }
    }


    componentDidMount(){


        this.loadData()

    }


    loadData(){

        let codeurl = host + '/member/index/getInviteCode';
        let formData = new FormData();
        // 请求参数 ('key',value)
        HttpUtils.post(codeurl, formData)
            .then(result => {
                let code = result['data']['code']
                let fubi = result['data']['fubi']
                this.setState({
                    text:code,
                    fubi:fubi
                });

            })
            .catch(error => {
                Alert.alert("获取二维码失败" );
            })

    }

    clickfourBtn(locationNum) {

        if (locationNum == 0) {

            const {navigate} = this.props.navigation;
            // this.props.navigation.navigate("RewardRules",{fubi:this.state.fubi});
            //this.props.navigation.navigate("BuycoinsCent");

        }
        if (locationNum == 1) {

            const {navigate} = this.props.navigation;
            this.props.navigation.navigate("ConsumeDetail",{fubi:this.state.fubi,identifier:this.state.identifier});

        }
        if (locationNum == 2) {
            let nav = this.props.navigation.state
            let iamgeurl = "http://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&" +
                "sec=1517200242979&di=7154c01a5ac587309759db3c573a0b35&imgtype=0&src=http%3A%2F%2Ffile06.16sucai.com" +
                "%2F2016%2F0714%2Fd4970ecff2fc980697ef71e4e91bfe64.jpg"
            let url = host + "/index/index/share?type=app&identifier="+nav.params.identifier
            NativeModules.RNShareModule.share('宗教','邀你来直播',iamgeurl,url, (name) => {

                Alert.alert(name)
            },(error)=>{
                Alert.alert(error)
            });



        }

        if (locationNum == 3) {


            const {navigate} = this.props.navigation;
            this.props.navigation.navigate("InviteHistory",{fubi:this.state.fubi});

        }

    }


    render() {

        let nav = this.props.navigation.state
        return (
            <View style={styles.container}>
                <View style={styles.yuerViewStyle}>
                    <Text style={styles.yuerStyle}>可提现福币</Text>

                    <Text style={styles.yuerNumStyle}>{this.state.fubi}</Text>
                </View>

                <TouchableOpacity style={styles.jlgz}  onPress={()=>this.clickfourBtn(0)}>
                <Text
                    style={styles.common}
                >
                    奖励规则>

                </Text>
                </TouchableOpacity>
                <TouchableOpacity  style={styles.sjmx} onPress={()=>this.clickfourBtn(1)}>
                <Text
                    style={styles.common}
                >
                    善金明细>

                </Text>
                </TouchableOpacity>
                <Text
                    style={styles.qrshang}
                >
                    扫描二维码注册宗教APP完成邀请

                </Text>
                <Text
                    style={styles.qrxia}
                >
                    {/*（长按可保存二维码)*/}

                </Text>
                <View style={styles.qrcode}>
                <QRCode
                    size={200}
                    value={this.state.text}
                    bgColor='#e78136'
                    fgColor='white'/>
                </View>
                <Text
                    style={styles.zyh}
                >
                    宗教号:{nav.params.identifier}

                </Text>
                <TouchableOpacity  style={styles.yqfy} onPress={()=>this.clickfourBtn(2)}>
                <Text
                    style={styles.common}
                >
                    邀请缘众

                </Text>
                </TouchableOpacity>
                <TouchableOpacity  style={styles.yqjl} onPress={()=>this.clickfourBtn(3)}>
                <Text
                    style={styles.common}
                >
                    邀请记录>

                </Text>
                </TouchableOpacity>
            </View>
        )

    }



}

const styles = StyleSheet.create({

    container: {
        flex:1,
        backgroundColor:sys.grayColor
    },


    yuerViewStyle:{
        marginTop:10,
        marginLeft:0,
        height:100,
        backgroundColor:'white',
        alignItems:'center'
    },
    changeViewStyle:{
        marginTop:0,
        marginLeft:0,
        height:86,
        backgroundColor:'white'
    },
    yuerStyle:{
        fontSize:16,
        color:"#333333",
        textAlign: 'left',
        marginTop:15,
        marginLeft:15,
        width:sys.dwidth - 30,
        height:18
    },

    yuerNumStyle:{
        fontSize:35,
        color:"#eb7136",
        textAlign: 'center',
        width:sys.dwidth - 30,
        height:40,

    },

    jlgz:{
        marginTop:10,
        marginLeft:10,
        width:90,
        height:20,
    },
    sjmx:{
        marginTop:-20,
        marginLeft:sys.dwidth-110,
        width:90,

    },

    qrshang:{
        marginTop:25,
        width:sys.dwidth,
        textAlign:'center'
    },
    qrxia:{
        marginTop:10,
        width:sys.dwidth,
        textAlign:'center'
    },
    zyh:{
        marginTop:20,
        color:'#333333',
        fontSize:15,
        width:200,
        textAlign:'center',
        marginLeft:(sys.dwidth-200)/2
    },

    qrcode:{
        marginLeft:(sys.dwidth-200)/2,
        marginTop:10
    },
    yqfy:{
        marginTop:10,
        marginLeft:10,

        width:90,
        height:20

    },

    yqjl:{
        marginTop:-20,
        marginLeft:sys.dwidth-110,

        width:90,

    },


    contentIconeImage:{
        marginTop:5,
        //alignSelf:'center',
        //alignItems:'center',
        marginLeft:10,
        width:40,
        height:40
    },
    contentInfoStyles:{
        // flexDirection:'row',
        //   backgroundColor:'white',
        marginTop:5,
        marginLeft:10,
        //width:150,
        height:40
    },
    common:{
        fontSize:15,
        color:sys.mainColor,
    }


})



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
    Platform
} from 'react-native';

import {sys} from "../common/Data"

const host = sys.host;
import HttpUtils from "../common/HttpUtil"

import codePush from "react-native-code-push";



export default class Setup extends React.Component {


    static  navigationOptions = ({navigation}) => ({
        headerTitle:"设置",
        headerTitleStyle:{
            alignSelf:'center',
            flex: 1,
            textAlign: 'center',
    
        },
        headerRight: <View />
    });

    logout(){

        let codeurl = host + '/index/index/logout';
        let formData = new FormData();
        // 请求参数 ('key',value)
        HttpUtils.post(codeurl, formData)
            .then(result => {
                // if (result['respCode'] == 1) {

                //    RNLogin.imLogout((name) => {
                //         //alert(name[0])

                //     }, (err) => {
                //         //Alert.alert(err[0])
                //     });
                // } else {
                //     //Alert.alert("登陆失败", result['respMsg'])
                // }

            })
            .catch(error => {
                //Alert.alert("登陆失败" + JSON.stringify(error));
            })

        DeviceEventEmitter.emit('ChangeUI', {islogin:0});
        this.popBack();
    }

    clickThirdViewButton(num){

        switch (num){

            case 0:{
                this.props.navigation.navigate("EditUser");
                break;
            }
            case 1:{
                this.props.navigation.navigate("FeedBack");
                break;
            }
            case 2:{
                // this.props.navigation.navigate("Aboutus");
                break;
            }
            case 3:{
                if(Platform.OS=='android'){
                    this.updateVersion()
                }
            }


        }
    }

    updateVersion(){

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


    renderLoginedView(){
        return <View style={styles.container}>
            <View style={styles.thirdView}>
                <TouchableOpacity style={styles.bthirdView} onPress={()=>this.clickThirdViewButton(0)}>
                    <Text style={styles.tbuttontext}>编辑资料</Text>
                    <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity>
                <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.clickThirdViewButton(1)}>
                    <Text style={styles.tbuttontext}>反馈意见</Text>
                    <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>

                </TouchableOpacity>
                <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.clickThirdViewButton(2)}>
                    <Text style={styles.tbuttontext}>关于我们</Text>
                    <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity>
                <TouchableOpacity style={styles.bthirdViewtwo} onPress={()=>this.clickThirdViewButton(3)}>
                    <Text style={styles.tbuttontext}>版本v1.0.0</Text>
                    <Image style={styles.btimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity>

            </View>
            <TouchableOpacity style={styles.button} onPress={()=>this.logout()}>
                <Text style={styles.buttontext}>退出登录</Text>
            </TouchableOpacity>
         
        </View>
    }

    render() {
        return this.renderLoginedView()
    }

    popBack(){
        this.props.navigation.goBack()
    }
}

const styles = StyleSheet.create({

    container: {
        flex:1,
        justifyContent:'space-between',
        backgroundColor:sys.grayColor

    },
    bthirdView:{
        marginTop:10,
        flexDirection:'row',
        backgroundColor:'white',
        height:50
    },
    bthirdViewtwo:{
        marginTop:1,
        flexDirection:'row',
        backgroundColor:'white',
        height:50
    },
    thirdView: {
        backgroundColor: '#f2f2f2',
        width: sys.dwidth,
        // height:Platform.OS=='ios'?sys.dheight - 114:sys.dheight-130,
    },

    tbuttontext:{
        marginLeft:10,
        marginTop:17,
        width:100,
        fontSize:16
    },
    btimage:{
        marginTop:12.5,
        marginLeft:sys.dwidth-100-24-10,
        width:12,
        height:19
    },
    button:{

        backgroundColor:sys.mainColor,
        height:50,
        alignItems:'center',
        justifyContent:'center',
    },
    buttontext:{

        color:'#ffffff',
        textAlign:'center',
        fontSize:18,
    },
})


import React, {Component} from 'react';
import {
    View, Text, StyleSheet, ScrollView, Alert,Platform,
    Image, TouchableOpacity, NativeModules, Dimensions,AsyncStorage,DeviceEventEmitter,TextInput,ImageBackground
} from 'react-native';

import {sys} from "../common/Data"
const host = sys.host;
import HttpUtils from "../common/HttpUtil"
var ImagePicker = require('react-native-image-picker');
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'

import DialogSelected from '../common/AlertSelect';

export default class EditUser extends Component {



    static  navigationOptions = ({navigation}) => ({
        headerTitle:"编辑资料",
        headerTitleStyle:{
            alignSelf:'center',
            flex: 1,
            textAlign: 'center',
            
        },
        headerRight: <View />
    });

    constructor() {
        super();
        this.state = {

            head_pic:'http://baidu.com',
            nick_name:'',
            identifier:'',
            id:0,
            describe:'暂无签名',
            change_pic:'',
            visible:false,
            front_pic:'',
            front_cover:'1',
            isCover:0,

        };
        this.showAlertSelected = this.showAlertSelected.bind(this);
        this.callbackSelected = this.callbackSelected.bind(this);

    }


    componentDidMount(){

        storage.load('islogin',(userInfo)=>{

        })

        // storage.load({
        //     key: 'islogin',
        //     // autoSync(default true) means if data not found or expired,
        //     // then invoke the corresponding sync method
        //     autoSync: true,
        //     // syncInBackground(default true) means if data expired,
        //     // return the outdated data first while invoke the sync method.
        //     // It can be set to false to always return data provided by sync method when expired.(Of course it's slower)
        //     syncInBackground: true,

        // }).then(ret => {
        //     if (ret != '' && ret != null) {
        //         let data = JSON.parse(ret);
        //         this.setState({
        //             head_pic: data['head_pic'],
        //             front_cover:data['front_cover'],
        //             nick_name: data['name'],
        //             identifier: data['identifier'],
        //             describe: data['describe']
        //         });
        //     }
        // })
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



    showAlertSelected(isCover){
        this.state.isCover = isCover
        this.dialog.show("请选择照片", ['拍照','相册'], '#333333', this.callbackSelected);
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
    render() {
        return (<View style={styles.container}>

         
            <KeyboardAwareScrollView style={styles.thirdView}>


                  <TouchableOpacity style={styles.oneView}

                                    onPress={() => this.showAlertSelected(0)}
                  >
                      <Text style={styles.leftText}>头像</Text>
                      <Image style={styles.head_pic}
                                    source={this.state.change_pic?{uri:this.state.change_pic}:{uri:this.state.head_pic}} />
                      <Image style={styles.rightimage} source={require('./images/icon_path.png')}></Image>
                  </TouchableOpacity>


                <View style={styles.twoView} onPress={()=>this.clickThirdViewButton(1)}>
                    <Text style={styles.leftText}>昵称</Text>

                        <TextInput style={styles.inputText}
                               underlineColorAndroid='transparent'
                               placeholder={this.state.nick_name?this.state.nick_name:"请输入昵称"}
                               value={this.state.nick_name?this.state.nick_name:''}
                               onChangeText={(text) => this.setState({nick_name:text})}
                                >
                       </TextInput>

                </View>
                <View style={styles.twoView} onPress={()=>this.clickThirdViewButton(2)}>
                    <Text style={styles.leftText}>宗教号</Text>
                    <Text style={styles.rightText}>{this.state.identifier}</Text>
                </View>
                <View style={styles.twoView} onPress={()=>this.clickThirdViewButton(1)}>
                    <Text style={styles.leftText}>签名</Text>

                    <TextInput style={styles.inputText}
                        underlineColorAndroid='transparent'
                               numberOfLines={5}
                               multiline={true}
                        placeholder={this.state.describe?this.state.describe:"请输入签名"}
                        value={this.state.describe?this.state.describe:''}
                        onChangeText={(text) => this.setState({describe:text})}
                    >
                    </TextInput>

                </View>
                <TouchableOpacity style={styles.hedBgImg}  onPress={()=>this.showAlertSelected(1)}>
                    <ImageBackground style={styles.hedBgImg}
                                     source={{uri:this.state.front_pic?this.state.front_pic:this.state.front_cover}}>
                        <View>
                            <Image style={styles.uploadImage} source={require('./images/icon_upload.png')} />
                            <Text style={styles.uploadText}>给你的直播设置一个默认的封面</Text>
                        </View>
                    </ImageBackground>
                </TouchableOpacity>
            </KeyboardAwareScrollView>

            <TouchableOpacity onPress={() => this.updateMemberInfo()} style={styles.button}>
                <Text style={styles.buttontext}>保存</Text>
            </TouchableOpacity>
            <DialogSelected ref={(dialog)=>{
                this.dialog = dialog;
            }} />
        </View>);
    }

    updateMemberInfo(){

        let codeurl = host + '/member/index/updateMemberInfo';
        let formData = new FormData();
        // 请求参数 ('key',value)
        formData.append('head_pic', this.state.change_pic?this.state.change_pic:this.state.head_pic);
        formData.append('front_cover', this.state.front_pic?this.state.front_pic:this.state.front_cover);
        formData.append('name', this.state.nick_name);
        formData.append('describe',this.state.describe);

        HttpUtils.post(codeurl, formData)
            .then(result => {
                this.setState({
                    visible: false
                });

                if (result['respCode'] == 1) {

                        let resultdata = result['data'];
                        resultdata['islogin']='1'
                        //登陆成功时候发送通知
                        DeviceEventEmitter.emit('ChangeUI', resultdata);

                        this.props.navigation.goBack();

                } else {
                    Alert.alert("保存失败", result['respMsg'])
                }


            })
            .catch(error => {
                 Alert.alert("保存失败" + JSON.stringify(error));


            })
    }

    uploadImage(picpath) {

        this.setState({
            visible: true
        });


        let codeurl = host + '/index/index/uploadImage';
        let imageArr = [picpath];
        // 请求参数 ('key',value)
        HttpUtils.uploadImage(codeurl, imageArr)
            .then(result => {
                this.setState({
                    visible: false
                });

                if (result['respCode'] == 1) {
                    if(this.state.isCover == 1){
                        this.setState({
                            front_cover:result['data']
                        })
                    }else{
                        this.setState({
                            change_pic:result['data']
                        })
                    }


                } else {
                    

                }

            })
            .catch(error => {
               
                this.setState({
                    visible: false
                });

            })

    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent:'space-between',
        backgroundColor:sys.grayColor
    },
    head_pic:{
        width: 44,
        height: 44,
        resizeMode: 'cover',
        borderRadius:22,
        marginTop:3,
        marginLeft:sys.dwidth-200,
    },

    oneView:{
        marginTop:10,
        flexDirection:'row',
        backgroundColor:'white',
        height:50,
        justifyContent:'space-between'

    },
    twoView:{
        marginTop:1,
        flexDirection:'row',
        backgroundColor:'white',
        height:50
    },
    leftText:{
        marginLeft:10,
        marginTop:17,

        width:100,
        fontSize:16,
        // alignSelf: 'flex-start',
    },

    inputText:{

        marginLeft:sys.dwidth-280,
        marginTop:14,
        width:150,
        color:"#999999",
        textAlign:'right'
    },

    rightText:{
        marginLeft:sys.dwidth-280,
        marginTop:18,
        width:150,
        color:"#999999",
        textAlign:'right'
    },
    rightimage:{
        marginTop:12.5,
        marginRight:25,
        width:12,
        height:19,

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
    hedBgImg:{


        justifyContent:'center',
        width:sys.dwidth,
        height:sys.dwidth/3*2,
        backgroundColor:sys.mainColor

    },

    uploadImage:{

        width:70,
        height:70,
        marginLeft:sys.dwidth/2-35

    },

    uploadText:{
        color:sys.whiteColor,
        backgroundColor: 'rgba(52, 52, 52, 0.1)',
        textAlign:'center',
        marginTop:10
    },


});

import React, {Component} from 'react';
import {
    View, Text, StyleSheet, ScrollView, Alert,Platform,
    Image, TouchableOpacity, NativeModules, Dimensions,AsyncStorage,DeviceEventEmitter,TextInput
    ,ImageBackground
} from 'react-native';

import {sys} from "../common/Data"

const host = sys.host;
import HttpUtils from "../common/HttpUtil"

import ImagePicker from 'react-native-image-picker'
import PopupDialog from 'react-native-popup-dialog';


export default class AFAnchors extends Component {

    static  navigationOptions = ({navigation}) => ({
        headerTitle:"申请主播",
        headerTitleStyle:{
            alignSelf:'center',
            flex: 1,
            textAlign: 'center',
            
        },
        headerRight: <View />
    });

    componentDidMount(){

        this.getDataList();
    }


    getDataList(isReload){

        let codeurl = host + '/member/index/getMemberInfoById';
        let formData = new FormData();

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                this.setState({
                    host_state:result['data']['host_state']
                })
            })
            .catch(error=>{
                Alert.alert('提示：'+error+'')
            })

    }


    constructor() {
        super();
        this.state = {
            visible:false,
            real_name:'',
            name_dharma:'',
            identity_card_num:'',
            diplomatic_pic:'1',
            identity_card_face_hand :'1',
            aggree:false,
            upload_identify:'',
            upload_diplomatic:'',
            host_state:0
        };
    }

    renderImage(image) {
        return <Image style={styles.head_pic}
                      source={this.state.change_pic?{uri:this.state.change_pic}:{uri:this.state.head_pic}} />
    }

    renderAsset(image) {
        return this.renderImage(image);
    }


    pickSingle(pic){
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
        ImagePicker.launchImageLibrary(options, (response) => {
            console.log('Response = ', response);

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
                let source = { uri: response.uri };

                // You can also display the image using data:
                // let source = { uri: 'data:image/jpeg;base64,' + response.data };

                if(pic == 'identity_card_face_hand'){
                    this.setState({
                        identity_card_face_hand:source.uri
                    });
                    this.uploadImage('identity_card_face_hand',this.state.identity_card_face_hand)
                }else{
                    this.setState({
                        diplomatic_pic:source.uri
                    });
                    this.uploadImage('diplomatic_pic',this.state.diplomatic_pic)
                }
            }
        });
    }

    closePopu(){
        if(this.popupDialog){
            this.popupDialog.dismiss(() => {
                this.props.navigation.goBack()
            });
        }else{
            this.props.navigation.goBack()
        }

    }

    modifyApply(){

        let codeurl = host + '/member/index/getMemberInfoById';
        let formData = new FormData();

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                let data = result['data']
                this.setState({
                    real_name:data['real_name'],
                    name_dharma:data['name_dharma'],
                    identity_card_num:data['identity_card_num'],
                    diplomatic_pic:data['diplomatic_pic'],
                    identity_card_face_hand :data['identity_card_face_hand'],
                    aggree:false,
                    upload_identify:data['upload_identify'],
                    upload_diplomatic:data['upload_diplomatic'],
                })
            })
            .catch(error=>{
                Alert.alert('提示：'+error+'')
            })


        this.setState(
            {
                host_state:0
            }
        )
    }

    goWebView(){
        let url = host + "/index/index/hostProtocol"
        const {navigate} = this.props.navigation;
        navigate('WebViewScene', {uri: url,title:"宗教主播协议"});
    }

    unApplyView(){

        return (
        <View  style={styles.container}>
    
            <PopupDialog style={{justifyContent:'center',alignItems:'center'}}
                         ref={(popupDialog) => { this.popupDialog = popupDialog; }}
            >
                <View>
                    <Text style={{marginTop:100,
                        fontSize:18,
                        marginLeft:(sys.dwidth-300)/2,width:300,textAlign:'center'}}>您的资料已提交我们会尽快审核</Text>
                    <TouchableOpacity onPress={() => this.closePopu()}
                                      style={{backgroundColor:sys.mainColor,
                                          height:44,width:200,marginTop:100,
                                          justifyContent:'center',
                                          marginLeft:(sys.dwidth-200)/2
                                      }}>
                        <Text style={{color:'#ffffff',textAlign:'center',fontSize:18}}>我知道了</Text>
                    </TouchableOpacity>
                </View>
            </PopupDialog>
            <ScrollView>



                <TextInput style={styles.inputText}
                           underlineColorAndroid='transparent'
                           placeholder="请输入您的真实姓名"
                           value={this.state.real_name?this.state.real_name:''}
                           onChangeText={(text) => this.setState({real_name:text})}
                >
                </TextInput>
                <View style={styles.boldlineView}></View>
                <TextInput style={styles.inputText}
                           underlineColorAndroid='transparent'
                           placeholder={"请输入您的法号"}
                           value={this.state.name_dharma?this.state.name_dharma:''}
                           onChangeText={(text) => this.setState({name_dharma:text})}
                >
                </TextInput>
                <View style={styles.boldlineView}></View>
                {/*<TextInput style={styles.inputText}*/}
                {/*underlineColorAndroid='transparent'*/}
                {/*placeholder={"请输入您的手机号"}*/}
                {/*value={this.state.phone_num?this.state.phone_num:''}*/}
                {/*onChangeText={(text) => this.setState({phone_num:text})}*/}
                {/*>*/}
                {/*</TextInput>*/}
                {/*<View style={styles.boldlineView}></View>*/}
                <TextInput style={styles.inputText}
                           underlineColorAndroid='transparent'
                           placeholder="请输入您的身份证号"
                           value={this.state.identity_card_num?this.state.identity_card_num:''}
                           onChangeText={(text) => this.setState({identity_card_num:text})}
                >
                </TextInput>
                <View style={styles.boldlineView}></View>
                <TextInput style={styles.inputText}
                           editable={false}
                           underlineColorAndroid='transparent'
                           placeholder={this.state.nick_name?this.state.nick_name:"请上传手持身份证(正面)"}
                           value={this.state.nick_name?this.state.nick_name:''}
                           onChangeText={(text) => this.setState({nick_name:text})}
                >
                </TextInput>

                <TouchableOpacity
                    onPress={() => this.pickSingle('identity_card_face_hand')}
                >
                    <ImageBackground style={styles.hedBgImg} source={{uri:this.state.identity_card_face_hand}}>
                        <Image
                            style={styles.iconeImg}
                            source={require('./images/icon_camera.png')}/>

                        <Text style={styles.nametext}>点击上传手持身份证照片</Text>
                        {this.renderAsset(this.state.image)}
                    </ImageBackground>
                </TouchableOpacity>
                <View style={styles.boldlineView}></View>
                <TextInput style={styles.inputText}
                           editable={false}
                           underlineColorAndroid='transparent'
                           placeholder={this.state.nick_name?this.state.nick_name:"请上传戒牒照(展开正面)"}
                           value={this.state.nick_name?this.state.nick_name:''}
                           onChangeText={(text) => this.setState({nick_name:text})}
                >
                </TextInput>
                <TouchableOpacity
                    onPress={() => this.pickSingle('diplomatic_pic')}
                >
                    <ImageBackground style={styles.hedBgImg1} source={{uri:this.state.diplomatic_pic}}>
                        <Image
                            style={styles.iconeImg}
                            source={require('./images/icon_camera.png')}/>
                        <Text style={styles.nametext}>点击上传戒牒照片</Text>
                    </ImageBackground>
                </TouchableOpacity>
                <View style={styles.greementView}>
                    <TouchableOpacity  onPress={()=>this.aggree()}>
                        <Image
                            style={styles.header_button}
                            source={this.state.aggree?require('./images/icon_selected.png'):require('./images/icon_unselected.png')} />
                    </TouchableOpacity>
                    <Text style={styles.agreeText1}>已阅读并同意</Text>
                    <TouchableOpacity style={styles.userAgreement} onPress={()=>this.goWebView()}>
                        <Text style={styles.agreeText2}>{"<<主播协议>>"}</Text>
                    </TouchableOpacity>
                </View>
            </ScrollView>

            <TouchableOpacity onPress={() => this.updateMemberInfo()} style={styles.button}>
                <Text style={styles.buttontext}>提交</Text>
            </TouchableOpacity>
        </View>)


    }

    appledView(){

        if(this.state.host_state == 1){
            return (
                <View  style={styles.container}>

                        <View style={{alignItems:'center',height:300}}>
                            <Image
                                style={{width:90,height:90,marginTop:30,resizeMode:'contain'}}
                                source={require('./images/icon_buddha.png')}/>
                            <Text style={{marginTop:20,
                                fontSize:18,
                                marginLeft:10,
                                width:300,
                                alignSelf:'flex-start',
                                textAlign:'left'}}>申请主播资料已提交</Text>

                            <Text style={styles.subTitleText}>1.审核时间:工作人员会在一个工作日内通过宗教客服
                                热线联系您与您进行核实，请保存您的电话通畅;
                            </Text>
                            <Text style={styles.subTitleText}>1.审核通知:审核通过我们将会已宗教客服热线通知，
                                若审核未通过请重新提交资料;
                            </Text>

                            <TouchableOpacity onPress={() => this.closePopu()}
                                              style={{backgroundColor:sys.mainColor,
                                                  height:44,width:200,marginTop:20,
                                                  justifyContent:'center',
                                                  marginLeft:(sys.dwidth-200)/2-50
                                              }}>
                                <Text style={{color:'#ffffff',textAlign:'center',fontSize:18}}>我知道了</Text>
                            </TouchableOpacity>
                        </View>


                </View>
            )
        }else if(this.state.host_state == 2){

            return (
                <View  style={styles.container}>

                        <View style={{alignItems:'center',height:300}}>
                            <Image
                                style={{width:90,height:90,marginTop:15,resizeMode:'contain'}}
                                source={require('./images/icon_buddha.png')}/>
                            <Text style={{marginTop:30,
                                fontSize:18,
                                width:300,textAlign:'center'}}>恭喜你申请成功
                                </Text>

                            <TouchableOpacity onPress={() => this.closePopu()}
                                              style={{backgroundColor:sys.mainColor,
                                                  height:44,width:200,marginTop:60,
                                                  justifyContent:'center',
                                                  marginLeft:(sys.dwidth-200)/2-50
                                              }}>
                                <Text style={{color:'#ffffff',textAlign:'center',fontSize:18}}>我知道了</Text>
                            </TouchableOpacity>
                        </View>


                </View>
            )

        }else if(this.state.host_state == 3){
            return( <View  style={styles.container}>

                <View style={{alignItems:'center'
                    ,height:300,
                    }}>
                    <Image
                        style={{width:90,height:90,marginTop:30,resizeMode:'contain'}}
                        source={require('./images/icon_buddha.png')}/>
                    <Text style={{marginTop:20,
                        fontSize:18,
                        width:300,
                        textAlign:'center'}}>很抱歉您的申请失败</Text>

                    <Text style={styles.subTitleText}>您的身份信息与戒牒信息不符合
                    </Text>
                    <TouchableOpacity onPress={() => this.modifyApply()}>
                    <Text style={styles.modifyText}>修改申请
                    </Text>
                    </TouchableOpacity>

                    <TouchableOpacity onPress={() => this.closePopu()}
                                      style={{backgroundColor:sys.mainColor,
                                          height:44,width:200,marginTop:20,
                                          justifyContent:'center',
                                          marginLeft:(sys.dwidth-200)/2-50
                                      }}>
                        <Text style={{color:'#ffffff',textAlign:'center',fontSize:18}}>我知道了</Text>
                    </TouchableOpacity>
                </View>


            </View>)
        }



    }

    render() {
        if(this.state.host_state == 0){
            return this.unApplyView()
        }else {
            return this.appledView()
        }


    }

    aggree(){
        this.setState({
            aggree: !this.state.aggree
        })
    }

    updateMemberInfo(){

        if(!this.state.aggree){
            Alert.alert("请先同意主播协议");
            return;
        }

        if(this.state.real_name.length<1){
            Alert.alert("请填写真实姓名");
            return;
        }
        if(this.state.name_dharma.length<1){
            Alert.alert("请填写真实法号");
            return;
        }
        if(this.state.identity_card_num.length<1){
            Alert.alert("请填写真实身份证号");
            return;
        }
        if(this.state.upload_identify.length<1){
            Alert.alert("请上传手持身份证照片");
            return;
        }
        if(this.state.upload_diplomatic<1){
            Alert.alert("请上传牒照");
            return;
        }
        this.setState({
            visible: !this.state.visible
        });

        let codeurl = host + '/member/index/applyHost';
        let formData = new FormData();
        // 请求参数 ('key',value)
        formData.append('real_name', this.state.real_name);
        formData.append('name_dharma', this.state.name_dharma);
        formData.append('identity_card_num',this.state.identity_card_num);
        formData.append('diplomatic_pic', this.state.diplomatic_pic);
        formData.append('identity_card_face_hand',this.state.identity_card_face_hand);
        HttpUtils.post(codeurl, formData)
            .then(result => {
                if (result['respCode'] == 1) {

                    let resultdata = result['data'];
                    this.popupDialog.show();

                } else {
                    Alert.alert("申请失败", result['respMsg'])
                }
                this.setState({
                    visible: !this.state.visible
                });

            })
            .catch(error => {
                Alert.alert("申请失败" + JSON.stringify(error));
                this.setState({
                    visible: !this.state.visible
                });
            })
    }

    uploadImage(pic,imagepath) {


        let codeurl = host + '/index/index/uploadImage';
        let imageArr = [imagepath];
        // 请求参数 ('key',value)
        HttpUtils.uploadImage(codeurl, imageArr)
            .then(result => {
                if (result['respCode'] == 1) {
                    if(pic == 'identity_card_face_hand'){
                        this.state.upload_identify = result['data'];
                    }else{
                        this.state.upload_diplomatic = result['data'];
                    }
                } else {
                    Alert.alert("修改头像失败", result['respMsg'])
                }

            })
            .catch(error => {
                Alert.alert("修改头像失败" + JSON.stringify(error));
            })

    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor:'#ffffff',
        alignItems:'center',
        justifyContent:'center'
    },
    hedBgImg:{
        width:sys.dwidth-28,
        height:(sys.dwidth - 28)*220/340,
        backgroundColor:'#666666',
        alignItems:'center',
        justifyContent:'center',
        marginLeft:14,
        marginBottom:10
    },
    hedBgImg1:{
        width:sys.dwidth-28,
        height:(sys.dwidth - 28)*312/347,
        backgroundColor:'#666666',
        alignItems:'center',
        justifyContent:'center',
        marginLeft:14,
        marginBottom:15
    },
    iconeImg:{
        width: 50,
        height: 48,
        resizeMode: 'center',
        borderRadius:22,
    },
    inputText:{
        height:44,
        marginLeft:14,
        width:sys.dwidth-14,
        color:"#333333",
        backgroundColor:'#ffffff'
    },
    boldlineView:{
        backgroundColor:"#f2f2f2",
        height:5
    },
    nametext:{
        marginTop:10,
        color:"#ffffff"
    },
    buttontext:{
        paddingTop:10,
        color:'#ffffff',
        textAlign:'center',
        fontSize:18,
    },
    button:{
        backgroundColor:sys.mainColor,
        height:50,
        width:sys.dwidth,
        alignItems:'center',
        justifyContent:'center',
    },
    loading:{
        width:100,
        height:100,
        resizeMode:'center'
    },
    greementView: {
        marginTop:10,
        flexDirection: 'row',
        paddingLeft:10,
        marginBottom:20
    },
    agreeText1:{
        marginLeft:10,
        color:"#999999",
        fontSize:12
    },
    agreeText2:{
        marginLeft:10,
        color:sys.mainColor,
        fontSize:12
    },
    titleText:{
        fontSize:15
    },
    subTitleText:{
        marginTop:10,
        fontSize:12,
        marginLeft:10,
        marginRight:10,
        color:'#999999',
    },
    modifyText:{
        marginTop:10,
        fontSize:12,
        marginLeft:10,
        marginRight:10,
        color:sys.mainColor
    }


});

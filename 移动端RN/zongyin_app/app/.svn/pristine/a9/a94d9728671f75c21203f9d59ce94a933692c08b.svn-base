import React, {Component} from 'react';
import {
    View, Text, StyleSheet, ScrollView, Alert,Platform,
    Image, TouchableOpacity, NativeModules, Dimensions,AsyncStorage,
    DeviceEventEmitter,TextInput,ActivityIndicator,Keyboard
} from 'react-native';

import {sys,NoDataView} from "../common/Data"
const host = sys.host;
import HttpUtils from "../common/HttpUtil"
var ImagePicker = require('react-native-image-picker');
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import Toast,{DURATION} from 'react-native-easy-toast';

import DialogSelected from '../common/AlertSelect';


var self = this;
export default class WithdrawCash extends Component {



    // static  navigationOptions = ({navigation}) => ({

    //     headerTitle:"提现",

    //     headerTitleStyle:{
    //         alignSelf:'center',
    //         flex: 1,
    //         textAlign: 'center',
    //         fontSize:18
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
                headerTitle:"提现",

                headerTitleStyle:{
                    alignSelf:'center',
                    flex: 1,
                    textAlign: 'center',
                    fontSize:18
                },
                headerRight: <View />,
                headerLeft:leftView
            }

        }  

        return {
            headerTitle:"提现",

            headerTitleStyle:{
                alignSelf:'center',
                flex: 1,
                textAlign: 'center',
                fontSize:18
            },
            headerRight: <View />
        }
    };




    constructor() {
        super();
        this.state = {
            image: null,
            head_pic:'http://baidu.com',
            fubi:0,
           // identifier:'',
            tixiCount:'',
            id:0,
            // describe:'暂无签名',
            tikuannumoverbilv:'',
            ximaNum:'',
            change_pic:'',
            visible:false,
            drawcash:'',
            user:{},
            bank_card_number:"",
            bank_card_bankaddress:'',
            bank_name:"123",
            balanceNum:"0",
            tikuanPsw:"",
            bankId:"",
            accountname:"",
            isTiXian:false,

            isloading:false,
            styleNum:0,

        };


    }

    rightViewClick(){

        const {navigate} = this.props.navigation;
        this.props.navigation.navigate("DrawCashHistory",{user:this.state.user});
    }


    componentDidMount(){
        self = this
        DeviceEventEmitter.addListener('updatebank',(dic)=>{
                this.getPersonInfo(0)
        });
        this.getPersonInfo(0)
    }
    componentWillUnmount(){
        DeviceEventEmitter.removeListener('updatebank');
    }

    getPersonInfo(num){

        //let codeurl = host + '/member/index/getMemberInfoById';
        let codeurl = host + '/AppAccount.withdrawalsSetting.do'// '/AppMember.index.do';
        let formData = new FormData();

        this.setState({
            isloading:true
        })

        HttpUtils.post(codeurl,formData)
            .then(result=>{

                console.log('ppp')
                console.log(result)

                let data = result['data'];
                this.setState({
                       
                        // head_pic: data['head_pic'],
                        balanceNum: data['balance'],
                        // identifier: data['identifier'],
                        tixiCount:data['count'],
                        // describe: data['describe'],
                        tikuannumoverbilv:data['tikuannumoverbilv'],
                        ximaNum:data['xima'],
                        isloading:false

                    });
               
                if (data['banklist'] == false) {

                    this.setState({
                        styleNum:1
                    })
                } else {
                    yinghanglist=data['banklist']['0'];
                
                    if (yinghanglist != false) {
                        this.setState({
                            bank_name:yinghanglist['bankname'],
                            bank_card_number:yinghanglist['banknumber'],
                            bank_card_bankaddress:yinghanglist['bankaddress'],
                            bankId:yinghanglist['id'],
                            accountname:yinghanglist['accountname'],
                            styleNum:2
                        })
                    }
                } 

                
            })
            .catch(error=>{
                // Alert.alert('提示：'+error+'')
                if(num<=0){
                    this.getPersonInfo(++num);
                }

                this.setState({
                    isloading:false,
                })

            })

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
                    let source = { uri: response.uri };
                    // You can also display the image using data:
                    // let source = { uri: 'data:image/jpeg;base64,' + response.data };
                    this.setState({
                        change_pic:source.uri
                    });
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
                let source = { uri: response.uri };
                // You can also display the image using data:
                // let source = { uri: 'data:image/jpeg;base64,' + response.data };
                this.setState({
                    change_pic:source.uri
                });
            }
        });

    }


    bankCardSucss(bankName,bankNum){

    }


    bankCard(){

        const {navigate} = this.props.navigation;
        this.props.navigation.navigate("BankCard",{user:this.state.user,bankCardSucss:(bankName,bankNum)=>{

          this.getPersonInfo();
        //this.setState({bank_name:bankName,bank_card_number:bankNum,banklist:["123"]});
            
        }
        });
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


        
      
       
        // let hedView =this.state.banklist?this.bankCardView():this.addBankCardView();

        let hedView=null;

        var tixiMoney = ''

        if (this.state.ximaNum<=0) {
            tixiMoney = this.state.balanceNum
        } else {
            tixiMoney='0(剩余洗码'+this.state.ximaNum +')'
        }

        if (this.state.styleNum==1) {


            // console.log('111111111')

            hedView = this.addBankCardView();
        }

        if (this.state.styleNum==2) {

            // console.log('222222222')

            hedView = this.bankCardView();
        }


        return (this.state.styleNum==0?<NoDataView click={()=>this.getPersonInfo()}
        isloading={this.state.isloading}
        text = {this.state.isloading?"加载中":'网络错误，点击重新加载'}
/>:<KeyboardAwareScrollView
            style={styles.container}
            keyboardShouldPersistTaps = {"always"}

        >
            <View style={styles.thirdView}>
               {hedView}
                <View style={[styles.lineView,{marginTop:10}]}></View>
                <Text style={styles.leftText}>提现数额</Text>


                <View style={styles.twoView}>


                    <TextInput style={styles.inputText}
                               underlineColorAndroid='transparent'
                               placeholder={"可提现金额"+tixiMoney}
                               value={this.state.drawcash}
                               onChangeText={(text) => this.setState(
                                   {
                                       drawcash:text

                                   }
                                   )}
                               ref={(c) => this._cashInput = c}
                    >

                    </TextInput>
                    <TouchableOpacity  style={styles.drawCashView} onPress={()=>this.clickDraw()}>
                    <Text style={styles.drawCash}>全部提现</Text>
                    </TouchableOpacity>
                </View>

                <View style={styles.lineView2}></View>

                <TextInput
                    underlineColorAndroid='transparent'
                    secureTextEntry
                    style={styles.textIp}
                    placeholder="提款密码"
                    onChangeText={(text) => this.setState({tikuanPsw:text})}
                />
                <View style={{
                    height:1,//TODO这里小于0.8没显示出来
                    backgroundColor:sys.grayColor,
                    marginTop:0,
                    marginLeft:12,
                    width:sys.dwidth-24,
                }}></View>


                
                <Text style={styles.tipText}>温馨提示：现仅可提现整百人民币数额，如有不便敬请谅解！</Text>
            </View>


            <TouchableOpacity disabled={this.state.isTiXian} onPress={() => this.updateMemberInfo()} style={styles.button}>
            
                {this.state.isTiXian?<ActivityIndicator />:null}
                <Text style={styles.buttontext}>{this.state.isTiXian?'申请中..':'申请提现'}</Text>
            </TouchableOpacity>
            <DialogSelected ref={(dialog)=>{
                this.dialog = dialog;
            }} />

            <Toast  //提示
                    ref="toast"
                    style={{backgroundColor:'gray'}}
                    position='center'
                    positionValue={200}
                    opacity={0.7}
                    textStyle={{color:'white'}}
                /> 
        </KeyboardAwareScrollView>
        
        );
    }


    addBankCardView() {

        return <View>

             <TouchableOpacity style={styles.oneView}
                                  onPress={() => this.bankCard()}
                >
                    {/* <Image style={styles.head_pic}
                           source={require('./images/icon_buddha.png')} /> */}
                    <Text style={styles.leftText}>添加银行卡 </Text>
                    <Image style={styles.rightimage} source={require('./images/icon_path.png')}></Image>
                </TouchableOpacity>
                </View>
    }


    bankCardView() {
        
        return <View>

                <View style={styles.bankCardView}>
                    <Text style={styles.newLeftText}>持卡人</Text>
                </View>
                <View style={styles.bankCardView}>
                
                    {/* <Image style={styles.head_pic}
                            source={require('./images/icon_buddha.png')} /> */}
                 

                        <Text style={styles.newLeftText}>
                        {this.state.accountname}
                        </Text> 
                </View>
                <View style={styles.lineView}></View>
            <View style={styles.bankCardView}>
                
                    {/* <Image style={styles.head_pic}
                            source={require('./images/icon_buddha.png')} /> */}
            
                        <Text style={styles.newLeftText}>
                        {this.state.bank_name}
                        </Text>
                </View>
                <View style={styles.bankCardView}>
              
                    {/* <Image style={styles.head_pic}
                            source={require('./images/icon_buddha.png')} /> */}
                 
                        <Text style={styles.newLeftText}>
                        {this.state.bank_card_number}
                        </Text>              
                </View>


                <View style={styles.lineView}></View>

                <View style={[styles.bankCardView,{marginTop:10}]}>
              
                  <Text style={styles.newLeftText}>剩余免费提现次数：<Text style={{color:sys.mainColor}}>{this.state.tixiCount}</Text>次
                  </Text>              
                </View>

                 <View style={[styles.bankCardView,{marginTop:10}]}>
              
                 <Text style={styles.newLeftText}>提现手续费：<Text style={{color:sys.mainColor}}>{this.state.tikuannumoverbilv}</Text>%</Text>             
                </View>



            </View>
    }




    clickDraw(){


        let tixianNum=0;
        if (this.state.ximaNum<=0) {
            tixianNum = this.state.balanceNum
        }
        this.setState({
            drawcash:tixianNum+""
        })
    }

    updateMemberInfo(){



        Keyboard.dismiss()

        if(this.state.bankId.length <= 0){

            this.refs.toast.show("请先添加银行卡", DURATION.LENGTH_LONG);
            return;
        }

        if(this.state.drawcash.length <= 0){
            this.refs.toast.show("请输入提现金额", DURATION.LENGTH_LONG);
         
            return;
        }
        if(this.state.tikuanPsw.length <= 0){

            this.refs.toast.show("请输入提现密码", DURATION.LENGTH_LONG);
            return;
         }

        this.setState({
            isTiXian:true,
        })


       // let codeurl = host + '/fubi/index/applyWithdrawalFubi';

        let codeurl = host + '/AppApijiekou.savetikuanorder.do';

        

        let formData = new FormData();
        // 请求参数 ('key',value)
       // formData.append('fubi', this.state.drawcash);
        formData.append('bankid', this.state.bankId);
        formData.append('amount', this.state.drawcash);
        formData.append('withdraw_pwd', this.state.tikuanPsw);
        // formData.append('tradepassword', this.state.tikuanPsw);
        formData.append('accountname', this.state.accountname);


        HttpUtils.post(codeurl, formData)
            .then(result => {
                if (result['respCode'] == 1) {

                    this.refs.toast.show("您的申请已提交", DURATION.LENGTH_LONG);
                    result['islogin']=1
                    //登陆成功时候发送通知
                    DeviceEventEmitter.emit('ChangeUI', result);
                    this.props.navigation.goBack();

                } else {
  
                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                }
                this.setState({
                    visible: false,
                    isTiXian:false,
                });


            })
            .catch(error => {
                this.refs.toast.show(error, DURATION.LENGTH_LONG);
    
                this.setState({
                    visible: false,
                    isTiXian:false,
                });

            })
    }

    uploadImage() {

        // this.setState({
        //     visible: !this.state.visible
        // });

        if(this.state.change_pic == ''){
            this.updateMemberInfo()
            return
        }

        let codeurl = host + '/index/index/uploadImage';
        let imageArr = [this.state.change_pic];
        // 请求参数 ('key',value)
        HttpUtils.uploadImage(codeurl, imageArr)
            .then(result => {
                if (result['respCode'] == 1) {
                    this.state.change_pic = result['data'];
                    this.updateMemberInfo()
                } else {
      
                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                    this.setState({
                        visible: false
                    });

                }

            })
            .catch(error => {
  
                this.refs.toast.show(JSON.stringify(error), DURATION.LENGTH_LONG);
                this.setState({
                    visible: false
                });

            })

    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,

        backgroundColor:sys.whiteColor
    },


    bankCardView:{
        marginTop:8,
        flexDirection:'row',
        height:25,
    },

    oneView:{
        marginTop:10,
        flexDirection:'row',
        height:50,
    },

    head_pic:{
        width: 30,
        height: 30,
        resizeMode: 'cover',
        borderRadius:15,
        marginTop:10,
        marginLeft:10,
    },
    leftText:{
        marginLeft:10,
        marginTop:17,
        color:sys.titleColor,
        width:120,
        fontSize:16,
    },

    newLeftText:{
        marginLeft:10,
        marginTop:0,
        color:sys.titleColor,
        width:sys.dwidth - 10,
        fontSize:16,
    },

    drawCash:{
      color:sys.mainColor,
      fontSize:14
    },
    drawCashView:{
        marginLeft:sys.dwidth-70,
        marginTop:-30,
        height:30,
    },

    inputText:{

        marginLeft:10,
        marginTop:14,
        width:sys.dwidth-100,
        color:sys.titleColor,
        textAlign:'left',
        fontSize:16,
        height:50,

    },


    rightimage:{
        marginTop:12.5,
        marginRight:25,
        width:12,
        height:19,
     //   marginLeft:sys.dwidth-160-32

        marginLeft:sys.dwidth-160-2

    },
    button:{
        flexDirection:'row',
        backgroundColor:sys.mainColor,
        height:50,
        marginLeft:20,
        alignItems:'center',
        width:sys.dwidth-40,
        justifyContent:'center',
        marginTop: 10,
        borderRadius:5,
    },
    buttontext:{
        color:'#ffffff',
        textAlign:'center',
        fontSize:18,
    },
    lineView:{
        height:1,//TODO这里小于0.8没显示出来
        backgroundColor:sys.grayColor,
        marginTop:0,
        marginLeft:12,
        width:sys.dwidth-24,
    },

    lineView2:{
        height:1,//TODO这里小于0.8没显示出来
        backgroundColor:sys.grayColor,
        marginTop:0,
        marginLeft:12,
        width:sys.dwidth-24,
    },

    thirdView: {

        height: sys.dheight-180,
        width: sys.dwidth,
    },
    tipText:{
        marginTop:15,
        color:sys.subTitleColor,
        marginLeft:10,
        width:sys.dwidth-20
    
    },


    textIp:{
        marginTop:0,
        marginLeft:12,
        width:sys.dwidth-40,
        height:40
        },

});

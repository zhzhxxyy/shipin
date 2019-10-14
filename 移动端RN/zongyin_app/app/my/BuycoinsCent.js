
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
    ScrollView,
    Platform, DeviceEventEmitter,
    ActivityIndicator,
    Keyboard
} from 'react-native';

import {sys} from "../common/Data"
const host =  sys.host;

import HttpUtils from "../common/HttpUtil"
import {RefreshState} from 'react-native-refresh-list-view'
import {NativeModules} from 'react-native';


import Toast,{DURATION} from 'react-native-easy-toast';//导入弹出框组件

import {RadioGroup, RadioButton} from 'react-native-flexi-radio-button'

import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'



var self;
export default class BuycoinsCent extends React.Component {



 
    // static  navigationOptions = ({navigation}) => ({
    //     headerTitle:"充值",
    //     headerTitleStyle:{
    //         alignSelf:'center',
    //         flex: 1,
    //         textAlign: 'center',
            
    //     },
    

    //     headerRight: <TouchableOpacity onPress={()=>self.rightViewClick()}>
    //     <Text style={{color:sys.titleColor,marginRight:8}}>客服</Text>
    // </TouchableOpacity>,

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
                headerTitle:"充值",
                headerTitleStyle:{
                    alignSelf:'center',
                    flex: 1,
                    textAlign: 'center',
                    
                },
            

                headerRight: <TouchableOpacity onPress={()=>self.rightViewClick()}>
                <Text style={{color:sys.titleColor,marginRight:8}}>客服</Text>
                </TouchableOpacity>,
                headerLeft:leftView
            }

        }  

        return {
            headerTitle:"充值",
            headerTitleStyle:{
                alignSelf:'center',
                flex: 1,
                textAlign: 'center',
                
            },
        

            headerRight: <TouchableOpacity onPress={()=>self.rightViewClick()}>
            <Text style={{color:sys.titleColor,marginRight:8}}>客服</Text>
            </TouchableOpacity>,
        }
    };
    

    constructor(props) {
        super(props)

        this.state = {
            isSubmit:0,
            czMoney:"",
            orderNum:"",
            fshuCode:"",
            dataList: [],
            refreshState: RefreshState.HeaderRefreshing,
            currentPage:1,
            pageSize:20,
            showLoading:false,
            fubi:{},
            headfubi:0,
            content:'正在购买',

            blance:"",
            zfNum:"",
            list:[
                {
                    index:1,
                    radioList: [
                        {
                         "id":"1",
                         "name":"支付宝",
                        },
                        {
                         "id":"2",
                         "name":"微信",
                        },
                         {
                         "id":"3",
                         "name":"网银",
                        }
                    ]
                }
            
            ],
            textWay:"0",
            isTiJiao:false,

            erweimaStr:''
        }
    }

    rightViewClick(){
        // const {navigate} = this.props.navigation;
        // navigate('WebViewScene', {uri: {content:'https://vs28.verifiedsafesite.com/chat/Hotline/chatbox.jsp?companyID=5004615&jid=3419858553&configID=3135&s=1'}});
        //打开客服聊天

        Keyboard.dismiss()

        var item={
            id: 1,
            typeid: null,
            title: "客服",
            ftitle: "客服",
            pic:'',
            name:'客服'
        };
        item['userInfo']=null;
        item['token']="";
        this.props.navigation.navigate("Chat",{item:item});
    }



    getPersonInfo(){

        let codeurl = host + '/member/index/getMemberInfoById';
        let formData = new FormData();

        HttpUtils.post(codeurl,formData)
            .then(result=>{

                let dic = result['data'];

                this.setState({
                    headfubi:dic['fubi']?dic['fubi']:'0.00',

                });
                this._header()
            })
            .catch(error=>{
                //Alert.alert('提示：'+error+'')
            })

    }

    componentDidMount(){


        // //注册通知
        // DeviceEventEmitter.addListener('Login',(dic)=>{
        //     //接收到详情页发送的通知，刷新首页的数据，改变按钮颜色和文字，刷新UI
        //     const {navigate} = this.props.navigation;
        //     this.props.navigation.navigate("Login",{name:"zhengsan"});
        // });

        // this.onHeaderRefresh()

    }

    onHeaderRefresh = () => {
        this.state.currentPage = 1;
        this.setState(
            {
                refreshState: RefreshState.HeaderRefreshing,
            })
        //获取测试数据
        if(this.state.refreshState !=RefreshState.FooterRefreshing) this.getDataList(true)

    }


    getDataList(isReload){

        this.getPersonInfo()
        let codeurl = host + '/fubi/index/getFubiProductList';
        let formData = new FormData();

        // 请求参数 ('key',value)
        formData.append('page',this.state.currentPage);
        formData.append('page_size',this.state.pageSize);

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                let testData = result['data']['rows'];
                let newList = testData.map((data) => {
                    return {
                        fubi:data["fubi"],
                        fubi_present:data["fubi_present"],
                        rmb:data["rmb"],
                        id:data["id"],
                    }
                });

                let dataList =  isReload ? newList : [...this.state.dataList, ...newList]
                let state = dataList.length > result['data']['total'] ? RefreshState.NoMoreData : RefreshState.Idle;
                if(isReload){
                    state = RefreshState.Idle;
                }
                this.setState({
                    dataList: dataList,
                    refreshState: dataList.length >= result['data']['total'] ? RefreshState.NoMoreData : RefreshState.Idle,
                })

            })
            .catch(error=>{
            
                this.refs.toast.show("获取产品失败", DURATION.LENGTH_LONG);

            })
    }


    onSelect(index, value){

        this.state.textWay = index;
    }

    render() {
        self = this;
        return(this.state.isSubmit?this.renderSubmitView():this.renderNotSubmitView())
    }

    keyExtractor = (item, index) => {
        return index
    }



    renderNotSubmitView() {

        let attributes=[];
        let list = this.state.list;
        // console.log("0000000");
        list.map((item,i)=>{
            attributes.push(
            <View key={i}>
                <View >
                <RadioGroup style={styles.radioView}
                        onSelect = {(index, value) => this.onSelect(index, value)}
                        size={20}
                        selectedIndex={0}
                        thickness ={2}>
                        {
                          item.radioList.map((elem,radioIndex) => {
                              return (

                                  <RadioButton style={{marginTop:8,width:(sys.dwidth-100)/3}} key={radioIndex} value={elem.id} >
                                        <Text style={{fontSize:13,marginTop:-2}}>{elem.name}</Text>
                                 </RadioButton>
                                
                              )
                          })
                        }
                </RadioGroup>

                {/* <Text>{this.state.text}</Text> */}
            </View>
            </View>
            )
        })



        // <TouchableOpacity disabled={this.state.iscommit} style={styles.button} onPress={()=>this.login()}>
        //             <Text style={styles.buttontext}>{this.state.iscommit?'登录中..':'登录'}</Text>
        //         </TouchableOpacity>


        // let bootmView = <TouchableOpacity disabled={this.state.isTiJiao} style={styles.button} onPress={()=>this.tijiao()}>
        //      {this.state.isTiJiao?<ActivityIndicator />:null}
        //      <Text style={styles.buttontext}>{this.state.iscommit?'提交中..':'提交'}</Text>
        //  </TouchableOpacity>


      return <KeyboardAwareScrollView style={styles.container}>
          
              <View style={styles.textInputView}>
                  <Text style={[styles.leftText,{marginTop:14}]}>充值金额</Text>

                  <TextInput style={styles.inputText}
                             ref={(c) => this.textInput = c}
                             underlineColorAndroid='transparent'
                             placeholder={"最低充值10"}
                            // value={this.state.phone?this.state.phone:''}
                             onChangeText={(text) => this.setState({blance:text})}
                  >
                  </TextInput>
              </View>

              <View style={styles.textInputView}>
                  <Text style={[styles.leftText,{marginTop:14}]}>支付账号</Text>

                  <TextInput style={styles.inputText}
                             ref={(c) => this.textInput = c}
                             underlineColorAndroid='transparent'
                             placeholder={"请输入你的支付账号"}
                           //  value={this.state.phone?this.state.phone:''}
                             onChangeText={(text) => this.setState({zfNum:text})}
                  >
                  </TextInput>
              </View>

              <View style={styles.textInputView}>

                  <Text style={styles.leftText}>充值方式</Text>
              
                  <View>
                  {attributes}
                  </View>
              </View>
              
              <TouchableOpacity disabled={this.state.isTiJiao} style={styles.button} onPress={()=>this.tijiao()}>
             {this.state.isTiJiao?<ActivityIndicator />:null}
             <Text style={styles.buttontext}>{this.state.isTiJiao?'提交中..':'提交'}</Text>
             </TouchableOpacity>
              {/* <TouchableOpacity style={styles.button} onPress={()=>this.tijiao()}>
                  <Text style={styles.buttontext}>提交</Text>
              </TouchableOpacity> */}


              <View style={styles.cotentView}>

                  <Text style={styles.contentText}>1.点击右上角客服，向在线客服咨询您需要的充值方式（微信，支付宝或者网银账号）；</Text>

                  <Text style={styles.contentText}>使用您向客服咨询到的充值方式进行充值；</Text>

                  <Text style={styles.contentText}>充值成功以后回到此页面；</Text>

                  <Text style={styles.contentText}>填写你已经支付成功的金额，支付账号，支付方式并确定提交；</Text>

                  <Text style={styles.contentText}>稍等片刻，刷新您的余额即可到账。；</Text>
          
              </View>

              <View style={{height:10,backgroundColor:'#FFF8DC',marginTop:0, marginLeft:10, marginRight:10}}/>

              <Toast  //提示
                    ref="toast"
                    style={{backgroundColor:'gray'}}
                    position='center'
                    positionValue={200}
                    opacity={0.6}
                    textStyle={{color:'white'}}
                />  
          </KeyboardAwareScrollView>
    }



    renderSubmitView() {


        //  self.state.erweimaStr =  sys.host + '/AppPublic.shareAddress.do'

        // self.state.erweimaStr=null
        let imgView=null

        if (self.state.erweimaStr !='' && self.state.erweimaStr != null) {
            imgView= <View style={{flexDirection:'row'}}>
                 
            <Text style={{marginTop:0,marginLeft:10,height:sys.dwidth/2,width:sys.dwidth/4-20,
           textAlign:'left',fontSize:20,color:sys.titleColor,
           textAlignVertical:'center',
            ...Platform.select({
                ios: { lineHeight:sys.dwidth/2},
                android: {}
            })
           }}>二维码</Text>

            <Image
               source={{
               uri:self.state.erweimaStr
               }}
               style={{height:sys.dwidth/2,width:sys.dwidth/2,marginTop:0,marginLeft:10,backgroundColor:sys.silveryColor}}
               />
               </View>
        }
        


        

        return <KeyboardAwareScrollView style={styles.container}>
        
            <View style={{backgroundColor:'white'}}>

                <Text style={styles.contentText}>
                    尊敬的客户您好，您的充值订单已经生成，点击完成支付即可
                </Text>


                <View style={styles.textInputView}>
                    <Text style={styles.leftText}>充值金额</Text>

                    <Text style={styles.rightText}>
                        {self.state.czMoney}元
                    </Text>
                </View>

                <View style={styles.textInputView}>
                    <Text style={styles.leftText}>订单编号</Text>

                    <Text style={styles.rightText}>
                    {self.state.orderNum}
                    </Text>
                </View>

                <View style={styles.textInputView}>
                    <Text style={styles.leftText}>附言码</Text>

                    <Text style={styles.rightText}>
                    {self.state.fshuCode}
                    </Text>
                </View>


                {imgView}


                <TouchableOpacity style={styles.buttonDone} onPress={()=>this.donePay()}>
                    <Text style={[styles.buttontext,{width:sys.dwidth/4, height:40,
                    fontSize:18,
                    textAlignVertical:'center',
                    ...Platform.select({
                        ios: { lineHeight: 40},
                         android: {}
                    })
                    }]}>完成支付</Text>
                </TouchableOpacity>

                <View style={{height:30}} />

            </View>

            <ScrollView style={styles.cotentView}>

                <Text style={styles.contentText}>1.点击右上角客服，向在线客服咨询您需要的充值方式（微信，支付宝或者网银账号）；</Text>

                <Text style={styles.contentText}>使用您向客服咨询到的充值方式进行充值；</Text>

                <Text style={styles.contentText}>充值成功以后回到此页面；</Text>

                <Text style={styles.contentText}>填写你已经支付成功的金额，支付账号，支付方式并确定提交；</Text>

                <Text style={styles.contentText}>稍等片刻，刷新您的余额即可到账。；</Text>

            
            </ScrollView>

            <View style={{height:10,backgroundColor:'#FFF8DC',marginTop:0, marginLeft:10, marginRight:10}}/>
 
            <View style={{height:10,backgroundColor:'#FFFFFF',marginTop:0, marginLeft:10, marginRight:10}}/>
            <Toast  //提示
                    ref="toast"
                    style={{backgroundColor:'gray'}}
                    position='center'
                    positionValue={200}
                    opacity={0.6}
                    textStyle={{color:'white'}}
                />  

        </KeyboardAwareScrollView>
    }




    _renderCell = (item) => {
        let fubiob =item.item
        var content = "0";
        if (fubiob.fubi > 0) {
            content = fubiob.fubi + "福币";
        }

        if (fubiob.fubi_present > 0) {
            content = fubiob.fubi + "+" +fubiob.fubi_present + "福币";

        }

        return <TouchableOpacity style={styles.content} onPress={this.itemClick.bind(this, fubiob)}>
            <Image style={styles.contentImage} source={require('./images/icon_goubizhongxin.png')}></Image>
            <Text style={styles.numFubi}>{content}</Text>

            <View style={styles.rtBtnView}>
                <Text style={styles.rtBtnText}>{fubiob.rmb}元</Text>
            </View>

        </TouchableOpacity>
    }

    //充值福币
    itemClick(fubi){

        this.state.fubi = fubi;
        if(Platform.OS=='android'){
            this.dialog.show("请选择支付方式", ['微信','支付宝'], '#333333', this.callbackSelected.bind(this,fubi));
            return;
        }else{
            this.choosePaytype("apple");
            return;
        }

        let codeurl = host + '/pay/payret/isUseApplePay';
        let formData = new FormData();

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                let applePay = result['data']['applePay'];
                if(applePay){
                    this.choosePaytype("apple");
                }else{
                    this.dialog.show("请选择支付方式", ['微信','支付宝'], '#333333', this.callbackSelected.bind(this,fubi));
                }
            })
            .catch(error=>{
               
               this.refs.toast.show(error, DURATION.LENGTH_LONG);
            })
    }



    donePay()
    {
        this.props.navigation.goBack();
    }

    tijiao() {

        Keyboard.dismiss()

        let codeurl = host + '/AppApijiekou.addrecharge.do';
        let formData = new FormData();
      let paytypeStr = "";

      if (this.state.textWay == "0") {
        paytypeStr = "alipay";
      }
      if (this.state.textWay == "1") {
        paytypeStr = "weixin";
        
    }
    if (this.state.textWay == "2") {
        paytypeStr = "wangyin";
        
    }

    if (paytypeStr.length<=0) {    
        this.refs.toast.show("请选择支付方式", DURATION.LENGTH_LONG);
    
        return;
    }


    var thas = this;

    if (this.state.blance.length<=0) {
        this.refs.toast.show("请输入充值金额", DURATION.LENGTH_LONG);
        return;
    } 
    if(isNaN(this.state.blance)){
        this.refs.toast.show("输入正确的充值金额", DURATION.LENGTH_LONG);
        return;
    }
    if (this.state.zfNum.length<=0) {

        this.refs.toast.show("请输入支付账户", DURATION.LENGTH_LONG);
        return;
    } 
    this.setState({
        isTiJiao:true,
    })

    formData.append('amount',this.state.blance);
    formData.append('userpayname',this.state.zfNum);
    formData.append('paytype',paytypeStr);
        HttpUtils.post(codeurl,formData)
            .then(result=>{

                // console.log(result)

                this.setState({
                    isTiJiao:false,
                })
                if(result['respCode']==1){
                    let dic = result['data'];
                    thas.setState({
                        isSubmit:1,
                        czMoney:dic['amount'],
                        orderNum:dic['trano'],
                        fshuCode:dic['id'],
                        erweimaStr:dic['erweima']
                    })
               
                }else{
                
                }
                this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
            })
            .catch(error=>{
  
                this.refs.toast.show(error, DURATION.LENGTH_LONG);
                this.setState({
                    isTiJiao:false,
                })
            })
    }

    // 回调
    callbackSelected(fubi,i){
        switch (i){
            case 0: // 微信
                this.choosePaytype('wechat')
                break;
            case 1: // 支付宝
                this.choosePaytype('alipay');
                break;
        }
    }

    choosePaytype(type){

        this.setState({
            showLoading:true
        })
        let codeurl = host + '/fubi/purchase/fubi';
        let formData = new FormData();

        // 请求参数 ('key',value)
        formData.append('id',this.state.fubi.id);
        formData.append('type',type);
        HttpUtils.post(codeurl,formData)
            .then(result=>{
                this.setState({
                    showLoading:false
                })
                if(result['respCode']==1){

                    // this.gotoPay(type,result['data']['param']);
                }else{
 
                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                }
            })
            .catch(error=>{

                this.refs.toast.show(error, DURATION.LENGTH_LONG);
             
            })

    }


    
    _header = () => {

        return <View style={styles.hedViewSty}>

            <View style={styles.yuerViewStyle}>
                <Text style={styles.yuerStyle}>余额 (福币)</Text>

                <Text style={styles.yuerNumStyle}>{this.state.headfubi}</Text>
            </View>

            <View style={styles.changeViewStyle}>
            <Text style={styles.changeYuerStyle}>请选择购买金额</Text>
            </View>

        </View>;
    }

    _footer = () => {return <TouchableOpacity style={styles.fodViewStyle} onPress={()=>this.getRecord()}>
        <Text style={styles.txt}>购币记录></Text>
        </TouchableOpacity>;
    }

    getRecord(){
        const {navigate} = this.props.navigation;
        this.props.navigation.navigate("RechargeRecord",{name:"zhengsan"});
    }

    _separator = () => {
        return <View style={{height:1,backgroundColor:'#f2f2f2'}}/>;
    }

}

const styles = StyleSheet.create({

    container: {
        flex:1,
        backgroundColor:sys.backgroundColor
    },

    radioContainer: {
        marginTop:1,
       // flexDirection:'row',
        backgroundColor:'white'
        //,
        //height:50
    },
    radioView:{
        flexDirection:'row',
      //  marginLeft:1,
       // textAlign:'left',
        backgroundColor:sys.whiteColor,
        width:sys.dwidth-100,
        height:50
    },

    textInputView:{
        marginTop:1,
        flexDirection:'row',
        backgroundColor:sys.whiteColor,
        height:50,
        width:sys.dwidth
    },

    leftText:{
        marginLeft:10,
        marginTop:17,
        width:90,
        backgroundColor:sys.whiteColor,
        fontSize:16,
    },
    rightText:{
        marginLeft:10,
        marginTop:17,

        width:sys.dwidth - 40,
        fontSize:15,
        color:sys.subTitleColor

    },
    
    inputText:{
        width:sys.dwidth-75,
        color:sys.subTitleColor,
        textAlign:'left',
        backgroundColor:sys.whiteColor,
     },
    hedViewSty:{
        height:140,
        backgroundColor:'#f2f2f2',
        width:sys.dwidth
    },


    changeViewStyle:{
        marginTop:0,
        marginLeft:0,
        height:30,
        justifyContent:'center'
    },
    yuerStyle:{
        fontSize:17,
        color:"#333333",
        textAlign: 'left',
        marginTop:15,
        marginLeft:15,
        width:sys.dwidth - 30
    },

    yuerViewStyle:{
        marginTop:0,
        marginLeft:0,
        height:110,
    },

    yuerNumStyle:{
        fontSize:35,
        color:"#eb7136",
        textAlign: 'center',
        marginTop:(110 - 36 - 36)/2,
        width:sys.dwidth - 30,
    },

    changeYuerStyle:{

        fontSize:14,
        color:"#666666",
        textAlign: 'left',
        marginLeft:15,

    },

    content:{
        backgroundColor:'white',
        flexDirection:'row',
        height:50,
        width:sys.dwidth,
        alignItems:'center'
    },
    contentImage:{
        //alignItems:'center',
        marginLeft:15,
        width:30,
        height:30
    },
    numFubi:{

        marginLeft:15,
        fontSize:14
    },
    rtBtnView: {
        backgroundColor:sys.mainColor,
        width:80,
        height:25,
        position: 'absolute',
        justifyContent:'center',
        alignItems:'center',
        bottom:12.5,
        right: 15,
        borderRadius:5,

    },
    rtBtnText:{
        fontSize:14,
        color:'white'
    },

    fodViewStyle:{
        justifyContent:'center',
        width:sys.dwidth,
        height:30,
    },

    txt:{
        position: 'absolute',
        right: 10,
        fontSize:14
    },

    button:{
        flexDirection:'row',
        width:sys.dwidth-40,
        marginLeft:20,
        marginTop:40,
        height:40,
        backgroundColor:sys.mainColor,
        justifyContent:'center',
        borderRadius:5,

    },


    buttonDone:{
        width:sys.dwidth/4,
        marginLeft:(sys.dwidth -sys.dwidth/4)/2,
        marginTop:20,
        height:40,
        backgroundColor:sys.mainColor,
        borderRadius:5,

    },


    buttontext:{
        width:80 ,
        height:40,
       // marginLeft:20,
        color:'#ffffff',
        textAlign:'center',
      //  backgroundColor:'blue',
        fontSize:18,
        textAlignVertical:'center',
        ...Platform.select({
            ios: { lineHeight: 40},
             android: {}
        })

    },
    cotentView:{
        marginTop:11,
        marginLeft:10,
        marginRight:10,
        backgroundColor:'#FFF8DC',
        //width:sys.width - 50
    },
    contentText:{
        marginLeft:10,
        marginTop:10,
       // marginRight:10,
      //  width:sys.width - 40,
        fontSize:16,
        color:'#696969'
    },
  
})



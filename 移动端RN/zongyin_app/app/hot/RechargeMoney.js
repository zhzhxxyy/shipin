import React, {Component} from 'react';
import {
    StyleSheet,
    View,
    Text,
    WebView,
    TouchableOpacity,
    ScrollView,
    Image,
    TextInput,
    Platform
} from 'react-native';
import { sys,NoDataView } from '../common/Data';
import HttpUtils from "../common/HttpUtil"
import toast from 'react-native-root-toast'

var _this;
var clickLastTime=0;
const shheight = 40;
export default class Recharge extends Component {

    static  navigationOptions = ({navigation}) => {
        const { params } = navigation.state;
        let titStr = '充值中心'
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
                headerTitle:titStr,
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
            headerTitle:titStr,
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
        // 初始状态
        this.state = {
            loading: true,
            isSuccess:false,
            goldPackageList:[],
            gold_exchange_rate:"",
            paymentList:[],
            rechargeList:[],
            showVip:true,
            currentPackage:0,
            currentPay:0,
            money:0.0,
            inputPrice:"",
        };
    }

    componentDidMount() {
        _this = this;
        this.getPackageInfo(true,0);
    }


    createOrder(){
        _this.tiaozhengJinbi();
        //计算金钱
        var price=0;
        var packageId=0;
        if(_this.state.showVip){
            var buyType=2;
            if(_this.state.rechargeList!=null&&_this.state.currentPackage>=0&&_this.state.currentPackage<_this.state.rechargeList.length){
                packageId= _this.state.rechargeList[_this.state.currentPackage].id;
                price=_this.state.rechargeList[_this.state.currentPackage].price;
            }
        }else{
            var buyType=1;
            if(_this.state.goldPackageList!=null&&_this.state.currentPackage>=0&&_this.state.currentPackage<_this.state.goldPackageList.length){
                packageId= _this.state.goldPackageList[_this.state.currentPackage].id;
                price=_this.state.goldPackageList[_this.state.currentPackage].price;
            }
            if(_this.state.currentPackage==-1&&_this.state.gold_exchange_rate!=0){
                price=Math.ceil(_this.state.inputPrice/_this.state.gold_exchange_rate);
            }
        }
        var payCode="";
        if(_this.state.paymentList!=null&&_this.state.currentPay>=0&&_this.state.currentPay<_this.state.paymentList.length){
            payCode= _this.state.paymentList[_this.state.currentPay].payCode;
        }
        if(price<=0){
            toast.show("充值的金额不小于0");
            return;
        }
        if(payCode==""){
            toast.show("请选择付款方式");
            return;
        }
        var nowTime= (new Date()).getTime();
        if(Math.abs(nowTime-clickLastTime)<sys.clickIntervalTime){
            toast.show("点击速度过快，请稍后");
            return false;
        }
        clickLastTime=nowTime;

        let codeurl = 'app_system_pay/createOrder';
        let formData = new FormData();
        formData.append('buyType',buyType);//1 金币充值  2 VIp
        formData.append('payCode',payCode);//支付方式
        formData.append('price',price);
        formData.append('packageId',packageId);

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                toast.show("创建订单成功")
                _this.props.navigation.navigate('WebViewScene', {uri: {content:result['data']},'title':"订单支付"});
            }).catch(error=>{
              toast.show("创建订单失败:"+error)
        })
    }
    getPackageInfo(isAuto,num){
        _this.setState({
            loading:true,
            isSuccess:false
        })
        let codeurl = 'app_system_pay/recharge';
        let formData = new FormData();
        HttpUtils.post(codeurl,formData)
            .then(result=>{
                _this.setState({
                    loading:false,
                    isSuccess:true,
                    goldPackageList:(result['data']['goldPackageList']==false||result['data']['goldPackageList'].length<=0)?[]:result['data']['goldPackageList'],
                    paymentList:(result['data']['paymentList']==false||result['data']['paymentList'].length<=0)?[]:result['data']['paymentList'],
                    rechargeList:(result['data']['rechargeList']==false||result['data']['rechargeList'].length<=0)?[]:result['data']['rechargeList'],
                    gold_exchange_rate:result['data']['gold_exchange_rate'],
                })
            }).catch(error=>{
            _this.setState({
                loading:false,
                isSuccess:false
            })
            if(!isAuto){
                toast.show("获取数据失败:"+error)
            }
        })
    }


    clickPackageType=(showVip)=>{
        if(_this.state.showVip==showVip){
            return;
        }
        _this.setState({
            currentPackage:0,
            showVip:showVip
        })
    }

    clickChoicePackage(index){
        if(_this.state.currentPackage!=index){
            _this.setState({
                currentPackage:index
            })
        }
    }
    clickChoicePay(index){
        if(_this.state.currentPay!=index){
            _this.setState({
                currentPay:index
            })
        }
    }

    onChangeText(text){
        if(_this.state.currentPackage==-1&&!_this.state.showVip){
            var upPrice=0;
            try{
               var valueString=parseInt(text);
                if(valueString<=0 || isNaN(valueString)){
                    valueString="";
                }
                if(_this.state.gold_exchange_rate!=0&&valueString!=""){
                    upPrice=Math.ceil(valueString/_this.state.gold_exchange_rate);
                }
            }catch(e){
            }
            _this.setState({
                inputPrice:text,
                money:upPrice
            })
        }else{
            _this.setState({
                inputPrice:text
            })
        }
    }

    tiaozhengJinbi(){
        if(_this.state.currentPackage==-1&&!_this.state.showVip){
            var inputPrice=this.state.money*_this.state.gold_exchange_rate;
            if(inputPrice==0){
                inputPrice="";
            }
            _this.setState({
                inputPrice:inputPrice+"",
            })
        }
    }




    render() {
        const {params} = this.props.navigation.state;
        var showVip=this.state.showVip;
        var currentPackage=this.state.currentPackage;
        var currentPay=this.state.currentPay;
        var paymentList=this.state.paymentList;
        var goldPackageList=this.state.goldPackageList;
        var rechargeList=this.state.rechargeList;
        var gold_exchange_rate=this.state.gold_exchange_rate;
        if(this.state.loading==true){
            return  <NoDataView isloading = {true} />
        }
        if(this.state.isSuccess==false){
              return  <NoDataView  click={()=>_this.getPackageInfo(false,0)}
                                   isloading = {false}
                                   text = {"加载失败，点击重新加载"} />
        }
        //计算金钱
        var money=0;
        if(showVip){
            if(currentPackage!=-1&&rechargeList!=null&&rechargeList.length>0&&currentPackage<rechargeList.length){
                money= rechargeList[currentPackage].price;
            }
        }else{
            if(currentPackage!=-1&&goldPackageList!=null&&goldPackageList.length>0&&currentPackage<rechargeList.length){
                money= goldPackageList[currentPackage].price;
            }
            if(currentPackage==-1&&this.state.inputPrice!=""&&this.state.inputPrice!=undefined&&this.state.gold_exchange_rate!=0){
                money=Math.ceil(this.state.inputPrice/this.state.gold_exchange_rate);
            }
        }
        this.state.money=money;


        if(showVip){
            var packageView=
              <ScrollView horizontal={true} style={{height:120,marginTop:20}}>
                  <View style={{flexDirection: 'row',paddingRight:5}}>
                      {
                          rechargeList.map((item, index) => {
                              var isChoice=false;
                              if(index==currentPackage){
                                  isChoice=true;
                              }
                              if(item.permanent!=1){
                                  var daysValue="VIP "+item.days+"天";
                              }else{
                                  var daysValue="永久VIP";
                              }
                              return(
                                  <TouchableOpacity  activeOpacity={1} onPress={() => { _this.clickChoicePackage(index);}}>
                                      <View style={{height:100,width:80,marginLeft:5}}>

                                          <View style={{alignItems: 'center',height:50,width:80,backgroundColor:"#a38a51",borderTopLeftRadius:5,borderTopRightRadius:5}}>
                                              <Text style={{fontSize: 12, color:"#FFFFFF",marginTop:8}}>{item.name}</Text>
                                              <Text style={{fontSize: 12, color:"#FFFFFF",marginTop:2}}>¥{item.price}</Text>
                                          </View>
                                          <View style={{alignItems: 'center',height:50,width:80,backgroundColor:"#ffffff",borderBottomLeftRadius:5,borderBottomRightRadius:5,borderLeftWidth:1,borderLeftColor:isChoice?"#a38a51":"#f1f1f1",borderRightWidth:1,borderRightColor:isChoice?"#a38a51":"#f1f1f1",borderBottomWidth:1,borderBottomColor:isChoice?"#a38a51":"#f1f1f1"}}>
                                              <Text style={{width:65,height:25,fontSize: 12, color:"#FFFFFF",backgroundColor:"#a38a51",borderRadius:15,marginTop:12,paddingLeft:5,paddingRight:5, textAlign: 'center',
                                                  textAlignVertical: 'center',
                                                  ...Platform.select({
                                                      ios: { lineHeight: 25},
                                                      android: {}
                                                  }) }}>{daysValue}</Text>
                                          </View>
                                      </View>
                                  </TouchableOpacity>
                              )
                          })
                      }
                  </View>
              </ScrollView>;

            var tishiView=null;
        }else{
            var packageView=
                <ScrollView horizontal={true} style={{height:120,marginTop:20}}>
                    <View style={{flexDirection: 'row',paddingRight:5}}>
                        {
                            goldPackageList.map((item, index) => {
                                var isChoice=false;
                                if(index==currentPackage){
                                    isChoice=true;
                                }
                                return(
                                    <TouchableOpacity  activeOpacity={1} onPress={() => { _this.clickChoicePackage(index);}}>
                                        <View style={{height:100,width:80,marginLeft:5}}>

                                            <View style={{alignItems: 'center',height:50,width:80,backgroundColor:"#a38a51",borderTopLeftRadius:5,borderTopRightRadius:5}}>
                                                <Text style={{fontSize: 12, color:"#FFFFFF",marginTop:8}}>{item.name}</Text>
                                                <Text style={{fontSize: 12, color:"#FFFFFF",marginTop:2}}>{item.gold}</Text>
                                            </View>
                                            <View style={{alignItems: 'center',height:50,width:80,backgroundColor:"#ffffff",borderBottomLeftRadius:5,borderBottomRightRadius:5,borderLeftWidth:1,borderLeftColor:isChoice?"#a38a51":"#f1f1f1",borderRightWidth:1,borderRightColor:isChoice?"#a38a51":"#f1f1f1",borderBottomWidth:1,borderBottomColor:isChoice?"#a38a51":"#f1f1f1"}}>
                                                <Text style={{width:65,height:25,fontSize: 12, color:"#FFFFFF",backgroundColor:"#a38a51",borderRadius:15,marginTop:12,paddingLeft:5,paddingRight:5, textAlign: 'center',
                                                    textAlignVertical: 'center',
                                                    ...Platform.select({
                                                        ios: { lineHeight: 25},
                                                        android: {}
                                                    }) }}>¥{item.price}</Text>
                                            </View>
                                        </View>
                                    </TouchableOpacity>
                                )
                            })
                        }
                        <TouchableOpacity  activeOpacity={1} onPress={() => { _this.clickChoicePackage(-1);}}>
                            <View style={{height:100,width:80,marginLeft:5}}>

                                <View style={{alignItems: 'center',height:50,width:80,backgroundColor:"#a38a51",borderTopLeftRadius:5,borderTopRightRadius:5}}>
                                    <Text style={{fontSize: 12, color:"#FFFFFF",marginTop:8}}>自定义</Text>
                                    <Text style={{fontSize: 12, color:"#FFFFFF",marginTop:2}}></Text>
                                </View>

                                <View style={{height:50,width:80,backgroundColor:"#ffffff",borderBottomLeftRadius:5,borderBottomRightRadius:5,borderLeftWidth:1,borderLeftColor:currentPackage==-1?"#a38a51":"#f1f1f1",borderRightWidth:1,borderRightColor:currentPackage==-1?"#a38a51":"#f1f1f1",borderBottomWidth:1,borderBottomColor:currentPackage==-1?"#a38a51":"#f1f1f1"}}>

                                    <TextInput style={{width:65,height:25,borderRadius:25,marginTop:12,marginLeft:6,borderColor:"#afafaf",borderWidth:1,padding:1,
                                        fontSize:10, textAlign: 'center',
                                        textAlignVertical: 'center',
                                        ...Platform.select({
                                            ios: { lineHeight: 25},
                                            android: {}
                                        })}}
                                               placeholder={""}
                                               value={this.state.inputPrice}
                                               onChangeText={(text) => _this.onChangeText(text)}
                                    />
                                </View>
                            </View>
                        </TouchableOpacity>

                    </View>
                </ScrollView>;

            var tishiView=<View style={{width:sys.dwidth-20,marginLeft:10,marginTop:5}}>

                <Text style={{ fontSize: 10, color:"#a38a51"}}>温馨提示：</Text>

                <Text style={{ fontSize: 10, color:"#a38a51",marginTop:3}}>1、当前金币兑换比例：1元人民币可兑换{gold_exchange_rate}个金币</Text>
                <Text style={{ fontSize: 10, color:"#a38a51"}}>2、您输入的金币将会自动调整为整数</Text>
            </View>;
        }




        return (
            <View  style={{flex:1}}>
                <View style={{width:sys.dwidth,height:1,backgroundColor:"#eee"}}/>
               <View style={{flexDirection: 'row',height:40}}>

                   <TouchableOpacity style={{ flex: 1,alignItems: 'center',textAlign:"center"}} activeOpacity={1} 
                   onPress={() => { _this.clickPackageType(true); }}>
                       <Text style={{  fontSize: 14, color:showVip?"#fe5d02":"#333333",height:40,  textAlign: 'center',
                           textAlignVertical: 'center',
                           ...Platform.select({
                               ios: { lineHeight:shheight},
                               android: {}
                           })}}>VIP会员</Text>
                   </TouchableOpacity>

                   <View style={{width:1,height:40,backgroundColor:"#eee"}}/>

                   <TouchableOpacity style={{ flex: 1,alignItems: 'center',textAlign:"center"}} activeOpacity={1} onPress={() => { _this.clickPackageType(false);}}>
                       <Text style={{  fontSize: 14, color: showVip?'#333333':"#fe5d02",height:40,textAlign: 'center',
                           textAlignVertical: 'center',
                           ...Platform.select({
                               ios: { lineHeight: shheight},
                               android: {}
                           })}}>金币</Text>
                   </TouchableOpacity>
               </View>
                <View style={{width:sys.dwidth,height:1,backgroundColor:"#eee"}}/>


                <ScrollView style={{flex:1,paddingBottom:20}}>

                    {packageView}

                    {tishiView}

                    <Text style={{fontSize: 14, color:"#696969",marginTop:20,marginLeft:10}}>支付方式</Text>
                    <View style={{width:sys.dwidth-20,height:1,backgroundColor:"#eee",marginLeft:10}}/>

                    {
                        paymentList.map((item, index) => {
                            return(
                                <TouchableOpacity  activeOpacity={1} onPress={() => { _this.clickChoicePay(index); }}>
                                    <View style={{width:sys.dwidth-20,height:44,marginLeft:10,marginTop:10,borderColor:index==currentPay?"#ff6e1e":"#d8d8d8",borderWidth:1,borderRadius:5}}>
                                        <Image resizeMode='contain'  style={{height:34,marginTop:5}} source={{uri:item.payIcon}} />
                                    </View>
                                </TouchableOpacity>
                            )
                        })
                    }

                    <View style={{width:sys.dwidth,height:20}}/>
                </ScrollView>

                <View style={{width:sys.dwidth,height:1,backgroundColor:"#eee"}}/>

                <View style={{flexDirection: 'row',height:60}}>
                    <Text style={{  fontSize: 10, color:"#333333",height:40,marginLeft:10,textAlignVertical: 'center'}}>支付</Text>
                    <Text style={{  fontSize: 10, color:"#333333",height:40,marginLeft:3,textAlignVertical: 'center'}}>¥</Text>


                    <Text style={{ fontSize: 13, color:"#FE5D02",flex:1,textAlignVertical: 'center'}}>{money}</Text>

                    <TouchableOpacity  activeOpacity={1} onPress={() => { _this.createOrder()}}>
                        <Text style={{ width:60,height:40, fontSize: 12, color:sys.whiteColor,backgroundColor:sys.mainColor, textAlign: 'center',
                            textAlignVertical: 'center',
                            ...Platform.select({
                                ios: { lineHeight: 40},
                                android: {}
                            })}}>充值</Text>
                    </TouchableOpacity>
                </View>

            </View>
        )
    }
}

const styles = StyleSheet.create({


    top: {
        top:0,

        width:sys.dwidth,
        height:50,
        backgroundColor: "red",
    },

    container: {
   

        width:sys.dwidth,
        height:400,
        backgroundColor: "red",
    },
    webview: {
       
        backgroundColor: "red",
        width:sys.dwidth,
        height:400
    },
    textCenter:{
        textAlign: 'center',
        textAlignVertical: 'center',
        ...Platform.select({
            ios: { lineHeight: shheight},
            android: {}
        })
    },
    loadingText: {
        color: '#8a8a8a',
        fontSize: 16
    }
})
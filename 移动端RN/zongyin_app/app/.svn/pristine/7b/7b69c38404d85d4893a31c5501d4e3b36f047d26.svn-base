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
    ActivityIndicator,
    Keyboard,
    ScrollView

} from 'react-native';


import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import {sys,NoDataView} from "../../common/Data"
const host = sys.host;


//import MyPicker from 'react-native-picker'

import RNLogin from "../../common/RNLoginModule"
import HttpUtils from "../../common/HttpUtil";

import Picker from 'react-native-picker';
import Toast,{DURATION} from 'react-native-easy-toast';

let zhongHeight = 40;
let recordWidth = sys.dwidth;
let qisuW = recordWidth/3 + 20;
let qitaW =  recordWidth/3-10;

const titList = ['充值','实到','帐变前','帐变后','单号','时间','状态']

export default class DelegateCunTk extends React.Component {
    // static  navigationOptions = ({navigation}) => ({
    //     headerTitle:"团队存提款",
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
            source={require('../../res/images/iosfanhui.png')}
            style={{marginLeft:10,marginTop:0}}
            />

            </TouchableOpacity>

            return {
                headerTitle:"团队存提款",
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
            headerTitle:"团队存提款",
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
            startDate:'',
            endDate:'',
            leiXin:'充值',
            zhuangtai:'全部状态',
            dingDanNum:'',
            acountNum:'',
            refreshState: RefreshState.HeaderRefreshing,
            isChaXun:false,
            dataList: [],
            currentPage:1,
            isloading:false,

            yeNum:0,
            xianShiArr:[],
            xianShiNum:parseInt((sys.dheight*4/7 - 45 - 35)/35)
        }
    }

    onHeaderRefresh = () => {

        this.setState(
            {
                currentPage:1,
                refreshState: RefreshState.HeaderRefreshing,
            },()=>{
                this.getDataList(true)
            })


    }

    onFooterRefresh = () => {

        this.setState({
            refreshState: RefreshState.FooterRefreshing
        },()=>{
            this.getDataList(false)
        })
    }

    componentDidMount(){


        if(!this.state.startDate.length) {
            this.state.startDate = this._getCurrentDate();
        }
        if(!this.state.endDate.length) {
            this.state.endDate = this._getCurrentDate();
        }

         this.getDataList(true);


    }

 

    


    //获取当前日期  格式如 2018-12-15
  _getCurrentDate(){
    var currDate = new Date()
    var year = currDate.getFullYear()
    var month = (currDate.getMonth()+1).toString()
    month = month.padStart(2,'0')
    var dateDay = currDate.getDate().toString()
    dateDay = dateDay.padStart(2,'0')
    let time = year+'-'+month+'-'+dateDay
    return time;
  }



    //组装日期数据
  _createDateData(){
    let date = [];
    var currDate = new Date()
    var year = currDate.getFullYear()
    var month = currDate.getMonth()+1
    for(let i=1970;i<=year;i++){
        let month = [];
        for(let j = 1;j<13;j++){
            let day = [];
            if(j === 2){
                for(let k=1;k<29;k++){
                    day.push(k+'日');
                }
                //Leap day for years that are divisible by 4, such as 2000, 2004
                if(i%4 === 0){
                    day.push(29+'日');
                }
            }
            else if(j in {1:1, 3:1, 5:1, 7:1, 8:1, 10:1, 12:1}){
                for(let k=1;k<32;k++){
                    day.push(k+'日');
                }
            }
            else{
                for(let k=1;k<31;k++){
                    day.push(k+'日');
                }
            }
            let _month = {};
            _month[j+'月'] = day;
            month.push(_month);
        }
        let _date = {};
        _date[i+'年'] = month;
        date.push(_date);
    }
    return date;
  }

//彩票状态
  showCaipiaoPicker(num){

    
    Picker.init({
        pickerData:(num == 1)?['充值','提款']:['全部状态','正在处理','审核通过','取消申请'],
        selectedValue: (num == 1)?['全部充提']:['全部状态'],
        pickerConfirmBtnText:'确定',
        pickerCancelBtnText:'取消',
        pickerTitleText:(num == 1)?'选择类型':'选择状态',
        pickerToolBarBg: [232, 232, 232, 1],
        pickerBg:[245,245,245,1],
        pickerToolBarFontSize: 16,
        pickerFontSize: 20,
        onPickerConfirm: (data) => {

            
            if (num ==1) {
                this.setState({leiXin:data[0]})  
            } else {
                this.setState({zhuangtai:data[0]})  
            }
                     
        }
    });

  
    Picker.show();
}


componentWillUnmount(){
    Picker.hide()
}

   //打开日期选择 视图
   _showDatePicker(num) {

    Keyboard.dismiss()

    var year = ''
    var month = ''
    var day = ''
    var dateStr = this._getCurrentDate();
    //console.log('dateStr',dateStr)
    year = dateStr.substring(0,4)
    month = parseInt(dateStr.substring(5,7))
    day = parseInt(dateStr.substring(8,10))
    Picker.init({
      pickerTitleText:'时间选择',
      pickerCancelBtnText:'取消',
      pickerConfirmBtnText:'确定',
      selectedValue:[year+'年',month+'月',day+'日'],
      pickerBg:[255,255,255,1],
      pickerData: this._createDateData(),
      pickerFontColor: [33, 33 ,33, 1],
      pickerToolBarFontSize: 16,
      pickerFontSize: 20,
      onPickerConfirm: (pickedValue, pickedIndex) => {
          var year = pickedValue[0].substring(0,pickedValue[0].length-1)
          var month = pickedValue[1].substring(0,pickedValue[1].length-1)
          month = month.padStart(2,'0')
          var day = pickedValue[2].substring(0,pickedValue[2].length-1)
          day = day.padStart(2,'0')
          let str = year+'-'+month+'-'+day

          if (num == 1) {
            this.setState({
                startDate:str,
              })
          }
          if (num == 2) {
            this.setState({
                endDate:str,
              })
          }         
      },
      onPickerCancel: (pickedValue, pickedIndex) => {
          console.log('date', pickedValue, pickedIndex);
      },
      onPickerSelect: (pickedValue, pickedIndex) => {
          console.log('date', pickedValue, pickedIndex);
      }
    });
    Picker.show();
  }



    render(){

 
        let hedView = this.renderHeadView()

        let showView = null
        if (this.state.dataList.length) {
            showView = this.itemView()          
        } else {
            showView = <NoDataView click={()=>this.getDataList(true)}
            isloading={this.state.isloading}
            text = {this.state.isloading?"加载中":"暂无存提款,点击重新加载"}
        />
        }

        // let showView = this.state.dataList.length?
        //     <RefreshListView
        //         data={this.state.dataList}
        //         keyExtractor={this.keyExtractor}
        //         renderItem={this.renderCell}
        //         refreshState={this.state.refreshState}
        //         onHeaderRefresh={this.onHeaderRefresh}
        //         onFooterRefresh={this.onFooterRefresh}
        //         numColumns ={1}
        //         // ListHeaderComponent={this.renderHeadView}
        //         // horizontal={false}
        //         //ItemSeparatorComponent={this._separator}
        //         // 可选
        //         footerRefreshingText= '玩命加载中 >.<'
        //         footerFailureText = '我擦嘞，居然失败了 =.=!'
        //         footerNoMoreDataText= '-我是有底线的-'
        //     />
        //     :<NoDataView click={()=>this.getDataList(true)}
        //         isloading={this.state.isloading}
        //         text = {this.state.isloading?"加载中":"暂无存提款,点击重新加载"}
        //     />

        return(

            <View style={{marginTop:1,width:recordWidth,height:'100%',backgroundColor:'white'}}>

                {hedView}
               {showView}

               <Toast  //提示
                    ref="toast"
                    style={{backgroundColor:'gray'}}
                    position='center'
                    positionValue={200}
                    opacity={0.6}
                    textStyle={{color:'white'}}
                />

            </View>
            

    
        )
    }


    renderHeadView(){

        return <View>

        <View style={{marginTop:10,marginLeft:0,width:sys.dwidth,height:42,
            textAlign:'left',flexDirection:'row'}}>    
                <TouchableOpacity style={styles.bankNameView} onPress={()=>this._showDatePicker(1)}>
                <Text
                    editable={false}

                    style={[styles.rightText,{marginLeft:0,color:sys.subTitleColor,fontSize:13}]}
                >
                    {this.state.startDate?this.state.startDate:this._getCurrentDate()}
                </Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.bankNameView} onPress={()=>this._showDatePicker(2)}>
                <Text
                    editable={false}

                    style={[styles.rightText,{marginLeft:0,color:sys.subTitleColor,fontSize:13}]}
                >
                    {this.state.endDate?this.state.endDate:this._getCurrentDate()}
                </Text>
            </TouchableOpacity>

            
            <TextInput
                underlineColorAndroid='transparent'
                style={styles.inputText}
                placeholder="用户名"
                onFocus={()=>{
                    Picker.hide()
                }}
                value={this.state.acountNum}
                onChangeText={(text) => this.setState({acountNum:text})}
            />

            
        </View>


        <View style={{marginTop:10,marginLeft:0,width:sys.dwidth,height:42,
            textAlign:'left',flexDirection:'row'}}>

               <TextInput
                underlineColorAndroid='transparent'
                style={styles.inputText}
                placeholder="订单编号"
                onFocus={()=>{
                    Picker.hide()
                }}
                value={this.state.dingDanNum}
                onChangeText={(text) => this.setState({dingDanNum:text})}
            />

            <TouchableOpacity style={styles.bankNameView} onPress={()=>this.showCaipiaoPicker(1)}>
                <Text
                    editable={false}

                    style={[styles.rightText,{marginLeft:0,color:sys.subTitleColor,fontSize:13}]}
                >
                    {this.state.leiXin?this.state.leiXin:"充值"}
                </Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.bankNameView} onPress={()=>this.showCaipiaoPicker(2)}>
                <Text
                    editable={false}

                    style={[styles.rightText,{marginLeft:0,color:sys.subTitleColor,fontSize:13}]}
                >
                    {this.state.zhuangtai?this.state.zhuangtai:"全部状态"}
                </Text>
            </TouchableOpacity>

        </View>

        <TouchableOpacity disabled={this.state.isChaXun} style={{width:sys.dwidth-40,
        marginLeft:20, marginTop:10,height:40,backgroundColor:sys.mainColor,borderRadius:5,
         flexDirection:'row',}} onPress={()=>this.chazhao()}>
                {this.state.isChaXun?<ActivityIndicator style={{marginLeft:(sys.dwidth-40-100)/2 - 10 - 20,width:20
                    ,height:20,marginTop:10}} />:null}
                <Text style={[{color:'#ffffff',textAlign:'center',fontSize:18,
                height:40, textAlignVertical:'center',...Platform.select({
                ios: { lineHeight: 40}, android: {}})
                },{width:100,marginLeft:this.state.isChaXun?10:(sys.dwidth-40-100)/2}]}>{this.state.isChaXun?'查询中..':'查询'}</Text>
            </TouchableOpacity>


            <View style={{height:10}}></View>

        </View>

    }


    timeWithDataStr(str)
    {
        var date = new Date(str)

        var time = date.getTime();

        return time;
    }


    chazhao() {

        Picker.hide()
        Keyboard.dismiss();


        if (this.state.startDate.length && this.state.endDate.length) {

            if (this.timeWithDataStr(this.state.startDate) > this.timeWithDataStr(this.state.endDate)) {

                this.refs.toast.show("初始时间不能大于结束时间", DURATION.LENGTH_LONG);

                return false
            }

        }

        this.setState({
            isChaXun:true,
            dataList:[]
        })
        this.state.currentPage=1;
        this.getDataList(true)

    }

    renderCell = (item) => {

        return this.itemView(item.index)
    }



    itemView(){


      return <View>
        <View style={{flexDirection: 'row'}}>

            <View>
                  
                   <View>
                         <View style={{backgroundColor:sys.mainColor}}>
                       <Text style={{marginLeft:10,marginRight:10,textAlign:'center',height:35,color:sys.whiteColor,textAlignVertical:'center'
                       ,...Platform.select({
                        ios: { lineHeight: 35},
                         android: {}
                    })
                    }}>用户名</Text>  
                    </View>

                    {this.state.xianShiArr.map((info,weizNum)=>{
                         return <View style={{borderRightWidth:1,borderColor:sys.grayColor,borderBottomWidth:1}}>
                         <Text key={weizNum} style={{marginLeft:10,marginRight:10,textAlign:'center'
                         ,height:35,color:sys.titleColor,textAlignVertical:'center'
                         ,backgroundColor:sys.whiteColor,...Platform.select({
                             ios: { lineHeight: 35},
                              android: {}
                         })
                        }}>{info.username}</Text>
                        
                        <View style={{height:1,backgroundColor:sys.grayColor}}></View>
                        </View>
                     })}
                     </View>

                     

            </View>

            <ScrollView horizontal={true}>
            
                {titList.map((titStr, index)=>{

                     return(
                     <View >
                         <View style={{backgroundColor:sys.mainColor}}>
                       <Text style={{marginLeft:10,marginRight:10,textAlign:'center',height:35,color:sys.whiteColor,textAlignVertical:'center'
                       ,...Platform.select({
                        ios: { lineHeight: 35},
                         android: {}
                    })
                    }}>{titStr}</Text>  
                    </View>

                     {this.state.xianShiArr.map((info,weizNum)=>{

                        var textType = '审核通过'
                        if(info.state==0){
                            textType = '正在处理';
                        }else if(info.state==1){
                            textType = '审核通过';
                        } else if (info.state==-1) {
                            textType = '取消申请'
                        }

                        var contentStr ='' 
                        if (index==0) {
                            contentStr = info.amount
                        } else if (index==1) {
                            contentStr = info.actualamount
                        } else if (index==2) {
                            contentStr = info.oldaccountmoney
                        } else if (index==3) {
                            contentStr = info.newaccountmoney
                        }else if (index==4) {
                            contentStr = info.trano
                        } else if (index==5) {
                            contentStr = info.oddtime
                        }  else if (index == 6) {
                            contentStr = textType
                        }

                         return <View style={{borderLeftWidth:index==0?0:1,borderColor:sys.grayColor,borderBottomWidth:1}}>
                         <Text key={weizNum} style={{marginLeft:10,marginRight:10,textAlign:'center'
                         ,height:35,color:sys.titleColor,textAlignVertical:'center'
                         ,backgroundColor:sys.whiteColor,...Platform.select({
                             ios: { lineHeight: 35},
                              android: {}
                         })
                        }}>{contentStr}</Text>
                        
                        <View style={{height:1,backgroundColor:sys.grayColor}}></View>
                        </View>
                     })}
                     
                     
                                   
                     </View>
                     )
                  })}

             </ScrollView>

        </View>
        <View style={{marginTop:5,marginLeft:0,width:sys.dwidth,height:35,
            textAlign:'left',flexDirection:'row'}}>    
                <TouchableOpacity onPress={()=>{

                     if (this.state.yeNum) {
                         this.setState({
                             yeNum:0
                         })

                         if (this.state.dataList.length) {
                            var newArr=[]
                            if (this.state.dataList.length>this.state.xianShiNum) {
                                
                                for(var i=0;i<this.state.xianShiNum;i++) {
                                    newArr.push(this.state.dataList[i])
                                }
    
                            } else {
                                for(var i=0;i<this.state.dataList.length;i++) {
                                    newArr.push(this.state.dataList[i])
                                }
                            }
                         }
                         

                        this.setState({
                           
                            xianShiArr:newArr,
                            yeNum:0,
                            // (testData.length >= result['data']['total']) ? RefreshState.NoMoreData : RefreshState.Idle,
                        })


                     } else {
                        this.refs.toast.show("当前是首页", DURATION.LENGTH_LONG);
                     }
                }}>
                <Text
                    editable={false}

                    style={{marginLeft:20,height:35,borderRadius:5,
                        width:(sys.dwidth-80)/3,borderColor:sys.silveryColor,
                        borderWidth:1,textAlign:'center',
                         fontSize:13,textAlignVertical:'center',
                         ...Platform.select({
                             ios: { lineHeight: 35},
                                 android: {}
                         })}}
                >
                    首页
                </Text>
            </TouchableOpacity>

            <TouchableOpacity onPress={()=>{
              
                if (this.state.yeNum) {

                    this.state.yeNum--;

                    var newArr=[]
                                 
                    for(var i=this.state.yeNum*this.state.xianShiNum;i<(this.state.yeNum+1)*this.state.xianShiNum;i++) {
                        newArr.push(this.state.dataList[i])
                    }

                    this.setState({
                           
                        xianShiArr:newArr,
                        yeNum:this.state.yeNum,
                        // (testData.length >= result['data']['total']) ? RefreshState.NoMoreData : RefreshState.Idle,
                    })
                         

                } else {
                    this.refs.toast.show("当前是首页", DURATION.LENGTH_LONG);
                 }

            }}>
                <Text
                    editable={false}

                    style={{marginLeft:20,height:35,borderRadius:5,
                        width:(sys.dwidth-80)/3,borderColor:sys.silveryColor,
                        borderWidth:1,textAlign:'center',
                         fontSize:13,textAlignVertical:'center',
                         ...Platform.select({
                             ios: { lineHeight: 35},
                                 android: {}
                         })}}
                >
                    上一页
                </Text>
            </TouchableOpacity>

            <TouchableOpacity  onPress={()=>{

                 if (this.state.dataList.length>(this.state.yeNum+1)*this.state.xianShiNum) {
                    
                    this.state.yeNum++;

                    var newArr=[]

                    var danQianNum = this.state.dataList.length - this.state.yeNum*this.state.xianShiNum

                    if (danQianNum >this.state.xianShiNum) {
                        danQianNum = (this.state.yeNum+1)*this.state.xianShiNum
                    } else{
                        danQianNum = this.state.dataList.length
                    }           
                    for(var i=this.state.yeNum*this.state.xianShiNum;i<danQianNum;i++) {
                        newArr.push(this.state.dataList[i])
                    }

                    this.setState({
                           
                        xianShiArr:newArr,
                        yeNum:this.state.yeNum,
                        // (testData.length >= result['data']['total']) ? RefreshState.NoMoreData : RefreshState.Idle,
                    })

                 } else {
                    // this.refs.toast.show("没有更多数据的", DURATION.LENGTH_LONG);

                    // this.getDataList(false)

                    if (this.state.xianShiArr.length == this.state.xianShiNum) {
                        this.getDataList(false)
                    } else {
                        this.refs.toast.show("没有更多数据的", DURATION.LENGTH_LONG)
                    }
                 }

            }}>
                <Text
                    editable={false}

                    style={{marginLeft:20,height:35,borderRadius:5,
                        width:(sys.dwidth-80)/3,borderColor:sys.silveryColor,
                        borderWidth:1,textAlign:'center',
                         fontSize:13,textAlignVertical:'center',
                         ...Platform.select({
                             ios: { lineHeight: 35},
                                 android: {}
                         })}}
                >
                    下一页
                </Text>
            </TouchableOpacity>


        </View>

        </View>
 

    }

    itemClick(e){

        const {navigate} = this.props.navigation;
        navigate('OrderDetail',{item:e})
    }

    // chaXun(){
//         jqueryGridPage: 1
// jqueryGridRows: 10
// trano: 
// expect: 
// startime: 2020-02-01
// endtime: 2020-03-01
// loginname: 
// state:

//AppApijiekou.downuserbetslist
    // }

    // dingDanNum:'',
    // qihaoNum:'',
    // acountNum:'',

    getDataList(isReload){


        if (this.state.isloading) return;

        if (!this.state.isChaXun) {
           
            if (this.state.startDate.length && this.state.endDate.length) {

                if (this.timeWithDataStr(this.state.startDate) > this.timeWithDataStr(this.state.endDate)) {
    
                    this.refs.toast.show("初始时间不能大于结束时间", DURATION.LENGTH_LONG);
    
                    return false
                }
    
            }
        }
        const {params} =this.props;
        let codeurl = host + '/AppApijiekou.downuserrechargeandwithdrawlist';
        let formData = new FormData();

        let typeStr=0

        if (this.state.leiXin == '提款') {
            typeStr = 1
        }


        let zhuantai = '';
        if (this.state.zhuangtai == '正在处理') {
            zhuantai=0;
        }
        if (this.state.zhuangtai == '审核通过') {
            zhuantai=1;
        }
        if (this.state.zhuangtai == '取消申请') {
            zhuantai=-1;
        }

        this.setState({
            isloading:true,
        })

        let pagesize = parseInt((sys.dheight*4/7 - 45 - 35)/35)

        formData.append('page',this.state.currentPage);
        formData.append('pagesize',pagesize);
        formData.append('trano',this.state.dingDanNum);
        formData.append('loginname',this.state.acountNum);
        formData.append('startime',this.state.startDate);
        formData.append('endtime',this.state.endDate);
        formData.append('state',zhuantai);
        formData.append('type',typeStr);

        HttpUtils.myPost(codeurl,formData)
            .then(result=>{


                // console.log('000')

                // console.log(result)

                this.setState({
                    isloading:false,
                    isChaXun:false,
                })

                if(result['respCode']==1) {
                    
                    let testData = result['data']['rows'];

                    if (testData == null) {
                        this.refs.toast.show("没有数据的", DURATION.LENGTH_LONG);
                    }

                    let newList = testData.map((data) => {
                        return {
    
                            actualamount: data["actualamount"],
                            actualfee:  data["actualfee"],
                            amount: data["amount"],
                            fee: data["fee"],
                            fuyanma: data["fuyanma"],
                            id: data["id"],
                            isauto: data["isauto"],
                            newaccountmoney: data["newaccountmoney"],
                            oddtime: data["oddtime"],
                            oldaccountmoney: data["oldaccountmoney"],
                            payname: data["payname"],
                            paytype: data["paytype"],
                            paytypetitle: data["paytypetitle"],
                            remark:  data["remark"],
                            sdtype: data["sdtype"],
                            state: data["state"],
                            stateadmin:  data["stateadmin"],
                            threetrano: data["threetrano"],
                            trano: data["trano"],
                            uid:  data["uid"],
                            username: data["username"],
                        }
                    });
    
                    let dataList =  isReload ? newList : [...this.state.dataList, ...newList]
                    // this.state.currentPage = parseInt(dataList.length/this.state.pageSize)+2;


                    if (this.state.currentPage == 0) {
                        this.state.yeNum = 0
                        this.state.xianShiArr = dataList 
                    } else {
                        if (newList.length) {
                            this.state.yeNum = this.state.currentPage-1
                            this.state.xianShiArr = newList
                        } else {
                            this.refs.toast.show("没有数据的", DURATION.LENGTH_LONG);
                        }
                        
                    }

                    if (newList.length) {
                        this.state.currentPage++
                    }
                
                    this.setState({
                        xianShiArr:this.state.xianShiArr,
                        
                        dataList: dataList,
                        refreshState: (dataList.length >= result['data']['total']) ? RefreshState.NoMoreData : RefreshState.Idle,
                    })
                } else {
                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                }

                

            })
            .catch(error=>{
               
                this.setState({
                    isloading:false,
                    isChaXun:false,
                    refreshState: RefreshState.Idle,
                })

            })

    }


}


const styles = StyleSheet.create({

    // content:{
    //     backgroundColor:sys.backgroundColor,

    //     width:sys.dwidth,

    //     bottom:10,
    // },


    contengtext:{
        // marginTop:5,
        marginBottom:10,
        left:10,
        fontSize:14,
    
  
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


    textStyle:{
        fontSize:18,
      },
      container:{
        flex:1,
        justifyContent:'center',
        alignItems:'center'
      },
      content:{
        width:'100%',
        flexDirection:'row',
        justifyContent:'space-around'
      },
      bankNameView:{
        marginTop:1,
        flexDirection:'row',
        height:40,
        marginLeft:10,
        borderRadius:5,
        width:(sys.dwidth-40)/3,
        borderColor:sys.backgroundColor,
        borderWidth:1
        },
    rightText:{

        marginLeft:10,
    
        height:40,

        borderRadius:5,
        width:(sys.dwidth-40)/3,
        borderColor:sys.silveryColor,
        borderWidth:1,
        textAlign:'center',
         fontSize:13,
         textAlignVertical:'center',
         ...Platform.select({
             ios: { lineHeight: 40},
                 android: {}
         })

    },
    
    inputText:{

        width:(sys.dwidth-40)/3,
        color:sys.subTitleColor,
        textAlign:'center',
        marginLeft:10,
        borderColor:sys.silveryColor,
        borderRadius:5,
        borderWidth:1,
        fontSize:13,
         height:40,
        //backgroundColor:'red'

    }


})

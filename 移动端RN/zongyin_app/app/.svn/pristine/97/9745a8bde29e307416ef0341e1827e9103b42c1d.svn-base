/**
 * Created by 卓原 on 2017/10/31.
 * zhuoyuan93@gmail.com
 */
import React from 'react';
import {
    View,
    Text,
    StyleSheet,
    TextInput,
    TouchableOpacity,
    Alert,
    ActivityIndicator,
    Platform,
    DeviceEventEmitter,
    NativeModules,
    ScrollView,
    Image
} from 'react-native';


import RefreshListView,{RefreshState} from 'react-native-refresh-list-view'
import {sys,NoDataView,isIphoneX} from "../../common/Data"
import {RadioGroup, RadioButton} from 'react-native-flexi-radio-button'
const host = sys.host;
import HttpUtils from "../../common/HttpUtil";
import Toast,{DURATION} from 'react-native-easy-toast';
import { Header } from 'react-navigation';


const { StatusBarManager } = NativeModules;


// var this;


const putongKaiHuData = ['1.自动注册的会员初始密码为“123456”','2.为提高服务器效率，系统'+
             '将自动清理注册一个月没有充值，或两个月未登录，并且金额低于10元的帐户','3.固定推广链接：']




export default class DelegateKaiHuCenter extends React.Component {
    // static  navigationOptions = ({navigation}) => ({
    //     headerTitle:"开户中心",
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
                headerTitle:"开户中心",
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
            headerTitle:"开户中心",
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
          
            headViewIndext:0,
            isProxy:0,
            refreshState: RefreshState.HeaderRefreshing,
            dataList:[],
            isloading:false,
            fandianTs:'返点在0.0-0.0',
            fanshuiTs:'返水在0.0-0.0',
            lijieStr:null,
            rebate:'',
            fanshuirebate:'',
            username:'',
            shiyongNum:'',
            isTiJiao:false,
            currentPage:1,
            list:[
                {
                    index:1,
                    radioList: [
                        {
                         "id":"0",
                         "name":"玩家",
                        },
                        {
                         "id":"1",
                         "name":"代理",
                        }
                    ],
                    // putongKaiHuData:['1.自动注册的会员初始密码为“123456”','2.为提高服务器效率，系统'+
                    // '将自动清理注册一个月没有充值，或两个月未登录，并且金额低于10元的帐户','3.固定推广链接：'],
                }
            
            ],
        }
    }

    onHeaderRefresh = () => {
        this.state.currentPage = 1;
        this.setState(
            {
                refreshState: RefreshState.HeaderRefreshing,
            },()=>{
                this.lianjiMangerData(true)
            })
    }



    onFooterRefresh = () => {
        // this.state.currentPage = this.state.currentPage+1;
        this.setState({
            refreshState: RefreshState.FooterRefreshing
        },()=>{
            this.lianjiMangerData(false)
        })
    }

    componentDidMount(){

        

        this.getPersonInfo(0)

        // 登录成功通知
         DeviceEventEmitter.addListener('ChangeUI',(dic)=>{
            //接收到详情页发送的通知，刷新首页的数据，改变按钮颜色和文字，刷新UI
            if(dic['islogin'] != 0){
                this.getPersonInfo(0)
            }
        }); 

    }


    getPersonInfo(num){
        let codeurl = host + '/AppMember.index.do';
        let formData = new FormData();
        HttpUtils.post(codeurl,formData)
            .then(result=>{
                if(result['respCode']==1){  
                    let dic = result['data'];
                    global.user.loginState=1;
                    global.user.userData=dic;
                    global.user.token=dic.token;
                    storage.save('islogin',dic)

                   

                    this.setState({
                        fandianTs:'返点在0.0-'+dic['fandian'],
                        fanshuiTs:'返点在0.0-'+dic['fanshui'],
                        lijieStr:sys.host+'/Public.register.tgid'+dic['id']
                    })




                }else{
                    storage.save('islogin', "")
                    global.user.loginState=0;
                    global.user.userData=null;
                    global.user.token="";
                      
                }
            })
            .catch(error=>{
                
                if(num<=0){
                    this.getPersonInfo(num+1);
                } else{
                
                    this.refs.toast.show('提示：'+error+'', DURATION.LENGTH_LONG);
                }
            })
    }



    render(){

      
        // this = this;

        let hedView = <View style={{marginTop:30,marginLeft:10,width:sys.dwidth - 20,height:30,borderColor:sys.mainColor,borderWidth:1
            ,borderRadius:5,flexDirection:'row',}}>

               <TouchableOpacity onPress={()=>this.headViewClick(0)}  activeOpacity={1}>
                   <View style={{backgroundColor:this.state.headViewIndext==0?sys.mainColor:sys.whiteColor,
                   width:(sys.dwidth - 20-2)/3,height:28,marginLeft:0,borderTopLeftRadius:5,borderBottomLeftRadius: 5,
                   }}>
                   <Text style={{color:this.state.headViewIndext==0?sys.whiteColor:sys.mainColor,
                   textAlign:'center',textAlignVertical:'center',
                   width:(sys.dwidth - 20-2)/3 - 10,height:28,marginLeft:5,fontSize:14,...Platform.select({ ios: { lineHeight: 26},
                   android: {} })}} numberOfLines={1}>普通开户</Text>

                   </View>

               </TouchableOpacity>
            
               <View style={{width:1,height:29,backgroundColor:sys.mainColor}}></View>
               <TouchableOpacity onPress={()=>this.headViewClick(1)}  activeOpacity={1}>
                   
                   <Text style={{color:this.state.headViewIndext==1?sys.whiteColor:sys.mainColor,
                   backgroundColor:this.state.headViewIndext==1?sys.mainColor:sys.whiteColor,
                   textAlign:'center',textAlignVertical:'center',
                   width:(sys.dwidth - 20-2)/3,height:28,marginLeft:0,fontSize:14,...Platform.select({ ios: { lineHeight: 26},
                   android: {} })}} numberOfLines={1}>链接开户</Text>

               </TouchableOpacity>
            
               <View style={{width:1,height:29,backgroundColor:sys.mainColor}}></View>

               <TouchableOpacity onPress={()=>this.headViewClick(2)}  activeOpacity={1}>
               <View style={{backgroundColor:this.state.headViewIndext==2?sys.mainColor:sys.whiteColor,
                   width:(sys.dwidth - 20-2)/3-2,height:28,marginLeft:0,borderTopRightRadius:5,borderBottomRightRadius:5,
                   }}>
                   <Text style={{color:this.state.headViewIndext==2?sys.whiteColor:sys.mainColor,
                   textAlign:'center',textAlignVertical:'center',
                   width:(sys.dwidth - 20-2)/3-10,height:28,marginLeft:5,fontSize:14,...Platform.select({ ios: { lineHeight: 26},
                   android: {} })}} numberOfLines={1}>链接管理</Text>

                   </View>

               </TouchableOpacity>

           </View>


           let footView = null;

        if (this.state.headViewIndext==2) {
            footView =this.state.dataList.length?<RefreshListView
                    data={this.state.dataList}
                    keyExtractor={this.keyExtractor}
                    renderItem={this.renderCell}
                    refreshState={this.state.refreshState}
                    onHeaderRefresh={this.onHeaderRefresh}
                    onFooterRefresh={this.onFooterRefresh}
                    numColumns ={1}
                    ListHeaderComponent={this.renderHeadView}
                    // horizontal={false}
                    ItemSeparatorComponent={this._separator}
                    // 可选
                    footerRefreshingText= '玩命加载中 >.<'
                    footerFailureText = '我擦嘞，居然失败了 =.=!'
                    footerNoMoreDataText= '-我是有底线的-'
                />:<NoDataView click={()=>this.lianjiMangerData(true)}
                isloading={this.state.isloading}
                text = {this.state.isloading?"加载中":"暂无数据,点击重新加载"}/>

        } else {
            
            
            footView = this.footViewScrollView()


        }
           
           
           

        return(

        <View style={{marginTop:1,width:sys.dwidth,height:'100%',backgroundColor:'white'}}>
            {hedView}
            {footView}

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



     footViewScrollView() {
        let attributes=[];
        let list = this.state.list;
        // console.log("0000000");
        list.map((item,i)=>{
            attributes.push(
            <View key={i}>
                <View >
                <RadioGroup style={{flexDirection:'row',backgroundColor:sys.whiteColor,
                  width:sys.dwidth-120, height:50}}
                        onSelect = {(index, value) => this.onSelect(index, value)}
                        size={20}
                        selectedIndex={0}
                        thickness ={2}>
                        {
                          item.radioList.map((elem,radioIndex) => {
                              return (

                                  <RadioButton style={{marginTop:8,width:(sys.dwidth-120)/3}} key={radioIndex} value={elem.id} >
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

//          Header.HEIGHT-
// 105-70-45 - (Platform.OS=='ios'?(isIphoneX()?48:0):StatusBarManager.HEIGHT+10),
        let scrollH = sys.dheight - Header.HEIGHT  - (Platform.OS=='ios'?(isIphoneX()?48:0):StatusBarManager.HEIGHT+10)

        if (this.state.headViewIndext == 0) {


         let gudLjstr = putongKaiHuData[2]
         if (this.state.lijieStr!=null) {
            gudLjstr = gudLjstr+this.state.lijieStr
         }

         return<ScrollView
            style={{
                backgroundColor:"white",
                width:sys.dwidth,
                height:scrollH
            }}
            >
            <View style={{marginLeft:10,marginTop:10,width:sys.dwidth -20,borderRadius:5,borderColor:sys.silveryColor,borderWidth:1}}>
                <Text style={{marginLeft:10,marginTop:10,fontSize:15}}>温馨提示:</Text>

                {putongKaiHuData.map((info, index) => {
                    return (
                       <Text key={index} style={{marginLeft:10,marginTop:5,fontSize:14,width:sys.dwidth - 40}}>{index==2?gudLjstr:info}</Text>
                    );
                })}

                <View style={{height:10}}></View>
            </View>

            <View style={{marginTop:15,marginLeft:10,backgroundColor:sys.silveryColor,
                height:0.5,width:sys.dwidth-20}}></View>


            <View style={{marginTop:1,flexDirection:'row',backgroundColor:sys.whiteColor,
               height:50,width:sys.dwidth}}>

                 <Text style={{marginLeft:20,marginTop:17,width:90,backgroundColor:sys.whiteColor,fontSize:16,}}>开户类别</Text>
          
                 <View>
                     {attributes}
                 </View>
            </View>

            <View style={{marginTop:0,marginLeft:10,backgroundColor:sys.silveryColor,
                height:0.5,width:sys.dwidth-20}}></View>

            <View style={{marginTop:0,flexDirection:'row',backgroundColor:sys.whiteColor,
                height:50,width:sys.dwidth}}>
              <Text style={{marginLeft:20,marginTop:17,width:90,backgroundColor:sys.whiteColor,ontSize:16,
               }}>开户名</Text>

              <TextInput style={{width:sys.dwidth-120,color:sys.subTitleColor,
               textAlign:'left',backgroundColor:sys.whiteColor,}}
                         ref={(c) => this.textInput = c}
                         underlineColorAndroid='transparent'
                         placeholder={"输入用户名"}
                         value={this.state.username}
                         onChangeText={(text) => this.setState({username:text})}
              >
              </TextInput>
            </View>

            <View style={{marginTop:0,marginLeft:10,backgroundColor:sys.silveryColor,
                height:0.5,width:sys.dwidth-20}}></View>

            <View style={{marginTop:1,flexDirection:'row',backgroundColor:sys.whiteColor,
                        height:50,width:sys.dwidth}}>
                <Text style={{marginLeft:20,marginTop:17,width:90,
                        backgroundColor:sys.whiteColor,fontSize:16,
                }}>彩票返点</Text>

              <TextInput style={{width:sys.dwidth-120,color:sys.subTitleColor,textAlign:'left',
                        backgroundColor:sys.whiteColor,}}
                         ref={(c) => this.textInput = c}
                         underlineColorAndroid='transparent'
                         placeholder={this.state.fandianTs}
                         value={this.state.rebate}
                         onChangeText={(text) => this.setState({rebate:text})}
              >
              </TextInput>
            </View>


            <View style={{marginTop:1,flexDirection:'row',backgroundColor:sys.whiteColor,
                        height:50,width:sys.dwidth}}>
                <Text style={{marginLeft:20,marginTop:17,width:90,
                        backgroundColor:sys.whiteColor,fontSize:16,
                }}>彩票返水</Text>

              <TextInput style={{width:sys.dwidth-120,color:sys.subTitleColor,textAlign:'left',
                        backgroundColor:sys.whiteColor,}}
                         ref={(c) => this.textInput = c}
                         underlineColorAndroid='transparent'
                         placeholder={this.state.fanshuiTs}
                         value={this.state.fanshuirebate}
                         onChangeText={(text) => this.setState({fanshuirebate:text})}
              >
              </TextInput>
            </View>


            <View style={{marginTop:0,marginLeft:10,backgroundColor:sys.silveryColor,
                height:0.5,width:sys.dwidth-20}}></View>

            <TouchableOpacity disabled={this.state.isTiJiao} style={{flexDirection:'row',width:sys.dwidth-40,marginLeft:20,
            marginTop:40,height:40,backgroundColor:sys.mainColor,justifyContent:'center',
                borderRadius:5,}} onPress={()=>this.tijiao(this.state.headViewIndext)}>
            {this.state.isTiJiao?<ActivityIndicator />:null}
            <Text style={styles.buttontext}>{this.state.isTiJiao?'提交中..':'提交'}</Text>
            </TouchableOpacity>

            <View style={{height:20}}></View>


        </ScrollView>

    } else if (this.state.headViewIndext == 1) {
        return<ScrollView
            style={{
                backgroundColor:"white",
                width:sys.dwidth,
                height:scrollH
            }}
            >
            <View style={{marginTop:1,flexDirection:'row',backgroundColor:sys.whiteColor,
               height:50,width:sys.dwidth}}>

                 <Text style={{marginLeft:20,marginTop:17,width:90,backgroundColor:sys.whiteColor,fontSize:16,}}>开户类别</Text>
          
                 <View>
                     {attributes}
                 </View>
            </View>

            <View style={{marginTop:0,marginLeft:10,backgroundColor:sys.silveryColor,
                height:0.5,width:sys.dwidth-20}}></View>

            <View style={{marginTop:0,flexDirection:'row',backgroundColor:sys.whiteColor,
                height:50,width:sys.dwidth}}>
              <Text style={{marginLeft:20,marginTop:17,width:90,backgroundColor:sys.whiteColor,ontSize:16,
               }}>使用次数</Text>

              <TextInput style={{width:sys.dwidth-120,color:sys.subTitleColor,
               textAlign:'left',backgroundColor:sys.whiteColor,}}
                         ref={(c) => this.textInput = c}
                         underlineColorAndroid='transparent'
                         placeholder={"输入使用次数"}
                         value={this.state.shiyongNum}
                         onChangeText={(text) => this.setState({shiyongNum:text})}
              >
              </TextInput>
            </View>
            <View style={{marginTop:0,marginLeft:10,backgroundColor:sys.silveryColor,
                height:0.5,width:sys.dwidth-20}}></View>
            <View style={{marginTop:1,flexDirection:'row',backgroundColor:sys.whiteColor,
                        height:50,width:sys.dwidth}}>
                <Text style={{marginLeft:20,marginTop:17,width:90,
                        backgroundColor:sys.whiteColor,fontSize:16,
                }}>彩票返点</Text>

              <TextInput style={{width:sys.dwidth-120,color:sys.subTitleColor,textAlign:'left',
                        backgroundColor:sys.whiteColor,}}
                         ref={(c) => this.textInput = c}
                         underlineColorAndroid='transparent'
                         placeholder={this.state.fandianTs}
                         value={this.state.rebate}
                         onChangeText={(text) => this.setState({rebate:text})}
              >
              </TextInput>
            </View>

            <View style={{marginTop:1,flexDirection:'row',backgroundColor:sys.whiteColor,
                        height:50,width:sys.dwidth}}>
                <Text style={{marginLeft:20,marginTop:17,width:90,
                        backgroundColor:sys.whiteColor,fontSize:16,
                }}>彩票返水</Text>

              <TextInput style={{width:sys.dwidth-120,color:sys.subTitleColor,textAlign:'left',
                        backgroundColor:sys.whiteColor,}}
                         ref={(c) => this.textInput = c}
                         underlineColorAndroid='transparent'
                         placeholder={this.state.fanshuiTs}
                         value={this.state.fanshuirebate}
                         onChangeText={(text) => this.setState({fanshuirebate:text})}
              >
              </TextInput>
            </View>

            <View style={{marginTop:0,marginLeft:10,backgroundColor:sys.silveryColor,
                height:0.5,width:sys.dwidth-20}}></View>

            <TouchableOpacity disabled={this.state.isTiJiao} style={{flexDirection:'row',width:sys.dwidth-40,marginLeft:20,
            marginTop:40,height:40,backgroundColor:sys.mainColor,justifyContent:'center',borderRadius:5,}} onPress={()=>this.tijiao(this.state.headViewIndext)}>
            {this.state.isTiJiao?<ActivityIndicator />:null}
            <Text style={styles.buttontext}>{this.state.isTiJiao?'提交中..':'提交'}</Text>
            </TouchableOpacity>
            <View style={{height:20}}></View>
            </ScrollView>
        } 
    }


     headViewClick(num) {

        if (this.state.headViewIndext == num) return;

        this.setState({
            isProxy:0,
            headViewIndext:num,
            username:'',
            rebate:'',
            fanshuirebate:'',
            shiyongNum:''        
         })

         if (num==2) {
            this.state.currentPage = 1;
           
            this.lianjiMangerData(true)
         }
         
     }


     onSelect(index, value){

        this.state.isProxy = index;
    }

     renderCell = (item) => {

            if (this.state.dataList.length <= 0) return

            var follow = this.state.dataList[item.index];

            let yonghuLeixi = '普通用户'

            if (follow.proxy==1) {
                yonghuLeixi = '代理'
            }
        
            return <View style={{marginTop:10,width:sys.dwidth,backgroundColor:'white'}}>
                    
            <View style={{marginTop:10,marginBottom:10,flexDirection:'row',width:sys.dwidth,backgroundColor:'white'}}>
                
                <Text style={{width:sys.dwidth - 50,left:10}}>{yonghuLeixi}</Text>

                <TouchableOpacity style={{width:40}} onPress={()=>this.chakan(follow.id)}>
                    <Text style={{color:sys.mainColor}}>查看</Text>
                </TouchableOpacity> 
                
            </View>
        
            <Text style={styles.contengtext}>{"使用次数："+follow.usenum+'，已使用：'+follow.okusenum}</Text>

            <View style={{marginLeft:10,height:0.5,width:sys.dwidth-20,marginTop:5,backgroundColor:sys.silveryColor}}></View>
        
       </View>
        

        
    }



    chakan(idStr){

         let codeurl = host + '/Public.register.linkid.'+idStr

        // Alert.alert(codeurl)
    }

    tijiao(num){

        if (num==0) {
             this.adduser()
        }
        
        if (num==1) {
            this.addsignup()
        }

    }


    lianjiMangerData(isReload){
        let codeurl = host + '/AppApijiekou.signuplinklist'

        let formData = new FormData();

        formData.append('jqueryGridPage',this.state.currentPage);
        formData.append('jqueryGridRows',10);

        this.setState({
            isTiJiao:true,
            isloading:true,
        })

        HttpUtils.post(codeurl,formData)
            .then(result=>{

                this.setState({
                    isTiJiao: false,
                });
         
                if(result['respCode']==1){

                    let testData = result['data']['rows'];

                    let totalpage = result['data']['totalPage'];

                    let newList = testData.map((data) => {
                        return {
                            fandian: data["fandian"],
                            id: data["id"],
                            oddtime: data["oddtime"],
                            okusenum: data["okusenum"],
                            proxy: data["proxy"],
                            tpltype: data["tpltype"],
                            uid: data["uid"],
                            usenum:  data["usenum"],
                            username:data['username'],
                            
                        }});


                    let dataList =  isReload ? newList : [...this.state.dataList, ...newList]
                  
                    if (newList.length) {
                        this.state.currentPage++
                    }

                    this.setState({
                        isloading:false,
                        dataList: dataList,
                        refreshState: (this.state.currentPage >= totalpage) ? RefreshState.NoMoreData : RefreshState.Idle,
                    })

                }



            })
            .catch(error=>{

                this.setState({
                    isloading:false,
                    refreshState: RefreshState.Idle,
                    isTiJiao: false,
                })

            })

    }

    addsignup(){
        let codeurl = host + '/AppApijiekou.addsignup'

        let formData = new FormData();

        if (this.state.shiyongNum == '' || this.state.shiyongNum==null) {
     

            this.refs.toast.show("请输入使用次数", DURATION.LENGTH_LONG);
            
            return;
        }
        if (this.state.rebate == '' || this.state.rebate==null) {

            this.refs.toast.show("请输入返点", DURATION.LENGTH_LONG);
            return;
        }

        if (this.state.fanshuirebate == '' || this.state.fanshuirebate==null) {

            this.refs.toast.show("请输入返水", DURATION.LENGTH_LONG);
            return;
        }


        

        this.setState({
            isTiJiao:true,
        })

        formData.append('times',this.state.shiyongNum);
        formData.append('isProxy',this.state.isProxy);
        formData.append('rebate',this.state.rebate);
        formData.append('rebateFanshui',this.state.fanshuirebate);
        formData.append('tpltype',0);

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                this.setState({
                    isTiJiao:false,
                })
                if(result['respCode']==1){  
                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                    
     
                    
                    this.setState({
                        shiyongNum:'',
                        rebate:'',
                        fanshuirebate:''
                    })

                }
            })
            .catch(error=>{
                this.refs.toast.show('提示：'+error+'', DURATION.LENGTH_LONG);
            
                this.setState({
                    isTiJiao:false,
                })
            })
    }

    adduser() {
        

        let codeurl = host + '/AppApijiekou.adduser';
        let formData = new FormData();


        

        if (this.state.username == '' || this.state.username==null) {
          
            this.refs.toast.show("请输入用户名", DURATION.LENGTH_LONG);
            return;
        }
        if (this.state.rebate == '' || this.state.rebate==null) {

            this.refs.toast.show("请输入返点", DURATION.LENGTH_LONG);
            return;
        }

        if (this.state.fanshuirebate == '' || this.state.fanshuirebate==null) {

            this.refs.toast.show("请输入返水", DURATION.LENGTH_LONG);
            return;
        }

        

        this.setState({
            isTiJiao:true,
        })
        formData.append('username',this.state.username);
        formData.append('isProxy',this.state.isProxy);
        formData.append('rebate',this.state.rebate);
        formData.append('rebateFanshui',this.state.fanshuirebate);
        

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                this.setState({
                    isTiJiao:false,
                })
                if(result['respCode']==1){  
            
                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                    
                    this.setState({
                        username:'',
                        rebate:'',
                        fanshuirebate:''
                    })

                }
            })
            .catch(error=>{
        
                this.refs.toast.show('提示：'+error+'', DURATION.LENGTH_LONG);
                this.setState({
                    isTiJiao:false,
                })
            })


    }


}


const styles = StyleSheet.create({

    content:{
        backgroundColor:sys.backgroundColor,

        width:sys.dwidth,

        bottom:10,
    },


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
    bankNameView:{
        marginTop:1,
        flexDirection:'row',
        height:40,
        marginLeft:10,
        borderRadius:5,
        width:(sys.dwidth-30)/2,
        borderColor:sys.backgroundColor,
        borderWidth:1
        },
        rightText:{

            marginLeft:10,
        
            height:40,
    
            borderRadius:5,
            width:(sys.dwidth-30)/2,
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
    
            width:(sys.dwidth-30)/2,
            color:sys.subTitleColor,
            textAlign:'left',
            marginLeft:10,
            borderColor:sys.silveryColor,
            borderRadius:5,
            borderWidth:1,
            fontSize:13,
             height:40,
            //backgroundColor:'red'
    
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


})

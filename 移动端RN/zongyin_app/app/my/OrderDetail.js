
import React from 'react';
import {
    View,
    Text,
    StyleSheet,
    ScrollView,
    TouchableOpacity,
    Alert,
    Switch,
    Image,
    ImageBackground,
    Platform,
} from 'react-native';
import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'
import {sys,NoDataView} from "../common/Data"
const host = sys.host;

import HttpUtils from "../common/HttpUtil"

import Toast,{DURATION} from 'react-native-easy-toast';//导入弹出框组件


export default class OrderDetail extends React.Component {


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
                headerTitle: params.item.title?params.item.title:'订单详情',
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
            headerTitle: params.item.title?params.item.title:'订单详情',
            headerTitleStyle:{
                alignSelf:'center',
                flex: 1,
                textAlign: 'center',
    
            },
            headerRight: <View />
        }
    };


    rightItemClick(){

    }

    constructor(props) {
        super(props)

        this.state = {
            dataList: [],
            refreshState: RefreshState.HeaderRefreshing,
            currentPage:1,
            pageSize:20,
            visible:false,
            isXiaXian:false
        }
    }

   

    componentDidMount(){


        const {params} =this.props.navigation.state;

        console.log('params.item')
        console.log(params.item)

        this.getDataList(0);
    }


    getDataList(num){


       // AppApijiekou.chedan

       this.setState({
        visible:true
       })

        const {params} =this.props.navigation.state;

        let codeurl = host + '/AppMember.betRecord_detail.id.'+params.item.id+'.do';

        let formData = new FormData();

        HttpUtils.post(codeurl,formData)
            .then(result=>{

                this.setState({
                    visible:false
                   })

                let testData = result['data'];

                let newList = testData.map((data) => {
                    return {
                        Chase: data["Chase"],
                        amount: data["amount"],
                        amountafter: data["amountafter"],
                        amountbefor: data["amountbefor"],
                        beishu: data["beishu"],
                        cpname: data["cpname"],
                        cptitle: data["cptitle"],
                        expect:  data["expect"],
                        id: data["id"],
                        isdraw:  data["isdraw"],
                        itemcount: data["itemcount"],
                        mode: data["mode"],
                        oddtime:  data["oddtime"],
                        okamount: data["okamount"],
                        okcount: data["okcount"],
                        opencode:  data["opencode"],
                        playid: data["playid"],
                        playtitle: data["playtitle"],
                        rate:data["rate"],
                        repoint: data["repoint"],
                        repointamout:data["repointamout"],
                        source: data["source"],
                        stopChase: data["stopChase"],
                        trano: data["trano"],
                        typeid: data["typeid"],
                        tzcode: data["tzcode"],
                        uid: data["uid"],
                        username: data["username"],
                        wanfatip:data["wanfatip"],
                        yingli:data["yingli"],
                        yjf: data["yjf"],
                        cdsxf:data['cdsxf'],
                        
                    }
                });

                 let dataList = newList
                // let state = dataList.length > result['data']['total'] ? RefreshState.NoMoreData : RefreshState.Idle;
                //Alert.alert( result['data']['total']+"<<"+this.state.currentPage+">>>"+dataList.length)
                // if(!isReload){
                //     this.state.currentPage += 1
                // }
                this.setState({
                    dataList: dataList,
                   // refreshState: (dataList.length >= result['data']['total']) ? RefreshState.NoMoreData : RefreshState.NoMoreData,
                 })

            })
            .catch(error=>{
                //Alert.alert(error)
                this.setState({
                    refreshState: RefreshState.Idle,
                })

                this.setState({
                    visible:false
                   })
                if (num<=2) {
                    this.getDataList(num+1)
                }
            })

    }

    render() {

    
        const {params} =this.props.navigation.state;
        if (params.item.hasOwnProperty('isXiaXian')) {
            this.state.isXiaXian=true
        }

        var changView = null;
        if (this.state.dataList.length) {
            changView = this.itemView(0)
        }else{
            changView = <NoDataView click={()=>this.getDataList(true)}
                         isloading = {true}
                         text = {"加载中"}></NoDataView>
        }
        
        return(

            <View style={styles.container}>

                {changView}
                <Toast  //提示
                    ref="toast"
                    style={{backgroundColor:'gray'}}
                    position='center'
                    positionValue={200}
                    opacity={0.7}
                    textStyle={{color:'white'}}
                />  
            </View>

        )


    }


    itemView(index){


        var isKaijian = false

        var follow = this.state.dataList[index];

        var meizhuPrice = (follow.amount/(follow.beishu*follow.itemcount))*follow.yjf


        var text = this.state.isXiaXian?'还没开奖':'还没开奖哦！耐心等一下！'
        var color = sys.subTitleColor
        if(follow.isdraw==1){
            text = this.state.isXiaXian?'中奖啦！！！':'恭喜您中奖啦！！！';
            color = sys.redColor
            isKaijian = true;
        }else if(follow.isdraw==-1){
            text = '噢NO～没中奖！';
            color = sys.blueColor
            isKaijian = true;
        } else if (follow.isdraw == -2) {
            text = '已撤单';
            color = sys.blueColor
            isKaijian = false;
        } 

        var userNameView=null

        if (this.state.isXiaXian) {
            userNameView = <Text style={[styles.nametext,{fontWeight: 'bold'}]}>{"用户名:"+follow.username}</Text>
        }

        var delView = (text == '还没开奖哦！耐心等一下！')?  <TouchableOpacity onPress={()=>this.quedingchedan()}>
        <Text style={[styles.nametext,{color:sys.blueColor}]}>是否撤单?</Text>
        </TouchableOpacity>:null;

        var yingliColor = '#333333'
        var yingli = '等开奖'
        if(isKaijian && follow.yingli != null && follow.yingli != ''){
             yingli = follow.yingli+''
           
            if(yingli.includes('-')){
                yingliColor = 'green'
            }else{
                yingliColor = sys.mainColor
            }
            
        }

        var wanfatip = follow.wanfatip +''
        if(wanfatip.substr(wanfatip.length-1,1) == ',' || wanfatip.substr(wanfatip.length-1,1) == '，'){
            wanfatip = wanfatip.substr(0,wanfatip.length-1)
        }



        return <ScrollView>

            <View>
            <View key={index} style={styles.content}>
                <View style={styles.leftStyle}>
                    <Text style={{marginLeft:10,fontSize:16,marginTop: 0,color:sys.titleColor,fontWeight: 'bold'}}>{follow.cptitle+"("+follow.expect+")"}</Text>
                    <Text style={{marginLeft:10,marginTop: 10,color:color,width:190,textAlign:'left',top:0,fontWeight: 'bold',fontSize:14}}>{text}</Text>
                </View>
                {/* <View style={styles.rightStyle}>
                    
                </View> */}


                <TouchableOpacity style={{width:70, marginLeft:10,marginTop:15,height:35,
        backgroundColor:sys.mainColor,
        borderRadius:5}} onPress={()=>this.zailaiyizhu(follow)}>
                <Text style={[styles.buttontext]}>去下注</Text>
            </TouchableOpacity>

            </View>

            <View style={{width:sys.dwidth,}}>
                    {userNameView}
                    <Text style={styles.nametext}>{"单号:"+follow.trano}</Text>
                    <Text numberOfLines={1} style={styles.nametext}>{"玩法:"+follow.playtitle+"（"+follow.mode+"）"}</Text>


                    {/* <Text style={[styles.nametext]}>投注内容:<Text style={{color:sys.mainColor}}>{follow.tzcode}</Text></Text> */}


                    <View style={{flexDirection:'row',width:sys.dwidth}}>
                        <Text style={styles.nametext}>{"投注内容:"}</Text>
                        <Text style={styles.nameRighttext}>{follow.tzcode}</Text>
                    </View>


                    <View style={{flexDirection:'row',width:sys.dwidth}}>
                        <Text style={{marginLeft:10,fontSize:14,marginTop: 8,color:'#333333',fontWeight: 'bold'}}>{"开奖结果:"}</Text>
                        <Text style={{marginLeft:3,fontSize:14,marginTop: 8,color:'#333333',fontWeight: 'bold',flexWrap: 'wrap',width:sys.dwidth-80}}>{follow.opencode}</Text>
                    </View>

                    <View style={{flexDirection:'row',width:sys.dwidth}}>
                        <View >
                            <Text style={[styles.nametext]}>投注金额:<Text style={{color:'green'}}>{follow.amount}</Text></Text>
                            <Text style={[styles.nametext]}>中奖金额:<Text style={{color:sys.mainColor}}>{follow.okamount}</Text></Text>
                        </View>

                        <View style={{left:10}}>
                            <Text style={styles.nametext}>{"投注注数:"+follow.itemcount}</Text>
                            <Text style={styles.nametext}>{"中奖注数:"+follow.okcount}</Text>
                        </View>
                        
                   </View>


                    
                   
                   <Text style={[styles.nametext]}>单注金额: <Text style={{color:sys.mainColor}}>{meizhuPrice}</Text></Text>
                    <Text style={styles.nametext}>{"盈利:"}<Text style={{color:yingliColor}}>{yingli}</Text></Text>
                    <Text style={styles.nametext}>{"赔率:"+follow.rate}</Text>
                    <Text style={[styles.nametext,{width:sys.dwidth - 20}]}>{"下单时间:"+follow.oddtime}</Text>
                    <Text style={[styles.nametext,{width:sys.dwidth - 20}]}>{wanfatip}</Text>

                   

             
            </View>
            
            {delView}

            {/* <TouchableOpacity style={{width:70, marginLeft:10,marginTop:10,height:35,
        backgroundColor:sys.mainColor,
        borderRadius:5}} onPress={()=>this.zailaiyizhu(follow)}>
                <Text style={[styles.buttontext]}>去下注</Text>
            </TouchableOpacity> */}

           

            <View style={{height:10}}></View>

            </View>

        </ScrollView>


    }



    zailaiyizhu(follow) {


        const {navigate} = this.props.navigation;

        let item = {
            name:follow.cpname,
            title:follow.cptitle,
            typeid:follow.typeid
        }

        switch(follow.typeid){
                
            case 'ssc':
                navigate('Cqssc',{item:item})
                break;
            case 'k3':
                navigate('KuaiThreeAction',{item:item})
                break;

            case 'pk10':

                navigate('Pkten',{item:item});
                break;

            case 'lhc':
                navigate('Sixhc',{item:item});
                break;

            case 'keno':
                navigate('Keno',{item:item});
                break;
            case 'dpc':
                navigate('Dpc',{item:item});
                break;

            case 'x5':
                navigate('ElevenSelectFive',{item:item});
                break;

            case 'pcdd':                  
                navigate('PcDanDan',{item:item});

            default: //更多
                
                break;

        }

    }

    quedingchedan()
    {

        var follow = this.state.dataList[0];
        //'请先登录',''


        Alert.alert('撤单将扣除投注金额的'+follow['cdsxf']+'% 手续','',
        [
            {text:"取消", onPress: ()=>{

                }},
            {text:"确定", onPress: ()=>{

                this.revokeDan(follow);

            }}
        ]
        );
    }

    revokeDan(follow){

        //var follow = this.state.dataList[0];

        this.setState({
            visible:true
           })
        let codeurl = host + '/AppApijiekou.chedan';

        let formData = new FormData();

        formData.append('trano',follow.trano);

        HttpUtils.post(codeurl,formData)
            .then(result=>{
               // let testData = result['data'];
               this.setState({
                visible:false
               })
              // console.log(result)

                if(result['respCode']==1){


                    // this.props.navigation.goBack();

                    follow.isdraw = -2

                    this.setState({
                        dataList:this.state.dataList
                    })

                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);

                } else {
                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                }

            

            })
            .catch(error=>{
                this.refs.toast.show(error, DURATION.LENGTH_LONG);
         
                // Alert.alert(error)
                // this.setState({
                //     refreshState: RefreshState.Idle,
                // })

                this.setState({
                    visible:false
                   })
            })


    }

    itemClick(e){

        const {navigate} = this.props.navigation;
        navigate('CourseDetail',{item:e})
    }

    keyExtractor = (item, index) => {
        return index
    }



    changeGz(gzob,flistD) {

        if (gzob.isgz == 0) {
            gzob.isgz =1;
        } else {
            gzob.isgz =0;
        }


        this.setState({

            flistData:flistD
        })

    }

    renderCell = (item) => {

        return this.itemView(item.index)
    }


    _separator = () => {
        return <View style={{height:1,backgroundColor:'#f2f2f2'}}/>;
    }

}

const styles = StyleSheet.create({

    container: {
        flex:1,
        backgroundColor:sys.whiteColor
    },

    content:{
        backgroundColor:sys.backgroundColor,
        // height:182,
        width:sys.dwidth,
        flexDirection: 'row',
        paddingBottom:10,
    },

    buttontext:{
        // marginTop:11,
         color:'#ffffff',
         textAlign:'center',
         fontSize:17,
         height:35,
         textAlignVertical:'center',
         ...Platform.select({
             ios: { lineHeight: 35},
              android: {}
         })
     },

    leftStyle:{
        // backgroundColor:'white',
        marginTop:15,
        width:sys.dwidth - 100 ,
        // marginBottom:5
        // height:135
    },
    rightStyle:{
        marginTop:15,
        width:100,
        // height:135,
        backgroundColor:'white'
    },

    switchStyte:{
        width:80,
        height:30,
        position: 'absolute',
        bottom: 10,
        // left: 0,
        right: 15,
        resizeMode:'center'
    },
    numtext:{
        fontSize:14,
        color:'#333333',
        marginTop:10
    },
    nametext:{
        marginLeft:10,
        fontSize:14,
        lineHeight:20,
        marginTop: 8,
        color:'#333333',
      //  width:sys.dwidth - 20
    },
    nameRighttext:{
        marginLeft:3,
        fontSize:14,
        lineHeight:20,
       
        marginTop: 8,
        color:'#333333',
        flexWrap: 'wrap',
        paddingRight:10,
        width:sys.dwidth - 80,
     
    },

  


})

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
    ScrollView,
    Alert,
    Keyboard,
    DeviceEventEmitter,
    AsyncStorage,
    Platform,
    Picker,
    Animated,
    Easing,
    TouchableHighlight,
    ActivityIndicator,
} from 'react-native';

import {sys,isIphoneX} from "../common/Data"
import HttpUtils from "../common/HttpUtil"
const host =  sys.host;
const key = "/index/index/code"
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import MyPicker from 'react-native-picker'
// import PopupDialog from 'react-native-popup-dialog';

import Toast,{DURATION} from 'react-native-easy-toast';//导入弹出框组件

export default class BankCard extends React.Component {


    // static  navigationOptions = ({navigation}) => ({
    //     headerTitle:"绑定银行卡",
    //     headerTitleStyle:{
    //         alignSelf:'center',
    //     }
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
                headerTitle:"绑定银行卡",
                headerTitleStyle:{
                    alignSelf:'center',
                },
                headerLeft:leftView
            }

        }  

        return {
            headerTitle:"绑定银行卡",
            headerTitleStyle:{
                alignSelf:'center',
            }
        }
    };



    constructor(props){
        super(props)
        this.state = {
            bank_name:"",
            bank_code:"",
            pro_name:"",
            city_name:"",
            real_name:"",
            bank_card_number:"",
            regbankCardNumber:"",
            bankTradPwd:"",
            bankBranch:"",
            card_type:"",
            phone: '',
            code: '',
            userInfo:{},
            requestSucc:"",
            result:0,
            bankNameList:[],

            isBanDing:false,

            proNameList:[
                {
                     "北京市":[
                        "东城",
                        "西城",
                        "崇文",
                        "宣武",
                        "朝阳",
                        "丰台",
                        "石景山",
                        "海淀",
                        "门头沟",
                        "房山",
                        "通州",
                        "顺义",
                        "昌平",
                        "大兴",
                        "平谷",
                        "怀柔",
                        "密云",
                        "延庆"
                    ]
                },
                {
                     "上海市"
                      :[
                        "黄浦",
                        "卢湾",
                        "徐汇",
                        "长宁",
                        "静安",
                        "普陀",
                        "闸北",
                        "虹口",
                        "杨浦",
                        "闵行",
                        "宝山",
                        "嘉定",
                        "浦东",
                        "金山",
                        "松江",
                        "青浦",
                        "南汇",
                        "奉贤",
                        "崇明"
                    ]
                },
                {
                     "天津市"
                      :[
                        "和平",
                        "东丽",
                        "河东",
                        "西青",
                        "河西",
                        "津南",
                        "南开",
                        "北辰",
                        "河北",
                        "武清",
                        "红挢",
                        "塘沽",
                        "汉沽",
                        "大港",
                        "宁河",
                        "静海",
                        "宝坻",
                        "蓟县"
                    ]
                },
                {
                     "重庆市"
                      :[
                        "万州",
                        "涪陵",
                        "渝中",
                        "大渡口",
                        "江北",
                        "沙坪坝",
                        "九龙坡",
                        "南岸",
                        "北碚",
                        "万盛",
                        "双挢",
                        "渝北",
                        "巴南",
                        "黔江",
                        "长寿",
                        "綦江",
                        "潼南",
                        "铜梁 ",
                        "大足",
                        "荣昌",
                        "壁山",
                        "梁平",
                        "城口",
                        "丰都",
                        "垫江",
                        "武隆",
                        "忠县",
                        "开县",
                        "云阳",
                        "奉节",
                        "巫山",
                        "巫溪",
                        "石柱",
                        "秀山",
                        "酉阳",
                        "彭水",
                        "江津",
                        "合川",
                        "永川",
                        "南川"
                    ]
                },
                {
                     "河北省"
                      :[
                        "石家庄",
                        "邯郸",
                        "邢台",
                        "保定",
                        "张家口",
                        "承德",
                        "廊坊",
                        "唐山",
                        "秦皇岛",
                        "沧州",
                        "衡水"
                    ]
                },
                {
                     "山西省"
                      :[
                        "太原",
                        "大同",
                        "阳泉",
                        "长治",
                        "晋城",
                        "朔州",
                        "吕梁",
                        "忻州",
                        "晋中",
                        "临汾",
                        "运城"
                    ]
                },
                {
                     "内蒙古自治区"
                      :[
                        "呼和浩特",
                        "包头",
                        "乌海",
                        "赤峰",
                        "呼伦贝尔盟",
                        "阿拉善盟",
                        "哲里木盟",
                        "兴安盟",
                        "乌兰察布盟",
                        "锡林郭勒盟",
                        "巴彦淖尔盟",
                        "伊克昭盟"
                    ]
                },
                {
                     "辽宁省"
                      :[
                        "沈阳",
                        "大连",
                        "鞍山",
                        "抚顺",
                        "本溪",
                        "丹东",
                        "锦州",
                        "营口",
                        "阜新",
                        "辽阳",
                        "盘锦",
                        "铁岭",
                        "朝阳",
                        "葫芦岛"
                    ]
                },
                {
                     "吉林省"
                      :[
                        "长春",
                        "吉林",
                        "四平",
                        "辽源",
                        "通化",
                        "白山",
                        "松原",
                        "白城",
                        "延边"
                    ]
                },
                {
                     "黑龙江省"
                      :[
                        "哈尔滨",
                        "齐齐哈尔",
                        "牡丹江",
                        "佳木斯",
                        "大庆",
                        "绥化",
                        "鹤岗",
                        "鸡西",
                        "黑河",
                        "双鸭山",
                        "伊春",
                        "七台河",
                        "大兴安岭"
                    ]
                },
                {
                     "江苏省"
                      :[
                        "南京",
                        "镇江",
                        "苏州",
                        "南通",
                        "扬州",
                        "盐城",
                        "徐州",
                        "连云港",
                        "常州",
                        "无锡",
                        "宿迁",
                        "泰州",
                        "淮安"
                    ]
                },
                {
                     "浙江省"
                      :[
                        "杭州",
                        "宁波",
                        "温州",
                        "嘉兴",
                        "湖州",
                        "绍兴",
                        "金华",
                        "衢州",
                        "舟山",
                        "台州",
                        "丽水"
                    ]
                },
                {
                     "安徽省"
                      :[
                        "合肥",
                        "芜湖",
                        "蚌埠",
                        "马鞍山",
                        "淮北",
                        "铜陵",
                        "安庆",
                        "黄山",
                        "滁州",
                        "宿州",
                        "池州",
                        "淮南",
                        "巢湖",
                        "阜阳",
                        "六安",
                        "宣城",
                        "亳州"
                    ]
                },
                {
                     "福建省"
                      :[
                        "福州",
                        "厦门",
                        "莆田",
                        "三明",
                        "泉州",
                        "漳州",
                        "南平",
                        "龙岩",
                        "宁德"
                    ]
                },
                {
                     "江西省"
                      :[
                        "南昌市",
                        "景德镇",
                        "九江",
                        "鹰潭",
                        "萍乡",
                        "新馀",
                        "赣州",
                        "吉安",
                        "宜春",
                        "抚州",
                        "上饶"
                    ]
                },
                {
                     "山东省"
                      :[
                        "济南",
                        "青岛",
                        "淄博",
                        "枣庄",
                        "东营",
                        "烟台",
                        "潍坊",
                        "济宁",
                        "泰安",
                        "威海",
                        "日照",
                        "莱芜",
                        "临沂",
                        "德州",
                        "聊城",
                        "滨州",
                        "菏泽"
                    ]
                },
                {
                     "河南省"
                      :[
                        "郑州",
                        "开封",
                        "洛阳",
                        "平顶山",
                        "安阳",
                        "鹤壁",
                        "新乡",
                        "焦作",
                        "濮阳",
                        "许昌",
                        "漯河",
                        "三门峡",
                        "南阳",
                        "商丘",
                        "信阳",
                        "周口",
                        "驻马店",
                        "济源"
                    ]
                },
                {
                     "湖北省"
                      :[
                        "武汉",
                        "宜昌",
                        "荆州",
                        "襄樊",
                        "黄石",
                        "荆门",
                        "黄冈",
                        "十堰",
                        "恩施",
                        "潜江",
                        "天门",
                        "仙桃",
                        "随州",
                        "咸宁",
                        "孝感",
                        "鄂州"
                    ]
                },
                {
                     "湖南省"
                      :[
                        "长沙",
                        "常德",
                        "株洲",
                        "湘潭",
                        "衡阳",
                        "岳阳",
                        "邵阳",
                        "益阳",
                        "娄底",
                        "怀化",
                        "郴州",
                        "永州",
                        "湘西",
                        "张家界"
                    ]
                },
                {
                     "广东省"
                      :[
                        "广州",
                        "深圳",
                        "珠海",
                        "汕头",
                        "东莞",
                        "中山",
                        "佛山",
                        "韶关",
                        "江门",
                        "湛江",
                        "茂名",
                        "肇庆",
                        "惠州",
                        "梅州",
                        "汕尾",
                        "河源",
                        "阳江",
                        "清远",
                        "潮州",
                        "揭阳",
                        "云浮"
                    ]
                },
                {
                     "广西壮族自治区"
                      :[
                        "南宁",
                        "柳州",
                        "桂林",
                        "梧州",
                        "北海",
                        "防城港",
                        "钦州",
                        "贵港",
                        "玉林",
                        "南宁地区",
                        "柳州地区",
                        "贺州",
                        "百色",
                        "河池"
                    ]
                },
                {
                     "海南省"
                      :[
                        "海口",
                        "三亚"
                    ]
                },
                {
                     "四川省"
                      :[
                        "成都",
                        "绵阳",
                        "德阳",
                        "自贡",
                        "攀枝花",
                        "广元",
                        "内江",
                        "乐山",
                        "南充",
                        "宜宾",
                        "广安",
                        "达川",
                        "雅安",
                        "眉山",
                        "甘孜",
                        "凉山",
                        "泸州"
                    ]
                },
                {
                     "贵州省"
                      :[
                        "贵阳",
                        "六盘水",
                        "遵义",
                        "安顺",
                        "铜仁",
                        "黔西南",
                        "毕节",
                        "黔东南",
                        "黔南"
                    ]
                },
                {
                     "云南省"
                      :[
                        "昆明",
                        "大理",
                        "曲靖",
                        "玉溪",
                        "昭通",
                        "楚雄",
                        "红河",
                        "文山",
                        "思茅",
                        "西双版纳",
                        "保山",
                        "德宏",
                        "丽江",
                        "怒江",
                        "迪庆",
                        "临沧"
                    ]
                },
                {
                     "西藏自治区"
                      :[
                        "拉萨",
                        "日喀则",
                        "山南",
                        "林芝",
                        "昌都",
                        "阿里",
                        "那曲"
                    ]
                },
                {
                     "陕西省"
                      :[
                        "西安",
                        "宝鸡",
                        "咸阳",
                        "铜川",
                        "渭南",
                        "延安",
                        "榆林",
                        "汉中",
                        "安康",
                        "商洛"
                    ]
                },
                {
                     "甘肃省"
                      :[
                        "兰州",
                        "嘉峪关",
                        "金昌",
                        "白银",
                        "天水",
                        "酒泉",
                        "张掖",
                        "武威",
                        "定西",
                        "陇南",
                        "平凉",
                        "庆阳",
                        "临夏",
                        "甘南"
                    ]
                },
                {
                     "宁夏回族自治区"
                      :[
                        "银川",
                        "石嘴山",
                        "吴忠",
                        "固原"
                    ]
                },
                {
                     "青海省"
                      :[
                        "西宁",
                        "海东",
                        "海南",
                        "海北",
                        "黄南",
                        "玉树",
                        "果洛",
                        "海西"
                    ]
                },
                {
                     "新疆维吾尔族自治区"
                      :[
                        "乌鲁木齐",
                        "石河子",
                        "克拉玛依",
                        "伊犁",
                        "巴音郭勒",
                        "昌吉",
                        "克孜勒苏柯尔克孜",
                        "博尔塔拉",
                        "吐鲁番",
                        "哈密",
                        "喀什",
                        "和田",
                        "阿克苏"
                    ]
                },
                {
                     "香港特别行政区"
                      :[
                        "香港特别行政区"
                    ]
                },
                {
                     "澳门特别行政区"
                      :[
                        "澳门特别行政区"
                    ]
                },
                {
                     "台湾省"
                      :[
                        "台北",
                        "高雄",
                        "台中",
                        "台南",
                        "屏东",
                        "南投",
                        "云林",
                        "新竹",
                        "彰化",
                        "苗栗",
                        "嘉义",
                        "花莲",
                        "桃园",
                        "宜兰",
                        "基隆",
                        "台东",
                        "金门",
                        "马祖",
                        "澎湖"
                    ]
                },
                {
                     "其它"
                      :[
                        "北美洲",
                        "南美洲",
                        "亚洲",
                        "非洲",
                        "欧洲",
                        "大洋洲"
                    ]
                }
            ]

        }
        this._requestAPI = this._requestAPI.bind(this)

        this.options = this.props.options;  
        this.callback = function () {};//回调方法  
        this.parent ={};  
    }

    componentDidMount(){

        storage.load('islogin',(data)=>{
            this.setState({
                head_pic: data['head_pic'],
                fubi: data['fubi'],
                identifier: data['identifier'],
                describe: data['describe'],
                bank_card_number:data['bank_card_number'],
                bank_name:data['bank_name'],
                real_name:data['real_name'],
                phone:data['phone']
            });
        })
        this.getbankList()
    }


    getbankList(){

      //  let codeurl = host + '/index/index/getBankList';
        let codeurl = host + '/AppMember.getBankAll.do';
        let formData = new FormData();
        // 请求参数 ('key',value)

        HttpUtils.post(codeurl,formData)
            .then(result=>{


                console.log("------getbankList-----");
                console.log(result);

                if(result['respCode']==1){
                    // let testData = result['data']['rows']
                    // let newList = testData.map((data) => {
                    //     return data["name"];
                    // })
                    // this.setState({
                    //     bankNameList:newList
                    // })

                    let newList = result['data'];
                    this.setState({
                        bankNameList:newList
                    })


                    // console.log("000000");
                    // console.log(newList);


                }else{
                 
                    this.refs.toast.show("获取银行列表失败", DURATION.LENGTH_LONG);
                }

            })
            .catch(error=>{
                this.refs.toast.show(JSON.stringify(error), DURATION.LENGTH_LONG);
                // Alert.alert(JSON.stringify(error))
            })
    }

    //2 秒后随机模拟获取验证码成功或失败的情况
    _requestAPI(shouldStartCounting){
        this.loadCode()
        setTimeout(()=>{
            const requestSucc = (this.state.result == 1);
            this.setState({
                state: `（随机）模拟验证码获取${requestSucc ? '成功' : '失败'}`
            })

            if(shouldStartCounting && shouldStartCounting(requestSucc)){
                // Alert.alert(this.state.result);
                this.refs.toast.show(this.state.result, DURATION.LENGTH_LONG);
            }

        }, 1000);
    }

    //请求验证吗
    loadCode() {

        let codeurl = host + '/index/index/code';
        let formData = new FormData();
        // 请求参数 ('key',value)
        formData.append('phone', this.state.phone);
        formData.append('type','bindBankCard');

        HttpUtils.post(codeurl,formData)
            .then(result=>{
                this.setState({
                    result:1
                })
               
                this.refs.toast.show('验证码已发送', DURATION.LENGTH_LONG);
            })
            .catch(error=>{
  
                this.refs.toast.show(JSON.stringify(error), DURATION.LENGTH_LONG);
            })
    }

    //注册
    submintInfo() {


        Keyboard.dismiss()

        if(!this.state.real_name){
         
            this.refs.toast.show("请输入姓名", DURATION.LENGTH_LONG);
            return;
        }

        if(this.state.bank_name.length<1){
    
            this.refs.toast.show("请选择银行名称", DURATION.LENGTH_LONG);
            return;
        }

        if(this.state.pro_name.length<1){
          
            this.refs.toast.show("请选择开户省", DURATION.LENGTH_LONG);
            return;
        }

        if(this.state.pro_city.length<1){
          
            this.refs.toast.show("请选择开户市", DURATION.LENGTH_LONG);
            return;
        }

        if(this.state.bankBranch.length<1){
  
            this.refs.toast.show("请输入网点", DURATION.LENGTH_LONG);
            return;
        }

        if(this.state.bank_card_number.length<1){
     
            this.refs.toast.show("请输入卡号", DURATION.LENGTH_LONG);
            return;
        }
        if(this.state.regbankCardNumber.length<1){

            this.refs.toast.show("请再次输入卡号", DURATION.LENGTH_LONG);
            return;
        }

        if(this.state.regbankCardNumber != this.state.bank_card_number){
    
            this.refs.toast.show("输入卡号不一致", DURATION.LENGTH_LONG);
            return;
        }

        if(this.state.bankTradPwd.length<1){
           
            this.refs.toast.show("请输入资金密码", DURATION.LENGTH_LONG);
            return;
        }


       // let codeurl = host + '/member/index/bindBankCard';


        let codeurl = host + '/AppApijiekou.userbindbankcard';

        let formData = new FormData();
       

        formData.append('accountname',this.state.real_name);

        formData.append('bankCode',this.state.bank_code);
        
        formData.append('bankAddress',this.state.pro_name+"-"+this.state.pro_city);
        formData.append('bankBranch',this.state.bankBranch)
        formData.append('bankCardNumber',this.state.bank_card_number)
        formData.append('regbankCardNumber',this.state.regbankCardNumber)

        formData.append('bankTradPwd',this.state.bankTradPwd)
 


        this.setState({
            isBanDing:true
        })
        
        HttpUtils.post(codeurl,formData)
            .then(result=>{


                this.setState({
                    isBanDing:false
                })
                // result['result =======>'];

                // console.log(result);

                console.log("result['respCode']" +result['respCode']);

                if(result['respCode']==1){

                    DeviceEventEmitter.emit('updatebank', {});

                    this.props.navigation.state.params.bankCardSucss(this.state.bank_name,this.state.bank_card_number);
                    
                    this.props.navigation.goBack()
                }else{
       
                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                }

            })
            .catch(error=>{

                this.refs.toast.show(JSON.stringify(error), DURATION.LENGTH_LONG);

                this.setState({
                    isBanDing:false
                })
            })
    }




    componentWillUnmount(){
        MyPicker.hide()
    }


    popBack(){
        this.props.navigation.goBack()
    }
    render() {

      

        return (
            <KeyboardAwareScrollView
                keyboardShouldPersistTaps = {"always"}
                style={styles.container}
                ref= {component => this.photoPickerRef = component}
            >


          

                {/* <Text style={{marginTop:15,marginLeft:10,height:30,color:sys.subTitleColor}}>请绑定持卡人本人的银行卡</Text> */}
                {/* <View style={styles.view}></View> */}
                <View style={styles.textInputView}>
                    <Text style={styles.leftText}>持卡人</Text>

                    <TextInput style={styles.inputText}
                               ref={(c) => this.textInput = c}
                               underlineColorAndroid='transparent'
                               placeholder={"请输入您的姓名"}
                               value={this.state.real_name?this.state.real_name:''}
                               onChangeText={(text) => this.setState({real_name:text})}
                    >
                    </TextInput>
                </View>

                <View style={styles.view}></View>
                <View style={styles.textInputView}>
                    <Text style={styles.leftText}>选择银行</Text>
                    <TouchableOpacity style={styles.bankNameView} onPress={()=>this.showBankPicker()}>
                    <Text
                        editable={false}

                        style={[styles.rightText,{marginLeft:0,color:sys.subTitleColor,fontSize:13}]}
                    >
                        {this.state.bank_name?this.state.bank_name:"请选择"}
                    </Text>
                    </TouchableOpacity>
                </View>

                <View style={styles.view}></View>
                <View style={styles.textInputView}>
                    <Text style={styles.leftText}>开户省市</Text>
                    <TouchableOpacity style={styles.bankNameView} onPress={()=>this.showProPicker()}>
                    <Text
                        editable={false}

                      //  style={{marginTop:18,color:sys.subTitleColor}}

                        style={[styles.rightText,{marginLeft:0,color:sys.subTitleColor,fontSize:13}]}
                    >
                        {this.state.pro_city?this.state.pro_name + '-' + this.state.pro_city:"请选择"}
                    </Text>
                    </TouchableOpacity>
                </View>

                {/* <View style={styles.view}></View>
                <View style={styles.textInputView}>
                    <Text style={styles.leftText}>开户市</Text>
                    <TouchableOpacity style={styles.bankNameView} onPress={()=>this.showCityPicker()}>
                    <Text
                        editable={false}

                        style={[styles.leftText,{marginLeft:0,color:sys.subTitleColor,fontSize:13}]}
                    >
                        {this.state.city_name?this.state.city_name:"请选择"}
                    </Text>
                    </TouchableOpacity>
                </View> */}

            
                <View style={styles.view}></View>
                <View style={styles.textInputView}>
                    <Text style={styles.leftText}>开户网点</Text>

                    <TextInput style={styles.inputText}
                               ref={(c) => this.textInput = c}
                               underlineColorAndroid='transparent'
                               placeholder={"请输入您的网点"}
                               value={this.state.bankBranch?this.state.bankBranch:''}
                               onChangeText={(text) => this.setState({bankBranch:text})}
                    >
                    </TextInput>
                </View>


                <View style={styles.view}></View>
                <View style={styles.textInputView}>
                    <Text style={styles.leftText}>银行卡号</Text>

                    <TextInput style={styles.inputText}
                               ref={(c) => this.textInput = c}
                               underlineColorAndroid='transparent'
                               placeholder={"请输入您的卡号"}
                               value={this.state.bank_card_number?this.state.bank_card_number:''}
                               onChangeText={(text) => this.setState({bank_card_number:text})}
                    >
                    </TextInput>
                </View>
                

            

                <View style={styles.view}></View>


                <View style={styles.textInputView}>
                    <Text style={styles.leftText}>确定卡号</Text>

                    <TextInput style={styles.inputText}
                               ref={(c) => this.textInput = c}
                               underlineColorAndroid='transparent'
                               placeholder={"请再次输入卡号"}
                               value={this.state.regbankCardNumber?this.state.regbankCardNumber:''}
                               onChangeText={(text) => this.setState({regbankCardNumber:text})}
                    >
                    </TextInput>
                </View>
                    <View style={styles.view}></View>


                    <View style={styles.textInputView}>
                    <Text style={styles.leftText}>资金密码</Text>

                    <TextInput style={styles.inputText}
                               ref={(c) => this.textInput = c}
                               underlineColorAndroid='transparent'
                               placeholder={"请输入您的资金密码"}
                               value={this.state.bankTradPwd?this.state.bankTradPwd:''}
                               onChangeText={(text) => this.setState({bankTradPwd:text})}
                    >
                    </TextInput>
                </View>

                    {/* <View style={styles.codeView}>
                        <TextInput
                            underlineColorAndroid='transparent'
                            style={styles.codeText}
                            placeholder="请输入验证码"
                            onChangeText={(text) => this.setState({code:text})}
                            textStyle={{color:'green'}}
                        />
                        <CountDownButton
                            style={styles.countButton}
                            textStyle={{color: '#ffffff'}}
                            disableColor="gray"
                            timerCount={60}
                            timerTitle={'获取验证码'}
                            enable={this.state.phone.length > 10}
                            onClick={(shouldStartCounting)=>{
                                this._requestAPI(shouldStartCounting)
                            }}


                            timerEnd={()=>{
                                this.setState({
                                    state: '倒计时结束'
                                })
                            }}/>
                    </View> */}
                    <View style={styles.view}></View>

                    <TouchableOpacity disabled={this.state.isBanDing} style={styles.button} onPress={()=>this.submintInfo()}>
                            {this.state.isBanDing?<ActivityIndicator />:null}
                        <Text style={styles.buttontext}>{this.state.isBanDing?'绑定中':'确定'}</Text>
                    </TouchableOpacity>

                    <Toast  //提示
                    ref="toast"
                    style={{backgroundColor:'gray'}}
                    position='center'
                    positionValue={200}
                    opacity={0.6}
                    textStyle={{color:'white'}}
                /> 

            </KeyboardAwareScrollView>

        )
    }



    showBankPicker(){
       

        var bankNameArr = [];
        for(i in this.state.bankNameList){
            bankNameArr.push(this.state.bankNameList[i].bankname)
        }


        if(bankNameArr.length < 1){
            return;
        }

        MyPicker.init({
            pickerData: bankNameArr,
            selectedValue: [bankNameArr[0]],
            pickerConfirmBtnText:'确定',
            pickerCancelBtnText:'取消',
            pickerTitleText:'选择银行',
            pickerToolBarBg: [232, 232, 232, 1],
            pickerBg:[245,245,245,1],
            pickerToolBarFontSize: 16,
            pickerFontSize: 20,
            onPickerConfirm: (data) => {
                for(i in this.state.bankNameList){               
                    if(data[0] == this.state.bankNameList[i].bankname){
                     this.setState({bank_name: this.state.bankNameList[i].bankname,bank_code:this.state.bankNameList[i].bankcode})
                    }
                }
            }
        });
        MyPicker.show();
    }

    showProPicker(){
        // Alert.alert("dia dian ji")
        MyPicker.init({
            pickerData: this.state.proNameList,
            selectedValue: ['北京市','东城'],
            pickerConfirmBtnText:'确定',
            pickerCancelBtnText:'取消',
            pickerTitleText:'选择地区',
            pickerToolBarBg: [232, 232, 232, 1],
            pickerBg:[245,245,245,1],
            pickerToolBarFontSize: 16,
            pickerFontSize: 20,
            onPickerConfirm: (data) => {
                this.setState({pro_city: data[1],pro_name:data[0]})          
            }
        });
        MyPicker.show();
    }


   

    

    

    goWebView(){
        let url = host + "/index/index/html?type=registration"
        const {navigate} = this.props.navigation;
        navigate('WebViewScene', {uri: url,title:"用户注册协议"});
    }



  


}



const styles = StyleSheet.create({
    tipTitleView: {  
        height:53,  
        width:sys.dwidth,  
        flexDirection:'row',  
        alignItems:'center',  
        justifyContent:'space-between',  
        borderBottomWidth:0.5,  
        borderColor:"#f0f0f0",  
    
      },  
      cancelText:{  
        color:"#e6454a",  
        fontSize:16,  
        paddingLeft:30,  
      },  
      okText:{  
        color:"#e6454a",  
        fontSize:16,  
        paddingRight:27,  
        fontWeight:'bold',  
      },  
   

    tip: {  
        width:sys.dwidth,  
        height:214,  
        // left:middleLeft,  
        backgroundColor:"#fff",  
        alignItems:"center",  
        justifyContent:"space-between",  
      },  
  

    row:{
        //改变主轴的方向
        marginTop:10,
        flexDirection:'row',
        height:60
    },
    back:{
        marginLeft:14,
        marginTop: Platform.OS == 'ios' ? 21 : 5,
    },
    container: {
        marginTop: 0,
        backgroundColor:'#ffffff',
        paddingTop:isIphoneX?20:0,
        flex:1
    },
    text:{
        fontSize:18,
        color:"#333333",
        textAlign: 'center',
        marginTop: Platform.OS == 'ios' ? 21 : 5,
        width:150,
        marginLeft:sys.dwidth/2-75-21
    },
    image:{
        marginTop:20,
        alignSelf:'center',
        width:100,
        height:100

    },
    textIp:{
        marginTop:30,
        marginLeft:12,
        width:sys.dwidth-40,
        height:40,

    },
    textIp3:{
        marginTop:25,
        marginLeft:12,
        width:sys.dwidth-40,
        height:40,
    },

    view:{
        height:0.8,//TODO这里小于0.8没显示出来
        backgroundColor:'#cccccc',
        marginTop:0,
        marginLeft:12,
        width:sys.dwidth-24,
    },

    button:{
        width:sys.dwidth-40,
        marginLeft:20,
        marginTop:10,
        height:40,
        backgroundColor:sys.mainColor,

        justifyContent:'center',
        marginTop: 8,
        borderRadius:5,
        flexDirection:'row',

    },
    buttontext:{
        marginTop:11,
        color:'#ffffff',
        textAlign:'center',
        fontSize:18
    },
    forgetregister:{
        flexDirection:'row'
    },
    forgetPw:{
        marginLeft:20,
        marginTop:20,
        width:100,
        color:sys.mainColor,
    },
    register:{
        marginLeft:sys.dwidth-120-100-20,
        flexDirection:'row',
        width:100,
        justifyContent:'flex-end',
        marginTop:20
    },
    registerTX:{
        fontSize:14,
        color:sys.mainColor
    },
    stateText:{
        paddingTop: 50,
        color: 'gray'
    },
    codeView:{
        flexDirection:'row'
    },
    codeText:{
        marginTop:25,
        width:sys.dwidth-130,
        marginLeft:12,
        height:40
    },
    countButton:{
        width: 110,
        marginLeft: 0,
        backgroundColor:sys.mainColor,
        marginTop:25,
        justifyContent:'center',
        flexDirection:'row',
        borderRadius:5,
        height:40
    },
    greementView: {
        marginTop:20,
        flexDirection: 'row',
        paddingLeft:10
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
    textInputView:{
        marginTop:1,
        flexDirection:'row',
        backgroundColor:'white',
        height:50
    },


    rightText:{

        marginLeft:10,
    
        height:50,
     
         fontSize:16,
 
         textAlignVertical:'center',
         ...Platform.select({
             ios: { lineHeight: 50},
                 android: {}
         })

    },

    leftText:{
        marginLeft:10,
       // marginTop:17,
       height:50,
        width:75,
        fontSize:16,
     //   backgroundColor:'red',
        textAlign:'center',
        textAlignVertical:'center',
        ...Platform.select({
            ios: { lineHeight: 50},
                android: {}
        })

        // textAlign:'center',
        //     textAlignVertical:'center',
        //     width:LEFT_WIDTH-20,
        //     height:50,
        //     ...Platform.select({
        //         ios: { lineHeight: 50},
        //          android: {}
        //     })


    },

    inputText:{

        width:sys.dwidth-75,
        color:sys.subTitleColor,
        textAlign:'left',
       // height:50,
        //backgroundColor:'red'

    },
    bankNameView:{
        marginTop:1,
        flexDirection:'row',
        height:50,
        width:sys.dwidth
    }


})
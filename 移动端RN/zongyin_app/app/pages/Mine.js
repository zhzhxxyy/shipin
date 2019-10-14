import React, { PureComponent } from 'react' 
import {
    Platform,
    Dimensions,
    StyleSheet,
    Text,
    View,
    Image,
    Alert,
    ScrollView,
    TouchableOpacity,
    Animated,
    Easing,
    DeviceEventEmitter
}from 'react-native'
import {sys} from "../common/Data"
import DeviceInfo from 'react-native-device-info';
import HttpUtils from "../common/HttpUtil"
import toast from 'react-native-root-toast'

const {width: screenWidth, height: screenHeight} = Dimensions.get('window')

var clickLastTime=0;

export default class Mine extends PureComponent {
  
    constructor(props) {
        super(props);
        this.state = {
            rotation: new Animated.Value(0),
            scale: new Animated.Value(1),
            translateY: new Animated.Value(10),
            opacity: new Animated.Value(0),
            userInfo:null,
            kefuUrl:""
        }
    }

    _this = null ;
    static navigationOptions = ({navigation}) => ({
        header:null,
        async tabBarOnPress({ navigation, defaultHandler }) { 
            DeviceEventEmitter.emit('stop', {})
            defaultHandler();
          },
    });

    
    

  
    
    settingData = [
        {
            leftText: '我的客服',
            rightText: '充值问题请联系',
            onPress(){
                var nowTime= (new Date()).getTime();
                if(Math.abs(nowTime-clickLastTime)<sys.clickIntervalTime){
                    toast.show("点击速度过快，请稍后");
                    return false;
                }
                clickLastTime=nowTime;
                if(_this.state.kefuUrl!=""){
                    _this.props.navigation.navigate('WebViewScene', {uri: {content:_this.state.kefuUrl},'title':"客服"});
                }else{
                    _this.getKefuUrl(false);
                }
            }
        },
        // {
        //     leftText: '我的收藏',
        // },
        {
            leftText: '购买金币',
            rightText: '立刻充值享受优惠',
            onPress(){
                 _this.props.navigation.navigate('Recharge', {});
            }
        },
        {
            leftText: '关于我们',
            rightText: '有问题请求助',
            onPress(){
                 _this.props.navigation.navigate('HelpPage', {});
            }
        }
        // {
        //     leftText: '充值记录',
        // },
        // {
        //     leftText: '消费记录',
        // }
    ]

    componentDidMount() {
        _this = this;
        //顺序执行
        Animated.sequence([
            //随着时间发展执行
            Animated.timing(
                this.state.rotation,{
                    toValue: 1,
                    duration: 500,
                    easing: Easing.linear,
                }
            ),
            Animated.timing(
                this.state.scale,{
                    toValue: 1.3,
                    duration: 600,
                }
            ),
            //同时执行
            Animated.parallel([
                Animated.timing(
                    this.state.scale,{
                        toValue: 1,
                        duration: 500,
                    }
                ),
                Animated.timing(
                    this.state.opacity,{
                        toValue: 1,
                        duration: 1000,
                    }
                ),
                Animated.timing(
                    this.state.translateY,{
                        toValue: 0,
                        duration: 600,
                    }
                )
            ])
        ])
        this.setState({
            userInfo: global.user.userData 
        })
        this.getUserData(true);
        this.getKefuUrl(true);
    }
    
    getUserData(isAuto){
        let codeurl = 'app_api/loginByUUID';
        let formData = new FormData();
        formData.append('uuid',DeviceInfo.getUniqueID());
        HttpUtils.post(codeurl,formData)
        .then(result=>{
            global.user.userData = result["data"]
            global.user.token=result["data"]['token'];
            global.user.loginState=1;
            this.setState({
                userInfo: global.user.userData 
            })
        }).catch(error=>{
           if(!isAuto){
             toast.show("登陆失败:"+error)
           }
        })  
    }


    getKefuUrl(isAuto){
        let codeurl = 'app_api/buy_cardpassword_uri.html';
        let formData = new FormData();
        HttpUtils.post(codeurl,formData)
        .then(result=>{
            _this.state.kefuUrl=result["data"];
            if(!isAuto){
                _this.props.navigation.navigate('WebViewScene', {uri: {content:_this.state.kefuUrl},'title':"客服"});
             }
        }).catch(error=>{
           if(!isAuto){
              toast.show("获取客服地址失败:"+error)
           }
        })  
    }


    render(){
        var vipString="否";//vip状态
        if(this.state.userInfo!=""&&this.state.userInfo!=null){
            //已经登录
            var touxiang=( <Animated.Image style={[styles.userImg, {}]} source={{uri:this.state.userInfo.headimgurl}} resizeMode={'contain'} />); 
            var username=this.state.userInfo.username;
            var money=this.state.userInfo.money;
            var loginHead=( <View style={styles.headCenterContainer}>{
                touxiang
            }
            <View style={styles.positionContainer}>
                <Image style={styles.positionImg} source={require('./../../assets/images/i_bookmark.png')} />
                <Text style={styles.positionText}>{username}</Text>
            </View>
        </View>);

            if(this.state.userInfo.is_permanent==1){
                vipString="永久";
            }else{
                if(this.state.userInfo.out_time==""||this.state.userInfo.out_time==null||this.state.userInfo.out_time=="null"||this.state.userInfo.out_time==undefined){
                    vipString="否";
                }else{
                    var nowTime=(new Date()).getTime();
                    if(nowTime>=this.state.userInfo.out_time*1000){
                        vipString="已过期";
                    }else{
                        var newTime = new Date(this.state.userInfo.out_time*1000);
                        vipString=newTime.format('yyyy-MM-dd')+"截止";
                    }
                }
            }
        }else{
            var touxiang=(
                 <TouchableOpacity onPress={()=>this.getUserData(false)}>
                     <Animated.Image style={[styles.userImg, {}]} source={require('./../../assets/images/i_user.jpg')} resizeMode={'contain'} />
                </TouchableOpacity> 
                ); 
            var username="未登录";
            var money=0;
            var loginHead=(
                <View style={styles.headCenterContainer}>
                {
                    touxiang
                }
              <View style={styles.positionContainer}>
                <Image style={styles.positionImg} source={require('./../../assets/images/i_bookmark.png')} />
                <TouchableOpacity onPress={()=>this.getUserData(false)}>
                <Text style={styles.positionText}>{username}</Text>
                </TouchableOpacity> 
             </View>
        </View>);
        }

          


        return(
            <ScrollView style={styles.container}>
                {/* 头部 */}
                <View style={styles.headContainer}>  
                    {
                        loginHead
                    }
         
                    {/* 收藏、历史、更贴 */}
                    <View style={styles.headBottomContainer}>
                        <TouchableOpacity style={styles.bottomBtn} activeOpacity={1} 
                        onPress={() => {}}>
                            <Text style={styles.bottomNum}>{vipString}</Text>
                            <Text style={styles.bottomText}>VIP</Text>
                        </TouchableOpacity>

                        <TouchableOpacity style={styles.bottomBtn} activeOpacity={1} onPress={() => {}}>
                            <Text style={{color:sys.yellowColor}}>{money+""}</Text>
                            <Text style={{color:sys.yellowColor}}>金币</Text>
                        </TouchableOpacity>

                        <TouchableOpacity style={styles.bottomBtn} activeOpacity={1} onPress={() => {}}>
                            <Text style={styles.bottomNum}>{sys.versionCode}</Text>
                            <Text style={styles.bottomText}>版本</Text>
                        </TouchableOpacity>
                    </View>
                </View>

                {/* 过渡条 */}
                <View style={styles.transitionView}></View>

                {/* 设置列表 */}
                <View style={styles.settingListContainer}>
                    {
                        this.settingData.map((item, index) => {
                            return(
                                <ListItem
                                    key={index}
                                    leftText={item.leftText}
                                    rightText={item.rightText}
                                    rightComponent={item.rightComponent}
                                    isShowArrow={item.isShowUnderline}
                                    onPress={item.onPress}
                                />
                            )
                        })
                    }
                </View>
            </ScrollView>
        )
    }
}

class ListItem extends React.PureComponent {
    static defaultProps = {
        leftText: '',
        rightText: '',
        isShowUnderline: true,
        isShowArrow: true,
    }

    _renderRight = () => {
        if (!this.props.rightText && !this.props.rightComponent) {
            return <Text />
        }

        if (this.props.rightText) {
            return(
                <Text style={styles.itemRightText}>
                    {this.props.rightText}
                </Text>
            )
        }

        if (this.props.rightComponent) {
            return(
                <this.props.rightComponent />
            )
        }
    }

    render(){
        return(
            <TouchableOpacity activeOpacity={0.9} style={[styles.itemContainer, this.props.isShowUnderline && styles.itemBorderBottom]} onPress={this.props.onPress}>
                <Text style={styles.itemLeftText}>{this.props.leftText}</Text>

                <View style={styles.itemRightContainer}>
                    {
                        this._renderRight()
                    }
                    {
                        !this.props.rightComponent && this.props.isShowArrow && <Image style={styles.itemRightImg} source={require('./../../assets/images/i_right.png')}/>
                    }
                </View>
            </TouchableOpacity>
        )
    }
}

const styles = StyleSheet.create({
    itemContainer: {
        flexDirection: 'row',
        backgroundColor: '#f8f8f8',
        justifyContent: 'space-between',
        alignItems: 'center',
        height: 50,
    },
    itemBorderBottom: {
        borderBottomWidth: 1,
        borderBottomColor: '#e6e6e6',
    },
    itemLeftText: {
        fontSize: 14,
        color: '#000',
    },
    itemRightContainer: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
    },
    itemRightText: {
        color: '#bfbfbf',
        fontSize: 12,
    },
    itemRightImg: {
        width: 20,
        height: 20,
        marginHorizontal: 7,
    },
    container: {
        flex: 1,
        backgroundColor: '#F8F8F8'
    },
    headContainer: {
        marginTop:45,
    },
   
    topBtnStyle: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        width: 70,
        height: 30,
        borderColor: '#e6e6e6',
        borderWidth: 1,
        borderRadius: 20,
    },
    headTopImg: {
        width: 15,
        height: 15,
        marginRight: 5
    },
    headTopText: {
        fontSize: 12,
        color: '#515151'
    },
    headCenterContainer: {
        alignItems: 'center',
        marginBottom: 15
    },
    userImg: {
        width: 80,
        height: 80,
        borderRadius: 40
    },
    userNickname: {
        marginVertical: 5,
        fontSize: 20,
        color: '#000'
    },
    positionContainer: {
        marginTop:15,
        flexDirection: 'row',
        alignItems: 'center'
    },
    positionImg: {
        width: 10,
        height: 10,
        marginRight: 2
    },
    positionText: {
        color: sys.titleColor,
        fontSize: 15
    },
    headBottomContainer: {
        flexDirection: 'row',
        justifyContent: 'space-around'
    },
    bottomBtn: {
      alignItems: 'center'
    },
    bottomNum: {
        fontSize: 20,
        color: '#000'
    },
    bottomText: {
        color: '#bfbfbf',
        fontSize: 12
    },
    transitionView: {
        height: 5,
        backgroundColor: 'rgba(230,230,230, .5)'
    },
    settingListContainer: {
        paddingLeft: 20,
    }
})
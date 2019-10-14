import React, { PureComponent } from 'react' 
import {
    TouchableOpacity,
    Text,
    View,
    Alert,
    StyleSheet,
    Dimensions,
    Image,
    ScrollView,
    DeviceEventEmitter,
    Clipboard
}from 'react-native'
import DeviceInfo from 'react-native-device-info';
import toast from 'react-native-root-toast'
const {width: screenWidth, height: screenHeight} = Dimensions.get('window')
import {sys} from "../common/Data"
import HttpUtils from "../common/HttpUtil"
export default class Spread extends PureComponent {
    
    constructor(props) {
        super(props);
        this.state={
            copyStr:""
        }
        
    }
    static navigationOptions = ({navigation}) => ({
        header:null,
        async tabBarOnPress({ navigation, defaultHandler }) {
                DeviceEventEmitter.emit('stop', {})
            defaultHandler();
          },
    });

    componentDidMount() {
        this.getUserData();
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
   
    render(){
  
        return(
            <ScrollView style={styles.container}>
               
               
                <View style={{marginLeft:10,marginTop:20,width:sys.dwidth-20,backgroundColor:sys.whiteColor,height:screenWidth/1.5+140}}>  
                
               <Image source={require('../../assets/images/spread.png')} resizeMode={'contain'} style={{width:screenWidth/1.5, height:screenWidth/1.5, marginLeft:screenWidth/6}} />

                  
                <Text style={{marginLeft:10,width:sys.dwidth-40,height:40}}> 1.将宣传链接推送出去，当别人打开您的链接,您将会得到3个金币奖励</Text> 
            
              
                    <Text style={{marginLeft:10,width:sys.dwidth-40,height:40}}> 2. 每天有效的宣传奖励共有1000次奖励</Text>
             
                    <TouchableOpacity  onPress={(()=>{})}  activeOpacity={0.2} focusedOpacity={0.5}>
                    
                          <View style=  {{marginLeft:20,justifyContent:'center',alignItems:'center',width:sys.dwidth-80,height:40,borderColor:sys.yellowColor,borderWidth:1,borderRadius:10}}>
                          <Text style={{color:sys.titleColor}}>{sys.host}</Text>
                         </View>
                      </TouchableOpacity> 

             
                </View>
                <TouchableOpacity  onPress={(()=>{
                    Clipboard.setString(sys.host+global.user.userData.id);
                    toast.show("复制成功")
                    })}  activeOpacity={0.2} focusedOpacity={0.5}>
                    
                          <View style=  {{marginLeft:50,marginTop:30,justifyContent:'center',alignItems:'center',width:sys.dwidth-100,height:40,borderColor:sys.yellowColor,borderWidth:1,borderRadius:10,backgroundColor:sys.yellowColor}}>
                          <Text style={{color:sys.whiteColor}}>复制宣传链接</Text>
                         </View>
                      </TouchableOpacity>
                    
            </ScrollView>
        )
    }
}

const styles = StyleSheet.create({
    container:{
        flex: 1,
        backgroundColor: sys.grayColor,
        paddingTop: 10,
    },
    tabViewItemContainer: {
        flex: 1,
        backgroundColor: '#FFCCCC',
        justifyContent: 'center',
        alignItems: 'center'
    },
})
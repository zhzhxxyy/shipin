import React, { PureComponent } from 'react'
import {
    TouchableOpacity,
    Text,
    View,
    Alert,
    StyleSheet,
    Dimensions,
    Image,
    DeviceEventEmitter,
    Platform,
}from 'react-native'
import ScrollableTabView, { DefaultTabBar, ScrollableTabBar } from 'react-native-scrollable-tab-view'
import HotUpdate, { ImmediateCheckCodePush } from 'react-native-code-push-dialog';

import HomeFlatListView from '../components/HomeFlatListView'
import MainTabNavigatorHeader from '../views/MainTabNavigatorHeader'
const {width:screenWidth, height:screenHeight} = Dimensions.get('window');
import HttpUtils from "../common/HttpUtil"
import DeviceInfo from 'react-native-device-info';
import MoviePage from './MoviePage'
import {sys,NoDataView} from '../common/Data'

export default class Home extends PureComponent {

    constructor(props) {
        super(props);
        
        this.state = {
            tabArr:['精彩'],
            videoList:{},
            banner:[],
            isLoaded:false,
            recom_list:[],
            new_list:[],
            hot_list:[]
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
        ImmediateCheckCodePush();
        //this.checkVersion()
        this._getNewsData()
        this.getUserData(0)
    }

    // checkVersion(){
    //     codePush.sync({
    //         updateDialog: {
    //             appendReleaseDescription: true,
    //             descriptionPrefix:'\n\n有新版本：\n',
    //             title:'更新',
    //             mandatoryUpdateMessage : "必须更新后才能使用" ,
    //             mandatoryContinueButtonLabel : "立即更新" ,
    //         },
    //         mandatoryInstallMode:codePush.InstallMode.IMMEDIATE,
    //     });
    // }

    getUserData(num){
        let codeurl = 'app_api/loginByUUID';
        let formData = new FormData();
        // Alert.alert(DeviceInfo.getUniqueID());
        formData.append('uuid',DeviceInfo.getUniqueID());
        HttpUtils.post(codeurl,formData)
        .then(result=>{
            global.user.userData = result["data"]
        }).catch(error=>{
            if(num<=0){
                this.getUserData(num+1);
            }
        
        })  


    }

    _getNewsData(num){
        
            let codeurl = 'app_index';
            let formData = new FormData();
            
            HttpUtils.post(codeurl,formData)
                .then(result=>{
                    if(result['respCode']==1){
                        tabArr = result["data"]["video_list"].map((data)=>{
                            return ({
                                columnName:data.name,
                                requestCode:'T1348647909107',
                                id:data.id
                            })
                        });
                        tabArr.unshift({columnName:'精彩'})
        
                        var vmap = new Map();
                        videoList:result["data"]["video_list"].map((data)=>{
                            vmap[data.name]=data.list
                        })
                        
                        this.state.videoList = vmap;
                        let arr = [];
                        
                        result["data"]["banner"].forEach(item => {
                            arr.push(item.images_url);
                        })
                        this.state.banner = arr;
                        this.state.recom_list = result["data"]["recom_list"]
                        this.state.new_list = result["data"]["new_list"]
                        this.state.hot_list = result["data"]["hot_list"]
                        this.setState({
                            tabArr:tabArr,
                            isLoaded:true
                        })

                    }else{
    
                    }
                }).catch(error=>{
                    if(num<=0){
                        this._getNewsData(isReload,num+1);
                    }
      
                })     
             }
        

    render(){
   
        return(
            this.state.isLoaded?
            <View style={styles.container}>
                {/* <MainTabNavigatorHeader
                        onRightClick={() => {
                            this.props.navigation.navigate('QueryMoreVideoPage',{id:1,title:'电影'})
                }}
                rightIcon={require('../../source/image/sx_icon.png')}
                navigation={this.props.navigation} /> */}
                  <HotUpdate />
                    <ScrollableTabView
                        ref={'tabView'}
                        renderTabBar={() => <ScrollableTabBar style={{borderBottomWidth:0, paddingBottom:5, width:screenWidth * 1, height:45}} />}
                        tabBarUnderlineStyle={{height:2, minWidth:Math.floor(screenWidth * 1 / 5), backgroundColor:sys.mainColor}}
                        tabBarInactiveTextColor='#515151'
                        tabBarActiveTextColor={sys.mainColor}
                        tabBarTextStyle={{fontSize: 15}}
                        onChangeTab={(ref) => {}}
                        onScroll={(position) => {}}
                        locked={false}
                        initialPage={0}>
                        {   
                            this.state.tabArr.map(item => {

                                 if(item.columnName == "精彩"){
                                 
                                     return <MoviePage   key={item.columnName}
                                     tabLabel={item.columnName}
                                     navi={this.props.navigation}
                                     />
                                 }
                                return(
                                    <HomeFlatListView
                                        dataMap={this.state.videoList[item.columnName]}
                                        cid={item.id}
                                        key={item.columnName}
                                        tabLabel={item.columnName}
                                        requestCode={item.requestCode}
                                        navigation={this.props.navigation}
                                        images={this.state.banner}
                                        recom_list={this.state.recom_list}
                                        new_list={this.state.new_list}
                                        hot_list={this.state.hot_list}
                                    />
                                )
                            })
                        }
                    </ScrollableTabView>
            </View>:<NoDataView isloading={true} text={"正在加载中..."}></NoDataView>
        )
    }
}

const styles = StyleSheet.create({
    container:{
        flex: 1,
        marginTop:Platform.OS=='ios'?35:0,
        backgroundColor: '#F8F8F8',
        position: 'relative',
    },
    headerContainer:{
        flexDirection: 'row',
        backgroundColor: '#d81e06',
        justifyContent: 'space-around',
        alignItems: 'center',
        height: 65,
        paddingTop: 25,
        paddingBottom: 5
    },
    headerLogo:{
        width:45,
        height:45,
    },
    headerSearchContainer:{
        width: screenWidth * 0.7,
        height: 33,
        borderRadius: 18,
        backgroundColor: 'rgba(255,255,255,.3)'
    },
    swipeItem:{
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center'
    },
    headerSearchImage:{
        width: 17,
        height: 17,
        marginRight: 5
    },
    headerSearchText:{
        color: '#F8F8F8',
    },
    headerRightImage:{
        width:27,
        height:27,
    },
    tabViewItemContainer: {
        flex: 1,
        backgroundColor: '#FFCCCC',
        justifyContent: 'center',
        alignItems: 'center'
    },
    columnSelect: {
        justifyContent: 'center',
        alignItems: 'center',
        position: 'absolute',
        width: screenWidth* .1,
        height: 50,
        top: 0,
        right: 0,
    }
})

import React, { PureComponent } from 'react'
import {
    Text,
    View,
    Image,
    TouchableOpacity,
    Dimensions,
    ImageBackground,
    StyleSheet,
    Alert,
    FlatList,
    DeviceEventEmitter,
    ActivityIndicator,
    TouchableWithoutFeedback,
    PanResponder
} from 'react-native'
import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'
import Slider from "react-native-slider";
// import VideoPlayer from '../components/VideoPlayer';
import Video from 'react-native-video'
import VideoWrapper from '../views/VideoWrapper'
import {sys,NoDataView,isIphoneX} from "../common/Data"
const {width: screenWidth, height: screenHeight} = Dimensions.get('window')
import { Platform, NativeModules } from 'react-native';
import AnimHeart from '../pages/AnimHeart'
const { StatusBarManager } = NativeModules;
import HttpUtils from "../common/HttpUtil"
const STATUSBAR_HEIGHT = Platform.OS === 'ios' ? (isIphoneX()?34:0) : StatusBarManager.HEIGHT;


export default class Live extends PureComponent {

  

    constructor(props) {
        super(props);
        
        this.state = {
            isShowControll:false,
            duration: 0, //视频时长
            currentTime: 0, //视频当前播放的时间
            isPaused: false,
            current:0,
            heartList: [],
            sourceData: [],
            currentPage:1,
            refreshState:false,
            isLoading:true,
            maximumValue:0,
            videoHeigh:screenHeight-49-STATUSBAR_HEIGHT
        }
        this.tapStartTime = null
        this._panResponder = PanResponder.create({
            onStartShouldSetPanResponder: (evt, gestureState) => true,
            onPanResponderGrant: this._onPanResponderGrant
        })
    
    }

    

    _onPanResponderGrant = (ev)=>{
        if(!this.isDoubleTap()) return
        const { pageX, pageY } = ev.nativeEvent
        var r = Math.random() * 16 | 0,
        v = (r & 0x3 | 0x8);
        let id = v.toString();

        // 设置位置数据，渲染AnimHeart组件
        // this.setState(({heartList})=>{
        //     heartList.push({
        //         x: pageX - 60,
        //         y: pageY - 60,
        //         key: id // 使用shortid生成唯一的key值
        //     })
        //     return {
        //         heartList
        //     }
        // })

        this.setState({
            heartList:[{
                x: pageX - 120,
                y: pageY - 120,
                key: id // 使用shortid生成唯一的key值
            }]
        })
        
      }
      
      // 检测是否为双击
      isDoubleTap(){
        const curTime = +new Date()
        if(!this.tapStartTime || curTime - this.tapStartTime > 300) {
            this.tapStartTime = curTime
            return false
        }
        this.tapStartTime = null
        return true
      }
    
      genId(){
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random() * 16 | 0,
              v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
          }).toUpperCase();
       }
 

     //加载数据
     getDataList(isReload,num=0){
        let codeurl = 'app_video/smalllists.html';
        let formData = new FormData();
        formData.append('offset',this.state.currentPage)
        formData.append('orderCode','lastTime');
        formData.append('cid',0);
        formData.append('tag_id',79)
        formData.append('pageSize',100)
        HttpUtils.post(codeurl,formData)
            .then(result=>{
                if(result['respCode']==1){
                    let newList = result["data"]["rows"];
                    let dataList =  isReload ? newList : [...this.state.sourceData, ...newList]
                    let state = dataList.length >= result['data']['total'] ? RefreshState.NoMoreData : RefreshState.Idle;
                    this.setState({
                        sourceData: dataList,
                        refreshState: state
                    })
                    if(state === RefreshState.Idle){
                        this.state.currentPage+=1
                    }
                   // 服务器没有数据
                    //this.setState({refreshState: RefreshState.EmptyData})
                }else{
                }
            }).catch(error=>{
                if(num<=0){
                    this._getNewsData(isReload,num+1);
                }
            Alert.alert("JSON.stringify(error")
            })  
    }

    onHeaderRefresh = () => {

        this.state.currentPage = 0;
        this.getDataList(true);

    }

    onFooterRefresh = () => {

        this.setState({
            refreshState: RefreshState.FooterRefreshing,
        })
        this.getDataList(false)
    }



    renderItem = ({item,index}) => {
        
        return(
            <View>
            
        
        <VideoWrapper
                ref={ref => this.videoWrapper = ref}
                item={item}
                hasfullScreen={false}
                hasBackButton={false}
                style={{flex: 1,width:screenWidth,height:this.state.videoHeigh}}
                paused={index===this.state.current?this.state.isPaused:true}
                />

       
          
            {/* <TouchableWithoutFeedback onPress={()=>{
                this.setState({
                    isPaused:!this.state.isPaused
                })
            }}>
                    <View
                        style={{
                            position: 'absolute',
                            top: 0,
                            left: 0,
                            width: screenWidth,
                            height: this.state.videoHeigh-40,
                            backgroundColor: this.state.isPaused ? 'rgba(0,0,0,0.2)' : 'transparent',
                            alignItems: 'center',
                            justifyContent: 'center',
                        }}
                    > */}
                        {/* {
                            this.state.isPaused ? 
                                <TouchableWithoutFeedback onPress={this._onTapPlayButton}>
                                    <Image 
                                        style={styles.playButton}
                                        source={require('../../assets/images/icon_video_play.png')}
                                    />
                                </TouchableWithoutFeedback> : null
                        } */}
                    {/* </View>
                </TouchableWithoutFeedback> */}
            
             <Text numberOfLines={2} style={{position:'absolute',color:sys.whiteColor,fontSize:15,marginTop:sys.dheight-200,width:screenWidth-100,textAlign:'center'}}>{item.title}</Text>
   

            {/* <View style={{marginLeft:sys.dwidth-200,marginTop:sys.dheight-400,position:'absolute',width:200,justifyContent:'flex-end',alignItems:'flex-end',padding: 20}}>
                   
                    <View   {...this._panResponder.panHandlers} style={{marginLeft:sys.dwidth/2+100}}>
                        <Image source={require('../../assets/images/praise.png')} resizeMode={'contain'} style={{width:40,height:40}}/>
                        <Text style={{textAlign:'center'}}>2.1万</Text>
                    </View>
                    <TouchableOpacity style={{marginLeft:sys.dwidth/2+100,marginTop:20}}>
                        <Image source={require('../../assets/images//i_reply.png')} resizeMode={'contain'} style={{width:40,height:40}}/>
                        <Text style={{textAlign:'center'}}>300</Text>
                    </TouchableOpacity>
                    <TouchableOpacity  style={{marginLeft:sys.dwidth/2+100,marginTop:20}}>
                        <Image source={require('../../assets/images/share.png')} resizeMode={'contain'} style={{width:40,height:40}}/>
                        <Text style={{textAlign:'center'}}>分享</Text>
                    </TouchableOpacity>
              </View>
              <Text style={{position: 'absolute',fontSize:15,marginTop:sys.dheight-100,textAlign:'center'}}>{item.title}</Text>
           */}
            </View> 
        )
    }

    // _setFlatListHeight = (e) => {
    //     let height = e.nativeEvent.layout.height
    //     if (this.state.flatHeight < height) {
    //         this.setState({
    //             flatHeight: height
    //         })
    //     }
    // }
    onLayout = (e) =>{

        if(this.state.videoHeigh != e.nativeEvent.layout.height){
            this.setState({
                videoHeigh:e.nativeEvent.layout.height
    
            })
        }
    }

  
    componentDidMount() {
        this.getDataList()

        DeviceEventEmitter.addListener('stop', param => {
            // console.log(param);
            // do something...
           
            // if(!this.state.isPaused){
                this.state.isPaused = true;
                this.forceUpdate();
            //  }
          
           
             
        });

    }

    componentWillUnmount() {
        // 页面卸载前处理监听
        DeviceEventEmitter.removeAllListeners();
        // Alert.alert("66666")
    }



    
  

    render(){

        _this = this;
        const VIEWABILITY_CONFIG = {
    		viewAreaCoveragePercentThreshold: 80,//item滑动80%部分才会到下一个
		};
        return(
            this.state.sourceData.length!=0?
            <View style={styles.container}>
                
           {/* <RefreshListView
          data={this.state.sourceData}
          keyExtractor={
            (item, index) => {index + '';
          }}
          renderItem={this.renderItem}
          refreshState={this.state.refreshState}
          onHeaderRefresh={this.onHeaderRefresh}
          onFooterRefresh={this.onFooterRefresh}
          ListHeaderComponent={this.renderHeader}
          pagingEnabled={true}
          // 可选
          footerRefreshingText='玩命加载中 >.<'
          footerFailureText='我擦嘞，居然失败了 =.=!'
          footerNoMoreDataText='-我是有底线的-'
          footerEmptyDataText='-好像什么东西都没有-'
          showsHorizontalScrollIndicator={false}
          onViewableItemsChanged={this._onViewableItemsChanged}/> */}
            <FlatList 
                onLayout={(event)=>this.onLayout(event)}
                style={{width:sys.dwidth}}
                  data={this.state.sourceData}
                  renderItem={this.renderItem}
                  horizontal={false}
                  pagingEnabled={true}
                  getItemLayout={(data, index) => {
                      return {length: this.state.videoHeigh, offset:  this.state.videoHeigh * index, index}
                  }}
                  keyExtractor={(item, index) => index.toString()}
                  //viewabilityConfig={VIEWABILITY_CONFIG}
                  showsHorizontalScrollIndicator={false}
                  onViewableItemsChanged={this._onViewableItemsChanged}
              />
    

              {
          this.state.heartList.map(({x, y, key}, index)=>{
            return <AnimHeart 
              onEnd={()=>{
                // 动画完成后销毁组件
                this.setState(({heartList})=>{
                    heartList.splice(index,1)
                    return {
                        heartList
                    }
                })
              }} 
              key={key} // 不要使用index作为key值
              x={x} 
              y={y} 
            />
          })
        }

            </View>:<NoDataView isloading={true} text={"正在加载中..."}></NoDataView>
        )
    }

    _onViewableItemsChanged({viewableItems, changed}) {
		//这个方法为了让state对应当前呈现在页面上的item的播放器的state
		//也就是只会有一个播放器播放，而不会每个item都播放
		//可以理解为，只要不是当前再页面上的item 它的状态就应该暂停
        //只有100%呈现再页面上的item（只会有一个）它的播放器是播放状态
        if(viewableItems.length === 1){
            _this.setState({
                current:viewableItems[0].index,
            })
        }
    }
}

export function formatTime(second) {
    let h = 0, i = 0, s = parseInt(second)
    if (s > 60) {
        i = parseInt(s / 60)
        s = parseInt(s % 60)
    }
    //补零
    let zero = function (v) {
        return (v >> 0) < 10 ? '0' + v : v
    }

    return [zero(h), zero(i), zero(s)].join(':');
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f8f8f8',
    },
    bottomControl: {
        flexDirection: 'row',
        height: 50,
        justifyContent: 'center',
        alignItems: 'center',
        position: 'absolute',
        bottom: 0,
        left: 0
      },
      timeText: {
        fontSize: 13,
        color: 'white',
        marginLeft: 5,
        marginRight: 5
      },
      videoTitle: {
        fontSize: 14,
        color: 'white'
      },
      control_play_btn: {
        width: 24,
        height: 24,
        marginLeft: 15
      },
      control_switch_btn: {
        width: 15,
        height: 15,
        marginRight: 15
      },
      backButton: {
        flexDirection:'row',
        width: 44,
        height: 44,
        alignItems:'center',
        justifyContent:'center',
        marginLeft: 10
      }
})

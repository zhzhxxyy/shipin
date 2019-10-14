/**
 * Created by guangqiang on 2017/9/7.
 */
import React, {Component} from 'react'
import {View, Text, Alert,TouchableOpacity,Dimensions,Image,Slider,StyleSheet, ActivityIndicator, Modal, ScrollView} from 'react-native'
import VideoWrapper from '../views/VideoWrapper'

import Orientation from 'react-native-orientation'

// import {Actions} from 'react-native-router-flux'


// import {MessageBarManager} from 'react-native-message-bar'
//import {StyleSheet} from '../../utils'
const playerHeight = 250
import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'
import HttpUtils from "../common/HttpUtil"
const {width: screenWidth, height: screenHeight} = Dimensions.get('window');
import {sys,DateTool} from "../common/Data"

var self;
export default class MovieDetail extends Component {

  constructor(props) {
  
    super(props)
    this.player = null
    this.state = {
      rate: 1,
      slideValue: 0.00,
      currentTime: 0.00,
      duration: 0.00,
      paused: false,
      playIcon: 'music_paused_o',
      isTouchedScreen: true,
      modalVisible: true,
      isLock: false,
      params:null
    }
  }

  componentWillMount() {
    const init = Orientation.getInitialOrientation()
    this.setState({
      init,
      orientation: init,
      specificOrientation: init
    })
  }

  
  componentDidMount() {
    self = this;
    this.getNetData();


    Orientation.addOrientationListener(this._updateOrientation)
    Orientation.addSpecificOrientationListener(this._updateSpecificOrientation)
  }

  getNetData(paramid=0){
    let codeurl = 'app_video/play/id/'+(paramid==0?this.props.navigation.state.params.item.id:paramid);
    
    let formData = new FormData();
    HttpUtils.post(codeurl,formData)
    .then(result=>{
        if(result['respCode']==1){

            this.setState({
                params:result["data"],
                sourceData:result["data"]["recom_list"]
            })
           // 服务器没有数据
            //this.setState({refreshState: RefreshState.EmptyData})
        }else{
        }
    }).catch(error=>{
        
        Alert.alert(">>>>"+JSON.stringify(error))
    })  
  

  }

  componentWillUnmount() {
    Orientation.removeOrientationListener(this._updateOrientation)
    Orientation.removeSpecificOrientationListener(this._updateSpecificOrientation)
  }

  _updateOrientation = orientation => this.setState({ orientation })
  _updateSpecificOrientation = specificOrientation => this.setState({ specificOrientation })

  loadStart(data) {
    console.log('loadStart', data)
  }

  setDuration(duration) {
    this.setState({duration: duration.duration})
  }

  setTime(data) {
    let sliderValue = parseInt(this.state.currentTime)
    this.setState({
      slideValue: sliderValue,
      currentTime: data.currentTime,
      modalVisible: false
    })
  }

  onEnd(data) {
    this.player.seek(0)
  }

  videoError(error) {
    this.showMessageBar('播放器报错啦！')(error.error.domain)('error')
    this.setState({
      modalVisible: false
    })
  }

  onBuffer(data) {
    console.log('onBuffer', data)
  }

  onTimedMetadata(data) {
    console.log('onTimedMetadata', data)
  }

  showMessageBar = title => msg => type => {
    // MessageBarManager.showAlert({
    //   title: title,
    //   message: msg,
    //   alertType: type,
    // })
  }

  play() {
    this.setState({
      paused: !this.state.paused,
      playIcon: this.state.paused ? 'music_paused_o' : 'music_playing_s'
    })
  }

  renderModal() {
    return (
      <Modal
        animationType={"none"}
        transparent={true}
        visible={this.state.modalVisible}
        onRequestClose={() => alert("Modal has been closed.")}
      >
        <View style={styles.indicator}>
          <ActivityIndicator
            animating={true}
            style={[{height: 80}]}
            color={'red'}
            size="large"
          />
        </View>
      </Modal>
    )
  }

  onFullScreen(status) {
    // Set the params to pass in fullscreen status to navigationOptions
    this.props.navigation.setParams({
      fullscreen: !status
    })
  }

  renderItem = ({item}) => {
    return(
        <TouchableOpacity onPress={()=>{
          self.getNetData(item.id);
        }} style={{width:sys.dwidth/2}}>
                        <View style={{marginLeft:5,width:sys.dwidth/2-10,marginBottom:10}}>
                            <Image source={{uri:item.thumbnail}} style={{width:sys.dwidth/2-10,height:sys.dwidth/4-20}} />
                            <Text numberOfLines={2} style={{marginTop:10,marginLeft:5,fontSize:13, 
                              width:sys.dwidth/2-20,lineHeight:15, 
                              color:sys.titleColor
                              }}>{item.title}</Text>
                        </View>
            
                    </TouchableOpacity>
    )
    }

    keyExtractor = (item, index) => index + '';
    
    renderFooter = () => {

      return (
        
                    <View style={{height:20}}></View>
                  )
    }

    renderHeader = () => {


        return (
        
            <View>


             
               <Text numberOfLines={2} style={{margin:10,fontSize:15,fontWeight:'bold',width:sys.dwidth-20}}>{this.state.params?this.state.params.title:""}</Text>
               
              <View style={{flexDirection:'row'}}>
                  <Text style={{color:sys.subTitleColor,marginLeft:10,fontSize:16,width:sys.dwidth/3*2}}>{this.state.params?DateTool.formatDate(this.state.params.update_time,"yyyy-MM-dd")+" . ":""}</Text>
                  <Text style={{marginLeft:10,width:sys.dwidth/3-30,color:sys.subTitleColor,textAlign:'right'}}>播放{this.state.params?this.state.params.click:100}次</Text>
              </View>

              <Text style={{margin:10,fontSize:17,fontWeight:'bold',width:sys.dwidth}}>猜你喜欢</Text>
               
             
            </View>
             

       )
    }

    onMorePress(){
        this.props.navigation.goBack()
    }

  render() {

    const {orientation, isLock} = this.state
    const url = 'http://flv3.bn.netease.com/tvmrepo/2018/6/H/9/EDJTRBEH9/SD/EDJTRBEH9-mobile.mp4'
    const logo = 'https://your-url.com/logo.png'
    const placeholder = 'https://your-url.com/placeholder.png'

    // Alert.alert(JSON.stringify(this.state.params?this.state.params.url:"nihao"));
    return (
          <ScrollView contentContainerStyle={styles.container}>
              {/* <Video
                  autoPlay
                  url={this.state.params?this.state.params.url:url}
                  //title={this.state.params?this.state.params.title:"加载中..."}
                  logo={logo}
                  style={{width:sys.dwidth,height:sys.dwidth/2}}
                  placeholder={this.state.params?this.state.params.thumbnail:logo}
                  onMorePress={() => this.onMorePress()}
                  //onFullScreen={status => this.onFullScreen(status)}
                  rotateToFullScreen
                  
              /> */}
               <VideoWrapper
               navigation={this.props.navigation}
                style={{flex: 1,width:screenWidth,height:screenWidth}}
                ref={ref => this.videoWrapper = ref}
                item={this.state.params}
                 />
            <RefreshListView
            data={this.state.sourceData}
            keyExtractor={this.keyExtractor}
            renderItem={this.renderItem}
            //refreshState={this.state.refreshState}
            onHeaderRefresh={()=>{}}
            numColumns ={2}
            //onFooterRefresh={this.onFooterRefresh}
            ListHeaderComponent={this.renderHeader}
            ListFooterComponent={this.renderFooter}
            // 可选
            footerRefreshingText='玩命加载中 >.<'
            footerFailureText='我擦嘞，居然失败了 =.=!'
            footerNoMoreDataText='-我是有底线的-'
            footerEmptyDataText='-好像什么东西都没有-'
          />
          </ScrollView>
        )
  }
}

const styles = StyleSheet.create({
    container: {
        flex: 1
      },  
  movieContainer: {
    // justifyContent: 'space-between'
  },
  videoPlayer: {
    position: 'absolute',
    top: 44,
    left: 0,
    bottom: 0,
    right: 0,
  },
  
  timeStyle: {
    width: 35,
    color: sys.whiteColor,
    fontSize: 12
  },
  slider: {
    flex: 1,
    marginHorizontal: 5,
    height: 20
  }
})
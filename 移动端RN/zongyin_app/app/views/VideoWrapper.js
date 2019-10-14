import Video from 'react-native-video'
import React from 'react'
import {
    View,
    StyleSheet,
    ActivityIndicator,
    Text,
    Image,
    TouchableOpacity,
    BackHandler,
    NativeModules,
    PanResponder,
    Alert,
    ImageBackground
} from 'react-native'
import Slider from "react-native-slider";
import Orientation from 'react-native-orientation'


/**
 * 格式化播放时间
 * @param {}} value 
 */
function formatTime(value) {
    let h = Math.floor(value / 60 / 60);
    let m = Math.floor(value / 60 - h * 60);
    let s = Math.floor(value - (h * 60 * 60 + m * 60));

    if (m < 10) {
        m = "0" + m;
    }

    if (s < 10) {
        s = "0" + s;
    }
    return `${h}:${m}:${s}`;
}

const ratio = 0.56;
const rate = [1,1.25,1.5,1.75,2]

export default class VideoWrapper extends React.Component {

  

    showControler = true;
    qualityMap = new Map();
    aualityArrays = []
    currentQuality;
    swichquality = false;
    showQualityModal = false;

    state = {
        loadding: true,
        maximumValue: 0,
        minimumValue: 0,
        value: 0,
        paused: this.props.hasOwnProperty('paused')?this.props.paused:true,
        hasfullScreen:this.props.hasOwnProperty('hasfullScreen')?this.props.hasfullScreen:true,
        fullscreen: false,
        hasBackButton:this.props.hasOwnProperty('hasBackButton')?this.props.hasBackButton:true,
        videoWidth: DEVICE.width,
        videoHeight: this.props.style.height,
        end: false,
        showSpeed: false,
        speedOffset: 0,
        rateIndex:0
    }

    firstPageX = 0;

    componentWillReceiveProps(nextProps) {
        console.log(">>>>>>>>>")
        console.log(nextProps)
        if(nextProps.hasOwnProperty('paused')){
            const { paused } = this.state
            const newdata = nextProps.paused.toString()
            if (paused.toString() !== newdata) {
                this.setState({
                    paused: nextProps.paused,
                    // hasfullscreen:nextProps.hasfullscreen
                 })
            }
        }
      }

    constructor(props) {
        super(props)

        this.panResponder = PanResponder.create({
            onMoveShouldSetPanResponder: (e, gestureState) => {
                let currentPageX = e.nativeEvent.pageX;
                let offset = currentPageX - this.firstPageX;
                if (Math.abs(offset) > 30) {
                    return true;
                }
                return false;
            },
            onStartShouldSetPanResponder: (e, gestureState) => {
                this.firstPageX = e.nativeEvent.pageX;
                this._showControllerMethod(true)
                return false;
            },
            onPanResponderGrant: (e, gestureState) => {
                console.log('netlog-onPanResponderGrant')
            },
            onPanResponderMove: (e, gestureState) => {
                let currentPageX = e.nativeEvent.pageX;
                let offset = currentPageX - this.firstPageX;
                console.log('netlog-onPanResponderMove', currentPageX)

                if(offset > 0){
                    offset -= 30
                }else{
                    offset += 30;
                }
                let value = this.state.value + Math.round(offset);
                
                this.video && this.video.seek(value)
                this.setState({ showSpeed: true, speedOffset: Math.round(offset) })
            },
            onPanResponderStart: (e, gestureState) => {

            },
            onPanResponderEnd: (e, gestureState) => {
                this.firstPageX = 0;
                this.setState({ showSpeed: false, speedOffset: 0 })
            },
        })
    }

    onBack = () => {
        if (this.state.fullscreen) {
            this._onFullscreenPlayerWillDismiss()
            return true;
        }
    }

    startPlayVideo() {
        this.setState({ paused: false })
    }

    _showControllerMethod(show) {
        if (show) {
            if (this.timeout) {
                clearTimeout(this.timeout)
                this.timeout = null;
            }
            this.showControler = true;
            this.forceUpdate();
            if (!this.state.paused) {
                this.timeout = setTimeout(() => {
                    this.showControler = false;
                    this.forceUpdate();
                }, 5000);
            }
        } else {
            if (!this.state.paused) {
                if (this.timeout) {
                    clearTimeout(this.timeout)
                    this.timeout = null;
                }
                this.timeout = setTimeout(() => {
                    this.showControler = false;
                    this.forceUpdate();
                }, 5000);
            }
        }
    }

    componentWillMount() {
        this.subscriber = BackHandler.addEventListener("hardwareBackPress", this.onBack)
    }

    componentWillUnmount() {
        if (this.timeout) {
            clearInterval(this.timeout)
        }
        this.subscriber && this.subscriber.remove()
    }

    _onLoadStart = () => {
        this.setState({ loadding: true })
        //console.log('netlog-', '_onLoadStart')
    }

    _onLoad = data => {
        this.props.onLoad && this.props.onLoad(data);
          if (this.state.loadding !== false) {
                this.setState({ loadding: false })
                //console.log('loadstart-', 'loadstart')
            }
        if (this.swichquality) {
            let ratio = data.duration / this.state.maximumValue;
            this.video.seek(this.state.value * ratio)
        } else {
            if (this.props.seek) {
                this.video.seek((this.props.seek / 100) * data.duration)
            }
            this._showControllerMethod(false)
        }
    }

    _onBuffer = data => {
        // if (this.state.loadding !== data.isBuffering) {
        //     this.setState({ loadding: data.isBuffering })
        // }
    }

    _onProgress = data => {
        this.props.onProgress && this.props.onProgress(data)
        this.setState({
            maximumValue: data.seekableDuration,
            value: data.currentTime
        })
    }

    _onEnd = () => {
        this.setState({ paused: true, value: 0, loadding: false }, () => {
            this.video.seek(0)
            this._showControllerMethod(true)
        })
        this._onFullscreenPlayerWillDismiss()
        this.props.onEnd && this.props.onEnd();
        console.log('netlog-', '_onEnd')
    }

    _onError = () => {
        console.log('netlog-', '_onError')
    }

    _onValueChange = number => {
        if (this.video) {
            this.video.seek(number);
        }
    }

    _onFullscreenPlayerWillPresent = (event) => {
        this.setState({
            fullscreen: true,
            videoWidth: DEVICE.screenHeight - (DEVICE.isIphoneX ? 0 : 0),
            videoHeight: DEVICE.width
        },() => {
            Orientation.lockToLandscape()
            if (DEVICE.android) {
                NativeModules.AppModule.setFullScreen(true)
            }
        })
    }

    _onFullscreenPlayerWillDismiss = event => {
        this.setState({
            fullscreen: false,
            videoWidth: DEVICE.width,
            videoHeight: this.props.style.height
        },() => {
            Orientation.lockToPortrait()
            if (DEVICE.android) {
                NativeModules.AppModule.setFullScreen(false)
            }
        })
    }

    _onSeek = () => {
        if (this.swichquality) {
            this.swichquality = false;
        }
    }

    _playerIconClick = () => {
        this.setState({ paused: !this.state.paused }, () => this._showControllerMethod(this.state.paused))
    }

   

    _renderControler = () => {
        // Alert.alert(this.state.videoHeight + ">>>>>>");
        let speedOffset = Math.round(this.state.speedOffset);
        let speedOffsetValue = speedOffset > 0 ? `+${speedOffset}s` : `${speedOffset}s`
        let videoWidth = this.state.videoWidth;
        let videoHeight = this.state.videoHeight;
        let totalTime = formatTime(this.state.maximumValue);
        let currentTime = formatTime(this.state.value);
        let playerIcon = this.state.paused ? require('../../source/image/icon_video_play.png') : require('../../source/image/icon_video_pause.png');
        let maginH = this.state.hasBackButton?105:40
        return (
            <View
                style={[styles.controler, {
                    width: videoWidth,
                    height: videoHeight,
                }]}>
                 {this.showControler ? 
                    <View style={{ position: 'absolute', width: videoWidth, height: videoHeight, justifyContent: 'space-between' }}>
                        {this.state.hasBackButton?
                        <TouchableOpacity
                            style={{ flexDirection: 'row', alignItems: 'center', marginLeft: 5, marginTop: 30 }}
                            activeOpacity={0.7}
                            onPress={() => {
                                
                                if (this.state.fullscreen) {
                                    this._onFullscreenPlayerWillDismiss()
                                } else {
                           
                                    this.props.navigation && this.props.navigation.goBack();
                                }
                            }}>
                            <Image
                                resizeMode="contain"
                                style={{ width: 35, height: 35 }}
                                source={require('../../source/image/player_return.png')}></Image>
                            {
                                this.state.fullscreen ? (
                                    <Text style={{ color: 'white', fontSize: 17, marginLeft: 10 }}>{this.props.item ? this.props.item.title : "糟糕,标题未获取到"}</Text>
                                ) : null
                            }
                        </TouchableOpacity>:null}

                        <View style={{flexDirection: 'row', alignItems: 'center',marginLeft: (this.state.fullscreen?15:5),marginTop:videoHeight-maginH}}>
                            <TouchableOpacity
                                activeOpacity={1}
                                onPress={this._playerIconClick}>
                                <Image
                                    resizeMode="cover"
                                    style={styles.controlerIcon}
                                    source={playerIcon}></Image>
                            </TouchableOpacity>
                            <Text style={[styles.textStyle,{fontSize:12}]}>{currentTime}/{totalTime}</Text>
                            <Slider
                                minimumTrackTintColor='red'
                                thumbTintColor="red"
                                maximumTrackTintColor="white"
                                onValueChange={this._onValueChange}
                                minimumValue={this.state.minimumValue}
                                maximumValue={this.state.maximumValue}
                                value={this.state.value}
                                style={{ flex: 1, marginHorizontal: 5 }}>
                            </Slider>

                            <Text 
                                style={{paddingHorizontal:5,color:'white'}}
                                onPress={() => {
                                    let index = this.state.rateIndex + 1;
                                    if(index >= rate.length){
                                        index = 0;
                                    }
                                    this.setState({rateIndex:index})
                                }}
                                >x{rate[this.state.rateIndex]}</Text>
                            {this.state.hasfullScreen?
                            <TouchableOpacity
                                activeOpacity={1}
                                style={{ marginRight: (this.state.fullscreen?30:10)}}
                                onPress={() => {
                                    if (this.state.fullscreen) {
                                        this._onFullscreenPlayerWillDismiss()
                                    } else {
                                        this._onFullscreenPlayerWillPresent()
                                    }
                                }}>
                                <Image
                                    resizeMode="cover"
                                    style={styles.controlerIcon}
                                    source={require('../../source/image/icon_video_fullscreen.png')}></Image>
                            </TouchableOpacity>:null}
                        </View>
                    </View>:null}
                
            </View>
        );
    }

  

    _renderGesture() {
        let videoWidth = this.state.videoWidth;
        let videoHeight = this.state.videoHeight;
        return (
            <View
                style={styles.gesture}
                {...this.panResponder.panHandlers}
            ></View>
        )
    }

    render() {
        let videoWidth = this.state.videoWidth;
        let videoHeight = this.props.style.height;
        let item = this.props.item;
        let url = null;
        if(item){
            url = item.url;//this.getVideoUrl(item, this.currentQuality);//item ? { uri: this.getVideoUrl(item, this.currentQuality) } : {};
        }
        // Alert.alert(url);
        let loaddingView = <View style={[styles.controler, { width: videoWidth, height: videoHeight }]}>
            <ActivityIndicator animating={true} color="white"></ActivityIndicator>
            <Text style={[styles.textStyle, { top: 3 }]}>拼命加载中...</Text>
    </View>
        return (
            <View>
               {url?
                <Video
                    ref={ref => this.video = ref}
                    muted={false} //控制音频是否静音
                    repeat={false} //确定在到达结尾时是否重复播放视频。
                    onEnd={this._onEnd} //视频播放结束时的回调函数
                    paused={this.state.paused} //暂停
                    rate={rate[this.state.rateIndex]}//播放速率
                    repeat={false}
                    playInBackground={false}
                    allowsExternalPlayback={false}
                    ignoreSilentSwitch={'ignore'}
                    onSeek={this._onSeek}
                    onProgress={this._onProgress} //视频播放过程中每个间隔进度单位调用的回调函数
                    onBuffer={this._onBuffer} //远程视频缓冲时的回调
                    onLoadStart={this._onLoadStart}
                    onLoad={this._onLoad} //加载媒体并准备播放时调用的回调函数。
                    onError={this._onError}               // 播放失败后的回调
                    resizeMode="contain" //缩放模式
                    controls={false}
                    style={{ width: videoWidth, height: videoHeight, backgroundColor: 'black' }} //组件样式
                    source={{uri:url}}
                ></Video>
              :loaddingView}
                {this.state.loadding? loaddingView:null}
                {this._renderGesture()}
                {this._renderControler()}

            </View>
        );
    }
}



const styles = StyleSheet.create({
    controler: {
        position: 'absolute',
        top: 0,
        justifyContent: 'center',
        alignItems: 'center'
    },
    gesture: {
        position: 'absolute',
        top: 35,
        left: 40,
        right: 40,
        bottom: 35,
        backgroundColor: 'transparent'
    },
    textStyle: {
        color: 'white',
    },
    controlerIcon: {
        width: 20,
        height: 20,
    }
})
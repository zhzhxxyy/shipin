import React, {Component} from 'react';
import {
    StyleSheet,
    View,
    Text,
    WebView,
    BackHandler,
    Platform,
    TouchableOpacity,
    Image
} from 'react-native';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import { sys,NoDataView } from '../common/Data';



export default class WebViewScene extends Component {



    // static navigationOptions = ({navigation}) => ({
    //     header:null
    // });


    static  navigationOptions = ({navigation}) => {
        const { params } = navigation.state;


        let titStr = ''

        if (params.hasOwnProperty('title')) {
            titStr = params['title']
        }


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
                headerTitle:titStr,
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
            headerTitle:titStr,
            headerTitleStyle:{
                alignSelf:'center',
                flex: 1,
                textAlign: 'center',
                
            },
            headerRight: <View />

        }
    };



    // 构造
    constructor(props) {
        super(props);
        // 初始状态
        this.state = {
            url: "",
            title: "",
            loading: true,
            isBackButtonEnable: false,
            isForwardButtonEnable: false
        };
    }

    componentDidMount() {

        BackHandler.addEventListener("webHardwareBackPress", ()=> {
            try {
                if (this.state.isBackButtonEnable) {
                    this.refs._webView.goBack();//返回上一个页面
                    return true;//true 系统不再处理 false交给系统处理
                }
            } catch (error) {
                return false;
            }
            return false;
        })
    }

    componentWillUnmount() {
        BackHandler.removeEventListener("webHardwareBackPress");
    }

    render() {
        const {params} = this.props.navigation.state;

        this.props.navigation.headerTitle = params.uri.title
        return (
            <View  style={{flex:1}}>
            {/* <View style={styles.top}></View> */}
            {/* <View style={styles.container}> */}

                <WebView
                    style={styles.webView}
                    ref="_webView"
                    title={params.uri.title}
                    source={{uri:params.uri.content}}//获取url参数
                    automaticallyAdjustContent Insets={true}
                    domStorageEnabled={true}
                    javaScriptEnabled={true}
                    scalesPageToFit={true}
                    startInLoadingState={true}
                    renderLoading={()=>{return <NoDataView isloading={true}/>}}
                    onNavigationStateChange={this._onNavigationStateChange.bind(this)}
                />
                </View>
                // </View>
        )
    }

    //WebView导航状态改变
    _onNavigationStateChange(navState) {

        this.setState({
            url: navState.url,
            title:  "",
            loading: navState.loading,
            isBackButtonEnable: navState.canGoBack,
            isForwardButtonEnable: navState.canGoForward,
        })
    }

}

const styles = StyleSheet.create({


    top: {
        top:0,

        width:sys.dwidth,
        height:50,
        backgroundColor: "red",
    },

    container: {
   

        width:sys.dwidth,
        height:400,
        backgroundColor: "red",
    },
    webview: {
       
        backgroundColor: "red",
        width:sys.dwidth,
        height:400
    },
    loadingText: {
        color: '#8a8a8a',
        fontSize: 16
    }
})
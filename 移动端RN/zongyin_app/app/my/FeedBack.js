import React, {Component} from 'react';
import {
    View, Text, StyleSheet, ScrollView, Alert,Platform,
    Image, TouchableOpacity, NativeModules, Dimensions,AsyncStorage,DeviceEventEmitter,TextInput
} from 'react-native';


var ImagePicker = NativeModules.ImageCropPicker;
import {sys} from "../common/Data"

const host = sys.host;
import HttpUtils from "../common/HttpUtil"

export default class FeedBack extends Component {

    static  navigationOptions = ({navigation}) => ({
        headerTitle:"意见反馈",
        headerTitleStyle:{
            alignSelf:'center',
            flex: 1,
            textAlign: 'center',
            
        },
        headerRight: <View />
    });

    constructor() {
        super();
        this.state = {
            message: ''
        };
    }

    render() {
        return (<View style={styles.container}>
                <View style={styles.view}>
                    <View style={styles.inputView}>
                        <TextInput style={styles.feedBack}
                            underlineColorAndroid='transparent'
                                   androidtextAlignVertical={'top'}
                                   multiline = {true}
                            placeholder="感谢您提宝贵的意见"
                            onChangeText={(text) => this.setState({message:text})}

                        >
                        </TextInput>
                    </View>

                </View>

            <TouchableOpacity onPress={() => this.feedBack()} style={styles.button}>
                <Text style={styles.buttontext}>提交</Text>
            </TouchableOpacity>

        </View>);
    }

    feedBack(){

        if(this.state.message.length <= 0){
            Alert.alert("请输入您的宝贵意见");
            return;
        }

        let codeurl = host + '/index/index/createOpinion';
        let formData = new FormData();
        // 请求参数 ('key',value)
        formData.append('message', this.state.message);

        HttpUtils.post(codeurl, formData)
            .then(result => {
                if (result['respCode'] == 1) {

                    Alert.alert("反馈成功", result['respMsg'])
                    this.props.navigation.goBack();
                } else {
                    Alert.alert("保存失败", result['respMsg'])
                }

            })
            .catch(error => {
                Alert.alert("反馈失败" + JSON.stringify(error));
            })
    }


}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent:'space-between',
        backgroundColor:sys.grayColor
    },
    // view:{
    //      height:Platform.OS=='ios'?sys.dheight - 113:sys.dheight-130,
    // },
    inputView: {
        backgroundColor: '#ffffff',
        width: sys.dwidth,
        height:200,
    },
    feedBack:{
        width: sys.dwidth,

        height:200,
    },
    button:{
        backgroundColor:sys.mainColor,
        height:50,
        alignItems:'center',
        justifyContent:'center',

    },
    buttontext:{
        color:'#ffffff',
        textAlign:'center',
        fontSize:18,
        marginBottom:0
    },
});

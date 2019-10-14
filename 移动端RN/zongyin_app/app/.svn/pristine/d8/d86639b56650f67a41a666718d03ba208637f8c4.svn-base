import React, { Component } from 'react';
import { Image, ScrollView, StyleSheet, Text, View,Dimensions, TouchableOpacity,ImageBackground } from 'react-native';
import {NavigationActions} from "react-navigation";
import {NoDataView, sys} from "./common/Data"
let image1 = require('./res/images/guide1.png');
let image2 = require('./res/images/guide2.png');
let image3 = require('./res/images/guide3.png');

const { width, height } = Dimensions.get('window');
export default class guideView extends Component {

    constructor() {
        super();
    };
    render() {
        return (
            <ScrollView
                contentContainerStyle={styles.contentContainer}
                bounces={false}
                pagingEnabled={true}
                horizontal={true}>

                <ImageBackground source={image1}
                                 style={styles.backgroundImage}>
                    <TouchableOpacity
                        style={styles.rightupBtn}
                        onPress={() => {
                            this.goMainPage()
                        }}
                    >
                        <Text style={styles.goText}>跳过</Text>
                    </TouchableOpacity>
                </ImageBackground>

                <ImageBackground source={image2} style={styles.backgroundImage}>
                    <TouchableOpacity
                        style={styles.rightupBtn}
                        onPress={() => {
                            this.goMainPage()
                        }}

                    >
                        <Text style={styles.goText}>跳过</Text>
                    </TouchableOpacity>
                </ImageBackground>
                <View style={[styles.backgroundImage,styles.btnOut]}>
                    <Image source={image3} style={[styles.backgroundImage1,styles.btnOut]} >

                    </Image>
                    <TouchableOpacity
                        style={styles.btn}
                        onPress={() => {
                            this.goMainPage()
                        }}
                    >
                    </TouchableOpacity>
                </View>

            </ScrollView>);
    }

    goMainPage(){
        const resetAction = NavigationActions.reset({
            index: 0,
            actions: [
                NavigationActions.navigate({routeName: 'Tab', params: { token: '123456' }})
            ]
        })
        this.props.navigation.dispatch(resetAction);
    }
};
var styles = StyleSheet.create({
    contentContainer: {
        width: width * 3,
        height: height,
        // backgroundColor:'red'
    },
    backgroundImage: {
        width: width,
        height: height,


    },
    backgroundImage1:{
        width: width,
        height: height,
        resizeMode:'stretch'
    },
    btnOut:{
        alignItems:'center',
    },
    rightupBtn:{
        marginLeft:sys.dwidth-100,
        width:100,
        height:100,
        backgroundColor:'transparent',
        alignItems:'center',
        justifyContent:'center'
    },
    btn:{
        width:sys.dwidth,
        height:100,
        borderRadius:8,
        justifyContent:'center',
        alignItems:'center',
        marginTop:-100,
         // backgroundColor:'yellow'

    },
    btnText:{
        fontSize:18,
        color:'#fff'
    },
    goText:{
        color:sys.whiteColor,
        fontSize:16,
        // backgroundColor:sys.mainColor
    }
});
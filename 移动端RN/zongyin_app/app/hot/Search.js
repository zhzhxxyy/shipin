import React, {Component} from 'react';
import {
    StyleSheet,
    View,
    Text,
    WebView,
    TouchableOpacity,
    ScrollView,
    Image,
    TextInput,
    Platform
} from 'react-native';
import { sys,NoDataView } from '../common/Data';
import HttpUtils from "../common/HttpUtil"
import toast from 'react-native-root-toast'

import Header, { HeaderItem } from '../components/Header'
import BaseComponent from '../components/BaseComponent'
import Colors from '../utils/Colors'

const backIcon = require('../../source/icons/back_icon.png')

var _this;
var clickLastTime=0;
const shheight = 40;
export default class Search extends BaseComponent {

    state = {
        // datas: [{id:"1",orderNum:"12",keyword:"yi"},{id:"2",orderNum:"22",keyword:"er"}],
        historyContents: [],
        hotKeys:[{name:"周星驰"},{name:"成龙"},{name:"周润发"},{name:"刘德华"},{name:"古天乐"},{name:"吴京"},{name:"李连杰"},{name:"王宝强"},{name:"甄子丹"}],
        content: '',
        LOAD_STATE:this.LOAD_SUCCESS
    }

    initData() {
        this.queryHistoryVideo()
    }

    /**
     * 查询搜索历史记录
     */
    queryHistoryVideo(){
        storage.load("searchKeys", (dic) => {
            if (dic != "" && dic != null) {
                this.setState({ historyContents:dic })
            }
         })
    }

    enterSearchInfo(key, flag) {
        if (key) {
            if (flag) {
                var length= this.state.historyContents.length;
                var history=[];
                for(var i=0;i<length;i++){
                    if(this.state.historyContents[i].name!=key){
                        history.push({name:this.state.historyContents[i].name});
                    }
                } 
                history.push({name:key});
                storage.save("searchKeys", history);
                this.setState({ historyContents:history });
            }
            this.props.navigation.navigate('SearchInfo', { key })
        }
    }

    _clearAllHistorySearchContens = () => {
        storage.save("searchKeys", [])
        this.setState({historyContents:[]})
    }

    _renderHeader() {
        return (
            <Header>
                <HeaderItem onClick={() => this.props.navigation.goBack()}>
                    <Image source={backIcon}></Image>
                </HeaderItem>
                <TextInput
                    autoFocus={true}
                    numberOfLines={1}
                    onChangeText={text => this.setState({ content: text })}
                    maxLength={20}
                    placeholder="搜一搜,全都有"
                    returnKeyType="search"
                    onSubmitEditing={e => this.enterSearchInfo(e.nativeEvent.text,true)}
                    underlineColorAndroid='transparent'
                    style={{ flex: 1, height: 35, padding: 0, borderRadius: 5, backgroundColor: 'rgba(0,0,0,0.1)', justifyContent: 'center', alignItems: 'center', textAlign: 'center' }}>
                </TextInput>
                <HeaderItem onClick={() => this.enterSearchInfo(this.state.content, true)}>
                    <Text style={{ fontSize: 15, color: 'black', fontWeight: 'bold' }}>搜索 </Text>
                </HeaderItem>
            </Header>
        );
    }

    renderComponent() {
        let keys = []
        keys = Object.keys(this.state.historyContents).reverse();
        keys.splice(10, keys.length);
        let showHistoryContents = keys.length > 0
       
        let hot=[];
        hot=Object.keys(this.state.hotKeys).reverse();
      

        return (
            <ScrollView contentContainerStyle={{ padding: 10 }}>
                {
                    showHistoryContents ? (
                        <View style={{ marginBottom: 15 }}>
                            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginVertical: 10 }}>
                                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                                    <View style={{ width: 3, height: 15, backgroundColor: 'black' }}></View>
                                    <Text style={{ color: 'black', fontSize: 15, marginLeft: 5 }}>历史搜索</Text>
                                </View>
                                <Text onPress={this._clearAllHistorySearchContens}>清空记录</Text>
                            </View>
                            <View style={{ flexDirection: 'row', flexWrap: 'wrap', alignItems: 'center', }}>
                                {
                                    keys.map((key, index) => {
                                        let item = this.state.historyContents[key]
                                        return (
                                            <Text
                                                key={'search_children_' + index}
                                                onPress={() => this.enterSearchInfo(item.name, true)}
                                                numberOfLines={1}
                                                style={{ fontSize: 15, margin: 5, padding: 5, color: 'white', backgroundColor: Colors.mainColor, borderRadius: 4 }}>{item.name}
                                            </Text>
                                        );
                                    })
                                }
                            </View>
                        </View>
                    ) : null
                }

                <View style={{ flexDirection: 'row', alignItems: 'center', marginVertical: 10 }}>
                    <View style={{ width: 3, height: 15, backgroundColor: 'black' }}></View>
                    <Text style={{ color: 'black', fontSize: 15, marginLeft: 5 }}>热门搜索</Text>
                </View>


                <View style={{ flexDirection: 'row', flexWrap: 'wrap', alignItems: 'center', }}>
                                {
                                    hot.map((key, index) => {
                                        let item = this.state.hotKeys[key]
                                        return (
                                            <Text
                                                key={'search_' + index}
                                                onPress={() => this.enterSearchInfo(item.name, true)}
                                                numberOfLines={1}
                                                style={{ fontSize: 15, margin: 5, padding: 5, color: 'white', backgroundColor: Colors.mainColor, borderRadius: 4 }}>{item.name}
                                            </Text>
                                        );
                                    })
                                }
                            </View>


                {/* <View style={styles.container}>
                    {
                        this.state.datas.map((item) => {
                            return (
                                <TouchableOpacity
                                    key={'search_' + item.id}
                                    style={{ width: itemWidth, marginVertical: 5, flexDirection: 'row', alignItems: 'center' }}
                                    onPress={() => this.enterSearchInfo(item.keyword, false)}
                                    activeOpacity={0.7}>
                                    <Text style={{ fontSize: 15 }}>{item.orderNum} </Text>
                                    <Text numberOfLines={1} style={{ fontSize: 15, marginLeft: 5 }}>{item.keyword}</Text>
                                </TouchableOpacity>
                            );
                        })
                    }
    
                </View> */}
            </ScrollView>
        );
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
    textCenter:{
        textAlign: 'center',
        textAlignVertical: 'center',
        ...Platform.select({
            ios: { lineHeight: shheight},
            android: {}
        })
    },
    loadingText: {
        color: '#8a8a8a',
        fontSize: 16
    }
})
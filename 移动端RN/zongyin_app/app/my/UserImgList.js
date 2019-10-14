//
//  Created by Liu Jinyong on 17/4/5.
//  Copyright © 2016年 Liu Jinyong. All rights reserved.
//
//  @flow
//  Github:
//  https://github.com/huanxsd/react-native-refresh-list-view

import React, {Component} from 'react'
import {View, StyleSheet,Text, DeviceEventEmitter,Platform,Alert,TouchableOpacity,Image,PixelRatio
    ,ScrollView,NativeModules} from 'react-native'
import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'

import {NoDataView, sys,TitleHeadView, isIphoneX} from "../common/Data"
const host = sys.host;
import HttpUtils from "../common/HttpUtil"

import Toast,{DURATION} from 'react-native-easy-toast';//导入弹出框组件


const { StatusBarManager } = NativeModules;



export default class UserImgList extends Component {



    static  navigationOptions = ({navigation}) => {
        const { params } = navigation.state;

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
                title:'头像编辑',
                headerTitleStyle:{
                    alignSelf:'center',
                    flex: 1,
                    textAlign: 'center',
                    
                },
                
                
                headerRight:React.createElement(View, null, null),
                headerLeft:leftView
            }

        }

        return {
        title:'选择头像',
        headerTitleStyle:{
            alignSelf:'center',
            flex: 1,
            textAlign: 'center',
            
        },
        
        
        headerRight:React.createElement(View, null, null),
       }
    };

    constructor(props) {
        super(props)

        this.state = {
            islogin:0,
            dataList:[],
            refreshState: false,
            currentPage:1,
            pageSize:10,
            noNetwork:false,
            noData:false,
            headViewIndext:0,
            isloading:false,
        }
    }


    componentDidMount() {
    

        storage.load(host + '/AppMember.getAllFace.do', (dic) => {
            if (dic != "" && dic != null) {
               
                this.setState({
                    dataList:dic
                })
             

            }
         })

        this.lodaData()
    }

    componentWillUnmount() {
      
     }

     lodaData(){

        this.setState({
            isloading:true
        })

        let codeurl = host + '/AppMember.getAllFace.do';
        let formData = new FormData();
        HttpUtils.post(codeurl,formData)
            .then(result=>{


                this.setState({
                    isloading:false
                })

                console.log('result')

                console.log(result)

                if(result['respCode']==1){  
                    this.setState({
                        dataList:result['data']
                    })

                    storage.save(codeurl, result['data'])

                }else{
                    
                }
            })
            .catch(error=>{
                this.setState({
                    isloading:false
                })
              
            })
     }


    renderCell = (info) => {



        // console.log('inininnnininininin')

        console.log(info)

        return <TouchableOpacity onPress={this.itemClick.bind(this, info)}>
          
        <View style={[styles.mengban,{marginLeft:0,height:(sys.dwidth-40)/3,
           //borderRightWidth:(index+1)%3==0?0:1,
           borderBottomWidth:1,borderColor:sys.silveryColor,width:(sys.dwidth-40)/3,marginTop:10,marginLeft:10}]}>
            <Image
            source={{
            uri: info.item.url
            }}
            style={{width:(sys.dwidth-40)/3,resizeMode:'contain',
                height:(sys.dwidth-40)/3,alignItems:'center'}}
            />

        </View>
    </TouchableOpacity>



        // return <Cell info={info.item}  onPress={this.itemClick.bind(this, info)}/>
    }




    itemClick(e){


        console.log('eeeeeeeeeeeeeeeee')
        console.log(e.item['value'])

        
        Alert.alert('是否要更换头像','',
                [
                    {text:"取消", onPress: ()=>{

                        }},
                    {text:"确定", onPress: ()=>{
                        

                        let codeurl = host + '/AppMember.personalInfo.do';
                        let formData = new FormData();

                        formData.append("face",e.item['value']);

                        HttpUtils.post(codeurl,formData)
                            .then(result=>{


                                // console.log('codeurl')

                                // console.log(result['respMsg'])

                                

                                if(result['respCode']==1){  
                                    
                                    this.props.navigation.state.params.changeUseImg(e.item['url']);

                                    this.props.navigation.goBack()

                                }else{
                                    this.refs.toast.show(result['respMsg'], DURATION.LENGTH_LONG);
                                }
                            })
                            .catch(error=>{
                                
                                // console.log('8888888888888')

                                // console.log(error)

                                this.refs.toast.show(error, DURATION.LENGTH_LONG);
                            
                            })


                    }}
                ]
            );


    }

    // renderHeadView(){
    //     // return this.state.dataList.length?null:<NoDataView text={"暂无修行"}/>
    //     return null;
    // }

    // renderFooterView(){
    //     return null;renderCell
    // }


    onHeaderRefresh = () => {
       

             this.lodaData()
        

    }

    render() {

                 
            return (
                (!this.state.dataList.length)?<NoDataView click={()=>this.lodaData()}
                    isloading = {this.state.isloading}
                        text = {this.state.isloading?"正在加载中":"暂无内容，点击重新加载"}
                    />:<View>
                    <RefreshListView
            style={{width:sys.dwidth}}
            data={this.state.dataList}
            keyExtractor={this.keyExtractor}
            renderItem={this.renderCell}
            refreshState={this.state.refreshState}
            onHeaderRefresh={this.onHeaderRefresh}
            // onFooterRefresh={this.onFooterRefresh}
            numColumns ={3}
            // ListHeaderComponent={this.renderHeadView}
            // ListFooterComponent={this.renderFooterView}
            // horizontal={false}
            ItemSeparatorComponent={this._separator}
            // 可选
            footerRefreshingText= '玩命加载中 >.<'
            footerFailureText = '我擦嘞，居然失败了 =.=!'
            footerNoMoreDataText= '-我是有底线的-'
        />

        <Toast  //提示
                    ref="toast"
                    style={{backgroundColor:'gray'}}
                    position='center'
                    positionValue={200}
                    opacity={0.7}
                    textStyle={{color:'white'}}
                /> 

               </View>

    
                
            );
             
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
         marginTop: Platform.OS == 'ios' ? 0 : 0,
        backgroundColor:sys.whiteColor
        
    }
})



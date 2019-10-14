import React from 'react'
import { Text, Platform,View,Alert } from 'react-native'
import BaseFlatListComponent from '../components/BaseFlatListComponent'
import Banner from '../views/Banner'
import ListItem from '../views/ListItem'

import {sys} from '../common/Data'
import HttpUtils from "../common/HttpUtil"
import EZSwiper from 'react-native-ezswiper';

let screenWidth = sys.dwidth;
export default class Movie extends BaseFlatListComponent {

    pageSize = 4;

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

    _renderHeader() {
        return null
    }

    getRequestAction(pageIndex, pageSize) {

        return new Promise((resolve,reject) => {
           
            this._getNewsData(0).then(result=>{
                resolve(result)
            })
        })
    }

    _getNewsData(num){
        
        let codeurl = 'app_index';
        let formData = new FormData();
       return new Promise((resolve,reject) => {
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
    
                    var vmap = new Map();
                    videoList:result["data"]["video_list"].map((data)=>{
                        vmap[data.name]=data.list
                    })
                    
                    this.state.videoList = vmap;
                    let arr = [];
                    
                    result["data"]["banner"].forEach(item => {
                        arr.push(item.images_url);
                    })

                    let MoviePageData = 
                            [{
                                title:"最热",
                                queryList:result["data"]["hot_list"]
                            },{
                                title:"最新",
                                queryList:result["data"]["new_list"]
                            },{
                                title:"推荐",
                                queryList:result["data"]["recom_list"]
                            }
                        
                            ]

                    
                    this.state.banner = arr;
                   
                
                    // this.state.recom_list = result["data"]["recom_list"]
                    // this.state.new_list = result["data"]["new_list"]
                    // this.state.hot_list = result["data"]["hot_list"]
                    // this.setState({
                    //     tabArr:tabArr,
                    //     isLoaded:true
                    // })


        
                    resolve(MoviePageData);
                
                }else{

                }
            }).catch(error=>{
                Alert.alert("xio"+error);
                if(num<=0){
                    this._getNewsData(isReload,num+1);
                }
             Alert.alert(JSON.stringify(error))
            })     
         })
        }
        

    filterResponse(result) {
        return result.data.data;
    }

    renderFlatViewHeader = () => {
        
        return <Banner id={1} banner={this.state.banner} navigation={this.props.navi}></Banner>

        return (
            // this.props.images?
            <View>
           
             <EZSwiper 
            dataSource={this.state.banner}
            width={ screenWidth }
            height={150 }
            renderRow={this.renderImageRow}
            onPress={this.onPressRow} 
            index={2}                
            cardParams={{cardSide:screenWidth*0.867, cardSmallSide:150*0.867,cardSpace:screenWidth*(1-0.867)/2*0.2}}  
            />

            <View style={{marginTop:5,flexDirection:'row'}}>
                <View style={{backgroundColor:sys.mainColor,marginLeft:10,width:50,height:25,
                borderRadius:15,borderColor:sys.yellowColor,borderWidth:1}}>
            <Text style={{width:50,height:25,textAlign:'center',marginTop:-1,textAlignVertical:'center',
            color:sys.whiteColor,fontSize:15,...Platform.select({
            ios: { lineHeight: 25},
            android: {},
         
            })}}>公告</Text>  
            </View>  
            <EZSwiper                             
            style={{}}  
            dataSource={['联系客服，充值永久会员只要299' ,'每邀请一人,奖励3金币']}                              
            width={screenWidth-40}                              
            height={30}                              
            renderRow={this.renderText}                              
            loop={true}                             
            horizontal={true}                                                 
            autoplayTimeout={2.5}                           
            onPress={this.onPressRow}                           
            scrollEnabled={false}/>
        
            </View> 
            </View>
        )
    }

    renderRow = rowData => {
        
        return (
            <ListItem navigation={this.props.navi} data={rowData}></ListItem>
        );
    }
}
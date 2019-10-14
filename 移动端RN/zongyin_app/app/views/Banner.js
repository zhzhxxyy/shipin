import React from 'react'
import {
    Image,
    View,
    Text,
    Alert,
    TouchableOpacity,
    Platform
} from 'react-native'
import BaseComponent from '../components/BaseComponent'



import {sys} from '../common/Data'

import EZSwiper from 'react-native-ezswiper';


const finalStyle = { width: DEVICE.width, height: DEVICE.width * 0.5 };
let screenWidth = sys.dwidth;
//this.props.id 0推荐 1电影 2电视剧 3动漫 4综艺
export default class Banner extends BaseComponent {

    containerStyle = finalStyle;

    state = {
        datas: [],
        classDatas: []
    }

    filterData(data) {
        //过滤掉广告轮播
        return data.filter(item => {
            return item.targetType == 2 && item.videoInfoId != 0;
        })
    }

    initData() {
        setTimeout(() => {
            // let result = this.props.id == 0 ? data.RecommendBannerData : (
            //     this.props.id == 1 ? data.MovieBannerData : (
            //         this.props.id == 2 ? data.TVBannerData : (
            //             this.props.id == 3 ? data.CartoonBannerData : data.VarietyBannerData
            //         )
            //     )
            // )
            this.setState({
                //datas: this.filterData(result.data)
                datas:this.props.banner
            }, () => this.update(this.LOAD_SUCCESS))
        }, config.delayed);
       
    }

    _enterVideoInfo = data => {
        data.coverUrl = data.thumbnailUrl;
        this.props.navigation.navigate("VideoInfoPage", { data })
    }

    renderImageRow(obj, index) {  

        return (
          <View style={style={flex:1,width: screenWidth*0.867, height: screenWidth*0.867/3,backgroundColor: 'white'}}>
           <Image
           style={{
                margin:5,
                flex: 1,
                alignItems: 'center',
                justifyContent: 'center',
                resizeMode: 'cover'
        }}
            source={{uri:obj}}/>
          </View>
        )
      } 

      renderText(obj){

                return (
                  
                     <Text style={{height:30,marginLeft:10,
                    textAlignVertical: 'center',fontSize:14,...Platform.select({
                        ios: { lineHeight: 30},
                        android: {}
                        })}}>{obj}</Text>
         
                )
          }

    renderComponent() {
       
        // let items = [];
        // for (let i = 0; i < this.state.datas.length; i++) {
        //     let obj = this.state.datas[i];
        //     let image = obj?{uri:obj} : require('../../source/image/nor.png')
        //     let item = (
        //         <TouchableOpacity
        //             key={'banner' + i}
        //             onPress={() => this._enterVideoInfo(obj)}
        //             activeOpacity={1}>
        //             <Image style={finalStyle} source={image} resizeMode="cover"></Image>
        //             {/* <View style={{ position: 'absolute', bottom: 0, paddingBottom: 25, paddingTop: 5, paddingLeft: 5, width: '100%', backgroundColor: 'rgba(0,0,0,0.3)' }}>
        //                 <Text
        //                     numberOfLines={1}
        //                     style={{ color: 'white', fontWeight: '400', fontSize: 15 }}>{obj.title}</Text>
        //             </View> */}
        //         </TouchableOpacity>
        //     );
        //     items.push(item)
        // }
        return (
            // <Swiper
            //     removeClippedSubviews={DEVICE.android ? true : false}
            //     paginationStyle={{ bottom: 10, justifyContent: 'flex-end', paddingRight: 5 }}
            //     style={finalStyle}
            //     width={finalStyle.width}
            //     height={finalStyle.height}
            //     loop={true}
            //     activeDotColor={Colors.mainColor}
            //     dotColor="white"
            //     autoplay={true}
            //     showsPagination={true}>
            //     {items}
            // </Swiper>
            <View>
           
             <EZSwiper 
            dataSource={this.state.datas}
            width={ screenWidth }
            height={screenWidth/3}
            renderRow={this.renderImageRow}
            //onPress={this.onPressRow} 
            index={2}                
            cardParams={{cardSide:screenWidth*0.867, cardSmallSide:screenWidth/3*0.867,cardSpace:screenWidth*(1-0.867)/2*0.2}}  
            />

            <View style={{marginTop:5,flexDirection:'row'}}>
                <View style={{backgroundColor:sys.mainColor,marginLeft:10,width:50,height:25,
                borderRadius:15,borderColor:sys.mainColor,borderWidth:1}}>
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
        );
    }
}

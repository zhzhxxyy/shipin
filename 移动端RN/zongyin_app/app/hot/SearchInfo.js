import React, { PureComponent } from 'react'
import {
    FlatList,
    TouchableOpacity,
    Dimensions,
    StyleSheet,
    Image,
    Text,
    View,
    Alert,
    ImageBackground,
} from 'react-native'
import RefreshListView, {RefreshState} from 'react-native-refresh-list-view'
import HttpUtils from "../common/HttpUtil"
import {sys} from '../common/Data'

import toast from 'react-native-root-toast'

let screenWidth=sys.dwidth


export default class SearchInfo extends PureComponent {

    self = null;

    static navigationOptions = options => {
        return {
            title: options.navigation.state.params.key
        }
    }

    constructor(props) {
        super(props);
   
        this.state = {
            sourceData: [],
            currentPage:1,
            refreshState:false,
            isEmpty:false,
            images:[],
            keyWords:this.props.navigation.state.params.key
        }
    }

    //加载数据
    getNewsList(isReload,num=0){
        let codeurl = 'app_video/lists.html';
        let formData = new FormData();
        formData.append('page',this.state.currentPage)
        formData.append('key_word',this.state.keyWords)
        // formData.append('orderCode','lastTime');
        // formData.append('cid',this.props.cid);
        formData.append('pageSize',10)
        HttpUtils.post(codeurl,formData)
            .then(result=>{
                if(result['respCode']==1){
                    let newList = result["data"]["rows"];
                    let dataList =  isReload ? newList : [...this.state.sourceData, ...newList]
                    let state = dataList.length >= result['data']['total'] ? RefreshState.NoMoreData : RefreshState.Idle;
                    this.setState({
                        sourceData: dataList,
                        refreshState: state,
                        isEmpty:dataList.length>0?false:true
                    })
                    if(state === RefreshState.Idle){
                        this.state.currentPage+=1
                    }
                    if(dataList.length<=0){
                        toast.show("暂无数据，请试试其他关键字");
                    }
                }else{
                }
            }).catch(error=>{
                if(num<=0){
                    this._getNewsData(isReload,num+1);
                }
                Alert.alert("JSON.stringify(error")
            })  
    }


    //Header视图
    renderHeader = () => {
       return null;
    }

    renderImageRow(obj, index) {  

        return (
          <View style={style={flex:1,width: screenWidth*0.867, height: screenWidth*0.867/2,backgroundColor: 'white'}}>
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


 


    /**
     * 此函数用于为给定的item生成一个不重复的key
     * key的作用是使React能够区分同类元素的不同个体，以便在刷新的时候能确定其变化的位置，减少重复渲染的开销
     * 若不指定此函数，则默认抽取item.key作为key值，若key.item不存在，则使用数组下标
     */
    keyExtractor = (item, index) => index + '';

    onHeaderRefresh = () => {
        this.state.currentPage = 1;
        this.getNewsList(true);
    }

    onFooterRefresh = () => {

        this.setState({
            refreshState: RefreshState.FooterRefreshing,
        })
        this.getNewsList(false)
    }

    renderItem = ({item}) => {
        return(
            <HomeFlatListItem 
                item={item}
                onPressItem={this._onPressItem}
                selected={this.state.selected === item.id}
            />
        )
    }

    _onPressItem = (item) => { 
        this.setState({
            selected: item.id
        })
            this.props.navigation.push('MovieDetail', {item})
            return
    }

    //组件渲染后开始加载数据
    componentDidMount() {
        self = this;
        this.setState({
            refreshState:true
        })
        this.state.currentPage = 1;
        this.getNewsList(true);
    }

    render(){

        return(
            <View style={styles.container}>
        <RefreshListView
          data={this.state.sourceData}
          keyExtractor={this.keyExtractor}
          renderItem={this.renderItem}
          refreshState={this.state.refreshState}
          onHeaderRefresh={this.onHeaderRefresh}
          onFooterRefresh={this.onFooterRefresh}
          ListHeaderComponent={this.renderHeader}

          // 可选
          footerRefreshingText='玩命加载中 >.<'
          footerFailureText='我擦嘞，居然失败了 =.=!'
          footerNoMoreDataText='-我是有底线的-'
          footerEmptyDataText='-好像什么东西都没有-'
        />
      </View>
        
        )
    }

}

class HomeFlatListItem extends React.PureComponent {

    _onPress = () => {
        this.props.onPressItem(this.props.item)
    }

    render(){
        let item = this.props.item
        //判断是否是三图布局
        let isThreePic = item['imgnewextra']
        //判断是否是视频布局
        let isVideo = true;//item['videoinfo']

        if (isThreePic){
            return(
                <TouchableOpacity
                    {...this.props}
                    onPress={this._onPress}
                    style={styles.picItem}
                    activeOpacity={0.8}
                >
                    <View style={{justifyContent:'space-between'}}>
                        <Text style={{fontSize:16, lineHeight:25, color:'#2c2c2c'}}>{item.title}</Text>

                        <View style={{flexDirection:'row', marginVertical:5, justifyContent:'space-between'}}>
                            <Image source={{uri:item.imgsrc}} style={{width:screenWidth*0.35, height:80}} />
                            {
                                item.imgnewextra.map((imgItem, index) => (
                                    <Image source={{uri:imgItem.imgsrc}} key={index+''} style={{width:screenWidth*.3, height:80}} />
                                ))
                            }
                        </View>

                        <View style={{flexDirection:'row', justifyContent:'space-between', alignItems:'center'}}>
                            <View style={{flexDirection:'row'}}>
                                <Text style={{marginRight:6}}>{item.source}</Text>
                                <Text style={{marginRight:6}}>{item.replyCount}跟帖</Text>
                            </View>

                            {/*这里应该有个X号*/}
                        </View>
                    </View>
                </TouchableOpacity>
            )
        }

        if (isVideo){
            return(
                <TouchableOpacity 
                    {...this.props}
                    onPress={this._onPress}
                    style={styles.picItem}
                    activeOpacity={0.8}
                >
                    <View>
                       
                        <ImageBackground source={{uri:item.thumbnail}} resizeMode={'cover'} style={{height:180, marginVertical:6, justifyContent:'center', alignItems:'center'}}>
                            <View style={{width:50, height:50,marginTop:65 ,borderRadius:25, backgroundColor:'rgba(0,0,0,0.5)', justifyContent:'center', alignItems:'center'}}>
                                <Image source={require('./../../assets/images/i_play.png')} resizeMode={'contain'} style={{width:18, height:18, marginLeft:3}} />
                            </View>
                            <Text style={{marginTop:25,textAlign:'right',fontSize:13 ,width:sys.dwidth-30, color:sys.whiteColor}}>{item.play_time}</Text>

                        </ImageBackground>
      
                        <View style={{flexDirection:'row', justifyContent:'space-between', alignItems:'center'}}>
                            <View style={{flexDirection:'row'}}>
                                <Text numberOfLines={2} style={{marginRight:6,fontSize:14,width:sys.dwidth/3*2}}>{item.title}</Text>
                                <Text style={{marginRight:6,width:sys.dwidth/3-20,color:sys.subTitleColor,textAlign:'right'}}>播放{item.click}次</Text>
                            </View>

                            {/*这里应该有个X号*/}
                        </View>
                    </View>
                </TouchableOpacity>
            )
        }

        return(
            <TouchableOpacity
                {...this.props}
                onPress={this._onPress}
                style={styles.item}
                activeOpacity={0.8}
            >
                <View style={{width:screenWidth*0.63, height:80, justifyContent:'space-between'}} >
                    <Text style={{fontSize:16, lineHeight:25, color:'#2c2c2c'}}>{item.title}</Text>

                    <View style={{flexDirection:'row', justifyContent:'space-between', alignItems:'center'}}>
                            <View style={{flexDirection:'row'}}>
                                <Text style={{marginRight:6}}>{item.source}</Text>
                                <Text style={{marginRight:6}}>{item.replyCount}跟帖</Text>
                            </View>

                            {/*这里应该有个X号*/}
                        </View>
                </View>

                <Image source={{uri:item.imgsrc}} style={{width:screenWidth*0.3, height:80}} />
            </TouchableOpacity>
        )
    }

}

const styles = StyleSheet.create({
    container:{
        flex: 1,
        backgroundColor:'white'
    },
    item:{
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: 7,
    },
    picItem:{
        padding: 7,
    }
})
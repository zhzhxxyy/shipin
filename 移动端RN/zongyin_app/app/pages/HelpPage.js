import React from 'react'
import {
    Text,
    View,
    Image,
    ScrollView,
    TouchableOpacity
} from 'react-native'

export default class HelpPage extends React.Component{

    static  navigationOptions = ({navigation}) => {
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
            headerTitle:'帮助咨询',
            headerTitleStyle:{
                alignSelf:'center',
                flex: 1,
                textAlign: 'center',
            },
            headerLeft:leftView,
            headerRight: <View />

        }
    }

    render(){
        return (
            <ScrollView
                contentContainerStyle={{flex:1,padding:10,backgroundColor:'white'}} 
                showsVerticalScrollIndicator={false}>
                <View style={{height:40,justifyContent:'center'}}>
                    <Text style={{color:'red'}}>1.无法播放视频</Text>
                </View>
                <Text>如果出现某些视频无法播放，包含一直缓冲，闪退，网络异常，请尝试多打开几次，如果还是无法播放，请联系我们的客服进行反馈。</Text>
                
                <View style={{height:40,justifyContent:'center',marginTop:10}}>
                    <Text style={{color:'red'}}>2.搜索不到想看的视频</Text>
                </View>
                <Text>由于版权的原因，某些视频暂时无法提供，请联系我们的客服进行反馈。</Text>

                <View style={{height:40,justifyContent:'center',marginTop:10}}>
                    <Text style={{color:'red'}}>3.界面展示异常</Text>
                </View>
                <Text>界面展示异常，不美观或适配遇到问题，请联系我们的客服进行反馈。</Text>

                <View style={{height:40,justifyContent:'center',marginTop:10}}>
                    <Text style={{color:'red'}}>4.图标加载不出来</Text>
                </View>
                <Text>Android:如果遇到启动页图片，返回按键图标加载不出来，请到设置-应用程序-嘻哈影视-存储-清空数据。</Text>
            
                <View style={{height:40,justifyContent:'center',marginTop:10}}>
                    <Text style={{color:'red'}}>5.分享得金币</Text>
                </View>
                <Text>为广大网友提供，高质量的影视作品,如有侵权，请联系告知</Text>
           
            </ScrollView>
        )
    }
}
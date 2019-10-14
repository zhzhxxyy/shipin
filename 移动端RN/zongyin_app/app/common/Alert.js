import React, { Component , } from 'react';

const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
import {
    StyleSheet,
    TouchableOpacity,
    Dimensions,
    View,
    Text,
    ListView,
    Animated
} from 'react-native';

var deviceHeight = Dimensions.get('window').height;
var deviceWidth = Dimensions.get('window').width;


class Alert extends Component {

    // 构造
    constructor(props) {
        super(props);
        // 初始状态
        this.state = {
            options:[],
            title:null,
            detailText:null,
            clickAction:null,
            isShow:false,
            alertText:null,
            opacityValue:new Animated.Value(0),
            scaleValue:new Animated.Value(0)
        };
    }

    close() {
        this.setState({
            isShow:false,
            options:[],
            title:null,
            detailText:null,
            clickAction:null,
            alertText:null
        });
        this.state.opacityValue.setValue(0);
        this.state.scaleValue.setValue(0);
    }

    alertWithOptions(data,clickAction) {
        this.setState({
            isShow:true,
            options:data.options,
            title:data.title,
            detailText:data.detailText,
            clickAction:clickAction && clickAction.bind(this)
        });
        Animated.parallel([
            Animated.timing(this.state.opacityValue, {
                toValue: 1,
                duration: 200
            }),
            Animated.timing(this.state.scaleValue, {
                toValue: 1,
                duration: 100
            })
        ]).start();


    }

    alert(text) {
        this.setState({
            isShow:true,
            alertText:text
        });
        Animated.parallel([
            Animated.timing(this.state.opacityValue, {
                toValue: 1,
                duration: 200
            }),
            Animated.timing(this.state.scaleValue, {
                toValue: 1,
                duration: 100
            })
        ]).start();
    }

    renderRow(contactOption,sectionID,rowID) {
        return(
            rowID != this.state.options.length - 1 &&
            <View style={{backgroundColor:'rgba(220,220,220,1)'}}>
                <View style={{height:0.5,backgroundColor:'rgba(235,235,235,1)'}}/>
                <TouchableOpacity style={{backgroundColor:'white'}}
                                  activeOpacity={0.7}
                                  onPress={()=>this.click(rowID)}>
                    <View style={{height:44,justifyContent:'center',alignItems:'center'}}>
                        <Text style={{fontSize:16,color:'rgba(0,127,247,1)'}}>{contactOption}</Text>
                    </View>
                </TouchableOpacity>
            </View>
        )
    }   

    click(rowID) {
        console.log(rowID)
        this.close();
        this.state.clickAction ? this.state.clickAction(rowID) : alert('没有传点击调用的方法')
    }


    render() {


        // let titleView = null
         
        // if( this.state.title && this.state.detailText){
            // let titleView  = (
            //     <View style={{height:this.state.options.length > 2 ? 70 : 75,borderTopLeftRadius:10,borderTopRightRadius:10,justifyContent:'center',alignItems:'center',backgroundColor:'white'}}>
            //        <Text style={{fontSize:16,color:'black'}}>{this.state.alertText ? '提示' : this.state.title}</Text> : null
            //        <Text style={{fontSize:12,marginTop:5,color:'rgba(40,40,40,1)'}}>{this.state.alertText ? this.state.alertText : this.state.detailText}</Text>:null
            //    </View>
            // )
        // }

        // if( this.state.title && this.state.detailText == null){
        //     titleView = (
        //         <View style={{height:this.state.options.length > 2 ? 50 : 55,borderTopLeftRadius:10,borderTopRightRadius:10,justifyContent:'center',alignItems:'center',backgroundColor:'white'}}>
        //            <Text style={{fontSize:16,color:'black'}}>{this.state.alertText ? '提示' : this.state.title}</Text> : null
        //            {/* <Text style={{fontSize:12,marginTop:5,color:'rgba(40,40,40,1)'}}>{this.state.alertText ? this.state.alertText : this.state.detailText}</Text>:null */}
        //        </View>
        //     )
        // }

        // if( this.state.title == null && this.state.detailText){
        //     titleView = (
        //         <View style={{height:this.state.options.length > 2 ? 50 : 55,borderTopLeftRadius:10,borderTopRightRadius:10,justifyContent:'center',alignItems:'center',backgroundColor:'white'}}>
        //            {/* <Text style={{fontSize:16,color:'black'}}>{this.state.alertText ? '提示' : this.state.title}</Text> : null */}
        //            <Text style={{fontSize:12,marginTop:5,color:'rgba(40,40,40,1)'}}>{this.state.alertText ? this.state.alertText : this.state.detailText}</Text>:null
        //        </View>
        //     )
        // }
          

        let bottomH = 0;
        if(this.state.detailText != null && this.state.detailText.length > 30 ){
             bottomH = this.state.detailText.length/30 * 10 + 20;
            //  console.log(bottomH);
        }
       


        
        return(

            this.state.isShow &&
            <Animated.View style={{height:deviceHeight,width:deviceWidth,justifyContent:'center',alignItems:'center',backgroundColor:'rgba(0,0,0,0.4)',position:'absolute',top:0,opacity: this.state.opacityValue}}>
                <Animated.View style={{width:deviceWidth / 6 * 4,bottom:bottomH,transform: [{scale:this.state.scaleValue}]}}>
                    <View style={{borderTopLeftRadius:10,borderTopRightRadius:10,justifyContent:'center',alignItems:'center',backgroundColor:'white'}}>
                        <Text style={{fontSize:16,marginTop:15,color:'black'}}>{this.state.alertText ? '提示' : this.state.title}</Text>
                        <Text style={{fontSize:14,padding:10,lineHeight:20,color:'rgba(40,40,40,1)'}}>{this.state.alertText ? this.state.alertText : this.state.detailText}</Text>
                    </View>
                    {/* {titleView} */}
                    {this.state.options.length > 2 &&
                    <ListView
                        dataSource={ds.cloneWithRows(this.state.options)}
                        renderRow={(contactOption,sectionID,rowID) => this.renderRow(contactOption,sectionID,rowID)}
                        renderFooter={()=>
                                       <View style={{backgroundColor:'rgba(220,220,220,1)',borderBottomLeftRadius:10,borderBottomRightRadius:10,
                                                     borderTopWidth:0.5,borderTopColor:'rgba(235,235,235,1)'}}>
                                            <TouchableOpacity style={{backgroundColor:'white',borderBottomLeftRadius:10,borderBottomRightRadius:10}}
                                                        activeOpacity={0.7}
                                                        onPress={()=>this.close()}>
                                                <View style={{height:44,justifyContent:'center',alignItems:'center'}}>
                                                    <Text style={{fontSize:16,color:'red'}}>{this.state.options[this.state.options.length - 1]}</Text>
                                                </View>
                                            </TouchableOpacity>
                                       </View>}
                    />
                    }

                    {this.state.options.length == 2 &&
                    <View style={{height:40,flexDirection:'row',borderTopWidth:0.5,borderTopColor:'rgba(235,235,235,1)'}}>

                        <TouchableOpacity style={{height:40,width:deviceWidth / 3 - 0.25,backgroundColor:'white',borderBottomLeftRadius:10,justifyContent:'center',alignItems:'center'}}
                                          activeOpacity={0.8}
                                          onPress={()=>this.click('0')}>
                            <Text style={{color:'rgba(0,115,250,1)'}}>{this.state.options[0]}</Text>
                        </TouchableOpacity>
                        <View style={{width:0.5,backgroundColor:'rgba(235,235,235,1)'}}/>
                        <TouchableOpacity style={{height:40,width:deviceWidth / 3 - 0.25,backgroundColor:'white',borderBottomRightRadius:10,justifyContent:'center',alignItems:'center'}}
                                          activeOpacity={0.8}
                                          onPress={()=>this.click('1')}>
                            <Text style={{color:'rgba(0,115,250,1)'}}>{this.state.options[1]}</Text>
                        </TouchableOpacity>
                    </View>
                    }

                    {this.state.options.length == 1 &&
                    <View style={{borderTopWidth:0.5,borderTopColor:'rgba(235,235,235,1)'}}>
                        <TouchableOpacity style={{height:40,justifyContent:'center',alignItems:'center',borderBottomLeftRadius:10,borderBottomRightRadius:10,backgroundColor:'white'}}
                                          activeOpacity={0.8}
                                          onPress={()=>this.close()}>
                            <Text style={{color:'rgba(0,115,250,1)'}}>{this.state.options[0]}</Text>
                        </TouchableOpacity>
                    </View>
                    }

                    {this.state.alertText &&
                    <View style={{borderTopWidth:0.5,borderTopColor:'rgba(235,235,235,1)'}}>
                        <TouchableOpacity style={{height:40,justifyContent:'center',alignItems:'center',borderBottomLeftRadius:10,borderBottomRightRadius:10,backgroundColor:'white'}}
                                          activeOpacity={0.8}
                                          onPress={()=>this.close()}>
                            <Text style={{color:'rgba(0,115,250,1)'}}>确定</Text>
                        </TouchableOpacity>
                    </View>
                    }
                </Animated.View>

            </Animated.View>

        );
    }
}

export default Alert;
import React from 'react'
import {
    View,
    Text,
    Image,
    StyleSheet,
    TouchableOpacity,
    ScrollView
} from 'react-native'
import Colors from '../utils/Colors';
import {sys} from '../common/Data'

var itemWidth = Math.floor((DEVICE.width)/ 2);
var itemHeight = Math.floor(itemWidth/2)
var finalStyle = { marginLeft:0,width: itemWidth, height: itemHeight }

export default class ListItem extends React.Component {

    enterDetialPage = data => {
        this.props.navigation.navigate("MovieDetail", {item:data})
    }

    enterViedoListPage = data => {
        this.props.navigation.navigate("Search", { title: data.title, id: data.id })
    }

    _getTagBackgroundColor = tag => {
        if (tag == "抢鲜") {
            return "#573D1B"
        } else if (tag == "1080P") {
            return "#C47F14";
        } else {
            return "red"
        }
    }

    render() {
        let data = this.props.data.item;
        let items = [];
        var ishorizontal = false
        var gheight = itemHeight + 50
        if (data.queryList) {

            for (let i = 0; i < data.queryList.length; i++) {
                let obj = data.queryList[i];
                let tagName = obj.gold == 0 ? null : obj.gold+"金币"
                let tagBackgroundColor = this._getTagBackgroundColor(tagName)
                let complete = obj.episodeState == 1;
                let updateTag;
                if (complete) {
                    if (obj.episodeUploadCount > 1) {
                        updateTag = "已完结";
                    }
                } else {
                    updateTag = obj.episodeUploadCount > 1 ? obj.type != 4 ? `更新至${obj.episodeUploadCount}集` : `更新至${obj.episodeUploadCount}期` : null;
                }


                let style = (i + 1) % 2 == 1 ? {} : {};
                let image = obj.thumbnail ? { uri: obj.thumbnail } : require('../../source/image/nor.png')
                
                if(data.title == "最热"){
                    ishorizontal = true
                    itemWidth = Math.floor((DEVICE.width)/ 3);
                    itemHeight = Math.floor(itemWidth/2)
                    finalStyle = { marginLeft:0,width: itemWidth, height: itemHeight }
                }else if(data.title == "最新"){
                    ishorizontal = false
                    itemWidth = Math.floor((DEVICE.width)/ 2);
                    itemHeight = Math.floor(itemWidth/2)
                    finalStyle = { marginLeft:0,width: itemWidth, height: itemHeight }
                }else{
                    ishorizontal = false
                    itemWidth = Math.floor((DEVICE.width));
                    itemHeight = Math.floor(itemWidth/2)
                    finalStyle = { marginLeft:0,width: itemWidth, height: itemHeight }
                }
               let item = (
                  <TouchableOpacity
                        key={'listitem_children' + data.title + i}
                        activeOpacity={0.7}
                        style={style}
                        onPress={() => this.enterDetialPage(obj)}>
                        <View style={finalStyle}>
                            <Image style={{marginLeft:5,width:itemWidth-10,height:itemHeight}} resizeMode="cover" source={image}></Image>
                            {tagName ? (
                                <View style={{ height:15,position: 'absolute', borderRadius: 10, backgroundColor: sys.yellowColor, top: 5, right: 5, paddingHorizontal: 5 }}>
                                    <Text style={{ color: 'white', fontSize: 10,marginTop:1 }}>{tagName}</Text>
                                </View>
                            ) : null}
                            {updateTag ? (
                                <View style={{ position: 'absolute', width: '100%', bottom: 0, paddingVertical: 5, backgroundColor: 'rgba(0,0,0,0.3)', alignItems: 'center', justifyContent: 'center' }}>
                                    <Text style={{ color: 'white', fontSize: 8 }}>{updateTag}</Text>
                                </View>
                            ) : null}
                        </View>
                        <Text
                            style={{ width: finalStyle.width, paddingVertical: 10, textAlign: 'center' ,fontSize:13}}
                            numberOfLines={2}>
                            {obj.title}
                        </Text>
                    </TouchableOpacity>

                );
                items.push(item)

            }

        }

        return (
            <View style={{ backgroundColor: 'white' }}>
                <View style={styles.itemBetweenStyle}>
                    <View style={{ flexDirection: 'row', alignItems: 'center', paddingLeft: 5, maxWidth: itemWidth * 2 }}>
                        <View style={{ width: 3, height: 15, backgroundColor: sys.mainColor, marginRight: 5 }}></View>
                        <Text numberOfLines={1} style={styles.title}>{data.title}</Text>
                    </View>
                    <TouchableOpacity
                        style={styles.itemBetweenStyle}
                        activeOpacity={0.7}
                        onPress={() => this.enterViedoListPage(data)}>
                        <Text style={[styles.title, { fontSize: 12 }]} >精彩内容绝对劲爆</Text>
                        {/* <Image
                            resizeMode="contain"
                            style={{ width: 15, height: 15 }}
                            source={require('../../source/image/detailspage_go.png')}></Image> */}
                    </TouchableOpacity>
                </View>


                {ishorizontal? <ScrollView horizontal={ishorizontal}  showsHorizontalScrollIndicator={false} style={{height:ishorizontal?itemHeight+50:200,width:sys.dwidth}}
                                           contentContainerStyle={{ padding: 0 }}>


                <View style={{
                    // width: DEVICE.width,
                    flexDirection: 'row',
                    flexWrap: 'wrap',
                }}>
                    {items}
                </View></ScrollView>: <View style={{
                    // width: DEVICE.width,
                    flexDirection: 'row',
                    flexWrap: 'wrap',
                }}>
                    {items}
                </View>
                    }
            </View>
        );
    }
}

const styles = StyleSheet.create({
    flex: {
        width: DEVICE.width,
        minHeight: finalStyle.height,
        flexDirection: 'row',
        flexWrap: 'wrap',
    },
    title: {
        color: Colors.mainColor,
        fontSize: 17,
        paddingVertical: 10,
    },
    itemBetweenStyle: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        height: 40,
        marginRight:5
        // backgroundColor:'blue'
    },
})
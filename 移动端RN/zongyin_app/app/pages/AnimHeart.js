import React, { PureComponent } from 'react'
import {Animated} from 'react-native'
const ROTATE_ANGLE = ['-35deg','-25deg','0deg','25deg','35deg']

export default class AnimHeart extends PureComponent{
  constructor(props){
    super(props)

    // 创建一个动画值对象，并使用插值运算实现透明度和缩放的效果
    this.anim = new Animated.Value(0)

    // 设置随机旋转角度
    this.rotateValue = ROTATE_ANGLE[Math.floor(Math.random()*4)]

  }
  render(){
    const {x, y} = this.props
    
    return <Animated.Image
        style={{
            position:'absolute',
            left: x,
            top: y,
            opacity: this.anim.interpolate({
              inputRange:[0, 1, 2],
              outputRange:[1, 1, 0] // 根据动画值0-1-2的变化，调整透明度
            }),
            transform: [{
              scale: this.anim.interpolate({
                inputRange: [0, 0.6, 0.8],
                outputRange: [1, 0.8, 0.6] // 根据动画值0-1-2的变化，调整缩放比例
              })
            },{
              rotate: this.rotateValue
            }],
            
        }}
        source={require('../../assets/images/i_praise.png')} 
    />
  }
  
  componentDidMount(){
    // 使用顺序执行动画函数
    Animated.sequence([
      
        // 使用弹簧动画函数
        Animated.spring(
            this.anim,
            {
                toValue: 1,
                useNativeDriver: true, // 使用原生驱动
                bounciness: 2 // 设置弹簧比例
            }
        ),

        // 使用定时动画函数
        Animated.timing(
            this.anim,
            {
                toValue: 2,
                useNativeDriver: true
            }
        )
    ]).start(()=>{
        // 动画完成后回调
        this.props.onEnd && this.props.onEnd()
    })
  }

  componentWillUnmount(){
    //console.warn('unmount')
  }

  // 禁止该组件重新渲染，提升性能
  shouldComponentUpdate(){
    return false
  }
}

/*
注意: 
动画序列中，如果第一个动画中的useNativeDriver设置为true，
此时动画便交于原生端进行执行，不可再切换为JS驱动，后续动画的useNativeDriver也必须设置为true
*/


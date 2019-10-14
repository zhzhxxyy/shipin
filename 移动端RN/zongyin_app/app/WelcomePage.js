/**
 * Created by penn on 2016/12/14.
 */

import React,{Component} from 'react';
import {
    View,
    StyleSheet,
    Image,
    Text,
    Alert, BackHandler, ToastAndroid
} from 'react-native'
import {NavigationActions} from 'react-navigation'

import ThemeDao from './common/ThemeDao'
import SplashScreen from 'react-native-splash-screen'
import { sys } from './common/Data';


export default class WelcomePage extends Component {
    constructor(props) {
        super(props);

    }

     componentDidMount() {

       
        if(true){
            // this.props.navigation.reset([NavigationActions.navigate({ routeName: 'Search' })]) ;
            this.props.navigation.reset([NavigationActions.navigate({ routeName: 'Tab' })])
            return;
        }
        new ThemeDao().getTheme().then((data)=>{
            this.theme=data;
        })
        this.timer=setTimeout(()=> {
            SplashScreen.hide();
            // storage.load('isFrist', (ret) => {
            //     Alert.alert('isfirst')
            //     if (ret != '' && ret != null) {
                    // const resetAction = NavigationActions.reset({
                    //     index: 0,
                    //     actions: [
                    //         NavigationActions.navigate({routeName: 'Tab', params: { token: '123456' }})
                    //     ]
                    // })
                    // this.props.navigation.dispatch(resetAction);
                    this.props.navigation.reset([NavigationActions.navigate({ routeName: 'Tab' })])
        //         } else {
        //             Alert.alert('no tisfirst')
        //             storage.save('isFrist', 'false')
        //             const resetAction = NavigationActions.reset({
        //                 index: 0,
        //                 actions: [
        //                     NavigationActions.navigate({routeName: 'GuideView', params: { token: '123456' }})
        //                 ]
        //             })
        //             this.props.navigation.dispatch(resetAction);
        //
        //         }
        //     }).catch((e)=>{
        //         Alert.alert('catch ')
        //         storage.save('isFrist', 'false')
        //         const resetAction = NavigationActions.reset({
        //             index: 0,
        //             actions: [
        //                 NavigationActions.navigate({routeName: 'GuideView', params: { token: '123456' }})
        //             ]
        //         })
        //         this.props.navigation.dispatch(resetAction);
        //
        //     })
        //     Alert.alert('isfirst'+"jin bu qu")
        }, 1000);
    }

    // componentWillUnmount(){
    //     this.timer&&clearTimeout(this.timer);
    // }
    render() {

         let req = require('../app/res/images/1242_2688.png')

       

        const {params} = this.props.navigation.state;
        return (
            <View style={styles.container}>
                    
                
                <Image  style={{top:0,width:sys.dwidth,height:sys.dheight,shadowColor:sys.deepGrayColor,shadowOpacity:0.7,shadowRadius:5,shadowOffset:{width:10,height:10}}} source={req}></Image>
                 
            </View>
        )
    }

}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems:'center',
        backgroundColor:sys.backgroundColor,
        // justifyContent:'center'

    },
    tips: {
        fontSize: 29
    }
})

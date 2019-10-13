<?php
/**
 * 宣传模块
 */
namespace app\controller;

use think\Request;
use think\Db;

class AppShare extends AppBaseController {

    /* 手机宣传页 */
    public function index(Request $request){
        $user_info= $this->isAccessThenLogin();
        $_config=get_config_by_group('base');
        $shareInfo=[];
        $shareInfo['id']=$user_info['id'];
        $shareInfo['money']=$user_info['money'];
        $shareInfo['nickname']=$user_info['nickname'];
        $shareInfo['propaganda_reward']=$_config['propaganda_reward'];    //奖励金币个数
        $shareInfo['share_num']=$_config['share_num'];                    //每天分享有效次数
//        $shareInfo['share_url']=$request->domain()."/share/".$user_info['id'];                    //分享链接
        $shareInfo['share_url']=$request->domain()."/index/xiazai?uid=".$user_info['id'];                    //分享链接
        $this->data['data']=$shareInfo;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }


}
<?php
namespace app\controller;

use app\common\behavior\Base;
use think\Db;
use think\Request;
class AppIndex extends AppBaseController{

    public function index(){

        $resourceLimit=get_config('homepage_resource_num')?(int)get_config('homepage_resource_num'):6;

        //焦点图
        $banner = Db::name('banner')->where(array('status'=>1))->field('url,images_url,target,info')->order('sort asc')->select();
        //推荐视频
        $recom_list =  Db::name('video')->where(array('status'=>1,'reco'=>array('neq','0'),'is_check'=>1))->order('reco desc')->field('id,title,click,good,thumbnail,play_time,gold,update_time')->limit($resourceLimit)->select();
        //最热视频
        $hot_list =  Db::name('video')->where(array('status'=>1,'is_check'=>1))->order('click desc')->field('id,title,click,good,thumbnail,play_time,gold,update_time')->limit($resourceLimit)->select();
        //最新视频
        $new_list =  Db::name('video')->where(array('status'=>1,'is_check'=>1))->order('update_time desc')->field('id,title,click,good,thumbnail,play_time,update_time,gold,update_time')->limit($resourceLimit)->select();


        //获取分类数据
        $list_data =  Db::name('class')->where(array('status'=>1,'home_dispay'=>1))->order('sort asc')->field('id,name,type')->select();
        $notice = Db::name('notice')->where(array('status'=>1,'out_time'=>array('gt',time())))->order('sort asc')->field('id,title,type,url,content')->select();
        $video_list = $image_list = $novel_list = array();
        foreach ($list_data as $k=>$v){
            $where =array(
                'status' =>1,
                'class' =>$v['id'],
                'is_check'=>1
            );
            switch ($v['type'])
            {
                case 1:
                    $db = Db::name('video');
                    $where['pid'] = 0;
                    $field = 'id,title,click,good,thumbnail,play_time,gold,update_time';
                    $v['list'] = $db->where($where)->field($field)->order('update_time desc')->limit($resourceLimit)->select();
                    $video_list[] = $v;
                    break;
                case 2:
                    $db = Db::name('atlas');
                    $field = 'id,title,click,good,cover,update_time,gold,update_time';
                    $v['list'] = $db->where($where)->field($field)->order('update_time desc')->limit($resourceLimit)->select();
                    $image_list[] = $v;
                    break;
                case 3:
                    $db = Db::name('novel');
                    $field = 'id,title,click,good,update_time,gold,update_time,thumbnail';
                    $v['list'] = $db->where($where)->field($field)->order('update_time desc')->limit($resourceLimit)->select();
                    $novel_list[] = $v;
                    break;
            }
        }
        $this->data['data']['demo_time']=$this->request->time();//给模版给以一个当前时间戳的值
        $this->data['data']['page_title']="首页";
        $this->data['data']['navTopTitle']="首页";
        $this->data['data']['notice']=$notice;
        $this->data['data']['banner']=$banner;
        $this->data['data']['recom_list']=$recom_list;
        $this->data['data']['hot_list']=$hot_list;
        $this->data['data']['new_list']=$new_list;
        $this->data['data']['video_list']=$video_list;
        $this->data['data']['image_list']=$image_list;
        $this->data['data']['novel_list']=$novel_list;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }

    public function home(){

        $resourceLimit=get_config('homepage_resource_num')?(int)get_config('homepage_resource_num'):6;
        $resourceLimit=8;
        //焦点图
        $banner = Db::name('banner')->where(array('status'=>1))->field('id,url,images_url,target,info')->order('sort asc')->select();
        //公告信息
        $notice = Db::name('notice')->where(array('status'=>1,'out_time'=>array('gt',time())))->order('sort asc')->field('id,title,type,url,content')->select();
        $video_list = array();
        //推荐视频
        $recom_list =  Db::name('video')->where(array('status'=>1,'reco'=>array('neq','0'),'is_check'=>1))->order('reco desc')->field('id,title,click,good,thumbnail,play_time,gold,update_time')->limit($resourceLimit)->select();
        if($recom_list){
            $item=[
                "id"=>0,
                "name"=>"推荐",
                "type"=>1,
                "list"=>$recom_list,
            ];
            $video_list[] = $item;
        }
        //最热视频
        $hot_list =  Db::name('video')->where(array('status'=>1,'is_check'=>1))->order('click desc')->field('id,title,click,good,thumbnail,play_time,reco,gold,update_time')->limit($resourceLimit)->select();
        if($hot_list){
            $item=[
                "id"=>0,
                "name"=>"最热",
                "type"=>1,
                "list"=>$hot_list,
            ];
            $video_list[] = $item;
        }
        //最新视频
        $new_list =  Db::name('video')->where(array('status'=>1,'is_check'=>1))->order('update_time desc')->field('id,title,click,good,thumbnail,play_time,update_time,gold,update_time')->limit($resourceLimit)->select();
        if($new_list){
            $item=[
                "id"=>0,
                "name"=>"最新",
                "type"=>1,
                "list"=>$new_list,
            ];
            $video_list[] = $item;
        }
        //获取视频的分类数据
        $list_data =  Db::name('class')->where(array('status'=>1,'home_dispay'=>1,'type'=>1))->order('sort asc')->field('id,name,type')->select();
        foreach ($list_data as $k=>$v){
            $where =array(
                'status' =>1,
                'class' =>$v['id'],
                'is_check'=>1
            );
            $db = Db::name('video');
            $where['pid'] = 0;
            $field = 'id,title,click,good,thumbnail,play_time,gold,update_time';
            $list = $db->where($where)->field($field)->order('update_time desc')->limit($resourceLimit)->select();
            if($list){
                $v['list']=$list;
                $video_list[] = $v;
            }
        }
        $this->data['data']['banner']=$banner;
        $this->data['data']['notice']=$notice;
        $this->data['data']['video']=$video_list;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }

    //获取版本
    public function version(Request $request){
        $versionType = $request->param('versionType/s', '');// android ios
        $versionCode= $request->param('versionCode/d', 0);// 1 版本号
        $versionName= $request->param('versionName/s', "");// "1.0.2" 版本名称

        if($versionType!="android"&&$versionType!="ios"){
            $this->ajaxReturn(true,"参数错误")->ajaxOutput();
        }
        $isHasNew=false;//是否有新版本
        $versionCodeNew=2;
        $versionNameNew="2.0.3";

        if($versionCode<$versionCodeNew){
            $isHasNew=true;
        }else if($versionCode==$versionCodeNew){
            //查看版本号
            if(empty($versionName)){
                $isHasNew=true;
            }else{
                if($versionNameNew!=$versionName){
                    $newArr= explode('.', $versionNameNew);
                    $newCode=isset($newArr[0])?($newArr[0]>=100?$newArr[0]:($newArr[0]>=10?$newArr[0].'0':($newArr[0].'00'))):"000";
                    $newCode.=isset($newArr[1])?($newArr[1]>=100?$newArr[1]:($newArr[1]>=10?$newArr[1].'0':($newArr[1].'00'))):"000";
                    $newCode.=isset($newArr[2])?($newArr[2]>=100?$newArr[2]:($newArr[2]>=10?$newArr[2].'0':($newArr[2].'00'))):"000";

                    $oldArr= explode('.', $versionName);
                    $oldCode=isset($oldArr[0])?($oldArr[0]>=100?$oldArr[0]:($oldArr[0]>=10?$oldArr[0].'0':($oldArr[0].'00'))):"000";
                    $oldCode.=isset($oldArr[1])?($oldArr[1]>=100?$oldArr[1]:($oldArr[1]>=10?$oldArr[1].'0':($oldArr[1].'00'))):"000";
                    $oldCode.=isset($oldArr[2])?($oldArr[2]>=100?$oldArr[2]:($oldArr[2]>=10?$oldArr[2].'0':($oldArr[2].'00'))):"000";
                    if($newCode>$oldCode){
                        $isHasNew=true;
                    }
                }
            }
        }
        $version['url']="https://shouji.baidu.com/software/26308845.html";
        $version['versionCode']=$versionCodeNew;
        $version['versionName']=$versionNameNew;
        $version['versionType']=$versionType;
        $version['description']="当前有测试新版本啦，可以下载更新啦！";//更新的描述信息
        $version['isHasNew']=$isHasNew;//是否有更新版本
        $this->data['data']=$version;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }

    //todo 线路检测
    public function checkRoute(Request $request){
        $list=[];
        $list[]="http://spf.zhzhxxyy.com";
        $this->data['data']=$list;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }
    //todo 代理推广


    //咨询页面
    public function zixun(Request $request){
        $this->setSessionMemberId();
        $this->redirect('index/zixun');
    }


}

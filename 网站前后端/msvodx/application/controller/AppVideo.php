<?php
/**
 * 视频控制器类
 */

namespace app\controller;

use think\Db;
use think\Request;


class AppVideo extends AppBaseController{



    //获取视频分类
    public function getClassList(Request $request){
        $class_list = Db::name('class')->where(array('status' => 1, 'type' => 1, 'pid' => 0))->field('id,name')->select();
        if(!$class_list){
            $class_list= array();
        }
        $tag_list = Db::name('tag')->where(array('status' => 1, 'type' => 1))->field('id,name')->select();
        if(!$tag_list){
            $tag_list= array();
        }
        $this->data['data']['class_list']=$class_list;
        $this->data['data']['tag_list']=$tag_list;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }


    //视频列表页
    public function lists(Request $request){
        $keyWord = trim($request->post('key_word/s', '', ''));//关键字搜索
        if(empty($keyWord)){
            $keyWord = trim($request->post('keyWord/s', '', ''));//关键字搜索
        }
        $tag_id = $request->post('tag_id/d', '0');
        $sub_cid = $request->post('sub_cid/d', '0');
        $cid = $request->post('cid/d', '0');
        $offset = $request->post('offset/d', '0');
        $wheres = $request->post('where/s', '');
        $limit = $request->post('pagesize/d', 8);
        if($limit<1){
            $limit=8;
        }
        $page = $request->post('page/d', 1);
        if($page<1){
            $page=1;
        }
        $orderCode = empty($request->post('orderCode')) ? 'lastTime' : $request->post('orderCode');
        switch ($orderCode) {
            case 'lastTime':
                $order = "v.update_time desc";
                break;
            case 'lastTimeASC':
                $order = "v.update_time asc";
                break;
            case 'hot':
                $order = "v.click desc";
                break;
            case 'hotASC':
                $order = "v.click asc";
                break;
            case 'reco':
                $order = "v.reco desc";
                break;
            case 'recoASC':
                $order = "v.reco asc";
                break;
            default:
                $order = "v.update_time desc";
                break;
        }
        $order = empty($order) ? 'v.id desc' : $order;
        $where = "v.status = 1 and v.is_check=1  and v.pid = 0 ";

        if (!empty($wheres)) {
            $where = $wheres;
        }
        if (!empty($tag_id)) {
            $where .= " and FIND_IN_SET( $tag_id, v.tag)";
        }
        if (!empty($cid)) {
            if (empty($sub_cid)) {
                $class_sublist = Db::name('class')->where(array('status' => 1, 'pid' => $cid))->field('id,name')->select();
                if (empty($class_sublist)) {
                    $where .= " and class = $cid";
                } else {
                    $param = array(
                        'db' => 'class',
                        'where' => array('status' => 1, 'pid' => $cid),
                        'field' => 'id',
                    );
                    $sub_array = get_field_values($param);
                    $where .= " and (v.class = $cid or v.class in (" . implode(',', $sub_array) . "))";
                }
            } else {
                $where .= " and v.class = $sub_cid";
            }
        }
        $start = ($limit * ($page-1)) + $offset;


        if (!empty($keyWord)) {
//            $where .= " and v.title like '%$keyWord%' ";
            $where .= " and ( v.title like '%$keyWord%'  or v.actor like '%$keyWord%' )";
//            $where .= "and ( v.title like '%$keyWord%' or m.nickname like '%$keyWord%' ) ";
//            $prefix= config('database.prefix');
//            $count = Db::name("video")->alias("v")->join($prefix.'member m','v.user_id = m.id')->where($where)->count();
//            $list = Db::name("video")->alias("v")->join($prefix.'member m','v.user_id = m.id')->where($where)->order($order)->limit($start, $limit)->field("v.id,v.title,v.click,v.good,v.thumbnail,v.play_time,v.reco,v.update_time,v.gold")->select();
        }
        $count = Db::name("video")->alias("v")->where($where)->count();
        $list = Db::name("video")->alias("v")->where($where)->order($order)->limit($start, $limit)->field("v.id,v.title,v.click,v.good,v.thumbnail,v.play_time,v.reco,v.update_time,v.gold")->select();
        if(!$list){
            $list=[];
        }
        $this->setReturnPagination($count,$limit);
        $this->data['data']['rows']=$list;
        $this->data['data']['page']=$page;
        $this->ajaxReturn(true,"成功")->ajaxOutput();

    }

    //视频播放页
    public function play(Request $request){
        $videoId = $request->param('id/d', 0);
        if (!$videoId){
            $this->ajaxReturn(false,"查看的视频不存在或已被删除!")->ajaxOutput();
        }
        //todo  需要处理登录时候的信息
        $this->setSessionMemberId();
        //获取当前id视频信息，并浏览量++
        $videoInfo= Db::name("video")->where(["id"=>$videoId])->find();
        if (!$videoInfo){
            $this->ajaxReturn(false,"您要查看的视频不存在!")->ajaxOutput();
        }
        Db::name('video')->where("id=$videoId")->setInc('click');

        if($videoInfo['status'] !=1 ){
            $this->ajaxReturn(false,"视频已被下架")->ajaxOutput();
        }
        if($videoInfo['is_check'] !=1 ){
            $this->ajaxReturn(false,"视频未通过审核")->ajaxOutput();
        }

        $videoSet=[];
        //视频集处理逻辑
        if (isset($videoInfo['type']) && $videoInfo['type'] == 1 || $videoInfo['pid'] > 0) {
            $where = ['pid' => $videoInfo['id']];
            if ($videoInfo['pid'] > 0) $where = ['pid' => $videoInfo['pid']];

            $videoSet = Db::name('video')
                ->where($where)
                ->order('sort asc')
                ->select();
            if ($videoSet) {
                $this->data['data']['videoSet']=$videoSet;
                //如果是视频集，那么进入后自动将第一个子视频的信息做为默认视频信息 @dreamer
                if ($videoInfo['type'] == 1) {
                    $videoInfo = $videoSet[0];
                }
            }else{
                $videoSet=[];
            }
        }
        $videoInfo['videoSet']=$videoSet;

        //获取视频作者
        $member_info = Db::name('member')->where(['id' => $videoInfo['user_id']])->field('nickname,headimgurl')->find();
        empty($member_info) ? $videoInfo['member'] = '管理员' : $videoInfo['member'] = $member_info['nickname'];
        empty($member_info) ? $videoInfo['headimgurl'] = '/static/images/user_dafault_headimg.jpg' : $videoInfo['headimgurl'] = $member_info['headimgurl'];

        //获取视频分类
        $class = Db::name('class')->where(['id' => $videoInfo['class']])->field('name,id')->find();
        if (empty($class)) {
            $videoInfo['classname'] = '未分类';
            $videoInfo['classid'] = '0';
        } else {
            $videoInfo['classname'] = $class['name'];
            $videoInfo['classid'] = $class['id'];
        }
        //获取视频标签
        if (empty($videoInfo['tag'])) {
            $videoInfo['tag'] = 0;
        }
        $tag_list = Db::name('tag')->field("id,name")->where("id in({$videoInfo['tag']})")->select();
        $videoInfo['taglist'] = $tag_list;

        //统计该视频的打赏
        $gratuity = Db::name('gratuity_record')->where(['content_type' => 1, 'content_id' => $videoId])->select();
        $count = Db::name('gratuity_record')->where(['content_type' => 1, 'content_id' => $videoId])->field(" count(distinct user_id) as count ")->find();
        $count_price = 0;
        foreach ($gratuity as $k => $v) {
            $json_relust = json_decode($v['gift_info']);
            $count_price = $json_relust->price + $count_price;
        }
        //获取推荐数据
        $params = array(
            'type' => 'video',
            'cid' => $videoInfo['class'],
        );
        $recom_list = get_recom_data($params);
        if(!$recom_list){
            $recom_list=[];
        }
        $videoInfo['recom_list']=$recom_list;

        //播放相关配置信息加载
        $adSetting = get_config_by_group('video');
        $videoInfo['headimgurl']=$this->addPreUrl($videoInfo['headimgurl']);

        $videoInfoNew['id']=$videoInfo['id'];
        $videoInfoNew['click']=$videoInfo['click'];
        $videoInfoNew['gold']=$videoInfo['gold'];
        $videoInfoNew['good']=$videoInfo['good'];
        $videoInfoNew['play_time']=$videoInfo['play_time'];
        $videoInfoNew['thumbnail']=$videoInfo['thumbnail'];
        $videoInfoNew['title']=$videoInfo['title'];
        $videoInfoNew['update_time']=$videoInfo['update_time'];
        $videoInfoNew['reco']=$videoInfo['reco'];
        $videoInfoNew['add_time']=$videoInfo['add_time'];
        $videoInfoNew['classid']=$videoInfo['classid'];
        $videoInfoNew['classname']=$videoInfo['classname'];
        $videoInfoNew['user_id']=$videoInfo['user_id'];
        $videoInfoNew['member']=$videoInfo['member'];
        $videoInfoNew['headimgurl']=$videoInfo['headimgurl'];
        $videoInfoNew['is_check']=$videoInfo['is_check'];
        $videoInfoNew['url']=$videoInfo['url'];

        $videoInfoNew['count_price']=$count_price;
        $videoInfoNew['isGooded']=isGooded('video', $videoInfo['id']);
        $videoInfoNew['isCollected']=isCollected('video', $videoInfo['id']);
        $videoInfoNew['adSetting']=$adSetting;
        $videoInfoNew['recom_list']=$videoInfo['recom_list'];
        $videoInfoNew['taglist']=$videoInfo['taglist'];
        $videoInfoNew['videoSet']=$videoInfo['videoSet'];
        $videoInfoNew['count']=$count['count'];

        //还需要加载一些播放配置  是否试看 试看时间等等


        $this->data['data']=$videoInfoNew;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }
   public function smalllists(Request $request){
        $keyWord = trim($request->post('key_word/s', '', ''));//关键字搜索
        if(empty($keyWord)){
            $keyWord = trim($request->post('keyWord/s', '', ''));//关键字搜索
        }
        $tag_id = $request->post('tag_id/d', '0');
        $tag_id=0;
        $sub_cid = $request->post('sub_cid/d', '0');
        $cid = $request->post('cid/d', '0');
        $offset = $request->post('offset/d', '0');
        $wheres = $request->post('where/s', '');
        $limit = $request->post('pagesize/d', 8);
        if($limit<1){
            $limit=8;
        }
        $page = $request->post('page/d', 1);
        if($page<1){
            $page=1;
        }
        $orderCode = empty($request->post('orderCode')) ? 'lastTime' : $request->post('orderCode');
        switch ($orderCode) {
            case 'lastTime':
                $order = "v.update_time desc";
                break;
            case 'lastTimeASC':
                $order = "v.update_time asc";
                break;
            case 'hot':
                $order = "v.click desc";
                break;
            case 'hotASC':
                $order = "v.click asc";
                break;
            case 'reco':
                $order = "v.reco desc";
                break;
            case 'recoASC':
                $order = "v.reco asc";
                break;
            default:
                $order = "v.update_time desc";
                break;
        }
        $order = empty($order) ? 'v.id desc' : $order;
        $where = "v.status = 1 and v.is_check=1  and v.pid = 0 ";

        if (!empty($wheres)) {
            $where = $wheres;
        }
        if (!empty($tag_id)) {
            $where .= " and FIND_IN_SET( $tag_id, v.tag)";
        }
        if (!empty($cid)) {
            if (empty($sub_cid)) {
                $class_sublist = Db::name('class')->where(array('status' => 1, 'pid' => $cid))->field('id,name')->select();
                if (empty($class_sublist)) {
                    $where .= " and class = $cid";
                } else {
                    $param = array(
                        'db' => 'class',
                        'where' => array('status' => 1, 'pid' => $cid),
                        'field' => 'id',
                    );
                    $sub_array = get_field_values($param);
                    $where .= " and (v.class = $cid or v.class in (" . implode(',', $sub_array) . "))";
                }
            } else {
                $where .= " and v.class = $sub_cid";
            }
        }
        $start = ($limit * ($page-1)) + $offset;


        if (!empty($keyWord)) {
//            $where .= " and v.title like '%$keyWord%' ";
            $where .= " and ( v.title like '%$keyWord%'  or v.actor like '%$keyWord%' )";
//            $where .= "and ( v.title like '%$keyWord%' or m.nickname like '%$keyWord%' ) ";
//            $prefix= config('database.prefix');
//            $count = Db::name("video")->alias("v")->join($prefix.'member m','v.user_id = m.id')->where($where)->count();
//            $list = Db::name("video")->alias("v")->join($prefix.'member m','v.user_id = m.id')->where($where)->order($order)->limit($start, $limit)->field("v.id,v.title,v.click,v.good,v.thumbnail,v.play_time,v.reco,v.update_time,v.gold")->select();
        }
        $count = Db::name("video")->alias("v")->where($where)->count();
        $list = Db::name("video")->alias("v")->where($where)->order($order)->limit($start, $limit)->field("v.id,v.title,v.click,v.good,v.thumbnail,v.play_time,v.reco,v.update_time,v.gold,v.url")->select();
        if(!$list){
            $list=[];
        }
        $this->setReturnPagination($count,$limit);
        $this->data['data']['rows']=$list;
        $this->data['data']['page']=$page;
        $this->ajaxReturn(true,"成功")->ajaxOutput();

    }


}
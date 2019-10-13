<?php
/**
 * 个人中心控制器类
 */

namespace app\controller;

use app\model\Atlas;
use think\Controller;
use think\Db;
use think\Request;

class AppMember extends AppBaseController{

    //会员中心、个人信息
    public function member(Request $request){
        $user_info= $this->getMemberInfoThenLogin();
        $this->data['data']=$user_info;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }

    //会员中心、我的视频
    public function video(Request $request){
        $memberInfo= $this->getMemberInfoThenLogin();
        $offset = $request->post('offset/d', '0');
        $limit = $request->post('pagesize/d', 8);
        if($limit<1){
            $limit=8;
        }
        $page = $request->post('page/d', 1);
        if($page<1){
            $page=1;
        }
        $db = Db::name('video');
        $where = "user_id = ".$memberInfo['id'];
        $order = 'update_time desc';
        $field = 'id,title,click,thumbnail,update_time,play_time,add_time,gold,is_check,status';
        $count = $db->where($where)->count();

        $start = ($limit * ($page-1)) + $offset;
        $list = $db->where($where)->order($order)->limit($start, $limit)->field($field)->select();
        if(!$list){
            $list=[];
        }
        $this->setReturnPagination($count,$limit);
        $this->data['data']['rows']=$list;
        $this->data['data']['page']=$page;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }

    //会员中心、我的收藏.视频
    public function collection_video(Request $request){
        $memberInfo= $this->getMemberInfoThenLogin();
        $limit = $request->param('pagesize/d', 8);
        if($limit<1){
            $limit=8;
        }
        $page = $request->param('page/d', 1);
        if($page<1){
            $page=1;
        }
        $config['page']=$page;

        $start = ($limit * ($page-1));
        $prefix= config('database.prefix');
        $where='v.status=1 and v.is_check=1 and vc.user_id='.$memberInfo['id'];
        $count = Db::name('video_collection')->alias('vc')->join($prefix.'video v','vc.video_id = v.id')->where($where)->count();

        $list = Db::name('video_collection')->alias('vc')->join($prefix.'video v','vc.video_id = v.id')
            ->where($where)
            ->limit($start, $limit)
            ->field('v.id,v.title,v.click,v.good,v.thumbnail,v.play_time,v.reco,v.update_time,v.gold')
            ->select();

        if(!$list){
            $list=[];
        }
        $this->setReturnPagination($count,$limit);
        $this->data['data']['rows']=$list;
        $this->data['data']['page']=$page;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }


    //会员中心、添加视频
    public function addVideo(Request $request){
        $memberInfo=  $this->getMemberInfoThenLogin();
        $data=[];
        $data['tag'] = $request->post('tag/s', '');
        $data['title'] = $request->post('title/s', '');
        $data['gold'] = $request->post('gold/s', '');
        $data['class'] = $request->post('class/d', '');
        $data['url'] = $request->post('url/s', '');
        $data['thumbnail'] = $request->post('thumbnail/s', '');
        $data['play_time'] = $request->post('play_time/s', '');
        $data['actor'] = $request->post('actor/s', '');

        $param['tag'] = 0;
        if (isset($data['tag'])&&!empty($data['tag'])) {
           $arr= explode(",",$data['tag']);
           $tag="";
           for($i=0;$i<sizeof($arr);$i++){
               if(!is_numeric($arr[$i])){
                   $this->ajaxReturn(false,"请选择正确的标签")->ajaxOutput();
               }
               if($i==0){
                   $tag=$arr[$i];
               }else{
                   $tag=$tag.",".$arr[$i];
               }
           }
            $param['tag'] = $tag;
        }
        $param['add_time'] = time();
        $param['update_time'] = time();
        if(empty($data['title'])){
            $this->ajaxReturn(false,"标题不能为空")->ajaxOutput();
        }
        $param['title'] = $data['title'];
        $param['gold'] = $data['gold'];

        if(empty($data['class'])){
            $this->ajaxReturn(false,"请选择视频分类")->ajaxOutput();
        }
        $param['class'] = $data['class'];

        if(empty($data['url'])){
            $this->ajaxReturn(false,"请选择视频地址不为空")->ajaxOutput();
        }
        if(strtolower(substr($data['url'], 0, 4))!="http"){
            $this->ajaxReturn(false,"视频地址格式不正确")->ajaxOutput();
        }
        $param['url'] = $data['url'];
        if(empty($data['thumbnail'])){
            $this->ajaxReturn(false,"缩略图不能为空")->ajaxOutput();
        }
        if(strtolower(substr($data['thumbnail'], 0, 4))!="http"){
            $this->ajaxReturn(false,"缩略图地址格式不正确")->ajaxOutput();
        }
        if(!empty($data['gold'])){
            if(!is_numeric($data['gold'])){
                $this->ajaxReturn(false,"金币格式不对")->ajaxOutput();
            }
            if($data['gold']<0){
                $this->ajaxReturn(false,"金币不小于0")->ajaxOutput();
            }
        }
        $param['thumbnail'] = $data['thumbnail'];
        $param['play_time'] = $data['play_time'];
        $param['actor'] = $data['actor'];
        $param['user_id'] = $memberInfo['id'];
        $param['is_check'] =  (get_config('resource_examine_on')  == 1) ?  0 : 1;
        $addId= db::name('video')->insert($param);
        if($addId>0){
            $this->data['data']=$addId;
            $this->ajaxReturn(true,"添加成功")->ajaxOutput();
        }else{
            $this->ajaxReturn(false,"添加失败")->ajaxOutput();
        }

    }




    //会员中心、充值记录
    public function record_pay(Request $request){
        $memberInfo= $this->getMemberInfoThenLogin();
        $user_id=$memberInfo['id'];
        $limit = $request->param('pagesize/d', 8);
        if($limit<1){
            $limit=8;
        }
        $page = $request->param('page/d', 1);
        if($page<1){
            $page=1;
        }
        $start = ($limit * ($page-1));

        $count = Db::name('order')->where(['user_id'=>$user_id])->count();
        $list = Db::name('order')
            ->where(['user_id'=>$user_id])
            ->order('add_time','desc')
            ->limit($start, $limit)
            ->select();

        if(!$list){
            $list=[];
        }
        foreach ($list as $k=>$v){
            if($v['buy_type'] == 2){
                $list[$k]['buy_vip_info']=json_decode($v['buy_vip_info'],true);
            }
        }
        $this->setReturnPagination($count,$limit);
        $this->data['data']['rows']=$list;
        $this->data['data']['page']=$page;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }


    //会员中心、消费记录 视频
    public function record_video(Request $request) {
        $memberInfo= $this->getMemberInfoThenLogin();
        $limit = $request->param('pagesize/d', 8);
        if($limit<1){
            $limit=8;
        }
        $page = $request->param('page/d', 1);
        if($page<1){
            $page=1;
        }
        $start = ($limit * ($page-1));
        $prefix= config('database.prefix');
        $where='vwl.user_id='.$memberInfo['id'].' and c.type=1 ';
        $count = Db::name('video_watch_log')->alias('vwl')
            ->join($prefix.'video v','vwl.video_id = v.id')
            ->join($prefix.'class c','v.class = c.id')
            ->where($where)->count();

        $list =Db::name('video_watch_log')->alias('vwl')
            ->join($prefix.'video v','vwl.video_id = v.id')
            ->join($prefix.'class c','v.class = c.id')
            ->where($where)
            ->limit($start, $limit)
            ->field('vwl.id,vwl.user_ip,vwl.gold,vwl.user_id,vwl.view_time,v.title,v.id as video_id,c.type,c.name')
            ->select();
        if(!$list){
            $list=[];
        }
        $this->setReturnPagination($count,$limit);
        $this->data['data']['rows']=$list;
        $this->data['data']['page']=$page;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }

}

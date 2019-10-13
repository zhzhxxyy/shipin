<?php
/**
 * Api接口层
 * LastDate:    2017/11/27
 */

namespace app\controller;

use think\Request;
use think\Db;

class AppApi extends AppBaseController{

    /*  登陆接口 */
    public function login(Request $request){
        $userName = $request->post('userName/s', '', '');
        $password = $request->post('password/s', '', '');
        if (empty($userName) || empty($password)){
            $this->ajaxReturn(false,'参数格式不正确:用户名或密码不能为空')->ajaxOutput();
        }

        if ($loginRs = check_member_password($userName, $password)) {
            if (is_array($loginRs) && isset($loginRs['rs']) && isset($loginRs['msg'])) {
                $this->ajaxReturn(false,$loginRs['msg'])->ajaxOutput();
            }
            $user_id = session('member_id');
            $login_reward = get_config('login_reward');

            if ($login_reward) {
                $today = strtotime(date('Y-m-d'));
                $yesterday = $today + (24 * 3600 - 1);
                $where = "user_id =  $user_id and (login_time between $today and $yesterday)";
                $result = Db::name('login_log')->where($where)->find();
                if (empty($result)) {
                    Db::name('member')->where(array('id' => $user_id))->setInc('money', $login_reward);
                    $gold_log_data = array(
                        'user_id' => $user_id,
                        'gold' => $login_reward,
                        'module' => 'login',
                        'explain' => '登录奖励'
                    );
                    write_gold_log($gold_log_data);
                }
            }
            $log_data = array(
                'user_id' => $user_id,
                'login_time' => time(),
                'ip' => $request->ip(),
            );
            Db::name('login_log')->insert($log_data);
            Db::name('member')->update(['last_time' => time(), 'last_ip' => $request->ip(), 'id' => $user_id, 'login_count' => '+1']);
            $member = model('Member')->get($user_id);
            $member->last_time = time();
            $member->last_ip = $request->ip();
            $member->login_count++;
            $member->save();

            $_REQUEST['token']=session('access_token');
            $memberInfo= $this->getMemberInfoThenLogin();
            $this->data['data']=$memberInfo;
            $this->ajaxReturn(true,'登陆成功')->ajaxOutput();
        }
        $this->ajaxReturn(false, '数据验证失败:用户名或密码不正确')->ajaxOutput();
    }

    /* 注册 */
    public function register(Request $request){
        $data['username'] = $request->post('username/s', '', '');
        $data['password'] = $request->post('password/s', '', '');
        $userdata['nickname'] = $data['nickname'] = $request->post('nickname/s', '', '');
        $data['verifyCode'] = trim($request->post('verifyCode/s', '', ''));         //邮箱or手机的验证码
        $data['confirm_password'] = $request->post('confirm_password/s', '', '');

        if (empty($data['username']) || empty($data['password'])){
            $this->ajaxReturn(false,'参数格式不正确:用户名或密码不能为空')->ajaxOutput();
        }
        if ($data['password'] != $data['confirm_password']){
            $this->ajaxReturn(false,'数据验证失败:两次密码不一致')->ajaxOutput();
        }
        $userdata['username'] = $data['username'];
        $result = $this->validate($data, 'Member.username_register');
        if ($result !== true){
            $this->ajaxReturn(false,'数据验证失败:' . $result)->ajaxOutput();
        }
        //添加账号处理
        $userdata['headimgurl'] = '/static/images/user_dafault_headimg.jpg';
        $userdata['password'] = encode_member_password($data['password']);
        $userdata['last_time'] = $userdata['add_time'] = time();
        $userdata['pid'] = empty(session("cur_agent_id")) ? 0 : session("cur_agent_id");
        if (db::name('member')->insert($userdata)) {
            $member_id = Db::name('member')->getLastInsID();
            $register_reward = get_config('register_reward');
            if ($register_reward) {
                $gold_log_data = array(
                    'user_id' => $member_id,
                    'gold' => $register_reward,
                    'module' => 'register',
                    'explain' => '注册奖励'
                );
                write_gold_log($gold_log_data);
                Db::name('member')->where(array('id' => $member_id))->setInc('money', $register_reward);
            }
            check_member_password($userdata['username'], $data['password']);
            $this->data['data']=$member_id;
            $this->ajaxReturn(true,'注册成功' )->ajaxOutput();
        } else {
            $this->ajaxReturn(false,'数据验证失败' )->ajaxOutput();
        }
    }

    /*根据uuid登录*/
    public function loginByUUID(Request $request){
        $uuid = $request->param('uuid/s', '', '');
        if(empty($uuid)||strlen($uuid)<10||strlen($uuid)>64){
            $this->ajaxReturn(false,'数据验证失败' )->ajaxOutput();
        }
        $memberInfo= Db::name('member')->field("id,username,password")->where(array('uuid' => $uuid))->find();
        if(!$memberInfo){
            //需要添加
            $data['password']=$this->getRandUserNumber();
            $isTrue=false;
            for($j=0;$j<5;$j++){
                $data['nickname']=$this->getRandUserNumber();
                $data['username']=$data['nickname'];
                $result = $this->validate($data, 'Member.username_register');
                if ($result == true){
                    $isTrue=true;
                    break;
                }
            }
            if(!$isTrue){
                $this->ajaxReturn(false,'数据验证失败' )->ajaxOutput();
            }
            //添加账号处理
            $userdata['username'] = $data['username'];
            $userdata['nickname'] = $data['nickname'];
            $userdata['headimgurl'] = '/static/images/user_dafault_headimg.jpg';
            $userdata['password'] = encode_member_password($data['password']);
            $userdata['last_time'] = $userdata['add_time'] = time();
            $userdata['pid'] = empty(session("cur_agent_id")) ? 0 : session("cur_agent_id");
            $userdata['uuid'] =$uuid;

            $result = $this->validate($data, 'Member.username_register');
            if ($result !== true){
                $this->ajaxReturn(false,'数据验证失败:' . $result)->ajaxOutput();
            }
            if (db::name('member')->insert($userdata)) {
                $member_id = Db::name('member')->getLastInsID();
                $register_reward = get_config('register_reward');
                if ($register_reward) {
                    $gold_log_data = array(
                        'user_id' => $member_id,
                        'gold' => $register_reward,
                        'module' => 'register',
                        'explain' => '注册奖励'
                    );
                    write_gold_log($gold_log_data);
                    Db::name('member')->where(array('id' => $member_id))->setInc('money', $register_reward);
                }
                $memberInfo=[];
                $memberInfo['id']=$member_id;
                $memberInfo['username']= $userdata['username'];
                $memberInfo['password']=$userdata['password'];
            } else {
                $this->ajaxReturn(false,'数据验证失败' )->ajaxOutput();
            }
        }
        if(!$memberInfo){
            $this->ajaxReturn(false,'数据验证失败' )->ajaxOutput();
        }
        $access_token = get_token($memberInfo);
        Db::name('member')->where(array('id' => $memberInfo['id']))->update(array('access_token' => $access_token));
        session('access_token', $access_token);
        session('member_id', $memberInfo['id']);
        session('member_info', $memberInfo);
        $user_id =$memberInfo['id'];
        $login_reward = get_config('login_reward');
        if ($login_reward) {
            $today = strtotime(date('Y-m-d'));
            $yesterday = $today + (24 * 3600 - 1);
            $where = "user_id =  $user_id and (login_time between $today and $yesterday)";
            $result = Db::name('login_log')->where($where)->find();
            if (empty($result)) {
                Db::name('member')->where(array('id' => $user_id))->setInc('money', $login_reward);
                $gold_log_data = array(
                    'user_id' => $user_id,
                    'gold' => $login_reward,
                    'module' => 'login',
                    'explain' => '登录奖励'
                );
                write_gold_log($gold_log_data);
            }
        }
        $log_data = array(
            'user_id' => $user_id,
            'login_time' => time(),
            'ip' => $request->ip(),
        );
        Db::name('login_log')->insert($log_data);
        Db::name('member')->update(['last_time' => time(), 'last_ip' => $request->ip(), 'id' => $user_id, 'login_count' => '+1']);
        $member = model('Member')->get($user_id);
        $member->last_time = time();
        $member->last_ip = $request->ip();
        $member->login_count++;
        $member->save();

        $_REQUEST['token']=session('access_token');
        $memberInfo= $this->getMemberInfoThenLogin();
        $this->data['data']=$memberInfo;
        $this->ajaxReturn(true,'游客登陆成功')->ajaxOutput();
    }



    /* 退出登陆接口 */
    public function logout(Request $request)
    {
        //todo 具体为完善
        if (member_logout()) {

        }
        $this->ajaxReturn(true,'退出成功')->ajaxOutput();
    }

    //获取列表
    public function moreData(Request $request){
        $tag_id = $request->post('tag_id/d', '0');
        $sub_cid = $request->post('sub_cid/d', '0');
        $cid = $request->post('cid/d', '0');
        $offset = $request->post('offset/d', '0');
        $type = $request->post('type/s', 'video');
        $wheres = $request->post('where/s', '');
        $limit = $request->post('pagesize/d', 8);
        if($limit<1){
            $limit=8;
        }
        $page = $request->post('page/d', 1);
        if($page<1){
            $page=1;
        }
        $field = array(
            'atlas' => 'id,title,cover,click,good,update_time',
            'novel' => 'id,title,click,good,gold,thumbnail,tag,short_info,update_time',
            'video' => 'id,title,click,good,thumbnail,play_time,reco,update_time,gold',
            'image' => 'id,url,add_time,atlas_id',
        );
        $orderCode = empty($request->post('orderCode')) ? 'lastTime' : $request->post('orderCode');
        switch ($orderCode) {
            case 'lastTime':
                $order = ($type == 'image') ? "add_time,id desc" : "update_time desc";
                break;
            case 'lastTimeASC':
                $order = "update_time asc";
                break;
            case 'hot':
                $order = "click desc";
                break;
            case 'hotASC':
                $order = "click asc";
                break;
            case 'reco':
                $order = "reco desc";
                break;
            case 'recoASC':
                $order = "reco asc";
                break;
            default:
                $order = "update_time desc";
                break;
        }
        $order = empty($order) ? 'id desc' : $order;
        if ($type == 'video') {
            $where = "status = 1 and is_check=1  and pid = 0 ";
        } else {
            $where = 'status = 1 and is_check=1 ';
        }
        if (!empty($wheres)) {
            $where = $wheres;
        }
        if (!empty($tag_id)) {
            $where .= " and FIND_IN_SET( $tag_id, tag)";
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
                    $where .= " and (class = $cid or class in (" . implode(',', $sub_array) . "))";
                }
            } else {
                $where .= " and class = $sub_cid";
            }
        }
        $count = Db::name($type)->where($where)->count();
        $start = ($limit * ($page-1)) + $offset;
        $list = Db::name($type)->where($where)->order($order)->limit($start, $limit)->field($field[$type])->select();
        if(!$list){
            $list=[];
        }
        $this->setReturnPagination($count,$limit);
        $this->data['data']['rows']=$list;
        $this->data['data']['page']=$page;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }

    /**
     * 打赏礼物api
     * itemid 礼物id ,itemprice 礼物价格，projectid 打赏目标id ,type 类型ID
     **/
    public function reward(Request $request)
    {
        $this->setSessionMemberId();
        if (session('member_id') <= 0||session('member_id')==""){
            $this->ajaxReturn(false,$this->MSG_LOGIN_FIRST)->ajaxOutput();
        }
        $user_id = session('member_id');

        //判断用户参数是否合法
        $itemid = $request->post('itemid/d');
        $itemprice = $request->post('itemprice/d');
        $projectid = $request->post('projectid/d');
        $type = $request->post('type/d');
        if ($itemid <= 0 || $projectid <= 0 || $type <= 0) {
            $this->ajaxReturn(false,"缺少请求参数。")->ajaxOutput();
        }
        //判断礼物是否存在
        $gift_info = Db::name('gift')->where(['id' => $itemid, 'price' => $itemprice, 'status' => 1])->field('id,name,images,price,info')->find();
        if (empty($gift_info)){
            $this->ajaxReturn(false,"打赏的礼物不{$itemid}存在！{$itemprice}")->ajaxOutput();
        }
        //判断用户金币是否足够
        $user_info = model('member')->get($user_id);
        if (intval($user_info->money) < intval($gift_info['price'])){
            $this->ajaxReturn(false,"你的金币不足")->ajaxOutput();
        }
        //打赏记录入库
        $gift_data = [
            'user_id' => $user_id,
            'gratuity_time' => time(),
            'content_type' => $type,
            'content_id' => $projectid,
            'gift_info' => json_encode($gift_info),
            'price' => intval($gift_info['price'])
        ];
        $result = Db::name('gratuity_record')->insert($gift_data);
        if ($result) {
            $user_info->money = $user_info->money - $gift_info['price'];
            $user_info->save();
        }
        //统计该视频的打赏
        $gratuity = Db::name('gratuity_record')->where(['content_type' => $type, 'content_id' => $projectid])->select();
        $count = Db::name('gratuity_record')->where(['content_type' => $type, 'content_id' => $projectid])->field(" count(distinct user_id) as count ")->find();
        $gold_log_data = array(
            'user_id' => $user_id,
            'gold' => '-' . intval($gift_info['price']),
            'module' => 'reward',
            'explain' => '打赏消费'
        );
        write_gold_log($gold_log_data);
        $count_price = 0;
        foreach ($gratuity as $k => $v) {
            $json_relust = json_decode($v['gift_info']);
            $count_price = $json_relust->price + $count_price;
        }
        $returndata = ['countprice' => $count_price, 'counts' => $count['count']];

        $this->data['data']=$returndata;
        $this->ajaxReturn(true,"谢谢你,打赏成功")->ajaxOutput();
    }



    /**
     * 获取礼物列表接口
     * @param orders  排序的方式
     * @param  where 查询的条件
     * @param  field 查询的字段
     */
    public function getgift(Request $request)
    {
        $data = $request->post();
        $where = empty($data['where']) ? "status = 1" : $data['where'];
        $orders = empty($data['orders']) ? 'sort DESC' : $data['orders'];
        $field = empty($data['field']) ? 'id,name,images,price,info' : $data['field'];
        $list = Db::name('gift')->where($where)->order($orders)->field($field)->select();
        if(!$list){
            $list=[];
        }
        $this->setReturnPagination(sizeof($list),8);
        $this->data['data']['rows']=$list;
        $this->data['data']['page']=1;
        $this->ajaxReturn(true,"成功")->ajaxOutput();
    }

    /**
     * 图片资讯视频收藏接口
     * @param type 1为视频，2为图片，3资讯
     * @param id    收藏id
     */
    public function colletion(Request $request)
    {
        $this->setSessionMemberId();
        if (session('member_id') <= 0||session('member_id')==""){
            $this->ajaxReturn(false,$this->MSG_LOGIN_FIRST)->ajaxOutput();
        }
        $member_id = session('member_id');
        $type = $request->post('resourceType/d', 0);
        $id = $request->post('resourceId/d', 0);
        //验证登陆
        if (intval($member_id) <= 0){
            $this->ajaxReturn(false,$this->MSG_LOGIN_FIRST)->ajaxOutput();
        }

        if (empty($type) || intval($type) <= 0 || empty($id) || intval($id) <= 0) {

            $this->ajaxReturn(false,"补足参数后重试")->ajaxOutput();
        }
        switch ($type) {
            case 1:
                $db = Db::name('video');
                $collect_db = Db::name('video_collection');
                //判断存如视频id是否存在
                $result_video = $db->where(['status' => 1, 'id' => $id])->find();
                if (empty($result_video)) {
                    $this->ajaxReturn(false,"当前视频不存在")->ajaxOutput();
                }
                //判断视频是否已经收藏
                $result_collect = $collect_db->where(['user_id' => $member_id, 'video_id' => $id])->find();
                if ($result_collect) {
                    $this->ajaxReturn(false,"该视频已经收藏过了")->ajaxOutput();
                }
                //插入用户收藏日志
                $collect_data = [
                    'user_id' => $member_id,
                    'video_id' => $id,
                    'collection_time' => $request->time()
                ];
                $insert_result = $collect_db->data($collect_data)->insert();
                if ($insert_result) {
                    $this->ajaxReturn(true,"收藏成功")->ajaxOutput();
                } else {
                    $this->ajaxReturn(false,"收藏失败")->ajaxOutput();
                }
                break;
            case 2:
                $db = Db::name('image');
                $collect_db = Db::name('image_collection');
                //判断存如视频id是否存在
                $result_image = $db->where(['status' => 1, 'id' => $id])->find();
                if (empty($result_image)) {
                    $this->ajaxReturn(false,"当前图片不存在")->ajaxOutput();
                }
                //判断视频是否已经收藏
                $result_collect = $collect_db->where(['user_id' => $member_id, 'image_id' => $id])->find();
                if ($result_collect) {

                    $this->ajaxReturn(false,"该图片已经收藏过了")->ajaxOutput();

                }

                //插入用户收藏日志
                $collect_data = [
                    'user_id' => $member_id,
                    'image_id' => $id,
                    'collection_time' => $request->time()
                ];
                $insert_result = $collect_db->data($collect_data)->insert();
                if ($insert_result) {
                    $this->ajaxReturn(false,"收藏成功")->ajaxOutput();
                } else {
                    $this->ajaxReturn(false,"收藏失败")->ajaxOutput();

                }
                break;
            case 3:
                $db = Db::name('novel');
                $collect_db = Db::name('novel_collection');
                //判断存如视频id是否存在
                $result_novel = $db->where(['status' => 1, 'id' => $id])->find();
                if (empty($result_novel)) {
                    $this->ajaxReturn(false,"当前资讯不存在")->ajaxOutput();

                }
                //判断视频是否已经收藏
                $result_collect = $collect_db->where(['user_id' => $member_id, 'novel_id' => $id])->find();
                if ($result_collect) {
                    $this->ajaxReturn(false,"该资讯已经收藏过了")->ajaxOutput();

                }
                //插入用户收藏日志
                $collect_data = [
                    'user_id' => $member_id,
                    'novel_id' => $id,
                    'collection_time' => $request->time()
                ];
                $insert_result = $collect_db->data($collect_data)->insert();
                if ($insert_result) {
                    $this->ajaxReturn(true,"收藏成功")->ajaxOutput();

                } else {
                    $this->ajaxReturn(false,"收藏失败")->ajaxOutput();
                }
                break;
            default :
                $this->ajaxReturn(false,"请求参数错误")->ajaxOutput();
                break;
        }


    }



    /**
     * 点赞接口
     * @param resourceType  点赞资料对象类型：  image / novel / video
     * @param id            点赞对象id
     */
    public function giveGood(Request $request){
        //验证登陆
        $this->setSessionMemberId();
        if (session('member_id') <= 0||session('member_id')==""){
            $this->ajaxReturn(false,$this->MSG_LOGIN_FIRST)->ajaxOutput();
        }
//        $resourceType = $request->post('resourceType/s', '');
        $resourceType="";
        $type = $request->post('resourceType/d', 0);
        if($type==1){
            $resourceType="video";
        }else if($type==2){
            $resourceType="image";
        }else if($type==3){
            $resourceType="novel";
        }else if($type==4){
            $resourceType="atlas";
        }
        $resourceId = $request->post('resourceId/d', 0);
        $allowType = ['atlas', 'novel', 'video'];
        $resourceTable = [
            'atlas' => 'atlas',
            'novel' => 'novel',
            'video' => 'video'
        ];

        if (empty($resourceType)){
            $this->ajaxReturn(false,"缺少请求参数:resourceType")->ajaxOutput();
        }
        if ($resourceId <= 0){
            $this->ajaxReturn(false,"参数格式不正确:resourceId")->ajaxOutput();
        }
        if (!in_array($resourceType, $allowType)){
            $this->ajaxReturn(false,'参数格式不正确:resourceType只能是' . implode(',', $allowType) . '中的一个')->ajaxOutput();
        }

        $goodHistory = Db::name("{$resourceType}_good_log")->where(["{$resourceType}_id" => $resourceId, 'user_id' => session('member_id')])->find();
        if ($goodHistory){
            $this->ajaxReturn(false,"已点过赞")->ajaxOutput();
        }


        $resource = model($resourceTable[$resourceType]);
        $dataObj = $resource::get($resourceId);
        if (!$dataObj){
            $this->ajaxReturn(false,"数据验证失败:资源不存在.")->ajaxOutput();
        }
        $dataObj->good += 1;
        $dataObj->save();


        //写入点赞日志表
        $goodLogData = [
            'add_time' => time(),
            $resourceTable[$resourceType] . '_id' => $resourceId,
            'user_id' => session('member_id'),
        ];

        $iRs = Db::name("{$resourceType}_good_log")->data($goodLogData)->insert();
        $this->data['data']=['resourceId' => $dataObj->id, 'good' => $dataObj->good];
        $this->ajaxReturn(true,"点赞成功")->ajaxOutput();

    }


    /**
     * 签到接口
     */
    function sign(Request $request){
        $this->setSessionMemberId();
        if (session('member_id') <= 0||session('member_id')==""){
            $this->ajaxReturn(false,$this->MSG_LOGIN_FIRST)->ajaxOutput();
        }
        $user_id = session('member_id');
        if (isSign()){
            $this->ajaxReturn(true,"您今天已经签过了，请明天再签")->ajaxOutput();
        }
        //判断当前用户是否已经签到
        $sign_reward = get_config('sign_reward');
        if (!empty($sign_reward)) {
            Db::name('member')->where(array('id' => $user_id))->setInc('money', $sign_reward);
            $gold_log_data = array(
                'user_id' => $user_id,
                'gold' => $sign_reward,
                'module' => 'sign',
                'explain' => '签到奖励'
            );
            write_gold_log($gold_log_data);
        } else {
            $sign_reward = 0;
        }
        $signData = array(
            'user_id' => $user_id,
            'sign_time' => time(),
        );
        Db::name('sign')->insert($signData);
        $this->ajaxReturn(true,"签到成功")->ajaxOutput();
    }

    
    //是否签到成功
    function isSign(Request $request){
        $this->setSessionMemberId();
        if (session('member_id') <= 0||session('member_id')==""){
            $this->ajaxReturn(false,$this->MSG_LOGIN_FIRST)->ajaxOutput();
        }
        $this->data['data']=isSign()?"true":"false";
        $this->ajaxReturn(true,"获取成功")->ajaxOutput();
    }


    /**
     * 评论接口
     * @param resourceType  评论资料对象类型： 2/3/1  image / novel / video  图片/资讯/视频
     * @param resourceId       资源对象id
     * @param content           内容
     * @param to_user            被评论者（可为空）
     */
    function comment(Request $request)
    {
        $memberInfo= $this->getMemberInfoThenLogin();
        session('member_id',$memberInfo['id']);
        $wheres = "name in ('comment_on','comment_examine_on')";
        $config = Db::name('admin_config')->where($wheres)->column('name,value');
        if ($config['comment_on'] != 1){
            $this->ajaxReturn(false,'当前暂未支持评论')->ajaxOutput();
        }
        if (session('member_id') <= 0||session('member_id')==""){
            $this->ajaxReturn(false,$this->MSG_LOGIN_FIRST)->ajaxOutput();
        }
        $resourceType = $request->post('resourceType/d', '1');
        $resourceId = $request->post('resourceId/d', 0);
        $content = $request->post('content/s', '');
        $content = htmlspecialchars(trim($content), ENT_QUOTES);
        $to_user = $request->post('to_user/s', '');
        $insertData = [
            'add_time' => time(),
            'last_time' => time(),
            'resources_type' => $resourceType,
            'resources_id' => $resourceId,
            'content' => $content,
            'send_user' => session('member_id'),
            'to_user' => $to_user,
        ];
        if (empty($config['comment_examine_on'])) {
            $data['comment_examine_on'] = 0;
            $insertData['status'] = 1;
            $message = '评论成功';
        } else {
            $data['comment_examine_on'] = 1;
            $insertData['status'] = 0;
            $message = '评论成功,待审核后才显示';
        }
        $insertData['status'] = empty($config['comment_examine_on']) ? 1 : 0;
        $insert_result = Db::name("comment")->data($insertData)->insert();
        if ($insert_result) {
            $insertData['id']=1;
            $insertData['headimgurl']=$memberInfo['headimgurl'];
            $insertData['nickname']=$memberInfo['nickname'];
            $insertData['username']=$memberInfo['username'];
            $this->data['data']=$insertData;
            $this->ajaxReturn(true,$message)->ajaxOutput();
        } else {
            $this->ajaxReturn(false,'评论失败')->ajaxOutput();

        }
    }


    /**
     * 评论列表接口
     * @param resourceType  评论资料对象类型： 2/3/1  image / novel / video  图片/资讯/视频
     * @param resourceId       资源对象id
     * @param limit       请求的数量
     * @param page      当前的页数，默认为0
     */
    function getCommentList(Request $request)
    {
        $resourceType = $request->param('resourceType/d', '1');
        $resourceId = $request->param('resourceId/d', 25);
        $limit = $request->param('pagesize/d', 8);
        $page = $request->param('page/d', 1);
        if($page<1){
            $page=1;
        }
        --$page;
        $where = "status = 1 and  resources_type = $resourceType and resources_id = $resourceId";
        $order = 'last_time desc';
        $start = $limit * $page;
        $field = 'id,send_user,content,last_time';
        $list = Db::view('comment', $field)
            ->view('member', 'username,headimgurl,nickname', 'comment.send_user=member.id', 'LEFT')
            ->where('comment.' . $where)
            ->limit($start, $limit)
            ->order($order)
            ->select();
        //$list = Db::name("comment")->where($where)->order($order)->limit($start,$limit)->field($field)->select();
        $count = Db::name("comment")->where($where)->count();

        if(!$list){
            $list=[];
        }
        $this->setReturnPagination($count,$limit);
        $this->data['data']['rows']=$list;
        $this->data['data']['page']=++$page;
        $this->ajaxReturn(true,"成功")->ajaxOutput();

    }



    /**
     * 视频播放接口
     * @author Dreamer
     * @param Request $request
     */
    function payVideo(Request $request){
        $videoId = $request->post('id/d', 0);
        if (!$videoId){
            $this->ajaxReturn(false,'参数格式不正确:id')->ajaxOutput();
        }
        $videoInfo = Db::name('video')->where(['id' => $videoId])->find();
        if (!$videoInfo){
            $this->ajaxReturn(false,'视频资源不存在')->ajaxOutput();
        }
        $this->setSessionMemberId();
        //视频密钥
        $videoInfo['url'] .= "?sign=" . create_yzm_play_sign();
        $surePlay = $request->param('surePlay/d', 0);
        //播放剩余次/秒数
        $remainderTrySee = get_remainder_try_see();
        $freePlayInfo = [];
        if (is_array($remainderTrySee)) {
            $freePlayInfo['freeType'] = 2;    //按秒数
            $freePlayInfo['freeNum'] = intval($remainderTrySee['look_at_num']);
        }
        if (is_numeric($remainderTrySee)) {
            $freePlayInfo['freeType'] = 1;    //按部
            $freePlayInfo['freeNum'] = intval($remainderTrySee);
        }

        //视频播放权限验证
        $authRs = check_video_auth($videoInfo);

        // 视频需要付费,请登陆后观看 playState 0
        // 正常播放 playState 1
        // 可扣费播放 playState 2
        // 金币不足扣费,请充值后观看 playState 3
        //result=>1:正常观看  2:需要扣金币观看，且金币够扣  3:需扣金币观看，且金币不够扣  4:视频收费，但未登陆
        $returnData=[];
        $returnData['playState']=0;
        $returnData['playStateMsg']="未知播放";
        switch ($authRs['result']) {
            case 1:
                $returnData['playState']=1;
                $returnData['playStateMsg']="正常播放";
                break;
            case 2:
                $returnData['playState']=2;
                $returnData['playStateMsg']="可扣费播放";
                break;
            case 3:
                $returnData['playState']=3;
                $returnData['playStateMsg']="金币不足扣费,请充值后观看";
                break;
            case 4:
                $returnData['playState']=4;
                $returnData['playStateMsg']="视频需要付费,请登陆后观看";
                break;
        }
        $returnData['freeType']=isset($freePlayInfo['freeType'])?$freePlayInfo['freeType']:0;
        $returnData['freeNum']=isset($freePlayInfo['freeNum'])?$freePlayInfo['freeNum']:0;
        $returnData['url']= $videoInfo['url'];
        $returnData['id']= $videoInfo['id'];

        $returnData['surePlay']= $surePlay?true:false;
        $returnData['isTrySee']= $request->post('isTrySee') == 'true'?true:false;

        if (!$surePlay) {
            //返回播放前处理数据
            $returnData['surePlay']=false;
            $returnData['isTrySee']=false;
            if($returnData['playState']!=1){
                $returnData['url']=null;
            }
            $this->data['data']=$returnData;
            $this->ajaxReturn(true,"成功")->ajaxOutput();
        }

        $returnData['surePlay']=true;
        if ($request->post('isTrySee') == 'true' ) {
            //试看
            if($returnData['freeNum']<=0&& $returnData['playState']!=1){
                $returnData['surePlay']=false;
                $returnData['url']=null;
                $this->data['data']=$returnData;
                $this->ajaxReturn(true,'成功')->ajaxOutput();
            }
            $successFlag = insert_watch_log('video', $videoInfo['id'], 0, true, $videoInfo['user_id']);
        } else {
            //扣费观看
            if($returnData['playState']==0||$returnData['playState']==3||$returnData['playState']==4){
                $returnData['surePlay']=false;
                $returnData['url']=null;
                $this->data['data']=$returnData;
                $this->ajaxReturn(true,'成功')->ajaxOutput();
            }
            $successFlag = insert_watch_log('video', $videoInfo['id'], $videoInfo['gold'], false, $videoInfo['user_id']);
        }
        if ($successFlag) {
            $this->data['data']=$returnData;
            $this->ajaxReturn(true,'成功')->ajaxOutput();
        } else {
            $this->ajaxReturn(false,$request->post('isTrySee') == 'true'?"今天试看次数为0，请推广或充值":'余额不足')->ajaxOutput();
        }
    }


    /**
     * 删除接口
     * @param table     数据库
     * @param pk        对象主键，可以是单值或数组 如 id = 1 or id = [1,2] or id = array(1,2,3)
     */
    public function del(Request $request){
        die;
        $this->setSessionMemberId();
        if (session('member_id') <= 0||session('member_id')==""){
            $this->ajaxReturn(false,$this->MSG_LOGIN_FIRST)->ajaxOutput();
        }

        $id = $request->post('id/d', 0);
        $type = $request->post('type/d', 0);
        $table = $request->post('table/s', '');
        $arr = ['images', 'atlas', 'video', 'novel'];
        if (!in_array($table, $arr)) {
            die(json_encode(['resultCode' => 5002, 'error' => '非法请求']));
        }
        if (empty($id)){
            $this->ajaxReturn(false,'缺少请求参数:id')->ajaxOutput();
        }
        if (empty($table)){
            $this->ajaxReturn(false,'缺少请求参数:table')->ajaxOutput();
        }
        // 获取主键
        $pk = Db::name($table)->getPk();
        $map = [];
        $map[$pk] = ['in', $id];
        if ($type == 0) {
            $map['user_id'] = session('member_id');
        }
        $res = Db::name($table)->where($map)->delete();

        /* 已在改良方法中实现
        //删除对应的收藏表  2018/01/16 $dreamer
        $needSycDel=[
            'video'=>['table'=>'video_collection','key'=>'video_id'],
            'images'=>['table'=>'image_collection','key'=>'image_id'],
            'video'=>['table'=>'novel_collection','key'=>'novel_id'],
        ];

        if(in_array($table,array_keys($needSycDel))){
            Db::name($needSycDel[$table]['table'])->where($needSycDel[$table]['key']."_id={$id}")->delete();
        }
        */

        if ($res === false) {
            $this->ajaxReturn(false,'数据验证失败')->ajaxOutput();
        }
        $this->ajaxReturn(false,'删除成功')->ajaxOutput();

    }


    /**
     * 修改用户信息
     * @param key     要修改的字段
     * @param value    要修改的值
     */
    public function editUserInfo(Request $request){
        $this->setSessionMemberId();
        if (session('member_id') <= 0||session('member_id')==""){
            $this->ajaxReturn(false,$this->MSG_LOGIN_FIRST)->ajaxOutput();
        }

        $key = $request->post('key');
        $value = $request->post('value');
        if (empty($key)){
            $this->ajaxReturn(false,"缺少请求参数:key")->ajaxOutput();
        }
        if (empty($value)){
            $this->ajaxReturn(false,"缺少请求参数:value")->ajaxOutput();
        }
        $allow = ['sex', 'nickname', 'headimgurl', 'email', 'tel'];   //$dreamer add 'email' 171221
        if (!in_array($key, $allow)){
            $this->ajaxReturn(false,"非法请求,参数有误。")->ajaxOutput();
        }
        $member_info = db::name('member')->where(array('id' => session('member_id')))->find();
        if ($member_info[$key] == $value){
            $this->ajaxReturn(false,"数据未修改")->ajaxOutput();
        }
        $member_info[$key] = $value;
        //.当修改的是用户昵称的时候，判断当前昵称是否已经存在
        if ($key == 'nickname') {
            $isset = db::name('member')->where(array('nickname' => $value))->find();
            if ($isset){ $this->ajaxReturn(false,"该用户昵称已经被占用")->ajaxOutput();}
        }
        db::name('member')->where(array('id' => session('member_id')))->update(array($key => $value));
        $this->ajaxReturn(true,"修改成功")->ajaxOutput();
    }

    /**
     * 代理商申请接口
     */
    public function agentApply()
    {
        $this->setSessionMemberId();
        if (session('member_id') <= 0||session('member_id')==""){
            $this->ajaxReturn(false,$this->MSG_LOGIN_FIRST)->ajaxOutput();
        }

        $user_info = db::name('member')->where(array('id' => session('member_id')))->find();
        if ($user_info['is_agent'] == 1){
            $this->ajaxReturn(false,"您已经是代理商了")->ajaxOutput();
        }
        $apply = db::name('agent_apply')->where(array('user_id' => session('member_id')))->find();
        if (!empty($apply)){
            $this->ajaxReturn(false,"您已经申请过了，不能重复申请！")->ajaxOutput();
        }
        $data = array(
            'user_id' => session('member_id'),
            'apply_time' => time(),
            'last_time' => time(),
        );
        db::name('agent_apply')->insert($data);
        $this->ajaxReturn(true,"申请成功！")->ajaxOutput();
    }



    /**
     * 返回想要的分类
     * @param $param array
     * @param card_number  卡号
     * @param verifyCode 验证码
     */
    function get_card_password_info(Request $request){
        $this->isAccessThenLogin();
        $card_number = $request->post('card_number/s');
        if (empty($card_number)){
            $this->ajaxReturn(false,"数据验证失败:卡号不能为空！")->ajaxOutput();
        }
        $field = 'id,card_number,card_type,out_time,status,price,gold,vip_time';
        $where['card_number'] = $card_number;
        $info = Db::name('card_password')->where($where)->field($field)->find();
        if (empty($info)){
            $this->ajaxReturn(false,"数据验证失败:该卡不存在！")->ajaxOutput();
        }
        $info['out_times'] = ($info['out_time'] < time()) ? '已过期' : date('Y-m-d H:i', $info['out_time']);

        $this->data['data']=$info;
        $this->ajaxReturn(true,"查询成功！")->ajaxOutput();
    }

    /**
     * 使用卡密
     * @param $param array
     * @param card_number  卡号
     * @param id 卡id
     */
    function use_card_password(Request $request){
        $user_info= $this->isAccessThenLogin();
        $memberId =$user_info['id'];

        $card_number = $request->post('card_number/s');
        $id = $request->post('id/d', '0');
        if (empty($card_number) || empty($id)){
            $this->ajaxReturn(false,"数据验证失败:缺少card_number/id！")->ajaxOutput();
        }
        $field = 'id,card_number,card_type,out_time,status,price,gold,vip_time';
        $where['card_number'] = $card_number;
        $where['id'] = $id;
        $info = Db::name('card_password')->where($where)->field($field)->find();
        if (empty($info)){
            $this->ajaxReturn(false,"数据验证失败:信息不匹配！")->ajaxOutput();
        }
        if ($info['status'] == 1){
            $this->ajaxReturn(false,"该卡密已经使用过了！")->ajaxOutput();
        }
        if ($info['out_time'] < time()){
            $this->ajaxReturn(false,"该卡密已过期了！")->ajaxOutput();
        }
        if ($info['card_type'] == 1) {
            $userinfo = db::name('member')->where(array('id' => $memberId))->field('out_time,is_permanent')->find();
            if ($userinfo['is_permanent'] == 1){
                $this->ajaxReturn(false,"您无需再充值vip！")->ajaxOutput();
            }
            if ($info['vip_time'] == 999999999) {
                db::name('member')->where(array('id' => $memberId))->update(array('is_permanent' => 1));
            } else {
                if ($userinfo['out_time'] < time()) {
                    $out_time = time() + $info['vip_time'] * 3600 * 24;
                } else {
                    $out_time = $userinfo['out_time'] + $info['vip_time'] * 3600 * 24;
                }
                db::name('member')->where(array('id' => $memberId))->update(array('out_time' => $out_time));
            }
            db::name('card_password')->where($where)->update(array('status' => 1, 'use_time' => time()));
        } else {
            db::name('member')->where(array('id' => $memberId))->setInc('money', $info['gold']);
            db::name('card_password')->where($where)->update(array('status' => 1, 'use_time' => time()));
            $gold_log_data = array(
                'user_id' => $memberId,
                'gold' => intval($info['gold']),
                'module' => 'card_password',
                'explain' => '卡密充值'
            );
            write_gold_log($gold_log_data);
        }
        $this->ajaxReturn(true,"充值成功！")->ajaxOutput();
    }


    //获取卡密的url客服
    function  buy_cardpassword_uri(){
       $url= get_config('buy_cardpassword_uri');
       if($url==false){
           $url="#";
       }
        $this->data['data']=$url;
        $this->ajaxReturn(true,"查询成功！")->ajaxOutput();
    }
}


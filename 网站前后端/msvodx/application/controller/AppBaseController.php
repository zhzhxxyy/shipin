<?php

namespace app\controller;

use think\Controller;
use think\Db;
use think\Request;

class AppBaseController extends Controller{

    public function __construct(Request $request = null){
        $this->checkSiteStatus();
        parent::__construct($request);
    }

    public function _empty(){
        $this->ajaxReturn(false,"非法调用")->ajaxOutput();
    }

    private function checkSiteStatus(){
        $baseConfig=get_config_by_group('base');
        if(isset($baseConfig['site_status']) && $baseConfig['site_status']==0){
            $this->ajaxReturn(false,"siteClosed")->ajaxOutput();
        }
    }

    protected $data;
    protected $pagesize = 10;
    protected $page = 1;
    protected $MSG_LOGIN_FIRST="请先登录！";
    protected $MSG_TOKEN_INVALID="token失效，请重新登录！";

    /**
     * 返回数据 0：错误，1：正常 2表示需要登录 3token失效
     * @param mixed $status
     * @param null $error
     * @return $this
     */
    protected function ajaxReturn($status, $error = null) {
        $this->data['respCode'] = $status ? 1 : 0;
        $this->data['respMsg'] = is_null($error) ? '' : $error;
        if($this->MSG_LOGIN_FIRST==$this->data['respMsg']&&!$status){
            $this->data['respCode']=2;
        }else if($this->MSG_TOKEN_INVALID==$this->data['respMsg']&&!$status){
            $this->data['respCode']=3;
        }
        return $this;
    }

    /**
     * @param string $str
     * @param string $type
     */
    protected function ajaxOutput($str = "") {
        if (!empty($str)) {
            $this->data = $str;
        }
        if(!isset($this->data['data'])){
            $this->data['data']='';
        }
        header('Content-Type:application/json; charset=utf-8');
        echo json_encode($this->data);
        exit;
    }




    /**
     * 设置返回给前端的记录总数，总页码数，页码计量
     * @param unknown $total
     * @param unknown $pagesize
     */
    protected function setReturnPagination($total, $pagesize) {
        $this->data['respCode'] = is_null($total) ? 0 : 1;
        $this->data['respMsg'] = is_null($total) ? '暂无数据' : '';
        $this->data['data']['total'] = is_null($total) ? 0 : $total;
        $this->data['data']['totalPage'] = ceil($total / $pagesize);
        $this->data['data']['pagesize'] = $pagesize;
        if ($this->data['data']['totalPage'] <= 0){
            $this->data['data']['totalPage'] = 1;
        }
    }



    protected function isLogin(){
        $token=isset($_REQUEST['token'])?$_REQUEST['token']:'';
        if(empty($token)){
            return false;
        }
        $user_info = db::name('member')->where(array('access_token' => $token))->find();
        if($user_info){
            return $user_info;
        }
        return false;
    }


    protected function setSessionMemberId(){
        $token=isset($_REQUEST['token'])?$_REQUEST['token']:'';
        session('member_id',"");
        if(!empty($token)){
            $user_info = db::name('member')->field('id')->where(array('access_token' => $token))->find();
            if($user_info){
                session('member_id',$user_info['id']);
            }
        }
    }

    protected function isAccessThenLogin() {
        $token=isset($_REQUEST['token'])?$_REQUEST['token']:'';
        if(empty($token)){
            $this->ajaxReturn(false, $this->MSG_LOGIN_FIRST)->ajaxOutput();
        }
        $user_info = db::name('member')->where(array('access_token' => $token))->find();
        if(!$user_info){
            $this->ajaxReturn(false, $this->MSG_TOKEN_INVALID)->ajaxOutput();
        }
        return $user_info;
    }

    protected function getMemberInfoThenLogin(){
        $token=isset($_REQUEST['token'])?$_REQUEST['token']:'';
        if(empty($token)){
            $this->ajaxReturn(false, $this->MSG_LOGIN_FIRST)->ajaxOutput();
        }
        $memberInfo = Db::name('member')->field('id,headimgurl,is_permanent,tel,sex,email,nickname,gid,out_time,money,is_agent,username')->where(array('access_token' => $token))->find();
        if (!$memberInfo){
            $this->ajaxReturn(false, $this->MSG_TOKEN_INVALID)->ajaxOutput();
        }
        $memberInfo['isVip'] = false;
        $memberInfo['isEverVip'] = false;
        if ($memberInfo['is_permanent'] == 1 || $memberInfo['out_time'] > time()) {
            $memberInfo['isVip'] = true;
            if ($memberInfo['is_permanent'] == 1){
                $memberInfo['isEverVip'] = true;
            }
        }
        $memberInfo['tel_asterisk'] = substr($memberInfo['tel'], 0, 4) . "****" . substr($memberInfo['tel'], 8, 3);
        $memberInfo['token'] = $token;
        $memberInfo['headimgurl']=$this->addPreUrl($memberInfo['headimgurl']);
        return $memberInfo;
    }


    protected function addPreUrl($url){
        if(!empty($url)&&strtolower(substr($url, 0, 4))!="http") {
            $url = $this->getPreUrl() . $url;
        }
        return $url;
    }


    protected function getPreUrl(){
        return $_SERVER['REQUEST_SCHEME'] . "://" . $_SERVER['HTTP_HOST'] ;
    }



    //获取随机用户名
    public function getRandUserNumber(){
        //12位
        $chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        $username = "";
        for ( $i = 0; $i < 6; $i++ ){
            $username .= $chars[mt_rand(0, strlen($chars))];
        }
        return strtoupper(base_convert(time() - 1420070400, 10, 36)).$username;
    }

}
<?php
/**
 * 系统支付处理逻辑
 * Date: 2017/12/29
 * Author: $dreamer
 */

namespace app\controller;

use app\model\Order;
use app\model\RechargePackage;
use think\Controller;
use think\Request;
use think\Db;

class AppSystemPay extends AppBaseController{



    public function recharge(Request $request){
        //  从系统配置获取系统应用的支付方式
        //  如果是codePay，那么显示出 微信支付、QQ支付、支付宝支付
        // 否则，再分别判断微信支付和支付宝支付是否开启,显示开启的支付方式信息
        $curSysPayCode = get_config('system_payment_code');
        //支付方式列表
        $preUrl=$this->getPreUrl();
        $paymentList = [];
        switch ($curSysPayCode) {
            case 'codePay':
                $codePayInfo = Db::name('payment')->where("pay_code='{$curSysPayCode}'")->find();
                if (!$codePayInfo || $codePayInfo['status'] != 1) {
                    $this->error('当前支付不可用，请联络管理员解决！', null, '', 10);
                }

                $paymentList[] = ['payName' => '支付宝支付', 'payCode' => 'codePay|aliPay', 'payIcon' => $preUrl.'/tpl/default/static/images/Alipay.png'];
                // $paymentList[] = ['payName' => '微信支付', 'payCode' => 'codePay|wxPay', 'payIcon' => $preUrl.'/tpl/default/static/images/WeChat.png'];
                // $paymentList[] = ['payName' => 'QQ钱包', 'payCode' => 'codePay|qqPay', 'payIcon' => $preUrl.'/tpl/default/static/images/QQ.png'];
                break;
            case 'nativePay':
                $where = ['status' => 1, 'is_third_payment' => 0];
                $nativeList = Db::name('payment')->where($where)->select();
                if (count($nativeList) < 1) $this->error('当前无可用的支付方式，请联络管理员解决！', null, '', 10);

                foreach ($nativeList as $item) {
                    switch ($item['pay_code']) {
                        case 'wxPay':
                            $paymentList[] = ['payName' => '微信支付', 'payCode' => 'nativePay|wxPay', 'payIcon' => $preUrl.'/tpl/default/static/images/WeChat.png'];
                            break;
                        case 'aliPay':
                            $paymentList[] = ['payName' => '支付宝支付', 'payCode' => 'nativePay|aliPay', 'payIcon' => $preUrl.'/tpl/default/static/images/Alipay.png'];
                            break;
                    }
                }
                break;
        }


        //整理套餐数据
        $rechargeList = Db::name('recharge_package')->where('status=1')->order('sort asc')->select();
        //金币套餐数据
        $goldPackageList = Db::name('gold_package')->select();

        $this->data['data']['payCode']=$curSysPayCode;
        $this->data['data']['paymentList']=$paymentList;
        $this->data['data']['rechargeList']=$rechargeList; //整理套餐数据
        $this->data['data']['goldPackageList']=$goldPackageList; //金币套餐数据
        $this->data['data']['gold_exchange_rate']= get_config('gold_exchange_rate');


        $this->ajaxReturn(true,"成功")->ajaxOutput();

    }



    /**
     * 订单创建
     * @param Request $request
     */
    public function createOrder(Request $request)
    {
        $memberInfo=$this->getMemberInfoThenLogin();
        session('member_id',$memberInfo['id']);

        //todo 设置 session('cur_agent_id') 代理商
        $buyType = $request->post('buyType/d', '0');
        $buyType = in_array($buyType, [1, 2]) ? $buyType : 1;
        $payCode = $request->post('payCode/s', '');
        $payCodeArr = explode('|', $payCode);
        if (count($payCodeArr) < 2){
            $this->ajaxReturn(false,"支付方式参数错误，请重试！")->ajaxOutput();
        }
        $price = (float)$request->post('price');
        if ($price <= 0){
            $this->ajaxReturn(false,"订单金额不正确！")->ajaxOutput();
        }
        $orderInfo = [
            'payment_code' => $payCodeArr[0],                   //支付方式的code
            'pay_channel' => $payCodeArr[1],                    //支付渠道：alipay qqpay wxpay
            'price' => $price,                                  //金额
            'buy_type' => $buyType,                             //购买类型，1:金币，2:vip
            'user_id' => session('member_id'),                  //会员Id
            'from_agent_id' => session('cur_agent_id'),           //当前代理商id
            'from_domain' => $request->domain(),                  //请求的来源网址
        ];

        switch ($buyType) {
            case 1: //gold
                $rate = get_config('gold_exchange_rate'); //金币兑换比例
                $orderInfo['buy_glod_num'] = (int)$orderInfo['price'] * $rate;  //购买的金币数
                break;
            case 2: //vip
                //如果已是永久会员，则无需再充值
                if($memberInfo['is_permanent']==1){
                    $this->ajaxReturn(false,"您已是我站永久VIP,无需充值VIP!")->ajaxOutput();
                }
                $packageId = $request->post('packageId/d', 0);
                $packageInfo = RechargePackage::get($packageId);
                if (!$packageInfo) {
                    $this->ajaxReturn(false,"您要购买的套餐不存在或已关闭!")->ajaxOutput();
                }
                //如果是购买vip套餐，那么金额以套餐金额为准
                if ($packageInfo->price != $orderInfo['price']) $orderInfo['price'] = $packageInfo->price;
                $orderInfo['buy_vip_info'] = $packageInfo->hidden(['status', 'sort'])->toJson();   //购买的vip信息
                break;
        }

        $orderInfo['order_sn'] = create_order_sn();
        $order = new Order();
        $order->save($orderInfo);
        $orderSn = $order->order_sn;
        if ($orderSn) {
            $this->data['data']=$this->getPreUrl()."/app_system_pay/pay?orderSn=".$orderSn."&token=".$_REQUEST['token'];
            $this->ajaxReturn(true,"创建订单成功，请支付!")->ajaxOutput();
        } else {
            $this->ajaxReturn(false,"创建订单失败，请重试!")->ajaxOutput();
        }
    }


    public function pay(Request $request){
        $this->setSessionMemberId();
        if(empty(session('member_id'))||session('member_id')<=0){
            //未登录
            $this->error('请先登录!');
            die;
        }
        $orderSn = $request->param('orderSn');
        if(empty($orderSn)){
            $this->error('订单不存在!');
            die;
        }
        $this->redirect('SystemPay/payapp', ['orderSn' => $orderSn]);
    }


}
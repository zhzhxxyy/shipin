<?php
/**
 * ja支付通知处理逻辑
 * @Author: $dreamer
 * @Date:   2017/12/28
 */

namespace app\controller;

use systemPay\aliPay;
use systemPay\wxPay;
use think\Request;
use app\model\Order as Order;

class PayNotify
{

    /**
     * 码支付通知处理
     * @param Request $request
     */
    function codepayNotify(Request $request)
    {
        #header('Content-type:text/html;charset=utf8'); 加上这句后codepay监测软件会出错  by $dreamer
        @file_put_contents('../logs/codepay_notify.log', "\r\n" . date('Y-m-d H:i:s') . str_repeat('===', 30) . "\r\n" . var_export($request->param(), 1), FILE_APPEND);

        $notifyData = $request->post();

        /*
        $notifyData=array (//模拟数据
          'app_time' => '1515127780',
          'chart' => 'utf-8',
          'id' => '18709',
          'money' => '0.01',
          'order_id' => '18709',
          'pay_id' => '2018010509574458116',
          'pay_no' => '20180105200040011100540086842664',
          'pay_time' => '1515117471',
          'price' => '0.01',
          'status' => '1',
          'tag' => '0',
          'trueID' => '18709',
          'type' => '1',
          'version' => '4.350',
          'sign' => '1d7215bd32aa207c507510be7acdcea0',
        );
        */

        $codepayer = new \systemPay\codePay();
        if ($codepayer->verifyNotifyData($notifyData)) {
            $updateRs = Order::updateOrder($notifyData['pay_id'], $notifyData['money']);

            if (is_array($updateRs) && isset($updateRs['result']) && $updateRs['result'] == 0) {
                ob_clean();
                exit('success');
            } else {
                exit($updateRs['msg']);
            }
        } else {
            exit('密钥验证失败');
        }
    }


    /** 支付宝支付通知 */
    function alipayNotify(Request $request)
    {
        $alipayer = new aliPay();

        if ($alipayer->verify($request->param())) {
            $updateRs = Order::updateOrder($request->param('out_trade_no'), $request->param('total_amount'));
            if (is_array($updateRs) && isset($updateRs['result']) && $updateRs['result'] == 0) {
                ob_clean();
                exit('success');
            } else {
                exit($updateRs['msg']);
            }
        } else {
            exit('数据验证失败');
        }
    }

    /** 微信支付通知 */
    function wxpayNotify(){
        $wxpayer=new wxPay();
        if(is_array($notifyData=$wxpayer->verify())){
            $updateRs = Order::updateOrder($notifyData['out_trade_no'], $notifyData['total_fee']);
            if (is_array($updateRs) && isset($updateRs['result']) && $updateRs['result'] == 0) {
                ob_clean();
                exit('success');
            } else {
                exit($updateRs['msg']);
            }
        }else{
            exit('数据验证失败');
        }
    }


}
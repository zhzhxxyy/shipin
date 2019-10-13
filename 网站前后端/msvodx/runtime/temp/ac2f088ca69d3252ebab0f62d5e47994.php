<?php if (!defined('THINK_PATH')) exit(); /*a:1:{s:43:"./tpl/default/mobile/system_pay/payapp.html";i:1566876626;}*/ ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><?php echo (isset($page_title) && ($page_title !== '')?$page_title:""); ?>_<?php echo $seo['site_title']; ?></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" href="__ROOT__/tpl/default/mobile/static/css/common.css" />
    <link rel="stylesheet" href="__ROOT__/tpl/default/mobile/static/css/member.css">
    <link rel="stylesheet" href="__ROOT__/tpl/default/mobile/static/css/style.css" />
    <link rel="stylesheet" href="__ROOT__/tpl/default/static/js/layui/css/layui.css">

    <script src="__ROOT__/tpl/default/mobile/static/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="__ROOT__/tpl/default/mobile/static/js/layer_mobile/layer.js"></script>
    <script type="text/javascript" charset="utf-8" src="__ROOT__/tpl/default/static/js/layui/layui.js"></script>
    <script type="text/javascript" src="__ROOT__/tpl/default/mobile/static/js/common.js"></script>
    <link rel="stylesheet" href="__ROOT__/tpl/default/static/css/font_485358_gtgl3zs6gyvqjjor/iconfont.css">
    <?php $menu = getMenu();?>
</head>
<body>
<div class="recharge">
            <p class="method"><img src="<?php echo $payImgs['logo']; ?>" /></p>
            <div class="pay-mode">
                <?php if(isset($payerInfo['realPayPrice'])): ?>
                <p class="money">￥<?php echo $payerInfo['realPayPrice']; ?>元<span class="no-pay"></span></p>
                <?php else: ?>
                <p class="money">￥<?php echo $payerInfo['price']; ?>元<span class="no-pay"></span></p>
                <?php endif; ?>

                <div class="ewmBox-bg">
                    <a>
                        <img src="<?php echo $payerInfo['qrcode']; ?>"/>
                        <div>
                            <img class="pay-btn" src="<?php echo $payImgs['icon']; ?>" />
                        </div>
                    </a>
                </div>
                <?php if(isset($payerInfo['realPayPrice'])): ?><span class="recharge-font">请确认支付<b><?php echo $payerInfo['realPayPrice']; ?></b>元，否则支付会失败</span><?php endif; if(isset($payerInfo['run_app_url'])): ?><p style="font-size:14px;margin:10px 5px;"><a href="<?php echo $payerInfo['run_app_url']; ?>" style="color:#00a8f2!important;">点击唤醒[<?php echo $payerInfo['payName']; ?>]直接支付</a></p><?php endif; ?>
                <div class="pay-money">
                    <div class="pay-explain">
                        <i class="btn fn-saoyisao"></i>
                        <div class="Explain">
                            <span>请使用<var><?php echo $payerInfo['payName']; ?></var>扫一扫</span>
                            <span>扫描二维码完成支付</span>
                        </div>
                    </div>
                </div>

            </div>
        </div>

<script type="text/javascript">
    $(function(){
        var orderSn="<?php echo request()->param('orderSn'); ?>";
        timer=setInterval("getStatus('"+orderSn+"')",2000);

        var navTopTitle="<?php echo (isset($navTopTitle) && ($navTopTitle !== '')?$navTopTitle:'视频站'); ?>";
        $('#navTopTitle').html(navTopTitle);
    });

    function getStatus(orderSn){
        $.post("<?php echo url('api/getOrderStatus'); ?>",{orderSn:orderSn},function (resp) {
            if(resp.statusCode==0){
                if(resp.data.orderStatus==1){
                    clearInterval(timer);
                    $("#payStatusDesc").html('支付完成');
                    layer.open({skin:'msg',content:'恭喜您，支付成功！',time:2,end:function () {
                       // location.href="<?php echo url('member/member'); ?>";
                    }});
                }
            }else{
                layer.open({skin:'msg',content:'请求支付状态时发生错误！ error:'+resp.message,time:2});
            }

        },'JSON');
    }
</script>
</body>
</html>
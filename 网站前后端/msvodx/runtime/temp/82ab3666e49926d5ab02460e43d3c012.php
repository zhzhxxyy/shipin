<?php if (!defined('THINK_PATH')) exit(); /*a:2:{s:37:"./tpl/default/mobile/index/login.html";i:1520830436;s:37:"./tpl/default/mobile/common/head.html";i:1520234274;}*/ ?>
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
    <!--
    <script>
        layui.use(['form', 'layedit', 'laydate'], function(){ });
    </script>
    -->
    <?php $menu = getMenu();?>
</head>
<body>
<header class="js-header">
    <div  class="head">
        <a id="navBackBtn" href="javascript:history.go(-1);" class="go-back"><i></i></a>
        <span id="navTopTitle"></span>
        <div class="menu"><i class="btn fn-sort"></i></div>
    </div>
    <nav class="js-nav">
        <ul>
            <?php if(is_array($menu) || $menu instanceof \think\Collection || $menu instanceof \think\Paginator): $i = 0; $__LIST__ = $menu;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
            <li <?php if($vo['current'] == 1): ?>class="cur"<?php endif; ?> >
            <a  target="_self" href="<?php echo $vo['url']; ?>" >
                <?php echo $vo['name']; if(!(empty($vo['sublist']) || (($vo['sublist'] instanceof \think\Collection || $vo['sublist'] instanceof \think\Paginator ) && $vo['sublist']->isEmpty()))): ?>
                <div class="menu-two">
                    <?php if(is_array($vo['sublist']) || $vo['sublist'] instanceof \think\Collection || $vo['sublist'] instanceof \think\Paginator): $i = 0; $__LIST__ = $vo['sublist'];if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$v): $mod = ($i % 2 );++$i;?>
                    <a target="_self" <?php if($v['current'] == 1): ?>class="cur"<?php endif; ?> href="<?php echo $v['url']; ?>"><?php echo $v['name']; ?></a>
            <?php endforeach; endif; else: echo "" ;endif; ?>
            </div>
            <?php endif; ?>
            </a>
            </li>
            <?php endforeach; endif; else: echo "" ;endif; ?>
        </ul>
    </nav>
    <div class="nav-masklayer"></div>
</header>


<div class="box">
<link rel="stylesheet" href="__ROOT__/tpl/default/mobile/static/css/login.css">

<style>
    .login-box button {
        margin: 60px auto 10px;
    }

    .login-box p span {
        border: 0;
        width: 100px;
    }
</style>


<div>
    <div class="login-box">
        <p><i class="btn fn-zhanghao"></i><input type="text" id="userName" placeholder="请输入登录账号" class="name"></p>
        <p><i class="btn fn-201"></i><input type="password" id="password" placeholder="请输入登录密码" class="pwd"></p>
        <?php if(get_config('verification_code_on')): ?>
        <p>
            <i class="btn fn-verification-code"></i>
            <input type="text" name="verifyCode" id="verifyCode"  placeholder="验证码">
            <span id="register_code">
                <img src="<?php echo url('api/getCaptcha'); ?>" onclick="this.src='<?php echo url('api/getCaptcha'); ?>?'+Math.random()"id="verifyCodeImg"/>
            </span>
        </p>
        <?php endif; ?>
        <button class="button" onclick="login()">登 录</button>
        <div class="forget"><?php if(get_config('register_validate')): ?><a href="<?php echo url('member/seek_pwd'); ?>">忘记密码？</a><?php endif; ?><a class="register-btn" href="<?php echo url('index/register'); ?>">新用户注册</a></div>
        <div class="login-Third">
            <?php  $longwait=get_sanfanlogin(); if(is_array($longwait) || $longwait instanceof \think\Collection || $longwait instanceof \think\Paginator): if( count($longwait)==0 ) : echo "" ;else: foreach($longwait as $key=>$vo): if($vo['login_code'] == 'qq'): ?>
            <a href="<?php echo url('open/login',['code'=>'qq']); ?>"><i class="btn fn-QQ"></i>QQ</a>
            <?php endif; if($vo['login_code'] == 'wechat'): ?>
            <a  href="<?php echo url('open/login',['code'=>'wechat']); ?>" ><i class="btn fn-weixin1"></i>微信</a>
            <?php endif; endforeach; endif; else: echo "" ;endif; ?>
        </div>
    </div>
</div>
<script>
    $(function () {
        $("input").focus(function () {
            $(this).parent().css("border-bottom", "1px solid #c1dffd");
        });
        $("input").blur(function () {
            $(this).parent().css("border-bottom", "1px solid #eee");
        });

        $(".submit").click(function () {
            var name = $(".name").val();
            var pwd = $(".pwd").val();
            var rePwd = $(".rePwd").val();
            var code = $(".code").val();

            if (name == '') {
                layer.open({
                    content: '账号不能为空！'
                    , skin: 'msg'
                    , time: 2
                });
            } else if (pwd == '') {
                layer.open({
                    content: '密码不能为空！'
                    , skin: 'msg'
                    , time: 2
                });
            } else if (6 > pwd.length || pwd.length > 20) {
                layer.open({
                    content: '密码不能小于6或者大于20！'
                    , skin: 'msg'
                    , time: 2
                });
            }
        });

    });

    //登陆
    function login() {
        var user = $('#userName').val();
        var password = $('#password').val();
        var verifyCode = $('#verifyCode');
        if (user == '' || password == '') {
            layer.open({skin: 'msg', time: 1, content: '用户名或密码不能为空.'});
            return false;
        }

        if (verifyCode.val() == '') {
            layer.open({skin: 'msg', time: 1, content: '验证码不能为空.'});
            verifyCode.focus();
            return false;
        }

        var url = "<?php echo url('api/login'); ?>";
        var refererUrl = "<?php echo $refererUrl; ?>";
        var jumpUrl = "<?php echo url('member/member'); ?>";
        if (refererUrl != '') jumpUrl = refererUrl;

        $.post(url, {userName: user, password: password, verifyCode: verifyCode.val()}, function (data) {

            if (data.statusCode == 0) {
                layer.open({
                    content: '登陆成功', time: 1, skin: 'msg', end: function () {
                        location.href = jumpUrl;
                    }
                });
            } else {
                layer.open({content: data.error, skin: 'msg', time: 1});
                $("#verifyCodeImg").click();
            }
        }, 'JSON');

    }

    $(function () {
        //to and footer nav setting
        var navTopTitle = "<?php echo (isset($navTopTitle) && ($navTopTitle !== '')?$navTopTitle:'视频站'); ?>";
        $('#navTopTitle').html(navTopTitle);
        $('.navFooter').removeClass('active');
        $('.navFooter:nth-child(<?php echo (isset($curFooterNavIndex) && ($curFooterNavIndex !== '')?$curFooterNavIndex:2); ?>)').addClass('active');
    });
</script>
</body>
</html>
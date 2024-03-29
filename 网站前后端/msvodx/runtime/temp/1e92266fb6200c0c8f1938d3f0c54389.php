<?php if (!defined('THINK_PATH')) exit(); /*a:4:{s:31:"./tpl/default/member/agent.html";i:1520336688;s:32:"./tpl/default/common/header.html";i:1521865672;s:32:"./tpl/default/member/common.html";i:1517316314;s:32:"./tpl/default/common/footer.html";i:1521865458;}*/ ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
     <?php $menu = getMenu(); $register_validate = empty(get_config('register_validate')) ? 0 : get_config('register_validate');?>
    <title><?php echo (isset($page_title) && ($page_title !== '')?$page_title:""); ?>_<?php echo $seo['site_title']; ?></title>
    <meta name="keywords" lang="zh-CN" content="<?php echo $seo['site_keywords']; ?>"/>
    <meta name="description" lang="zh-CN" content="<?php echo $seo['site_description']; ?>" />
    <meta name="renderer" content="webkit">

    <link rel="stylesheet" href="__ROOT__/tpl/default/static/js/layui/css/layui.css">
    <link href="__ROOT__/tpl/default/static/css/common.css" rel="stylesheet">

    <script type="text/javascript" src="__ROOT__/tpl/default/static/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="__ROOT__/tpl/default/static/js/layer/layer.js"></script>
    <script type="text/javascript" src="__ROOT__/tpl/default/static/js/common.js"></script>
    <script type="text/javascript" charset="utf-8" src="__ROOT__/tpl/default/static/js/layui/layui.js"></script>

    <link rel="stylesheet" href="__ROOT__/tpl/default/static/css/font_485358_gtgl3zs6gyvqjjor/iconfont.css">

    <!--[if IE]>
    <link href="__ROOT__/tpl/default/static/css/demo.css" rel="stylesheet">
    <![endif]-->

    <script>
        layui.use(['form', 'layedit', 'laydate'], function(){

        });
        if(!!window.ActiveXObject || "ActiveXObject" in window){
            location.href="<?php echo url('index/remind'); ?>"
        }

    </script>

</head>
<body>
<div class="g-header g-head" id="qheader">
    <div class="g-header-container">
        <div class="g-box">
            <!--logo-->
            <div class="yk-logo">
                <a href="/" title="<?php echo $seo['site_title']; ?>">
                    <img src="<?php echo $seo['site_logo']; ?>"/> <!--__ROOT__/tpl/default/static/images/img/login.png -->
                </a>
            </div>
            <!--顶部导航-->
            <div class="g-head-center">
                <ul>
                    <?php if(is_array($menu) || $menu instanceof \think\Collection || $menu instanceof \think\Paginator): $i = 0; $__LIST__ = $menu;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
                    <li <?php if($vo['current'] == 1): ?>class="current"<?php endif; ?> >
                        <a class="bar-list" href="<?php echo $vo['url']; ?>" >
                            <?php echo $vo['name']; ?>
                        </a>
                        <?php if(!(empty($vo['sublist']) || (($vo['sublist'] instanceof \think\Collection || $vo['sublist'] instanceof \think\Paginator ) && $vo['sublist']->isEmpty()))): ?>
                        <div class="menu-level">
                            <?php if(is_array($vo['sublist']) || $vo['sublist'] instanceof \think\Collection || $vo['sublist'] instanceof \think\Paginator): $i = 0; $__LIST__ = $vo['sublist'];if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$v): $mod = ($i % 2 );++$i;?>
                            <span <?php if($v['current'] == 1): ?>class="current"<?php endif; ?>><a href="<?php echo $v['url']; ?>"><?php echo $v['name']; ?></a></span>
                            <?php endforeach; endif; else: echo "" ;endif; ?>
                        </div>
                        <?php endif; ?>
                    </li>
                    <?php endforeach; endif; else: echo "" ;endif; ?>
                </ul>
            </div>
            <?php $controller =  lcfirst(request()->controller());?>
            <!--顶部搜索-->
            <div class="search">
                <div class="search-box">
                    <form
                            <?php switch($controller): case "images": ?> action="<?php echo url('search/index',array('type'=>'atlas')); ?>"<?php break; case "atlas": ?> action="<?php echo url('search/index',array('type'=>'atlas')); ?>"<?php break; case "novel": ?>action="<?php echo url('search/index',array('type'=>'novel')); ?>"<?php break; case "search": ?>action="<?php echo url('search/index',array('type'=>$type)); ?>"<?php break; default: ?>
                                action="<?php echo url('search/index',array('type'=>'video')); ?>"
                            <?php endswitch; ?>
                            method="get">
                            <input type="text" class="default" value='<?php if(isset($key_word)): ?><?php echo $key_word; endif; ?>' name="key_word">
                            <button type="submit">
                                搜索
                            </button>
                    </form>

                </div>
            </div>
            <!--顶部右边导航-->
            <div class="g-ucenter">
                <div class="u-box">
                    <div class="handle">
                        <a href="<?php echo url('member/member'); ?>" ><i class="btn fn-vip2"></i>VIP</a>
                    </div>
                </div>
                <div class="u-box">
                    <div class="handle">
                        <a href="<?php echo url('member/agent'); ?>"><i class="btn fn-dailishang"></i>代理</a>
                    </div>
                </div>
                <div class="u-box">
                    <div class="handle">
                        <a href="<?php echo url('member/video'); ?>" ><i class="btn fn-shangchuan"></i>上传</a>
                    </div>
                </div>
                <div class="u-box web">
                    <div class="handle">
                        <a href="javaScript:void(0);" ><i class="btn fn-erweima"></i>移动端</a>
                    </div>
                    <div class="main-code login-after" style="display: none;">
                        <div id="qrcode"></div>
                        <div style="line-height: 30px;font-weight: bold;">扫我在手机上浏览</div>
                    </div>
                </div>
                <div class="u-login">
                    <?php $memberInfo = get_member_info();$login_status = check_is_login();if($login_status['resultCode'] != 1): ?>
                    <!-- 未登陆 -->
                    <div class="login-before" style="display: block;">
                        <a href="javascript:void(0);">
                            <img class="avatar" src="__ROOT__/tpl/default/static/images/header.png" title="未登录">
                        </a>
                    </div>
                    <?php else: ?>
                    <!-- 已登陆 -->
                    <div class="login-before" style="display: block;">
                        <a href="<?php echo url('member/member'); ?>">
                            <img class="logged" src="<?php if(session('member_info')['headimgurl'] != ''): ?><?php echo session('member_info')['headimgurl']; else: ?>/static/images/user_dafault_headimg.jpg<?php endif; ?>" title="已登录">
                        </a>
                        <div class="after-box">
                            <div class="login-after dropdown">
                                <div class="userbox">
                                    <img src="<?php if(session('member_info')['headimgurl'] != ''): ?><?php echo session('member_info')['headimgurl']; else: ?>/static/images/user_dafault_headimg.jpg<?php endif; ?>" />
                                    <span><?php echo session('member_info')['nickname']; ?></span>
                                    <?php if($memberInfo['isVip']==false): ?>
                                        <div class="set-vip">
                                            <span style="color: #a0a0a0">您还不是会员</span>
                                            <a href="<?php echo url('system_pay/recharge'); ?>">开通/续费</a>
                                            <p>开通VIP，全站海量大片随意看</p>
                                        </div>
                                    <?php else: ?>
                                        <div class="set-vip">
                                            <span >VIP会员</span>
                                            <?php if($memberInfo['is_permanent'] != 1): ?>
                                                <a href="<?php echo url('system_pay/recharge'); ?>">开通/续费</a>
                                            <?php else: ?>
                                                <a href="<?php echo url('system_pay/recharge',array('type'=>'1')); ?>">充金币</a>
                                            <?php endif; ?>
                                            <p>会员日期 :  <?php if($memberInfo['is_permanent'] == 1): ?>永久<?php else: if($memberInfo['out_time'] > time()): ?><?php echo safe_date('Y-m-d',$memberInfo['out_time']); ?> 到期 <?php else: ?>已过期<?php endif; endif; ?></p>
                                        </div>
                                    <?php endif; ?>
                                    <a class="a-avatar" href="<?php echo url('member/member'); ?>" target="_self">个人中心</a>
                                    <a <?php if(isSign() != '1'): ?>class="sign-btn"  onclick="sign()" <?php else: ?> class="sign-btn Completion" <?php endif; ?> ><var>+2</var><i ></i>签到</a>
                                </div>
                                <div class="u-bottom">
                                    <!--<a href="javascript:void(0);" class="set-up">账户设置</a>-->
                                    <a href="javascript:void(0);" class="singout" onclick="logout()">退出登录</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <?php endif; ?>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="login-box" id="login-box">
    <div class="box login">
        <div class="login-list"><i class="btn fn-zhanghao"></i><input type="text" id="userName" placeholder="手机号/邮箱"/></div>
        <div class="login-list"><i class="btn fn-201"></i><input type="password" id="password" placeholder="登录密码"/></div>
        <?php if(get_config('verification_code_on')): ?>
        <div class="login-list"><i class="btn fn-verification-code"></i><input type="text" name="verifyCode" id="verifyCode" class="code" placeholder="验证码" /><img src="<?php echo url('api/getCaptcha'); ?>" onclick="this.src='<?php echo url('api/getCaptcha'); ?>?'+Math.random()" id="verifyCodeImg" /></div>
        <?php endif; ?>
        <a class="submit-btn" onclick="login()">登录</a>
        <div >
            <div class="forget"><?php if($register_validate != 0): ?><a href="<?php echo url('member/seek_pwd'); ?>">忘记密码？</a> |  <?php endif; ?><a class="reg-btn">注册</a></div>
            <div class="login-Third">
                <?php  $longwait=get_sanfanlogin(); if(is_array($longwait) || $longwait instanceof \think\Collection || $longwait instanceof \think\Paginator): if( count($longwait)==0 ) : echo "" ;else: foreach($longwait as $key=>$vo): if($vo['login_code'] == 'qq'): ?>
                    <a href="<?php echo url('open/login',['code'=>'qq']); ?>"><i class="btn fn-QQ"></i> QQ </a>
                <?php endif; if($vo['login_code'] == 'wechat'): ?>
                    <a href="<?php echo url('open/login',['code'=>'wechat']); ?>"><i class="btn fn-weixin1"></i> 微信 </a>
                <?php endif; endforeach; endif; else: echo "" ;endif; ?>
            </div>
        </div>
    </div>
    <div class="box register">
        <div class="login-list">
            <i class="btn fn-zhanghao"></i>
            <input type="text" id="reg_userName" class="phone"  <?php if($register_validate == 1): ?>placeholder="邮箱地址"<?php else: if($register_validate == 2): ?>placeholder="手机号码"<?php else: ?>placeholder="用户名"<?php endif; endif; ?> />
        </div>

        <div class="login-list">
            <i class="btn fn-nicheng"></i>
            <input type="text" id="nickname" class="pwd" placeholder="用户昵称"/>
        </div>

        <div class="login-list">
            <i class="btn fn-201"></i>
            <input type="password" id="reg_pwd" class="pwd" placeholder="密码"/>
        </div>

        <div class="login-list">
            <i class="btn fn-201"></i>
            <input type="password" id="reg_pwd_re" placeholder="确认密码"/>
        </div>
        <?php if($register_validate != 0): ?>
        <div class="login-list code-box">
            <div class="code-input">
                <i class="btn fn-verification-code"></i>
                <input type="text" name="verifyCode" id="codes" placeholder="验证码"/>
            </div>
            <span id="register_code" onclick="getCode()">获取验证码</span>
        </div>
        <?php else: if(get_config('verification_code_on')): ?>
                <div class="login-list"><i class="btn fn-verification-code"></i><input type="text" name="verifyCode" id="codes" class="code" placeholder="验证码" /><img src="<?php echo url('api/getCaptcha'); ?>" onclick="this.src='<?php echo url('api/getCaptcha'); ?>?'+Math.random()" id="verifyCodeImg" /></div>
            <?php endif; endif; ?>
        <a class="submit-btn" onclick="register()">注册</a>
        <div class="forget"><div><a class="reg-btn">登录</a></div></div>
    </div>
</div>


<script type="text/javascript">

    //console.log("当前用户Id:<?php echo session('member_id'); ?>");
    var disabled = 0;
    function login() {
        var user = $('#userName').val();
        var password = $('#password').val();
        var verifyCode=$('#verifyCode');
        if (user == '' || password == '') {
            layer.msg('用户名或密码不能为空.', {icon: 2, anim: 6, time: 1000});
            return false;
        }

        if(verifyCode.val()==''){
            layer.msg('验证码不能为空.', {icon: 2, anim: 6, time: 1000});
            verifyCode.focus();
            return false;
        }

        var url = "<?php echo url('api/login'); ?>";
        $.post(url, {userName: user, password: password,verifyCode:verifyCode.val()}, function (data) {

            if (data.statusCode == 0) {
                layer.msg('登陆成功', {time: 1000, icon: 1}, function() {
                    location.reload();
                });
            } else {
                layer.msg(data.error, {icon: 2, anim: 6, time: 1000});
                $("#verifyCodeImg").click();
            }
        }, 'JSON');

    }

    $(document).keyup(function(event){
        if(event.keyCode ==13){
            if($(".login").is(":hidden")){
               return null;
            }else{
                login();
            }

        }
    });


    function codetTmes() {
        var second = $('#register_code').html();
        second--;
        if(second > 0){
            $('#register_code').html(second);
            setTimeout("codetTmes()",1000);
        }else{
            $('#register_code').html('获取验证码');
            disabled = 0;
        }
    }

    function getCode(){
        var user = $('#reg_userName').val();
        if(disabled)  return false;
        if (user == '' || password == '') {
            $('#reg_userName').focus();
            layer.msg('用户名不能为空.', {icon: 2, anim: 6, time: 1000});
            return false;
        }
        var url = "<?php echo url('api/getRegisterCode'); ?>";
        $.post(url, {username: user}, function (data) {
            if (data.statusCode == 0) {
                disabled = 1;
                layer.msg(data.error, {icon: 1, anim: 6, time: 3000});
                $('#register_code').html('60');
                codetTmes();
            }else{
                layer.msg(data.error, {icon: 2, anim: 6, time: 1000});
            }
        }, 'JSON');
    }

    function register(){
        var user = $('#reg_userName').val();
        var password = $('#reg_pwd').val();
        var confirm_password=$('#reg_pwd_re').val();
        var verifyCode=$('#codes').val();
        var nickname = $('#nickname').val();
        if (user == '') {
            layer.msg('用户名不能为空.', {icon: 2, anim: 6, time: 1000});
            return false;
        }
        if (nickname == '') {
            layer.msg('用户昵称不能为空.', {icon: 2, anim: 6, time: 1000});
            return false;
        }
        if (password == '') {
            layer.msg('密码不能为空.', {icon: 2, anim: 6, time: 1000});
            return false;
        }
        if (password != confirm_password) {
            layer.msg('两次密码不一致.', {icon: 2, anim: 6, time: 1000});
            return false;
        }
        if(verifyCode==''){
            layer.msg('验证码不能为空.', {icon: 2, anim: 6, time: 1000});
            $('#codes').focus();
            return false;
        }
        var url = "<?php echo url('api/register'); ?>";
        $.post(url, {username: user,nickname:nickname, password: password,confirm_password:confirm_password,verifyCode:verifyCode}, function (data) {
            if (data.statusCode == 0) {
                console.log(data);
                layer.msg('注册成功', {time: 1000, icon: 1}, function() {
                    location.reload();
                });
            }else{
                layer.msg(data.error, {icon: 2, anim: 6, time: 1000});
            }
        }, 'JSON');

    }

    function sign(){

        var url = "<?php echo url('api/sign'); ?>";
        $.post(url, {}, function (data) {
            if (data.resultCode == 0) {
                $('.sign-btn').find('var').html('+'+data.data['value']);
                $('.sign-btn').addClass("signs");
                $('.sign-btn').addClass("Completion");
                layer.msg('签到成功',  {icon: 1, anim: 6, time: 2000},function () {
                    $('.sign-btn').removeClass("signs");
                });
            }else{
                layer.msg(data.error, {icon: 2, anim: 6, time: 2000});
            }
        }, 'JSON');
    }

    function logout(){
        var url="<?php echo url('api/logout'); ?>";
        $.post(url,{},function(){
            layer.msg('退出成功', {time: 1000, icon: 1}, function() {
                location.reload();
            });
        },'JSON');
    }

    //$.post("",{userName:})

</script>

<!-- qrcode start -->
<script src="/static/js/qrcode.min.js" type="text/javascript"></script>
<script type="text/javascript">
    // 设置 qrcode 参数
    var qrcode = new QRCode('qrcode', {
        text: location.href,
        width: 130,
        height: 130,
        colorDark: '#000000',
        colorLight: '#ffffff',
        correctLevel: QRCode.CorrectLevel.H
    });
</script>
<!-- qrcode end -->

<link rel="stylesheet" href="__ROOT__/tpl/default/static/css/viewer.min.css">
<link rel="stylesheet" href="__ROOT__/tpl/default/static/css/member.css">
<script type="application/javascript" charset="utf-8" src="/static/UEditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/static/UEditor/ueditor.all.min.js"></script>
<script src="__ROOT__/tpl/default/static/js/jquery.zclip.min.js"></script>

<!--uper js files-->
<script type="text/javascript" src="/static/plupload-2.3.6/js/plupload.full.min.js"></script>
<script type="text/javascript" src="/static/xuploader/webServerUploader.js"></script>

<script type="text/javascript">
    $(function(){
        $("#cp-btn").zclip({
            path:'__ROOT__/tpl/default/static/js/ZeroClipboard.swf', //记得把ZeroClipboard.swf引入到项目中
            copy:function(){
                return $('#tg-link').val();
            }
        });
        createWebUploader('logo_btn','','','image',setLogoUrl,false);
    });
    function setLogoUrl(resp){
        if(resp.filePath!=undefined){
            $("#site_logo_url").val(resp.filePath);
            $("#site_logo").attr('src',resp.filePath);
            //editUserInfo('headimgurl',resp.filePath);
            layer.msg('上传成功！');
        }else{
            layer.msg('上传发生异常,请重试');
            createWebUploader('logo_btn','','','image',setLogoUrl,false);
        }
    }
    function agent_apply() {
        var url = "<?php echo url('api/agentApply'); ?>";
        $.post(url, {}, function (data) {
            if (data.resultCode == 0) {
                layer.msg(data.message, {time: 1000, icon: 1}, function() {
                    location.reload();
                });
            } else {
                layer.msg(data.error, {icon: 2, anim: 6, time: 1000});
            }
        }, 'JSON');

    }
</script>

<div class="s-body">
    <div class="content">
        <!--左边选择-->
        <div class="M-left">

    <ul class="select-box">
        <li <?php if($current_left_menu == 'member'): ?> class='on'<?php endif; ?> data-for="my-info">
            <a href="<?php echo url('member/member'); ?>">
                <i class="btn fn-info"></i><span>个人设置</span>
            </a>
        </li>
        <li <?php if($current_left_menu == 'recharge'): ?> class='on'<?php endif; ?> data-for="my-info">
        <a target="_blank" href="<?php echo url('system_pay/recharge'); ?>">
            <i class="btn fn-recharge"></i><span>我要充值</span>
        </a>
        </li>
        <li <?php if($current_left_menu == 'card_pwd'): ?> class='on'<?php endif; ?>>
        <a href="<?php echo url('member/card_pwd'); ?>">
            <i class="btn fn-qiaquan"></i><span>卡密充值</span>
        </a>
        </li>
        <li <?php if($current_left_menu == 'collection'): ?> class='on'<?php endif; ?>>
            <a href="<?php echo url('member/collection_video'); ?>">
                <i class="btn fn-collection"></i><span>我的收藏</span>
            </a>
        </li>
        <li <?php if($current_left_menu == 'video'): ?> class='on'<?php endif; ?>>
            <a href="<?php echo url('member/video'); ?>">
                <i class="btn fn-video"></i><span>我的视频</span>
            </a>
        </li>
        <li <?php if($current_left_menu == 'novel'): ?> class='on'<?php endif; ?>>
            <a href="<?php echo url('member/novel'); ?>">
                <i class="btn fn-novel"></i><span>我的资讯</span>
            </a>
        </li>
        <li <?php if($current_left_menu == 'img'): ?> class='on'<?php endif; ?>>
            <a href="<?php echo url('member/img'); ?>">
                <i class="btn fn-img"></i><span>我的图片</span>
            </a>
        </li>
        <li <?php if($current_left_menu == 'get_out_pay'): ?> class='on'<?php endif; ?>>
        <a href="<?php echo url('member/get_out_pay'); ?>">
            <i class="btn fn-tixian"></i><span>提现管理</span>
        </a>
        </li>
        <li <?php if($current_left_menu == 'record_gold'): ?> class='on'<?php endif; ?>>
        <a href="<?php echo url('member/record_gold'); ?>">
            <i class="btn fn-tubiao207"></i><span>金币记录</span>
        </a>
        </li>
        <li <?php if($current_left_menu == 'record_pay'): ?> class='on'<?php endif; ?>>
        <a href="<?php echo url('member/record_pay'); ?>">
            <i class="btn fn-3"></i><span>充值记录</span>
        </a>
        </li>
        <li <?php if($current_left_menu == 'record'): ?> class='on'<?php endif; ?>>
            <a href="<?php echo url('member/record_video'); ?>">
                <i class="btn fn-icon12"></i><span>消费记录</span>
            </a>
        </li>
        <li <?php if($current_left_menu == 'agent'): ?> class='on'<?php endif; ?> class="management">
            <a href="<?php echo url('member/agent'); ?>">
                <i class="btn fn-management"></i><span>代理商</span>
            </a>

        </li>
    </ul>
</div>
        <!--右边内容-->
        <div class="M-content">
            <?php if($is_agent != 1): ?>
            <!--代理申请-->
            <div class="apply">
                <p class="title">代理申请</p>
                <p>亲爱的用户<b> <?php echo session('member_info')['nickname']; ?></b>，您好！您当前还不是代理商，成为代理商后将享有更多权益。</p>
                <p>
                    <?php if($status != 3): ?>
                        您的申请审核状态：
                        <?php if($status == 0): ?>
                        <span class="adopt">审核中......</span>
                        <?php elseif($status == 2): ?>
                        <span class="adopt">已拒绝......</span>
                        <?php endif; endif; ?>
                </p>
                <a  onclick="agent_apply()"  <?php if($status != 3): else: ?>class="apply-btn" <?php endif; ?>>申请成为代理商</a><!-- class="apply-btn"-->
            </div>
            <?php else: ?>
            <!--代理申请结束-->
            <form action="" method="post" id="myform">
                <!--我的站点SEO设置-->
                <div class="set-up">
                    <p class="title">我的站点SEO设置</p>
                    <div class="set-box">
                        <p><label>我的独立域名</label>
                            <input type="text"  id="tg-link" class="extension" onmouseover="this.select();" readonly="readonly" value="<?php echo $agentDomain; ?>" >
                            <button id="cp-btn" type="button" class="exten-btn" style="height:32px;margin-left:-60px;">复制</button>
                        </p>
                        <script>
                            $(function(){
                                $("#cp-btn").zclip({
                                    path:'__ROOT__/tpl/default/static/js/ZeroClipboard.swf',
                                    copy:function(){
                                        return $('#tg-link').val();
                                    }
                                });
                            });
                        </script>

                        <p><label>网站主标题</label><input type="text" name="site_title" placeholder="显示在浏览器选项卡上的内容" value="<?php echo (isset($agent_config['site_title']) && ($agent_config['site_title'] !== '')?$agent_config['site_title']:''); ?>" /></p>
                        <p><label>网站keywords</label><input type="text" name="site_keywords" placeholder="提升站点在搜索引擎上的收录" value="<?php echo (isset($agent_config['site_keywords']) && ($agent_config['site_keywords'] !== '')?$agent_config['site_keywords']:''); ?>"  /></p>
                        <p><label>网站描述信息</label><input type="text" name="site_description" placeholder="提升站在搜索引擎上的收录" value="<?php echo (isset($agent_config['site_description']) && ($agent_config['site_description'] !== '')?$agent_config['site_description']:''); ?>" /></p>
                        <p>
                            <label>网站LOGO</label>
                            <a  class="layui-btn" id="logo_btn" value="" style="margin-right:15px;width: 105px;float: left;" >上传LOGO</a>
                            <img id="site_logo" src="<?php echo (isset($agent_config['site_logo']) && ($agent_config['site_logo'] !== '')?$agent_config['site_logo']:''); ?>" style="height:36px;display:block;border-radius:5px;border:1px solid #ccc;max-width: 200px;">
                            <input name="site_logo" id="site_logo_url"  type="hidden" value="<?php echo (isset($agent_config['site_logo']) && ($agent_config['site_logo'] !== '')?$agent_config['site_logo']:''); ?>">
                        </p>
                        <a class="submit" onclick="$('#myform').submit()" style="background: #3b9eff;" >保存设置</a>
                    </div>
                </div>
            </form>
            <?php endif; ?>

            <div class="apply">
                <p class="title">代理关系</p>
                <table border="1" class="distribution_table">
                    <tr>
                        <th width="130" style="min-width:130px;">级别</th>
                        <th width="780">用户名称</th>
                    </tr>
                    <tr>
                        <td >我的上级</td>
                        <td width="780"><?php if(!(empty($pid) || (($pid instanceof \think\Collection || $pid instanceof \think\Paginator ) && $pid->isEmpty()))): ?><?php echo $puserinfo['username']; else: ?>没有上级<?php endif; ?></td>
                    </tr>
                    <?php if($is_agent == 1): 
                        $user1 =  implode(" , ",array_column($list[1], 'username'));
                        $user2 =  implode(" , ",array_column($list[2], 'username'));
                        $user3 =  implode(" , ",array_column($list[3], 'username'));
                    ?>
                    <tr>
                        <td >一级代理（<?php echo count($list[1]); ?>）<br><i>下级</i></td>
                        <td width="780"><?php echo (isset($user1) && ($user1 !== '')?$user1:'暂无'); ?></td>
                    </tr>
                    <tr>
                        <td>二级代理（<?php echo count($list[2]); ?>）<br><i>下下级</i></td>
                        <td  width="780"><?php echo (isset($user2) && ($user2 !== '')?$user2:'暂无'); ?></td>
                    </tr>
                    <tr>
                        <td>三级代理（<?php echo count($list[3]); ?>）<br><i>下下下级</i></td>
                        <td  width="780"> <?php echo (isset($user3) && ($user3 !== '')?$user3:'暂无'); ?> </td>
                    </tr>
                    <?php endif; ?>
                </table>
            </div>


            <!--代理权益-->
            <div class="agent">
                <p class="title">代理权益</p>
                <div class="picBox">
                    <div class="img-cel">
                        <div>
                            <img src="/static/images/myself_domain.png" />
                        </div>
                        <span>独立域名</span>
                    </div>
                    <div class="img-cel">
                        <div>
                            <img src="/static/images/myself_seo.png" />
                        </div>
                        <span>定义站点SEO信息</span>
                    </div>
                    <div class="img-cel">
                        <div>
                            <img src="/static/images/myself_moey.png" />
                        </div>
                        <span>充值收益</span>
                    </div>
                </div>
            </div>
            <!--代理权益结束-->

        </div>
    </div>
</div>

    <!--底部-->
    <?php 
    $baseConfig = get_config_by_group('base');
    $baseConfig['friend_link'] =  empty($seo['friend_link']) ? $baseConfig['friend_link'] : $seo['friend_link'];
    $baseConfig['site_icp'] = empty($seo['site_icp']) ? $baseConfig['site_icp'] : $seo['site_icp'];
    $baseConfig['site_statis'] = empty($seo['site_statis']) ? $baseConfig['site_statis'] : $seo['site_statis'];
    $linkList=get_friend_link($baseConfig);
     ?>
    <div class="f12 footer">
        <div class="copyright">
            <div class="main">
                <p class="notice"><br>
                   本站所有视频、图片、资讯均由网友上传，如有侵犯权限请联系本站客服删除，本站不承担任何版权相关的法律责任， 请遵守本站协议勿上传不合法内容。<br>
                    </p>

                <p class="link">
                    <?php if(is_array($linkList) || $linkList instanceof \think\Collection || $linkList instanceof \think\Paginator): $i = 0; $__LIST__ = $linkList;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$link): $mod = ($i % 2 );++$i;?>
                    <a href="<?php echo $link['url']; ?>"><?php echo $link['name']; ?></a>
                    <?php endforeach; endif; else: echo "" ;endif; ?>
                </p>

                <p class="copyright">ICP备案号:<?php echo $baseConfig['site_icp']; ?>&nbsp;&nbsp; Copyright (c) 2017-2018 All Rights Reserved.</p>

                <!--统计代码-->
                <span>
                    <?php echo htmlspecialchars_decode($baseConfig['site_statis']); ?>
                </span>
            </div>
        </div>
    </div>
</div>
    <?php if($login_status['resultCode'] == 3): ?>
    <script>
        layer.msg('该账号已在其他地方登陆',
            {
                icon: 5,
                time: 0,
                shadeClose: true,
                shade: 0.8,
                btn: ['确定'],
                yes:function (index) {
                    layer.close(index);
                    window.location.reload();
                },
                success: function (layero) {
                    var btn = layero.find('.layui-layer-btn');
                    btn.css('text-align', 'center');
                }
            });
    </script>
    <?php endif; ?>
</body>
</html
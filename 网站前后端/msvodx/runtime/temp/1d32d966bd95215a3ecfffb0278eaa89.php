<?php if (!defined('THINK_PATH')) exit(); /*a:3:{s:30:"./tpl/default/video/lists.html";i:1514362072;s:32:"./tpl/default/common/header.html";i:1521865672;s:32:"./tpl/default/common/footer.html";i:1521865458;}*/ ?>
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

<link href="__ROOT__/tpl/default/static/css/video-sort.css" rel="stylesheet">

<div class="s-body">
    <div class="content">
        <div class="vault-top">
            <div class="sort-filter">
                <div class="sort-filter-panel">
                    <div class="item " style="display:block;"><label>分类：</label>
                        <ul id="class_box">
                            <li <?php if(empty($cid) || (($cid instanceof \think\Collection || $cid instanceof \think\Paginator ) && $cid->isEmpty())): ?>class="current" <?php endif; ?> data="0"><a href="#">全部</a></li>
                            <?php if(is_array($class_list) || $class_list instanceof \think\Collection || $class_list instanceof \think\Paginator): $i = 0; $__LIST__ = $class_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
                                <li  data="<?php echo $vo['id']; ?>"  <?php if($cid == $vo['id']): ?>class="current"<?php endif; ?>>
                                    <a href="#"  >
                                        <?php echo $vo['name']; ?>
                                    </a>
                                </li>
                            <?php endforeach; endif; else: echo "" ;endif; ?>
                        </ul>
                    </div>

                    <?php if(!(empty($class_sublist) || (($class_sublist instanceof \think\Collection || $class_sublist instanceof \think\Paginator ) && $class_sublist->isEmpty()))): ?>
                    <div class="item" style="display:block;"><label>子分类：</label>
                        <ul id="sub_box">
                            <li <?php if(empty($sub_cid) || (($sub_cid instanceof \think\Collection || $sub_cid instanceof \think\Paginator ) && $sub_cid->isEmpty())): ?>class="current" <?php endif; ?> data="0"><a href="#">全部</a></li>
                            <?php if(is_array($class_sublist) || $class_sublist instanceof \think\Collection || $class_sublist instanceof \think\Paginator): $i = 0; $__LIST__ = $class_sublist;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
                            <li  data="<?php echo $vo['id']; ?>"  <?php if($sub_cid == $vo['id']): ?>class="current"<?php endif; ?>>
                                <a href="#"  >
                                    <?php echo $vo['name']; ?>
                                </a>
                            </li>
                            <?php endforeach; endif; else: echo "" ;endif; ?>
                        </ul>
                    </div>
                    <?php endif; ?>

                    <div class="item"  style="display:block;"><label>标签：</label>
                        <ul id="tag_box">
                            <li <?php if(empty($tag_id) || (($tag_id instanceof \think\Collection || $tag_id instanceof \think\Paginator ) && $tag_id->isEmpty())): ?>class="current" <?php endif; ?> data="0"><a href="#">全部</a></li>
                            <?php if(is_array($tag_list) || $tag_list instanceof \think\Collection || $tag_list instanceof \think\Paginator): $i = 0; $__LIST__ = $tag_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
                                <li data="<?php echo $vo['id']; ?>"  <?php if($tag_id == $vo['id']): ?>class="current"<?php endif; ?>>
                                    <a href="#" ><?php echo $vo['name']; ?></a>
                                </li>
                            <?php endforeach; endif; else: echo "" ;endif; ?>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="vault-main">
            <div class="sort-title">

                <div class="sort-label">排序：</div>
                <div class="sort-item">
                    <select id="orderCode" name="orderCode">
                        <!--
                        <option value="<?php echo url('video/lists',array('orderCode'=>'lastTime')); ?>" <?php if($orderCode == 'lastTime'): ?>selected="selected"<?php endif; ?>>最新视频</option>
                        <option value="<?php echo url('video/lists',array('orderCode'=>'hot')); ?>" <?php if($orderCode == 'hot'): ?>selected="selected"<?php endif; ?>>最热视频</option>
                        <option value="<?php echo url('video/lists',array('orderCode'=>'reco')); ?>" <?php if($orderCode == 'reco'): ?>selected="selected"<?php endif; ?>>推荐视频</option>-->
                        <option value="lastTime" <?php if($orderCode == 'lastTime'): ?>selected="selected"<?php endif; ?>>最新视频</option>
                        <option value="hot" <?php if($orderCode == 'hot'): ?>selected="selected"<?php endif; ?>>最热视频</option>
                        <option value="reco" <?php if($orderCode == 'reco'): ?>selected="selected"<?php endif; ?>>推荐视频</option>
                    </select>
                </div>
                <div class="sort-stat">共<b><?php echo $count; ?></b>个筛选结果</div>

            </div>
            <div class="sort-box">
                <ul class="panel">
                    <?php if(!(empty($video_list) || (($video_list instanceof \think\Collection || $video_list instanceof \think\Paginator ) && $video_list->isEmpty()))): if(is_array($video_list) || $video_list instanceof \think\Collection || $video_list instanceof \think\Paginator): $i = 0; $__LIST__ = $video_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
                    <li class="sort-cel">
                        <a href="<?php echo url('video/play',array('id'=>$vo['id'])); ?>" class="sort-pack">
                            <img src="<?php echo $vo['thumbnail']; ?>"/>
                            <p><?php echo $vo['play_time']; ?></p>
                            <?php if($vo['reco'] > '0'): ?>
                                <i class="straw"></i>
                            <?php endif; if($vo['type'] == 1): ?><span class="btn fn-duoji"></span><?php endif; ?>
                            <div class="play-bg"><span><i></i></span></div>
                        </a>
                        <ul class="info-list">
                            <li class="title">
                                <a href="javascript:void(0)" title="<?php echo $vo['title']; ?>"><?php echo $vo['title']; ?></a>
                            </li>
                            <li>
                                <var><i class="btn fn-time"></i><?php echo date('Y/m/d',$vo['update_time']); ?></var>

                                <span><i class="btn fn-jinbi1"></i><?php echo $vo['gold']; ?></span>
                                <i class="btn fn-see"></i><?php echo $vo['click']; ?>
                            </li>
                            <style>

                            </style>
                        </ul>
                    </li>
                    <?php endforeach; endif; else: echo "" ;endif; else: ?>
                    <div class="not-comment not">暂时没有数据 ~</div>
                    <?php endif; ?>
                </ul>
            </div>
            <div class="sort-pager">
            <?php echo $pages; ?>
            </div>

        </div>
    </div>
    <form action="" method="get" id="forms">
        <input type="hidden" id="current_orderCodes"  name="orderCode" value="<?php echo (isset($orderCode) && ($orderCode !== '')?$orderCode:'0'); ?>" >
        <input type="hidden" id="current_tag_id" name="tag_id"  value="<?php echo (isset($tag_id) && ($tag_id !== '')?$tag_id:'0'); ?>" >
        <input type="hidden" id="current_sub_cid" name="sub_cid"  value="<?php echo (isset($sub_cid) && ($sub_cid !== '')?$sub_cid:'0'); ?>" >
        <input type="hidden" id="current_cid" name="cid"  value="<?php echo (isset($cid) && ($cid !== '')?$cid:'0'); ?>" >
    </form>
</div>

<script type="text/javascript">
    $(function () {
        $('#orderCode').change(function(){
            $('#current_orderCodes').val($(this).val());
            $('#forms').submit();
        })

        $('#sub_box').find('li').click(function(){
            var sub = $(this).attr('data');
            $('#current_sub_cid').val(sub);
            $('#forms').submit();
        })

        $('#class_box').find('li').click(function(){
            var cid = $(this).attr('data');
            $('#current_cid').val(cid);
            $('#current_sub_cid').val(0);
            $('#forms').submit();
        })

        $('#tag_box').find('li').click(function(){
            var tag_id = $(this).attr('data');
            $('#current_tag_id').val(tag_id);
            $('#forms').submit();
        })

        $(".sort-pack").hover(function(){
            $(this).find(".play-bg").show();
            $(this).find("span").addClass("cur");
        },function(){
            $(this).find(".play-bg").hide();
            $(this).find("span").removeClass("cur");
        });
    });

</script>
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
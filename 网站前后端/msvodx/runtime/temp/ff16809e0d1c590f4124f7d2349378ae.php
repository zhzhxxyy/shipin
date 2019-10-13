<?php if (!defined('THINK_PATH')) exit(); /*a:3:{s:29:"./tpl/default/video/play.html";i:1567675330;s:32:"./tpl/default/common/header.html";i:1521865672;s:32:"./tpl/default/common/footer.html";i:1521865458;}*/ ?>
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

<link href="__ROOT__/tpl/default/static/css/play.css" rel="stylesheet">
<script type="text/javascript" src="/static/ckplayer/ckplayer.js"></script>

<style>
    .layui-layer-hui{
        background-color: #fff!important;
        color:#000;
    }
    .layui-layer-hui .layui-layer-content{
        font-size:16px!important;
        text-align: left!important;
    }

    .important{
        font-weight: bold;
        color:#843534;
    }
</style>
<script src="/static/msvodx/js/video.js"></script>
<script type="text/javascript">

    var playRequestUrl='<?php echo url("api/payVideo"); ?>';    //*必需配置项
    var player,timer;
    var page = 0;
    var addLiIndex=1;
    var trySeeTime=10;

    function adjump(){
        var canJumpAd="<?php echo $adSetting['skip_ad_on']; ?>";
        if(canJumpAd==1){
            player.videoPlay();
        }else{
            layer.msg('您不能跳过广告',{icon:2,time:2000});
        }
    }


    function getCommentList(){
        var nowDate = new Date().getTime();
        var times = "";
        var url = "<?php echo url('api/getCommentList'); ?>";
        var  resourceId = " <?php echo $videoInfo['id']; ?>";
        var data = {
            "page":page,
            "resourceId":resourceId,
            "resourceType":1,
        };
        var html = '';
        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            dataType: 'json',
            success: function(data){
                $('#comment_num').html("("+data.data.count+")");
                var datas = data.data.list;
                if(datas==undefined) return false;
                page++;
                datas.forEach(function(item) {
                    var headimgurl = '/static/images/user_dafault_headimg.jpg';
                    if(item.headimgurl){
                        headimgurl = item.headimgurl;
                    }
                    var time = parseInt(item.last_time*1000);
                    if(parseInt(time+60*30*1000) > nowDate){
                        times = '刚刚';
                    }else if(parseInt(time+60*60*1000) > nowDate){
                        times = '半个小时前';
                    } else if(parseInt(time+2*60*60*1000) > nowDate){
                        times = '1小时前';
                    }else{
                        var oDate = new Date(time);
                        var Hours = oDate.getHours()>10 ? oDate.getHours() :  '0'+oDate.getHours();
                        var Minutes = oDate.getMinutes()>10 ? oDate.getMinutes() :  '0'+oDate.getMinutes();
                        times = oDate.getFullYear()+'-'+(oDate.getMonth()+1)+'-'+oDate.getDate()+' '+Hours+':'+Minutes;
                    }
                    html += '<li>';
                    html += '   <div class="user-avatar">';
                    html += '       <a href="javascript:">';
                    html += '           <img src="'+headimgurl+'">';
                    html += '       </a>';
                    html += '    </div>';
                    html += '    <div class="comment-section">';
                    html += '   <div class="user-info">';
                    html += '       <a href="javascript:" class="user-name">'+item.nickname+'</a>';
                    html += '       <span class="comment-timestamp">'+times+'</span>';
                    html += '   </div>';
                    html += '   <div class="comment-text">'+item.content+'</div>';
                    html += '   </div>';
                    html += ' </li>';
                })
                if(data.data.isMore == 1){
                    $('#more-comment').show();
                }else{
                    $('#more-comment').hide();
                }
                $('#not-comment').remove();
                $('#comment-list').append(html);
            }
        });
    }


    function loadedHandler(){
        if(player.getMetaDate()){
            player.addListener('pause', pauseHandler);
            player.addListener('play', playHandler);
        }
    }

    function pauseHandler(){
        console.log('pause');
        clearInterval(timer);
        if(trySeeTime>0)layer.msg('试看计时暂停',{icon:6,time:1000});
    }

    function playHandler(){
        layer.msg('试看计时开始……',{icon:6,time:1000});
        timer=setInterval(checkTrySeeTime,1000);
    }

    function checkTrySeeTime(){
        if(trySeeTime<=0){
            layer.msg('很抱歉，试看时间到.',{icon:2,time:1000});
            //setTimeout("videoPlayInit(<?php echo $videoInfo['id']; ?>)",2000);
            setTimeout("location.href=\"<?php echo url('index/prompt',['id'=>$videoInfo['id']]); ?>\"",1500);
            player.videoPause();
        }else{
            trySeeTime--;
            console.log(trySeeTime);
        }
    }

    function createPlayer(playParams,isTrySee){
        //console.log(playParams);
        if(isTrySee==undefined) isTrySee=false;
        if(layer!=undefined) layer.closeAll();
        var vodUrl=(playParams==undefined)?'#':playParams.videoInfo.url;
        var params={
            container: '#playerBox',
            variable: 'player',
            poster:'<?php echo $videoInfo["thumbnail"]; ?>',
            video: vodUrl,
            //loaded:'loadedHandler',
            autoplay:false,
            flashplayer:false
        };


        if(playParams!=undefined){
            $.ajax({
                url:playRequestUrl,
                type:'POST',
                dataType:'JSON',
                data:{id:playParams.videoInfo.id,surePlay:1,isTrySee:isTrySee},
                async:false,
                success:function(resp){
                    //console.log(resp);
                    if(resp.resultCode!=0){
                        layer.msg('您不能观看此影片！',{time:2000,icon:2});
                        return false;
                    }
                    params.video=resp.data.videoInfo.url;

                    if(resp.data.freeType==2 && resp.data.videoInfo.gold>0 && isTrySee){
                        //按时长试看
                        trySeeTime=resp.data.freeNum;
                        console.log('begion try see:'+trySeeTime+'s');
                        params.loaded="loadedHandler";
                    }

                },
                error:function(){
                    layer.msg('影片信息加载失败！',{time:2000,icon:2});
                    return false;
                }
            });
        }

        <?php if($adSetting['ad_on']==1): ?>
        params.adfront='<?php echo $adSetting["pre_ad"]; ?>';
        params.adfrontlink='<?php echo $adSetting["pre_ad_url"]; ?>';
        params.adfronttime='<?php echo $adSetting["play_video_ad_time"]; ?>';

        params.adpause='<?php echo $adSetting["suspend_ad"]; ?>';
        params.adpauselink='<?php echo $adSetting["suspend_ad_url"]; ?>';
        params.adpausetime='<?php echo $adSetting["play_video_ad_time"]; ?>';
        <?php endif; ?>

        params.skipButtonShow=false;
        player = new ckplayer(params);
        if(isTrySee) setTimeout("player.changeControlBarShow(false)",1000); //hide control
        if(playParams!=undefined){
            setTimeout("player.videoPlay()",1000);//play
        }

    }


    $(function(){
        getCommentList();
        getGift();
        createPlayer(null,null);
        videoPlayInit(<?php echo $videoInfo['id']; ?>);

        //点赞
        <?php if($isGooded==false): ?>
        $("#giveGoodBtn").on('click',function(){
            var reqData={resourceType:'video',resourceId:<?php echo $videoInfo['id']; ?>};
            $.post('<?php echo url("api/giveGood"); ?>',reqData,function(data){
                //console.log(data);
                if(data.resultCode==0){
                    $('#goodNum').html(data.data.good);
                    $('#giveGoodBtn').addClass("isSelected");

                    layer.msg('点赞成功',{time:1000,icon:1});
                }else{
                    layer.msg('点赞失败，原因：'+data.error,{time:1000,icon:2});
                }
            },'JSON');
        });
        <?php endif; ?>

        //收藏
        <?php if(!$isCollected): ?>
        $(".fn-shoucang1").on("click",function () {
            var collectData={type:'1',id:'<?php echo $videoInfo["id"]; ?>'};
            $.post('<?php echo url("api/colletion"); ?>',collectData,function (data) {
                if(data.resultCode==0){
                    $('#shoucang').html('已收藏');
                    $('.fn-shoucang1').addClass("isSelected");
                    layer.msg('收藏成功',{time:1000,icon:1});
                }else{
                    layer.msg('收藏失败，原因：'+data.error,{time:1000,icon:2});
                }
            },'json');
        });
        <?php endif; ?>

        $("#comment_content").on("keyup",function () {
            var length = $("#comment_content").val().length;
            $("#length").html(length);
            if(length > 300){
                var text = $("#comment_content").val().substring(0,300);
                $("#comment_content").val(text);
            }

        });

        //评论
        $("#submit_comment").on("click",function () {
            var content =  $.trim($("#comment_content").val());
            if((content == "" || content == undefined || content == null)) {
                layer.msg('请输入评论的内容!');
                return false;
            }
            var reqData={resourceType:'1',resourceId:<?php echo $videoInfo['id']; ?>,content:content};
            $.post('<?php echo url("api/comment"); ?>',reqData,function(data){
                if(data.resultCode==0){
                    layer.msg(data.message,{time:1000,icon:1});
                    $("#comment_content").val('');
                    $("#length").html(0);
                    if(data.data.comment_examine_on != 1){
                        var headimgurl = '/static/images/user_dafault_headimg.jpg';
                        if(data.data.userinfo.headimgurl){
                            headimgurl = data.data.userinfo.headimgurl;
                        }
                        var html = '';
                        html += '<li id="addLi_'+addLiIndex+'">';
                        html += '   <div class="user-avatar">';
                        html += '       <a href="javascript:">';
                        html += '           <img src="'+headimgurl+'">';
                        html += '       </a>';
                        html += '    </div>';
                        html += '    <div class="comment-section">';
                        html += '   <div class="user-info">';
                        html += '       <a href="javascript:" class="user-name">'+data.data.userinfo.nickname+'</a>';
                        html += '       <span class="comment-timestamp">刚刚</span>';
                        html += '   </div>';
                        html += '   <div class="comment-text">'+data.data.content+'</div>';
                        html += '   </div>';
                        html += ' </li>';
                        $('#not-comment').remove();
                        $("#comment-list ").prepend(html);
                        $("#addLi_"+addLiIndex).hide().slideDown('fast');
                        addLiIndex++;
                    }
                }else{
                    layer.msg('评论失败，原因：'+data.error,{time:2000,icon:2});
                }
            },'JSON');
        });
    });


    function getGift(){
        var url = "<?php echo url('Api/getGift'); ?>";
        var data = {
            "func": "getNameAndTime" ,
        };
        var html = '';
        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            dataType: 'json',
            success: function(data){
                data.forEach(function(item) {
                    html += '<div class="r-cel" title="'+item.name+'">' ;
                    html += '  <img src="'+item.images+'"/>' ;
                    html += '       <span>'+item.name+'</span>' ;
                    html += '       <p>'+item.price+'金币</p>' ;
                    html += '        <a onclick="reward('+item.id+','+item.price+',<?php echo $videoInfo['id']; ?>,1)">打赏</a>' ;
                    html += ' </div>' ;
                })
                $('#gift_box').html(html);
            }
        });
    }

</script>
<div class="s-body">
    <!---->
    <div class="content">
        <!--视频标题和广告-->
        <div class="base">
            <div class="base-info">
                <span class="title" title="<?php echo $videoInfo['title']; ?>"><?php echo $videoInfo['title']; ?></span>
            </div>
            <div class="base-area">广告</div>
        </div>
        <!--视频和选集、打赏-->
        <div class="spv-player">
            <div class="play-box" id="playerBox">
                <img src="<?php echo $videoInfo['thumbnail']; ?>" width="100%" height="100%" />
            </div>
            <div class="list-box">
                <div class="tab">
                    <span data-for="reward" class="cur">打赏</span>
                    <?php if(isset($videoSet)): ?>
                    <span data-for="select" id="videoSetLabel">选集</span>
                    <script>
                        setTimeout("$('#videoSetLabel').click()",500);
                    </script>
                    <?php endif; ?>
                </div>
                <div class="sub-tab">
                    <!--打赏-->
                    <div class="reward" style="display: block;">
                        <div class="reward-box" style="overflow:hidden;">
                            <div class="reward-content" id="gift_box">
                            </div>
                        </div>
                        <p class="text">该视频被<var id="count"><?php echo $count; ?></var>位网友打赏过,总额为<var id='countprice'><?php echo $count_price; ?></var>元</p>
                    </div>
                    <!--选集-->
                    <?php if(isset($videoSet)): ?>
                    <div class="select" >
                        <ul>
                            <?php if(is_array($videoSet) || $videoSet instanceof \think\Collection || $videoSet instanceof \think\Paginator): $i = 0; $__LIST__ = $videoSet;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$video): $mod = ($i % 2 );++$i;?>
                            <li <?php if($video['id'] == $videoInfo['id']): ?>class="curlPlayVideoLi"<?php endif; ?>>
                                <a href="<?php echo url('video/play',array('id'=>$video['id'])); ?>" _stat="video-list-column:click">
                                    <div class="pic" style="background:#1f1f1f;border:1px solid #535351;">
                                        <img src="<?php echo $video['thumbnail']; ?>" />
                                        <!--<span><?php echo $video['play_time']; ?></span>-->
                                    </div>
                                    <p><?php echo $video['title']; ?></p>
                                    <p class="content-like">
                                        <span class="mod-like"><i class="btn fn-see"></i><?php echo $video['click']; ?></span>
                                        <span class="mod-like" style="float: right;"><i class="btn fn-zan2"></i><?php echo (isset($video['good']) && ($video['good'] !== '')?$video['good']:"0"); ?></span>
                                    </p>
                                </a>
                            </li>
                            <?php endforeach; endif; else: echo "" ;endif; ?>
                        </ul>
                    </div>
                    <?php endif; ?>
                </div>

            </div>
            <div class="interact">
                <div class="share">
                    <div class="bdsharebuttonbox">
                        <a href="#" class="bds_more" data-cmd="more"></a>
                        <a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a>
                        <a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a>
                        <a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a>
                        <a href="#" class="bds_renren" data-cmd="renren" title="分享到人人网"></a>
                        <a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a>
                    </div>
                    <script>window._bd_share_config = {
                        "common": {
                            "bdSnsKey": {},
                            "bdText": "",
                            "bdMini": "2",
                            "bdPic": "",
                            "bdStyle": "0",
                            "bdSize": "16"
                        }, "share": {}
                    };
                    with (document) 0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)];</script>
                    <div class="fn-updown">
                        <span><i class="btn fn-shoucang1 <?php if($isCollected): ?>isSelected<?php endif; ?>"></i><var id="shoucang"><?php if($isCollected): ?>已收藏<?php else: ?>收藏<?php endif; ?></var></span>
                        <span><i class="btn fn-zan2 <?php if($isGooded): ?>isSelected<?php endif; ?>" id="giveGoodBtn"></i><var id="goodNum"><?php echo $videoInfo['good']; ?></var></span>
                    </div>
                </div>
                <div class="interact-box">
                    <ul>
                        <li>
                            <div><img src="<?php echo (isset($videoInfo['headimgurl']) && ($videoInfo['headimgurl'] !== '')?$videoInfo['headimgurl']:''); ?>" /></div>
                            <p><?php echo (isset($videoInfo['member']) && ($videoInfo['member'] !== '')?$videoInfo['member']:''); ?></p><br>
                            <p><span class="btn fn-time"></span><?php echo date('Y-m-d', $videoInfo['add_time']); ?></p>
                        </li>
                        <li><i></i></li>
                        <li>分类：
                            <a href="/video/lists.html?cid=<?php echo (isset($videoInfo['classid']) && ($videoInfo['classid'] !== '')?$videoInfo['classid']:''); ?>" class="label"><?php echo (isset($videoInfo['classname']) && ($videoInfo['classname'] !== '')?$videoInfo['classname']:''); ?></a>
                        </li>
                        <li><i></i></li>
                        <li>标签：
                            <?php if(is_array((isset($videoInfo['taglist']) && ($videoInfo['taglist'] !== '')?$videoInfo['taglist']:'')) || (isset($videoInfo['taglist']) && ($videoInfo['taglist'] !== '')?$videoInfo['taglist']:'') instanceof \think\Collection || (isset($videoInfo['taglist']) && ($videoInfo['taglist'] !== '')?$videoInfo['taglist']:'') instanceof \think\Paginator): $i = 0; $__LIST__ = (isset($videoInfo['taglist']) && ($videoInfo['taglist'] !== '')?$videoInfo['taglist']:'');if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$v): $mod = ($i % 2 );++$i;?>
                            <a  href="/video/lists.html?tag_id=<?php echo $v['id']; ?>" class="tag"><?php echo (isset($v['name']) && ($v['name'] !== '')?$v['name']:''); ?></a>
                            <?php endforeach; endif; else: echo "" ;endif; ?>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <!--评论和推荐-->
        <div class="spv-comment comment">
            <!--评论和展示-->
            <div class="comment-left">
                <!--评论-->
                <div class="area-form">
                    <div class="comment-form">
                        <div class="form-cell">
                            <?php if(session('member_id') <= 0): ?>
                            <div class="form-user-info">
                                <a href="javascript:;" class="form-user-login avatar">登录</a>
                                <span>|</span>
                                <a href="" target="_blank">注册</a>
                            </div>
                            <?php endif; ?>
                            <div class="form-wordlimit">
                                <span class="form-wordlimit-input input-count" id="length">0</span>
                                <span class="form-wordlimit-upper">/300</span>
                            </div>
                            <div class="form-textarea form-textarea-picdom">
                            <textarea data-maxlength="300" name="content"
                                      placeholder="看点槽点，不吐不快！别憋着，马上大声说出来吧！"  id="comment_content" ></textarea>
                            </div>
                            <div class="form-toolbar">
                                <div class="form-tool form-action">
                                    <a href="javascript:;" class="form-btn-submit" id="submit_comment">发表评论</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--展示-->
                <div class="area-box">
                    <div class="comment-tab">
                        <span class="comment-tab-left">全部评论<em class="num" id="comment_num">(0)</em></span>
                    </div>
                    <div class="comment-list" >
                        <ul id="comment-list">
                            <li id='not-comment'>暂没评论~</li>
                        </ul>
                        <div id="more-comment"><span onclick="getCommentList()">更多<i class="btn fn-more"></i></span></div>
                    </div>
                </div>
            </div>
            <!--相关推荐-->
            <div class="comment-right">
                <div class="sub-tab">
                    <p class="title">相关推荐</p>
                    <div class="select" style="display: block;">
                        <ul>
                            <?php if(is_array($recom_list) || $recom_list instanceof \think\Collection || $recom_list instanceof \think\Paginator): $i = 0; $__LIST__ = $recom_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
                            <li>
                                <a href="<?php echo url('video/play',array('id'=>$vo['id'])); ?>">
                                    <div class="pic">
                                        <img src="<?php echo $vo['thumbnail']; ?>">
                                        <!--<span><?php echo $vo['play_time']; ?></span>-->
                                    </div>
                                    <p><?php echo $vo['title']; ?></p>
                                    <p class="content-like">
                                        <span class="mod-like"><i class="btn fn-see"></i><?php echo $vo['click']; ?></span>
                                        <span class="mod-like" style="float: right;"><i class="btn fn-zan2"></i><?php echo $vo['good']; ?></span>
                                    </p>
                                </a>
                            </li>
                            <?php endforeach; endif; else: echo "" ;endif; ?>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

<script>
    $(function(){
        $(".list-box .tab span").click(function(){
            var $self = $(this);
            $self.addClass("cur").siblings().removeClass("cur");
            var $attr = $self.attr("data-for");
            $(".list-box .sub-tab>div").hide();
            $(".list-box .sub-tab").find("."+ $attr).show();
        });

       $(".select1").on("click","a",function(){
           $(".select1 a").removeClass("cur");
           $(this).addClass("cur");
       });

    });
    function check_logins(){
        $.post('/api/get_login_status','{}',function (e) {
                if(e.resultCode == 3){
                    layer.msg('该账号已在其他地方登陆',
                        {
                            icon: 5,
                            time: 3000,
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
                        },function () {
                            window.location.reload();
                        });
               }
        },'json');
        setTimeout('check_logins()', 5000);
    }

</script>
    <?php if($login_status['resultCode'] == 1): ?>
    <script>
        check_logins();
    </script>
    <?php endif; ?>
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
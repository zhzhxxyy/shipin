<?php if (!defined('THINK_PATH')) exit(); /*a:3:{s:39:"./tpl/default/mobile/member/member.html";i:1517473362;s:39:"./tpl/default/mobile/common/header.html";i:1520232422;s:39:"./tpl/default/mobile/common/footer.html";i:1546664214;}*/ ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <?php $menu = getMenu();?>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="telephone=no" name="format-detection">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title><?php echo (isset($page_title) && ($page_title !== '')?$page_title:""); ?>_<?php echo $seo['site_title']; ?></title>
    <meta name="keywords" lang="zh-CN" content="<?php echo $seo['site_keywords']; ?>"/>
    <meta name="description" lang="zh-CN" content="<?php echo $seo['site_description']; ?>" />
    <link rel="stylesheet" href="__ROOT__/tpl/default/mobile/static/css/swiper.min.css">
    <link rel="stylesheet" href="__ROOT__/tpl/default/mobile/static/css/common.css" />


    <script src="__ROOT__/tpl/default/mobile/static/js/jquery-3.2.1.min.js"></script>
    <script src="__ROOT__/tpl/default/mobile/static/js/swiper.min.js"></script>
    <script type="text/javascript" src="__ROOT__/tpl/default/mobile/static/js/layer_mobile/layer.js"></script>
    <script type="text/javascript" src="__ROOT__/tpl/default/mobile/static/js/common.js"></script>
    <link rel="stylesheet" href="__ROOT__/tpl/default/static/css/font_485358_gtgl3zs6gyvqjjor/iconfont.css">


</head>
<body>
<div>
    <header class="js-header">
        <div class="head">
            <a class="logo"><img src="<?php echo $seo['site_logo_mobile']; ?>"/></a>
            <?php $controller =  lcfirst(request()->controller());?>
            <form
                <?php switch($controller): case "images": ?> action="<?php echo url('search/index',array('type'=>'atlas')); ?>"<?php break; case "atlas": ?> action="<?php echo url('search/index',array('type'=>'atlas')); ?>"<?php break; case "novel": ?>action="<?php echo url('search/index',array('type'=>'novel')); ?>"<?php break; case "search": ?>action="<?php echo url('search/index',array('type'=>$type)); ?>"<?php break; default: ?>
                action="<?php echo url('search/index',array('type'=>'video')); ?>"
                <?php endswitch; ?>
            method="get" id="myform">
            <div class="search">
                <input class="js-placeholder" placeholder="请输入" type="search" value='<?php if(isset($key_word)): ?><?php echo $key_word; endif; ?>' name="key_word"><i class="btn fn-sousuo" onclick="$('#myform').submit();"></i>
            </div>
            </form>

            <div class="menu"><span class="btn fn-sort"></span></div>
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
    <div class="content">
<link rel="stylesheet" href="__ROOT__/tpl/default/mobile/static/css/member.css">
<style>
    .choice a{display: block;margin: 10px 5px;border:1px solid #ddd;padding: 7px 0;border-radius: 32px;width: 80%;margin: 10px auto;}
    .layui-m-layercont{padding: 30px;}
    .layui-m-layer0 .layui-m-layerchild{width: 70%;background: rgba(255,255,255,.9);}
</style>
<script>
    function sign(){
        var url = "<?php echo url('api/sign'); ?>";
        $.post(url, {}, function (data) {
            if (data.resultCode == 0) {
                $('.sign').find('var').html('+'+data.data['value']);
                $('.sign').addClass("signs");
                $('.sign').addClass("Completion");
                layer.open({skin:'msg',content:'签到成功',time:2, end:function(){
                    $('.sign').removeClass("signs");
                }});
            }else{
                layer.open({skin:'msg',content:data.error,time:2});
            }
        }, 'JSON');
    }
</script>
    <div>
        <div class="info-box">
            <img class="pic-info" src="<?php echo $info['headimgurl']; ?>" />
            <div class="info-d">
                <span class="name"><?php echo $info['nickname']; ?></span>

                <span>
                    <a href="<?php echo url('member/record_gold'); ?>" style="display: inline-block;color:#fff;margin-right: 10px;"><i class="btn fn-jinbi2 vip"></i><?php echo $info['money']; ?></a>
                    <?php if($info['is_permanent'] == 1): ?>
                            <i class="btn fn-vip1 vip"></i> 永久vip
                   <?php else: if($info['out_time'] > time()): ?>
                    <i class="btn fn-vip1 vip"></i> <?php echo safe_date('Y/m/d',$info['out_time']); ?> 截止
                    <?php else: if(empty($info['out_time'])): ?>
                            <i class="btn fn-vip1"></i>您还不是VIP
                         <?php else: ?>
                            <i class="btn fn-vip1"></i>已过期
                         <?php endif; endif; endif; ?>
                </span>
            </div>
            <div class="foot-bg"></div>
        </div>
        <div class="M-member">
           <!-- <div class="m-box money info-d">
                &lt;!&ndash;<a href="<?php echo url('member/record_gold'); ?>" style="display: inline-block;"><i class="btn fn-jinbi2 vip"></i><?php echo $info['money']; ?></a>&ndash;&gt;
                <div><a>卡密充值</a></div>
            </div>-->
            <div class="m-box info-d" style="width: 30%;">
                <div><a href="<?php echo url('member/card_pwd'); ?>">卡密充值</a></div>
            </div>
            <div class="m-box info-d">
                <div>
                    <a href="<?php echo url('systemPay/recharge'); ?>">充值金币/VIP</a>
                </div>
            </div>
            <div class="m-box ">
                <a  <?php if(isSign() != '1'): ?>class="sign"   <?php else: ?> class="sign Completion" <?php endif; ?>  onclick="sign()">签到 <var>+2</var></a>
            </div>

        </div>
        <div class="M-left">
            <ul>
                <li><a href="<?php echo url('member/video'); ?>"><i class="btn fn-video"></i><br />我的视频</a></li>
                <li><a href="<?php echo url('member/novel'); ?>"><i class="btn fn-novel"></i><br />我的资讯</a></li>
                <li><a href="<?php echo url('member/img'); ?>"><i class="btn fn-img"></i><br />我的图片</a></li>
                <li><a href="<?php echo url('member/collection_video'); ?>"><i class="btn fn-info"></i><br />我的收藏</a></li>
                <li class="withdrawals"><a><i class="btn fn-tixian"></i><br />提现管理</a></li>
                <li><a href="<?php echo url('member/record_pay'); ?>"><i class="btn fn-3"></i><br />充值记录</a></li>
                <li><a href="<?php echo url('member/record_video'); ?>"><i class="btn fn-icon12"></i><br />消费记录</a></li>
                <li><a href="<?php echo url('member/agent'); ?>"><i class="btn fn-management"></i><br />代理商</a></li>
                <li class="manage"><a><i class="btn fn-info-mage"></i><br />信息管理</a></li>
                <!--<li><a href="<?php echo url('member/card_pwd'); ?>"><i class="btn fn-qiaquan"></i><br />卡密充值</a></li>-->
            </ul>
        </div>
        <div class="logout">
            <i class="btn fn-tuichu"></i>
            <a onclick="logout()" >退出登陆</a>
        </div>
    </div>
    <?php  $register_validate = empty(get_config('register_validate')) ? 0 : get_config('register_validate');?>
<script>
    function logout(){
        var url="<?php echo url('api/logout'); ?>";
        $.post(url,{},function(){
            layer.open({time: 2,skin:'msg',content: '退出成功',end:function(){
                location.href="/";
            }});
        },'JSON');
    }

    $(function () {
      var register_validate =  "<?php echo $register_validate; ?>";
       $(".M-left li.manage").click(function(){
           var info = "<?php echo url('member/info'); ?>";
           var email = "<?php echo url('member/set_email'); ?>";
           var phone = "<?php echo url('member/set_phone'); ?>";
           var pwd = "<?php echo url('member/set_pwd'); ?>";
           var gold = "<?php echo url('member/record_gold'); ?>";
           var content = '';
           content +='<div class="choice"><a href="'+info+'">个人信息</a>';
           if(register_validate == 1){
               content +='<a href="'+email+'">更换邮箱</a>';
           }
           if(register_validate == 2){
               content +='<a href="'+phone+'">更换手机</a>';
           }
           content +='<a href="'+pwd+'">修改密码</a>';
           content +='<a href="'+gold+'">金币记录</a></div>';
           layer.open({
               content: content
           });
       });
       /*提现*/
        $(".M-left li.withdrawals").click(function(){
            var add = "<?php echo url('member/add_card'); ?>";
            var operate = "<?php echo url('member/get_out_pay'); ?>";
            var record = "<?php echo url('member/record_out_pay'); ?>";
            layer.open({
                content: '<div class="choice"><a href="'+operate+'">提现</a><a href="'+record+'">提现记录</a><a href="'+add+'">新增账户</a></div>'
            });
        });


    });
</script>

</div>
<footer>
    <a class="navFooter" target="_self" href="/"><i class="btn fn-shouye"></i>首页</a>
    <a class="navFooter active" target="_self" href="<?php echo url('video/lists'); ?>"><i class="btn fn-sort"></i>分类</a>
    <a class="navFooter" target="_self" href="<?php echo url('Share/Index'); ?>"><i class="btn fn-xuanchuan"></i>宣传</a>
    <a class="navFooter" target="_self" href="<?php echo url('member/member'); ?>"><i class="btn fn-wode"></i>我的</a>
</footer>
<a class="btn-gotop" id="btn-gotop"><i class="btn fn-top"></i></a>
</div>
<script type="text/javascript">
    $(function(){
        //to and footer nav setting
        var navTopTitle="<?php echo (isset($navTopTitle) && ($navTopTitle !== '')?$navTopTitle:'视频站'); ?>";
        $('#navTopTitle').html(navTopTitle);
        $('.navFooter').removeClass('active');
        $('.navFooter:nth-child(<?php echo (isset($curFooterNavIndex) && ($curFooterNavIndex !== '')?$curFooterNavIndex:2); ?>)').addClass('active');

        $(window).scroll(function () {
            if ($(window).scrollTop() > 100) {
                $("#btn-gotop").fadeIn(500);
            }else {
                $("#btn-gotop").fadeOut(500);
            }
        });
        //当点击跳转链接后，回到页面顶部位置
        $("#btn-gotop").click(function () {
            $('body,html').animate({ scrollTop: 0 }, 1000); //1000代表1秒时间回到顶点
            return false;
        });

    });
</script>
<style>
    .btn-gotop{display: none;position: fixed;bottom: 150px;right: 10px;background: rgba(0,0,0,.5);width: 50px;height:50px;border-radius: 5px;z-index: 99;text-align: center;line-height: 50px;color:#fff;}
    .btn-gotop:hover{color:#fff;}
    .btn-gotop i{font-size: 40px;}

</style>
<?php $login_status = check_is_login();if($login_status['resultCode'] == 3): ?>
<script>
    layer.open({content:'该账号已在其他地方登陆！',time:3,skin:'msg'});
</script>
<?php endif; ?>
</body>
</html>
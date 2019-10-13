<?php if (!defined('THINK_PATH')) exit(); /*a:3:{s:37:"./tpl/default/mobile/index/index.html";i:1550131332;s:39:"./tpl/default/mobile/common/header.html";i:1520232422;s:39:"./tpl/default/mobile/common/footer.html";i:1546664214;}*/ ?>
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
<style>
    .panel{margin-top: 15px;}
</style>
        <!--滚动条-->
        <div class="swiper-container">
            <div class="swiper-wrapper">
                <?php if(is_array($banner) || $banner instanceof \think\Collection || $banner instanceof \think\Paginator): $i = 0; $__LIST__ = $banner;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$v): $mod = ($i % 2 );++$i;?>
                <a href="<?php echo $v['url']; ?>" class="swiper-slide" style="background: url(<?php echo $v['images_url']; ?>) no-repeat;background-size: cover;background-position: center;" >
                </a>
                <?php endforeach; endif; else: echo "" ;endif; ?>
            </div>
            <!-- Add Pagination -->
            <div class="swiper-pagination"></div>
        </div>
        <!---->
        <div class="panel">
            <div class="title">
                <div class="name">热点<i class="btn fn-video1"></i></div>
                <div class="column-tip"><a href="<?php echo url('video/lists',array('orderCode'=>'hot')); ?>">更多</a></div>
            </div>
            <ul class="content-list">
                <?php if(!(empty($recom_list) || (($recom_list instanceof \think\Collection || $recom_list instanceof \think\Paginator ) && $recom_list->isEmpty()))): if(is_array($recom_list) || $recom_list instanceof \think\Collection || $recom_list instanceof \think\Paginator): $i = 0; $__LIST__ = $recom_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
                <li>
                    <div class="v">
                        <div class="v-thumb">
                            <!--<div class="v-pic-default">
                                <img src="<?php echo $vo['thumbnail']; ?>">
                            </div>-->
                            <a class="v-link" href="<?php echo url('video/play',array('id'=>$vo['id'])); ?>">
                                <div class="v-pic-real" style="background-image:url('<?php echo $vo['thumbnail']; ?>');"></div>
                            </a>
                            <div class="v-tagrb"><span class="v-time"><?php echo $vo['play_time']; ?></span></div>
                        </div>
                        <div class="v-metadata">
                            <div class="v-title"><?php echo $vo['title']; ?>
                            </div>
                            <div class="v-desc">
                                <i class="btn fn-bofang" title="播放"></i>
                                <span class="v-num"><?php echo $vo['click']; ?></span>
                                <span>
                                    <i class="btn fn-jinbi1" style="margin-left: 5px;"></i>
                                    <span class="v-num"><?php echo $vo['gold']; ?></span>
                                </span>&nbsp;
                                <span style="float: right;">
                                    <i class="btn fn-zan2" title="点赞"></i>
                                    <span class="v-num"><?php echo $vo['good']; ?></span>
                                 </span>

                            </div>

                        </div>
                    </div>
                </li>
                <?php endforeach; endif; else: echo "" ;endif; else: ?>
                <div class="not-comment not">暂时没有数据 ~</div>
                <?php endif; ?>
            </ul>
        </div>
        <!--广告位-->

        <div class="adver" style="margin-bottom: 48px;">
            <script language="javascript" src="<?php echo url('/poster/index',array('pid'=>1)); ?>"></script>
        </div>

        <?php if(is_array($video_list) || $video_list instanceof \think\Collection || $video_list instanceof \think\Paginator): $i = 0; $__LIST__ = $video_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;if(!(empty($vo['list']) || (($vo['list'] instanceof \think\Collection || $vo['list'] instanceof \think\Paginator ) && $vo['list']->isEmpty()))): ?>
        <div class="panel">
            <div class="title" style="margin-bottom: 5px;">
                <div class="name"><?php echo $vo['name']; ?><i class="btn fn-video1"></i></div>
                <div class="column-tip"><a href="<?php echo url('video/lists',array('orderCode'=>'hot')); ?>">更多</a></div>
            </div>
            <ul class="content-list">
                <?php if(is_array($vo['list']) || $vo['list'] instanceof \think\Collection || $vo['list'] instanceof \think\Paginator): $i = 0;$__LIST__ = is_array($vo['list']) ? array_slice($vo['list'],0,10, true) : $vo['list']->slice(0,10, true); if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$v): $mod = ($i % 2 );++$i;?>
                <li>
                    <div class="v">
                        <div class="v-thumb">
                            <a class="v-link" href="<?php echo url('video/play',array('id'=>$v['id'])); ?>">
                                <div class="v-pic-real" style="background-image:url('<?php echo $v['thumbnail']; ?>');"></div>
                            </a>
                            <div class="v-tagrb"><span class="v-time"><?php echo $v['play_time']; ?></span></div>
                        </div>
                        <div class="v-metadata">
                            <div class="v-title">
                                <a href="#"><?php echo $v['title']; ?></a>
                            </div>
                            <div class="v-desc">
                                <i class="btn fn-bofang" title="播放"></i>
                                <span class="v-num"><?php echo $v['click']; ?></span>
                                <span style="display: inline-block;">
                                    <i class="btn fn-jinbi1" style="margin-left: 5px;"></i>
                                    <span class="v-time"><?php echo $v['gold']; ?></span>
                                </span>&nbsp;&nbsp;
                                <span style="float: right;">
                                    <i class="btn fn-zan2" title="点赞"></i>
                                    <span class="v-num"><?php echo $v['good']; ?></span>
                                </span>
                            </div>
                        </div>
                    </div>
                </li>
                <?php endforeach; endif; else: echo "" ;endif; ?>
            </ul>
        </div>
        <?php endif; endforeach; endif; else: echo "" ;endif; ?>






<script>
    var swiper = new Swiper('.swiper-container', {
        autoplay:true,
        pagination: {
            el: '.swiper-pagination',
        },
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
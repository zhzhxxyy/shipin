<?php if (!defined('THINK_PATH')) exit(); /*a:3:{s:36:"./tpl/default/mobile/video/play.html";i:1567678716;s:37:"./tpl/default/mobile/common/head.html";i:1520234274;s:39:"./tpl/default/mobile/common/footer.html";i:1546664214;}*/ ?>
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
<?php $login_status = check_is_login();?>
<link rel="stylesheet" href="__ROOT__/tpl/default/mobile/static/css/play.css">
<style type="text/css">
    .layui-m-layercont{
        text-align: left;
    }
</style>

<script type="text/javascript" src="/static/ckplayer/ckplayer.js"></script>
<script src="/static/msvodx/js/video_mobile.js"></script>

<script>
    var page = 0;
    var addLiIndex=1;
    var status =1;
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
                console.log(data);
                $('#comment_num').html("("+data.data.count+")");
                var datas = data.data.list;
                if(datas){
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
                        html += '<div class="user-avatar">';
                        html += '    <a href="javascript:">';
                        html += '    <img src="'+headimgurl+'">';
                        html += '    </a>';
                        html += '    </div>';
                        html += '    <div class="comment-section">';
                        html += '    <div class="user-info">';
                        html += '    <a href="javascript:" class="user-name">'+item.nickname+'</a>';
                        html += '    <span class="comment-timestamp">'+times+'</span>';
                        html += '</div>';
                        html += '<div class="comment-text">'+item.content+'</div>';
                        html += '</div>';
                        html += '</li>';

                    })
                    $('#not-comment').remove();
                    $('#comment-list').append(html);
                    status =1;
                }
            }
        });
    }
    function clearComment(){
        $('.send').removeClass('submit');
        $("#length").html('0/300');
        $("#comment_content").val(' ');
    }
    function  addComment() {
        var content =  $.trim($("#comment_content").val());
        if((content == "" || content == undefined || content == null)) {
            layer.open({content:'请输入评论的内容!',skin:'msg',time:1});
            return false;
        }
        var reqData={resourceType:'1',resourceId:<?php echo $videoInfo['id']; ?>,content:content};
        $.post('<?php echo url("api/comment"); ?>',reqData,function(data){
            if(data.resultCode==0){
                layer.open({time:1,content:data.message,skin:'msg'});
                if(data.data.comment_examine_on != 1){
                    var headimgurl = '/static/images/user_dafault_headimg.jpg';
                    if(data.data.userinfo.headimgurl){
                        headimgurl = data.data.userinfo.headimgurl;
                    }
                    var html = '';
                    html += '<li id="liAdd_'+addLiIndex+'">';
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
                    $("#liAdd_"+addLiIndex).hide().slideDown('fast');
                    addLiIndex++;
                }
                clearComment();
            }else{
                layer.open({content:'评论失败，原因：'+data.error,time:1,skin:'msg'});
            }
        },'JSON');
    }
    $(function () {
        getCommentList();
        $(window).scroll(function(){
            var srollPos = $(window).scrollTop();
            if(srollPos+$(window).height() > $(document).height() - 80){
                if(status == 1){
                    status = 0;
                    getCommentList();
                }
            }
        });
        $("#comment_content").on("keyup",function () {
            var length = $("#comment_content").val().length;
            $("#length").html(length+'/300');
            if(length > 0){
                $('.send').addClass('submit');
                $('.submit').unbind();
                $('.submit').bind('click',function (){
                    addComment();
                })
            }else{
                $('.send').removeClass('submit');
            }
            if(length > 300){
                var text = $("#comment_content").val().substring(0,300);
                $("#comment_content").val(text);
            }
        });

        getGift();

    })

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
                    html+='<li>' +
                        '<a onclick="reward('+item.id+','+item.price+',<?php echo $videoInfo['id']; ?>,1)">'+
                        '       <img src="'+item.images+'">\n' +
                        '       <span>'+item.name+'</span>\n' +
                        '       <span class="money">'+item.price+'金币</span>\n' +
                        '       </a>'+
                        '  </li>';
                })
                $('#gift_box').html(html);
            }
        });
    }
</script>
<!--播放-->

    <div class="video-box" id="playerBox">
        <img src="<?php echo $videoInfo['thumbnail']; ?>" width="100%" height="100%" />
    </div>
    <div class="panel p-title">
        <p class="title"><?php echo $videoInfo['title']; ?></p>
        <span><i class="btn fn-bofang"></i><?php echo $videoInfo['click']; ?></span>&nbsp;&nbsp;
        <span><i class="btn fn-time" style="font-size: 12px;"></i><?php echo date('Y-m-d H:i:s',(int)$videoInfo['add_time']); ?></span>
        <div class="drop-down"><i class="btn fn-down-bold"></i></div>
        <div class="desc">
            <p><img title="上传者" width="30" src="<?php echo (isset($videoInfo['headimgurl']) && ($videoInfo['headimgurl'] !== '')?$videoInfo['headimgurl']:''); ?>" /><span><?php echo (isset($videoInfo['member']) && ($videoInfo['member'] !== '')?$videoInfo['member']:''); ?></span></p>
            <p><label>分类：</label><a href="/video/lists.html?cid=<?php echo (isset($videoInfo['classid']) && ($videoInfo['classid'] !== '')?$videoInfo['classid']:''); ?>"><?php echo (isset($videoInfo['classname']) && ($videoInfo['classname'] !== '')?$videoInfo['classname']:''); ?></a></p>
            <p><label>标签：</label>
                <?php if(is_array((isset($videoInfo['taglist']) && ($videoInfo['taglist'] !== '')?$videoInfo['taglist']:'')) || (isset($videoInfo['taglist']) && ($videoInfo['taglist'] !== '')?$videoInfo['taglist']:'') instanceof \think\Collection || (isset($videoInfo['taglist']) && ($videoInfo['taglist'] !== '')?$videoInfo['taglist']:'') instanceof \think\Paginator): $i = 0; $__LIST__ = (isset($videoInfo['taglist']) && ($videoInfo['taglist'] !== '')?$videoInfo['taglist']:'');if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$v): $mod = ($i % 2 );++$i;?>
                <a  href="/video/lists.html?tag_id=<?php echo $v['id']; ?>"><?php echo $v['name']; ?></a>
                <?php endforeach; endif; else: echo "" ;endif; ?>
            </p>
        </div>
    </div>
    <ul class="panel powerful">
        <li id="giveGoodBtn"><i class="btn fn-zan2 <?php if($isGooded==true): ?>gived<?php endif; ?>"></i>点赞(<var id="goodNum"><?php echo (isset($videoInfo['good']) && ($videoInfo['good'] !== '')?$videoInfo['good']:"0"); ?></var>)</li>
        <li><i class="btn fn-shoucang1 <?php if($isCollected==true): ?>gived<?php endif; ?>"></i><var id="shoucang"><?php if($isCollected==true): ?>已收藏<?php else: ?>未收藏<?php endif; ?></var></li>
        <!--<li><i class="btn fn-xiazai"></i>下载</li>-->
        <li onclick="layer.open({skin:'msg',content:'建议使用浏览器自带的分享功能 ',time:2})"><i class="btn fn-fenxiang"></i>分享</li>
    </ul>

    <div class="reward">
        <p>打赏</p>
        <ul id="gift_box">

        </ul>
    </div>

    <div class="tab">
        <?php if(isset($videoSet)): ?>
        <span data-for="drama" class="active">剧集</span>
        <?php endif; ?>
        <span data-for="discuss">评论</span>
        <span data-for="r-list">推荐</span>
    </div>
    <div class="set-box">
        <!--剧集-->
        <?php if(isset($videoSet)): ?>
        <div class="panel drama" style="display: block;">
            <ul>
                <?php if(is_array($videoSet) || $videoSet instanceof \think\Collection || $videoSet instanceof \think\Paginator): $i = 0; $__LIST__ = $videoSet;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$video): $mod = ($i % 2 );++$i;?>
                <li <?php if($video['id'] == $videoInfo['id']): ?>class="curlPlayVideoLi"<?php endif; ?>>
                    <a href="<?php echo url('video/play',array('id'=>$video['id'])); ?>">
                        <div class="img-box">
                            <img src="<?php echo $video['thumbnail']; ?>" />
                            <span><?php echo $video['play_time']; ?></span>
                        </div>
                        <div class="font-box">
                            <p><?php echo $video['title']; ?></p>
                            <span><i class="btn fn-zan2"></i><?php echo $video['good']; ?></span>
                            <span style="margin-left: 20px;"><i class="btn fn-see"></i><?php echo $video['click']; ?></span>
                        </div>
                    </a>
                </li>
                <?php endforeach; endif; else: echo "" ;endif; ?>
            </ul>
        </div>
        <?php endif; ?>
        <!--推荐-->
        <div class="panel r-list">
            <!--<p class="title">大家都在看</p>-->
            <ul>
                <?php if(is_array($recom_list) || $recom_list instanceof \think\Collection || $recom_list instanceof \think\Paginator): $i = 0; $__LIST__ = $recom_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
                <li>
                    <a href="<?php echo url('video/play',array('id'=>$vo['id'])); ?>">
                        <div class="img-box">
                            <img src="<?php echo $vo['thumbnail']; ?>">
                            <span><?php echo $vo['play_time']; ?></span>
                        </div>
                        <div class="font-box">
                            <p><?php echo $vo['title']; ?></p>
                            <span><i class="btn fn-zan2"></i><?php echo $vo['good']; ?></span>
                            <span style="margin-left: 20px;"><i class="btn fn-see"></i><?php echo $vo['click']; ?></span>
                        </div>
                    </a>
                </li>
                <?php endforeach; endif; else: echo "" ;endif; ?>
            </ul>
        </div>
        <!--评论-->
        <div class="panel discuss">
            <p class="title">评论</p>
            <!--<div class="cmt-input">快来说说你的感想吧</div>-->
            <div class="font-text">
                <img src="/tpl/default/static/images/header.png">
                <input type="text" />
                <div class="area-box">
                    <textarea id="comment_content"></textarea>
                    <span id="length">0/300</span>
                    <div class="btn-box">
                        <a class="canel" onclick="clearComment()">取消</a>
                        <a class="send">发送</a><!--class="submit"-->
                    </div>
                </div>
            </div>
            <div class="comment-list">
                <ul id="comment-list">
                    <li class='not-comment' id='not-comment'>暂没评论 ~</li>
                </ul>
            </div>
        </div>
    </div>

</div>



<script>
    function check_logins(){
        $.post('/api/get_login_status','{}',function (e) {
            if(e.resultCode == 3){
                layer.open(
                    {
                        content:'该账号已在其他地方登陆！',
                        time:3,
                        skin:'msg',
                        end: function(){
                            window.location.reload();
                        }
                    });
            }
        },'json');
        setTimeout('check_logins()', 5000);
    }
    $(function(){

        /*推荐，评论，剧集的切换*/
        $(".tab span").click(function(){
            var $t = $(this).attr("data-for");
            $(".set-box").children().hide();
            $(".set-box").find("."+$t).show();
            $(".tab span").removeClass("active");
            $(this).addClass("active");
        });

        $(".drop-down").click(function () {
            if($(this).hasClass("on")==false){
                $(".p-title .desc").stop().show();
                $(this).find(".fn-down-bold").css("transform","rotate(180deg)");
                $(this).addClass("on");
            }else{
                $(".p-title .desc").stop().hide();
                $(this).find(".fn-down-bold").css("transform","rotate(0deg)");
                $(this).removeClass("on");
            }
        });

    });


    //---------play video ---------------------------------------------------------

    var playRequestUrl='<?php echo url("api/payVideo"); ?>';    //*必需配置项
    var player,timer;
    var page = 0;
    var addLiIndex=1;
    var trySeeTime=10;
    var loginPageUrl="<?php echo url('index/login'); ?>";

    function adjump(){
        var canJumpAd="<?php echo $adSetting['skip_ad_on']; ?>";
        if(canJumpAd==1){
            player.videoPlay();
        }else{
            layer.msg('您不能跳过广告',{icon:2,time:2000});
        }
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
        if(trySeeTime>0)layer.open({content:'试看计时暂停',time:1,skin:'msg'});
    }

    function playHandler(){
        layer.open({content:'试看计时开始……',time:1,skin:'msg'});
        timer=setInterval(checkTrySeeTime,1000);
    }

    function checkTrySeeTime(){
        if(trySeeTime<=0){
            layer.open({content:'很抱歉，试看时间到.',time:1,skin:'msg'});
            //setTimeout("videoPlayInit(<?php echo $videoInfo['id']; ?>)",2000);

            setTimeout("location.reload()",1000);
            player.videoPause();
        }else{
            trySeeTime--;
            console.log(trySeeTime);
        }
    }

    function createPlayer(playParams,isTrySee){
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
            flashplayer: true,
        };


        if(playParams!=undefined){
            $.ajax({
                url:playRequestUrl,
                type:'POST',
                dataType:'JSON',
                data:{id:playParams.videoInfo.id,surePlay:1,isTrySee:isTrySee},
                async:false,
                success:function(resp){
                    if(resp.resultCode!=0){
                        layer.open({content:'您不能观看此影片！',time:1,skin:'msg'});
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
                    layer.open({content:'影片信息加载失败！',time:2,skin:'msg'});
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
            if(playParams!=undefined) setTimeout("player.videoPlay()",1000); //play
        }


        $(function(){
            createPlayer(null,null);
            videoPlayInit(<?php echo $videoInfo['id']; ?>);

            $(".tab").find('span').first().click();

            //点赞
            <?php if($isGooded==false): ?>
            $("#giveGoodBtn").on('click',function(){
                var reqData={resourceType:'video',resourceId:<?php echo $videoInfo['id']; ?>};
                $.post('<?php echo url("api/giveGood"); ?>',reqData,function(data){
                    //console.log(data);
                    if(data.resultCode==0){
                        $('#goodNum').html(data.data.good);
                        $('#giveGoodBtn').find('.fn-zan2').addClass("gived");
                        $('#giveGoodBtn').unbind('click')
                        layer.open({content:'点赞成功',skin:'msg',time:2});
                    }else{
                        if(data.error=='用户未登陆'){
                            layer.open({content:'点赞失败,请登陆后再试!',skin:'msg',time:2,end:function () {
                                location.href="<?php echo url('index/login'); ?>";
                            }});
                        }else{
                            layer.open({content:'点赞失败，原因：'+data.error,skin:'msg',time:2});
                        }
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
                            $('.fn-shoucang1').addClass("gived").unbind('click');
                            layer.open({content:'收藏成功',skin:'msg',time:2});
                        }else{
                            if(data.error=='用户未登陆'){
                                layer.open({content:'收藏失败,请登陆后再试!',skin:'msg',time:2,end:function () {
                                    location.href="<?php echo url('index/login'); ?>";
                                }});
                            }else{
                                layer.open({content:'收藏失败，原因：'+data.error,skin:'msg',time:2});
                            }
                        }
                    },'json');
                });
                <?php endif; ?>
                });

</script>
<?php if($login_status['resultCode'] == 1): ?>
<script>
    check_logins();
</script>
<?php endif; ?>
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
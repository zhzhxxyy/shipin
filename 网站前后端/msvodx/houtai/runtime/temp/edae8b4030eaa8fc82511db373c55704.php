<?php if (!defined('THINK_PATH')) exit(); /*a:4:{s:63:"/www/wwwroot/msvodx/houtai/app/admin/view/system/attachment.php";i:1551859078;s:52:"/www/wwwroot/msvodx/houtai/app/admin/view/layout.php";i:1527499864;s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/block/header.php";i:1564651146;s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/block/footer.php";i:1564651146;}*/ ?>
<?php if(input('param.hisi_iframe') || cookie('hisi_iframe') || $tab_type==='no_menu'): ?>
<!DOCTYPE html>
<html>
<head>
    <title><?php echo $_admin_menu_current['title']; ?> -  Powered by baidu版</title>
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <link rel="stylesheet" href="__ADMIN_JS__/layui/css/layui.css">
    <link rel="stylesheet" href="__ADMIN_CSS__/style.css">
    <link rel="stylesheet" href="__STATIC__/fonts/typicons/min.css">
    <link rel="stylesheet" href="__STATIC__/fonts/font-awesome/min.css">
    <script src="__ADMIN_JS__/layui/layui.js"></script>
    <script>
        var ADMIN_PATH = "<?php echo $_SERVER['SCRIPT_NAME']; ?>", LAYUI_OFFSET = 0;
        layui.config({
            base: '__ADMIN_JS__/',
            version: '<?php echo config("hisiphp.version"); ?>'
        }).use('global');
    </script>
</head>
<body>
<div style="padding:0 10px;" class="mcolor"><?php echo runhook('system_admin_tips'); ?></div>
<?php else: ?>
<!DOCTYPE html>
<html>
<head>
    <title><?php if($_admin_menu_current['url'] == 'admin/index/index'): ?>管理控制台<?php else: ?><?php echo $_admin_menu_current['title']; endif; ?> -   Powered by baidu版</title>
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <link rel="stylesheet" href="__ADMIN_JS__/layui/css/layui.css">
    <link rel="stylesheet" href="__ADMIN_CSS__/style.css">
    <link rel="stylesheet" href="__STATIC__/fonts/typicons/min.css">
    <link rel="stylesheet" href="__STATIC__/fonts/font-awesome/min.css">
    <script src="__ADMIN_JS__/layui/layui.js"></script>
    <script>
        var ADMIN_PATH = "<?php echo $_SERVER['SCRIPT_NAME']; ?>", LAYUI_OFFSET = 60;
        layui.config({
            base: '__ADMIN_JS__/',
            version: '<?php echo config("hisiphp.version"); ?>'
        }).use('global');
    </script>
</head>
<body>
<?php 
$ca = strtolower(request()->controller().'/'.request()->action());
 ?>
<div class="layui-layout layui-layout-admin">
    <div class="layui-header" style="z-index:999!important;">
        <div class="fl header-logo">管理控制台</div>
        <div class="fl header-fold"><a href="javascript:;" title="打开/关闭左侧导航" class="aicon ai-caidan" id="foldSwitch"></a></div>
        <ul class="layui-nav fl nobg main-nav">
            <?php if(is_array($_admin_menu) || $_admin_menu instanceof \think\Collection || $_admin_menu instanceof \think\Paginator): $i = 0; $__LIST__ = $_admin_menu;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;if(($_admin_menu_parents['pid'] == $vo['id'] and $ca != 'plugins/run') or ($ca == 'plugins/run' and $vo['id'] == 3)): ?>
               <li class="layui-nav-item layui-this">
                <?php else: ?>
                <li class="layui-nav-item">
                <?php endif; ?>
                <a href="javascript:;"><?php echo $vo['title']; ?></a></li>
            <?php endforeach; endif; else: echo "" ;endif; ?>
        </ul>
        <ul class="layui-nav fr nobg head-info">
            <li class="layui-nav-item">
                <a href="javascript:void(0);"><?php echo $admin_user['version']; ?>&nbsp;&nbsp;</a>
                <dl class="layui-nav-child">
                    <dd><a href="<?php echo url('admin/user/info'); ?>">个人设置</a></dd>
                    <dd><a href="<?php echo url('admin/user/iframe?val=1'); ?>" class="j-ajax" refresh="1">框架布局</a></dd>
                    <dd><a href="<?php echo url('admin/publics/logout'); ?>">退出登陆</a></dd>
                </dl>
            </li>
            <!--<li class="layui-nav-item">
                <a href="javascript:void(0);"><?php echo $languages[cookie('admin_language')]['name']; ?>&nbsp;&nbsp;</a>
                <dl class="layui-nav-child">
                    <?php if(is_array($languages) || $languages instanceof \think\Collection || $languages instanceof \think\Paginator): $i = 0; $__LIST__ = $languages;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;if($vo['pack']): ?>
                        <dd><a href="<?php echo url('admin/index/index'); ?>?lang=<?php echo $vo['code']; ?>"><?php echo $vo['name']; ?></a></dd>
                        <?php endif; endforeach; endif; else: echo "" ;endif; ?>
                    <dd><a href="<?php echo url('admin/language/index'); ?>">语言包管理</a></dd>
                </dl>
            </li>-->
            <li class="layui-nav-item"><a href="<?php echo url('admin/index/clear'); ?>" class="j-ajax">清缓存</a></li>
            <li class="layui-nav-item"><a href="javascript:void(0);" id="lockScreen">锁屏</a></li>
        </ul>
    </div>
    <div class="layui-side layui-bg-black" id="switchNav">
        <div class="layui-side-scroll">
            <?php if(is_array($_admin_menu) || $_admin_menu instanceof \think\Collection || $_admin_menu instanceof \think\Paginator): $i = 0; $__LIST__ = $_admin_menu;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$v): $mod = ($i % 2 );++$i;if(($_admin_menu_parents['pid'] == $v['id'] and $ca != 'plugins/run') or ($ca == 'plugins/run' and $v['id'] == 3)): ?>
            <ul class="layui-nav layui-nav-tree">
            <?php else: ?>
            <ul class="layui-nav layui-nav-tree" style="display:none;">
            <?php endif; if(is_array($v['childs']) || $v['childs'] instanceof \think\Collection || $v['childs'] instanceof \think\Paginator): $kk = 0; $__LIST__ = $v['childs'];if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vv): $mod = ($kk % 2 );++$kk;?>
                <li class="layui-nav-item <?php if($kk == 1): ?>layui-nav-itemed<?php endif; ?>">
                    <a href="javascript:;"><i class="<?php echo $vv['icon']; ?>"></i><?php echo $vv['title']; ?><span class="layui-nav-more"></span></a>
                    <dl class="layui-nav-child">
                        <?php if($vv['title'] == '快捷菜单'): ?>
                            <dd><a class="admin-nav-item" href="<?php echo url('admin/index/index'); ?>">后台首页</a></dd>
                            <?php if(is_array($vv['childs']) || $vv['childs'] instanceof \think\Collection || $vv['childs'] instanceof \think\Paginator): $i = 0; $__LIST__ = $vv['childs'];if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vvv): $mod = ($i % 2 );++$i;?>
                            <dd><a class="admin-nav-item" href="<?php echo url($vvv['url'].'?'.$vvv['param']); ?>"><?php echo $vvv['title']; ?></a><i data-href="<?php echo url('menu/del?ids='.$vvv['id']); ?>" class="layui-icon j-del-menu">&#xe640;</i></dd>
                            <?php endforeach; endif; else: echo "" ;endif; else: if(is_array($vv['childs']) || $vv['childs'] instanceof \think\Collection || $vv['childs'] instanceof \think\Paginator): $i = 0; $__LIST__ = $vv['childs'];if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vvv): $mod = ($i % 2 );++$i;?>
                            <dd><a class="admin-nav-item" href="<?php if(strpos('http', $vvv['url']) === false): ?><?php echo url($vvv['url'].'?'.$vvv['param']); else: ?><?php echo $vvv['url']; endif; ?>"> <?php echo $vvv['title']; ?></a></dd>
                            <?php endforeach; endif; else: echo "" ;endif; endif; ?>
                    </dl>
                </li>
                <?php endforeach; endif; else: echo "" ;endif; ?>
            </ul>
            <?php endforeach; endif; else: echo "" ;endif; ?>
        </div>
    </div>
    <div class="layui-body" id="switchBody">
        <ul class="bread-crumbs">
            <?php if(is_array($_bread_crumbs) || $_bread_crumbs instanceof \think\Collection || $_bread_crumbs instanceof \think\Paginator): $i = 0; $__LIST__ = $_bread_crumbs;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$v): $mod = ($i % 2 );++$i;if($key > 0 && $i != count($_bread_crumbs)): ?>
                    <li>></li>
                    <li><a href="<?php echo url($v['url'].'?'.$v['param']); ?>"><?php echo $v['title']; ?></a></li>
                <?php elseif($i == count($_bread_crumbs)): ?>
                    <li>></li>
                    <li><a href="javascript:void(0);"><?php echo $v['title']; ?></a></li>
                <?php else: ?>
                    <li><a href="javascript:void(0);"><?php echo $v['title']; ?></a></li>
                <?php endif; endforeach; endif; else: echo "" ;endif; ?>
            <!--<li><a href="<?php echo url('admin/menu/quick?id='.$_admin_menu_current['id']); ?>" title="添加到首页快捷菜单" class="j-ajax">[+]</a></li>-->
        </ul>
        <div style="padding:0 10px;" class="mcolor"><?php echo runhook('system_admin_tips'); ?></div>
        <div class="page-body">
<?php endif; switch($tab_type): case "1": ?>
    
        <div class="layui-tab layui-tab-card">
            <ul class="layui-tab-title">
                <?php if(is_array($tab_data['menu']) || $tab_data['menu'] instanceof \think\Collection || $tab_data['menu'] instanceof \think\Paginator): $i = 0; $__LIST__ = $tab_data['menu'];if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;if($vo['url'] == $_admin_menu_current['url'] or (url($vo['url']) == $tab_data['current'])): ?>
                    <li class="layui-this">
                    <?php else: ?>
                    <li>
                    <?php endif; if(substr($vo['url'], 0, 4) == 'http'): ?>
                        <a href="<?php echo $vo['url']; ?>" target="_blank"><?php echo $vo['title']; ?></a>
                    <?php else: ?>
                        <a href="<?php echo url($vo['url']); ?>"><?php echo $vo['title']; ?></a>
                    <?php endif; ?>
                    </li>
                <?php endforeach; endif; else: echo "" ;endif; ?>
                <div class="tool-btns">
                    <a href="javascript:location.reload();" title="刷新当前页面" class="aicon ai-shuaxin2 font18"></a>
                    <a href="javascript:;" class="aicon ai-quanping1 font18" id="fullscreen-btn" title="打开/关闭全屏"></a>
                </div>
            </ul>
            <div class="layui-tab-content page-tab-content">
                <div class="layui-tab-item layui-show">
                    <div class="layui-tab-item layui-show">
    <form class="layui-form layui-form-pane" id="attachment_setting" name="attachment_setting" method="post">
        <div class="layui-form-item">
            <label class="layui-form-label">图片存储方式</label>
            <div class="layui-input-inline">
                <select class="field-role_id" lay-verify="checkSaveType" name="image_save_server_type" id="image_save_server_type"  lay-skin="switch" lay-filter="image_save_server_type"  >
                    <option value="web_server" <?php if($config['image_save_server_type'] == 'web_server'): ?>selected<?php endif; ?>>web服务器</option>
                    <option value="qiniuyun" <?php if($config['image_save_server_type'] == 'qiniuyun'): ?>selected<?php endif; ?>>七牛云存储</option>
                    <option value="aliyunoss" <?php if($config['image_save_server_type'] == 'aliyunoss'): ?>selected<?php endif; ?>>阿里云存储</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">视频存储方式</label>
            <div class="layui-input-inline">
                <select class="field-role_id" lay-verify="checkSaveType" name="video_save_server_type" id="video_save_server_type"  lay-skin="switch" lay-filter="video_save_server_type"  >
                    <option value="web_server" <?php if($config['video_save_server_type'] == 'web_server'): ?>selected<?php endif; ?>>web服务器</option>
                    <option value="qiniuyun" <?php if($config['video_save_server_type'] == 'qiniuyun'): ?>selected<?php endif; ?>>七牛云存储</option>
                    <option value="aliyunoss" <?php if($config['video_save_server_type'] == 'aliyunoss'): ?>selected<?php endif; ?>>阿里云存储</option>
                </select>
            </div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>Web服务器设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">Web服务器URL</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="web_server_url" id="web_server_url" value="<?php echo $config['web_server_url']; ?>" autocomplete="off" placeholder="请填写Web服务器URL">
            </div>
            <div class="layui-form-mid layui-word-aux">直接填写Web服务器(您的前端网站)的「url」即可</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>七牛云存储设置</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">存储区域</label>
            <div class="layui-input-inline">
                <select  name="qiniu_upload_server" id="qiniu_upload_server" lay-skin="switch" lay-filter="qiniu_upload_server"  >
                    <option value="华东" <?php if($config['qiniu_upload_server'] == '华东'): ?>selected<?php endif; ?> >华东(z0)</option>
                    <option value="华北" <?php if($config['qiniu_upload_server'] == '华北'): ?>selected<?php endif; ?> >华北(z1)</option>
                    <option value="华南" <?php if($config['qiniu_upload_server'] == '华南'): ?>selected<?php endif; ?> >华南(z2)</option>
                    <option value="北美" <?php if($config['qiniu_upload_server'] == '北美'): ?>selected<?php endif; ?> >北美(na0)</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">外链默认域名</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_resource_default_domain" id="qiniu_resource_default_domain" value="<?php echo (isset($config['qiniu_resource_default_domain']) && ($config['qiniu_resource_default_domain'] !== '')?$config['qiniu_resource_default_domain']:''); ?>" autocomplete="off" placeholder="请填写七牛外链默认域名">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">AccessKey</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_accesskey" id="qiniu_accesskey" value="<?php echo $config['qiniu_accesskey']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">SecretKey</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_secretkey" id="qiniu_secretkey" value="<?php echo $config['qiniu_secretkey']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Bucket</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_bucket" id="qiniu_bucket" value="<?php echo $config['qiniu_bucket']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">你的七牛云存储仓库名称</div>
        </div>

        <!--杭州、上海、青岛、北京、张家口、深圳、香港、硅谷、弗吉尼亚、新加坡、悉尼、日本、法兰克福、迪拜-->
        <fieldset class="layui-elem-field layui-field-title">
            <legend>阿里云存储(OSS)设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">存储服务器地址</label>
            <div class="layui-input-inline">
                <select  name="aliyun_oss_city" id="aliyun_oss_city" lay-skin="switch" lay-filter="aliyun_oss_city"  >
                    <option value="深圳" <?php if($config['aliyun_oss_city'] == '深圳'): ?>selected<?php endif; ?>>深圳</option>
                    <option value="杭州" <?php if($config['aliyun_oss_city'] == '杭州'): ?>selected<?php endif; ?>>杭州</option>
                    <option value="上海" <?php if($config['aliyun_oss_city'] == '上海'): ?>selected<?php endif; ?>>上海</option>
                    <option value="青岛" <?php if($config['aliyun_oss_city'] == '青岛'): ?>selected<?php endif; ?>>青岛</option>
                    <option value="北京" <?php if($config['aliyun_oss_city'] == '北京'): ?>selected<?php endif; ?>>北京</option>
                    <option value="张家" <?php if($config['aliyun_oss_city'] == '张家口'): ?>selected<?php endif; ?>>张家口</option>
                    <option value="香港" <?php if($config['aliyun_oss_city'] == '香港'): ?>selected<?php endif; ?>>香港</option>
                    <option value="硅谷" <?php if($config['aliyun_oss_city'] == '硅谷'): ?>selected<?php endif; ?>>硅谷</option>
                    <option value="弗吉尼亚" <?php if($config['aliyun_oss_city'] == '弗吉尼亚'): ?>selected<?php endif; ?>>弗吉尼亚</option>
                    <option value="新加坡" <?php if($config['aliyun_oss_city'] == '新加坡'): ?>selected<?php endif; ?>>新加坡</option>
                    <option value="悉尼" <?php if($config['aliyun_oss_city'] == '悉尼'): ?>selected<?php endif; ?>>悉尼</option>
                    <option value="日本" <?php if($config['aliyun_oss_city'] == '日本'): ?>selected<?php endif; ?>>日本</option>
                    <option value="法兰克福" <?php if($config['aliyun_oss_city'] == '法兰克福'): ?>selected<?php endif; ?>>法兰克福</option>
                    <option value="迪拜" <?php if($config['aliyun_oss_city'] == '迪拜'): ?>selected<?php endif; ?>>迪拜</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">AccessKeyId</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_accesskey" id="aliyun_accesskey" value="<?php echo $config['aliyun_accesskey']; ?>" autocomplete="off" placeholder="请填写AccessKeyId">
            </div>
            <div class="layui-form-mid layui-word-aux">阿里云官网申请，<a target="_blank" href="https://wanwang.aliyun.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">AccessKeySecret</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_secretkey" id="aliyun_secretkey" value="<?php echo $config['aliyun_secretkey']; ?>" autocomplete="off" placeholder="请填写AccessKeySecret">
            </div>
            <div class="layui-form-mid layui-word-aux">阿里云官网申请，<a target="_blank" href="https://wanwang.aliyun.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Bucket</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_bucket"  id="aliyun_bucket" value="<?php echo $config['aliyun_bucket']; ?>" autocomplete="off" placeholder="请填写Bucket">
            </div>
            <div class="layui-form-mid layui-word-aux">你的阿里云存储仓库名称</div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button type="submit" class="layui-btn" lay-submit lay-filter="formSubmit">提交</button>
            </div>
        </div>

    </form>
</div>
<style type="text/css">
    .layui-form-item .layui-form-label{width:150px;}
    .layui-form-item .layui-input-inline{max-width:80%;width:auto;min-width:400px;}
    .layui-field-title:not(:first-child){margin: 30px 0}
</style>
<script>
    layui.use('form', function(){
        var $ = layui.jquery;
        var form = layui.form;

        form.verify({
            checkSaveType:function(value,item){
                if(value=='') return '储存方式不能为空！';

                //根据其值判断对应的设置是否有效
                if(value=='qiniuyun'){
                    if($('#qiniu_accesskey').val()=='' || $('#qiniu_secretkey').val()=='' || $('#qiniu_bucket').val()==''){
                        $("#qiniu_accesskey").focus();
                        return "请完善七牛存储配置！"
                    }
                }

                if(value=='aliyunoss'){
                    if($('#aliyun_accesskey').val()=='' || $('#aliyun_secretkey').val()=='' || $('#aliyun_bucket').val()==''){
                        $("#aliyun_accesskey").focus();
                        return "请完善阿里云存储配置！"
                    }
                }

            },


        });


        form.on('submit(formSubmit)',function(data){
            console.log(data);
            //return true;
        });
    });
</script>

                </div>
            </div>
        </div>
    <?php break; case "2": ?>
    
        <div class="layui-tab layui-tab-card">
            <ul class="layui-tab-title">
                <?php if(is_array($tab_data['menu']) || $tab_data['menu'] instanceof \think\Collection || $tab_data['menu'] instanceof \think\Paginator): $k = 0; $__LIST__ = $tab_data['menu'];if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($k % 2 );++$k;if($k == 1): ?>
                    <li class="layui-this">
                    <?php else: ?>
                    <li>
                    <?php endif; ?>
                    <a href="javascript:;"><?php echo $vo['title']; ?></a>
                    </li>
                <?php endforeach; endif; else: echo "" ;endif; ?>
                <div class="tool-btns">
                    <a href="javascript:location.reload();" title="刷新当前页面" class="aicon ai-shuaxin2 font18"></a>
                    <a href="javascript:;" class="aicon ai-quanping1 font18" id="fullscreen-btn" title="打开/关闭全屏"></a>
                </div>
            </ul>
            <div class="layui-tab-content page-tab-content">
                <div class="layui-tab-item layui-show">
    <form class="layui-form layui-form-pane" id="attachment_setting" name="attachment_setting" method="post">
        <div class="layui-form-item">
            <label class="layui-form-label">图片存储方式</label>
            <div class="layui-input-inline">
                <select class="field-role_id" lay-verify="checkSaveType" name="image_save_server_type" id="image_save_server_type"  lay-skin="switch" lay-filter="image_save_server_type"  >
                    <option value="web_server" <?php if($config['image_save_server_type'] == 'web_server'): ?>selected<?php endif; ?>>web服务器</option>
                    <option value="qiniuyun" <?php if($config['image_save_server_type'] == 'qiniuyun'): ?>selected<?php endif; ?>>七牛云存储</option>
                    <option value="aliyunoss" <?php if($config['image_save_server_type'] == 'aliyunoss'): ?>selected<?php endif; ?>>阿里云存储</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">视频存储方式</label>
            <div class="layui-input-inline">
                <select class="field-role_id" lay-verify="checkSaveType" name="video_save_server_type" id="video_save_server_type"  lay-skin="switch" lay-filter="video_save_server_type"  >
                    <option value="web_server" <?php if($config['video_save_server_type'] == 'web_server'): ?>selected<?php endif; ?>>web服务器</option>
                    <option value="qiniuyun" <?php if($config['video_save_server_type'] == 'qiniuyun'): ?>selected<?php endif; ?>>七牛云存储</option>
                    <option value="aliyunoss" <?php if($config['video_save_server_type'] == 'aliyunoss'): ?>selected<?php endif; ?>>阿里云存储</option>
                </select>
            </div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>Web服务器设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">Web服务器URL</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="web_server_url" id="web_server_url" value="<?php echo $config['web_server_url']; ?>" autocomplete="off" placeholder="请填写Web服务器URL">
            </div>
            <div class="layui-form-mid layui-word-aux">直接填写Web服务器(您的前端网站)的「url」即可</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>七牛云存储设置</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">存储区域</label>
            <div class="layui-input-inline">
                <select  name="qiniu_upload_server" id="qiniu_upload_server" lay-skin="switch" lay-filter="qiniu_upload_server"  >
                    <option value="华东" <?php if($config['qiniu_upload_server'] == '华东'): ?>selected<?php endif; ?> >华东(z0)</option>
                    <option value="华北" <?php if($config['qiniu_upload_server'] == '华北'): ?>selected<?php endif; ?> >华北(z1)</option>
                    <option value="华南" <?php if($config['qiniu_upload_server'] == '华南'): ?>selected<?php endif; ?> >华南(z2)</option>
                    <option value="北美" <?php if($config['qiniu_upload_server'] == '北美'): ?>selected<?php endif; ?> >北美(na0)</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">外链默认域名</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_resource_default_domain" id="qiniu_resource_default_domain" value="<?php echo (isset($config['qiniu_resource_default_domain']) && ($config['qiniu_resource_default_domain'] !== '')?$config['qiniu_resource_default_domain']:''); ?>" autocomplete="off" placeholder="请填写七牛外链默认域名">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">AccessKey</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_accesskey" id="qiniu_accesskey" value="<?php echo $config['qiniu_accesskey']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">SecretKey</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_secretkey" id="qiniu_secretkey" value="<?php echo $config['qiniu_secretkey']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Bucket</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_bucket" id="qiniu_bucket" value="<?php echo $config['qiniu_bucket']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">你的七牛云存储仓库名称</div>
        </div>

        <!--杭州、上海、青岛、北京、张家口、深圳、香港、硅谷、弗吉尼亚、新加坡、悉尼、日本、法兰克福、迪拜-->
        <fieldset class="layui-elem-field layui-field-title">
            <legend>阿里云存储(OSS)设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">存储服务器地址</label>
            <div class="layui-input-inline">
                <select  name="aliyun_oss_city" id="aliyun_oss_city" lay-skin="switch" lay-filter="aliyun_oss_city"  >
                    <option value="深圳" <?php if($config['aliyun_oss_city'] == '深圳'): ?>selected<?php endif; ?>>深圳</option>
                    <option value="杭州" <?php if($config['aliyun_oss_city'] == '杭州'): ?>selected<?php endif; ?>>杭州</option>
                    <option value="上海" <?php if($config['aliyun_oss_city'] == '上海'): ?>selected<?php endif; ?>>上海</option>
                    <option value="青岛" <?php if($config['aliyun_oss_city'] == '青岛'): ?>selected<?php endif; ?>>青岛</option>
                    <option value="北京" <?php if($config['aliyun_oss_city'] == '北京'): ?>selected<?php endif; ?>>北京</option>
                    <option value="张家" <?php if($config['aliyun_oss_city'] == '张家口'): ?>selected<?php endif; ?>>张家口</option>
                    <option value="香港" <?php if($config['aliyun_oss_city'] == '香港'): ?>selected<?php endif; ?>>香港</option>
                    <option value="硅谷" <?php if($config['aliyun_oss_city'] == '硅谷'): ?>selected<?php endif; ?>>硅谷</option>
                    <option value="弗吉尼亚" <?php if($config['aliyun_oss_city'] == '弗吉尼亚'): ?>selected<?php endif; ?>>弗吉尼亚</option>
                    <option value="新加坡" <?php if($config['aliyun_oss_city'] == '新加坡'): ?>selected<?php endif; ?>>新加坡</option>
                    <option value="悉尼" <?php if($config['aliyun_oss_city'] == '悉尼'): ?>selected<?php endif; ?>>悉尼</option>
                    <option value="日本" <?php if($config['aliyun_oss_city'] == '日本'): ?>selected<?php endif; ?>>日本</option>
                    <option value="法兰克福" <?php if($config['aliyun_oss_city'] == '法兰克福'): ?>selected<?php endif; ?>>法兰克福</option>
                    <option value="迪拜" <?php if($config['aliyun_oss_city'] == '迪拜'): ?>selected<?php endif; ?>>迪拜</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">AccessKeyId</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_accesskey" id="aliyun_accesskey" value="<?php echo $config['aliyun_accesskey']; ?>" autocomplete="off" placeholder="请填写AccessKeyId">
            </div>
            <div class="layui-form-mid layui-word-aux">阿里云官网申请，<a target="_blank" href="https://wanwang.aliyun.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">AccessKeySecret</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_secretkey" id="aliyun_secretkey" value="<?php echo $config['aliyun_secretkey']; ?>" autocomplete="off" placeholder="请填写AccessKeySecret">
            </div>
            <div class="layui-form-mid layui-word-aux">阿里云官网申请，<a target="_blank" href="https://wanwang.aliyun.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Bucket</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_bucket"  id="aliyun_bucket" value="<?php echo $config['aliyun_bucket']; ?>" autocomplete="off" placeholder="请填写Bucket">
            </div>
            <div class="layui-form-mid layui-word-aux">你的阿里云存储仓库名称</div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button type="submit" class="layui-btn" lay-submit lay-filter="formSubmit">提交</button>
            </div>
        </div>

    </form>
</div>
<style type="text/css">
    .layui-form-item .layui-form-label{width:150px;}
    .layui-form-item .layui-input-inline{max-width:80%;width:auto;min-width:400px;}
    .layui-field-title:not(:first-child){margin: 30px 0}
</style>
<script>
    layui.use('form', function(){
        var $ = layui.jquery;
        var form = layui.form;

        form.verify({
            checkSaveType:function(value,item){
                if(value=='') return '储存方式不能为空！';

                //根据其值判断对应的设置是否有效
                if(value=='qiniuyun'){
                    if($('#qiniu_accesskey').val()=='' || $('#qiniu_secretkey').val()=='' || $('#qiniu_bucket').val()==''){
                        $("#qiniu_accesskey").focus();
                        return "请完善七牛存储配置！"
                    }
                }

                if(value=='aliyunoss'){
                    if($('#aliyun_accesskey').val()=='' || $('#aliyun_secretkey').val()=='' || $('#aliyun_bucket').val()==''){
                        $("#aliyun_accesskey").focus();
                        return "请完善阿里云存储配置！"
                    }
                }

            },


        });


        form.on('submit(formSubmit)',function(data){
            console.log(data);
            //return true;
        });
    });
</script>

            </div>
        </div>
    <?php break; case "3": ?>
    
        <div class="layui-tab-item layui-show">
    <form class="layui-form layui-form-pane" id="attachment_setting" name="attachment_setting" method="post">
        <div class="layui-form-item">
            <label class="layui-form-label">图片存储方式</label>
            <div class="layui-input-inline">
                <select class="field-role_id" lay-verify="checkSaveType" name="image_save_server_type" id="image_save_server_type"  lay-skin="switch" lay-filter="image_save_server_type"  >
                    <option value="web_server" <?php if($config['image_save_server_type'] == 'web_server'): ?>selected<?php endif; ?>>web服务器</option>
                    <option value="qiniuyun" <?php if($config['image_save_server_type'] == 'qiniuyun'): ?>selected<?php endif; ?>>七牛云存储</option>
                    <option value="aliyunoss" <?php if($config['image_save_server_type'] == 'aliyunoss'): ?>selected<?php endif; ?>>阿里云存储</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">视频存储方式</label>
            <div class="layui-input-inline">
                <select class="field-role_id" lay-verify="checkSaveType" name="video_save_server_type" id="video_save_server_type"  lay-skin="switch" lay-filter="video_save_server_type"  >
                    <option value="web_server" <?php if($config['video_save_server_type'] == 'web_server'): ?>selected<?php endif; ?>>web服务器</option>
                    <option value="qiniuyun" <?php if($config['video_save_server_type'] == 'qiniuyun'): ?>selected<?php endif; ?>>七牛云存储</option>
                    <option value="aliyunoss" <?php if($config['video_save_server_type'] == 'aliyunoss'): ?>selected<?php endif; ?>>阿里云存储</option>
                </select>
            </div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>Web服务器设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">Web服务器URL</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="web_server_url" id="web_server_url" value="<?php echo $config['web_server_url']; ?>" autocomplete="off" placeholder="请填写Web服务器URL">
            </div>
            <div class="layui-form-mid layui-word-aux">直接填写Web服务器(您的前端网站)的「url」即可</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>七牛云存储设置</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">存储区域</label>
            <div class="layui-input-inline">
                <select  name="qiniu_upload_server" id="qiniu_upload_server" lay-skin="switch" lay-filter="qiniu_upload_server"  >
                    <option value="华东" <?php if($config['qiniu_upload_server'] == '华东'): ?>selected<?php endif; ?> >华东(z0)</option>
                    <option value="华北" <?php if($config['qiniu_upload_server'] == '华北'): ?>selected<?php endif; ?> >华北(z1)</option>
                    <option value="华南" <?php if($config['qiniu_upload_server'] == '华南'): ?>selected<?php endif; ?> >华南(z2)</option>
                    <option value="北美" <?php if($config['qiniu_upload_server'] == '北美'): ?>selected<?php endif; ?> >北美(na0)</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">外链默认域名</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_resource_default_domain" id="qiniu_resource_default_domain" value="<?php echo (isset($config['qiniu_resource_default_domain']) && ($config['qiniu_resource_default_domain'] !== '')?$config['qiniu_resource_default_domain']:''); ?>" autocomplete="off" placeholder="请填写七牛外链默认域名">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">AccessKey</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_accesskey" id="qiniu_accesskey" value="<?php echo $config['qiniu_accesskey']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">SecretKey</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_secretkey" id="qiniu_secretkey" value="<?php echo $config['qiniu_secretkey']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Bucket</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_bucket" id="qiniu_bucket" value="<?php echo $config['qiniu_bucket']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">你的七牛云存储仓库名称</div>
        </div>

        <!--杭州、上海、青岛、北京、张家口、深圳、香港、硅谷、弗吉尼亚、新加坡、悉尼、日本、法兰克福、迪拜-->
        <fieldset class="layui-elem-field layui-field-title">
            <legend>阿里云存储(OSS)设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">存储服务器地址</label>
            <div class="layui-input-inline">
                <select  name="aliyun_oss_city" id="aliyun_oss_city" lay-skin="switch" lay-filter="aliyun_oss_city"  >
                    <option value="深圳" <?php if($config['aliyun_oss_city'] == '深圳'): ?>selected<?php endif; ?>>深圳</option>
                    <option value="杭州" <?php if($config['aliyun_oss_city'] == '杭州'): ?>selected<?php endif; ?>>杭州</option>
                    <option value="上海" <?php if($config['aliyun_oss_city'] == '上海'): ?>selected<?php endif; ?>>上海</option>
                    <option value="青岛" <?php if($config['aliyun_oss_city'] == '青岛'): ?>selected<?php endif; ?>>青岛</option>
                    <option value="北京" <?php if($config['aliyun_oss_city'] == '北京'): ?>selected<?php endif; ?>>北京</option>
                    <option value="张家" <?php if($config['aliyun_oss_city'] == '张家口'): ?>selected<?php endif; ?>>张家口</option>
                    <option value="香港" <?php if($config['aliyun_oss_city'] == '香港'): ?>selected<?php endif; ?>>香港</option>
                    <option value="硅谷" <?php if($config['aliyun_oss_city'] == '硅谷'): ?>selected<?php endif; ?>>硅谷</option>
                    <option value="弗吉尼亚" <?php if($config['aliyun_oss_city'] == '弗吉尼亚'): ?>selected<?php endif; ?>>弗吉尼亚</option>
                    <option value="新加坡" <?php if($config['aliyun_oss_city'] == '新加坡'): ?>selected<?php endif; ?>>新加坡</option>
                    <option value="悉尼" <?php if($config['aliyun_oss_city'] == '悉尼'): ?>selected<?php endif; ?>>悉尼</option>
                    <option value="日本" <?php if($config['aliyun_oss_city'] == '日本'): ?>selected<?php endif; ?>>日本</option>
                    <option value="法兰克福" <?php if($config['aliyun_oss_city'] == '法兰克福'): ?>selected<?php endif; ?>>法兰克福</option>
                    <option value="迪拜" <?php if($config['aliyun_oss_city'] == '迪拜'): ?>selected<?php endif; ?>>迪拜</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">AccessKeyId</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_accesskey" id="aliyun_accesskey" value="<?php echo $config['aliyun_accesskey']; ?>" autocomplete="off" placeholder="请填写AccessKeyId">
            </div>
            <div class="layui-form-mid layui-word-aux">阿里云官网申请，<a target="_blank" href="https://wanwang.aliyun.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">AccessKeySecret</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_secretkey" id="aliyun_secretkey" value="<?php echo $config['aliyun_secretkey']; ?>" autocomplete="off" placeholder="请填写AccessKeySecret">
            </div>
            <div class="layui-form-mid layui-word-aux">阿里云官网申请，<a target="_blank" href="https://wanwang.aliyun.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Bucket</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_bucket"  id="aliyun_bucket" value="<?php echo $config['aliyun_bucket']; ?>" autocomplete="off" placeholder="请填写Bucket">
            </div>
            <div class="layui-form-mid layui-word-aux">你的阿里云存储仓库名称</div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button type="submit" class="layui-btn" lay-submit lay-filter="formSubmit">提交</button>
            </div>
        </div>

    </form>
</div>
<style type="text/css">
    .layui-form-item .layui-form-label{width:150px;}
    .layui-form-item .layui-input-inline{max-width:80%;width:auto;min-width:400px;}
    .layui-field-title:not(:first-child){margin: 30px 0}
</style>
<script>
    layui.use('form', function(){
        var $ = layui.jquery;
        var form = layui.form;

        form.verify({
            checkSaveType:function(value,item){
                if(value=='') return '储存方式不能为空！';

                //根据其值判断对应的设置是否有效
                if(value=='qiniuyun'){
                    if($('#qiniu_accesskey').val()=='' || $('#qiniu_secretkey').val()=='' || $('#qiniu_bucket').val()==''){
                        $("#qiniu_accesskey").focus();
                        return "请完善七牛存储配置！"
                    }
                }

                if(value=='aliyunoss'){
                    if($('#aliyun_accesskey').val()=='' || $('#aliyun_secretkey').val()=='' || $('#aliyun_bucket').val()==''){
                        $("#aliyun_accesskey").focus();
                        return "请完善阿里云存储配置！"
                    }
                }

            },


        });


        form.on('submit(formSubmit)',function(data){
            console.log(data);
            //return true;
        });
    });
</script>

    <?php break; default: ?>
    
        <div class="layui-tab layui-tab-card">
            <ul class="layui-tab-title">
                <li class="layui-this">
                    <a href="javascript:;" id="curTitle"><?php echo $_admin_menu_current['title']; ?></a>
                </li>
                <div class="tool-btns">
                    <a href="javascript:location.reload();" title="刷新当前页面" class="aicon ai-shuaxin2 font18"></a>
                    <a href="javascript:;" class="aicon ai-quanping1 font18" id="fullscreen-btn" title="打开/关闭全屏"></a>
                </div>
            </ul>
            <div class="layui-tab-content page-tab-content">
                <div class="layui-tab-item layui-show">
                    <div class="layui-tab-item layui-show">
    <form class="layui-form layui-form-pane" id="attachment_setting" name="attachment_setting" method="post">
        <div class="layui-form-item">
            <label class="layui-form-label">图片存储方式</label>
            <div class="layui-input-inline">
                <select class="field-role_id" lay-verify="checkSaveType" name="image_save_server_type" id="image_save_server_type"  lay-skin="switch" lay-filter="image_save_server_type"  >
                    <option value="web_server" <?php if($config['image_save_server_type'] == 'web_server'): ?>selected<?php endif; ?>>web服务器</option>
                    <option value="qiniuyun" <?php if($config['image_save_server_type'] == 'qiniuyun'): ?>selected<?php endif; ?>>七牛云存储</option>
                    <option value="aliyunoss" <?php if($config['image_save_server_type'] == 'aliyunoss'): ?>selected<?php endif; ?>>阿里云存储</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">视频存储方式</label>
            <div class="layui-input-inline">
                <select class="field-role_id" lay-verify="checkSaveType" name="video_save_server_type" id="video_save_server_type"  lay-skin="switch" lay-filter="video_save_server_type"  >
                    <option value="web_server" <?php if($config['video_save_server_type'] == 'web_server'): ?>selected<?php endif; ?>>web服务器</option>
                    <option value="qiniuyun" <?php if($config['video_save_server_type'] == 'qiniuyun'): ?>selected<?php endif; ?>>七牛云存储</option>
                    <option value="aliyunoss" <?php if($config['video_save_server_type'] == 'aliyunoss'): ?>selected<?php endif; ?>>阿里云存储</option>
                </select>
            </div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>Web服务器设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">Web服务器URL</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="web_server_url" id="web_server_url" value="<?php echo $config['web_server_url']; ?>" autocomplete="off" placeholder="请填写Web服务器URL">
            </div>
            <div class="layui-form-mid layui-word-aux">直接填写Web服务器(您的前端网站)的「url」即可</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>七牛云存储设置</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">存储区域</label>
            <div class="layui-input-inline">
                <select  name="qiniu_upload_server" id="qiniu_upload_server" lay-skin="switch" lay-filter="qiniu_upload_server"  >
                    <option value="华东" <?php if($config['qiniu_upload_server'] == '华东'): ?>selected<?php endif; ?> >华东(z0)</option>
                    <option value="华北" <?php if($config['qiniu_upload_server'] == '华北'): ?>selected<?php endif; ?> >华北(z1)</option>
                    <option value="华南" <?php if($config['qiniu_upload_server'] == '华南'): ?>selected<?php endif; ?> >华南(z2)</option>
                    <option value="北美" <?php if($config['qiniu_upload_server'] == '北美'): ?>selected<?php endif; ?> >北美(na0)</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">外链默认域名</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_resource_default_domain" id="qiniu_resource_default_domain" value="<?php echo (isset($config['qiniu_resource_default_domain']) && ($config['qiniu_resource_default_domain'] !== '')?$config['qiniu_resource_default_domain']:''); ?>" autocomplete="off" placeholder="请填写七牛外链默认域名">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">AccessKey</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_accesskey" id="qiniu_accesskey" value="<?php echo $config['qiniu_accesskey']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">SecretKey</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_secretkey" id="qiniu_secretkey" value="<?php echo $config['qiniu_secretkey']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">七牛官网申请，<a target="_blank" href="https://www.qiniu.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Bucket</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="qiniu_bucket" id="qiniu_bucket" value="<?php echo $config['qiniu_bucket']; ?>" autocomplete="off" placeholder="请填写七牛AccessKey">
            </div>
            <div class="layui-form-mid layui-word-aux">你的七牛云存储仓库名称</div>
        </div>

        <!--杭州、上海、青岛、北京、张家口、深圳、香港、硅谷、弗吉尼亚、新加坡、悉尼、日本、法兰克福、迪拜-->
        <fieldset class="layui-elem-field layui-field-title">
            <legend>阿里云存储(OSS)设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">存储服务器地址</label>
            <div class="layui-input-inline">
                <select  name="aliyun_oss_city" id="aliyun_oss_city" lay-skin="switch" lay-filter="aliyun_oss_city"  >
                    <option value="深圳" <?php if($config['aliyun_oss_city'] == '深圳'): ?>selected<?php endif; ?>>深圳</option>
                    <option value="杭州" <?php if($config['aliyun_oss_city'] == '杭州'): ?>selected<?php endif; ?>>杭州</option>
                    <option value="上海" <?php if($config['aliyun_oss_city'] == '上海'): ?>selected<?php endif; ?>>上海</option>
                    <option value="青岛" <?php if($config['aliyun_oss_city'] == '青岛'): ?>selected<?php endif; ?>>青岛</option>
                    <option value="北京" <?php if($config['aliyun_oss_city'] == '北京'): ?>selected<?php endif; ?>>北京</option>
                    <option value="张家" <?php if($config['aliyun_oss_city'] == '张家口'): ?>selected<?php endif; ?>>张家口</option>
                    <option value="香港" <?php if($config['aliyun_oss_city'] == '香港'): ?>selected<?php endif; ?>>香港</option>
                    <option value="硅谷" <?php if($config['aliyun_oss_city'] == '硅谷'): ?>selected<?php endif; ?>>硅谷</option>
                    <option value="弗吉尼亚" <?php if($config['aliyun_oss_city'] == '弗吉尼亚'): ?>selected<?php endif; ?>>弗吉尼亚</option>
                    <option value="新加坡" <?php if($config['aliyun_oss_city'] == '新加坡'): ?>selected<?php endif; ?>>新加坡</option>
                    <option value="悉尼" <?php if($config['aliyun_oss_city'] == '悉尼'): ?>selected<?php endif; ?>>悉尼</option>
                    <option value="日本" <?php if($config['aliyun_oss_city'] == '日本'): ?>selected<?php endif; ?>>日本</option>
                    <option value="法兰克福" <?php if($config['aliyun_oss_city'] == '法兰克福'): ?>selected<?php endif; ?>>法兰克福</option>
                    <option value="迪拜" <?php if($config['aliyun_oss_city'] == '迪拜'): ?>selected<?php endif; ?>>迪拜</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">AccessKeyId</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_accesskey" id="aliyun_accesskey" value="<?php echo $config['aliyun_accesskey']; ?>" autocomplete="off" placeholder="请填写AccessKeyId">
            </div>
            <div class="layui-form-mid layui-word-aux">阿里云官网申请，<a target="_blank" href="https://wanwang.aliyun.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">AccessKeySecret</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_secretkey" id="aliyun_secretkey" value="<?php echo $config['aliyun_secretkey']; ?>" autocomplete="off" placeholder="请填写AccessKeySecret">
            </div>
            <div class="layui-form-mid layui-word-aux">阿里云官网申请，<a target="_blank" href="https://wanwang.aliyun.com">点击申请</a></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">Bucket</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="aliyun_bucket"  id="aliyun_bucket" value="<?php echo $config['aliyun_bucket']; ?>" autocomplete="off" placeholder="请填写Bucket">
            </div>
            <div class="layui-form-mid layui-word-aux">你的阿里云存储仓库名称</div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button type="submit" class="layui-btn" lay-submit lay-filter="formSubmit">提交</button>
            </div>
        </div>

    </form>
</div>
<style type="text/css">
    .layui-form-item .layui-form-label{width:150px;}
    .layui-form-item .layui-input-inline{max-width:80%;width:auto;min-width:400px;}
    .layui-field-title:not(:first-child){margin: 30px 0}
</style>
<script>
    layui.use('form', function(){
        var $ = layui.jquery;
        var form = layui.form;

        form.verify({
            checkSaveType:function(value,item){
                if(value=='') return '储存方式不能为空！';

                //根据其值判断对应的设置是否有效
                if(value=='qiniuyun'){
                    if($('#qiniu_accesskey').val()=='' || $('#qiniu_secretkey').val()=='' || $('#qiniu_bucket').val()==''){
                        $("#qiniu_accesskey").focus();
                        return "请完善七牛存储配置！"
                    }
                }

                if(value=='aliyunoss'){
                    if($('#aliyun_accesskey').val()=='' || $('#aliyun_secretkey').val()=='' || $('#aliyun_bucket').val()==''){
                        $("#aliyun_accesskey").focus();
                        return "请完善阿里云存储配置！"
                    }
                }

            },


        });


        form.on('submit(formSubmit)',function(data){
            console.log(data);
            //return true;
        });
    });
</script>

                </div>
            </div>
        </div>
<?php endswitch; if(input('param.hisi_iframe') || cookie('hisi_iframe')): ?>
</body>
</html>
<?php else: ?>
        </div>
    </div>
    <div class="layui-footer footer">
        <span class="fl">  © 2017-2018 baidu All Rights Reserved</span>
    </div>
</div>
</body>
</html>
<?php endif; ?>
<?php if (!defined('THINK_PATH')) exit(); /*a:4:{s:56:"/www/wwwroot/msvodx/houtai/app/admin/view/notice/add.php";i:1526288762;s:52:"/www/wwwroot/msvodx/houtai/app/admin/view/layout.php";i:1527499864;s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/block/header.php";i:1564651146;s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/block/footer.php";i:1564651146;}*/ ?>
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
                    ﻿<form class="layui-form layui-form-pane" action="<?php echo url(); ?>" method="post" id="editForm">
    <div class="layui-form-item">
        <label class="layui-form-label">公告标题</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="title"  autocomplete="off" placeholder="请输入公告标题" style="width: 650px;padding-right:10px; ">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">展示方式</label>
        <div class="layui-input-inline">
            <select name="type" class="field-role_id" type="select"   lay-skin="switch" lay-filter="type"  >
                <option value="1" >弹出层展示</option>
                <option value="2" >新页面展示</option>
            </select>
        </div>
        <div class="layui-form-mid layui-word-aux">内容展示方式，当用户点击公告标题后的内容展示形式选择</div>
    </div>

    <div class="layui-form-item" id='content'>
        <label class="layui-form-label">展示内容</label>
        <div class="layui-input-inline">
            <textarea rows="6" class="layui-textarea" name="content" autocomplete="off" placeholder="请填写弹出层展示的内容"  style="width: 650px;padding-right:10px; ">

            </textarea>
        </div>
    </div>
    <div class="layui-form-item" id='url' style="display: none">
        <label class="layui-form-label">外链网址</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="url"  autocomplete="off" placeholder="请输入外链网址" style="width: 650px;padding-right:10px; ">
        </div>
    </div>
    <script>
        layui.use('form', function(){
            var $ = layui.jquery;
            var form = layui.form;
            form.on('select(type)', function(data){
                if(data.value == 1){
                    $('#url').hide();
                    $('#content').show();
                }else{
                    $('#url').show();
                    $('#content').hide();
                }
            });
        });
    </script>
    <div class="layui-form-item">
        <label class="layui-form-label">排序</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="sort"  autocomplete="off" placeholder="请输入排序编号">
        </div>
        <div class="layui-form-mid layui-word-aux">数值越小越靠前</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">公告有效期</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-expire_time" name="out_time" autocomplete="off" placeholder="请设置公告有效期" onclick="layui.laydate({elem: this,format:'YYYY-MM-DD'})" readonly>
        </div>
        <div class="layui-form-mid layui-word-aux">过期后公告将不再显示</div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <input type="hidden" class="field-id" name="id">
            <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
            <a href="<?php echo url('index'); ?>" class="layui-btn layui-btn-primary ml10"><i class="aicon ai-fanhui"></i>返回</a>
        </div>
    </div>
</form>

<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script><script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>
<script>
var formData = <?php echo json_encode($data_info); ?>;

layui.use(['jquery', 'laydate'], function() {
    var $ = layui.jquery, laydate = layui.laydate;
    laydate.render({
        elem: '.field-expire_time',
        min:'0'
    });

    $('#reset_expire').on('click', function(){
        $('input[name="expire_time"]').val(0);
    });
});
</script>
<script src="__ADMIN_JS__/footer.js"></script>
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
                ﻿<form class="layui-form layui-form-pane" action="<?php echo url(); ?>" method="post" id="editForm">
    <div class="layui-form-item">
        <label class="layui-form-label">公告标题</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="title"  autocomplete="off" placeholder="请输入公告标题" style="width: 650px;padding-right:10px; ">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">展示方式</label>
        <div class="layui-input-inline">
            <select name="type" class="field-role_id" type="select"   lay-skin="switch" lay-filter="type"  >
                <option value="1" >弹出层展示</option>
                <option value="2" >新页面展示</option>
            </select>
        </div>
        <div class="layui-form-mid layui-word-aux">内容展示方式，当用户点击公告标题后的内容展示形式选择</div>
    </div>

    <div class="layui-form-item" id='content'>
        <label class="layui-form-label">展示内容</label>
        <div class="layui-input-inline">
            <textarea rows="6" class="layui-textarea" name="content" autocomplete="off" placeholder="请填写弹出层展示的内容"  style="width: 650px;padding-right:10px; ">

            </textarea>
        </div>
    </div>
    <div class="layui-form-item" id='url' style="display: none">
        <label class="layui-form-label">外链网址</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="url"  autocomplete="off" placeholder="请输入外链网址" style="width: 650px;padding-right:10px; ">
        </div>
    </div>
    <script>
        layui.use('form', function(){
            var $ = layui.jquery;
            var form = layui.form;
            form.on('select(type)', function(data){
                if(data.value == 1){
                    $('#url').hide();
                    $('#content').show();
                }else{
                    $('#url').show();
                    $('#content').hide();
                }
            });
        });
    </script>
    <div class="layui-form-item">
        <label class="layui-form-label">排序</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="sort"  autocomplete="off" placeholder="请输入排序编号">
        </div>
        <div class="layui-form-mid layui-word-aux">数值越小越靠前</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">公告有效期</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-expire_time" name="out_time" autocomplete="off" placeholder="请设置公告有效期" onclick="layui.laydate({elem: this,format:'YYYY-MM-DD'})" readonly>
        </div>
        <div class="layui-form-mid layui-word-aux">过期后公告将不再显示</div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <input type="hidden" class="field-id" name="id">
            <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
            <a href="<?php echo url('index'); ?>" class="layui-btn layui-btn-primary ml10"><i class="aicon ai-fanhui"></i>返回</a>
        </div>
    </div>
</form>

<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script><script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>
<script>
var formData = <?php echo json_encode($data_info); ?>;

layui.use(['jquery', 'laydate'], function() {
    var $ = layui.jquery, laydate = layui.laydate;
    laydate.render({
        elem: '.field-expire_time',
        min:'0'
    });

    $('#reset_expire').on('click', function(){
        $('input[name="expire_time"]').val(0);
    });
});
</script>
<script src="__ADMIN_JS__/footer.js"></script>
            </div>
        </div>
    <?php break; case "3": ?>
    
        ﻿<form class="layui-form layui-form-pane" action="<?php echo url(); ?>" method="post" id="editForm">
    <div class="layui-form-item">
        <label class="layui-form-label">公告标题</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="title"  autocomplete="off" placeholder="请输入公告标题" style="width: 650px;padding-right:10px; ">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">展示方式</label>
        <div class="layui-input-inline">
            <select name="type" class="field-role_id" type="select"   lay-skin="switch" lay-filter="type"  >
                <option value="1" >弹出层展示</option>
                <option value="2" >新页面展示</option>
            </select>
        </div>
        <div class="layui-form-mid layui-word-aux">内容展示方式，当用户点击公告标题后的内容展示形式选择</div>
    </div>

    <div class="layui-form-item" id='content'>
        <label class="layui-form-label">展示内容</label>
        <div class="layui-input-inline">
            <textarea rows="6" class="layui-textarea" name="content" autocomplete="off" placeholder="请填写弹出层展示的内容"  style="width: 650px;padding-right:10px; ">

            </textarea>
        </div>
    </div>
    <div class="layui-form-item" id='url' style="display: none">
        <label class="layui-form-label">外链网址</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="url"  autocomplete="off" placeholder="请输入外链网址" style="width: 650px;padding-right:10px; ">
        </div>
    </div>
    <script>
        layui.use('form', function(){
            var $ = layui.jquery;
            var form = layui.form;
            form.on('select(type)', function(data){
                if(data.value == 1){
                    $('#url').hide();
                    $('#content').show();
                }else{
                    $('#url').show();
                    $('#content').hide();
                }
            });
        });
    </script>
    <div class="layui-form-item">
        <label class="layui-form-label">排序</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="sort"  autocomplete="off" placeholder="请输入排序编号">
        </div>
        <div class="layui-form-mid layui-word-aux">数值越小越靠前</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">公告有效期</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-expire_time" name="out_time" autocomplete="off" placeholder="请设置公告有效期" onclick="layui.laydate({elem: this,format:'YYYY-MM-DD'})" readonly>
        </div>
        <div class="layui-form-mid layui-word-aux">过期后公告将不再显示</div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <input type="hidden" class="field-id" name="id">
            <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
            <a href="<?php echo url('index'); ?>" class="layui-btn layui-btn-primary ml10"><i class="aicon ai-fanhui"></i>返回</a>
        </div>
    </div>
</form>

<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script><script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>
<script>
var formData = <?php echo json_encode($data_info); ?>;

layui.use(['jquery', 'laydate'], function() {
    var $ = layui.jquery, laydate = layui.laydate;
    laydate.render({
        elem: '.field-expire_time',
        min:'0'
    });

    $('#reset_expire').on('click', function(){
        $('input[name="expire_time"]').val(0);
    });
});
</script>
<script src="__ADMIN_JS__/footer.js"></script>
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
                    ﻿<form class="layui-form layui-form-pane" action="<?php echo url(); ?>" method="post" id="editForm">
    <div class="layui-form-item">
        <label class="layui-form-label">公告标题</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="title"  autocomplete="off" placeholder="请输入公告标题" style="width: 650px;padding-right:10px; ">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">展示方式</label>
        <div class="layui-input-inline">
            <select name="type" class="field-role_id" type="select"   lay-skin="switch" lay-filter="type"  >
                <option value="1" >弹出层展示</option>
                <option value="2" >新页面展示</option>
            </select>
        </div>
        <div class="layui-form-mid layui-word-aux">内容展示方式，当用户点击公告标题后的内容展示形式选择</div>
    </div>

    <div class="layui-form-item" id='content'>
        <label class="layui-form-label">展示内容</label>
        <div class="layui-input-inline">
            <textarea rows="6" class="layui-textarea" name="content" autocomplete="off" placeholder="请填写弹出层展示的内容"  style="width: 650px;padding-right:10px; ">

            </textarea>
        </div>
    </div>
    <div class="layui-form-item" id='url' style="display: none">
        <label class="layui-form-label">外链网址</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="url"  autocomplete="off" placeholder="请输入外链网址" style="width: 650px;padding-right:10px; ">
        </div>
    </div>
    <script>
        layui.use('form', function(){
            var $ = layui.jquery;
            var form = layui.form;
            form.on('select(type)', function(data){
                if(data.value == 1){
                    $('#url').hide();
                    $('#content').show();
                }else{
                    $('#url').show();
                    $('#content').hide();
                }
            });
        });
    </script>
    <div class="layui-form-item">
        <label class="layui-form-label">排序</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-username" name="sort"  autocomplete="off" placeholder="请输入排序编号">
        </div>
        <div class="layui-form-mid layui-word-aux">数值越小越靠前</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">公告有效期</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input field-expire_time" name="out_time" autocomplete="off" placeholder="请设置公告有效期" onclick="layui.laydate({elem: this,format:'YYYY-MM-DD'})" readonly>
        </div>
        <div class="layui-form-mid layui-word-aux">过期后公告将不再显示</div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <input type="hidden" class="field-id" name="id">
            <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
            <a href="<?php echo url('index'); ?>" class="layui-btn layui-btn-primary ml10"><i class="aicon ai-fanhui"></i>返回</a>
        </div>
    </div>
</form>

<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script><script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>
<script>
var formData = <?php echo json_encode($data_info); ?>;

layui.use(['jquery', 'laydate'], function() {
    var $ = layui.jquery, laydate = layui.laydate;
    laydate.render({
        elem: '.field-expire_time',
        min:'0'
    });

    $('#reset_expire').on('click', function(){
        $('input[name="expire_time"]').val(0);
    });
});
</script>
<script src="__ADMIN_JS__/footer.js"></script>
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
<?php if (!defined('THINK_PATH')) exit(); /*a:4:{s:60:"/www/wwwroot/msvodx/houtai/app/admin/view/dbconfig/index.php";i:1517283504;s:52:"/www/wwwroot/msvodx/houtai/app/admin/view/layout.php";i:1527499864;s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/block/header.php";i:1564651146;s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/block/footer.php";i:1564651146;}*/ ?>
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
    <!--
    +----------------------------------------------------------------------
    | 添加修改实例模板，可直接复制以下代码使用
    | select元素需要加type="select"
    | 所有可编辑的表单元素需要按以下格式添加class名：class="field-字段名"
    +----------------------------------------------------------------------
    -->

    <form class="layui-form layui-form-pane" action="<?php echo url(); ?>" id="editForm" method="post">
        <fieldset class="layui-elem-field layui-field-title">
            <legend>数据库配置</legend>
        </fieldset>
        <form class="layui-form layui-form-pane" action="?step=4" method="post">
            <div class="layui-form-item">
                <label class="layui-form-label">服务器地址</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="hostname" lay-verify="title" value="<?php echo $config['hostname']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">数据库服务器地址，一般为127.0.0.1</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库端口</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="hostport" lay-verify="title" value="<?php echo $config['hostport']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">系统数据库端口，一般为3306</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库名称</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="database" lay-verify="title" value="<?php echo $config['database']; ?>" >
                </div>
                <div class="layui-form-mid layui-word-aux">系统数据库名,必须包含字母</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库账号</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="username" lay-verify="title" value="<?php echo $config['username']; ?>"  autocomplete="off" >
                </div>
                <div class="layui-form-mid layui-word-aux">连接数据库的用户名</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库密码</label>
                <div class="layui-input-inline w200">
                    <input type="password" class="layui-input" name="password" lay-verify="title"  value=""  autocomplete="off" >
                </div>
                <div class="layui-form-mid layui-word-aux">连接数据库的密码</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库前缀</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="prefix" lay-verify="title" value="<?php echo $config['prefix']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">建议使用默认,数据库前缀必须带 '_'</div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <input type="hidden" class="field-id" name="id">
                    <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
                </div>
            </div>
    </form>
</div>
<div class="layui-tab-item">
    <style type="text/css">
    .site-demo-code{
    left: 0;
    top: 0;
    width: 100%;
    height: 600px;
    border: none;
    padding: 10px;
    font-size: 12px;
    background-color: #F7FBFF;
    color: #881280;
    font-family: Courier New;}
    </style>
    <textarea class="layui-border-box site-demo-text site-demo-code" spellcheck="false" readonly>
<!--
+----------------------------------------------------------------------
| 添加修改实例模板，Ctrl+A 可直接复制以下代码使用
| select元素需要加type="select"
| 所有可编辑的表单元素需要按以下格式添加class名：class="field-字段名"
+----------------------------------------------------------------------
-->
<div class="layui-collapse page-tips">
  <div class="layui-colla-item">
    <h2 class="layui-colla-title">温馨提示</h2>
    <div class="layui-colla-content">
      <p>此页面为后台[添加/修改]标准模板，您可以直接复制使用修改</p>
    </div>
  </div>
</div>
<script>
/* 修改模式下需要将数据放入此变量 */
var formData = {:json_encode($data_info)};
// 会员选择回调函数
function func(data) {
    var $ = layui.jquery;
    $('input[name="member"]').val('['+data[0]['id']+']'+data[0]['username']);
}
layui.use(['upload'], function() {
    var $ = layui.jquery, layer = layui.layer, upload = layui.upload;
    /**
     * 附件上传url参数说明
     * @param string $from 来源
     * @param string $group 附件分组,默认sys[系统]，模块格式：m_模块名，插件：p_插件名
     * @param string $water 水印，参数为空默认调用系统配置，no直接关闭水印，image 图片水印，text文字水印
     * @param string $thumb 缩略图，参数为空默认调用系统配置，no直接关闭缩略图，如需生成 500x500 的缩略图，则 500x500多个规格请用";"隔开
     * @param string $thumb_type 缩略图方式
     * @param string $input 文件表单字段名
     */
    upload.render({
        elem: '.layui-upload'
        ,url: '{:url("admin/annex/upload?water=&thumb=&from=&group=")}'
        ,method: 'post'
        ,before: function(input) {
            layer.msg('文件上传中...', {time:3000000});
        },done: function(res, index, upload) {
            var obj = this.item;
            if (res.code == 0) {
                layer.msg(res.msg);
                return false;
            }
            layer.closeAll();
            var input = $(obj).parents('.upload').find('.upload-input');
            if ($(obj).attr('lay-type') == 'image') {
                input.siblings('img').attr('src', res.data.file).show();
            }
            input.val(res.data.file);
        }
    });
});
</script>

    </textarea>
</div>

<script>
/* 修改模式下需要将数据放入此变量 */
var formData = {"id":1,"role_id":1,"nick":"\u8d85\u7ea7\u7ba1\u7406\u5458","email":"chenf4hua12@qq.com","mobile":13888888888,"status":0};
// 会员选择回调函数
function func(data) {
    var $ = layui.jquery;
    $('input[name="member"]').val('['+data[0]['id']+']'+data[0]['username']);
}

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
                <div class="layui-tab-item layui-show">
    <!--
    +----------------------------------------------------------------------
    | 添加修改实例模板，可直接复制以下代码使用
    | select元素需要加type="select"
    | 所有可编辑的表单元素需要按以下格式添加class名：class="field-字段名"
    +----------------------------------------------------------------------
    -->

    <form class="layui-form layui-form-pane" action="<?php echo url(); ?>" id="editForm" method="post">
        <fieldset class="layui-elem-field layui-field-title">
            <legend>数据库配置</legend>
        </fieldset>
        <form class="layui-form layui-form-pane" action="?step=4" method="post">
            <div class="layui-form-item">
                <label class="layui-form-label">服务器地址</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="hostname" lay-verify="title" value="<?php echo $config['hostname']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">数据库服务器地址，一般为127.0.0.1</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库端口</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="hostport" lay-verify="title" value="<?php echo $config['hostport']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">系统数据库端口，一般为3306</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库名称</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="database" lay-verify="title" value="<?php echo $config['database']; ?>" >
                </div>
                <div class="layui-form-mid layui-word-aux">系统数据库名,必须包含字母</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库账号</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="username" lay-verify="title" value="<?php echo $config['username']; ?>"  autocomplete="off" >
                </div>
                <div class="layui-form-mid layui-word-aux">连接数据库的用户名</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库密码</label>
                <div class="layui-input-inline w200">
                    <input type="password" class="layui-input" name="password" lay-verify="title"  value=""  autocomplete="off" >
                </div>
                <div class="layui-form-mid layui-word-aux">连接数据库的密码</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库前缀</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="prefix" lay-verify="title" value="<?php echo $config['prefix']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">建议使用默认,数据库前缀必须带 '_'</div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <input type="hidden" class="field-id" name="id">
                    <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
                </div>
            </div>
    </form>
</div>
<div class="layui-tab-item">
    <style type="text/css">
    .site-demo-code{
    left: 0;
    top: 0;
    width: 100%;
    height: 600px;
    border: none;
    padding: 10px;
    font-size: 12px;
    background-color: #F7FBFF;
    color: #881280;
    font-family: Courier New;}
    </style>
    <textarea class="layui-border-box site-demo-text site-demo-code" spellcheck="false" readonly>
<!--
+----------------------------------------------------------------------
| 添加修改实例模板，Ctrl+A 可直接复制以下代码使用
| select元素需要加type="select"
| 所有可编辑的表单元素需要按以下格式添加class名：class="field-字段名"
+----------------------------------------------------------------------
-->
<div class="layui-collapse page-tips">
  <div class="layui-colla-item">
    <h2 class="layui-colla-title">温馨提示</h2>
    <div class="layui-colla-content">
      <p>此页面为后台[添加/修改]标准模板，您可以直接复制使用修改</p>
    </div>
  </div>
</div>
<script>
/* 修改模式下需要将数据放入此变量 */
var formData = {:json_encode($data_info)};
// 会员选择回调函数
function func(data) {
    var $ = layui.jquery;
    $('input[name="member"]').val('['+data[0]['id']+']'+data[0]['username']);
}
layui.use(['upload'], function() {
    var $ = layui.jquery, layer = layui.layer, upload = layui.upload;
    /**
     * 附件上传url参数说明
     * @param string $from 来源
     * @param string $group 附件分组,默认sys[系统]，模块格式：m_模块名，插件：p_插件名
     * @param string $water 水印，参数为空默认调用系统配置，no直接关闭水印，image 图片水印，text文字水印
     * @param string $thumb 缩略图，参数为空默认调用系统配置，no直接关闭缩略图，如需生成 500x500 的缩略图，则 500x500多个规格请用";"隔开
     * @param string $thumb_type 缩略图方式
     * @param string $input 文件表单字段名
     */
    upload.render({
        elem: '.layui-upload'
        ,url: '{:url("admin/annex/upload?water=&thumb=&from=&group=")}'
        ,method: 'post'
        ,before: function(input) {
            layer.msg('文件上传中...', {time:3000000});
        },done: function(res, index, upload) {
            var obj = this.item;
            if (res.code == 0) {
                layer.msg(res.msg);
                return false;
            }
            layer.closeAll();
            var input = $(obj).parents('.upload').find('.upload-input');
            if ($(obj).attr('lay-type') == 'image') {
                input.siblings('img').attr('src', res.data.file).show();
            }
            input.val(res.data.file);
        }
    });
});
</script>

    </textarea>
</div>

<script>
/* 修改模式下需要将数据放入此变量 */
var formData = {"id":1,"role_id":1,"nick":"\u8d85\u7ea7\u7ba1\u7406\u5458","email":"chenf4hua12@qq.com","mobile":13888888888,"status":0};
// 会员选择回调函数
function func(data) {
    var $ = layui.jquery;
    $('input[name="member"]').val('['+data[0]['id']+']'+data[0]['username']);
}

<script src="__ADMIN_JS__/footer.js"></script>
            </div>
        </div>
    <?php break; case "3": ?>
    
        <div class="layui-tab-item layui-show">
    <!--
    +----------------------------------------------------------------------
    | 添加修改实例模板，可直接复制以下代码使用
    | select元素需要加type="select"
    | 所有可编辑的表单元素需要按以下格式添加class名：class="field-字段名"
    +----------------------------------------------------------------------
    -->

    <form class="layui-form layui-form-pane" action="<?php echo url(); ?>" id="editForm" method="post">
        <fieldset class="layui-elem-field layui-field-title">
            <legend>数据库配置</legend>
        </fieldset>
        <form class="layui-form layui-form-pane" action="?step=4" method="post">
            <div class="layui-form-item">
                <label class="layui-form-label">服务器地址</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="hostname" lay-verify="title" value="<?php echo $config['hostname']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">数据库服务器地址，一般为127.0.0.1</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库端口</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="hostport" lay-verify="title" value="<?php echo $config['hostport']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">系统数据库端口，一般为3306</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库名称</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="database" lay-verify="title" value="<?php echo $config['database']; ?>" >
                </div>
                <div class="layui-form-mid layui-word-aux">系统数据库名,必须包含字母</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库账号</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="username" lay-verify="title" value="<?php echo $config['username']; ?>"  autocomplete="off" >
                </div>
                <div class="layui-form-mid layui-word-aux">连接数据库的用户名</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库密码</label>
                <div class="layui-input-inline w200">
                    <input type="password" class="layui-input" name="password" lay-verify="title"  value=""  autocomplete="off" >
                </div>
                <div class="layui-form-mid layui-word-aux">连接数据库的密码</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库前缀</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="prefix" lay-verify="title" value="<?php echo $config['prefix']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">建议使用默认,数据库前缀必须带 '_'</div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <input type="hidden" class="field-id" name="id">
                    <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
                </div>
            </div>
    </form>
</div>
<div class="layui-tab-item">
    <style type="text/css">
    .site-demo-code{
    left: 0;
    top: 0;
    width: 100%;
    height: 600px;
    border: none;
    padding: 10px;
    font-size: 12px;
    background-color: #F7FBFF;
    color: #881280;
    font-family: Courier New;}
    </style>
    <textarea class="layui-border-box site-demo-text site-demo-code" spellcheck="false" readonly>
<!--
+----------------------------------------------------------------------
| 添加修改实例模板，Ctrl+A 可直接复制以下代码使用
| select元素需要加type="select"
| 所有可编辑的表单元素需要按以下格式添加class名：class="field-字段名"
+----------------------------------------------------------------------
-->
<div class="layui-collapse page-tips">
  <div class="layui-colla-item">
    <h2 class="layui-colla-title">温馨提示</h2>
    <div class="layui-colla-content">
      <p>此页面为后台[添加/修改]标准模板，您可以直接复制使用修改</p>
    </div>
  </div>
</div>
<script>
/* 修改模式下需要将数据放入此变量 */
var formData = {:json_encode($data_info)};
// 会员选择回调函数
function func(data) {
    var $ = layui.jquery;
    $('input[name="member"]').val('['+data[0]['id']+']'+data[0]['username']);
}
layui.use(['upload'], function() {
    var $ = layui.jquery, layer = layui.layer, upload = layui.upload;
    /**
     * 附件上传url参数说明
     * @param string $from 来源
     * @param string $group 附件分组,默认sys[系统]，模块格式：m_模块名，插件：p_插件名
     * @param string $water 水印，参数为空默认调用系统配置，no直接关闭水印，image 图片水印，text文字水印
     * @param string $thumb 缩略图，参数为空默认调用系统配置，no直接关闭缩略图，如需生成 500x500 的缩略图，则 500x500多个规格请用";"隔开
     * @param string $thumb_type 缩略图方式
     * @param string $input 文件表单字段名
     */
    upload.render({
        elem: '.layui-upload'
        ,url: '{:url("admin/annex/upload?water=&thumb=&from=&group=")}'
        ,method: 'post'
        ,before: function(input) {
            layer.msg('文件上传中...', {time:3000000});
        },done: function(res, index, upload) {
            var obj = this.item;
            if (res.code == 0) {
                layer.msg(res.msg);
                return false;
            }
            layer.closeAll();
            var input = $(obj).parents('.upload').find('.upload-input');
            if ($(obj).attr('lay-type') == 'image') {
                input.siblings('img').attr('src', res.data.file).show();
            }
            input.val(res.data.file);
        }
    });
});
</script>

    </textarea>
</div>

<script>
/* 修改模式下需要将数据放入此变量 */
var formData = {"id":1,"role_id":1,"nick":"\u8d85\u7ea7\u7ba1\u7406\u5458","email":"chenf4hua12@qq.com","mobile":13888888888,"status":0};
// 会员选择回调函数
function func(data) {
    var $ = layui.jquery;
    $('input[name="member"]').val('['+data[0]['id']+']'+data[0]['username']);
}

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
                    <div class="layui-tab-item layui-show">
    <!--
    +----------------------------------------------------------------------
    | 添加修改实例模板，可直接复制以下代码使用
    | select元素需要加type="select"
    | 所有可编辑的表单元素需要按以下格式添加class名：class="field-字段名"
    +----------------------------------------------------------------------
    -->

    <form class="layui-form layui-form-pane" action="<?php echo url(); ?>" id="editForm" method="post">
        <fieldset class="layui-elem-field layui-field-title">
            <legend>数据库配置</legend>
        </fieldset>
        <form class="layui-form layui-form-pane" action="?step=4" method="post">
            <div class="layui-form-item">
                <label class="layui-form-label">服务器地址</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="hostname" lay-verify="title" value="<?php echo $config['hostname']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">数据库服务器地址，一般为127.0.0.1</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库端口</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="hostport" lay-verify="title" value="<?php echo $config['hostport']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">系统数据库端口，一般为3306</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库名称</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="database" lay-verify="title" value="<?php echo $config['database']; ?>" >
                </div>
                <div class="layui-form-mid layui-word-aux">系统数据库名,必须包含字母</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库账号</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="username" lay-verify="title" value="<?php echo $config['username']; ?>"  autocomplete="off" >
                </div>
                <div class="layui-form-mid layui-word-aux">连接数据库的用户名</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库密码</label>
                <div class="layui-input-inline w200">
                    <input type="password" class="layui-input" name="password" lay-verify="title"  value=""  autocomplete="off" >
                </div>
                <div class="layui-form-mid layui-word-aux">连接数据库的密码</div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">数据库前缀</label>
                <div class="layui-input-inline w200">
                    <input type="text" class="layui-input" name="prefix" lay-verify="title" value="<?php echo $config['prefix']; ?>">
                </div>
                <div class="layui-form-mid layui-word-aux">建议使用默认,数据库前缀必须带 '_'</div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <input type="hidden" class="field-id" name="id">
                    <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
                </div>
            </div>
    </form>
</div>
<div class="layui-tab-item">
    <style type="text/css">
    .site-demo-code{
    left: 0;
    top: 0;
    width: 100%;
    height: 600px;
    border: none;
    padding: 10px;
    font-size: 12px;
    background-color: #F7FBFF;
    color: #881280;
    font-family: Courier New;}
    </style>
    <textarea class="layui-border-box site-demo-text site-demo-code" spellcheck="false" readonly>
<!--
+----------------------------------------------------------------------
| 添加修改实例模板，Ctrl+A 可直接复制以下代码使用
| select元素需要加type="select"
| 所有可编辑的表单元素需要按以下格式添加class名：class="field-字段名"
+----------------------------------------------------------------------
-->
<div class="layui-collapse page-tips">
  <div class="layui-colla-item">
    <h2 class="layui-colla-title">温馨提示</h2>
    <div class="layui-colla-content">
      <p>此页面为后台[添加/修改]标准模板，您可以直接复制使用修改</p>
    </div>
  </div>
</div>
<script>
/* 修改模式下需要将数据放入此变量 */
var formData = {:json_encode($data_info)};
// 会员选择回调函数
function func(data) {
    var $ = layui.jquery;
    $('input[name="member"]').val('['+data[0]['id']+']'+data[0]['username']);
}
layui.use(['upload'], function() {
    var $ = layui.jquery, layer = layui.layer, upload = layui.upload;
    /**
     * 附件上传url参数说明
     * @param string $from 来源
     * @param string $group 附件分组,默认sys[系统]，模块格式：m_模块名，插件：p_插件名
     * @param string $water 水印，参数为空默认调用系统配置，no直接关闭水印，image 图片水印，text文字水印
     * @param string $thumb 缩略图，参数为空默认调用系统配置，no直接关闭缩略图，如需生成 500x500 的缩略图，则 500x500多个规格请用";"隔开
     * @param string $thumb_type 缩略图方式
     * @param string $input 文件表单字段名
     */
    upload.render({
        elem: '.layui-upload'
        ,url: '{:url("admin/annex/upload?water=&thumb=&from=&group=")}'
        ,method: 'post'
        ,before: function(input) {
            layer.msg('文件上传中...', {time:3000000});
        },done: function(res, index, upload) {
            var obj = this.item;
            if (res.code == 0) {
                layer.msg(res.msg);
                return false;
            }
            layer.closeAll();
            var input = $(obj).parents('.upload').find('.upload-input');
            if ($(obj).attr('lay-type') == 'image') {
                input.siblings('img').attr('src', res.data.file).show();
            }
            input.val(res.data.file);
        }
    });
});
</script>

    </textarea>
</div>

<script>
/* 修改模式下需要将数据放入此变量 */
var formData = {"id":1,"role_id":1,"nick":"\u8d85\u7ea7\u7ba1\u7406\u5458","email":"chenf4hua12@qq.com","mobile":13888888888,"status":0};
// 会员选择回调函数
function func(data) {
    var $ = layui.jquery;
    $('input[name="member"]').val('['+data[0]['id']+']'+data[0]['username']);
}

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
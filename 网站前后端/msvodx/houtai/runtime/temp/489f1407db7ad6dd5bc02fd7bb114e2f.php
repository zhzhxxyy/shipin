<?php if (!defined('THINK_PATH')) exit(); /*a:4:{s:71:"/www/wwwroot/msvodx/houtai/app/admin/view/drawmoney/draw_money_list.php";i:1516851478;s:52:"/www/wwwroot/msvodx/houtai/app/admin/view/layout.php";i:1527499864;s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/block/header.php";i:1564651146;s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/block/footer.php";i:1564651146;}*/ ?>
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
                    <div class="page-toolbar" >
    <div class="layui-btn-group fl">

    </div>
</div>
<form id="pageListForm">
    <div class="layui-form">
        <table class="layui-table mt10" lay-even=""  lay-size="lg">
            <colgroup>
                <col width="60">
            </colgroup>
            <thead>
            <tr>
                <th>ID</th>
                <th>用户信息</th>
                <th>提现信息</th>
                <th>提现额度</th>
                <th>提现申请时间</th>
                <th>审核状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <?php if(is_array($data_list) || $data_list instanceof \think\Collection || $data_list instanceof \think\Paginator): $i = 0; $__LIST__ = $data_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
            <tr>
                <td class="font12">
                  <?php echo $vo['id']; ?>
                </td>
                <td class="font12">
                    <img src="<?php if($vo['headimgurl']): ?><?php echo $vo['headimgurl']; else: ?>__ADMIN_IMG__/avatar.png<?php endif; ?>" width="60" height="60" class="fl">
                    <p class="ml10 fl"><strong class="mcolor"><?php echo $vo['nickname']; ?> </strong><br><?php echo $vo['tel']; ?><br><?php echo $vo['email']; ?></p>
                </td>
                <td class="font12">
                    <strong>收款方式：</strong> <?php if($vo['info']['type'] == 1): ?>
                    支付宝  <br>
                    <!--<strong> 标题：</strong><?php echo $vo['info']['title']; ?> <br>-->
                    <strong>收款账号：</strong><?php echo $vo['info']['account']; elseif($vo['info']['type'] == 2): ?>
                       银行卡<br>
                    <!--<strong>标题：</strong><?php echo $vo['info']['title']; ?> <br>-->
                    <strong>账号：</strong><?php echo $vo['info']['account']; ?><br>
                    <strong>姓名:</strong><?php echo $vo['info']['account_name']; ?><br>
                    <strong>银行:</strong><?php echo $vo['info']['bank']; endif; ?>

                </td>
                <td class="font12">
                    <span class="layui-badge layui-bg-black">提现额度：<?php echo $vo['money']; ?>元</span> <br />
                    <span class="layui-badge layui-bg-black">兑换金币：<?php echo $vo['gold']; ?>个</span> <br />
                </td>
                <td class="font12">
                    <?php echo date('Y-m-d H:i:s', $vo['add_time']); ?>
                    <!--<br>修改时间：<?php echo date('Y-m-d H:i:s', $vo['update_time']); ?><br></p>-->
                </td>
                <td class="font12">
                     <?php if($vo['status'] == 0): ?>
                        <span class="layui-badge layui-bg-orange">待审核</span>
                        <?php elseif($vo['status'] == 1): ?>
                        <span class="layui-badge layui-bg-green">已通过</span>
                        <?php elseif($vo['status'] == 2): ?>
                        <span class="layui-badge layui-bg-red">已拒绝</span>
                    <?php endif; ?>
                </td>
                <td>
                    <?php if($vo['status'] == 2): ?>
                        <a data-href="#" data-id="<?php echo $vo['id']; ?>"  class="layui-btn layui-btn-disabled">已处理</a>
                    <?php else: ?>
                        <a data-href="#" data-id="<?php echo $vo['id']; ?>" class="layui-btn layui-btn-primary agreen"><i class="aicon ai-qiyong"></i>审核</a>
                    <?php endif; ?>

                </td>

            </tr>
            <?php endforeach; endif; else: echo "" ;endif; ?>
            </tbody>
        </table>
        <?php echo $pages; ?>
    </div>
</form>
<script>
    layui.use(['jquery', 'laydate'], function() {
        var $ = layui.jquery, laydate = layui.laydate;
        $(".agreen").on('click',function () {
            var id=$(this).data('id');
            var url='agreen';
            layer.confirm('您确定已打款吗？', {
                icon: 0,
                btn: ['确认打款','拒绝申请','取消'] //按钮
            }, function(){
                var data={id:id,type:'1'}
                $.get(url,data,function(){

                },'json');
                layer.msg('审核通成功', {icon: 1,end:function(){
                    location.reload()
                }});
            }, function(){
                var data={id:id,type:'2'}
                $.get(url,data,function(){

                },'json');
                layer.msg('审核成功', {icon: 1,end:function(){
                    location.reload()
                }});
            });

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
                <div class="page-toolbar" >
    <div class="layui-btn-group fl">

    </div>
</div>
<form id="pageListForm">
    <div class="layui-form">
        <table class="layui-table mt10" lay-even=""  lay-size="lg">
            <colgroup>
                <col width="60">
            </colgroup>
            <thead>
            <tr>
                <th>ID</th>
                <th>用户信息</th>
                <th>提现信息</th>
                <th>提现额度</th>
                <th>提现申请时间</th>
                <th>审核状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <?php if(is_array($data_list) || $data_list instanceof \think\Collection || $data_list instanceof \think\Paginator): $i = 0; $__LIST__ = $data_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
            <tr>
                <td class="font12">
                  <?php echo $vo['id']; ?>
                </td>
                <td class="font12">
                    <img src="<?php if($vo['headimgurl']): ?><?php echo $vo['headimgurl']; else: ?>__ADMIN_IMG__/avatar.png<?php endif; ?>" width="60" height="60" class="fl">
                    <p class="ml10 fl"><strong class="mcolor"><?php echo $vo['nickname']; ?> </strong><br><?php echo $vo['tel']; ?><br><?php echo $vo['email']; ?></p>
                </td>
                <td class="font12">
                    <strong>收款方式：</strong> <?php if($vo['info']['type'] == 1): ?>
                    支付宝  <br>
                    <!--<strong> 标题：</strong><?php echo $vo['info']['title']; ?> <br>-->
                    <strong>收款账号：</strong><?php echo $vo['info']['account']; elseif($vo['info']['type'] == 2): ?>
                       银行卡<br>
                    <!--<strong>标题：</strong><?php echo $vo['info']['title']; ?> <br>-->
                    <strong>账号：</strong><?php echo $vo['info']['account']; ?><br>
                    <strong>姓名:</strong><?php echo $vo['info']['account_name']; ?><br>
                    <strong>银行:</strong><?php echo $vo['info']['bank']; endif; ?>

                </td>
                <td class="font12">
                    <span class="layui-badge layui-bg-black">提现额度：<?php echo $vo['money']; ?>元</span> <br />
                    <span class="layui-badge layui-bg-black">兑换金币：<?php echo $vo['gold']; ?>个</span> <br />
                </td>
                <td class="font12">
                    <?php echo date('Y-m-d H:i:s', $vo['add_time']); ?>
                    <!--<br>修改时间：<?php echo date('Y-m-d H:i:s', $vo['update_time']); ?><br></p>-->
                </td>
                <td class="font12">
                     <?php if($vo['status'] == 0): ?>
                        <span class="layui-badge layui-bg-orange">待审核</span>
                        <?php elseif($vo['status'] == 1): ?>
                        <span class="layui-badge layui-bg-green">已通过</span>
                        <?php elseif($vo['status'] == 2): ?>
                        <span class="layui-badge layui-bg-red">已拒绝</span>
                    <?php endif; ?>
                </td>
                <td>
                    <?php if($vo['status'] == 2): ?>
                        <a data-href="#" data-id="<?php echo $vo['id']; ?>"  class="layui-btn layui-btn-disabled">已处理</a>
                    <?php else: ?>
                        <a data-href="#" data-id="<?php echo $vo['id']; ?>" class="layui-btn layui-btn-primary agreen"><i class="aicon ai-qiyong"></i>审核</a>
                    <?php endif; ?>

                </td>

            </tr>
            <?php endforeach; endif; else: echo "" ;endif; ?>
            </tbody>
        </table>
        <?php echo $pages; ?>
    </div>
</form>
<script>
    layui.use(['jquery', 'laydate'], function() {
        var $ = layui.jquery, laydate = layui.laydate;
        $(".agreen").on('click',function () {
            var id=$(this).data('id');
            var url='agreen';
            layer.confirm('您确定已打款吗？', {
                icon: 0,
                btn: ['确认打款','拒绝申请','取消'] //按钮
            }, function(){
                var data={id:id,type:'1'}
                $.get(url,data,function(){

                },'json');
                layer.msg('审核通成功', {icon: 1,end:function(){
                    location.reload()
                }});
            }, function(){
                var data={id:id,type:'2'}
                $.get(url,data,function(){

                },'json');
                layer.msg('审核成功', {icon: 1,end:function(){
                    location.reload()
                }});
            });

        });
    });
</script>

            </div>
        </div>
    <?php break; case "3": ?>
    
        <div class="page-toolbar" >
    <div class="layui-btn-group fl">

    </div>
</div>
<form id="pageListForm">
    <div class="layui-form">
        <table class="layui-table mt10" lay-even=""  lay-size="lg">
            <colgroup>
                <col width="60">
            </colgroup>
            <thead>
            <tr>
                <th>ID</th>
                <th>用户信息</th>
                <th>提现信息</th>
                <th>提现额度</th>
                <th>提现申请时间</th>
                <th>审核状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <?php if(is_array($data_list) || $data_list instanceof \think\Collection || $data_list instanceof \think\Paginator): $i = 0; $__LIST__ = $data_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
            <tr>
                <td class="font12">
                  <?php echo $vo['id']; ?>
                </td>
                <td class="font12">
                    <img src="<?php if($vo['headimgurl']): ?><?php echo $vo['headimgurl']; else: ?>__ADMIN_IMG__/avatar.png<?php endif; ?>" width="60" height="60" class="fl">
                    <p class="ml10 fl"><strong class="mcolor"><?php echo $vo['nickname']; ?> </strong><br><?php echo $vo['tel']; ?><br><?php echo $vo['email']; ?></p>
                </td>
                <td class="font12">
                    <strong>收款方式：</strong> <?php if($vo['info']['type'] == 1): ?>
                    支付宝  <br>
                    <!--<strong> 标题：</strong><?php echo $vo['info']['title']; ?> <br>-->
                    <strong>收款账号：</strong><?php echo $vo['info']['account']; elseif($vo['info']['type'] == 2): ?>
                       银行卡<br>
                    <!--<strong>标题：</strong><?php echo $vo['info']['title']; ?> <br>-->
                    <strong>账号：</strong><?php echo $vo['info']['account']; ?><br>
                    <strong>姓名:</strong><?php echo $vo['info']['account_name']; ?><br>
                    <strong>银行:</strong><?php echo $vo['info']['bank']; endif; ?>

                </td>
                <td class="font12">
                    <span class="layui-badge layui-bg-black">提现额度：<?php echo $vo['money']; ?>元</span> <br />
                    <span class="layui-badge layui-bg-black">兑换金币：<?php echo $vo['gold']; ?>个</span> <br />
                </td>
                <td class="font12">
                    <?php echo date('Y-m-d H:i:s', $vo['add_time']); ?>
                    <!--<br>修改时间：<?php echo date('Y-m-d H:i:s', $vo['update_time']); ?><br></p>-->
                </td>
                <td class="font12">
                     <?php if($vo['status'] == 0): ?>
                        <span class="layui-badge layui-bg-orange">待审核</span>
                        <?php elseif($vo['status'] == 1): ?>
                        <span class="layui-badge layui-bg-green">已通过</span>
                        <?php elseif($vo['status'] == 2): ?>
                        <span class="layui-badge layui-bg-red">已拒绝</span>
                    <?php endif; ?>
                </td>
                <td>
                    <?php if($vo['status'] == 2): ?>
                        <a data-href="#" data-id="<?php echo $vo['id']; ?>"  class="layui-btn layui-btn-disabled">已处理</a>
                    <?php else: ?>
                        <a data-href="#" data-id="<?php echo $vo['id']; ?>" class="layui-btn layui-btn-primary agreen"><i class="aicon ai-qiyong"></i>审核</a>
                    <?php endif; ?>

                </td>

            </tr>
            <?php endforeach; endif; else: echo "" ;endif; ?>
            </tbody>
        </table>
        <?php echo $pages; ?>
    </div>
</form>
<script>
    layui.use(['jquery', 'laydate'], function() {
        var $ = layui.jquery, laydate = layui.laydate;
        $(".agreen").on('click',function () {
            var id=$(this).data('id');
            var url='agreen';
            layer.confirm('您确定已打款吗？', {
                icon: 0,
                btn: ['确认打款','拒绝申请','取消'] //按钮
            }, function(){
                var data={id:id,type:'1'}
                $.get(url,data,function(){

                },'json');
                layer.msg('审核通成功', {icon: 1,end:function(){
                    location.reload()
                }});
            }, function(){
                var data={id:id,type:'2'}
                $.get(url,data,function(){

                },'json');
                layer.msg('审核成功', {icon: 1,end:function(){
                    location.reload()
                }});
            });

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
                    <div class="page-toolbar" >
    <div class="layui-btn-group fl">

    </div>
</div>
<form id="pageListForm">
    <div class="layui-form">
        <table class="layui-table mt10" lay-even=""  lay-size="lg">
            <colgroup>
                <col width="60">
            </colgroup>
            <thead>
            <tr>
                <th>ID</th>
                <th>用户信息</th>
                <th>提现信息</th>
                <th>提现额度</th>
                <th>提现申请时间</th>
                <th>审核状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <?php if(is_array($data_list) || $data_list instanceof \think\Collection || $data_list instanceof \think\Paginator): $i = 0; $__LIST__ = $data_list;if( count($__LIST__)==0 ) : echo "" ;else: foreach($__LIST__ as $key=>$vo): $mod = ($i % 2 );++$i;?>
            <tr>
                <td class="font12">
                  <?php echo $vo['id']; ?>
                </td>
                <td class="font12">
                    <img src="<?php if($vo['headimgurl']): ?><?php echo $vo['headimgurl']; else: ?>__ADMIN_IMG__/avatar.png<?php endif; ?>" width="60" height="60" class="fl">
                    <p class="ml10 fl"><strong class="mcolor"><?php echo $vo['nickname']; ?> </strong><br><?php echo $vo['tel']; ?><br><?php echo $vo['email']; ?></p>
                </td>
                <td class="font12">
                    <strong>收款方式：</strong> <?php if($vo['info']['type'] == 1): ?>
                    支付宝  <br>
                    <!--<strong> 标题：</strong><?php echo $vo['info']['title']; ?> <br>-->
                    <strong>收款账号：</strong><?php echo $vo['info']['account']; elseif($vo['info']['type'] == 2): ?>
                       银行卡<br>
                    <!--<strong>标题：</strong><?php echo $vo['info']['title']; ?> <br>-->
                    <strong>账号：</strong><?php echo $vo['info']['account']; ?><br>
                    <strong>姓名:</strong><?php echo $vo['info']['account_name']; ?><br>
                    <strong>银行:</strong><?php echo $vo['info']['bank']; endif; ?>

                </td>
                <td class="font12">
                    <span class="layui-badge layui-bg-black">提现额度：<?php echo $vo['money']; ?>元</span> <br />
                    <span class="layui-badge layui-bg-black">兑换金币：<?php echo $vo['gold']; ?>个</span> <br />
                </td>
                <td class="font12">
                    <?php echo date('Y-m-d H:i:s', $vo['add_time']); ?>
                    <!--<br>修改时间：<?php echo date('Y-m-d H:i:s', $vo['update_time']); ?><br></p>-->
                </td>
                <td class="font12">
                     <?php if($vo['status'] == 0): ?>
                        <span class="layui-badge layui-bg-orange">待审核</span>
                        <?php elseif($vo['status'] == 1): ?>
                        <span class="layui-badge layui-bg-green">已通过</span>
                        <?php elseif($vo['status'] == 2): ?>
                        <span class="layui-badge layui-bg-red">已拒绝</span>
                    <?php endif; ?>
                </td>
                <td>
                    <?php if($vo['status'] == 2): ?>
                        <a data-href="#" data-id="<?php echo $vo['id']; ?>"  class="layui-btn layui-btn-disabled">已处理</a>
                    <?php else: ?>
                        <a data-href="#" data-id="<?php echo $vo['id']; ?>" class="layui-btn layui-btn-primary agreen"><i class="aicon ai-qiyong"></i>审核</a>
                    <?php endif; ?>

                </td>

            </tr>
            <?php endforeach; endif; else: echo "" ;endif; ?>
            </tbody>
        </table>
        <?php echo $pages; ?>
    </div>
</form>
<script>
    layui.use(['jquery', 'laydate'], function() {
        var $ = layui.jquery, laydate = layui.laydate;
        $(".agreen").on('click',function () {
            var id=$(this).data('id');
            var url='agreen';
            layer.confirm('您确定已打款吗？', {
                icon: 0,
                btn: ['确认打款','拒绝申请','取消'] //按钮
            }, function(){
                var data={id:id,type:'1'}
                $.get(url,data,function(){

                },'json');
                layer.msg('审核通成功', {icon: 1,end:function(){
                    location.reload()
                }});
            }, function(){
                var data={id:id,type:'2'}
                $.get(url,data,function(){

                },'json');
                layer.msg('审核成功', {icon: 1,end:function(){
                    location.reload()
                }});
            });

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
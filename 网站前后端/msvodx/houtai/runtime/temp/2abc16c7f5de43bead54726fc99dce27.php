<?php if (!defined('THINK_PATH')) exit(); /*a:4:{s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/system/index.php";i:1566355644;s:52:"/www/wwwroot/msvodx/houtai/app/admin/view/layout.php";i:1527499864;s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/block/header.php";i:1564651146;s:58:"/www/wwwroot/msvodx/houtai/app/admin/view/block/footer.php";i:1564651146;}*/ ?>
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
    <form class="layui-form layui-form-pane" action="<?php echo url(''); ?>" id="editForm" method="post">
        <fieldset class="layui-elem-field layui-field-title">
            <legend>网站设置</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">网站状态</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="site_status"  lay-skin="switch" lay-text="开启|关闭" <?php if($config['site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">站点关闭后客户界面将不能访问</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">手机网站</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="wap_site_status" lay-skin="switch" lay-text="开启|关闭" <?php if($config['wap_site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">如果有手机网站，请设置为开启状态，否则只显示PC网站</div>
        </div>

        <!--<div class="layui-form-item">
            <label class="layui-form-label">是否开启伪静态</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="wap_site_status" lay-skin="switch" lay-text="开启|关闭" <?php if($config['wap_site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">开启的情况下，可以支持html等后缀的访问形式</div>
        </div>-->

        <div class="layui-form-item">
            <label class="layui-form-label">网站LOGO</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_logo_chose_btn""><?php if(empty($config['site_logo'])): ?>请上传网站Logo<?php else: ?>更改Logo<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_logo" id="site_logo" value="<?php echo $config['site_logo']; ?>">
                <img src="<?php echo $config['site_logo']; ?>" id="img_logo" onmouseover="imgTips(this,{width:400,className:'imgTips',bgColor:'#fff'})" style="display:block;border-radius:5px;border:1px solid #ccc;max-width: 200px;min-width: 200px;margin-top:5px;">
            </div>
            <div class="layui-form-mid layui-word-aux"> 网站LOGO图片</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">手机站LOGO</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_logo_chose_mobile_btn""><?php if(empty($config['site_logo_mobile'])): ?>请上传手机站Logo<?php else: ?>更改Logo<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_logo_mobile" id="site_logo_mobile" value="<?php echo $config['site_logo_mobile']; ?>">
                <img src="<?php echo $config['site_logo_mobile']; ?>" id="img_logo_mobile" onmouseover="imgTips(this,{width:400,className:'imgTips',bgColor:'#fff'})" style="display:block;border-radius:5px;border:1px solid #ccc;max-width: 200px;min-width: 200px;margin-top:5px;">
            </div>
            <div class="layui-form-mid layui-word-aux"> 推荐尺寸:210x70px(宽x高)</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站收藏图标</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_ico_chose_btn"><?php if(empty($config['site_favicon'])): ?>请上传网站图标<?php else: ?>更改图标<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_favicon" id="site_favicon" value="<?php echo $config['site_favicon']; ?>">
                <img id="img_ico" onmouseover="imgTips(this,{width:100,className:'imgTips'})" src="<?php echo $config['site_favicon']; ?>" style="border-radius:5px;border:1px solid #ccc" width="36" height="36">
            </div>
            <div class="layui-form-mid layui-word-aux"> 又叫网站收藏夹图标，它显示位于浏览器的地址栏或者标题前面，<strong class="red">.ico格式</strong>，<a href="https://www.baidu.com/s?ie=UTF-8&amp;wd=favicon" target="_blank">点此了解网站图标</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站标题</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_title" value="<?php echo $config['site_title']; ?>" autocomplete="off" placeholder="请填写网站标题">
            </div>
            <div class="layui-form-mid layui-word-aux">网站标题是体现一个网站的主旨，要做到主题突出、标题简洁、连贯等特点，建议不超过28个字</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站关键词</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_keywords" value="<?php echo $config['site_keywords']; ?>" autocomplete="off" placeholder="请填写网站关键词">
            </div>
            <div class="layui-form-mid layui-word-aux">网页内容所包含的核心搜索关键词，多个关键字请用英文逗号","分隔</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站描述</label>
            <div class="layui-input-inline">
                <textarea rows="6" class="layui-textarea" name="site_description" autocomplete="off" placeholder="请填写网站描述"  ><?php echo $config['site_description']; ?></textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">网页的描述信息，搜索引擎采纳后，作为搜索结果中的页面摘要显示，建议不超过80个字</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">ICP备案信息</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_icp" value="<?php echo $config['site_icp']; ?>" autocomplete="off" placeholder="请填写ICP备案信息">
            </div>
            <div class="layui-form-mid layui-word-aux">请填写ICP备案号，用于展示在网站底部，ICP备案官网：<a href="http://www.miibeian.gov.cn" target="_blank">http://www.miibeian.gov.cn</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">站点统计代码</label>
            <div class="layui-input-inline">
                <textarea rows="6" class="layui-textarea" name="site_statis" autocomplete="off" placeholder="请填写站点统计代码"  ><?php echo $config['site_statis']; ?></textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">第三方流量统计代码，前台调用时请先用 htmlspecialchars_decode函数转义输出</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>金币设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">金币汇率</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="gold_exchange_rate" value="<?php echo $config['gold_exchange_rate']; ?>" autocomplete="off" placeholder="金币跟人民币的比率">
            </div>
            <div class="layui-form-mid layui-word-aux">1元人民币可兑换的金币个数,如1元可兑换10金币则填写10</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否允许提现</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="is_withdrawals"  lay-skin="switch" lay-text="开启|关闭" <?php if($config['is_withdrawals'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否允许金币提现</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">提现频率</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="withdrawals_frequency" value="<?php echo $config['withdrawals_frequency']; ?>" autocomplete="off" placeholder="输入间隔小时">
            </div>
            <div class="layui-form-mid layui-word-aux">单位小时：提交了提现申请之后多久可以再次申请</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">提现最低限额</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="min_withdrawals" value="<?php echo $config['min_withdrawals']; ?>" autocomplete="off" placeholder="输入最低提现金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">申请提现最低提现金币数</div>
        </div>


        <fieldset class="layui-elem-field layui-field-title">
            <legend>奖励设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">注册奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="register_reward" value="<?php echo $config['register_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">新用户注册奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">登录奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="login_reward" value="<?php echo $config['login_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户当天首次登录奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">签到奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="sign_reward" value="<?php echo $config['sign_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户签到奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">宣传奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="propaganda_reward" value="<?php echo $config['propaganda_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户宣传奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">宣传次数</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="share_num" value="<?php echo $config['share_num']; ?>" autocomplete="off" placeholder="请填写用户宣传可获取奖励金币次数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户宣传可获取奖励金币次数</div>
        </div>
        <fieldset class="layui-elem-field layui-field-title">
            <legend>评论设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">评论功能</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="comment_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['comment_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否开启评论功能</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">评论审核</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="comment_examine_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['comment_examine_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">评论是否需要审核</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>资源审核设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">新增资源审核</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="resource_examine_on"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['resource_examine_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户上传了新资源（视频、图册、资讯）是否需要审核</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">视频</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="video_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['video_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户编辑视频信息后是否需要重新审核，如修改了标题，标签，视频地址等</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图片</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="image_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['image_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户上传了新的图片或者修改了图册信息后是否需要重新审核</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">资讯</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="novel_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['novel_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户编辑资讯信息后是否需要重新审核，如修改了标题，标签，资讯内容等</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>其他设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">注册校验</label>
            <div class="layui-input-inline">
                <select name="register_validate" class="field-role_id" type="select"   lay-skin="switch" lay-filter="look_at_measurement"  >
                    <option value="0"  <?php if($config['register_validate'] == 0): ?>selected="selected"<?php endif; ?> >不需要校验</option>
                    <option value="1"  <?php if($config['register_validate'] == 1): ?>selected="selected"<?php endif; ?>  >邮箱校验</option>
                    <option value="2"  <?php if($config['register_validate'] == 2): ?>selected="selected"<?php endif; ?>>手机短信校验</option>
                </select>
            </div>
            <div class="layui-form-mid layui-word-aux">注册账号校验方式</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">消费有效期</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="message_validity" value="<?php echo $config['message_validity']; ?>" autocomplete="off" placeholder="输入间隔小时">
            </div>
            <div class="layui-form-mid layui-word-aux">单位小时：例如，视频付费了之后，多长时间内可以免费观看</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">打赏排行</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="reward_num" value="<?php echo $config['reward_num']; ?>" autocomplete="off" placeholder="输入显示名次">
            </div>
            <div class="layui-form-mid layui-word-aux">首页打赏排行显示名次：例如首页排行榜显示前五名</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">客服地址</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="buy_cardpassword_uri" value="<?php echo $config['buy_cardpassword_uri']; ?>" autocomplete="off" placeholder="输入客服地址">
            </div>
            <div class="layui-form-mid layui-word-aux">如若不设置输入：#</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">客服QQ</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="lianxi_qq" value="<?php echo $config['lianxi_qq']; ?>" autocomplete="off" placeholder="输入客服QQ">
            </div>
            <div class="layui-form-mid layui-word-aux"></div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">验证码</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="verification_code_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['verification_code_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否开启验证码</div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <input type="hidden" class="field-id" name="id">
                <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
            </div>
        </div>
    </form>
</div>
<style type="text/css">
    .layui-form-item .layui-form-label{width:150px;}
    .layui-form-item .layui-input-inline{max-width:80%;width:auto;min-width:320px;}
    .layui-field-title:not(:first-child){margin: 30px 0}
</style>



<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script><script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>

<script>
    function afterUpLogo(resp){
        $('#img_logo').attr('src',resp.filePath);
        $('#site_logo').val(resp.filePath);
        layer.msg('上传Logo完成',{time:500});
    }

    function afterUpIcon(resp){
        $('#img_ico').attr('src',resp.filePath);
        $('#site_favicon').val(resp.filePath);
        layer.msg('上传图标完成',{time:500});
    }

    function afterUpLogoMobile(resp){
        $('#img_logo_mobile').attr('src',resp.filePath);
        $('#site_logo_mobile').val(resp.filePath);
        layer.msg('上传Logo完成',{time:500});
    }

    $(function(){
        //函数调用说明  createWebUploader(选择上传的对象id,上传按钮id,指定文件名称,文件类型,上传完成回调)
        createWebUploader('upload_logo_chose_btn','','','image',afterUpLogo);
        createWebUploader('upload_logo_chose_mobile_btn','','','image',afterUpLogoMobile);
        createWebUploader('upload_ico_chose_btn','','','ico',afterUpIcon);
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
    <form class="layui-form layui-form-pane" action="<?php echo url(''); ?>" id="editForm" method="post">
        <fieldset class="layui-elem-field layui-field-title">
            <legend>网站设置</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">网站状态</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="site_status"  lay-skin="switch" lay-text="开启|关闭" <?php if($config['site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">站点关闭后客户界面将不能访问</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">手机网站</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="wap_site_status" lay-skin="switch" lay-text="开启|关闭" <?php if($config['wap_site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">如果有手机网站，请设置为开启状态，否则只显示PC网站</div>
        </div>

        <!--<div class="layui-form-item">
            <label class="layui-form-label">是否开启伪静态</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="wap_site_status" lay-skin="switch" lay-text="开启|关闭" <?php if($config['wap_site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">开启的情况下，可以支持html等后缀的访问形式</div>
        </div>-->

        <div class="layui-form-item">
            <label class="layui-form-label">网站LOGO</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_logo_chose_btn""><?php if(empty($config['site_logo'])): ?>请上传网站Logo<?php else: ?>更改Logo<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_logo" id="site_logo" value="<?php echo $config['site_logo']; ?>">
                <img src="<?php echo $config['site_logo']; ?>" id="img_logo" onmouseover="imgTips(this,{width:400,className:'imgTips',bgColor:'#fff'})" style="display:block;border-radius:5px;border:1px solid #ccc;max-width: 200px;min-width: 200px;margin-top:5px;">
            </div>
            <div class="layui-form-mid layui-word-aux"> 网站LOGO图片</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">手机站LOGO</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_logo_chose_mobile_btn""><?php if(empty($config['site_logo_mobile'])): ?>请上传手机站Logo<?php else: ?>更改Logo<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_logo_mobile" id="site_logo_mobile" value="<?php echo $config['site_logo_mobile']; ?>">
                <img src="<?php echo $config['site_logo_mobile']; ?>" id="img_logo_mobile" onmouseover="imgTips(this,{width:400,className:'imgTips',bgColor:'#fff'})" style="display:block;border-radius:5px;border:1px solid #ccc;max-width: 200px;min-width: 200px;margin-top:5px;">
            </div>
            <div class="layui-form-mid layui-word-aux"> 推荐尺寸:210x70px(宽x高)</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站收藏图标</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_ico_chose_btn"><?php if(empty($config['site_favicon'])): ?>请上传网站图标<?php else: ?>更改图标<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_favicon" id="site_favicon" value="<?php echo $config['site_favicon']; ?>">
                <img id="img_ico" onmouseover="imgTips(this,{width:100,className:'imgTips'})" src="<?php echo $config['site_favicon']; ?>" style="border-radius:5px;border:1px solid #ccc" width="36" height="36">
            </div>
            <div class="layui-form-mid layui-word-aux"> 又叫网站收藏夹图标，它显示位于浏览器的地址栏或者标题前面，<strong class="red">.ico格式</strong>，<a href="https://www.baidu.com/s?ie=UTF-8&amp;wd=favicon" target="_blank">点此了解网站图标</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站标题</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_title" value="<?php echo $config['site_title']; ?>" autocomplete="off" placeholder="请填写网站标题">
            </div>
            <div class="layui-form-mid layui-word-aux">网站标题是体现一个网站的主旨，要做到主题突出、标题简洁、连贯等特点，建议不超过28个字</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站关键词</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_keywords" value="<?php echo $config['site_keywords']; ?>" autocomplete="off" placeholder="请填写网站关键词">
            </div>
            <div class="layui-form-mid layui-word-aux">网页内容所包含的核心搜索关键词，多个关键字请用英文逗号","分隔</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站描述</label>
            <div class="layui-input-inline">
                <textarea rows="6" class="layui-textarea" name="site_description" autocomplete="off" placeholder="请填写网站描述"  ><?php echo $config['site_description']; ?></textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">网页的描述信息，搜索引擎采纳后，作为搜索结果中的页面摘要显示，建议不超过80个字</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">ICP备案信息</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_icp" value="<?php echo $config['site_icp']; ?>" autocomplete="off" placeholder="请填写ICP备案信息">
            </div>
            <div class="layui-form-mid layui-word-aux">请填写ICP备案号，用于展示在网站底部，ICP备案官网：<a href="http://www.miibeian.gov.cn" target="_blank">http://www.miibeian.gov.cn</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">站点统计代码</label>
            <div class="layui-input-inline">
                <textarea rows="6" class="layui-textarea" name="site_statis" autocomplete="off" placeholder="请填写站点统计代码"  ><?php echo $config['site_statis']; ?></textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">第三方流量统计代码，前台调用时请先用 htmlspecialchars_decode函数转义输出</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>金币设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">金币汇率</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="gold_exchange_rate" value="<?php echo $config['gold_exchange_rate']; ?>" autocomplete="off" placeholder="金币跟人民币的比率">
            </div>
            <div class="layui-form-mid layui-word-aux">1元人民币可兑换的金币个数,如1元可兑换10金币则填写10</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否允许提现</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="is_withdrawals"  lay-skin="switch" lay-text="开启|关闭" <?php if($config['is_withdrawals'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否允许金币提现</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">提现频率</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="withdrawals_frequency" value="<?php echo $config['withdrawals_frequency']; ?>" autocomplete="off" placeholder="输入间隔小时">
            </div>
            <div class="layui-form-mid layui-word-aux">单位小时：提交了提现申请之后多久可以再次申请</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">提现最低限额</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="min_withdrawals" value="<?php echo $config['min_withdrawals']; ?>" autocomplete="off" placeholder="输入最低提现金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">申请提现最低提现金币数</div>
        </div>


        <fieldset class="layui-elem-field layui-field-title">
            <legend>奖励设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">注册奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="register_reward" value="<?php echo $config['register_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">新用户注册奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">登录奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="login_reward" value="<?php echo $config['login_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户当天首次登录奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">签到奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="sign_reward" value="<?php echo $config['sign_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户签到奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">宣传奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="propaganda_reward" value="<?php echo $config['propaganda_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户宣传奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">宣传次数</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="share_num" value="<?php echo $config['share_num']; ?>" autocomplete="off" placeholder="请填写用户宣传可获取奖励金币次数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户宣传可获取奖励金币次数</div>
        </div>
        <fieldset class="layui-elem-field layui-field-title">
            <legend>评论设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">评论功能</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="comment_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['comment_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否开启评论功能</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">评论审核</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="comment_examine_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['comment_examine_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">评论是否需要审核</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>资源审核设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">新增资源审核</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="resource_examine_on"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['resource_examine_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户上传了新资源（视频、图册、资讯）是否需要审核</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">视频</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="video_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['video_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户编辑视频信息后是否需要重新审核，如修改了标题，标签，视频地址等</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图片</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="image_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['image_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户上传了新的图片或者修改了图册信息后是否需要重新审核</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">资讯</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="novel_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['novel_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户编辑资讯信息后是否需要重新审核，如修改了标题，标签，资讯内容等</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>其他设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">注册校验</label>
            <div class="layui-input-inline">
                <select name="register_validate" class="field-role_id" type="select"   lay-skin="switch" lay-filter="look_at_measurement"  >
                    <option value="0"  <?php if($config['register_validate'] == 0): ?>selected="selected"<?php endif; ?> >不需要校验</option>
                    <option value="1"  <?php if($config['register_validate'] == 1): ?>selected="selected"<?php endif; ?>  >邮箱校验</option>
                    <option value="2"  <?php if($config['register_validate'] == 2): ?>selected="selected"<?php endif; ?>>手机短信校验</option>
                </select>
            </div>
            <div class="layui-form-mid layui-word-aux">注册账号校验方式</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">消费有效期</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="message_validity" value="<?php echo $config['message_validity']; ?>" autocomplete="off" placeholder="输入间隔小时">
            </div>
            <div class="layui-form-mid layui-word-aux">单位小时：例如，视频付费了之后，多长时间内可以免费观看</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">打赏排行</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="reward_num" value="<?php echo $config['reward_num']; ?>" autocomplete="off" placeholder="输入显示名次">
            </div>
            <div class="layui-form-mid layui-word-aux">首页打赏排行显示名次：例如首页排行榜显示前五名</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">客服地址</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="buy_cardpassword_uri" value="<?php echo $config['buy_cardpassword_uri']; ?>" autocomplete="off" placeholder="输入客服地址">
            </div>
            <div class="layui-form-mid layui-word-aux">如若不设置输入：#</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">客服QQ</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="lianxi_qq" value="<?php echo $config['lianxi_qq']; ?>" autocomplete="off" placeholder="输入客服QQ">
            </div>
            <div class="layui-form-mid layui-word-aux"></div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">验证码</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="verification_code_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['verification_code_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否开启验证码</div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <input type="hidden" class="field-id" name="id">
                <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
            </div>
        </div>
    </form>
</div>
<style type="text/css">
    .layui-form-item .layui-form-label{width:150px;}
    .layui-form-item .layui-input-inline{max-width:80%;width:auto;min-width:320px;}
    .layui-field-title:not(:first-child){margin: 30px 0}
</style>



<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script><script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>

<script>
    function afterUpLogo(resp){
        $('#img_logo').attr('src',resp.filePath);
        $('#site_logo').val(resp.filePath);
        layer.msg('上传Logo完成',{time:500});
    }

    function afterUpIcon(resp){
        $('#img_ico').attr('src',resp.filePath);
        $('#site_favicon').val(resp.filePath);
        layer.msg('上传图标完成',{time:500});
    }

    function afterUpLogoMobile(resp){
        $('#img_logo_mobile').attr('src',resp.filePath);
        $('#site_logo_mobile').val(resp.filePath);
        layer.msg('上传Logo完成',{time:500});
    }

    $(function(){
        //函数调用说明  createWebUploader(选择上传的对象id,上传按钮id,指定文件名称,文件类型,上传完成回调)
        createWebUploader('upload_logo_chose_btn','','','image',afterUpLogo);
        createWebUploader('upload_logo_chose_mobile_btn','','','image',afterUpLogoMobile);
        createWebUploader('upload_ico_chose_btn','','','ico',afterUpIcon);
    });
</script>
            </div>
        </div>
    <?php break; case "3": ?>
    
        <div class="layui-tab-item layui-show">
    <form class="layui-form layui-form-pane" action="<?php echo url(''); ?>" id="editForm" method="post">
        <fieldset class="layui-elem-field layui-field-title">
            <legend>网站设置</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">网站状态</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="site_status"  lay-skin="switch" lay-text="开启|关闭" <?php if($config['site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">站点关闭后客户界面将不能访问</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">手机网站</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="wap_site_status" lay-skin="switch" lay-text="开启|关闭" <?php if($config['wap_site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">如果有手机网站，请设置为开启状态，否则只显示PC网站</div>
        </div>

        <!--<div class="layui-form-item">
            <label class="layui-form-label">是否开启伪静态</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="wap_site_status" lay-skin="switch" lay-text="开启|关闭" <?php if($config['wap_site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">开启的情况下，可以支持html等后缀的访问形式</div>
        </div>-->

        <div class="layui-form-item">
            <label class="layui-form-label">网站LOGO</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_logo_chose_btn""><?php if(empty($config['site_logo'])): ?>请上传网站Logo<?php else: ?>更改Logo<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_logo" id="site_logo" value="<?php echo $config['site_logo']; ?>">
                <img src="<?php echo $config['site_logo']; ?>" id="img_logo" onmouseover="imgTips(this,{width:400,className:'imgTips',bgColor:'#fff'})" style="display:block;border-radius:5px;border:1px solid #ccc;max-width: 200px;min-width: 200px;margin-top:5px;">
            </div>
            <div class="layui-form-mid layui-word-aux"> 网站LOGO图片</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">手机站LOGO</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_logo_chose_mobile_btn""><?php if(empty($config['site_logo_mobile'])): ?>请上传手机站Logo<?php else: ?>更改Logo<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_logo_mobile" id="site_logo_mobile" value="<?php echo $config['site_logo_mobile']; ?>">
                <img src="<?php echo $config['site_logo_mobile']; ?>" id="img_logo_mobile" onmouseover="imgTips(this,{width:400,className:'imgTips',bgColor:'#fff'})" style="display:block;border-radius:5px;border:1px solid #ccc;max-width: 200px;min-width: 200px;margin-top:5px;">
            </div>
            <div class="layui-form-mid layui-word-aux"> 推荐尺寸:210x70px(宽x高)</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站收藏图标</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_ico_chose_btn"><?php if(empty($config['site_favicon'])): ?>请上传网站图标<?php else: ?>更改图标<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_favicon" id="site_favicon" value="<?php echo $config['site_favicon']; ?>">
                <img id="img_ico" onmouseover="imgTips(this,{width:100,className:'imgTips'})" src="<?php echo $config['site_favicon']; ?>" style="border-radius:5px;border:1px solid #ccc" width="36" height="36">
            </div>
            <div class="layui-form-mid layui-word-aux"> 又叫网站收藏夹图标，它显示位于浏览器的地址栏或者标题前面，<strong class="red">.ico格式</strong>，<a href="https://www.baidu.com/s?ie=UTF-8&amp;wd=favicon" target="_blank">点此了解网站图标</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站标题</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_title" value="<?php echo $config['site_title']; ?>" autocomplete="off" placeholder="请填写网站标题">
            </div>
            <div class="layui-form-mid layui-word-aux">网站标题是体现一个网站的主旨，要做到主题突出、标题简洁、连贯等特点，建议不超过28个字</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站关键词</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_keywords" value="<?php echo $config['site_keywords']; ?>" autocomplete="off" placeholder="请填写网站关键词">
            </div>
            <div class="layui-form-mid layui-word-aux">网页内容所包含的核心搜索关键词，多个关键字请用英文逗号","分隔</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站描述</label>
            <div class="layui-input-inline">
                <textarea rows="6" class="layui-textarea" name="site_description" autocomplete="off" placeholder="请填写网站描述"  ><?php echo $config['site_description']; ?></textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">网页的描述信息，搜索引擎采纳后，作为搜索结果中的页面摘要显示，建议不超过80个字</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">ICP备案信息</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_icp" value="<?php echo $config['site_icp']; ?>" autocomplete="off" placeholder="请填写ICP备案信息">
            </div>
            <div class="layui-form-mid layui-word-aux">请填写ICP备案号，用于展示在网站底部，ICP备案官网：<a href="http://www.miibeian.gov.cn" target="_blank">http://www.miibeian.gov.cn</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">站点统计代码</label>
            <div class="layui-input-inline">
                <textarea rows="6" class="layui-textarea" name="site_statis" autocomplete="off" placeholder="请填写站点统计代码"  ><?php echo $config['site_statis']; ?></textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">第三方流量统计代码，前台调用时请先用 htmlspecialchars_decode函数转义输出</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>金币设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">金币汇率</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="gold_exchange_rate" value="<?php echo $config['gold_exchange_rate']; ?>" autocomplete="off" placeholder="金币跟人民币的比率">
            </div>
            <div class="layui-form-mid layui-word-aux">1元人民币可兑换的金币个数,如1元可兑换10金币则填写10</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否允许提现</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="is_withdrawals"  lay-skin="switch" lay-text="开启|关闭" <?php if($config['is_withdrawals'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否允许金币提现</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">提现频率</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="withdrawals_frequency" value="<?php echo $config['withdrawals_frequency']; ?>" autocomplete="off" placeholder="输入间隔小时">
            </div>
            <div class="layui-form-mid layui-word-aux">单位小时：提交了提现申请之后多久可以再次申请</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">提现最低限额</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="min_withdrawals" value="<?php echo $config['min_withdrawals']; ?>" autocomplete="off" placeholder="输入最低提现金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">申请提现最低提现金币数</div>
        </div>


        <fieldset class="layui-elem-field layui-field-title">
            <legend>奖励设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">注册奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="register_reward" value="<?php echo $config['register_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">新用户注册奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">登录奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="login_reward" value="<?php echo $config['login_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户当天首次登录奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">签到奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="sign_reward" value="<?php echo $config['sign_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户签到奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">宣传奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="propaganda_reward" value="<?php echo $config['propaganda_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户宣传奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">宣传次数</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="share_num" value="<?php echo $config['share_num']; ?>" autocomplete="off" placeholder="请填写用户宣传可获取奖励金币次数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户宣传可获取奖励金币次数</div>
        </div>
        <fieldset class="layui-elem-field layui-field-title">
            <legend>评论设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">评论功能</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="comment_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['comment_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否开启评论功能</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">评论审核</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="comment_examine_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['comment_examine_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">评论是否需要审核</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>资源审核设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">新增资源审核</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="resource_examine_on"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['resource_examine_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户上传了新资源（视频、图册、资讯）是否需要审核</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">视频</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="video_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['video_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户编辑视频信息后是否需要重新审核，如修改了标题，标签，视频地址等</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图片</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="image_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['image_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户上传了新的图片或者修改了图册信息后是否需要重新审核</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">资讯</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="novel_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['novel_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户编辑资讯信息后是否需要重新审核，如修改了标题，标签，资讯内容等</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>其他设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">注册校验</label>
            <div class="layui-input-inline">
                <select name="register_validate" class="field-role_id" type="select"   lay-skin="switch" lay-filter="look_at_measurement"  >
                    <option value="0"  <?php if($config['register_validate'] == 0): ?>selected="selected"<?php endif; ?> >不需要校验</option>
                    <option value="1"  <?php if($config['register_validate'] == 1): ?>selected="selected"<?php endif; ?>  >邮箱校验</option>
                    <option value="2"  <?php if($config['register_validate'] == 2): ?>selected="selected"<?php endif; ?>>手机短信校验</option>
                </select>
            </div>
            <div class="layui-form-mid layui-word-aux">注册账号校验方式</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">消费有效期</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="message_validity" value="<?php echo $config['message_validity']; ?>" autocomplete="off" placeholder="输入间隔小时">
            </div>
            <div class="layui-form-mid layui-word-aux">单位小时：例如，视频付费了之后，多长时间内可以免费观看</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">打赏排行</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="reward_num" value="<?php echo $config['reward_num']; ?>" autocomplete="off" placeholder="输入显示名次">
            </div>
            <div class="layui-form-mid layui-word-aux">首页打赏排行显示名次：例如首页排行榜显示前五名</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">客服地址</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="buy_cardpassword_uri" value="<?php echo $config['buy_cardpassword_uri']; ?>" autocomplete="off" placeholder="输入客服地址">
            </div>
            <div class="layui-form-mid layui-word-aux">如若不设置输入：#</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">客服QQ</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="lianxi_qq" value="<?php echo $config['lianxi_qq']; ?>" autocomplete="off" placeholder="输入客服QQ">
            </div>
            <div class="layui-form-mid layui-word-aux"></div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">验证码</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="verification_code_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['verification_code_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否开启验证码</div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <input type="hidden" class="field-id" name="id">
                <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
            </div>
        </div>
    </form>
</div>
<style type="text/css">
    .layui-form-item .layui-form-label{width:150px;}
    .layui-form-item .layui-input-inline{max-width:80%;width:auto;min-width:320px;}
    .layui-field-title:not(:first-child){margin: 30px 0}
</style>



<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script><script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>

<script>
    function afterUpLogo(resp){
        $('#img_logo').attr('src',resp.filePath);
        $('#site_logo').val(resp.filePath);
        layer.msg('上传Logo完成',{time:500});
    }

    function afterUpIcon(resp){
        $('#img_ico').attr('src',resp.filePath);
        $('#site_favicon').val(resp.filePath);
        layer.msg('上传图标完成',{time:500});
    }

    function afterUpLogoMobile(resp){
        $('#img_logo_mobile').attr('src',resp.filePath);
        $('#site_logo_mobile').val(resp.filePath);
        layer.msg('上传Logo完成',{time:500});
    }

    $(function(){
        //函数调用说明  createWebUploader(选择上传的对象id,上传按钮id,指定文件名称,文件类型,上传完成回调)
        createWebUploader('upload_logo_chose_btn','','','image',afterUpLogo);
        createWebUploader('upload_logo_chose_mobile_btn','','','image',afterUpLogoMobile);
        createWebUploader('upload_ico_chose_btn','','','ico',afterUpIcon);
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
    <form class="layui-form layui-form-pane" action="<?php echo url(''); ?>" id="editForm" method="post">
        <fieldset class="layui-elem-field layui-field-title">
            <legend>网站设置</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">网站状态</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="site_status"  lay-skin="switch" lay-text="开启|关闭" <?php if($config['site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">站点关闭后客户界面将不能访问</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">手机网站</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="wap_site_status" lay-skin="switch" lay-text="开启|关闭" <?php if($config['wap_site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">如果有手机网站，请设置为开启状态，否则只显示PC网站</div>
        </div>

        <!--<div class="layui-form-item">
            <label class="layui-form-label">是否开启伪静态</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="wap_site_status" lay-skin="switch" lay-text="开启|关闭" <?php if($config['wap_site_status'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">开启的情况下，可以支持html等后缀的访问形式</div>
        </div>-->

        <div class="layui-form-item">
            <label class="layui-form-label">网站LOGO</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_logo_chose_btn""><?php if(empty($config['site_logo'])): ?>请上传网站Logo<?php else: ?>更改Logo<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_logo" id="site_logo" value="<?php echo $config['site_logo']; ?>">
                <img src="<?php echo $config['site_logo']; ?>" id="img_logo" onmouseover="imgTips(this,{width:400,className:'imgTips',bgColor:'#fff'})" style="display:block;border-radius:5px;border:1px solid #ccc;max-width: 200px;min-width: 200px;margin-top:5px;">
            </div>
            <div class="layui-form-mid layui-word-aux"> 网站LOGO图片</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">手机站LOGO</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_logo_chose_mobile_btn""><?php if(empty($config['site_logo_mobile'])): ?>请上传手机站Logo<?php else: ?>更改Logo<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_logo_mobile" id="site_logo_mobile" value="<?php echo $config['site_logo_mobile']; ?>">
                <img src="<?php echo $config['site_logo_mobile']; ?>" id="img_logo_mobile" onmouseover="imgTips(this,{width:400,className:'imgTips',bgColor:'#fff'})" style="display:block;border-radius:5px;border:1px solid #ccc;max-width: 200px;min-width: 200px;margin-top:5px;">
            </div>
            <div class="layui-form-mid layui-word-aux"> 推荐尺寸:210x70px(宽x高)</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站收藏图标</label>
            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload"  id="upload_ico_chose_btn"><?php if(empty($config['site_favicon'])): ?>请上传网站图标<?php else: ?>更改图标<?php endif; ?></button>
                <input type="hidden" class="upload-input" name="site_favicon" id="site_favicon" value="<?php echo $config['site_favicon']; ?>">
                <img id="img_ico" onmouseover="imgTips(this,{width:100,className:'imgTips'})" src="<?php echo $config['site_favicon']; ?>" style="border-radius:5px;border:1px solid #ccc" width="36" height="36">
            </div>
            <div class="layui-form-mid layui-word-aux"> 又叫网站收藏夹图标，它显示位于浏览器的地址栏或者标题前面，<strong class="red">.ico格式</strong>，<a href="https://www.baidu.com/s?ie=UTF-8&amp;wd=favicon" target="_blank">点此了解网站图标</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站标题</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_title" value="<?php echo $config['site_title']; ?>" autocomplete="off" placeholder="请填写网站标题">
            </div>
            <div class="layui-form-mid layui-word-aux">网站标题是体现一个网站的主旨，要做到主题突出、标题简洁、连贯等特点，建议不超过28个字</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站关键词</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_keywords" value="<?php echo $config['site_keywords']; ?>" autocomplete="off" placeholder="请填写网站关键词">
            </div>
            <div class="layui-form-mid layui-word-aux">网页内容所包含的核心搜索关键词，多个关键字请用英文逗号","分隔</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">网站描述</label>
            <div class="layui-input-inline">
                <textarea rows="6" class="layui-textarea" name="site_description" autocomplete="off" placeholder="请填写网站描述"  ><?php echo $config['site_description']; ?></textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">网页的描述信息，搜索引擎采纳后，作为搜索结果中的页面摘要显示，建议不超过80个字</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">ICP备案信息</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="site_icp" value="<?php echo $config['site_icp']; ?>" autocomplete="off" placeholder="请填写ICP备案信息">
            </div>
            <div class="layui-form-mid layui-word-aux">请填写ICP备案号，用于展示在网站底部，ICP备案官网：<a href="http://www.miibeian.gov.cn" target="_blank">http://www.miibeian.gov.cn</a></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">站点统计代码</label>
            <div class="layui-input-inline">
                <textarea rows="6" class="layui-textarea" name="site_statis" autocomplete="off" placeholder="请填写站点统计代码"  ><?php echo $config['site_statis']; ?></textarea>
            </div>
            <div class="layui-form-mid layui-word-aux">第三方流量统计代码，前台调用时请先用 htmlspecialchars_decode函数转义输出</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>金币设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">金币汇率</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="gold_exchange_rate" value="<?php echo $config['gold_exchange_rate']; ?>" autocomplete="off" placeholder="金币跟人民币的比率">
            </div>
            <div class="layui-form-mid layui-word-aux">1元人民币可兑换的金币个数,如1元可兑换10金币则填写10</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否允许提现</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="is_withdrawals"  lay-skin="switch" lay-text="开启|关闭" <?php if($config['is_withdrawals'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否允许金币提现</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">提现频率</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="withdrawals_frequency" value="<?php echo $config['withdrawals_frequency']; ?>" autocomplete="off" placeholder="输入间隔小时">
            </div>
            <div class="layui-form-mid layui-word-aux">单位小时：提交了提现申请之后多久可以再次申请</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">提现最低限额</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="min_withdrawals" value="<?php echo $config['min_withdrawals']; ?>" autocomplete="off" placeholder="输入最低提现金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">申请提现最低提现金币数</div>
        </div>


        <fieldset class="layui-elem-field layui-field-title">
            <legend>奖励设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">注册奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="register_reward" value="<?php echo $config['register_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">新用户注册奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">登录奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="login_reward" value="<?php echo $config['login_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户当天首次登录奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">签到奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="sign_reward" value="<?php echo $config['sign_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户签到奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">宣传奖励</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="propaganda_reward" value="<?php echo $config['propaganda_reward']; ?>" autocomplete="off" placeholder="请填写奖励金币数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户宣传奖励多少金币，可空</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">宣传次数</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="share_num" value="<?php echo $config['share_num']; ?>" autocomplete="off" placeholder="请填写用户宣传可获取奖励金币次数">
            </div>
            <div class="layui-form-mid layui-word-aux">用户宣传可获取奖励金币次数</div>
        </div>
        <fieldset class="layui-elem-field layui-field-title">
            <legend>评论设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">评论功能</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="comment_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['comment_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否开启评论功能</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">评论审核</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="comment_examine_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['comment_examine_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">评论是否需要审核</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>资源审核设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">新增资源审核</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="resource_examine_on"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['resource_examine_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户上传了新资源（视频、图册、资讯）是否需要审核</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">视频</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="video_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['video_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户编辑视频信息后是否需要重新审核，如修改了标题，标签，视频地址等</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图片</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="image_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['image_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户上传了新的图片或者修改了图册信息后是否需要重新审核</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">资讯</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="novel_reexamination"   lay-skin="switch" lay-text="需要|不用"  <?php if($config['novel_reexamination'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">客户编辑资讯信息后是否需要重新审核，如修改了标题，标签，资讯内容等</div>
        </div>

        <fieldset class="layui-elem-field layui-field-title">
            <legend>其他设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">注册校验</label>
            <div class="layui-input-inline">
                <select name="register_validate" class="field-role_id" type="select"   lay-skin="switch" lay-filter="look_at_measurement"  >
                    <option value="0"  <?php if($config['register_validate'] == 0): ?>selected="selected"<?php endif; ?> >不需要校验</option>
                    <option value="1"  <?php if($config['register_validate'] == 1): ?>selected="selected"<?php endif; ?>  >邮箱校验</option>
                    <option value="2"  <?php if($config['register_validate'] == 2): ?>selected="selected"<?php endif; ?>>手机短信校验</option>
                </select>
            </div>
            <div class="layui-form-mid layui-word-aux">注册账号校验方式</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">消费有效期</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="message_validity" value="<?php echo $config['message_validity']; ?>" autocomplete="off" placeholder="输入间隔小时">
            </div>
            <div class="layui-form-mid layui-word-aux">单位小时：例如，视频付费了之后，多长时间内可以免费观看</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">打赏排行</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="reward_num" value="<?php echo $config['reward_num']; ?>" autocomplete="off" placeholder="输入显示名次">
            </div>
            <div class="layui-form-mid layui-word-aux">首页打赏排行显示名次：例如首页排行榜显示前五名</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">客服地址</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="buy_cardpassword_uri" value="<?php echo $config['buy_cardpassword_uri']; ?>" autocomplete="off" placeholder="输入客服地址">
            </div>
            <div class="layui-form-mid layui-word-aux">如若不设置输入：#</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">客服QQ</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="lianxi_qq" value="<?php echo $config['lianxi_qq']; ?>" autocomplete="off" placeholder="输入客服QQ">
            </div>
            <div class="layui-form-mid layui-word-aux"></div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label">验证码</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="verification_code_on"   lay-skin="switch" lay-text="开启|关闭"  <?php if($config['verification_code_on'] == 1): ?>checked=""<?php endif; ?>>
            </div>
            <div class="layui-form-mid layui-word-aux">是否开启验证码</div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <input type="hidden" class="field-id" name="id">
                <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
            </div>
        </div>
    </form>
</div>
<style type="text/css">
    .layui-form-item .layui-form-label{width:150px;}
    .layui-form-item .layui-input-inline{max-width:80%;width:auto;min-width:320px;}
    .layui-field-title:not(:first-child){margin: 30px 0}
</style>



<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script><script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>

<script>
    function afterUpLogo(resp){
        $('#img_logo').attr('src',resp.filePath);
        $('#site_logo').val(resp.filePath);
        layer.msg('上传Logo完成',{time:500});
    }

    function afterUpIcon(resp){
        $('#img_ico').attr('src',resp.filePath);
        $('#site_favicon').val(resp.filePath);
        layer.msg('上传图标完成',{time:500});
    }

    function afterUpLogoMobile(resp){
        $('#img_logo_mobile').attr('src',resp.filePath);
        $('#site_logo_mobile').val(resp.filePath);
        layer.msg('上传Logo完成',{time:500});
    }

    $(function(){
        //函数调用说明  createWebUploader(选择上传的对象id,上传按钮id,指定文件名称,文件类型,上传完成回调)
        createWebUploader('upload_logo_chose_btn','','','image',afterUpLogo);
        createWebUploader('upload_logo_chose_mobile_btn','','','image',afterUpLogoMobile);
        createWebUploader('upload_ico_chose_btn','','','ico',afterUpIcon);
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
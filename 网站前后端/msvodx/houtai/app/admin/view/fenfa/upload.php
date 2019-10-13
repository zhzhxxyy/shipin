<link rel="stylesheet" type="text/css" href="/static/zmup/css/upload.css" />
<script type="text/javascript" src="/static/zmup/js/jquery.js"></script>
<script type="text/javascript" src="/static/zmup/js/webuploader.js"></script>
<script type="text/javascript" src="/static/zmup/js/md5.js"></script>
<script type="text/javascript" src="/static/zmup/js/upload.js"></script>


<script type="text/javascript" src="/static/zmup/js/jquery.xdomainrequest.min.js"></script>
<script language="javascript">
    <?php
    $yzm_url=x_get_webseting('yzm_upload_url');
    ?>
    //上传地址
    var ServerUrl = "{$yzm_url}/uploads";
</script>
<style type="text/css">
    .layui-form-item .layui-input-inline{max-width:80%;width:auto;min-width:260px;}
    .layui-form-mid{padding:0!important;}
    .layui-form-mid code{color:#5FB878;}
    dl.layui-anim.layui-anim-upbit{z-index:1000;}
</style>
<div style="display:block;width:100%;overflow:hidden;">
    {:runhook('system_admin_index')}
</div>
<form action="#" class="page-list-form layui-form layui-form-pane" method="post">
    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">APP名称：</label>
        <div class="layui-input-inline">
            <input type="text" id="app_name" class="layui-input" name="video[app_name]" value="" autocomplete="off" placeholder="请填写">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">APP域名：</label>
        <div class="layui-input-inline">
            <input type="text" id="app_domain" class="layui-input" name="video[app_domain]" value="" autocomplete="off" placeholder="请填写">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">APP图标：</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" name="video[app_icon]" id="app_icon"  value="" autocomplete="off"  placeholder="APP图标">
        </div>
        <div class="layui-input-inline">
            <a id="icon_up_btn" href="javascript:" class="layui-btn layui-btn-primary" style="background-color: #fff;">上传</a>
            &nbsp;&nbsp; <img onmouseout="layer.closeAll();"  onmouseover="imgTips(this,{width:320})" style="border-radius:5px;border:1px solid #ccc;"  height="36" id="app_icon_thumb" src="/static/images/images_default.png">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">IOS bundle：</label>
        <div class="layui-input-inline">
            <input type="text" id="app_bid_ios" class="layui-input" name="video[app_bid_ios]" value="" autocomplete="off" placeholder="请填写IOS bundle">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">IOS版本号：</label>
        <div class="layui-input-inline">
            <input type="text" id="app_bsvs_ios" class="layui-input" name="video[app_bsvs_ios]" value="" autocomplete="off" placeholder="请填写IOS版本号">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">IOS文件路径：</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" name="video[app_plist_ios]"  id="app_plist_ios" value="" autocomplete="off"  placeholder="IOS文件路径">
        </div>
        <div class="layui-input-inline">
            <a id="ipa_up_btn" href="javascript:" class="layui-btn layui-btn-primary" style="background-color: #fff;">上传</a>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">Android bundle：</label>
        <div class="layui-input-inline">
            <input type="text" id="app_bid_android" class="layui-input" name="video[app_bid_android]" value="" autocomplete="off" placeholder="请填写Android bundle">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">Android版本号：</label>
        <div class="layui-input-inline">
            <input type="text" id="app_bsvs_android" class="layui-input" name="video[app_bsvs_android]" value="" autocomplete="off" placeholder="请填写Android版本号">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">Android文件路径：</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" name="video[app_plist_android]"  id="app_plist_android" value="" autocomplete="off"  placeholder="Android文件路径">
        </div>
        <div class="layui-input-inline">
            <a id="apk_up_btn" href="javascript:" class="layui-btn layui-btn-primary" style="background-color: #fff;">上传</a>
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-input-block">
            <input type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit" class="layui-btn"/>
        </div>
    </div>
</form>

<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script><script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>
<script>

    function afterUpIcon(resp){
        $('#app_icon_thumb').attr('src',resp.filePath);
        $('#app_icon').val(resp.filePath);
        layer.msg('上传图标完成',{time:500});
    }

    function afterApk(resp){
        $('#app_plist_android').val(resp.filePath);
        layer.msg('安装安装包上传完成',{time:500});
    }

    function afterIpa(resp){
        $('#app_plist_ios').val(resp.filePath);
        layer.msg('苹果安装包上传完成',{time:500});
    }
    $(function(){
        createWebUploader('icon_up_btn','','','image',afterUpIcon);
        createWebUploader('apk_up_btn','','','apk',afterApk);
        createWebUploader('ipa_up_btn','','','ipa',afterIpa);
    });
</script>



<div class="layui-tab-item layui-show">
    <form class="layui-form layui-form-pane" action="{:url('')}" id="editForm" method="post">
        <!--
        <fieldset class="layui-elem-field layui-field-title">
            <legend>添加banner</legend>
        </fieldset>-->
        <div class="layui-form-item">
            <label class="layui-form-label">标题</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="name" lay-verify="required" value="" autocomplete="off" placeholder="请填写标题">
            </div>
            <div class="layui-form-mid layui-word-aux"></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">banner图</label>

            <div class="layui-input-inline upload">
                <button type="button" name="upload" class="layui-btn layui-btn-primary layui-upload" id="banner_chose_btn">上传图片</button>
                <input type="hidden" class="upload-input" name="images_url" id="images_url" value="">
                <img onmouseout="layer.closeAll();"  onmouseover="imgTips(this)" style="display:none;border-radius:5px;border:1px solid #ccc;"  height="36" id="img_banner">

            </div>

        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">外链地址</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" name="url" value="" autocomplete="off" placeholder="请填外链地址">
            </div>
            <div class="layui-form-mid layui-word-aux">点击banner图转跳的网址</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">新页打开</label>
            <div class="layui-input-inline">
                <input type="radio" name="target" value="1" title="是" checked>
                <input type="radio" name="target" value="0" title="否">
            </div>
            <div class="layui-form-mid layui-word-aux">点击banner图新窗口打开</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">排序</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input field-nick" name="sort" lay-verify="required" autocomplete="off" value="0">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label layui-bg-gray">说明</label>
            <div class="layui-input-inline">
                <textarea rows="6"  class="layui-textarea" name="info" autocomplete="off" placeholder="请填写说明"></textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">提交</button>
            </div>
        </div>
    </form>
</div>
<style type="text/css">
    .layui-form-item .layui-form-label{width:150px;}
    .layui-form-item .layui-input-inline{max-width:80%;width:auto;min-width:320px;}
    .layui-field-title:not(:first-child){margin: 30px 0}
    td{
        border-right: dashed 1px #c7c7c7;
        text-align:center;
    }
    .layui-layer-tips{
        width: 830px!important;
    }

</style>

<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script><script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>
<script>
    function afterUpLogo(resp){
        $('#img_banner').attr('src',resp.filePath);
        $('#img_banner').show();
        $('#images_url').val(resp.filePath);
    }

    $(function(){
        createWebUploader('banner_chose_btn','','','image',afterUpLogo);
    });

</script>
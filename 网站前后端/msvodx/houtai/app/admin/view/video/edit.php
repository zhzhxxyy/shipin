
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
        <label class="layui-form-label layui-bg-gray">视频标题：</label>
        <div class="layui-input-inline">
            <input type="text" id="title" class="layui-input" name="video[title]" value="{$videoinfo['title']}" autocomplete="off" placeholder="请填写">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">视频推荐：</label>
        <div class="layui-input-inline">
            <select name="video[reco]" class="field-pid" type="select" lay-filter="pai">
                <option value="0" level="0" {if condition="0 eq $videoinfo['reco']"}selected="selected"{/if} >视频推荐</option>
                <option value="1" level="1" {if condition="1 eq $videoinfo['reco']"}selected="selected"{/if} >★☆☆☆☆</option>
                <option value="2" level="2" {if condition="2 eq $videoinfo['reco']"}selected="selected"{/if} >★★☆☆☆</option>
                <option value="3" level="3" {if condition="3 eq $videoinfo['reco']"}selected="selected"{/if} >★★★☆☆</option>
                <option value="4" level="4" {if condition="4 eq $videoinfo['reco']"}selected="selected"{/if} >★★★★☆</option>
                <option value="5" level="5" {if condition="5 eq $videoinfo['reco']"}selected="selected"{/if} >★★★★★</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">观看金币：</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" name="video[gold]" value="{$videoinfo['gold']}" autocomplete="off" >
        </div>
        <div class="layui-form-mid layui-word-aux"> *非会员观看需要支付金币</div>
    </div>
<!--    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">关键字：</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" name="video[key_word]" value="{$videoinfo['key_word']}" autocomplete="off" placeholder="请填写">
        </div>
    </div> -->
    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">视频标签</label>
        <div class="layui-input-inline" style="width: 50%;">
            {volist name="tag_result" id="v"}
            <input type="checkbox"  class="layui-checkbox checkbox-ids"  name="tag[]" {if in_array($v['id'],$videoinfo['tag'])}checked="checked" {/if} value="{$v['id']}" title="{$v['name']}">
            {/volist}
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">视频分类：</label>
        <div class="layui-input-inline">
            <select name="video[class]" class="field-pid" type="select" lay-filter="pai">
                {volist name="classlist" id="v" }
                <option value="{$v['id']}" level="{$v['id']}" {if condition="$v['id'] eq $videoinfo['class']"}selected="selected"{/if} >|-{$v['name']}</option>
                {volist name="v['childs']" id="vv" }
                <option value="{$vv['id']}" level="{$vv['id']}" {if condition="$vv['id'] eq $videoinfo['class']"}selected="selected"{/if} >&nbsp;&nbsp;&nbsp;&nbsp;|-{$vv['name']}</option>
                {/volist}
                {/volist}
            </select>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">上传视频：</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" name="video[url]"  id="odownpath1" value="{$videoinfo['url']}" autocomplete="off">
        </div>
        <div id="yzm_panel" class="layui-input-inline" style="display: none">
            <label  id="chosevideo" style="display: none">上传视频</label>
        </div>
        <div class="layui-input-inline">
           <a id="video_up_btn" href="javascript:" class="layui-btn layui-btn-primary" style="background-color: #fff;">更换视频</a>
        </div>
    </div>

    <div class="layui-form-item" id="yzm_file_list" style="width: 450px;font-size:12px!important;color:grey"></div>
    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">下载链接：</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" name="video[download_url]" id="downpath1" value="{$videoinfo['download_url']}" autocomplete="off" >
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">缩略图：</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" name="video[thumbnail]"  id="titlepic"  value="{$videoinfo['thumbnail']}" autocomplete="off" >
        </div>
        <div class="layui-input-inline">
            <a id="video_thumb_up_btn" href="javascript:" class="layui-btn layui-btn-primary" style="background-color: #fff;">更换</a>
            &nbsp;&nbsp; <img onmouseout="layer.closeAll();"  onmouseover="imgTips(this,{width:320})" style="border-radius:5px;border:1px solid #ccc;"  height="36" id="img_video_thumb" src="{$videoinfo['thumbnail']}">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">视频时长：</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" name="video[play_time]" id="playtime" id="playtime" value="{$videoinfo['play_time']}" autocomplete="off" >
        </div>
    </div>
<!--    <div class="layui-form-item">
        <label class="layui-form-label layui-bg-gray">视频说明</label>
        <div class="layui-input-inline">
            <textarea rows="6"  class="layui-textarea" name="video[short_info]" autocomplete="off" placeholder="请填写视频说明">{$videoinfo['short_info']}</textarea>
        </div>
    </div> -->
    <div class="layui-form-item">
        <label class="layui-form-label  layui-bg-gray">视频简介:</label>
        <div class="layui-input-block">
            <textarea id="UEditor1" name="video[info]" style="width: 60%;">{$videoinfo['info']}</textarea>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <input type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit" class="layui-btn"/>
        </div>
    </div>
</form>
{:editor(['UEditor1'], 'ueditor','/Xuploader.php?&from=ueditor')}


<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/plupload-2.3.6/js/plupload.full.min.js"></script>
<script src="/static/plupload-2.3.6/js/i18n/zh_CN.js"></script>
<script src="/static/xuploader/webServerUploader.js"></script>
<script src="/static/js/XCommon.js"></script>

<script>
    function afterUpThumb(resp){
        $('#img_video_thumb').attr('src',resp.filePath);
        $('#titlepic').val(resp.filePath);
        layer.msg('上传缩略图完成',{time:500});
    }

    function afterUpVideo(resp){
        console.log(resp);
        $('#odownpath1').val(resp.filePath);
        layer.msg('上传视频完成',{time:500});
    }

    $(function(){
        createWebUploader('video_thumb_up_btn','','','image',afterUpThumb);
        createWebUploader('video_up_btn','','','video',afterUpVideo);

        //隐藏云转码按钮
        {php}$webType=x_get_webseting('video_save_server_type');{/php}
            {if $webType=='yunzhuanma'}
            $("#chosevideo").show();
            $("#video_up_btn").hide();
        {/if}
    });
</script>


<div class="layui-tab-item layui-show">
    <form class="layui-form layui-form-pane" action="{:url('')}" id="editForm" method="post">
        <fieldset class="layui-elem-field layui-field-title">
            <legend>提成设置</legend>
        </fieldset>

        <div class="layui-form-item">
            <label class="layui-form-label">视频提成</label>
            <div class="layui-input-inline">
                <input type="number" min="1" max="10" class="layui-input" name="video_commission" value="{$config['video_commission']}" autocomplete="off" placeholder="请填写1-100的数字">
            </div>
            <div class="layui-form-mid layui-word-aux">填写1-100的数字，如填写1则上传者获取的提成为(1%)视频观看费用</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">图片提成</label>
            <div class="layui-input-inline">
                <input type="number" min="1" max="10" class="layui-input" name="atlas_commission" value="{$config['atlas_commission']}" autocomplete="off" placeholder="请填写1-100的数字">
            </div>
            <div class="layui-form-mid layui-word-aux">填写1-100的数字，如填写1则上传者获取的提成为(1%)图片观看费用</div>
        、</div>
        <div class="layui-form-item">
            <label class="layui-form-label">资讯提成</label>
            <div class="layui-input-inline">
                <input type="number" min="1" max="10" class="layui-input" name="novel_commission" value="{$config['novel_commission']}" autocomplete="off" placeholder="请填写1-100的数字">
            </div>
            <div class="layui-form-mid layui-word-aux">填写1-100的数字，如填写1则上传者获取的提成为(1%)资讯观看费用</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">代理商提成（%）</label>
            <div class="layui-input-inline">
                <input type="number" min="1" max="100" class="layui-input" name="agent_commission" value="{$config['agent_commission']}" autocomplete="off" placeholder="请填写1-100的数字">
            </div>
            <div class="layui-form-mid layui-word-aux"> 填写1-100的数字，当客户在代理商网站上充值的时候，代理商可获取充值金额*（提成数%）的提成金额</div>
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
<script>
    layui.use('form', function(){
        var $ = layui.jquery;
        var form = layui.form;

    });
</script>

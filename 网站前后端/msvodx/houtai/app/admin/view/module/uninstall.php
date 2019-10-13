<form class="layui-form" action="{:url()}" method="post">
<div class="page-form">
    <fieldset class="layui-elem-field layui-field-title">
      <legend>卸载模块：{$data_info['title']}[{$data_info['name']}]</legend>
    </fieldset>
    <div class="layui-form-item">
        <label class="layui-form-label">清除数据</label>
        <div class="layui-input-inline">
            <input type="radio" name="clear" value="1" title="是">
            <input type="radio" name="clear" value="0" title="否" checked>
        </div>
        <div class="layui-form-mid layui-word-aux">
            选择“是”，将删除该模块下所有数据表
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"> </label>
        <div class="layui-input-block">
            <input type="hidden" name="id" value="{$data_info['id']}">
            <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">立即卸载</button>
            <a href="{:url('index')}" class="layui-btn layui-btn-primary ml10"><i class="aicon ai-fanhui"></i>返回</a>
        </div>
    </div>
</div>
</form>
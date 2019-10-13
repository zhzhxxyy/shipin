<div class="layui-tab-item layui-show">
    <form class="layui-form layui-form-pane" action="{:url('')}" id="editForm" method="post">
        <fieldset class="layui-elem-field layui-field-title">
            <legend>试看设置</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">是否支持试看</label>
            <div class="layui-input-inline">
                <input type="checkbox" name="look_at_on"  lay-skin="switch" lay-text="支持|关闭" {if condition="$config['look_at_on'] eq 1"}checked=""{/if}>
            </div>
            <div class="layui-form-mid layui-word-aux"></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"><b>PC</b>计量单位</label>
            <div class="layui-input-inline">
                <select name="look_at_measurement" class="field-role_id" type="select"  name="look_at_measurement"  lay-skin="switch" lay-filter="look_at_measurement"  >
                    <option value="1" {if condition="$config['look_at_measurement'] eq 1"}selected=""{/if}>部</option>
                    <option value="2" {if condition="$config['look_at_measurement'] eq 2"}selected=""{/if}>秒</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" id="look_at_num"><b>PC</b>试看{if condition="$config['look_at_measurement'] eq 1"}部{else}秒{/if}数</label>
            <div class="layui-input-inline">
                <input type="number"  class="layui-input" name="look_at_num" value="{$config['look_at_num']}" autocomplete="off" placeholder="请填写可试看数量">
            </div>
            <div class="layui-form-mid layui-word-aux" id="look_at_tpis" >{if condition="$config['look_at_measurement'] eq 1"}可试看的部数，如可试看1部，则填写1{else}每部可试看多少秒，如可试看30秒，则填写30{/if}</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" id="look_at_num"><b>手机</b>试看部数</label>
            <div class="layui-input-inline">
                <input type="number"  class="layui-input" name="look_at_num_mobile" value="{$config['look_at_num_mobile']}" autocomplete="off" placeholder="请填写可试看数量">
            </div>
            <div class="layui-form-mid layui-word-aux" id="look_at_tpis" >防止UC手机端非法浏览VIP资源，故手机端单独设置</div>
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
        form.on('select(look_at_measurement)', function(data){
            console.log(data);
            if(data.value == 1){
                $('#look_at_num').html('<b>PC</b>试看部数');
                $('#look_at_tpis').html('可试看的部数，如可试看1部，则填写1');
            }else{
                $('#look_at_num').html('<b>PC</b>试看秒数');
                $('#look_at_tpis').html('每部可试看多少秒，如可试看30秒，则填写30' );
            }
        });
    });
</script>

{php}
$error = false;
{/php}
<form class="layui-form" action="{:url()}" method="post">
<div class="page-form">
    {if condition="$data_info['module_depend']"}
    <fieldset class="layui-elem-field layui-field-title">
      <legend>模块依赖检查</legend>
    </fieldset>
    <div class="layui-form-item">
        <div class="layui-input-inline" style="width:800px;">
        <table class="layui-table ml30" lay-even="" lay-skin="row">
            <thead>
                <tr>
                    <th>依赖模块</th>
                    <th>模块标识</th>
                    <th>当前版本</th>
                    <th>需要版本</th>
                    <th>结果</th>
                </tr> 
            </thead>
            <tbody>
                {volist name="data_info['module_depend']" id="v"}
                <tr>
                    <td>{$v[0]}</td>
                    <td>{$v[1]}</td>
                    <td>{$v[5]}</td>
                    <td>{$v[2]}</td>
                    <td>{$v[4]}</td>
                </tr>
                {if condition="$v[4] neq '✔︎'"}
                    {php}
                        $error = true;
                    {/php}
                {/if}
                {/volist}
            </tbody>
        </table>
        </div>
    </div>
    {/if}
    {if condition="$tables"}
    <fieldset class="layui-elem-field layui-field-title">
      <legend>数据库表检查</legend>
    </fieldset>
    <div class="layui-form-item">
        <div class="layui-input-inline w400">
        <table class="layui-table ml30" lay-even="" lay-skin="row">
            <colgroup>
                <col >
                <col width="110">
            </colgroup>
            <thead>
                <tr>
                    <th>数据库表</th>
                    <th>结果</th>
                </tr> 
            </thead>
            <tbody>
                {volist name="tables" id="vo"}
                <tr>
                    <td>{$vo['table']}</td>
                    <td>{$vo['exist']}</td>
                </tr>
                {/volist}
            </tbody>
        </table>
        </div>
    </div>
    {/if}
    <div class="layui-form-item">
        <label class="layui-form-label">清除旧数据</label>
        <div class="layui-input-inline">
            <input type="radio" name="clear" value="1" title="是">
            <input type="radio" name="clear" value="0" title="否" checked>
        </div>
        <div class="layui-form-mid layui-word-aux">
            选择“是”，将删除数据库中已存在的相同数据表
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"> </label>
        <div class="layui-input-block">
            <input type="hidden" name="id" value="{$data_info['id']}">
            <button type="submit" {if condition="$error"}class="layui-btn layui-btn-disabled" disabled{else /}class="layui-btn"{/if} lay-submit="" lay-filter="formSubmit">立即安装</button>
            <a href="{:url('index')}" class="layui-btn layui-btn-primary ml10"><i class="aicon ai-fanhui"></i>返回</a>
        </div>
    </div>
</div>
</form>
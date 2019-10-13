<ul class="themes">
    {volist name="data_list" id="vo"}
    <li>
        <img src="{$vo['thumb']}" width="150" height="150">
        <dl>
            <dd>名称：<span class="mcolor">{$vo['title']}</span></dd>
            <dd>作者：<span class="mcolor2">{$vo['author']}</span></dd>
            <dd>版本：<span class="mcolor2">{$vo['version']}</span></dd>
            <dd>时间：<span class="mcolor2">{$vo['time']}</span></dd>
            <dt>{if condition="$vo['name'] eq $data_info['theme']"}<a class="layui-btn layui-btn-normal layui-btn-small layui-btn-disabled">默认主题</a>{else /}<a data-href="{:url('setDefaultTheme?id='.$data_info['id'].'&theme='.$vo['name'])}" class="layui-btn layui-btn-small j-ajax">设为默认</a>{/if}{if condition="isset($vo['identifier']) && !empty($vo['identifier'])"}<a href="{:url('upgrade/lists?app_name='.$data_info['name'].'&app_type=theme&app_version='.$vo['version'].'&identifier='.$vo['identifier'])}" class="layui-btn layui-btn-primary layui-btn-small">更新</a>{/if}<a data-href="{:url('delTheme?id='.$data_info['id'].'&theme='.$vo['name'])}" class="layui-btn layui-btn-primary layui-btn-small j-ajax" title="删除之后将无法恢复！您确定要执行删除吗？">删除</a></dt>
        </dl>
    </li>
    {/volist}
    <li style="width:100%;margin-left:0;">
        <a href="{:url('index')}" class="layui-btn layui-btn-primary ml10"><i class="aicon ai-fanhui"></i>返回</a>
    </li>
</ul>
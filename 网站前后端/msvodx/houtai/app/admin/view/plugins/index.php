<div class="layui-form">
    <table class="layui-table mt10" lay-even="" lay-skin="row">
        <colgroup>
            <col>
            <col width="120">
            <col width="120">
            <col width="240">
        </colgroup>
        <thead>
            <tr>
                <th>插件信息</th>
                <th>插件版本</th>
                <th>插件状态</th>
                <th>操作</th>
            </tr> 
        </thead>
        <tbody>
            {volist name="data_list" id="vo" empty="<tr><td colspan='5' align='center' height='100'>未发现相关插件，快去<a href='http://store.hisiphp.com/addons' target='_blank'> <strong style='color:#428bca'>应用市场</strong> </a>看看吧！</td></tr>"}
            <tr>
                <td>
                    <div class="module-list-info">
                        {if condition="$vo['icon']"}
                            <img src="{$vo['icon']}" width="80" height="80">
                        {else /}
                            <img src="/static/admin/image/app.png" width="80" height="80">
                        {/if}
                        <div class="txt">
                            <h3>{$vo['title']}</h3>
                            <p><span>服务商：</span><a href="">{$vo['author']}</a><br><span>简&nbsp;&nbsp;&nbsp;&nbsp;介：</span>{$vo['intro']}</p>
                        </div>
                    </div>
                </td>
                <td>{$vo['version']}</td>
                <td>
                   {:app_status($vo['status'])}
                </td>
                <td>
                    {if condition="$vo['system'] neq 1"}
                    <div class="layui-btn-group">
                        <div class="layui-btn-group">
                            {switch name="vo['status']"}
                                {case value="0"}
                                    <a data-href="{:url('install?id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small j-ajax">安装</a>
                                    {if condition="$vo['app_id'] eq 0"}
                                    <!-- <a data-href="{:url('package?id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small j-ajax">打包</a> -->
                                    {/if}
                                    <a data-href="{:url('del?id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small j-tr-del">删除</a>
                                {/case}
                                {case value="1"}
                                    <a href="{:url('setting?id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small">配置</a>
                                    <a data-href="{:url('status?val=2&id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small j-ajax">启用</a>
                                    {if condition="$vo['app_id'] gt 0"}
                                    <a href="{:url('upgrade/lists?app_type=plugins&app_id='.$vo['app_id'])}" class="layui-btn layui-btn-primary layui-btn-small">更新</a>
                                    {else /}
                                    <!-- <a data-href="{:url('package?id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small j-ajax">打包</a> -->
                                    {/if}
                                    <a href="{:url('uninstall?id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small">卸载</a>
                                {/case}
                                {case value="2"}
                                    <a href="{:url('setting?id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small">配置</a>
                                    <a title="禁用后将无法使用此插件，您确定要禁用吗？" data-href="{:url('status?val=1&id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small j-ajax">禁用</a>
                                    {if condition="$vo['app_id'] gt 0"}
                                    <a href="{:url('upgrade/lists?app_type=plugins&identifier='.$vo['identifier'])}" class="layui-btn layui-btn-primary layui-btn-small">更新</a>
                                    {else /}
                                    <!-- <a data-href="{:url('package?id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small j-ajax">打包</a> -->
                                    {/if}
                                    <a title="卸载后将无法使用此插件，您确定要卸载吗？" data-href="{:url('uninstall?id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small j-ajax">卸载</a>
                                {/case}
                                {default /}
                            {/switch}
                        </div>
                    </div>
                    {else /}
                    <button class="layui-btn layui-btn-mini layui-btn-disabled">不可操作</button>
                    {/if}
                </td>
            </tr>
            {/volist}
        </tbody>
    </table>
</div>
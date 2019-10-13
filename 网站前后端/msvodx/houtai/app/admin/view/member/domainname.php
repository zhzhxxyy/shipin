<style>
    .layui-table[lay-skin=row] td, .layui-table[lay-skin=row] th{
        text-align: left;
    }
    .layui-form-select{position: fixed!important;z-index: 999;right: 20px; margin-top: -15px;}
</style>
<div class="page-toolbar" >
    <div class="layui-btn-group fl">
        <a data-href="{:url('status?table=domain_cname_binding&val=1')}" class="layui-btn layui-btn-primary j-page-btns"><i class="aicon ai-qiyong"></i>通过</a>
        <a data-href="{:url('status?table=domain_cname_binding&val=2')}" class="layui-btn layui-btn-primary j-page-btns"><i class="aicon ai-jinyong1"></i>拒绝</a>
        <a data-href="{:url('del?table=domain_cname_binding')}" class="layui-btn layui-btn-primary j-page-btns confirm"><i class="aicon ai-jinyong"></i>删除</a>
    </div>
    <div class="fr">
        <form class="page-list-form" id="myforms">
            <div class="layui-form-item ">
                <div class="layui-input-inline layui-form">
                    <select name="status" lay-filter="status">
                        <option value="3" {if condition="$status eq 3"}selected{/if}>所有状态</option>
                        <option value="0" {if condition="$status eq 0"}selected{/if}>未处理</option>
                        <option value="1" {if condition="$status eq 1"}selected{/if}>已通过</option>
                        <option value="2" {if condition="$status eq 2"}selected{/if}>已拒绝</option>
                    </select>
                </div>
            </div>
        </form>
    </div>
</div>
<form id="pageListForm">
<div class="layui-form">
    <table class="layui-table mt10" lay-even="" lay-skin="row">
        <colgroup>
            <col width="50">
        </colgroup>
        <thead>
            <tr>
                <th><input type="checkbox" lay-skin="primary" lay-filter="allChoose"></th>
                <th>会员</th>
                <th>域名</th>
                <th>申请时间</th>
                <th>最后修改时间</th>
                <th>状态</th>
                <th>操作</th>
            </tr> 
        </thead>
        <tbody>
            {volist name="data_list" id="vo"}
            <tr>
                <td><input type="checkbox" name="ids[]" class="layui-checkbox checkbox-ids" value="{$vo['id']}" lay-skin="primary"></td>
                <td class="font12">
                    <img src="{if condition="$vo['headimgurl']"}{if condition="stripos($vo['headimgurl'],'http') === false"}{:x_get_webseting('web_server_url')}{/if}{$vo['headimgurl']}{else /}__ADMIN_IMG__/avatar.png{/if}" width="60" height="60" class="fl">
                    <p class="ml10 fl"><strong class="mcolor">昵称：{notempty name="vo.nickname"}{$vo['nickname']}{else/}{$vo['username']}{/notempty} </strong><br>手机：{$vo['tel']}<br>邮箱：{$vo['email']}</p>
                </td>
                <td>{$vo['webhost']}</td>
                <td>{:date('Y-m-d H:i:s', $vo['add_time'])}</td>
                <td>{:date('Y-m-d H:i:s', $vo['last_time'])}</td>
                <td>
                    {if condition="$vo['status'] eq 1"}
                    <span style="color:green">已通过</span>
                    {/if}
                    {if condition="$vo['status'] eq 0"}
                    <span style="color:blue">未处理</span>
                    {/if}
                    {if condition="$vo['status'] eq 2"}
                    <span style="color:red">已拒绝</span>
                    {/if}
                </td>
                <td>
                    <div class="layui-btn-group">
                        <div class="layui-btn-group">
                        <a data-href="{:url('del?table=domain_cname_binding&ids='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small j-tr-del"><i class="layui-icon">&#xe640;</i></a>
                        </div>
                    </div>
                </td>
            </tr>
            {/volist}
        </tbody>
    </table>
    {$pages}
    <script src="/static/js/jquery.2.1.4.min.js"></script>
    <script>
        layui.use('form', function(){
            var form = layui.form;
            form.on('select(status)', function(data){
                $('#myforms').submit();
            });
        });
    </script>
</div>
</form>

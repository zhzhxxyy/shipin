<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/js/XCommon.js"></script>

<style>
    td{
        border-right: dashed 1px #c7c7c7;
        text-align:center;
    }
</style>

<script>

    function copyUrl(id) {
        alert("复制："+id);

        return;
    }

</script>
<form id="pageListForm" class="layui-form layui-form-pane" method="get">
    <div class="layui-form">
        <table class="layui-table mt10" lay-even="" lay-skin="row">
            <colgroup>
                <col width="50">
            </colgroup>
            <thead>
            <tr>
                <th><input type="checkbox" lay-skin="primary" lay-filter="allChoose"></th>
                <th width="40px;">ID</th>
                <th width="70px;">Icon图</th>
                <th width="300px;">app名称</th>
                <th width="90px;">更新时间</th>
                <th width="200px;" style="text-align: center">操作</th>
            </tr>
            </thead>
            <tbody>
            {volist name="data_list" id="vo"}
            <tr>
                <td><input type="checkbox" name="ids[]" class="layui-checkbox checkbox-ids" value="{$vo['id']}" lay-skin="primary"></td>
                <td>{$vo['id']}</td>
                <td>
                    <a href="{$vo['app_icon']}" target="pic">
                        <img height="30" src="{$vo['app_icon']}" onmouseover="imgTips(this,{width:300})">
                    </a>
                </td>
                <td>{$vo['app_name']}</td>
                <td>{:date('Y-m-d',$vo['addtime'])}</td>
                <td>
                    <div class="layui-btn-group">
                        <a href="{:url('admin/fenfa/edit',['id'=>$vo['id']])}" class="layui-btn layui-btn-primary layui-btn-small"><i class="layui-icon">&#xe642;</i></a>
                        <a data-href="{:url('khdel?table=share_app&id='.$vo['id'])}" class="layui-btn layui-btn-primary layui-btn-small j-tr-del"><i class="layui-icon">&#xe640;</i></a>
                        <div onclick="copyUrl('{$vo['id']}')" style="display: inline-block;
    height: 28px;
    line-height: 30px;
    padding: 0 10px;
    white-space: nowrap;
    text-align: center;
    font-size: 12px;
    cursor: pointer;
    /*border: 1px solid #C9C9C9;*/
    background-color: #fff;
    color: #555;
    vertical-align: middle;
    border-left:1px none #C9C9C9 ;
    border-top:1px solid #C9C9C9 ;
    border-right:1px solid #C9C9C9 ;
    border-bottom:1px solid #C9C9C9 ;" title="复制宣传地址">复制</div>
                    </div>
                </td>
            </tr>
            {/volist}
            </tbody>
        </table>
        {$pages}
    </div>
</form>
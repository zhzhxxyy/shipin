<form class="layui-form layui-form-pane" action="{:url()}" method="post">
    <fieldset class="layui-elem-field layui-field-title">
      <legend>模块基本信息</legend>
    </fieldset>
    <div class="layui-form-item">
        <label class="layui-form-label">模块名</label>
        <div class="layui-input-inline w300">
            <input type="text" class="layui-input" name="name" lay-verify="required" autocomplete="off" placeholder="请输入模块名">
        </div>
        <div class="layui-form-mid layui-word-aux">模块名称只能为字母</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">模块标题</label>
        <div class="layui-input-inline w300">
            <input type="text" class="layui-input" name="title" lay-verify="required" autocomplete="off" placeholder="请输入模块标题">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">模块标识</label>
        <div class="layui-input-inline w300">
            <input type="text" class="layui-input" name="identifier" lay-verify="required" autocomplete="off" placeholder="请输入模块标识">
        </div>
        <div class="layui-form-mid layui-word-aux">格式：模块名(只能为字母).开发者标识(只能为字母、数字、下划线).module</div>
    </div>
<!--     <div class="layui-form-item">
        <label class="layui-form-label">模块图标</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" id="input-icon" name="icon" lay-verify="" autocomplete="off" placeholder="可自定义或使用系统图标">
        </div>
        <i class="" id="form-icon-preview"></i>
        <a href="{:url('publics/icon?input=input-icon&show=form-icon-preview')}" class="layui-btn layui-btn-primary j-iframe-pop fl">选择图标</a>
    </div> -->
    <div class="layui-form-item">
        <label class="layui-form-label">模块描述</label>
        <div class="layui-input-inline w300">
            <textarea  class="layui-textarea" name="intro" lay-verify="" autocomplete="off" placeholder="请填写模块描述"></textarea>
        </div>
        <div class="layui-form-mid layui-word-aux"></div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">开发者</label>
        <div class="layui-input-inline w300">
            <input type="text" class="layui-input" name="author" lay-verify="" autocomplete="off" placeholder="请输入开发者">
        </div>
        <div class="layui-form-mid layui-word-aux">建议填写</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">开发者网址</label>
        <div class="layui-input-inline w300">
            <input type="text" class="layui-input" name="url" lay-verify="" autocomplete="off" placeholder="请输入开发者网址">
        </div>
        <div class="layui-form-mid layui-word-aux">建议填写</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">版本号</label>
        <div class="layui-input-inline w300">
            <input type="text" class="layui-input" name="version" value="1.0.0" lay-verify="required" autocomplete="off" placeholder="请输入版本号">
        </div>
        <div class="layui-form-mid layui-word-aux">版本号格式采用三段式：主版本号.次版本号.修订版本号</div>
    </div>
    <fieldset class="layui-elem-field layui-field-title">
      <legend>快速生成模块目录结构</legend>
    </fieldset>
    <div class="layui-form-item">
        <label class="layui-form-label">公共文件</label>
        <div class="layui-input-inline w300">
            <input type="text" class="layui-input" name="file" value="common.php,config.php" lay-verify="" autocomplete="off" placeholder="多个文件以逗号(,)分割">
        </div>
        <div class="layui-form-mid layui-word-aux">多个文件以逗号 "," 分割</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">模块目录</label>
        <div class="layui-input-inline w300">
            <textarea rows="8"  class="layui-textarea" name="dir" lay-verify="" autocomplete="off">admin
home
model
lang
sql
validate
view</textarea>
        </div>
        <div class="layui-form-mid layui-word-aux">在当前模块下生成目录<br>admin(后台控制器)<br>home(前台控制器)<br>model(模型层)<br>lang(语言包)<br>sql(数据库文件)<br>vaidate(验证规则)<br>view(视图)<br><span style="color:red">前台模板路径：/theme/模块名/default/，后台静态文件路径：/static/模块名/</span></div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button type="submit" class="layui-btn" lay-submit="" lay-filter="formSubmit">生成模块</button>
            <a href="{:url('index')}" class="layui-btn layui-btn-primary ml10"><i class="aicon ai-fanhui"></i>返回</a>
        </div>
    </div>
</form>
<?php if (!defined('THINK_PATH')) exit(); /*a:1:{s:37:"./tpl/default/mobile/index/zixun.html";i:1566873346;}*/ ?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>在线咨询</title>
  <meta name="renderer" content="webkit">
  <script type="text/javascript" src="__ROOT__/tpl/peixun/static/js/jquery-3.2.1.min.js"></script>
</head>
<body>



<?php if(isset($lianxi_qq)): ?><a style="margin: 20px;" target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=<?php echo $lianxi_qq; ?>&site=qq&menu=yes"><img style="width: 240px;height: 80px" border="0" src="http://wpa.qq.com/pa?p=2:<?php echo $lianxi_qq; ?>:41" alt="点击这里给我发消息" title="点击这里给我发消息"/></a><?php endif; if(isset($lianxi_qq)): ?><div style="margin: 20px;">QQ:<?php echo $lianxi_qq; ?></div><?php endif; if(!isset($lianxi_qq)): ?><a style="margin: 20px;" href="#">暂无客服</a><?php endif; ?>

</body>
</html>

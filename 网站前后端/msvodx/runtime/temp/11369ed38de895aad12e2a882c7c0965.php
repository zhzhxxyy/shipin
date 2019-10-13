<?php if (!defined('THINK_PATH')) exit(); /*a:1:{s:38:"./tpl/default/mobile/poster/index.html";i:1516953840;}*/ ?>

<?php if(empty($advertisement_info)): ?>
document.write("黄金广告位招租");
<?php endif; if($advertisement_info['type'] == 2): ?>
document.write("<div class='advert' style=' height:<?php echo $position['height']; ?>px;'>"
    +"<a href='<?php echo $advertisement_info['url']; ?>'>"
+"<img style='width: 100%; height:<?php echo $position['height']; ?>px;' src='<?php echo $advertisement_info['content']; ?>'>"
+"</a></div>");
<?php endif; if($advertisement_info['type'] == 1): ?>
document.write("<?php echo $advertisement_info['content']; ?>");
<?php endif; ?>

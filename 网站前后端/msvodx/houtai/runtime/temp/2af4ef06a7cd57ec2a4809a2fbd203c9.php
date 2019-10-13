<?php if (!defined('THINK_PATH')) exit(); /*a:1:{s:56:"/www/wwwroot/msvodx/houtai/app/admin/view/video/play.php";i:1516072302;}*/ ?>
<script src="/static/js/jquery.2.1.4.min.js"></script>
<script src="/static/ckplayer/ckplayer.js"></script>

<div id="playerBox">
    <img src="<?php echo $videoInfo['thumbnail']; ?>" />
</div>

<script type="text/javascript">
    var vodUrl = "<?php echo $videoInfo['url']; ?>";
    var params = {
        container: '#playerBox',
        variable: 'player',
        poster: '<?php echo $videoInfo["thumbnail"]; ?>',
        video: vodUrl,
        //loaded:'loadedHandler',
        autoplay: false,
        flashplayer: false
    };

    player = new ckplayer(params);

</script>
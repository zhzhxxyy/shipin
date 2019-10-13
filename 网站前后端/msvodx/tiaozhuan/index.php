<?php
$suiji=mt_rand(999, 9999);

function getRandomString($len, $chars=null)
{
    if (is_null($chars)){
        $chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    }  
    mt_srand(10000000*(double)microtime());
    for ($i = 0, $str = '', $lc = strlen($chars)-1; $i < $len; $i++){
        $str .= $chars[mt_rand(0, $lc)];  
    }
    return $str;
}

?>
<!DOCTYPE html>
<html lang="zh-cn">
 <head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0" /> 
  <title>更多精彩内容尽在这里！！！</title>
  <link rel="stylesheet" href="https://cdn.bootcss.com/normalize/5.0.0/normalize.min.css" />
  <style>html,body {height: 100%;margin: 0;padding: 0;}body {font-family: 'lucida grande', 'lucida sans unicode', lucida, helvetica, 'Hiragino Sans GB', 'Microsoft YaHei', 'WenQuanYi Micro Hei', sans-serif;align-items: center;display: flex;}a{text-decoration:none;}#container {max-width: 400px;flex-basis: 100%;margin: 0 auto;background: #FFF;border-radius: 10px;box-shadow: 0 0 30px rgba(0, 0, 0, 0.3);-webkit-box-shadow: 0 0 30px rgba(0, 0, 0, 0.3);overflow: hidden;}#container #hero-img {width: 100%;height: 200px;background: #007bff;}#container #profile-img {width: 160px;height: 160px;margin: -80px auto 0 auto;border: 6px solid #FFF;border-radius: 50%;box-shadow: 0 0 5px rgba(90, 90, 90, 0.3);}#container #profile-img img {width: 100%;background: #FFF;border-radius: 50%;}#container #content {text-align: center;width: 320px;margin: 0 auto;padding: 0 0 50px 0;}#container #content h1 {font-size: 29px;font-weight: 500;margin: 50px 0 0 0;}#container #content p {font-size: 18px;font-weight: 400;line-height: 1.4;color: #666;margin: 15px 0 40px 0;}#container #content a {color: #CCC;font-size: 20px;margin: 0 10px;transition: color .3s ease-in-out;-webkit-transition: color .3s ease-in-out;}#container #content a:hover {color: #007bff;}.btn{background: none repeat scroll 0 0 #1BA1E2; border: 0 none; border-radius: 2px; color: #FFFFFF !important; cursor: pointer; font-family: "Open Sans","Hiragino Sans GB","Microsoft YaHei","WenQuanYi Micro Hei",Arial,Verdana,Tahoma,sans-serif; font-size: 14px;  padding: 10px 10%;}.btn:hover,.yanshibtn:hover{background: none repeat scroll 0 0 #9B59B6; border: 0 none; border-radius: 2px; color: #FFFFFF!important; cursor: pointer; font-family: "Open Sans","Hiragino Sans GB","Microsoft YaHei","WenQuanYi Micro Hei",Arial,Verdana,Tahoma,sans-serif; font-size: 14px; padding: 10px 10%;}</style>
 </head>
 <body>
  <div id="container">
   <div id="hero-img"></div>
   <div id="profile-img">
    <img src="https://ws3.sinaimg.cn/large/005BYqpgly1fua355p7n6j30m80m8myd.jpg" />
   </div>
   <div id="content">
    <h1>更多精彩内容尽在这里</h1>
    <p>注册会员随意看</p>
    <a href="http://<?php echo $suiji; ?>.<?php echo getRandomString(4); ?>.tp9gp9.cn" target="_blank" class="btn btn-default" rel="nofollow">点击进入网站</a>
   </div>
  </div> 
 </body>
</html>
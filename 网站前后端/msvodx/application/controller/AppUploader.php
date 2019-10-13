<?php
/**
 * 上传api类
 * Date:    2017/12/15
 * Author:  @Dreamer
 */

namespace app\controller;

use think\Controller;
use think\Request;
use UploadUtils\Uploader as UploadUtil;

class AppUploader extends AppBaseController{

    /***
     * 根据请求的文件类型，生成上传相应的参数
     *
     * @param Request $request
     * @return string|void
     */
    public function createUploader(Request $request){

        $fileName = $request->request('fileName', '');
        $uper = UploadUtil::instance();
        $upConfig = $uper->getUploadConfig();   //获取当前上传配置信息
        if($upConfig==false){
            $this->ajaxReturn(false, '配置信息失败，请联系客服人员')->ajaxOutput();
        }
        $params = [];
        $params['image']=$uper->createAppUploadParams($upConfig['image']['image_save_server_type'], 'image',$fileName);
        $params['ico']=$uper->createAppUploadParams($upConfig['ico']['ico_save_server_type'], 'ico');
        $params['video']=$uper->createAppUploadParams($upConfig['video']['video_save_server_type'], 'video',$fileName);
        $this->data['data']=$params;
        $this->ajaxReturn(true, '成功')->ajaxOutput();
    }


    public function appXuploader(){
        $user_info=$this->isAccessThenLogin();
        ini_set('display_errors',0);
        error_reporting(~E_ALL);
        header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
        header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
        header("Cache-Control: no-store, no-cache, must-revalidate");
        header("Cache-Control: post-check=0, pre-check=0", false);
        header("Pragma: no-cache");
        header("Access-Control-Allow-Origin: *");
        if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
            $this->ajaxReturn(false, '上传失败')->ajaxOutput();
        }
        @set_time_limit(5 * 60);
        date_default_timezone_set('PRC');
        $baseDir= "XResource".DIRECTORY_SEPARATOR.date('Ymd');
        $targetDir =$_SERVER['DOCUMENT_ROOT'].DIRECTORY_SEPARATOR.$baseDir;
        $cleanupTargetDir = true; // Remove old files
        $maxFileAge = 5 * 3600; // Temp file age in seconds
        if (!file_exists($targetDir)) {
            @mkdir($targetDir,0766,true);
        }
        $allowFileType=['jpg','gif','png','jpeg', 'rmvb','flv','mp4','mov','3gp','wmv','mp3','avi','mpeg','ico'];

        $_REQUEST['newFileName']=uniqid("file_".$user_info['id']."_");
        if (isset($_REQUEST["name"])) {
            $fileName = $_REQUEST["name"];
            $fileName=$this->XRenameFileName($fileName,$_REQUEST['newFileName']);
            $_tmpType=end(explode('.',$fileName));
            if(!in_array($_tmpType,$allowFileType)){
                $this->ajaxReturn(false, 'Error file  .')->ajaxOutput();
            }
        } elseif (!empty($_FILES)) {
            $fileName = $_FILES["file"]["name"];
            $fileName=$this->XRenameFileName($fileName,$_REQUEST['newFileName']);
            $_tmpType=end(explode('.',$fileName));
            if(!in_array($_tmpType,$allowFileType)){
                $this->ajaxReturn(false, 'Error file  .')->ajaxOutput();
            }
        } else {
            $fileName = uniqid("file_");
        }
        $filePath = $targetDir . DIRECTORY_SEPARATOR . $fileName;
        $chunk = isset($_REQUEST["chunk"]) ? intval($_REQUEST["chunk"]) : 0;
        $chunks =isset($_REQUEST["chunks"]) ? intval($_REQUEST["chunks"]) : 0;
        if ($cleanupTargetDir) {
            if (!is_dir($targetDir) || !$dir = opendir($targetDir)) {
                $this->ajaxReturn(false, 'Failed to open temp directory.')->ajaxOutput();
            }
            while (($file = readdir($dir)) !== false) {
                $tmpfilePath = $targetDir . DIRECTORY_SEPARATOR . $file;
                if ($tmpfilePath == "{$filePath}.part") {
                    continue;
                }
                if (preg_match('/\.part$/', $file) && (filemtime($tmpfilePath) < time() - $maxFileAge)) {
                    @unlink($tmpfilePath);
                }
            }
            closedir($dir);
        }

        if (!$out = @fopen("{$filePath}.part", $chunks ? "ab" : "wb")) {
            $this->ajaxReturn(false, 'Failed to open output stream.')->ajaxOutput();

        }

        if (!empty($_FILES)) {
            if ($_FILES["file"]["error"] || !is_uploaded_file($_FILES["file"]["tmp_name"])) {
                $this->ajaxReturn(false, 'Failed to move uploaded file.')->ajaxOutput();
            }
            if (!$in = @fopen($_FILES["file"]["tmp_name"], "rb")) {
                $this->ajaxReturn(false, 'Failed to open input stream.')->ajaxOutput();
            }
        } else {
            if (!$in = @fopen("php://input", "rb")) {
                $this->ajaxReturn(false, 'Failed to open input stream.')->ajaxOutput();
            }
        }

        while ($buff = fread($in, 4096)) {
            fwrite($out, $buff);
        }

        @fclose($out);
        @fclose($in);
        if (!$chunks || $chunk == $chunks - 1) {
            if(!file_exists($filePath)) {
                rename("{$filePath}.part", $filePath);
                $newFilePath=$filePath;
            }else{
                $_tmpArr=explode('.',$fileName);
                $filePrefix=end($_tmpArr);
                $newFileName=str_replace(".{$filePrefix}",'',$fileName).'___'.time().'.'.$filePrefix;
                $newFilePath=$targetDir.'\\'.$newFileName;
                rename("{$filePath}.part",$newFilePath);
                $fileName=$newFileName;
            }

            $httpType = ((isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] == 'on') || (isset($_SERVER['HTTP_X_FORWARDED_PROTO']) && $_SERVER['HTTP_X_FORWARDED_PROTO'] == 'https')) ? 'https://' : 'http://';
            $returnData=array(
                'jsonrpc'=>'2.0',
                'result'=>'success',
                'id'=>'id',
                'filePath'=>$httpType.$_SERVER['HTTP_HOST']."/".str_replace('\\','/',$baseDir)."/".str_replace('\\','/',$fileName)
            );
            if(isset($_REQUEST['isFavicon'])&& $_REQUEST['isFavicon']){
                $favicon=dirname(__FILE__).'/favicon.ico';

                if(file_exists($favicon)) unlink($favicon);

                $rs=rename($newFilePath,$favicon);
                file_put_contents(dirname(__FILE__).'/logs.log',"\n 上传完成后路径：".$newFilePath."\t移到到的位置：{$favicon}\t".var_export($rs,1),FILE_APPEND);
                $returnData['filePath']=$httpType.$_SERVER['HTTP_HOST']."/favicon.ico";
            }
            $this->data['data']=$returnData['filePath'];
            $this->ajaxReturn(true, '上传成功')->ajaxOutput();

        }
        $this->ajaxReturn(false, '上传失败')->ajaxOutput();
    }

    private  function XRenameFileName($oldName,$newName){
        if(empty($newName)) return $oldName;
        $_tmpArr=explode('.',$oldName);
        $filePrefix=end($_tmpArr);
        return $newName.".{$filePrefix}";
    }

}
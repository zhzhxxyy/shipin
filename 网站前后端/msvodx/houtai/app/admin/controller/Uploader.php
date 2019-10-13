<?php
/**
 * 上传api类
 * Date:    2017/12/15
 * Author:  @Dreamer
 */

namespace app\admin\controller;

use think\Controller;
use think\Request;
use UploadUtils\Uploader as UploadUtil;

class Uploader extends Controller
{

    private $allowFileType = ['video', 'image', 'ico', 'ipa', 'apk'];
    private $uper = null;

    public function __construct(Request $request = null)
    {
        parent::__construct($request);

        $this->uper = UploadUtil::instance();
    }


    /***
     * 根据请求的文件类型，生成上传相应的参数
     *
     * @param Request $request
     * @return string|void
     */
    public function createUploader(Request $request)
    {

        $fileType = $request->request('fileType', '');
        $fileName = $request->request('fileName', '');
        if (empty($fileType)) die(json_encode(['statusCode' => 4003, 'error' => '缺少参数：fileType .']));
        if (!in_array($fileType, $this->allowFileType))  die(json_encode(['statusCode' => 4004, 'error' => '参数格式不正确：fileType ,允许的fileType只能是' . implode(',', $this->allowFileType)]));


        $upConfig = $this->uper->getUploadConfig();   //获取当前上传配置信息

        $params = [];
        switch ($fileType) {
            case 'image':
                $params = $this->uper->createWebUploadParams($upConfig['image']['image_save_server_type'], 'image',$fileName);
                break;
            case 'ico':
                $params = $this->uper->createWebUploadParams($upConfig['ico']['ico_save_server_type'], 'ico');
                break;
            case 'video':
                $params = $this->uper->createWebUploadParams($upConfig['video']['video_save_server_type'], 'video',$fileName);
                break;
            case 'apk':
                $params = $this->uper->createWebUploadParams($upConfig['apk']['apk_save_server_type'], 'apk',$fileName);
                break;
            case 'ipa':
                $params = $this->uper->createWebUploadParams($upConfig['ipa']['ipa_save_server_type'], 'ipa',$fileName);
                break;
        }

        die(json_encode(['statusCode' => 0, 'data' => $params]));
    }


    /**
     * 更新后台的缓存（主要是目录菜单）
     */
    public function refreshCache()
    {
        $cache_tag = '_admin_menu'.ADMIN_ID.dblang('admin');
        cache($cache_tag,null);
        
        // return $this->success('刷新缓存成功');

        die(json_encode(['statusCode' => 0, 'message' => '刷新成功!']));
    }

}
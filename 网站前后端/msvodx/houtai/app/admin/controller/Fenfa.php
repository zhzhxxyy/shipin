<?php

namespace app\admin\controller;

use think\Db;
use think\Request;

class Fenfa extends Admin{

    /**
     *  分发列表
     */

    function lists(Request $request){
        $where=" 1=1 ";
        $data_list= $this->myDb->name('share_app')->where($where)->order('id desc')->paginate(null,false,['query'=>$request->get()]);
        $list=$data_list->toArray();
        $pages = $data_list->render();
        $this->assign('pages', $pages);
        $this->assign('data_list',$list['data']);
        return $this->fetch();
    }


    /**
     * 添加分发
     * @author frs whs tcl dreamer ?2016
     * @return mixed
     */
    function upload(Request $request){
        if ($this->request->isPost()) {
        
            $videoinfo=$request->post('video/a');
            $data=[];

            $data['app_name']=isset($videoinfo["app_name"])?$videoinfo["app_name"]:"";
            $data['app_domain']=isset($videoinfo["app_domain"])?$videoinfo["app_domain"]:"";
            $data['app_icon']=isset($videoinfo["app_icon"])?$videoinfo["app_icon"]:"";
            $data['app_bid_ios']=isset($videoinfo["app_bid_ios"])?$videoinfo["app_bid_ios"]:"";
            $data['app_bsvs_ios']=isset($videoinfo["app_bsvs_ios"])?$videoinfo["app_bsvs_ios"]:"";
            $data['app_size_ios']=isset($videoinfo["app_size_ios"])?$videoinfo["app_size_ios"]:"";
            $data['app_plist_ios']=isset($videoinfo["app_plist_ios"])?$videoinfo["app_plist_ios"]:"";
            $data['app_plist_plist']="";//地址后面处理
            $data['app_bid_android']=isset($videoinfo["app_bid_android"])?$videoinfo["app_bid_android"]:"";;
            $data['app_bsvs_android']=isset($videoinfo["app_bsvs_android"])?$videoinfo["app_bsvs_android"]:"";;
            $data['app_size_android']=isset($videoinfo["app_size_android"])?$videoinfo["app_size_android"]:"";;
            $data['app_plist_android']=isset($videoinfo["app_plist_android"])?$videoinfo["app_plist_android"]:"";;
            $data['addtime']=time();

            if(empty($data['app_name'])){
                return $this->error('请填写app名称！');
            }
            if(empty($data['app_domain'])){
                return $this->error('请填写app域名！');
            }
            if(empty($data['app_icon'])){
                return $this->error('请填写app图标！');
            }
            if(empty($data['app_bid_ios'])){
                return $this->error('请填写IOS bundle！');
            }
            if(empty($data['app_bsvs_ios'])){
                return $this->error('请填写IOS版本号！');
            }
            if(empty($data['app_plist_ios'])){
                return $this->error('请填写IOS文件路径！');
            }
            if(empty($data['app_bid_android'])){
                return $this->error('请填写Android bundle！');
            }
            if(empty($data['app_bsvs_android'])){
                return $this->error('请填写Android版本号！');
            }
            if(empty($data['app_plist_android'])){
                return $this->error('请填写Android文件路径！');
            }
            
            $basePath=dirname($_SERVER['DOCUMENT_ROOT']). DIRECTORY_SEPARATOR ."public/XResource";
            
       
            if(!file_exists($basePath."/app/default.plist")){
                return $this->error('联系技术人员，请在public/XResource/app文件中添加default.plist文件');
            }
            
            $iosArr=explode('/XResource',$data['app_plist_ios'],2);
            if(!(sizeof($iosArr)==2&&file_exists($basePath.DIRECTORY_SEPARATOR.$iosArr[1]))){
                return $this->error('苹果安装包不存在!');
            }
            $androidArr=explode('/XResource',$data['app_plist_android'],2);
            
            if(!(sizeof($androidArr)==2&&file_exists($basePath.DIRECTORY_SEPARATOR.$androidArr[1]))){
                return $this->error('Android安装包不存在!');
            }

            $defaultListPath=$basePath."/app/default.plist";//默认的文件地址
            $ipaPath=$basePath.DIRECTORY_SEPARATOR.$iosArr[1];//苹果地址
            $androidPath=$basePath.DIRECTORY_SEPARATOR.$androidArr[1];//Android地址
            $data['app_size_ios']=$this->formatsize(filesize($ipaPath));
            $data['app_size_android']=$this->formatsize(filesize($androidPath));
            $plistPathName= DIRECTORY_SEPARATOR.date('Ymd').DIRECTORY_SEPARATOR.uniqid("plist_")."plist";
            $plistPath=$basePath.$plistPathName;
            if(file_exists($plistPath)){
                unlink($plistPath);
            }
           
            $str = file_get_contents($defaultListPath);
            $str = str_replace(array('{ipa}', '{icon}', '{bid}', '{bvs}', '{name}'), array($data['app_domain'].$data['app_plist_ios'], $data['app_domain'].$data['app_icon'], $data['app_bid_ios'],$data['app_bsvs_ios'], $data['app_name']), $str);
            fwrite(fopen($plistPath, 'w'),iconv('UTF-8', 'UTF-8//IGNORE', $str));
            $data['app_plist_plist']=$data['app_domain']."/XResource".$plistPathName;

            if(!empty($data['app_plist_ios'])&&strtolower(substr($data['app_plist_ios'], 0, 5))!="https") {
                $data['app_plist_ios']="https".substr($data['app_plist_ios'],4);
            }

            if(!empty($data['app_plist_plist'])&&strtolower(substr($data['app_plist_plist'], 0, 5))!="https") {
                $data['app_plist_plist']="https".substr($data['app_plist_plist'],4);
            }

            $videodb=$this->myDb->name('share_app');
            $insert=$videodb->insert($data);
            if($insert){
                return $this->success('添加成功',url('lists'));
            }
            return $this->error('哎呀，出错了！');
        }
        return $this->fetch();
    }


    /**
     * 编辑
     * @param Request $request
     * @return mixed|void
     */
    function edit(Request $request){
        $id=$request->param('id');
        $appDb=$this->myDb->name('share_app');
        if(empty($id)){
            return $this->error('出错了,请联系管理员解决！');
        }
        $appInfo=$appDb->where(['id'=>$id])->find();
        if(!$appInfo){
            return $this->error('出错了,不存在！');
        }
        if($request->isPost()){
            $videoinfo=$request->post('video/a');
            $data=[];
            $data['id']=$id;
            $data['app_name']=isset($videoinfo["app_name"])?$videoinfo["app_name"]:"";
            $data['app_domain']=isset($videoinfo["app_domain"])?$videoinfo["app_domain"]:"";
            $data['app_icon']=isset($videoinfo["app_icon"])?$videoinfo["app_icon"]:"";
            $data['app_bid_ios']=isset($videoinfo["app_bid_ios"])?$videoinfo["app_bid_ios"]:"";
            $data['app_bsvs_ios']=isset($videoinfo["app_bsvs_ios"])?$videoinfo["app_bsvs_ios"]:"";
            $data['app_size_ios']=$appInfo['app_size_ios'];
            $data['app_plist_ios']=isset($videoinfo["app_plist_ios"])?$videoinfo["app_plist_ios"]:"";
            $data['app_plist_plist']=$appInfo['app_plist_plist'];
            $data['app_bid_android']=isset($videoinfo["app_bid_android"])?$videoinfo["app_bid_android"]:"";;
            $data['app_bsvs_android']=isset($videoinfo["app_bsvs_android"])?$videoinfo["app_bsvs_android"]:"";;
            $data['app_size_android']=$appInfo['app_size_android'];
            $data['app_plist_android']=isset($videoinfo["app_plist_android"])?$videoinfo["app_plist_android"]:"";;
            $data['addtime']=time();
            if(empty($data['app_name'])){
                return $this->error('请填写app名称！');
            }
            if(empty($data['app_domain'])){
                return $this->error('请填写app域名！');
            }
            if(empty($data['app_icon'])){
                return $this->error('请填写app图标！');
            }
            if(empty($data['app_bid_ios'])){
                return $this->error('请填写IOS bundle！');
            }
            if(empty($data['app_bsvs_ios'])){
                return $this->error('请填写IOS版本号！');
            }
            if(empty($data['app_plist_ios'])){
                return $this->error('请填写IOS文件路径！');
            }
            if(empty($data['app_bid_android'])){
                return $this->error('请填写Android bundle！');
            }
            if(empty($data['app_bsvs_android'])){
                return $this->error('请填写Android版本号！');
            }
            if(empty($data['app_plist_android'])){
                return $this->error('请填写Android文件路径！');
            }

            $basePath=dirname($_SERVER['DOCUMENT_ROOT']). DIRECTORY_SEPARATOR ."public/XResource";
            if(!file_exists($basePath."/app/default.plist")){
                return $this->error('联系技术人员，请在public/XResource/app文件中添加default.plist文件');
            }
            $iosArr=explode('/XResource',$data['app_plist_ios'],2);
            if(!(sizeof($iosArr)==2&&file_exists($basePath.DIRECTORY_SEPARATOR.$iosArr[1]))){
                return $this->error('苹果安装包不存在!');
            }
            $androidArr=explode('/XResource',$data['app_plist_android'],2);
            if(!(sizeof($androidArr)==2&&file_exists($basePath.DIRECTORY_SEPARATOR.$androidArr[1]))){
                return $this->error('Android安装包不存在!');
            }
            $defaultListPath=$basePath."/app/default.plist";//默认的文件地址
            $ipaPath=$basePath.DIRECTORY_SEPARATOR.$iosArr[1];//苹果地址
            $androidPath=$basePath.DIRECTORY_SEPARATOR.$androidArr[1];//Android地址
            $data['app_size_ios']=$this->formatsize(filesize($ipaPath));
            $data['app_size_android']=$this->formatsize(filesize($androidPath));
            if(!empty($data['app_plist_ios'])&&strtolower(substr($data['app_plist_ios'], 0, 5))!="https") {
                $data['app_plist_ios']="https".substr($data['app_plist_ios'],4);
            }
            if(!empty($data['app_plist_plist'])&&strtolower(substr($data['app_plist_plist'], 0, 5))!="https") {
                $data['app_plist_plist']="https".substr($data['app_plist_plist'],4);
            }
            if($data['app_plist_ios']!=$appInfo['app_plist_ios']){
                $plistPathName= DIRECTORY_SEPARATOR.date('Ymd').DIRECTORY_SEPARATOR.uniqid("plist_")."plist";
                $plistPath=$basePath.$plistPathName;
                if(file_exists($plistPath)){
                    unlink($plistPath);
                }
                $str = file_get_contents($defaultListPath);
                $str = str_replace(array('{ipa}', '{icon}', '{bid}', '{bvs}', '{name}'), array($data['app_domain'].$data['app_plist_ios'], $data['app_domain'].$data['app_icon'], $data['app_bid_ios'],$data['app_bsvs_ios'], $data['app_name']), $str);
                fwrite(fopen($plistPath, 'w'),iconv('UTF-8', 'UTF-8//IGNORE', $str));
                $data['app_plist_plist']=$data['app_domain']."/XResource".$plistPathName;
            }
            $insert=$appDb->where(['id'=>$id])->update($videoinfo);
            if($insert){
                return $this->success('修改成功',url('lists'));
            }
            return $this->error('哎呀，修改失败了！');
        }
        $this->assign('info',$appInfo);
        return $this->fetch();
    }


    //格式化大小
    private  function formatsize($size){
        $prec = 3;
        $size = round(abs($size));
        $units = array(0 => " B", 1 => " KB", 2 => " MB", 3 => " GB", 4 => " TB");
        if($size == 0){
            return str_repeat(" ", $prec)."0".$units[0];
        }
        $unit = min(4, floor(log($size) / log(2) / 10));
        $size = $size * pow(2, -10 * $unit);
        $digi = $prec - 1 - floor(log($size) / log(10));
        $size = round($size * pow(10, $digi)) * pow(10, -$digi);
        return $size.$units[$unit];
    }

}
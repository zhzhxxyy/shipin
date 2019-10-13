<?php

namespace app\controller;

use think\Controller;
use think\Db;
use think\Request;

class BaseController extends Controller
{

    public function __construct(Request $request = null)
    {
        $request->action();
        if(!file_exists(APP_PATH.'db/install.lock')){
            $this->redirect('install/index');
            $this->themeBasename='default';
        }else{
            if($testTheme=strtolower($request->get('t/s'))){
                //theme view
                $havaThemeList=array_map('basename',glob('./tpl/*',GLOB_ONLYDIR));
                if(in_array($testTheme,$havaThemeList)){
                    $this->themeBasename=$testTheme;
                    session('testThemeBasename',$this->themeBasename);
                }else{
                    $this->themeBasename='default';
                }
            }else{
                if(session('testThemeBasename')){
                    $this->themeBasename=session('testThemeBasename');
                }else{
                    $this->themeBasename=get_config('theme_basename')?get_config('theme_basename'):'default';
                }
            }
        }
        $this->checkSiteStatus();

        $__domain=$request->param('__domain__');
        $systemDomain = ['admin', 'system'];
        $seoInfo=null;
        $preg='/[aA](\d+)/i';
        $rs=preg_match($preg,$__domain,$pregRs);
        if (isset($__domain) && !in_array($__domain,$systemDomain) && $rs) {
            $seoInfo=get_seo_info($pregRs[1]);
            session("cur_agent_id",$pregRs[1]);
            //if(!$seoInfo)  $this->redirect('/',302);    //此处有可能会造成多次的重定向  $dreamer 20180224
        }
        if(!$seoInfo) $seoInfo=get_seo_info(0,$request->domain());
        if($request->isMobile()){
            config('template.view_path','./tpl/'.$this->themeBasename.'/mobile/');
            config('dispatch_success_tmpl','./tpl/default/mobile/systemMsg/message.html');
            config('dispatch_error_tmpl','./tpl/default/mobile/systemMsg/message.html');
        }else{
            config('template.view_path','./tpl/'.$this->themeBasename.'/');
            config('dispatch_success_tmpl','./tpl/default/systemMsg/message.html');
            config('dispatch_error_tmpl','./tpl/default/systemMsg/message.html');
        }
        parent::__construct($request);
        //$this->checkSiteStatus();
    //判断用户是否登录然后是否已经填写用户名用于第三方登录 whs 20180306
        if($request->action()!="binding_third"){

        if(!empty(session('member_id'))){
            $user=Db::name('member')->where(['id'=>session('member_id')])->find();
          if(empty($user['username'])){
                $this->redirect('member/binding_third');
            }
        } }
        $this->assign('seo',$seoInfo);
    }

    public function _empty(){
        $this->redirect('/');
    }

    private function checkSiteStatus()
    {
        $request=Request::instance();

        $baseConfig=get_config_by_group('base');

        if(isset($baseConfig['site_status']) && $baseConfig['site_status']==0 && !$request->isMobile()){
            header("location:/error/index.html");
            exit('siteClosed');
        }

        if(isset($baseConfig['wap_site_status']) && $baseConfig['wap_site_status']==0 && $request->isMobile()){
            header("location:/error/m.html");
            exit('wapClosed');
        }
    }
}
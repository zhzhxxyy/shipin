<?php
// +----------------------------------------------------------------------
// | baidu[TP5内核]
// +----------------------------------------------------------------------
// | Copyright © 2019-QQ97250974
// +----------------------------------------------------------------------
// | 专业二开仿站定制修改,做最专业的视频点播系统
// +----------------------------------------------------------------------
// | Author: cherish ©2018
// +----------------------------------------------------------------------
/**
 * 系统默认控制器
// +----------------------------------------------------------------------
// | 警告：请勿在index模块下开发任何东西，系统升级会自动覆盖此模块
// +----------------------------------------------------------------------
 * @package app\index\controller
 */

namespace app\index\controller;

use app\admin\model\AdminPlugins as PluginsModel;

class Error extends Home
{
    public function _empty()
    {
        // 运行插件
        if (defined('PLUGIN_ENTRANCE')) {
            /**
             * 支持以下两种URL模式
             * URL模式1 [/plugins/插件名/控制器/[方法]?参数1=参数值&参数2=参数值]
             * URL模式2 [/plugins.php?_p=插件名&_c=控制器&_a=方法&参数1=参数值&参数2=参数值] 推荐
             */
            $plugin = $_GET['_p'] = input('param._p');
            $controller = $_GET['_c'] = input('param._c', 'Index');
            $controller = ucfirst($controller);
            $action = $_GET['_a'] = input('param._a', 'index');

            $params = $this->request->except(['_p', '_c', '_a'], 'param');
            if (empty($plugin)) {
                return $this->error('插件参数传递错误！');
            }            
            if (!PluginsModel::where(['name' => $plugin, 'status' => 2])->find() ) {
                return $this->error("插件可能不存在或者未安装！");
            }
            if (!plugins_action_exist($plugin.'/'.$controller.'/'.$action, 'home')) {
                return $this->error("插件方法不存在[".$plugin.'/'.$controller.'/'.$action."]！");
            }
            return plugins_action($plugin.'/'.$controller.'/'.$action, $params, 'home');
        }
        return $this->error('这是系统默认模块，您可以在后台指定其他模块为默认访问首页！', '', '', 100);
    }
}

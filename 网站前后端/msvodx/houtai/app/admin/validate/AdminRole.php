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
namespace app\admin\validate;

use think\Validate;

/**
 * 角色验证器
 * @package app\admin\validate
 */
class AdminRole extends Validate
{
    //定义验证规则
    protected $rule = [
        'name|角色名称' => 'require|unique:admin_role',
        'auth|设置权限'    => 'require',
        'status|状态设置'  => 'require|in:0,1',
    ];

    //定义验证提示
    protected $message = [
        'name.require' => '请输入角色名称',
        'name.unique' => '角色名称已存在',
        'auth.require'    => '请设置权限',
        'status.require'    => '请设置角色状态',
    ];
}

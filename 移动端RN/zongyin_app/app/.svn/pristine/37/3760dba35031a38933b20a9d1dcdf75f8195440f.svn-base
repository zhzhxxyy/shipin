

import {Platform, AsyncStorage, Alert, DeviceEventEmitter} from 'react-native'

export default class TabGame {

    static get() {
        var inputVal = ''; //用户填写的倍数
        var zhushus = []; //注数数组;
        var currNumber = [] //存储每组位数的数组
        var minMoney = 1; //每注金额
        var lastMoney = 0.00;//计算出的金额
        var AllZhushu = 0;//方案注数
        var AllMoney = 0;//方案注数金额
        var danshiNumberL = 0;//单式号码长度
        var yesArr = [];//单式正确的数组
        var orderList = [];//投注数组
        var yrates = k3lotteryrates.rates;
        var _thisPlayid = '';
        var maxbeishu = 10000000;
        var wxGetMaxMoney = { //每种玩法的可中金额
            wx_fs: 192000.00,
            wx_zx120: 1600.00,
            wx_zx60: 3200.00,
            wx_zx30: 6400.00,
            wx_zx20: 9600.00,
            wx_zx10: 19200.00,
            wx_zx5: 38400.00,
            wx_1mbdw: 4.68,
            wx_2mbdw: 13.08,
            wx_3mbdw: 44.13,
            wx_yffs: 4.68,
            wx_hscs: 23.56,
            wx_sxbx: 224.29,
            wx_sjfc: 4173.91,
        }


        function tabGameInit() {

            _thisPlayid = 'dweid';
            rates = yrates[_thisPlayid];
            gameSwitch($('.bet_filter_box'), ssc_1x_title, ssc_1x_arr);
            $('.play_select_prompt').find('span[way-data="tabDoc"]')
                .html(rates.remark + "赔率" + '<em style="color:red;">' + rates.rate + '</em>倍');
            gameNumber(wx_fs);
            // _thisPlayid = 'dxdsdwd';
            // rates = yrates[_thisPlayid];
            // gameSwitch($('.bet_filter_box'),ssc_dsds_title,ssc_dsds_arr);
            // $('.play_select_prompt').find('span[way-data="tabDoc"]')
            //       .html('每位至少选择一个号码，竞猜开奖号码的全部五位，号码和位置都对应即中奖，奖金 <em style="color:red;">'+rates.maxjj+'</em>元');
            // gameNumberZxbd(dxdsdwd,'dxdsdwd');
        }

        tabGameInit();
        getUserBetsListToday(lotteryname);
        window.setInterval("getUserBetsListToday(lotteryname);", 6000);

        if ($('.selectMultipInput').val() <= 1) {
            $('.reduce').addClass('noReduce');
        }

        //倍数减
        $('.reduce').on('click', function () {
            addAndSubtract('-');
            countMoney();
        })
        //倍数加
        $('.selectMultiple .add').on('click', function () {
            addAndSubtract('+');
            countMoney();
        })
        //倍数输入框
        $('.selectMultipInput').on('change keyup', function () {
            addAndSubtract();
            countMoney();
        })

        //人民币单位换算
        $('.selectMultipleCon').on('change', function () {
            countMoney();
        })

        //号码点击
        $('.g_Number_Section').on('click', '.selectNumbers a', function () {
            if (_thisPlayid == 'zuxcsbd' || _thisPlayid == 'zuxzsbd' || _thisPlayid == 'zuxhsbd' || _thisPlayid == 'zuxcebd' || _thisPlayid == 'zuxhebd') {
                $(this).addClass('curr').siblings().removeClass('curr');
            } else {
                if ($(this).hasClass('curr')) {
                    $(this).removeClass('curr');
                } else {
                    $(this).addClass('curr')
                }
            }
            currNumber = currList();
            countFun()
            countMoney();
        })

        function countFun() {
            switch (_thisPlayid) {
                case 'wxzhixfs': //五星组选复式
                    zhushus = combination(currNumber);
                    break;
                case 'wxzxyel':
                    zhushus = combine(currNumber[0], 5);
                    break;
                case 'wxzxls'://五星组选60
                    zhushus.length = combine60();

                    break;
                case 'wxzxsl':
                    zhushus.length = combine30();
                    break;
                case 'wxzxel':
                    zhushus.length = combine20();
                    break;
                case 'wxzxyl':
                    zhushus.length = combine10();
                    break;
                case 'wxzxw':
                    zhushus.length = combine5();
                    break;
                case 'bdw5x1m':
                    zhushus.length = currNumber[0].length;
                    break;
                case 'bdw5x2m':
                    zhushus = combine(currNumber[0], 2);
                    break;
                case 'bdw5x3m':
                    zhushus = combine(currNumber[0], 3);
                    break;
                case 'qwyffs':
                case 'qwhscs':
                case 'qwsxbx':
                case 'qwsjfc':
                    zhushus.length = currNumber[0].length;
                    break;
                case 'sixzhixfsh':
                    zhushus = combination(currNumber);
                    break;
                case 'hsizxes':
                    zhushus = combine(currNumber[0], 4);
                    break;
                case 'hsizxye':
                    zhushus.length = sxCombine12(currNumber);
                    break;
                case 'hsizxl':
                    zhushus = combine(currNumber[0], 2);
                    break;
                case 'hsizxs':
                    zhushus.length = sxCombine4(currNumber);
                    break;
                case 'bdw4x1m':
                    zhushus.length = currNumber[0].length;
                    break;
                case 'bdw4x2m':
                    zhushus = combine(currNumber[0], 2);
                    break;
                case 'sxzhixfsq':
                case 'sxzhixfsz':
                case 'sxzhixfsh':
                    zhushus = combination(currNumber);
                    break;
                case 'zhixhzqs':
                case 'zhixhzzs':
                case 'zhixhzhs':
                    zhushus.length = qszxhzCombine();
                    break;
                case 'kuaduqs':
                case 'kuaduzs':
                case 'kuaduhs':
                    zhushus.length = qskdCombine();
                    break;
                case 'zuxhzqs':
                case 'zuxhzzs':
                case 'zuxhzhs'://组选和值
                    zhushus.length = qszuxhzCombine();
                    break;
                case 'sxzuxzsq':
                case 'sxzuxzsz':
                case 'sxzuxzsh':
                    zhushus.length = currNumber[0].length * (currNumber[0].length - 1);
                    break;
                case 'sxzuxzlq':
                case 'sxzuxzlz':
                case 'sxzuxzlh':
                    zhushus = combine(currNumber[0], 3);
                    break;
                case 'zuxcsbd':
                case 'zuxzsbd':
                case 'zuxhsbd':
                    zhushus.length = 54;
                    break;
                case 'bdwqs':
                case 'bdwzs':
                case 'bdwhs':
                    zhushus.length = currNumber[0].length;
                    break;
                case 'bdwqs2m':
                case 'bdwzs2m':
                case 'bdwhs2m':
                    zhushus = combine(currNumber[0], 2);
                    break;
                case 'exzhixfsq':
                case 'exzhixfsh':
                    zhushus = combination(currNumber);
                    break;
                case 'zhixhzqe':
                case 'zhixhzhe':
                    zhushus.length = hezxhz();
                    break;
                case 'kuaduqe':
                case 'kuaduhe':
                    zhushus.length = exkuadu();
                    break;
                case 'exzuxfsq':
                case 'exzuxfsh':
                    zhushus = combine(currNumber[0], 2);
                    break;
                case 'zuxhzqe':
                case 'zuxhzhe':
                    zhushus.length = exzuxhz();
                    break;
                case 'zuxcebd':
                case 'zuxhebd':
                    zhushus.length = 9;
                    break;
                case 'dweid':
                    zhushus.length = $('.g_Number_Section').find('.curr').length;
                    break;
                case 'dxdsqe':
                case 'dxdshe':
                case 'dxdsqs':
                case 'dxdshs':
                    zhushus = combination(currNumber);
                    break;

                case 'dxdsdwd':
                    zhushus.length = $('.g_Number_Section').find('.curr').length;
                    break;

            }
            //console.log(_thisPlayid,zhushus.length,currNumber);
        }

        var d_balls = [];
        var t_balls = [];
        var d_count = 0;
        var t_count = 0;

        function combineArrUpdata() {
            d_balls = [];
            t_balls = [];
            d_count = 0;
            t_count = 0;
            for (var i = 0; i < currNumber.length; i++) {
                for (var j = 0; j < currNumber[i].length; j++) {
                    if (i == 0) {
                        d_balls[currNumber[i][j]] = currNumber[i][j]
                    } else {
                        t_balls[currNumber[i][j]] = currNumber[i][j]
                    }
                }
                if (i == 0) {
                    d_count = currNumber[i].length;
                } else {
                    t_count = currNumber[i].length;
                }
            }
        }

        var arrexzuxhz = [0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 4, 4, 3, 3, 2, 2, 1, 1];

        function exzuxhz() {
            var itemcount = 0;
            var vballs = [];
            for (var i = 0; i < currNumber.length; i++) {
                for (var k = 0; k < currNumber[i].length; k++) {
                    vballs[currNumber[i][k]] = currNumber[i][k]
                }
            }
            for (j = 0; j < vballs.length; j++) {
                if (vballs[j] != "" && !isNaN(vballs[j])) {
                    itemcount += arrexzuxhz[parseInt(vballs[j])];
                }
            }
            return itemcount;
        }

        var arrkuaduex = [10, 18, 16, 14, 12, 10, 8, 6, 4, 2];

        function exkuadu() {
            var itemcount = 0;
            var vballs = [];
            for (var i = 0; i < currNumber.length; i++) {
                for (var k = 0; k < currNumber[i].length; k++) {
                    vballs[currNumber[i][k]] = currNumber[i][k]
                }
            }
            for (j = 0; j < vballs.length; j++) {
                if (vballs[j] != "" && !isNaN(vballs[j])) {
                    itemcount += arrkuaduex[parseInt(vballs[j])];
                }
            }
            return itemcount;
        }

        var arrzxhzex = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1];

        function hezxhz() {
            var itemcount = 0;
            var vballs = [];
            for (var i = 0; i < currNumber.length; i++) {
                for (var k = 0; k < currNumber[i].length; k++) {
                    vballs[currNumber[i][k]] = currNumber[i][k]
                }
            }
            for (j = 0; j < vballs.length; j++) {
                if (vballs[j] != "" && !isNaN(vballs[j])) {
                    itemcount += arrzxhzex[parseInt(vballs[j])];
                }
            }

            return itemcount;
        }

        var arrzuxhz = [1, 2, 2, 4, 5, 6, 8, 10, 11, 13, 14, 14, 15, 15, 14, 14, 13, 11, 10, 8, 6, 5, 4, 2, 2, 1];

        function qszuxhzCombine() {
            var itemcount = 0;
            var vballs = [];
            var string = [];
            for (var i = 0; i < currNumber.length; i++) {
                for (var k = 0; k < currNumber[i].length; k++) {
                    vballs[currNumber[i][k]] = currNumber[i][k];
                }
            }
            for (j = 0; j < vballs.length; j++) {
                if (vballs[j] != "" && !isNaN(vballs[j])) {

                    itemcount += parseInt(arrzuxhz[parseInt(vballs[j]) - 1]);
                }
            }
            return itemcount;
        }

        var arrkuadusx = [10, 54, 96, 126, 144, 150, 144, 126, 96, 54];

        function qskdCombine() {
            var itemcount = 0;
            var vballs = [];
            for (var i = 0; i < currNumber.length; i++) {
                for (var k = 0; k < currNumber[i].length; k++) {
                    vballs[currNumber[i][k]] = currNumber[i][k]
                }
            }
            for (j = 0; j < vballs.length; j++) {
                if (vballs[j] != "" && !isNaN(vballs[j])) {
                    itemcount += arrkuadusx[parseInt(vballs[j])];
                }
            }

            return itemcount;
        }

        var arrzxhz = [1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69, 73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1];

        function qszxhzCombine() {
            //console.log(currNumber);
            var itemcount = 0;
            var vballs = [];
            for (var i = 0; i < currNumber.length; i++) {
                for (var k = 0; k < currNumber[i].length; k++) {
                    vballs[currNumber[i][k]] = currNumber[i][k]
                }
            }
            for (var j = 0; j < vballs.length; j++) {
                if (vballs[j] != "" && !isNaN(vballs[j])) {
                    itemcount += arrzxhz[parseInt(vballs[j])];
                }
            }

            return itemcount;
        }

        function sxCombine4() {
            combineArrUpdata();
            var itemcount = 0;
            if (d_count > 0 && t_count > 0) {
                for (var i = 0; i < d_balls.length; i++) {
                    if (d_balls[i] != undefined && d_balls[i] != "") {
                        if (t_balls[i] != undefined && t_balls[i] != "") {
                            if (t_count > 1) {
                                itemcount += t_count - 1;
                            }
                        } else {
                            itemcount += t_count;
                        }
                    }
                }
            }
            return itemcount;
        }

        function sxCombine12() {
            combineArrUpdata();
            var itemcount = 0;
            if (d_count > 0 && t_count > 1) {
                for (var i = 0; i < d_balls.length; i++) {
                    if (d_balls[i] != undefined && d_balls[i] != "") {
                        if (t_balls[i] != undefined && t_balls[i] != "") {
                            if (t_count > 2) {
                                itemcount += (t_count - 1) * (t_count - 2) / 2;
                            }
                        } else {
                            itemcount += t_count * (t_count - 1) / 2;
                        }
                    }
                }
            }
            return itemcount;
        }

        function combine5() {
            combineArrUpdata();
            var itemcount = 0;
            if (d_count > 0 && t_count > 0) {
                for (var i = 0; i < d_balls.length; i++) {
                    if (d_balls[i] != undefined && d_balls[i] != "") {
                        if (t_balls[i] != undefined && t_balls[i] != "") {
                            if (t_count > 1) {
                                itemcount += t_count - 1;
                            }
                        } else {
                            itemcount += t_count;
                        }
                    }
                }
            }
            return itemcount;
        }

        function combine10() {
            combineArrUpdata();
            var itemcount = 0;
            if (d_count > 0 && t_count > 0) {
                for (var i = 0; i < d_balls.length; i++) {
                    if (d_balls[i] != undefined && d_balls[i] != "") {
                        if (t_balls[i] != undefined && t_balls[i] != "") {
                            if (t_count > 1) {
                                itemcount += t_count - 1;
                            }
                        } else {
                            itemcount += t_count;
                        }
                    }
                }
            }
            return itemcount;
        }

        function combine20() {
            combineArrUpdata();
            var itemcount = 0;
            if (d_count > 0 && t_count > 1) {
                for (var i = 0; i < d_balls.length; i++) {
                    if (d_balls[i] != undefined && d_balls[i] != "") {
                        if (t_balls[i] != undefined && t_balls[i] != "") {
                            if (t_count > 2) {
                                itemcount += (t_count - 1) * (t_count - 2) / 2;
                            }
                        } else {
                            itemcount += t_count * (t_count - 1) / 2;
                        }
                    }
                }
            }
            return itemcount;
        }

        function combine30() {
            combineArrUpdata();
            var itemcount = 0;
            if (d_count > 1 && t_count > 0) {
                for (var i = 0; i < t_balls.length; i++) {
                    if (t_balls[i] != undefined && t_balls[i] != "") {
                        if (d_balls[i] != undefined && d_balls[i] != "") {
                            if (d_count > 2) {
                                itemcount += (d_count - 1) * (d_count - 2) / 2;
                            }
                        } else {
                            itemcount += d_count * (d_count - 1) / 2;
                        }
                    }
                }
            }
            return itemcount;
        }

        function combine60() {
            combineArrUpdata();
            var recount = 0; //重复数
            if (d_balls && d_balls.length > 0 && t_balls && t_balls.length > 0) {
                for (i = 0; i < d_balls.length; i++) {
                    for (j = 0; j < t_balls.length; j++) {
                        if (t_balls[j] && t_balls[j] == d_balls[i]) {
                            recount++;
                        }
                    }
                }
            }

            var itemcount = 0;
            if (t_count >= 3 && d_count >= 1) {
                for (d_count; d_count > 0; d_count--) {
                    if (recount > 0) {
                        var diffcount = t_count - 4;
                        var topcount = t_count - 1;
                        var subcount = t_count - 4;
                        if (diffcount > 0) {
                            var temp = t_count - 1;
                            while (diffcount > 1) {
                                diffcount--;
                                temp--;
                                topcount = topcount * temp;
                                subcount = subcount * diffcount;
                            }
                            itemcount += (topcount / subcount);
                        } else if (diffcount < 0) {

                        } else {
                            itemcount += 1;
                        }
                        recount--;
                    } else {
                        var diffcount = t_count - 3;
                        var topcount = t_count;
                        var subcount = t_count - 3;
                        if (diffcount > 0) {
                            var temp = t_count;
                            while (diffcount > 1) {
                                diffcount--;
                                temp--;
                                topcount = topcount * temp;
                                subcount = subcount * diffcount;
                            }
                            itemcount += (topcount / subcount);
                        } else {
                            itemcount += 1;
                        }
                    }
                }
            }
            return itemcount;
        }

        //投注区删除单个
        $('.yBettingLists').on('click', '.sc', function () {
            var len = $('.yBettingLists').find('.yBettingList');
            var _id = $(this).parent().attr('id');
            var indexs = 0;
            len.each(function (i) {
                if (_id == orderList[i].trano) {
                    indexs = i;
                }
            });
            orderList.splice(indexs, 1);
            $(this).parents('.yBettingList').remove();
            //console.log(orderList);
            countAll();
        })

        //少于一注
        $('.yBettingLists').on('click', '.numberInfo', function () {
            var text = $(this).siblings('.number').find('em').text();
            alt(text);
        })

        //清空单号
        $('#orderlist_clear').on('click', function () {
            $('.yBettingLists').html('');
            orderList = [];
            countAll();
        })

        //单式textarea框
        $('.g_Number_Section').on('change keyup', '#text', function () {
            chkPrice(this);
            chkLast(this);
            var text = $('#text').val();
            checkNumber(text, danshiNumberL);
            yesArr = unique1(yesArr);
            currNumber = yesArr;
            zhushus = yesArr;
            countMoney();
        })

        //去重数组
        function unique1(args) {
            var str1 = [];
            for (var i = 0; i < args.length; i++) {
                if (str1.indexOf(args[i]) < 0) {
                    str1.push(args[i])
                }
            }
            return str1;
        }

        //删除错误项
        $('.g_Number_Section').on('click', '.remove_btn', function () {
            var text = $('#text').val();
            checkNumber(text, danshiNumberL, 'remove');
        })

        //检查格式是否正确
        $('.g_Number_Section').on('click', '.test_istrue', function () {
            var text = $('#text').val();
            checkNumber(text, danshiNumberL, 'test');
        })

        //清空文本
        $('.g_Number_Section').on('click', '.empty_text', function () {
            $('#text').val('');
            currNumber = [];
            zhushus = [];
            countMoney();
        })

        //玩法内容切换
        $('.bet_filter_box').on('click', '.bet_options', function () {
            var _thisType = $(this).attr('lottery_code_two');
            $('#bet_filter').find('.bet_options').removeClass('curr');
            $(this).addClass('curr');
            $('.g_Number_Section').html('');
            currNumber = [];
            zhushus = [];
            countMoney();
            _thisPlayid = _thisType;
            rates = yrates[_thisPlayid];
            // console.log(">>>>>>" + JSON.stringify(rates));
            switch (_thisType) {
                case 'wxzhixfs':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_fs);
                    break;
                case 'wxzhixds':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    danshiNumberL = 5;
                    danshiGame();
                    break;
                case 'wxzxyel':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_zx_120);
                    break;
                case 'wxzxls':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_zx_60);
                    break;
                case 'wxzxsl':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_zx_60);
                    break;
                case 'wxzxel':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_zx_20);
                    break;
                case 'wxzxyl':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_zx_10);
                    break;
                case 'wxzxw':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_zx_5);
                    break;
                case 'bdw5x1m':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_bdw);
                    break;
                case 'bdw5x2m':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_bdw);
                    break;
                case 'bdw5x3m':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_bdw);
                    break;
                case 'qwyffs':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(['一帆风顺']);
                    break;
                case 'qwhscs':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(['好事成双']);
                    break;
                case 'qwsxbx':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(['三星报喜']);
                    break;
                case 'qwsjfc':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(['四季发财']);
                    break;
                case 'sixzhixfsh':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(sx_fs);
                    break;
                case 'sixzhixdsh':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    danshiNumberL = 4;
                    danshiGame();
                    break;
                case 'hsizxes':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(sx_zx24);
                    break;
                case 'hsizxye':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(sx_zx12);
                    break;
                case 'hsizxl':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(sx_zx6);
                    break;
                case 'hsizxs':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(sx_zx4);
                    break;
                case 'bdw4x1m':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(sx_bdw);
                    break;
                case 'bdw4x2m':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(sx_bdw);
                    break;
                case 'sxzhixfsq':
                case 'sxzhixfsz':
                case 'sxzhixfsh':
                    if (_thisType == 'sxzhixfsq') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                        gameNumber(q3_sxzhixfsq);
                    } else if (_thisType == 'sxzhixfsz') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                        gameNumber(z3_sxzhixfsq);
                    } else if (_thisType == 'sxzhixfsh') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                        gameNumber(h3_sxzhixfsq);
                    }
                    break;
                case 'sxzhixdsq':
                case 'sxzhixdsz':
                case 'sxzhixdsh':
                    if (_thisType == 'sxzhixdsq') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'sxzhixdsz') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'sxzhixdsh') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    danshiNumberL = 3;
                    danshiGame();
                    break;
                case 'zhixhzqs':
                case 'zhixhzzs':
                case 'zhixhzhs':
                    if (_thisType == 'zhixhzqs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zhixhzzs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zhixhzhs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(q3_zhixhzqs, 27);
                    break;
                case 'kuaduqs':
                case 'kuaduzs':
                case 'kuaduhs':
                    if (_thisType == 'kuaduqs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'kuaduzs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'kuaduhs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(q3_kuaduqs);
                    break;
                case 'zuxhzqs':
                case 'zuxhzzs':
                case 'zuxhzhs':
                    if (_thisType == 'zuxhzqs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zuxhzzs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zuxhzhs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(q3_zhixhzqs, 26, 1);
                    break;
                case 'sxzuxzsq':
                case 'sxzuxzsz':
                case 'sxzuxzsh':
                    if (_thisType == 'sxzuxzsq') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'sxzuxzsz') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'sxzuxzsh') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(q3_sxzuxzsq);
                    break;
                case 'sxzuxzlq':
                case 'sxzuxzlz':
                case 'sxzuxzlh':
                    if (_thisType == 'sxzuxzlq') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'sxzuxzlz') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'sxzuxzlh') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(q3_sxzuxzlq);
                    break;
                case 'sxhhzxq':
                case 'sxhhzxz':
                case 'sxhhzxh':
                    if (_thisType == 'sxhhzxq') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'sxhhzxz') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'sxhhzxh') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    danshiNumberL = 3;
                    danshiGame();
                    break;
                case 'zuxcsbd':
                case 'zuxzsbd':
                case 'zuxhsbd':
                    if (_thisType == 'zuxcsbd') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zuxzsbd') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zuxhsbd') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumberZxbd(zuxcsbd);
                    break;
                case 'qszsds':
                case 'zszsds':
                case 'hszsds':
                    if (_thisType == 'qszsds') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zszsds') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'hszsds') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    danshiNumberL = 3;
                    danshiGame();
                    break;
                case 'qszlds':
                case 'zszlds':
                case 'hszlds':
                    if (_thisType == 'qszlds') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zszlds') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'hszlds') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    danshiNumberL = 3;
                    danshiGame();
                    break;
                case 'bdwqs':
                case 'bdwzs':
                case 'bdwhs':
                    if (_thisType == 'bdwqs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'bdwzs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'bdwhs') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(q3_bdw);
                    break;
                case 'bdwqs2m':
                case 'bdwzs2m':
                case 'bdwhs2m':
                    if (_thisType == 'bdwqs2m') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'bdwzs2m') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'bdwhs2m') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(q3_bdw);
                    break;
                case 'exzhixfsq':
                case 'exzhixfsh':
                    if (_thisType == 'exzhixfsq') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                        gameNumber(q2_exzhixfs);
                    } else if (_thisType == 'exzhixfsh') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                        gameNumber(h2_exzhixfs);
                    }
                    break;
                case 'exzhixdsq':
                case 'exzhixdsh':
                    if (_thisType == 'exzhixdsq') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'exzhixdsh') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    danshiNumberL = 2;
                    danshiGame();
                    break;
                case 'zhixhzqe':
                case 'zhixhzhe':
                    if (_thisType == 'zhixhzqe') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zhixhzhe') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(ex_exzhixdsh, 18);
                    break;
                case 'kuaduqe':
                case 'kuaduhe':
                    if (_thisType == 'kuaduqe') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'kuaduhe') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(ex_kuaduhe);
                    break;
                case 'exzuxfsq':
                case 'exzuxfsh':
                    if (_thisType == 'exzuxfsq') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'exzuxfsh') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(ex_exzuxfsh);
                    break;
                case 'exzuxdsq':
                case 'exzuxdsh':
                    if (_thisType == 'exzuxdsq') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'exzuxdsh') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    danshiNumberL = 2;
                    danshiGame();
                    break;
                case 'zuxhzqe':
                case 'zuxhzhe':
                    if (_thisType == 'zuxhzqe') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zuxhzhe') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumber(ex_zsxhz, 17, 1);
                    break;
                case 'zuxcebd':
                case 'zuxhebd':
                    if (_thisType == 'zuxcebd') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    } else if (_thisType == 'zuxhebd') {
                        $('.play_select_prompt').find('span[way-data="tabDoc"]')
                            .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    }
                    gameNumberZxbd(ex_zsxbd);
                    break;
                case 'dweid':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_fs);
                    break;


                case 'dxdsqe':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumberZxbd(dxdsqe, 'dxds');
                    break;
                case 'dxdshe':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumberZxbd(dxdshe, 'dxds');
                    break;
                case 'dxdsqs':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumberZxbd(dxdsqs, 'dxds');
                    break;
                case 'dxdshs':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumberZxbd(dxdshs, 'dxds');
                    break;

                //2018-12-03
                case 'dxdsdwd':
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumberZxbd(dxdsdwd, 'dxdsdwd');
                    break;

            }
        })

        function gameNumberZxbd(arr, type) {


            var box = $('.g_Number_Section');
            var dxdsObj = {
                '0': '大',
                '1': '小',
                '2': '单',
                '3': '双'
            }
            var lhhObj = {
                '4': '龙',
                '5': '虎',
                '6': '和',
            }
            for (var i = 0; i < arr.length; i++) {
                var boxList = $('<div class="selectNmuverBox"></div>');
                if (type == 'dxdsdwd') {
                    var boxNumber = $('<div class="selectNumbers"></div>');
                } else {
                    var boxNumber = $('<div class="selectNumbers"></div>');
                }

                boxList.append('<span class="numberTitle">' + arr[i] + '</span>');
                boxList.append(boxNumber);
                if (type == 'dxds') {
                    for (var j in dxdsObj) {
                        boxNumber.append('<a href="javascript:void(0);" class="selectNumber" data-number="' + j + '">' + dxdsObj[j] + '</a>');
                    }
                } else if (type == 'dxdsdwd') {
                    if (i != arr.length - 1) {
                        for (var j in dxdsObj) {
                            boxNumber.append('<a href="javascript:void(0);" class="selectNumber" data-number="' + j + '">' + dxdsObj[j] + '</a>');
                        }
                    } else {
                        for (var j in lhhObj) {
                            boxNumber.append('<a href="javascript:void(0);" class="selectNumber" data-number="' + j + '">' + lhhObj[j] + '</a>');
                        }
                    }
                } else {
                    for (var j = 0; j <= 9; j++) {
                        boxNumber.append('<a href="javascript:void(0);" class="selectNumber" data-number="' + j + '">' + j + '</a>');
                    }
                }

                box.append(boxList);
            }
        }

        //确认选号，添加到投注区
        $('.addtobetbtn').on('click', function () {
            var yBetting = $('.yBettingList');
            var menu0 = $('.play_select_tit').find('.curr').text();
            var menu1 = $('#bet_filter').find('.curr').parent().siblings('.title').text();
            var menu2 = $('#bet_filter').find('.curr').text();
            var times = $('.selectMultipInput').val();
            var selectRmb = $('.selectMultipleCon').val();
            var selectRmbStr = $('.selectMultipleCon').find('option:selected').text();
            var Numbers = '';
            var winningMoney = $('.play_select_prompt').find('span[way-data="tabDoc"] em').text();
            var winningMoneys = times * winningMoney * selectRmb;
            var bool = true;
            var trano = generateMixed(20);
            var rate = yrates[_thisPlayid];

            if (zhushus.length >= 1) {
                for (var i = 0; i < currNumber.length; i++) {
                    for (var j = 0; j < currNumber[i].length; j++) {
                        // if(currNumber[i][j].length > 0){
                        if ((currNumber[i].length - 1) != j) {
                            Numbers += currNumber[i][j] + ' ';
                        } else {
                            Numbers += currNumber[i][j]
                        }
                        // }

                    }
                    // if(currNumber[i].length > 0){
                    if ((currNumber.length - 1) != i) {
                        Numbers = Numbers + ',';
                    }
                    // }

                }

                yBetting.each(function (i) {
                    var gameNumber = $(this).find('.number em').text();
                    if (_thisPlayid == 'dxdsqe' || _thisPlayid == 'dxdshe' || _thisPlayid == 'dxdsqs' || _thisPlayid == 'dxdshs') {
                        gameNumber = gameNumber.replace(/大/g, '0');
                        gameNumber = gameNumber.replace(/小/g, '1');
                        gameNumber = gameNumber.replace(/单/g, '2');
                        gameNumber = gameNumber.replace(/双/g, '3');
                    }
                    var gameNumberType = $(this).find('.number .yBettingType').text();
                    var _thisType = '[' + menu0 + ',' + menu1 + ',' + menu2 + ']';
                    var _thisRmb = $(this).find('.rmb').text();
                    //console.log(gameNumberType == _thisType,gameNumberType, _thisType)
                    if (gameNumber == Numbers && _thisRmb == selectRmbStr && gameNumberType == _thisType) {
                        bool = false;
                        var _thisTimes = parseInt($(this).find('.yBettingTimess').text()) + parseInt(times);
                        winningMoneys = _thisTimes * winningMoney * selectRmb;
                        winningMoneys = winningMoneys.toFixed(2);
                        $(this).find('.yBettingTimess').text(_thisTimes);
                        $(this).find('.maxMoneyNumber').text(winningMoneys + '元');
                        $(this).find('#betting_money').text(zhushus.length * minMoney * _thisTimes * selectRmb);
                        //alert(zhushus.length * minMoney * _thisTimes *  selectRmb);
                        orderList[i].beishu = _thisTimes;
                        orderList[i].price = zhushus.length * minMoney * _thisTimes * selectRmb;
                    }
                })

                if (bool) {
                    var Numbersh = Numbers.replace(/,/g, '|');
                    Numbersh = Numbersh.replace(/\s/g, ',');

                    // var ymaxjj = (rate.maxjj * selectRmb) + '';
                    //     if(ymaxjj.indexOf('.') == '-1'){
                    //       ymaxjj = ymaxjj + '.00';
                    //     }else{
                    //       ymaxjj = ymaxjj.substring(0,ymaxjj.indexOf('.') + 3);
                    //     }

                    // var yminjj = (rate.minjj * selectRmb) + '';
                    //     if(yminjj.indexOf('.') == '-1'){
                    //       yminjj = yminjj + '.00';
                    //     }else{
                    //       yminjj = yminjj.substring(0,yminjj.indexOf('.') + 3);
                    //     }


                    var arr = {
                        'trano': trano,
                        'playtitle': rate.title,
                        'playid': rate.playid,
                        'number': Numbersh,
                        'zhushu': zhushus.length,
                        'price': lastMoney,
                        'minxf': rate.minxf,
                        'totalzs': rate.totalzs,
                        'maxjj': rate.maxjj,
                        'minjj': rate.minjj,
                        'maxzs': rate.maxzs,
                        'rate': rate.rate, //12-10 rate.maxjj->rate.rate
                        'beishu': parseInt(times),
                        'yjf': selectRmb,
                    }
                    orderList.push(arr);
                    if (_thisPlayid == 'dxdsqe' || _thisPlayid == 'dxdshe' || _thisPlayid == 'dxdsqs' || _thisPlayid == 'dxdshs') {
                        Numbers = Numbers.replace(/0/g, '大');
                        Numbers = Numbers.replace(/1/g, '小');
                        Numbers = Numbers.replace(/2/g, '单');
                        Numbers = Numbers.replace(/3/g, '双');
                    }

                    var html = '<dd class="yBettingList" id="' + trano + '">' +
                        '<div class="numberBox yBettingDiv">' +
                        '<span class="number"><div class="yBettingType">[' + menu0 + ',' + menu1 + ',' + menu2 + ']</div> <em>' + Numbers + '</em></span>' +
                        '<a href="javascript:void(0);" class="numberInfo">详细</a> ' +
                        '</div>' +
                        '&nbsp;<div class="yBettingZhushu yBettingDiv">' +
                        '<em>' + zhushus.length + '</em>注' +
                        '</div>' +
                        '&nbsp;<div class="yBettingTimes yBettingDiv">' +
                        '<em class="yBettingTimess">' + times + '</em>倍' +
                        '</div>' +
                        '&nbsp;<div class="rmb yBettingDiv">' +
                        '' + selectRmbStr + '' +
                        '</div>' +
                        '&nbsp;<div class="maxMoney yBettingDiv">' +
                        '可中金额' +
                        '<em class="maxMoneyNumber">' + winningMoneys.toFixed(2) + '元</em>' +
                        '</div>' +
                        '<div class="sc" style="float: right;padding-right: 5px;">' +
                        '<a href="javascript:void(0);">' +
                        '<i class="fa fa-times" style="color: red;"></i>' +
                        '</a>' +
                        '</div>' +
                        '<div id="betting_money" style="display: none;">' + lastMoney + '</div>' +
                        '</dd>';
                    $('.yBettingLists').append(html);
                }
                //console.log(orderList);
                $('.g_Number_Section').find('a').removeClass('curr');
                $('#text').val('');
                currNumber = [];
                zhushus = [];
                countMoney();
                countAll();
            } else {
                alt('最少选择一注')
            }

        })


        //确认投注
        $(document).on("click", "#f_submit_order", function () {
            if (orderList.length < 1) {
                alt('请选择投注号码', -1);
                return false;
            }
            var Orderdetaillist = '';
            var Orderdetailtotalprice = 0;
            for (var i = 0; i < orderList.length; i++) {
                var cur_order = orderList[i];
                var rate = yrates[cur_order.playid];
                var oprice = Number(cur_order.price);
                var cur_number = cur_order.number;
                Orderdetailtotalprice += oprice;
                if (_thisPlayid == 'dxdsqe' || _thisPlayid == 'dxdshe' || _thisPlayid == 'dxdsqs' || _thisPlayid == 'dxdshs') {
                    cur_number = cur_number.replace(/0/g, '大');
                    cur_number = cur_number.replace(/1/g, '小');
                    cur_number = cur_number.replace(/2/g, '单');
                    cur_number = cur_number.replace(/3/g, '双');
                } else {
                    cur_number = cur_order.number;
                }
                Orderdetaillist += "<p>" + rate.title + ':<span class="mark">' + cur_number + '</span>&nbsp;&nbsp;注数:<span class="mark">' + cur_order.zhushu + '</span>&nbsp;&nbsp;金额:<span class="mark">' + oprice.toFixed(2) + "</span></p>";
            }
            $("#Orderdetaillist").html(Orderdetaillist);
            $("#Orderdetailtotalprice").text(Orderdetailtotalprice.toFixed(2));
            //console.log(orderList);
            artDialog({
                title: "投注详情<span style='margin-left:15px;'><img src='" + WebConfigs["ROOT"] + "/resources/images/icon/icon_09.png'>截至时间:<strong class='sty-h gametimes' style='font-weight:normal'>00:00:00</strong></span>",
                content: $("#submitComfirebox").html(),
                cancel: function () {
                },
                ok: function () {
                    if (!user) {
                        alt('请先登录', -1);
                    }
                    $.ajax({
                        type: "POST",
                        url: apirooturl + 'cpbuy',
                        data: {
                            "orderList": orderList,
                            'expect': lottery.currFullExpect,
                            'lotteryname': lotteryname
                        },
                        beforeSend: function () {
                            $('.looding').show();
                        },
                        success: function (json) {
                            if (json.sign) {
                                $("#orderlist_clear").click();
                                getUserBetsListToday(lotteryname);
                                alt('投注成功', 1);
                            } else {
                                alt(json.message, -1);
                            }
                            $('.looding').hide();
                        }
                    })
                },
                lock: true
            });
        });

        //玩法切换
        $(document).on('click', '#j_play_select li', function () {
            var this_attr = $(this).attr('lottery_code');
            $(this).addClass('curr').siblings('li').removeClass('curr');
            $('.g_Number_Section').html('');

            switch (this_attr) {
                case '5x':
                    $('#bet_filter').remove();
                    gameSwitch($('.bet_filter_box'), ssc_5x_title, ssc_5x_arr);
                    _thisPlayid = 'wxzhixfs';
                    rates = yrates[_thisPlayid];
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_fs);
                    break;
                case '4x':
                    $('#bet_filter').remove();
                    gameSwitch($('.bet_filter_box'), ssc_4x_title, ssc_4x_arr);
                    _thisPlayid = 'sixzhixfsh';
                    rates = yrates[_thisPlayid];
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(sx_fs);
                    break;
                case 'q3':
                    $('#bet_filter').remove();
                    gameSwitch($('.bet_filter_box'), ssc_q3_title, ssc_q3_arr);
                    _thisPlayid = 'sxzhixfsq';
                    rates = yrates[_thisPlayid];
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(q3_sxzhixfsq);
                    break;
                case 'z3':
                    $('#bet_filter').remove();
                    gameSwitch($('.bet_filter_box'), ssc_q3_title, ssc_z3_arr);
                    _thisPlayid = 'sxzhixfsz';
                    rates = yrates[_thisPlayid];
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(z3_sxzhixfsq);
                    break;
                case 'h3':
                    $('#bet_filter').remove();
                    gameSwitch($('.bet_filter_box'), ssc_q3_title, ssc_h3_arr);
                    _thisPlayid = 'sxzhixfsh';
                    rates = yrates[_thisPlayid];
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(h3_sxzhixfsq);
                    break;
                case 'q2':
                    $('#bet_filter').remove();
                    gameSwitch($('.bet_filter_box'), ssc_q2_title, ssc_q2_arr);
                    _thisPlayid = 'exzhixfsq';
                    rates = yrates[_thisPlayid];
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(q2_exzhixfs);
                    break;
                case 'h2':
                    $('#bet_filter').remove();
                    gameSwitch($('.bet_filter_box'), ssc_q2_title, ssc_h2_arr);
                    _thisPlayid = 'exzhixfsh';
                    rates = yrates[_thisPlayid];
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(h2_exzhixfs);
                    break;
                case '1x':
                    $('#bet_filter').remove();
                    gameSwitch($('.bet_filter_box'), ssc_1x_title, ssc_1x_arr);
                    _thisPlayid = 'dweid';
                    rates = yrates[_thisPlayid];
                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumber(wx_fs);
                    break;
                case 'dsds':
                    $('#bet_filter').remove();
                    gameSwitch($('.bet_filter_box'), ssc_dsds_title, ssc_dsds_arr);
                    _thisPlayid = 'dxdsdwd';
                    rates = yrates[_thisPlayid];

                    $('.play_select_prompt').find('span[way-data="tabDoc"]')
                        .html(rates.remark + '赔率' + '<em style="color:red;">' + rates.rate + '</em>倍');
                    gameNumberZxbd(dxdsdwd, 'dxdsdwd');
                    break;
            }
            addAndSubtract();
        })

        //全，大，小，奇，偶，清
        $('.g_Number_Section').on('click', '.selectNumberFilters a', function () {
            var _thisAttr = $(this).attr('data-param');
            switch (_thisAttr) {
                case 'js-btn-all':
                    $(this).parent().siblings('.selectNumbers').find('a').addClass('curr');
                    break;
                case 'js-btn-big':
                    $(this).parent().siblings('.selectNumbers').find('a').each(function (i) {
                        if (i < 5) {
                            $(this).removeClass('curr');
                        } else {
                            $(this).addClass('curr');
                        }
                    })
                    break;
                case 'js-btn-small':
                    $(this).parent().siblings('.selectNumbers').find('a').each(function (i) {
                        if (i >= 5) {
                            $(this).removeClass('curr');
                        } else {
                            $(this).addClass('curr');
                        }
                    })
                    break;
                case 'js-btn-odd':
                    $(this).parent().siblings('.selectNumbers').find('a').each(function (i) {
                        if (i % 2 == 0) {
                            $(this).removeClass('curr');
                        } else {
                            $(this).addClass('curr');
                        }
                    });
                    break;
                case 'js-btn-even':
                    $(this).parent().siblings('.selectNumbers').find('a').each(function (i) {
                        if (i % 2 != 0) {
                            $(this).removeClass('curr');
                        } else {
                            $(this).addClass('curr');
                        }
                    });
                    break;
                case 'js-btn-clean':
                    $(this).parent().siblings('.selectNumbers').find('a').removeClass('curr');
                    break;
            }
            currNumber = currList();
            countFun();
            countMoney();
        });

        function util_unique(v, reg, digit, itemsort, baohao) {
            if (digit == undefined || digit == null) {
                digit = 1;
            }
            //v = v.replace(/ /g, ',');
            var sszz = new Array();
            var titems = {};
            var titem;
            while ((titem = reg.exec(v)) != null) {
                var key = titem[0];
                if (itemsort) {
                    if (digit == 1) {
                        key = key.match(/./g).sort().join('');
                    } else if (digit == 2) {
                        key = key.match(/.{2}/g).sort().join(' ');
                    } else {
                        key = key.match(/./g).sort().join('');
                    }
                } else {
                    if (digit == 2) {
                        key = key.match(/.{2}/g).join(' ');
                    }
                }
                if (!titems[key]) {
                    if (baohao) {
                        // 去除豹子号如222，用户前三 中三 后三 任选三混合组选
                        if (!(key.charAt(0) == key.charAt(1) && key.charAt(0) == key.charAt(2) && key.charAt(1) == key.charAt(2))) {
                            titems[key] = 1;
                            sszz.push(key);
                        }
                    } else {
                        titems[key] = 1;
                        sszz.push(key);
                    }
                }
            }
            return sszz;
        };
        function sortNumber(a, b) {
            return a - b
        }

        //检测相同的数字
        function checkRepeat(str) {
            var arr = str.split("");
            for (var i = 0, length = arr.length; i < length - 1; i++) {
                if (arr.slice(i + 1).indexOf(arr[i]) >= 0) {
                    return true;
                }
            }
            return false;
        }

        function checkNumber(string, len, clickObj) {
            var NumberArr = string.split(' ');
            var errArr = [];
            yesArr = [];
            var errString = '';
            var yesString = '';
            var itemcount = 0;
            for (var i = 0; i < NumberArr.length; i++) {
                if (NumberArr[i].length > len || NumberArr[i].length < len) {
                    errArr.push(NumberArr[i]);
                } else {
                    yesArr.push(NumberArr[i]);
                }
            }
            for (var j = 0; j < errArr.length; j++) {
                errString += errArr[j] + ' ';
            }
            for (var k = 0; k < yesArr.length; k++) {
                yesString += yesArr[k] + ' ';
            }

            if (_thisPlayid == 'sxhhzxq' || _thisPlayid == 'sxhhzxz' || _thisPlayid == 'sxhhzxh') {
                var v = string;
                var reg = /\b[0-9]{3}\b/g;
                // 去重复
                var sszz = util_unique(v, reg, 1, true, true);
                sszz = sszz.sort();
                if (sszz) {
                    itemcount = sszz.length;
                    yesArr = sszz;
                }
            }

            if (_thisPlayid == 'qszsds' || _thisPlayid == 'zszsds' || _thisPlayid == 'hszsds') {

                var stringArr = [];
                var lens = yesArr.length;
                //console.log(yesArr)
                for (var x = 0; x < lens; x++) {
                    var yesArrbox = [];
                    stringArr = yesArr[x].split('');
                    stringArr.sort(sortNumber);
                    yesArr[x] = stringArr.join('');
                }

                for (var xx = 0; xx < lens; xx++) {
                    if (yesArr[xx]) {
                        if (!checkRepeat(yesArr[xx]) || /^(\d)\1+$/.test(yesArr[xx])) {
                            yesArr.splice(xx--, 1);
                        }
                    }
                }

            }

            if (_thisPlayid == 'qszlds' || _thisPlayid == 'zszlds' || _thisPlayid == 'hszlds') {

                var stringArr = [];
                var lens = yesArr.length;
                for (var x = 0; x < lens; x++) {
                    var yesArrbox = [];
                    stringArr = yesArr[x].split('');
                    stringArr.sort(sortNumber);
                    yesArr[x] = stringArr.join('');
                }

                for (var xx = 0; xx < lens; xx++) {
                    if (yesArr[xx]) {
                        if (checkRepeat(yesArr[xx]) || /^(\d)\1+$/.test(yesArr[xx])) {
                            yesArr.splice(xx--, 1);
                        }
                    }
                }

            }

            if (_thisPlayid == 'exzuxdsq' || _thisPlayid == 'exzuxdsh') {
                var v = string;
                var reg = /\b([0-9])(?!\1)([0-9])\b/g;
                // 去重复
                var sszz = util_unique(v, reg, 1, true);
                sszz = sszz.sort();
                if (sszz) {
                    itemcount = sszz.length;
                    yesArr = sszz;
                }
            }

            if (clickObj == 'remove') {
                if (string == '') {
                    alt('请投注');
                    return;
                }
                if (errArr.length < 1) {
                    alt('全部投注格式正确');
                } else {
                    $('#text').val(yesString);
                    alt('以下投注格式不正确： <br /> ' + errString + '');
                }
            }

            if (clickObj == 'test') {
                if (string == '') {
                    alt('请投注');
                    return;
                }
                if (errArr.length < 1) {
                    alt('全部投注格式正确');
                } else {
                    alt('以下投注格式不正确： <br /> ' + errString + '');
                }
            }

        }

        function danshiGame() {
            var html = '<div class="g_text">' +
                '<textarea name="" value="123456" id="text" cols="30" rows="10" placeholder="每注号码以空格进行分割"></textarea>' +
                '<button type="button" class="remove_btn">删除错误项</button>' +
                '<button type="button" class="test_istrue">检查格式是否正确	</button>' +
                '<button type="button" class="empty_text">清空文本框</button>' +
                '</div>';
            $('.g_Number_Section').append(html);
        }

        //添加game号码区
        function gameNumber(arr, number, start) {
            var box = $('.g_Number_Section');
            for (var i = 0; i < arr.length; i++) {
                var filterHtml = '<div class="selectNumberFilters">' +
                    '<a href="javascript:void(0);" class="selectNumberFilter" data-param="js-btn-all">全</a>' +
                    '<a href="javascript:void(0);" class="selectNumberFilter" data-param="js-btn-big">大</a>' +
                    '<a href="javascript:void(0);" class="selectNumberFilter" data-param="js-btn-small">小</a>' +
                    '<a href="javascript:void(0);" class="selectNumberFilter" data-param="js-btn-odd">奇</a>' +
                    '<a href="javascript:void(0);" class="selectNumberFilter" data-param="js-btn-even">偶</a>' +
                    '<a href="javascript:void(0);" class="selectNumberFilter" data-param="js-btn-clean">清</a>' +
                    '</div>';
                var boxList = $('<div class="selectNmuverBox"></div>');
                var boxNumber = $('<div class="selectNumbers"></div>');
                boxList.append('<span class="numberTitle">' + arr[i] + '</span>');
                boxList.append(boxNumber);
                boxList.append(filterHtml);
                if (number && start) {
                    for (var j = start; j <= number; j++) {
                        boxNumber.append('<a href="javascript:void(0);" class="selectNumber" data-number="' + j + '">' + j + '</a>');
                    }
                } else if (number) {
                    for (var j = 0; j <= number; j++) {
                        boxNumber.append('<a href="javascript:void(0);" class="selectNumber" data-number="' + j + '">' + j + '</a>');
                    }
                } else {
                    for (var j = 0; j <= 9; j++) {
                        boxNumber.append('<a href="javascript:void(0);" class="selectNumber" data-number="' + j + '">' + j + '</a>');
                    }
                }

                box.append(boxList);
            }
        }

        //添加二级玩法切换
        function gameSwitch(obj, title_arr, option_arrs) {
            var ul = $('<ul></ul>');
            var span = '';
            var bool = true;
            ul.attr('id', 'bet_filter');

            for (var i = 0; i < title_arr.length; i++) {
                var li = $('<li></li>');
                var betOptionDiv = $('<div class="bet_option"></div>');
                li.attr('class', 'bet_filter_item');
                li.append('<strong class="title">' + title_arr[i] + '</strong>');
                for (j in option_arrs[i]) {
                    if (bool) {
                        span = '<span class="bet_options curr" lottery_code_two="' + j + '">' + option_arrs[i][j] + '</span>';
                        bool = false;
                    } else {
                        span = '<span class="bet_options" lottery_code_two="' + j + '">' + option_arrs[i][j] + '</span>';
                    }
                    betOptionDiv.append(span);
                }
                li.append(betOptionDiv);
                ul.append(li);
            }
            $('.bet_filter_item').eq(0).find('.bet_options').eq(0).addClass('curr');
            obj.append(ul);
        }


        //倍数加减fn
        function addAndSubtract(string) {
            inputVal = isNaN(parseInt($('.selectMultipInput').val())) ? 1 : parseInt($('.selectMultipInput').val());
            // if(_thisPlayid == 'dxdsqe' || _thisPlayid == 'dxdshe' || _thisPlayid == 'dxdsqs' || _thisPlayid == 'dxdshs'){
            //   maxbeishu = 1000000;
            // }else{
            //   maxbeishu = 100000;
            // }
            if (inputVal <= 1) {
                //$('.selectMultipInput').val(1);
                $('.reduce').addClass('noReduce');
            }
            if (inputVal > maxbeishu) {
                $('.selectMultipInput').val(maxbeishu);
                $('.reduce').removeClass('noReduce');
                $('.selectMultiple .add').addClass('noReduce');
                return;
            }
            if ('+' == string) {
                inputVal++;
                if (inputVal >= maxbeishu) {
                    $('.selectMultipInput').val(maxbeishu);
                    $('.selectMultiple .add').addClass('noReduce');
                    return;
                }
                $('.selectMultiple .add').removeClass('noReduce');
                $('.selectMultipInput').val(inputVal);
            } else if ('-' == string) {
                inputVal--;
                if (inputVal <= 1) {
                    $('.selectMultipInput').val(1);
                    $('.reduce').addClass('noReduce');
                    return;
                }
                $('.reduce').removeClass('noReduce');
                $('.selectMultipInput').val(inputVal);
            }
            if (inputVal > 1 && inputVal < maxbeishu) {
                $('.reduce').removeClass('noReduce');
            }
            if (inputVal < maxbeishu) {
                $('.selectMultiple .add').removeClass('noReduce');
            }
        }

        //生成随机订单号
        var chars = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];

        function generateMixed(n) {
            var res = "";
            for (var i = 0; i < n; i++) {
                var id = Math.ceil(Math.random() * 35);
                res += chars[id];
            }
            return res;
        }

        //计算方案注数
        function countAll() {
            var eachZhushus = 0;
            var eachMoneys = 0;

            $('.yBettingList').each(function (i) {
                var eachZhushu = parseInt($(this).find('.yBettingZhushu em').text());
                var eachMoney = parseFloat($(this).find('#betting_money').text());
                eachZhushus += eachZhushu;
                eachMoneys += eachMoney;
            })

            AllZhushu = eachZhushus;
            AllMoney = eachMoneys;
            $('#f_gameOrder_lotterys_num').text(AllZhushu);
            $('#f_gameOrder_amount').text(AllMoney.toFixed(2));
        }

        //计算选号金额
        function countMoney() {
            var currZhushu = parseInt(zhushus.length);
            var currTimes = parseInt($('.selectMultipInput').val());
            var currSlect = parseFloat($('.selectMultipleCon').val());
            //2018-10-08 2
            var rate = yrates[_thisPlayid];

            var toTal = currZhushu * minMoney * currTimes * currSlect;
            lastMoney = toTal.toFixed(2);
            $('.zhushu').text(currZhushu);
            $('.selectMultipleOldMoney').text(lastMoney);
        }

        //组合排列
        function combination(arr) {
            var sarr = [[]];

            for (var i = 0; i < arr.length; i++) {
                var sta = [];
                for (var j = 0; j < sarr.length; j++) {
                    for (var k = 0; k < arr[i].length; k++) {
                        sta.push(sarr[j].concat(arr[i][k]));
                    }
                }
                sarr = sta;
            }
            return sarr;
        }

        //组合算法
        function combine(arr, num) {
            var r = [];
            (function f(t, a, n) {
                if (n == 0) return r.push(t);
                for (var i = 0, l = a.length; i <= l - n; i++) {
                    f(t.concat(a[i]), a.slice(i + 1), n - 1);
                }
            })([], arr, num);
            return r;
        }

        //获取每个位数选中的数
        function currList() {
            var currArr = [];
            $('.selectNumbers').each(function (i) {
                var acArr = [];
                $(this).find('.curr').each(function (i) {
                    // acArr.push($(this).attr('data-number'));
                    acArr.push($(this).text());
                })
                currArr.push(acArr);
            })
            return currArr;
        }

        //验证数字空格
        function chkPrice(obj) {
            obj.value = obj.value.replace(/[^\d.\s*]/g, "");
            //必须保证第一位为数字而不是.
            obj.value = obj.value.replace(/^\./g, "");
            //保证只有出现一个.而没有多个.
            obj.value = obj.value.replace(/\.{2,}/g, ".");
            //保证.只出现一次，而不能出现两次以上
            obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        }

        //非法字符截取
        function chkLast(obj) {
            if (obj.value.substr((obj.value.length - 1), 1) == '.')
                obj.value = obj.value.substr(0, (obj.value.length - 1));
        }

        /**
         * 当天投注记录
         * @param shortName
         */
        function getUserBetsListToday(_lotteryname) {
            if (!user || user.islogin != 1) {
                return false;
            }
            lotteryname = _lotteryname ? _lotteryname : lotteryname;
            var tabs = $("#userBetsListToday");
            // tabs.empty();
            var url = apirooturl + 'betslisttoday';
            var pagination = $.pagination({
                render: '.paging',
                pageSize: jqueryGridRows,
                pageLength: 7,
                ajaxType: 'post',
                //hideInfos: false,
                hideGo: true,
                ajaxUrl: url,
                ajaxData: {
                    "lotteryname": lotteryname, 'jqueryGridPage': jqueryGridPage, 'jqueryGridRows': jqueryGridRows
                },
                complete: function () {
                },
                success: function (data) {
                    tabs.empty();
                    $.each(data, function (index, val) {

                        var html = '<tr id="' + val.trano + '">';
                        html += '<td> <a href="javascript:getBillInfo(\'' + val.trano + '\')">' + val.trano + '</a></td>';
                        html += '<td>' + val.expect + '</td>';
                        html += '<td>' + val.opencode + '</td>';
                        html += '<td>' + val.playtitle + '(' + val.tzcode + ')' + '</td>';
                        html += '<td>' + val.mode + '</td>';
                        html += '<td>' + val.amount + '</td>';
                        html += '<td>' + (val.okamount ? val.okamount : 0) + '</td>';
                        html += '<td>' + val.oddtime + '</td>';
                        html += '<td>';
                        //'<td>' + val.betsTimes + '</td>' +
                        if (val.isdraw == -1) { // 未中奖绿色
                            html += '<span style="color:green">未中奖</span>';
                        } else if (val.isdraw == 1) { // 已中奖红色
                            html += '<span style="color:red">已中奖</span>';
                        } else if (val.isdraw == -2) {
                            html += '<del>已撤单</del>';
                        } else if (val.isdraw == 0) {
                            html += '<span id="span' + val.trano + '">未开奖</span>';
                        } else {
                            html += '<span>未知状态</span>';
                        }
                        html += '</td>';
                        html += '</tr>';
                        tabs.append(html);


                    });
                },
                pageError: function (response) {
                },
                emptyData: function () {
                }
            });
            pagination.init();
        }

    }



}

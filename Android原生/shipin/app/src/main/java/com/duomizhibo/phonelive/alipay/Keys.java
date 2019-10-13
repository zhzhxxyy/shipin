/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.duomizhibo.phonelive.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static  String DEFAULT_PARTNER = "2088411851435855";

	//收款支付宝账号
	public static  String DEFAULT_SELLER = "zqm@taianweb.com";

	//将RSA私钥转换成PKCS8格式
	public static  String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANsw9XNYs2GWps4O" +
			"MKksMkYqZypkvS7EzlPvDcR6cjqxJFMFI26U67YbUBdKEzIpCofpGCotTQ8UyZN9" +
			"tiVPud/kuPW0n9RU7CeQapA0hk1zZI+uXhGt1ktUQBTT+MpMe8szBrS1qhKJoonK" +
			"q/vfw+08OkesFo6oCIHkStEdLrntAgMBAAECgYBgqtPlHf5mkJFaGMn/If2+Eh9T" +
			"hAAnKyavv6L7vuC3373cW0zIDSdzNdJ5ovKaUZ1SWUuN9lKgzxMjV/LHu8SGF1pr" +
			"slK2uxOsqIdgNplKiPYToRdD0LHiB2VVd26SmNHJ/cbDOI2Wj5lXivruTcQzA9uw" +
			"0yv3ZFGnr16kOpAtJQJBAPli12bq5SykCsGFijzVwlL7i4KNf7vQn/It6DM+H1II" +
			"IQIilo9h3oVdpntSzKJAm4PEC29IYvwayl40QZZCq5sCQQDhAR2CN7FYP67MwDlQ" +
			"Ih5JdT4s8MaX0Gna3w09bj0ebSgMBUwCZzYWbV98Nv5A9DcWHuEiKipbsxUFMfEd" +
			"Ll0XAkEAoNN1RhHFqXxA03xjIchYgVtnJNJLxbtM6slgLWuqlyRW5SGZJu5eqnMy" +
			"oeVLwncX02niVenArAQ67XWVtmlYcQJAYwPCoJMxj8w2eBP/JKxe96SIf+5U9mnY" +
			"q2CMywPAEaune+K42DXjL2tiIZ9xs9PEig98szQq/7+G+IpJuLW2cQJAWbKtHArv" +
			"sJBc6SfjVp216Dj1KPTXQ9+CMhrDGFA8WQ08XRiinW1STM/eHtCSa0Ran4TEL57a" +
			"Zvan+b6WSGNppQ==";


}

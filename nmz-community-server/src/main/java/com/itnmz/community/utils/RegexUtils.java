package com.itnmz.community.utils;

import cn.hutool.core.util.StrUtil;

public class RegexUtils {

    /**
     * 手机号正则
     */
    public static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    /**
     * 是否是无效手机格式
     * @param phone 要校验的手机号
     * @return true:符合，false：不符合
     */
    public static boolean isPhoneInvalid(String phone){
        return mismatch(phone, PHONE_REGEX);
    }

    // 校验是否不符合正则格式
    private static boolean mismatch(String str, String regex){
        //判断null、“”、“ ”
        if (StrUtil.isBlank(str)) {
            return true;
        }
        //matches9()：通过正则表达式来校验整个字符串是否完全符合指定的格式规则。
        return !str.matches(regex);
    }


}

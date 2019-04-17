package com.caterpillar.mvp.processor.complier.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/29
 */
public class MessagerUtil {

    private static Messager mMessager;

    /**
     * 初始化
     *
     * @param messager 消息管理器
     */
    public static void init(Messager messager) {
        mMessager = messager;
    }

    /**
     * 输出错误信息，并终止apt编译
     *
     * @param s    格式化字符串
     * @param args 参数
     */
    public static void error(String s, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(s, args));
    }

    /**
     * 输出警告信息
     *
     * @param s    格式化字符串
     * @param args 参数
     */
    public static void warning(String s, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.WARNING, String.format(s, args));
    }

    /**
     * 提示信息
     *
     * @param s    格式化字符串
     * @param args 参数
     */
    public static void note(String s, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(s, args));
    }
}

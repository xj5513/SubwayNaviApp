package com.caterpillar.mvp.processor.complier.utils;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

/**
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/29
 */
public class AnnotationUtil {
    /**
     * 获取注解的值
     *
     * @param element    被注解修饰的元素
     * @param annotation 注解
     * @param key        要获取注解的值对应的键
     * @return 注解的值
     */
    public static String getAnnotationValue(Element element, Class<? extends Annotation> annotation, String key) {
        //参数不能为空
        if (element == null || annotation == null || key == null || "".equals(key)) {
            MessagerUtil.error("getAnnotationValue() 的参数不能为空！");
        }

        for (AnnotationMirror am : element.getAnnotationMirrors()) {

            if (am == null) {
                continue;
            }

            if (annotation.getName().equals(am.getAnnotationType().toString())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                    if (entry.getKey() == null) {
                        continue;
                    }

                    if (key.equals(entry.getKey().getSimpleName().toString())) {
                        return entry.getValue().getValue().toString();
                    }
                }
            }
        }
        return null;
    }
}

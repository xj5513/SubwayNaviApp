package com.caterpillar.mvp.annotation

import kotlin.reflect.KClass


/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/29
 *
 * view的注解
 *
 */
annotation class MvpView(val key: String, val presenter: KClass<*>)
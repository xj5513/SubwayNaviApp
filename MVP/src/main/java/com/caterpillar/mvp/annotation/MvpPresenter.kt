package com.caterpillar.mvp.annotation

import kotlin.reflect.KClass

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2018/12/29
 *
 * presenter的注解
 *
 */

annotation class MvpPresenter(val key: String, val view: KClass<*>)
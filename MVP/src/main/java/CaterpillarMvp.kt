import com.caterpillar.mvp.annotation.CaterpillarMvpPresenter
import com.caterpillar.mvp.annotation.CaterpillarMvpView
import com.caterpillar.mvp.annotation.MvpPresenter
import com.caterpillar.mvp.annotation.MvpView
import sun.plugin2.liveconnect.JavaClass
import java.lang.ref.WeakReference
import java.lang.reflect.InvocationTargetException
import java.util.HashMap

/**
 *
 * Author  xia jie
 * Mail    xj5513@163.com
 * created 2019/1/3
 *
 */

object CaterpillarMvp {

    private val sCache = HashMap<String, WeakReference<CaterpillarMvpPresenter<*,*>>>()

    fun createPresenter(view: Any): CaterpillarMvpPresenter<*,*> ?{
        val mvpViewAnnotation = view.javaClass.getAnnotation(MvpView::class.java) ?: throw RuntimeException("view 未被MvpView注解修饰！")

        val key = mvpViewAnnotation.key
        val presClz = mvpViewAnnotation.presenter

        try {
            val presObj = presClz.objectInstance

            val presProxyClz = Class.forName(view.javaClass.`package`.name + ".presenter." + key.toLowerCase() + "." + key + "PresenterProxy")
            val presProxy = presProxyClz.getConstructor(view.javaClass, presClz as Class<*>)
                    .newInstance(view, presObj) as CaterpillarMvpPresenter<*, *>

            sCache.put(key, WeakReference(presProxy))

            return presProxy
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }

    fun getViewProxy(presenter: Any): CaterpillarMvpView<*>? {
        val mvpPresenterAnnotation = presenter.javaClass.getAnnotation(MvpPresenter::class.java) ?: throw RuntimeException("presenter 未被MvpPresenter注解修饰！")

        val key = mvpPresenterAnnotation.key
        val wrPresenter = sCache[key]
        return if (wrPresenter?.get() != null) {
            wrPresenter.get()?.getViewProxy()
        } else null

    }
}

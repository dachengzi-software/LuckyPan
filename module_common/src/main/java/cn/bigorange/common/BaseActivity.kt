package cn.bigorange.common

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {


    protected val binding: VB by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*> //使用反射得到viewbinding的class
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(null, layoutInflater) as VB
    }

    protected val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        immersive()
        initView()
        initListener()
        initData()
    }


    override fun onStop() {
        super.onStop()
        mainHandler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacksAndMessages(null)
    }

    //加载布局
    protected open fun initView() {}

    //初始化监听事件
    protected open fun initListener() {}

    //初始化数据
    protected open fun initData() {}

    //设置沉浸式
    private fun immersive() {
//        statusBarOnly {
//            fitWindow = true
//            light = true
//            transparent()
//        }
    }


}
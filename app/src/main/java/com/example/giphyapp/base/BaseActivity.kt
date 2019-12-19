package com.example.giphyapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.koin.android.ext.android.getKoin
import java.lang.reflect.ParameterizedType
import kotlin.jvm.internal.Reflection

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    abstract val fragmentContainerId: Int
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        javaClass.genericSuperclass?.also {
            viewModel = ViewModelProvider(this.viewModelStore, object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val vmClass = Reflection.createKotlinClass(modelClass)
                    return getKoin().get(
                        vmClass, null, null
                    )
                }
            }).get((it as ParameterizedType).actualTypeArguments[0] as Class<VM>)
        }
        super.onCreate(savedInstanceState)
    }

    protected fun replaceFragment(fragment: BaseFragment<VM>, addToBackStack: Boolean = false) {
        supportFragmentManager.beginTransaction()
            .replace(fragmentContainerId, fragment, fragment::class.java.simpleName)
            .apply {
                if (addToBackStack) {
                    addToBackStack(null)
                }
            }
            .commit()
    }
}
package com.example.giphyapp.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.getKoin
import java.lang.reflect.ParameterizedType
import kotlin.jvm.internal.Reflection

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected lateinit var viewModel: VM
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        javaClass.genericSuperclass?.also { type ->
            activity?.viewModelStore?.also { vmStore ->
                viewModel = ViewModelProvider(vmStore, object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        val vmClass = Reflection.createKotlinClass(modelClass)
                        return getKoin().get(
                            vmClass, null, null
                        )
                    }
                })
                    .get((type as ParameterizedType).actualTypeArguments[0] as Class<VM>)
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }

    protected fun autodispose(block: () -> Disposable) {
        disposable.add(block())
    }

    protected fun <T> LiveData<T>.subscribe(block: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer {
            block(it)
        })
    }
}
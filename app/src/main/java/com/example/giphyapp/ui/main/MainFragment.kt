package com.example.giphyapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giphyapp.base.BaseFragment
import com.example.giphyapp.databinding.FragmentMainBinding
import com.example.giphyapp.ui.adapter.GiphAdapter
import com.example.giphyapp.util.Result
import com.example.giphyapp.viewmodel.MainViewModel
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.concurrent.TimeUnit

class MainFragment : BaseFragment<MainViewModel>() {

    companion object {

        fun newInstance() = MainFragment()

    }

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        autodispose {
            input_search_query.textChanges()
                .skipInitialValue()
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    button_clear_query.isVisible = it.isNotEmpty()
                    viewModel.load(it.toString().trim())
                }, {
                    Log.e("GIPHY", "error: $it")
                })
        }

        button_clear_query.setOnClickListener {
            input_search_query.setText("")
        }

        val adapter = GiphAdapter(viewModel.giphItems)
        recycler_giph.adapter = adapter
        recycler_giph.layoutManager = GridLayoutManager(context, 3)
        viewModel.itemReceivedData.subscribe {
            if (it.status == Result.Status.ERROR) {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            } else {
                adapter.notifyAdapter(it.data)
            }
        }

        viewModel.load()

        recycler_giph.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.nextPage()
                }
            }
        })
    }
}
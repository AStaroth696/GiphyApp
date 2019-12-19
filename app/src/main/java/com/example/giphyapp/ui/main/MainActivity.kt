package com.example.giphyapp.ui.main

import android.os.Bundle
import com.example.giphyapp.R
import com.example.giphyapp.base.BaseActivity
import com.example.giphyapp.viewmodel.MainViewModel

class MainActivity : BaseActivity<MainViewModel>() {

    override val fragmentContainerId = R.id.frame_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(MainFragment.newInstance())

    }

}

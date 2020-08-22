package com.hunger.kotlingallery.entity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.hunger.kotlingallery.VolleySingleTon

enum class NetWorkStatus {
    LOADING,
    FIALED,
    COMPLETED
}

class PixabayDataSource(private val context: Context) : PageKeyedDataSource<Int, PhotoItem>() {
    var retry: (() -> Any)? = null
    private val _netWorkStatus = MutableLiveData<NetWorkStatus>()
    var netWorkStatus: LiveData<NetWorkStatus> = _netWorkStatus

    private val keywords = arrayOf("cities", "technology", "earth", "sea", "flower", "sky")
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, PhotoItem>) {
        _netWorkStatus.postValue(NetWorkStatus.LOADING)
        var requestUrl = "https://pixabay.com/api/?key=16902026-685439c5da533184c424ae01c&q=${keywords.random()}&per_page=20&page=1"
        StringRequest(
            Request.Method.GET,
            requestUrl,
            Response.Listener {
                retry = null
                var datalist = Gson().fromJson(it, Pixabay::class.java).hits.toList()
                callback.onResult(datalist, null, 2)
            },
            Response.ErrorListener {
                retry = { loadInitial(params, callback) }
                _netWorkStatus.postValue(NetWorkStatus.FIALED)
            }
        ).also { VolleySingleTon.getInstance(context).requestQueue.add(it) }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {
        var requestUrl = "https://pixabay.com/api/?key=16902026-685439c5da533184c424ae01c&q=${keywords.random()}&per_page=20&page=${params.key}"
        StringRequest(
            Request.Method.GET,
            requestUrl,
            Response.Listener {
                retry = null
                var datalist = Gson().fromJson(it, Pixabay::class.java).hits.toList()
                callback.onResult(datalist, params.key + 1)
            },
            Response.ErrorListener {
                retry = { loadAfter(params, callback) }
            }
        ).also { VolleySingleTon.getInstance(context).requestQueue.add(it) }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {
        TODO("Not yet implemented")
    }

}
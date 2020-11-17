package com.example.projectnoodle

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson


class ContentDataSource(private val context: Context, private val viewModel: NoodleViewModel)
    : PageKeyedDataSource<Int, ContentItem>() {
    var retry : (()->Any)? = null
    private val _networkStatusLive = MutableLiveData<NetworkStatus>()
    val networkStatusLive: LiveData<NetworkStatus> = _networkStatusLive

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ContentItem>
    ) {
        _networkStatusLive.postValue(NetworkStatus.INITIAL_LOADING)
        with (viewModel) {
            retry = null
            val user = userLive.value
            val url = "${HTTP_QUERY_CONTENT_API_PREFIX}/find-by-range/?${getUserAndTokenString()}&start=${1}&end=${CONTENTS_PER_PAGE}"
            val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                {
                    val contentList = Gson().fromJson(it, ContentData::class.java).hits.toList()
                    callback.onResult(contentList, null, 1)
                    _networkStatusLive.postValue(NetworkStatus.LOADED)
                },
                {
                    retry = {loadInitial(params, callback)}
                    Log.d("noodle", "loadInitial: ${it}")
                    _networkStatusLive.postValue(NetworkStatus.NETWORK_ERROR)
                }
            )
            VolleySingleton.getInstance(context).requestQueue.add(stringRequest)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ContentItem>) {
        with (viewModel) {
            _networkStatusLive.postValue(NetworkStatus.LOADING)
            retry = null
            val user = userLive.value
            val offset = params.key * CONTENTS_PER_PAGE
            val url = "${HTTP_QUERY_CONTENT_API_PREFIX}/find-by-range/?${getUserAndTokenString()}&start=${offset+1}&end=${offset+CONTENTS_PER_PAGE}"
            val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                {
                    val contentList = Gson().fromJson(it, ContentData::class.java).hits.toList()
                    callback.onResult(contentList, params.key + 1)
                    _networkStatusLive.postValue(NetworkStatus.LOADED)
                },
                {
                    retry = {loadAfter(params, callback)}
                    Log.d("noodle", "loadAfter: ${it}")
                    _networkStatusLive.postValue(NetworkStatus.NETWORK_ERROR)
                }
            )
            VolleySingleton.getInstance(context).requestQueue.add(stringRequest)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ContentItem>) {
    }
}
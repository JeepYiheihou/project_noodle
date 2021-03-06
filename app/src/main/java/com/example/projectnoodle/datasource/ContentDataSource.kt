package com.example.projectnoodle.datasource

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.projectnoodle.*
import com.example.projectnoodle.customobject.VolleySingleton
import com.google.gson.Gson


class ContentDataSource(private val context: Context, private val noodleViewModel: NoodleViewModel)
    : PageKeyedDataSource<Int, ContentItem>() {
    var retry : (()->Any)? = null
    private val _networkStatusLive = MutableLiveData<NetworkStatus>()
    val networkStatusLive: LiveData<NetworkStatus> = _networkStatusLive

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ContentItem>
    ) {
        _networkStatusLive.postValue(NetworkStatus.INITIAL_LOADING)
        with (noodleViewModel) {
            retry = null
            val url = "$HTTP_QUERY_CONTENT_API_PREFIX/find-by-range/?${getUserAndTokenString()}&start=${1}&end=$CONTENTS_PER_PAGE"
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
                    _networkStatusLive.postValue(NetworkStatus.NETWORK_ERROR)
                }
            )
            VolleySingleton.getInstance(context).requestQueue.add(stringRequest)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ContentItem>) {
        with (noodleViewModel) {
            _networkStatusLive.postValue(NetworkStatus.LOADING)
            retry = null
            val user = userLive.value
            val offset = params.key * CONTENTS_PER_PAGE
            val url = "$HTTP_QUERY_CONTENT_API_PREFIX/find-by-range/?${getUserAndTokenString()}&start=${offset+1}&end=${offset+ CONTENTS_PER_PAGE}"
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
                    _networkStatusLive.postValue(NetworkStatus.NETWORK_ERROR)
                }
            )
            VolleySingleton.getInstance(context).requestQueue.add(stringRequest)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ContentItem>) {
    }
}
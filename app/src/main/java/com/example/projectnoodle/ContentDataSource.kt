package com.example.projectnoodle

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson


class ContentDataSource(private val context: Context, private val viewModel: NoodleViewModel)
    : PageKeyedDataSource<Int, ContentItem>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ContentItem>
    ) {
        with (viewModel) {
            val user = userLive.value
            val url = "${HTTP_QUERY_CONTENT_API_PREFIX}/find-by-range/?${getUserAndTokenString()}&start=${1}&end=${CONTENTS_PER_PAGE}"
            val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                {
                    val contentList = Gson().fromJson(it, ContentData::class.java).hits.toList()
                    updateDataStatus(DataStatus.CAN_LOAD_MORE)
                    callback.onResult(contentList, null, 1)
                },
                {
                    Log.d("noodle", "loadInitial: ${it}")
                }
            )
            VolleySingleton.getInstance(context).requestQueue.add(stringRequest)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ContentItem>) {
        with (viewModel) {
            val user = userLive.value
            val offset = params.key * CONTENTS_PER_PAGE
            val url = "${HTTP_QUERY_CONTENT_API_PREFIX}/find-by-range/?${getUserAndTokenString()}&start=${offset+1}&end=${offset+CONTENTS_PER_PAGE}"
            val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                {
                    val contentList = Gson().fromJson(it, ContentData::class.java).hits.toList()
                    updateDataStatus(DataStatus.CAN_LOAD_MORE)
                    callback.onResult(contentList, params.key + 1)
                },
                {
                    Log.d("noodle", "loadAfter: ${it}")
                }
            )
            VolleySingleton.getInstance(context).requestQueue.add(stringRequest)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ContentItem>) {
    }
}
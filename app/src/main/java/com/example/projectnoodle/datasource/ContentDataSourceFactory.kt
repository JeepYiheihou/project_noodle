package com.example.projectnoodle.datasource

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.projectnoodle.ContentItem
import com.example.projectnoodle.NoodleViewModel

class ContentDataSourceFactory(private val context: Context, private val viewModel: NoodleViewModel)
    : DataSource.Factory<Int, ContentItem>() {
    private var _contentDataSourceLive = MutableLiveData<ContentDataSource>()
    val contentDataSource: LiveData<ContentDataSource> get() = _contentDataSourceLive
    override fun create(): DataSource<Int, ContentItem> {
        return ContentDataSource(context, viewModel).also { _contentDataSourceLive.postValue(it) }
    }
}
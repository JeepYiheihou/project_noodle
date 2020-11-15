package com.example.projectnoodle

import android.content.Context
import androidx.paging.DataSource

class ContentDataSourceFactory(private val context: Context, private val viewModel: NoodleViewModel)
    : DataSource.Factory<Int, ContentItem>() {
    override fun create(): DataSource<Int, ContentItem> {
        return ContentDataSource(context, viewModel)
    }
}
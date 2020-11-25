package com.example.projectnoodle.customobject

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/*
 * The global singleton Volley message queue, to serialize and handle http request.
 * If INSTANCE is null, then create one. Otherwise just use the existing one.
 */
class VolleySingleton private constructor(context: Context) {
    companion object {
        private var INSTANCE : VolleySingleton?=null
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            VolleySingleton(context).also{ INSTANCE = it }
        }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}
package com.example.projectnoodle

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.toLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

enum class DataStatus(val value: Int) {
    CAN_LOAD_MORE(1),
    LOADING(2),
    NO_MORE(3),
    NETWORK_ERROR(4)
}

enum class HTTPQueryBehavior {
    resetContentList
}

/*
 * The view model collecting data to monitor and control the app.
 */
class NoodleViewModel(application: Application) : AndroidViewModel(application) {

    /* Log in status parameters. */
    var currentTypedUserName = ""
    var currentTypedPassword = ""
    private val _isLoggedInLive = MutableLiveData<Boolean>()
    val isLoggedInLive : LiveData<Boolean> get() = _isLoggedInLive

    private val _isTokenRetryNeeded = MutableLiveData<Boolean>(false)
    val isTokenRetryNeeded : LiveData<Boolean> get() = _isTokenRetryNeeded

    private val _userLive = MutableLiveData<User>()
    val userLive : LiveData<User> get() = _userLive

    /* To track the "load more" status of the gallery list page. */
    private val _dataStatusLive = MutableLiveData<DataStatus>()
    val dataStatusLive : LiveData<DataStatus> get() = _dataStatusLive

    fun updateDataStatus(status: DataStatus) {
        _dataStatusLive.value = status
    }

    fun updateLogStatus(isLoggedIn: Boolean) {
        _isLoggedInLive.value = isLoggedIn
    }

    val contentListLive = ContentDataSourceFactory(application, this).toLiveData(1)

    fun login() {
        if (_isTokenRetryNeeded.value == true) {
            Toast.makeText(getApplication(), "Another pending query going on", Toast.LENGTH_SHORT).show()
            return
        }
        val stringRequest = object : StringRequest(
                Request.Method.POST,
                "${HTTP_QUERY_USER_API_PREFIX}/login",
                {
                    with(Gson().fromJson(it, User::class.java)) {
                        _userLive.value = this
                    }
                    _isLoggedInLive.value = true
                },
                {
                    Log.e("volley!", it.toString())
                    Toast.makeText(getApplication(), "Error logging in", Toast.LENGTH_SHORT).show()
                }
        ) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val params = HashMap<String, String>()
                params["name"] = currentTypedUserName
                params["password"] = currentTypedPassword
                return JSONObject(params as Map<*, *>).toString().toByteArray()
            }
        }
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    fun retryToken() {
        val stringRequest = object : StringRequest(
                Request.Method.POST,
                "${HTTP_QUERY_USER_API_PREFIX}/login",
                {
                    with(Gson().fromJson(it, User::class.java)) {
                        _userLive.value = this
                    }
                    val v = _isTokenRetryNeeded.value
                    _isTokenRetryNeeded.value = false
                },
                {
                    Toast.makeText(getApplication(), "Error retrying logging in", Toast.LENGTH_SHORT).show()
                    _isLoggedInLive.value = false
                }
        ) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val params = HashMap<String, String>()
                params["name"] = currentTypedUserName
                params["password"] = currentTypedPassword
                return JSONObject(params as Map<*, *>).toString().toByteArray()
            }
        }
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    fun resetQuery() {
        contentListLive.value?.dataSource?.invalidate()
    }

    fun getUserAndTokenString(): String {
        val user = userLive.value
        return "id=${user?.id}&token=${user?.token?.token}"
    }

    fun generateFullThumbUrl(thumbUrl: String): String {
        return "${HTTP_QUERY_THUMB_API_PREFIX}/${thumbUrl}?${getUserAndTokenString()}"
    }
}
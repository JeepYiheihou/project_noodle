package com.example.projectnoodle

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.toLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.projectnoodle.customobject.VolleySingleton
import com.example.projectnoodle.datasource.ContentDataSourceFactory
import com.google.gson.Gson
import org.json.JSONObject

enum class NetworkStatus {
    INITIAL_LOADING,
    LOADING,
    LOADED,
    NO_MORE,
    NETWORK_ERROR
}

enum class TokenStatus {
    ACTIVE,
    EXPIRED,
    UPDATING,
    FAILED
}

/*
 * The view model collecting data to monitor and control the app.
 */
class NoodleViewModel(application: Application) : AndroidViewModel(application) {

    /* Log in status parameters. */
    var currentTypedUserName = ""
    var currentTypedPassword = ""

    /* New user data for sign up. */
    var signUpUser = User()

    private val _isLoggedInLive = MutableLiveData(false)
    val isLoggedInLive : LiveData<Boolean> get() = _isLoggedInLive
    fun updateLoginStatus(isLoggedIn: Boolean) {
        _isLoggedInLive.value = isLoggedIn
    }

    private val _tokenStatus = MutableLiveData<TokenStatus>()

    private val _userLive = MutableLiveData<User>()
    val userLive : LiveData<User> get() = _userLive

    private val _playContent = MutableLiveData<Boolean>()
    val playContent : LiveData<Boolean> get() = _playContent
    fun updatePlayContentStatus(isPlayingContent: Boolean) {
        _playContent.value = isPlayingContent
    }


    /* The content list for contents data retrieved from backend server.
     * We use paged list to simplify queries, so we have to initialize from factory. */
    private val factory = ContentDataSourceFactory(application, this)
    val contentListLive = factory.toLiveData(1)

    val networkStatus = Transformations.switchMap(factory.contentDataSource) { it.networkStatusLive }

    private fun parseVolleyError(error: VolleyError): String {
        val responseBody = String(error.networkResponse.data)
        val jsonObject = JSONObject(responseBody)
        return jsonObject.getString("message")
    }

    fun signUp() {
        val url = "$HTTP_QUERY_USER_API_PREFIX/create"
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            {
                Toast.makeText(getApplication(), "Sign up successful!", Toast.LENGTH_SHORT).show()
            },
            {
                Toast.makeText(getApplication(), "Error sign up: ${parseVolleyError(it)}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return Gson().toJson(signUpUser).toString().toByteArray()
            }
        }
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    fun login() {
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
                Toast.makeText(getApplication(), "Error logging in", Toast.LENGTH_SHORT).show()
                Log.e("noodle!", "login: $it")
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

    fun retry() {
        factory.contentDataSource.value?.retry?.invoke()
    }

    fun resetQuery() {
        contentListLive.value?.dataSource?.invalidate()
    }

    fun getUserAndTokenString(): String {
        val user = userLive.value
        return "id=${user?.id}&token=${user?.token?.token}"
    }

    fun generateFullVideoUrl(realUrl: String): String {
        return "${HTTP_QUERY_VIDEO_API_PREFIX}/${realUrl}?${getUserAndTokenString()}"
    }

    fun generateFullThumbUrl(thumbUrl: String): String {
        return "${HTTP_QUERY_THUMB_API_PREFIX}/${thumbUrl}?${getUserAndTokenString()}"
    }
}
package com.example.projectnoodle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest

enum class DataStatus(val value: Int) {
    CAN_LOAD_MORE(1),
    NO_MORE(2),
    NETWORK_ERROR(3)
}

/*
 * The view model collecting data to monitor and control the app.
 */
class NoodleViewModel : ViewModel() {

    /* Log in status parameters. */
    var currentTypedUserName = ""
    var currentTypedPassword = ""
    private val _isLoggedInLive = MutableLiveData<Boolean>()
    val isLoggedInLive : LiveData<Boolean> get() = _isLoggedInLive

    /* User's data. If it's null, then pop to login page. */
    private val _userLive = MutableLiveData<User>()
    val userLive : LiveData<User> get() = _userLive

    /* To track the "load more" status of the gallery list page. */
    private val _dataStatusLive = MutableLiveData<DataStatus>()
    val dataStatusLive : LiveData<DataStatus> get() = _dataStatusLive

    /* To track the content list of the gallery list page. */
    private val _contentListLive = MutableLiveData<List<ContentItem>>()
    val contentListLive : LiveData<List<ContentItem>> get() = _contentListLive

    /* Each time after refresh, Android tries to automatically scroll to bottom.
     * This is not what we want so after refresh, use the flag to mark it and scroll back to top. */
    var needToScrollToTop = true

    /* How many contents we want to load from backend server per page. */
    private val contentsPerPage = 50

    /* Current latest loaded item number in gallery list*/
    private val contentCount = 0

    fun updateLogStatus(isLoggedIn: Boolean) {
        _isLoggedInLive.value = isLoggedIn
    }

    fun fetchData() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            getBatchFetchUrl(),
            {
                _dataStatusLive.value = DataStatus.NETWORK_ERROR
            },
            {
                _dataStatusLive.value = DataStatus.NETWORK_ERROR
            }
        )
    }

    private fun getBatchFetchUrl(): String {
        val user = userLive.value
        return "${HTTP_QUERY_CONTENT_API_PREFIX}/find-by-range/?id=${user?.id}&token=${user?.token?.token}&start=${contentCount+1}&end=${contentCount+contentsPerPage+1}"
    }
}
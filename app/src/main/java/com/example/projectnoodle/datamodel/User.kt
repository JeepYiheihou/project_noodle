package com.example.projectnoodle

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize data class User(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("createdTime") var createdTime: String? = null,
    @SerializedName("role") var role: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("token") var token: Token? = null
): Parcelable

@Parcelize data class  Token(
    @SerializedName("token") var token: String,
    @SerializedName("ttl") var ttl: String
): Parcelable
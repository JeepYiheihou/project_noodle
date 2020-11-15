package com.example.projectnoodle

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("createdTime") val createdTime: String,
    @SerializedName("role") val role: String,
    @SerializedName("status") val status: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("token") var token: Token
): Parcelable

@Parcelize data class  Token(
    @SerializedName("token") val token: String
): Parcelable
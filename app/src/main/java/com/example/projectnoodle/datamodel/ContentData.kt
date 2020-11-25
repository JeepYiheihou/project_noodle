package com.example.projectnoodle

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/*
 * The data structure parsed from a get content query response.
 */
data class ContentData (
    val totalHits:Int,
    val hits:Array<ContentItem>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContentData

        if (totalHits != other.totalHits) return false
        if (!hits.contentEquals(other.hits)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = totalHits
        result = 31 * result + hits.contentHashCode()
        return result
    }
}

@Parcelize
data class ContentItem (
    @SerializedName("id") val id: Int,
    @SerializedName("creater") val creater: String,
    @SerializedName("createdTime") val createdTime: String,
    @SerializedName("type") val type: String,
    @SerializedName("thumbUrl") val thumbUrl: String,
    @SerializedName("realUrl") val realUrl: String,
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("length") val length: Int
): Parcelable
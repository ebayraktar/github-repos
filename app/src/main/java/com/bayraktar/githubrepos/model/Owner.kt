package com.bayraktar.githubrepos.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Owner(

    @SerializedName("id")
    @Expose val id: Long?,

    @SerializedName("login")
    @Expose val login: String?,

    @SerializedName("node_id")
    @Expose val nodeId: String?,

    @SerializedName("avatar_url")
    @Expose val avatarUrl: String?,
) : Parcelable

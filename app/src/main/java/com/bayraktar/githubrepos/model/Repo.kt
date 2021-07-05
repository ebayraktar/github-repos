package com.bayraktar.githubrepos.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repo(
    @SerializedName("id")
    @Expose val id: Long?,

    @SerializedName("node_id")
    @Expose val nodeId: String?,

    @SerializedName("name")
    @Expose val name: String?,

    @SerializedName("description")
    @Expose val description: String?,

    @SerializedName("created_at")
    @Expose val createdAt: String?,

    @SerializedName("updated_at")
    @Expose val updatedAt: String?,

    @SerializedName("stargazers_count")
    @Expose val stargazersCount: Int?,

    @SerializedName("open_issues_count")
    @Expose val openIssuesCount: Int?,

    @SerializedName("owner")
    @Expose val owner: Owner?,
) : Parcelable

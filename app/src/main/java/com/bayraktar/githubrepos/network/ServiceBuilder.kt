package com.bayraktar.githubrepos.network

import com.bayraktar.githubrepos.BASE_URL
import com.bayraktar.githubrepos.model.Repo
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object ServiceBuilder {
    private val client = OkHttpClient
        .Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(GitHubApi::class.java)

    fun buildService(): GitHubApi {
        return retrofit
    }
}


class GitHubRepository(private val userApi: GitHubApi) {

    var cachedRepos = emptyList<Repo>()

    fun getRepos(username: String): Observable<List<Repo>> {
        return if (cachedRepos.isEmpty()) {
            //Returning users from API
            userApi.getRepos(username)
                .doOnNext { cachedRepos = it }
        } else {
            //Returning cached users first, and then API users
            Observable.just(cachedRepos)
                .mergeWith(userApi.getRepos(username))
                .doOnNext { cachedRepos = it }
        }

    }
}

interface GitHubApi {
    @GET("users/{user}/repos")
    fun getRepos(@Path("user") user: String): Observable<List<Repo>>
}
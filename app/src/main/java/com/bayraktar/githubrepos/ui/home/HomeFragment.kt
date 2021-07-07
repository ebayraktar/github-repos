package com.bayraktar.githubrepos.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bayraktar.githubrepos.ARG_REPO_ITEM
import com.bayraktar.githubrepos.ARG_TITLE
import com.bayraktar.githubrepos.App
import com.bayraktar.githubrepos.R
import com.bayraktar.githubrepos.adapter.IRepoListener
import com.bayraktar.githubrepos.adapter.RepositoryAdapter
import com.bayraktar.githubrepos.model.Repo
import com.bayraktar.githubrepos.room.Favorite
import com.bayraktar.githubrepos.utils.UIController
import com.bayraktar.githubrepos.utils.common.gone
import com.bayraktar.githubrepos.utils.common.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var repositoryAdapter: RepositoryAdapter
    private var uiController: UIController? = null
    private var username: String = ""

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (activity?.application as App).repository,
            (activity?.application as App).gitHubRepository
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom)
        repositoryAdapter = RepositoryAdapter(animation)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvRepos.layoutManager = LinearLayoutManager(context)
        rvRepos.adapter = repositoryAdapter

        homeViewModel.allFavorites.observe(viewLifecycleOwner, {

        })

        initEvents()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UIController)
            uiController = context
    }

    private fun initEvents() {
        repositoryAdapter.setListener(object : IRepoListener {
            override fun onClick(index: Int) {
                activity?.let {
                    val bundle = Bundle()
                    bundle.putString(
                        ARG_TITLE,
                        repositoryAdapter.getList()[index].name ?: "Unknown Repo"
                    )
                    bundle.putParcelable(ARG_REPO_ITEM, repositoryAdapter.getList()[index])
                    Navigation.findNavController(activity!!, R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_home_to_nav_repo_detail, bundle)
                }
            }

            override fun onFavClick(index: Int) {
                val repo = repositoryAdapter.getList()[index]
                val fav = Favorite(
                    title = repo.name ?: "",
                    id = repo.id ?: 0L,
                    username = repo.owner?.login ?: ""
                )
                val isFav: Boolean
                val list = homeViewModel.allFavorites.value

                if (list == null) {
                    isFav = true
                    homeViewModel.insert(fav)
                } else {
                    var contains = false
                    for (i in list.indices) {
                        val favorite = list[i]
                        if (repo.id == favorite.id) {
                            contains = true
                            break
                        }
                    }

                    if (contains)
                        homeViewModel.delete(fav)
                    else
                        homeViewModel.insert(fav)
                    isFav = !contains
                }
                repositoryAdapter.setFav(index, isFav)
            }
        })

        etSearchUser.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                uiController?.hideSoftKeyboard()
                username = etSearchUser.text.toString()
                if (username.isEmpty())
                    return@setOnEditorActionListener false

                discover(username)
                return@setOnEditorActionListener true
            }
            false
        }
    }


    private fun discover(username: String) {
        emptyState(true)
        compositeDisposable.add(
            homeViewModel.getRepos(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { response -> onResponse(response) },
                    { t -> onFailure(t) })
        )
    }

    private fun onFailure(t: Throwable) {
        emptyState(false)
        uiController?.showSnacbkarMessage("Error: $t")
    }

    private fun onResponse(response: List<Repo>) {
        emptyState(false)
        prepareFavs(response)
        repositoryAdapter.setItems(response)
        if (response.isEmpty())
            uiController?.showToastMessage("Kullanıcı bulunamadı!")
    }

    private fun prepareFavs(repos: List<Repo>) {
        val list = homeViewModel.allFavorites.value ?: return

        if (list.size > repos.size) {
            repos.forEach { repo ->
                if (list.find { favorite -> favorite.id == repo.id } != null)
                    repo.isFav = true
            }
        } else
            list.forEach { fav -> repos.find { repo -> fav.id == repo.id }?.isFav = true }

    }

    private fun emptyState(isVisible: Boolean) {
        when (isVisible) {
            true -> {
                shimmerRepoList.visible()
                rvRepos.gone()
            }
            false -> {
                shimmerRepoList.gone()
                rvRepos.visible()
            }
        }
    }
}
package com.bayraktar.githubrepos.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bayraktar.githubrepos.ARG_REPO_ITEM
import com.bayraktar.githubrepos.ARG_TITLE
import com.bayraktar.githubrepos.R
import com.bayraktar.githubrepos.adapter.IRepoListener
import com.bayraktar.githubrepos.adapter.RepositoryAdapter
import com.bayraktar.githubrepos.model.Repo
import com.bayraktar.githubrepos.network.ServiceBuilder
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
    private lateinit var homeViewModel: HomeViewModel
    private var repos: List<Repo?> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom)
        repositoryAdapter = RepositoryAdapter(animation)

        homeViewModel.text.observe(viewLifecycleOwner, {

        })

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvRepos.layoutManager = LinearLayoutManager(context)
        rvRepos.adapter = repositoryAdapter

        repositoryAdapter.setListener(object : IRepoListener {
            override fun onClick(index: Int) {
                activity?.let {
                    val bundle = Bundle()
                    bundle.putString(ARG_TITLE, repos[index]?.name ?: "Unknown Repo")
                    bundle.putParcelable(ARG_REPO_ITEM, repos[index])
                    Navigation.findNavController(activity!!, R.id.nav_host_fragment)
                        .navigate(R.id.action_nav_home_to_nav_repo_detail, bundle)
                }
            }
        })

        etSearchUser.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(context is UIController){
                    (context as UIController).hideSoftKeyboard()
                }
                discover(etSearchUser.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun discover(userName: String) {
        emptyState(true)
        compositeDisposable.add(
            ServiceBuilder.buildService().listRepos(userName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { response -> onResponse(response) },
                    { t -> onFailure(t) })
        )
    }

    private fun onFailure(t: Throwable) {
        emptyState(false)
        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
    }

    private fun onResponse(response: List<Repo?>) {
        emptyState(false)
        repos = response
        repositoryAdapter.setItems(response.map { it!! })

        Log.d("TAG", "onResponse: ${response[0]?.name}")
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
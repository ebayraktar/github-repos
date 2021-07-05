package com.bayraktar.githubrepos.ui.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bayraktar.githubrepos.ARG_REPO_ITEM
import com.bayraktar.githubrepos.R
import com.bayraktar.githubrepos.model.Repo
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_repo_detail.*

class RepoDetailFragment : Fragment() {

    private var repo: Repo? = null
    private lateinit var viewModel: RepoDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repo = it.getParcelable(ARG_REPO_ITEM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RepoDetailViewModel::class.java)

        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (repo == null)
            return

        Glide.with(ivUserAvatar)
            .load(repo?.owner?.avatarUrl)
            .fitCenter()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_broken_image_24)
            .into(ivUserAvatar)

        tvOwnerName.text = repo?.owner?.login ?: "Unknown Owner"
        tvStarCount.text = "Stars: ${repo?.stargazersCount}"
        tvIssueCount.text = "Open Issues: ${repo?.openIssuesCount}"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_repo_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
package com.bayraktar.githubrepos.ui.detail

import android.os.Bundle
import android.view.*
import androidx.annotation.DrawableRes
import androidx.fragment.app.viewModels
import com.bayraktar.githubrepos.ARG_REPO_ITEM
import com.bayraktar.githubrepos.App
import com.bayraktar.githubrepos.R
import com.bayraktar.githubrepos.model.Repo
import com.bayraktar.githubrepos.room.Favorite
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_repo_detail.*

class RepoDetailFragment : BottomSheetDialogFragment() {

    private var repo: Repo? = null
    private val viewModel: RepoDetailViewModel by viewModels {
        RepoDetailViewModelFactory(
            (activity?.application as App).repository
        )
    }

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
        setHasOptionsMenu(true)
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
        @DrawableRes
        val drawableId = when (repo?.isFav == true) {
            true -> R.drawable.ic_star_24
            else -> R.drawable.ic_unstar_24
        }
        menu.findItem(R.id.menu_star).setIcon(drawableId)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_star) {
            val fav = Favorite(
                title = repo?.name ?: "",
                username = repo?.owner?.login ?: "",
                id = repo?.id ?: 0L
            )
            if (repo?.isFav == true) {
                repo?.isFav = false
                viewModel.delete(fav)
            }
            else{
                repo?.isFav = true
                viewModel.insert(fav)
            }
            requireActivity().invalidateOptionsMenu()
        }
        return super.onOptionsItemSelected(item)
    }
}
package com.bayraktar.githubrepos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.bayraktar.githubrepos.R
import com.bayraktar.githubrepos.model.Repo
import kotlinx.android.synthetic.main.item_repo.view.*

class RepositoryAdapter(private val animation: Animation) :
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {
    private var mRepos: MutableList<Repo> = ArrayList()
    private var listener: IRepoListener? = null

    fun getList(): List<Repo> = mRepos.toList()

    fun setFav(index: Int, boolean: Boolean )
    {
        mRepos[index].isFav = boolean
        notifyItemChanged(index)
    }

    fun setListener(listener: IRepoListener) {
        this.listener = listener
    }

    fun setItems(repos: List<Repo>) {
        mRepos = repos.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false),
            animation,
            listener
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mRepos[position])
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mRepos.size

    class ViewHolder(
        private val view: View,
        private val animation: Animation,
        private val listener: IRepoListener?
    ) :
        RecyclerView.ViewHolder(view) {
        fun bind(item: Repo) {
            view.setOnClickListener { listener?.onClick(adapterPosition) }
//            view.startAnimation(animation)
            view.ivFav.setOnClickListener { listener?.onFavClick(adapterPosition) }
            view.tvRepoName.text = item.name ?: "UNKNOWN NAME"

            @DrawableRes
            val resId = when (item.isFav) {
                true -> R.drawable.ic_star_24
                false -> R.drawable.ic_unstar_24
            }

            view.ivFav.setImageResource(resId)
        }
    }
}

interface IRepoListener {
    fun onClick(index: Int)
    fun onFavClick(index: Int)
}
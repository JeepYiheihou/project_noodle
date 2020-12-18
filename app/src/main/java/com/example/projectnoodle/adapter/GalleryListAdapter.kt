package com.example.projectnoodle

import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.gallery_cell.view.*
import kotlinx.android.synthetic.main.gallery_list_footer.view.*


class GalleryListAdapter(
    private val noodleViewModel: NoodleViewModel,
):
    PagedListAdapter<ContentItem, RecyclerView.ViewHolder>(DIFFCALLBACK) {
    /* We don't want to display footer at initial load. For two reasons:
    * 1. We already have the circling indicator from the swipe layout;
    * 2. Having footer at initial load results in automatic scrolling to bottom. */
    private var hasFooter = false
    private var networkStatus : NetworkStatus? = null

    init {
        noodleViewModel.retry()
    }

    fun updateNetworkStatus(networkStatus: NetworkStatus?) {
        this.networkStatus = networkStatus
        if (networkStatus == NetworkStatus.INITIAL_LOADING) {
            hideFooter()
        } else {
            showFooter()
        }
    }

    private fun hideFooter() {
        if (hasFooter) {
            notifyItemRemoved(itemCount - 1)
        }
        hasFooter = false
    }

    private fun showFooter() {
        if (hasFooter) {
            notifyItemChanged(itemCount - 1)
        } else {
            hasFooter = true
            notifyItemInserted(itemCount - 1)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasFooter && position == itemCount - 1) R.layout.gallery_list_footer else R.layout.gallery_cell
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.gallery_cell -> ContentViewHolder.newInstance(parent, noodleViewModel)
                .also { holder ->
                    holder.itemView.setOnClickListener {
                        holder.realUrl?.apply {
                            noodleViewModel.updatePlayContentStatus(true)
                            val authString = noodleViewModel.getUserAndTokenString()
                            val url = "${HTTP_QUERY_VIDEO_API_PREFIX}/${this}?${authString}"
                        }
                    }
                }
            else -> FooterViewHolder.newInstance(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.gallery_list_footer -> (holder as FooterViewHolder).bindWithNetworkStatus(
                networkStatus
            )
            else -> {
                val contentItem = getItem(position) ?: return
                (holder as ContentViewHolder).bindWithContentItem(contentItem)
            }
        }
    }

    object DIFFCALLBACK : DiffUtil.ItemCallback<ContentItem>() {
        override fun areItemsTheSame(oldItem: ContentItem, newItem: ContentItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ContentItem, newItem: ContentItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

class ContentViewHolder(itemView: View, private val viewModel: NoodleViewModel)
    : RecyclerView.ViewHolder(itemView) {
    var realUrl : String ?= null

    companion object {
        fun newInstance(parent: ViewGroup, viewModel: NoodleViewModel): ContentViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.gallery_cell,
                parent,
                false
            )
            return ContentViewHolder(view, viewModel)
        }
    }

    private fun adjustCellHeight(contentItem: ContentItem, view: View): Int {
        val displayMetrics = view.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        return (screenWidth * contentItem.height / contentItem.width / 2)
    }

    fun bindWithContentItem(contentItem: ContentItem) {
        realUrl = contentItem.realUrl
        with(itemView) {
            galleryShimmerCell.apply {
                setShimmerColor(0x55FFFFFF)
                setShimmerAngle(0)
                startShimmerAnimation()
            }
            this.layoutParams.height = adjustCellHeight(contentItem, this)
        }

        Glide.with(itemView)
            .load(contentItem.thumbUrl.let { viewModel.generateFullThumbUrl(it) })
            .placeholder(R.drawable.content_thumb_placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also { itemView.galleryShimmerCell?.stopShimmerAnimation() }
                }
            }).into(itemView.galleryCellThumbImageView)
    }
}

class FooterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        fun newInstance(parent: ViewGroup): FooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.gallery_list_footer,
                parent,
                false
            )
            (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
            return FooterViewHolder(view)
        }
    }

    fun bindWithNetworkStatus(networkStatus: NetworkStatus?) {
        with(itemView) {
            when (networkStatus) {
                NetworkStatus.NETWORK_ERROR -> {
                    galleryListFooterText.text = "Click to retry loading"
                    galleryListFooterProgressBar.visibility = View.GONE
                    isClickable = true
                }
                NetworkStatus.NO_MORE -> {
                    galleryListFooterText.text = "All contents loaded"
                    galleryListFooterProgressBar.visibility = View.GONE
                    isClickable = false
                }
                else -> {
                    galleryListFooterText.text = "Loading"
                    galleryListFooterProgressBar.visibility = View.VISIBLE
                    isClickable = false
                }
            }
        }
    }
}
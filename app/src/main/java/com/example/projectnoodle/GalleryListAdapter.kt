package com.example.projectnoodle

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.gallery_cell.view.*

enum class PagerItemType(val value: Int) {
    NORMAL_VIEW(1),
    FOOTER_VIEW(2)
}

class GalleryListAdapter(val noodleViewModel: NoodleViewModel): PagedListAdapter<ContentItem, MyViewHolder>(DIFFCALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val holder: MyViewHolder
        if (viewType == PagerItemType.NORMAL_VIEW.value) {
            holder = MyViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell, parent, false)
            )
            holder.itemView.setOnClickListener{
                // TODO: populate the on click listener
            }

        } else {
            holder = MyViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell, parent, false)
            )
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contentItem = getItem(position)
        with (holder.itemView) {
            galleryShimmerCell.apply {
                setShimmerColor(0x55FFFFFF)
                setShimmerAngle(0)
                startShimmerAnimation()
            }
            //galleryCellThumbImageView.layoutParams.height = contentItem.height
        }

        Glide.with(holder.itemView)
                .load(getItem(position)?.thumbUrl?.let { noodleViewModel.generateFullThumbUrl(it) })
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
                        return false.also { holder.itemView.galleryShimmerCell?.stopShimmerAnimation() }
                    }

                })
                .into(holder.itemView.galleryCellThumbImageView)
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

class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
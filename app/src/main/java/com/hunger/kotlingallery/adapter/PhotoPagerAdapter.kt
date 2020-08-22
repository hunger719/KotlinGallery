package com.hunger.kotlingallery.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hunger.kotlingallery.R
import com.hunger.kotlingallery.entity.PhotoItem
import kotlinx.android.synthetic.main.gallery_item.view.*
import kotlinx.android.synthetic.main.pager_photo_item.view.*

class PhotoPagerAdapter : androidx.recyclerview.widget.ListAdapter<PhotoItem, PhotoPagerViewHolder>(PhotoItemDIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoPagerViewHolder {
        LayoutInflater.from(parent.context)
            .inflate(R.layout.pager_photo_item, parent, false)
            .apply { return PhotoPagerViewHolder(this) }
    }

    override fun onBindViewHolder(holder: PhotoPagerViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(getItem(position).previewUrl)
            .placeholder(R.drawable.place_icon)
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
                    return false.also { holder.itemView.shimmerlayoutitem?.stopShimmerAnimation() }
                }
            })
            .into(holder.itemView.pager_photo)
    }
}

class PhotoPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
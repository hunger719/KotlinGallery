package com.hunger.kotlingallery.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hunger.kotlingallery.R
import com.hunger.kotlingallery.entity.PhotoItem
import kotlinx.android.synthetic.main.gallery_item.view.*
import java.util.*

class GalleryAdapter : PagedListAdapter<PhotoItem, MyViewHolder>(PhotoItemDIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var holder = MyViewHolder(inflater.inflate(R.layout.gallery_item, parent, false))
        holder.itemView.setOnClickListener {
            Bundle().apply {
                putParcelableArrayList("PHOTO_LIST",ArrayList(currentList))
                putInt("PHOTO_INDEX", holder.adapterPosition)
                holder.itemView.findNavController().navigate(R.id.action_gallery_to_photo, this)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.shimmerlayoutitem.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        Glide.with(holder.itemView)
            .load(getItem(position)?.previewUrl)
            .placeholder(R.drawable.place_icon)
            .override(getItem(position)!!.width, getItem(position)!!.height)
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
            .into(holder.itemView.imageView)
        holder.itemView.gallery_item_author.text = getItem(position)?.user
        holder.itemView.gallery_item_tv_like.text = getItem(position)?.likes.toString()
        holder.itemView.gallery_item_tv_well.text = getItem(position)?.favorites.toString()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

object PhotoItemDIFF : DiffUtil.ItemCallback<PhotoItem>() {
    override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem?.photoId == newItem?.photoId
    }

    override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem == newItem
    }
}
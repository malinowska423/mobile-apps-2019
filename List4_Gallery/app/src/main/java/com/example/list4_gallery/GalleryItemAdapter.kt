package com.example.list4_gallery

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.gallery_item.view.*

class GalleryItemAdapter(private val photos : ArrayList<Photo>, private val fragment: GalleryViewerFragment)
    : RecyclerView.Adapter<ViewHolder>() {
    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(fragment.context).inflate(R.layout.gallery_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(photos[position].thumbPath).into(holder.galleryItem)
        holder.galleryItem.setOnClickListener { fragment.showImage(position) }
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val galleryItem:ImageView = view.galleryItem
}
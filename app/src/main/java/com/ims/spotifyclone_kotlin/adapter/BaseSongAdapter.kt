package com.ims.spotifyclone_kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ims.spotifyclone_kotlin.R
import com.ims.spotifyclone_kotlin.data.entities.Song
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

abstract class BaseSongAdapter@Inject constructor(
    private val layoutId: Int
) : RecyclerView.Adapter<BaseSongAdapter.SongViewHolder>() {


    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    protected val diffCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract val differ: AsyncListDiffer<Song>

    var songs: List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }



//
//    override fun onBindViewHolder(holder: BaseSongViewHolder, position: Int) {
//        val song = songs[position]
//        holder.itemView.apply {
//            tvPrimary.text = song.title
//            tvSecondary.text = song.subtitle
//            glide.load(song.imageUrl).into(ivItemImage)
//
//            setOnClickListener {
//                onItemClickListener?.let { click ->
//                    click(song)
//                }
//            }
//        }
//    }

    protected var onItemClickListener: ((Song) -> Unit)? = null

    fun setItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}
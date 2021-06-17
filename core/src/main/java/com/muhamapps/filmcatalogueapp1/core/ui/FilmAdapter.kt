package com.muhamapps.filmcatalogueapp1.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muhamapps.filmcatalogueapp1.core.R
import com.muhamapps.filmcatalogueapp1.core.databinding.ItemListFilmBinding
import com.muhamapps.filmcatalogueapp1.core.domain.model.Film
import com.muhamapps.filmcatalogueapp1.core.utils.NetworkInfo.IMAGE_URL

class FilmAdapter(private val callback: FilmShareCallback) : RecyclerView.Adapter<FilmAdapter.ListViewHolder>() {

    private var listData = ArrayList<Film>()
    var onItemClick: ((Film) -> Unit)? = null

    fun setData(newListData: List<Film>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_film, parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListFilmBinding.bind(itemView)
        fun bind(data: Film) {
            with(binding) {
                imgShare.setOnClickListener { callback.onShareClick(data) }
                Glide.with(itemView.context)
                    .load(IMAGE_URL + data.poster)
                    .into(ivItemImage)
                tvItemTitle.text = data.title
                tvItemSubtitle.text = data.rating
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}
package com.example.homework3.adapter

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.homework3.R
import com.example.homework3.data.Item
import com.example.homework3.databinding.FirstNewsItemBinding
import com.example.homework3.databinding.NewsItemBinding

class NewsAdapter(rssDataList: List<Item>, val context: Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){
    private val logTag = "NewsListAdapter"


    private var displayImage: Boolean = true
    var rssList: List<Item> = emptyList()
    var onItemClickListener: ((Item) -> Unit)? = null

    fun setDisplayImage(displayImage: Boolean){
        this.displayImage = displayImage
        notifyDataSetChanged()
    }

    fun setRssList(rssList: List<Item>){
        this.rssList = rssList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = when(viewType){
            R.layout.first_news_item -> FirstNewsItemBinding.inflate(inflater, parent, false)
            else -> NewsItemBinding.inflate(inflater, parent, false)
        }
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = rssList[position]
        holder.bindToNews(item)
        holder.itemView.setOnClickListener{ onItemClickListener?.invoke(item) }
        holder.setImageViewVisibility(displayImage)

    }

    override fun getItemCount(): Int{
        return rssList.size
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0){
            return R.layout.first_news_item
        }
        return R.layout.news_item
    }



    class NewsViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindToNews(item: Item){
            when(binding){
                is NewsItemBinding -> {
                    binding.title.text = item.title
                    binding.author.text = item.creator
                    binding.date.text = item.pubDate.toString()
                }
                is FirstNewsItemBinding -> {
                    binding.title.text = item.title
                }
            }


        }

        fun setImageViewVisibility(displayImage: Boolean) {
            val imageView = itemView.findViewById<ImageView>(R.id.imageView2)
            val imageView2 = itemView.findViewById<ImageView>(R.id.imageView)

            if (displayImage) {
                imageView?.visibility = View.VISIBLE
                imageView2?.visibility = View.VISIBLE
            } else {
                imageView?.visibility = View.GONE
                imageView2?.visibility = View.GONE
            }
        }
        /**
        var title: String?,
        var link: String?,
        var description: String?,
        var pubDate: Date?,
        var creator: String?,
        var source_id: String?,
        var categories: MutableList<String?>,
        var mediaContent: MutableList<String?>
        **/

    }
}
package com.example.homework3.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.homework3.R
import com.example.homework3.data.Item
import com.example.homework3.databinding.FirstNewsItemBinding
import com.example.homework3.databinding.NewsItemBinding

class NewsAdapter(rssDataList: List<Item>, val context: Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){
    private val logTag = "NewsListAdapter"

    var onItemClickListener: ((Item) -> Unit)? = null

    private var displayImage: Boolean = true

    fun setDisplayImage(displayImage: Boolean){
        this.displayImage = displayImage
        notifyDataSetChanged()
    }

    var rssList = rssDataList
    set(value){
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        Log.e(logTag, "ON BIND VIEWHOLDER $position")
        val item = rssList[position]
        holder.bindToNews(item)
        holder.itemView.setOnClickListener{ onItemClickListener?.invoke(item)}

        if(displayImage){
            val imageView = holder.itemView.findViewById<ImageView>(R.id.imageView2)
            val imageView2 = holder.itemView.findViewById<ImageView>(R.id.imageView)
            imageView?.visibility = View.VISIBLE
            imageView2?.visibility = View.VISIBLE

        } else {
            val imageView = holder.itemView.findViewById<ImageView>(R.id.imageView2)
            val imageView2 = holder.itemView.findViewById<ImageView>(R.id.imageView)

            imageView?.visibility = View.GONE
            imageView2?.visibility = View.GONE

        }
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

        companion object {
            fun create(parent: ViewGroup, viewType: Int): NewsViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = when(viewType){
                    R.layout.first_news_item -> FirstNewsItemBinding.inflate(inflater, parent, false)
                    else -> NewsItemBinding.inflate(inflater, parent, false)
                }
                return NewsViewHolder(binding)
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
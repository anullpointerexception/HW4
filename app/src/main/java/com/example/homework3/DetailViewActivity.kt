package com.example.homework3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceManager
import com.example.homework3.adapter.NewsAdapter
import com.example.homework3.data.Item
import com.example.homework3.databinding.ActivityDetailViewBinding
import java.util.*

class DetailViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val RSSInfo = intent.getSerializableExtra("RSSInfo") as Item

        binding.title.text = RSSInfo.title
        /**
        binding.link.text = RSSInfo.link
        binding.description.text = RSSInfo.description
        binding.pubDate.text = RSSInfo.pubDate.toString()
        binding.creator.text = RSSInfo.creator
        binding.sourceId.text = RSSInfo.source_id
        binding.categories.text = RSSInfo.categories.toString()
        // Image
        binding.mediacontent.text = RSSInfo.mediaContent.toString()**/

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val displayImage = sharedPreferences.getBoolean("displayimage", true)

        if(!displayImage){
            binding.image.visibility = View.GONE
        }
    }
}
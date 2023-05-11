package com.example.homework3

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
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

        val htmlContent = RSSInfo.description

        val convertedHTML = if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
            Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(htmlContent)
        }

        binding.title.text = RSSInfo.title
        binding.keywords.text = RSSInfo.categories.toString()
        binding.multilineText.text = convertedHTML
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
            binding.title.setTextColor(Color.BLACK)
        }
    }
}
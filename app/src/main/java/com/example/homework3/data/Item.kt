package com.example.homework3.data

import java.util.Date

data class Item(
    var title: String?,
    var link: String?,
    var description: String?,
    var pubDate: Date?,
    var creator: String?,
    var source_id: String?,
    var categories: MutableList<String?>,
    var mediaContent: MutableList<String?>
) : java.io.Serializable



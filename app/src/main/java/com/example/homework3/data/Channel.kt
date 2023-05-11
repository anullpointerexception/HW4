package com.example.homework3.data



data class Channel(
    val title: String,
    val link: String,
    val description: String,
    val language: String,
    val copyright: String,
    val pubDate: String,
    val generator: String,
    val items: List<Item>
)

package com.example.homework3.data

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RSSParser {
    private val ns: String? = null

    @Throws(IOException::class, XmlPullParserException::class)
    fun parseResult(inputString: String): List<Item>? {
        val inputStream = ByteArrayInputStream(inputString.toByteArray())

        inputStream.use { stream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(stream, null)
            parser.nextTag()
            Log.e("parser after one nextTag", "${parser.name}")
            parser.nextTag()
            Log.e("parser after two nextTag", "${parser.name}") // is on channel
            return readChannel(parser)
        }
        inputStream.close()
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readChannel(parser: XmlPullParser): List<Item> {
        val channels = mutableListOf<Item>() //(Documentation uses val entries

        parser.require(XmlPullParser.START_TAG, ns, "channel")
        while (parser.next() != XmlPullParser.END_TAG) {
            Log.e("NewsPARSER", "${parser.name}")
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            //Starts by looking for the "item" tag (Documentation uses entry)
            if(parser.name == "item") {
                channels.add(readItem(parser))
            } else {
                skip(parser)
            }
        }
        return channels
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readItem(parser: XmlPullParser) : Item {
        parser.require(XmlPullParser.START_TAG, ns, "item")
        //define the tags I want to parse
        var title: String? = null
        var link: String? = null
        var description: String? = null
        var pubDate: Date? = null
        var dcCreator: String? = null
        var source_id: String? = null
        val category = mutableListOf<String?>()
        val mediaContent = mutableListOf<String?>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when(parser.name) {
                "title" -> title = readTitle(parser)
                "source_id" -> source_id = readUid(parser)
                "description" -> description = readDescription(parser)
                "link" -> link = readLink(parser)
                "pubDate" -> pubDate = readPubDate(parser)
                "dc:creator" -> dcCreator = readDcCreator(parser)
                "media:content" -> mediaContent.add(readContent(parser))
                "category" -> category.add(readCategory(parser))
                else -> skip(parser)
            }
        }
        return Item(title, link, description, pubDate, dcCreator, source_id, category, mediaContent)

    }
    // Reader for seperate <xml> Tags

    // Read Title
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readTitle(parser: XmlPullParser) : String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    // Read Link
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLink(parser: XmlPullParser) : String {
        parser.require(XmlPullParser.START_TAG, ns, "link")
        val link = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "link")
        return link
    }

    // Read Description
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readDescription(parser: XmlPullParser) : String {
        parser.require(XmlPullParser.START_TAG, ns, "description")
        val description = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "description")
        return description
    }
    
    // Read pubDate
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readPubDate(parser: XmlPullParser) : Date? {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate")
        val pubDate = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "pubDate")
        //change date format
        val inputDateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US)
        return inputDateFormat.parse(pubDate)
    }
    
    // Read dcCreator
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readDcCreator(parser: XmlPullParser) : String {
        parser.require(XmlPullParser.START_TAG, ns, "dc:creator")
        val dcCreator = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "dc:creator")
        return dcCreator
    }
    
    // Read source ID
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readUid(parser: XmlPullParser) : String {
        parser.require(XmlPullParser.START_TAG, ns, "source_id")
        val source_id = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "source_id")
        return source_id
    }

    // Read Category

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readCategory(parser: XmlPullParser) : String {
        parser.require(XmlPullParser.START_TAG, ns, "category")
        val category = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "category")
        return category
    }

    // Read Content
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readContent(parser: XmlPullParser) : String {
        var imageURl = ""
        parser.require(XmlPullParser.START_TAG, ns, "media:content")
        imageURl = readAttribute(parser, "url")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            skip(parser)
        }
        parser.require(XmlPullParser.END_TAG, ns, "media:content")
        return imageURl
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readAttribute(parser: XmlPullParser, attribute: String) : String {
        var result = ""
        result = parser.getAttributeValue(null, attribute)
        return result
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser) : String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun skip(parser: XmlPullParser) {
        if(parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when(parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}
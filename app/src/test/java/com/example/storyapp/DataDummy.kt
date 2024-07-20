package com.example.storyapp

import com.example.storyapp.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                photoUrl = "",
                createdAt = "",
                name = "name $i",
                description = "description $i",
                lon = 0.0,
                lat = 0.0,
                id = "$i",
            )
            items.add(story)
        }
        return items
    }
}
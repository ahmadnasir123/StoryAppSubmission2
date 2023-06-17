package com.sirdev.storyappsubmissionakhir.utils

import com.sirdev.storyappsubmissionakhir.data.response.ListUserStoriesItem

object Dummy {
    fun generateDataDummyStories(): List<ListUserStoriesItem> {
        val storiesList = ArrayList<ListUserStoriesItem>()
        for (i in 1..20) {
            val stroies = ListUserStoriesItem(
                photoUrl =  "0",
                createdAt = "",
                name = "Name $i",
                description = "Description $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10
            )
            storiesList.add(stroies)
        }
        return storiesList
    }
}
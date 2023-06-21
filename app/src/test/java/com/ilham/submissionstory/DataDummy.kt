package com.ilham.submissionstory

import com.ilham.submissionstory.model.UserModel
import com.ilham.submissionstory.networking.DataResponse
import com.ilham.submissionstory.networking.ListStories
import com.ilham.submissionstory.networking.LoginResult

object DataDummy {
    private fun generateDummyLoginResult(): LoginResult {
        return LoginResult(
            "wiwu",
            "1234",
            "token"
        )
    }

    fun generateDummyUserModel(): UserModel {
        return UserModel(
            "userId",
            "userName",
            "token",
            true
        )
    }

    fun generateDummyStoryListResponse(): List<ListStories> {
        val items: MutableList<ListStories> = arrayListOf()
        for (i in 0..100) {
            val story = ListStories(
                i.toString(),
                "user + $i",
                "photo $i",
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyMapsEntity(): DataResponse {
        val stories = arrayListOf<ListStories>()

        for (i in 0 until 10) {
            val story = ListStories(
                "story-$i",
                "Dimas",
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "Lorem Ipsum",
                "2022-01-08T06:34:18.598Z",
                -16.002,
                -10.212
            )

            stories.add(story)
        }
        return DataResponse(
            false, "Stories fetched successfully",
            generateDummyLoginResult(), stories
        )
    }
    fun generateDummyDataResponse(): DataResponse {
        return DataResponse(
            error = false,
            message = "Success",
            result = generateDummyLoginResult(),
            story = generateDummyStoryListResponse()
        )
    }
}
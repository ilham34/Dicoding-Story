package com.ilham.submissionstory

import android.content.Context
import com.ilham.submissionstory.database.StoryDatabase
import com.ilham.submissionstory.model.StoryRepository
import com.ilham.submissionstory.networking.ApiConfig

object StoryInjection {
    fun provider(context: Context): StoryRepository {
        val apiSer = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(database, apiSer)
    }
}
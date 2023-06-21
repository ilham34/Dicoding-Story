package com.ilham.submissionstory.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ilham.submissionstory.networking.ListStories

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStories?>?)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, ListStories>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}
package com.ilham.submissionstory.view.maps

import androidx.lifecycle.ViewModel
import com.ilham.submissionstory.model.StoryRepository

class MapsViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {
    fun getStoriesWithMaps(token: String) = storyRepository.getMaps(token)
}
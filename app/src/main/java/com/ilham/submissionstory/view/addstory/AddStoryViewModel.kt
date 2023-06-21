package com.ilham.submissionstory.view.addstory

import androidx.lifecycle.ViewModel
import com.ilham.submissionstory.model.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun uploadStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody
    ) = storyRepository.uploadStory(token, imageMultipart, description)
}
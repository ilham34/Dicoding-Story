package com.ilham.submissionstory.view.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ilham.submissionstory.DataDummy
import com.ilham.submissionstory.getOrAwaitValue
import com.ilham.submissionstory.model.Result
import com.ilham.submissionstory.model.StoryRepository
import com.ilham.submissionstory.networking.DataResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyToken = "riaebhsfwrjekwan"

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepository: StoryRepository

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(mockRepository)
    }

    @Test
    fun `when upload story is success, then should not null and return Success`() {
        val file = Mockito.mock(File::class.java)
        val description = "auewfbjkdirodwfn".toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        val expectedStoryPost = MutableLiveData<Result<DataResponse>>()
        expectedStoryPost.value = Result.Success(DataDummy.generateDummyDataResponse())
        Mockito.`when`(mockRepository.uploadStory(dummyToken, imageMultipart, description)).thenReturn(expectedStoryPost)

        val postStory = addStoryViewModel.uploadStory(dummyToken, imageMultipart, description).getOrAwaitValue()
        verify(mockRepository).uploadStory(dummyToken, imageMultipart, description)
        assertTrue(postStory is Result.Success)
        assertNotNull(postStory)
    }
}
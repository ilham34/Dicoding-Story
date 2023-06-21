package com.ilham.submissionstory.view.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ilham.submissionstory.DataDummy
import com.ilham.submissionstory.MainCoroutineRule
import com.ilham.submissionstory.getOrAwaitValue
import com.ilham.submissionstory.model.Result
import com.ilham.submissionstory.model.StoryRepository
import com.ilham.submissionstory.networking.DataResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyMaps = DataDummy.generateDummyMapsEntity()
    private val dummyToken = "hfoklndpiqqwe"
    private val mockRepository = Mockito.mock(StoryRepository::class.java)

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(mockRepository)
    }

    @Test
    fun `when user get Stories with Maps, then should not null and Return Success`() {
        val expectedStory = MutableLiveData<Result<DataResponse>>()
        expectedStory.value = Result.Success(dummyMaps)

        Mockito.`when`(mockRepository.getMaps(dummyToken)).thenReturn(expectedStory)

        val story = mapsViewModel.getStoriesWithMaps(dummyToken).getOrAwaitValue()

        Assert.assertNotNull(story)
        Assert.assertTrue(story is Result.Success<*>)
        assertEquals(dummyMaps.story.size, (story as Result.Success).data.story.size)
    }
}
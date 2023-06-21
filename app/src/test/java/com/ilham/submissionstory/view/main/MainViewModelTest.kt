package com.ilham.submissionstory.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.ilham.submissionstory.DataDummy
import com.ilham.submissionstory.MainCoroutineRule
import com.ilham.submissionstory.adapter.StoriesAdapter
import com.ilham.submissionstory.getOrAwaitValue
import com.ilham.submissionstory.model.StoryRepository
import com.ilham.submissionstory.model.UserPreference
import com.ilham.submissionstory.networking.ListStories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainCoroutineRule()

    private lateinit var mainViewModel: MainViewModel
    private val mockRepository = Mockito.mock(StoryRepository::class.java)
    private val mockPreference = Mockito.mock(UserPreference::class.java)

    private val dummyToken = "snau9dneorqbdjlsak"

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(mockPreference, mockRepository)
    }

    @Test
    fun `when user get saved User, then should not null`() = runTest {
        val expectedResponse = flowOf(DataDummy.generateDummyUserModel())

        Mockito.`when`(mockPreference.getUser()).thenReturn(expectedResponse)

        mainViewModel.getUser().collect {
            assertNotNull(it.token)
            assertEquals(DataDummy.generateDummyUserModel().token, it.token)
        }

        Mockito.verify(mockPreference).getUser()
    }

    @Test
    fun `when user get Stories, then should not null`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryListResponse()
        val data: PagingData<ListStories> = StoryPagedTestSource.snapshot(dummyStory)

        val expectedStory = MutableLiveData<PagingData<ListStories>>()
        expectedStory.value = data

        Mockito.`when`(mockRepository.getStories(dummyToken)).thenReturn(expectedStory)

        val story: PagingData<ListStories> =
            mainViewModel.getStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainDispatcherRules.dispatcher,
            workerDispatcher = mainDispatcherRules.dispatcher
        )

        differ.submitData(story)

        advanceUntilIdle()

        Mockito.verify(mockRepository).getStories(dummyToken)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }

    @Test
    fun `when user logout, then Success logout`() = runTest {
        mainViewModel.logout()
        Mockito.verify(mockPreference).logout()
    }
}

class StoryPagedTestSource : PagingSource<Int, LiveData<List<ListStories>>>() {
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStories>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStories>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<ListStories>): PagingData<ListStories> {
            return PagingData.from(items)
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
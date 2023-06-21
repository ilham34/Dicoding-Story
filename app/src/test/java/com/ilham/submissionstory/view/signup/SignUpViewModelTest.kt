package com.ilham.submissionstory.view.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ilham.submissionstory.DataDummy
import com.ilham.submissionstory.getOrAwaitValue
import com.ilham.submissionstory.model.Result
import com.ilham.submissionstory.model.StoryRepository
import com.ilham.submissionstory.networking.DataResponse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignUpViewModelTest {

    private lateinit var signUpViewModel: SignUpViewModel
    private val dummyName = "wiwu"
    private val dummyEmail = "wiwu@ytb.com"
    private val dummyPassword = "qwerty"

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepository: StoryRepository

    @Before
    fun setUp() {
        signUpViewModel = SignUpViewModel(mockRepository)
    }

    @Test
    fun `when user register is success, then should not null and Return Success`() {
        val expectedResult = MutableLiveData<Result<DataResponse>>()
        expectedResult.value = Result.Success(DataDummy.generateDummyDataResponse())
        Mockito.`when`(mockRepository.signUpAccount(dummyName, dummyEmail, dummyPassword))
            .thenReturn(expectedResult)

        val user =
            signUpViewModel.signUpAccount(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(mockRepository).signUpAccount(dummyName, dummyEmail, dummyPassword)
        assertTrue(user is Result.Success)
        assertNotNull(user)
    }
}
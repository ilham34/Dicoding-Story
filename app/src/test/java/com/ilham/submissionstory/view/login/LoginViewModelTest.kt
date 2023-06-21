package com.ilham.submissionstory.view.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ilham.submissionstory.DataDummy
import com.ilham.submissionstory.MainCoroutineRule
import com.ilham.submissionstory.getOrAwaitValue
import com.ilham.submissionstory.model.Result
import com.ilham.submissionstory.model.StoryRepository
import com.ilham.submissionstory.model.UserModel
import com.ilham.submissionstory.model.UserPreference
import com.ilham.submissionstory.networking.DataResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private val mockRepository = Mockito.mock(StoryRepository::class.java)
    private val mockPreference = Mockito.mock(UserPreference::class.java)
    private lateinit var loginViewModel: LoginViewModel
    private val dummyID = "265"
    private val dummyName = "Wiwu"
    private val dummyToken = "hfojnafdsojrn"
    private val dummyEmail = "wiwu@dbz.com"
    private val dummyPassword = "qwerty"
    private val dummyLoginSession = true

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(mockPreference, mockRepository)
    }

    @Test
    fun `when user saved Successfully, then data saved in datastore`() = runTest {
        loginViewModel.saveUser(UserModel(dummyID, dummyName, dummyToken, dummyLoginSession))
        Mockito.verify(mockPreference)
            .saveUser(UserModel(dummyID, dummyName, dummyToken, dummyLoginSession))
    }

    @Test
    fun `when user login, then should not null and return Success`() = runTest {
        val expectedResult = MutableLiveData<Result<DataResponse>>()
        expectedResult.value = Result.Success(DataDummy.generateDummyDataResponse())

        Mockito.`when`(mockRepository.loginAccount(dummyEmail, dummyPassword))
            .thenReturn(expectedResult)
        val actualUser = loginViewModel.loginAccount(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(mockRepository).loginAccount(dummyEmail, dummyPassword)
        assertTrue(actualUser is Result.Success)
        assertNotNull(actualUser)
    }


}
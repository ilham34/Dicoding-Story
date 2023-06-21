package com.ilham.submissionstory.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.ilham.submissionstory.database.StoryDatabase
import com.ilham.submissionstory.networking.ApiService
import com.ilham.submissionstory.networking.DataResponse
import com.ilham.submissionstory.networking.ListStories
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,

    ) {
    fun getStories(token: String): LiveData<PagingData<ListStories>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.databaseDao().getAllStories()
            }
        ).liveData
    }

    fun uploadStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<DataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.postImage(token, imageMultipart, description)
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d(TAG, "uploadStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun loginAccount(email: String, password: String): LiveData<Result<DataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.loginAccount(email, password)
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d(TAG, "loginAccount: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun signUpAccount(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<DataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.signupAccount(name, email, password)
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d(TAG, "signUpAccount: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getMaps(token: String): LiveData<Result<DataResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getStories("Bearer $token", location = 1)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        const val TAG = "StoryRepository"
    }
}
package com.android.footballapi.feature.main

import com.android.footballapi.data.api.ApiRepository
import com.android.footballapi.feature.TestContextProvider
import com.android.footballapi.model.Team
import com.android.footballapi.model.TeamResponse
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.*

class TeamsPresenterTest {

    @Mock
    private lateinit var view: TeamsView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var presenter: TeamsPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TeamsPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetTeamList() {
        val teams: MutableList<Team> = mutableListOf()
        val response = TeamResponse(teams)
        val league = "German Bundesliga"

        runBlocking {
            Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))?.thenReturn(apiResponse)
            Mockito.`when`(apiResponse.await())?.thenReturn("")
            Mockito.`when`(gson.fromJson("", TeamResponse::class.java))?.thenReturn(response)

            presenter.getTeamList(league)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showTeamList(teams)
            Mockito.verify(view).hideLoading()
        }
    }

}
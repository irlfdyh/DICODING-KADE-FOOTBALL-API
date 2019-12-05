package com.android.footballapi.feature.main

import com.android.footballapi.model.TeamResponse
import com.android.footballapi.data.api.ApiRepository
import com.android.footballapi.data.api.TheSportDBApi
import com.android.footballapi.feature.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamsPresenter(private val view: TeamsView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson,
                     private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getTeamList(league: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository.doRequestAsync(TheSportDBApi.getTeams(league)).await(),
                TeamResponse::class.java
            )

            view.showTeamList(data.teams)
            view.hideLoading()
        }
    }
}

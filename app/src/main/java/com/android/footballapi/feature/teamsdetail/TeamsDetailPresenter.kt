package com.android.footballapi.feature.teamsdetail

import com.android.footballapi.data.api.ApiRepository
import com.android.footballapi.data.api.TheSportDBApi
import com.android.footballapi.model.TeamResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamsDetailPresenter(private val view: TeamsDetailView,
                           private val apiRepository: ApiRepository,
                           private val gson: Gson) {

    fun getTeamDetail(teamId: String){
        view.showLoading()

        GlobalScope.launch(Dispatchers.Main) {
            val data = gson.fromJson(
                apiRepository.doRequestAsync(TheSportDBApi.getTeamDetail(teamId)).await(),
                TeamResponse::class.java
            )

            view.showDetailTeam(data.teams)
            view.hideLoading()
        }
    }
}
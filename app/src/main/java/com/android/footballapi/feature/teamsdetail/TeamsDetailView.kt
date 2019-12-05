package com.android.footballapi.feature.teamsdetail

import com.android.footballapi.model.Team

interface TeamsDetailView {
    fun showLoading()
    fun hideLoading()
    fun showDetailTeam(data: List<Team>)
}
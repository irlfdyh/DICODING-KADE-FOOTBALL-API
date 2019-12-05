package com.android.footballapi.feature.main

import com.android.footballapi.model.Team

interface TeamsView {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Team>)
}
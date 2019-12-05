package com.android.footballapi.feature.teamsdetail

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.footballapi.model.Favorite
import com.android.footballapi.R
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import com.android.footballapi.R.color.*
import com.android.footballapi.data.api.ApiRepository
import com.android.footballapi.data.local.database
import com.android.footballapi.model.Team
import com.android.footballapi.util.invisible
import com.android.footballapi.util.visible
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TeamsDetailActivity : AppCompatActivity(), TeamsDetailView {

    //SQLite
    private var menuItem: Menu? = null
    private var isBoolean: Boolean = false

    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var teamBadge: ImageView
    private lateinit var teamName: TextView
    private lateinit var teamFormedYear: TextView
    private lateinit var teamStadium: TextView
    private lateinit var teamDescription: TextView

    private lateinit var presenter: TeamsDetailPresenter
    private lateinit var teams: Team
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            backgroundColor = Color.WHITE

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light)

                scrollView {
                    isVerticalScrollBarEnabled = false
                    relativeLayout {
                        lparams(width = matchParent, height = wrapContent)

                        linearLayout{
                            lparams(width = matchParent, height = wrapContent)
                            padding = dip(10)
                            orientation = LinearLayout.VERTICAL
                            gravity = Gravity.CENTER_HORIZONTAL

                            teamBadge =  imageView(R.drawable.ic_added_to_favorites) {}.lparams(height = dip(75))

                            teamName = textView("Team Name") {
                                this.gravity = Gravity.CENTER
                                textSize = 20f
                                textColor = ContextCompat.getColor(context, colorAccent)
                            }.lparams{
                                topMargin = dip(5)
                            }

                            teamFormedYear = textView ("formed") {
                                this.gravity = Gravity.CENTER
                            }

                            teamStadium = textView ("stadium"){
                                this.gravity = Gravity.CENTER
                                textColor = ContextCompat.getColor(context, colorSecondaryText)
                            }

                            teamDescription = textView("desc").lparams{
                                topMargin = dip(20)
                            }
                        }
                        progressBar = progressBar {
                        }.lparams {
                            centerHorizontally()
                        }
                    }
                }
            }
        }

        val intent = intent
        id = intent.getStringExtra("id")

        favoriteState()

        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamsDetailPresenter(this, request, gson)
        presenter.getTeamDetail(id)
        swipeRefresh.onRefresh {
            presenter.getTeamDetail(id)
        }

        supportActionBar?.title = "Team Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showDetailTeam(data: List<Team>) {
        swipeRefresh.isRefreshing = false

        teams = Team(data[0].teamId,
            data[0].teamName,
            data[0].teamBadge)

        Picasso.get().load(data[0].teamBadge).into(teamBadge)
        teamName.text = data[0].teamName
        teamDescription.text = data[0].teamDescription
        teamFormedYear.text = data[0].teamFormedYear
        teamStadium.text = data[0].teamStadium
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favotite -> {
                if(isBoolean) removeFromFavorite() else addToFavorite()

                isBoolean =! isBoolean
                setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(
                    Favorite.TABLE_FAVORITE,
                    Favorite.TEAM_ID to teams.teamId,
                    Favorite.TEAM_NAME to teams.teamName,
                    Favorite.TEAM_BADGE to teams.teamBadge)
            }
            swipeRefresh.snackbar("Added To Favorite").show()
        } catch (e: SQLiteConstraintException){
            swipeRefresh.snackbar(e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try{
            database.use {
                delete(
                    Favorite.TABLE_FAVORITE, "(TEAM_ID = {id})",
                    "id" to id)
            }
            swipeRefresh.snackbar("Removed from favorite").show()
        } catch (e: SQLiteConstraintException){
            swipeRefresh.snackbar(e.localizedMessage).show()
        }
    }

    private fun setFavorite(){
        if (isBoolean){
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        } else {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
        }
    }

    private fun favoriteState(){
        database.use {
            val result = select(Favorite.TABLE_FAVORITE)
                .whereArgs("(TEAM_ID = {id})",
                    "id" to id)

            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isBoolean = true
        }
    }
}
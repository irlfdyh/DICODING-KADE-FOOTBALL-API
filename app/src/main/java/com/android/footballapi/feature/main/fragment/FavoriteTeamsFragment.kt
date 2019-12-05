package com.android.footballapi.feature.main.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.footballapi.model.Favorite
import com.android.footballapi.R
import com.android.footballapi.adapter.FavoriteTeamsAdapter
import com.android.footballapi.data.local.database
import com.android.footballapi.feature.teamsdetail.TeamsDetailActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteTeamsFragment : Fragment(), AnkoComponent<Context> {

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var listTeam: RecyclerView

    private var favorites: MutableList<Favorite> = mutableListOf()
    private lateinit var adapter: FavoriteTeamsAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = FavoriteTeamsAdapter(favorites){
            context?.startActivity<TeamsDetailActivity>("id" to "${it.teamId}")
        }
        listTeam.adapter = adapter
        swipeRefresh.onRefresh {
            showFavorite()
        }
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(requireContext()))
    }

    private fun showFavorite(){
        favorites.clear()
        context?.database?.use{
            swipeRefresh.isRefreshing = false

            val result = select(Favorite.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<Favorite>())

            favorites.addAll(favorite)
            adapter.notifyDataSetChanged()
        }
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            lparams (width = matchParent, height = wrapContent)
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(
                    R.color.colorAccent,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light)

                listTeam = recyclerView {
                    lparams (width = matchParent, height = wrapContent)
                    layoutManager = LinearLayoutManager(ctx)
                }
            }
        }
    }

}
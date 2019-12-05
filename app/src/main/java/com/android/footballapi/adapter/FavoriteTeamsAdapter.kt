package com.android.footballapi.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.footballapi.model.Favorite
import com.android.footballapi.R
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

class FavoriteTeamsAdapter(private val favorite: List<Favorite>, private val listener: (Favorite) -> Unit)
    : RecyclerView.Adapter<FavoriteTeamsAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(TeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bindItem(favorite[position], listener)
    }

    override fun getItemCount(): Int = favorite.size


    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val teamBadge: ImageView = view.find(R.id.team_badge)
        private val teamName: TextView = view.find(R.id.team_name)

        fun bindItem(favorite: Favorite, listener: (Favorite) -> Unit) {
            Picasso.get().load(favorite.teamBadge).into(teamBadge)
            teamName.text = favorite.teamName
            itemView.setOnClickListener { listener(favorite) }
        }
    }

}
package com.example.uas_movie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieUserAdapter(

    private val movieUserList: ArrayList<MovieAdminData>) : RecyclerView.Adapter<MovieUserAdapter.MovieUserViewHolder>() {
    class MovieUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleUser: TextView = itemView.findViewById(R.id.textViewTitle)
        val genreMovie: TextView = itemView.findViewById(R.id.viewGenre)
        val deskripsiMovie: TextView = itemView.findViewById(R.id.textViewDeskripsi)
        val ratingMovie: TextView = itemView.findViewById(R.id.ratingView)
        val durationMovie: TextView = itemView.findViewById(R.id.viewDuration)
        val imageUser: ImageView = itemView.findViewById(R.id.imageViewPoster)
        val buttonShow: Button = itemView.findViewById(R.id.buttonShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieUserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item_user, parent, false)
        return MovieUserViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movieUserList.size
    }

    override fun onBindViewHolder(holder: MovieUserViewHolder, position: Int) {
        val currentItem = movieUserList[position]
        holder.titleUser.text = currentItem.title
        holder.genreMovie.text = currentItem.genre
        holder.deskripsiMovie.text = currentItem.deskripsi
        holder.ratingMovie.text = currentItem.rating
        holder.durationMovie.text = currentItem.duration

        // Use Glide or Picasso to load the image from the URL into the ImageView
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.imageUser)


        // Button click to open DetailMovie activity
        holder.buttonShow.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailMovie::class.java)

            // Pass movie data to the DetailMovie activity
            intent.putExtra("MOVIE_TITLEAAAAA", currentItem.title)
            intent.putExtra("MOVIE_RATING", currentItem.rating)
            intent.putExtra("MOVIE_DURATION", currentItem.duration)
            intent.putExtra("MOVIE_GENRE", currentItem.genre)
            intent.putExtra("MOVIE_DESCRIPTION", currentItem.deskripsi)
            intent.putExtra("MOVIE_IMAGE_URL",  currentItem.imageUrl)

            context.startActivity(intent)

        }



    }
    fun setData(dataMovie: List<MovieAdminData>) {
        movieUserList.clear()
        movieUserList.addAll(dataMovie)
        notifyDataSetChanged()
    }
}
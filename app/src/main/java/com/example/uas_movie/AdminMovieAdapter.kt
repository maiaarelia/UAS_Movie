package com.example.uas_movie


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

// Define the interface
interface MovieItemClickListener {
    fun onEditButtonClick(movie: MovieAdminData)
    fun onDeleteButtonClick(movie: MovieAdminData)

}


class AdminMovieAdapter(

//
    private val MovieAdmin: ArrayList<MovieAdminData>,
    private val clickListener: MovieItemClickListener
    ):
    RecyclerView.Adapter<AdminMovieAdapter.MovieAdminViewHolder>()
    {

        class MovieAdminViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.textViewTitle)
            val genre: TextView = itemView.findViewById(R.id.textViewGenre)
            val duration: TextView = itemView.findViewById(R.id.textViewDuration)
            val image: ImageView = itemView.findViewById(R.id.imageViewPoster)
            val buttonEdit : Button = itemView.findViewById(R.id.buttonEdit)
            val buttonHapus : Button = itemView.findViewById(R.id.buttonHapus)
        }


        override fun onBindViewHolder(holder: MovieAdminViewHolder, position: Int) {
            val currentItem = MovieAdmin[position]
            currentItem.imageUrl?.let { Log.d("ImageURL", it) }
            holder.title.setText(currentItem.title)
            holder.genre.setText(currentItem.genre)
            holder.duration.setText(currentItem.duration)

            // Use Glide or Picasso to load the image from the URL into the ImageView

                Glide.with(holder.itemView.context)
                    .load(currentItem.imageUrl)
                    .into(holder.image)


            // Set click listeners for buttons
            holder.buttonEdit.setOnClickListener {
                clickListener.onEditButtonClick(currentItem)
            }

            holder.buttonHapus.setOnClickListener {
                clickListener.onDeleteButtonClick(currentItem)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdminViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_item_admin, parent, false)
            return MovieAdminViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return MovieAdmin.size
        }


//        private fun deleteItemFromDatabase(imgId: String) {
//            // Reference to the Firebase Storage
//            val storageReference = FirebaseStorage.getInstance().getReference("images").child(imgId)
//
//            // Delete the image from Firebase Storage
//            storageReference.delete().addOnSuccessListener {
//                // Image deleted successfully, now delete the corresponding data from the Realtime Database
//                val database = FirebaseDatabase.getInstance().getReference("Film")
//                database.child(imgId).removeValue()
//                    .addOnCompleteListener {
//                        // Handle success if needed
//                    }
//                    .addOnFailureListener {
//                        // Handle failure if needed
//                    }
//            }.addOnFailureListener {
//                // Handle failure if the image deletion fails
//            }
//        }

        fun setData(dataMovie: List<MovieAdminData>){
            MovieAdmin.clear()
            MovieAdmin.addAll(dataMovie)
        }

    }




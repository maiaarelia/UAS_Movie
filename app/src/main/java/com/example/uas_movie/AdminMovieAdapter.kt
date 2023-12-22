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
    private val MovieAdmin: ArrayList<MovieAdminData>,  // Daftar film yang akan ditampilkan
    private val clickListener: MovieItemClickListener  // Antarmuka untuk menangani klik pada item film
) : RecyclerView.Adapter<AdminMovieAdapter.MovieAdminViewHolder>() {

    // ViewHolder untuk menahan tampilan item film
    class MovieAdminViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewTitle)      // Judul film
        val genre: TextView = itemView.findViewById(R.id.textViewGenre)      // Genre film
        val duration: TextView = itemView.findViewById(R.id.textViewDuration)  // Durasi film
        val image: ImageView = itemView.findViewById(R.id.imageViewPoster)  // Gambar poster film
        val buttonEdit: Button = itemView.findViewById(R.id.buttonEdit)      // Tombol Edit
        val buttonHapus: Button = itemView.findViewById(R.id.buttonHapus)    // Tombol Hapus
    }

    // Dipanggil saat RecyclerView perlu tampilan baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdminViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item_admin, parent, false)
        return MovieAdminViewHolder(itemView)
    }

    // Dipanggil untuk menetapkan data pada tampilan item
    override fun onBindViewHolder(holder: MovieAdminViewHolder, position: Int) {
        val currentItem = MovieAdmin[position]
        currentItem.imageUrl?.let { Log.d("ImageURL", it) }  // Log URL gambar (jika ada)
        holder.title.text = currentItem.title  // Set judul film
        holder.genre.text = currentItem.genre  // Set genre film
        holder.duration.text = currentItem.duration  // Set durasi film

        // Gunakan Glide untuk memuat gambar dari URL ke ImageView
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.image)

        // Atur pendengar klik untuk tombol Edit
        holder.buttonEdit.setOnClickListener {
            clickListener.onEditButtonClick(currentItem)
        }

        // Atur pendengar klik untuk tombol Hapus
        holder.buttonHapus.setOnClickListener {
            clickListener.onDeleteButtonClick(currentItem)
        }
    }

    // Mengembalikan jumlah item dalam daftar
    override fun getItemCount(): Int {
        return MovieAdmin.size
    }

    // Mengganti data yang ada dengan data baru
    fun setData(dataMovie: List<MovieAdminData>) {
        MovieAdmin.clear()
        MovieAdmin.addAll(dataMovie)
    }
}
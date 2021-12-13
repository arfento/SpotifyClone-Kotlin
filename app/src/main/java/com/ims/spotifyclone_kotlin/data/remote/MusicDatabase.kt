package com.ims.spotifyclone_kotlin.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.ims.spotifyclone_kotlin.data.entities.Song
import com.ims.spotifyclone_kotlin.others.Constants.SONG_COLLECTION
import kotlinx.coroutines.tasks.await

class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch(e: Exception) {
            emptyList()
        }
    }
}
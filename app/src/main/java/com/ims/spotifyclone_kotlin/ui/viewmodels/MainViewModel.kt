package com.ims.spotifyclone_kotlin.ui.viewmodels

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ims.spotifyclone_kotlin.data.entities.Song
import com.ims.spotifyclone_kotlin.exoplayer.MusicServiceConnection
import com.ims.spotifyclone_kotlin.exoplayer.isPlayEnabled
import com.ims.spotifyclone_kotlin.exoplayer.isPlaying
import com.ims.spotifyclone_kotlin.exoplayer.isPrepared
import com.ims.spotifyclone_kotlin.others.Constants.MEDIA_ROOT_ID
import com.ims.spotifyclone_kotlin.others.Resource

class MainViewModel @ViewModelInject constructor(
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {
    private val _mediaItems = MutableLiveData<Resource<List<Song>>>()
    val mediaItem : LiveData<Resource<List<Song>>> = _mediaItems

    val isConnected = musicServiceConnection.isConnected
    val networkError = musicServiceConnection.networkError
    val curPlayingSong = musicServiceConnection.curPlayingSong
    val playbackState = musicServiceConnection.playbackState

    init {
        _mediaItems.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val items = children.map {
                    Song(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.subtitle.toString(),
                        it.description.mediaUri.toString(),
                        it.description.iconUri.toString(),
                    )
                }
                _mediaItems.postValue(Resource.success(items))
            }

        })
    }

    fun skipToNextSong(){
        musicServiceConnection.transportasiController.skipToNext()
    }

    fun skipToPreviousSong(){
        musicServiceConnection.transportasiController.skipToPrevious()
    }

    fun seekTo(pos : Long){
        musicServiceConnection.transportasiController.seekTo(pos)
    }

    fun playOrToggleSong(mediaItem: Song, toggle : Boolean = false){
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId ==
                curPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)){
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> if (toggle) musicServiceConnection.transportasiController.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportasiController.play()
                    else -> Unit
                }

            }
        } else{
            musicServiceConnection.transportasiController.playFromMediaId(mediaItem.mediaId, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback(){

        })
    }

}




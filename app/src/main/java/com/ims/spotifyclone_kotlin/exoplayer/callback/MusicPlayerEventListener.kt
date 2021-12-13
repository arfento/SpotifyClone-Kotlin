package com.ims.spotifyclone_kotlin.exoplayer.callback

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.ims.spotifyclone_kotlin.exoplayer.MusicService

class MusicPlayerEventListener(
    private val musicService: MusicService
) : Player.EventListener{

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if (playbackState == Player.STATE_READY && !playWhenReady){
            musicService.stopForeground(false)

        }

    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
    }
}
package com.mikkipastel.exoplayer18

import android.app.Activity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())

        playerView.player = player

        val dataSourceFactory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)))
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Samples.MP4_URI)

        player?.prepare(mediaSource)
        player?.playWhenReady = true

    }

    override fun onStop() {
        super.onStop()

        playerView.player = null
        player?.release()
        player = null
    }
}

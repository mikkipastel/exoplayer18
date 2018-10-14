package com.mikkipastel.exoplayer18

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.mikkipastel.exoplayer18.C.MEDIA_SESSION_TAG
import com.mikkipastel.exoplayer18.C.PLAYBACK_CHANNEL_ID
import com.mikkipastel.exoplayer18.C.PLAYBACK_NOTIFICATION_ID
import com.mikkipastel.exoplayer18.Samples.SAMPLES

class AudioPlayerService: Service() {

    lateinit var player: SimpleExoPlayer

    lateinit var playerNotificationManager: PlayerNotificationManager

    lateinit var mediaSession: MediaSessionCompat
    lateinit var mediaSessionConnector: MediaSessionConnector

    override fun onCreate() {
        super.onCreate()
        val context = this

        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())

        val dataSourceFactory = DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, "AudioDemo")
        )

        val concatenatingMediaSource = ConcatenatingMediaSource()
        for (sample in SAMPLES) {
            val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(sample.uri)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        player.prepare(concatenatingMediaSource)
        player.playWhenReady = true

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context,
                PLAYBACK_CHANNEL_ID,
                R.string.playback_channel_name,
                PLAYBACK_NOTIFICATION_ID,
                object: PlayerNotificationManager.MediaDescriptionAdapter {
                    override fun getCurrentContentTitle(player: Player?): String {
                        return SAMPLES[player!!.currentWindowIndex].title
                    }

                    override fun createCurrentContentIntent(player: Player?): PendingIntent? {
                        return PendingIntent.getActivity(
                                context,
                                0,
                                Intent(context, MainActivity::class.java),
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }

                    override fun getCurrentContentText(player: Player?): String? {
                        return SAMPLES[player!!.currentWindowIndex].description
                    }

                    override fun getCurrentLargeIcon(player: Player?, callback: PlayerNotificationManager.BitmapCallback?): Bitmap? {
                        return Samples.getBitmap(
                                context, SAMPLES[player!!.currentWindowIndex].bitmapResource)
                    }

                })

        playerNotificationManager.apply {
            setNotificationListener(object: PlayerNotificationManager.NotificationListener {
                override fun onNotificationStarted(notificationId: Int, notification: Notification?) {
                    startForeground(notificationId, notification)
                }

                override fun onNotificationCancelled(notificationId: Int) {
                    stopSelf()
                }

            })
            setPlayer(player)
        }

        mediaSession = MediaSessionCompat(context, MEDIA_SESSION_TAG)
        mediaSession.isActive = true
        playerNotificationManager.setMediaSessionToken(mediaSession.sessionToken)

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.apply {
            setQueueNavigator(object: TimelineQueueNavigator(mediaSession) {
                override fun getMediaDescription(player: Player?, windowIndex: Int): MediaDescriptionCompat {
                    return Samples.getMediaDescription(context, SAMPLES[windowIndex])
                }

            })
            setPlayer(player, null)
        }
    }

    override fun onDestroy() {
        mediaSession.release()
        mediaSessionConnector.setPlayer(null, null)
        playerNotificationManager.setPlayer(null)
        player.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

}
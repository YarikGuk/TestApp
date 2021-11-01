package by.huk.testaudiovideoapplication.second

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.IBinder
import android.util.Log
import by.huk.testaudiovideoapplication.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util


class PlayerNotificationService : Service() {


    private lateinit var mPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerNotificationManager: PlayerNotificationManager

    private var notificationId = 123
    private var channelId = "channelId"


    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate")

        val context = this
        mPlayer = SimpleExoPlayer.Builder(this).build()
        // Create a data source factory.
//        dataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(context, "app-name"))


        val userAgent = Util.getUserAgent(context, "TestAudioVideoApplication")
        val httpDataSourceFactory = DefaultHttpDataSourceFactory(
            userAgent,
            null,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true
        )

        dataSourceFactory = httpDataSourceFactory
//        val dataSourceFactory = DefaultDataSourceFactory(
//            context,
//            null,
//            httpDataSourceFactory
//        )
//        dataSourceFactory = dataSourceFactory

        mPlayer.prepare(getListOfMediaSource())
        Log.e(TAG, "6")
        mPlayer.playWhenReady = true

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            channelId,
            R.string.channel_name,
            R.string.channel_desc,
            notificationId,
            object : PlayerNotificationManager.MediaDescriptionAdapter {


                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    // return pending intent
                    val intent = Intent(context, SecondActivity::class.java);
                    return PendingIntent.getActivity(
                        context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                //pass description here
                override fun getCurrentContentText(player: Player): String? {
                    return "Some description"
                }

                //pass title (mostly playing audio name)
                override fun getCurrentContentTitle(player: Player): String {
                    return "BBC Radio 1"
                }

                // pass image as bitmap
                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback,
                ): Bitmap? {
                    return null
                }
            },
            object : PlayerNotificationManager.NotificationListener {

                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    onGoing: Boolean,
                ) {

                    notification.color = getColor(R.color.black)
                    startForeground(notificationId, notification)

                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean,
                ) {
                    stopSelf()
                }

            }
        )
        playerNotificationManager.setPlayer(mPlayer)


    }


    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onBind")
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }


    private fun getListOfMediaSource(): ConcatenatingMediaSource {

        val concatenatingMediaSource = ConcatenatingMediaSource()
        val mediaUrl =
            "http://a.files.bbci.co.uk/media/live/manifesto/audio/simulcast/hls/nonuk/sbr_low/ak/bbc_radio_one.m3u8"
        concatenatingMediaSource.addMediaSource(buildMediaSource(mediaUrl))

        return concatenatingMediaSource

    }

    private fun buildMediaSource(audioUrl: String): HlsMediaSource? {
        Log.e(TAG, "onBindMS")
        val uri = Uri.parse(audioUrl)
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    override fun onDestroy() {
        playerNotificationManager.setPlayer(null)
        mPlayer.release()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }
}


package by.huk.testaudiovideoapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.huk.testaudiovideoapplication.databinding.ActivityMainBinding
import by.huk.testaudiovideoapplication.second.SecondActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions





class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val iFramePlayerOptions: IFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(0)
            .rel(0)
            .ivLoadPolicy(3)
            .ccLoadPolicy(1)
            .build()

        val listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = "dQw4w9WgXcQ"
                youTubePlayer.loadVideo(videoId, 0f)
            }
        }

        binding.youtubePlayerView.initialize(listener,true,iFramePlayerOptions)
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.getPlayerUiController().showMenuButton(false).showBufferingProgress(false).showVideoTitle(false)
        binding.nextPage.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))

        }
    }
}

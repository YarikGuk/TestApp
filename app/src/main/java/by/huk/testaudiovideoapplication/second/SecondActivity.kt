package by.huk.testaudiovideoapplication.second

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import by.huk.testaudiovideoapplication.R


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val intent = Intent(this,PlayerNotificationService::class.java)
        findViewById<Button>(R.id.play).setOnClickListener {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               startForegroundService(intent)
           } else
               startService(intent)
        }
        findViewById<Button>(R.id.stop).setOnClickListener {
            stopService(intent)
        }

    }


}
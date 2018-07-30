package com.mikkipastel.exoplayer18

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.android.exoplayer2.util.Util
import com.mikkipastel.exoplayer18.Samples.SAMPLES
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, AudioPlayerService::class.java)
        Util.startForegroundService(this, intent)

        listview.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                emptyArray<Samples>()
        )

    }

}

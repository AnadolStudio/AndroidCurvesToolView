package com.anadolstudio.androidcurvestoolview

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.anadolstudio.library.curvestool.listener.CurvesValuesChangeListener
import com.anadolstudio.library.curvestool.view.CurvesView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val curve = findViewById<CurvesView>(R.id.curve)
        curve.setChangeListener(
            CurvesValuesChangeListener.Simple(
                onWhiteChanelChanged = { Log.d(TAG, "onWhiteChanelChanged: $it") },
                onRedChanelChanged = { Log.d(TAG, "onRedChanelChanged: $it") },
                onGreenChanelChanged = { Log.d(TAG, "onGreenChanelChanged: $it") },
                onBlueChanelChanged = { Log.d(TAG, "onBlueChanelChanged: $it") }
            )
        )

        findViewById<Button>(R.id.white).setOnClickListener { curve.showWhiteState() }
        findViewById<Button>(R.id.red).setOnClickListener { curve.showRedState() }
        findViewById<Button>(R.id.green).setOnClickListener { curve.showGreenState() }
        findViewById<Button>(R.id.blue).setOnClickListener { curve.showBlueState() }
    }
}

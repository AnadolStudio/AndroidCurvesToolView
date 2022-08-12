package com.anadolstudio.androidcurvestoolview

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.anadolstudio.library.curvestool.view.CurvesView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val curve = findViewById<CurvesView>(R.id.curve)

        findViewById<Button>(R.id.white).setOnClickListener { curve.showWhiteState() }
        findViewById<Button>(R.id.red).setOnClickListener { curve.showRedState() }
        findViewById<Button>(R.id.green).setOnClickListener { curve.showGreenState() }
        findViewById<Button>(R.id.blue).setOnClickListener { curve.showBlueState() }
    }
}

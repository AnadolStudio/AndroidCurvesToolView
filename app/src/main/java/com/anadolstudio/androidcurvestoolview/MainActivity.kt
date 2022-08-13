package com.anadolstudio.androidcurvestoolview

import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.anadolstudio.androidcurvestoolview.function.CurveFunction
import com.anadolstudio.androidcurvestoolview.function.Function
import com.anadolstudio.library.curvestool.listener.CurvesValuesChangeListener
import com.anadolstudio.library.curvestool.view.CurvesView
import org.wysaid.view.ImageGLSurfaceView

class MainActivity : AppCompatActivity() {

    private lateinit var surfaceView: ImageGLSurfaceView
    private lateinit var curve: CurvesView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSurface()
        initCurves()
        initButton()
    }

    private fun initButton() {
        findViewById<ImageView>(R.id.reset).setOnClickListener {
            curve.reset()
        }

        findViewById<RadioGroup>(R.id.radio_group).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.white -> curve.showWhiteState()
                R.id.red -> curve.showRedState()
                R.id.green -> curve.showGreenState()
                R.id.blue -> curve.showBlueState()
            }
        }
    }

    private fun initCurves() {
        curve = findViewById(R.id.curve)

        curve.setChangeListener(
            CurvesValuesChangeListener.Save(
                onChanged = { white, red, green, blue ->
                    surfaceView.setFilterWithConfig(
                        CurveFunction(white, red, green, blue).getFunction()
                    )
                },
                onReset = {
                    surfaceView.setFilterWithConfig(Function.Empty.getFunction())
                }
            )
        )
    }

    private fun initSurface() {
        surfaceView = findViewById<ImageGLSurfaceView>(R.id.image).apply {
            holder.setFormat(PixelFormat.RGBA_8888)
            displayMode = ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FILL
        }

        surfaceView.setSurfaceCreatedCallback {
            val bitmapDrawable =
                ContextCompat.getDrawable(this, R.drawable.sample_photo) as BitmapDrawable

            surfaceView.setImageBitmap(bitmapDrawable.bitmap)
        }
    }

    override fun onResume() {
        super.onResume()

        surfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        surfaceView.release()
        surfaceView.onPause()
    }
}

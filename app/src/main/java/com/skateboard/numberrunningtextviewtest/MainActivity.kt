package com.skateboard.numberrunningtextviewtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.skateboard.numberrunningview.NumberRunningView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val redDotList = mutableListOf(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
        13, 14, 15, 16, 17, 18, 19, 20,
        21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33
    )

    private val blueDotList = mutableListOf(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
        , 12, 13, 14, 15, 16
    )

    private var curPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        numberRunningView.dataList = redDotList
        numberRunningView.onNumberSelectedListener = object : NumberRunningView.OnNumberSelectedListenern {

            override fun onNumberSelected(num: Int) {

                if (curPos > 6) {
                    return
                }
                val resultTextView = resultLL.getChildAt(curPos) as TextView
                resultTextView.text = num.toString()
                if (curPos < 6) {
                    redDotList.remove(num)
                    numberRunningView.dataList = redDotList
                }
                curPos++
                if (curPos == 6) {
                    numberRunningView.dataList = blueDotList
                }

            }
        }
        numberRunningView.start()
        startBtn.setOnClickListener(this)
        stopBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.startBtn -> {

                numberRunningView.start()
            }

            R.id.stopBtn -> {

                numberRunningView.stop()
            }

        }

    }
}

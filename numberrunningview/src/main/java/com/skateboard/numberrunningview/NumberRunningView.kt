package com.skateboard.numberrunningview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

class NumberRunningView(context: Context, attributes: AttributeSet?) : View(context, attributes) {


    private var numberColor = Color.WHITE

    private var numberSize = 15.0f

    var min = 0

    var max = 0

    private var now = min

    private var offset = 0

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var isStart = false

    var maxSpeed = 10f

    private var curSpeed = 0f

    private var speedOffset = 0.1f

    var dataList: List<Int>? = null
        set(value) {
            field = value
            min = 0
            max = (value?.size ?: 1) - 1
            now = min
            offset = 0
        }

    var onNumberSelectedListener: OnNumberSelectedListenern? = null

    init {

        if (attributes != null) {
            parseAttrs(attributes)
        }
        initPaint()
    }

    constructor(context: Context) : this(context, null)

    private fun parseAttrs(attributes: AttributeSet) {

        val typedArray = context.obtainStyledAttributes(attributes, R.styleable.NumberRunningView)
        min = typedArray.getInt(R.styleable.NumberRunningView_min, min)
        max = typedArray.getInt(R.styleable.NumberRunningView_max, max)
        maxSpeed = typedArray.getFloat(R.styleable.NumberRunningView_maxSpeed, maxSpeed)
        numberColor = typedArray.getColor(R.styleable.NumberRunningView_numberColor, numberColor)
        numberSize = typedArray.getDimension(R.styleable.NumberRunningView_numberSize, numberSize)
        speedOffset = typedArray.getFloat(R.styleable.NumberRunningView_speedOffset, 0.1f)
        typedArray.recycle()
        now = min
    }

    private fun initPaint() {
        paint.textSize = numberSize
        paint.color = numberColor
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawNow(it)
            drawNext(it)
            calCurSpeed()
            calOffset()
        }
    }

    private fun calCurSpeed() {
        curSpeed += speedOffset
        if (curSpeed > maxSpeed) curSpeed = maxSpeed
    }

    private fun drawNow(canvas: Canvas) {
        val curDataList = dataList
        var nowNum = "0"
        nowNum = if (curDataList != null) {
            curDataList[now].toString()
        } else {
            now.toString()
        }
        val numWidth = paint.measureText(nowNum)
        canvas.drawText(nowNum, width / 2 - numWidth / 2, height / 2 - offset + paint.textSize / 2, paint)
    }


    private fun drawNext(canvas: Canvas) {
        val curDataList = dataList
        var nextNum = ""
        if (curDataList == null) {
            nextNum = if (now + 1 > max) {
                min.toString()
            } else {
                (now + 1).toString()
            }

        } else {
            nextNum = if (now + 1 > max) {
                curDataList[min].toString()
            } else {
                (curDataList[now + 1]).toString()
            }
        }
        val numWidth = paint.measureText(nextNum)
        canvas.drawText(nextNum, width / 2 - numWidth / 2, 1.5f * height - offset + paint.textSize / 2, paint)
    }

    private fun calOffset() {
        if (isStart) {
            if (offset == height) {
                offset = 0
                if (now + 1 > max) {
                    now = min
                } else {
                    now += 1
                }
            } else if (offset + curSpeed > height) {
                offset = height
            } else {
                offset = (offset + curSpeed).toInt()
            }
            postInvalidate()
        } else {
            if (offset != 0 && offset != height) {
                offset = if (offset + curSpeed > height) {
                    height
                } else {
                    (offset + curSpeed).toInt()
                }
                postInvalidate()
            } else {
                if (offset == 0) {
                    val curDataList = dataList
                    if (curDataList != null) {
                        onNumberSelectedListener?.onNumberSelected(curDataList[now])
                    } else {
                        onNumberSelectedListener?.onNumberSelected(now)
                    }
                } else {
                    val curDataList = dataList
                    if (curDataList != null) {
                        onNumberSelectedListener?.onNumberSelected(if (now == max) curDataList[min] else curDataList[now + 1])
                    } else {
                        onNumberSelectedListener?.onNumberSelected(if (now == max) min else now + 1)
                    }
                }
            }
        }
    }

    fun start() {
        if (isStart) {
            return
        }
        curSpeed = 0f
        isStart = true
        if (ViewCompat.isAttachedToWindow(this)) {
            postInvalidate()
        }
    }

    fun stop() {
        isStart = false
    }

    interface OnNumberSelectedListenern {

        fun onNumberSelected(num: Int)
    }
}
package com.example.drawing_app

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView (context: Context, attrs: AttributeSet) : View (context, attrs) {
    private var mDrawPath : CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
//    private var mPaths = ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND

        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    /**
     *   This method is called when a stroke is drawn on canvas
     *   as a part of the painting.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
            canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

//        for (p in mPaths) {
//            mDrawPaint?.strokeWidth = p.brushThickness
//            mDrawPaint?.color = p.color
//            canvas.drawPath(p, mDrawPaint!!)
//        }

        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                mDrawPath!!.moveTo(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.lineTo(event.x, event.y)
            }

            MotionEvent.ACTION_UP -> {
//                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }

        invalidate()

        return true
    }

    internal inner class CustomPath (var color: Int, var brushThickness: Float) : Path() {}
}
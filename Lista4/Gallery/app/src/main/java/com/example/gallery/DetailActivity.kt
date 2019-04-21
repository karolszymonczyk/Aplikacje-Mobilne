package com.example.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import kotlinx.android.synthetic.main.info_fragment.*
import kotlinx.android.synthetic.main.photo_fragment.*
import android.content.Context.WINDOW_SERVICE
import android.net.Uri
import android.support.v4.content.ContextCompat.getSystemService
import android.view.WindowManager

class DetailActivity : AppCompatActivity() {

    private var currRating: Float = 0f
    private var oldRate: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        val item: Photo = intent.getParcelableExtra("Item") as Photo
        val screenHeight = getScreenHeight()

        currRating = item.rating
        ivPhoto.setImageURI(Uri.parse(item.image))
        ivPhoto.layoutParams.height = (screenHeight.toDouble()).toInt()
        ivPhoto.requestLayout()
        tvDesc.text = item.desc
        tvRating.text = item.rating.toString()
        ratingBar.rating = item.rate
        oldRate = item.rate
    }

    private fun calcRating(rate: Float, counter: Int) {
        val sum = currRating * counter
        currRating = (sum + rate) / (counter + 1)
    }

    private fun undoRate(rate: Float, counter: Int) {
        val sum = currRating * counter - oldRate
        if (counter - 1 == 0) {
            currRating = 0f
            return
        } else {
            currRating = sum / (counter - 1)
        }
        currRating = sum / (counter -1)
    }

    private fun changeRate(rate: Float, counter: Int) {
        undoRate(rate, counter)
        calcRating(rate, counter - 1)
    }

    override fun onBackPressed() {
        val rate = ratingBar.rating

        when {
            oldRate == 0f && rate != 0f -> setOutput(rate, changed = false, undo = false)
            rate != oldRate && rate != 0f -> setOutput(rate, changed = true, undo = false)
            rate != oldRate && rate == 0f -> setOutput(rate, changed = true, undo = true)
            else -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
        super.onBackPressed()
    }

    private fun setOutput(rate: Float, changed: Boolean, undo: Boolean) {
        val output = Intent()
        output.putExtra("Position", intent.getIntExtra("Position", 0))
        output.putExtra("Rate", rate)
        val counter = intent.getIntExtra("Counter", 1)
        if (changed) {
            if (undo) {
                undoRate(rate, counter)
                output.putExtra("Counter", counter - 1)
            } else {
                changeRate(rate, counter)
                output.putExtra("Counter", counter)
            }
        } else {
            calcRating(rate, counter)
            output.putExtra("Counter", counter + 1)
        }
        output.putExtra("Rating", currRating)
        setResult(1, output)
        finish()
    }

    private fun getScreenHeight(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}
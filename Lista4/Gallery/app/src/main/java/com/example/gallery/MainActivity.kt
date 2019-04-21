package com.example.gallery

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    private val lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."

    private var photos = ArrayList<Photo>()
    private val PICK_RATE = 1
    private val ADD_PHOTO = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            photos = savedInstanceState.getParcelableArrayList("Photos")
        } else {
            photos.add(Photo(lorem, getUriString(R.drawable.pic1), 0f, 1, 3.5f))
            photos.add(Photo(lorem, getUriString(R.drawable.pic2), 0f, 5, 4.75f))
            photos.add(Photo(lorem, getUriString(R.drawable.pic3), 0f, 1, 5.0f))
            photos.add(Photo(lorem, getUriString(R.drawable.pic4), 0f, 0, 0f)) //4.35 i 7
            photos.add(Photo(lorem, getUriString(R.drawable.pic5), 0f, 3, 3.55f))
            photos.add(Photo(lorem, getUriString(R.drawable.pic6), 0f, 4, 3.4f))
            photos.add(Photo(lorem, getUriString(R.drawable.pic7), 0f, 3, 4.2f))
            photos.add(Photo(lorem, getUriString(R.drawable.pic8), 0f, 8, 4.25f))
            photos.add(Photo(lorem, getUriString(R.drawable.pic9), 0f, 2, 4.5f))
            photos.add(Photo(lorem, getUriString(R.drawable.pic10), 0f, 5, 3.75f))
            photos.add(Photo(lorem, getUriString(R.drawable.pic11), 0f, 5, 3.2f))
        }

        sortPhotos()

        recycler_view.layoutManager =
            GridLayoutManager(this, getColumns()) //seting number of columns to adjust recyclerView to the screen

        recycler_view.adapter = MyAdapter(photos, this) { position: Int ->
            val intent = Intent(this, DetailActivity::class.java)
            val item = photos[position]
            with(intent) {
                putExtra("Item", item)
                putExtra("Position", position)
                putExtra("Counter", item.counter)
            }
            startActivityForResult(intent, PICK_RATE)
        }
    }

    private fun getColumns(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        return when {
            width < 600 -> 1
            width < 1200 -> 2
            width < 2000 -> 3
            width < 3000 -> 4
            else -> 5
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            PICK_RATE -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    return
                }
                val position = data!!.getIntExtra("Position", 0)
                val rate = data.getFloatExtra("Rate", 0f)
                val rating = data.getFloatExtra("Rating", 0f)
                val counter = data.getIntExtra("Counter", 1)

                val item = photos[position]
                item.rate = rate
                item.rating = rating
                item.counter = counter

                sortPhotos()
            }
            ADD_PHOTO -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    return
                }
                val photo = data!!.getStringExtra("Photo")
                val desc = data.getStringExtra("Desc")
                photos.add(Photo(desc, photo, 0f, 0, 0f))
                Toast.makeText(this, "New photo added!", Toast.LENGTH_SHORT).show()
                sortPhotos()
            }
            else -> {
                Toast.makeText(this, "Unrecognised request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sortPhotos() {
        photos.sortByDescending { it.rating }
        recycler_view.adapter?.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("Photos", photos)
        super.onSaveInstanceState(outState)
    }

    fun bAddClick(view: View) {
        val intent = Intent(this, AddPhotoActivity::class.java)
        startActivityForResult(intent, ADD_PHOTO)
    }

    private fun getUriString(id: Int): String {
        return Uri.parse("android.resource://" + this.packageName + "/" + id).toString()
    }
}

package com.example.gallery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.description_fragment.*
import kotlinx.android.synthetic.main.photo_fragment.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddPhotoActivity : AppCompatActivity() {

    private val PICK_PHOTO = 3
    private val REQUEST_WRITE_EXTERNAL = 4
    private var isPicked = false
    private var photoPath = Uri.parse("android.resource://com.example.gallery/" + R.drawable.ic_image).toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_photo)

        if (savedInstanceState != null) {
            isPicked = savedInstanceState.getBoolean("isPicked")
            photoPath = savedInstanceState.getString("Photo")
            etDesc.setText(savedInstanceState.getString("Desc"))
            ivPhoto.setImageURI(Uri.parse(photoPath))
        }

        if (!isPicked) {
            takePhoto()
        }

        bApply.isEnabled = isPicked

        val screenHeight = getScreenHeight()
        ivPhoto.layoutParams.height = (screenHeight.toDouble()).toInt()
    }

    fun bTakePhotoClick(view: View) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL
            )
        } else {
            takePhoto()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_WRITE_EXTERNAL) takePhoto()
    }

    private fun takePhoto() {
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (callCameraIntent.resolveActivity(packageManager) != null) {

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
            }

            if (photoFile != null) {
                val photoUri = FileProvider.getUriForFile(
                    this,
                    "com.example.android.fileprovider",
                    photoFile
                )
                callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(callCameraIntent, PICK_PHOTO)
            }
        }
    }

    fun bApply(view: View) {
        val output = Intent()
        output.putExtra("Photo", ivPhoto.tag.toString())
        var desc = ""
        if (etDesc.text.toString() != "") {
            desc = etDesc.text.toString()
        }
        output.putExtra("Desc", desc)
        setResult(1, output)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_PHOTO -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ivPhoto.setImageURI(Uri.parse(photoPath))
                    ivPhoto.tag = Uri.parse(photoPath).toString()
                    bApply.isEnabled = true
                    addToGallery()
                    isPicked = true
                } else {
                    photoPath = Uri.parse("android.resource://com.example.gallery/" + R.drawable.ic_image).toString()
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognised request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addToGallery() {
        val mediaScan = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val file = File(photoPath)
        val uri = Uri.fromFile(file)
        mediaScan.data = uri
        this.sendBroadcast(mediaScan)
    }

    private fun getScreenHeight(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isPicked", isPicked)
        outState.putString("Photo", photoPath)
        outState.putString("Desc", etDesc.text.toString())
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File? {

        val fileName: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/GalleryPhotos")
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        val image = File.createTempFile(
            "Pic_$fileName",
            ".jpg",
            storageDir
        )

        photoPath = image.absolutePath
        return image
    }
}
package com.example.shop.utility

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.fragment.app.FragmentActivity
import java.io.ByteArrayOutputStream

class ImageConverter{


    companion object{

        /**
         * To encode the image into base64
         */
        fun encode(imageUri: Uri, activity:Activity): String {
            val input = activity?.contentResolver?.openInputStream(imageUri)
            val image = BitmapFactory.decodeStream(input, null, null)

            // Encode image to base64 string
            val baos = ByteArrayOutputStream()
            image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            var imageBytes = baos.toByteArray()
            val imageString = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
            return imageString
        }


        /**
         * To decode the image from base64 into bitmap
         */
        fun decode(imageString: String): Bitmap {
            // Decode base64 string to image
            val imageBytes = Base64.decode(imageString, Base64.NO_WRAP)
           return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

    }
}
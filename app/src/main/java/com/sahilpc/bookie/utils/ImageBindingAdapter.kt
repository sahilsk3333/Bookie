package com.sahilpc.bookie.utils

import android.graphics.Bitmap
import android.widget.ImageView

import androidx.databinding.BindingAdapter


@BindingAdapter("bind:imageBitmap")
fun loadImage(iv: ImageView, bitmap: Bitmap?) {
    iv.setImageBitmap(bitmap)
}
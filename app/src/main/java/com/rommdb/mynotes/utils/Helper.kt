package com.rommdb.mynotes.utils

import android.content.Context
import android.widget.Toast

// Extention function
fun Context.toast(msg: String) =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
package com.m391.quiz.database.remote

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class MediaStorage {
    private val storageRef = FirebaseStorage.getInstance()
    suspend fun uploadImage(uri: String, userType: String): Pair<String, Uri> =
        withContext(Dispatchers.IO) {
            val path = "${System.currentTimeMillis()}${UUID.randomUUID()}"
            val process = storageRef.getReference(userType)
                .child(path)
                .putFile(uri.toUri()).asDeferred().await()
            val imageUri = process.metadata!!.reference!!.downloadUrl.await()
            return@withContext Pair(first = path, second = imageUri)
        }

    suspend fun uploadImageByteArray(image: ByteArray?, reference: String): Pair<String?, String?> =
        withContext(Dispatchers.IO) {
            if (image == null) return@withContext Pair(null, null)
            val path = "${System.currentTimeMillis()}${UUID.randomUUID()}"
            val process = storageRef.getReference(reference)
                .child(path)
                .putBytes(image).asDeferred().await()
            val imageUri = process.metadata!!.reference!!.downloadUrl.await()
            return@withContext Pair(first = path, second = imageUri.toString())
        }
}
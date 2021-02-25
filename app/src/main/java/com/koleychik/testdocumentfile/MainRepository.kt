package com.koleychik.testdocumentfile

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

typealias ImagesStorage = MediaStore.Images.Media


class MainRepository(private val context: Context) {

    fun getImages(): List<ImageModel> {
        val uriExternal: Uri = ImagesStorage.EXTERNAL_CONTENT_URI
        val listRes = mutableListOf<ImageModel>()
        val sorterOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = queryContentResolver(
            uriExternal,
            imagesProjections,
            sortedOrder = sorterOrder
        ) ?: return emptyList()

        var id: Long
        while (cursor.moveToNext()) {
            id = cursor.getLong(0)
            listRes.add(
                ImageModel(
                    id = id,
                    name = cursor.getString(1),
                    uri = Uri.withAppendedPath(uriExternal, id.toString()),
                    sizeAbbreviation = cursor.getString(2),
                    dateAdded = cursor.getLong(3)
                )
            )
        }
        cursor.close()
        return listRes
    }


    private fun queryContentResolver(
        uri: Uri,
        projections: Array<out String>,
        selection: String? = null,
        selectionArgs: Array<out String>? = null,
        sortedOrder: String? = null
    ) = context.contentResolver.query(
        uri,
        projections,
        selection,
        selectionArgs,
        sortedOrder
    )


}

data class ImageModel(
    val id: Long,
    val name: String,
    val uri: Uri,
    val sizeAbbreviation: String,
    val dateAdded: Long?
)

val imagesProjections = arrayOf(
    ImagesStorage._ID,
    ImagesStorage.DISPLAY_NAME,
    ImagesStorage.SIZE,
    ImagesStorage.DATE_ADDED
)



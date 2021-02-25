package com.koleychik.testdocumentfile

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import coil.load
import java.io.FileInputStream

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var list: List<ImageModel> = emptyList()

    fun setList(newList: List<ImageModel>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
    )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: ImageModel) {
            val image = itemView.findViewById<ImageView>(R.id.image)
            val context = itemView.context.applicationContext
            image.run {
                load(model.uri)
                itemView.setOnClickListener {
                    delete(context, DocumentFile.fromSingleUri(context, model.uri)!!)
                }
            }
        }

        private fun delete(context: Context, file: DocumentFile) {

            val testFile = FileInputStream(file.name)

            testFile.close()
            if (context.deleteFile(file.uri.path)) Log.d(TAG, "file was deleted")
            else Log.d(TAG, "file was not deleted")

            if (file.canWrite()) Log.d(TAG, "File can be written")
            else Log.d(TAG, "File cannot be written")

            if (file.exists()) {
                if (file.delete()) {
                    Log.d(TAG, "File was deleted")
                } else {
                    Log.d(TAG, "File was not deleted")
                }
            } else Log.d(TAG, "File doesn't exists")
        }

    }

}
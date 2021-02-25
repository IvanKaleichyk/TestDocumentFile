package com.koleychik.testdocumentfile

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

const val TAG = "MAIN_APP_TAG"

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }

    private fun start() {
        Log.d(TAG, "start")


        val repository = MainRepository(applicationContext)
        val list = repository.getImages()

        val file: DocumentFile? = DocumentFile.fromSingleUri(applicationContext, list[0].uri)

        if (file == null) Log.d(TAG, "File == null")

        Log.d(TAG, "file.type = ${file?.type}")
        Log.d(TAG, "file.length = ${file?.length()}")
        Log.d(TAG, "file.name = ${file?.name}")
        Log.d(TAG, "documentType = ${list[0].type}")
//        Log.d(TAG, "documentType = ${list[0].type}")


        val rootFolder = File("storage/emulated/0")
        val mainFile = DocumentFile.fromFile(rootFolder)
        val childrenFiles = mainFile.listFiles()
        if (childrenFiles.isEmpty()) Log.d(TAG, "childrenList is Empty")
        for (i in childrenFiles) {
            Log.d(TAG, "------------------------------")
            Log.d(TAG, "childrenFile.type = ${i.type}")
            Log.d(TAG, "childrenFile.name = ${i.name}")
        }

        val rootList = rootFolder.listFiles()
        Log.d(TAG, "rootList.size = ${rootList.size}")

    }

    @AfterPermissionGranted(123)
    private fun checkPermission() {
        Log.d(TAG, "MainActivity start checkPermission")
        if (EasyPermissions.hasPermissions(applicationContext, *permissions)) start()
        else {
            EasyPermissions.requestPermissions(
                    this,
                    applicationContext.getString(R.string.cannot_without_permissions),
                    123,
                    *permissions
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult")
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "onPermissionsGranted")
        start()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(applicationContext, R.string.cannot_without_permissions, Toast.LENGTH_LONG)
                .show()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Log.d(TAG, "onActivityResult")
        }
    }

}
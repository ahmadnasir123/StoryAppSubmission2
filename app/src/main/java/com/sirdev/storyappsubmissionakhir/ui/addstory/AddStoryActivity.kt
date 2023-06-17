package com.sirdev.storyappsubmissionakhir.ui.addstory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.sirdev.storyappsubmissionakhir.data.Utils.getImageUriFromBitmap
import com.sirdev.storyappsubmissionakhir.data.Utils.reduceFileImage
import com.sirdev.storyappsubmissionakhir.data.Utils.rotateBitmap
import com.sirdev.storyappsubmissionakhir.data.Utils.uriToFile
import com.sirdev.storyappsubmissionakhir.data.preferences.UserStoryPreferences
import com.sirdev.storyappsubmissionakhir.databinding.ActivityAddStoryBinding
import com.sirdev.storyappsubmissionakhir.ui.ViewModelFactory
import com.sirdev.storyappsubmissionakhir.ui.addstory.AddStoryViewModel
import com.sirdev.storyappsubmissionakhir.ui.kamera.CameraActivity
import com.sirdev.storyappsubmissionakhir.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addNewViewModel: AddStoryViewModel
    private lateinit var userToken: String

    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak memiliki izin.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("kameraBelakang", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            val resultUri = getImageUriFromBitmap(this,result)
            getFile = uriToFile(resultUri, this)

            binding.thumbnailIv.setImageBitmap(result)
        }
    }

    private val launcherIntentGalery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val galeriFile = uriToFile(selectedImg, this)
            getFile = galeriFile

            binding.thumbnailIv.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        addNewViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserStoryPreferences.getInstance(dataStore))
        )[AddStoryViewModel::class.java]

        addNewViewModel.isLoading.observe(this){ loading ->
            showLoading(loading)
        }

        binding.cameraBtn.setOnClickListener { startCamera() }
        binding.galleryBtn.setOnClickListener { startGalery() }
        addNewViewModel.dataState().observe(this) {
            userToken = it
        }
        binding.btnUpload.setOnClickListener { uploadStory(getFile, userToken) }
        addNewViewModel.messageError.observe(this){message->
            toastMessage(message)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

    private fun uploadStory(file: File?, token: String) {
        if (file != null) {

            val imageFile = reduceFileImage(file)
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            val desc = binding.descriptionEdt.text.toString()
            val description = desc.toRequestBody("text/plain".toMediaType())
            if (desc.isEmpty()){
                toastMessage("Deskripsi tidak boleh kosong")
            }else {
                addNewViewModel.postNewStory(token, imageMultipart, description)
            }
        }
    }

    private fun startGalery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherIntentGalery.launch(chooser)
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLoadingScreen.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
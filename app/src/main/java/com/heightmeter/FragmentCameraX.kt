@file:Suppress("OVERRIDE_DEPRECATION")

package com.heightmeter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.RectF
import android.media.MediaActionSound
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity  // для скрытия APPBAR
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.heightmeter.databinding.FragmentCameraXBinding
import kotlinx.android.synthetic.main.fragment_camera_x.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Suppress("DEPRECATION")

class FragmentCameraX : Fragment() {

    lateinit var binding: FragmentCameraXBinding

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private var cameraControl: CameraControl? = null   // нужна ли?
    private var cameraInfo: CameraInfo? = null

    private lateinit var safeContext: Context
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    var rectSize = 100 // размер прямоугольника автофокуса

    //Только для скрытия системной панели
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //Скрытие системной панели
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

// Hide status bar
//      requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Show status bar
//      requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

        binding = FragmentCameraXBinding.inflate(inflater)

//return inflater.inflate(R.layout.fragment_camera_x, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        // Setup the listener for take photo button
        binding.cameraCaptureButton.setOnClickListener { takePhoto() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
//        cameraExecutor = Executors.newCachedThreadPool()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder().build()

//  сюда добавляются допы

            imageCapture = ImageCapture.Builder().build()

            // Выбор задней камеры
//            val cameraSelector =
//                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Отвязка всех привязок камеры
                cameraProvider.unbindAll()

                // Bind use cases to camera

                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                preview?.setSurfaceProvider(viewFinder.surfaceProvider)

                cameraControl = camera?.cameraControl
                cameraInfo = camera?.cameraInfo


                ////////
                //listener тапа по экрану
                viewFinder.setOnTouchListener { _, event ->
                    if (event.action != MotionEvent.ACTION_UP) {
                        return@setOnTouchListener true
                    }

                    //val factory = viewFinder.getMeteringPointFactory()
                    val factory = viewFinder.meteringPointFactory
                    val point = factory.createPoint(event.x, event.y)
                    val action = FocusMeteringAction.Builder(point).build()

                    cameraControl?.startFocusAndMetering(action)
                    Toast.makeText(safeContext, "onTouch", Toast.LENGTH_SHORT).show()
//
                    val focusRects = listOf(
                        RectF(
                            event.x - rectSize,
                            event.y - rectSize,
                            event.x + rectSize,
                            event.y + rectSize
                        )
                    )

 //                   overlay_info.post { overlay_info.drawRectBounds(focusRects) }

//                    rect_overlay.post { rect_overlay.drawRectBounds(focusRects) }

                    overlay_rect_af.post { overlay_rect_af.drawRectBounds(focusRects) }
// OverlayRectAF
                    Log.e(TAG, "Focus Coordinates: " + event.x + " , " + event.y)
                    Log.e(TAG, "preview view dimensions: " + viewFinder.width + " x " + viewFinder.height)

                    return@setOnTouchListener true
                }
                //////// Конец listener тапа по экрану
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed. Cбой подключения камеры", exc)
            }
//            cameraControl = camera?.cameraControl
//            cameraInfo = camera?.cameraInfo

        }, ContextCompat.getMainExecutor(safeContext))

    }


    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create timestamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Setup image capture listener which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(safeContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(safeContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            })
        val sound = MediaActionSound()
        sound.play(MediaActionSound.SHUTTER_CLICK)

    }


    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar!!.hide()  // для скрытия APPBAR
    }

    override fun onStop() {
        (activity as AppCompatActivity).supportActionBar!!.show()  // для показа APPBAR
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        isOffline = true
        // Show status bar
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()  // для скрытия APPBAR
        isOffline = false
    }

    override fun onDestroyView() {
        //_fragmentCameraBinding = null
        super.onDestroyView()

        // Shut down our background executor
        cameraExecutor.shutdown()

        // Unregister the broadcast receivers and listeners
        // broadcastManager.unregisterReceiver(volumeDownReceiver)
        // displayManager.unregisterDisplayListener(displayListener)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    safeContext,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
//                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity?.filesDir!!
    }

//    // нужна ли эта устаревшая ф-я ?
//    private fun getStatusBarHeight(): Int {
//        val resourceId =
//            safeContext.resources.getIdentifier("status_bar_height", "dimen", "android")
//        return if (resourceId > 0) {
//            safeContext.resources.getDimensionPixelSize(resourceId)
//        } else 0
//    }

    companion object {
        const val TAG = "FragmentCameraX"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        internal const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        var isOffline = false // prevent app crash when goes offline
    }

}



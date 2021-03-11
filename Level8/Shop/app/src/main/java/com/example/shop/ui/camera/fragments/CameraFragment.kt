package com.example.shop.ui.camera.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akexorcist.example.camerax.helper.ShortenMultiplePermissionListener
import com.akexorcist.example.camerax.helper.ShortenSeekBarChangeListener
import com.akexorcist.example.camerax.helper.applyWindowInserts
import com.example.shop.R
import com.example.shop.databinding.FragmentCameraBinding
import com.example.shop.ui.camera.adapter.ImageAdapter
import com.example.shop.ui.main.viewModel.AdvertisementViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import okio.FileNotFoundException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {
    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()

    private val fileName = "labels.txt"
    private lateinit var labelsString: String
    private val REQUEST_PERMISSION_CODE_GALLERY = 100
    private val RESULT_CODE = 1002

    private lateinit var imageAdapter: ImageAdapter

    private lateinit var binding: FragmentCameraBinding

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var flashMode = ImageCapture.FLASH_MODE_OFF
    private var isTorchModeEnabled = false

    private var camera: Camera? = null
    private var imageCapture: ImageCapture? = null
    private var cameraProvider: ProcessCameraProvider? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        showButtonAndAmount()

    }

    private fun init() {

        binding.viewSafeArea.applyWindowInserts()
        binding.buttonCaptureImage.setOnClickListener { captureImage() }
        binding.buttonToggleCamera.setOnClickListener { switchCamera() }
        binding.buttonToggleFlash.setOnClickListener { changeFlashMode() }
//        binding.buttonToggleTorch.setOnClickListener { changeTorchMode() }
        binding.previewView.setOnTouchListener(onPreviewTouchListener)
        binding.seekBarZoom.setOnSeekBarChangeListener(onSeekBarChangeListener)

        imageAdapter = ImageAdapter(advertisementViewModel.bitmapList.value!!, ::onDelete)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.imgCnt.layoutManager = layoutManager
        binding.imgCnt.adapter = imageAdapter

        labelsString = requireActivity().application.assets.open(fileName).bufferedReader()
            .use { it.readText() }

        requestRuntimePermission()
        orientationEventListener.enable()

        binding.galleryBtn.setOnClickListener {
            onGalleryClick()
        }

        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_cameraFragment_to_addItemFragment)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        orientationEventListener.disable()
    }


    /**
     * Recognise an image based on the given byteArray
     */
//    private fun recogniseImage(data: ByteArray) {
//        var  imageBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
//        imageBitmap = rotateImage(imageBitmap, 90F)
//        //set preview for the user
//        previewCapturedImage(imageBitmap)
//
//        val resizedBitmap: Bitmap = Bitmap.createScaledBitmap(imageBitmap, 224, 224, true)
//        val model = ModelTF.newInstance(requireContext())
//        val tbuffer = TensorImage.fromBitmap(resizedBitmap)
//        val byteBuffer = tbuffer.buffer
//        // Creates inputs for reference.
//        val inputFeature0 =
//            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
//        inputFeature0.loadBuffer(byteBuffer)
//
//        // Runs model inference and gets result.
//        val outputs = model.process(inputFeature0)
//        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//        val townList = inputString.split("\n")
//        val max = getMax(outputFeature0.floatArray)
//
////        binding.textView14.text = townList[max]
//        // Releases model resources if no longer used.
//        model.close()
//    }


    /**
     * To get the max floating index, which is the index of the label
     */
    private fun getMax(array: FloatArray): Int {
        var index = 0;
        var min = 0.0f
        for (i in array.indices) {
            if (array[i] > min) {
                index = i
                min = array[i]
            }
        }
        return index
    }


    /**
     * To show the the add button and the amount of taken images
     */
    private fun showButtonAndAmount() {
        binding.amountImg.isVisible = advertisementViewModel.bitmapList.value!!.size > 0
        binding.addBtn.isVisible = advertisementViewModel.bitmapList.value!!.size > 0
        binding.imgCnt.isVisible = advertisementViewModel.bitmapList.value!!.size > 0
        binding.amountImg.text = getString(
            R.string.amount,
            advertisementViewModel.bitmapList.value!!.size
        )
    }

    /**
     * Request permission for using the camera
     */
    private fun requestRuntimePermission() {
        Dexter.withContext(requireContext())
            .withPermissions(Manifest.permission.CAMERA)
            .withListener(multiplePermissionsListener)
            .check()
    }

    /**
     * get the camera
     */
    private fun setupCameraProvider() {
        ProcessCameraProvider.getInstance(requireContext()).also { provider ->
            provider.addListener({
                cameraProvider = provider.get()
                bindCamera()
            }, ContextCompat.getMainExecutor(requireContext()))
        }
    }

    /**
     * Add thecamera in the preview surface
     */
    private fun bindCamera() {
        val preview: Preview = createPreview()
        val cameraSelector = createCameraSelector()
        imageCapture = createCameraCapture()
        camera = cameraProvider?.bindToLifecycle(
            requireActivity(),
            cameraSelector,
            preview,
            imageCapture
        )
        camera?.let { camera ->
            preview.setSurfaceProvider(binding.previewView.surfaceProvider)
            setupCameraSetting(camera)
        }
    }

    private fun setupCameraSetting(camera: Camera) {
        camera.cameraInfo.zoomState.observe(requireActivity(), zoomStateObserver)
        changeZoomLevel(0f)
        updateFlashAvailable(camera.cameraInfo.hasFlashUnit())
//        updateTorchAvailable(camera.cameraInfo.hasFlashUnit())
    }

    private fun updateFlashAvailable(isEnabled: Boolean) {
        binding.buttonToggleFlash.isEnabled = isEnabled
        updateFlashModeButton()
    }

//    private fun updateTorchAvailable(isEnabled: Boolean) {
//        binding.buttonToggleTorch.isEnabled = isEnabled
//        updateTorchModeButton()
//    }

    /**
     * Update rotation of the camera
     */
    private fun updateImageCaptureRotation(orientation: Int) {
        imageCapture?.targetRotation = when (orientation) {
            in 45..134 -> Surface.ROTATION_270
            in 135..224 -> Surface.ROTATION_180
            in 225..314 -> Surface.ROTATION_90
            else -> Surface.ROTATION_0
        }
    }

    private fun unbindCamera() {
        camera?.cameraInfo?.zoomState?.removeObserver(zoomStateObserver)
        cameraProvider?.unbindAll()
    }

    private fun createPreview(): Preview = Preview.Builder()
        .build()

    private fun createCameraCapture(): ImageCapture = ImageCapture.Builder()
        .setFlashMode(flashMode)
        .setTargetResolution(Size(1200, 1200))
        .build()

    private fun createCameraSelector(): CameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    /**
     * To be able switch to front/back camera
     */
    private fun switchCamera() {
        lensFacing = when (lensFacing) {
            CameraSelector.LENS_FACING_BACK -> CameraSelector.LENS_FACING_FRONT
            else -> CameraSelector.LENS_FACING_BACK
        }
        unbindCamera()
        bindCamera()
        binding.seekBarZoom.progress = 0
    }

    /**
     * On click on the flash button
     */
    private fun changeFlashMode() {
        if (camera?.cameraInfo?.hasFlashUnit() == true) {
            flashMode = when (flashMode) {
                ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
                ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
                else -> ImageCapture.FLASH_MODE_OFF
            }
            imageCapture?.flashMode = flashMode
            updateFlashModeButton()
        }
    }

    /**
     * On the zoom level slides
     */
    private fun changeZoomLevel(@FloatRange(from = 0.0, to = 1.0) level: Float) {
        camera?.cameraControl?.setLinearZoom(level)
    }

//    private fun changeTorchMode() {
//        isTorchModeEnabled = !isTorchModeEnabled
//        updateFlashAvailable(!isTorchModeEnabled)
//        camera?.cameraControl?.enableTorch(isTorchModeEnabled)
//        updateTorchModeButton()
//
//    }

    /**
     * Capture the image and save it in the internal storage
     */
    private fun captureImage() {
        val file = File(requireContext().filesDir.absoluteFile, "temp.jpg")
        val outputFileOptions: ImageCapture.OutputFileOptions =
            ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture?.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(requireContext()),
            imageSavedCallback
        )
    }

    /**
     * When clicking on the surface the capture will get focused
     */
    private fun performFocus(x: Float, y: Float) {
        camera?.let { camera ->
            val pointFactory: MeteringPointFactory = binding.previewView.meteringPointFactory
            val afPointWidth = 1.0f / 6.0f
            val aePointWidth = afPointWidth * 1.5f
            val afPoint = pointFactory.createPoint(x, y, afPointWidth)
            val aePoint = pointFactory.createPoint(x, y, aePointWidth)
            val future = camera.cameraControl.startFocusAndMetering(
                FocusMeteringAction.Builder(
                    afPoint,
                    FocusMeteringAction.FLAG_AF
                ).addPoint(
                    aePoint,
                    FocusMeteringAction.FLAG_AE
                ).build()
            )
            future.addListener({}, ContextCompat.getMainExecutor(requireContext()))
        }
    }

    private val multiplePermissionsListener = object : ShortenMultiplePermissionListener() {
        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            if (report.areAllPermissionsGranted()) {
                onPermissionGrant()
            } else {
                onPermissionDenied()
            }
        }
    }

    private val orientationEventListener: OrientationEventListener by lazy {
        object : OrientationEventListener(requireContext()) {
            override fun onOrientationChanged(orientation: Int) {
                updateImageCaptureRotation(orientation)
            }
        }
    }

    private val onPreviewTouchListener = object : View.OnTouchListener {
        private var actionDownTimestamp: Long = 0
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) {
                actionDownTimestamp = System.currentTimeMillis()
            } else if (event.action == MotionEvent.ACTION_UP && isInLongPressDuration()) {
                view.performClick()
                performFocus(event.x, event.y)
            }
            return true
        }

        private fun isInLongPressDuration(): Boolean =
            System.currentTimeMillis() - actionDownTimestamp < ViewConfiguration.getLongPressTimeout()
    }

    private val onSeekBarChangeListener = object : ShortenSeekBarChangeListener() {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            changeZoomLevel(progress / 100f)
        }
    }

    private val zoomStateObserver = Observer { state: ZoomState ->
        binding.textViewZoomLevel.text =
            getString(com.example.shop.R.string.zoom_ratio, state.zoomRatio)
    }

    /**
     * callback for the saved image
     */
    private val imageSavedCallback = object : ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            confirmCapturing()
            showResultMessage(getString(com.example.shop.R.string.image_capture_success))
        }

        override fun onError(exception: ImageCaptureException) {
            showResultMessage(
                getString(
                    com.example.shop.R.string.image_capture_error,
                    exception.message,
                    exception.imageCaptureError
                )
            )
        }
    }

    private fun onPermissionGrant() {
        setupCameraProvider()
    }

    private fun onPermissionDenied() {
        showResultMessage(getString(com.example.shop.R.string.permission_denied))
    }

    /**
     * To update the flash mode button on click
     */
    private fun updateFlashModeButton() {
        val mode = getString(
            when (flashMode) {
                ImageCapture.FLASH_MODE_ON -> com.example.shop.R.string.flash_on
                ImageCapture.FLASH_MODE_AUTO -> com.example.shop.R.string.flash_auto
                else -> com.example.shop.R.string.flash_off
            }
        )
        val drawable = ContextCompat.getDrawable(
            requireContext(),
            when (flashMode) {
                ImageCapture.FLASH_MODE_ON -> R.drawable.ic_flash_on
                ImageCapture.FLASH_MODE_AUTO -> R.drawable.ic_flash_auto
                else -> R.drawable.ic_flash_off
            }
        )
//        binding.buttonToggleFlash.text = mode
        binding.buttonToggleFlash.setCompoundDrawablesWithIntrinsicBounds(
            null,
            drawable,
            null,
            null
        )
    }

//    /**
//     * To update the button on click
//     */
//    private fun updateTorchModeButton() {
//        val mode = getString(
//            when (isTorchModeEnabled) {
//                true -> com.example.shop.R.string.torch_on
//                false -> com.example.shop.R.string.torch_off
//            }
//        )
//        val drawable = ContextCompat.getDrawable(
//            requireContext(),
//            when (isTorchModeEnabled) {
//                true -> com.example.shop.R.drawable.ic_torch_on
//                false -> com.example.shop.R.drawable.ic_torch_off
//            }
//        )
//        binding.buttonToggleTorch.text = mode
//        binding.buttonToggleTorch.setCompoundDrawablesWithIntrinsicBounds(
//            null,
//            drawable,
//            null,
//            null
//        )
//    }

    private fun showResultMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    /**
     * When the user clicks on an image, alert dialog will be prompted to confirm the deletion
     */
    private fun onDelete(index: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.sureDelete))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { dialog, id ->
                // Delete selected note from database
                advertisementViewModel.bitmapList.value!!.removeAt(index)
                imageAdapter.notifyDataSetChanged()
                showButtonAndAmount()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    /**
     * To add image to the container of recycler view
     */
    private fun addImageToContainer(scaledBitmap: Bitmap) {
        try {
            advertisementViewModel.bitmapList.value!!.add(scaledBitmap)
            imageAdapter.notifyDataSetChanged()
            showButtonAndAmount()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }


    /**
     * To confirm capturing
     */
    private fun confirmCapturing() {
        showCapturedImageWithConfirmation(true)
        //get the image from the internal storage
        val f = File(requireContext().filesDir.absoluteFile.toString(), "temp.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        //set the image as preview
        binding.preview.setImageBitmap(b)

        binding.clearBtn.setOnClickListener {
            showCapturedImageWithConfirmation(false)
        }
        binding.checkBtn.setOnClickListener {
            showCapturedImageWithConfirmation(false)
            val scaledBitmap: Bitmap = resizeImage(b)
            addImageToContainer(scaledBitmap)
        }
    }

    private fun resizeImage(b: Bitmap): Bitmap {
        val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(b, 250, 200, false)
        return scaledBitmap
    }

    /**
     * to show the captured image with clear/check button
     */
    private fun showCapturedImageWithConfirmation(show: Boolean) {
        binding.clearBtn.isVisible = show
        binding.checkBtn.isVisible = show
        binding.preview.isVisible = show
    }


    /**
     *check the permission first when choosing images from the gallery
     */
    private fun onGalleryClick() {
        requestStoragePermission()
    }

    private fun requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder(requireContext()).setTitle("Permission needed")
                .setMessage("This permission needed to be able to select images from gallery")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE_GALLERY)

                }).setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }.create().show()

        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE_GALLERY)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == REQUEST_PERMISSION_CODE_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickFromGallery()
            }else{
                Toast.makeText(context,"Is not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * To handle the click to get images from the gallery
     */
    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setType("image/*")
        startActivityForResult(intent, RESULT_CODE)
    }


    /**
     * When the user has selected the images, add them to the imageBitmapList
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        extractImagesFromGallery(requestCode, resultCode, data)
    }

    /**
     * To convert each chosen image to bitmap and add it to the bitmaplist
     */
    private fun extractImagesFromGallery(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_CODE  && resultCode == Activity.RESULT_OK) {
            val clipData: ClipData? = data!!.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val imageUri: Uri? = clipData.getItemAt(i).uri
                    try {
                        val inputStream: InputStream? =
                            requireContext().contentResolver.openInputStream(
                                imageUri!!
                            )

                        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                        val resized = resizeImage(bitmap)
                        advertisementViewModel.bitmapList.value!!.add(resized)
                    } catch (e: Exception) {
                    }
                }
            } else {
                val imageUri: Uri? = data.data

                try {
                    val inputStream: InputStream? =
                        requireContext().contentResolver.openInputStream(
                            imageUri!!
                        )
                    val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                    val resized = resizeImage(bitmap)
                    val blob = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob)

//                    recogniseImage(blob.toByteArray())
                    advertisementViewModel.bitmapList.value!!.add(resized)

                } catch (e: Exception) {

                }
            }
            imageAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        bindCamera()
    }

    override fun onPause() {
        super.onPause()
        unbindCamera()
    }
}
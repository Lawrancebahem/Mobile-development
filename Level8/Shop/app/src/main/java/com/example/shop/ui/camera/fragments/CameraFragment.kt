package com.example.shop.ui.camera.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.R
import com.example.shop.ui.camera.adapter.ImageAdapter
import com.example.shop.databinding.FragmentCameraBinding
import com.example.shop.ml.ModelTF
import com.example.shop.ui.main.viewModel.AdvertisementViewModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

import android.graphics.Bitmap








/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {

    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()

    private val REQUEST_CODE_GALLERY = 100
    private var btnCapture: Button? = null
    private lateinit var binding: FragmentCameraBinding
    private var textureView: TextureView? = null
    private val fileName = "labels.txt"
    private lateinit var inputString: String
    private lateinit var capturedImage: ByteArray
    private lateinit var imageAdapter: ImageAdapter

    private var cameraId: String? = null
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSessions: CameraCaptureSession? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var imageDimension: Size? = null

    //Save to FILE
    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null
    private var isSave: Boolean = false

    companion object {
        //Check state orientation of output image
        private val ORIENTATIONS = SparseIntArray(4)
        private const val REQUEST_CAMERA_PERMISSION = 200
        private val paramsImageView = LinearLayout.LayoutParams(300, 300)

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
            paramsImageView.setMargins(0, 0, 1, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        //read the label text file

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.imageView5.setImageResource(R.drawable.test)
        init()
    }


    /**
     * Configure the listeners and navigations
     */
    private fun init() {
        textureView = binding.textureView
        textureView!!.surfaceTextureListener = textureListener

        inputString = requireActivity().application.assets.open(fileName).bufferedReader().use { it.readText() }

        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.addItemFragment)
        }

        //skip and clear the bitmap
        binding.skipBtn.setOnClickListener{
            advertisementViewModel.bitmapList.value?.clear()
            findNavController().navigate(R.id.addItemFragment)
        }

        //capture button
        binding.capture.setOnClickListener {
//            Toast.makeText(context, "is cliced", Toast.)
            takePicture()
        }


        //get images from gallery
        binding.galleryBtn.setOnClickListener {
            onGalleryClick()
        }

        //set adapter
        imageAdapter = ImageAdapter(advertisementViewModel.bitmapList.value!!, ::onDelete)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.imageContainer.layoutManager = layoutManager
        binding.imageContainer.adapter = imageAdapter

        //show the clear/check buttons
        showButtonAndAmount()


        //this means add it to the image container
        binding.checkBtn.setOnClickListener {
            try {
                val bitm:Bitmap = (binding.preview.drawable).toBitmap()
                addImageToContainer(getBytArray(bitm))
                hidePreview()
            }catch (e:Exception){

            }
            toggleCheckClearButtons(false)
        }

        binding.clearBtn.setOnClickListener {
            isSave = false
            toggleCheckClearButtons(false)
            hidePreview()
        }
    }


    /**
     *check the permission first when choosing images from the gallery
     */
    private fun onGalleryClick() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_GALLERY)
            return
        }
        cameraDevice!!.close()
        pickFromGallery()
    }

    /**
     * To handle the click to get images from the gallery
     */
    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setType("image/*")
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
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
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {

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
                        advertisementViewModel.bitmapList.value!!.add(bitmap)
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
                    val blob = ByteArrayOutputStream()
                    bitmap.compress(CompressFormat.PNG, 0 /* Ignored for PNGs */, blob)

//                    recogniseImage(blob.toByteArray())
                    advertisementViewModel.bitmapList.value!!.add(bitmap)

                } catch (e: Exception) {

                }
            }
            imageAdapter.notifyDataSetChanged()
            showButtonAndAmount()
        }
    }


    //To create the camera from the device
    private var stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraDevice.close()
        }

        override fun onError(cameraDevice: CameraDevice, i: Int) {
            var cameraDevice: CameraDevice? = cameraDevice
            cameraDevice!!.close()
            cameraDevice = null
        }
    }

    /**
     * This method gets fired when the user click on capture button,
     * the listener to the onCaptureCompleted, onImageAvailable are handled in this method
     */
    private fun takePicture() {
        if (cameraDevice == null) return
        val manager = requireContext().getSystemService(AppCompatActivity.CAMERA_SERVICE) as CameraManager
        try {
            val characteristics = manager.getCameraCharacteristics(cameraDevice!!.id)
            var jpegSizes: Array<Size>? = null
            if (characteristics != null) jpegSizes = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                    .getOutputSizes(ImageFormat.JPEG)

            //Capture image with custom size
            var width = 640
            var height = 480
            if (jpegSizes != null && jpegSizes.isNotEmpty()) {
                width = jpegSizes[0].width
                height = jpegSizes[0].height
            }
            //read the image with the specified width, height nad format
            val reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
            val outputSurface: MutableList<Surface> = ArrayList(2)
            outputSurface.add(reader.surface)
            //Set the captured image in the texture view
            outputSurface.add(Surface(textureView!!.surfaceTexture))
            val captureBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(reader.surface)
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

            //Check orientation base on device
            val rotation = requireActivity().windowManager.defaultDisplay.rotation
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[rotation])

            //add onImageAvailable listener
            val readerListener: ImageReader.OnImageAvailableListener = onImageAvailableListener(reader)
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler)

            //add capture listener
            val captureListener: CameraCaptureSession.CaptureCallback = captureCallback()

            //confiqure callback
            onConfigureCallback(outputSurface, captureBuilder, captureListener)


        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * To configure the capture session builder, by adding the captureListener and mBackgroundHandler
     */
    private fun onConfigureCallback(outputSurface: MutableList<Surface>, captureBuilder: CaptureRequest.Builder, captureListener: CameraCaptureSession.CaptureCallback) {
        cameraDevice!!.createCaptureSession(
                outputSurface, object : CameraCaptureSession.StateCallback() {

            override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                try {
                    cameraCaptureSession.capture(captureBuilder.build(), captureListener, mBackgroundHandler)

                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
            }

            override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {}
        },
                mBackgroundHandler
        )
    }

    /**
     * This is a listener to the image is availability
     */
    private fun onImageAvailableListener(reader: ImageReader): ImageReader.OnImageAvailableListener {
        val readerListener: ImageReader.OnImageAvailableListener = object :
                ImageReader.OnImageAvailableListener {
            //When the image is available recognise it and assign the bytes to capturedImage
            override fun onImageAvailable(imageReader: ImageReader) {
                var image: Image? = null
                try {
                    image = reader.acquireLatestImage()
                    val buffer = image.planes[0].buffer
                    val bytes = ByteArray(buffer.capacity())
                    buffer[bytes]
                    //execute the recognising onto the main thread
                    requireActivity().runOnUiThread {
                        recogniseImage(bytes)
                        //set preview for the user
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    run { image?.close() }
                }
            }
        }
        return readerListener
    }

    /**
     * To give a callback when the image is captured
     */
    private fun captureCallback(): CameraCaptureSession.CaptureCallback {
        val captureListener: CameraCaptureSession.CaptureCallback = object : CameraCaptureSession.CaptureCallback() {
            override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                super.onCaptureCompleted(session, request, result)
                createCameraPreview()
            }
        }
        return captureListener
    }

    /**
     * Set the preview in the texture
     */
    private fun createCameraPreview() {

        try {
            val texture = textureView!!.surfaceTexture!!
            texture.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)
            val surface = Surface(texture)
            captureRequestBuilder =
                    cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder!!.addTarget(surface)

            cameraDevice!!.createCaptureSession(listOf(surface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            if (cameraDevice == null) return
                            cameraCaptureSessions = cameraCaptureSession
                            updatePreview()
                        }

                        override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                            Toast.makeText(requireContext(), "Changed", Toast.LENGTH_SHORT).show()
                        }
                    },
                    null
            )

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * This to update the preview once an image has been captured
     */
    private fun updatePreview() {
        if (cameraDevice == null) Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT)
                .show()
        captureRequestBuilder!!.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)
        try {
            cameraCaptureSessions!!.setRepeatingRequest(
                    captureRequestBuilder!!.build(),
                    null,
                    mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * get the camera from the manager and open it
     */
    fun openCamera() {
//        transformImage()
        val manager = requireContext().getSystemService(AppCompatActivity.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = manager.cameraIdList[0]
            val characteristics = manager.getCameraCharacteristics(cameraId!!)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
            imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]
            //Check realtime permission if run higher API 23
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                return
            }
            manager.openCamera(cameraId!!, stateCallback, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    var textureListener: TextureView.SurfaceTextureListener = object : TextureView.SurfaceTextureListener {
        //start the camera once the texture is ready
        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, i: Int, i1: Int) {
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, i: Int, i1: Int) {}

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
            if (cameraDevice != null) {
                cameraCaptureSessions?.close()
                cameraDevice = null
            }
            return false
        }

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
//            updatePreview()
        }
    }

    /**
     * Result of the permission
     */
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "You can't use camera without permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        startBackgroundThread()
        if (textureView!!.isAvailable) openCamera() else textureView?.surfaceTextureListener = textureListener
        super.onResume()
    }

    override fun onPause() {
        stopBackgroundThread()
        cameraCaptureSessions?.close()
        cameraDevice?.close()
        super.onPause()
    }

    //
    /**
     * Stop processing in the main
     */
    private fun stopBackgroundThread() {
        mBackgroundThread?.quitSafely()
        try {
            mBackgroundThread?.join()
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("Camera Background")
        mBackgroundThread?.start()
        mBackgroundHandler = Handler(mBackgroundThread?.looper!!)
    }


    /**
     * Recognise an image based on the given byteArray
     */
    private fun recogniseImage(data: ByteArray) {
        var  imageBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        imageBitmap = rotateImage(imageBitmap, 90F)
        //set preview for the user
        previewCapturedImage(imageBitmap)

        val resizedBitmap: Bitmap = Bitmap.createScaledBitmap(imageBitmap, 224, 224, true)
        val model = ModelTF.newInstance(requireContext())
        val tbuffer = TensorImage.fromBitmap(resizedBitmap)
        val byteBuffer = tbuffer.buffer
        // Creates inputs for reference.
        val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val townList = inputString.split("\n")
        val max = getMax(outputFeature0.floatArray)

//        binding.textView14.text = townList[max]
        // Releases model resources if no longer used.
        model.close()
    }


    private fun getBytArray(imageBitmap:Bitmap): ByteArray {
        val blob = ByteArrayOutputStream()
        imageBitmap.compress(CompressFormat.PNG, 0 /* Ignored for PNGs */, blob)
       return blob.toByteArray()
    }
    private fun hidePreview(){
        binding.preview.isVisible = false
        binding.preview.setImageBitmap(null)
    }

    private fun previewCapturedImage(imageBitmap: Bitmap){
        binding.preview.setImageBitmap(imageBitmap)
        binding.preview.isVisible = true
        toggleCheckClearButtons(true)
    }

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
     * To show/hide the check and clear buttons of the camera
     */
    private fun toggleCheckClearButtons(visible: Boolean) {
        binding.clearBtn.isVisible = visible
        binding.checkBtn.isVisible = visible;
    }


    /**
     * To create an image view of the bytArray and add it to the container of the captured images
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun addImageToContainer(capturedImage: ByteArray) {
        isSave = false
        hidePreview()
        val bmp = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.size)
        var scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bmp, 250, 200, false)

//        imageView.setImageBitmap(rotatedBitmap)

        advertisementViewModel.bitmapList.value!!.add(scaledBitmap)
        imageAdapter.notifyDataSetChanged()
        showButtonAndAmount()

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
     * To show the the add button and the amount of taken images
     */
    private fun showButtonAndAmount() {
        binding.amountImg.isVisible = advertisementViewModel.bitmapList.value!!.size > 0
        binding.addBtn.isVisible = advertisementViewModel.bitmapList.value!!.size > 0
        binding.imageContainer.isVisible = advertisementViewModel.bitmapList.value!!.size > 0
        binding.amountImg.text = getString(
                R.string.amount,
                advertisementViewModel.bitmapList.value!!.size
        )
    }


    fun rotateImage(src: Bitmap, degree: Float): Bitmap {
        // create new matrix
        val matrix = Matrix()
        // setup rotation degree
        matrix.postRotate(degree)
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

}
package com.sahilpc.bookie.presentation.home_screen

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sahilpc.bookie.R
import com.sahilpc.bookie.databinding.FragmentHomeBinding
import com.sahilpc.bookie.domain.model.Note
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var homeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.btnLogout.setOnClickListener {
            viewModel.logOut()
        }

        binding.addBtn.setOnClickListener {
            showDialog()
        }

        homeAdapter.setOnItemClickListener { note ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                    note
                )
            )
        }

        viewModel.isLoggedIn.observe(viewLifecycleOwner) {
            if (!it) findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSignInFragment())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notesList.collectLatest { notesList ->
                if (notesList.isEmpty()) {
                    binding.btnExportPdf.visibility = View.GONE
                } else {
                    binding.btnExportPdf.visibility = View.VISIBLE
                }

                homeAdapter.differ.submitList(notesList)
            }
        }

        binding.btnExportPdf.setOnClickListener {
            printPdf()
        }

    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        val noteTitle = dialog.findViewById(R.id.dialogTitle) as EditText
        val noteDescription = dialog.findViewById(R.id.dialogDes) as EditText
        val addBtn = dialog.findViewById(R.id.cd_addBtn) as Button
        val noBtn = dialog.findViewById(R.id.cd_close) as ImageView
        val dialogImage = dialog.findViewById(R.id.cdImageView) as ImageView
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

        dialogImage.setOnClickListener {
            com.github.dhaval2404.imagepicker.ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    720
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                    dialogImage.setImageURI(viewModel.imageUri)
                }


        }
        addBtn.setOnClickListener {
            val title = noteTitle.text.toString().trim()
            val des = noteDescription.text.toString().trim()
            val image: Bitmap = MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                viewModel.imageUri
            )
            if (title.length < 5) {
                noteTitle.error = "too short"
            } else if (des.length < 100) {
                noteDescription.error = "Must have more then 100 Characters"
            } else {
                viewModel.insertNote(
                    Note(
                        title = title,
                        description = des,
                        image = image
                    )
                )
                dialog.dismiss()
            }

        }


    }

    private fun setupRecyclerView() {
        binding.homeRv.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    viewModel.imageUri = fileUri

                }
                com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireActivity(),
                        com.github.dhaval2404.imagepicker.ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    @Throws(IOException::class)
    fun printPdf() {

        try {

            val storageManager =
                getSystemService(requireActivity().applicationContext, StorageManager::class.java)
            val storageVolume = storageManager!!.storageVolumes[0] // internal memory/ storage

            val filePDFOutput = File(storageVolume.directory?.path + "/Download/Bookie.pdf");
            val pdfDocument = PdfDocument()

            val listOfNotes = viewModel.notesList.value


            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.home_rv_item, null)

            val head = view.findViewById<TextView>(R.id.head)
            val subhead = view.findViewById<TextView>(R.id.subhead)
            val image = view.findViewById<ImageView>(R.id.image)

            listOfNotes.forEachIndexed { index, note ->

                head.text = note.title
                subhead.text = note.description
                image.setImageBitmap(note.image)

                val pageInfo = PageInfo.Builder(
                    binding.homeRv.width,
                    binding.homeRv.height,
                    index + 1
                ).create()
                val page = pdfDocument.startPage(pageInfo)


                val bitmap = getBitmapFromView(view)
                page.canvas.drawBitmap(bitmap!!, 0F, 0F, null)
                pdfDocument.finishPage(page)

            }

            pdfDocument.writeTo(FileOutputStream(filePDFOutput))
            pdfDocument.close()

            Toast.makeText(requireContext(), "Pdf saved to downloads folder", Toast.LENGTH_SHORT)
                .show()


        }catch (e:Exception){
            println(e.stackTrace)

            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                .show()

        }



    }

    private fun getBitmapFromView(view: View): Bitmap? {
        //Fetch the dimensions of the viewport
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context?.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        } else {
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        }
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)

        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }


}
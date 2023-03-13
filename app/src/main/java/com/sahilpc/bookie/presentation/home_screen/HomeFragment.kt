package com.sahilpc.bookie.presentation.home_screen

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import at.favre.lib.crypto.bcrypt.BCrypt.with
import com.google.android.gms.cast.framework.media.ImagePicker
import com.sahilpc.bookie.R
import com.sahilpc.bookie.data.local.datastore.AuthDatastore
import com.sahilpc.bookie.databinding.FragmentHomeBinding
import com.sahilpc.bookie.domain.model.Note
import com.sahilpc.bookie.presentation.MainActivity
import com.sahilpc.bookie.presentation.signin_screen.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

        homeAdapter.setOnItemClickListener {note ->
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(note))
        }

        viewModel.isLoggedIn.observe(viewLifecycleOwner){
            if (!it) findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSignInFragment())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notesList.collectLatest { notesList ->
                homeAdapter.differ.submitList(notesList)
            }
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
                noteTitle.setError("too short")
            } else if (des.length < 100) {
                noteDescription.setError("Must have more then 100 Characters")
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

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                viewModel.imageUri = fileUri

            } else if (resultCode == com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR) {
                Toast.makeText(
                    requireActivity(),
                    com.github.dhaval2404.imagepicker.ImagePicker.getError(data),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }


}
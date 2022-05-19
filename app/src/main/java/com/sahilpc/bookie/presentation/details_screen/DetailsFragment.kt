package com.sahilpc.bookie.presentation.details_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sahilpc.bookie.R
import com.sahilpc.bookie.databinding.FragmentDetailsBinding
import com.sahilpc.bookie.databinding.FragmentHomeBinding
import com.sahilpc.bookie.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {


    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsFragmentArgs>()

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.note = args.note

        binding.btnBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteNoteById(args.note.id)
        }

        viewModel.isNoteDeleted.observe(viewLifecycleOwner){
            if (it)findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToHomeFragment())
        }
    }


}
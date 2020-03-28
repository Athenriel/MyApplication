package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentUsersHomeBinding

/**
 * Created by Athenriel on 26/03/2020.
 */
class UsersHomeFragment : Fragment() {

    private var _binding: FragmentUsersHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentUsersHomeCreateUserBtn.setOnClickListener {
            val directions =
                UsersHomeFragmentDirections.actionUsersHomeFragmentToCreateUserFragment()
            findNavController().navigate(directions)
        }
        binding.fragmentUsersHomeViewUsersBtn.setOnClickListener {
            val directions =
                UsersHomeFragmentDirections.actionUsersHomeFragmentToViewUsersFragment()
            findNavController().navigate(directions)
        }
        binding.fragmentUsersHomeShareBtn.setOnClickListener {
            val directions =
                UsersHomeFragmentDirections.actionUsersHomeFragmentToShareFragment()
            findNavController().navigate(directions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

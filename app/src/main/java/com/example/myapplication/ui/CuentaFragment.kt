package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.DialogBottomDarkBinding
import com.example.myapplication.databinding.FragmentCuentaBinding
import com.example.myapplication.viewmodel.CuentaViewModel
import com.example.myapplication.viewmodel.UserViewModel
import org.koin.android.ext.android.inject

/**
 * Created by Francisco Bartilotti on 05/03/2021.
 */
class CuentaFragment: Fragment() {

    private var _binding: FragmentCuentaBinding? = null
    private val binding get() = _binding!!
    private val cuentaViewModel: CuentaViewModel by inject()
    private var cuentaInicial: String? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCuentaBinding.inflate(inflater, container, false)
        arguments?.let { argumentsSafe ->
            cuentaInicial = CuentaFragmentArgs.fromBundle(argumentsSafe).openWithUser
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cuentaTituloTv.text = cuentaInicial
        cuentaViewModel.clearJob()
        cuentaViewModel.getUsers().observe(viewLifecycleOwner, { userListResource ->
            if (userListResource.error == null && userListResource.data != null) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

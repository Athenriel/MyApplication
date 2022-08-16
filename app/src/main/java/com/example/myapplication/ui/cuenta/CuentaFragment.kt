package com.example.myapplication.ui.cuenta

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentCuentaBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.viewmodel.CuentaViewModel
import org.koin.android.ext.android.inject

/**
 * Created by Francisco Bartilotti on 05/03/2021.
 */
class CuentaFragment : BaseFragment<FragmentCuentaBinding>(FragmentCuentaBinding::inflate) {

    private val cuentaViewModel: CuentaViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cuentaInicial = arguments?.let { argumentsSafe ->
            CuentaFragmentArgs.fromBundle(argumentsSafe).openWithUser
        } ?: ""
        binding.cuentaTituloTv.text = cuentaInicial
        cuentaViewModel.clearJob()
        cuentaViewModel.getUsers().observe(viewLifecycleOwner) { userListResource ->
            if (userListResource.error == null && userListResource.data != null) {

            }
        }
    }

}

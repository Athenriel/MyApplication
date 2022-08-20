package com.example.myapplication.ui.createuser

import android.os.Bundle
import android.view.View
import com.example.myapplication.databinding.FragmentAccountBinding
import com.example.myapplication.ui.BaseFragment
import com.example.myapplication.viewmodel.AccountViewModel
import org.koin.android.ext.android.inject

/**
 * Created by Athenriel on 05/03/2021.
 */
class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {

    private val accountViewModel: AccountViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val initialAccount = arguments?.let { argumentsSafe ->
            AccountFragmentArgs.fromBundle(argumentsSafe).openWithUser
        } ?: ""
        binding.accountTitleTextView.text = initialAccount
        accountViewModel.clearJob()
        accountViewModel.getUsers().observe(viewLifecycleOwner) { userListResource ->
            if (userListResource.error == null && userListResource.data != null) {

            }
        }
    }

}

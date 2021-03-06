package com.example.myapplication.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.DialogBottomDarkBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by Athenriel on 09/04/2020.
 */
class DarkBottomDialogFragment : BottomSheetDialogFragment() {

    private var _binding: DialogBottomDarkBinding? = null
    private val binding get() = _binding!!
    var bottomDialogListener: BottomDialogListener? = null

    companion object {
        private const val TITLE_EXTRA = "titleExtra"
        private const val DESCRIPTION_EXTRA = "descriptionExtra"

        fun newInstance(title: String?, description: String?): DarkBottomDialogFragment {
            val bundle = Bundle()
            bundle.putString(TITLE_EXTRA, title)
            bundle.putString(DESCRIPTION_EXTRA, description)
            val fragment = DarkBottomDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogNoDim)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = DialogBottomDarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { argumentsSafe ->
            argumentsSafe.getString(TITLE_EXTRA)?.let { title ->
                binding.bottomDialogTitleTv.text = title
            }
            argumentsSafe.getString(DESCRIPTION_EXTRA)?.let { description ->
                binding.bottomDialogDescriptionTv.text = description
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        bottomDialogListener?.onDismiss()
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

interface BottomDialogListener {
    fun onDismiss()
}

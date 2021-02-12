package com.atriztech.passwordmanager.view.fragmentnewpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.atriztech.passwordmanager.service.di.App
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.NewPasswordFragmentBinding
import javax.inject.Inject

class NewPasswordFragment : Fragment() {
    private lateinit var binding: NewPasswordFragmentBinding

    @Inject
    lateinit var viewModel: NewPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized ) {
            App.component()!!.inject(this)

            binding = DataBindingUtil.inflate(inflater,
                R.layout.new_password_fragment, container, false);
            binding.viewModel = viewModel
            binding.fragment = this
        } else {
            this.requireActivity().finish()
        }

        return binding.root
    }

    fun confirmPassword(){
        var status = viewModel.comparePassword()

        when (status) {
            "ok" -> {
                viewModel.addNewKeyToDB()
                startListGroupsActivity(viewModel.password.get()!!)
            }
            "short" -> Toast.makeText(this.requireContext(), "Пароль слишком короткий. Минимальная длинна 4 символа.", Toast.LENGTH_SHORT).show()
            "not equal" -> Toast.makeText(this.requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startListGroupsActivity(password: String){
        var bundle = Bundle()
        bundle.putString("password", password)
        this.findNavController().navigate(R.id.action_new_password_fragment_to_groups_fragment, bundle)
    }
}
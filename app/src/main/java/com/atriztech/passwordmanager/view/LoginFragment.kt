package com.atriztech.passwordmanager.view

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
import com.atriztech.passwordmanager.databinding.LoginFragmentBinding
import com.atriztech.passwordmanager.viewmodels.LoginViewModel
import javax.inject.Inject

class LoginFragment: Fragment() {
    private lateinit var binding: LoginFragmentBinding
    private var passKey = ""

    @Inject
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!retainInstance ) {
            retainInstance = true

            App.component()!!.inject(this)

            passKey = requireArguments().getString("pass_key")!!

            binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
            binding.viewModel = viewModel
            binding.fragment = this

            return binding.root
        } else {
            return binding.root
        }
    }

    fun confirmPassword(view: View){
        if (viewModel.checkPassword(passKey)){
            startListItemsActivity()
        } else {
            Toast.makeText(this.requireContext(), "Пароль введен неверно", Toast.LENGTH_SHORT).show()
        }
    }

    fun startListItemsActivity(){
        var bundle = Bundle()
        bundle.putString("password", viewModel.password.get())
        this.findNavController().navigate(R.id.action_login_fragment_to_list_groups_fragment, bundle)
    }
}
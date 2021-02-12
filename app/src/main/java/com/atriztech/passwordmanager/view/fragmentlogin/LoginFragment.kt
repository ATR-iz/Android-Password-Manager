package com.atriztech.passwordmanager.view.fragmentlogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.atriztech.passwordmanager.service.di.App
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.LoginFragmentBinding
import com.atriztech.passwordmanager.view.fragmentlistgroups.ListGroupsFragment
import javax.inject.Inject

class LoginFragment: Fragment() {

    companion object{
        const val PASS_KEY = "pass_key"

        fun bundleFor(passKey: String): Bundle {
            val bundle = Bundle()
            bundle.putString(PASS_KEY, passKey)
            return bundle
        }
    }

    @Inject lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding
    private var passKey = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized ) {
            App.component()!!.inject(this)

            passKey = requireArguments().getString(PASS_KEY)!!

            binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
            binding.viewModel = viewModel
            binding.fragment = this
        } else {
            this.requireActivity().finish()
        }

        return binding.root
    }

    fun confirmPassword(){
        if (viewModel.checkPassword(passKey)){
            openListGroups()
        } else {
            Toast.makeText(this.requireContext(), "Пароль введен неверно", Toast.LENGTH_SHORT).show()
        }
    }

    fun openListGroups() =
        this.findNavController().navigate(R.id.action_login_fragment_to_list_groups_fragment, ListGroupsFragment.bundleFor(viewModel.password.get()!!))
}
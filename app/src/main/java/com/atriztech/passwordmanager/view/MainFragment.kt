package com.atriztech.passwordmanager.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.atriztech.passwordmanager.R
import com.atriztech.passwordmanager.databinding.MainFragmentBinding
import com.atriztech.passwordmanager.model.Dir
import com.atriztech.passwordmanager.model.database.GroupWithItemDB
import com.atriztech.passwordmanager.viewmodels.MainViewModel

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!retainInstance ) {
            retainInstance = true

            createDirs()
            viewModel = MainViewModel()

            binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
            binding.viewModel = viewModel
            binding.fragment = this

            viewModel.status.observe(this.requireActivity(), Observer {
                    passKey -> if(passKey == "First") startNewPasswordFragment()
            else startLoginFragment(passKey)
            })

            var db = Room.databaseBuilder(this.requireActivity(), GroupWithItemDB::class.java, "db").build()

            viewModel.getTestDataFromDB(db)
        }

        return binding.root
    }

    private fun startNewPasswordFragment(){
        findNavController().navigate(R.id.action_main_fragment_to_new_password_fragment, Bundle())
    }

    private fun startLoginFragment(passKey: String){
        var bundle = Bundle()
        bundle.putString("pass_key", passKey)
        findNavController().navigate(R.id.action_main_fragment_to_login_fragment, bundle)
    }

    private fun createDirs(){
        Dir.homeDirOnMemory = requireActivity().filesDir.absoluteFile.toString()
        Dir.createDir(Dir.homeDirOnMemory + "/image", false)
        Dir.createDir(Dir.homeDirOnMemory + "/tmp_image", true)
    }
}
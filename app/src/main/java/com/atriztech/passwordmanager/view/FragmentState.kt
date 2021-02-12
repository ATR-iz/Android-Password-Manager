package com.atriztech.passwordmanager.view

sealed class FragmentState{
    class Save: FragmentState()
    class Delete: FragmentState()
    class Cancel: FragmentState()
}
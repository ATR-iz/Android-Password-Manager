package com.atriztech.passwordmanager.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.model.entity.GroupEntity

class GroupViewModel: ViewModel() {
    var group = ObservableField<GroupEntity>(GroupEntity(name = "", url = ""))
    var old_url = ""
}
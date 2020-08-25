package com.atriztech.passwordmanager.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.model.entity.GroupEntity

class GroupViewModel: ViewModel() {
    var group = ObservableField<GroupEntity>(GroupEntity(name = "", url = ""))
}
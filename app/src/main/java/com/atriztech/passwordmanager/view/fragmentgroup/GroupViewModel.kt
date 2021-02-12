package com.atriztech.passwordmanager.view.fragmentgroup

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.passwordmanager.model.entity.GroupEntity
import javax.inject.Inject

class GroupViewModel @Inject constructor(): ViewModel() {
    var group = ObservableField<GroupEntity>(GroupEntity(name = "", url = ""))
    var url: String = ""
    var old_url = ""
}
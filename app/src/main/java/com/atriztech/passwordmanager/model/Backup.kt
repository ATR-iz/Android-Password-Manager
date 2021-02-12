package com.atriztech.passwordmanager.model

import com.atriztech.file_manager_impl.FileImpl

class Backup(dirBackup: DirBackup): FileImpl(dirBackup.path, "Backup.json")
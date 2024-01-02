package com.hayatibahar.hackomthing.ui

import com.hayatibahar.hackomthing.domain.BluetoothDevice

data class BluetoothUiState(
    val scannedDevices : List<BluetoothDevice> = emptyList(),
    val pairedDevices : List<BluetoothDevice> = emptyList()
)
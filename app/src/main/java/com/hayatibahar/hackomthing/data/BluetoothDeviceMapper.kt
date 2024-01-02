package com.hayatibahar.hackomthing.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.hayatibahar.hackomthing.domain.BluetoothDeviceDomain


@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}
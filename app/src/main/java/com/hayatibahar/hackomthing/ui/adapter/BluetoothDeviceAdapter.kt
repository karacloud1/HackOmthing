package com.hayatibahar.hackomthing.ui.adapter

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hayatibahar.hackomthing.databinding.RecyclerItemBinding
import com.hayatibahar.hackomthing.domain.BluetoothDeviceDomain


class BluetoothDeviceAdapter(
    private val devices: List<BluetoothDeviceDomain>,
    private val onClick: (BluetoothDeviceDomain) -> Unit,
) :
    RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder>() {

    class ViewHolder(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        holder.binding.deviceName.text = device.name ?: "(No name)"
        holder.binding.root.setOnClickListener { onClick(device) }
    }

    override fun getItemCount() = devices.size
}


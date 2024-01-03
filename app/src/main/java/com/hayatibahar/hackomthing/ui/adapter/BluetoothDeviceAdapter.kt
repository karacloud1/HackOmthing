package com.hayatibahar.hackomthing.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hayatibahar.hackomthing.databinding.DeviceItemBinding
import com.hayatibahar.hackomthing.domain.BluetoothDeviceDomain


class BluetoothDeviceAdapter(
    private val onClick: (BluetoothDeviceDomain) -> Unit,
) :
    RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder>() {

    private val devices = mutableListOf<BluetoothDeviceDomain>()

     inner class DeviceViewHolder(private val binding: DeviceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(device: BluetoothDeviceDomain) {
            with(binding) {
                deviceName.text = device.name ?: "(No name)"
                root.setOnClickListener { onClick(device) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val binding = DeviceItemBinding.inflate(inflater,parent,false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount() = devices.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newDevices: List<BluetoothDeviceDomain>) {
        devices.clear()
        devices.addAll(newDevices)
        notifyDataSetChanged()
    }
}


package com.hayatibahar.hackomthing.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hayatibahar.hackomthing.databinding.ActivityMainBinding
import com.hayatibahar.hackomthing.domain.BluetoothDeviceDomain
import com.hayatibahar.hackomthing.ui.adapter.BluetoothDeviceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<BluetoothViewModel>()

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }
    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    private lateinit var enableBluetoothLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val pairedDevicesAdapter : BluetoothDeviceAdapter by lazy { BluetoothDeviceAdapter(::deviceClickEvent) }
    private val scannedDevicesAdapter : BluetoothDeviceAdapter by lazy { BluetoothDeviceAdapter(::deviceClickEvent) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            startScanBtn.setOnClickListener {
                viewModel.startScan()
            }
            stopScanBtn.setOnClickListener {
                viewModel.stopScan()
            }
            startServerBtn.setOnClickListener {
                viewModel.waitForIncomingConnections()
            }
            pairedDevicesRv.adapter = pairedDevicesAdapter
            scannedDevicesRv.adapter = scannedDevicesAdapter
        }

        enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { /* Not needed */ }

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true

            if (canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                pairedDevicesAdapter.updateItems(state.pairedDevices)
                scannedDevicesAdapter.updateItems(state.scannedDevices)
                when {
                    state.isConnecting -> {
                        binding.progressCircular.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.progressCircular.visibility = View.INVISIBLE
                    }
                }
                state.errorMessage?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deviceClickEvent(device: BluetoothDeviceDomain) {
        viewModel.connectToDevice(device)
    }
}
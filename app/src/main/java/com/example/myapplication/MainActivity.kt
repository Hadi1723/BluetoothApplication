package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.myapplication.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var REQUEST_CODE_ENABLE_BT = 1
    private var REQUEST_CODE_DISCOVERABLE_BT = 2

    //defining the bluetooth adapter
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    //var bluetoothManager = applicationContext.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
    //var bluetoothAdapter = bluetoothManager.adapter

    //private lateinit var availableDevices: Set<BluetoothDevice>

    //private var listAvailableDevices: StringBuffer = StringBuffer()


    //var counter = 0

    private lateinit var listOfPaired: MutableList<String>

    val context: Context = this

    //var counter = 120

    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 12000000.0

    //private lateinit var adapterAvailableDevices: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)

        bluetoothManager = applicationContext.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter


        if (bluetoothAdapter.isEnabled) {
            binding.tvNumPaired.text = "Paired: ${getNumPaired()}"


            // isDiscovering returns true if the local Bluetooth adapter is currently in the device discovery process.
            if (!bluetoothAdapter.isDiscovering) {
                binding.discoverButton.visibility = View.VISIBLE
            }

            binding.btnEnable.visibility = View.GONE
            Toast.makeText(this, "Bluetooth already enabled. See results now!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Enable Bluetooth to see results", Toast.LENGTH_LONG).show()
        }

        binding.btnEnable.setOnClickListener(View.OnClickListener {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            //binding.btnEnable.text = intent.toString()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions

            }
            startActivityForResult(intent, 1)

        })

        binding.discoverButton.setOnClickListener {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            startActivityForResult(intent, 2)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemSelected = item.itemId

        if (bluetoothAdapter.isEnabled) {

            when(itemSelected) {
                R.id.paired_list -> {

                    // https://www.baeldung.com/kotlin/convert-list-to-array
                    var dataList = getPairedList().toTypedArray()

                    var intent = Intent(this, ListPaired::class.java)

                    binding.scanActive.text = "Discovery: Inactive"

                    intent.putExtra("ListP", dataList)
                    intent.putExtra("NumP", getNumPaired())
                    startActivity(intent)
                }
                R.id.off_enable -> {
                    warnOffBluetooth("Are you sure you want to turn off Bluetooth?", null, View.OnClickListener {
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions

                            binding.btnEnable.visibility = View.VISIBLE
                            binding.discoverButton.visibility = View.GONE

                            binding.scanActive.text = "Discovery: Inactive"
                            //disabling bluetooth
                            bluetoothAdapter.disable()
                        }

                    })
                }
                else -> {

                    return true
                }
            }
        } else {
            Toast.makeText(this, "Enable Bluetooth before viewing other pages", Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getNumPaired(): Int {
        var data: StringBuffer = StringBuffer()

        var pairedDevices = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            bluetoothAdapter.bondedDevices
        } else {
            return 0
        }

        return pairedDevices.size
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_DISCOVERABLE_BT -> {

                if (resultCode == 120) {
                    binding.discoverButton.visibility = View.GONE
                    binding.scanActive.text = "Discovery: Active"
                } else {
                    Toast.makeText(this, "Device still not discoverable.", Toast.LENGTH_LONG).show()
                }
            }
            REQUEST_CODE_ENABLE_BT ->
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth still not enabled.", Toast.LENGTH_LONG).show()

                } else {
                    binding.tvNumPaired.text = "Paired: ${getNumPaired()}"

                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_SCAN
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    if (!bluetoothAdapter.isDiscovering) {
                        binding.discoverButton.visibility = View.VISIBLE
                    }

                    binding.btnEnable.visibility = View.GONE

                    Toast.makeText(this, "Bluetooth now enabled. See results now!", Toast.LENGTH_LONG).show()

                    //binding.discoverButton.visibility
                }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    //creating warning screen
    private fun warnOffBluetooth(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Ok") {_, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun getPairedList(): MutableList<String> {
        var data: String

        listOfPaired = mutableListOf("List of Paired Devices \n")

        var pairedDevices = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            bluetoothAdapter.bondedDevices
        } else {
            return listOfPaired
        }

        var counter = 0
        for (device in pairedDevices) {
            counter++
            //data.append("$counter. Device Name = ${device.name} Device Address: ${device.address}\n")
            listOfPaired.add("$counter. Device Name = ${device.name} Device Address: ${device.address}\n")

        }

        return listOfPaired
    }

}


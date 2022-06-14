package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
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

class ListPaired: AppCompatActivity() {
    //lateinit var binding: ActivityMainBinding

    private lateinit var pairedList: ListView
    private  lateinit var numPList: TextView
    private lateinit var searchItem: SearchView

    //defining the bluetooth adapter
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paired_list)

        bluetoothManager = applicationContext.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        pairedList = findViewById(R.id.ListofPaired)
        numPList = findViewById(R.id.tvNumPaired)
        searchItem = findViewById(R.id.searchView)
        //pairedList.text = intent.getStringExtra("ListP")

        numPList.text = "Paired: ${intent.getIntExtra("NumP", 0)}"

        //val user = listOfPaired()

        /*
        val user: Array<String> = intent.getStringArrayExtra("ListP") as Array<String>

        //getStringExtra("ListP")

        val userAdapter : ArrayAdapter<String> = ArrayAdapter(
            this,android.R.layout.simple_list_item_1,
            user
        )

        //binding.userList.adapter = userAdapter;

        pairedList.adapter = userAdapter

        searchItem.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchItem.clearFocus()
                if (user.contains(query)){

                    userAdapter.filter.filter(query)

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }


        }) */

        getSearch()
    }

    private fun listOfPaired(): ArrayAdapter<String> {
        val user: Array<String> = intent.getStringArrayExtra("ListP") as Array<String>

        //getStringExtra("ListP")

        val userAdapter : ArrayAdapter<String> = ArrayAdapter(
            this,android.R.layout.simple_list_item_1,
            user
        )

        return userAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //first parameter is file directory path
        menuInflater.inflate(R.menu.menu_paired, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemSelected = item.itemId

        when(itemSelected) {
            R.id.paired_list -> {
                return true
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

                        //disabling bluetooth
                        bluetoothAdapter.disable()
                    }

                })
            }
            R.id.search -> {
                if (searchItem.visibility == View.VISIBLE) {
                    searchItem.visibility = View.GONE
                } else{
                    searchItem.visibility = View.VISIBLE
                }

            }
            R.id.refresh -> {
                getSearch()
                /*
                var intent = Intent(this, ListPaired::class.java)
                intent.putExtra("ListP", intent.getStringArrayExtra("ListP"))
                startActivity(intent)

                val user: Array<String> = intent.getStringArrayExtra("ListP") as Array<String>

                //getStringExtra("ListP")

                val userAdapter : ArrayAdapter<String> = ArrayAdapter(
                    this,android.R.layout.simple_list_item_1,
                    user
                )

                //binding.userList.adapter = userAdapter;

                pairedList.adapter = userAdapter

                searchItem.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {

                        /* Focusable means that it can gain the focus from an input device like a keyboard. Input devices like keyboards cannot decide which view to send its input events to based on the inputs itself, so they send them to the view that has focus. */
                        searchItem.clearFocus()
                        if (user.contains(query)){

                            userAdapter.filter.filter(query)

                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        userAdapter.filter.filter(newText)
                        return false
                    }


                }) */
            }
            else -> {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getSearch() {
        val user: Array<String> = intent.getStringArrayExtra("ListP") as Array<String>

        //getStringExtra("ListP")

        val userAdapter : ArrayAdapter<String> = ArrayAdapter(
            this,android.R.layout.simple_list_item_1,
            user
        )

        //binding.userList.adapter = userAdapter;

        pairedList.adapter = userAdapter

        searchItem.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                /* Focusable means that it can gain the focus from an input device like a keyboard. Input devices like keyboards cannot decide which view to send its input events to based on the inputs itself, so they send them to the view that has focus. */
                searchItem.clearFocus()
                if (user.contains(query)){

                    userAdapter.filter.filter(query)

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }


        })
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

}
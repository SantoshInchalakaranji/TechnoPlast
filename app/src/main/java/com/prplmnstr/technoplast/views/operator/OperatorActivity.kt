package com.prplmnstr.technoplast.views.operator

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorInflater
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.prplmnstr.technoplast.R
import com.prplmnstr.technoplast.databinding.ActivityOperatorBinding
import com.prplmnstr.technoplast.models.Machine
import com.prplmnstr.technoplast.models.Mould
import com.prplmnstr.technoplast.models.Record
import com.prplmnstr.technoplast.utils.Constants
import com.prplmnstr.technoplast.utils.CreatePdf
import com.prplmnstr.technoplast.utils.Helper
import com.prplmnstr.technoplast.viewModel.OperatorActivityViewModel
import com.prplmnstr.technoplast.views.LoginActivity

class OperatorActivity : AppCompatActivity() {

    val createPdf: CreatePdf = CreatePdf()
    private lateinit var binding : ActivityOperatorBinding
    private  var shiftRecord = Record()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: OperatorActivityViewModel
    private lateinit var loader: Dialog
    private val machineList = mutableListOf<Machine>()
    private val mouldList = mutableListOf<Mould>()
    private val machineNamesList = mutableListOf<String>()
    private val mouldNameList = mutableListOf<String>()
    private var spinnerAdapter: ArrayAdapter<String>? = null
    private var mouldSpinnerAdapter: ArrayAdapter<String>? = null

    private var isImage1Visible = true
    private var day_mode = true
    private var shiftMode = Constants.DAY_SHIFT

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email
    private lateinit var operatorName :String
    private var selectedMachine = Machine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(OperatorActivityViewModel::class.java)


        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )

        initializeLoader()
        loadMachines()



        binding.logoutImage.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.logout_menu, popupMenu.menu)
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signOut()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

                // You can add your SharedPreferences logic here
                // ...
                viewModel.removeUserTypeFromSharedPref(this)
                startActivity(Intent(this, LoginActivity::class.java))
               this.finish()
                true
            }
        }



        binding.submitBtn.setOnClickListener {


            if(addDataToShiftRecord()){
                if(shiftRecord.shift.equals(Constants.DAY_SHIFT)){
                    createPdf.createPdf(this,this,shiftRecord, Record())
                }else{

                    createPdf.createPdf(this,this, Record(),shiftRecord)
                }

                uploadRecord()
            }else{
                return@setOnClickListener
            }

        }
        setSwitch()

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {

                shiftRecord.name = machineNamesList[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        binding.mouldSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {

                setMouldDetails(mouldList[i])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }


    }

    private fun addDataToShiftRecord() : Boolean{

        shiftRecord.productionWT = binding.productinWtEt.text.toString()
        shiftRecord.article = binding.articleEt.text.toString()
        shiftRecord.orderQty = binding.orderQtyEt.text.toString()
        shiftRecord.loadTime = binding.mLoadTimeEt.text.toString()
        shiftRecord.unloadTime = binding.mUnloadTimeEt.text.toString()
        shiftRecord.numCavity = binding.cavityEt.text.toString()
        shiftRecord.heating = binding.heatingEt.text.toString()
        shiftRecord.heatingAct = binding.heatingActEt.text.toString()
        shiftRecord.rawMaterial = binding.rawMaterialEt.text.toString()
        shiftRecord.totalMaterialUsed = binding.materialUsedEt.text.toString()
        shiftRecord.pigment = binding.pigmentEt.text.toString()
        shiftRecord.totalMbUsed = binding.mbUsedEt.text.toString()
        shiftRecord.operator = binding.operatorEt.text.toString()


        selectedMachine.name = shiftRecord.name


        if(shiftRecord.loadTime.isEmpty()){
            showToast("Please enter Mould Load Time")
            binding.mLoadTimeEt.error = "Please enter Mould Load Time"
            return false
        }

        if(shiftRecord.unloadTime.isEmpty()){
            showToast("Please enter Mould Unload Time")
            binding.mUnloadTimeEt.error = "Please enter Mould Unload Time"
            return false
        }

        if(shiftRecord.operator.isEmpty()){
            showToast("Please enter operator name")
            binding.operatorEt.error = "Please enter operator name"
            return false
        }


        var one = binding.firstEt.text.toString()
        var two = binding.secondEt.text.toString()
        var three = binding.threeEt.text.toString()
        var four = binding.fourEt.text.toString()
        var five = binding.fiveEt.text.toString()
        var six = binding.sixEt.text.toString()
        var seven = binding.sevenEt.text.toString()
        var eight = binding.eightEt.text.toString()
        var nine = binding.eightEt.text.toString()
        var ten = binding.tenEt.text.toString()
        var eleven = binding.elevenEt.text.toString()
        var twelve = binding.twelveTvEt.text.toString()
        var qty = binding.qtyEt.text.toString()
        var bag = binding.balEt.text.toString()
        var start = binding.startEt.text.toString()
        var end = binding.endEt.text.toString()
        var lump = binding.lumpEt.text.toString()
        var materialLeft = binding.materialLeftEt.text.toString()

        var reason = binding.reasonEt.text.toString()

        if(!one.isEmpty())
            shiftRecord.one = one.toInt()
        if(!two.isEmpty())
            shiftRecord.two = two.toInt()
        if(!three.isEmpty())
            shiftRecord.three = three.toInt()
        if(!four.isEmpty())
            shiftRecord.four = four.toInt()
        if(!five.isEmpty())
            shiftRecord.five = five.toInt()
        if(!six.isEmpty())
            shiftRecord.six = six.toInt()
        if(!seven.isEmpty())
            shiftRecord.seven = seven.toInt()
        if(!eight.isEmpty())
            shiftRecord.eight = eight.toInt()
        if(!nine.isEmpty())
            shiftRecord.nine = nine.toInt()
        if(!ten.isEmpty())
            shiftRecord.ten = ten.toInt()
        if(!eleven.isEmpty())
            shiftRecord.eleven = eleven.toInt()
        if(!twelve.isEmpty())
            shiftRecord.twelve = twelve.toInt()

        if(one.isEmpty() ||
            two.isEmpty() ||
            three.isEmpty() ||
            four.isEmpty() ||
            five.isEmpty() ||
            six.isEmpty() ||
            seven.isEmpty() ||
            eight.isEmpty() ||
            nine.isEmpty() ||
            ten.isEmpty() ||
            eleven.isEmpty() ||
            twelve.isEmpty()
                ){

            if(reason.isEmpty()){
                binding.reasonEt.error = "One or more shift field is empty please write the reason"
                return false
            }
        }


        if(!qty.isEmpty())
            shiftRecord.qty = qty.toInt()
        if(!bag.isEmpty())
            shiftRecord.bag = bag.toInt()

        shiftRecord.start = start
        shiftRecord.end = end
        if(!lump.isEmpty())
            shiftRecord.lump = lump.toInt()
        if(!materialLeft.isEmpty())
            shiftRecord.grindingMaterialLeft = materialLeft.toInt()

        shiftRecord.shift = shiftMode
        shiftRecord.reason = reason

        return true
    }


    private fun uploadRecord() {
        var today = Helper.getTodayDateObject()
        shiftRecord.date = today.dateInStringFormat


        loader.show()
        viewModel.sendUserDataToFirestore(shiftRecord,selectedMachine) { isSuccess ->
            if (isSuccess) {
             showToast("Production report added successfully!")
            } else {
                showToast("Failed to upload report.")
            }
            clearDate()
            loader.dismiss()
        }
    }

    private fun clearDate() {


// Get the SharedPreferences.Editor

        binding.firstEt.setText("")
        binding.secondEt.setText("")
        binding.threeEt.setText("")
        binding.fourEt.setText("")
        binding.fiveEt.setText("")
        binding.sixEt.setText("")
        binding.sevenEt.setText("")
        binding.eightEt.setText("")
        binding.nineEt.setText("")
        binding.tenEt.setText("")
        binding.elevenEt.setText("")
        binding.twelveTvEt.setText("")
        binding.qtyEt.setText("")
        binding.balEt.setText("")
        binding.startEt.setText("")
        binding.endEt.setText("")
        binding.lumpEt.setText("")
        binding.materialLeftEt.setText("")
        binding.operatorEt.setText("")
        binding.reasonEt.setText("")
    }





    private fun setSwitch() {
        binding.switchButton.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            day_mode = !day_mode
            if(day_mode){
               showToast("Day Shift")
                shiftMode = Constants.DAY_SHIFT
            }else{
                showToast("Night Shift")
                shiftMode = Constants.NIGHT_SHIFT

            }
            shiftRecord.shift = shiftMode
            val animation = AnimatorInflater.loadAnimator(this, R.animator.fade_animation)
            animation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    isImage1Visible = !isChecked
                    if (isImage1Visible) {
                        binding.sunIv.setImageResource(R.drawable.day)
                    } else {
                        binding.sunIv.setImageResource(R.drawable.night)
                    }
                    binding.sunIv.alpha = 1.0f // Reset the alpha value
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            animation.setTarget(binding.sunIv)
            animation.start()
        }
    }

    private fun initializeLoader() {
        loader = Dialog(this)
        loader.setContentView(R.layout.loader)
        loader.window?.setBackgroundDrawableResource(R.color.transparent)
        loader.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        loader.setCancelable(false)
        loader.window?.attributes?.windowAnimations = R.style.animation
    }

    private fun loadMachines() {
        loader.show()


        viewModel.retrieveMachines().observe(this) { retrievedList ->
            if (retrievedList.isEmpty()) {
                binding.scroller.visibility = View.GONE
                binding.switchButton.visibility = View.INVISIBLE
                binding.noPlantsLayout.visibility = View.VISIBLE
            } else {
                machineList.clear()
                machineList.addAll(retrievedList)
                loadMoulds()
                for(machine in machineList){
                    machineNamesList.add(machine.name)
                }

            }
            loader.dismiss()
        }
    }

    private fun loadMoulds() {
        loader.show()


        viewModel.retrieveMoulds().observe(this) { retrievedList ->
            if (retrievedList.isEmpty()) {
                binding.scroller.visibility = View.GONE
                binding.switchButton.visibility = View.INVISIBLE
                binding.noPlantsLayout.visibility = View.VISIBLE
            } else {
                mouldList.clear()
                mouldList.addAll(retrievedList)

                for(mould in mouldList){
                    mouldNameList.add(mould.name)
                }
                loadUI()


                binding.noPlantsLayout.visibility = View.GONE
                binding.scroller.visibility = View.VISIBLE
                binding.switchButton.visibility = View.VISIBLE

            }
            loader.dismiss()
        }
    }

    private fun loadUI() {

        shiftRecord.shift = Constants.DAY_SHIFT
        shiftRecord.date = Helper.getTodayDateObject().dateInStringFormat


            setMachineAndMouldNamesToSpinner()





    }

    private fun setStoredData() {

        binding.productinWtEt.setText(shiftRecord.productionWT)
        binding.orderQtyEt.setText(shiftRecord.orderQty)
        binding.mLoadTimeEt.setText(shiftRecord.loadTime)
        binding.mUnloadTimeEt.setText(shiftRecord.unloadTime)
        binding.articleEt.setText(shiftRecord.article)
        binding.heatingEt.setText(shiftRecord.heating)
        binding.cavityEt.setText(shiftRecord.numCavity)
        binding.heatingActEt.setText(shiftRecord.heatingAct)
        binding.rawMaterialEt.setText(shiftRecord.rawMaterial)
        binding.materialUsedEt.setText(shiftRecord.totalMaterialUsed)
        binding.pigmentEt.setText(shiftRecord.pigment)
        binding.mbUsedEt.setText(shiftRecord.totalMbUsed)


        binding.firstEt.setText(shiftRecord.one.toString())
        binding.secondEt.setText(shiftRecord.two.toString())
        binding.threeEt.setText(shiftRecord.three.toString())
        binding.fourEt.setText(shiftRecord.four.toString())
        binding.fiveEt.setText(shiftRecord.five.toString())
        binding.sixEt.setText(shiftRecord.six.toString())
        binding.sevenEt.setText(shiftRecord.seven.toString())
        binding.eightEt.setText(shiftRecord.eight.toString())
        binding.nineEt.setText(shiftRecord.nine.toString())
        binding.tenEt.setText(shiftRecord.ten.toString())
        binding.elevenEt.setText(shiftRecord.eleven.toString())
        binding.twelveTvEt.setText(shiftRecord.twelve.toString())
        binding.qtyEt.setText(shiftRecord.qty.toString())
        binding.balEt.setText(shiftRecord.bag.toString())
        binding.startEt.setText(shiftRecord.start)
        binding.endEt.setText(shiftRecord.end)
        binding.lumpEt.setText(shiftRecord.lump.toString())
        binding.materialLeftEt.setText(shiftRecord.grindingMaterialLeft.toString())

        if(shiftRecord.shift.equals(Constants.NIGHT_SHIFT)){
            binding.switchButton.isChecked = true
        }
    }

    fun retrieveRecordFromSharedPreferences(): Record {
        val gson = Gson()
        val userJson = sharedPreferences.getString(Constants.RECORD, "")
        if (userJson != null) {
            if (!userJson.isEmpty()) {

                return gson.fromJson(userJson, Record::class.java)
            } else {
                // User object doesn't exist in SharedPreferences
                return Record()
            }
        }
        return Record()


    }

    private fun setMachineAndMouldNamesToSpinner() {
        spinnerAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            machineNamesList
        )
        spinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.setAdapter(spinnerAdapter)


        mouldSpinnerAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            mouldNameList
        )
        mouldSpinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mouldSpinner.setAdapter(mouldSpinnerAdapter)


    }

    private fun setMouldDetails(selectedMould:Mould) {
            //update record list with selected Machine data and set UI

             viewModel.setMachineDetails(selectedMould,shiftRecord)

            binding.orderQtyEt.setText(shiftRecord.orderQty)
            binding.productinWtEt.setText(shiftRecord.productionWT)
            binding.mLoadTimeEt.setText(shiftRecord.loadTime)
            binding.mUnloadTimeEt.setText(shiftRecord.unloadTime)
            binding.articleEt.setText(shiftRecord.article)
            binding.heatingEt.setText(shiftRecord.heating)
            binding.cavityEt.setText(shiftRecord.numCavity)
            binding.heatingActEt.setText(shiftRecord.heatingAct)
            binding.rawMaterialEt.setText(shiftRecord.rawMaterial)
            binding.materialUsedEt.setText(shiftRecord.totalMaterialUsed)
            binding.pigmentEt.setText(shiftRecord.pigment)
            binding.mbUsedEt.setText(shiftRecord.totalMbUsed)

    }

    override fun onDestroy() {
        super.onDestroy()
        var one = binding.firstEt.text.toString()
        var two = binding.secondEt.text.toString()
        var three = binding.threeEt.text.toString()
        var four = binding.fourEt.text.toString()
        var five = binding.fiveEt.text.toString()
        if(!one.isEmpty() || !two.isEmpty() || !three.isEmpty() || !four.isEmpty()|| !five.isEmpty())
        autoSaveEnteredData()
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun autoSaveEnteredData() {

        shiftRecord.productionWT = binding.productinWtEt.text.toString()
        shiftRecord.article = binding.articleEt.text.toString()
        shiftRecord.orderQty = binding.orderQtyEt.text.toString()
        shiftRecord.loadTime = binding.mLoadTimeEt.text.toString()
        shiftRecord.unloadTime = binding.mUnloadTimeEt.text.toString()
        shiftRecord.numCavity = binding.cavityEt.text.toString()
        shiftRecord.heating = binding.heatingEt.text.toString()
        shiftRecord.heatingAct = binding.heatingActEt.text.toString()
        shiftRecord.rawMaterial = binding.rawMaterialEt.text.toString()
        shiftRecord.totalMaterialUsed = binding.materialUsedEt.text.toString()
        shiftRecord.pigment = binding.pigmentEt.text.toString()
        shiftRecord.totalMbUsed = binding.mbUsedEt.text.toString()


        var one = binding.firstEt.text.toString()
        var two = binding.secondEt.text.toString()
        var three = binding.threeEt.text.toString()
        var four = binding.fourEt.text.toString()
        var five = binding.fiveEt.text.toString()
        var six = binding.sixEt.text.toString()
        var seven = binding.sevenEt.text.toString()
        var eight = binding.eightEt.text.toString()
        var nine = binding.eightEt.text.toString()
        var ten = binding.tenEt.text.toString()
        var eleven = binding.elevenEt.text.toString()
        var twelve = binding.twelveTvEt.text.toString()
        var qty = binding.qtyEt.text.toString()
        var bag = binding.balEt.text.toString()
        var start = binding.startEt.text.toString()
        var end = binding.endEt.text.toString()
        var lump = binding.lumpEt.text.toString()
        var materialLeft = binding.materialLeftEt.text.toString()

        if(!one.isEmpty())
        shiftRecord.one = one.toInt()
        if(!two.isEmpty())
        shiftRecord.two = two.toInt()
        if(!three.isEmpty())
        shiftRecord.three = three.toInt()
        if(!four.isEmpty())
        shiftRecord.four = four.toInt()
        if(!five.isEmpty())
        shiftRecord.five = five.toInt()
        if(!six.isEmpty())
        shiftRecord.six = six.toInt()
        if(!seven.isEmpty())
        shiftRecord.seven = seven.toInt()
        if(!eight.isEmpty())
        shiftRecord.eight = eight.toInt()
        if(!nine.isEmpty())
        shiftRecord.nine = nine.toInt()
        if(!ten.isEmpty())
        shiftRecord.ten = ten.toInt()
        if(!eleven.isEmpty())
        shiftRecord.eleven = eleven.toInt()
        if(!twelve.isEmpty())
        shiftRecord.twelve = twelve.toInt()


            if(!qty.isEmpty())
        shiftRecord.qty = qty.toInt()
        if(!bag.isEmpty())
        shiftRecord.bag = bag.toInt()

        shiftRecord.start = start
        shiftRecord.end = end
        if(!lump.isEmpty())
        shiftRecord.lump = lump.toInt()
        if(!materialLeft.isEmpty())
        shiftRecord.grindingMaterialLeft = materialLeft.toInt()

        shiftRecord.shift = shiftMode


        viewModel.saveRecordToSharedPreferences(shiftRecord,sharedPreferences)


    }

}
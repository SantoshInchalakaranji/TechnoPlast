package com.prplmnstr.technoplast.views.admin

import android.animation.Animator
import android.animation.AnimatorInflater
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.prplmnstr.technoplast.R
import com.prplmnstr.technoplast.databinding.ActivityAdminBinding
import com.prplmnstr.technoplast.databinding.ActivityMachineDetailsBinding
import com.prplmnstr.technoplast.databinding.ActivityOperatorBinding
import com.prplmnstr.technoplast.models.Date
import com.prplmnstr.technoplast.models.Machine
import com.prplmnstr.technoplast.models.Record
import com.prplmnstr.technoplast.utils.Constants
import com.prplmnstr.technoplast.utils.CreatePdf
import com.prplmnstr.technoplast.utils.Helper
import com.prplmnstr.technoplast.viewModel.MachineDetailsActivityViewModel
import com.prplmnstr.technoplast.viewModel.OperatorActivityViewModel
import java.util.Calendar


private lateinit var viewModel: MachineDetailsActivityViewModel
private lateinit var loader: Dialog
private lateinit var binding : ActivityMachineDetailsBinding
private var isImage1Visible = true
private var day_mode = true
private var dayShiftRecord=Record()
private var nightShiftRecord=Record()
private var todayDate = Helper.getTodayDateObject().dateInStringFormat
private lateinit var machineName:String
private val createPdf = CreatePdf()

class MachineDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMachineDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MachineDetailsActivityViewModel::class.java)

        val machineJson = intent.getStringExtra(Constants.MACHINE)
        val recievedMacine = Gson().fromJson(machineJson, Machine::class.java)
        machineName = recievedMacine.name

        setMachineDetails(recievedMacine)
        initializeLoader()
        binding.spinner1.text = todayDate
        loadData(todayDate)



        binding.switchButton.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            day_mode = !day_mode
            if(day_mode){
                loadUIWithShift(dayShiftRecord)
                Toast.makeText(this, "Day Shift", Toast.LENGTH_SHORT).show()
            }else{
                loadUIWithShift(nightShiftRecord)
                Toast.makeText(this, "Night Shift", Toast.LENGTH_SHORT).show()
            }
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


        binding.spinner1.setOnClickListener {
            showDatePickerDialog()

        }

        binding.downloadIv.setOnClickListener {
            createPdf.createPdf(this,this, dayShiftRecord, nightShiftRecord)
        }

        binding.backIv.setOnClickListener {
            day_mode =true
            finish()
        }
//        binding.submitBtn.setOnClickListener {
//            if(day_mode==true)
//            updateShiftData(dayShiftRecord,Machine())
//            else{
//                updateShiftData(nightShiftRecord,Machine())
//            }
//        }
    }

    private fun loadUIWithShift(shiftRecord: Record) {
        binding.mouldEt.setText(shiftRecord.mould)
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
        binding.reasonEt.setText(shiftRecord.reason)


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

        binding.totalTv.text = Constants.OPERATOR + " : " + shiftRecord.operator
    }


private fun updateShiftData(shiftRecord: Record, selectedMachine:Machine){
    shiftRecord.mould = binding.mouldEt.text.toString()
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

    selectedMachine.name = shiftRecord.name
//    selectedMachine.mould = shiftRecord.mould
//    selectedMachine.productionWT = binding.productinWtEt.text.toString()
//    selectedMachine.article = binding.articleEt.text.toString()
//    selectedMachine.orderQty = binding.orderQtyEt.text.toString()
//    selectedMachine.loadTime = binding.mLoadTimeEt.text.toString()
//    selectedMachine.unloadTime = binding.mUnloadTimeEt.text.toString()
//    selectedMachine.numCavity = binding.cavityEt.text.toString()
//    selectedMachine.heating = binding.heatingEt.text.toString()
//    selectedMachine.heatingAct = binding.heatingActEt.text.toString()
//    selectedMachine.rawMaterial = binding.rawMaterialEt.text.toString()
//    selectedMachine.totalMaterialUsed = binding.materialUsedEt.text.toString()
//    selectedMachine.pigment = binding.pigmentEt.text.toString()
//    selectedMachine.totalMbUsed = binding.mbUsedEt.text.toString()

    loader.show()
    viewModel.sendUserDataToFirestore(shiftRecord,selectedMachine) { isSuccess ->
        if (isSuccess) {
            showToast("Production report updated successfully!")
        } else {
            showToast("Failed to update report.")
        }

        loader.dismiss()
    }

}

    private fun loadData(date:String) {

        bringDayShiftData(date)
        bringNightShiftData(date)
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

    private fun bringNightShiftData(date:String) {
        loader.show()
        viewModel.retrieveRecord(date, machineName, Constants.NIGHT_SHIFT)
            .observe(this, Observer { record ->
                // Handle the retrieved record here
                if (record != null) {
                    nightShiftRecord = record

                } else {
                    nightShiftRecord = Record()
                    nightShiftRecord.name = machineName
                    nightShiftRecord.shift = Constants.NIGHT_SHIFT

                }
                loader.dismiss()
            })
    }
    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }


    private fun bringDayShiftData(date:String) {
        loader.show()
        viewModel.retrieveRecord(date, machineName, Constants.DAY_SHIFT)
            .observe(this, Observer { record ->
                // Handle the retrieved record here
                if (record != null) {

                    dayShiftRecord = record
                    loadUIWithShift(dayShiftRecord)

                } else {
                    dayShiftRecord = Record()
                    dayShiftRecord.name = machineName
                    dayShiftRecord.shift = Constants.DAY_SHIFT

                }
                loader.dismiss()
            })
    }

    private fun setMachineDetails(recievedMacine: Machine) {
        binding.machineNameTv.text = recievedMacine.name

            binding.machineNameEt.setText(recievedMacine.name)
           // binding.mouldEt.setText(recievedMacine.mould)
            //binding.productinWtEt.setText(recievedMacine.productionWT)
           // binding.orderQtyEt.setText(recievedMacine.orderQty)
           // binding.mLoadTimeEt.setText(recievedMacine.loadTime)
           // binding.mUnloadTimeEt.setText(recievedMacine.unloadTime)
           // binding.articleEt.setText(recievedMacine.article)
            //binding.heatingEt.setText(recievedMacine.heating)
           // binding.cavityEt.setText(recievedMacine.numCavity)
           // binding.heatingActEt.setText(recievedMacine.heatingAct)
            //binding.rawMaterialEt.setText(recievedMacine.rawMaterial)
            //binding.materialUsedEt.setText(recievedMacine.totalMaterialUsed)
            //binding.pigmentEt.setText(recievedMacine.pigment)
            //binding.mbUsedEt.setText(recievedMacine.totalMbUsed)

    }

    fun showDatePickerDialog() {
        // Get the current date

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog and set the listener
        val datePickerDialog = DatePickerDialog(
           this,
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                // Update the selected date in your UI
              var  recordDate = Date(day,month+1,year,Helper.getDateInStringFormat(day, month + 1, year))

                binding.spinner1.text = recordDate.dateInStringFormat
                loadData(recordDate.dateInStringFormat)

            },
            year, month, dayOfMonth
        )

        // Show the date picker dialog
        datePickerDialog.show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        day_mode = true
        //binding.switchButton.isChecked = false
        finish()
    }
}
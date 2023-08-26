package com.prplmnstr.technoplast.views.admin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.prplmnstr.technoplast.R
import com.prplmnstr.technoplast.adapters.MachineItemAdapter
import com.prplmnstr.technoplast.adapters.MouldItemAdapter
import com.prplmnstr.technoplast.adapters.OperatorItemAdapter
import com.prplmnstr.technoplast.databinding.AddMachineDialogBinding
import com.prplmnstr.technoplast.databinding.AddMouldDialogBinding
import com.prplmnstr.technoplast.databinding.AddUserDialogBinding
import com.prplmnstr.technoplast.databinding.FragmentAddWorkerBinding
import com.prplmnstr.technoplast.databinding.FragmentMachinesBinding
import com.prplmnstr.technoplast.databinding.UpdateMouldDialogBinding
import com.prplmnstr.technoplast.models.Machine
import com.prplmnstr.technoplast.models.Mould
import com.prplmnstr.technoplast.models.Operator
import com.prplmnstr.technoplast.utils.SwipeToDeleteCallback
import com.prplmnstr.technoplast.viewModel.AddUserFragmentViewModel
import com.prplmnstr.technoplast.viewModel.MachineFragmentViewModel
import com.prplmnstr.technoplast.views.LoginActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var viewModel: MachineFragmentViewModel
private lateinit var binding: FragmentMachinesBinding
private lateinit var addMachineDialog: Dialog
private lateinit var addMouldDialog: Dialog
private lateinit var updateMouldDialog: Dialog
private lateinit var loader: Dialog
private lateinit  var addMachineDialogBinding: AddMachineDialogBinding
private lateinit  var addMouldDialogBinding: AddMouldDialogBinding
private lateinit  var updateMouldDialogBinding: UpdateMouldDialogBinding
private lateinit var machineAdapter: MachineItemAdapter
private lateinit var mouldAdapter: MouldItemAdapter
private val machineList = mutableListOf<Machine>()
private val mouldList = mutableListOf<Mould>()

class MachinesFragment : Fragment(), MouldItemAdapter.OnMouldItemClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MachineFragmentViewModel::class.java)

        loadDialog()
        loadAddMouldDialog()
        loadUpadateMouldDialog()
        initializeLoader()
        loadMachines()


        mouldAdapter = MouldItemAdapter(mouldList,this)


        binding.mouldRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = mouldAdapter
        }

        binding.addImage.setOnClickListener {
          displayDailog()

        }
        binding.noPlantAddButton.setOnClickListener {
                 displayDailog()

        }
        binding.logoutImage.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireContext(), view)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.machine_fragment_menu, popupMenu.menu)
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.addMachine ->{displayDailog()}
                    R.id.addMould ->{displayAddMouldDailog()
                    }
                    R.id.logout_menu_item ->{ val firebaseAuth = FirebaseAuth.getInstance()
                        firebaseAuth.signOut()
                        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

                        // You can add your SharedPreferences logic here
                        // ...
                        viewModel.removeUserTypeFromSharedPref(requireContext())
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()}
                }

                true
            }
        }


    }



    private fun initializeLoader() {
        loader = Dialog(requireActivity())
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
        machineAdapter = MachineItemAdapter(machineList)


        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = machineAdapter
        }


        viewModel.retrieveMachines().observe(viewLifecycleOwner) { retrievedList ->
            if (retrievedList.isEmpty()) {
                binding.scroller.visibility = View.GONE
                binding.noPlantsLayout.visibility = View.VISIBLE
            } else {
                machineList.clear()
                machineList.addAll(retrievedList)
                machineAdapter.notifyDataSetChanged()
                loadMoulds()
                binding.scroller.visibility = View.VISIBLE
                binding.noPlantsLayout.visibility = View.GONE
            }
            loader.dismiss()
        }
    }


    private fun loadMoulds() {
        loader.show()
        mouldAdapter = MouldItemAdapter(mouldList,this)


        binding.mouldRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = mouldAdapter
        }


        viewModel.retrieveMoulds().observe(viewLifecycleOwner) { retrievedList ->
            if (retrievedList.isEmpty()) {
               // binding.scroller.visibility = View.GONE
               // binding.noPlantsLayout.visibility = View.VISIBLE
            } else {
                mouldList.clear()
                mouldList.addAll(retrievedList)
                mouldAdapter.notifyDataSetChanged()

               // binding.scroller.visibility = View.VISIBLE
               // binding.noPlantsLayout.visibility = View.GONE
            }
            loader.dismiss()
        }
    }
    private fun loadDialog() {
        addMachineDialogBinding = AddMachineDialogBinding.inflate(layoutInflater)
        addMachineDialog = Dialog(requireContext())
        addMachineDialog.setContentView(addMachineDialogBinding.root)
        addMachineDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        addMachineDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        addMachineDialog.setCancelable(true)
        addMachineDialog.window?.attributes?.windowAnimations = R.style.animation_popup



    }
    private fun loadAddMouldDialog() {
        addMouldDialogBinding = AddMouldDialogBinding.inflate(layoutInflater)
        addMouldDialog = Dialog(requireContext())
        addMouldDialog.setContentView(addMouldDialogBinding.root)
        addMouldDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        addMouldDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        addMouldDialog.setCancelable(true)
        addMouldDialog.window?.attributes?.windowAnimations = R.style.animation_popup



    }

    private fun loadUpadateMouldDialog() {
        updateMouldDialogBinding = UpdateMouldDialogBinding.inflate(layoutInflater)
        updateMouldDialog = Dialog(requireContext())
        updateMouldDialog.setContentView(updateMouldDialogBinding.root)
        updateMouldDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        updateMouldDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        updateMouldDialog.setCancelable(true)
        updateMouldDialog.window?.attributes?.windowAnimations = R.style.animation_popup



    }
    private fun displayDailog() {

        addMachineDialog.show()



        addMachineDialogBinding.dialogButton.setOnClickListener {
            val machineName = addMachineDialogBinding.machineNameEt.text.toString().trim()
            if (machineName.isEmpty()){
                addMachineDialogBinding.machineNameEt.error = "Enter Machine Name"
                Toast.makeText(requireContext(), "Enter machine name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var oldMachine = false
            if(!machineList.isEmpty()){
                oldMachine = viewModel.isMachineExist(machineList,machineName)
            }

            if (oldMachine) {
                Toast.makeText(requireContext(), "Machine name already exist.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            } else {
                loader.show()
                val newMachine = Machine(machineName)
                viewModel.uploadMachine(newMachine) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(requireContext(), "Machine Details added successfully!", Toast.LENGTH_SHORT).show()

                        machineList.add(newMachine)
                        machineAdapter.notifyDataSetChanged()
                        if(!machineList.isEmpty()){
                            binding.scroller.visibility = View.VISIBLE
                            binding.noPlantsLayout.visibility = View.GONE
                        }
                        addMachineDialog.dismiss()
                        loader.dismiss()

                    } else {
                        Toast.makeText(requireContext(), "Failed to add machine.", Toast.LENGTH_SHORT).show()
                    }
                }



            }

        }


    }

    private fun displayAddMouldDailog() {

        addMouldDialog.show()



        addMouldDialogBinding.dialogButton.setOnClickListener {
            val mouldName = addMouldDialogBinding.mouldEt.text.toString().trim()
            val production = addMouldDialogBinding.productinWtEt.text.toString().trim()
            val orderQty = addMouldDialogBinding.orderQtyEt.text.toString().trim()
            val article = addMouldDialogBinding.articleEt.text.toString().trim()
            val heating = addMouldDialogBinding.heatingEt.text.toString().trim()
            val cavity = addMouldDialogBinding.cavityEt.text.toString().trim()
            val heatingAct = addMouldDialogBinding.heatingActEt.text.toString().trim()
            val rawMaterial = addMouldDialogBinding.rawMaterialEt.text.toString().trim()
            val materialUsed = addMouldDialogBinding.materialUsedEt.text.toString().trim()
            val pigment = addMouldDialogBinding.pigmentEt.text.toString().trim()
            val mbUsed = addMouldDialogBinding.mbUsedEt.text.toString().trim()
            if (mouldName.isEmpty()){
                addMouldDialogBinding.mouldEt.error = "Enter Mould Name"
                Toast.makeText(requireContext(), "Enter Mould name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var oldMould = false
            if(!mouldList.isEmpty()){
                oldMould = viewModel.isMouldExist(mouldList,mouldName)
            }

            if (oldMould) {
                Toast.makeText(requireContext(), "Mould name already exist.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }else if(production.isEmpty() ||
                orderQty.isEmpty() ||
                article.isEmpty() ||
                heating.isEmpty() ||
                cavity.isEmpty() ||
                heatingAct.isEmpty() ||
                rawMaterial.isEmpty() ||
                materialUsed.isEmpty() ||
                pigment.isEmpty() ||
                mbUsed.isEmpty()
                    ){
                Toast.makeText(requireContext(), "Fill all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                loader.show()
                val newMould = Mould(mouldName,production,orderQty,"","",
                article,heating,cavity,heatingAct, rawMaterial,materialUsed,pigment,mbUsed)
                viewModel.uploadMould(newMould) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(requireContext(), "Mould Details added successfully!", Toast.LENGTH_SHORT).show()

                        mouldList.add(newMould)
                        mouldAdapter.notifyDataSetChanged()
//                        if(!mouldList.isEmpty()){
//                            binding.scroller.visibility = View.VISIBLE
//                            binding.noPlantsLayout.visibility = View.GONE
//                        }
                        addMouldDialog.dismiss()
                        loader.dismiss()

                    } else {
                        Toast.makeText(requireContext(), "Failed to add mould.", Toast.LENGTH_SHORT).show()
                    }
                }



            }

        }


    }
    private fun displayUpdateMouldDailog(position: Int) {

        updateMouldDialogBinding.mouldEt.setText( mouldList[position].name)
        updateMouldDialogBinding.productinWtEt.setText( mouldList[position].productionWT)
        updateMouldDialogBinding.orderQtyEt.setText( mouldList[position].orderQty)
        updateMouldDialogBinding.articleEt.setText( mouldList[position].article)
        updateMouldDialogBinding.heatingEt.setText( mouldList[position].heating)
        updateMouldDialogBinding.cavityEt.setText( mouldList[position].numCavity)
        updateMouldDialogBinding.heatingActEt.setText( mouldList[position].heatingAct)
        updateMouldDialogBinding.rawMaterialEt.setText( mouldList[position].rawMaterial)
        updateMouldDialogBinding.materialUsedEt.setText( mouldList[position].totalMaterialUsed)
        updateMouldDialogBinding.pigmentEt.setText( mouldList[position].pigment)
        updateMouldDialogBinding.mbUsedEt.setText( mouldList[position].totalMbUsed)

        updateMouldDialog.show()



        updateMouldDialogBinding.dialogButton.setOnClickListener {
            val mouldName = updateMouldDialogBinding.mouldEt.text.toString().trim()
            val production = updateMouldDialogBinding.productinWtEt.text.toString().trim()
            val orderQty = updateMouldDialogBinding.orderQtyEt.text.toString().trim()
            val article = updateMouldDialogBinding.articleEt.text.toString().trim()
            val heating = updateMouldDialogBinding.heatingEt.text.toString().trim()
            val cavity = updateMouldDialogBinding.cavityEt.text.toString().trim()
            val heatingAct = updateMouldDialogBinding.heatingActEt.text.toString().trim()
            val rawMaterial = updateMouldDialogBinding.rawMaterialEt.text.toString().trim()
            val materialUsed = updateMouldDialogBinding.materialUsedEt.text.toString().trim()
            val pigment = updateMouldDialogBinding.pigmentEt.text.toString().trim()
            val mbUsed = updateMouldDialogBinding.mbUsedEt.text.toString().trim()


             if(production.isEmpty() ||
                orderQty.isEmpty() ||
                article.isEmpty() ||
                heating.isEmpty() ||
                cavity.isEmpty() ||
                heatingAct.isEmpty() ||
                rawMaterial.isEmpty() ||
                materialUsed.isEmpty() ||
                pigment.isEmpty() ||
                mbUsed.isEmpty()
            ){
                Toast.makeText(requireContext(), "Fill all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                loader.show()
                val newMould = Mould(mouldName,production,orderQty,"","",
                    article,heating,cavity,heatingAct, rawMaterial,materialUsed,pigment,mbUsed)
                viewModel.uploadMould(newMould) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(requireContext(), "Mould Details updated successfully!", Toast.LENGTH_SHORT).show()

                        mouldList[position] = newMould
                        mouldAdapter.notifyItemChanged(position)
//                        if(!mouldList.isEmpty()){
//                            binding.scroller.visibility = View.VISIBLE
//                            binding.noPlantsLayout.visibility = View.GONE
//                        }
                        updateMouldDialog.dismiss()
                        loader.dismiss()

                    } else {
                        Toast.makeText(requireContext(), "Failed to update mould.", Toast.LENGTH_SHORT).show()
                    }
                }



            }

        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMachinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MachinesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(position: Int) {
        displayUpdateMouldDailog(position)
    }
}
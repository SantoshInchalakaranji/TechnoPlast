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
import com.prplmnstr.technoplast.adapters.OperatorItemAdapter
import com.prplmnstr.technoplast.databinding.AddMachineDialogBinding
import com.prplmnstr.technoplast.databinding.AddUserDialogBinding
import com.prplmnstr.technoplast.databinding.FragmentAddWorkerBinding
import com.prplmnstr.technoplast.databinding.FragmentMachinesBinding
import com.prplmnstr.technoplast.models.Machine
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
private lateinit var loader: Dialog
private lateinit  var addMachineDialogBinding: AddMachineDialogBinding
private lateinit var machineAdapter: MachineItemAdapter
private val machineList = mutableListOf<Machine>()

class MachinesFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MachineFragmentViewModel::class.java)

        loadDialog()
        initializeLoader()
        loadMachines()

        binding.addImage.setOnClickListener {
          displayDailog()

        }
        binding.noPlantAddButton.setOnClickListener {
                 displayDailog()

        }
        binding.logoutImage.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireContext(), view)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.logout_menu, popupMenu.menu)
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener { menuItem ->
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signOut()
                Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

                // You can add your SharedPreferences logic here
                // ...
                viewModel.removeUserTypeFromSharedPref(requireContext())
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
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

                binding.scroller.visibility = View.VISIBLE
                binding.noPlantsLayout.visibility = View.GONE
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
    private fun displayDailog() {
        var mouldEtText =""
        var productionEtText=""
        var orderQtyEtText=""
        var loadTimeEtText=""
        var unloadTimeEtText=""
        var articleEtText=""
        var heatingEtText=""
        var cavityEtText=""
        var heatingActEtText=""
        var rawMaterialEtText=""
        var materialUsedEtText=""
        var pigmentEtText=""
        var mbUsedEtText=""
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

            if (!oldMachine) {
                mouldEtText = addMachineDialogBinding.mouldEt.text.toString().trim()
                productionEtText = addMachineDialogBinding.productinWtEt.text.toString().trim()
                orderQtyEtText = addMachineDialogBinding.orderQtyEt.text.toString().trim()
                loadTimeEtText = addMachineDialogBinding.mLoadTimeEt.text.toString().trim()
                unloadTimeEtText = addMachineDialogBinding.mUnloadTimeEt.text.toString().trim()
                heatingEtText = addMachineDialogBinding.heatingEt.text.toString().trim()
                articleEtText = addMachineDialogBinding.articleEt.text.toString().trim()
                cavityEtText = addMachineDialogBinding.cavityEt.text.toString().trim()
                heatingActEtText = addMachineDialogBinding.heatingActEt.text.toString().trim()
                rawMaterialEtText = addMachineDialogBinding.rawMaterialEt.text.toString().trim()
                materialUsedEtText = addMachineDialogBinding.materialUsedEt.text.toString().trim()
                pigmentEtText = addMachineDialogBinding.pigmentEt.text.toString().trim()
                mbUsedEtText = addMachineDialogBinding.mbUsedEt.text.toString().trim()


            } else {
                Toast.makeText(requireContext(), "Machine name already exist.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mouldEtText.isEmpty()) {
                addMachineDialogBinding.mouldEt.error = "Enter Mould Name"
            }else {
                loader.show()
                val newMachine = Machine(machineName,mouldEtText,productionEtText,orderQtyEtText,
                loadTimeEtText,unloadTimeEtText,articleEtText,heatingEtText,
                cavityEtText,heatingActEtText,rawMaterialEtText,
                materialUsedEtText,pigmentEtText,mbUsedEtText)
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
                        Toast.makeText(requireContext(), "Failed to add operator.", Toast.LENGTH_SHORT).show()
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
}
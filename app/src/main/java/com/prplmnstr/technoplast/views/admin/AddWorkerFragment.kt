package com.prplmnstr.technoplast.views.admin

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.prplmnstr.technoplast.R
import com.prplmnstr.technoplast.adapters.OperatorItemAdapter
import com.prplmnstr.technoplast.databinding.AddUserDialogBinding
import com.prplmnstr.technoplast.databinding.FragmentAddWorkerBinding
import com.prplmnstr.technoplast.databinding.OperatorItemBinding
import com.prplmnstr.technoplast.models.Operator
import com.prplmnstr.technoplast.utils.SwipeToDeleteCallback
import com.prplmnstr.technoplast.viewModel.AddUserFragmentViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddWorkerFragment : Fragment() , OperatorItemAdapter.DeleteCallback{

    private lateinit var viewModel: AddUserFragmentViewModel
    private lateinit var binding: FragmentAddWorkerBinding
    private lateinit var addUserDialog: Dialog
    private lateinit var loader: Dialog
    private lateinit  var addUserDialogBinding: AddUserDialogBinding
    private lateinit var userAdapter: OperatorItemAdapter
    private val operatorList = mutableListOf<Operator>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddUserFragmentViewModel::class.java)



        loadDialog()
        initializeLoader()
        loadOperators()

        binding.addUserButton.setOnClickListener {
           displayDailog()

        }
    }

    private fun loadOperators() {
        loader.show()
        userAdapter = OperatorItemAdapter(operatorList,viewModel)
        userAdapter.callback = this
        val swipeToDeleteCallback = SwipeToDeleteCallback(userAdapter)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.workersRV)

        binding.workersRV.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = userAdapter
        }


        viewModel.retrieveUserDataFromFirestore().observe(viewLifecycleOwner) { retrievedList ->
            if (retrievedList.isEmpty()) {
                binding.workersRV.visibility = View.GONE
                binding.workerEmptyLayout.visibility = View.VISIBLE
            } else {
                operatorList.clear()
                operatorList.addAll(retrievedList)
                userAdapter.notifyDataSetChanged()

                binding.workersRV.visibility = View.VISIBLE
                binding.workerEmptyLayout.visibility = View.GONE
            }

            loader.dismiss()
        }
    }


    private fun loadDialog() {
        addUserDialogBinding = AddUserDialogBinding.inflate(layoutInflater)
        addUserDialog = Dialog(requireContext())
        addUserDialog.setContentView(addUserDialogBinding.root)
        addUserDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        addUserDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        addUserDialog.setCancelable(true)
        addUserDialog.window?.attributes?.windowAnimations = R.style.animation_popup



    }
    private fun displayDailog() {
        var userName:String =""
        var userPassword: String=""
        addUserDialog.show()



        addUserDialogBinding.dialogButton.setOnClickListener {
           val userEmail = addUserDialogBinding.emailET.text.toString().trim().toLowerCase()
            var oldUser = false
                if(!operatorList.isEmpty()){
                oldUser = viewModel.isEmailExists(operatorList,userEmail)
            }

            if (!oldUser) {
                userName = addUserDialogBinding.nameET.text.toString().trim()
                userPassword = addUserDialogBinding.passwordET.text.toString()

            } else {
                Toast.makeText(requireContext(), "Supervisor email already exist.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!userEmail.endsWith("@gmail.com")) {
                addUserDialogBinding.emailET.error = "Enter valid Email Address"
            } else if (userName.isEmpty()) {
                addUserDialogBinding.nameET.error = "Enter Supervisor Name"
            } else if (userPassword.length < 8) {
                addUserDialogBinding.passwordET.error = "Password should be at least 8 characters"
            } else {
                loader.show()
                viewModel.sendUserDataToFirestore(userName, userEmail,userPassword) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(requireContext(), "Supervisor Details added successfully!", Toast.LENGTH_SHORT).show()
                        val operator = Operator(userName,userEmail, userPassword)
                        viewModel.signUpUser(userEmail,userPassword,requireContext())
                        operatorList.add(operator)
                        userAdapter.notifyDataSetChanged()
                        if(!operatorList.isEmpty()){
                            binding.workersRV.visibility = View.VISIBLE
                            binding.workerEmptyLayout.visibility = View.GONE
                        }
                        addUserDialog.dismiss()
                        loader.dismiss()

                    } else {
                        Toast.makeText(requireContext(), "Failed to add Supervisor.", Toast.LENGTH_SHORT).show()
                    }
                }



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

    private fun reloadOperators() {
       // TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWorkerBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddWorkerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemDeleted(position: Int) {
        if(operatorList.isEmpty()){
            binding.workersRV.visibility = View.GONE
            binding.workerEmptyLayout.visibility = View.VISIBLE
        }
    }
}
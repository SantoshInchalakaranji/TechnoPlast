package com.prplmnstr.technoplast.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.prplmnstr.technoplast.R
import com.prplmnstr.technoplast.databinding.MachineItemBinding
import com.prplmnstr.technoplast.databinding.OperatorItemBinding
import com.prplmnstr.technoplast.models.Machine
import com.prplmnstr.technoplast.models.Operator
import com.prplmnstr.technoplast.utils.Constants
import com.prplmnstr.technoplast.viewModel.AddUserFragmentViewModel
import com.prplmnstr.technoplast.views.admin.MachineDetailsActivity

class MachineItemAdapter(private val machineList: MutableList<Machine>) : RecyclerView.Adapter<MachineItemAdapter.MachineViewHolder>() {

    private lateinit var binding: MachineItemBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MachineViewHolder {
        context = parent.context
        binding = MachineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MachineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MachineViewHolder, position: Int) {
        val currentMachine = machineList[position]
        holder.bind(currentMachine)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MachineDetailsActivity::class.java)
            val userJson = Gson().toJson(currentMachine) // Convert to JSON string
            intent.putExtra(Constants.MACHINE, userJson)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return machineList.size
    }

    inner class MachineViewHolder(private val binding: MachineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(machine: Machine) {
            binding.machineNameTv.text = machine.name
            binding.mouldTv.text = machine.mould



        }
    }
}
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
import com.prplmnstr.technoplast.databinding.MouldItemBinding
import com.prplmnstr.technoplast.databinding.OperatorItemBinding
import com.prplmnstr.technoplast.models.Machine
import com.prplmnstr.technoplast.models.Mould
import com.prplmnstr.technoplast.models.Operator
import com.prplmnstr.technoplast.utils.Constants
import com.prplmnstr.technoplast.viewModel.AddUserFragmentViewModel
import com.prplmnstr.technoplast.views.admin.MachineDetailsActivity

class MouldItemAdapter(private val mouldList: MutableList<Mould>,private val listener: OnMouldItemClickListener) : RecyclerView.Adapter<MouldItemAdapter.MouldViewHolder>() {

    private lateinit var binding: MouldItemBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MouldViewHolder {
        context = parent.context
        binding = MouldItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MouldViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MouldViewHolder, position: Int) {
        val currentMould = mouldList[position]
        holder.bind(currentMould)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            listener.onItemClick(position)
            //load dialog,

        }
    }

    override fun getItemCount(): Int {
        return mouldList.size
    }

    inner class MouldViewHolder(private val binding: MouldItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mould: Mould) {
            binding.mouldNameTv.text = mould.name
            //binding.mouldTv.text = machine.mould



        }
    }
    interface OnMouldItemClickListener {
        fun onItemClick(position: Int)
    }
}
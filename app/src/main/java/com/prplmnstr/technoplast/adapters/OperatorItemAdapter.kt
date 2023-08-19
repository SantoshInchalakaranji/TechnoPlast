package com.prplmnstr.technoplast.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.prplmnstr.technoplast.R
import com.prplmnstr.technoplast.databinding.OperatorItemBinding
import com.prplmnstr.technoplast.models.Operator
import com.prplmnstr.technoplast.viewModel.AddUserFragmentViewModel

class OperatorItemAdapter(private val userList: MutableList<Operator>,val viewModel: AddUserFragmentViewModel) : RecyclerView.Adapter<OperatorItemAdapter.UserViewHolder>() {

    private lateinit var binding: OperatorItemBinding
    private lateinit var context: Context
    var callback: DeleteCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        context = parent.context
         binding = OperatorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.bind(currentUser)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(private val binding: OperatorItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(operator: Operator) {
            binding.nameTV.text = operator.name
            binding.emailTV.text = operator.email
            binding.passwordTV.text = operator.password
        }
    }
    fun showDeleteConfirmationDialog(position: Int) {
        val context = binding.root.context
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Delete") { _, _ ->
                deleteItem(position)
            }
            .setNegativeButton("Cancel") { _, _ ->
                notifyDataSetChanged() // Reload data to cancel the deletion
            }


            .create()

        alertDialog.setOnCancelListener{
            notifyDataSetChanged()
        }
        alertDialog.show()
    }
    fun deleteItem(position: Int) {

        viewModel.deleteOperatorByDocumentName(userList[position].email ){isSuccess ->

            if(isSuccess){

                Toast.makeText(context,"Deleted successfully.",Toast.LENGTH_SHORT)
            }else{
                Toast.makeText(context,"Something went wrong, please try again.",Toast.LENGTH_SHORT)
            }
        }
        userList.removeAt(position)
        notifyItemRemoved(position)
        callback?.onItemDeleted(position)
    }
    interface DeleteCallback {
        fun onItemDeleted(position: Int)
    }
}











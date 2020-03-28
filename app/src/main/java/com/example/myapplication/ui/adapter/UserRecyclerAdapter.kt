package com.example.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemUserBinding
import com.example.myapplication.datasource.local.database.entity.UserEntity

/**
 * Created by Athenriel on 26/03/2020.
 */
class UserRecyclerAdapter(
    var users: List<UserEntity>,
    private val userRecyclerClickListener: UserRecyclerListener?
) :
    RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class UserViewHolder(
        private val parent: ViewGroup,
        private val binding: ItemUserBinding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userEntity: UserEntity) {
            binding.itemUserNameTv.text = userEntity.firstName
            binding.itemUserLastNameTv.text = userEntity.lastName
            binding.itemUserRemoveBtn.setOnClickListener {
                userRecyclerClickListener?.deleteUser(userEntity)
            }
        }
    }
}

interface UserRecyclerListener {
    fun deleteUser(user: UserEntity)
}

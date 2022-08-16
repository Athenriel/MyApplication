package com.example.myapplication.ui.cuenta.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemTitleBinding
import com.example.myapplication.databinding.ItemUserBinding
import com.example.myapplication.datasource.local.database.entity.UserEntity

/**
 * Created by Athenriel on 26/03/2020.
 */
class UserRecyclerAdapter(
        var users: List<UserEntity>,
        private val userRecyclerClickListener: UserRecyclerListener?
) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_TITLE = 100
        private const val TYPE_USER = 200
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> {
                TitleViewHolder(parent)
            }
            TYPE_USER -> {
                UserViewHolder(parent)
            }
            else -> {
                TitleViewHolder(parent)
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_TITLE
        } else {
            TYPE_USER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> {
                holder.bind(users[position - 1])
            }
            is TitleViewHolder -> {
                holder.bind()
            }
        }
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

    inner class TitleViewHolder(
            private val parent: ViewGroup,
            private val binding: ItemTitleBinding = ItemTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
            )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.cuentaTituloTv.text = "Title"
        }
    }
}

interface UserRecyclerListener {
    fun deleteUser(user: UserEntity)
}

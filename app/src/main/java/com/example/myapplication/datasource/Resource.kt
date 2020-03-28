package com.example.myapplication.datasource

import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 * Created by Athenriel on 25/03/2020.
 */
class Resource<ObjType, ErrorType>(
    @param:NonNull @field:NonNull
    val status: Status, @param:Nullable @field:Nullable
    val data: ObjType?, @param:Nullable @field:Nullable
    val error: ErrorType?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        } else if (other != null && this.javaClass == other.javaClass) {
            val var2 = other as Resource<*, *>?
            if (this.status !== var2!!.status) {
                return false
            } else {
                if (this.error != null) {
                    if (this.error == var2!!.error) {
                        return if (this.data != null) this.data == var2.data else var2.data == null
                    }
                } else if (var2!!.error == null) {
                    return if (this.data != null) this.data == var2.data else var2.data == null
                }

                return false
            }
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        var var1 = this.status.hashCode()
        var1 = 31 * var1 + if (this.error != null) this.error.hashCode() else 0
        var1 = 31 * var1 + if (this.data != null) this.data.hashCode() else 0
        return var1
    }

    override fun toString(): String {
        return "Resource{status=" + this.status + ", error='" + this.error + '\''.toString() + ", data=" + this.data + '}'.toString()
    }

    companion object {

        fun <ObjType, ErrorType> success(@Nullable var0: ObjType): Resource<ObjType, ErrorType> {
            return Resource(
                Status.SUCCESS,
                var0,
                null as ErrorType?
            )
        }

        fun <ObjType, ErrorType> error(var0: ErrorType, @Nullable var1: ObjType): Resource<ObjType, ErrorType> {
            return Resource(
                Status.ERROR,
                var1,
                var0
            )
        }

        fun <ObjType, ErrorType> loading(@Nullable var0: ObjType): Resource<ObjType, ErrorType> {
            return Resource(
                Status.LOADING,
                var0,
                null as ErrorType?
            )
        }

        fun <ObjType, ErrorType> nullResource(): Resource<ObjType, ErrorType>? {
            val var0 = null
            return var0
        }
    }

}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

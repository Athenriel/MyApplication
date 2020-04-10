package com.example.myapplication.datasource

/**
 * Created by Athenriel on 09/04/2020.
 */
data class Resource<ObjType, ErrorType>(
    val data: ObjType?,
    val error: ErrorType?,
    val errorExtra: String?
) {
    companion object {
        fun <ObjType, ErrorType> success(objType: ObjType): Resource<ObjType, ErrorType> {
            return Resource(
                objType,
                null,
                null
            )
        }

        fun <ObjType, ErrorType> error(
            errorType: ErrorType,
            errorExtra: String?
        ): Resource<ObjType, ErrorType> {
            return Resource(
                null,
                errorType,
                errorExtra
            )
        }
    }
}

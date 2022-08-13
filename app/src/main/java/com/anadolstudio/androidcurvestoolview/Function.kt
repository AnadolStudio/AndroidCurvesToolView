package com.anadolstudio.library

interface Function {

    fun getFunction(): String


    object Empty : Function {

        override fun getFunction(): String = ""
    }

}

package com.anadolstudio.androidcurvestoolview.function

interface Function {

    fun getFunction(): String


    object Empty : Function {

        override fun getFunction(): String = ""
    }

}

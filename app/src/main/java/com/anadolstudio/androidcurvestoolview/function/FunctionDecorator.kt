package com.anadolstudio.androidcurvestoolview.function

import com.anadolstudio.library.util.StringConst.SPACE
import com.anadolstudio.library.util.ifNotEmpty

abstract class FunctionDecorator(private val function: Function) : Function {

    abstract fun type(): String

    override fun getFunction(): String =
        function.getFunction().ifNotEmpty { SPACE } + type() + SPACE

}

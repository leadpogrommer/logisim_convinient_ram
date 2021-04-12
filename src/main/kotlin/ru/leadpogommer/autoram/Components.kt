package ru.leadpogommer.autoram

import com.cburch.logisim.tools.AddTool
import com.cburch.logisim.tools.Library
import com.cburch.logisim.tools.Tool
import java.lang.System.getProperty
import java.nio.file.Paths
import java.lang.System.setProperty

class Components : Library() {
    private val tools = mutableListOf<Tool>(AddTool(Autoram()), AddTool(Gamepad()))


    override fun getTools(): MutableList<out Tool> {
        return tools;
    }

    override fun getDisplayName(): String {
        return "AutoRAM"
    }

}
package ru.leadpogommer.autoram

import com.cburch.logisim.tools.AddTool
import com.cburch.logisim.tools.Library
import com.cburch.logisim.tools.Tool

class Components : Library() {
    private val tools = mutableListOf<Tool>(AddTool(Autoram()))


    override fun getTools(): MutableList<out Tool> {
        return tools;
    }

    override fun getDisplayName(): String {
        return "AutoRAM"
    }
}
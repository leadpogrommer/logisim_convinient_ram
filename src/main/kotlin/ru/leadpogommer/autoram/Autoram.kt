package ru.leadpogommer.autoram

import com.cburch.logisim.data.Attribute
import com.cburch.logisim.data.AttributeSet
import com.cburch.logisim.data.AttributeSets
import com.cburch.logisim.data.Attributes
import com.cburch.logisim.instance.Instance
import com.cburch.logisim.instance.InstanceState
import com.cburch.logisim.std.memory.Ram
import java.io.File
import java.io.IOException

class Autoram:  Ram(){
    override fun createAttributeSet(): AttributeSet {
        val superAttribs = super.createAttributeSet()
        val attribs = mutableListOf<Attribute<*>>()
        val defaults = mutableListOf<Any>()
        superAttribs.attributes.forEach{
            attribs.add(it)
            defaults.add(superAttribs.getValue(it))
        }
        attribs.add(PATH_ATTRIB)
        defaults.add("")
        return AttributeSets.fixedSet(attribs.toTypedArray(), defaults.toTypedArray())
    }
    override fun loadImage(instanceState: InstanceState?, imageFile: File?) {
        instanceState?.attributeSet?.setValue(PATH_ATTRIB, imageFile!!.absolutePath)
        super.loadImage(instanceState, imageFile)
    }

    override fun propagate(state: InstanceState?) {
        if(state?.data == null){
            println("Newly created")
            val filename = state?.attributeSet?.getValue(PATH_ATTRIB)
            println(filename)
            filename?.let {
                try {
                    val file = File(filename)
                    loadImage(state, file)
                    println("Loaded image file $filename")

                }catch (e: IOException){
                    println("Image file not found!")
                }
            }
        }

        super.propagate(state)
    }



    companion object{
        val PATH_ATTRIB:Attribute<String> = Attributes.forString("Image file")
    }

}
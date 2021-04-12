package ru.leadpogommer.autoram

import com.cburch.logisim.data.Attribute
import com.cburch.logisim.data.BitWidth
import com.cburch.logisim.data.Bounds
import com.cburch.logisim.data.Value
import com.cburch.logisim.instance.*
import com.cburch.logisim.util.GraphicsUtil
import com.studiohartman.jamepad.ControllerManager
import com.studiohartman.jamepad.ControllerState
import javax.naming.directory.AttributeModificationException
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


class Gamepad : InstanceFactory("Gamepad") {

    private val controllers = ControllerManager()
    private lateinit var state : ControllerState

    init {
        controllers.initSDLGamepad()
    }

    private var lastPolled: Long = 0



    abstract class GamepadComponent(val name: String){
        abstract fun v(bits: Int = 1): Int
    }
    class BoolComponent(name: String, val getter: () -> Boolean): GamepadComponent(name){
        override fun v(bits: Int):Int {
            return if(getter()) 1 else 0
        }
    }
    class IntComponent(name: String, val getter: () -> Float): GamepadComponent(name){
        override fun v(bits: Int):Int {
            var n = max(-1.0f, min(1.0f, getter()))
            n = (n + 1.0f) / 2.0f
            return (n * (2.0f.pow(bits) - 1.0f)).toInt()
        }
    }

    val components = arrayOf(
        BoolComponent("Start" ) {state.start},
        BoolComponent("Select" ) {state.back},
        BoolComponent("A" ) {state.a},
        BoolComponent("B" ) {state.b},
        IntComponent("X") {state.leftStickX},
        IntComponent("Y" ) {state.leftStickY},
    )

    init {
        setAttributes(arrayOf(StdAttr.WIDTH), arrayOf(BitWidth.create(5)))
        setOffsetBounds(Bounds.create(0, 0, WIDTH, components.size * PORT_HEIGHT));
        val ports = arrayListOf<Port>()
        for(i in components.indices){

            when(components[i]){
                is BoolComponent ->  ports.add(Port(WIDTH, i* PORT_HEIGHT + PORT_HEIGHT/2, Port.OUTPUT, 1 ))
                is IntComponent -> ports.add(Port(WIDTH, i* PORT_HEIGHT + PORT_HEIGHT/2, Port.OUTPUT, StdAttr.WIDTH ))
            }
        }
        ports.add(Port(0, PORT_HEIGHT/2, Port.INPUT, 1))

        setPorts(ports.toTypedArray())
    }




    override fun paintInstance(painter: InstancePainter?) {
        if (painter == null) return;
        painter.drawBounds()
        painter.drawPorts()
        val bds = painter.location
        for(i in components.indices){
            GraphicsUtil.drawText(painter.graphics, components[i].name, WIDTH + bds.x, i * PORT_HEIGHT + PORT_HEIGHT/2 + bds.y, GraphicsUtil.H_RIGHT, GraphicsUtil.V_CENTER);
        }
    }

    override fun propagate(instanceState: InstanceState?) {
        if(instanceState ==  null)return
        pollController()
        val bits = instanceState.getAttributeValue(StdAttr.WIDTH)
        for(i in components.indices){

            when(components[i]){
                is BoolComponent -> instanceState.setPort(i, Value.createKnown(BitWidth.ONE, components[i].v()), 0)
                is IntComponent -> instanceState.setPort(i, Value.createKnown(bits, components[i].v(bits.width)), 0)
            }
        }
    }

    

    private fun pollController() {
        if (lastPolled + POLL_INTERVAL < System.currentTimeMillis()) {
            lastPolled = System.currentTimeMillis()
        } else {
            return
        }
        val currState = controllers.getState(0)
        if(!currState.isConnected)return
        state = currState
//        println("${state.a}, ${state.leftStickX}")
    }

    companion object {
        const val POLL_INTERVAL = 16
        const val WIDTH = 50
        const val PORT_HEIGHT = 20
    }
}
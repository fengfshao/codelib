package com.berry.scalaProgramming

import scala.swing._
import scala.swing.event.{ButtonClicked, EditDone}
/**
 * scala编程（第三版）32章
 *
 * @author leonardo
 * @since 2024/11/18
 *
 */
object FirstSwingApp extends SimpleSwingApplication {
  override def top: Frame = new MainFrame {
    title = "First Swing App"
    val button = new Button {
      text = "Click me"
    }
    val label = new Label {
      text = "No button clicks registered"
    }
    contents = new BoxPanel(Orientation.Vertical) {
      contents += button
      contents += label
      border = Swing.EmptyBorder(30,30,10,30)
    }
    var nClicks=0
    listenTo(button)
    reactions += {
      case ButtonClicked(b)=>
        nClicks+=1
        label.text = "Number of button clicks: "+nClicks
    }
  }
}
object TempConverter extends SimpleSwingApplication{
  def top=new MainFrame{
    title="Celsius/Fahrenheit Converter"
    object celsius extends TextField { columns = 5}
    object fahrenheit extends TextField { columns = 5}
    contents = new FlowPanel {
      contents += celsius
      contents += new Label(" Celsius = ")
      contents += fahrenheit
      contents += new Label(" Fahrenheit ")
      border = Swing.EmptyBorder(15,10,10,10)
    }
    listenTo(celsius,fahrenheit)
    reactions += {
      case EditDone(`fahrenheit`)=>
        val f=fahrenheit.text.toInt
        val c=(f-32)*5/9
        celsius.text = c.toString
      case EditDone(`celsius`)=>{
        val c=celsius.text.toInt
        val f=c*9/5+32
        fahrenheit.text = f.toString
      }
    }
  }
}
object Ch32 {}

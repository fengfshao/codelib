package com.berry.scalaProgramming

/**
 * scala编程第三版16-20章本地调试
 *
 * @author leonardo
 * @since 2024/10/13
 *
 */
// java bean的scala版本，如果不需要重写getter和setter不必写这个
class Thermometer {
  private[this] var celsius: Float = _
  def fahrenheit: Float = celsius * 9 / 5 + 32
  def fahrenheit_=(f: Float): Unit = {
    celsius = (f - 32) * 5 / 9
  }
}

abstract class Simulation {
  type Action = () => Unit
  case class WorkItem(time: Int, action: Action)
  private var curtime = 0
  def currentTime: Int = curtime
  private var agenda: List[WorkItem] = List()

  private def insert(ag: List[WorkItem], item: WorkItem): List[WorkItem] = {
    if (ag.isEmpty || item.time < ag.head.time) item :: ag
    else ag.head :: insert(ag.tail, item)
  }

  def afterDelay(delay: Int)(block: => Unit): Unit = {
    val item = WorkItem(curtime + delay, () => block)
    agenda = insert(agenda, item)
  }

  private def next(): Unit = agenda match {
    case item :: rest =>
      agenda = rest
      curtime = item.time
      item.action()
  }

  def run(): Unit = {
    afterDelay(0) {
      println(
        "*** simulation started, time = " +
          currentTime + " ***")
    }
    while (agenda.nonEmpty) next()
  }
}

abstract class BasicCircuitSimulation extends Simulation {
  def InverterDelay: Int
  def AndGateDelay: Int
  def OrGetDelay: Int

  class Wire {
    private var sigVal = false
    private var actions: List[Action] = List()
    def getSignal: Boolean = sigVal
    def setSignal(s: Boolean): Unit = {
      if (s != sigVal) {
        sigVal = s
        actions foreach (_())
      }
    }
    def addAction(a: Action): Unit = {
      actions = a :: actions
      a()
    }
  }

  def inverter(input: Wire, output: Wire): Unit = {
    def invertAction(): Unit = {
      val inputSig = input.getSignal
      afterDelay(InverterDelay) {
        output setSignal !inputSig
      }
    }
    input addAction invertAction
  }

  def andGate(a1: Wire, a2: Wire, output: Wire): Unit = {
    def andAction(): Unit = {
      val a1Sig = a1.getSignal
      val a2Sig = a2.getSignal
      afterDelay(AndGateDelay) {
        output setSignal (a1Sig & a2Sig)
      }
    }
    a1 addAction andAction
    a2 addAction andAction
  }

  def orGate(a1: Wire, a2: Wire, output: Wire): Unit = {
    def orAction(): Unit = {
      val a1Sig = a1.getSignal
      val a2Sig = a2.getSignal
      afterDelay(AndGateDelay) {
        output setSignal (a1Sig || a2Sig)
      }
    }
    a1 addAction orAction
    a2 addAction orAction
  }

  def probe(name: String, wire: Wire): Unit = {
    def probeAction(): Unit = {
      println(name + " " + currentTime + " new-value = " + wire.getSignal)
    }
    wire addAction probeAction
  }
}

abstract class CircuitSimulation extends BasicCircuitSimulation {
  def halfAdder(a: Wire, b: Wire, s: Wire, c: Wire): Unit = {
    val d, e = new Wire
    orGate(a, b, d)
    andGate(a, b, c)
    inverter(c, e)
    andGate(d, e, s)
  }

  def fullAdder(a: Wire, b: Wire, cin: Wire, sum: Wire, cout: Wire): Unit = {
    val s, c1, c2 = new Wire
    halfAdder(a, cin, s, c1)
    halfAdder(b, s, sum, c2)
    orGate(c1, c2, cout)
  }
}

object MySimulation extends CircuitSimulation {
  override def InverterDelay: Int = 1
  override def AndGateDelay: Int = 3
  override def OrGetDelay: Int = 5
}

// 完全隐藏实现，函数式队列
trait Queue[+T] {
  def head: T
  def tail: Queue[T]
  def append[U >: T](x: U): Queue[U]
}
object Queue {
  def apply[T](xs: T*): Queue[T] = new QueueImpl[T](xs.toList, Nil)
  private class QueueImpl[+T](private val leading: List[T], private val trailing: List[T]) extends Queue[T] {
    private def mirror: QueueImpl[T] = if (leading.isEmpty) new QueueImpl(trailing.reverse, Nil) else this
    override def head: T = mirror.leading.head
    override def tail: Queue[T] = {
      val q = mirror
      new QueueImpl(q.leading.tail, q.trailing)
    }
    override def append[U >: T](x: U): Queue[U] = new QueueImpl[U](leading, x :: trailing)
  }
}
class Cell[+T](init: T) {
  private[this] var current: T = init
  def get = current
  // def set(x: T) { current = x } 协变类型T出现在值x的类型T的逆变位置，即参数位置，通常会引入错误
  // def set[U>:T](x: U) { current = x } // 需要改变current的类型此时
}

class Fruit
class Orange extends Fruit
class Apple extends Fruit

// -T表示是逆变关系，逻辑上说一个OutputChannel[Number]必然能够处理Int类型的数据
// 因此可以使用OutputChannel[Number]替代OutputChannel[Int]，因为前者是后者的子类，这也符合里氏替换原则
trait OutputChannel[-T] {
  def write(x: T)
}

class Food
class Grass extends Food
abstract class Animal {
  type SuitableFood <: Food
  def eat(food: SuitableFood): Unit
}
class Cow extends Animal {
  type SuitableFood = Grass
  override def eat(food: Grass): Unit = {}
}

// scala中的枚举
object Color extends Enumeration {
  val Red, Green, Blue = Value
}

// 货币例子的类抽象设计
abstract class CurrencyZone {
  type Currency <: AbstractCurrency
  def make(x: Long): Currency
  abstract class AbstractCurrency {
    val amount: Long
    def designation: String
    override def toString: String = amount + " " + designation
    def +(that: Currency): Currency = make(this.amount + that.amount)
    def *(x: Double): Currency = make((this.amount * x).toLong)
  }
}
object US extends CurrencyZone {
  abstract class Dollar extends AbstractCurrency {
    def designation = "USD"
  }
  type Currency = Dollar
  override def make(x: Long): Dollar = new Dollar { val amount: Long = x }
  val Cent = make(1)
  val Dollar = make(100)
  val CurrencyUnit = Dollar
}

object Europe extends CurrencyZone {
  class Euro(override val amount: Long) extends AbstractCurrency {
    def designation = "EUR"
  }
  type Currency = Euro
  override def make(x: Long): Euro = new Euro(x)

  val Cent = make(1)
  val Euro = make(100)
  val CurrencyUnit = Euro
}

object Ch16 {
  def main(args: Array[String]): Unit = {
    // 不指明类型参数，类型将为Nothing，这是因为List是协变的
    val a = List() //
    val b: List[Nothing] = Nil

    val thermometer = new Thermometer
    thermometer.fahrenheit = 32
    //thermometer._celsius=1

    import MySimulation._
    val input1, input2, sum, carry = new Wire
    probe("sum", sum)
    probe("carry", carry)
    halfAdder(input1, input2, sum, carry)
    input1 setSignal true
    run()
    input2 setSignal true
    run()

    // 协变和增加参数类型的超类型限定
    val c1 = new Cell[String]("abc")
    val c2: Cell[Any] = c1

    val queue: Queue[Orange] = Queue(new Orange)
    val queue2: Queue[Fruit] = queue
    val queue3: Queue[Fruit] = queue.append(new Fruit)
  }
}

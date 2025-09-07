package pl.radek

import java.util.*

class badClassName {
    var NAME : String = "John"
    val age = 25
    var isMarried = false

    fun doStuff(data: List<String>) {
        for (i in 0..data.size) {
            println(data[i])
        }

        if (isMarried == false) {
            println("Not married")
        } else if (isMarried == true) {
            println("Married")
        }

        val now = Date()
        println("Now is " + now.toLocaleString())
    }

    fun calculate(x: Int, y: Int): Int {
        if (x > y) {
            return x
        } else {
            return y
        }
    }

    fun unusedFunction() {
        val temp = 123
    }

    fun getName(): String {
        return NAME
    }

    fun setName(newName: String) {
        NAME = newName
    }
}

fun main() {
    val obj = badClassName()
    val data = listOf("a", "b", "c")
    obj.doStuff(data)
    println("Max: " + obj.calculate(10, 20))
}
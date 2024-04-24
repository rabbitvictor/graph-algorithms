package com.rabbitvictor.graphalgorithms

import java.util.LinkedList
import kotlin.random.Random


typealias AdjListGraph = Array<LinkedList<Vertex>>

data class Vertex(val index: Int, val label: String? = null)

enum class Color {
    WHITE, // not visited
    GREY, // visited, not finished
    BLACK // visited, finished
}

fun AdjListGraph.addUndirectedEdge(u: Int, v: Int) {
    // undirected graphs
    this[u].add(Vertex(v))
    this[v].add(Vertex(u))
}

fun AdjListGraph.addUndirectedEdge(u: Vertex, v: Vertex) {
    // undirected graphs
    this[u.index].add(v)
    this[v.index].add(u)
}

fun randomGraphAdjList(numVertices: Int, probability: Int): AdjListGraph {
    val adjList = Array<LinkedList<Vertex>>(numVertices) { _ ->
        LinkedList<Vertex>()
    }

    (0..<numVertices).forEach { i ->
        (i + 1..<numVertices).forEach { j ->
            if (Random.nextInt(probability) == 0) {
                adjList.addUndirectedEdge(i, j)
            }
        }
    }

    return adjList
}

fun Array<Vertex?>.toUndirectedGraphViz(): String {
    val pi = this

    return buildString {
        append("strict graph {\n")
        pi.forEachIndexed { current, predecessor ->
            if (predecessor == null) return@forEachIndexed
            append("  $current -- ${predecessor.index}\n")
        }
        append("}")
    }
}


fun printPathIndex(s: Vertex, v: Vertex, pi: Array<Vertex?>) {
    if (v.index == s.index) {
        print(" ${s.index} -> ")
    } else if (pi[v.index] == null) {
        println("there is no path between ${s.index} and ${v.index}.")
    } else {
        printPathIndex(s, pi[v.index]!!, pi)
        print(" ${v.index} ->")
    }
}

fun printPathLabel(s: Vertex, v: Vertex, pi: Array<Vertex?>) {
    if (v.index == s.index) {
        print(" ${s.label} -> ")
    } else if (pi[v.index] == null) {
        println("there is no path between ${s.index} and ${v.index}.")
    } else {
        printPathLabel(s, pi[v.index]!!, pi)
        print(" ${v.label} ->")
    }
}
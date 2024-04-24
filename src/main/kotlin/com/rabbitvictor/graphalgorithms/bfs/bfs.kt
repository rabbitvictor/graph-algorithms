package com.rabbitvictor.graphalgorithms.bfs

import com.rabbitvictor.graphalgorithms.bfs.Color.BLACK
import com.rabbitvictor.graphalgorithms.bfs.Color.GREY
import com.rabbitvictor.graphalgorithms.bfs.Color.WHITE
import java.util.LinkedList
import kotlin.random.Random

data class Vertex(val index: Int, val label: String? = null)
data class BFSResult(val pi: Array<Vertex?>, val distance: Array<Int>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BFSResult

        if (!pi.contentEquals(other.pi)) return false
        if (!distance.contentEquals(other.distance)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pi.contentHashCode()
        result = 31 * result + distance.contentHashCode()
        return result
    }
}

enum class Color {
    WHITE, // Not visited
    GREY, // On queue, visited
    BLACK // Finished
}

fun List<LinkedList<Vertex>>.addEdge(u: Int, v: Int) {
    // undirected graphs
    this[u].add(Vertex(v))
    this[v].add(Vertex(u))
}

fun List<LinkedList<Vertex>>.addEdge(u: Vertex, v: Vertex) {
    // undirected graphs
    this[u.index].add(v)
    this[v.index].add(u)
}


fun randomGraphAdjList(numVertices: Int, probability: Int): List<LinkedList<Vertex>> {
    val adjList = List<LinkedList<Vertex>>(numVertices) { _ ->
        LinkedList<Vertex>()
    }

    (0..<numVertices).forEach { i ->
        (i + 1..<numVertices).forEach { j ->
            if (Random.nextInt(probability) == 0) {
                adjList.addEdge(i, j)
            }
        }
    }

    return adjList
}

fun bfs(graph: List<LinkedList<Vertex>>, s: Vertex): BFSResult {
    val color = Array<Color>(graph.size) { index ->
        /* initializes s vertex as GREY, because it is the first to be visited */
        if (index != s.index) WHITE else GREY
    }

    val distance = Array<Int>(graph.size) { index ->
        /* initializes s vertex distance to itself as 0 and every other vertex distance to s as highest possible value */
        if (index != s.index) Int.MAX_VALUE else 0
    }

    /* initializes search tree array representation as null since there are no edges from s to any other vertex at first*/
    val pi = Array<Vertex?>(graph.size) { _ -> null }

    val queue = mutableListOf<Vertex>()
    queue.add(s)

    while (queue.isNotEmpty()) {
        val u = queue.removeFirst()
        graph[u.index].forEach { v ->
            if (color[v.index] == WHITE) {
                color[v.index] = GREY
                distance[v.index] = distance[u.index] + 1
                pi[v.index] = u
                queue.add(v)
            }
        }
        color[u.index] = BLACK
    }

    return BFSResult(pi, distance)
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

fun main() {
    slideExample()
    println()
}

private fun slideExample() {
    val graph = List<LinkedList<Vertex>>(8) { _ ->
        LinkedList<Vertex>()
    }

    /*
    * s -> 0
    * r -> 1
    * w -> 2
    * v -> 3
    * t -> 4
    * x -> 5
    * u -> 6
    * y -> 7
    * */

    graph.addEdge(Vertex(0, "s"), Vertex(1, "r"))
    graph.addEdge(Vertex(0, "s"), Vertex(2, "w"))
    graph.addEdge(Vertex(1, "r"), Vertex(3, "v"))
    graph.addEdge(Vertex(2, "w"), Vertex(4, "t"))
    graph.addEdge(Vertex(2, "w"), Vertex(5, "x"))
    graph.addEdge(Vertex(4, "t"), Vertex(6, "u"))
    graph.addEdge(Vertex(4, "t"), Vertex(5, "x"))
    graph.addEdge(Vertex(5, "x"), Vertex(7, "y"))
    graph.addEdge(Vertex(5, "x"), Vertex(6, "u"))
    graph.addEdge(Vertex(6, "u"), Vertex(7, "y"))

    val result = bfs(graph, Vertex(0, "s"))

    val vertices = listOf(
//        Vertex(0, "s"),
        Vertex(1, "r"),
        Vertex(2, "w"),
        Vertex(3, "v"),
        Vertex(4, "t"),
        Vertex(5, "x"),
        Vertex(6, "u"),
        Vertex(7, "y"),
    )


    vertices.forEach { vertex ->
        println("path for ${vertex.label}...")
        printPathLabel(Vertex(0, "s"), vertex, result.pi)
        println()
    }
}

private fun randomExample() {
    val graph = randomGraphAdjList(100, 10)
    val result = bfs(graph, Vertex(3))
    (0..<100).forEach { i ->
        println("path for $i...")
        printPathIndex(Vertex(3), Vertex(i), result.pi)
        println()
    }
}


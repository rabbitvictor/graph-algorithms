package com.rabbitvictor.graphalgorithms

import com.rabbitvictor.graphalgorithms.Color.BLACK
import com.rabbitvictor.graphalgorithms.Color.GREY
import com.rabbitvictor.graphalgorithms.Color.WHITE
import java.util.LinkedList

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

fun breadthFirstSearch(graph: AdjListGraph, s: Vertex): BFSResult {
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

fun main() {
    randomExample()
}

private fun slideExample() {
    val graph = Array<LinkedList<Vertex>>(8) { _ ->
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

    graph.addUndirectedEdge(Vertex(0, "s"), Vertex(1, "r"))
    graph.addUndirectedEdge(Vertex(0, "s"), Vertex(2, "w"))
    graph.addUndirectedEdge(Vertex(1, "r"), Vertex(3, "v"))
    graph.addUndirectedEdge(Vertex(2, "w"), Vertex(4, "t"))
    graph.addUndirectedEdge(Vertex(2, "w"), Vertex(5, "x"))
    graph.addUndirectedEdge(Vertex(4, "t"), Vertex(6, "u"))
    graph.addUndirectedEdge(Vertex(4, "t"), Vertex(5, "x"))
    graph.addUndirectedEdge(Vertex(5, "x"), Vertex(7, "y"))
    graph.addUndirectedEdge(Vertex(5, "x"), Vertex(6, "u"))
    graph.addUndirectedEdge(Vertex(6, "u"), Vertex(7, "y"))

    val result = breadthFirstSearch(graph, Vertex(0, "s"))

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

    val t = result.pi.toUndirectedGraphViz()
    println(t)
}

private fun randomExample() {
    val graph = randomGraphAdjList(100, 10)
    val result = breadthFirstSearch(graph, Vertex(3))
    println(result.pi.toUndirectedGraphViz())
}
package com.rabbitvictor.graphalgorithms

import com.rabbitvictor.graphalgorithms.Color.BLACK
import com.rabbitvictor.graphalgorithms.Color.GREY
import com.rabbitvictor.graphalgorithms.Color.WHITE
import java.util.LinkedList


class DFSResult(private val graph: AdjListGraph) {
    private var time = 0
    private val color = Array<Color>(graph.size) { _ -> WHITE }

    /* initializes search tree array representation as null since there are no edges from s to any other vertex at first*/
    val pi = Array<Vertex?>(graph.size) { _ -> null }
    val finished = Array<Int>(graph.size) { 0 }
    val discovered = Array<Int>(graph.size) { 0 }

    fun dfs() {
        graph.forEachIndexed { index, _ ->
            if (color[index] == WHITE) {
                dfsVisit(Vertex(index))
            }
        }
    }

    private fun dfsVisit(
        u: Vertex,
    ) {
        time += 1
        color[u.index] = GREY
        discovered[u.index] = time
        graph[u.index].forEachIndexed { index, v ->
            if (color[v.index] == WHITE) {
                pi[v.index] = u
                dfsVisit(v)
            }
        }

        time +=1
        color[u.index] = BLACK
        finished[u.index] = time
    }
}

fun main() {
    val randomGraph = randomGraphAdjList(100, 10)
    val dfsResult = DFSResult(randomGraph)
    dfsResult.dfs()
    println(dfsResult.pi.toUndirectedGraphViz())
}
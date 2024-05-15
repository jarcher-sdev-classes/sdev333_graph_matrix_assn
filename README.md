# Individual Assignment: Graph Matrix

## Overview

In this assignment, we will explore one of the popular ways to store graph data in an application. You will be using 2D arrays in Java to write an adjacency matrix with all the basic operations of data structures we have seen in our past topics.

## Objectives

Here is what we'd like you take out of this assignment.

### Course
- Implement a graph data structure using an adjacency list or adjacency matrix.

### Module
- To demonstrate how to access row/column data in a two-dimensional array.
- To write a complex data structure using other sub-structures for support.
- To write a Java class that stores data from different types of networks.

## Getting Started

You have been provided with a start project, please download here: [project files](https://egator.greenriver.edu/courses/2466854/files/245548005/download?wrap=1)

Download project files. Take a quick look at the interfaces and classes that have been provided for you:
- `IGraph` - an interface describing the basic behavior of a graph.
- `Edge` - a simple class that stores a pair of vertices that form an edge and the weight of that edge.
- `Bijection` - A two-way map structure where keys & values are stored and a key or value can be provided to find a pair in the map.
    - Note: This means that both your keys are unique and your values are unique.
    - Note: This type of structure is often called a one-to-one correspondence in mathematics.
- `GraphsTest` - A file with unit tests to verify the behavior of your class. You will need to un-comment the line of code in the setup() method once you have begun to write your class.

## Adjacency Matrix

Your task is to write a graph structure that is based upon a matrix, as described in class. To implement such a structure in Java will require you to manage a two-dimensional array, storing rows and columns in the array defined by their indices.

Notice how elements are arranged in rows and columns, where if there is an edge in the graph. To declare a grid of elements as a two-dimensional array in Java you write a statement like the following:

```java
int[][] matrix = new int[rows][columns];
```

## The Graph You Build Should Also Have the Following Properties:

- The graph is directed.
- The graph is a weighted graph, where edge weights replace the value one in the matrix above. Negative edge weights are not supported, and a missing edge is stored as -1.

## The IGraph Interface

The expected operations of your class are defined in the IGraph interface. I have included the Javadoc descriptions of each method below:

| Operation | Description |
|-----------|-------------|
| `boolean addVertex(V vertex)` | Adds a new vertex to the graph. If the vertex already exists, then no change is made to the graph. |
| `boolean addEdge(V source, V destination, int weight)` | Adds a new edge to the graph. If the edge already exists, then no change is made to the graph. Edges are considered to be directed. |
| `int vertexSize()` | Returns the number of vertices in the graph. |
| `int edgeSize()` | Returns the number of edges in the graph. |
| `boolean containsVertex(V vertex)` | Reports whether a vertex is in the graph or not. |
| `boolean containsEdge(V source, V destination)` | Reports whether an edge is in the graph or not. |
| `int edgeWeight(V source, V destination)` | Returns the edge weight of an edge in the graph. |
| `Set<V> vertices()` | Returns a set with all vertices in the graph. The set returned shares no references with any internal structures used by the DirectedGraph class. |
| `Set<Edge<V>> edges()` | Returns a set with all edges in the graph. The set returned shares no references with any internal structures used by the DirectedGraph class. |
| `boolean removeVertex(V vertex)` | Removes a vertex from the graph. |
| `boolean removeEdge(V source, V destination)` | Removes an edge from the graph. |
| `void clear()` | Removes all vertices and edges from the graph. |

## Managing Indices

It can be particularly challenging to assign new vertex elements to indices in your two-dimensional array. Recall that any object can be assigned as a vertex in a graph. Vertices could be colors, cars, nations, cities, etc., depending on the graph. Your program will need to maintain mapping from vertices to indices. For example, given the insertion of color-vertices in the following order - pink, magenta, blue, green - your program could assign indices in insertion order:

![Adjacency Matrix 2](adjacency_matrix_2.png)

Notice how a new element would be assigned index 5, then 6, and so on. This works well, but what if we ended up deleting a vertex? For example, deleting blue from the graph above:

![Adjacency Matrix 3](adjacency_matrix_3.png)

Notice how we have an empty space in our graph. We should not leave unused space in the graph. So we should reassign indices from deleted elements as needed. This can be challenging to accomplish if we pick the wrong data structure. The goal is for the data structure to provide previously used indices if available (to reuse them) or the next available index.

A stack can be used to solve this problem. Use the following steps to provide indices for new vertices:

- The stack should initially store just the first index (0).
- When a request is made to add a new vertex to the graph, pull the top number off the stack. This is the index of your new vertex.
  - If the stack is now empty, then increment the last index pulled from the stack and add this number back onto the stack.
  - If the stack is not empty, then do nothing. There is an index still available for all new vertices added to the graph.

## Using the Bijection

I have provided a two-way map for you to store your vertex - index pairs. This look-up table will be needed for most of the methods you will write. Sometimes you will need to use a vertex to look up an index in the matrix. Other times you will do the opposite, using an index to find a vertex that belongs to a position. Below is an example of how we can use this look-up table:

![Bijection](bijection.png)

The Bijection class allows you to search by key or value in constant time (at the cost of some extra space). Take a moment and familiarize yourself with the class. You are required to use this as part of your solution. Your code is required to provide O(1) look up time when identifying the index of a vertex (or the opposite, a vertex at an index). Use the Bijection class to accomplish this.

## Making Room for More Elements

Your graph should continuously grow to accommodate more vertices. If addVertex() is invoked on a matrix with no more row/columns for a new vertex, then the matrix should be replaced with a larger matrix.

![Adjacency Matrix 4](adjacency_matrix_4.png)

You should be conservative with how quickly you allow your matrix to grow, as you are using space. My suggestion is to use a 50% growth factor. Once you have created a larger two-dimensional array, copy all elements from the previous array to the new array.

## Verifying Your Work

I have provided a set of unit tests to verify the behavior of your class. It will be beneficial to observe how I have written the tests, as guidance when writing your graph methods.

## Requirements

Any submissions ignoring the following requirements will be forced to resubmit their work:
- Your graph must be directed, weighted, and use an adjacency matrix.
- Your class must use a two-dimensional array to store elements in the matrix.
- You should not be storing any Edge objects in the fields in your class. This includes lists, sets, or maps that store Edge objects. The only place an Edge object should be created is within your edges() method.
# Power Grid Project

## 📌 Problem Overview

This project deals with analyzing a **power grid** represented as a graph with **cities** as nodes and **transmission lines** as edges. It solves two main problems:

1. **Find all Critical Transmission Lines** (i.e., lines whose removal increases the number of disconnected components in the grid).
2. **Find the Number of Important Lines Between Two Cities** (i.e., how many critical lines are in the path between two cities).

---

## 📁 Input Format

The input file passed to `PowerGrid(String filename)` contains:

1. First line: Number of cities `n`
2. Second line: Number of power lines `m`
3. Next `n` lines: Names of the cities
4. Next `m` lines: Pairs of cities with a power line between them

---

## 🔧 Core Functionalities

---

### 1. `ArrayList<PowerLine> criticalLines()`

**🔍 Objective**: Identify all the **critical transmission lines** (bridges) in the power grid.

**🧠 Algorithm Used**: Modified DFS using **Tarjan's Algorithm** for finding bridges in O(n + m).

**🧩 Steps in Code**:

* Initialize discovery and low time maps (`no_of_steps` and `lowest_no_of_steps`)
* Start DFS from any city (cityNames\[0])
* For each neighbor:

  * If not visited: recurse and update `lowest_no_of_steps`
  * If already visited and not parent: update `lowest_no_of_steps`
* If `lowest_no_of_steps[neighbor] > no_of_steps[current]`, mark as bridge.
* Maintain a separate graph (`graph2`) without bridges.
* Store bridges in the `bridges` list.

---

### 2. `void preprocessImportantLines()`

**🔍 Objective**: Preprocess data structures to efficiently answer `numImportantLines(cityA, cityB)` in O(log n).

**🧩 Steps in Code**:

* **Step 1**: Call `criticalLines()` to identify all bridges.
* **Step 2**: Build **connected components** of the remaining graph (`graph2`) and assign them a virtual group name (`newname`).
* **Step 3**: Build a new graph (`graph3_new`) **only with bridge nodes** between components.
* **Step 4**: Perform a DFS on `graph3_new` to compute:

  * Entry and exit time (`t_in`, `t_out`) for ancestor checks
  * Depth of each node (`heightarray`)
  * Binary Lifting table (`ancestors`) for fast LCA (Lowest Common Ancestor)

---

### 3. `int numImportantLines(String cityA, String cityB)`

**🔍 Objective**: Return number of important (bridge) lines in the path from `cityA` to `cityB`.

**🧠 Algorithm Used**: Binary Lifting for **LCA** in bridge-tree.

**🧩 Steps in Code**:

* Convert `cityA` and `cityB` to their component labels from `newname`.
* If same component, return 0.
* Else:

  * Use `is_ancestor()` to check ancestor relation.
  * If one is ancestor of other: return depth difference.
  * Otherwise, compute LCA using binary lifting and return `height(cityA) + height(cityB) - 2 * height(LCA)`.

---

## 🧠 Time Complexities

| Function                     | Time Complexity |
| ---------------------------- | --------------- |
| `PowerGrid(String file)`     | O(n + m)        |
| `criticalLines()`            | O(n + m)        |
| `preprocessImportantLines()` | O(n log n)      |
| `numImportantLines()`        | O(log n)        |

---

## 🧾 Example

**Input File (example.txt)**:

```
5
5
A
B
C
D
E
A B
B C
C D
D E
B D
```

**Query**: `numImportantLines("A", "E")`
**Output**: `1`
**(Bridge: C-D)**

---

## 🌍 Real-World Applications

* **Power Grid Design**: Detecting single points of failure in a national electricity grid.
* **Network Reliability**: Ensuring redundancy in critical infrastructure.
* **Traffic Routing**: Finding chokepoints in a road or railway system.
* **Cybersecurity**: Identifying critical links in computer networks for vulnerability analysis.

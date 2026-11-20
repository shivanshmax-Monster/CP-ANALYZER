package com.example.model

enum class Difficulty(val label: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard")
}

data class ProblemItem(
    val id: String,
    val number: String,
    val title: String,
    val platform: String,
    val difficulty: Difficulty,
    val tags: List<String>,
    val complexityTime: String = "O(N)",
    val complexitySpace: String = "O(1)",
    val stageText: String = "Stage 1 (3d)",
    val dueStatus: String = "Due Today",
    val isDueToday: Boolean = false,
    val isBlindSpot: Boolean = false,
    val masteryPercent: Int = 50,
    val consecutiveRecalls: Int = 1,
    val pastStumbleMemo: String? = null,
    val intuitionNote: String? = null,
    val codeSnippet: String? = null,
    val beatsSummary: String? = null,
    val timeAgo: String = "1d ago",
    val sm2EaseFactor: String = "2.5 EF",
    val isMastered: Boolean = false,
    val intervalDays: Int = 3
)

data class SyncCadenceDay(
    val week: Int,
    val dayOfWeek: Int,
    val level: Int // 0 to 3
)

object SampleData {
    val initialProblems = listOf(
        ProblemItem(
            id = "lc-146",
            number = "LC #146",
            title = "LRU Cache",
            platform = "LeetCode",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("Linked List", "Hash Table"),
            complexityTime = "O(1)",
            complexitySpace = "O(capacity)",
            stageText = "Stage 3 (21d)",
            dueStatus = "Due Today",
            isDueToday = true,
            isBlindSpot = false,
            masteryPercent = 75,
            consecutiveRecalls = 3,
            pastStumbleMemo = "Always update dummy head/tail pointers and remove from map on capacity eviction.",
            intuitionNote = "Doubly linked list paired with hash table mapping key -> node for O(1) eviction & lookup.",
            codeSnippet = "class LRUCache {\n  Node head, tail;\n  Map<Integer, Node> map;\n  void put(int k, int v) { ... }\n}",
            beatsSummary = "0ms • 99% Beats",
            timeAgo = "Due Today",
            intervalDays = 21
        ),
        ProblemItem(
            id = "lc-210",
            number = "LC #210",
            title = "Course Schedule II",
            platform = "LeetCode",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("Topological Sort", "BFS", "Graph"),
            complexityTime = "O(V + E)",
            complexitySpace = "O(V + E)",
            stageText = "Stage 2 (7d)",
            dueStatus = "Due Today",
            isDueToday = true,
            isBlindSpot = true,
            masteryPercent = 60,
            consecutiveRecalls = 1,
            pastStumbleMemo = "Check if output list size equals numCourses; otherwise a cycle exists.",
            intuitionNote = "Kahn algorithm with in-degree array and queue. Push nodes with 0 in-degree.",
            timeAgo = "Due in 2h",
            intervalDays = 7
        ),
        ProblemItem(
            id = "lc-269",
            number = "LC #269",
            title = "Alien Dictionary",
            platform = "LeetCode",
            difficulty = Difficulty.HARD,
            tags = listOf("Topological Sort", "Directed Graph", "BFS/DFS"),
            complexityTime = "O(C)",
            complexitySpace = "O(1)",
            stageText = "Interval: 21d → 45d if passed",
            dueStatus = "Due Today",
            isDueToday = true,
            isBlindSpot = false,
            masteryPercent = 70,
            consecutiveRecalls = 3,
            pastStumbleMemo = "Forgot cycle detection when prefix is longer than next word (e.g. \"abc\" before \"ab\").",
            intuitionNote = "Build adjacency graph by comparing adjacent words at first mismatch character.",
            codeSnippet = "string alienOrder(vector<string>& words) {\n  unordered_map<char, unordered_set<char>> adj;\n  // Kahn Topological sort ...\n}",
            timeAgo = "Decaying in 4h",
            intervalDays = 21
        ),
        ProblemItem(
            id = "lc-72",
            number = "LC #72",
            title = "Edit Distance",
            platform = "LeetCode",
            difficulty = Difficulty.HARD,
            tags = listOf("2D Dynamic Programming", "String Matching"),
            complexityTime = "O(M × N)",
            complexitySpace = "O(M × N)",
            stageText = "Stage 2 (7 Days cadence)",
            dueStatus = "Due Today",
            isDueToday = true,
            isBlindSpot = true,
            masteryPercent = 45,
            consecutiveRecalls = 0,
            pastStumbleMemo = "Failed last retry on memoization base case when i==0 or j==0.",
            intuitionNote = "dp[i][j] = 1 + min(insert, delete, replace) when characters differ, else dp[i-1][j-1].",
            timeAgo = "Due Today",
            intervalDays = 7
        ),
        ProblemItem(
            id = "lc-53",
            number = "LC #53",
            title = "Maximum Subarray",
            platform = "LeetCode",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("Kadane's Dynamic Programming", "Array"),
            complexityTime = "O(N)",
            complexitySpace = "O(1)",
            stageText = "Stage 4 (45d)",
            dueStatus = "Due in 18 hrs",
            isDueToday = false,
            isBlindSpot = false,
            masteryPercent = 88,
            consecutiveRecalls = 5,
            intuitionNote = "Kadane's algorithm: currMax = max(x, currMax + x); globalMax = max(globalMax, currMax).",
            timeAgo = "Due in 18 hrs",
            intervalDays = 45
        ),
        ProblemItem(
            id = "lc-124",
            number = "LC #124",
            title = "Binary Tree Max Path Sum",
            platform = "LeetCode",
            difficulty = Difficulty.HARD,
            tags = listOf("Post-Order Tree DFS", "Subtree Gain"),
            complexityTime = "O(N)",
            complexitySpace = "O(H)",
            stageText = "Stage 1 (3d)",
            dueStatus = "Due in 1 day",
            isDueToday = false,
            isBlindSpot = false,
            masteryPercent = 52,
            consecutiveRecalls = 2,
            pastStumbleMemo = "Remember to ignore negative branch gains using max(0, gain).",
            intuitionNote = "Bottom-up recursion returning max single-path gain while tracking root + left + right max.",
            timeAgo = "Due in 1 day",
            intervalDays = 3
        ),
        ProblemItem(
            id = "lc-42",
            number = "LeetCode #42",
            title = "Trapping Rain Water",
            platform = "LeetCode",
            difficulty = Difficulty.HARD,
            tags = listOf("TwoPointers", "MonotonicStack"),
            complexityTime = "O(N)",
            complexitySpace = "O(1) Aux Space",
            stageText = "Stage 1 (5d)",
            dueStatus = "Revisit in 5 days",
            isDueToday = false,
            masteryPercent = 92,
            intuitionNote = "Two pointer approach maintaining maxLeft and maxRight. Water trapped is bounded by min(maxL, maxR) - height[i].",
            codeSnippet = "while (left < right) {\n  maxL = max(maxL, height[left]);\n  water += (maxL < maxR) ? (maxL - height[left++]) : (maxR - height[right--]);\n}",
            beatsSummary = "C++ (Optimized Two Pointers) • 0ms • 100% Beats",
            timeAgo = "2d ago",
            sm2EaseFactor = "2.6 EF",
            intervalDays = 5
        ),
        ProblemItem(
            id = "cf-1600",
            number = "Codeforces 1600",
            title = "Maximum Sum on Even Positions",
            platform = "Codeforces",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("DynamicProgramming", "Divide&Conquer"),
            complexityTime = "O(N)",
            complexitySpace = "O(N)",
            stageText = "Mastered (Interval 45d+)",
            dueStatus = "Mastered",
            isDueToday = false,
            masteryPercent = 95,
            intuitionNote = "Kadane variation on difference array between even and odd indices. Running max can flip adjacent pairs.",
            timeAgo = "18d ago",
            sm2EaseFactor = "2.8 EF",
            isMastered = true,
            intervalDays = 45
        ),
        ProblemItem(
            id = "gfg-cycle",
            number = "GeeksforGeeks",
            title = "Detect Cycle in a Directed Graph",
            platform = "GeeksForGeeks",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("Graphs", "TopologicalSort"),
            complexityTime = "O(V + E)",
            complexitySpace = "O(V)",
            stageText = "Stage 2 (7d)",
            dueStatus = "Needs Review (Due Tomorrow)",
            isDueToday = false,
            masteryPercent = 65,
            intuitionNote = "DFS using recursion stack array or Kahn Algorithm using in-degrees. Cross edges do not indicate cycle!",
            timeAgo = "6d ago",
            sm2EaseFactor = "2.4 EF",
            intervalDays = 7
        ),
        ProblemItem(
            id = "cf-1400",
            number = "CF 1400",
            title = "Segment Tree with Max Subsegment Sum",
            platform = "Codeforces",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("Data Structures", "Segment Tree"),
            complexityTime = "O(log N)",
            complexitySpace = "O(N)",
            stageText = "Stage 1 (3d)",
            dueStatus = "Synced",
            isDueToday = false,
            masteryPercent = 82,
            intuitionNote = "Maintain prefix sum, suffix sum, total sum, and max subsegment sum in tree nodes.",
            timeAgo = "3h ago",
            intervalDays = 3
        ),
        ProblemItem(
            id = "lc-140",
            number = "LeetCode #140",
            title = "Word Break II",
            platform = "LeetCode",
            difficulty = Difficulty.HARD,
            tags = listOf("DP / Trie", "Backtracking"),
            complexityTime = "O(2^N)",
            complexitySpace = "O(2^N)",
            stageText = "Stage 2 (7d)",
            dueStatus = "Synced",
            isDueToday = false,
            masteryPercent = 68,
            intuitionNote = "Memoized DFS / Trie prefix check to construct all valid sentence segmentations.",
            timeAgo = "Yesterday",
            intervalDays = 7
        )
    )

    fun generateCadenceGrid(): List<SyncCadenceDay> {
        val list = mutableListOf<SyncCadenceDay>()
        // 12 weeks x 4-5 days shown in grid
        val pattern = listOf(
            listOf(2, 2, 2, 2),
            listOf(1, 2, 1, 0),
            listOf(2, 2, 0, 2),
            listOf(0, 2, 2, 1),
            listOf(2, 1, 2, 2),
            listOf(1, 0, 2, 1),
            listOf(2, 2, 2, 2),
            listOf(2, 2, 2, 2),
            listOf(1, 1, 2, 1),
            listOf(2, 2, 2, 2),
            listOf(2, 2, 2, 2),
            listOf(2, 2, 2, 2)
        )
        for (w in 0 until 12) {
            val days = pattern[w]
            for (d in 0 until 4) {
                list.add(SyncCadenceDay(w, d, days[d]))
            }
        }
        return list
    }
}

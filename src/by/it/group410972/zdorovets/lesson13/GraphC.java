package by.it.group410972.zdorovets.lesson13;

import java.util.*;

public class GraphC {

  private static void dfs1(String v, Map<String, Set<String>> g,
                           Set<String> used, Deque<String> st) {
    used.add(v);
    for (String u : g.get(v))
      if (!used.contains(u))
        dfs1(u, g, used, st);
    st.push(v);
  }

  private static void dfs2(String v, Map<String, Set<String>> gt,
                           Set<String> used, List<String> comp) {
    used.add(v);
    comp.add(v);
    for (String u : gt.get(v))
      if (!used.contains(u))
        dfs2(u, gt, used, comp);
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String input = sc.nextLine();

    Map<String, Set<String>> g = new HashMap<>();
    Map<String, Set<String>> gt = new HashMap<>();
    Set<String> nodes = new TreeSet<>();

    for (String e : input.split(",")) {
      String[] p = e.trim().split("->");
      String a = p[0].trim();
      String b = p[1].trim();

      nodes.add(a);
      nodes.add(b);

      g.putIfAbsent(a, new TreeSet<>());
      g.putIfAbsent(b, new TreeSet<>());
      g.get(a).add(b);

      gt.putIfAbsent(a, new TreeSet<>());
      gt.putIfAbsent(b, new TreeSet<>());
      gt.get(b).add(a);
    }

    // 1. Косарайю
    Set<String> used = new HashSet<>();
    Deque<String> stack = new ArrayDeque<>();
    for (String v : nodes)
      if (!used.contains(v))
        dfs1(v, g, used, stack);

    used.clear();
    List<List<String>> comps = new ArrayList<>();
    Map<String, Integer> id = new HashMap<>();

    while (!stack.isEmpty()) {
      String v = stack.pop();
      if (!used.contains(v)) {
        List<String> comp = new ArrayList<>();
        dfs2(v, gt, used, comp);
        Collections.sort(comp);
        int idx = comps.size();
        for (String x : comp)
          id.put(x, idx);
        comps.add(comp);
      }
    }

    // 2. Граф компонент
    int n = comps.size();
    List<Set<Integer>> dag = new ArrayList<>();
    int[] indeg = new int[n];

    for (int i = 0; i < n; i++)
      dag.add(new HashSet<>());

    for (String u : g.keySet())
      for (String v : g.get(u)) {
        int cu = id.get(u);
        int cv = id.get(v);
        if (cu != cv && dag.get(cu).add(cv))
          indeg[cv]++;
      }

    // 3. Топологическая сортировка с лексикографическим приоритетом
    PriorityQueue<Integer> q = new PriorityQueue<>(
            Comparator.comparing(i -> String.join("", comps.get(i)))
    );

    for (int i = 0; i < n; i++)
      if (indeg[i] == 0)
        q.add(i);

    StringBuilder out = new StringBuilder();
    boolean first = true;

    while (!q.isEmpty()) {
      int c = q.poll();
      if (!first) out.append('\n');
      first = false;
      out.append(String.join("", comps.get(c)));

      for (int to : dag.get(c)) {
        if (--indeg[to] == 0)
          q.add(to);
      }
    }

    System.out.print(out);
  }
}

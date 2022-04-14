package friends;

import java.util.ArrayList;
import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2)
	{
		/** COMPLETE THIS METHOD **/
		
		// if either p1 or p2 has no friends then shortestChain is immediately 0
		if(g.members[g.map.get(p1)].first==null || g.members[g.map.get(p2)].first == null)
			return null;
		
		ArrayList<String> shortestPath = new ArrayList<String>();
		boolean[] visited = new boolean[g.members.length];
		Person[] back = new Person[g.members.length]; // to find route back.
		Queue<Person> queue = new Queue<>();
		
		// bfs
		queue.enqueue(g.members[g.map.get(p1)]); // enqueue person 1 (p1)
		visited[g.map.get(p1)] = true;	// we have visited p1
		while(!queue.isEmpty())
		{
			Person p = queue.dequeue();
			visited[g.map.get(p.name)] = true; // dequeue and mark as visited
			
			for(Friend e = p.first; e!=null; e = e.next)
			{	
				// each friend is also a person.
				Person temp = g.members[e.fnum];
				
				if(!visited[g.map.get(temp.name)])
				{
					visited[g.map.get(temp.name)] = true;
					queue.enqueue(temp);
					
					/*
					 * In order to trace back, insert at temp's int value
					 * the name of the person that led to temp.
					 * 
					 * "It's a mystery tool that will help us later." - Mickey Mouse
					 */
					
					back[g.map.get(temp.name)] = p;
					
					// If e is actually p2 then backtrack:
					if(temp.name.equals(p2))
					{
						// Trace back from temp to p1 and insert into array as you go.
						while(!temp.name.equals(p1))
						{
							shortestPath.add(0, temp.name);
							temp = back[g.map.get(temp.name)];
						}
						
						shortestPath.add(0, temp.name); // temp is now p1
						return shortestPath;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school)
	{
		/** COMPLETE THIS METHOD **/
		
		// this is driver method to restart each time we find another person.
		
		ArrayList<ArrayList<String>> cliques = new ArrayList<>(); // master arrayList of size 0 for now.
		
		/*
		 * For each person p in g.members:
		 * 		if p is a student and goes to input school
		 *         and if p is not found to be in any clique, then we can call cliquebfs
		 */
		for(Person P : g.members)
		{
			if(P.school!=null && P.school.equals(school))
			{
				boolean containedInAnyClique = false;
				
				if(cliques.size() == 0)
					cliques.add(cliquebfs(g, school, P));
				
				for(ArrayList<String> clique : cliques)
					if(clique.contains(P.name))
						containedInAnyClique = true;
				
				if(!containedInAnyClique)
					cliques.add(cliquebfs(g, school, P));
			}
		}
		return cliques;
	}
	
	private static ArrayList<String> cliquebfs(Graph g, String school, Person p)
	{
		// criteria A ==>  Friend which is STUDENT && ATTENDS INPUT SCHOOL
		// standard bfs to explore all friends who fit criteria A
		
		ArrayList<String> clique = new ArrayList<>();
		clique.add(p.name);
		
		boolean visited[] = new boolean[g.members.length];
		Queue<Person> queue = new Queue<>();
		
		queue.enqueue(p);
		visited[g.map.get(p.name)] = true; 
		
		while(!queue.isEmpty())
		{
			Person v = queue.dequeue();
			for(Friend e = v.first; e!=null; e = e.next)
			{
				Person temp = g.members[e.fnum];
				
				// criteria A
				if(!(temp.school!=null && temp.school.equals(school)))
					continue;
			
				if(!visited[e.fnum])
				{
					visited[e.fnum] = true;
					queue.enqueue(temp);
					clique.add(temp.name);
				}
			}
		}
		
		return clique;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g)
	{
		/** COMPLETE THIS METHOD **/
		
		// Every array needed to hold information about all vertices
		int numVertices = g.members.length;
		boolean visited[] = new boolean[numVertices];
		ArrayList<String> connectors = new ArrayList<String>();
		
		int[] dfsnum = new int[numVertices];
		int[] back = new int[numVertices];
		int[] backTrack = new int[numVertices];
		int[] discovery = {0};
		
		
		for(int i=0; i<backTrack.length; i++)
			backTrack[i] = Integer.MAX_VALUE;
		
		for(int i=0; i<numVertices; i++)
			if(!visited[i])
				findConnectors(g, connectors, visited, dfsnum, back, backTrack, i, discovery);
		
		return connectors;
	}
	
	// Discovery of all parents are less than their children guaranteed.
	private static void findConnectors(Graph g, ArrayList<String> connectors,
			boolean[] visited, int[] dfsnum, int[] back, int[] backTrack, int startPoint, int[] discovery)
	{
		// Start dfs with startPoint
		visited[startPoint] = true;
		
		// Discovery refers to the dfs number, each time a new node is discovered, discovery gets incremented by 1
		discovery[0] = discovery[0] + 1;
		dfsnum[startPoint] = discovery[0];
		back[startPoint] = discovery[0];
		
		int numChildren = 0;
		
		// Visit each neighbor:
		for(Friend e = g.members[startPoint].first; e!=null; e = e.next) // suppose startPoint is u
		{
			int v = e.fnum;
			/*
			 * Should v not been visited, it is now a child of u
			 * Since v is connected to u and discovery(v) > discovery(u)
			 */
			if(!visited[v])
			{
				visited[v] = true;
				numChildren++;
				backTrack[v] = startPoint; // parent of v is u
				
				// recursively call Dfs
				findConnectors(g, connectors, visited, dfsnum, back, backTrack, v, discovery);
				
				// Given algorithm:
				back[startPoint] = Math.min(back[startPoint], back[v]);
				
				/* startPoint is variable that we passed:
				 * 
				 * startPoint is a connector if and only if:
				 * --> startPoint is root and has more than 1 child
				 * or 
				 * --> startPoint is not root and 
				 */
				if((backTrack[startPoint] == Integer.MAX_VALUE) && numChildren > 1)
				{
					String name = g.members[startPoint].name;
					if(!connectors.contains(name))
						connectors.add(name);
				}
				
				if((backTrack[startPoint] != Integer.MAX_VALUE) && back[v] >= dfsnum[startPoint])
				{
					String name = g.members[startPoint].name;
					if(!connectors.contains(name))
						connectors.add(name);
				}
				
			}
			else if(v != backTrack[startPoint])
				back[startPoint] = Math.min(back[startPoint], dfsnum[v]); // from algorithm
		}
	}
}


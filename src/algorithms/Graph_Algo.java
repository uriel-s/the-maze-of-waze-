package algorithms;

import java.util.*;


import dataStructure.DGraph;
import dataStructure.DNode;
import dataStructure.Dedge;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms{
	DGraph g;
	public List<node_data> list = new ArrayList <node_data> ();	


	public void init(graph g) {
		this.g=(DGraph) g;		
	}

	@Override
	public void init(String file_name) {
		DGraph g =null;          

		try
		{    
			FileInputStream file = new FileInputStream("file_name"); 
			ObjectInputStream in = new ObjectInputStream(file); 

			g = (DGraph)in.readObject(); 

			in.close(); 
			file.close(); 

			System.out.println("Object has been deserialized"); 
			System.out.println(g);
		} 

		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 

		catch(ClassNotFoundException ex) 
		{ 
			System.out.println("ClassNotFoundException is caught"); 
		} 

	}




	@Override
	public void save(String file_name) {
		DGraph g =null;          
		try
		{    
			FileOutputStream file = new FileOutputStream(file_name); 
			ObjectOutputStream out = new ObjectOutputStream(file); 

			out.writeObject(g); 

			out.close(); 
			file.close(); 

			System.out.println("Object has been serialized"); 
		}   
		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 


	}

	@Override
	public boolean isConnected() {
		all0(g);
		Iterator<node_data> iter= g.getV().iterator();
		DNode root= (DNode) iter.next();
		//painting all the Nodea that connect with Root.
		Rootconect(root);
		while(iter.hasNext()) 
			//if one of the Node isnt painting return false;
		{
			DNode n= (DNode) iter.next();
			if (!n.isVisited() ) { 
				//System.out.println("Node N is rootconect "+n.getKey());
				return false;
			}


		}
		all0(g);

		Iterator<node_data> iter2= g.getV().iterator();
		//checking if every Nodes connect to root
		iter2.next();
		while(iter2.hasNext()) 
		{
			DNode n= (DNode) iter2.next();
			if(ConnectWith (n.getKey(),root.getKey())==false) {	
				//		System.out.println("N isnt conect with root " + n.getKey() ); 
				return false;
			}
		}
		return true;
	}





	public void all0(DGraph g) {
		Iterator<node_data> itrerator= g.getV().iterator();
		DNode n= new DNode();
		while( itrerator.hasNext())
		{		
			n= (DNode) itrerator.next();
			n.setVisited(false);
		}

	}

	public void Rootconect(DNode root)
	{
		if (root.isVisited()) return ;
		root.setVisited(true);

		Iterator<edge_data> iter= g.getE(root.getKey()).iterator();
		Dedge e=  new Dedge(0);

		while(iter.hasNext())
		{
			e=  (Dedge) iter.next();
			int key= e.getDest();
			DNode n = (DNode) g.getVErtex().get(key);
			Rootconect(n) ;
		}
	}
	public boolean ConnectWith(int src,int dest) 
	{
		DNode n = (DNode) g.getNode(src);
		if(n.isVisited())return false;
		n.setVisited(true);
		//if( n.getEdge(dest)!=null ) return true;
		if(n.getEdges().containsKey(dest))return true;

		Iterator<edge_data> iter= g.getE(n.getKey()).iterator();
		Dedge e= new Dedge(0); 

		while(iter.hasNext())
		{
			e=(Dedge) iter.next();
			int key= e.getKey();
			n = (DNode) g.getVErtex().get(key);
			if(ConnectWith(key, dest))return true;
		}

		return false;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		// set all the  Nodes wight= infinit .src Node 0 
		this.invinityAll();
		this.all0(g);
		DNode Src = (DNode) g.getNode(src);
		DNode Dst = (DNode) g.getNode(dest);

		Src.setWeight(0);

		Sourcdijkstra(Src);
		//if(Dst.getWeight()==Double.MAX_VALUE) 
		//  			throw new  RuntimeException("Nodes arent connected");

		return Dst.getWeight();

	}


	public void Sourcdijkstra (DNode src) 
	{ //need to test the first if
		if (src.isVisited())return;
		//	System.out.println("this is "+src.getKey()+" Node");

		Iterator<edge_data> I= g.getE(src.getKey()).iterator();
		Dedge e= new Dedge(0);
		//checking all the nibires for the min wight

		while (I.hasNext() )
		{
			e=(Dedge) I.next();

			minWeight(e);
		}
		//checking  the last nibier for the min wight
		src.setVisited(true);
		//System.out.println("visited "+src.getKey()+" Node");

		NeighborsDijkstra(src);
	}

	public void NeighborsDijkstra (DNode src)
	{
		Iterator<edge_data> I= g.getE(src.getKey()).iterator();
		Dedge e=  new Dedge(0);
		while (I.hasNext() )
		{
			e=(Dedge) I.next();
			DNode n =(DNode) g.getNode(e.getDest());
			Sourcdijkstra(n);
		}	
	}



	public void  minWeight(Dedge e)
	{
		DNode dest = (DNode) g.getNode( e.getDest() );
		DNode src = (DNode) g.getNode( e.getSrc() );
		double NewWeight= e.getWeight()+src.getWeight();
		if(NewWeight<dest.getWeight()) {
			dest.setWeight(NewWeight);	
			SetShortList(src,dest);
		}
	}


	public void SetShortList(DNode src,DNode dest) {	
		List<node_data> ans = new ArrayList <node_data> ();	
		ans.addAll(src.GetShortestPath());
		ans.add(dest);
		dest.setShortestPath(ans);
	}



	public Dedge minEdge(DNode n)
	{
		Iterator<edge_data> I= g.getE(n.getKey()).iterator();
		Dedge e=  (Dedge) I.next();
		Dedge ans=e;
		double min ;
		while (I.hasNext())
		{
			e=(Dedge) I.next();
			if( e.getWeight()>ans.getWeight() ) ans=e;

		}
		return e;
	}

	public void invinityAll() 
	{
		Iterator<node_data> I= g.getV().iterator();
		DNode n= new  DNode();
		while(I.hasNext()) 
		{
			n= (DNode) I.next();
			n.setWeight(Double.MAX_VALUE);
		}
	}



	@Override
	public List<node_data> shortestPath(int src, int dest) {
		double x =shortestPathDist(src, dest);
		DNode n=(DNode) g.getNode(dest);
		return n.GetShortestPath();		

	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		if(!this.isConnected()) return null;
		//all0(g);		
		List<node_data> ans = new ArrayList <node_data> ();	
		List<node_data> tmp = new ArrayList <node_data> ();	

		Iterator<Integer> I= targets.iterator();
		int	src= I.next();
		int dest;
		if(targets.size()<2) return  (List<node_data>) g.getNode(src);

		while(I.hasNext())
		{
			dest=I.next();
			tmp=shortestPath(src, dest);
			ans.addAll(tmp);
			src=dest;
		}
		return ans;
	}

	@Override
	public graph copy() {
		// TODO Auto-generated method stub
		return null;
	}
	public Iterator<node_data> iterator() {
		return this.list.iterator();
	}
}

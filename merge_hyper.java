package multithreading;
import java.io.*;
import java.util.Scanner;
public class merge_hyper extends Thread
{
	static int n[];
	int start, end;
	static int p;
	static merge_hyper t[];
	public merge_hyper()
	{
		this.start=0;
		this.end=0;
	}
	public void run()
	{
		int k= Integer.parseInt(this.getName());
		int x= n.length/p;
		if(n.length%p!=0)
			x+=1;
		start= k*x;
		end= (x*(k+1))-1;
		if(start>=n.length)
		{
			start=-1;
			return;
		}
		if(end>=n.length)
			end= n.length-1;
		
		mergesort(this.start, this.end);
	
		try 
		{
			sleep(500);
		} catch (InterruptedException e1) 
		{
			e1.printStackTrace();
		}
		
		for(int phase=1 ;phase<=(int)(Math.log(p)/Math.log(2)); phase++)
		{                                                               
			                                                            
			int next_id= k+(int)Math.pow(2, phase-1);                   
			int mask= (int)Math.pow(2, phase);                          
			if(k%mask==0)                                               
			{                                                           
				if(t[next_id]!= null)                                    
				{                                                       
					try                                                 
					{                                                   
						t[next_id].join();                              
					}                                                   
					catch(InterruptedException exp)                     
					{                                                   
						System.out.println("Thread Interrupted");       
					}                                                   
					int s= t[next_id].start, e=t[next_id].end;  
					if(t[next_id].start!= -1)
					{
						merge(start,end,s,e);                               
						end= e;
					}
				}                                                       
			}                                                           
		}                                                               
	}
	public void mergesort(int s, int e)
	{
		if(s<e)
		{
			int mid= (s+e)/2;
			mergesort(s, mid);
			mergesort(mid+1, e);
			merge(s,mid,mid+1, e);
		}
	}
	public void merge(int s1, int e1,int s2, int e2)
	{
		int i=s1, j=s2;
		int temp[]= new int[e2-s1+1];
		for(int k=0; k<temp.length; k++)
		{
			if(j> e2 || (i<=e1 && n[i]<= n[j]))
			{
				temp[k]= n[i];
				i++;
			}
			else
			{
				temp[k]= n[j];
				j++;
			}
		}
		j=s1;
		for(i=0; i<temp.length; i++)
		{
			n[j]= temp[i];
			j++;
		}
	}
	public static void main(String args[])throws IOException
	{
		Scanner sc= new Scanner(System.in);
		System.out.println("Enter number of processors in power of 2: ");
		p= sc.nextInt();
		System.out.println("Enter number of elements: ");
		int num= sc.nextInt();
		n= new int[num];
		System.out.println("Enter the elements to be sorted: ");
		for(int i=0; i<num; i++)
		n[i]= sc.nextInt();
		t= new merge_hyper[p];
		for(int i=0; i<p; i++)
		{
			t[i]= new merge_hyper();
			t[i].setName(Integer.toString(i));
			t[i].start();
		}
		try
		{
			t[0].join();
		}
		catch(InterruptedException exp)
		{
			System.out.println("Thread Interrupted");
		}
	
		System.out.println("Sorted array");
		for(int i= 0; i<n.length; i++)
		{
			System.out.print(n[i]+" ");
		}
		sc.close();
	}
}

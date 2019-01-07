import java.util.*;
import java.io.*;

public class ClosestPair {
  public static void main(String[] args) throws IOException {
		ArrayList<Point> list = new ArrayList<Point>();
		BufferedReader br = new BufferedReader(new FileReader("data05_closest.txt"));
		String line = br.readLine();

		while (line != null) {
			StringTokenizer st = new StringTokenizer(line, ",");
			while (st.hasMoreTokens()) {
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				Point P = new Point(x, y);
				list.add(P); 
			}
			line = br.readLine();
		}
	  
		System.out.println("[Input Data]");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
	  
		x_Divide_Conquer(list, 0, list.size()-1);
	  	Pair CP = Divide_Conquer(list);
	  
		System.out.println("[Output Data]");
		System.out.println("CloSest Pair -> P1:" + CP.p1 + " --- P2:" + CP.p2);
		System.out.printf("Distance -> " + "%.3f", CP.d);
	}
	
	private static double distance(Point a, Point b){
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public static void x_Divide_Conquer(List<Point> A, int p, double r) {
		if (p < r) {
			int q = x_partition(A, p, (int) r);
			x_Divide_Conquer(A, p, q - 1);
			x_Divide_Conquer(A, q + 1, r);
		}
	}
  
	public static void y_Divide_Conquer(List<Point> A, int p, double r) {
		if (p < r) {
			int q = y_partition(A, p, (int) r);
			y_Divide_Conquer(A, p, q - 1);
			y_Divide_Conquer(A, q + 1, r);
		}
	}
	public static int x_partition(List<Point> A, int p, int r)  {
		Point mypoint = A.get(r);
		int i = p - 1;
		for (int j = p; j <= r - 1; j++) {
			if (A.get(j).getX() <= mypoint.getX()) {
				i = i + 1;
				Point temp = A.get(i);
				A.set(i, A.get(j));
				A.set(j, temp);
			}
		}
		i = i + 1;
		Point temp = A.get(i);
		A.set(i, A.get(r));
		A.set(r, temp);
		return i; 
	}
	public static int y_partition(List<Point> A, int p, int r)  {
		Point mypoint = A.get(r);
		int i = p - 1;
		for (int j = p; j <= r - 1; j++) {
			if (A.get(j).getY() <= mypoint.getY()) {
				i = i + 1;
				Point temp = A.get(i);
				A.set(i, A.get(j));
				A.set(j, temp);
			}
		}
		i = i + 1;
		Point temp = A.get(i); 
		A.set(i, A.get(r));
		A.set(r, temp);
		return i; 
	}
	public static Pair Divide_Conquer(List<Point> A) {
		ArrayList<Point> SortX= new ArrayList<Point>(A);
		x_Divide_Conquer(SortX, 0, SortX.size() - 1); 
		
		ArrayList<Point> SortY = new ArrayList<Point>(A);
		y_Divide_Conquer(SortY, 0, SortY.size() - 1);
		
		return Closest_Pair(SortX, SortY);
	}
	public static Pair BruteForce(List<Point> A) {
		int size = A.size();
		
		if (size < 2)
			return null;
		
		Pair pair = new Pair(A.get(0), A.get(1)); 
		
		if (size > 2) {
			for (int i = 0; i < size - 1; i++) {
				Point point1 = A.get(i);
				for (int j = i + 1; j < size; j++) {
					Point point2 = A.get(j);
					double distance = distance(point1, point2);
					
					if (distance < pair.d)
						pair.Pair_Dist(point1, point2, distance);
				}
			}
		}
		return pair;
	}
	public static Pair Closest_Pair(List<Point> SortX, List<Point> SortY) {
		int size = SortX.size();
		
		if (size <= 3) 
			return BruteForce(SortX);
		
		int mid = size >> 1;
		List<Point> left = SortX.subList(0, mid);
		List<Point> right = SortX.subList(mid, size);

		List<Point> temp = new ArrayList<Point>(left); 
		y_Divide_Conquer(temp, 0, temp.size()-1); 
		Pair CP = Closest_Pair(left, temp);
		
		temp.clear();
		temp.addAll(right);
		
		y_Divide_Conquer(temp, 0, temp.size()-1); 
		Pair CP_right = Closest_Pair(right, temp);

		if (CP_right.d < CP.d)
			CP = CP_right;
		
		temp.clear(); 
		double min_dist = CP.d;
		
		for(int i = 0; i < SortY.size(); i++) {
			if (Math.abs((SortY.get(i).x-SortY.get(mid).x))<min_dist) {
				temp.add(SortY.get(i));
			}
		}
		
		for (int i = 0; i < temp.size() - 1; i++) {
			Point point1 = temp.get(i);
			for (int j = i + 1; j < temp.size(); j++) {
				Point point2 = temp.get(j);
				if ((point2.y - point1.y) >= min_dist)
					break;
				double distance = distance(point1, point2);
				if (distance < CP.d) {
					CP.Pair_Dist(point1, point2, distance);
					min_dist = distance;
				}
			}
		}
		return CP;
	}
	
	  
  private static class Point {
		public double x = 0.0;
		public double y = 0.0;
		
		public double getX() {
			return x;
		}
		public double getY() {
			return y;
		}
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
		public String toString() {
			return this.x + "," + this.y;
		}
	}
	
	public static class Pair {
		public Point p1; 
		public Point p2;
		public double d = 0.0;

		public Pair(Point point1, Point point2) {
			this.p1 = point1;
			this.p2 = point2;
			this.d = distance(point1, point2);
		}
		public void Pair_Dist(Point point1, Point point2, double distance) {
			this.p1 = point1;
			this.p2 = point2;
			this.d = distance;
		}
	}
	
}

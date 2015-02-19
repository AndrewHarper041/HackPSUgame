/**
 * Overrides java's Point so that it only uses int values.
 * @author Tommy
 *
 */
public class Point {
	public int x, y;
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	public String toString(){
		return x + "," + y;
	}
}

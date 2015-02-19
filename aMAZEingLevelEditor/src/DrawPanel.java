import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

/**
 * Drawing/display area for editor.
 * @author Tommy
 *
 */
public class DrawPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3035343344915833396L;
	private final int TILE_SIZE = 30;
	private static int selected = 0;
	private static Point point1 = new Point(-1, -1);
	private static Point point2 = new Point(-1, -1);
	private int buttonDragged = 0;
	private final Color BG_COLOR = new Color(180,180,150);
	private int[][] map;
	private static HashMap<Integer, Image> images;
	//	private HashMap<Point, Point> switchSet;
	private static ArrayList<Point> sourceList;
	private static ArrayList<Point> targetList;
	private int xDisplace = 0, yDisplace = 0;

	public static void resetPoints(){
		point1 = new Point(-1, -1);
		point2 = new Point(-1, -1);
	}
	public void paint(Graphics g){
		//Clear everything.
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		if (map == null){
			return;
		}

		//Draw base map.
		for (int x = 0; x < map.length; ++x){
			for (int y = 0; y < map[0].length; ++y){
				g.drawImage(images.get(map[x][y]), (x * TILE_SIZE) + xDisplace,
						(y * TILE_SIZE) + yDisplace, null);
			}
		}

		//Draws outline on selected relationship.
		if (selected >= images.size()){
			if (selected >= images.size() + sourceList.size()){
				if (point1.x != -1){
					g.setColor(Color.RED);
					partialRect((point1.x * TILE_SIZE) + xDisplace, (point1.y * TILE_SIZE) + yDisplace,
							TILE_SIZE, TILE_SIZE, 3, g);
				}
				if (point2.x != -1){
					g.setColor(Color.BLUE);
					partialRect((point2.x * TILE_SIZE) + xDisplace, (point2.y * TILE_SIZE) + yDisplace,
							TILE_SIZE, TILE_SIZE, 3, g);
				}
			} else{
				Point pt1 = sourceList.get(selected - images.size());
				Point pt2 = targetList.get(selected - images.size());
				g.setColor(Color.RED);
				partialRect((pt1.x * TILE_SIZE) + xDisplace, (pt1.y * TILE_SIZE) + yDisplace,
						TILE_SIZE, TILE_SIZE, 3, g);
				g.setColor(Color.BLUE);
				partialRect((pt2.x * TILE_SIZE) + xDisplace, (pt2.y * TILE_SIZE) + yDisplace,
						TILE_SIZE, TILE_SIZE, 3, g);
			}
		}
	}
	private void partialRect(int x, int y, int width, int height, int thickness, Graphics g){
		for (int i = 0; i < thickness; ++i){
			g.drawRect(x + i, y + i, width - i * 2, height - i * 2);
		}
	}

	public int[][] getMap(){
		return map;
	}
	public ArrayList<Point> getSourceList(){
		return sourceList;
	}
	public ArrayList<Point> getTargetList(){
		return targetList;
	}
	public static void setSourceList(ArrayList<Point> sourceList){
		DrawPanel.sourceList = sourceList;
	}
	public static void setTargetList(ArrayList<Point> targetList){
		DrawPanel.targetList = targetList;
	}
	public void sendData(int[][] map, ArrayList<Point> sourceList, ArrayList<Point> targetList){
		this.map = map;
		DrawPanel.sourceList = sourceList;
		DrawPanel.targetList = targetList;
		xDisplace = 0;
		yDisplace = 0;
		point1 = new Point(-1, -1);
		point2 = new Point(-1, -1);
		SelectPanel.setSourceList(sourceList);
		SelectPanel.setTargetList(targetList);

		this.getParent().repaint();
	}
	public static void setSelected(int i){
		selected = i;
	}

	/**
	 * Should be called right after construction.
	 * @param images Map of images from Editor.
	 */
	public void initImages(HashMap<Integer, Image> images){
		DrawPanel.images = images;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();

		switch(code){
		case 87:
		case 38:
			yDisplace += TILE_SIZE;
			break;
		case 65:
		case 37:
			xDisplace += TILE_SIZE;
			break;
		case 83:
		case 40:
			yDisplace -= TILE_SIZE;
			break;
		case 68:
		case 39:
			xDisplace -= TILE_SIZE;
			break;
		}
		getParent().repaint();
	}
	public void keyReleased(KeyEvent ke) {}
	public void keyTyped(KeyEvent ke) {}

	public void mouseDragged(MouseEvent me) {
		if (map == null){
			return;
		}
		//		if (inDrag){
		Point pt = new Point(me.getPoint().x, me.getPoint().y);
		int x = -xDisplace + pt.x;
		int y = -yDisplace + pt.y;
		x /= TILE_SIZE;
		y /= TILE_SIZE;
		if (buttonDragged == MouseEvent.BUTTON1){
			if (x >= 0 && x < map.length && y >= 0 && y < map[0].length){
				if (selected >= images.size()){
					//Sets the first point of a relationship, or [Sets the second
					//point and adds the relationship]. If both have been set, does
					//nothing.
					point1 = new Point(x, y);
					if (selected >= images.size() + sourceList.size()){
						if (point1.x != -1 && point2.x != -1){
							sourceList.add(point1);
							targetList.add(point2);
							SelectPanel.setSourceList(sourceList);
							SelectPanel.setTargetList(targetList);
						}
					} else{
						sourceList.set(selected - images.size(), point1);
					}
				} else{
					map[x][y] = selected;
				}
			}
		} else if (buttonDragged == MouseEvent.BUTTON3){
			if (selected >= images.size()){
				point2 = new Point(x, y);
				if (selected >= images.size() + sourceList.size()){
					if (point1.x != -1 && point2.x != -1){
						sourceList.add(point1);
						targetList.add(point2);
						SelectPanel.setSourceList(sourceList);
						SelectPanel.setTargetList(targetList);
					}
				} else{
					targetList.set(selected - images.size(), point2);
				}
			}
		}
		//		}
		getParent().repaint();
	}
	public void mouseMoved(MouseEvent me) {}
	public void mouseClicked(MouseEvent me) {
		if (map == null){
			return;
		}
		Point pt = new Point(me.getPoint().x, me.getPoint().y);
		int x = -xDisplace + pt.x;
		int y = -yDisplace + pt.y;
		x /= TILE_SIZE;
		y /= TILE_SIZE;
		if (me.getButton() == MouseEvent.BUTTON1){
			if (x >= 0 && x < map.length && y >= 0 && y < map[0].length){
				if (selected >= images.size()){
					//Sets the first point of a relationship, or [Sets the second
					//point and adds the relationship]. If both have been set, does
					//nothing.
					point1 = new Point(x, y);
					if (selected >= images.size() + sourceList.size()){
						if (point1.x != -1 && point2.x != -1){
							sourceList.add(point1);
							targetList.add(point2);
							SelectPanel.setSourceList(sourceList);
							SelectPanel.setTargetList(targetList);
						}
					} else{
						sourceList.set(selected - images.size(), point1);
					}
				} else{
					map[x][y] = selected;
				}
			}
		} else if (me.getButton() == MouseEvent.BUTTON3){
			if (selected >= images.size()){
				point2 = new Point(x, y);
				if (selected >= images.size() + sourceList.size()){
					if (point1.x != -1 && point2.x != -1){
						sourceList.add(point1);
						targetList.add(point2);
						SelectPanel.setSourceList(sourceList);
						SelectPanel.setTargetList(targetList);
					}
				} else{
					targetList.set(selected - images.size(), point2);
				}
			}
		}

		getParent().repaint();
	}
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}
	public void mousePressed(MouseEvent me) {
		buttonDragged = me.getButton();
	}
	public void mouseReleased(MouseEvent me) {
		buttonDragged = 0;
	}
}

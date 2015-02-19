import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 * Allows the user to select which tile to draw and stuff.
 * @author Tommy
 *
 */
public class SelectPanel extends JPanel implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8712703485081396198L;
	private static HashMap<Integer, Image> images;
	private int rDisplace = 0;
	private static ArrayList<Point> sourceList;
	private static ArrayList<Point> targetList;
	private final int TILE_SIZE = 30;
	private final Color BG_COLOR = new Color(200,220,220);
	private static int selectedImg;

	public SelectPanel(){
		super();
		sourceList = new ArrayList<Point>();
		targetList = new ArrayList<Point>();
	}

	public static ArrayList<Point> getSourceList(){
		return sourceList;
	}
	public static ArrayList<Point> getTargetList(){
		return sourceList;
	}

	public static void setSourceList(ArrayList<Point> sourceList){
		SelectPanel.sourceList = sourceList;
	}
	public static void setTargetList(ArrayList<Point> targetList){
		SelectPanel.targetList = targetList;
	}

	public static int getSelected(){
		return selectedImg;
	}

	public void paint(Graphics g){
		g.setFont(new Font("Arial", 1, 20));

		//Clear drawing area.
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		int yDisplace = 10;
		//Draw selected image.
		if (selectedImg >= images.size()){
			g.setColor(Color.WHITE);
			g.fillRect(10, yDisplace, TILE_SIZE, TILE_SIZE);
			g.setColor(Color.BLACK);
			g.drawString(selectedImg - images.size() + "", 10, yDisplace + 20);
		} else{
			g.drawImage(images.get(selectedImg), 10, yDisplace, null);
		}
		yDisplace += TILE_SIZE + 10;

		//Bar.
		g.setColor(Color.BLACK);
		g.fillRect(0, yDisplace, TILE_SIZE + 20, 4);
		yDisplace += 14;

		//Draw list of images.
		for (int i = 0; i < images.size(); ++i){
			g.drawImage(images.get(i), 10, yDisplace, null);
			yDisplace += TILE_SIZE + 10;
		}

		//Bar.
		g.setColor(Color.BLACK);
		g.fillRect(0, yDisplace, getWidth(), 4);
		yDisplace += 14;

		//Paint up button
		g.setColor(Color.GREEN);
		g.fillRect(0, yDisplace, getWidth(), 15);
		yDisplace += 25;

		//Paint relationships.
		int top = yDisplace;
		for (int i = 0; i < sourceList.size(); ++i){
			if (yDisplace + rDisplace < top){
				yDisplace += TILE_SIZE + 10;
			} else{
				g.setColor(Color.WHITE);
				g.fillRect(10, yDisplace + rDisplace, TILE_SIZE, TILE_SIZE);
				g.setColor(Color.BLACK);
				g.drawString(i + "", 10, yDisplace + rDisplace + 20);
				yDisplace += TILE_SIZE + 10;
				//Cutoff point
				if (yDisplace + rDisplace >= getHeight() - TILE_SIZE * 3){
					//Do nothing.
				}
			}
		}

		//Paint R+ "button".
		g.setColor(Color.BLACK);
		g.fillRect(8, getHeight() - 35 - TILE_SIZE - 2, TILE_SIZE + 4, TILE_SIZE + 4);
		g.setColor(new Color(240,200,200));
		g.fillRect(10, getHeight() - 35 - TILE_SIZE, TILE_SIZE, TILE_SIZE);
		g.setColor(Color.BLACK);
		g.drawString("R+", 10, getHeight() - 15 - TILE_SIZE);
		yDisplace += TILE_SIZE + 10;

		//Paint down button
		g.setColor(Color.GREEN);
		g.fillRect(0, getHeight() - 25, getWidth(), 15);
	}

	public void initImages(HashMap<Integer, Image> images){
		SelectPanel.images = images;
	}
	public int getTileSize(){
		return TILE_SIZE;
	}
	public static void setSelected(int s){
		selectedImg = s;
	}


	public void mouseClicked(MouseEvent me){}
	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	@Override
	public void mousePressed(MouseEvent me){
		int mouseBtn = me.getButton();
		Point pt = new Point(me.getPoint().x, me.getPoint().y);
		if (mouseBtn == MouseEvent.BUTTON1){
			//If it is within the clickable column.
			if (pt.x >= 10 && pt.x <= getWidth() - 10){
				//Check regular tiles.
				int yDisplace = TILE_SIZE + 34;
				for (int i = 0; i < images.size(); ++i){
					int lBound = yDisplace;
					int uBound = yDisplace + TILE_SIZE;
					if (pt.y >= lBound && pt.y <= uBound){
						selectedImg = i;
						DrawPanel.setSelected(i);
						i = images.size();
						DrawPanel.resetPoints();
					}
					yDisplace += TILE_SIZE + 10;
				}

				//Skip the bar
				yDisplace += 14;

				//Check the up button.
				if (pt.y >= yDisplace && pt.y <= yDisplace + 20){
					rDisplace += TILE_SIZE + 10;
					System.out.println(rDisplace);
				}
				yDisplace += 25;

				//Check relationships.
				int top = yDisplace;
				for (int i = 0; i < sourceList.size(); ++i){
					if (yDisplace + rDisplace < top){
						yDisplace += TILE_SIZE + 10;
					} else{
						int lBound = yDisplace + rDisplace;
						int uBound = lBound + TILE_SIZE;
						if (pt.y >= lBound && pt.y <= uBound){
							selectedImg = i + images.size();
							DrawPanel.setSelected(selectedImg);
							i = sourceList.size();
							DrawPanel.resetPoints();
						}
						yDisplace += TILE_SIZE + 10;
						if (yDisplace + rDisplace >= getHeight() - TILE_SIZE * 3){
							//Do nothing.
						}
					}
				}

				//Check R+ button.
				int lBound = getHeight() - 35 - TILE_SIZE;
				int uBound = lBound + TILE_SIZE;
				if (pt.y >= lBound && pt.y <= uBound){
					selectedImg = sourceList.size() + images.size();
					DrawPanel.setSelected(selectedImg);
				}

				//Check down button
				if (pt.y >= getHeight() - 25 && pt.y <= getHeight() - 10){
					rDisplace -= (TILE_SIZE + 10);
					System.out.println(rDisplace);
				}
				yDisplace += 25;
			}
		}
		if (mouseBtn == MouseEvent.BUTTON3){
			//Check regular tiles.
			int yDisplace = TILE_SIZE + 34;
			for (int i = 0; i < images.size(); ++i){
				yDisplace += TILE_SIZE + 10;
			}

			//Skip the bar
			yDisplace += 14;

			for (int i = 0; i < sourceList.size(); ++i){
				int lBound = yDisplace;
				int uBound = yDisplace + TILE_SIZE;
				if (pt.y >= lBound && pt.y <= uBound){
					//Remove the relationship.
					sourceList.remove(i);
					targetList.remove(i);
					i = sourceList.size();
					DrawPanel.setSourceList(sourceList);
					DrawPanel.setTargetList(targetList);
					DrawPanel.resetPoints();
				}
				yDisplace += TILE_SIZE + 10;
			}
		}
		getParent().repaint();
	}
	public void mouseReleased(MouseEvent me){}
}

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Editor for the aMAZEing game.
 * @author Tommy
 *
 */
public class Editor extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3956148653239718864L;
	private DrawPanel drawPnl;
	private SelectPanel selectPnl;	private HashMap<Integer, Image> images;
	public static void main(String[] args){
		Editor frm = new Editor();
		frm.start();
	}
	public void start(){
		//Do stuff about the frame.
		setTitle("aMAZEing Level Editor!");
		setMinimumSize(new Dimension(600,500));
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initMenuBar();


		//Do init stuff for panels.
		initImages();
		drawPnl = new DrawPanel();
		drawPnl.initImages(images);
		drawPnl.addMouseListener(drawPnl);
		drawPnl.addMouseMotionListener(drawPnl);
		addKeyListener(drawPnl);
		selectPnl = new SelectPanel();
		selectPnl.initImages(images);
		selectPnl.addMouseListener(selectPnl);
		selectPnl.setPreferredSize(new Dimension(30 + 20, 500));



		add(drawPnl, BorderLayout.CENTER);
		add(selectPnl, BorderLayout.EAST);

		setVisible(true);
	}
	private void initMenuBar(){
		JMenuBar bar = new JMenuBar();
		JMenu fileM = new JMenu("File");
		JMenuItem newM = new JMenuItem("New");
		newM.setActionCommand("new");
		newM.addActionListener(this);
		JMenuItem openM = new JMenuItem("Open");
		openM.setActionCommand("open");
		openM.addActionListener(this);
		JMenuItem saveM = new JMenuItem("Save");
		saveM.setActionCommand("save");
		saveM.addActionListener(this);
		JMenuItem exitM = new JMenuItem("Exit");
		exitM.addActionListener(this);
		exitM.setActionCommand("exit");
		fileM.add(newM);
		fileM.add(openM);
		fileM.add(saveM);
		fileM.add(exitM);
		bar.add(fileM);
		setJMenuBar(bar);
	}
	/**
	 * Fill the images map by reading from file.
	 */
	public void initImages(){
		BufferedImage img;
		images = new HashMap<Integer, Image>();
		int i = 0;
		try{
			img = ImageIO.read(new File("src/images/blank.png"));
			images.put(i, img);
		} catch (IOException e){
			e.printStackTrace();
			System.err.println("Could not load image !");
		}
		i++;

		try{
			img = ImageIO.read(new File("src/images/player.png"));
			images.put(i, img);
		} catch (IOException e){
			e.printStackTrace();
			System.err.println("Could not load image !");
		}
		i++;
		try{
			img = ImageIO.read(new File("src/images/end.png"));
			images.put(i, img);
		} catch (IOException e){
			e.printStackTrace();
			System.err.println("Could not load image !");
		}
		i++;
		try{
			img = ImageIO.read(new File("src/images/wall.png"));
			images.put(i, img);
		} catch (IOException e){
			e.printStackTrace();
			System.err.println("Could not load image !");
		}
		i++;
		try{
			img = ImageIO.read(new File("src/images/gate.png"));
			images.put(i, img);
		} catch (IOException e){
			e.printStackTrace();
			System.err.println("Could not load image !");
		}
		i++;
		try{
			img = ImageIO.read(new File("src/images/switch.png"));
			images.put(i, img);
		} catch (IOException e){
			e.printStackTrace();
			System.err.println("Could not load image !");
		}
		i++;
	}
	private void exportData(File file){
		try{
			FileWriter fstream = new FileWriter(file.getName());
			BufferedWriter writer = new BufferedWriter(fstream);

			int[][] map = drawPnl.getMap();
			if (map == null){
				System.err.println("Error in saving file!");
				return;
			}

			ArrayList<Point> sourceList = drawPnl.getSourceList();
			ArrayList<Point> targetList = drawPnl.getTargetList();
			int width = map.length;
			int height = map[0].length;

			writer.write(width + "," + height + "\n");
			for (int y = 0; y < height; ++y){
				for (int x = 0; x < width; ++x){
					writer.write(map[x][y] + " ");
				}
				writer.write("\n");
			}

			for (int i = 0; i < sourceList.size(); ++i){
				writer.write(sourceList.get(i) + " " + targetList.get(i) + " \n");
			}

			writer.close();
		} catch(Exception e){
			System.err.println("Error in saving file!");
		}
	}
	private void importData(File file){
		try{
			Scanner sc = new Scanner(new FileInputStream(file));
			String dim = sc.next();
			for (int i = 0; i < dim.length(); ++i){
				if (dim.charAt(i) == ','){
					dim = dim.substring(0, i) + " " + dim.substring(i + 1, dim.length());
				}
			}
			Scanner scDim = new Scanner(dim);
			int x = scDim.nextInt();
			int y = scDim.nextInt();
			int[][] map = new int[x][y];
			for (int j = 0; j < y; ++j){
				for (int i = 0; i < x; ++i){
					map[i][j] = sc.nextInt();
				}
			}

			ArrayList<Point> sourceList = new ArrayList<Point>();
			ArrayList<Point> targetList = new ArrayList<Point>();
			sc.nextLine();
			while (sc.hasNext()){
				dim = sc.nextLine();
				if (dim.length() > 2){
					for (int i = 0; i < dim.length(); ++i){
						if (dim.charAt(i) == ','){
							dim = dim.substring(0, i) + " " + dim.substring(i + 1, dim.length());
						}
					}
					scDim = new Scanner(dim);
					Point source = new Point(scDim.nextInt(), scDim.nextInt());
					Point target = new Point(scDim.nextInt(), scDim.nextInt());
					sourceList.add(source);
					targetList.add(target);
				}
			}
			drawPnl.sendData(map, sourceList, targetList);
			SelectPanel.setSourceList(sourceList);
			SelectPanel.setTargetList(targetList);
		} catch (Exception e){
			e.printStackTrace();
			System.err.println("File is corrupt or not found!");
		}
	} 

	@Override
	public void actionPerformed(ActionEvent ae){
		String command = ae.getActionCommand();
		if (command.equals("new")){
			JTextField xField = new JTextField();
			JTextField yField = new JTextField();
			JComponent[] fields = new JComponent[]{
					new JLabel("Width:"), xField, new JLabel("Height:"), yField
			};
			JOptionPane.showMessageDialog(null, fields, "New Map", JOptionPane.PLAIN_MESSAGE);

			try{
				int x = Integer.parseInt(xField.getText());
				int y = Integer.parseInt(yField.getText());
				if (x < 1 || y < 1){
					throw new Exception();
				}
				int[][] map = new int[x][y];
				ArrayList<Point> sourceList = new ArrayList<Point>();
				ArrayList<Point> targetList = new ArrayList<Point>();
				drawPnl.sendData(map, sourceList, targetList);
				repaint();

			} catch (Exception e){
				System.err.println("The values were not valid integers!");
			}


		} else if (command.equals("open")){
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("src/"));
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
				importData(file);
			}
		} else if (command.equals("save")){
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("src/"));
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
				exportData(file);
			}
		} else if (command.equals("exit")){
			setVisible(false);
			System.exit(0);
		}
	}

}

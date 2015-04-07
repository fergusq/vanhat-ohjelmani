package cubegame.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileFilter;

import cubegame.map.math.PerlinNoise;
import cubegame.save.BCCHelper;

class MapPane extends JDesktopPane{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private MapViewer2 control;

		public MapPane(MapViewer2 control){
			super();
			this.control = control;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (int x = 0; x < MapViewer2.SIZE; x++){
				for (int y = 0; y < MapViewer2.SIZE; y++){
					g.clearRect(x, y, 1, 1);
					double n = control.noiseMap[x][y];
					g.setColor(MapViewer2.getHeightColor(n));
					g.drawOval(x, y, 1, 1);
				}
			}
			
		}

	}

public class MapViewer2 extends JFrame implements ActionListener{
	
	
	
	enum NoiseGenerator{
		PerlinMapGenerator, 
		PerlinGenerator, 
		PerlinNoise, 
		PerlinNoiseC, 
		ImprovedNoise, 
		Noise, 
		NoiseGenerator
	}
	
	enum MsgBox{
		Ok,
		OkCancel,
		YesNo,
		YesNoCancel,
		OK,
		YES,
		NO,
		CANCEL
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Noise noise;
	
	double[][] noiseMap = new double[640][640];

	private int seed = 2;
	@SuppressWarnings("unused")
	private int factor = 1;

	private static boolean round = true;
	
	static final int SIZE = 640;
	public static final int HALF_SIZE = SIZE / 2;
	public float scale = 6.0f;

	private NoiseGenerator noiseGen = NoiseGenerator.PerlinMapGenerator;

	private boolean isEmpty;
	
	private JInternalFrame d;
	private JTextField smoothnessArea;
	private JTextField maxMoveArea;
	private JButton saveOptions;
	
	private int maxMove = 32;
	private int smoothness = 16;
	private int erodeFactor = 10;

	private JTextField erodeArea;

	private JDesktopPane mapPane;
	
	JFileChooser fd;
	
	public double[][] generateTerrain(int seed){
		PerlinMapGenerator h = new PerlinMapGenerator(640, seed);
		h.addPerlinNoise(scale);
		h.perturb(32.0f, maxMove);
		for (int i = 0; i < erodeFactor; i++ )
		h.erode(smoothness);
		h.smoothen();
		
		double[][] map = new double[640][640];
		for (int x = 0; x < SIZE; x++){
			for (int y = 0; y < SIZE; y++){
				double n = h.heights[x][y];
				map[x][y] = n;
			}
		}
		return map;
	}
	
	/*@Override
	public void paint(Graphics g) {
		for (int x = 0; x < SIZE; x++){
			for (int y = 0; y < SIZE; y++){
				g.clearRect(x, y, 1, 1);
				double n = noiseMap[x][y];
				g.setColor(getHeightColor(n));
				g.drawOval(x, y, 1, 1);
			}
		}
		super.paint(g);
	}*/

	public static Color getHeightColor(double n) {
		if (n < -1){
			return Color.black;
		} /*else if (n < -0.9){
			return new Color(0.1f, 0.1f, 0.1f);
		} else if (n < -0.8){
			return new Color(0.15f, 0.15f, 0.15f);
		} else if (n < -0.7){
			return new Color(0.2f, 0.2f, 0.2f);
		} else if (n < -0.6){
			return new Color(0.25f, 0.25f, 0.25f);
		} else if (n < -0.5){
			return new Color(0.3f, 0.3f, 0.3f);
		} else if (n < -0.4){
			return new Color(0.35f, 0.35f, 0.35f);
		} else if (n < -0.3){
			return new Color(0.4f, 0.4f, 0.4f);
		} else if (n < -0.2){
			return new Color(0.45f, 0.45f, 0.45f);
		} else if (n < -0.1){
			return new Color(0.5f, 0.5f, 0.5f);
		} else if (n < 0.1){
			return new Color(0.55f, 0.55f, 0.55f);
		} else if (n < 0.2){
			return new Color(0.6f, 0.6f, 0.6f);
		} else if (n < 0.3){
			return new Color(0.65f, 0.65f, 0.65f);
		} else if (n < 0.4){
			return new Color(0.7f, 0.7f, 0.7f);
		} else if (n < 0.5){
			return new Color(0.75f, 0.75f, 0.75f);
		} else if (n < 0.6){
			return new Color(0.8f, 0.8f, 0.8f);
		} else if (n < 0.7){
			return new Color(0.85f, 0.85f, 0.85f);
		} else if (n < 0.8){
			return new Color(0.9f, 0.9f, 0.9f);
		} else if (n < 0.9){
			return new Color(0.95f, 0.95f, 0.95f);
		}*/
		double h = n / 2;
		float f = (float) (h + 0.5);
		if (round) f = (float) roundTwoDecimals(f);
		try{
			return new Color(f, f, f);
		} catch (Exception ex){
		//	println(f+"");
		}
		return null;
	}

	public static double roundTwoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("0.05");
    	return Double.valueOf(twoDForm.format(d).replace(',', '.'));
	}
	
	public static double roundOneDecimals(double d) {
    	DecimalFormat oneDForm = new DecimalFormat("0.5");
    	return Double.valueOf(oneDForm.format(d).replace(',', '.'));
	}
	
	public static int randomInt() {
		Random r = new Random();
		int i = r.nextInt(100);
		return i;
	}
	
	public static float randomFloat() {
		Random r = new Random();
		float i = r.nextFloat();
		return i;
	}

	public static void println(String s) {
		System.out.println(s);
		
	}

	public void init(){
		if (!isEmpty){
		//	if (msgbox("You have unsaved changes. Overwrite?", "Generate new map", MsgBox.YesNo) == MsgBox.NO) return;
		}
		isEmpty = false;
		noise = new Noise(new Random(), 1, SIZE, SIZE);
		noise.initialise();
		switch (noiseGen){
		case PerlinMapGenerator:
			noiseMap = generateTerrain(seed);
			break;
		case PerlinGenerator:
			PerlinGenerator g = new PerlinGenerator(seed);
			for (int x = 0; x < SIZE; x++){
				for (int y = 0; y < SIZE; y++){
					double n = g.noise(scale * x / (float)SIZE, scale * y / (float)SIZE, 0);
					noiseMap[x][y] = n;
				}
			}
			break;
		case PerlinNoise:
			PerlinNoise p = new PerlinNoise();
			for (int x = 0; x < SIZE; x++){
				for (int y = 0; y < SIZE; y++){
					double n = p.noise2(scale * x / (float)SIZE, scale * y / (float)SIZE);
					noiseMap[x][y] = n;
				}
			}
			break;
		case PerlinNoiseC:
			for (int x = 0; x < SIZE; x++){
				for (int y = 0; y < SIZE; y++){
					double n = PerlinNoiseC.perlinNoise2D(scale * x / (float)SIZE, scale * y / (float)SIZE, seed, scale, erodeFactor);
					noiseMap[x][y] = n;
				}
			}
			break;
		case ImprovedNoise:
			for (int x = 0; x < SIZE; x++){
				for (int y = 0; y < SIZE; y++){
					double n = ImprovedNoise.noise(scale * x / (float)SIZE, scale * y / (float)SIZE, 0.1);
					noiseMap[x][y] = n;
				}
			}
			break;
		case Noise:
			noiseMap = noise.toMap();
			break;
		}
	}
	

	public MapViewer2() {
		setSize(SIZE, SIZE);
		setTitle("CubeGame Map Preview");
		setResizable(false);
		
		isEmpty = true;
		
		//JDesktopPane desktop = new JDesktopPane();
		//setContentPane(desktop);
		
		JMenuBar bar = new JMenuBar();
		
		JMenu FileMenu = new JMenu("File");
		
		FileMenu.add(new JMenuItem("Open BCC file"));
		FileMenu.getItem(0).addActionListener(this);
		FileMenu.addSeparator();
		FileMenu.add(new JMenuItem("Save as BinaryCube (BCC)"));
		FileMenu.getItem(2).addActionListener(this);
		FileMenu.add(new JMenuItem("Save as CubeMapSave (CMS)"));
		FileMenu.getItem(3).addActionListener(this);
		
		JMenu GenerateMenu = new JMenu("Generate");
		
		GenerateMenu.add(new JMenuItem("Custom seed"));
		GenerateMenu.getItem(0).addActionListener(this);
		GenerateMenu.add(new JMenuItem("Random seed"));
		GenerateMenu.getItem(1).addActionListener(this);
		GenerateMenu.addSeparator();
		GenerateMenu.add(new JMenuItem("Custom factor"));
		GenerateMenu.getItem(3).addActionListener(this);
		GenerateMenu.addSeparator();
		GenerateMenu.add(new JMenuItem("Select noise generator"));
		GenerateMenu.getItem(5).addActionListener(this);
		GenerateMenu.add(new JMenuItem("Advanced generator options"));
		GenerateMenu.getItem(6).addActionListener(this);
		
		JMenu ViewMenu = new JMenu("View");
		
		ViewMenu.add(new JMenuItem("Custom scale"));
		ViewMenu.getItem(0).addActionListener(this);
		ViewMenu.addSeparator();
		ViewMenu.add(new JMenuItem("Smooth"));
		ViewMenu.getItem(2).addActionListener(this);
		ViewMenu.add(new JMenuItem("Height curves"));
		ViewMenu.getItem(3).addActionListener(this);
		
		bar.add(FileMenu);
		bar.add(GenerateMenu);
		bar.add(ViewMenu);
		
		Container cont = getContentPane();
		
		mapPane = new MapPane(this);
		cont.add(mapPane, BorderLayout.CENTER);
		
		mapPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		
		cont.add(bar, BorderLayout.NORTH);
		
		d = new JInternalFrame("Advansed noise generator options", true, true, true, true);
		d.setSize(200, 120);
		d.setLayout(new GridLayout(4, 2));
		
		d.add(new JLabel("Smoothness:"));
		smoothnessArea = new JTextField(smoothness+"");
		d.add(smoothnessArea);
		
		d.add(new JLabel("Max moving:"));
		maxMoveArea = new JTextField(maxMove+"");
		d.add(maxMoveArea);
		
		d.add(new JLabel("Erode factor:"));
		erodeArea = new JTextField(erodeFactor+"");
		d.add(erodeArea);
		
		saveOptions = new JButton("Save options");
		saveOptions.addActionListener(this);
		d.add(saveOptions);
		
		JButton cancel = new JButton("Close dialog");
		cancel.addActionListener(this);
		d.add(cancel);
		
		mapPane.add(d);
		
		fd = new JFileChooser();
		fd.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				if (arg0.getName().toLowerCase().endsWith(".bcc") || arg0.isDirectory()) return true;
				return false;
			}

			@Override
			public String getDescription() {
				
				return "BinaryCube (.bcc)";
			}
		});
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex){
			
		}
		
		MapViewer2 frame = new MapViewer2();
		
		frame.addWindowListener(new Closer());
		frame.init();
		frame.mapPane.repaint();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String item = e.getActionCommand().toString();
		
		if (item.toLowerCase().equals("open bcc file")) {
			try {
				int ret = fd.showOpenDialog(this);
				if (ret == JFileChooser.APPROVE_OPTION){
					String filename = fd.getSelectedFile().getName();
					CubeMap m = BCCHelper.readBCCFile(filename);
					noiseMap = m.getHeightMap();
					round = false;
					if (!isEmpty){
						//	if (msgbox("You have unsaved changes. Overwrite?", "Generate new map", MsgBox.YesNo) == MsgBox.NO) return;
					}
					mapPane.repaint();
				}
				
			} catch (Exception ex){
					
			}
		} else if (item.toLowerCase().equals("save as binarycube (bcc)")){
			fd.showSaveDialog(this);
		} else if (item.equals("Custom seed")){
			String s = (String)JOptionPane.showInputDialog(
                    this,
                    "Enter seed (must be integer)",
                    "Custom seed",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null, "2");
		
			try {
				seed = Integer.parseInt(s);
				init();
				this.repaint();
			} catch (NumberFormatException ex){
				warnbox("Only Integer is valid", "Custom seed");
			}
		} else if (item.equals("Random seed")){
			seed = randomInt();
			init();
			this.repaint();
		} else if (item.equals("Custom factor")){
			String s = (String)JOptionPane.showInputDialog(
                    this,
                    "Enter factor (must be double) BROKEN!",
                    "Custom factor",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null, "2");
		
			try {
				factor = (int) Double.parseDouble(s);
				init();
				mapPane.repaint();
			} catch (NumberFormatException ex){
				warnbox("Only Integer is valid", "Custom seed");
			}
		} else if (item.equals("Custom scale")){
			String s = (String)JOptionPane.showInputDialog(
                    this,
                    "Select scale",
                    "Custom scale",
                    JOptionPane.PLAIN_MESSAGE,
                    null, 
                    new Object[] {"5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5"},
                    "6");
		
			try {
				scale = Float.parseFloat(s);
				init();
				mapPane.repaint();
			} catch (NumberFormatException ex){
				
			}
		} else if (item.equals("Smooth")){
			round = false;
			this.repaint();
		} else if (item.equals("Height curves")){
			round = true;
			mapPane.repaint();
		}  else if (item.equals("Select noise generator")){
			String s = (String)JOptionPane.showInputDialog(
                    this,
                    "Select noise generator",
                    "Noise generator",
                    JOptionPane.PLAIN_MESSAGE,
                    null, 
                    new Object[] {"PerlinMapGenerator", "PerlinGenerator", "PerlinNoise", "PerlinNoiseC", "ImprovedNoise (Perlin)", "Noise (Fractal)"},
                    "PerlinMapGenerator");
			if (s == null || s.equals("")) return;
			if (s.equals("PerlinMapGenerator")){
				noiseGen = NoiseGenerator.PerlinMapGenerator;
			} else if (s.equals("PerlinGenerator")){
				noiseGen = NoiseGenerator.PerlinGenerator;
			} else if (s.equals("PerlinNoise")){
				noiseGen = NoiseGenerator.PerlinNoise;
			} else if (s.equals("PerlinNoiseC")){
				noiseGen = NoiseGenerator.PerlinNoiseC;
			} else if (s.equals("ImprovedNoise (Perlin)")){
				noiseGen = NoiseGenerator.ImprovedNoise;
			} else if (s.equals("Noise (Fractal)")){
				noiseGen = NoiseGenerator.Noise;
			}
			init();
			mapPane.repaint();
		} else if (item.equals("Advanced generator options")){
			d.setVisible(true);
		} else if (item.equals("Save options")){
			smoothness = Integer.parseInt(smoothnessArea.getText());
			maxMove = Integer.parseInt(maxMoveArea.getText());
			erodeFactor = Integer.parseInt(erodeArea.getText());
			init();
			mapPane.repaint();
		} else if (item.equals("Close dialog")){
			d.setVisible(false);
			smoothnessArea.setText(smoothness+"");
			maxMoveArea.setText(maxMove+"");
			erodeArea.setText(erodeFactor+"");
		} 
		
	}

	public static void msgbox(String message, String title) {
	    JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
	  }
	public MsgBox msgbox(String message, String title, MsgBox type) {
		int ret = 0;
		switch (type) {
		case YesNoCancel:
			 ret = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, 2);
			break;
		case YesNo:
			 ret = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, 1);
			break;
		default:
			break;
		}
		switch (ret) {
		case JOptionPane.NO_OPTION:
			return MsgBox.NO;
		case JOptionPane.YES_OPTION:
			return MsgBox.YES;
		case JOptionPane.CANCEL_OPTION:
			return MsgBox.CANCEL;
		default:
			return MsgBox.CANCEL;
		}
	}
	
	public static void warnbox(String message, String title) {
	    JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
	  }
	
	class OptionsCloser extends WindowAdapter {
	    public void windowClosing (WindowEvent event) {
	    	d.setVisible(false);
			smoothnessArea.setText(smoothness+"");
			maxMoveArea.setText(maxMove+"");
			erodeArea.setText(erodeFactor+"");
	    }
	}
}

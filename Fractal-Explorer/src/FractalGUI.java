import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicLookAndFeel;

import com.bulenkov.darcula.DarculaLaf;

public class FractalGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	protected GenerateMandelbrot mandelbrot;
	GenerateMandelbrot b;
	
	protected Complex fractalComplex;
	private Complex juliaComplex = new Complex();
	// protected static JRadioButton burningShip;
	protected static JComboBox<String> fractalSelect;
	protected JPanel panel;
	static int x, y, x2, y2;
	private boolean clicked = false;
	static boolean dragging = false;
	MouseMotionListener ml;
	protected JuliaSet julia = new JuliaSet(new Complex());

	protected FractalGUI(String title) {
		super(title);
		mandelbrot = new GenerateMandelbrot();
	}

	protected void setLookAndFeel() {
		BasicLookAndFeel darcula = new DarculaLaf();
		try {
			UIManager.setLookAndFeel(darcula);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

	}

	protected void init() {

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLookAndFeel();

		Container pane = this.getContentPane();
		this.setLayout(new BorderLayout());

		mandelbrot.init();

		pane.add(mandelbrot, BorderLayout.CENTER);
		pane.add(controlsPanel(), BorderLayout.EAST);
		pane.add(imagesAndJuliaPanel(), BorderLayout.WEST);

		mandelbrot.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				dragging = true;
				setStartPoint(e.getX(), e.getY());
				System.out.println(dragging);
			}

			public void mouseDragged(MouseEvent e) {

				setEndPoint(e.getX(), e.getY());
				repaint();
				System.out.println("hi");

			}

			public void mouseReleased(MouseEvent e) {
				dragging = false;
				setEndPoint(e.getX(), e.getY());
				repaint();
				System.out.println(dragging);

			}
		});

		ml = new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {}

			@Override
			public void mouseMoved(MouseEvent e) {
				Complex juliaComplex = mandelbrot.getComplexValues(e.getX(), e.getY(), mandelbrot.WIDTH,
						mandelbrot.HEIGHT);

				julia.setOutOfMandel(false);
				julia.setComplex(juliaComplex);

				repaint();

			}

		};
		
		this.setJMenuBar(menuBar());
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void setStartPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setEndPoint(int x, int y) {
		x2 = (x);
		y2 = (y);
	}

	public static void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
		int px = Math.min(x, x2);
		int py = Math.min(y, y2);
		int pw = Math.abs(x - x2);
		int ph = Math.abs(y - y2);
		g.drawRect(px, py, pw, ph);

	}

	protected JPanel controlsPanel() {

		JPanel motherPanel = new JPanel();

		JPanel fractalControls = new JPanel();
		JPanel rangeControls = new JPanel();
		JPanel colourControls = new JPanel();
		JPanel saveControls = new JPanel();

		// --- Fractal Controls --//

		TitledBorder title;
		title = BorderFactory.createTitledBorder("Fractal Controls");
		fractalControls.setBorder(title);

		JLabel iterations = new JLabel("Iterations: ");
		JLabel userPoint = new JLabel("User Selected Point: ");
		JLabel cursorPoint = new JLabel("CursorPoint: ");
		JTextField iterationsInput = new JTextField();
		iterationsInput.setPreferredSize(new Dimension(120, 20));
		JTextField userPDisplay = new JTextField();
		userPDisplay.setEditable(false);
		JTextField cursorDisplay = new JTextField();
		cursorDisplay.setEditable(false);

		fractalControls.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(5, 2, 0, 2);

		int i = 0;

		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.anchor = GridBagConstraints.LINE_START;
		fractalControls.add(iterations, gbc);

		gbc.weightx = 0.0;
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = i;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fractalControls.add(iterationsInput, gbc);
		iterationsInput.setToolTipText("Press Enter to apply changes");
		iterationsInput.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					mandelbrot.setIterations(Integer.parseInt(iterationsInput.getText()));

					repaint();
				}
			}
		});

		++i;

		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.anchor = GridBagConstraints.LINE_END;
		fractalControls.add(userPoint, gbc);

		gbc.gridx = 1;
		gbc.gridy = i;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fractalControls.add(userPDisplay, gbc);
		mandelbrot.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent ev) {

				fractalComplex = mandelbrot.getComplexValues(ev.getX(), ev.getY(), mandelbrot.WIDTH, mandelbrot.HEIGHT);

				// here there is a check to see if the imaginary part is
				// negative and appends it with a "-" sign instead on displaying
				// " + - "
				if (fractalComplex.getImaginaryPart() < 0) {
					userPDisplay.setText((double) Math.round(fractalComplex.getRealPart() * 1000d) / 1000d + " - "
							+ (double) Math.round(Math.abs(fractalComplex.getImaginaryPart()) * 1000d) / 1000d + "i");
				} else {
					userPDisplay.setText((double) Math.round(fractalComplex.getRealPart() * 1000d) / 1000d + " + "
							+ (double) Math.round(fractalComplex.getImaginaryPart() * 1000d) / 1000d + "i");
				}
			}
		});

		++i;

		gbc.gridx = 0;
		gbc.gridy = i;
		gbc.anchor = GridBagConstraints.LINE_END;
		fractalControls.add(cursorPoint, gbc);

		gbc.gridx = 1;
		gbc.gridy = i;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fractalControls.add(cursorDisplay, gbc);
		mandelbrot.addMouseMotionListener(new MouseAdapter() {

			public void mouseMoved(MouseEvent e) {

				// this retrieved the complex point at the point of the cursor
				// by getting the x,y coordinates and converting them to complex
				// values
				Complex c = mandelbrot.getComplexValues(e.getX(), e.getY(), mandelbrot.WIDTH, mandelbrot.HEIGHT);

				// here there is a check to see if the imaginary part is
				// negative and appends it with a "-" sign instead on displaying
				// " + - "
				if (c.getImaginaryPart() < 0) {
					cursorDisplay.setText((double) Math.round(c.getRealPart() * 1000d) / 1000d + " - "
							+ (double) Math.round(Math.abs(c.getImaginaryPart()) * 1000d) / 100d + "i");
				} else {
					cursorDisplay.setText((double) Math.round(c.getRealPart() * 1000d) / 1000d + " + "
							+ (double) Math.round(c.getImaginaryPart() * 1000d) / 1000d + "i");
				}
			}
		});

		// --- Range Controls --//

		TitledBorder title1;
		title1 = BorderFactory.createTitledBorder("Range Controls");
		rangeControls.setBorder(title1);

		JLabel xmin = new JLabel("xmin: ");
		JLabel xmax = new JLabel("xmax: ");
		JLabel ymin = new JLabel("ymin: ");
		JLabel ymax = new JLabel("ymax: ");
		JTextField xminInput = new JTextField();
		xminInput.setPreferredSize(new Dimension(200, 20));
		JTextField xmaxInput = new JTextField();
		JTextField yminInput = new JTextField();
		JTextField ymaxInput = new JTextField();
		JButton change = new JButton("Apply");
		change.setPreferredSize(new Dimension(90, 20));
		JButton revert = new JButton("Revert");
		revert.setPreferredSize(new Dimension(90, 20));

		rangeControls.setLayout(new GridBagLayout());
		GridBagConstraints gbc2 = new GridBagConstraints();

		gbc2.insets = new Insets(5, 2, 0, 2);

		int j = 0;

		gbc2.gridx = 0;
		gbc2.gridy = j;
		rangeControls.add(xmin, gbc2);

		gbc2.gridx = 1;
		gbc2.gridy = j;
		gbc2.weightx = 1.;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		rangeControls.add(xminInput, gbc2);

		++j;

		gbc2.gridx = 0;
		gbc2.gridy = j;
		rangeControls.add(xmax, gbc2);

		gbc2.gridx = 1;
		gbc2.gridy = j;
		gbc2.weightx = 1;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		rangeControls.add(xmaxInput, gbc2);

		++j;

		gbc2.gridx = 0;
		gbc2.gridy = j;
		rangeControls.add(ymin, gbc2);

		gbc2.gridx = 1;
		gbc2.gridy = j;
		gbc2.weightx = 1;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		rangeControls.add(yminInput, gbc2);

		++j;

		gbc2.gridx = 0;
		gbc2.gridy = j;
		rangeControls.add(ymax, gbc2);

		gbc2.gridx = 1;
		gbc2.gridy = j;
		gbc2.weightx = 1;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		rangeControls.add(ymaxInput, gbc2);

		++j;

		gbc2.fill = GridBagConstraints.HORIZONTAL;
		gbc2.weightx = 0.0;
		gbc2.gridwidth = 2;
		gbc2.gridx = 0;
		gbc2.gridy = j;
		rangeControls.add(change, gbc2);
		change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					mandelbrot.setMaxX(Double.parseDouble(xmaxInput.getText()));
					mandelbrot.setMinX(Double.parseDouble(xminInput.getText()));
					mandelbrot.setMaxY(Double.parseDouble(ymaxInput.getText()));
					mandelbrot.setMinY(Double.parseDouble(ymaxInput.getText()));

					// sets the new max point that has been inputed
					// mandelbrot.setMaxPoint(Double.parseDouble(xmaxInput.getText()),
					// Double.parseDouble(ymaxInput.getText()));
					// // sets the new min point that has been inputed
					// mandelbrot.setMinPoint(Double.parseDouble(xminInput.getText()),
					// Double.parseDouble(yminInput.getText()));

				} catch (NumberFormatException err) {
					System.err.println(err);
				}
				repaint();
			}
		});

		++j;

		gbc2.fill = GridBagConstraints.HORIZONTAL;
		gbc2.weightx = 0.0;
		gbc2.gridwidth = 2;
		gbc2.gridx = 0;
		gbc2.gridy = j;
		rangeControls.add(revert, gbc2);
		revert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					mandelbrot.setMaxX(1.5);
					mandelbrot.setMinX(-2.5);
					mandelbrot.setMaxY(1.6);
					mandelbrot.setMinY(-1.6);

					// // sets the new max point that has been inputed
					// mandelbrot.setMaxPoint(mandelbrot.DEFAULT_MAXX,
					// mandelbrot.DEFAULT_MAXY);
					// // sets the new min point that has been inputed
					// mandelbrot.setMinPoint(mandelbrot.DEFAULT_MINX,
					// mandelbrot.DEFAULT_MINY);

				} catch (NumberFormatException err) {
					System.err.println(err);
				}
				repaint();
			}
		});

		// --- Colour Controls --//

//		
//		TitledBorder title2;
//		title2 = BorderFactory.createTitledBorder("Colour Controls");
//		colourControls.setBorder(title2);
//
//		JLabel predefined = new JLabel("Predefined Selection: ");
//		JComboBox<String> setSchemes = new JComboBox<String>();
//		JLabel options = new JLabel("Options: ");
//		JCheckBox smooth = new JCheckBox("Smooth Shading");
//		JButton customise = new JButton("Customise Colour Scheme");
//		customise.setPreferredSize(new Dimension(90, 20));
//		JButton apply = new JButton("Apply");
//		apply.setPreferredSize(new Dimension(240, 20));
//		// burningShip = new JRadioButton("burn ");
//				
//		colourControls.setLayout(new GridBagLayout());
//		GridBagConstraints gbc3 = new GridBagConstraints();
//
//		gbc3.insets = new Insets(5, 2, 0, 2);
//
//		int k = 0;
//
//		gbc3.gridx = 0;
//		gbc3.gridy = k;
//		colourControls.add(predefined, gbc3);
//
//		++k;
//
//		gbc3.gridx = 0;
//		gbc3.gridy = k;
//		gbc3.weightx = 1;
//		gbc3.fill = GridBagConstraints.HORIZONTAL;
//		colourControls.add(setSchemes, gbc3);
//
//		++k;
//
//		gbc3.gridx = 0;
//		gbc3.gridy = k;
//		colourControls.add(options, gbc3);
//
//		++k;
//
//		gbc3.gridx = 0;
//		gbc3.gridy = k;
//		colourControls.add(smooth, gbc3);
//
//		++k;
//
//		// gbc3.gridx = 0;
//		// gbc3.gridy = k;
//		// colourControls.add(burningShip, gbc3);
//
//		++k;
//
//		gbc3.gridx = 0;
//		gbc3.gridy = k;
//		colourControls.add(customise, gbc3);
//
//		++k;
//
//		gbc3.gridx = 0;
//		gbc3.gridy = k;
//		colourControls.add(apply, gbc3);
		
		ColourScheme scheme = new ColourScheme(this);

		// --- Save Controls --//

		TitledBorder title3;
		title3 = BorderFactory.createTitledBorder("Iamge Size/Save Controls");
		saveControls.setBorder(title3);

		JLabel size = new JLabel("Size [width , height] : ");
		JLabel x = new JLabel(" x ");
		JTextField xSize = new JTextField(8);
		JTextField ySize = new JTextField(8);
		JButton saveImage = new JButton("Save Image");
		saveImage.setPreferredSize(new Dimension(90, 20));
		JButton applyImage = new JButton("Apply");
		applyImage.setPreferredSize(new Dimension(90, 20));
		JButton revertImage = new JButton("Revert");
		revertImage.setPreferredSize(new Dimension(90, 20));

		saveControls.setLayout(new GridBagLayout());
		GridBagConstraints gbc4 = new GridBagConstraints();

		gbc4.insets = new Insets(5, 2, 0, 2);

		int a = 0;

		gbc4.gridx = 0;
		gbc4.gridy = a;
		saveControls.add(size, gbc4);

		++a;

		gbc4.gridx = 0;
		gbc4.gridy = a;
		gbc4.gridwidth = 1;
		gbc4.anchor = GridBagConstraints.CENTER;
		gbc4.fill = GridBagConstraints.HORIZONTAL;
		saveControls.add(xSize, gbc4);

		gbc4.gridx = 1;
		gbc4.gridy = a;
		gbc4.anchor = GridBagConstraints.CENTER;
		gbc4.fill = GridBagConstraints.HORIZONTAL;
		saveControls.add(x, gbc4);

		gbc4.gridx = 2;
		gbc4.gridy = a;
		gbc4.gridwidth = 1;
		gbc4.anchor = GridBagConstraints.CENTER;
		gbc4.fill = GridBagConstraints.HORIZONTAL;
		saveControls.add(ySize, gbc4);

		++a;

		gbc4.fill = GridBagConstraints.HORIZONTAL;
		gbc4.weightx = 0.0;
		gbc4.gridwidth = 3;
		gbc4.gridx = 0;
		gbc4.gridy = a;
		saveControls.add(applyImage, gbc4);
		applyImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					// sets the new max point that has been inputed
					mandelbrot.setHeight(Integer.parseInt(ySize.getText()));
					// sets the new min point that has been inputed
					mandelbrot.setWidth(Integer.parseInt(xSize.getText()));

				} catch (NumberFormatException err) {
					System.err.println(err);
				}
				repaint();
			}
		});

		++a;

		gbc4.fill = GridBagConstraints.HORIZONTAL;
		gbc4.weightx = 0.0;
		gbc4.gridwidth = 3;
		gbc4.gridx = 0;
		gbc4.gridy = a;
		saveControls.add(revertImage, gbc4);
		revertImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					// sets the new max point that has been inputed

					mandelbrot.setHeight(750);
					// sets the new min point that has been inputed
					mandelbrot.setWidth(1000);

				} catch (NumberFormatException err) {
					System.err.println(err);
				}
				repaint();
			}
		});

		++a;

		gbc4.fill = GridBagConstraints.HORIZONTAL;
		gbc4.weightx = 0.0;
		gbc4.gridwidth = 3;
		gbc4.gridx = 0;
		gbc4.gridy = a;
		saveControls.add(saveImage, gbc4);
		saveImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					Robot robot = new Robot();
					String format = "jpg";

					JOptionPane.showMessageDialog(null, "Please add .jpeg to the file name!");

					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

					String fileName = save(fileChooser);

					Point p = mandelbrot.getLocationOnScreen();

					Rectangle captureRect = new Rectangle(p.x, p.y, mandelbrot.getWidth(), mandelbrot.getHeight());

					BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
					ImageIO.write(screenFullImage, format, new File(fileName));

					fileChooser.setDialogTitle("Specify a file to save");

					JOptionPane.showMessageDialog(null, "Screenshot saved!\nFind it in project directory");
				} catch (AWTException | IOException | NullPointerException ex) {
					System.err.println(ex);
				}
			}
		});

		// --- Fractal Choices ---//

		JPanel fractalChoices = new JPanel();

		TitledBorder title4;
		title4 = BorderFactory.createTitledBorder("Fractal");
		fractalChoices.setBorder(title4);

		JLabel displayed = new JLabel("Currently Viewing:");
		fractalSelect = new JComboBox<String>();
		fractalSelect.setPreferredSize(new Dimension(240, 25));
		fractalSelect.addItem("Mandelbrot Set");
		fractalSelect.addItem("Burning Ship");
		fractalSelect.setSelectedItem("Mandelbrot Set");
		JCheckBox liveJulia = new JCheckBox("Live Julia Set");


		fractalChoices.setLayout(new GridBagLayout());
		GridBagConstraints gcb5 = new GridBagConstraints();

		gcb5.insets = new Insets(5, 2, 0, 2);

		int b = 0;

		gcb5.gridx = 0;
		gcb5.gridy = b;
		gcb5.anchor = GridBagConstraints.LINE_START;
		fractalChoices.add(displayed, gcb5);

		++b;

		gcb5.gridx = 0;
		gcb5.gridy = b;
		gcb5.weightx = 1;
		gcb5.fill = GridBagConstraints.HORIZONTAL;
		fractalChoices.add(fractalSelect, gcb5);

		++b;

		gcb5.gridx = 0;
		gcb5.gridy = b;
		fractalChoices.add(liveJulia, gcb5);
		liveJulia.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (liveJulia.isSelected()) {

					mandelbrot.addMouseMotionListener(ml);

				} else {

					mandelbrot.removeMouseMotionListener(ml);

				}
			}

		});

		// ---Putting everything together ---//

		motherPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(3, 0, 0, 0);

		int m = 0;

		c.gridx = 0;
		c.gridy = m;
		motherPanel.add(fractalControls, c);

		++m;

		c.gridx = 0;
		c.gridy = m;
		motherPanel.add(rangeControls, c);

		++m;

		c.gridx = 0;
		c.gridy = m;
		motherPanel.add(saveControls, c);

		++m;

		c.gridx = 0;
		c.gridy = m;
		motherPanel.add(scheme.init(), c);

		++m;

		c.gridx = 0;
		c.gridy = m;
		motherPanel.add(fractalChoices, c);

		return motherPanel;

	}

	protected JPanel imagesAndJuliaPanel() {

		JPanel imagesAndJulia = new JPanel();

		JPanel juliaView = new JPanel();
		JPanel images = new JPanel();
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Julia Set");
		juliaView.setBorder(title);
		juliaView.add(julia);

		// --- Images Saved List ---//

		images.setLayout(new BorderLayout());

		JButton addB = new JButton("Add");
		JButton removeB = new JButton("Remove");

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 2));
		buttons.add(addB);
		buttons.add(removeB);

		DefaultListModel model = new DefaultListModel();
		JList<DefaultListModel> list1 = new JList<DefaultListModel>(model);
		JScrollPane scrollPane = new JScrollPane(list1);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(200, 300));

		TitledBorder title4;
		title4 = BorderFactory.createTitledBorder("Image Favourites");
		scrollPane.setBorder(title4);

		images.add(scrollPane, BorderLayout.CENTER);

		images.add(buttons, BorderLayout.SOUTH);

		addB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Robot robot;

				try {
					robot = new Robot();

					Point p = mandelbrot.getLocationOnScreen();

					Rectangle captureRect = new Rectangle(p.x, p.y, mandelbrot.getWidth(), mandelbrot.getHeight());

					BufferedImage screenFullImage = robot.createScreenCapture(captureRect);

					int w = (int) (screenFullImage.getWidth() * 0.22);
					int h = (int) (screenFullImage.getHeight() * 0.22);

					BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
					AffineTransform at = new AffineTransform();
					at.scale(0.21, 0.21);
					AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
					after = scaleOp.filter(screenFullImage, after);
					ImageIcon icon = new ImageIcon(after);

					model.addElement(icon);
					System.out.println(icon.getIconHeight());

				} catch (AWTException e) {
					e.printStackTrace();
				}

			}

		});

		removeB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.removeElement(list1.getSelectedIndex());
			}

		});

		// --- Favourites Controls --//

		JPanel favouritesControls = new JPanel();

		TitledBorder title5;
		title5 = BorderFactory.createTitledBorder("Favourites");
		favouritesControls.setBorder(title5);

		DefaultListModel dlModel = new DefaultListModel();
		JList<DefaultListModel> list = new JList<DefaultListModel>(dlModel);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(200, 100));
		JButton add = new JButton("Add");
		add.setPreferredSize(new Dimension(100, 20));
		JButton remove = new JButton("Remove");
		remove.setPreferredSize(new Dimension(100, 20));
		JPanel buttonPanel = new JPanel();

		favouritesControls.setLayout(new BorderLayout());

		add.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {

				dlModel.addElement("Point: " + (double) Math.round(fractalComplex.getRealPart() * 1000d) / 1000d + " , "
						+ (double) Math.round(fractalComplex.getImaginaryPart() * 1000d) / 1000d + "i");

			}
		});

		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlModel.removeElementAt(list.getSelectedIndex());
			}
		});

		buttonPanel.add(add);
		buttonPanel.add(remove);

		favouritesControls.add(scroll, BorderLayout.CENTER);
		favouritesControls.add(buttonPanel, BorderLayout.SOUTH);

		// --- Putting everything together ---//

		imagesAndJulia.setLayout(new BorderLayout());

		imagesAndJulia.add(juliaView, BorderLayout.NORTH);
		imagesAndJulia.add(images, BorderLayout.CENTER);
		imagesAndJulia.add(favouritesControls, BorderLayout.SOUTH);

		return imagesAndJulia;
	}

	protected JMenuBar menuBar() {

		JMenuBar menuBar = new JMenuBar();

		JMenu options = new JMenu("Options");
		JMenu help = new JMenu("Help");

		menuBar.add(options);
		menuBar.add(help);

		return menuBar;
	}

	private String save(JFileChooser f) {
		int retVal = f.showSaveDialog(null);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = f.getSelectedFile();
			return file.getAbsolutePath();
		}
		return null;
	}

}

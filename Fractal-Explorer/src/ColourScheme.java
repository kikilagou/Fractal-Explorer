import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class ColourScheme extends JPanel {

	FractalGUI gui;
	// To hold preset schemes
		private JComboBox<String> setSchemes;

	protected ColourScheme(FractalGUI gui) {
		this.gui = gui;
	}
	
	protected JPanel init() {

		JPanel colourScheme = new JPanel();
		
		TitledBorder title2;
		title2 = BorderFactory.createTitledBorder("Colour Controls");
		colourScheme.setBorder(title2);

		JLabel predefined = new JLabel("Predefined Selection: ");
		setSchemes = new JComboBox<String>();
		JLabel options = new JLabel("Options: ");
		JCheckBox smooth = new JCheckBox("Smooth Shading");
		JButton customise = new JButton("Customise Colour Scheme");
		customise.setPreferredSize(new Dimension(90, 20));
		JButton apply = new JButton("Apply");
		apply.setPreferredSize(new Dimension(240, 20));
		// burningShip = new JRadioButton("burn ");
				
		colourScheme.setLayout(new GridBagLayout());
		GridBagConstraints gbc3 = new GridBagConstraints();

		gbc3.insets = new Insets(5, 2, 0, 2);

		int k = 0;

		gbc3.gridx = 0;
		gbc3.gridy = k;
		colourScheme.add(predefined, gbc3);

		++k;

		addPresetColours();
		gbc3.gridx = 0;
		gbc3.gridy = k;
		gbc3.weightx = 1;
		gbc3.fill = GridBagConstraints.HORIZONTAL;
		colourScheme.add(setSchemes, gbc3);

		++k;

		gbc3.gridx = 0;
		gbc3.gridy = k;
		colourScheme.add(options, gbc3);

		++k;

		gbc3.gridx = 0;
		gbc3.gridy = k;
		colourScheme.add(smooth, gbc3);

		++k;

		// gbc3.gridx = 0;
		// gbc3.gridy = k;
		// colourControls.add(burningShip, gbc3);

		++k;

		gbc3.gridx = 0;
		gbc3.gridy = k;
		colourScheme.add(customise, gbc3);

		++k;

		gbc3.gridx = 0;
		gbc3.gridy = k;
		colourScheme.add(apply, gbc3);
		apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				setColours();
				gui.mandelbrot.repaint();
				
			}

		});
		
		return colourScheme;
	}

	// Method to define the colours for the preset colour scheme
	protected void addPresetColours() {

		// There are 4 preset schemes to choose from
		String select = "Select Colour Scheme";
		// Sets the colours to the defaults
		String original = "Original";
		// Sets colours to grey hues
		String greyscale = "Greyscale";
		// Sets colours to suitable ones for the colour blind
		String colourblind = "Colour Blind";
		// Sets colours to the opposite of the defaults
		String negative = "Negative";

		// Adding to the combo box
		setSchemes.addItem(select);
		setSchemes.addItem(original);
		setSchemes.addItem(greyscale);
		setSchemes.addItem(colourblind);
		setSchemes.addItem(negative);
	}
	
	// Method to set the colours for all the components of the view
		protected void setColours() {
			
			// Checks if the selected option is "Original" and sets the original
					// colours for all possible views
			if (setSchemes.getSelectedItem().equals("Original")) {
				GenerateMandelbrot mandel = gui.mandelbrot;
				mandel.colour = Color.HSBtoRGB((float) (0.5 * mandel.smoothColour), 0.9f, 0.9f);
			}

			// Checks if the selected option is "Greyscale" and sets the colours for
					// all possible views
			if (setSchemes.getSelectedItem().equals("Greyscale")) {
				GenerateMandelbrot mandel = gui.mandelbrot;
				mandel.colour = Color.HSBtoRGB((float) (0.1 * mandel.smoothColour), 0.1f, 0.1f);
			}

			// Checks if the selected option is "Negative" and sets the colours for
					// all possible views
			if (setSchemes.getSelectedItem().equals("Negative")) {

			}

			// Checks if the selected option is "Colour Blind" and sets the colours
					// for all possible views
			if (setSchemes.getSelectedItem().equals("Colour Blind")) {

			}
		}
}

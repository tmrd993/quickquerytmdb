package tmdbwrapper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class QuickMovieSearch {
    private JTextArea pictureInfoTextArea;

    public void initGui() {
	JFrame mainFrame = new JFrame();
	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	mainFrame.setTitle("Quick Movie Search");

	JLabel poster = new JLabel();

	Box box = Box.createHorizontalBox();
	JTextField searchField = new JTextField(25);
	JButton searchButton = new JButton("Search");
	JRadioButton movieButton = new JRadioButton("Movie");
	JRadioButton tvButton = new JRadioButton("TV-show");

	ButtonGroup group = new ButtonGroup();
	group.add(movieButton);
	group.add(tvButton);
	movieButton.setSelected(true);

	box.add(searchField);
	searchField.setAlignmentX(Component.LEFT_ALIGNMENT);
	searchField.setAlignmentY(Component.LEFT_ALIGNMENT);
	box.add(movieButton);
	box.add(tvButton);
	movieButton.setAlignmentX(Component.LEFT_ALIGNMENT);
	movieButton.setAlignmentY(Component.LEFT_ALIGNMENT);
	tvButton.setAlignmentX(Component.LEFT_ALIGNMENT);
	tvButton.setAlignmentY(Component.LEFT_ALIGNMENT);
	box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	mainFrame.add(box, BorderLayout.NORTH);

	box = Box.createVerticalBox();
	box.add(searchButton);
	searchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
	searchButton.setAlignmentY(Component.LEFT_ALIGNMENT);
	box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	mainFrame.add(box, BorderLayout.CENTER);

	box = Box.createHorizontalBox();
	pictureInfoTextArea = new JTextArea(12, 50);
	pictureInfoTextArea.setFont(new Font("Serif", Font.PLAIN, 20));
	pictureInfoTextArea.setEditable(false);
	box.add(pictureInfoTextArea);
	pictureInfoTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
	pictureInfoTextArea.setAlignmentY(Component.LEFT_ALIGNMENT);
	box.add(Box.createHorizontalStrut(5));
	box.add(poster);
	poster.setAlignmentX(Component.LEFT_ALIGNMENT);
	poster.setAlignmentY(Component.LEFT_ALIGNMENT);
	box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	mainFrame.add(box, BorderLayout.SOUTH);

	searchButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		BufferedImage picturePoster = null;
		if (movieButton.isSelected()) {
		    Movie movie = new MovieDataRetriever(searchField.getText()).fetchData();
		    picturePoster = movie.getPoster();

		    pictureInfoTextArea.setText(movie.toString());
		} else {
		    TvShow tvShow = new TvShowDataRetriever(searchField.getText()).fetchData();
		    picturePoster = tvShow.getPoster();
		    pictureInfoTextArea.setText(tvShow.toString());
		}

		if (poster != null) {
		    if (((ImageIcon) poster.getIcon()) != null) {
			((ImageIcon) poster.getIcon()).getImage().flush();
		    }
		    poster.setIcon(new ImageIcon(picturePoster));
		}
	    }
	});
	mainFrame.pack();
	mainFrame.setVisible(true);
    }

    // Unit test
    public static void main(String[] args) {
	java.awt.EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		new QuickMovieSearch().initGui();
	    }
	});
    }
}
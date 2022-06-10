// This class is main class which have main method and generate JFrame.

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;
import javax.swing.SwingWorker;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.UIManager;
import java.awt.Component;
import javax.swing.SwingConstants;

public class ExpressWayView extends JFrame {

	private JPanel viewPane; // the biggest based panel
	private JComboBox selectUpDown; // the combobox for selecting southbound or northboud lane
	// This two label is used to show image for the main theme of this program "Highway Condition"
	private JLabel mainIconLabel; 
	private JLabel titleIconLabel;
	private ExpresswayCrawling crawling; // the object of ExpresswayCrawling
	private List<ExpressWay> wayList; // for store the return object of the method getexpressList of the object crawling
	private ViewNameWorker worker; // Swingworker to show the name list of each expressway on namePanel
	private JPanel namePanel; // to display the list of the name of each expressway
	private JList nameList; // for store the list of the name of each expressway
	private JLabel selectDir;
	private JProgressBar progressBar; // for show how the work did of crawling and displaying
	private JPanel detailPanel; // for displaying the speed, distance, junciton name, and time
	private JLabel junctionLabel;
	private JLabel distanceLabel;
	private JLabel speedLabel;
	private JLabel timeLabel;
	private JTextField junctionTxt;
	private JTextField distanceTxt;
	private JTextField speedTxt;
	private JTextField timeTxt;
	private JLabel selectOne;
	private JButton addBtn; // if user click this button, the expressway is selected and display on resultPanel
	private JPanel resultPanel; // for displaying the list user selected
	private JScrollPane resultScroll;
	private JList resultList; // user selected list
	private DefaultListModel resultStr; // for storing the String value of user seleted express' conjunction name
	private float totalDistance; // the total distance of user selected distance
	private float totalTime; // the total time of user selected time
	private int planNum; // the number of expressway user selected
	private int score; // the average score of the plan user made
	private JLabel selectedLabel;
	private JLabel totalDistanceLabel;
	private JLabel totalTimeLabel;
	private JTextField totalDistanceTxt;
	private JTextField totalTimeTxt;
	private JLabel scoreLabel;
	private JTextField scoreTxt;
	private JButton initializeButton; // initialize total distance, total time and score by 0.
	private JButton fileBtn; // make file to store the plan on "Plan.txt"

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExpressWayView frame = new ExpressWayView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ExpressWayView() {
		totalDistance = 0;
		totalTime = 0;
		planNum = 0;
		score = 0;
		setTitle("Real-Time ExpressWay");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 685, 645);
		viewPane = new JPanel();
		viewPane.setBackground(Color.WHITE);
		viewPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(viewPane);
		
		selectUpDown = new JComboBox();
		selectUpDown.setBounds(140, 5, 162, 32);
		selectUpDown.setModel(new DefaultComboBoxModel(new String[] {"None", "Northbound Lane", "Southbound Lane"}));
		selectUpDown.addActionListener(new ActionListener() { // when user select one on the combobox
			public void actionPerformed(ActionEvent e) {
				// setting the progress bar
				progressBar.setString(0 + "%");
				progressBar.setValue(0);
				progressBar.setStringPainted(true);
				worker = new ViewNameWorker();
				if(selectUpDown.getSelectedItem().equals("None")) { // if user select the None element
					String[] str = {""};
					nameList.setListData(str); // display nothing
					detailInitialize(); // make list empty and add button unable.
				} else {
					worker.execute(); // if user select northbound land or southbound lane, the worker run. and display the name of expressway of each direction.
				}
			}
		});
		viewPane.setLayout(null);
		viewPane.add(selectUpDown);
		
		selectDir = new JLabel("Select Direction: ");
		selectDir.setBounds(29, 12, 113, 16);
		viewPane.add(selectDir);
		
		selectOne = new JLabel("<Select One>");
		selectOne.setBounds(29, 29, 113, 16);
		viewPane.add(selectOne);
		
		mainIconLabel = new JLabel("");
		mainIconLabel.setBounds(430, 133, 214, 74);
		mainIconLabel.setIcon(new ImageIcon(ExpressWayView.class.getResource("/images/icon.jpeg")));
		viewPane.add(mainIconLabel);
		
		titleIconLabel = new JLabel("");
		titleIconLabel.setBounds(406, 0, 273, 177);
		titleIconLabel.setIcon(new ImageIcon(ExpressWayView.class.getResource("/images/main.png")));
		viewPane.add(titleIconLabel);
		
		namePanel = new JPanel();
		namePanel.setBounds(29, 49, 273, 270);
		viewPane.add(namePanel);
		
		nameList = new JList<String>();
		nameList.addMouseListener(new MouseAdapter() { // when user select a name of an expressway by mouse click, the detail of the express way will display on detailPanel
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!selectUpDown.getSelectedItem().equals("None")){ // when the selected combobox is not "None"
					ViewDetailWorker worker = new ViewDetailWorker(); 
					worker.execute(); // the worker run and bring detail information
					addBtn.setEnabled(true); // then make addButton enable.
				}
			}
		});
		nameList.setVisibleRowCount(16);
		namePanel.add(new JScrollPane(nameList));
		
		progressBar = new JProgressBar();
		progressBar.setOpaque(true);
		progressBar.setBackground(Color.LIGHT_GRAY);
		progressBar.setString(0 + "%");
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setBounds(29, 320, 273, 20);
		viewPane.add(progressBar);
		
		detailPanel = new JPanel();
		detailPanel.setBounds(29, 352, 471, 212);
		viewPane.add(detailPanel);
		detailPanel.setLayout(null);
		
		junctionLabel = new JLabel("Juntion Name : ");
		junctionLabel.setBounds(6, 6, 103, 40);
		detailPanel.add(junctionLabel);
		
		distanceLabel = new JLabel("Distance :");
		distanceLabel.setBounds(6, 58, 85, 40);
		detailPanel.add(distanceLabel);
		
		speedLabel = new JLabel("Speed : ");
		speedLabel.setBounds(6, 110, 85, 40);
		detailPanel.add(speedLabel);
		
		timeLabel = new JLabel("The Time required : ");
		timeLabel.setBounds(6, 162, 126, 40);
		detailPanel.add(timeLabel);
		
		junctionTxt = new JTextField();
		junctionTxt.setEditable(false);
		junctionTxt.setBounds(160, 13, 200, 26);
		detailPanel.add(junctionTxt);
		junctionTxt.setColumns(10);
		
		distanceTxt = new JTextField();
		distanceTxt.setEditable(false);
		distanceTxt.setColumns(10);
		distanceTxt.setBounds(160, 65, 130, 26);
		detailPanel.add(distanceTxt);
		
		speedTxt = new JTextField();
		speedTxt.setEditable(false);
		speedTxt.setColumns(10);
		speedTxt.setBounds(160, 117, 130, 26);
		detailPanel.add(speedTxt);
		
		timeTxt = new JTextField();
		timeTxt.setEditable(false);
		timeTxt.setColumns(10);
		timeTxt.setBounds(160, 169, 200, 26);
		detailPanel.add(timeTxt);
		
		addBtn = new JButton("ADD");
		addBtn.addActionListener(new ActionListener() { // when user push the add button
			public void actionPerformed(ActionEvent e) {
				addBtn.setEnabled(false); // the button become unable again
				ResultWorker worker = new ResultWorker(); 
				worker.execute(); // the worker run and the selected express way enter the list of the selected list and display total distance, time and score.
			}
		});
		addBtn.setOpaque(true);
		addBtn.setEnabled(false);
		addBtn.setBorder(UIManager.getBorder("Button.border"));
		addBtn.setBounds(335, 40, 130, 130);
		addBtn.setBackground(Color.WHITE);
		detailPanel.add(addBtn);
		addBtn.setFont(new Font("Nanum Gothic", Font.BOLD | Font.ITALIC, 36));
		
		resultPanel = new JPanel();
		resultPanel.setBounds(502, 219, 177, 345);
		viewPane.add(resultPanel);
		resultPanel.setLayout(null);
		
		resultScroll = new JScrollPane((Component) null);
		resultScroll.setBounds(6, 45, 165, 127);
		resultPanel.add(resultScroll);
		
		resultList = new JList<String>();
		resultStr = new DefaultListModel();
		resultList.setVisibleRowCount(16);
		resultScroll.setViewportView(resultList);
		
		selectedLabel = new JLabel("<Selected>");
		selectedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		selectedLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		selectedLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 15));
		selectedLabel.setBackground(Color.WHITE);
		selectedLabel.setBounds(52, 6, 73, 35);
		resultPanel.add(selectedLabel);
		
		totalDistanceLabel = new JLabel("Total Distance");
		totalDistanceLabel.setBounds(6, 184, 119, 16);
		resultPanel.add(totalDistanceLabel);
		
		totalTimeLabel = new JLabel("Total Time Required");
		totalTimeLabel.setBounds(6, 227, 131, 16);
		resultPanel.add(totalTimeLabel);
		
		totalDistanceTxt = new JTextField();
		totalDistanceTxt.setEditable(false);
		totalDistanceTxt.setText("0");
		totalDistanceTxt.setBounds(6, 200, 165, 26);
		resultPanel.add(totalDistanceTxt);
		totalDistanceTxt.setColumns(10);
		
		totalTimeTxt = new JTextField();
		totalTimeTxt.setEditable(false);
		totalTimeTxt.setText("0");
		totalTimeTxt.setColumns(10);
		totalTimeTxt.setBounds(6, 244, 165, 26);
		resultPanel.add(totalTimeTxt);
		
		scoreLabel = new JLabel("Plan Score");
		scoreLabel.setBounds(6, 269, 131, 16);
		resultPanel.add(scoreLabel);
		
		scoreTxt = new JTextField();
		scoreTxt.setText("0");
		scoreTxt.setEditable(false);
		scoreTxt.setColumns(10);
		scoreTxt.setBounds(6, 282, 165, 26);
		resultPanel.add(scoreTxt);
		
		initializeButton = new JButton("Initialize");
		initializeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // when user push initialize button
				// the detailPanel will be initialized by clearing and add button will be unable
				// average score, Total distacne, time become 0 and display on the panel.
				detailInitialize();
				totalDistance = 0;
				totalTime = 0;
				score = 0;
				
				totalDistanceTxt.setText("0");
				totalTimeTxt.setText("0");
				scoreTxt.setText("0");
				
				// the user selected list will be cleared to empty
				resultStr.clear();
				resultList.setModel(resultStr);
			}
		});
		initializeButton.setFont(new Font("Nanum Gothic", Font.PLAIN, 15));
		initializeButton.setBounds(30, 310, 117, 29);
		resultPanel.add(initializeButton);
		
		fileBtn = new JButton("Make File");
		fileBtn.addActionListener(new ActionListener() { // when user push make file button
			public void actionPerformed(ActionEvent e) {
				FileWorker worker = new FileWorker();
				worker.execute(); // the worker run, and "Plan.txt" file will be generated.
			}
		});
		fileBtn.setFont(new Font("Nanum Gothic", Font.BOLD, 15));
		fileBtn.setBounds(535, 576, 117, 29);
		viewPane.add(fileBtn);
	}
	
	public void detailInitialize() { // this method initialize the components of detailPanel
		addBtn.setEnabled(false);
		addBtn.setBackground(Color.WHITE);
		junctionTxt.setText("");
		distanceTxt.setText("");
		speedTxt.setText("");
		timeTxt.setText("");
	}
	
	class ViewNameWorker extends SwingWorker<String[], Integer>{ // when user select one from combobox
		@Override
		protected String[] doInBackground() throws Exception {
			String type = null;
			if(selectUpDown.getSelectedItem().equals("Northbound Lane")) {
				type = "up";
			} else if(selectUpDown.getSelectedItem().equals("Southbound Lane")) {
				type = "dn";
			}
			wayList = crawling.getexpressList(type); // crawled from naver
			String[] publishArr = new String[wayList.size()];
			for(int i = 0; i < wayList.size(); i++) {
				publishArr[i] = wayList.get(i).getName(); // the name of expressway is stored on a String array
				publish(i);
			}
			return publishArr;
		}
		
		protected void process(List<Integer> chunks) { // this method show the progress of displaying
			int number = chunks.get(chunks.size()-1);
			number = (number*100) / (wayList.size()-1);
			progressBar.setString(number + "%");
			progressBar.setValue(number);
			progressBar.setStringPainted(true);
		}
		
		protected void done() {
			try {
				nameList.setListData(get()); // the name list will be displayed on nameList.
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class ViewDetailWorker extends SwingWorker<ExpressWay, Integer>{ // when user select one from nameList by mouse click
		@Override
		protected ExpressWay doInBackground() throws Exception {
			String selected = (String) nameList.getSelectedValue();
			ExpressWay ret = new ExpressWay();
			int speed;
			for(int i = 0; i < wayList.size(); i++) {
				if(selected.equals(wayList.get(i).getName())) { // From crawled data, this loop find the matching data to selected String.
					speed = Integer.parseInt(wayList.get(i).getSpeed().replaceAll("[^0-9]", "")); // transfrom speed from String to Integer
					publish(speed); // pass to process method
					
					// polymorphism
					if(speed >= 80) {
						ret = new ExpressClear(wayList.get(i)); // when speed is fast, make new ExpressClear object by polymorphism
					} else if(speed >= 40) {
						ret = new ExpressSlow(wayList.get(i)); // when speed is slow, make new ExpressSlow object by polymorphism
					} else {
						ret = new ExpressDelay(wayList.get(i)); // when speed is near 0, make new ExpressDelay object by polymorphism
					}
				}
			}
			return ret; // pass to done method
		}
		
		protected void process(List<Integer> chunks) {
			int speed = chunks.get(chunks.size()-1);
			if(speed >= 80) {
				addBtn.setBackground(Color.GREEN); // make the border of add button GREEN when speed is fast
			} else if(speed >= 40) {
				addBtn.setBackground(Color.YELLOW); // make the border of add button YELLOW when speed is slow
			} else if(speed >= 0) {
				addBtn.setBackground(Color.RED); // make the border of add button RED when speed is too slow
			}
		}
		
		protected void done() {
			try {
				ExpressWay get = get();
				// display the detail information of the expressway on each TextField
				junctionTxt.setText(get.getSubname());
				distanceTxt.setText(get.getDistance());
				speedTxt.setText(get.getSpeed());
				timeTxt.setText(get.getTime());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class ResultWorker extends SwingWorker<ExpressWay, Float>{ // when user push add button
		@Override
		protected ExpressWay doInBackground() throws Exception {
			ExpressWay ret = new ExpressWay();
			float distance = 0;
			float time = 0;
			int speed = Integer.parseInt(speedTxt.getText().replaceAll("[^0-9]", ""));
			
			try {
				for(int i = 0; i < resultStr.size(); i++) { // when user select same expressway again, this is not allowed!
					if(junctionTxt.getText().equals(resultStr.get(i)))
						throw new OverlapException();
				}
			} catch(OverlapException overlapE) {
				JOptionPane.showMessageDialog(null, overlapE.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE); // show message
			}
			
			// By polymorphism, each object is made.
			if(speed >= 80) {
				ret = new ExpressClear((String) nameList.getSelectedValue(), junctionTxt.getText(), distanceTxt.getText(), speedTxt.getText(), timeTxt.getText());
			} else if(speed >= 40) {
				ret = new ExpressSlow((String) nameList.getSelectedValue(), junctionTxt.getText(), distanceTxt.getText(), speedTxt.getText(), timeTxt.getText());
			} else {
				ret = new ExpressDelay((String) nameList.getSelectedValue(), junctionTxt.getText(), distanceTxt.getText(), speedTxt.getText(), timeTxt.getText());
			}
						
			distance = Float.parseFloat(distanceTxt.getText().substring(0, distanceTxt.getText().lastIndexOf("K"))); // distance is transformed from String to Float
			time = (distance / speed) * 60; // time is transformed from String to Float by calculating distance and speed 
			//and conversion to be minutes.
			
			//System.out.println(distance + " " + time);
			
			// the transformed distance and time pass to process and will added to total distance and total time
			publish(distance);
			publish(time);
			
			return ret; // pass the ExpressWay object to done method.
		}
		
		protected void process(List<Float> chunks) {
			try {
				for(int i = 0; i < resultStr.size(); i++) {
					if(junctionTxt.getText().equals(resultStr.get(i)))
						throw new OverlapException();
				}
				
				totalDistance += chunks.get(chunks.size()-2); // total distance is calculated
				totalTime  += chunks.get(chunks.size()-1); // total time is calculated
				
				// display total distance and total time on the Text Field
				totalDistanceTxt.setText(String.format("%.1fKm", totalDistance));
				totalTimeTxt.setText((int)(totalTime/60) + "hours " + (int)(totalTime%60) + "minutes");
			} catch(OverlapException overlapE) {
			}
		}
		
		protected void done() {
			try {
				ExpressWay get = get();
				
				for(int i = 0; i < resultStr.size(); i++) {
					if(get.getSubname().equals(resultStr.get(i)))
						throw new OverlapException();
				}
				
				// diplay the selected express way on resultList
				resultStr.addElement(junctionTxt.getText());
				resultList.setModel(resultStr);
				
				// calculate the score by calling the method in each class(ExpressClear, ExpressSlow, ExpressDelay) by polymorphism
				score = score * planNum;
				score += get.score();
				// calculate the average score of the whole selected expressway
				score = score / (++planNum);
				
				// Then display the average score on the TextField
				scoreTxt.setText(score+"");
			} catch(OverlapException overlapE) {
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class FileWorker extends SwingWorker<ExpressWay, Float>{ // when user push "Make File" Button
		@Override
		protected ExpressWay doInBackground() throws Exception {
			PrintWriter file = new PrintWriter(new FileOutputStream("Plan.txt", true));
			file.printf("\n<Plan>\n");
			for(int i = 0; i < resultStr.getSize(); i++) {
				file.println(resultStr.elementAt(i));
				file.println("");
			}
			file.printf("Total Distance : %s\n", totalDistanceTxt.getText());
			file.printf("Total Time Required : %s\n", totalTimeTxt.getText());
			
			file.close();
			// the "Plan.txt" will be generated and the file is appended by previous plan.
			// the file have information about the name of junction, total distance, and total time.
			return null;
		}
		
		protected void done() {
			
		}
	}
}

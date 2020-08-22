
// COVID TRACKER
/*
Brief:
	A covid tracker app to track the number of cases within different sections of society.
	All affected persons records are displayed with expected recovery date.
	A summary of section of society is also shown presenting number of active and recovered cases.
Uses:
	It takes a date and sections of scoiety for which to display the record.	
Creidt:
	Solely Built by Abhimanyu Gupta(2019226) for Refresher Module CSE201- Advanced Programming at IIIT Delhi.
*/

import java.util.ArrayList;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.*; 
import java.awt.event.*; 

class Patient
{
	private String name;
	private int age;
	private char tower;
	private LocalDate dateOfReporting;

	public Patient()
	{
		this.name="";
		this.age=-1;
		this.tower='-';
		this.dateOfReporting=LocalDate.MIN;
	}
	public Patient(String name,int age,char tower,String date)
	{
		this.name=name;
		this.age=age;
		this.tower=tower;
		this.dateOfReporting=LocalDate.parse(date,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	@Override
	public String toString()
	{
		return this.name+" | "+this.age+" | "+this.tower+" | "+this.get_dateOfReporting_in_local_format();
	}

	public void display_patient()
	{
		System.out.println();
		System.out.println("----------------------Patient Record----------------------");
		System.out.println("Name: "+this.name);
		System.out.println("Age: "+this.age);
		System.out.println("Tower: "+this.tower);
		System.out.println("Date of Reporting: "+this.get_dateOfReporting_in_local_format());
		System.out.println("----------------------------------------------------------");
		System.out.println();

		return;
	}

	public String get_name()
	{
		return this.name;
	}
	public int get_age()
	{
		return this.age;
	}
	public char get_tower()
	{
		return this.tower;
	}
	public LocalDate get_dateOfReporting()
	{
		return this.dateOfReporting;
	}
	public String get_dateOfReporting_in_local_format()
	{
		return this.dateOfReporting.getDayOfMonth()+"/"+this.dateOfReporting.getMonthValue()+"/"+this.dateOfReporting.getYear();
	}
}

class CovidTracker extends JFrame implements ActionListener
{
	private static HashMap<Character,ArrayList<Patient>> patients;
	private static String title;
	private static int recoveryDays=22;

	private static Container ctPane;
	private static JLabel ctTitle;
	private static JLabel date;
	private static JComboBox<String> day;
	private static JComboBox<String> month;
	private static JComboBox<String> year;
	private static JLabel tower;
	private static JCheckBox option1;
	private static JCheckBox option2;
	private static JCheckBox option3;
	private static JCheckBox option4;
	private static JButton submit;
	private static JLabel head1;
	private static JTextArea records;
	private static JScrollPane recordsScroll;
	private static JLabel head2;
	private static JTextArea brief;
	private static JScrollPane briefScroll;

	private static String[] days=
	{ "Day","1", "2", "3", "4", "5", 
      "6", "7", "8", "9", "10", 
      "11", "12", "13", "14", "15", 
      "16", "17", "18", "19", "20", 
      "21", "22", "23", "24", "25", 
      "26", "27", "28", "29", "30", 
      "31"
  	}; 
    private static String[] months={"Month","Apr","May","Jun","Jul","Aug"};
    private static String[] years={"Year","2020"}; 

	public static ArrayList<Patient> readRecords_1(String fileName) throws IOException
	{
		BufferedReader br=new BufferedReader(new FileReader(fileName));

		ArrayList<Patient> patients=new ArrayList<Patient>();

		String line;
		line=br.readLine();
		while(line!=null)
		{
			String[] fields=line.split("\\s");
			patients.add(new Patient(fields[0],Integer.parseInt(fields[1]),fields[2].charAt(0),fields[3]));

			line=br.readLine();
		}
		br.close();

		return patients;
	}
	public static HashMap<Character,ArrayList<Patient>> readRecords_2(String fileName) throws IOException
	{
		BufferedReader br=new BufferedReader(new FileReader(fileName));

		HashMap<Character,ArrayList<Patient>> patients=new HashMap<Character,ArrayList<Patient>>();
		String line;
		line=br.readLine();
		while(line!=null)
		{
			String[] fields=line.split("\\s");
			if(!patients.containsKey(fields[2].charAt(0)))
				patients.put(fields[2].charAt(0),new ArrayList<Patient>());
			patients.get(fields[2].charAt(0)).add(new Patient(fields[0],Integer.parseInt(fields[1]),fields[2].charAt(0),fields[3]));

			line=br.readLine();
		}
		br.close();

		return patients;
	}

	public static void display_records(ArrayList<Patient> patients)
	{
		for(int i=0;i<patients.size();++i)
			patients.get(i).display_patient();

		return;
	}
	public static void display_records(HashMap<Character,ArrayList<Patient>> patients)
	{
		for(char tower : patients.keySet())
			display_records(patients.get(tower));

		return;
	}

	public void display_selected(LocalDate selectedDate, ArrayList<Character> selectedTowers)
	{
		records.setText("\n FORMAT:\n Name | Age | Tower | ReportingDate | CurrentStatus | RecoveryDate");
		records.append("\n\n");
		brief.setText("\n");

		ArrayList<Integer> activeCases=new ArrayList<Integer>();
		ArrayList<Integer> recoveredCases=new ArrayList<Integer>();
		for(int i=0;i<selectedTowers.size();++i)
		{
			activeCases.add(0);
			recoveredCases.add(0);
		}

		for(int i=0;i<selectedTowers.size();++i)
		{
			ArrayList<Patient> towerPatients=this.patients.get(selectedTowers.get(i));
			ArrayList<String> towerRecords=new ArrayList<String>();

			for(int j=0;j<towerPatients.size();++j)
			{
				LocalDate reportingDate=towerPatients.get(j).get_dateOfReporting();
				if(reportingDate.isAfter(selectedDate))
					continue;
				else
				{
					String patientDetail=towerPatients.get(j).toString();
					patientDetail+=" | ";
					LocalDate recoveringDate=reportingDate.plusDays(this.recoveryDays);
					if(recoveringDate.isAfter(selectedDate))
					{
						activeCases.set(i,activeCases.get(i)+1);
						patientDetail+="ACTIVE";
					}
					else
					{
						recoveredCases.set(i,recoveredCases.get(i)+1);
						patientDetail+="RECOVERED";
					}
					patientDetail+=" | ";
					patientDetail+=recoveringDate.getDayOfMonth()+"/"+recoveringDate.getMonthValue()+"/"+recoveringDate.getYear();
					towerRecords.add(patientDetail);
				}
			}

			records.append(" \'"+selectedTowers.get(i)+"\' Tower Record-\n");
			for(int j=0;j<towerRecords.size();++j)
				records.append(" "+towerRecords.get(j)+"\n");
			if(towerRecords.size()==0)
				records.append(" Nothing to show\n");
			records.append("\n");

			brief.append(" \'"+selectedTowers.get(i)+"\' Tower Brief-\n");
			brief.append(" Active Cases: "+activeCases.get(i)+"\n");
			brief.append(" Recovered Cases: "+recoveredCases.get(i)+"\n");
			brief.append("\n");
		}

		return;
	}

	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource()==submit)
		{
			if(day.getSelectedItem().equals("Day") || month.getSelectedItem().equals("Month") || year.getSelectedItem().equals("Year"))
			{
				head1.setVisible(false);
				records.setText("");
				records.setVisible(false);
				recordsScroll.setVisible(false);
				head2.setVisible(false);
				brief.setText("");
				brief.setVisible(false);
				briefScroll.setVisible(false);
				return;
			}
			if(!option1.isSelected() && !option2.isSelected() && !option3.isSelected() && !option4.isSelected())
			{
				head1.setVisible(false);
				records.setText("");
				records.setVisible(false);
				recordsScroll.setVisible(false);
				head2.setVisible(false);
				brief.setVisible(false);
				briefScroll.setVisible(false);
				return;
			}
			String tempMonth=(String)month.getSelectedItem();
			if((tempMonth.equals("Apr") || tempMonth.equals("Jun")) && day.getSelectedItem().equals("31"))
			{
				day.setSelectedItem("30");
				head1.setVisible(false);
				records.setText("");
				records.setVisible(false);
				recordsScroll.setVisible(false);
				head2.setVisible(false);
				brief.setText("");
				brief.setVisible(false);
				briefScroll.setVisible(false);
				return;
			}

			String date=(String)day.getSelectedItem();
			if(date.length()==1)
				date="0"+date;
			date+="/";
			if(tempMonth.equals("Apr"))
				date+="04";
			else if(tempMonth.equals("May"))
				date+="05";
			else if(tempMonth.equals("Jun"))
				date+="06";
			else if(tempMonth.equals("Jul"))
				date+="07";
			else if(tempMonth.equals("Aug"))
				date+="08";
			date+="/";
			date+=(String)year.getSelectedItem();

			ArrayList<Character> selectedTowers=new ArrayList<Character>();
			if(option1.isSelected())
				selectedTowers.add(option1.getText().charAt(0));
			if(option2.isSelected())
				selectedTowers.add(option2.getText().charAt(0));
			if(option3.isSelected())
				selectedTowers.add(option3.getText().charAt(0));
			if(option4.isSelected())
				selectedTowers.add(option4.getText().charAt(0));

			display_selected(LocalDate.parse(date,DateTimeFormatter.ofPattern("dd/MM/yyyy")),selectedTowers);

			head1.setVisible(true);
			records.setVisible(true);
			recordsScroll.setVisible(true);
			head2.setVisible(true);
			brief.setVisible(true);
			briefScroll.setVisible(true);
		}

		return;
	}

	CovidTracker()
	{
		title="Covid Tracker";
		
		setTitle(title);
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		setSize(xSize,ySize);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ctPane=this.getContentPane(); 
        ctPane.setLayout(null);

		ctTitle=new JLabel(title); 
        ctTitle.setFont(new Font("Arial", Font.PLAIN, 50)); 
        ctTitle.setSize(title.length()*25, 50);
        ctTitle.setLocation((this.getWidth()-ctTitle.getWidth())/2,50);
        ctPane.add(ctTitle);

        date=new JLabel("Date");
        date.setFont(new Font("Arial", Font.PLAIN, 30)); 
        date.setSize(4*30,30); 
        date.setLocation(this.getWidth()/4-27,200); 
        ctPane.add(date);

        day=new JComboBox<String>(days); 
        day.setFont(new Font("Arial", Font.PLAIN, 15)); 
        day.setSize(60,20); 
        day.setLocation(this.getWidth()/4-105,250); 
        ctPane.add(day); 
  
        month=new JComboBox<String>(months); 
        month.setFont(new Font("Arial", Font.PLAIN, 15)); 
        month.setSize(70,20); 
        month.setLocation(this.getWidth()/4-30,250); 
        ctPane.add(month); 
  
        year=new JComboBox<String>(years); 
        year.setFont(new Font("Arial", Font.PLAIN, 15)); 
        year.setSize(70,20); 
        year.setLocation(this.getWidth()/4+60,250); 
        ctPane.add(year); 
  
        tower=new JLabel("Towers");
        tower.setFont(new Font("Arial", Font.PLAIN, 30)); 
        tower.setSize(7*30,30);
        tower.setLocation((2*this.getWidth())/3,200);
        ctPane.add(tower);

        option1=new JCheckBox("A"); 
        option1.setFont(new Font("Arial", Font.PLAIN, 15)); 
       	option1.setSize(50,20); 
        option1.setLocation((2*this.getWidth())/3-40,250); 
        ctPane.add(option1);

        option2=new JCheckBox("B"); 
        option2.setFont(new Font("Arial", Font.PLAIN, 15)); 
       	option2.setSize(50,20); 
        option2.setLocation((2*this.getWidth())/3+10,250); 
        ctPane.add(option2);

        option3=new JCheckBox("C"); 
        option3.setFont(new Font("Arial", Font.PLAIN, 15)); 
       	option3.setSize(50,20); 
        option3.setLocation((2*this.getWidth())/3+60,250); 
        ctPane.add(option3);

        option4=new JCheckBox("D"); 
        option4.setFont(new Font("Arial", Font.PLAIN, 15)); 
       	option4.setSize(50,20); 
        option4.setLocation((2*this.getWidth())/3+110,250); 
        ctPane.add(option4);

        submit=new JButton("Submit"); 
        submit.setFont(new Font("Arial", Font.PLAIN, 15)); 
        submit.setSize(6*15, 20); 
        submit.setLocation((this.getWidth()-submit.getWidth())/2, 300); 
        submit.addActionListener(this); 
        ctPane.add(submit);

        head1=new JLabel("Records...."); 
        head1.setFont(new Font("Arial", Font.PLAIN, 35)); 
        head1.setSize(200,40); 
        head1.setLocation(this.getWidth()/4+20,this.getHeight()/2-40);
        head1.setVisible(false); 
        ctPane.add(head1);

        records=new JTextArea(""); 
        records.setFont(new Font("Arial", Font.BOLD, 20)); 
        records.setLineWrap(true);
        records.setVisible(false);
        records.setEditable(false);

        recordsScroll=new JScrollPane(records);
        recordsScroll.setSize((2*this.getWidth())/3-50,this.getHeight()/2-70); 
        recordsScroll.setLocation(25,this.getHeight()/2+10);
        recordsScroll.setVisible(false);
        ctPane.add(recordsScroll);

        head2=new JLabel("Brief"); 
        head2.setFont(new Font("Arial", Font.PLAIN, 35)); 
        head2.setSize(200,40); 
        head2.setLocation((4*this.getWidth())/5,this.getHeight()/2-40);
        head2.setVisible(false); 
        ctPane.add(head2);

        brief=new JTextArea(""); 
        brief.setFont(new Font("Arial", Font.BOLD, 20)); 
        brief.setLineWrap(true);
        brief.setVisible(false);
        brief.setEditable(false); 

        briefScroll=new JScrollPane(brief);
        briefScroll.setSize(this.getWidth()/3-50,this.getHeight()/2-70); 
        briefScroll.setLocation((2*this.getWidth())/3+10,this.getHeight()/2+10); 
        briefScroll.setVisible(false);
        ctPane.add(briefScroll);

        this.setVisible(true);
	}

	public static void main(String[] args) throws IOException
	{
		String fileName="records.txt";
		patients=readRecords_2(fileName);

		new CovidTracker();

		return;
	}
}
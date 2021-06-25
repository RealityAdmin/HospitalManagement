package hospitalManagement;

//AUTHOR: Khubaib Ahmed
//Completed June 20, 2003
//For ICS4UA Culminating Project
//Simple hospital management system that manipulates a hospital with 30 beds and 15 ICUs
//Requires database to be set up before hand
//Also requires Java connector and MySQL to be installed


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.InputMismatchException;
import java.util.Scanner;


public class HospialManagement {
	
	static Scanner userInput = new Scanner(System.in);
	static DateTimeFormatter dateMaker = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/Hospital";
        String username = "root";
        String password = ""; //LEFT BLANK FOR REASONS; FILL IN UR OWN
        Connection connection;
        Statement statement;
        //String query = "select * from Persons";
        
        //checking if drivers are there
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
        	System.out.println("Unable to find com.mysql.cj.jdbc.Driver. Shutting down");
            //e.printStackTrace();
        }
        
        //Beginning connection
        try
        {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            mainMenu(connection, statement);
            System.out.println("Ending connection....");
            connection.close();
        }
        catch (SQLException e)
        {
        	System.out.println("Unable to establish proper connection. Shutting down");
            //e.printStackTrace();
        }
        
        
	}
	
	public static void mainMenu(Connection connection, Statement statement)
	{
		boolean loop = true;
		while (loop)
		{
			System.out.println();
			System.out.println("Welcome back to the K Hospital Management System! What do you want to do?");
			System.out.println("Type Patients to access patient-related functions");
			System.out.println("Type Beds to access Bed related functions");
			System.out.println("Type ICU to access ICU related functions");
			System.out.println("Type Finish to exit the System");
			String input = userInput.nextLine();
			//userInput.nextLine();
			if (input.equals("Patients") || input.equals("patients"))
			{
				System.out.println("Type in Add Patient to Add a Patient to the Database");
				System.out.println("Type in Remove Patient to remover a patient from the database");
				System.out.println("Type in Edit Patient to edit a specific patient");
				System.out.println("Type in View Patients to view all patients");
				System.out.println("Type in Back to go Back to the main menu");
				input = userInput.nextLine();
				if (input.equals("Add Patient")) {
					addPatient(connection);
				}
				else if (input.equals("Remove Patient")) {
					removePatient(connection);
				}
				else if (input.equals("Edit Patient")) {
					editPatient(connection);
				}
				else if (input.equals("View Patients")) {
					viewPatients(connection);
				}
			}
			else if (input.equals("Beds") || input.equals("beds"))
			{
				System.out.println("Type in Add Bed to place a patient from the database into a bed");
				System.out.println("Type in View Beds to view all patient occupying a bed");
				System.out.println("Type in Release Patient to take a patient off of a bed");
				System.out.println("Type in Send to ICU to tranfer a patient to the ICU units");
				input = userInput.nextLine();
				if (input.equals("Add Bed"))
				{
					addBed(connection);
				}
				else if (input.equals("View Beds"))
				{
					viewNormalBeds(connection);
				}
				else if (input.equals("Release Patient"))
				{
					removeBed(connection);
				}
				else if (input.equals("Send to ICU"))
				{
					transferToICU(connection);
				}
			}
			else if (input.equals("ICU") || input.equals("icu"))
			{
				System.out.println("Type in Add ICU to place a patient from the database into an ICU unit");
				System.out.println("Type in View ICU to view all patient occupying an ICU unit.");
				System.out.println("Type in Release Patient to take a patient off of an ICU unit.");
				System.out.println("Type in Send to Beds to tranfer a patient from the ICU units to a hospital bed if they have stabilized.");
				input = userInput.nextLine();
				if (input.equals("View ICU"))
				{
					viewICUBeds(connection);
				}
				else if (input.equals("Add ICU"))
				{
					addICU(connection);
				}
				else if (input.equals("Release Patient"))
				{
					removeICU(connection);
				}
				else if (input.equals("Send to Beds"))
				{
					transferToBed(connection);
				}
			}
			else if (input.equals("Finish") || input.equals("finish"))
			{
				System.out.println("Logging out....");
				loop = false;
			}
			System.out.println();
		}
	}
	
	public static void addPatient(Connection connection)
	{
		String input = "";
		String query = "INSERT INTO Patients (Health_Card, First_Name, Last_Name, Home_Phone_Number, Cell_Phone_Number,"
				+ "Contact_First_Name, Contact_Last_Name, Past_Medical_History, Allergic_Reactions, Medical_Conditions, Prescriptions) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try {
			boolean loop = true;
			PreparedStatement command = connection.prepareStatement(query);
			while (loop)
			{
				System.out.println("Enter the health card # (or type Back to return to main menu): ");
				input = userInput.nextLine();
				if (input.equals("Back"))
				{
					System.out.println("Returning to main menu...");
					return;
				}
				if (!checkHealthCard(connection, input))
				{
					System.out.println("Invalid input");
				}
				else
				{
					loop = false;
				}
			}
			command.setString(1, input.substring(0));

			loop = true;
			while (loop)
			{
				System.out.println("Enter the first name  (or type Back to return to main menu): ");
				input = userInput.nextLine();
				if (input.equals("Back"))
				{
					System.out.println("Returning to main menu...");
					return;
				}
				if (input.length() < 1)
				{
					System.out.println("This is a mandatory field");
				}
				else
				{
					loop = false;
				}
			}
			command.setString(2, input.substring(0));

			loop = true;
			while (loop)
			{
				System.out.println("Enter the last name  (or type Back to return to main menu): ");
				input = userInput.nextLine();
				if (input.equals("Back"))
				{
					System.out.println("Returning to main menu...");
					return;
				}
				if (input.length() < 1)
				{
					System.out.println("This is a mandatory field");
				}
				else
				{
					loop = false;
				}
			}
			command.setString(3, input.substring(0));

			loop = true;
			while (loop)
			{
				System.out.println("Enter the home phone # (no dashes) (or type Back to return to main menu): ");
				input = userInput.nextLine();
				if (input.equals("Back"))
				{
					System.out.println("Returning to menu...");
					return;
				}
				loop = !checkPhoneNumber(input);
			}
			command.setString(4, input.substring(0));

			loop = true;
			while (loop)
			{
				System.out.println("Enter the cell phone # (no dashes)  (or type Back to return to main menu): ");
				input = userInput.nextLine();
				if (input.equals("Back"))
				{
					System.out.println("Returning to menu...");
					return;
				}
				loop = !checkPhoneNumber(input);
			}
			command.setString(5, input.substring(0));

			System.out.println("Enter the first name of a person to contact (or leave blank)  (or type Back to return to main menu): ");
			String contactFirst = userInput.nextLine();
			if (contactFirst.equals("Back"))
			{
				System.out.println("Returning to main menu...");
				return;
			}
			if (contactFirst.equals(""))
			{
				command.setNull(6, Types.NULL);
			}
			else {
				command.setString(6, contactFirst);
			}
			
			System.out.println("Enter the last name of a person to contact (or leave blank)  (or type Back to return to main menu): ");
			String contactLast = userInput.nextLine();
			if (contactLast.equals("Back"))
			{
				System.out.println("Returning to menu...");
				return;
			}
			if (contactLast.equals(""))
			{
				command.setNull(7, Types.NULL);
				
			}
			else {
				command.setString(7, contactLast);
			}

			loop = true;
			while (loop)
			{
				System.out.println("In 200 characters or less, write down the past medical history (can leave blank if not applicable)  (or type Back to return to main menu): :");
				input = userInput.nextLine();
				if (input.equals("Back"))
				{
					System.out.println("Returning to menu...");
					return;
				}
				if (input.length() > 200)
				{
					System.out.println("Too large");
				}
				else
				{
					loop = false;
				}
			}
			String pastMedicalHistory = input.substring(0);
			if (pastMedicalHistory.equals(""))
			{
				command.setNull(8, Types.NULL);
				
			}
			else {
				command.setString(8, pastMedicalHistory);
			}

			loop = true;
			while (loop)
			{
				System.out.println("In 100 characters or less, write down any allergic reactions (can leave blank if not applicable) (or type Back to return to main menu): ");
				input = userInput.nextLine();
				if (input.equals("Back"))
				{
					System.out.println("Returning to menu...");
					return;
				}
				if (input.length() > 100)
				{
					System.out.println("Too large");
				}
				else
				{
					loop = false;
				}
			}
			String allergies = input.substring(0);
			if (allergies.equals(""))
			{
				command.setNull(9, Types.NULL);
				
			}
			else {
				command.setString(9, allergies);
			}

			loop = true;
			while (loop)
			{
				System.out.println("In 200 characters or less, write down any medical conditions (can leave blank if not applicable)  (or type Back to return to main menu):");
				input = userInput.nextLine();
				if (input.equals("Back"))
				{
					System.out.println("Returning to menu...");
					return;
				}
				if (input.length() > 200)
				{
					System.out.println("Too large");
				}
				else
				{
					loop = false;
				}
			}
			String medicalConditions = input.substring(0);
			if (medicalConditions.equals(""))
			{
				command.setNull(10, Types.NULL);
				
			}
			else {
				command.setString(10, medicalConditions);
			}
			
			loop = true;
			while (loop)
			{
				System.out.println("In 100 characters or less, write down any prescriptions (can leave blank if not applicable)  (or type Back to return to main menu): ");
				input = userInput.nextLine();
				if (input.equals("Back"))
				{
					System.out.println("Returning to menu...");
					return;
				}
				if (input.length() > 200)
				{
					System.out.println("Too large");
				}
				else
				{
					loop = false;
				}
			}
			String prescriptions = input.substring(0);
			if (prescriptions.equals(""))
			{
				command.setNull(11, Types.NULL);
				
			}
			else {
				command.setString(11, prescriptions);
			}

			try
			{
				command.execute();
				System.out.println("Patient successfully added to patient database");
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			finally
			{
				System.out.println("Returning to main menu...");
			}
		}
		catch (SQLException e)
		{
			System.out.println("Error in code");
		}
		
		
	}
	
	public static void removePatient(Connection connection)
	{
		System.out.println("Enter the health card number of the patient to remove from the database (or type Back to go back): ");
		String remove = userInput.nextLine();
		if (remove.equals("Back"))
		{
			return;
		}
		else if (!searchHealthCard(connection, remove, "Patients"))
		{
			System.out.println("That health card is not in the patients database");
			return;
		}
		String query = "DELETE FROM Patients WHERE Health_Card = ?;";
		
		try
		{
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, remove);
			statement.execute();
			System.out.println("Patient successfully removed");
		}
		catch (SQLException e)
		{
			System.out.println("Could not delete that patient");
			//e.printStackTrace();
		}
		finally
		{
			System.out.println("Returning to main menu...");
		}
	}
	
	public static void editPatient(Connection connection)
	{
		System.out.println("Enter the health card # of the patient to edit");
		String healthCard = userInput.nextLine();
		System.out.println();
		
		//check if health card number exists in the patient database
		
		if (!searchHealthCard(connection, healthCard, "patients"))
		{
			System.out.println("That health card does not exits in the database");
		}
		else
		{
			boolean loop = true;	
			String edit = null;
			PreparedStatement statement = null;

			while (loop)
			{
				try
				{
					
					statement = connection.prepareStatement("SELECT * FROM Patients WHERE Health_Card = ? ");
					statement.setString(1, healthCard);
					ResultSet info = statement.executeQuery();
					while (info.next())
					{
						System.out.println("Health Card #: " + info.getString("Health_Card"));
						System.out.println("Name: " + info.getString("First_Name") + " " + info.getString("Last_Name"));
						System.out.println("Home Phone: " + info.getString("Home_Phone_Number"));
						System.out.println("Cell Phone: " + info.getString("Cell_Phone_Number"));
						System.out.println("Contact Name: " + info.getString("Contact_First_Name") + " " + info.getString("Contact_Last_Name"));
						System.out.println("Medical History: " + info.getString("Past_Medical_History"));
						System.out.println("Allergies: " + info.getString("Allergic_Reactions"));
						System.out.println("Medical Conditions: " + info.getString("Medical_Conditions"));
						System.out.println("Prescriptions: " + info.getString("Prescriptions"));
					}
					
					System.out.println();
					System.out.println("What do you want to update?");
					System.out.println("Type Card for Health Card #, Name for Patient Name, ");
					System.out.println("Phone for patient's phone #s, Contact for Contact Information,");
					System.out.println("History for Patient's Medical History, Allergies for allergen info, Conditions for Medical Conditions,");
					System.out.println("and Prescriptions for prescriptions.");
					System.out.println("Type Exit to return to Main Menu");
					String field = userInput.nextLine();
					switch (field)
					{					
						case "Card":
							edit = "UPDATE Patients SET Health_Card=? WHERE Health_Card = ?";
							statement = connection.prepareStatement(edit);
							statement.setString(2, healthCard);
							if (! (searchHealthCard(connection, healthCard, "Normal_Beds") || 
									searchHealthCard(connection, healthCard, "ICU_Beds") ))
							{
								boolean c = true;
								while (c)
								{
									System.out.println("Enter the new health card (or type Back to go back): ");
									String newHealth = userInput.nextLine();
									if (checkHealthCard(connection, newHealth))
									{
										statement.setString(1, newHealth);
										healthCard = newHealth;
										statement.execute();
										System.out.println("Card number updated successfully");
										c = false;
									}
									else if (newHealth.equals("Back"))
									{
										System.out.println("Health Card unchanged");
										c = false;
									}
									else
									{
										System.out.println("Invalid health card number. Try again.");
									}
								}
							}
							else
							{
								System.out.println("This patient is currently hospitalized; health card cannot be changed at this time.");
							}
							//TODO: Update it so that when card is updated, corresponding bed entries are also updated. Also preventing conflict with other health cards
							break;
						
						case "Name":							
							//TODO
							edit = "UPDATE Patients SET First_Name=? WHERE Health_Card = ?";
							statement = connection.prepareStatement(edit);
							//PreparedStatement statement2 = connection.prepareStatement(edit2);
							statement.setString(2, healthCard);
							//statement2.setString(2, healthCard);
							
							boolean n = true;
							while (n)
							{
								System.out.println("Enter the first name (or type Skip to skip): ");
								String firstName = userInput.nextLine();
								if (firstName.strip().equals(""))
								{
									System.out.println("Cannot be left blank:");
								}
								else if (firstName.strip().equals("Skip")){
									n = false;
								}
								else
								{
									statement.setString(1, firstName);
									statement.execute();
									n = false;
								}
							}
							
							edit = "UPDATE Patients SET Last_Name=? WHERE Health_Card = ?";
							//Last name
							statement = connection.prepareStatement(edit);
							statement.setString(2, healthCard);
							
							n = true;
							while (n)
							{
								System.out.println("Enter the last name (or type Skip to skip): ");
								String lastName = userInput.nextLine();
								if (lastName.strip().equals(""))
								{
									System.out.println("Cannot be left blank:");
								}
								else if (lastName.strip().equals("Skip")){
									n = false;
								}
								else
								{
									statement.setString(1, lastName);
									statement.execute();
									n = false;
								}
							}
							
							break;
							
						case "Phone":
							//TODO							
							System.out.println("Edit home phone # (type home) or phone cell # (type cell)?");
							String phoneType = userInput.nextLine();
							if (phoneType.equals("home"))
							{
								edit = "UPDATE Patients SET Home_Phone_Number=? WHERE Health_Card = ?";
								statement = connection.prepareStatement(edit);
								statement.setString(2, healthCard);
								boolean l = true;
								while (l)
								{
									System.out.println("Enter the new phone # (or type back to go back): ");
									String newHome = userInput.nextLine();
									if (checkPhoneNumber(newHome))
									{
										statement.setString(1, newHome);
										statement.execute();
										l = false;
									}
									else if (newHome.equals("Back"))
									{
										l = false;
									}
									else {
										System.out.println("Invalid phone #");
									}
								}
							}
							else if (phoneType.equals("cell"))
							{
								edit = "UPDATE Patients SET Cell_Phone_Number=? WHERE Health_Card = ?";
								statement = connection.prepareStatement(edit);
								statement.setString(2, healthCard);
								boolean l = true;
								while (l)
								{
									System.out.println("Enter the new phone # (or type Back to go back): ");
									String newCell = userInput.nextLine();
									if (checkPhoneNumber(newCell))
									{
										statement.setString(1, newCell);
										statement.execute();
										l = false;
									}
									else if (newCell.equals("Back"))
									{
										l = false;
									}
									else {
										System.out.println("Invalid phone #");
									}
								}
							}
							else {
								System.out.println("Invalid input");
							}
							break;
							
						case "Contact":
							//TODO
							edit = "UPDATE Patients SET First_Name=? WHERE Health_Card = ?";
							statement = connection.prepareStatement(edit);
							//PreparedStatement statement2 = connection.prepareStatement(edit2);
							statement.setString(2, healthCard);
							//statement2.setString(2, healthCard);
							
							boolean c = true;
							while (c)
							{
								System.out.println("Enter the first name (or type Skip to skip): ");
								String firstName = userInput.nextLine();
								if (firstName.strip().equals(""))
								{
									System.out.println("Cannot be left blank:");
								}
								else if (firstName.strip().equals("Skip")){
									c = false;
								}
								else
								{
									statement.setString(1, firstName);
									statement.execute();
									c = false;
								}
							}
							
							edit = "UPDATE Patients SET Last_Name=? WHERE Health_Card = ?";
							//Last name
							statement = connection.prepareStatement(edit);
							statement.setString(2, healthCard);
							
							c = true;
							while (c)
							{
								System.out.println("Enter the last name (or type Skip to skip): ");
								String lastName = userInput.nextLine();
								if (lastName.strip().equals(""))
								{
									System.out.println("Cannot be left blank:");
								}
								else if (lastName.strip().equals("Skip")){
									c = false;
								}
								else
								{
									statement.setString(1, lastName);
									statement.execute();
									c = false;
								}
							}
							break;
							
						case "History":
							//TODO
							edit = "UPDATE Patients SET Past_Medical_History=? WHERE Health_Card = ?";
							statement = connection.prepareStatement(edit);
							statement.setString(2, healthCard);
							System.out.println("Enter the revised medical history in < 200 characters (or type nothing to go back): ");
							String history = userInput.nextLine();
							if (history.strip().equals(""))
							{
								System.out.println("History unchanged");
							}
							else if (history.length() > 200)
							{
								System.out.println("Too large");
							}
							else
							{
								statement.setString(1, history);
								statement.execute();
							}
							break;
							
						case "Allergies":
							//TODO
							edit = "UPDATE Patients SET Allergic_Reactions=? WHERE Health_Card = ?";
							statement = connection.prepareStatement(edit);
							statement.setString(2, healthCard);
							System.out.println("Enter the revised allergy info in < 100 characters (or type nothing to go back): ");
							String allergies = userInput.nextLine();
							if (allergies.strip().equals(""))
							{
								System.out.println("Allergy info unchanged");
							}
							else if (allergies.length() > 100)
							{
								System.out.println("Too large");
							}
							else
							{
								statement.setString(1, allergies);
								statement.execute();
							}
							break;
							
						case "Conditions":
							//TODO
							edit = "UPDATE Patients SET Medical_Conditions=? WHERE Health_Card = ?";
							statement = connection.prepareStatement(edit);
							statement.setString(2, healthCard);
							System.out.println("Enter the revised medical conditions in < 200 characters (or type nothing to go back): ");
							String conditions = userInput.nextLine();
							if (conditions.strip().equals(""))
							{
								System.out.println("Conditions info unchanged");
							}
							else if (conditions.length() > 200)
							{
								System.out.println("Too large");
							}
							else
							{
								statement.setString(1, conditions);
								statement.execute();
							}
							break;
							
						case "Exit":
							loop = false;
							System.out.println("Returning to main menu");
							break;
							
						case "Prescriptions":
							//TODO
							edit = "UPDATE Patients SET Prescriptions=? WHERE Health_Card = ?";
							statement = connection.prepareStatement(edit);
							statement.setString(2, healthCard);
							System.out.println("Enter the revised prescription info in < 100 characters (or type nothing to go back): ");
							String prescriptions = userInput.nextLine();
							if (prescriptions.strip().equals(""))
							{
								System.out.println("Prescription unchanged");
							}
							else if (prescriptions.length() > 100)
							{
								System.out.println("Too large");
							}
							else
							{
								statement.setString(1, prescriptions);
								statement.execute();
							}
							break;
							
						
					}
				}
				catch (SQLException e)
				{
					System.out.println("There was an error in trying to edit the field. Please try again.");
					//e.printStackTrace();
					loop = false;
				}
				
			}
		}
		
	}
	
	//View Patients is VERY work in progress, might cut
	public static void viewPatients(Connection connection) 
	{
		
		//Filters 
		//boolean loop = true;
		//String[] filters = new String[10];
		//System.out.println("What ");
		String query = "SELECT * FROM Patients WHERE ";
		
		try
		{
			Statement statement = connection.createStatement();
			ResultSet patients = statement.executeQuery("SELECT Health_Card, First_Name, Last_Name FROM Patients");
			System.out.println("Health Card | First Name | Last Name ");
			while (patients.next())
			{
				System.out.printf("%s | %s | %s %n", patients.getString(1), patients.getString(2), patients.getString(3));
			}
			
		}
		catch (SQLException e)
		{
			System.out.println("There was an error");
		}
		
	}
	
	public static int countNormalBeds(Connection connection)
	{
		try {
			Statement statement = connection.createStatement();
			ResultSet count = statement.executeQuery("SELECT COUNT(*) from Normal_Beds");
			String c = "";
			while (count.next())
			{
				c += count.getString(1);
			}
			int cnt = Integer.parseInt(c);
			return cnt;
		} catch (SQLException e) {
			System.out.println("Error in ocunting normal beds occupied.");
			//e.printStackTrace();
		}
		return 0;
	}
	
	public static int countICUBeds(Connection connection)
	{
		try {
			Statement statement = connection.createStatement();
			ResultSet count = statement.executeQuery("SELECT COUNT(*) from ICU_Beds");
			String c = "";
			while (count.next())
			{
				c += count.getString(1);
			}
			int cnt = Integer.parseInt(c);
			return cnt;
		} catch (SQLException e) {
			System.out.println("Error in counting occupied ICU Beds");
			//e.printStackTrace();
		}
		return 0;
	}
	
	public static boolean searchHealthCard(Connection connection, String healthCard, String database)
	{
		
		boolean isInDatabase = false;
		try {
			String query = "SELECT Health_Card, COUNT(*) from " + database + " WHERE Health_Card = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, healthCard);
			//System.out.println(statement);
			ResultSet search = statement.executeQuery();
			String c = "";
			while (search.next())
			{
				c += search.getString("COUNT(*)");
			}
			int cnt = Integer.parseInt(c);
			isInDatabase = cnt > 0;
			
		}
		catch (SQLException e) {
			System.out.println("Error in searching for health card");
			//e.printStackTrace();
		}
		return isInDatabase;
	}
	
	public static boolean checkPhoneNumber(String number)
	{
		boolean isValid = true;
		if (number.length() < 10)
		{
			isValid = false;
			return isValid;
		}
		for (int i = 0; i < number.length(); i++)
		{
			char c = number.charAt(i);
			if (!Character.isDigit(c)) {
				isValid = false;
				//stop the for loop
				i = number.length();
			}
		}
		return isValid; 
	}
	
	public static boolean checkHealthCard(Connection connection, String healthCard)
	{
		if (healthCard.length() != 12)
		{
			return false;
		}
		else
		{
			for (int i = 0; i <= 9; i++)
			{
				char c = healthCard.charAt(i);
				if (!Character.isDigit(c))
				{
					return false;
				}
			}
			for (int i = 10; i < 12; i++)
			{
				char c = healthCard.charAt(i);
				if (!Character.isAlphabetic(c))
				{
					return false;
				}
			}
		}
		if (searchHealthCard(connection, healthCard, "Patients"))
		{
			return false;
		}		
		
		return true;
	}
	
	public static void viewNormalBeds(Connection connection)
	{
		try
		{
			Statement statement = connection.createStatement();
			ResultSet beds = statement.executeQuery("SELECT * FROM Normal_Beds");
			System.out.println("Health Card | First Name | Last Name | Past Medical History | Allergies | Medical Conditions"
					+ " | Date of Entry | Severity | Date of Release | Cause of Hospitalization");
			while (beds.next())
			{
				System.out.printf("%s | %s | %s | %s | %s | %s | %s | %s | %s | %s %n", beds.getString("Health_Card"),
						beds.getString("First_Name"), beds.getString("Last_Name"), beds.getString("Past_Medical_History"),
						beds.getString("Allergic_Reactions"), beds.getString("Medical_Conditions"), beds.getString("date_entry"),
						beds.getString("severity"), beds.getString("date_release"), beds.getString("Cause_Of_Entry"));
			}
			
		}
		catch (SQLException e)
		{
			System.out.println("Error in displaying beds");
		}
	}

	public static void addBed(Connection connection)
	{
		System.out.println("Enter the health card of the patient (in the database) to put on a bed: ");
		String healthCard = userInput.nextLine();
		if (searchHealthCard(connection, healthCard, "Patients") && 
				!searchHealthCard(connection, healthCard, "Normal_Beds") && 
				!searchHealthCard(connection, healthCard, "ICU_Beds") && countNormalBeds(connection) < 30)  
		{
			try
			{
				String command = "INSERT INTO Normal_Beds (Health_Card, First_Name, Last_Name, Past_Medical_History, Allergic_Reactions, Medical_Conditions)"
						+ " SELECT Health_Card, First_Name, Last_Name, Past_Medical_History, Allergic_Reactions, Medical_Conditions FROM Patients WHERE Health_Card = ?;";
				PreparedStatement statement = connection.prepareStatement(command);
				statement.setString(1, healthCard);
				//Getting patient info into the bed database
				
				//Next lines of code will get the date of entry (current date), severity of the patient, and asks if there is an estimated release date
				String command2 = "UPDATE Normal_Beds SET date_entry=?, severity=?, date_release=?, Cause_Of_Entry=? WHERE Health_Card=?;";
				PreparedStatement statement2 = connection.prepareStatement(command2);
				statement2.setString(5, healthCard);
				Date date = new Date(System.currentTimeMillis());
				statement2.setDate(1, date);
				
				//Loop to get severity of condition of patient
				boolean loop = true;
				while (loop)
				{
					System.out.println("On a scale of 1 to 10, give the severity of the patient's condition: ");
					try
					{
						int severity = userInput.nextInt();
						if (severity > 10 || severity < 1)
						{
							System.out.println("Outside the scale, try again");
						}
						else
						{
							statement2.setInt(2, severity);
							//System.out.println(severity);
							userInput.nextLine();
							loop = false;
						}
					}
					catch (InputMismatchException e)
					{
						System.out.println("That is not an integer, try again");
						userInput.nextLine();
					}
				}
				
				loop = true;
				while (loop)
				{
					System.out.println("What is the cause for being hospitalized? In < 200 characters: ");
					String cause = userInput.nextLine();
					if (cause.length() > 200)
					{
						System.out.println("Over character limit, try again");
					}
					else if (cause.strip().length() < 1)
					{
						System.out.println("This cannot be left blank, enter a cause of hospitalization");
					}
					else
					{
						statement2.setString(4, cause);
						loop = false;
					}
				}
				
				loop = true;
				while (loop)
				{
					System.out.println("Can you estimate a date of release yet? Enter how many days the patient will stay (or enter -1) if not sure)");
					try
					{
						int stay = userInput.nextInt();
						LocalDate dateR = LocalDate.now();
						if (stay < 0)
						{
							statement2.setNull(3, Types.NULL);
							loop = false;
						}
						else
						{
							dateR = dateR.plusDays(stay);
							String dareRelease = dateR.format(dateMaker);
							statement2.setDate(3, Date.valueOf(dareRelease));
							loop = false;
						}
						
					}
					catch (InputMismatchException e)
					{
						System.out.println("That is not an integer, try again");
						userInput.nextLine();
					}
				}
				
				statement.execute();
				statement2.execute();
				System.out.println("Patient transferred into a bed. ");
				System.out.println();
				
			}
			catch (SQLException e)
			{
				System.out.println("Error in trying to add patient to a bed");
				//e.printStackTrace();
			}
		}
		else
		{
			System.out.println("That patient is either not in our database or already in a bed.");
		}
	}
	
	public static void removeBed(Connection connection)
	{
		System.out.println("Enter the health card # of the person to transfer out of a bed: ");
		String healthCard = userInput.nextLine();
		String getDate = "SELECT date_release from Normal_Beds WHERE Health_Card = ?";
		String command1 = "DELETE FROM Normal_Beds WHERE Health_Card = ?";
		
		//Only proceed if the patient is actually in a bed
		if (searchHealthCard(connection, healthCard, "Normal_Beds"))
		{
			try
			{
				PreparedStatement dateStatement = connection.prepareStatement(getDate);
				PreparedStatement delStatement = connection.prepareStatement(command1);
				dateStatement.setString(1, healthCard);
				
				//Check if date of release is today; if not confirm choices
				ResultSet date = dateStatement.executeQuery();
				LocalDate dateR = null;
				while (date.next())
				{
					try
					{
						dateR = LocalDate.parse(date.getString(1));
					}
					catch (NullPointerException e)
					{
						dateR = LocalDate.now();
					}
				}
				LocalDate today = LocalDate.now();
				
				if (dateR.compareTo(today) > 0)
				{
					boolean loop = true;
					while (loop)
					{
						System.out.println("The scheduled release date for that patient is not today."
								+ " Are you sure you want to release this patient? (Yes or No)");
						String response = userInput.nextLine();
						if (response.equals("Yes"))
						{
							System.out.println("Very well");
							loop = false;
						}
						else if (response.equals("No"))
						{
							System.out.println("Returning to main menu then...");
							loop = false;
							return;
						}
						else
						{
							System.out.println("Invalid input, try again. ");
						}
					}
				}
				System.out.println("Proceeding with deletion...");
				delStatement.setString(1, healthCard);
				delStatement.execute();
				
				boolean loop = true;
				while (loop) {
					System.out.println("Do you want to make any changes to the prescriptions that the patient has? (Yes or No)");
					String input = userInput.nextLine();
					if (input.equals("Yes"))
					{
						try
						{
							String c1 = "SELECT Prescriptions FROM Patients WHERE Health_Card=?";
							PreparedStatement p1 = connection.prepareStatement(c1);
							p1.setString(1, healthCard);
							ResultSet result = p1.executeQuery();
							while (result.next())
							{
								System.out.println("Here is the prescription info:");
								System.out.println(result.getString(1));
							}
							System.out.println();
							
							boolean l = true;
							String c2 = "UPDATE Patients SET Prescriptions=? WHERE Health_Card=?";
							PreparedStatement p2 = connection.prepareStatement(c2);
							p2.setString(2, healthCard);
							while (l)
							{
								System.out.println("Enter the changed prescription info: ");
								String prescription = userInput.nextLine();
								if (prescription.length() > 100)
								{
									System.out.println("Too large; try again");
								}
								else if (prescription.strip().length() < 0)
								{
									p2.setNull(1, Types.NULL);
									l = false;
								}
								else
								{
									p2.setString(1, prescription);
									l = false;
								}
							}
							
							p2.execute();
							System.out.println("Prescription info saved.");
							loop = false;
						}
						catch (SQLException e)
						{
							System.out.println("Error in changing prescriptions");
							loop = false;
						}
					}
					else if (input.equals("No"))
					{
						System.out.println("Prescription unchanged");
						loop = false;
					}
					else
					{
						System.out.println("Invalid input");
					}
				}
				
				
			}
			catch (SQLException e)
			{
				System.out.println("Error in removing patient from bed.");
			}
		}
		
		else
		{
			System.out.println("That patient is not in the bed database");
		}
		
		System.out.println("Returning to main menu... ");
		
	}

	public static void transferToICU(Connection connection)
	{
		System.out.println("Enter the health card of the patient to transfer from hospital bed to ICU");
		String healthCard = userInput.nextLine();
		if (!searchHealthCard(connection, healthCard, "Normal_Beds"))
		{
			System.out.println("That patient is not in the beds. ");
			return;
		}
		System.out.println("Transferring to ICU... ");
		try
		{
			String c = "INSERT INTO ICU_Beds (Health_Card, First_Name, Last_Name, Past_Medical_History, "
					+ "Allergic_Reactions, Medical_Conditions, Cause_Of_Entry) "
					+ "SELECT Health_Card, First_Name, Last_Name, Past_Medical_History, "
					+ "Allergic_Reactions, Medical_Conditions, Cause_Of_Entry "
					+ "FROM Normal_Beds WHERE Health_Card=?;";
			PreparedStatement pst = connection.prepareStatement(c);
			pst.setString(1, healthCard);
			
			Date entry = new Date(System.currentTimeMillis());
			String c2 = "UPDATE ICU_beds SET date_entry = ?, date_release = ? WHERE Health_Card = ?";
			PreparedStatement pst2 = connection.prepareStatement(c2);
			pst2.setDate(1, entry);
			pst2.setString(3, healthCard);
			
			boolean loop = true;
			while (loop)
			{
				System.out.println("Can you estimate a date of release yet? Enter how many days the patient will stay (or enter -1) if not sure)");
				try
				{
					int stay = userInput.nextInt();
					LocalDate dateR = LocalDate.now();
					if (stay < 0)
					{
						pst2.setNull(2, Types.NULL);
						loop = false;
					}
					else
					{
						dateR = dateR.plusDays(stay);
						String dareRelease = dateR.format(dateMaker);
						pst2.setDate(2, Date.valueOf(dareRelease));
						loop = false;
					}
					
				}
				catch (InputMismatchException e)
				{
					System.out.println("That is not an integer, try again");
					userInput.nextLine();
				}
			}
			
			System.out.println("Transferring to ICU...");
			pst.execute();
			pst2.execute();
			System.out.println("Freeing up hospital bed...");
			
			pst = connection.prepareStatement("DELETE FROM normal_beds WHERE Health_Card=?;");
			pst.setString(1, healthCard);
			pst.execute();
			
			System.out.println("Successful transfer to ICU");
			
			
		}
		catch (SQLException e)
		{
			System.out.println("Error in tranferring patient to ICU. ");
			e.printStackTrace();
		}
		finally{
			System.out.println("Returning to Main Menu");
		}
	}
	
	public static void viewICUBeds(Connection connection)
	{
		try
		{
			Statement statement = connection.createStatement();
			ResultSet beds = statement.executeQuery("SELECT * FROM ICU_Beds");
			System.out.println("Health Card | First Name | Last Name | Past Medical History | Allergies | Medical Conditions"
					+ " | Date of Entry |  Date of Release | Cause of Hospitalization");
			while (beds.next())
			{
				System.out.printf("%s | %s | %s | %s | %s | %s | %s | %s | %s %n", beds.getString("Health_Card"),
						beds.getString("First_Name"), beds.getString("Last_Name"), beds.getString("Past_Medical_History"),
						beds.getString("Allergic_Reactions"), beds.getString("Medical_Conditions"), beds.getString("date_entry"),
						beds.getString("date_release"), beds.getString("Cause_Of_Entry"));
			}
			
		}
		catch (SQLException e)
		{
			System.out.println("Error in displaying ICU Units. ");
		}
	}

	public static void addICU(Connection connection)
	{
		System.out.println("Enter the health card of the patient (in the database) to put on a bed: ");
		String healthCard = userInput.nextLine();
		if (searchHealthCard(connection, healthCard, "Patients") && 
				!searchHealthCard(connection, healthCard, "Normal_Beds") && 
				!searchHealthCard(connection, healthCard, "ICU_Beds") && countICUBeds(connection) < 15)  
		{
			try
			{
				String command = "INSERT INTO ICU_Beds (Health_Card, First_Name, Last_Name, Past_Medical_History, Allergic_Reactions, Medical_Conditions)"
						+ " SELECT Health_Card, First_Name, Last_Name, Past_Medical_History, Allergic_Reactions, Medical_Conditions FROM Patients WHERE Health_Card = ?;";
				PreparedStatement statement = connection.prepareStatement(command);
				statement.setString(1, healthCard);
				//Getting patient info into the bed database
				
				//Next lines of code will get the date of entry (current date), severity of the patient, and asks if there is an estimated release date
				String command2 = "UPDATE ICU_Beds SET date_entry=?, date_release=?, Cause_Of_Entry=? WHERE Health_Card=?;";
				PreparedStatement statement2 = connection.prepareStatement(command2);
				statement2.setString(4, healthCard);
				Date date = new Date(System.currentTimeMillis());
				statement2.setDate(1, date);
				
				//Loop to get severity of condition of patient
				boolean loop = true;
				while (loop)
				{
					System.out.println("What is the cause for being hospitalized? In < 200 characters: ");
					String cause = userInput.nextLine();
					if (cause.length() > 200)
					{
						System.out.println("Over character limit, try again");
					}
					else if (cause.strip().length() < 1)
					{
						System.out.println("This cannot be left blank, enter a cause of hospitalization");
					}
					else
					{
						statement2.setString(3, cause);
						loop = false;
					}
				}
				
				loop = true;
				while (loop)
				{
					System.out.println("Can you estimate a date of release yet? Enter how many days the patient will stay (or enter -1) if not sure)");
					try
					{
						int stay = userInput.nextInt();
						LocalDate dateR = LocalDate.now();
						if (stay < 0)
						{
							statement2.setNull(2, Types.NULL);
							loop = false;
						}
						else
						{
							dateR = dateR.plusDays(stay);
							String dareRelease = dateR.format(dateMaker);
							statement2.setDate(2, Date.valueOf(dareRelease));
							loop = false;
						}
						
					}
					catch (InputMismatchException e)
					{
						System.out.println("That is not an integer, try again");
						userInput.nextLine();
					}
				}
				
				statement.execute();
				statement2.execute();
				System.out.println("Patient transferred into an ICU unit. ");
				System.out.println();
				
			}
			catch (SQLException e)
			{
				System.out.println("Error in trying to add patient to an ICU");
				//e.printStackTrace();
			}
		}
		else
		{
			System.out.println("That patient is either not in our database or already in a bed.");
		}
	}

	public static void removeICU(Connection connection)
	{
		System.out.println("Enter the health card # of the person to transfer out of an ICU unit: ");
		String healthCard = userInput.nextLine();
		String getDate = "SELECT date_release from ICU_Beds WHERE Health_Card = ?";
		String command1 = "DELETE FROM ICU_Beds WHERE Health_Card = ?";
		
		//Only proceed if the patient is actually in a bed
		if (searchHealthCard(connection, healthCard, "ICU_Beds"))
		{
			try
			{
				PreparedStatement dateStatement = connection.prepareStatement(getDate);
				PreparedStatement delStatement = connection.prepareStatement(command1);
				dateStatement.setString(1, healthCard);
				
				//Check if date of release is today; if not confirm choices
				ResultSet date = dateStatement.executeQuery();
				LocalDate dateR = null;
				while (date.next())
				{
					try
					{
						dateR = LocalDate.parse(date.getString(1));
					}
					catch (NullPointerException e)
					{
						dateR = LocalDate.now();
					}
				}
				LocalDate today = LocalDate.now();
				
				if (dateR.compareTo(today) > 0)
				{
					boolean loop = true;
					while (loop)
					{
						System.out.println("The scheduled release date for that patient is not today."
								+ " Are you sure you want to release this patient? (Yes or No)");
						String response = userInput.nextLine();
						if (response.equals("Yes"))
						{
							System.out.println("Very well");
							loop = false;
						}
						else if (response.equals("No"))
						{
							System.out.println("Returning to main menu then...");
							loop = false;
							return;
						}
						else
						{
							System.out.println("Invalid input, try again. ");
						}
					}
				}
				System.out.println("Proceeding with deletion...");
				delStatement.setString(1, healthCard);
				delStatement.execute();
				
				boolean loop = true;
				while (loop) {
					System.out.println("Do you want to make any changes to the prescriptions that the patient has? (Yes or No)");
					String input = userInput.nextLine();
					if (input.equals("Yes"))
					{
						try
						{
							String c1 = "SELECT Prescriptions FROM Patients WHERE Health_Card=?";
							PreparedStatement p1 = connection.prepareStatement(c1);
							p1.setString(1, healthCard);
							ResultSet result = p1.executeQuery();
							while (result.next())
							{
								System.out.println("Here is the prescription info:");
								System.out.println(result.getString(1));
							}
							System.out.println();
							
							boolean l = true;
							String c2 = "UPDATE Patients SET Prescriptions=? WHERE Health_Card=?";
							PreparedStatement p2 = connection.prepareStatement(c2);
							p2.setString(2, healthCard);
							while (l)
							{
								System.out.println("Enter the changed prescription info: ");
								String prescription = userInput.nextLine();
								if (prescription.length() > 100)
								{
									System.out.println("Too large; try again");
								}
								else if (prescription.strip().length() < 0)
								{
									p2.setNull(1, Types.NULL);
									l = false;
								}
								else
								{
									p2.setString(1, prescription);
									l = false;
								}
							}
							
							p2.execute();
							System.out.println("Prescription info saved.");
							loop = false;
						}
						catch (SQLException e)
						{
							System.out.println("Error in changing prescriptions");
							loop = false;
						}
					}
					else if (input.equals("No"))
					{
						System.out.println("Prescription unchanged");
						loop = false;
					}
					else
					{
						System.out.println("Invalid input");
					}
				}
				
				
			}
			catch (SQLException e)
			{
				System.out.println("Error in removing patient from ICU Unit.");
			}
		}
		
		else
		{
			System.out.println("That patient is not in the ICU database");
		}
		
		System.out.println("Returning to main menu... ");
		
	}
	
	public static void transferToBed(Connection connection)
	{
		System.out.println("Enter the health card of the patient to transfer from an ICU to a hospital bed");
		String healthCard = userInput.nextLine();
		if (!searchHealthCard(connection, healthCard, "ICU_Beds"))
		{
			System.out.println("That patient is not in the beds. ");
			return;
		}
		System.out.println("Transferring to ICU... ");
		try
		{
			String c = "INSERT INTO Normal_Beds (Health_Card, First_Name, Last_Name, Past_Medical_History, "
					+ "Allergic_Reactions, Medical_Conditions, Cause_Of_Entry) "
					+ "SELECT Health_Card, First_Name, Last_Name, Past_Medical_History, "
					+ "Allergic_Reactions, Medical_Conditions, Cause_Of_Entry "
					+ "FROM ICU_Beds WHERE Health_Card=?;";
			PreparedStatement pst = connection.prepareStatement(c);
			pst.setString(1, healthCard);
			
			Date entry = new Date(System.currentTimeMillis());
			String c2 = "UPDATE Normal_Beds SET date_entry = ?, severity=?, date_release = ? WHERE Health_Card = ?";
			PreparedStatement pst2 = connection.prepareStatement(c2);
			pst2.setDate(1, entry);
			pst2.setString(4, healthCard);
			
			boolean loop = true;
			while (loop)
			{
				System.out.println("On a scale of 1 to 10, give the severity of the patient's condition: ");
				try
				{
					int severity = userInput.nextInt();
					if (severity > 10 || severity < 1)
					{
						System.out.println("Outside the scale, try again");
					}
					else
					{
						pst2.setInt(2, severity);
						//System.out.println(severity);
						userInput.nextLine();
						loop = false;
					}
				}
				catch (InputMismatchException e)
				{
					System.out.println("That is not an integer, try again");
					userInput.nextLine();
				}
			}
			
			loop = true;
			while (loop)
			{
				System.out.println("Can you estimate a date of release yet? Enter how many days the patient will stay (or enter -1) if not sure)");
				try
				{
					int stay = userInput.nextInt();
					LocalDate dateR = LocalDate.now();
					if (stay < 0)
					{
						pst2.setNull(3, Types.NULL);
						loop = false;
					}
					else
					{
						dateR = dateR.plusDays(stay);
						String dateRelease = dateR.format(dateMaker);
						pst2.setDate(3, Date.valueOf(dateRelease));
						loop = false;
					}
					
				}
				catch (InputMismatchException e)
				{
					System.out.println("That is not an integer, try again");
					userInput.nextLine();
				}
			}
			
			System.out.println("Transferring to ICU...");
			pst.execute();
			pst2.execute();
			System.out.println("Freeing up hospital bed...");
			
			pst = connection.prepareStatement("DELETE FROM ICU_beds WHERE Health_Card=?;");
			pst.setString(1, healthCard);
			pst.execute();
			
			System.out.println("Successful transfer to ICU");
			
			
		}
		catch (SQLException e)
		{
			System.out.println("Error in tranferring patient to bed from ICU. ");
			e.printStackTrace();
		}
		finally{
			System.out.println("Returning to Main Menu");
		}
	}
}

package pkg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Install
 */
@WebServlet("/Install")
public class Install extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	ApplicationDB ap = new ApplicationDB();
	Connection c = ap.getConnection();
	Common lib = new Common();
	
    public Install() {
        super();
  
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("type") != null)
		{
			
			try {
			//DOES NOT WORK ANYMORE BECAUSE "ON UPDATE CASCADE", "ON DELETE CASCADE" NOT SET!!!!
			//int rowsAffected = generate_station_id();
				
			if (request.getParameter("type").equals("trains"))
				createTrains(Integer.parseInt(request.getParameter("trains")));
			else if (request.getParameter("type").equals("stops"))
				createStops();
			}
			catch (SQLException e)
			{
				response.getWriter().print("SQL error.");
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	private ResultSet getStations() throws SQLException
	{
		ApplicationDB ap = new ApplicationDB();
		Connection c = ap.getConnection();
		
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Stations");
		return ps.executeQuery();
	}
	private int generate_station_id() throws SQLException
	{
		String sql = "UPDATE Stations SET station_id = ? WHERE station_id = ?";
		ApplicationDB ap = new ApplicationDB();
		Connection c = ap.getConnection();
		try {
			ResultSet stations = getStations();
			PreparedStatement ps = c.prepareStatement(sql);
			String currentSID = "", newSID = "";
			String default_id = "";
			String station_name;
			int timesFound = 1;
			int affectedRows = 0;
			int totalRows = 0;
			
			while (stations.next())
			{
				//First we must clear ids so set them to first 10 letters of station name.
				
				station_name = stations.getString("station_name");
				if (station_name.length() < 10)
				{
					default_id = station_name.substring(0, station_name.length());
				}
				else
				{
					default_id = station_name.substring(0, 10);
				}
				currentSID = stations.getString("station_id");
				newSID = stations.getString("station_name").substring(0,3) + stations.getString("city").substring(0,3);
				newSID = newSID.toUpperCase() + String.format("%02d", timesFound);
				
				ps.setString(1, default_id);
				ps.setString(2, currentSID);
				ps.executeUpdate();
				
				while (findID(newSID).next())
				{
					timesFound++;
					newSID = newSID.substring(0,6);
					newSID = newSID.toUpperCase() + String.format("%02d", timesFound);
				}
				
				
				
				ps.setString(1, newSID);
				ps.setString(2, default_id);
				
				affectedRows = ps.executeUpdate();
				totalRows += affectedRows;
				
				timesFound = 1;
			}
			return totalRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private ResultSet findID(String find_id) throws SQLException
	{
		ApplicationDB ap = new ApplicationDB();
		Connection c = ap.getConnection();
		String sql = "SELECT station_id FROM Stations WHERE station_id = ?";
		
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, find_id);
		return ps.executeQuery();
	}
	
	private int createTrains(int t) throws SQLException
	{
		final int BASE_ID = 1000;
		int TRAINS_PER_LINE = t;
		
		
		int[] car = {32, 48, 40};
		int[] seats = {147, 132, 100};
		
		ApplicationDB ap = new ApplicationDB();
		Connection c = ap.getConnection();
		ServletContext sql_settings = getServletContext();
		
		String settings = sql_settings.getInitParameter("safe-update-off");
		String delete_trains = "DELETE FROM Trains;";
		String insert_train = "INSERT INTO Trains (t_id, num_cars, num_seats, transitLine_name) VALUES (?,?,?,?);";
		
		ResultSet transitLines = getTransitLines();
		
		int current_line = 1, current_train = 0, current_train_cars = 0, current_train_seats = 0;
		int id;
		String current_transit_line;
		
		PreparedStatement settings_off = c.prepareStatement(settings);
		settings_off.executeQuery();
		PreparedStatement next_statement = c.prepareStatement(delete_trains);
		next_statement.executeUpdate();
		
		while (transitLines.next())
		{
			current_transit_line = transitLines.getString(1); //Get current transit line name
			id = BASE_ID * current_line; //1000 * 2 = #000
			current_line++;
			
			for (int i = 0; i < TRAINS_PER_LINE; i++)
			{
				current_train = i + 1; //1 through range of trains
				id+=current_train; 
				current_train_cars = car[(int) (Math.random() * 3)];
				current_train_seats = seats[(int) (Math.random() * 3)];
				
				next_statement = c.prepareStatement(insert_train);
				next_statement.setString(1, Integer.toString(id));
				next_statement.setString(2, Integer.toString(current_train_cars));
				next_statement.setString(3, Integer.toString(current_train_cars * current_train_seats));
				next_statement.setString(4, current_transit_line);
				
				next_statement.executeUpdate();
				id-=current_train;
			}
		}
		return 1;
	}
	
	private ResultSet getTransitLines() throws SQLException
	{
		ApplicationDB ap = new ApplicationDB();
		Connection c = ap.getConnection();
		PreparedStatement ps = c.prepareStatement("SELECT name FROM Transit_Lines ORDER BY name ASC");
		return ps.executeQuery();
	}
	private void setUp() throws SQLException, ParseException
	{
		
		String sql0 = "SELECT arrive_time FROM Stops ORDER BY arrive_time DESC LIMIT 1";
		PreparedStatement check = c.prepareStatement(sql0);
		ResultSet rs = check.executeQuery();
		rs.next();
		//String lastEntry = rs.getString(1);
		//lastEntry = lastEntry.substring(0,10);
		
//		if (lib.greaterThanDate(lib.getToday(), lastEntry))
//		{
//			System.out.println("Will not add any new entries because SQLException will be thrown for duplicate entry");
//		}
		
		ServletContext cxt = getServletContext();
		
		String sql1 = cxt.getInitParameter("safe-update-off");
		PreparedStatement ps = c.prepareStatement(sql1);
		ps.executeQuery();
		
		String sql2 = "ALTER TABLE Stops DROP FOREIGN KEY nextStop_id";
		ps = c.prepareStatement(sql2);
		ps.executeUpdate();
		
		String sql3 = "DELETE FROM Stops";
		ps = c.prepareStatement(sql3);
		ps.executeUpdate();
		
		
		
		//Removes safe mode.
		//Drops foreign key
		//Deletes all contents
		//Adds foreign key
	}
	private void close() throws SQLException
	{
		String sql = "ALTER TABLE Stops " + 
				"ADD CONSTRAINT nextStop_id " + 
				"FOREIGN KEY (nextStop_id) REFERENCES Stops(stop_id)";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.executeUpdate();
	}
	private void createStops() throws SQLException, ParseException
	{
		setUp();
		final String SQL_INSERT = "INSERT INTO Stops (stop_id, station_id, arrive_time, train_id, position, nextStop_id)"
				+ " VALUES (?,?,?,?,?,?)";
		PreparedStatement ps = c.prepareStatement(SQL_INSERT);
		final String TRAIN_START_TIME = "05:00"; //Trains start running at 5 AM
		final String TRAIN_STOP_TIME = "23:59";  //All trains stop running at 11:59 PM
		final int TRANSPORT_MINUTES = 7; //Wait at station for 2 minutes. Then go to next one takes 5 minutes. Total time 7 min.
		final int NEXT_TRAIN_MINUTES = 30;//Every 30 minutes another train starts at the origin.
		
		//stop_id: generate from line name + position
		//station_id: get from id_from_name
		//arrive_time: TBD
		//nextStop_id: station_order.value[i+1] unless it's null..
		//train_id: get_trains_for_line()
		//position = station_order index do some fancy calculations
		
		ArrayList<Integer> trains;
		Map<String, String[]> station_order_for_line = initialize_list();
		Set<String> line_names = station_order_for_line.keySet();
		String[] station_order;
		String today = lib.getToday();
		int turn = 1;
		
		//Parameter list
		int position = 1, train_id;
		String stop_id, station_id, nextStop_id;
		String arrive_time;
		
		//Temp storage
		int totalTime;
		String current_time, next_station_time;
		
		//Should run for each transit line
			for (String line_name : line_names)
			{
				
				current_time = TRAIN_START_TIME;											//Always generate schedule starting at 5:00 AM
				
				trains = get_trains_for_line(line_name);									//Get list of trains that can run this line
				station_order = station_order_for_line.get(line_name);						//Get list of stations they need to stop at
				totalTime = TRANSPORT_MINUTES * station_order.length;			//Calculate the total time a train runs through a line
				do																			//Check if train will run past day ends
				{
					train_id = next_available_train(trains, current_time, turn);
					//Generate stops for 1 train
					if (train_id != -1)				//This means there are available trains
					{
						next_station_time = current_time;
						if (turn == 1)
						{
							//Runs through all stations except destination station.
							for (int j = 0; j < station_order.length; j++)
							{
								position = j+1;										//Position = station_list index + 1
								
								stop_id = lib.getInitials(line_name);					//Begin by taking initials of the transit line
								stop_id += String.format("%02d", position);			//Add on position number for stop ID
								
								station_id = id_from_name(station_order[j]);		//Station id fetched from station name
								if (j == station_order.length - 1)
								{
									nextStop_id = null;
								}
								else
								{
									nextStop_id = lib.getInitials(line_name);				
									nextStop_id+= String.format("%02d", position + 1);
								}
								
								arrive_time = lib.getSqlDate(today, next_station_time);	//Generate date for SQL
								
								next_station_time = lib.addTime(next_station_time, TRANSPORT_MINUTES);
								
								ps.setString(1, stop_id);
								ps.setString(2, station_id);
								ps.setString(3, arrive_time);
								ps.setString(4, Integer.toString(train_id));
								ps.setString(5, Integer.toString(position));
								ps.setString(6, nextStop_id);
								
								ps.executeUpdate();
								
								turn = 2;
							}
						}
						else if (turn == 2)
						{
							//Handle destination back to start from here
							for (int j = station_order.length; j > 0; j--)
							{
								position = j;
								
								stop_id = lib.getInitials(line_name);
								stop_id += String.format("%02d", position);
								
								station_id = id_from_name(station_order[j - 1]);
								if (j == 1)
								{
									nextStop_id = null;
								}
								else
								{
									nextStop_id = lib.getInitials(line_name);
									nextStop_id += String.format("%02d", position - 1);
								}
								arrive_time = lib.getSqlDate(today, next_station_time);
								
								next_station_time = lib.addTime(next_station_time, TRANSPORT_MINUTES);
								
								ps.setString(1, stop_id);
								ps.setString(2, station_id);
								ps.setString(3, arrive_time);
								ps.setString(4, Integer.toString(train_id));
								ps.setString(5, Integer.toString(position));
								ps.setString(6, nextStop_id);
								
								ps.executeUpdate();
								
								turn = 1;
							}
						}
					}
					if (turn == 1)
					{
						current_time = lib.addTime(current_time, NEXT_TRAIN_MINUTES);
					}
				} while (can_run(current_time, TRAIN_STOP_TIME, totalTime));
			}
		close();
	}
	
	
	private ArrayList<Integer> get_trains_for_line(String line_name) throws SQLException
	{
		PreparedStatement p = c.prepareStatement("SELECT t_id FROM Trains WHERE transitLine_Name = ?");
		p.setString(1, line_name);
		ResultSet rs = p.executeQuery();
		ArrayList<Integer> names = new ArrayList<Integer>();
		while (rs.next())
		{
			names.add(rs.getInt(1));
		}
		return names;
	}
	
	private Map<String, String[]> initialize_list() throws SQLException
	{
		final String[] NEC = {
				"Trenton",
				"Hamilton",
				"Princeton Junction",
				"Jersey Avenue",
				"New Brunswick",
				"Edison",
				"Metuchen",
				"Metropark",
				"Rahway",
				"Linden",
				"Elizabeth",
				"North Elizabeth",
				"Newark Int'l Airport",
				"Newark Penn Station",
				"Secaucus Junction",
				"New York",
		};
		final String[] NJCL = {
				"Bay Head",
				"Point Pleasant Beach",
				"Manasquan",
				"Spring Lake",
				"Belmar",
				"Bradley Beach",
				"Asbury Park",
				"Allenhurst",
				"Elberon",
				"Long Branch",
				"Monmouth Park",
				"Little Silver",
				"Red Bank",
				"Middletown",
				"Hazlet",
				"Aberdeen-Matawan",
				"South Amboy",
				"Perth Amboy",
				"Woodbridge",
				"Avenel",
				"Rahway",
				"Linden",
				"Elizabeth",
				"North Elizabeth",
				"Newark Int'l Airport",
				"Newark Penn Station",
				"Secaucus Junction",
				"New York",
		};
		final String[] ACL = {
				"Atlantic City",
				"Absecon",
				"Egg Harbor City",
				"Hammonton",
				"Atco",
				"Lindenwold",
				"Cherry Hill",
				"Pennsauken Transit Center",
				"Philadelphia 30th St."
		};
		
		ArrayList<String> transit_line_names = lib.get_transit_line_names();
		Map<String, String[]> station_order = new HashMap<String, String[]>();
		station_order.put(transit_line_names.get(0), ACL);
		station_order.put(transit_line_names.get(1), NJCL);
		station_order.put(transit_line_names.get(2), NEC);
		
		return station_order;
	}
	
	private String id_from_name(String name) throws SQLException
	{
		String SQL = "SELECT station_id FROM Stations WHERE station_name = ?";
		PreparedStatement ps = c.prepareStatement(SQL);
		ps.setString(1, name);
		ResultSet id = ps.executeQuery();
		if (id.next())
			return id.getString(1);
		else
			return null;
	}

	private boolean can_run(String t1, String latest_time, int minutes) throws ParseException
	{
		SimpleDateFormat d = new SimpleDateFormat("HH:mm");
		d.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		Date d1 = new Date();
		Date d2 = new Date();
		Date cannot_exceed = new Date();
		
		d1 = d.parse(t1);								//Original Time as date
		cannot_exceed = d.parse(latest_time);			//Cannot exceed time
		
		String finalTime = lib.addTime(t1, minutes);		//Add minutes to original time
		d2 = d.parse(finalTime);						//New time as date
		
		if (d1.after(d2))								//If new time is somehow before original time - new day occurred
			return false;
		else 
		{
			if (cannot_exceed.after(d1))
				return true;
			else
				return false;
		}
	}
	

	private int next_available_train(ArrayList<Integer> ids, String time, int turn) throws SQLException, ParseException
	{
		SimpleDateFormat d = new SimpleDateFormat("HH:mm");
		d.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String trainLocation;
		if (turn == 1)
		{
			trainLocation = "destination_id";
		}
		else
		{
			trainLocation = "origin_id";
		}
		String SQL = "SELECT arrive_time, station_id FROM Stops WHERE train_id = ? AND nextStop_id IS NULL ORDER BY arrive_time DESC";
		String getBadStation = "SELECT tr." + trainLocation +
				" FROM Transit_Lines tr, Trains t "
				+ "WHERE t.transitLine_Name = tr.name "
				+ "AND t.t_id = ?";
		
		PreparedStatement ps = c.prepareStatement(SQL);
		PreparedStatement ps2 = c.prepareStatement(getBadStation);
		ps2.setString(1, ids.get(0).toString());
		ResultSet times;
		ResultSet badStationRS = ps2.executeQuery();
		badStationRS.next();
		String cannotBeHere = badStationRS.getString(1);
		String trainHereLast;
		Time t;
		String arr;
		Date given_time = new Date();
		given_time = d.parse(time);
		Date found_time = new Date();
		
		for (Integer id : ids)
		{
			ps.setString(1, id.toString());
			times = ps.executeQuery();
			boolean hasNext = times.next();
			
			if (!hasNext)
			{
				return id;
			}
			else
			{
				trainHereLast = times.getString("station_id");
				if (!trainHereLast.equals(cannotBeHere))
				{
					t = times.getTime("arrive_time");
					arr = t.toString();
					arr = arr.substring(0,5);
					arr = lib.addTime(arr, 300);
					found_time = d.parse(arr);
				
//					System.out.println(d.format(found_time));
//					System.out.println(d.format(given_time));
//					System.out.println(given_time.after(found_time));
				
					if (given_time.after(found_time))
					{
						return id;
					}
				}
			}
		}
		return -1;
	}
}

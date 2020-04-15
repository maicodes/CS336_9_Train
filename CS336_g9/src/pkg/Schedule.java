package pkg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pkg.Models.Stop;
import pkg.Models.Train;

/**
 * Servlet implementation class Schedule
 */
@WebServlet("/Schedule")
public class Schedule extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ApplicationDB ap = new ApplicationDB();
	Connection c = ap.getConnection();
	Common lib = new Common();
	
	//INSERT INTO Stops (stop_id, station_id, arrive_time, train_id, position)
	//VALUES ("ACL01", "ATLATL01", "2020-04-01 12:00:00", 1001, 1);
	
//	SELECT sta.station_name, sto.nextStop_id, sto.arrive_time, sto.stop_id, sto.position
//	FROM Stations sta, Stops sto
//	WHERE sta.station_id = sto.station_id
//	AND train_id = 1003
//	ORDER BY arrive_time DESC;
	
	public Schedule() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("origin") != null && request.getParameter("destination") != null)
		{
			//bad design code but works don't care to fix at the moment
			String origin = request.getParameter("origin").replaceAll("(.)([A-Z0-9])", "$1 $2");
			if (origin.equals("Aberdeen- Matawan")) origin = "Aberdeen-Matawan";
			String destination = request.getParameter("destination").replaceAll("(.)([A-Z0-9])", "$1 $2");
			if (destination.equals("Aberdeen- Matawan")) destination = "Aberdeen-Matawan";
			
			try {
				request.setAttribute("trains", get_trains(transit_line_for_stations(origin, destination)));
				request.setAttribute("forward-run", forward_run(origin, destination));
				request.setAttribute("origin", origin);
				request.setAttribute("destination", destination);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		else if (request.getParameter("origin") != null)
		{
			String name = request.getParameter("origin").replaceAll("(.)([A-Z0-9])", "$1 $2");
			if (name.equals("Aberdeen- Matawan")) name = "Aberdeen-Matawan";
			try {
				request.setAttribute("stations", destinations(name));
				request.setAttribute("selection", "Destination");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		else if (request.getParameter("line") == null)
		{
			try {
				ArrayList<String> transit_line_names = lib.get_transit_line_names();
				request.setAttribute("transit_line_names", transit_line_names);
				
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		
		}
		else {
			if (request.getParameter("line").equals("all"))
			{
				try {
					request.setAttribute("stations", get_stations());
					request.setAttribute("selection", "Origin");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			else
			{
				String name = request.getParameter("line").replaceAll("(.)([A-Z0-9])", "$1 $2");
				
				try {
					ArrayList<Train> trains_for_line = get_trains(name);
					
					request.setAttribute("trains", trains_for_line);
					request.setAttribute("line_name", name);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		request.getRequestDispatcher("schedule.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public ArrayList<Train> get_trains(String line) throws SQLException {
		ArrayList<Train> trains = new ArrayList<Train>();
		ArrayList<Stop> trips = new ArrayList<Stop>();
		
		String sql = "SELECT *" + 
				"FROM Trains t " + 
				"WHERE t.t_id IN " + 
				"(SELECT t_id FROM Stops s WHERE s.train_id = t.t_id) " + 
				"AND t.transitLine_Name = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, line);
		
		ResultSet rs = ps.executeQuery();
		while (rs.next())
		{
			Train t = new Train();
			t.setT_id(rs.getInt("t_id"));
			t.setTransit_line(rs.getString("transitLine_Name"));
			t.setCars(rs.getInt("num_cars"));
			t.setSeats(rs.getInt("num_seats"));
			
			trips = get_trips(t.getT_id());
			Collections.reverse(trips);
			if (!trips.isEmpty())
			{
				for (Stop s : trips)
				{
					t.add_trip(s);
				}
			}
			trains.add(t);
		}
		return trains;
	}
	
	private ArrayList<Stop> get_trips(int id) throws SQLException
	{
		ArrayList<Stop> unlinkedTrips = new ArrayList<Stop>();
		ArrayList<Stop> linkedTrips = new ArrayList<Stop>();
		String SQL =
				"SELECT sta.station_name, sto.nextStop_id, sto.arrive_time, sto.stop_id, sto.position "
				+ "FROM Stations sta, Stops sto "
				+ "WHERE sta.station_id = sto.station_id "
				+ "AND train_id = ? "
				+ "ORDER BY arrive_time DESC;";
		PreparedStatement ps = c.prepareStatement(SQL);
		ps.setString(1, Integer.toString(id));
		ResultSet stops = ps.executeQuery();
		
		String nextStop;
		while (stops.next())
		{
			nextStop = stops.getString("nextStop_id");
			if ((nextStop == null && !unlinkedTrips.isEmpty()))
			{
				for (int i = unlinkedTrips.size() - 1; i > 0; i--)
				{
					unlinkedTrips.get(i).set_nextStop(unlinkedTrips.get(i - 1));
				}
				linkedTrips.add(unlinkedTrips.get(unlinkedTrips.size() - 1));
				unlinkedTrips.clear();
			}
				Stop s = new Stop();
				s.set_stop_id(stops.getString("stop_id"));
				s.set_station_name(stops.getString("station_name"));
				s.set_arrive_time(lib.stripTime(stops.getString("arrive_time")));
				s.set_position(stops.getInt("position"));
				
				if (stops.isFirst())
				{
					s.lastOfLine(true);
				}
				
				unlinkedTrips.add(s);
			if (stops.isLast())
			{
				for (int i = unlinkedTrips.size() - 1; i > 0; i--)
				{
					unlinkedTrips.get(i).set_nextStop(unlinkedTrips.get(i - 1));
				}
				linkedTrips.add(unlinkedTrips.get(unlinkedTrips.size() - 1));
			}
		}
		
		return linkedTrips;
	}
	
	private ArrayList<String> get_stations() throws SQLException
	{
		ArrayList<String> stations = new ArrayList<String>();
		PreparedStatement ps = c.prepareStatement("SELECT st.station_name " + 
				"FROM Stops s, Stations st " + 
				"WHERE s.station_id = st.station_id " + 
				"GROUP BY s.station_id");
		
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			stations.add(rs.getString(1));
		}
		
		return stations;
	}
	
	private ArrayList<String> destinations(String origin) throws SQLException
	{
		ArrayList<String> stations = new ArrayList<String>();
		String SQL = "SELECT st2.station_name " + 
				"FROM Trains t2, Stops s2, Stations st2 " + 
				"WHERE t2.t_id = s2.train_id " + 
				"AND st2.station_id = s2.station_id " +
				"AND st2.station_name <> ? " +
				"AND t2.transitLine_Name IN " + 
				"(SELECT t.transitLine_Name " + 
				"FROM Stops s, Stations st, Trains t " + 
				"WHERE s.station_id = st.station_id " + 
				"AND t.t_id = s.train_id " + 
				"AND st.station_name = ? " + 
				"GROUP BY transitLine_Name) " + 
				"GROUP BY station_name";
		PreparedStatement ps = c.prepareStatement(SQL);
		ps.setString(1, origin);
		ps.setString(2, origin);
		ResultSet rs = ps.executeQuery();
		while (rs.next())
		{
			stations.add(rs.getString(1));
		}
		return stations;
	}
	
	private String transit_line_for_stations(String origin, String destination) throws SQLException
	{
		String SQL = "SELECT DISTINCT station_name, Trains.transitLine_Name " + 
				"FROM Stops " + 
				"JOIN Stations " + 
				"ON Stops.station_id = Stations.station_id " + 
				"JOIN Trains " + 
				"ON Trains.t_id = Stops.train_id " + 
				"WHERE station_name = ? OR station_name = ?;";
		PreparedStatement ps = c.prepareStatement(SQL);
		ps.setString(1, origin);
		ps.setString(2, destination);
		
		ResultSet rs = ps.executeQuery();
		ArrayList<String> potential_lines = new ArrayList<String>();
		String line;
		
		while (rs.next())
		{
			if (potential_lines.contains(rs.getString("transitLine_Name")))
			{
				line = rs.getString("transitLine_Name");
				return line;
			}
			else
			{
				potential_lines.add(rs.getString("transitLine_Name"));
			}
		}
		
		String fallback = "SELECT DISTINCT Trains.transitLine_Name " + 
				"FROM Trains " + 
				"JOIN Stops " + 
				"ON Stops.train_id = Trains.t_id " + 
				"JOIN Stations " + 
				"ON Stations.station_id = Stops.station_id " + 
				"WHERE station_name = ?";
		
		ps = c.prepareStatement(fallback);
		ps.setString(1, origin);
		rs = ps.executeQuery();
		rs.next();
		return rs.getString(1);
	}
	
	private boolean forward_run(String origin, String destination) throws SQLException
	{
		String SQL = "SELECT DISTINCT position " + 
				"FROM Stops " + 
				"JOIN Stations " + 
				"ON Stations.station_id = Stops.station_id " + 
				"WHERE Stations.station_name = ?;";
		PreparedStatement stmt = c.prepareStatement(SQL);
		
		stmt.setString(1, origin);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		int origin_position = rs.getInt(1);
		
		stmt.setString(1, destination);
		rs = stmt.executeQuery();
		rs.next();
		int destination_position = rs.getInt(1);
		
		return destination_position > origin_position;
	}
}

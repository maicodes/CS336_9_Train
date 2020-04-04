package pkg;

import java.io.IOException;
import java.io.PrintWriter;
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
		
		//The plan is to just fetch all the transit lines and let the user pick a schedule from them.
		//Not very familiar with MVC design principles, but I will not create a model for this
		//because all I'm really doing is fetching the names of every transit line and storing it in
		//maybe an array or ArrayList.
		if (request.getParameter("line") == null)
		{
			try {
				ArrayList<String> transit_line_names = lib.get_transit_line_names();
				request.setAttribute("transit_line_names", transit_line_names);
				
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		
		request.getRequestDispatcher("schedule.jsp").forward(request, response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
		}
		else {
			PrintWriter ps = response.getWriter();
			String name = request.getParameter("line").replaceAll("(.)([A-Z])", "$1 $2");
			
			try {
				ArrayList<Train> trains_for_line = get_trains(name);
				
				request.setAttribute("trains", trains_for_line);
				request.setAttribute("line_name", name);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		request.getRequestDispatcher("schedule.jsp").forward(request, response);
		}
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
	
}

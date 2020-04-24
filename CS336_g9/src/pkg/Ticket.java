package pkg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pkg.Models.Fare;

/**
 * Servlet implementation class Ticket
 */
@WebServlet("/Ticket")
public class Ticket extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ApplicationDB ap = new ApplicationDB();
	Connection c = ap.getConnection();
	Common lib = new Common();
       
    public Ticket() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("origin") == null || request.getParameter("destination") == null)
		{
			response.sendRedirect("Schedule?line=all");
		}
		else
		{
			{
			String origin = request.getParameter("origin").replaceAll("(.)([A-Z0-9])", "$1 $2");
			if (origin.equals("Aberdeen- Matawan")) origin = "Aberdeen-Matawan";
			String destination = request.getParameter("destination").replaceAll("(.)([A-Z0-9])", "$1 $2");
			if (destination.equals("Aberdeen- Matawan")) destination = "Aberdeen-Matawan";
			
			int distance = 0;
			String line = "";
			Fare pricing;
			try {
				distance = distance(origin,destination);
				line = lib.transit_line_for_stations(origin, destination);
				pricing = new Fare(line);
				request.setAttribute("pricing", pricing);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			request.setAttribute("origin", origin);
			request.setAttribute("destination", destination);
			request.setAttribute("distance", distance);
			
			request.getRequestDispatcher("ticket.jsp").forward(request, response);
			}
		}
	}
	
	
	private int distance(String origin, String destination) throws SQLException {
		int pos1, pos2;
		
		String SQL = 
		"SELECT DISTINCT position " + 
		"FROM Stops " + 
		"JOIN Stations " + 
		"ON Stops.station_id = Stations.station_id " + 
		"WHERE station_name = ? " + 
		"OR station_name = ?";
		
		PreparedStatement ps = c.prepareStatement(SQL);
		ps.setString(1, origin);
		ps.setString(2, destination);
		
		ResultSet rs = ps.executeQuery();
		
		if (!rs.next())
			return 0;
		
		pos1 = rs.getInt(1);
		if (!rs.next())
			return 0;
		pos2 = rs.getInt(1);
		
		return Math.abs(pos1 - pos2);
	}
	
}

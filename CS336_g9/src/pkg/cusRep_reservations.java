package pkg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class cusRep_reservations
 */
@WebServlet("/cusRep_reservations")
public class cusRep_reservations extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public cusRep_reservations() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(req.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		
		
		ApplicationDB ap = new ApplicationDB();
		Connection con = ap.getConnection();
		
		if(con == null) {
			System.out.print("Cannot connect to database");
			return;
		}
		
		if(req.getParameter("type").equals("add")) {
			// Create a reservation for a customer
			Map<String, String[]> params = req.getParameterMap();
			
			// get fees
			Map<String, Double> fees = Common.getFees(null, params.get("origin-stop")[0], con);
			
			// get weekly/ monthly discount if it is a weekly/monthly fare
			double otherDiscount = 0.0;
			if (params.get("fare")[0].equals("weekly") || params.get("fare")[0].equals("monthly")) {
				otherDiscount = Common.getMonthlyWeeklyDiscount(fees.get("fareID").intValue(), params.get("fare")[0].equals("weekly"), con) ;
			}
			
			
			// Calculates total fare and total discount
			double totalFare = fees.get("farePrice") + fees.get("bookingFee");
			double totalDiscount = 0.0;
			
			if()
			
			
			
		} else if (req.getParameter("type").equals("edit")) {
			// Edit a reservation
		} else {
			// Delete a reservation
		}
	}

}

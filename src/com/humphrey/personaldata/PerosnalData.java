package com.humphrey.personaldata;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;



@WebServlet("/PersonalData")
@MultipartConfig(fileSizeThreshold =500* 1024 * 1024, maxFileSize = 500 * 1024 * 1024, maxRequestSize = 5 * 500 * 1024 * 1024)
public class PerosnalData extends HttpServlet{
	
	String insertData = "INSERT INTO IMG(IMGNO,IMGNAME, IMAGE) VALUES(IMG_SEQ.NEXTVAL,?,?)";
	Connection con;
	PreparedStatement pstmt;
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException{
		
		req.setCharacterEncoding("Big5");
		res.setContentType("text/html; charset=Big5");
		PrintWriter out = res.getWriter();
		
		String filename = req.getParameter("filename");
		
		
		
		Part part = req.getPart("upfile");
		InputStream upfile = part.getInputStream();
		
		try {
			pstmt = con.prepareStatement(insertData);
			pstmt.setString(1, filename);
			System.out.println("filename= " + filename);
			pstmt.setBinaryStream(2, upfile, upfile.available());
			pstmt.executeUpdate();
			
			out.println("picture is finish!!");
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			try{
				pstmt.close();
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
			
		}
		
		
		
	}
	public void init()	throws ServletException{
		try{
			javax.naming.Context ctx = new javax.naming.InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/WEBSERVERDB");
			con = ds.getConnection();
		}catch(Exception e){
			throw new UnavailableException("Couldn't get db connection");
		}
	}

}

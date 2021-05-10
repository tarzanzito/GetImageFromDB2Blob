package candal.app.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class DB2wrapper {
	
    private String _defaultFolder = null;
    private String _fileSeparator = null;
    private Connection _connection = null;
    private String _url = null;
    private String _userId = null;
    private String _userPwd = null;
    private String _schema = null;
    
    public DB2wrapper(String url, String userId, String userPwd, String schema, String outputFolder) {

    	_url = url;
    	_userId = userId;
    	_userPwd = userPwd;
    	_schema = schema;
    	_defaultFolder = outputFolder;
    	
		_fileSeparator = System.getProperty("file.separator");
    }
    
	private void open() throws ClassNotFoundException, SQLException {
		
        //Load class into memory
		Class.forName("com.ibm.db2.jcc.DB2Driver");
        
        //Establish connection
		_connection = DriverManager.getConnection(_url, _userId, _userPwd);
		//_connection.setAutoCommit(false);
	}

	private void close() throws SQLException {
	
		if (_connection == null)
			return;
		
		_connection.close();
	}

	public ImagesFullFileName getAllImages(String regID) throws SQLException, IOException, ClassNotFoundException {

		if (_connection == null)
			open();
		
		//SQL - Inquire DB2
    	String SQL = "SELECT IMAGEM1, IMAGEM2, IMAGEM3 "
				+ "FROM " + _schema + ".MY_TABLE_IMAGENS "
				+ "WHERE IMAGE_ID = '" + regID + "'";
        
        Statement stmt = _connection.createStatement();

        java.sql.ResultSet rs = stmt.executeQuery(SQL);

		if (!rs.next()) {
			rs.close();
			close();
			return null;
		}
        
    	String img0 = saveImageToDisk("IMAGEM1", "jpg", rs);
    	String img1 = saveImageToDisk("IMAGEM2", "jpg", rs); 
    	String img2 = saveImageToDisk("IMAGEM3", "tiff", rs); 

        ImagesFullFileName imagesFullFileName = new ImagesFullFileName();
        imagesFullFileName.setImageFullFileName(0, img0);
        imagesFullFileName.setImageFullFileName(1, img1);
        imagesFullFileName.setImageFullFileName(2, img2);
        
		close();
        
        return imagesFullFileName;
	}

	private String saveImageToDisk(String columnName, String extension, java.sql.ResultSet rs) throws SQLException, IOException {
		
		   java.sql.Blob blob = rs.getBlob(columnName);
	       long blobLength = blob.length();
	        
	       if (blobLength == 0)
	    	   return "";
	       
	       int pos = 1; // getBytes begin in 1
	       byte[] bytes = blob.getBytes(pos, (int) blobLength);
	        
	       //write like txt
	       //String fullFileName = _defaultFolder + _fileSeparator + columnName + ".txt";
	       //OutputStream outputStream = new FileOutputStream(fullFileName);
	       //outputStream.write(bytes);
	       //outputStream.flush();
	       //outputStream.close();

	       //base64 to bin
	       String str = new String(bytes);
	       byte[] decodedValue = Base64.getDecoder().decode(str);

	       //write jpg
	       String fullFileName = _defaultFolder + _fileSeparator + columnName + "." + extension;
	       OutputStream outputStream = new FileOutputStream(fullFileName);
	       outputStream.write(decodedValue);
	       outputStream.flush();
	       outputStream.close(); 
	       
	       return fullFileName;
	}
}


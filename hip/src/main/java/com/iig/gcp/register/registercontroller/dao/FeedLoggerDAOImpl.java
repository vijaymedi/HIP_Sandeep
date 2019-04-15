package com.iig.gcp.register.registercontroller.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.iig.gcp.register.registercontroller.dto.*;
import com.iig.gcp.hip.hipcontroller.utils.*;

@Component
public class FeedLoggerDAOImpl implements FeedLoggerDAO {

	@Autowired
	private ConnectionUtils ConnectionUtils;
	private static String SPACE = " ";
	private static String COMMA = ",";
	private static String QUOTE = "\'";
	private static String TABLE_NAME = "logger_master";

	@Override
	public void loadFeed(List<FeedLoggerDTO> dataLoggerCollection) throws Exception, SQLException {

		Connection conn = null;
		String pstm3 = null;
		String insertFeedLoggerQuery = null;
		ArrayList<String> distf = new ArrayList<String>();

		try {

			conn = ConnectionUtils.getConnection();
			for (FeedLoggerDTO feedLogger : dataLoggerCollection) {
				distf.add(feedLogger.getFeed_id());
			}
			System.out.println(distf);
			Set<String> distinctfeed = new HashSet<String>(distf);
			System.out.println(distinctfeed);
			for (String feed : distinctfeed) {
				pstm3 = "delete from " + TABLE_NAME + " where feed_id='" + feed + "'";
				Statement statement1 = conn.createStatement();
				statement1.executeUpdate(pstm3);
			}

			for (FeedLoggerDTO feedLogger : dataLoggerCollection) {
				insertFeedLoggerQuery = "INSERT INTO " + SPACE + TABLE_NAME + SPACE
						+ "(feed_id,classification,subclassification,value)" + "VALUES" + "(" + QUOTE
						+ feedLogger.getFeed_id() + QUOTE + COMMA + QUOTE + feedLogger.getClassification() + QUOTE
						+ COMMA + QUOTE + feedLogger.getSubclassification() + QUOTE + COMMA + QUOTE
						+ feedLogger.getValue() + QUOTE + ")";

				Statement statement = conn.createStatement();
				statement.execute(insertFeedLoggerQuery);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			conn.close();

		}
	}

}
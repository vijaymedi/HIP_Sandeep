package com.iig.gcp.lineage.lineageservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iig.gcp.hip.hipcontroller.utils.ConnectionUtils;
import com.iig.gcp.lineage.lineagecontroller.dto.FeedDetailDTO;

@Service
public class LineageServiceImpl implements LineageService {

	@Autowired
	private ConnectionUtils ConnectionUtils;
	LineageServiceImpl lineageServiceImpl;

	/*************************
	 * Get available EIMs
	 *******************************************************************************/

	@Override
	/**
	 return LinkedList<String>
	 */
	public LinkedList<String> getEIM() throws SQLException, Exception {
		LinkedList<String> arr = new LinkedList<String>();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		try {
			connection = ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"SELECT DISTINCT concat(concat(SYSTEM_EIM,'#'),SYSTEM_NAME) FROM JUNIPER_SYSTEM_MASTER");
			rs = pstm.executeQuery();
			while (rs.next()) {
				arr.add(rs.getString(1));
				// .put(rs.getInt(1), rs.getString(2));
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw e;
		} finally {
			if (pstm != null)
				pstm.close();
			if (rs != null)
				rs.close();
			if (connection != null)
				connection.close();

		}
		return arr;

	}

	/*********************************
	 * Get all feed details for eim
	 ********************************************/

	@Override
	/**
	 return LinkedList<FeedDetailDTO> 
	 */
	public LinkedList<FeedDetailDTO> getFeedDetfromEIM(String eim) throws SQLException, Exception {
		LinkedList<FeedDetailDTO> arr = null;
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		try {

			connection = ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"SELECT DISTINCT f.FEED_SEQUENCE , f.FEED_UNIQUE_NAME, p.PROJECT_ID, sys_src.SYSTEM_EIM,sys_tgt.SYSTEM_EIM\r\n"
							+ "FROM JUNIPER_EXT_FEED_MASTER f\r\n"
							+ "inner join JUNIPER_EXT_FEED_SRC_TGT_LINK st on\r\n"
							+ "f.FEED_SEQUENCE=st.FEED_SEQUENCE\r\n" + "inner join JUNIPER_PUB_FEED_DTLS pub on\r\n"
							+ "f.FEED_SEQUENCE=pub.EXT_FEED_SEQUENCE \r\n"
							+ "inner join (SELECT * from JUNIPER_PUB_FEED_STATUS  WHERE UPPER(STATUS)='SUCCESS')pub_status\r\n"
							+ "on pub.PUB_FEED_SEQUENCE=pub_status.PUB_FEED_SEQUENCE\r\n"
							+ "left join JUNIPER_EXT_SRC_CONN_MASTER s on\r\n"
							+ "f.PROJECT_SEQUENCE=s.PROJECT_SEQUENCE and st.SRC_CONN_SEQUENCE=s.SRC_CONN_SEQUENCE\r\n"
							+ "left join JUNIPER_EXT_TARGET_CONN_MASTER t on\r\n"
							+ "f.PROJECT_SEQUENCE=t.PROJECT_SEQUENCE and st.TARGET_SEQUENCE=t.TARGET_CONN_SEQUENCE\r\n"
							+ "inner join JUNIPER_PROJECT_MASTER p on\r\n"
							+ "f.PROJECT_SEQUENCE=p.PROJECT_SEQUENCE and s.PROJECT_SEQUENCE=p.PROJECT_SEQUENCE and t.PROJECT_SEQUENCE=p.PROJECT_SEQUENCE\r\n"
							+ "inner join JUNIPER_SYSTEM_MASTER sys_src on\r\n"
							+ "s.SYSTEM_SEQUENCE= sys_src.SYSTEM_SEQUENCE \r\n"
							+ "inner join JUNIPER_SYSTEM_MASTER sys_tgt on\r\n"
							+ "t.SYSTEM_SEQUENCE=sys_tgt.SYSTEM_SEQUENCE\r\n" + "where \r\n"
							+ "s.SYSTEM_SEQUENCE in (SELECT DISTINCT system_sequence from JUNIPER_SYSTEM_MASTER WHERE CONCAT(CONCAT(SYSTEM_EIM,'#'),SYSTEM_NAME)=?) \r\n"
							+ "or t.SYSTEM_SEQUENCE in (SELECT DISTINCT system_sequence from JUNIPER_SYSTEM_MASTER WHERE CONCAT(CONCAT(SYSTEM_EIM,'#'),SYSTEM_NAME)=?)\r\n"
							+ "order by f.FEED_UNIQUE_NAME");
			pstm.setString(1, eim);
			pstm.setString(2, eim);
			rs = pstm.executeQuery();
			arr = new LinkedList<FeedDetailDTO>();
			while (rs.next()) {
				FeedDetailDTO fd = new FeedDetailDTO();
				fd.setFeed_id(rs.getString(1));
				fd.setFeed_name(rs.getString(2));
				fd.setProject_id(rs.getString(3));
				fd.setSrc_conn(rs.getString(4));
				fd.setTgt_conn(rs.getString(5));
				arr.add(fd);

			}
		} catch (ClassNotFoundException | SQLException e) {
			throw e;
		} finally {
			if (pstm != null)
				pstm.close();
			if (rs != null)
				rs.close();
			if (connection != null)
				connection.close();

		}
		return arr;
	}

	@Override
	/**
	 return String invokeRest
	 */
	public String invokeRest(String json, String url) throws UnsupportedOperationException, Exception {
		String resp = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(url);
		// System.out.println("url rest: "+MySqlConstants.CDG_URL+url);
		postRequest.setHeader("Content-Type", "application/json");
		StringEntity input = new StringEntity(json);
		postRequest.setEntity(input);
		HttpResponse response = httpClient.execute(postRequest);
		String response_string = EntityUtils.toString(response.getEntity(), "UTF-8");
		if (response.getStatusLine().getStatusCode() != 200) {
			resp = "Error" + response_string;
			throw new Exception("Error" + response_string);
		} else {
			
			resp = response_string;
		}
		return resp;
	}

}

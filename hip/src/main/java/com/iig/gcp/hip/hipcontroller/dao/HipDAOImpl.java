package com.iig.gcp.hip.hipcontroller.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.iig.gcp.hip.hipcontroller.dto.HipDashboardDTO;
import com.iig.gcp.hip.hipcontroller.utils.ConnectionUtils;
import com.iig.gcp.register.registercontroller.dto.FeedLoggerDTO;

@Component
public class HipDAOImpl implements HipDAO {

	@Autowired
	private ConnectionUtils ConnectionUtils;

	/*****************
	 * This method fetches the existing platforms
	 ****************************************/
	@Override
	/**
	 return ArrayList<String> getPlatform
	 */
	public ArrayList<String> getPlatform() throws SQLException, Exception {
		ArrayList<String> arr = new ArrayList<String>();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"select DISTINCT UPPER(TRIM((lm.VALUE))) from logger_master lm where UPPER(TRIM(lm.subclassification))='PLATFORM'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				arr.add(rs.getString(1));
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

	/*****************
	 * This method fetches the feed registered for a particular platform
	 ****************************************/
	@Override
	/**
	 return ArrayList<String> getPlatformFeed
	 */
	public ArrayList<String> getPlatformFeed(final String platform) throws SQLException, Exception {
		ArrayList<String> feed = new ArrayList<String>();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"select DISTINCT TRIM(UPPER(lm.feed_id)) from logger_master lm where UPPER(TRIM((lm.VALUE)))=? order by 1");
			pstm.setString(1, platform);
			rs = pstm.executeQuery();
			while (rs.next()) {
				feed.add(rs.getString(1));
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
		return feed;
	}

	/*****************
	 * This method fetches the table names associated with the feed
	 ****************************************/
	@Override
	/**
	 return ArrayList<String> getTablefromFeed
	 */
	public ArrayList<String> getTablefromFeed(final String tbl_feed_id, final String run_id)
			throws SQLException, Exception {
		ArrayList<String> arr = new ArrayList<String>();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"SELECT DISTINCT UPPER(SUBSTR(EVENT_TYPE,1,INSTR(EVENT_TYPE,'-')-1)) as table_nm FROM LOGGER_STATS_MASTER WHERE UPPER(EVENT_FEED_ID)=? "
							+ "AND CONCAT(TO_CHAR(EVENT_BATCH_DATE, 'YYYYMMDD'),concat('-',EVENT_RUN_ID))=? AND   EVENT_TYPE not like '%~%-count' and event_type like '%-count' order by substr(table_nm,instr(table_nm,'.'), length(table_nm)-instr(table_nm,'.'))");
			pstm.setString(1, tbl_feed_id);
			pstm.setString(2, run_id);
			rs = pstm.executeQuery();
			while (rs.next()) {
				arr.add(rs.getString(1));
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

	/*****************
	 * This method fetches the static details of the feed like platform, source,* target, scheduler	 ****************************************/
	@Override
	/**
	 return ArrayList<FeedLoggerDTO> getfeeddetails
	 */
	public ArrayList<FeedLoggerDTO> getfeeddetails(final String feed_id) throws SQLException, Exception {
		ArrayList<FeedLoggerDTO> arr = new ArrayList<FeedLoggerDTO>();
		Connection connection = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"select distinct UPPER(FEED_ID),CLASSIFICATION,SUBCLASSIFICATION,case when CLASSIFICATION='Table' then  UPPER(VALUE) else VALUE end from logger_master where trim(UPPER(feed_id))='"
							+ feed_id + "' order by classification,subclassification");
			rs = pstm.executeQuery();

			while (rs.next()) {
				FeedLoggerDTO fl = new FeedLoggerDTO();
				fl.setFeed_id(rs.getString(1));
				fl.setClassification(rs.getString(2));
				fl.setSubclassification(rs.getString(3));
				fl.setValue(rs.getString(4));
				arr.add(fl);
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

	/***************************
	 * This Method fetches the feed statistics start, end time and duration	 ****************************************************************/
	@Override
	/**
	 return ArrayList<HipDashboardDTO> getTableChartLoggerStats
	 */
	public ArrayList<HipDashboardDTO> getTableChartLoggerStats(@Valid final String feed_id)
			throws SQLException, Exception {

		ArrayList<HipDashboardDTO> arrHipDashboard = new ArrayList<HipDashboardDTO>();
		HipDashboardDTO hipDashboardDTO = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date edat = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -60);
		Date todate1 = cal.getTime();
		String sdate = dateFormat.format(todate1);
		String edate = dateFormat.format(edat);
		Connection connection = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement("WITH feed_id AS ( \r\n"
					+ "SELECT DISTINCT feed_id FROM logger_master ) \r\n"
					+ "SELECT distinct st.event_feed_id, to_char(st.event_batch_date,'DD-MON-YY') as batch_date, st.event_run_id, st.event_value AS start_time, case when en.event_value < st.event_value then TO_CHAR(en.event_timestamp, 'DD-MON-YY hh24:mi') else  en.event_value end AS end_time,"
					+ " case when en.event_value < st.event_value then CAST((TO_DATE(en.event_timestamp, 'DD-MM-YY hh24:mi:ss') - TO_DATE(concat(concat(st.event_batch_date , ' '), st.event_value), 'DD-MM-YY hh24:mi:ss')) * 1440 AS DECIMAL(20,0)) else CAST((TO_DATE(concat(concat(en.event_batch_date , ' '), en.event_value), 'DD-MM-YY hh24:mi:ss') - TO_DATE(concat(concat(st.event_batch_date , ' '), st.event_value), 'DD-MM-YY hh24:mi:ss')) * 1440 AS DECIMAL(20,0)) end AS duration \r\n"
					+ "FROM ( SELECT * FROM logger_stats_master \r\n"
					+ "INNER JOIN feed_id ON upper(TRIM(feed_id)) = upper(TRIM(event_feed_id)) AND upper(TRIM(event_type)) = upper('feed-start') AND TRIM(upper(event_feed_id)) = TRIM(upper(?))  AND event_batch_date BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND ( TO_DATE(?, 'YYYY-MM-DD') )) st \r\n"
					+ "INNER JOIN ( SELECT * FROM logger_stats_master \r\n"
					+ "INNER JOIN feed_id ON upper(TRIM(feed_id)) = upper(TRIM(event_feed_id)) AND upper(TRIM(event_type)) = upper('feed-end') \r\n"
					+ "AND TRIM(upper(event_feed_id)) = TRIM(upper(?))  AND EVENT_VALUE is not null  AND event_batch_date BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND ( TO_DATE(?, 'YYYY-MM-DD') ) ) en \r\n"
					+ "ON st.event_feed_id = en.event_feed_id AND st.event_run_id = en.event_run_id AND st.event_batch_date = en.event_batch_date \r\n"
					+ "ORDER BY st.event_run_id,to_date(batch_date)");
			pstm.setString(1, feed_id);
			pstm.setString(2, sdate);
			pstm.setString(3, edate);
			pstm.setString(4, feed_id);
			pstm.setString(5, sdate);
			pstm.setString(6, edate);

			rs = pstm.executeQuery();
			while (rs.next()) {
				hipDashboardDTO = new HipDashboardDTO();
				hipDashboardDTO.setBatch_id(rs.getString(1));
				hipDashboardDTO.setBatch_date(rs.getString(2));
				hipDashboardDTO.setRun_id(rs.getString(3));
				hipDashboardDTO.setStart_time(rs.getString(4));
				hipDashboardDTO.setEnd_time(rs.getString(5));
				hipDashboardDTO.setDuration(rs.getString(6));

				arrHipDashboard.add(hipDashboardDTO);
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
		return arrHipDashboard;
	}

	/**************************** Checks if feed statistical data is available ****************************************************************/

	@Override
	/**
	 return String checkFeedAvailable
	 */
	public String checkFeedAvailable(@Valid final String feed_id) throws SQLException, Exception {
		int stat = 0;
		String strfeed_id = null;
		Connection connection = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"select UPPER(event_feed_id) from logger_stats_master where UPPER(event_feed_id)='" + feed_id
							+ "'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				strfeed_id = rs.getString(1);
				stat = 1;
				break;
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
		return stat + strfeed_id;
	}

	/***************************This Method fetches the feed statistics start, end time and duration for a* date range ****************************************************************/

	@Override
	/**
	 return ArrayList<HipDashboardDTO> getTablechartUsingDate
	 */
	public ArrayList<HipDashboardDTO> getTablechartUsingDate(@Valid final String feedIdFilter, final String date1)
			throws SQLException, Exception {
		String first_date = "";
		String last_date = "";
		String[] dates = date1.split("-");
		first_date = dates[0].trim() + "-" + dates[1].trim() + "-" + dates[2].trim();
		last_date = dates[3].trim() + "-" + dates[4].trim() + "-" + dates[5].trim();

		ArrayList<HipDashboardDTO> arrHipDashboard = new ArrayList<HipDashboardDTO>();

		HipDashboardDTO hipDashboardDTO = null;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = this.ConnectionUtils.getConnection();
			pstm = conn.prepareStatement("WITH feed_id AS ( \r\n" + "SELECT DISTINCT feed_id FROM logger_master ) \r\n"
					+ "SELECT distinct st.event_feed_id, to_char(st.event_batch_date,'DD-MON-YY') as batch_date, st.event_run_id, st.event_value AS start_time, case when en.event_value < st.event_value then TO_CHAR(en.event_timestamp, 'DD-MON-YY hh24:mi') else  en.event_value end AS end_time,  \r\n"
					+ "case when en.event_value < st.event_value then CAST((TO_DATE(en.event_timestamp, 'DD-MM-YY hh24:mi:ss') - TO_DATE(concat(concat(st.event_batch_date , ' '), st.event_value), 'DD-MM-YY hh24:mi:ss')) * 1440 AS DECIMAL(20,0)) else CAST((TO_DATE(concat(concat(en.event_batch_date , ' '), en.event_value), 'DD-MM-YY hh24:mi:ss') - TO_DATE(concat(concat(st.event_batch_date , ' '), st.event_value), 'DD-MM-YY hh24:mi:ss')) * 1440 AS DECIMAL(20,0)) end AS duration \r\n"
					+ "FROM ( SELECT * FROM logger_stats_master \r\n"
					+ "INNER JOIN feed_id ON upper(TRIM(feed_id)) = upper(TRIM(event_feed_id)) AND upper(TRIM(event_type)) = upper('feed-start') AND TRIM(upper(event_feed_id)) = TRIM(upper(?)) \r\n"
					+ "AND event_batch_date BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND ( TO_DATE(?, 'YYYY-MM-DD') ) ) st \r\n"
					+ "INNER JOIN ( SELECT * FROM logger_stats_master \r\n"
					+ "INNER JOIN feed_id ON upper(TRIM(feed_id)) = upper(TRIM(event_feed_id)) AND upper(TRIM(event_type)) = upper('feed-end') \r\n"
					+ "AND TRIM(upper(event_feed_id)) = TRIM(upper(?))  AND EVENT_VALUE is not null \r\n"
					+ "AND event_batch_date BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND ( TO_DATE(?, 'YYYY-MM-DD') ) ) en \r\n"
					+ "ON st.event_feed_id = en.event_feed_id AND st.event_run_id = en.event_run_id AND st.event_batch_date = en.event_batch_date \r\n"
					+ "ORDER BY  st.event_run_id,to_date(batch_date)");
			pstm.setString(1, feedIdFilter);
			pstm.setString(2, first_date);
			pstm.setString(3, last_date);
			pstm.setString(4, feedIdFilter);
			pstm.setString(5, first_date);
			pstm.setString(6, last_date);

			rs = pstm.executeQuery();
			while (rs.next()) {
				hipDashboardDTO = new HipDashboardDTO();
      			hipDashboardDTO.setBatch_id(rs.getString(1));
				hipDashboardDTO.setBatch_date(rs.getString(2));
				hipDashboardDTO.setRun_id(rs.getString(3));
				hipDashboardDTO.setStart_time(rs.getString(4));
				hipDashboardDTO.setEnd_time(rs.getString(5));
				hipDashboardDTO.setDuration(rs.getString(6));
				arrHipDashboard.add(hipDashboardDTO);
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw e;
		} finally {
			if (pstm != null)
				pstm.close();
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		}
		return arrHipDashboard;
	}

	/************************** This Method fetches the table level statistics start, end time and duration ***************************************************************/

	@Override
	/**
	 return ArrayList<HipDashboardDTO> getTableDashboardChartLoggerStats
	 */
	public ArrayList<HipDashboardDTO> getTableDashboardChartLoggerStats(final String plt_id, final String feed_id,
			final String tbl_id, final String run_id, String sdate, String edate) throws SQLException, Exception {
		ArrayList<HipDashboardDTO> arrHipDashboard = new ArrayList<HipDashboardDTO>();
		HipDashboardDTO hipDashboardDTO = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		try {
			conn = this.ConnectionUtils.getConnection();

			pstm = conn.prepareStatement(" WITH feed_id AS " + " ( SELECT distinct feed_id,? as con_cal, value "
					+ " FROM logger_master WHERE upper(TRIM(feed_id)) = upper(TRIM(?)) "
					+ " AND ( upper(TRIM(classification)) = 'TABLE' OR upper(TRIM(classification)) = 'FILE' ) "
					+ " AND upper(TRIM(subclassification)) = 'NAME' AND upper(TRIM(value)) = ? ) ," + " start_time AS"
					+ " (select DISTINCT EVENT_FEED_ID,EVENT_BATCH_DATE,EVENT_RUN_ID,UPPER(EVENT_TYPE) as tbl ,MAX(EVENT_TIMESTAMP) as timestamp,MAX(EVENT_VALUE) as time"
					+ " from logger_stats_master where UPPER(EVENT_FEED_ID)=? AND UPPER(EVENT_TYPE)= UPPER(concat(?,'-start')) AND event_batch_date between ? and ?"
					+ " group by EVENT_FEED_ID,EVENT_BATCH_DATE,EVENT_RUN_ID,UPPER(EVENT_TYPE))," + " end_time AS "
					+ " (select DISTINCT EVENT_FEED_ID,EVENT_BATCH_DATE,EVENT_RUN_ID,UPPER(EVENT_TYPE) as tbl ,MAX(EVENT_TIMESTAMP) as timestamp,MAX(EVENT_VALUE) as time "
					+ " from logger_stats_master where UPPER(EVENT_FEED_ID)=? AND UPPER(EVENT_TYPE)= UPPER(concat(?,'-end')) AND event_batch_date between ? and ?"
					+ " group by EVENT_FEED_ID,EVENT_BATCH_DATE,EVENT_RUN_ID,UPPER(EVENT_TYPE)), " + " count AS "
					+ " (select DISTINCT EVENT_FEED_ID,EVENT_BATCH_DATE,EVENT_RUN_ID,UPPER(EVENT_TYPE) as tbl ,SUM(EVENT_VALUE) as tbl_count"
					+ " from logger_stats_master where UPPER(EVENT_FEED_ID)=?  AND UPPER(EVENT_TYPE)= UPPER(concat(?,'-count')) AND event_batch_date between ? and ?"
					+ " group by EVENT_FEED_ID,EVENT_BATCH_DATE,EVENT_RUN_ID,UPPER(EVENT_TYPE)) "
					+ " SELECT distinct st.event_feed_id,    "
					+ " CASE WHEN st.tbl  like '%|%' THEN  SUBSTR(st.tbl,INSTR(st.tbl,'|')+1,INSTR(st.tbl,'-')-INSTR(st.tbl,'|')-1) ELSE 'NA' END filedt,  "
					+ " to_char(st.event_batch_date,'DD-MON-YY') as batch_date," + " st.event_run_id, st.time, "
					+ " case when en.time < st.time then TO_CHAR(en.timestamp, 'DD-MON-YY hh24:mi') else  en.time end AS en_time,"
					+ " case when en.time < st.time then CAST((TO_DATE(en.timestamp, 'DD-MM-YY hh24:mi:ss') - TO_DATE(concat(concat(st.event_batch_date , ' '), st.time), 'DD-MM-YY hh24:mi:ss')) * 1440 AS DECIMAL(20,0)) else CAST((TO_DATE(concat(concat(en.event_batch_date , ' '), en.time), 'DD-MM-YY hh24:mi:ss') - TO_DATE(concat(concat(st.event_batch_date , ' '), st.time), 'DD-MM-YY hh24:mi:ss')) * 1440 AS DECIMAL(20,0)) end AS duration , "
					+ " UPPER(st.value) AS tb,  co.tbl_count AS tablecount  FROM "
					+ " (SELECT * FROM start_time  LEFT JOIN feed_id ON upper(TRIM(feed_id)) = upper(TRIM(event_feed_id)) ) st "
					+ " INNER JOIN  "
					+ " ( SELECT * FROM end_time LEFT JOIN feed_id ON upper(TRIM(feed_id)) = upper(TRIM(event_feed_id)) ) en   "
					+ " ON st.event_feed_id = en.event_feed_id AND st.event_run_id = en.event_run_id AND st.event_batch_date = en.event_batch_date "
					+ " LEFT OUTER JOIN "
					+ " (SELECT * FROM count LEFT JOIN feed_id  ON upper(TRIM(feed_id)) = upper(TRIM(event_feed_id)) ) co  ON  st.event_run_id = co.event_run_id AND st.event_batch_date = co.event_batch_date  order by st.event_run_id, to_date(batch_date)");
			pstm.setString(1, tbl_id);
			pstm.setString(2, feed_id);
			if (tbl_id.contains("|")) {
				pstm.setString(3, tbl_id.substring(0, tbl_id.indexOf("|")));
			} else {
				pstm.setString(3, tbl_id);
			}
			pstm.setString(4, feed_id);
			pstm.setString(5, tbl_id);
			pstm.setString(6, sdate);
			pstm.setString(7, edate);
			pstm.setString(8, feed_id);
			pstm.setString(9, tbl_id);
			pstm.setString(10, sdate);
			pstm.setString(11, edate);
			pstm.setString(12, feed_id);
			pstm.setString(13, tbl_id);
			pstm.setString(14, sdate);
			pstm.setString(15, edate);
			rs = pstm.executeQuery();

			while (rs.next()) {
				hipDashboardDTO = new HipDashboardDTO();
				hipDashboardDTO.setBatch_id(rs.getString(1));
				hipDashboardDTO.setFile_date(rs.getString(2));
				hipDashboardDTO.setBatch_date(rs.getString(3));
				hipDashboardDTO.setRun_id(rs.getString(4));
				hipDashboardDTO.setStart_time(rs.getString(5));
				hipDashboardDTO.setEnd_time(rs.getString(6));
				hipDashboardDTO.setDuration(rs.getString(7));
				hipDashboardDTO.setTable_name(rs.getString(8));
				hipDashboardDTO.setTable_count(rs.getString(9));
				arrHipDashboard.add(hipDashboardDTO);
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw e;
		} finally {
			if (pstm != null)
				pstm.close();
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		}
		return arrHipDashboard;
	}

	/*********************** This method fetches the Table Metadata ****************************************************************/
	@Override
	/**
	 return  ArrayList<HipDashboardDTO> getTableMetadata
	 */
	public ArrayList<HipDashboardDTO> getTableMetadata(@Valid final String feed_id, @Valid final String sdate,
			@Valid final String edate) throws SQLException, Exception {
		ArrayList<HipDashboardDTO> arr = new ArrayList<HipDashboardDTO>();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		HipDashboardDTO hipDashboardDTO = null;
		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					" SELECT DISTINCT coalesce(tab.RUN_ID,'NA'), coalesce(UPPER(tab.TABLE_NAME),'NA') as tbl_nm, CASE  WHEN TRIM(tab.WHERE_CLAUSE)='1=1'  THEN 'No Where Clause'  ELSE coalesce(UPPER(tab.WHERE_CLAUSE),'NA') END , coalesce(fil.INTERFACE_ID,'NA'),coalesce(fil.LOGICAL_FILE_ID,'NA'), coalesce(fil.LOGICAL_FILE_NAME,'NA'), coalesce(fil.LOGICAL_FILE_DESCRIPTION,'NA'), coalesce(fil.DATA_CATEGORIZATION_CODE,'NA'), coalesce(fil.DATA_CATEGORIZATION_INDICATOR,'NA'),coalesce(fil.MASKING_INDICATOR,'NA') \r\n "
							+ " FROM (SELECT * FROM JUNIPER_EXT_TABLE_STATUS_VW WHERE UPPER(FEED_UNIQUE_NAME)=? AND  CONCAT(CONCAT(EXTRACTED_DATE,'-'),RUN_ID) = (SELECT MAX(CONCAT(CONCAT(EXTRACTED_DATE,'-'),RUN_ID)) FROM JUNIPER_EXT_TABLE_STATUS_VW WHERE UPPER(FEED_UNIQUE_NAME)=?)  ) tab LEFT JOIN JUNIPER_EXT_FILE_BG_MASTER fil ON tab.feed_id=fil.FEED_SEQUENCE  and tab.TABLE_NAME=fil.SRC_TABLE \r\n "
							+ " order by substr(tbl_nm,instr(tbl_nm,'.'), length(tbl_nm)-instr(tbl_nm,'.'))");
			pstm.setString(1, feed_id);
			pstm.setString(2, feed_id);
			rs = pstm.executeQuery();
			while (rs.next()) {
				hipDashboardDTO = new HipDashboardDTO();
				hipDashboardDTO.setRun_id(rs.getString(1));
				hipDashboardDTO.setTable_name(rs.getString(2));
				hipDashboardDTO.setWhere_clause(rs.getString(3));
				hipDashboardDTO.setInterface_id(rs.getString(4));
				hipDashboardDTO.setLog_file_id(rs.getString(5));
				hipDashboardDTO.setLog_file_name(rs.getString(6));
				hipDashboardDTO.setLog_file_desc(rs.getString(7));
				hipDashboardDTO.setData_cat_code(rs.getString(8));
				hipDashboardDTO.setData_cat_indicator(rs.getString(9));
				hipDashboardDTO.setMasking_indicator(rs.getString(10));

				arr.add(hipDashboardDTO);
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

	/*********************** This method fetches the Run Ids for the Feed****************************************************************/
	@Override
	/**
	 return ArrayList<String> getTableRunId
	 */
	public ArrayList<String> getTableRunId(@Valid final String feed_id, @Valid final String sdate,
			@Valid final String edate) throws SQLException, Exception {
		ArrayList<String> arr_run = new ArrayList<String>();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(" SELECT DISTINCT date_run_id FROM \r\n"
					+ " (SELECT concat(TO_CHAR(EVENT_BATCH_DATE, 'YYYYMMDD'),concat('-',EVENT_RUN_ID) ) AS date_run_id , EVENT_VALUE \r\n"
					+ " FROM LOGGER_STATS_MASTER WHERE UPPER(EVENT_FEED_ID)=? AND EVENT_BATCH_DATE BETWEEN ? AND ? AND EVENT_RUN_ID  NOT IN \r\n"
					+ " (SELECT DISTINCT EVENT_RUN_ID FROM LOGGER_STATS_MASTER WHERE UPPER(EVENT_FEED_ID)=? AND  UPPER(EVENT_VALUE) ='FAILED' AND EVENT_TYPE='Feed-status')) a ORDER BY date_run_id DESC");
			pstm.setString(1, feed_id);
			pstm.setString(2, sdate);
			pstm.setString(3, edate);
			pstm.setString(4, feed_id);
			rs = pstm.executeQuery();
			while (rs.next()) {
				arr_run.add(rs.getString(1));
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
		return arr_run;
	}

	/*********************** This method fetches the Table-Column Level Metadata***************************************************************/
	@Override
	/**
	 return ArrayList<HipDashboardDTO> getColumnMetadata
	 */
	public ArrayList<HipDashboardDTO> getColumnMetadata(@Valid final String feed_id, final String tbl_id,
			final String run_id) throws SQLException, Exception {
		ArrayList<HipDashboardDTO> arr_cm = new ArrayList<HipDashboardDTO>();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		HipDashboardDTO hipDashboardDTO = null;
		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"SELECT DISTINCT coalesce(col.COLUMN_NAME,'NA') as colname, coalesce(col.COLUMN_TYPE,'NA') ,coalesce(fel.INTERFACE_ID,'NA'),coalesce(fel.LOGICAL_FILE_ID,'NA'), coalesce(fel.LOGICAL_FILE_NAME,'NA'), coalesce(fel.LOGICAL_FILE_DESCRIPTION,'NA'), coalesce(fel.DATA_CATEGORIZATION_CODE,'NA'), coalesce(fel.DATA_CATEGORIZATION_INDICATOR,'NA'),coalesce(fel.MASKING_INDICATOR,'NA') \r\n"
							+ " FROM (SELECT * FROM JUNIPER_EXT_COLUMN_STATUS WHERE UPPER(FEED_UNIQUE_NAME)=? and TABLE_NAME=? and concat(concat(EXTRACTED_DATE,'-'),RUN_ID)=substr(?,1,24)) col left join JUNIPER_EXT_FIELD_BG_MASTER fel \r\n"
							+ " on col.feed_id=fel.FEED_SEQUENCE  and col.TABLE_NAME=fel.SRC_TABLE  and col.column_name = fel.SRC_PHY_ATTRIBUTE_NAME order by colname");
			pstm.setString(1, feed_id);
			pstm.setString(2, tbl_id);
			pstm.setString(3, run_id);
			rs = pstm.executeQuery();
			while (rs.next()) {
				hipDashboardDTO = new HipDashboardDTO();
				hipDashboardDTO.setColumn_name(rs.getString(1));
				hipDashboardDTO.setColumn_type(rs.getString(2));
				hipDashboardDTO.setInterface_id(rs.getString(3));
				hipDashboardDTO.setLog_file_id(rs.getString(4));
				hipDashboardDTO.setLog_file_name(rs.getString(5));
				hipDashboardDTO.setLog_file_desc(rs.getString(6));
				hipDashboardDTO.setData_cat_code(rs.getString(7));
				hipDashboardDTO.setData_cat_indicator(rs.getString(8));
				hipDashboardDTO.setMasking_indicator(rs.getString(9));
				arr_cm.add(hipDashboardDTO);
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
		return arr_cm;
	}

	/*********************** This method fetches the Targets for a feed ****************************************************************/
	@Override
	/**
	 return ArrayList<String> getTargets
	 */
	public ArrayList<String> getTargets(String feed_id) throws SQLException, Exception {

		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		ArrayList<String> tar_arr = null;

		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"select distinct coalesce(value,'NA') from logger_master where classification like 'Target%' and subclassification='Name' and upper(feed_id)=?");
			pstm.setString(1, feed_id);
			tar_arr = new ArrayList<String>();
			rs = pstm.executeQuery();
			while (rs.next()) {
				tar_arr.add(rs.getString(1));
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
		return tar_arr;
	}

	/************************ This method fetches the source of a feed ****************************************************************/
	@Override
	/**
	 return String getSourceType
	 */
	public String getSourceType(String feed_id) throws SQLException, Exception {

		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		String src_type = null;

		try {
			connection = this.ConnectionUtils.getConnection();
			pstm = connection.prepareStatement(
					"select distinct coalesce(value,'NA') from logger_master where classification='Source' and subclassification='Type' and upper(feed_id)=?");
			pstm.setString(1, feed_id);
			rs = pstm.executeQuery();
			while (rs.next()) {
				src_type = rs.getString(1);
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
		return src_type;
	}

	/*********************** This method fetches the Recon Counts****************************************************************/
	@Override
	/**
	 return ArrayList<HipDashboardDTO> getTblReconCounts
	 */
	public ArrayList<HipDashboardDTO> getTblReconCounts(@Valid final String feed_id, @Valid final String run_id,
			String target, String src_type) throws SQLException, Exception {
		ArrayList<HipDashboardDTO> runs = new ArrayList<HipDashboardDTO>();
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		HipDashboardDTO hipDashboardDTO = null;
		try {
			connection = this.ConnectionUtils.getConnection();
			if (src_type.equalsIgnoreCase("Unix")) {
				pstm = connection.prepareStatement(
						"Select DISTINCT CASE WHEN src.src_tbl like '%|%' THEN  UPPER(SUBSTR(src.src_tbl,1,INSTR(src.src_tbl,'|')-1)) ELSE UPPER(src.src_tbl) END table_nm, \r\n"
								+ "CASE WHEN src.src_tbl  like '%|%' THEN  TRIM(SUBSTR(src.src_tbl,INSTR(src.src_tbl,'|')+1,length('src.src_tbl'))) ELSE 'NA' END file_dt, \r\n"
								+ "src.src_count, case when tgt.tgt_count is null then src.src_count else coalesce(tgt.tgt_count,'NA') end as tgt_count FROM \r\n"
								+ "(SELECT UPPER(SUBSTR(EVENT_TYPE,1,INSTR(EVENT_TYPE,'-')-1)) as src_tbl ,EVENT_VALUE as src_count  FROM LOGGER_STATS_MASTER \r\n"
								+ " WHERE UPPER(EVENT_FEED_ID)=UPPER(?) AND \r\n"
								+ " CONCAT(TO_CHAR(EVENT_BATCH_DATE, 'YYYYMMDD'),concat('-',EVENT_RUN_ID))=? \r\n "
								+ "AND EVENT_TYPE not like '%~%-count' and event_type like '%-count' \r\n "
								// + "group by SUBSTR(EVENT_TYPE,1,INSTR(EVENT_TYPE,'-')-1)"
								+ " )src LEFT JOIN \r\n"
								+ "(SELECT UPPER(SUBSTR(EVENT_TYPE,INSTR(EVENT_TYPE,'~')+1,INSTR(EVENT_TYPE,'-count')-INSTR(EVENT_TYPE,'~')-1)) as tgt_tbl, EVENT_VALUE as tgt_count FROM LOGGER_STATS_MASTER "
								+ "WHERE UPPER(EVENT_FEED_ID)=UPPER(?) AND  \r\n"
								+ "CONCAT(TO_CHAR(EVENT_BATCH_DATE, 'YYYYMMDD'),concat('-',EVENT_RUN_ID))=? \r\n "
								+ "AND EVENT_TYPE  like ?) tgt on src_tbl=tgt_tbl order by substr(table_nm,instr(table_nm,'.'), length(table_nm)-instr(table_nm,'.')) ");

				pstm.setString(1, feed_id);
				pstm.setString(2, run_id);
				pstm.setString(3, feed_id);
				pstm.setString(4, run_id);
				pstm.setString(5, target.concat("~%-count"));

			} else {
				pstm = connection.prepareStatement(
						"Select DISTINCT CASE WHEN src.src_tbl like '%|%' THEN  SUBSTR(src.src_tbl,1,INSTR(src.src_tbl,'|')-1) ELSE src.src_tbl END table_nm, \r\n"
								+ "CASE WHEN src.src_tbl  like '%|%' THEN  TRIM(SUBSTR(src.src_tbl,INSTR(src.src_tbl,'|')+1,length('src.src_tbl'))) ELSE 'NA' END file_dt, \r\n"
								+ "src.src_count,coalesce(tgt.tgt_count,'NA') FROM \r\n"
								+ "(SELECT UPPER(SUBSTR(EVENT_TYPE,1,INSTR(EVENT_TYPE,'-')-1)) as src_tbl ,sum(EVENT_VALUE) as src_count  FROM LOGGER_STATS_MASTER \r\n"
								+ "WHERE UPPER(EVENT_FEED_ID)=UPPER(?) AND \r\n"
								+ "CONCAT(TO_CHAR(EVENT_BATCH_DATE, 'YYYYMMDD'),concat('-',EVENT_RUN_ID))=? \r\n "
								+ "AND EVENT_TYPE not like '%~%-count' and event_type like '%-count' \r\n "
								+ "group by SUBSTR(EVENT_TYPE,1,INSTR(EVENT_TYPE,'-')-1) )src LEFT JOIN \r\n"
								+ "(SELECT UPPER(SUBSTR(EVENT_TYPE,INSTR(EVENT_TYPE,'~')+1,INSTR(EVENT_TYPE,'-count')-INSTR(EVENT_TYPE,'~')-1)) as tgt_tbl, EVENT_VALUE as tgt_count FROM LOGGER_STATS_MASTER "
								+ "WHERE UPPER(EVENT_FEED_ID)=UPPER(?) AND  \r\n"
								+ "CONCAT(TO_CHAR(EVENT_BATCH_DATE, 'YYYYMMDD'),concat('-',EVENT_RUN_ID))=? \r\n "
								+ "AND EVENT_TYPE  like ?) tgt on src_tbl=tgt_tbl  order by substr(table_nm,instr(table_nm,'.'), length(table_nm)-instr(table_nm,'.')) ");

				pstm.setString(1, feed_id);
				pstm.setString(2, run_id);
				pstm.setString(3, feed_id);
				pstm.setString(4, run_id);
				pstm.setString(5, target.concat("~%-count"));

			}
			rs = pstm.executeQuery();
			while (rs.next()) {
				hipDashboardDTO = new HipDashboardDTO();
				hipDashboardDTO.setTable_name(rs.getString(1));
				hipDashboardDTO.setFile_date(rs.getString(2));
				hipDashboardDTO.setSrc_count(rs.getString(3));
				hipDashboardDTO.setTgt_count(rs.getString(4));
				runs.add(hipDashboardDTO);
			}
			hipDashboardDTO = new HipDashboardDTO();
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
		return runs;
	}

}
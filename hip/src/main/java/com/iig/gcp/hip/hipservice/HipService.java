package com.iig.gcp.hip.hipservice;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.validation.Valid;
import com.iig.gcp.hip.hipcontroller.dto.HipDashboardDTO;
import com.iig.gcp.register.registercontroller.dto.FeedLoggerDTO;

public interface HipService {

	ArrayList<FeedLoggerDTO> getfeeddetails(String feed_id) throws SQLException, Exception;

	ArrayList<HipDashboardDTO> getTableChartLoggerStats(@Valid String feed_id) throws SQLException, Exception;

	String checkFeedAvailable(@Valid String feed_id) throws SQLException, Exception;

	ArrayList<HipDashboardDTO> getTablechartUsingDate(@Valid String feedIdFilter, String date)
			throws SQLException, Exception;

	ArrayList<String> getPlatform() throws SQLException, Exception;

	ArrayList<String> getTablefromFeed(String tbl_feed_id, String run_id) throws SQLException, Exception;

	ArrayList<HipDashboardDTO> getTableDashboardChartLoggerStats(final String plt_id, final String feed_id,
			final String tbl_id, final String run_id, String sdate, String edate) throws SQLException, Exception;

	ArrayList<String> getPlatformFeed(String platform) throws SQLException, Exception;

	ArrayList<HipDashboardDTO> getTableMetadata(@Valid String feed_id, @Valid String sdate, @Valid String edate)
			throws SQLException, Exception;

	ArrayList<String> getTableRunId(@Valid String feed_id, @Valid String sdate, @Valid String edate)
			throws SQLException, Exception;

	ArrayList<HipDashboardDTO> getColumnMetadata(@Valid String feed_id, String tbl_id, String run_id)
			throws SQLException, Exception;

	ArrayList<HipDashboardDTO> getTblReconCounts(@Valid final String feed_id, @Valid final String run_id, String target,
			String src_type) throws SQLException, Exception;

	ArrayList<String> getTargets(String feed_id) throws SQLException, Exception;

	String getSourceType(String feed_id) throws SQLException, Exception;
}

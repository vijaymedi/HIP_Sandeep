package com.iig.gcp.hip.hipservice;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iig.gcp.hip.hipcontroller.dao.HipDAO;
import com.iig.gcp.hip.hipcontroller.dto.HipDashboardDTO;
import com.iig.gcp.register.registercontroller.dto.FeedLoggerDTO;

@Service
public class HipServiceImpl implements HipService {

	@Autowired
	HipDAO hipDao;

	@Override
	/**
	 return ArrayList<FeedLoggerDTO>getfeeddetails
	 */
	public ArrayList<FeedLoggerDTO> getfeeddetails(String feed_id) throws SQLException, Exception {
		return hipDao.getfeeddetails(feed_id);
	}

	@Override
	/**
	 return ArrayList<HipDashboardDTO>getTableChartLoggerStats
	 */
	public ArrayList<HipDashboardDTO> getTableChartLoggerStats(@Valid String feed_id) throws SQLException, Exception {
		return hipDao.getTableChartLoggerStats(feed_id);
	}

	@Override
	/**
	 return String checkFeedAvailable
	 */
	public String checkFeedAvailable(@Valid String feed_id) throws SQLException, Exception {
		return hipDao.checkFeedAvailable(feed_id);
	}

	@Override
	/**
	 return ArrayList<HipDashboardDTO>getTablechartUsingDate
	 */
	public ArrayList<HipDashboardDTO> getTablechartUsingDate(@Valid String feedIdFilter, String date)
			throws SQLException, Exception {
		return hipDao.getTablechartUsingDate(feedIdFilter, date);
	}

	@Override
	/**
	 return ArrayList<String>getPlatform
	 */
	public ArrayList<String> getPlatform() throws SQLException, Exception {
		return hipDao.getPlatform();
	}

	@Override
	/**
	 return ArrayList<String>getTablefromFeed
	 */
	public ArrayList<String> getTablefromFeed(String tbl_feed_id, String run_id) throws SQLException, Exception {
		return hipDao.getTablefromFeed(tbl_feed_id, run_id);
	}

	@Override
	/**
	 return ArrayList<String>getPlatformFeed
	 */
	public ArrayList<String> getPlatformFeed(String platform) throws SQLException, Exception {
		return hipDao.getPlatformFeed(platform);
	}

	@Override
	/**
	 return ArrayList<HipDashboardDTO>getTableMetadata
	 */
	public ArrayList<HipDashboardDTO> getTableMetadata(@Valid String feed_id, @Valid String sdate, @Valid String edate)
			throws SQLException, Exception {
		return hipDao.getTableMetadata(feed_id, sdate, edate);
	}

	@Override
	/**
	 return ArrayList<String>getTableRunId
	 */
	public ArrayList<String> getTableRunId(@Valid String feed_id, @Valid String sdate, @Valid String edate)
			throws SQLException, Exception {
		return hipDao.getTableRunId(feed_id, sdate, edate);
	}

	@Override
	/**
	 return ArrayList<HipDashboardDTO>getColumnMetadata
	 */
	public ArrayList<HipDashboardDTO> getColumnMetadata(@Valid String feed_id, String tbl_id, String run_id)
			throws SQLException, Exception {
		return hipDao.getColumnMetadata(feed_id, tbl_id, run_id);
	}

	@Override
	/**
	 return ArrayList<HipDashboardDTO>getTableDashboardChartLoggerStats
	 */
	public ArrayList<HipDashboardDTO> getTableDashboardChartLoggerStats(String plt_id, String feed_id, String tbl_id,
			String run_id, String sdate, String edate) throws SQLException, Exception {
		return hipDao.getTableDashboardChartLoggerStats(plt_id, feed_id, tbl_id, run_id, sdate, edate);
	}

	@Override
	/**
	 return ArrayList<HipDashboardDTO>getTblReconCounts
	 */
	public ArrayList<HipDashboardDTO> getTblReconCounts(@Valid String feed_id, @Valid String run_id, String target,
			String src_type) throws SQLException, Exception {
		return hipDao.getTblReconCounts(feed_id, run_id, target, src_type);
	}

	@Override
	/**
	 return ArrayList<String> getTargets
	 */
	public ArrayList<String> getTargets(String feed_id) throws SQLException, Exception {
		return hipDao.getTargets(feed_id);
	}

	@Override
	/**
	 return String getSourceType
	 */
	public String getSourceType(String feed_id) throws SQLException, Exception {
		return hipDao.getSourceType(feed_id);
	}

}

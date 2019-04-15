package com.iig.gcp.register.registerservice;

import java.sql.SQLException;
import java.util.List;
import com.iig.gcp.register.registercontroller.dto.FeedLoggerDTO;

public interface FeedLoggingService {
	public void feedDataLogging(List<FeedLoggerDTO> dataLoggerCollection) throws SQLException, Exception;

}

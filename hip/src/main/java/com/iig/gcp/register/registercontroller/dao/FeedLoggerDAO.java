package com.iig.gcp.register.registercontroller.dao;

import java.sql.SQLException;
import java.util.List;
import com.iig.gcp.register.registercontroller.dto.FeedLoggerDTO;

public interface FeedLoggerDAO {
	public void loadFeed(List<FeedLoggerDTO> dataLoggerCollection) throws Exception, SQLException;
}

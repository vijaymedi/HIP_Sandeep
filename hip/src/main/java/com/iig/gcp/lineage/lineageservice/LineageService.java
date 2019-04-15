
package com.iig.gcp.lineage.lineageservice;

import java.sql.SQLException;
import java.util.LinkedList;
import com.iig.gcp.lineage.lineagecontroller.dto.FeedDetailDTO;

public interface LineageService {

	String invokeRest(String json, String url) throws UnsupportedOperationException, Exception;

	LinkedList<String> getEIM() throws SQLException, Exception;

	LinkedList<FeedDetailDTO> getFeedDetfromEIM(String eim) throws SQLException, Exception;

}
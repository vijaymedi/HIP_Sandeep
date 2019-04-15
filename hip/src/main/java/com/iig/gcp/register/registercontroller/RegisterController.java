package com.iig.gcp.register.registercontroller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.iig.gcp.register.registercontroller.dto.*;
import com.iig.gcp.register.registerservice.*;

@Controller
public class RegisterController {

	@Autowired
	private FeedLoggingService feedLoggingService;

	@RequestMapping(value = { "/register" }, method = RequestMethod.GET)
	public String register() {
		return "/register/registerFeed";
	}

	@RequestMapping(value = { "/register/submit" }, method = RequestMethod.POST)
	/**
	 * 
	 * @param multiPartFile
	 * @param map
	 * @return ModelAndView dataLoader
	 * @throws Exception
	 */
	public ModelAndView dataLoader(@RequestParam("file") MultipartFile multiPartFile, ModelMap map) throws Exception {
		List<FeedLoggerDTO> dataLoggerCollection = parseFeedRegisterExcel(multiPartFile);
		if (dataLoggerCollection.size() == 0) {
			map.addAttribute("errorString", "Feed Not Registered");
			return new ModelAndView("register/registerFeed");
		} else {

			feedLoggingService.feedDataLogging(dataLoggerCollection);
			map.addAttribute("successString", "Feed Registered");

		}
		return new ModelAndView("register/registerFeed");
	}

	/**
	 * This method reads the uploaded excel and create FeedDTO object.
	 * 
	 * @param multiPartFile
	 * @return List<FeedLoggerDTO>
	 * @throws Exception
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private List<FeedLoggerDTO> parseFeedRegisterExcel(MultipartFile multiPartFile)
			throws Exception, IOException, InvalidFormatException {
		File file = convert(multiPartFile);

		Workbook workbook = WorkbookFactory.create(file);
		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();

		FeedLoggerDTO dataLoggerDTO;
		List<FeedLoggerDTO> dataLoggerCollection = new ArrayList<FeedLoggerDTO>();

		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			dataLoggerDTO = new FeedLoggerDTO();

			// This is done to eliminate top row in excel with heading
			if (row.getRowNum() == 0) {
				continue;
			}
			// If there are no data in columns then we will end process and proceed next.
			if (row.getPhysicalNumberOfCells() == 0) {
				break;
			}

			// Now let's iterate over the columns of the current row
			Iterator<Cell> cellIterator = row.cellIterator();

			String feedId = dataFormatter.formatCellValue(cellIterator.next());
			if (!feedId.equals("")) {
				dataLoggerDTO.setFeed_id(feedId);
			} else {
				break;
			}

			String classification = dataFormatter.formatCellValue(cellIterator.next());
			if (!classification.equals("")) {
				dataLoggerDTO.setClassification(classification);
			} else {
				break;
			}

			String subClassification = dataFormatter.formatCellValue(cellIterator.next());
			if (!subClassification.equals("")) {
				dataLoggerDTO.setSubclassification(subClassification);
			} else {
				break;
			}

			String dataValue = dataFormatter.formatCellValue(cellIterator.next());
			if (!dataValue.equals("")) {
				dataLoggerDTO.setValue(dataValue);
			} else {
				break;
			}

			dataLoggerCollection.add(dataLoggerDTO);

		}
		return dataLoggerCollection;
	}

	/**
	 * This method converts the multipart file to single file instance.
	 * 
	 * @param multipart file
	 * @return file
	 * @throws Exception
	 */
	public File convert(MultipartFile multiPartFile) throws Exception {
		File convFile = new File(multiPartFile.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(multiPartFile.getBytes());
		fos.close();
		return convFile;
	}

}

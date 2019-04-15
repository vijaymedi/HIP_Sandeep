package com.iig.gcp.hip.hipcontroller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.iig.gcp.hip.hipcontroller.dto.HipDashboardDTO;
import com.iig.gcp.hip.hipservice.HipService;
import com.iig.gcp.register.registercontroller.dto.FeedLoggerDTO;

@Controller
public class HipController {

	@Autowired
	HipService hipService;

	@Value("${parent.front.micro.services}")
	private String parent_service_running_instance;

	/*****************
	 * Retrieving all existing platforms
	 *************************************/

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	/**
	 * 
	 * @param jsonObject
	 * @param map
	 * @param request
	 * @return String hipDashboard
	 */
	public String hipDashboard(@Valid @ModelAttribute("jsonObject") final String jsonObject, final ModelMap map,
			final HttpServletRequest request) {

		try {
			ArrayList<String> pltList = this.hipService.getPlatform();
			map.addAttribute("pltList", pltList);
		}

		catch (Exception e) {

			e.printStackTrace();
		}

		return "/index";
	}

	@RequestMapping(value = { "/JuniperHome" }, method = RequestMethod.GET)
	/**
	 * 
	 * @return String hipHome
	 */
	public String hipHome() {
		return "redirect:" + "//" + this.parent_service_running_instance;
	}

	/*****************
	 * Retrieving feeds registered under the selected platform
	 *************************************/

	@RequestMapping(value = { "/hip/hipfeedregisterd" }, method = RequestMethod.POST)
	/**
	 * 
	 * @param plt_id
	 * @param map
	 * @return ModelAndView hipmasterfeed
	 * @throws Exception
	 */
	public ModelAndView hipmasterfeed(@Valid @ModelAttribute("plt_id") final String plt_id, final ModelMap map)
			throws Exception {
		ArrayList<String> feed = new ArrayList<String>();
		feed = this.hipService.getPlatformFeed(plt_id);
		map.addAttribute("feed", feed);
		map.addAttribute("feed_len", feed.size());
		return new ModelAndView("/hip/hipfeedregisterd");
	}

	/************************
	 * Feed Statistics, Table Metadata and Run Ids for default date range
	 ****************************/
	@RequestMapping(value = { "/hip/hipmasterdashboard1" }, method = RequestMethod.POST)
	/**
	 * 
	 * @param feed_id
	 * @param plt_id
	 * @param feed_len
	 * @param map
	 * @return ModelAndView hipmasterDashboard1
	 * @throws Exception
	 */
	public ModelAndView hipmasterDashboard1(@Valid @ModelAttribute("feed_id") final String feed_id,
			@ModelAttribute("plt_id") final String plt_id, @ModelAttribute("feed_len") final String feed_len,
			final ModelMap map) throws Exception {

		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
		Date edat = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -60);
		Date todate1 = cal.getTime();
		String sdate = dateFormat.format(todate1);
		String edate = dateFormat.format(edat);
		String src_flag = "0";
		ArrayList<FeedLoggerDTO> fs = this.hipService.getfeeddetails(feed_id);
		ArrayList<String> tbls = new ArrayList<String>();
		map.addAttribute("feed", fs);
		for (FeedLoggerDTO fs1 : fs) {
			if (fs1.getClassification().matches("Table")) {
				tbls.add(fs1.getValue());
			}
			if (fs1.getClassification().matches("Source") && fs1.getSubclassification().matches("Type")
					&& fs1.getValue().matches("Unix")) {
				src_flag = "1";
			}
		}
		map.addAttribute("tbls", tbls);
		map.addAttribute("tbl_sz", tbls.size());
		map.addAttribute("src_flag", src_flag);
		map.addAttribute("plt_id", plt_id);
		map.addAttribute("feed_id", feed_id);
		map.addAttribute("feed_len", feed_len);
		ArrayList<String> arrBatchDate = new ArrayList<String>();
		ArrayList<String> arrDuration = new ArrayList<String>();
		ArrayList<HipDashboardDTO> arrHipDashboard = this.hipService.getTableChartLoggerStats(feed_id);
		ArrayList<String> runidlist = this.hipService.getTableRunId(feed_id, sdate, edate);
		ArrayList<HipDashboardDTO> arr = this.hipService.getTableMetadata(feed_id, sdate, edate);

		if (arrHipDashboard.isEmpty()) {
			map.addAttribute("feed_id", feed_id);
			map.addAttribute("sdate", sdate);
			map.addAttribute("edate", edate);

		} else {

			map.addAttribute("arrHipDashboard", arrHipDashboard);
			map.addAttribute("arr", arr);
			map.addAttribute("runidlist", runidlist);
			ArrayList<Integer> arrDurationInt = new ArrayList<Integer>();
			for (HipDashboardDTO hipVO : arrHipDashboard) {
				arrDurationInt.add(Integer.parseInt(hipVO.getDuration()));
				arrBatchDate.add(hipVO.getBatch_date().toString());
				arrDuration.add(hipVO.getDuration());
			}
			map.addAttribute("feed_id", feed_id);
			map.addAttribute("x", arrBatchDate);
			map.addAttribute("y", arrDuration);
			map.addAttribute("max", Collections.max(arrDurationInt));
			map.addAttribute("min", Collections.min(arrDurationInt));
			map.addAttribute("average", calculateAverage(arrDurationInt));
			map.addAttribute("sdate", sdate);
			map.addAttribute("edate", edate);
		}
		return new ModelAndView("/hip/hipmasterdashboard1");
	}

	/************************
	 * Feed Statistics, Table Metadata and Run Ids along with Date Filter
	 ****************************/
	@RequestMapping(value = { "/hip/datefilter" }, method = RequestMethod.POST)
	/**
	 * 
	 * @param map
	 * @param feed_id
	 * @param date
	 * @param src_flag
	 * @return ModelAndView hipdateFilter
	 * @throws Exception
	 */
	public ModelAndView hipdateFilter(final ModelMap map, @Valid @RequestParam("feed_id") final String feed_id,
			@RequestParam("date") final String date, @Valid @RequestParam("src_flag") final String src_flag)
			throws Exception {

		String statFeed = this.hipService.checkFeedAvailable(feed_id);
		String stat = statFeed.substring(0, 1);
		String sdate = "";
		String edate = "";
		String[] dates = date.split("-");
		String first_date = dates[0].trim() + "-" + dates[1].trim() + "-" + dates[2].trim();
		String last_date = dates[3].trim() + "-" + dates[4].trim() + "-" + dates[5].trim();
		Date sd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(first_date);
		sdate = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH).format(sd);
		Date ed = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(last_date);
		edate = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH).format(ed);

		if (stat.equals("1")) {
			map.addAttribute("stat", Integer.parseInt(stat));
			ArrayList<String> arrBatchDate = new ArrayList<String>();
			ArrayList<String> arrDuration = new ArrayList<String>();
			ArrayList<HipDashboardDTO> arrHipDashboard = this.hipService.getTablechartUsingDate(feed_id, date);
			if (arrHipDashboard.isEmpty()) {
				map.addAttribute("feed_id", feed_id);
				map.addAttribute("src_flag", src_flag);
			} else {
				map.addAttribute("stat", 2);
				map.addAttribute("arrHipDashboard", arrHipDashboard);
				ArrayList<Integer> arrDurationInt = new ArrayList<Integer>();
				for (HipDashboardDTO hipVO : arrHipDashboard) {
					arrDurationInt.add(Integer.parseInt(hipVO.getDuration()));
					arrBatchDate.add(hipVO.getBatch_date().toString());
					arrDuration.add(hipVO.getDuration());
				}
				map.addAttribute("feed_id", feed_id);
				map.addAttribute("src_flag", src_flag);
				map.addAttribute("x", arrBatchDate);
				map.addAttribute("y", arrDuration);
				map.addAttribute("max", Collections.max(arrDuration));
				map.addAttribute("min", Collections.min(arrDuration));
				map.addAttribute("average", calculateAverage(arrDurationInt));
				ArrayList<HipDashboardDTO> arr = this.hipService.getTableMetadata(feed_id, sdate, edate);
				ArrayList<String> runidlist = this.hipService.getTableRunId(feed_id, sdate, edate);
				map.addAttribute("arr", arr);
				map.addAttribute("runidlist", runidlist);
				map.addAttribute("sdate", sdate);
				map.addAttribute("edate", edate);
			}
			return new ModelAndView("/hip/hipdashboard2");
		} else {
			map.addAttribute("stat", Integer.parseInt(stat));
			map.addAttribute("feed_id", feed_id);
			map.addAttribute("sdate", sdate);
			map.addAttribute("edate", edate);

			return new ModelAndView("/hip/hipdashboard2");
		}

	}

	/************************
	 * * Recon Statistics,Table Names For a run Id
	 ****************************/
	@RequestMapping(value = { "/hip/tablerecon" }, method = RequestMethod.POST)
	/**
	 * 
	 * @param map
	 * @param feed_id
	 * @param src_flag
	 * @param run_id
	 * @param sdate
	 * @param edate
	 * @return ModelAndView tblreconstats
	 */
	public ModelAndView tblreconstats(final ModelMap map, @Valid @RequestParam("feed_id") final String feed_id,
			@Valid @RequestParam("src_flag") final String src_flag, @ModelAttribute("run_id") final String run_id,
			@Valid @RequestParam("sdate") final String sdate, @Valid @RequestParam("edate") final String edate) {
		try {

			ArrayList<String> target = this.hipService.getTargets(feed_id);
			Map<String, ArrayList<HipDashboardDTO>> hm = new HashMap();
			map.addAttribute("target", target);
			String src_type = this.hipService.getSourceType(feed_id);
			for (String t : target) {

				ArrayList<HipDashboardDTO> rArr = this.hipService.getTblReconCounts(feed_id, run_id, t, src_type);
				hm.put(t, rArr);
			}

			map.addAttribute("hm", hm);
			ArrayList<String> table_list = this.hipService.getTablefromFeed(feed_id, run_id);
			map.addAttribute("table_list", table_list);
			map.addAttribute("src_flag", src_flag);
			map.addAttribute("sdate", sdate);
			map.addAttribute("edate", edate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/hip/hiptblreconstats");

	}

	/*************************
	 * Table Level Statistics, Column Metadata
	 ****************************/
	@RequestMapping(value = { "/hip/tableIdFilter" }, method = RequestMethod.POST)
	/**
	 * 
	 * @param plt_id
	 * @param feed_id
	 * @param tbl_id
	 * @param run_id
	 * @param map
	 * @param sdate
	 * @param edate
	 * @return ModelAndView tableIdFilter
	 * @throws Exception
	 */
	public ModelAndView tableIdFilter(@Valid @ModelAttribute("plt_id") final String plt_id,
			@ModelAttribute("feed_id") final String feed_id, @ModelAttribute("tbl_id") final String tbl_id,
			@ModelAttribute("run_id") final String run_id, final ModelMap map,
			@Valid @RequestParam("sdate") final String sdate, @Valid @RequestParam("edate") final String edate)
			throws Exception {

		ArrayList<String> tblArrBatchDate = new ArrayList<String>();
		ArrayList<String> tblArrDuration = new ArrayList<String>();
		ArrayList<String> tblArrCount = new ArrayList<String>();
		ArrayList<HipDashboardDTO> tblArrHipDashboard = this.hipService.getTableDashboardChartLoggerStats(plt_id,
				feed_id, tbl_id, run_id, sdate, edate);
		map.addAttribute("tblArrHipDashboard", tblArrHipDashboard);
		ArrayList<Integer> arrDurationInt = new ArrayList<Integer>();
		ArrayList<Integer> arrCountInt = new ArrayList<Integer>();
		ArrayList<HipDashboardDTO> metadatarr = this.hipService.getColumnMetadata(feed_id, tbl_id, run_id);
		map.addAttribute("metadatarr", metadatarr);
		if (tblArrHipDashboard.isEmpty()) {

		} else {
			for (HipDashboardDTO hipVO : tblArrHipDashboard) {
				arrDurationInt.add(Integer.parseInt(hipVO.getDuration()));
				arrCountInt.add(Integer.parseInt(hipVO.getTable_count()));
				tblArrBatchDate.add(hipVO.getBatch_date().toString());
				tblArrDuration.add(hipVO.getDuration());
				tblArrCount.add(hipVO.getTable_count());

			}
			map.addAttribute("feed_id", feed_id);
			map.addAttribute("tbl_x", tblArrBatchDate);
			map.addAttribute("tbl_y", tblArrDuration);
			map.addAttribute("tbl_z", tblArrCount);
			map.addAttribute("stat", 2);
			map.addAttribute("tbl_max_duration", Collections.max(arrDurationInt));
			map.addAttribute("tbl_min_duration", Collections.min(arrDurationInt));
			map.addAttribute("tbl_avg_duration", calculateAverage(arrDurationInt));
			map.addAttribute("tbl_max_count", Collections.max(arrCountInt));
			map.addAttribute("tbl_min_count", Collections.min(arrCountInt));
			map.addAttribute("tbl_avg_count", calculateAverage(arrCountInt));

		}

		return new ModelAndView("/hip/hipTableDashboard");
	}

	public double calculateAverage(final List<Integer> arr) {
		Double sum = 0.0;
		if (!arr.isEmpty()) {
			for (Integer mark : arr) {
				sum += mark;
			}
			Double a = sum / arr.size();
			double roundOff = Math.round(a * 100.0) / 100.0;
			return roundOff;
		}
		return sum;
	}

}
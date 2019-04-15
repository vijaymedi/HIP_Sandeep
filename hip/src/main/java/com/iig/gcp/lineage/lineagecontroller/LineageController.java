package com.iig.gcp.lineage.lineagecontroller;

import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.iig.gcp.lineage.lineagecontroller.dto.FeedDetailDTO;
import com.iig.gcp.lineage.lineageservice.LineageService;

@Controller
public class LineageController {

	@Autowired
	private LineageService ls;

	@Value("${parent.front.micro.services}")
	private String parent_service_running_instance;

	@Value("${lineage.backend.micro.services}")
	private String lineage_service_running_instance;

	@RequestMapping(value = { "/lineage" }, method = RequestMethod.GET)
	/**
	 * 
	 * @param jsonObject
	 * @param map
	 * @param request
	 * @return String lineage
	 */
	public String lineage(@Valid @ModelAttribute("jsonObject") final String jsonObject, final ModelMap map,
			final HttpServletRequest request) {
		try {

			LinkedList<String> eim = ls.getEIM();
			map.addAttribute("eim", eim);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/lineage/lineageDashboard";
	}

	@RequestMapping(value = { "/JuniperHome1" }, method = RequestMethod.GET)
	public String hipHome1() {
		return "redirect:" + "//" + this.parent_service_running_instance;
	}

	@RequestMapping(value = { "/lineage/FeedList" }, method = RequestMethod.POST)
	/**
	 * 
	 * @param eim
	 * @param map
	 * @return ModelAndView hipmasterfeed
	 * @throws Exception
	 */
	public ModelAndView hipmasterfeed(@Valid @ModelAttribute("eim") final String eim, final ModelMap map)
			throws Exception {
		try {

			LinkedList<FeedDetailDTO> feeddet = ls.getFeedDetfromEIM(eim);
			map.addAttribute("feeddet", feeddet);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("/lineage/lineageDashboard1");
	}

	@RequestMapping(value = "/dashboard/getLineageView", method = RequestMethod.POST)
	/**
	 * 
	 * @param x
	 * @param model
	 * @return ModelAndView getLineageView
	 * @throws UnsupportedOperationException
	 * @throws Exception
	 */
	public ModelAndView getLineageView(@Valid @ModelAttribute("x") String x, ModelMap model)
			throws UnsupportedOperationException, Exception {

		String resp = ls.invokeRest(x, "http://" + lineage_service_running_instance + "/get_lineage");
		if (resp.isEmpty()) {
			model.addAttribute("errorString", "No Lineage available for this DataSet!");
		} else {
			model.addAttribute("resp", resp);

		}
		return new ModelAndView("/lineage/lineageDashboard3");
	}

}
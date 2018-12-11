package com.canalbrewing.myabcdata.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.canalbrewing.myabcdata.logic.ObservedLogic;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.Observed;

@Controller
@RequestMapping("/")
public class ExportController {
	
	@Autowired
	private ObservedLogic observedLogic;
		
	@GetMapping("/exportLog")
	public String exportLog(Model model, @RequestParam(value = "obsId", required = true) String obsId) 
	{
		List<Incident> incidents = new ArrayList<Incident>();
		
		try
		{
			incidents = observedLogic.getIncidentsByObserved(obsId);
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
		}
		
		model.addAttribute("incidents", incidents);
	    return "logExportView";
	}
	
	@GetMapping("/exportDataSheet")
	public String exportDataSheet(Model model, @RequestParam(value = "obsId", required = true) String obsId) 
	{
		Observed observed = new Observed();
		
		try
		{
			observed = observedLogic.getObservedABCs(obsId);
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
		}
		
		model.addAttribute("observed", observed);
		
	    return "dataSheetExportView";
	}

}

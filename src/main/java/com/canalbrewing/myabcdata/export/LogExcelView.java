package com.canalbrewing.myabcdata.export;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.canalbrewing.myabcdata.model.Incident;

@Component("logExportView")
public class LogExcelView extends AbstractXlsView  {
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
			HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		response.setHeader("Content-disposition","attachment; filename=Incidents.xls");
		response.setContentType("application/vnd.ms-excel");

		Sheet excelSheet = workbook.createSheet("Incidents");
		setExcelHeader(workbook, excelSheet);
 
		@SuppressWarnings("unchecked")
		List<Incident> incidents = (List<Incident>)model.get("incidents");
		setExcelRows(excelSheet, incidents);
	}
		
	 public void setExcelHeader(Workbook workbook, Sheet excelSheet) 
	 {
		 	     
	     String[] headers = {
	    		 "Date/Time", "Intensity", "Duration", "Seconds", "Location",
	    		 "Antecedent", "Behavior", "Consequence", "Description"
	    		 };
	        
	     Row row = excelSheet.createRow(0);
	     int column = 0;
	     for ( String header : headers )
	     {
			Cell cell = row.createCell(column);
			cell.setCellValue(header);
			excelSheet.setColumnWidth(column,((header.length() + 10) * 256));
			column++;
	     }
	 }
	 	 
	 public void setExcelRows(Sheet excelSheet, List<Incident> incidents)
	 {
		 int record = 1;
		 for ( Incident incident : incidents )
		 {
			 Row row = excelSheet.createRow(record++);
			 row.createCell(0).setCellValue(incident.getIncidentDtStr());
			 row.createCell(1).setCellValue(incident.getIntensity());
		     row.createCell(2).setCellValue(incident.getDurationStr());
		     row.createCell(3).setCellValue(incident.getDuration());
		     row.createCell(4).setCellValue(incident.getLocation());
		     row.createCell(5).setCellValue(incident.getAntecedentStr().replaceAll(Incident.ABC_DELIMITER, ", "));
		     row.createCell(6).setCellValue(incident.getBehaviorStr().replaceAll(Incident.ABC_DELIMITER, ", "));
		     row.createCell(7).setCellValue(incident.getConsequenceStr().replaceAll(Incident.ABC_DELIMITER, ", "));
		     row.createCell(8).setCellValue(incident.getDescription());
		 }
	 }

}


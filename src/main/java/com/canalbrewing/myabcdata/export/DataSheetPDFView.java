package com.canalbrewing.myabcdata.export;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.canalbrewing.myabcdata.model.Abc;
import com.canalbrewing.myabcdata.model.Observed;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component("dataSheetExportView")
public class DataSheetPDFView extends AbstractITextPdfView {
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,        
			HttpServletRequest request, HttpServletResponse response) throws Exception  
    {
		Observed observed = (Observed)model.get("observed");
		
		response.setContentType("application/pdf");
    	response.setHeader("Expires", "0");
	    response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
	    response.setHeader("Pragma", "public");
    	response.setHeader("Content-disposition","attachment; filename=DataSheet.pdf");
    	
    	PdfPTable layoutTable = new PdfPTable(2);
		layoutTable.setWidthPercentage(100);
		
		PdfPCell observedCell = getCell("Observed:", 0, 12, true);
		observedCell.setColspan(2);
		
		
		layoutTable.addCell(observedCell);
					
		PdfPTable entryTable = new PdfPTable(3);
		entryTable.setWidthPercentage(100);
		
		PdfPTable topTable = new PdfPTable(2);
		topTable.setWidthPercentage(100);
		
		topTable.addCell(getCell("Date/Time:", 0, 8, true));
		topTable.addCell(getCell("Location:", 0));
		topTable.addCell(getCell("Duration:", 0));
		topTable.addCell(getCell("Intensity:", 0));
		
		
		PdfPCell topCell = new PdfPCell(topTable);
		topCell.setColspan(3);
		topCell.setPaddingBottom(5); 
		
		entryTable.addCell(topCell);
						
		entryTable.addCell(
				getCell(buildSectionTable("Antecedent", observed.getAntecedents()), 0, 0)
						);
		entryTable.addCell(
				getCell(buildSectionTable("Behavior", observed.getBehaviors()), 0, 0)
				);
		entryTable.addCell(
				getCell(buildSectionTable("Consequence", observed.getConsequences()), 0, 0)
				);
		
		
		PdfPTable descriptionTable = new PdfPTable(1);
		PdfPCell descrCell = getCell("Description:", 0);
		descrCell.setFixedHeight(50);
		
		descriptionTable.addCell(descrCell);
		
		PdfPCell descriptionCell = new PdfPCell(descriptionTable);
		descriptionCell.setColspan(3);
		entryTable.addCell(descriptionCell);
		
		// Add four instances of it per page
		layoutTable.addCell(getCell(entryTable, 0, 5));
		layoutTable.addCell(getCell(entryTable, 0, 5));
		layoutTable.addCell(getCell(entryTable, 0, 5));
		layoutTable.addCell(getCell(entryTable, 0, 5));
					
		document.add(layoutTable);	
		
	}
	
	private PdfPTable buildSectionTable(String sectionName, java.util.List<Abc> abcList)
	{
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);
		Font boldFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);
		
		PdfPTable sectionTable = new PdfPTable(1);
		sectionTable.setWidthPercentage(100);
		
		Paragraph sectionHeader = new Paragraph(sectionName, boldFont);
		PdfPCell headerCell = new PdfPCell(sectionHeader);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
						
		List list = new List(false,15);
		list.setListSymbol("___");
		for (Abc abc : abcList )
		{
			list.add(new ListItem(abc.getTypeValue(), font));
		}
				
		PdfPCell abcsCell = new PdfPCell();
		abcsCell.addElement(list);
		abcsCell.setFixedHeight(180);
		
		sectionTable.addCell(headerCell);
		sectionTable.addCell(abcsCell);
		
		return sectionTable;
	}
	
	private PdfPCell getCell(PdfPTable table, int border, float padding)
	{
		PdfPCell cell = new PdfPCell(table);
		cell.setBorder(border);
		cell.setPadding(padding);
		return cell;
	}
    	
	private PdfPCell getCell(String value, int border)
	{
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);
		
		PdfPCell cell = new PdfPCell(new Paragraph(value, font));
		cell.setBorder(border);
		cell.setPaddingBottom(10); 
		
		return cell;
	}
	
	private PdfPCell getCell(String value, int border, int fontSize, boolean isBold)
	{
		Font font = FontFactory.getFont(FontFactory.HELVETICA, fontSize);
		if ( isBold )
		{
			font = FontFactory.getFont(FontFactory.HELVETICA, fontSize, Font.BOLD);
		}		
		
		PdfPCell cell = new PdfPCell(new Paragraph(value, font));
		cell.setBorder(border);
		cell.setPaddingBottom(10); 
		return cell;
	}

}

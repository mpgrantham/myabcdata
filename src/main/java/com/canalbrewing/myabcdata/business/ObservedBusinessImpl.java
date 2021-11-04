package com.canalbrewing.myabcdata.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.canalbrewing.myabcdata.dal.ObservedDao;
import com.canalbrewing.myabcdata.model.Abc;
import com.canalbrewing.myabcdata.model.Incident;
import com.canalbrewing.myabcdata.model.IncidentSummary;
import com.canalbrewing.myabcdata.model.Observed;
import com.canalbrewing.myabcdata.model.RequestAbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

@Component
public class ObservedBusinessImpl implements ObservedBusiness {

	private static final String NA = "N/A";

	@Autowired
	ObservedDao observedDao;

	public List<Observed> getObservedByUser(int userId) throws SQLException {
		List<Observed> observedList = observedDao.getObservedByUser(userId);

		for (Observed obs : observedList) {
			String id = String.valueOf(obs.getId());
			obs.setIncidentSummary(getIncidentSummaryByObserved(id));
		}

		return observedList;
	}

	public Observed getObserved(String observedId) throws SQLException {

		int id = Integer.parseInt(observedId);

		Observed observed = observedDao.getObservedById(id);
		observed.getLocations().add(new Abc(0, NA));

		List<Abc> abcs = observedDao.getObservedABCs(id);
		for (Abc abc : abcs) {
			observed.addValue(abc);
		}

		return observed;
	}

	public List<Incident> getIncidentsByObserved(String observedId, String incidentStartDt) throws SQLException {
		return observedDao.getIncidentsByObserved(Integer.parseInt(observedId), incidentStartDt);
	}

	public Incident getIncidentById(String observedId, String incidentId) throws SQLException {
		return observedDao.getIncidentById(Integer.parseInt(observedId), Integer.parseInt(incidentId));
	}

	public IncidentSummary getIncidentSummaryByObserved(String observedId) throws SQLException {
		IncidentSummary summary = new IncidentSummary();

		List<Date> incidentDates = observedDao.getIncidentDatesByObserved(Integer.parseInt(observedId));

		if (incidentDates.isEmpty()) {
			return summary;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		cal.add(Calendar.DATE, -7);
		Date daysAgo7 = cal.getTime();

		cal.add(Calendar.DATE, -23);
		Date daysAgo30 = cal.getTime();

		summary.setLatestIncidentDt(incidentDates.get(0));
		summary.setTotalIncidentCount(incidentDates.size());

		summary.setLast7IncidentCount((int) incidentDates.stream().filter(d -> d.after(daysAgo7)).count());
		summary.setLast30IncidentCount((int) incidentDates.stream().filter(d -> d.after(daysAgo30)).count());

		return summary;
	}

	public Abc addObservedABC(String observedId, RequestAbc requestAbc) throws SQLException {
		Abc abc = new Abc();
		abc.setTypeCd(requestAbc.getTypeCd());
		abc.setTypeValue(requestAbc.getTypeValue());

		if (Abc.LOCATION.equals(requestAbc.getTypeCd())) {
			return observedDao.insertObservedLocation(Integer.parseInt(observedId), abc);
		} else {
			return observedDao.insertObservedABC(Integer.parseInt(observedId), abc);
		}
	}

	public Abc updateObservedABC(String observedId, RequestAbc requestAbc) throws SQLException {
		Abc abc = new Abc();
		abc.setValueId(requestAbc.getValueId());
		abc.setTypeCd(requestAbc.getTypeCd());
		abc.setTypeValue(requestAbc.getTypeValue());
		abc.setActiveFl(requestAbc.getActiveFl());

		if (Abc.LOCATION.equals(requestAbc.getTypeCd())) {
			return observedDao.updateObservedLocation(abc);
		} else {
			return observedDao.updateObservedABC(abc);
		}
	}

	public Observed deleteObserved(String observedId) throws SQLException {

		int id = Integer.parseInt(observedId);

		Observed observed = observedDao.getObservedById(id);

		observedDao.deleteObserved(observed);

		return observed;
	}

	public byte[] getObservedDataSheet(String observedId) throws DocumentException, IOException {

		ByteArrayOutputStream output = new ByteArrayOutputStream(4096);

		Document document = new Document(PageSize.A4);

		PdfWriter writer = PdfWriter.getInstance(document, output);
		writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);

		document.open();

		Chunk chunk = new Chunk("TEST");

		Paragraph para = new Paragraph(chunk);
		document.add(para);

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

		document.close();
		writer.close();
		output.flush();
		output.close();

		return output.toByteArray();
	}

	private PdfPCell getCell(PdfPTable table, int border, float padding) {
		PdfPCell cell = new PdfPCell(table);
		cell.setBorder(border);
		cell.setPadding(padding);
		return cell;
	}

	private PdfPCell getCell(String value, int border) {
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);

		PdfPCell cell = new PdfPCell(new Paragraph(value, font));
		cell.setBorder(border);
		cell.setPaddingBottom(10);

		return cell;
	}

	private PdfPCell getCell(String value, int border, int fontSize, boolean isBold) {
		Font font = FontFactory.getFont(FontFactory.HELVETICA, fontSize);
		if (isBold) {
			font = FontFactory.getFont(FontFactory.HELVETICA, fontSize, Font.BOLD);
		}

		PdfPCell cell = new PdfPCell(new Paragraph(value, font));
		cell.setBorder(border);
		cell.setPaddingBottom(10);
		return cell;
	}
}
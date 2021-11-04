package com.canalbrewing.myabcdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.canalbrewing.myabcdata.business.ObservedBusiness;
import com.canalbrewing.myabcdata.model.StatusMessage;
import com.itextpdf.text.DocumentException;

@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    ObservedBusiness observedBusiness;

    @GetMapping("/datasheet/{observedId}")
    public ResponseEntity<InputStreamResource> exportPDFBinary(@RequestHeader("Authorization") String sessionToken,
            @PathVariable String observedId) throws DocumentException, IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        byte[] data = observedBusiness.getObservedDataSheet(observedId);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);

        return ResponseEntity.ok().headers(headers).contentLength(data.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(byteStream));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<StatusMessage> handleIOException(Exception ex) {
        return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DocumentException.class)
    public ResponseEntity<StatusMessage> handleDocumentException(Exception ex) {
        return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

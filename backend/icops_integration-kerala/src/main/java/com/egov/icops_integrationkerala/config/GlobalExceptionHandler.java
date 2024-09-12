package com.egov.icops_integrationkerala.config;

import com.egov.icops_integrationkerala.model.ChannelMessage;
import com.egov.icops_integrationkerala.util.ProcessReportException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProcessReportException.class)
    public ResponseEntity<ChannelMessage> handleProcessReportException(ProcessReportException ex) {
        ChannelMessage channelMessage = new ChannelMessage();
        channelMessage.setAcknowledgementStatus("FAILURE");
        channelMessage.setFailureMsg(ex.getMessage());

        return new ResponseEntity<>(channelMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ChannelMessage> handleGenericException(Exception ex) {
        ChannelMessage channelMessage = new ChannelMessage();
        channelMessage.setAcknowledgementStatus("FAILURE");
        channelMessage.setFailureMsg("An unexpected error occurred: " + ex.getMessage());

        return new ResponseEntity<>(channelMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

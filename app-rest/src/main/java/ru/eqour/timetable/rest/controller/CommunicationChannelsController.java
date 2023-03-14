package ru.eqour.timetable.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import ru.eqour.timetable.rest.exception.NotifierNotFoundException;
import ru.eqour.timetable.rest.exception.SendCodeException;
import ru.eqour.timetable.rest.model.channels.CodeRequest;
import ru.eqour.timetable.rest.model.channels.CommunicationChannel;
import ru.eqour.timetable.rest.model.channels.UpdateChannelActiveRequest;
import ru.eqour.timetable.rest.model.channels.UpdateChannelRecipientRequest;
import ru.eqour.timetable.rest.service.CommunicationChannelsService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/communication-channels")
public class CommunicationChannelsController {

    private CommunicationChannelsService channelsService;

    @Autowired
    public void setChannelsService(CommunicationChannelsService channelsService) {
        this.channelsService = channelsService;
    }

    @PostMapping
    public ResponseEntity<Map<String, CommunicationChannel>> getCommunicationChannels(@CurrentSecurityContext SecurityContext context) {
        return ResponseEntity.ok(channelsService.findAllChannelsByEmail(context.getAuthentication().getPrincipal().toString()));
    }

    @PostMapping("{channelId}/code")
    public ResponseEntity<?> code(@RequestParam String channelId, @RequestBody CodeRequest request, @CurrentSecurityContext SecurityContext context) {
        if (request == null || request.getRecipient() == null && channelId == null) {
            return ResponseEntity.badRequest().build();
        }
        String email = context.getAuthentication().getPrincipal().toString();
        channelsService.registerCode(email, channelId, request.getRecipient());
        return ResponseEntity.ok().build();
    }

    @PutMapping("{channelId}/id")
    public ResponseEntity<?> updateChannelRecipient(@RequestParam String channelId, @RequestBody UpdateChannelRecipientRequest request,
                                                    @CurrentSecurityContext SecurityContext context) {
        if (request == null || request.getRecipient() == null || request.getCode() == null && channelId == null) {
            return ResponseEntity.badRequest().build();
        }
        String email = context.getAuthentication().getPrincipal().toString();
        if (channelsService.verifyCode(email, channelId, request.getRecipient(), request.getCode())) {
            channelsService.updateChannelRecipient(channelId, request.getRecipient(), request.getCode());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("{channelId}/active")
    public ResponseEntity<?> updateActive(@RequestParam String channelId, @RequestBody UpdateChannelActiveRequest request) {
        if (request == null || channelId == null) {
            return ResponseEntity.badRequest().build();
        }
        channelsService.setActive(channelId, request.isActive());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{channelId}")
    public ResponseEntity<?> delete(@RequestParam String channelId) {
        channelsService.deleteChannel(channelId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(SendCodeException.class)
    private ResponseEntity<?> handleSendCodeException(SendCodeException exception) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler(NotifierNotFoundException.class)
    private ResponseEntity<?> handleNotifierNotFoundException(NotifierNotFoundException exception) {
        return ResponseEntity.badRequest().build();
    }
}

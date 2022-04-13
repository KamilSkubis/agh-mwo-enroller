package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.model.ParticipantDTO;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @GetMapping(value = "")
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<>(meetings, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSingleMeeting(@PathVariable("id") String meetingId) {
        Meeting meeting = meetingService.getMeetingById(meetingId);
        if (meeting == null) {
            return new ResponseEntity("Meeting with id:" + meetingId + " doesnt exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
        if (meetingService.getMeetingById(String.valueOf(meeting.getId())) == null) {
            meetingService.addMeeting(meeting);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Meeting with this id:" + meeting.getId() + " already exist", HttpStatus.CONFLICT);
    }

    @PatchMapping(value = "/{meetingId}")
    public ResponseEntity<?> addParticipantToMeeting(@RequestBody ParticipantDTO participantId,
                                                     @PathVariable("meetingId") String id) {
        Meeting meeting = meetingService.getMeetingById(id);
        ParticipantService participantService = new ParticipantService();
        Participant participant = participantService.findByLogin(String.valueOf(participantId.getLogin()));

        if (meeting == null) {
            return new ResponseEntity<>("No meeting with id:" + id, HttpStatus.NOT_FOUND);
        }

        meetingService.addParticipantToMeeting(participant, meeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
}
package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
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
    public ResponseEntity<?> updateMeeting(@RequestBody Map<String, String> updates,
                                           @PathVariable("meetingId") String id) {

        Meeting meeting = meetingService.getMeetingById(id);
        if (meeting == null) {
            return new ResponseEntity<>("No meeting with id:" + id, HttpStatus.NOT_FOUND);
        }
        meetingService.updateData(updates, meeting);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/participants")
    public ResponseEntity<?> getParticipantsFromMeeting(@PathVariable("id") String meetingId) {
        Meeting meeting = meetingService.getMeetingById(meetingId);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meeting.getParticipants(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") String meetingId) {
        Meeting meeting = meetingService.getMeetingById(meetingId);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meetingService.deleteMeeting(meeting);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/participants")
    public ResponseEntity<?> deleteParticipantFromMeeting(@PathVariable("id") String meetingId,
                                                          @RequestBody() Map<String, String> participantId) {
        Meeting meeting = meetingService.getMeetingById(meetingId);
        Participant participant = new ParticipantService().findByLogin(participantId.get("login"));

        if (meeting == null && participant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        meetingService.deleteParticipantFromMeeting(meeting, participant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
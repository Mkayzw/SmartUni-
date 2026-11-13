package com.smartuni.announcement;

import com.smartuni.common.ResourceNotFoundException;
import com.smartuni.course.Course;
import com.smartuni.course.CourseRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The finished reference feature: read it end to end before starting the TODOs.
 */
@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final CourseRepository courseRepository;

    private Announcement findById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement", id));
    }


    private AnnouncementResponse saveAnnouncement(Announcement announcement, AnnouncementRequest request) {
        Course course = null;

        if (request.courseId() != null) {
            course = courseRepository.findById(request.courseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course", request.courseId()));
        }

        announcement.setTitle(request.title().trim());
        announcement.setContent(request.content().trim());
        announcement.setPinned(request.pinned());
        announcement.setCourse(course);

        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return toResponse(savedAnnouncement);
    }

    private AnnouncementResponse toResponse(Announcement announcement) {
        Course course = announcement.getCourse();
        Long courseId;
        String courseCode;

        if (course == null) {
            courseId = null;
            courseCode = null;
        } else {
            courseId = course.getId();
            courseCode = course.getCode();
        }

        return new AnnouncementResponse(
                announcement.getId(),
                courseId,
                courseCode,
                announcement.getTitle(),
                announcement.getContent(),
                announcement.isPinned(),
                announcement.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> listAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();
        List<AnnouncementResponse> responses = new ArrayList<>();

        for (Announcement announcement : announcements) {
            responses.add(toResponse(announcement));
        }

        return responses;
    }

    @Transactional
    public AnnouncementResponse createAnnouncement(AnnouncementRequest request) {
        Announcement announcement = new Announcement();
        return saveAnnouncement(announcement, request);
    }

    /**
     * TODO-BE-005: Delete an announcement. Unknown id must produce 404, not 200 and not 500.
     * Note: what does repository.deleteById(...) do in Spring Data JPA 3 when the id does not exist?
     */

    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = findById(id);

        announcementRepository.delete(announcement);
    }

    /**
     * TODO-BE-016: Pin or unpin. Unknown id -> 404.
     */
    @Transactional
    public AnnouncementResponse setPinned(Long id) {
        Announcement announcement = findById(id);
        announcement.setPinned(!announcement.isPinned());
        Announcement saved = announcementRepository.save(announcement);

        return toResponse(saved);
    }

    @Transactional
    public AnnouncementResponse update(Long id, AnnouncementRequest request) {
        Announcement announcement = findById(id);
        return saveAnnouncement(announcement, request);
    }



}

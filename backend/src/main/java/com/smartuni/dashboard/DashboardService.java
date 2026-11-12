package com.smartuni.dashboard;

import com.smartuni.announcement.AnnouncementRepository;
import com.smartuni.booking.VenueBookingRepository;
import com.smartuni.course.CourseRepository;
import com.smartuni.enrollment.EnrollmentRepository;
import com.smartuni.enrollment.EnrollmentStatus;
import com.smartuni.student.StudentRepository;
import com.smartuni.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final VenueRepository venueRepository;
    private final VenueBookingRepository venueBookingRepository;
    private final AnnouncementRepository announcementRepository;

    @Transactional(readOnly = true)
    public DashboardSummary getSummary() {
        return new DashboardSummary(
                studentRepository.count(),
                courseRepository.count(),
                enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE),
                venueRepository.count(),
                venueBookingRepository.count(),
                announcementRepository.count());
    }
}

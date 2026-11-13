package com.smartuni.announcement;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findAllByOrderByIdDesc();
    List<Announcement> findAllByOrderByCreatedAtDesc();
    // TODO-BE-016: pinned announcements first, then newest first. Express the ordering in the method name.
}

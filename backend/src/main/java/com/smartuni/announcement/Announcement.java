package com.smartuni.announcement;

import com.smartuni.course.Course;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "announcements",
indexes={
        @Index( name = "idx_announcements_pinned_id",
                columnList = "pinned DESC,id DESC")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement {

    @ManyToOne(fetch= FetchType.LAZY,optional = true)
    @JoinColumn(name="course_id",nullable = true)
    private Course course;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private boolean pinned;

    @Column(nullable = false)
    private LocalDateTime createdAt;




    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

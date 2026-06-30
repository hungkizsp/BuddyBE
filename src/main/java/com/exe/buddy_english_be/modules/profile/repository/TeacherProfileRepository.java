package com.exe.buddy_english_be.modules.profile.repository;

import com.exe.buddy_english_be.modules.profile.entity.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
}

package com.exe.buddy_english_be.modules.profile.repository;

import com.exe.buddy_english_be.modules.profile.entity.ParentChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentChildRepository extends JpaRepository<ParentChild, Long> {

    List<ParentChild> findByParentId(Long parentId);

    List<ParentChild> findByChildId(Long childId);
}

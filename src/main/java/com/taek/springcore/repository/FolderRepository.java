package com.taek.springcore.repository;


import com.taek.springcore.model.Folder;
import com.taek.springcore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// @Repository - 이거 안적어도 구현체에서 가지고 있다?
public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUser(User user);
}

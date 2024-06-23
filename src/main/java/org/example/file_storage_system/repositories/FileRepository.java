package org.example.file_storage_system.repositories;

import org.example.file_storage_system.Models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long>{
}

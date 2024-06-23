package org.example.file_storage_system.repositories;

import org.example.file_storage_system.Models.FileVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileVersionRepository extends JpaRepository<FileVersion, Long> {
}

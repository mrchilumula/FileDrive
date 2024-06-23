package org.example.file_storage_system.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class FileVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String version;
    private String path;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    // Getters and Setters
}

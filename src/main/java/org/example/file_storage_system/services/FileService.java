package org.example.file_storage_system.services;

import org.example.file_storage_system.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.file_storage_system.repositories.FileVersionRepository;
import org.example.file_storage_system.repositories.UserRepository;
import org.example.file_storage_system.Models.File;
import org.example.file_storage_system.Models.FileVersion;
import org.example.file_storage_system.Models.User;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileVersionRepository fileVersionRepository;

    @Autowired
    private UserRepository userRepository;

    private final Path rootLocation;

    @Autowired
    public FileService(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
    }

    public File store(MultipartFile file, String username) throws IOException {
        User user = userRepository.findByname(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String filename = file.getOriginalFilename();
        Path destinationFile = rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
            throw new RuntimeException("Cannot store file outside current directory.");
        }

        Files.copy(file.getInputStream(), destinationFile);

        File newFile = new File();
        newFile.setName(filename);
        newFile.setPath(destinationFile.toString());
        newFile.setUser(user);
        fileRepository.save(newFile);

        FileVersion fileVersion = new FileVersion();
        fileVersion.setVersion("1.0");
        fileVersion.setPath(destinationFile.toString());
        fileVersion.setFile(newFile);
        fileVersionRepository.save(fileVersion);

        return newFile;
    }

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    public Optional<File> getFile(Long id) {
        return fileRepository.findById(id);
    }

    public byte[] downloadFile(Long id) throws IOException {
        File file = fileRepository.findById(id).orElseThrow();
        Path path = Paths.get(file.getPath());
        return Files.readAllBytes(path);
    }

    public void sendFile(Long fileId, String recipientUsername) {
        User recipient = userRepository.findByname(recipientUsername);
        if (recipient == null) {
            throw new RuntimeException("Recipient not found");
        }

        File file = fileRepository.findById(fileId).orElseThrow();

        File newFile = new File();
        newFile.setName(file.getName());
        newFile.setPath(file.getPath());
        newFile.setUser(recipient);
        fileRepository.save(newFile);

        for (FileVersion version : file.getVersions()) {
            FileVersion newVersion = new FileVersion();
            newVersion.setVersion(version.getVersion());
            newVersion.setPath(version.getPath());
            newVersion.setFile(newFile);
            fileVersionRepository.save(newVersion);
        }
    }
}
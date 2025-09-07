package pl.radek.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import pl.radek.config.ApplicationProperties;
import pl.radek.exception.FileSystemException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileSystemService
{
    private final ApplicationProperties properties;
    
    public File createTemporaryDirectory() {
        try {
            Path pathDir = Path.of(properties.getBaseDir());
            Files.createDirectories(pathDir);
            
            String prefix = "repo-" + UUID.randomUUID();
            Path newDir = pathDir.resolve(prefix);
            
            Files.createDirectory(newDir);
            return newDir.toFile();
        }
        catch (IOException e) {
            throw new FileSystemException(String.format(
                    "🚫 Could not create directory: %s",
                    e.getMessage()
            ));
        }
    }
    
    public String readFileAsString(File file) {
        try {
            log.debug("Read file: {}", file.getAbsolutePath());
            return Files.readString(file.toPath());
        }
        catch (IOException e) {
            throw new FileSystemException(String.format(
                    "🚫 Could not read file: %s",
                    file.getAbsolutePath()
            ));
        }
    }
    
    public List<File> findFilesInDirectory(final File rootDir) {
        if (!rootDir.exists())
            return Collections.emptyList();
        
        List<File> files = findFilesRecursively(rootDir, properties.getFileExtensionsWhiteList());
        
        log.debug("Found a total of ({}) files:", files.size());
        files.forEach(file -> log.debug("📄 {}", file.getName()));
        return files;
    }
    
    private List<File> findFilesRecursively(final File dir, final Set<String> extensions) {
        File[] files = dir.listFiles();
        if (files == null)
            return Collections.emptyList();
        
        List<File> fileCollection = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                fileCollection.addAll(findFilesRecursively(file, extensions));
            }
            else {
                for (String ext : extensions) {
                    if (file.getName().endsWith(ext)) {
                        fileCollection.add(file);
                        break;
                    }
                }
            }
        }
        
        return fileCollection;
    }
    
    public BigDecimal calculateDirectorySize(final File directory) {
        long totalBytes = calculateSizeRecursively(directory);
        // KB -> 1024
        // MB -> 1024 * 1024
        BigDecimal size = BigDecimal.valueOf(totalBytes)
                .divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP);
        log.debug("Directory ({}) size: {} KB", directory.getName(), size);
        return size;
    }
    
    private long calculateSizeRecursively(File dir) {
        File[] files = dir.listFiles();
        if (files == null)
            return 0L;
        
        long size = 0;
        for (File file : files) {
            if (file.isDirectory())
                size += calculateSizeRecursively(file);
            else
                size += file.length();
        }
        return size;
    }
    
    public void deleteDirectory(File dir) {
        if (dir == null) {
            log.warn("Directory is null, cannot delete.");
            return;
        }
        
        if (!dir.isDirectory()) {
            log.warn("Cannot delete if it is not a directory: {}.", dir.getAbsolutePath());
            return;
        }
        
        makeWritableRecursively(dir);
        
        if (FileSystemUtils.deleteRecursively(dir))
            log.debug("Deleted directory: {}", dir.getAbsolutePath());
        else
            log.warn("Directory doesn't exist or couldn't be deleted: {}", dir.getAbsolutePath());
    }
    
    private void makeWritableRecursively(File dir) {
        if (!dir.exists())
            return;
        
        if (!dir.canWrite() && !dir.setWritable(true))
                log.warn("Failed to make object writable: {}", dir.getAbsolutePath());
        
        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles()))
                makeWritableRecursively(file);
        }
    }
}

package pl.radek.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;
import pl.radek.config.ApplicationProperties;
import pl.radek.exception.FileSystemException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemServiceTest
{
    private ApplicationProperties properties;
    private FileSystemService service;
    
    private File tmpDir;
    
    @BeforeEach
    void setUp() throws IOException {
        Path testDirPath = Path.of("repository-test");
        Files.createDirectories(testDirPath);
        tmpDir = testDirPath.toFile();
        
        createTemporaryFiles();
        
        properties = new ApplicationProperties();
        properties.setBaseDir(tmpDir.getAbsolutePath());
        properties.setFileExtensionsWhiteList(Set.of(".java", ".kt"));
        
        service = new FileSystemService(properties);
    }
    
    @AfterEach
    void tearDown() {
        FileSystemUtils.deleteRecursively(tmpDir);
    }
    
    @Test
    void shouldFindOnlyJavaAndKotlinFiles() {
        List<File> result = service.findFilesInDirectory(tmpDir);
        
        result.sort(Comparator.comparing(File::getName));
        assertAll(
                () -> assertEquals(6, result.size(), "Should find only 6 files!"),
                () -> assertEquals("extra code.java", result.get(0).getName()),
                () -> assertEquals("java code 1.java", result.get(1).getName()),
                () -> assertEquals("java code 2.java", result.get(2).getName()),
                () -> assertEquals("java code 3.java", result.get(3).getName()),
                () -> assertEquals("kt code 1.kt", result.get(4).getName()),
                () -> assertEquals("kt code 2.kt", result.get(5).getName())
        );
    }
    
    @Test
    void shouldCalculateDirectorySizeInKB() {
        BigDecimal sizeKb = service.calculateDirectorySize(tmpDir);
        
        assertAll(
                () -> assertTrue(sizeKb.compareTo(BigDecimal.ZERO) > 0,
                        "Directory size should be greater than 0 KB!"),
                () -> assertTrue(sizeKb.doubleValue() > 0.07,
                        "Total size should be greater than 0.07 KB!")
        );
    }
    
    @Test
    void shouldReadFileContentCorrectly() {
        File file = new File(tmpDir, "cmd file 1.cmd");
        
        String actualContent = service.readFileAsString(file);

        assertEquals("some cmd 1", actualContent);
    }
    
    @Test
    void shouldThrowFileSystemException_WhenFileCannotBeRead() {
        File file = new File(tmpDir, "nonexistent file.txt");
        
        assertThrows(FileSystemException.class, () -> service.readFileAsString(file));
    }
    
    private void createTemporaryFiles() throws IOException {
        Path javaDir = tmpDir.toPath().resolve("java");
        Path extraJavaDir = javaDir.resolve("extra");
        Path exeDir = tmpDir.toPath().resolve("exe");
        Path ktDir = tmpDir.toPath().resolve("kotlin");
        
        Files.createDirectories(javaDir);
        Files.createDirectories(extraJavaDir);
        Files.createDirectories(exeDir);
        Files.createDirectories(ktDir);
        
        Files.writeString(extraJavaDir.resolve("extra code.java"), "extra java code");
        Files.writeString(javaDir.resolve("java code 1.java"), "java code 1");
        Files.writeString(javaDir.resolve("java code 2.java"), "java code 2");
        Files.writeString(javaDir.resolve("java code 3.java"), "java code 3");
        Files.writeString(exeDir.resolve("app 1.exe"), "exe 1");
        Files.writeString(exeDir.resolve("app 2.exe"), "exe 2");
        Files.writeString(exeDir.resolve("app 3.exe"), "exe 3");
        Files.writeString(ktDir.resolve("kt code 1.kt"), "kt code 1");
        Files.writeString(ktDir.resolve("kt code 2.kt"), "kt code 2");
        Files.writeString(tmpDir.toPath().resolve("cmd file 1.cmd"), "some cmd 1");
        Files.writeString(tmpDir.toPath().resolve("cmd file 2.cmd"), "some cmd 2");
    }
}
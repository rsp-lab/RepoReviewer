package pl.radek.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;
import pl.radek.exception.GitHubCloneException;

import java.io.File;

@Service
@Slf4j
public class GitHubService
{
    public void cloneGitRepository(final String repoUrl, final File tempDir) {
        try {
            try (Git git = Git.cloneRepository()
                              .setURI(repoUrl)
                              .setDirectory(tempDir)
                              // .setBranch("main")
                              // .setCloneAllBranches(false)
                              .setTimeout(60).call()) {
                log.debug("Repository ({}) successfully cloned into directory: {}",
                        repoUrl, tempDir);
            }
        }
        catch (Exception e) {
            throw new GitHubCloneException(String.format("🚫 Failed to clone github repository (%s): %s", repoUrl, e.getMessage()));
        }
    }
}
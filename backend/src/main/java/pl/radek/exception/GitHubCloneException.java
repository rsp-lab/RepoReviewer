package pl.radek.exception;

public class GitHubCloneException extends RuntimeException
{
    public GitHubCloneException(String message) {
        super(message);
    }
}

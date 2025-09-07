package pl.radek;

import pl.radek.dto.Issue;
import pl.radek.dto.ScanFullReview;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SampleMongoData
{
    public static ScanFullReview getSampleData() {
        List<Issue> list = new ArrayList<>();
        list.add(new Issue(
                "src\\com\\mock\\data\\UserService.java",
                45,
                Issue.Severity.LOW,
                "This method sometimes returns null unexpectedly. It could break other components if not checked. Consider adding proper validation.",
                "Add proper null checks for all return values. Validate all input parameters before processing. Refactor complex loops to simplify logic. Implement consistent exception handling. Document the purpose and behavior of the function. Provide clear error messages to guide users. Write unit tests to cover edge cases."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\AuthController.java",
                112,
                Issue.Severity.CRITICAL,
                "Password is stored as plain text. This is a severe security vulnerability. It can expose user data to attackers.",
                "Hash all passwords before storing them. Implement salting for added security. Enforce strong password rules. Review all authentication-related logging. Add unit and integration tests for security functions. Rotate existing credentials if needed. Ensure compliance with industry security standards."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\Repository.java",
                78,
                Issue.Severity.MEDIUM,
                "Query construction uses string concatenation. This can lead to SQL injection. Performance may also degrade with complex queries.",
                "Use prepared statements or ORM methods. Validate input parameters. Consider caching repeated queries."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\UserService.java",
                42,
                Issue.Severity.LOW,
                "The method occasionally returns null, which can lead to NullPointerExceptions. Input validation is missing for several parameters. Complex loop logic makes the code difficult to maintain. Exceptions are not handled properly in edge cases. There are no comments explaining the purpose of the function. Error messages are unclear and do not guide the user. The function may produce inconsistent results under certain conditions.",
                "Add null checks before returning values. Document potential null returns in method comments. Refactor method to reduce ambiguity."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\AuthController.java",
                108,
                Issue.Severity.CRITICAL,
                "Passwords are stored in plain text. This is a serious security vulnerability. It could expose user data to attackers.",
                "Use a secure hashing algorithm to store passwords. Implement proper salting. Ensure all authentication endpoints handle sensitive data safely."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\Repository.java",
                76,
                Issue.Severity.MEDIUM,
                "Query construction uses string concatenation. This can lead to SQL injection attacks. Performance might degrade on complex queries.",
                "Switch to prepared statements or ORM methods. Validate all input parameters. Consider caching repeated queries for efficiency."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\PaymentService.java",
                150,
                Issue.Severity.CRITICAL,
                "Transaction processing does not handle exceptions correctly. This may lead to inconsistent database state. Users could be charged incorrectly.",
                "Add proper exception handling for all transactional operations. Log errors for debugging. Ensure rollback on failure to maintain consistency."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\NotificationService.java",
                88,
                Issue.Severity.MEDIUM,
                "Notifications are sent synchronously which may block the main thread. This could delay other operations. Performance degradation is possible under load.",
                "Refactor notifications to use asynchronous processing. Consider using message queues. Ensure proper error handling for failed deliveries."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\OrderController.java",
                63,
                Issue.Severity.LOW,
                "Method parameters are not validated. Invalid input could lead to exceptions or unexpected behavior. This might confuse API consumers.",
                "Use parameterized queries or ORM methods instead of string concatenation. Sanitize all user inputs. Add indexing on frequently queried columns. Improve error handling for database operations. Optimize queries for large datasets. Add unit tests to validate query results. Review repository methods for consistency."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\InvoiceService.java",
                29,
                Issue.Severity.MEDIUM,
                "Loop iterates over a large collection inefficiently. This can slow down processing for large datasets. Memory usage may also spike unexpectedly.",
                "Optimize loop using streams or more efficient algorithms. Consider processing in batches. Profile memory usage under load to detect bottlenecks."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\FileUtils.java",
                95,
                Issue.Severity.LOW,
                "File handles are not always closed properly. This could lead to resource leaks. Over time, it may affect system stability.",
                "Ensure all file streams are closed using try-with-resources. Add logging for potential errors. Review other utility methods for similar issues."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\EmailService.java",
                134,
                Issue.Severity.MEDIUM,
                "Email templates are hardcoded inside the code. This makes updates cumbersome and error-prone. It may also cause duplication issues.",
                "Move email templates to configuration files or database. Use placeholders for dynamic content. Implement template rendering logic for flexibility."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\AnalyticsProcessor.java",
                201,
                Issue.Severity.CRITICAL,
                "Heavy computation is done on the main thread. This can freeze the application under high load. Users may experience severe performance issues.",
                "Move computation to a separate worker thread or executor. Consider batching calculations. Profile performance to ensure responsiveness."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\CacheManager.java",
                55,
                Issue.Severity.LOW,
                "Data aggregation logic does not handle missing or null values. Reports may produce inaccurate results. No validation is performed before processing input data. Downstream services rely on potentially corrupted metrics. Exception handling is inconsistent across aggregation functions. The algorithm is difficult to read and maintain. Documentation does not describe the limitations or assumptions made.",
                "Define and document cache eviction rules. Implement automatic removal for outdated entries. Add unit tests to verify cache behavior."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\PaymentGateway.java",
                122,
                Issue.Severity.CRITICAL,
                "Passwords are stored in plain text, creating a major security vulnerability. Hashing and salting are not implemented. Logs may inadvertently expose sensitive data. There is no enforcement of password complexity rules. Unit tests for authentication are missing. Unauthorized access could occur if the system is compromised. The current implementation does not comply with security best practices.",
                "Move API keys to environment variables or secure vaults. Rotate keys regularly. Ensure all team members are aware of secure handling procedures."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\ReportGenerator.java",
                88,
                Issue.Severity.MEDIUM,
                "Report generation takes too long for large datasets. Users may experience timeouts. The algorithm is not optimized for scalability.",
                "Optimize report generation logic. Consider using streaming or pagination. Profile performance with large datasets."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\EmailScheduler.java",
                134,
                Issue.Severity.LOW,
                "Scheduled emails may overlap when multiple instances run. This can result in duplicate notifications. Consider synchronizing tasks across instances.",
                "Implement distributed locks or coordination. Log duplicates for monitoring. Review scheduler configuration to avoid overlaps."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\UserProfileService.java",
                47,
                Issue.Severity.MEDIUM,
                "Profile picture upload does not validate file type. Users could upload unsupported formats. This may cause errors in the image processing module.",
                "Validate uploaded file types. Restrict size and format. Provide clear error messages to users for unsupported files."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\SessionManager.java",
                91,
                Issue.Severity.CRITICAL,
                "Sessions are not invalidated after logout. This could allow unauthorized access. Security policies are not enforced correctly.",
                "Invalidate sessions on logout. Implement proper session timeout. Review authentication flow for security compliance."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\OrderValidator.java",
                66,
                Issue.Severity.LOW,
                "Query strings are constructed using string concatenation, which risks SQL injection attacks. Input parameters are not sanitized. The database may throw errors with malformed input. Performance may degrade with large datasets. Indexing is missing on key columns. Error handling is inconsistent across repository methods. This could lead to corrupted or incomplete data retrieval.",
                "Add comprehensive validation for all fields. Return clear error messages. Include unit tests for edge cases."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\AnalyticsService.java",
                158,
                Issue.Severity.MEDIUM,
                "Data aggregation logic is duplicated in multiple methods. This increases maintenance complexity. Changes in one method may not reflect in others.",
                "Refactor to reuse aggregation logic in a single method. Add unit tests to verify correctness. Document shared logic for future developers."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\NotificationHandler.java",
                73,
                Issue.Severity.LOW,
                "Notification retries are not limited. This could flood users with repeated messages. No exponential backoff is implemented.",
                "Implement retry limits and exponential backoff. Log failed attempts. Notify users only once per failure."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\FileParser.java",
                109,
                Issue.Severity.CRITICAL,
                "File uploads are not validated for file type or size. Users can upload unsupported formats. Malformed files can cause exceptions during processing. There is no logging for invalid uploads. The system does not provide clear feedback to users. Security checks for malicious files are missing. This could expose the application to remote code execution risks.",
                "Add proper exception handling for malformed files. Log detailed error messages. Validate input format before processing."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\ConfigLoader.java",
                37,
                Issue.Severity.MEDIUM,
                "Configuration values are not validated. Invalid configurations could break the application. Default values are not clearly documented.",
                "Validate file types and sizes before processing. Reject unsupported or potentially malicious files. Provide clear feedback to users on invalid uploads. Implement proper exception handling during file parsing. Log all invalid attempts for auditing purposes. Use secure storage for uploaded files. Add automated tests to cover file validation logic."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\DatabaseConnector.java",
                121,
                Issue.Severity.CRITICAL,
                "Database connections are not closed properly. This may lead to connection leaks. Application may fail under high load.",
                "Use try-with-resources to ensure connections are closed. Add monitoring for open connections. Review all database access points for leaks."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\LoggerUtils.java",
                59,
                Issue.Severity.LOW,
                "Logging statements are inconsistent across modules. Some important events may not be logged. This reduces traceability.",
                "Standardize logging format and levels. Ensure key operations are logged. Review existing logs for gaps."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\EmailTemplateManager.java",
                144,
                Issue.Severity.MEDIUM,
                "Email templates are duplicated in multiple classes. This makes maintenance error-prone. Updating templates requires changes in several places.",
                "Centralize templates in a single location. Use placeholders for dynamic content. Document template usage across the system."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\TaskScheduler.java",
                82,
                Issue.Severity.LOW,
                "Error messages returned by the API are vague and unhelpful. Users cannot determine what went wrong. Validation rules are not consistently applied. Exceptions leak internal details in some cases. Logging does not provide enough context for debugging. The API does not follow a standardized error format. Client applications may fail to handle errors correctly.",
                "Add synchronization or locking mechanisms. Test scheduler under concurrent load. Document expected execution order."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\PaymentProcessor.java",
                197,
                Issue.Severity.CRITICAL,
                "Payment amounts are not validated for negative or zero values. This could lead to incorrect transactions. Financial loss may occur.",
                "Implement proper synchronization for asynchronous tasks. Ensure exceptions are propagated to the caller. Log task execution and failures clearly. Define and enforce task dependencies. Test concurrent execution thoroughly. Consider using thread-safe data structures. Optimize task scheduling to prevent bottlenecks."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\UserSettingsService.java",
                53,
                Issue.Severity.MEDIUM,
                "User settings are not persisted consistently. Some changes may be lost after restart. There is no transaction management.",
                "Handle missing or null values in all aggregation functions. Validate input data before processing. Ensure downstream services receive consistent metrics. Implement proper exception handling. Refactor aggregation logic for readability and maintainability. Add unit and integration tests for accuracy. Document assumptions and limitations of aggregation algorithms."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\ImageProcessor.java",
                110,
                Issue.Severity.LOW,
                "Image resizing can distort aspect ratio. This affects display quality. No quality checks are performed.",
                "Maintain aspect ratio when resizing images. Add quality checks for output. Consider using high-quality scaling algorithms."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\ApiRequestHandler.java",
                77,
                Issue.Severity.MEDIUM,
                "Input parameters are not sanitized before processing. This may allow injection attacks. Logging is insufficient for debugging.",
                "Sanitize all inputs before use. Improve logging for API requests. Add unit tests for edge cases."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\CacheCleaner.java",
                133,
                Issue.Severity.CRITICAL,
                "The caching mechanism does not handle expiration correctly. Stale data may be returned to users. Concurrent access can lead to inconsistent state. Cache keys are not namespaced, causing collisions. Eviction policies are not well-defined. Logging for cache hits and misses is insufficient. The system may consume unnecessary memory over time.",
                "Implement checks before deleting cache entries. Schedule cleanup during low activity periods. Log all deletion operations."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\UserService.java",
                68,
                Issue.Severity.MEDIUM,
                "User input is not sanitized before database insertion. This may lead to SQL injection. Error handling is insufficient for invalid input.",
                "Sanitize all user inputs before processing. Add proper exception handling. Include automated tests for input validation."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\AuthController.java",
                95,
                Issue.Severity.CRITICAL,
                "Session tokens are not invalidated on logout. This can allow unauthorized access. There is no expiration policy enforced.",
                "Invalidate session tokens immediately on logout. Implement token expiration. Log logout events for security monitoring."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\PaymentProcessor.java",
                180,
                Issue.Severity.MEDIUM,
                "Currency conversion is performed without rounding. This may lead to minor discrepancies. Financial reports may be affected.",
                "Validate all configuration parameters at startup. Provide default values for missing or invalid entries. Store sensitive configuration securely. Add monitoring and alerting for configuration errors. Document expected configuration formats clearly. Write tests to verify configuration loading. Ensure the system behaves predictably under invalid configurations."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\EmailService.java",
                132,
                Issue.Severity.LOW,
                "Emails are sent without checking recipient preferences. Users may receive unwanted messages. This may affect engagement.",
                "Check user notification preferences before sending emails. Add opt-out handling. Log messages sent to respect privacy policies."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\Repository.java",
                77,
                Issue.Severity.MEDIUM,
                "Large data sets are processed entirely in memory. This can cause out-of-memory errors. Processing time increases exponentially with dataset size. There is no batching or streaming mechanism. Logging does not provide sufficient information about processing stages. Exceptions during processing are not handled gracefully. The system may crash under high load, affecting availability.",
                "Add proper indexes to frequently queried columns. Optimize queries for performance. Test response times under load."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\NotificationService.java",
                59,
                Issue.Severity.LOW,
                "Notifications are sent in plain text. Sensitive information may be exposed. Formatting is inconsistent across messages.",
                "Process large datasets in smaller batches or using streaming. Monitor memory usage to avoid out-of-memory errors. Optimize algorithms for better performance. Add logging to track processing progress. Handle exceptions gracefully to avoid crashes. Write unit tests for edge cases. Document expected dataset sizes and limitations."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\FileParser.java",
                101,
                Issue.Severity.CRITICAL,
                "Parser does not handle malformed files correctly. Application may crash when encountering invalid input. No error recovery is implemented.",
                "Add robust exception handling for file parsing. Validate file structure before processing. Log errors with clear messages for debugging."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\CacheManager.java",
                47,
                Issue.Severity.MEDIUM,
                "Cache refresh is not atomic. Concurrent access may lead to stale data. Race conditions can cause inconsistent state.",
                "Implement atomic refresh operations. Synchronize access to shared cache. Test concurrency scenarios to ensure consistency."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\AnalyticsService.java",
                163,
                Issue.Severity.CRITICAL,
                "Data aggregation does not account for missing values. Results may be inaccurate. Downstream reporting may be affected.",
                "Handle missing values explicitly in aggregation. Validate input datasets. Document assumptions for reporting purposes."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\OrderValidator.java",
                72,
                Issue.Severity.LOW,
                "Validation messages are generic and unhelpful. Users may not understand why input is rejected. Error codes are missing.",
                "Provide specific validation messages for each field. Include error codes. Improve user guidance for correction."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\UserService.java",
                54,
                Issue.Severity.MEDIUM,
                "User creation does not check for duplicate emails. This may result in multiple accounts with the same email. Data integrity could be compromised.",
                "Validate uniqueness of email before creating a user. Add database constraints for email. Notify users if email is already registered."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\AuthController.java",
                110,
                Issue.Severity.CRITICAL,
                "Login attempts are not rate-limited. This can enable brute-force attacks. No alerting is configured for suspicious activity.",
                "Implement rate limiting for login attempts. Monitor and log suspicious activity. Alert administrators for repeated failed logins."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\PaymentProcessor.java",
                205,
                Issue.Severity.MEDIUM,
                "Refund calculation does not include taxes properly. Customers may receive incorrect refund amounts. Reports may show inconsistent totals.",
                "Include taxes in refund calculation. Add unit tests for edge cases. Update documentation to reflect the correct logic."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\EmailService.java",
                141,
                Issue.Severity.LOW,
                "Email attachments are not scanned for viruses. This could expose users to malware. No warnings are displayed.",
                "Implement attachment scanning before sending. Notify users of detected threats. Log suspicious attachments for auditing."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\Repository.java",
                82,
                Issue.Severity.MEDIUM,
                "Lazy loading is used without proper transaction management. This may cause LazyInitializationException. Fetching data could fail unexpectedly.",
                "Ensure transactions are active when lazy-loading data. Consider eager loading where appropriate. Add unit tests for data access."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\NotificationService.java",
                64,
                Issue.Severity.LOW,
                "Notifications are retried indefinitely on failure. This can flood the system. Users may receive duplicate notifications.",
                "Implement retry limits with backoff strategy. Log failed attempts for monitoring. Notify administrators if repeated failures occur."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\FileParser.java",
                115,
                Issue.Severity.CRITICAL,
                "Parsing large files can exhaust memory. Application may crash under high load. No streaming or chunking is implemented.",
                "Use streaming to process files in chunks. Monitor memory usage during parsing. Add tests for large file handling."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\CacheManager.java",
                52,
                Issue.Severity.MEDIUM,
                "Configuration values are not validated on startup. Invalid configurations may prevent the application from running correctly. Defaults are not documented clearly. Sensitive values are stored in plain text. Missing validation can lead to runtime exceptions. There is no monitoring for configuration errors. System behavior under invalid configuration is unpredictable.",
                "Ensure cache expiration and eviction policies are implemented correctly. Use namespaced cache keys to avoid collisions. Synchronize cache updates across concurrent threads. Log cache hits and misses consistently. Monitor memory usage for potential leaks. Add unit tests to verify cache behavior. Review and optimize caching strategies regularly."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\AnalyticsService.java",
                178,
                Issue.Severity.CRITICAL,
                "Aggregated metrics are calculated incorrectly when input data is missing. Reports may be inaccurate. Business decisions could be affected.",
                "Validate all input data before aggregation. Handle missing values explicitly. Add automated tests to verify metric accuracy."
        ));
        list.add(new Issue(
                "src\\com\\mock\\data\\OrderValidator.java",
                79,
                Issue.Severity.LOW,
                "Asynchronous tasks are executed without proper synchronization. Concurrent execution may produce inconsistent results. Exceptions are not propagated back to the caller. Logging is insufficient for debugging. Task dependencies are not clearly defined. There is a risk of race conditions under heavy load. Performance may degrade if multiple tasks run simultaneously.",
                "Provide detailed and actionable error messages for API responses. Apply consistent validation rules across all endpoints. Avoid exposing internal exception details. Standardize error response formats. Log sufficient context for debugging. Test API clients to handle errors properly. Update documentation to describe error handling policies."
        ));
        
        String summary = """
                         The project is a web-based e-commerce platform designed to manage product listings, handle customer orders, and provide analytics. Its architecture follows a layered design with distinct presentation, business logic, and data access layers, though some modules exhibit tight coupling and inconsistent adherence to design patterns. Core technologies include Java, TypeScript, frameworks such as Spring Boot, React, and libraries for common tasks, e.g. data serialization, logging.
                         
                         The system relies on a PostgreSQL and integrates with external APIs for payment processing and messaging services.
                         
                         In terms of code organization, there are opportunities to improve modularity and reusability; some components violate the separation of concerns principle, leading to complex interdependencies. Error handling is inconsistent, with some critical flows lacking proper exception management. Unit and integration testing coverage is partial, and automated tests could be expanded to improve reliability. Configuration management follows environment-based profiles, though sensitive credentials require improved handling and encryption.
                         
                         Security review indicates potential concerns in password storage, API authentication, and data protection measures. Performance analysis reveals bottlenecks in database queries or data processing loops, suggesting caching and query optimization as potential improvements.
                         
                         Refactoring suggestions include adopting stricter architectural patterns, enhancing code modularity, and introducing shared utility libraries. Code quality could be improved with consistent naming conventions, adherence to SOLID principles, and removal of redundant logic. Scalability enhancements may involve decoupling services, adding asynchronous processing, and leveraging caching layers.
                         
                         Overall, the project demonstrates a functional architecture with core technologies and frameworks effectively utilized. Key findings highlight areas for improved code structure, enhanced security, better error handling, testing coverage, and performance optimization.\
                         """;
        
        return new ScanFullReview(
                "00000000-0000-0000-0000-000000000000",
                "https://sample.entry/someUser/someExampleProject",
                OffsetDateTime.now(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm 'UTC'x")),
                48,
                197,
                110,
                62,
                25,
                34867,
                BigDecimal.valueOf(12031),
                summary,
                list
        );
    }
}

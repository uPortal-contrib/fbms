package org.apereo.portal.fbms.data;

/**
 * Implementations of {@link ExtensionFilter} may throw exceptions of this type in their
 * <code>doFilter</code> method to indicate that the opperation must not proceed, but <strong>only
 * before <code>chain.doFilter()</code> is called</strong>.  Most commonly these exceptions signal
 * field validation issues.
 *
 * <p>In addition to the standard <code>message</code> property, exceptions of this type may
 * optionally include a <code>feedback</code> property that will be communicated to the front-end
 * for possible display to users.
 */
public class FilterChainAbortException extends RuntimeException {

    private final String feedback;

    public FilterChainAbortException() {
        this.feedback = null;
    }

    public FilterChainAbortException(String message) {
        super(message);
        this.feedback = null;
    }

    public FilterChainAbortException(String message, Throwable cause) {
        super(message, cause);
        this.feedback = null;
    }

    public FilterChainAbortException(Throwable cause) {
        super(cause);
        this.feedback = null;
    }

    public FilterChainAbortException(String message, String feedback) {
        super(message);
        this.feedback = feedback;
    }

    public FilterChainAbortException(String message, Throwable cause, String feedback) {
        super(message, cause);
        this.feedback = feedback;
    }

    public FilterChainAbortException(Throwable cause, String feedback) {
        super(cause);
        this.feedback = feedback;
    }

    /**
     * Message primarily intended for user viewing.
     */
    public String getFeedback() {
        return feedback;
    }

}

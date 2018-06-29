package org.apereo.portal.fbms.data;

import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * Provide an implementation of this interface to inject custom logic into FBMS when data is
 * created, read, updated, or deleted.  Filter objects always surround repository interactions.
 * You can execute custom logic either before or after the repository does it's part.
 *
 * <p>Filters may throw exceptions <i>before</i> repository operations to cancel them.
 */
public interface FbmsFilter <E extends FbmsEntity> extends Ordered {

    /*
     * Helper constants for ordering;  filters take effect in the reverse of the eorder in which
     * they are invoked.
     */
    int ORDER_FIRST = Integer.MAX_VALUE;
    int ORDER_VERY_EARLY = 100;
    int ORDER_EARLY = 10;
    int ORDER_NORMAL = 0;
    int ORDER_LATE = -10;
    int ORDER_VERY_LATE = -100;
    int ORDER_LAST = Integer.MIN_VALUE;

    /**
     * This method allows {@link SubmissionFilter} to indicate whether they apply to the specified
     * request and/or entity.  The decision can be based on HTTP method, URI, the user who sent it,
     * etc.  Filters that do not apply are not included in the filter chain.
     */
    boolean appliesTo(HttpServletRequest request, FbmsEntity entity);

    /**
     * Invoke custom logic when an entity is created, read, updated, or deleted.
     */
    E doFilter(HttpServletRequest request, E entity, FbmsFilterChain<E> chain);

}

package org.apereo.portal.fbms.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Component
public class FilterChainBuilder {

    @Autowired
    private List<FbmsFilter> filters;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init() {
        logger.info("Found the following FbmsFilter beans:  {}", filters);
    }

    /**
     * Builds a filter chain based on a <code>Supplier</code>.
     */
    public <E extends FbmsEntity> Supplier<E> fromSupplier(HttpServletRequest request, final Supplier<E> callback) {

        /*
         * Wrap the callback in a FbmsFilterChain at the center of the "onion."
         */
        FbmsFilterChain<E> chain = entity1 -> callback.get();

        /*
         * Add layers of the onion
         */
        for (FbmsFilter filter : filters) {
            final boolean applies = filter.appliesTo(request, null);
            logger.debug("FbmsFilter bean {} {} apply to request with URI='{}' and method='{}'",
                    filter,
                    applies ? "DOES" : "DOES NOT",
                    request.getRequestURI(),
                    request.getMethod());
            if (applies) {
                chain = new FbmsFilterChainImpl(filter, request, chain);
            }
        }

        /*
         * Decorate the whole business in a Supplier
         */
        final FbmsFilterChain<E> rslt = chain;
        return () -> rslt.doFilter(null);

    }

    public <E extends FbmsEntity> Supplier<E> fromUnaryOperator(HttpServletRequest request, E entity, final UnaryOperator<E> callback) {

        /*
         * Wrap the callback in a FbmsFilterChain at the center of the "onion."
         */
        FbmsFilterChain<E> chain = entity1 -> callback.apply(entity);

        /*
         * Add layers of the onion
         */
        for (FbmsFilter filter : filters) {
            final boolean applies = filter.appliesTo(request, entity);
            logger.debug("FbmsFilter bean {} {} apply to request with URI='{}', method='{}', and entity='{}'",
                    filter,
                    applies ? "DOES" : "DOES NOT",
                    request.getRequestURI(),
                    request.getMethod(),
                    entity);
            if (applies) {
                chain = new FbmsFilterChainImpl(filter, request, chain);
            }
        }

        /*
         * Decorate the whole business in a Supplier
         */
        final FbmsFilterChain<E> rslt = chain;
        return () -> rslt.doFilter(entity);

    }

    /*
     * Nested Types
     */

    private static final class FbmsFilterChainImpl<E extends FbmsEntity> implements FbmsFilterChain<E> {

        private final FbmsFilter<E> enclosed;
        private final HttpServletRequest request;
        private final FbmsFilterChain<E> nextLink;

        /* package-private */ FbmsFilterChainImpl(FbmsFilter<E> enclosed, HttpServletRequest request, FbmsFilterChain<E> nextLink) {
            this.enclosed = enclosed;
            this.request = request;
            this.nextLink = nextLink;
        }

        @Override
        public final E doFilter(E entity) {
            return enclosed.doFilter(request, entity, nextLink);
        }

    }

}

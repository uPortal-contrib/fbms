package org.apereo.portal.fbms.data;

import org.apereo.portal.fbms.data.filter.AbstractExtensionFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.core.OrderComparator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class tests the {@link ExtensionFilterChainBuilder} to insure it builds a chain that
 * contains the correct filters and in the correct order.
 */
@RunWith(JUnitPlatform.class)
public class ExtensionFilterChainBuilderTests extends ExtensionFilterChainBuilder {

    private List<ExtensionFilter> accumulatedFilters = new ArrayList<>();

    private ExtensionFilter<FormEntity> veryEarlyAllEntities =
            new AbstractTestExtensionFilter<FormEntity>("veryEarlyAllEntities", ExtensionFilter.ORDER_VERY_EARLY) {
        @Override
        public boolean appliesTo(ExtensionFilterChainMetadata metadata, FbmsEntity entity, HttpServletRequest request) {
            return true; // Always applies
        }
    };

    private ExtensionFilter<FormEntity> normalFormOnly =
            new AbstractTestExtensionFilter<FormEntity>("normalFormOnly", ExtensionFilter.ORDER_NORMAL) {
        @Override
        public boolean appliesTo(ExtensionFilterChainMetadata metadata, FbmsEntity entity, HttpServletRequest request) {
            return metadata.getEntityClass().equals(FormEntity.class); // Forms only
        }
    };

    private ExtensionFilter<FormEntity> normalSubmissionsOnly =
            new AbstractTestExtensionFilter<FormEntity>("normalSubmissionsOnly", ExtensionFilter.ORDER_NORMAL) {
        @Override
        public boolean appliesTo(ExtensionFilterChainMetadata metadata, FbmsEntity entity, HttpServletRequest request) {
            return metadata.getEntityClass().equals(SubmissionEntity.class); // Forms only
        }
    };

    private ExtensionFilter<FormEntity> lateAllEntities =
            new AbstractTestExtensionFilter<FormEntity>("lateAllEntities", ExtensionFilter.ORDER_LATE) {
        @Override
        public boolean appliesTo(ExtensionFilterChainMetadata metadata, FbmsEntity entity, HttpServletRequest request) {
            return true; // Always applies
        }
    };

    private ExtensionFilter<FormEntity> veryLateAllEntities =
            new AbstractTestExtensionFilter<FormEntity>("veryLateAllEntities", ExtensionFilter.ORDER_VERY_LATE) {
        @Override
        public boolean appliesTo(ExtensionFilterChainMetadata metadata, FbmsEntity entity, HttpServletRequest request) {
            return true; // Always applies
        }
    };

    private ExtensionFilterChainMetadata formMetedata;

    private ExtensionFilterChainMetadata submissionMetedata;

    private HttpServletRequest request = mock(HttpServletRequest.class);

    private HttpServletResponse response = mock(HttpServletResponse.class);

    @BeforeEach
    public void beforeEach() {

        final List<ExtensionFilter> filters = Arrays.asList(
                veryEarlyAllEntities,
                normalFormOnly,
                normalSubmissionsOnly,
                lateAllEntities,
                veryLateAllEntities
        );
        filters.sort(new OrderComparator());
        setFilters(filters);

        formMetedata = mock(ExtensionFilterChainMetadata.class);
        when(formMetedata.getEntityClass())
                .thenAnswer(invocation -> FormEntity.class);

        submissionMetedata = mock(ExtensionFilterChainMetadata.class);
        when(submissionMetedata.getEntityClass())
                .thenAnswer(invocation -> SubmissionEntity.class);

    }

    @Test
    public void fromSupplierFormEntityTest() {
        final Supplier<? extends FbmsEntity> seed = () -> null;
        final Supplier<? extends FbmsEntity> supplier = fromSupplier(formMetedata, request, response, seed);
        supplier.get();

        assertArrayEquals(
                new Object[] { veryEarlyAllEntities, normalFormOnly, lateAllEntities, veryLateAllEntities },
                accumulatedFilters.toArray()
        );
    }

    @Test
    public void fromUnaryOperatorSubmissionEntityTest() {
        final UnaryOperator<? extends FbmsEntity> seed = entity -> entity;
        final Supplier<? extends FbmsEntity> supplier = fromUnaryOperator(submissionMetedata, null, request, response, seed);
        supplier.get();

        assertArrayEquals(
                new Object[] { veryEarlyAllEntities, normalSubmissionsOnly, lateAllEntities, veryLateAllEntities },
                accumulatedFilters.toArray()
        );
    }

    /*
     * Nested Types
     */

    private /* non-static */ abstract class AbstractTestExtensionFilter<E extends FbmsEntity> extends AbstractExtensionFilter<E> {

        private final String name;

        public AbstractTestExtensionFilter(String name, int order) {
            super(order);
            this.name = name;
        }

        @Override
        public final E doFilter(E entity, HttpServletRequest request, HttpServletResponse response, ExtensionFilterChain<E> chain) {
            /*
             * All filters in this test class do the same thing when applied:
             *
             *   - Add themselves to the list
             *   - Invoke the rest of the chain
             */
            accumulatedFilters.add(this);
            return chain.doFilter(entity);
        }

        @Override
        public String toString() {
            return name;
        }

    }

}

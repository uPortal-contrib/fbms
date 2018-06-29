package org.apereo.portal.fbms.data.filter;

import org.apereo.portal.fbms.data.FbmsEntity;
import org.apereo.portal.fbms.data.FbmsFilter;
import org.apereo.portal.fbms.data.SubmissionFilter;

/**
 * Convenient base class for {@link SubmissionFilter} implementations that handles ordering.
 */
public abstract class AbstractFbmsFilter <E extends FbmsEntity> implements FbmsFilter<E> {

    private final int order;

    protected AbstractFbmsFilter() {
        this.order = FbmsFilter.ORDER_NORMAL;
    }

    protected AbstractFbmsFilter(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

}

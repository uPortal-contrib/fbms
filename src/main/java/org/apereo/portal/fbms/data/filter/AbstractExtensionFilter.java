package org.apereo.portal.fbms.data.filter;

import org.apereo.portal.fbms.data.FbmsEntity;
import org.apereo.portal.fbms.data.ExtensionFilter;

/**
 * Convenient base class for {@link ExtensionFilter} implementations that handles ordering.
 */
public abstract class AbstractExtensionFilter<E extends FbmsEntity> implements ExtensionFilter<E> {

    private final int order;

    protected AbstractExtensionFilter() {
        this.order = ExtensionFilter.ORDER_NORMAL;
    }

    protected AbstractExtensionFilter(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

}

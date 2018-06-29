package org.apereo.portal.fbms.data;

public interface FbmsFilterChain<E extends FbmsEntity> {

    E doFilter(E entity);

}

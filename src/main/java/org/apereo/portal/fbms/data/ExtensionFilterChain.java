package org.apereo.portal.fbms.data;

public interface ExtensionFilterChain<E extends FbmsEntity> {

    E doFilter(E entity);

}

package org.apereo.portal.fbms.data;

/**
 * Encapsulates some essential information about an operation invoked via an
 * {@link ExtensionFilterChain}.
 */
public class ExtensionFilterChainMetadata {

    private final String fname;

    private final Class<? extends FbmsEntity> entityClass;

    public ExtensionFilterChainMetadata(String fname, Class<? extends FbmsEntity> entityClass) {
        this.fname = fname;
        this.entityClass = entityClass;
    }

    /**
     * Returns the fname of the relevant form (if the operation targets a form or a  submission), or
     * <code>null</code> otherwise.
     */
    public String getFname() {
        return fname;
    }

    public Class<? extends FbmsEntity> getEntityClass() {
        return entityClass;
    }

}

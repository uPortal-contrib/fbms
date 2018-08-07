package org.apereo.portal.fbms.data;

/**
 * Encapsulates some essential information about an operation invoked via an
 * {@link ExtensionFilterChain}.
 */
public class ExtensionFilterChainMetadata {

    private final String formFname;

    private final Class<? extends FbmsEntity> entityClass;

    public ExtensionFilterChainMetadata(String formFname, Class<? extends FbmsEntity> entityClass) {
        this.formFname = formFname;
        this.entityClass = entityClass;
    }

    public String getFormFname() {
        return formFname;
    }

    public Class<? extends FbmsEntity> getEntityClass() {
        return entityClass;
    }

}

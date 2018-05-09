package org.jasig.portal.fbms;

import java.util.UUID;

/**
 * Forms in FBMS are tree structures that support nested elements to arbitrary depth.  This
 * interface defines the root (or trunk) of that structure.
 */
public interface Form {

    UUID getUuid();

    int getVersion();

}

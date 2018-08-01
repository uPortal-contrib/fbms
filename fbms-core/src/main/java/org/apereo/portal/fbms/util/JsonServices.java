package org.apereo.portal.fbms.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Utility methods for manipulating <code>JsonNode</code> instances (Jackson).
 */
@Service
public class JsonServices {

    private enum SupportedType {

        BIG_DECIMAL {
            @Override
            public boolean appliesTo(Object value) {
                return BigDecimal.class.isInstance(value);
            }
            @Override
            public void put(ObjectNode node, String propertyName, Object value) {
                node.put(propertyName, (BigDecimal) value);
            }
            @Override
            public void add(ArrayNode node, Object value) {
                node.add((BigDecimal) value);
            }
        },

        BOOLEAN{
            @Override
            public boolean appliesTo(Object value) {
                return Boolean.class.isInstance(value);
            }
            @Override
            public void put(ObjectNode node, String propertyName, Object value) {
                node.put(propertyName, (Boolean) value);
            }
            @Override
            public void add(ArrayNode node, Object value) {
                node.add((Boolean) value);
            }
        },

        DOUBLE{
            @Override
            public boolean appliesTo(Object value) {
                return Double.class.isInstance(value);
            }
            @Override
            public void put(ObjectNode node, String propertyName, Object value) {
                node.put(propertyName, (Double) value);
            }
            @Override
            public void add(ArrayNode node, Object value) {
                node.add((Double) value);
            }
        },

        FLOAT{
            @Override
            public boolean appliesTo(Object value) {
                return Float.class.isInstance(value);
            }
            @Override
            public void put(ObjectNode node, String propertyName, Object value) {
                node.put(propertyName, (Float) value);
            }
            @Override
            public void add(ArrayNode node, Object value) {
                node.add((Float) value);
            }
        },

        INTEGER{
            @Override
            public boolean appliesTo(Object value) {
                return Integer.class.isInstance(value);
            }
            @Override
            public void put(ObjectNode node, String propertyName, Object value) {
                node.put(propertyName, (Integer) value);
            }
            @Override
            public void add(ArrayNode node, Object value) {
                node.add((Integer) value);
            }
        },

        LONG{
            @Override
            public boolean appliesTo(Object value) {
                return Long.class.isInstance(value);
            }
            @Override
            public void put(ObjectNode node, String propertyName, Object value) {
                node.put(propertyName, (Long) value);
            }
            @Override
            public void add(ArrayNode node, Object value) {
                node.add((Long) value);
            }
        },

        SHORT{
            @Override
            public boolean appliesTo(Object value) {
                return Short.class.isInstance(value);
            }
            @Override
            public void put(ObjectNode node, String propertyName, Object value) {
                node.put(propertyName, (Short) value);
            }
            @Override
            public void add(ArrayNode node, Object value) {
                node.add((Short) value);
            }
        },

        STRING{
            @Override
            public boolean appliesTo(Object value) {
                return String.class.isInstance(value);
            }
            @Override
            public void put(ObjectNode node, String propertyName, Object value) {
                node.put(propertyName, (String) value);
            }
            @Override
            public void add(ArrayNode node, Object value) {
                node.add((String) value);
            }
        },

        ARRAY {
            @Override
            public boolean appliesTo(Object value) {
                return value != null && value.getClass().isArray();
            }
            @Override
            public void put(ObjectNode node, String propertyName, Object value) {
                final ArrayNode arrayNode = node.putArray(propertyName);

                final Object[] values = (Object[]) value;
                for (Object o : values) {
                    final SupportedType supportedType = SupportedType.selectForValue(o);
                    if (supportedType != null) {
                        supportedType.add(arrayNode, o);
                    } else {
                        throw new IllegalArgumentException("Value type not supported:  " + value.getClass().getName());
                    }
                }
            }
            @Override
            public void add(ArrayNode node, Object value) {
                throw new UnsupportedOperationException("Cannot call add on SupportedType.ARRAY itself");
            }
        };

        public static SupportedType selectForValue(Object value) {
            return Arrays.stream(SupportedType.values())
                    .filter(type -> type.appliesTo(value))
                    .findFirst()
                    .orElse(null);
        }

        public abstract boolean appliesTo(Object value);

        /**
         * For ObjectNode nodes.
         */
        public abstract void put(ObjectNode node, String propertyName, Object value);

        /**
         * For ArrayNode nodes.
         */
        public abstract void add(ArrayNode node, Object value);

    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Updates the specified path to the specified value, creating elements of that path as necessary.
     */
    public void setValueAtPath(JsonNode originNode, String jsonPointer, Object value) {

        // Assertions
        if (originNode == null) {
            throw new IllegalArgumentException("Argument 'originNode' cannot be null");
        }
        if (!JsonNodeType.OBJECT.equals(originNode.getNodeType())
                && !JsonNodeType.ARRAY.equals(originNode.getNodeType())) {
            throw new IllegalArgumentException("Argument 'originNode' must be an ObjectNode or an ArrayNode;  was " + originNode.getNodeType());
        }
        if (jsonPointer == null) {
            throw new IllegalArgumentException("Argument 'jsonPointer' cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Argument 'value' cannot be null");
        }

        // The node we ultimately want to adjust
        ObjectNode targetNode = (ObjectNode) originNode;

        final List<String> pathTokens = Arrays.asList(jsonPointer.split("/"));
        logger.debug("Found the following pathTokens for jsonPointer='{}':  {}", jsonPointer, pathTokens);

        for (String token : pathTokens.subList(0, pathTokens.size() - 1)) {
            if (StringUtils.isNotBlank(token)) {
                // The leading slash adds an empty token at the beginning
                targetNode = targetNode.with(token);
            }
        }
        logger.debug("Found the following targetNode for jsonPointer='{}':  {}", jsonPointer, targetNode);

        final String propertyName = pathTokens.get(pathTokens.size() - 1);

        // We have to know the runtime type of the value...
        final SupportedType supportedType = SupportedType.selectForValue(value);

        if (supportedType != null) {
            supportedType.put(targetNode, propertyName, value);
        } else {
            throw new IllegalArgumentException("Value type not supported:  " + value.getClass().getName());
        }

    }

    /**
     * Where the node specified by <code>jsonPointer</code> is an <code>ArrayNode</code>, adds the
     * specified values to existing values (if any) instead of replacing them.
     */
    public void addValuesToArray(JsonNode originNode, String jsonPointer, Object... values) {

        // Assertions
        if (originNode == null) {
            throw new IllegalArgumentException("Argument 'originNode' cannot be null");
        }
        if (!JsonNodeType.OBJECT.equals(originNode.getNodeType())
                && !JsonNodeType.ARRAY.equals(originNode.getNodeType())) {
            throw new IllegalArgumentException("Argument 'originNode' must be an ObjectNode or an ArrayNode;  was " + originNode.getNodeType());
        }
        if (jsonPointer == null) {
            throw new IllegalArgumentException("Argument 'jsonPointer' cannot be null");
        }
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Argument 'values' cannot be null or empty");
        }

        // Obtain the existingValues if any...
        final JsonNode targetNode = originNode.at(jsonPointer);
        if (targetNode.isMissingNode()) {
            // Set as normal...
            setValueAtPath(originNode, jsonPointer, values);
        } else if (!JsonNodeType.ARRAY.equals(targetNode.getNodeType())) {
            throw new IllegalArgumentException("The node specified by jsonPointer is not an ArrayNode:  " + jsonPointer);
        } else {
            final ArrayNode arrayNode = (ArrayNode) targetNode;
            for (Object o : values) {
                final SupportedType supportedType = SupportedType.selectForValue(o);
                if (supportedType != null) {
                    supportedType.add(arrayNode, o);
                } else {
                    throw new IllegalArgumentException("Value type not supported:  " + o.getClass().getName());
                }
            }
        }


    }


}

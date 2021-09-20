package com.banelco.empresas.util.security;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SecurityUtil {
    public static final String[] WIPED_ATTRS = new String[]{"password"};
    public static final String[] MASKED_ATTRS = new String[]{"cardNumber", "card_number"};
    private static final String REPLACED_CHAR = "_";
    private static final String WIPED = "[WIPED]";
    private static final int MAX_JSON_ENTRIES = 20;
    private static final int MAX_ENTITY_SIZE = MAX_JSON_ENTRIES * 1024;

    private SecurityUtil() {
    }

    public static String wipe() {
        return WIPED;
    }

    public static String protect(String s) {
        if (StringUtils.isBlank(s))
            return "";
        if (s.length() < 5)
            return StringUtils.rightPad("", s.length(), REPLACED_CHAR);
        int l = 0;
        int r = 0;
        if (s.length() < 15) {
            l = r = (s.length() - 4) / 2;
            if ((s.length() % 2) != 0)
                r += 1;
        } else {
            l = 6;
            r = 4;
        }
        return StringUtils.replace(s, s.substring(l, s.length() - r),
                StringUtils.repeat(REPLACED_CHAR, s.length() - r - l));
    }
    
    public static String wipeAndProtectData(String json) {
    	if(json == null || json.isEmpty())
    		return "";
    	if(json.length() > MAX_ENTITY_SIZE)
    		return wipe();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(json);
			if(rootNode.size() > MAX_JSON_ENTRIES) {
				return wipe();
			}
			if(rootNode.isArray()) {
				wipeArrayNode((ArrayNode)rootNode);
			} else if (rootNode.isObject()) {
				wipeObjectNode((ObjectNode)rootNode);
			}
			return rootNode.toString();
		} catch(Exception ex) {
			// probably not json
			if (StringUtils.isBlank(json))
				return json;
			try {
				StringBuilder sb = new StringBuilder();
				List<String> requestData = Arrays.asList(json.split("&"));
				for (String string : requestData) {
					String[] parts = string.split("=");
					sb.append(parts[0]);
					sb.append("=");
					if (Arrays.asList(WIPED_ATTRS).contains(parts[0]))
						sb.append(wipe());
					else if (Arrays.asList(MASKED_ATTRS).contains(parts[0]))
						sb.append(protect(ObjectUtils.toString(parts[1])));
					else
						sb.append(ObjectUtils.toString(parts[1]));
					sb.append("&");
				}
				sb.setLength(sb.length() - 1);
				return sb.toString();
			} catch (Exception e1) {
				return "";
			}
		}
	}
	
	private static void wipeArrayNode(ArrayNode arrayNode) {
		Iterator<JsonNode> it = arrayNode.elements();
		while(it.hasNext()) {
			JsonNode jsonNode = it.next();
			if(jsonNode.isArray()) {
				wipeArrayNode((ArrayNode)jsonNode);
			} else if (jsonNode.isObject()) {
				wipeObjectNode((ObjectNode)jsonNode);
			}
		}
	}
	
	private static void wipeObjectNode(ObjectNode objectNode) {
		for(String wipedAttr : WIPED_ATTRS) {
			if(objectNode.has(wipedAttr)) {
				objectNode.put(wipedAttr, wipe());
			}
		}
		for(String maskedAttr : MASKED_ATTRS) {
			if(objectNode.has(maskedAttr)) {
				objectNode.put(maskedAttr, protect(objectNode.get(maskedAttr).asText()));
			}
		}
		Iterator<JsonNode> it = objectNode.elements();
		while(it.hasNext()) {
			JsonNode jsonNode = it.next();
			if(jsonNode.isArray()) {
				wipeArrayNode((ArrayNode)jsonNode);
			} else if (jsonNode.isObject()) {
				wipeObjectNode((ObjectNode)jsonNode);
			}
		}
	}
}
package com.guns21.user.login.mixin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.guns21.user.login.domain.Role;
import com.guns21.user.login.domain.UserInfo;

import java.io.*;
import java.util.Arrays;
import java.util.List;

class UserInfoDeserializer extends JsonDeserializer<UserInfo> {
    UserInfoDeserializer() {
    }

    public UserInfo deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = (JsonNode) mapper.readTree(jp);
        String id = readJsonNode(jsonNode, "id").asText();
        List<String> managedUserIds;
        if (jsonNode.has("managedUserIds")) {
            managedUserIds = mapper.convertValue(jsonNode.get("managedUserIds"), new TypeReference<List<String>>() { });
        } else {
            managedUserIds = Arrays.asList(id);
        }
        UserInfo role = new UserInfo(
                id,
                readJsonNode(jsonNode, "name").asText(),
                readJsonNode(jsonNode, "nickname").asText(),
                mapper.convertValue(jsonNode.get("roles"), new TypeReference<List<Role>>() { }),
                managedUserIds);
        return role;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return (JsonNode) (jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance());
    }
}

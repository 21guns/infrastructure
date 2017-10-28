package com.guns21.authentication.mixin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.guns21.authentication.api.entity.Role;
import com.guns21.web.entity.UserInfo;

import java.io.*;
import java.util.List;

class UserInfoDeserializer extends JsonDeserializer<UserInfo> {
    UserInfoDeserializer() {
    }

    public UserInfo deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = (JsonNode) mapper.readTree(jp);
        UserInfo role = new UserInfo(readJsonNode(jsonNode, "id").asText(),
                readJsonNode(jsonNode, "name").asText(), readJsonNode(jsonNode, "nickname").asText(),
                mapper.convertValue(jsonNode.get("roles"), new TypeReference<List<Role>>() {
                }));
        return role;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return (JsonNode) (jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance());
    }
}

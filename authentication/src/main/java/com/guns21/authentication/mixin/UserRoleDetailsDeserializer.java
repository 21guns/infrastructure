package com.guns21.authentication.mixin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.guns21.authentication.api.entity.UserRoleDetails;
import com.guns21.web.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

class UserRoleDetailsDeserializer extends JsonDeserializer<User> {
    UserRoleDetailsDeserializer() {
    }

    public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = (JsonNode) mapper.readTree(jp);
        Set<GrantedAuthority> authorities = Collections.EMPTY_SET; //(Set) mapper.convertValue(jsonNode.get("authorities"), new TypeReference<Set<SimpleGrantedAuthority>>() { });
        JsonNode password = this.readJsonNode(jsonNode, "password");
        UserRoleDetails result = new UserRoleDetails(this.readJsonNode(jsonNode, "username").asText(), password.asText(""), this.readJsonNode(jsonNode, "enabled").asBoolean(), this.readJsonNode(jsonNode, "accountNonExpired").asBoolean(), this.readJsonNode(jsonNode, "credentialsNonExpired").asBoolean(), this.readJsonNode(jsonNode, "accountNonLocked").asBoolean(), authorities);
        if (password.asText((String) null) == null) {
            result.eraseCredentials();
        }
        result.setNickname(readJsonNode(jsonNode, "nickname").asText());
        result.setUserId(readJsonNode(jsonNode, "userId").asText());

        result.setOrganizationId(readJsonNode(jsonNode, "organizationId").asText());
        result.setRoles(mapper.convertValue(jsonNode.get("roles"), new TypeReference<List<Role>>() {
        }));
        return result;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return (JsonNode) (jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance());
    }
}
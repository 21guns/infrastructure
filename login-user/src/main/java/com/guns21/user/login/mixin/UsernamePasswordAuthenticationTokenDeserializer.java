//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.guns21.user.login.mixin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.guns21.user.login.domain.UserRoleDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.List;

/**
 * @see org.springframework.security.jackson2.UsernamePasswordAuthenticationTokenDeserializer
 */
public class UsernamePasswordAuthenticationTokenDeserializer extends JsonDeserializer<UsernamePasswordAuthenticationToken> {
    UsernamePasswordAuthenticationTokenDeserializer() {
    }

    public UsernamePasswordAuthenticationToken deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        UsernamePasswordAuthenticationToken token = null;
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = (JsonNode) mapper.readTree(jp);
        Boolean authenticated = this.readJsonNode(jsonNode, "authenticated").asBoolean();
        JsonNode principalNode = this.readJsonNode(jsonNode, "principal");
        Object principal = null;
        if (principalNode.isObject()) {
            principal = mapper.readValue(principalNode.toString(), new TypeReference<UserRoleDetails>() {
            });
        } else {
            principal = principalNode.asText();
        }

        Object credentials = this.readJsonNode(jsonNode, "credentials").asText();
        List<GrantedAuthority> authorities = (List) mapper.readValue(this.readJsonNode(jsonNode, "authorities").toString(), new TypeReference<List<GrantedAuthority>>() {
        });
        if (authenticated.booleanValue()) {
            token = new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
        } else {
            token = new UsernamePasswordAuthenticationToken(principal, credentials);
        }

        token.setDetails(this.readJsonNode(jsonNode, "details"));
        return token;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return (JsonNode) (jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance());
    }
}

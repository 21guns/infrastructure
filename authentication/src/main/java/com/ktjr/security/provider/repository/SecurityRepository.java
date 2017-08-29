package com.ktjr.security.provider.repository;

import com.ktjr.security.api.entity.MyPermission;
import com.ktjr.security.api.entity.MyUser;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jll on 17/6/21.
 */
@Repository
public class SecurityRepository {
    @Value("${com.ktjr.security.permission-redis-key:PERMISSION-REDIS-KEY-USERMANAGE}")
    private String PERMISSION_REDIS_KEY;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private RedisTemplate<String, String> template;

    /**
     * 根据用户名称获取用户信息(MySQL)
     *
     * @param username
     * @return
     */
    public MyUser getUser(String username) {
        EntityManager em = entityManagerFactory.createEntityManager();
        MyUser user = null;

        try {
            Query query = em.createNativeQuery("select ID id, USER_NAME userName, PASSWORD password, SALT salt FROM TB_USERS WHERE USER_NAME = ?1 AND ENABLED=1");
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(MyUser.class));
            query.setParameter(1, username);
            user = (MyUser) query.getSingleResult();
        } catch (NoResultException ex) {
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }

        return user;
    }

    /**
     * 根据用户名称获取用户的角色列表(MySQL)
     *
     * @param username
     * @return
     */
    public List<String> getUserRoles(String username) {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<String> roles = null;

        try {
            Query query = em.createNativeQuery("select r.ROLE_NAME from TB_USERS u join RF_USER_ROLE ur on u.ID = ur.USER_ID join TB_ROLES r on ur.ROLE_ID = r.ID where u.USER_NAME = ?1 and u.ENABLED=1 and r.ENABLED=1");
            query.setParameter(1, username);
            roles = (List<String>) query.getResultList();
        } catch (NoResultException ex) {
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }

        return roles;
    }

    /**
     * 获取系统中所有权限与角色的对应关系(MySQL)
     *
     * @return
     */
    public List<MyPermission> listGetPermissionsFormMysql() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<MyPermission> permissions = null;

        try {
            Query query = em.createNativeQuery("select r.ROLE_NAME role,p.PERMISSION_URL permissionUrl from TB_ROLES r join RF_ROLE_PERMISSION rp on r.ID = rp.ROLE_ID join TB_PERMISSIONS p on rp.PERMISSION_ID = p.ID where r.ENABLED = 1 and p.ENABLED = 1");
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(MyPermission.class));
            permissions = (List<MyPermission>) query.getResultList();
        } catch (NoResultException ex) {
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }

        return permissions;
    }


    /**
     * 先从redis里面获取，如果获取不到，则从mysql中获取，并把数据写入到redis中。
     *
     * @return
     */
    public HashMap<String, Collection<ConfigAttribute>> listGetPermissions() {
        HashOperations<String, String, String> ops = template.opsForHash();
        Map<String, String> pmap = ops.entries(PERMISSION_REDIS_KEY);

        HashMap<String, Collection<ConfigAttribute>> map = new HashMap<>();
        if (pmap != null && pmap.size() != 0) {
            pmap.forEach((key, value) -> {
                map.put(key,
                        Arrays.stream(value.split(","))
                                .map(v -> new SecurityConfig(v))
                                .collect(Collectors.toList()));
            });
        } else {
            /** 从数据库中读取权限信息 **/
            List<MyPermission> permissionList = listGetPermissionsFormMysql();

            /** 把权限信息写入redis **/
            long start = System.nanoTime();

            StringBuilder temp = new StringBuilder();
            HashMap<String, String> tempMap = new HashMap<>();
            permissionList.stream()
                    .collect(Collectors.groupingBy(p -> p.getPermissionUrl()))
                    .forEach((key, value) -> {
                        value.stream().map(v -> v.getRole()).distinct()
                                .forEach(role -> {
                                    if (temp.length() != 0) {
                                        temp.append(",");
                                    }
                                    temp.append(role);
                                });
                        tempMap.put(key, temp.toString());
                        temp.delete(0, temp.length());
                    });
            System.out.println(System.nanoTime() - start);
            ops.putAll(PERMISSION_REDIS_KEY, tempMap);
            System.out.println(System.nanoTime() - start);

            /** 把获取权限信息打包成security需要的格式 **/
            if (permissionList != null) {
                permissionList.stream()
                        .collect(Collectors.groupingBy(p -> p.getPermissionUrl()))
                        .forEach((key, value) -> {
                            map.put(key,
                                    value.stream()
                                            .map(v -> v.getRole()).distinct()
                                            .map(r -> new SecurityConfig(r))
                                            .collect(Collectors.toList())
                            );
                        });
            }
        }

        return map;
    }
}

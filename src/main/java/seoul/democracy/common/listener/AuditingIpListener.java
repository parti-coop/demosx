package seoul.democracy.common.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import seoul.democracy.common.annotation.CreatedIp;
import seoul.democracy.common.annotation.ModifiedIp;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuditingIpListener {

    final private static Map<Class<?>, IpField> ipFieldMap = new ConcurrentHashMap<>();

    private IpField getIpField(Object target) {
        IpField ipField = ipFieldMap.get(target.getClass());
        if (ipField == null) {
            Field createdIpField = null;
            Field modifiedIpField = null;
            for (Class clazz = target.getClass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getAnnotation(CreatedIp.class) != null) {
                        field.setAccessible(true);
                        createdIpField = field;
                    }
                    if (field.getAnnotation(ModifiedIp.class) != null) {
                        field.setAccessible(true);
                        modifiedIpField = field;
                    }
                }
            }
            IpField newIpField = IpField.of(createdIpField, modifiedIpField);
            ipFieldMap.put(target.getClass(), newIpField);
            return newIpField;
        }
        return ipField;
    }

    private String getIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request == null) return "empty";

        String remoteAddr = request.getHeader("X-FORWARDED_FOR");
        if (StringUtils.isEmpty(remoteAddr)) {
            return request.getRemoteAddr();
        }
        return "empty";
    }

    @PrePersist
    public void touchForCreate(Object target) {
        IpField ipField = getIpField(target);
        if (ipField.getCreatedIp() != null || ipField.getModifiedIp() != null) {
            String ip = getIp();
            ipField.updateCreatedIp(ip, target);
            ipField.updateModifiedIp(ip, target);
        }
    }

    @PreUpdate
    public void touchForUpdate(Object target) {
        IpField ipField = getIpField(target);
        if (ipField.getModifiedIp() != null) {
            String ip = getIp();
            ipField.updateModifiedIp(ip, target);
        }
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    static class IpField {
        private Field createdIp;
        private Field modifiedIp;

        void updateCreatedIp(String ip, Object target) {
            if (createdIp == null) return;
            try {
                createdIp.set(target, ip);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        void updateModifiedIp(String ip, Object target) {
            if (modifiedIp == null) return;
            try {
                modifiedIp.set(target, ip);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.youpeng.jpowl.autoconfigure.properties;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class JpOwlPropertiesValidator implements Validator {
    
    @Override
    public boolean supports(Class<?> clazz) {
        return JpOwlProperties.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        JpOwlProperties properties = (JpOwlProperties) target;
        
        if (properties.isEnabled()) {
            // 验证文件路径
            if (properties.getOutputType() == OutputType.FILE) {
                if (StringUtils.isEmpty(properties.getFilePath())) {
                    errors.rejectValue("filePath", "filePath.empty", 
                        "File path must be set when output type is FILE");
                }
            }
            
            // 验证告警配置
            if (properties.getAlert().isEnabled()) {
                validateAlertChannels(properties.getAlert(), errors);
            }
        }
    }
    
    private void validateAlertChannels(JpOwlProperties.Alert alert, Errors errors) {
        if (alert.getChannels().isEmpty()) {
            errors.rejectValue("alert.channels", "channels.empty", 
                "At least one alert channel must be configured when alert is enabled");
        }
    }
} 
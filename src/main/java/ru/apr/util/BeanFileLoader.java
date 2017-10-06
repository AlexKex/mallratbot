package ru.apr.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;
import ru.apr.MallratbotApplication;

import java.io.File;

@Component
public class BeanFileLoader {

    @Autowired
    ApplicationContext context;

    @Value(value="${beanconfig.locale}")
    private String locale;

    @Value("${beanconfig.path}")
    private String beanPath;

    private String beanName;

    public ApplicationContext getModuleContext(String moduleName){
        beanName = moduleName;
        File contextFile = new File(beanPath + beanName + "_" + locale + ".xml");
        ApplicationContext moduleContext = null;

        if(contextFile.canRead()){
            moduleContext = new FileSystemXmlApplicationContext(contextFile.getAbsolutePath());
        }
        else{
            MallratbotApplication.logger.error("Can't find bean config xml at " + beanPath + beanName);
        }

        return moduleContext;
    }
}

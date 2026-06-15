package com.stano.domain_jpa;

import com.stano.domain_jpa.jpa.converters.JpaConvertersPackage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.persistence.autoconfigure.EntityScanPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

class EnableJpaRegistrar implements ImportBeanDefinitionRegistrar {
  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
    List<String> packages = new ArrayList<>();
    packages.add(JpaConvertersPackage.class.getPackageName());

    AnnotationAttributes attrs =
        AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableJpa.class.getName()));
    if (attrs != null) {
      Class<?>[] entityPackages = attrs.getClassArray("entityPackages");
      if (entityPackages.length > 0) {
        Arrays.stream(entityPackages)
            .map(clazz -> clazz.getPackage().getName())
            .forEach(packages::add);
      } else {
        String className = metadata.getClassName();
        int dot = className.lastIndexOf('.');
        if (dot > 0) {
          packages.add(className.substring(0, dot));
        }
      }
    }

    EntityScanPackages.register(registry, packages);
  }
}

package tech.jhipster.forge.generator.springboot.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tech.jhipster.forge.TestUtils.*;
import static tech.jhipster.forge.common.domain.Constants.MAIN_RESOURCES;
import static tech.jhipster.forge.common.utils.FileUtils.getPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.jhipster.forge.IntegrationTest;
import tech.jhipster.forge.common.domain.Project;
import tech.jhipster.forge.error.domain.GeneratorException;
import tech.jhipster.forge.generator.init.primary.java.InitJava;
import tech.jhipster.forge.generator.maven.application.MavenApplicationService;

@IntegrationTest
class SpringBootApplicationServiceIT {

  @Autowired
  InitJava initJava;

  @Autowired
  MavenApplicationService mavenApplicationService;

  @Autowired
  SpringBootApplicationService springBootApplicationService;

  @Test
  void shouldAddSpringBoot() {
    Project project = tmpProject();
    project.addConfig("springBootVersion", "2.5.3");
    initJava.init(project);
    mavenApplicationService.initPomXml(project);
    mavenApplicationService.addMavenWrapper(project);

    springBootApplicationService.addSpringBoot(project);

    assertFileContent(project, "pom.xml", "<artifactId>spring-boot-starter-parent</artifactId>");
    assertFileContent(project, "pom.xml", "<version>2.5.3</version>");

    assertFileContent(project, "pom.xml", "<groupId>org.springframework.boot</groupId>");
    assertFileContent(project, "pom.xml", "<artifactId>spring-boot-starter</artifactId>");
    assertFileContent(project, "pom.xml", "<groupId>org.apache.commons</groupId>");
    assertFileContent(project, "pom.xml", "<artifactId>commons-lang3</artifactId>");
    assertFileContent(project, "pom.xml", "<artifactId>spring-boot-starter-test</artifactId>");

    assertFileContent(project, "pom.xml", "<groupId>org.springframework.boot</groupId>");
    assertFileContent(project, "pom.xml", "<artifactId>spring-boot-maven-plugin</artifactId>");

    assertFileExist(project, "src/main/java/com/mycompany/myapp/JhipsterApp.java");
    assertFileExist(project, "src/test/java/com/mycompany/myapp/JhipsterAppIT.java");

    assertFileExist(project, "src/main/resources/config/application.properties");
  }

  @Test
  void shouldAddSpringBootParent() {
    Project project = tmpProject();
    project.addConfig("springBootVersion", "2.5.3");
    initJava.init(project);
    mavenApplicationService.initPomXml(project);

    springBootApplicationService.addSpringBootParent(project);
    assertFileContent(project, "pom.xml", "<artifactId>spring-boot-starter-parent</artifactId>");
    assertFileContent(project, "pom.xml", "<version>2.5.3</version>");

    // add again the parent, with wrong version
    project.addConfig("springBootVersion", "X.X.X");
    springBootApplicationService.addSpringBootParent(project);
    assertFileContent(project, "pom.xml", "<artifactId>spring-boot-starter-parent</artifactId>");
    assertFileNoContent(project, "pom.xml", "<version>X.X.X</version>");
  }

  @Test
  void shouldNotAddSpringBootParentWhenNoPomXml() {
    Project project = tmpProject();

    assertThatThrownBy(() -> springBootApplicationService.addSpringBootParent(project)).isExactlyInstanceOf(GeneratorException.class);
  }

  @Test
  void shouldAddSpringBootDependencies() {
    Project project = tmpProject();
    initJava.init(project);
    mavenApplicationService.initPomXml(project);

    springBootApplicationService.addSpringBootDependencies(project);

    assertFileContent(project, "pom.xml", "<groupId>org.springframework.boot</groupId>");
    assertFileContent(project, "pom.xml", "<artifactId>spring-boot-starter</artifactId>");

    assertFileContent(project, "pom.xml", "<groupId>org.apache.commons</groupId>");
    assertFileContent(project, "pom.xml", "<artifactId>commons-lang3</artifactId>");

    assertFileContent(project, "pom.xml", "<artifactId>spring-boot-starter-test</artifactId>");
  }

  @Test
  void shouldNotAddSpringBootDependenciesWhenNoPomXml() {
    Project project = tmpProject();

    assertThatThrownBy(() -> springBootApplicationService.addSpringBootDependencies(project)).isExactlyInstanceOf(GeneratorException.class);
  }

  @Test
  void shouldAddSpringBootPlugin() {
    Project project = tmpProject();
    initJava.init(project);
    mavenApplicationService.initPomXml(project);

    springBootApplicationService.addSpringBootMavenPlugin(project);

    assertFileContent(project, "pom.xml", "<groupId>org.springframework.boot</groupId>");
    assertFileContent(project, "pom.xml", "<artifactId>spring-boot-maven-plugin</artifactId>");
  }

  @Test
  void shouldNotAddSpringBootPluginWhenNoPomXml() {
    Project project = tmpProject();

    assertThatThrownBy(() -> springBootApplicationService.addSpringBootMavenPlugin(project)).isExactlyInstanceOf(GeneratorException.class);
  }

  @Test
  void shouldAddMainApp() {
    Project project = tmpProject();
    initJava.init(project);
    mavenApplicationService.initPomXml(project);

    springBootApplicationService.addMainApp(project);

    assertFileExist(project, "src/main/java/com/mycompany/myapp/JhipsterApp.java");
    assertFileExist(project, "src/test/java/com/mycompany/myapp/JhipsterAppIT.java");
  }

  @Test
  void shouldAddApplicationProperties() {
    Project project = tmpProject();
    initJava.init(project);

    springBootApplicationService.addApplicationProperties(project);

    assertFileExist(project, "src/main/resources/config/application.properties");
  }

  @Test
  void shouldAddPropertiesWithInteger() {
    Project project = tmpProject();
    springBootApplicationService.addApplicationProperties(project);

    springBootApplicationService.addProperties(project, "server.port", 8080);

    String applicationProperties = getPath(MAIN_RESOURCES, "config/application.properties");
    assertFileContent(project, applicationProperties, "server.port=8080");
    assertFileContent(project, applicationProperties, "# jhipster-needle-application-properties");
  }

  @Test
  void shouldAddPropertiesWithBoolean() {
    Project project = tmpProject();
    springBootApplicationService.addApplicationProperties(project);

    springBootApplicationService.addProperties(project, "spring.jmx.enabled", false);

    String applicationProperties = getPath(MAIN_RESOURCES, "config/application.properties");
    assertFileContent(project, applicationProperties, "spring.jmx.enabled=false");
    assertFileContent(project, applicationProperties, "# jhipster-needle-application-properties");
  }

  @Test
  void shouldAddPropertiesWithString() {
    Project project = tmpProject();
    springBootApplicationService.addApplicationProperties(project);

    springBootApplicationService.addProperties(project, "jhipster.application", "jhforge");

    String applicationProperties = getPath(MAIN_RESOURCES, "config/application.properties");
    assertFileContent(project, applicationProperties, "jhipster.application=jhforge");
    assertFileContent(project, applicationProperties, "# jhipster-needle-application-properties");
  }

  @Test
  void shouldNotAddPropertiesWhenNoApplicationProperties() {
    Project project = tmpProject();

    assertThatThrownBy(() -> springBootApplicationService.addProperties(project, "jhipster.application", "jhforge"))
      .isExactlyInstanceOf(GeneratorException.class);
  }
}

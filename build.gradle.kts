plugins {
    `java-library`
    `maven-publish`
	id("io.papermc.paperweight.userdev") version "1.3.8"
	id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenCentral()
	maven("https://repo.aikar.co/content/groups/aikar/")
	maven("https://jitpack.io")
	maven("https://nexus.civunion.com/repository/maven-public/")
}

dependencies {
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    implementation("com.rabbitmq:amqp-client:5.6.0")
	compileOnly("net.luckperms:api:5.0")
	compileOnly("net.civmc.civmodcore:CivModCore:2.4.0:dev-all")
	compileOnly("net.civmc.namelayer:NameLayer:3.1.0:dev")
	compileOnly("net.civmc.civchat2:CivChat2:2.1.0:dev")
	compileOnly("net.civmc.jukealert:JukeAlert:3.1.0:dev")
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
	withJavadocJar()
	withSourcesJar()
}

tasks {
	build {
		dependsOn(reobfJar)
	}
	compileJava {
		options.encoding = Charsets.UTF_8.name()
		options.release.set(17)
	}
	processResources {
		filteringCharset = Charsets.UTF_8.name()
		filesMatching("**/plugin.yml") {
			expand( project.properties )
		}
	}
	javadoc {
		options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
	}
	test {
		useJUnitPlatform()
	}
}

publishing {
	repositories {
		maven {
			url = uri("https://nexus.civunion.com/repository/maven-releases/")
			credentials {
				username = System.getenv("REPO_USERNAME")
				password = System.getenv("REPO_PASSWORD")
			}
		}
	}
	publications {
		register<MavenPublication>("main") {
			from(components["java"])
		}
	}
}

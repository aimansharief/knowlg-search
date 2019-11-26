package org.sunbird.schema.impl;

import com.typesafe.config.ConfigFactory;
import org.leadpony.justify.api.JsonSchema;
import org.sunbird.common.Platform;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


public class JsonSchemaValidator extends BaseSchemaValidator {
    private String basePath = Platform.config.hasPath("schema.base_path") ? Platform.config.getString("schema.base_path") : "https://sunbirddev.blob.core.windows.net/sunbird-content-dev/schemas/";

    public JsonSchemaValidator(String name, String version) throws Exception {
        super(name, version);
        basePath = basePath + name.toLowerCase() + "/" + version + "/";
        loadSchema();
        loadConfig();
    }

    private void loadSchema() throws Exception {
        System.out.println("Schema path: " + basePath + "schema.json");
        if(basePath.startsWith("http")){
            InputStream stream = new URL( basePath + "schema.json").openStream();
            this.schema = readSchema(stream);
        }else {
            Path schemaPath = Paths.get(getClass().getClassLoader().getResource( basePath + "schema.json").toURI());
            this.schema = readSchema(schemaPath);
        }
    }

    private void loadConfig() throws Exception {
        System.out.println("Config path: " + basePath + "config.json");
        if(basePath.startsWith("http")){
            this.config = ConfigFactory.parseURL(new URL( basePath + "config.json"));
        } else {
            Path configPath = Paths.get(getClass().getClassLoader().getResource( basePath + "config.json").toURI());
            this.config = ConfigFactory.parseFile(configPath.toFile());
        }

    }


    /**
     * Resolves the referenced JSON schemas.
     *
     * @param id the identifier of the referenced JSON schemas.
     * @return referenced JSON schemas.
     */
    public JsonSchema resolveSchema(URI id) {
        // The schema is available in the local filesystem.
//        try {
//            Path path = Paths.get( getClass().getClassLoader().getResource(basePath + id.getPath()).toURI());
//            return readSchema(path);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }
}

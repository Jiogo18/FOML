package dev.ender.foml.obj;

import com.google.gson.*;
import dev.ender.foml.FOML;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec3f;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Function;

/***
 *  ItemOBJLoader
 *  Child class of the OBJ loader that loads basic item OBJ models with JSON model transformations.
 *
 *  Created by jard at 2:27 AM on September 22, 2019.
 ***/
public class ItemOBJLoader implements ModelVariantProvider, Function<ResourceManager, ModelVariantProvider> {
    public static ItemOBJLoader INSTANCE = new ItemOBJLoader();
    public static final Gson GSON = (new GsonBuilder())
            .registerTypeAdapter (ModelTransformation.class, new ModelTransformDeserializer())
            .registerTypeAdapter (Transformation.class, new TransformationDeserializer())
            .create ();
    private static final OBJLoader OBJ_LOADER = OBJLoader.INSTANCE;

    @Override
    public UnbakedModel loadModelVariant(ModelIdentifier modelId, ModelProviderContext context) {
        if(modelId.getVariant().equals("inventory")) {
            ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();

            Identifier modelPath = new Identifier (modelId.getNamespace (),
                    "models/item/" + modelId.getPath () + ".json");

            Optional<Resource> resource = resourceManager.getResource(modelPath);
            if (resource.isPresent()) {
                try (Reader reader = new InputStreamReader(resource.get().getInputStream())) {
                    JsonObject rawModel = JsonHelper.deserialize(reader);

                    JsonElement parent = rawModel.get("parent");
                    if (parent instanceof JsonPrimitive && ((JsonPrimitive) parent).isString()) {
                        String objPath = parent.getAsString();
                        if (!objPath.endsWith(".obj"))
                            throw new IllegalStateException("Parent of JsonOBJ model must be a .obj file.");

                        Identifier parentPath = new Identifier(objPath);

                        ModelTransformation transformation = null;
                        if (rawModel.has("display")) {
                            JsonObject rawTransform = JsonHelper.getObject(rawModel, "display");
                            transformation = GSON.fromJson(rawTransform, ModelTransformation.class);
                        }

                        return OBJ_LOADER.loadModelResource(parentPath,
                                context, transformation);
                    }
                } catch (Exception e) {
                    // Silently ignore general IllegalStateExceptions, as all vanilla models in a registered namespace would
                    // otherwise spew the console with this error.
                    if (!(e instanceof IllegalStateException)) {
                        FOML.LOGGER.error("Unable to load OBJ Model, Source: " + modelId.toString(), e);
                    }
                }
            }
        }
        return null;
    }

    @Environment(EnvType.CLIENT)
    public static class ModelTransformDeserializer extends ModelTransformationDeserializer {
        protected ModelTransformDeserializer () {
            super ();
        }
    }
    @Environment(EnvType.CLIENT)
    public static class TransformDeserializer extends TransformationDeserializer {
        protected TransformDeserializer () {
            super ();
        }
    }

    @Override
    public ModelVariantProvider apply(ResourceManager manager) {
        return this;
    }

    public static class TransformationDeserializer implements JsonDeserializer<Transformation> {
        private static final Vec3f DEFAULT_ROTATION = new Vec3f(0.0F, 0.0F, 0.0F);
        private static final Vec3f DEFAULT_TRANSLATION = new Vec3f(0.0F, 0.0F, 0.0F);
        private static final Vec3f DEFAULT_SCALE = new Vec3f(1.0F, 1.0F, 1.0F);

        protected TransformationDeserializer() {
        }

        public Transformation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Vec3f Vec3f = this.parseVec3f(jsonObject, "rotation", DEFAULT_ROTATION);
            Vec3f Vec3f2 = this.parseVec3f(jsonObject, "translation", DEFAULT_TRANSLATION);
            Vec3f2.scale(0.0625F);
            Vec3f2.clamp(-5.0F, 5.0F);
            Vec3f Vec3f3 = this.parseVec3f(jsonObject, "scale", DEFAULT_SCALE);
            Vec3f3.clamp(-4.0F, 4.0F);
            return new Transformation(Vec3f, Vec3f2, Vec3f3);
        }

        private Vec3f parseVec3f(JsonObject jsonObject, String string, Vec3f Vec3f) {
            if (!jsonObject.has(string)) {
                return Vec3f;
            } else {
                JsonArray jsonArray = JsonHelper.getArray(jsonObject, string);
                if (jsonArray.size() != 3) {
                    throw new JsonParseException("Expected 3 " + string + " values, found: " + jsonArray.size());
                } else {
                    float[] fs = new float[3];

                    for(int i = 0; i < fs.length; ++i) {
                        fs[i] = JsonHelper.asFloat(jsonArray.get(i), string + "[" + i + "]");
                    }

                    return new Vec3f(fs[0], fs[1], fs[2]);
                }
            }
        }
    }

    public static class ModelTransformationDeserializer implements JsonDeserializer<ModelTransformation> {
        protected ModelTransformationDeserializer() {
        }

        public ModelTransformation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Transformation transformation = this.parseModelTransformation(jsonDeserializationContext, jsonObject, "thirdperson_righthand");
            Transformation transformation2 = this.parseModelTransformation(jsonDeserializationContext, jsonObject, "thirdperson_lefthand");
            if (transformation2 == Transformation.IDENTITY) {
                transformation2 = transformation;
            }

            Transformation transformation3 = this.parseModelTransformation(jsonDeserializationContext, jsonObject, "firstperson_righthand");
            Transformation transformation4 = this.parseModelTransformation(jsonDeserializationContext, jsonObject, "firstperson_lefthand");
            if (transformation4 == Transformation.IDENTITY) {
                transformation4 = transformation3;
            }

            Transformation transformation5 = this.parseModelTransformation(jsonDeserializationContext, jsonObject, "head");
            Transformation transformation6 = this.parseModelTransformation(jsonDeserializationContext, jsonObject, "gui");
            Transformation transformation7 = this.parseModelTransformation(jsonDeserializationContext, jsonObject, "ground");
            Transformation transformation8 = this.parseModelTransformation(jsonDeserializationContext, jsonObject, "fixed");
            return new ModelTransformation(transformation2, transformation, transformation4, transformation3, transformation5, transformation6, transformation7, transformation8);
        }

        private Transformation parseModelTransformation(JsonDeserializationContext jsonDeserializationContext, JsonObject jsonObject, String string) {
            return jsonObject.has(string) ? (Transformation)jsonDeserializationContext.deserialize(jsonObject.get(string), Transformation.class) : Transformation.IDENTITY;
        }
    }

}

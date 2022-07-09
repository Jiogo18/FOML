package dev.ender.foml;

import dev.ender.foml.obj.ItemOBJLoader;
import dev.ender.foml.obj.OBJLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FOML implements ClientModInitializer {
    public static final String MODID = "foml";
    public static Logger LOGGER = LoggerFactory.getLogger("FOML");

    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(OBJLoader.INSTANCE);
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(ItemOBJLoader.INSTANCE);
    }
}

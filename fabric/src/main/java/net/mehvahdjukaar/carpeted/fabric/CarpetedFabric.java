package net.mehvahdjukaar.carpeted.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mehvahdjukaar.carpeted.Carpeted;
import net.mehvahdjukaar.carpeted.CarpetedClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;

public class CarpetedFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Carpeted.commonInit();

        if (PlatformHelper.getEnv().isClient()) {
            FabricSetupCallbacks.CLIENT_SETUP.add(CarpetedClient::init);
        }

        UseBlockCallback.EVENT.register(Carpeted::onRightClickBlock);
    }
}

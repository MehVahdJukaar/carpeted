package net.mehvahdjukaar.carpeted.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mehvahdjukaar.carpeted.Carpeted;
import net.mehvahdjukaar.carpeted.CarpetedClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.fabric.MLFabricSetupCallbacks;

public class CarpetedFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Carpeted.commonInit();

        if (PlatHelper.getPhysicalSide().isClient()) {
            MLFabricSetupCallbacks.CLIENT_SETUP.add(CarpetedClient::init);
        }

        UseBlockCallback.EVENT.register(Carpeted::onRightClickBlock);

        //MLFabricSetupCallbacks.finishModInit(Carpeted.MOD_ID);

    }
}

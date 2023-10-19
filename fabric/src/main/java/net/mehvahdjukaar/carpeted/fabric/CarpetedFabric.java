package net.mehvahdjukaar.carpeted.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mehvahdjukaar.carpeted.Carpeted;
import net.mehvahdjukaar.carpeted.CarpetedClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CarpetBlock;

public class CarpetedFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Carpeted.commonInit();

        if (PlatformHelper.getEnv().isClient()) {
            FabricSetupCallbacks.CLIENT_SETUP.add(CarpetedClient::init);
        }

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack stack = player.getItemInHand(hand);
             if(stack.getItem() instanceof BlockItem bi &&
                    bi.getBlock() instanceof CarpetBlock cb) {
                 return Carpeted.onRightClickBlock(player, world, stack, cb, hitResult);
             }
             return InteractionResult.PASS;
        });

        FabricSetupCallbacks.finishModInit(Carpeted.MOD_ID);

    }
}

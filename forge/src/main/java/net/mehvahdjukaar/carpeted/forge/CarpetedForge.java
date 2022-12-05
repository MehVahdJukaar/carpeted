package net.mehvahdjukaar.carpeted.forge;

import net.mehvahdjukaar.carpeted.Carpeted;
import net.mehvahdjukaar.carpeted.CarpetedClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: MehVahdJukaar
 */
@Mod(Carpeted.MOD_ID)
public class CarpetedForge {

    public CarpetedForge() {
        Carpeted.commonInit();

        if (PlatformHelper.getEnv().isClient()) {
            CarpetedClient.init();
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onUseBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCanceled()) {
            var ret = Carpeted.onRightClickBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
            if (ret != InteractionResult.PASS) {
                event.setCanceled(true);
                event.setCancellationResult(ret);
            }
        }
    }

}


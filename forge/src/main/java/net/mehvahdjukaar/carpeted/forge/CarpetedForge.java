package net.mehvahdjukaar.carpeted.forge;

import net.mehvahdjukaar.carpeted.Carpeted;
import net.mehvahdjukaar.carpeted.CarpetedClient;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: MehVahdJukaar
 */
@Mod(Carpeted.MOD_ID)
public class CarpetedForge {

    private static final boolean QUARK = ModList.get().isLoaded("quark");

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
            ItemStack stack = event.getEntity().getItemInHand(event.getHand());
            if (stack.getItem() instanceof BlockItem bi) {
                CarpetBlock carpet = null;
                if (bi.getBlock() instanceof CarpetBlock b) {
                    carpet = b;
                } else if (QUARK) {
                    var cc = QuarkCompat.getCarpet(event.getEntity(), bi);
                    if (cc != null) carpet = cc;
                }
                if (carpet != null) {
                    var ret = Carpeted.onRightClickBlock(event.getEntity(), event.getLevel(), stack, carpet, event.getHitVec());
                    if (ret != InteractionResult.PASS) {
                        event.setCanceled(true);
                        event.setCancellationResult(ret);

                    }
                }
            }
        }
    }

}


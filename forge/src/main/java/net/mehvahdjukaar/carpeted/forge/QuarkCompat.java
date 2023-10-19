package net.mehvahdjukaar.carpeted.forge;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.CarpetBlock;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.content.experimental.module.VariantSelectorModule;

public class QuarkCompat {

    public static CarpetBlock getCarpet(Player player, BlockItem bi) {
        if (ModuleLoader.INSTANCE.isModuleEnabled(VariantSelectorModule.class)) {
            String variant = VariantSelectorModule.getSavedVariant(player);
            if (variant != null) {
                var v = VariantSelectorModule.getVariantForBlock(bi.getBlock(), variant);

                if (v instanceof CarpetBlock b) {
                    return b;
                }
            }
        }
        return null;
    }
}

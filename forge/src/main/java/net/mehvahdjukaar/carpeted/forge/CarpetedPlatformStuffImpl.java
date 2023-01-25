package net.mehvahdjukaar.carpeted.forge;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

import java.util.Arrays;
import java.util.List;

public class CarpetedPlatformStuffImpl {
    public static void removeAmbientOcclusion(List<BakedQuad> supportQuads) {
        supportQuads.replaceAll(quad->{
            int[] vertices = quad.getVertices();
            return new BakedQuad(
                    Arrays.copyOf(vertices, vertices.length), quad.getTintIndex(), quad.getDirection(), quad.getSprite(),
                    true, false
            );
        });
    }


}

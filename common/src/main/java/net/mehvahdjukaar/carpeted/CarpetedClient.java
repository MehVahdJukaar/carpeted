package net.mehvahdjukaar.carpeted;

import net.mehvahdjukaar.carpeted.client.CarpetStairsModel;
import net.mehvahdjukaar.moonlight.api.block.IBlockHolder;
import net.mehvahdjukaar.moonlight.api.client.model.NestedModelLoader;
import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CarpetedClient {

    public static final ResourceLocation LABEL_MODEL = Carpeted.res("block/label");

    public static void init() {
        ClientPlatformHelper.addSpecialModelRegistration(CarpetedClient::registerSpecialModels);
        ClientPlatformHelper.addModelLoaderRegistration(CarpetedClient::registerModelLoaders);
        ClientPlatformHelper.addBlockColorsRegistration(CarpetedClient::registerBlockColors);

    }

    public static void  setup(){
        ClientPlatformHelper.registerRenderType(Carpeted.CARPET_STAIRS.get(), RenderType.translucent());
    }

    @EventCalled
    private static void registerSpecialModels(ClientPlatformHelper.SpecialModelEvent event) {
        event.register(LABEL_MODEL);
    }

    @EventCalled
    private static void registerModelLoaders(ClientPlatformHelper.ModelLoaderEvent event) {
        event.register(Carpeted.res("carpet_overlay"), new NestedModelLoader("carpet",
                CarpetStairsModel::new));

    }

    @EventCalled
    private static void registerBlockColors(ClientPlatformHelper.BlockColorEvent event) {
        event.register(new MimicBlockColor(), Carpeted.CARPET_STAIRS.get());
    }

    public static class MimicBlockColor implements BlockColor {

        @Override
        public int getColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tint) {
            return col(state, world, pos, tint);
        }

        public static int col(BlockState state, BlockAndTintGetter level, BlockPos pos, int tint) {
            if (level != null && pos != null) {
                if (level.getBlockEntity(pos) instanceof IBlockHolder tile) {
                    BlockState mimic = tile.getHeldBlock();
                    if (mimic != null && !mimic.hasBlockEntity()) {
                        return Minecraft.getInstance().getBlockColors().getColor(mimic, level, pos, tint);
                    }
                }
            }
            return -1;
        }
    }

}

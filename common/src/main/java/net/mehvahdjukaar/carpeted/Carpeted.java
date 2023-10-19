package net.mehvahdjukaar.carpeted;

import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Author: MehVahdJukaar
 */
public class Carpeted {

    public static final String MOD_ID = "carpeted";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void commonInit() {

    }

    @EventCalled
    public static InteractionResult onRightClickBlock(Player player, Level level, ItemStack stack, CarpetBlock block,
                                                      BlockHitResult hitResult) {
        if (player.isSecondaryUseActive()) return InteractionResult.PASS;
        if (player.getAbilities().mayBuild) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            BlockState replacingBlock = getReplacingBlock(state);
            if (replacingBlock != null) {
                level.setBlockAndUpdate(pos, replacingBlock);
                if (level.getBlockEntity(pos) instanceof CarpetedBlockTile tile) {
                    var carpet = block.defaultBlockState();
                    tile.initialize(state, carpet);
                    if (!player.getAbilities().instabuild) stack.shrink(1);

                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
                    }
                    level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);

                    SoundType sound = carpet.getSoundType();
                    level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS,
                            (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    private static BlockState getReplacingBlock(BlockState state) {
        Block b = state.getBlock();
        if (!(b instanceof EntityBlock)) {
            if (b instanceof StairBlock &&
                    state.getValue(StairBlock.HALF) == Half.BOTTOM) {
                return CARPET_STAIRS.get().withPropertiesOf(state);
            } else if (b instanceof SlabBlock && state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
                return CARPET_SLAB.get().withPropertiesOf(state);
            }
        }
        return null;
    }

    public static final Supplier<Block> CARPET_STAIRS = RegHelper.registerBlock(res("carpet_stairs"),
            () -> new CarpetStairBlock(Blocks.OAK_STAIRS));

    public static final Supplier<Block> CARPET_SLAB = RegHelper.registerBlock(res("carpet_slab"),
            () -> new CarpetSlabBlock(Blocks.OAK_SLAB));

    public static final Supplier<BlockEntityType<CarpetedBlockTile>> CARPET_STAIRS_TILE =
            RegHelper.registerBlockEntityType(res("carpeted_block"),
                    () -> PlatformHelper.newBlockEntityType(CarpetedBlockTile::new, CARPET_STAIRS.get(), CARPET_SLAB.get()));

}

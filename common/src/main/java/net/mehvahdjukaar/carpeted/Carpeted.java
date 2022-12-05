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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand,
                                                      BlockHitResult hitResult) {
        if (player.isSecondaryUseActive()) return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);
        if (player.getAbilities().mayBuild && stack.getItem() instanceof BlockItem bi &&
                bi.getBlock() instanceof CarpetBlock) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof StairBlock &&
                    state.getValue(StairBlock.HALF) == Half.BOTTOM &&
                    !(state.getBlock() instanceof EntityBlock)) {
                level.setBlockAndUpdate(pos, CARPET_STAIRS.get().withPropertiesOf(state));
                if (level.getBlockEntity(pos) instanceof CarpetStairsBlockTile tile) {
                    var carpet = bi.getBlock().defaultBlockState();
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

    public static final Supplier<Block> CARPET_STAIRS = RegHelper.registerBlock(res("carpet_stairs"),
            () -> new CarpetStairBlock(Blocks.OAK_STAIRS));

    public static final Supplier<BlockEntityType<CarpetStairsBlockTile>> CARPET_STAIRS_TILE =
            RegHelper.registerBlockEntityType(res("carpet_stairs"),
                    () -> PlatformHelper.newBlockEntityType(CarpetStairsBlockTile::new, CARPET_STAIRS.get()));

}

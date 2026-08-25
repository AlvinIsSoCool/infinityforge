package net.alvin.infinityforge.infinity.snap;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.entity.effect.SnapStatusEffect;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.entity.effect.ModStatusEffects;
import net.alvin.infinityforge.world.data.SnappedEntitiesState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SnapFunctionsHelper {
    public static void killHalf(ServerWorld world, ServerPlayerEntity player) {
        List<LivingEntity> targets = new ArrayList<>();
        for (Entity entity : world.iterateEntities()) {
            if (entity instanceof LivingEntity living
                    && living.isAlive() && living != player)
                targets.add(living);
        }

        List<LivingEntity> halfTargets = getRandomTargetsHalf(targets);
        InfinityForge.LOGGER.info("Found {} entities for KILL_HALF Snap Function.", halfTargets.size());
        applyEffectByDistance(halfTargets, player);
        sendSnapMessageStyled(player, "snapmessages.infinityforge.kill_half",
                Style.EMPTY.withColor(0xFA8128));
        applyPostSnapEffects(player);
    }

    public static void killAll(ServerWorld world, ServerPlayerEntity player) {
        List<LivingEntity> targets = new ArrayList<>();

        for (Entity entity : world.iterateEntities()) {
            if (entity instanceof LivingEntity living
                    && living.isAlive() && living != player)
                targets.add(living);
        }

        InfinityForge.LOGGER.info("Found {} entities for KILL_ALL Snap Function.", targets.size());
        applyEffectByDistance(targets, player);
        sendSnapMessage(player, "snapmessages.infinityforge.kill_all", Formatting.DARK_PURPLE);
        applyPostSnapEffects(player);
    }

    public static void killHostiles(ServerWorld world, ServerPlayerEntity player) {
        List<MobEntity> targets = new ArrayList<>();

        for (Entity entity : world.iterateEntities()) {
            if (entity instanceof MobEntity mob && mob.isAlive())
                targets.add(mob);
        }

        InfinityForge.LOGGER.info("Found {} entities for KILL_HOSTILES Snap Function.", targets.size());
        applyEffectByDistance(targets, player);
        sendSnapMessage(player, "snapmessages.infinityforge.kill_hostiles", Formatting.DARK_RED);
        applyPostSnapEffects(player);
    }

    public static void revertKills(ServerWorld world, ServerPlayerEntity player) {
        SnappedEntitiesState state = SnappedEntitiesState.get(world);
        List<SnappedEntitiesState.SnappedEntry> entries = state.popAll();
        MinecraftServer server = world.getServer();

        for (SnappedEntitiesState.SnappedEntry entry : entries) {
            RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(entry.dimensionId()));
            ServerWorld targetWorld = server.getWorld(worldKey);
            if (targetWorld == null) continue;

            EntityType<?> type = Registries.ENTITY_TYPE.get(entry.entityType());
            Entity entity = type.create(targetWorld);
            if (entity == null) continue;

            entity.refreshPositionAndAngles(entry.x(), entry.y(), entry.z(), entity.getYaw(), entity.getPitch());
            targetWorld.spawnEntity(entity);
        }

        sendSnapMessage(player, "snapmessages.infinityforge.revert_kills", Formatting.DARK_BLUE);
        applyPostSnapEffects(player);
    }

    public static void destroyStones(ServerPlayerEntity player) {
        ItemStack stack = InfinityGauntletItem.findGauntlet(player);
        if (stack == null) return;

        sendSnapMessage(player, "snapmessages.infinityforge.destroy_stones", Formatting.LIGHT_PURPLE);
        InfinityGauntletItem.removeStones(stack);
        applyPostSnapEffects(player);
    }

    private static void sendSnapMessage(PlayerEntity player, String key, Formatting formatting) {
        String name = player.getName().getString();
        String message = Text.translatable(key).getString();
        String formattedMessage = String.format(message, name);
        player.sendMessage(Text.literal(formattedMessage).formatted(formatting), false);
    }

    private static void sendSnapMessageStyled(PlayerEntity player, String key, Style style) {
        String name = player.getName().getString();
        String message = Text.translatable(key).getString();
        String formattedMessage = String.format(message, name);
        player.sendMessage(Text.literal(formattedMessage).setStyle(style), false);
    }

    private static void applyPostSnapEffects(PlayerEntity player) {
        player.getHungerManager().setSaturationLevel(1f);
        player.getHungerManager().setFoodLevel(1);
        player.damage(player.getDamageSources().magic(), 1.0f);
        player.setHealth(1.0f);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 255));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 255));
    }

    private static <T extends LivingEntity> List<T> getRandomTargetsHalf(List<T> targets) {
        int n = targets.size();
        int k = n / 2;
        Random rng = new Random();
        List<T> result = new ArrayList<>(k);

        for (int i = 0; i < k; i++)
            result.add(targets.get(i));

        for (int i = k; i < n; i++) {
            int j = rng.nextInt(i + 1);
            if (j < k) result.set(j, targets.get(i));
        }

        return result;
    }

    private static <T extends LivingEntity> void applyEffectByDistance(
            @NotNull List<T> targets,
            PlayerEntity player
    ) {
        if (targets.isEmpty()) return;
        targets.sort(Comparator.comparingDouble(e -> e.squaredDistanceTo(player)));

        int n = targets.size();
        for (int i = 0; i < n; i++) {
            T entity = targets.get(i);
            int duration;
            if (n == 1) duration = SnapStatusEffect.EFFECT_MIN_DURATION;
            else {
                double fraction = ((double) i) / (n - 1);
                duration = (int)(SnapStatusEffect.EFFECT_MIN_DURATION + fraction *
                        (SnapStatusEffect.EFFECT_MAX_DURATION - SnapStatusEffect.EFFECT_MIN_DURATION));
            }
            entity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SNAP_EFFECT, duration, 0));
        }
    }
}

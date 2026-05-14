package net.alvin.infinityforge.registry;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

public class ModCustomTrades {
    public static void register() {
        TradeOfferHelper.registerVillagerOffers(
                VillagerProfession.LIBRARIAN, 5,
                factories -> factories.add(
                        (entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 64), new ItemStack(Items.PAPER), new ItemStack(ModItems.INFINITY_GAUNTLET_BLUEPRINT),
                                1, 10, 0f
                        )
                )
        );
    }
}

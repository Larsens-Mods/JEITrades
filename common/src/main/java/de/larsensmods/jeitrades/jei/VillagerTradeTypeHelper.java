package de.larsensmods.jeitrades.jei;

import de.larsensmods.jeitrades.JEITradesMod;
import de.larsensmods.jeitrades.data.VillagerTradeData;
import de.larsensmods.jeitrades.data.transformed.TransformedVillagerTrade;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VillagerTradeTypeHelper implements IRecipeCategoryExtension<VillagerTradeTypeHelper> {

    public static final IRecipeType<VillagerTradeTypeHelper> RECIPE_TYPE = IRecipeType.create(Identifier.fromNamespaceAndPath(JEITradesMod.MOD_ID, "villager_trades"), VillagerTradeTypeHelper.class);

    public static List<VillagerTradeTypeHelper> buildRecipes(VillagerTradeData data) {
        JEITradesMod.LOG.info("Building VillagerTrades recipes");

        if(data == null) {
            JEITradesMod.LOG.warn("VillagerTrades data is null");
            return Collections.emptyList();
        }

        List<VillagerTradeTypeHelper> recipes = new ArrayList<>();

        //Wandering Trader
        ItemStackTemplate traderPoiBlock = ItemStackTemplate.fromNonEmptyStack(Items.LEAD.getDefaultInstance());
        Component traderProfession = Component.translatable("entity.minecraft.wandering_trader");
        //Buys
        for(TransformedVillagerTrade trade : data.wanderingBuy().trades()){
            recipes.add(new VillagerTradeTypeHelper(traderPoiBlock, trade.wants(), trade.additionalWants(), trade.gives(), Component.literal("B"), traderProfession, true));
        }
        //Sells Common
        for(TransformedVillagerTrade trade : data.wanderingSellCommon().trades()){
            recipes.add(new VillagerTradeTypeHelper(traderPoiBlock, trade.wants(), trade.additionalWants(), trade.gives(), Component.literal("C"), traderProfession, true));
        }
        //Sells Uncommon
        for(TransformedVillagerTrade trade : data.wanderingSellUncommon().trades()){
            recipes.add(new VillagerTradeTypeHelper(traderPoiBlock, trade.wants(), trade.additionalWants(), trade.gives(), Component.literal("U"), traderProfession, true));
        }

        //Professions
        for(VillagerTradeData.ProfessionData profession : data.villagerProfessions()){
            Component professionName = profession.name();
            ItemStackTemplate professionPoiBlock = profession.poiBlock();
            for(int level : profession.tradeSetsByLevel().keySet()){
                Identifier trade = profession.tradeSetsByLevel().get(level);
                if(data.villagerTradeSets().containsKey(trade)){
                    for(TransformedVillagerTrade tradeData : data.villagerTradeSets().get(trade).trades()){
                        recipes.add(new VillagerTradeTypeHelper(professionPoiBlock, tradeData.wants(), tradeData.additionalWants(), tradeData.gives(), Component.translatable("jeitrades.villager.level_" + level), professionName, false));
                    }
                }
            }
        }

        return recipes;
    }

    //Implementation

    public final ItemStackTemplate poiBlock, wants, gives;
    public final Optional<ItemStackTemplate> additionalWants;
    public final Component levelDisplay, professionName;
    public final boolean isTrader;

    public VillagerTradeTypeHelper(ItemStackTemplate poiBlock, ItemStackTemplate wants, Optional<ItemStackTemplate> additionalWants, ItemStackTemplate gives, Component levelDisplay, Component professionName, boolean isTrader) {
        this.poiBlock = poiBlock;
        this.wants = wants;
        this.additionalWants = additionalWants;
        this.gives = gives;
        this.levelDisplay = levelDisplay;
        this.professionName = professionName;
        this.isTrader = isTrader;
    }

    @Override
    public void drawInfo(VillagerTradeTypeHelper recipe, int recipeWidth, int recipeHeight, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {

        int textX = 25, textY = 12;

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(textX, textY);
        guiGraphics.pose().scale(0.9f, 0.9f);
        guiGraphics.text(Minecraft.getInstance().font, recipe.levelDisplay, 0, 0, ARGB.opaque(8), false);
        guiGraphics.pose().popMatrix();
    }
}

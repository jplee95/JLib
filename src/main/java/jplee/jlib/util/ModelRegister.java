package jplee.jlib.util;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ModelRegister {
	
	public void registerBlockItemModel(Block block) {
		final Item item = Item.getItemFromBlock(block);
		if (item != null) {
			registerItemModel(item);
		}
	}

	public void registerBlockItemModel(Block block, String modelLocation) {
		final Item item = Item.getItemFromBlock(block);
		if (item != null) {
			registerItemModel(item, modelLocation);
		}
	}

	public <T extends IVariant> void registerVariantBlockItemModels(Block block, String variantName, T[] variants) {
		final Item item = Item.getItemFromBlock(block);
		if (item != null) {
			registerVariantItemModels(item, variantName, variants);
		}
	}

	public void registerBlockItemModel(Block block, ModelResourceLocation fullModelLocation) {
		final Item item = Item.getItemFromBlock(block);
		if (item != null) {
			registerItemModel(item, fullModelLocation);
		}
	}

	public void registerBlockItemModelForMeta(Block block, int metadata, String variant) {
		final Item item = Item.getItemFromBlock(block);
		if (item != null) {
			registerItemModelForMeta(item, metadata, variant);
		}
	}
	
	public void registerItemModel(Item item) {
		registerItemModel(item, item.getRegistryName().toString());
	}

	public void registerItemModel(Item item, String modelLocation) {
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
		registerItemModel(item, fullModelLocation);
	}

	public void registerItemModel(Item item, ModelResourceLocation fullModelLocation) {
		ModelBakery.registerItemVariants(item, fullModelLocation);
		registerItemModel(item, IMeshDefinitionFix.create(stack -> fullModelLocation));
	}

	public void registerItemModel(Item item, ItemMeshDefinition meshDefinition) {
		ModelLoader.setCustomMeshDefinition(item, meshDefinition);
	}

	public <T extends IVariant> void registerVariantItemModels(Item item, String variantName, T[] variants) {
		for (T variant : variants) {
			registerItemModelForMeta(item, variant.getMeta(), variantName + "=" + variant.getName());
		}
	}

	public void registerItemModelForMeta(Item item, int metadata, String variant) {
		registerItemModelForMeta(item, metadata, new ModelResourceLocation(item.getRegistryName(), variant));
	}

	public void registerItemModelForMeta(Item item, int metadata, ModelResourceLocation modelResourceLocation) {
		ModelLoader.setCustomModelResourceLocation(item, metadata, modelResourceLocation);
	}
}

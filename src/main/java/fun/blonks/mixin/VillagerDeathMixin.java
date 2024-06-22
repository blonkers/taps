package fun.blonks.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import fun.blonks.Taps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillagerProfession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerDeathMixin {

	@Inject(at = @At("TAIL"), method = "onDeath")
	private void onDeath(DamageSource source, CallbackInfo ci) {
		VillagerEntity killed = (VillagerEntity)(Object)this;

		var world = killed.getEntityWorld();
		MinecraftServer server = world.getServer();

		// Client-side, do nothing
		if (world.isClient) return;

		var data = killed.getVillagerData();

		// Notify on all deaths, for pizzazz
		if (server != null) {
			MutableText deathMessage = source.getDeathMessage(killed).copy();
			String job = data.getProfession() == VillagerProfession.NONE ? "jobless" : data.getProfession().toString();
			MutableText formattedJob = Text.literal(" (" + job + ")").formatted(Formatting.GOLD);
			MutableText postedMessage = deathMessage.append(formattedJob);
			server.getPlayerManager().broadcast(postedMessage, true);
		}

		// Don't cry over spilled milk
		if (data.getLevel() <= 1) return;

		var pos = killed.getPos();
		if (pos != null) {
			world.playSound(
				null,
				pos.x,
				pos.y,
				pos.z,
				Taps.TAPS_EVENT,
				SoundCategory.AMBIENT
			);
		}
	}
}